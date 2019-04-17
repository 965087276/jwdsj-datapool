package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.datastat.repo.StatsTableRepo;
import cn.ict.jwdsj.datapool.datastat.entity.QStatTable;
import cn.ict.jwdsj.datapool.datastat.service.DictClient;
import cn.ict.jwdsj.datapool.datastat.service.StatsTableService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsTableServiceImpl implements StatsTableService {
    @Autowired private StatsTableRepo statsTableRepo;
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private DictClient dictClient;

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
    public List<StatsTable> listAll() {
        return statsTableRepo.findAll();
    }

    @Override
    public Date getTableCreateTime(long tableId) {
        QDictTable dictTable = QDictTable.dictTable;
        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;

        DictTable table = dictClient.findDictTableById(tableId);

        DictDatabase dictDb = dictClient.findDictDatabaseBy(table.getDictDatabase().getId());

        String enTable = table.getEnTable();
        String enDatabase = dictDb.getEnDatabase();

        String sql = String.format(
                "select CREATE_TIME from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = '%s' and TABLE_NAME = '%s'",
                enDatabase, enTable);
        return jdbcTemplate.queryForObject(sql, Date.class);
    }

    @Override
    public void saveTablesNotAdd() {
        QDictTable dictTable = QDictTable.dictTable;
        QStatTable statTable = QStatTable.statTable;

        List<StatsTable> tablesNotAdd =  jpaQueryFactory
                .selectDistinct(dictTable.dictDatabase.id, dictTable.id)
                .from(dictTable)
                .leftJoin(statTable)
                .on(dictTable.eq(statTable.dictTable))
                .where(statTable.isNull())
                .fetch()
                .stream()
                .map(tuple -> new StatsTable()
                        .dictDatabase(tuple.get(dictTable.dictDatabase.id))
                        .dictTable(tuple.get(dictTable.id))
                        .updateDate(getTableCreateTime(tuple.get(dictTable.id)))
                )
                .collect(Collectors.toList());

        statsTableRepo.saveAll(tablesNotAdd);

    }

    @Override
    public void deleteTablesNotExist() {
        QDictTable dictTable = QDictTable.dictTable;
        QStatTable statTable = QStatTable.statTable;

        List<StatsTable> statsTables = jpaQueryFactory
                .selectDistinct(statTable.id)
                .from(statTable)
                .leftJoin(dictTable)
                .on(statTable.dictTable.eq(dictTable))
                .where(dictTable.isNull())
                .fetch()
                .stream()
                .map(id -> new StatsTable().id(id))
                .collect(Collectors.toList());
        statsTableRepo.deleteAll(statsTables);
    }

}
