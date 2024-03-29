package cn.ict.jwdsj.datapool.datastats.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.entity.datastats.StatsColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.datastats.dao.mapper.primary.StatsColumnMapper;
import cn.ict.jwdsj.datapool.datastats.dao.repo.StatsColumnRepo;
import cn.ict.jwdsj.datapool.datastats.service.StatsColumnService;
import cn.ict.jwdsj.datapool.datastats.service.StatsService;
import cn.ict.jwdsj.datapool.dictionary.service.column.DictColumnService;
import cn.ict.jwdsj.datapool.dictionary.service.table.DictTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatsColumnServiceImpl implements StatsColumnService {
    @Autowired
    private StatsColumnRepo statsColumnRepo;

    @Autowired
    private DictTableService dictTableService;

    @Autowired
    private DictColumnService dictColumnService;

    @Autowired
    private StatsColumnMapper statsColumnMapper;

    @Autowired
    private StatsService statsService;

    /**
     * 计算并返回缺陷字段
     * @param tableId
     * @return
     */
    @Override
    public List<String> initAndListDefectedColumns(long tableId) {
        DictTable dictTable = dictTableService.findById(tableId);

        List<Map<String, Object>> list = statsService.getTableData(dictTable.getEnDatabase(), dictTable.getEnTable());
        if (list.size() == 0) return new ArrayList<>();

        return list.get(0).keySet().stream()
                .filter(column -> this.isDefectedColumn(column, list)) // 过滤出缺陷字段
                .collect(Collectors.toList());

    }


    @Override
    public void updateDefectedColumns() {
        // dict_table表中的所有表,也是stats_column中的所有表
        List<Long> tableIds = dictTableService.listTableId();

        for (Long tableId : tableIds) {
            // 缺陷字段
            List<String> defectColumns = this.initAndListDefectedColumns(tableId);
//            log.info("tableId is {}, defected columns are {}", tableId, defectColumns);
            // 字段名与字段id的对应关系
            Map<String, Long> dictColNameAndId = dictColumnService.listByTableId(tableId)
                    .stream()
                    .collect(Collectors.toMap(DictColumn::getEnColumn, DictColumn::getId));
            // 缺陷字段的columnId
            List<Long> defectColumnIds = defectColumns
                    .stream()
                    .filter(dictColNameAndId::containsKey) // 该字段要在字典表（同样也在字段统计表）中存在,因为录入字段时可能并没有录入了所有的字段
                    .map(dictColNameAndId::get)
                    .collect(Collectors.toList());

            List<StatsColumn> statsColumns = statsColumnRepo.findByTableId(tableId);

            // is_defect发生改变的那些字段
            List<StatsColumn> statsColumnsChanged = new ArrayList<>();

            for (StatsColumn statsColumn : statsColumns) {
                long columnId = statsColumn.getColumnId();
                boolean isDefected = statsColumn.isDefected();
                boolean existsInDefected = defectColumnIds.contains(columnId);
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
    public int countColumnsByTableId(long tableId) {
        return statsColumnMapper.countColumnsByTableId(tableId);
    }

    @Override
    public int countDefectedColumnsByTableId(long tableId) {
        return statsColumnMapper.countDefectedColumnsByTableId(tableId);
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
        return statsColumnRepo.findByTableId(tableId);
    }

    /**
     * 更新字段名信息
     *
     * @param columnId
     * @param enColumn
     * @param chColumn
     */
    @Override
    public void updateColumnInfo(long columnId, String enColumn, String chColumn) {
        statsColumnRepo.updateColumnInfo(columnId, enColumn, chColumn);
    }

    /**
     * 批量存入dict_column表新增的数据
     *
     * @param currentTime 插入时的时间
     */
    @Override
    public void saveAllFromDictColumn(String currentTime) {
        statsColumnMapper.insertAll(currentTime);
    }

    /**
     * 根据column_id删除
     *
     * @param columnId
     */
    @Override
    public void deleteByColumnId(long columnId) {
        statsColumnRepo.deleteByColumnId(columnId);
    }

    /**
     * 根据table_id删除
     *
     * @param tableId
     */
    @Override
    public void deleteByTableId(long tableId) {
        statsColumnRepo.deleteByTableId(tableId);
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

}
