package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.api.feign.DictClient;
import cn.ict.jwdsj.datapool.common.entity.datastats.QStatsTable;
import cn.ict.jwdsj.datapool.common.entity.datastats.StatsTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.common.utils.StrJudgeUtil;
import cn.ict.jwdsj.datapool.datastat.mapper.StatsTableMapper;
import cn.ict.jwdsj.datapool.datastat.repo.StatsTableRepo;
import cn.ict.jwdsj.datapool.datastat.service.StatsTableService;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsTableServiceImpl implements StatsTableService {
    @Autowired private StatsTableRepo statsTableRepo;
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private DictClient dictClient;
    @Autowired private StatsTableMapper statsTableMapper;

    @Override
    public void save(StatsTable statsTable) {
        statsTableRepo.save(statsTable);
    }

    @Override
    public void saveAll(List<StatsTable> statsTables) {
        statsTableRepo.saveAll(statsTables);
    }

    @Override
    public void update(StatsTable statsTable) {
        statsTableRepo.save(statsTable);
    }

    @Override
    public StatsTable findByDictTableId(long dictTableId) {
        return statsTableRepo.findByDictTableId(dictTableId);
    }

    @Override
    public List<StatsTable> listAll() {
        return statsTableRepo.findAll();
    }

    @Override
    public LocalDate getTableCreateTime(long tableId) {

        DictTable table = dictClient.findDictTableById(tableId);

        DictDatabase dictDb = dictClient.findDictDatabaseById(table.getDictDatabase().getId());

        String enTable = table.getEnTable();
        String enDatabase = dictDb.getEnDatabase();

        String sql = String.format(
                "select CREATE_TIME from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = '%s' and TABLE_NAME = '%s'",
                enDatabase, enTable);
        return jdbcTemplate.queryForObject(sql, LocalDate.class);
    }

    @Override
    public void saveTablesNotAdd() {

        List<StatsTable> tablesNotAdd = statsTableMapper.listTablesNotAdd();



        tablesNotAdd.forEach(table -> table.setUpdateDate(getTableCreateTime(table.getDictTableId())));

        statsTableRepo.saveAll(tablesNotAdd);

    }

    @Override
    public void deleteTablesNotExist() {
        QDictTable dictTable = QDictTable.dictTable;
        QStatsTable statsTable = QStatsTable.statsTable;

        List<StatsTable> statsTables = jpaQueryFactory
                .select(statsTable.id)
                .from(statsTable)
                .leftJoin(dictTable)
                .on(statsTable.dictTableId.eq(dictTable.id))
                .where(dictTable.isNull())
                .fetch()
                .stream()
                .map(id -> new StatsTable().id(id))
                .collect(Collectors.toList());
        statsTableRepo.deleteAll(statsTables);
    }

    /**
     * 表信息列表
     *
     * @param curPage    第几页
     * @param pageSize   每页多少条
     * @param databaseId 库的dict_id
     * @param nameLike   表名搜索
     * @return
     */
    @Override
    public Page<StatsTable> listAll(int curPage, int pageSize, long databaseId, String nameLike) {
        Pageable pageable = PageRequest.of(curPage-1, pageSize);
        QStatsTable statsTable = QStatsTable.statsTable;

        Predicate predicate = statsTable.dictDatabaseId.eq(databaseId);
        // 根据输入的待查询表名是中文还是英文来判断搜索哪个字段
        predicate = StrUtil.isBlank(nameLike) ? predicate : StrJudgeUtil.isContainChinese(nameLike) ?
                ExpressionUtils.and(predicate, statsTable.chTable.like('%' + nameLike + '%')) :
                ExpressionUtils.and(predicate, statsTable.enTable.like('%' + nameLike + '%'));

        return statsTableRepo.findAll(predicate, pageable);
    }

}
