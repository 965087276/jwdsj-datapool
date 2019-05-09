package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.api.feign.DictClient;
import cn.ict.jwdsj.datapool.common.entity.datastats.QStatsColumn;
import cn.ict.jwdsj.datapool.common.entity.datastats.StatsColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.QDictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.datastat.mapper.StatsColumnMapper;
import cn.ict.jwdsj.datapool.datastat.repo.StatsColumnRepo;
import cn.ict.jwdsj.datapool.datastat.service.StatsColumnService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatsColumnServiceImpl implements StatsColumnService {
    @Autowired
    private StatsColumnRepo statsColumnRepo;
    @Autowired
    private DictClient dictClient;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private StatsColumnMapper statsColumnMapper;

    /**
     * 计算并返回缺陷字段
     * @param tableId
     * @return
     */
    @Override
    public List<String> initAndListDefectedColumns(long tableId) {
        DictTable dictTable = dictClient.findDictTableById(tableId);
        DictDatabase dictDatabase = dictClient.findDictDatabaseById(dictTable.getDictDatabase().getId());
        String database = "`" + dictDatabase.getEnDatabase() + "`";
        String table = "`" + dictTable.getEnTable() + "`";

        String sql = "select * from " + database + "." + table + " limit 400";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list.size() == 0) return new ArrayList<>();

        return list.get(0).keySet().stream()
                .filter(column -> this.isDefectedColumn(column, list)) // 过滤出缺陷字段
                .collect(Collectors.toList());

    }

    /**
     * 删去字典中不存在的字段
     */
    @Override
    public void deleteColumnsNotExist() {
        // dict_table表中的所有表
        Set<Long> dictTableIds = this.listDictTableIdsInDictTableModule().stream().collect(Collectors.toSet());
        // stat_column表中的所有表
        List<Long> statsColumnTableIds = this.listDictTableIds();

        // 若表A已经从dict_table中删除，则在stats_column表中删除表A的所有字段（某表的所有字段删除）
        for (Long tb : statsColumnTableIds)  {
            if (!dictTableIds.contains(tb)) {
                statsColumnRepo.deleteAllByDictTableId(tb);
            }
        }

        // 删去在dict_column中不存在但是在stats_column中存在的字段（某表的部分字段删除）
        QDictColumn dictColumn = QDictColumn.dictColumn;
        QStatsColumn statsColumn = QStatsColumn.statsColumn;
        List<Long> ids = jpaQueryFactory.select(statsColumn.id)
                .from(statsColumn)
                .leftJoin(dictColumn)
                .on(dictColumn.id.eq(statsColumn.dictColumnId))
                .where(dictColumn.id.isNull())
                .fetch();
        statsColumnRepo.deleteAllByIdIn(ids);
    }


    /**
     * 添加字典中存在但是统计模块不存在的字段
     */
    @Override
    public void saveColumnsNotAdd() {
        // dict_table表中的所有表
        List<Long> dictTableIds = this.listDictTableIdsInDictTableModule();
        // stats_column表中的所有表
        Set<Long> statsColumnTableIds = this.listDictTableIds().stream().collect(Collectors.toSet());

        // 若dict_table中的表A在stats_column表中不存在，则将表A的所有字段加入到stats_column表中（某表的所有字段添加）
        for (Long tb : dictTableIds) {
            if (!statsColumnTableIds.contains(tb)) {
                List<StatsColumn> statsColumns = dictClient.listDictColumnsByTableId(tb)
                        .stream()
                        .map(this::convertToStatsColumn)
                        .collect(Collectors.toList());
                statsColumnRepo.saveAll(statsColumns);
            }
        }

        // 添加在dict_column中存在但是在stats_column中不存在的字段（某表的部分字段添加）
        QDictColumn dictColumn = QDictColumn.dictColumn;
        QStatsColumn statsColumn = QStatsColumn.statsColumn;
        List<StatsColumn> statsColumns = jpaQueryFactory
                .selectFrom(dictColumn)
                .leftJoin(statsColumn)
                .on(dictColumn.id.eq(statsColumn.dictColumnId))
                .where(statsColumn.id.isNull())
                .fetch()
                .stream()
                .map(this::convertToStatsColumn)
                .collect(Collectors.toList());
        statsColumnRepo.saveAll(statsColumns);
    }

    @Override
    public void updateDefectedColumns() {
        // dict_table表中的所有表,也是stats_column中的所有表（因为已经执行了deleteColumnsNotExist和saveColumnsNotAdd方法）
        List<Long> dictTableIds = this.listDictTableIdsInDictTableModule();

        for (Long dictTableId : dictTableIds) {
            // 缺陷字段
            List<String> defectColumns = this.initAndListDefectedColumns(dictTableId);
            log.info("tableId is {}, defected columns are {}", dictTableId, defectColumns);
            // 字段名与字段id的对应关系
            Map<String, Long> dictColNameAndId = dictClient.listDictColumnsByTableId(dictTableId)
                    .stream()
                    .collect(Collectors.toMap(DictColumn::getEnColumn, DictColumn::getId));
            // 缺陷字段的dictColumnId
            List<Long> defectColumnIds = defectColumns
                    .stream()
                    .filter(dictColNameAndId::containsKey) // 该字段要在字典表（同样也在字段统计表）中存在
                    .map(dictColNameAndId::get)
                    .collect(Collectors.toList());

            List<StatsColumn> statsColumns = statsColumnRepo.findByDictTableId(dictTableId);

            // is_defect发生改变的行
            List<StatsColumn> statsColumnsChanged = new ArrayList<>();

            for (StatsColumn statsColumn : statsColumns) {
                long dictColumnId = statsColumn.getDictColumnId();
                boolean isDefected = statsColumn.isDefected();
                boolean existsInDefected = defectColumnIds.contains(dictColumnId);
                // 原来存的“是否缺陷”与新算的“是否缺陷”作异或，若值不为0，则证明发生了改变
                if (isDefected ^ existsInDefected) {
                    statsColumn.setDefected(!isDefected);
                    statsColumnsChanged.add(statsColumn);
                }
            }
            statsColumnRepo.saveAll(statsColumns);
        }

    }

    @Override
    public int countColumnsByTableId(long dictTableId) {
        QStatsColumn statsColumn = QStatsColumn.statsColumn;
        return jpaQueryFactory
                .select(statsColumn.count())
                .from(statsColumn)
                .where(statsColumn.dictTableId.eq(dictTableId))
                .fetchOne()
                .intValue();
    }

    @Override
    public int countDefectedColumnsByTableId(long dictTableId) {
        QStatsColumn statsColumn = QStatsColumn.statsColumn;
        return jpaQueryFactory
                .select(statsColumn.count())
                .from(statsColumn)
                .where(statsColumn.dictTableId.eq(dictTableId).and(statsColumn.defected.eq(Boolean.TRUE)))
                .fetchOne()
                .intValue();
    }

    /**
     * 字段列表展示
     *
     * @param databaseId 库id
     * @param tableId    表id
     * @return
     */
    @Override
    public List<StatsColumn> listAll(long databaseId, long tableId) {
        return statsColumnRepo.findByDictTableId(tableId);
    }

    @Override
    public List<String> listColumnData(String enDatabase, String enTable, String enColumn) {
        return statsColumnMapper.getColumnData(enDatabase, enTable, enColumn);
    }

    /**
     * 判断表的某个字段是否为缺陷字段
     * 缺陷字段定义为90%以上的记录中该字段都为空
     * @param column 字段名
     * @param list 表的前300条数据
     * @return
     */
    private boolean isDefectedColumn(String column, List<Map<String, Object>> list) {
        double total = list.size();
        double defect = 0;
        for (Map<String, Object> record : list) {
            Object value = record.get(column);
            if (Objects.isNull(value) || StrUtil.isBlankIfStr(value) || "null".equals(value.toString().trim().toLowerCase())) {
                ++defect;
            }
        }
        return defect / total >= 0.90;
    }

    /**
     * 列出dict_table表中的所有表的id
     * @return
     */
    private List<Long> listDictTableIdsInDictTableModule() {
        QDictTable dictTable = QDictTable.dictTable;
        return jpaQueryFactory
                .select(dictTable.id)
                .from(dictTable)
                .fetch();
    }

    /**
     * 列出stats_column表中的所有表的id
     * @return
     */
    private List<Long> listDictTableIds() {
        QStatsColumn statsColumn = QStatsColumn.statsColumn;
        return jpaQueryFactory
                .selectDistinct(statsColumn.dictTableId)
                .from(statsColumn)
                .groupBy(statsColumn.dictTableId)
                .fetch();
    }

    private StatsColumn convertToStatsColumn(DictColumn dictColumn) {
        StatsColumn statsColumn = new StatsColumn();
        statsColumn.setDictColumnId(dictColumn.getId());
        statsColumn.setEnColumn(dictColumn.getEnColumn());
        statsColumn.setChColumn(dictColumn.getChColumn());
        statsColumn.setDictDatabaseId(dictColumn.getDictDatabaseId());
        statsColumn.setDictTableId(dictColumn.getDictTableId());
        return statsColumn;
    }

}
