package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.datastat.repo.StatTableRepo;
import cn.ict.jwdsj.datapool.datastat.entity.QStatTable;
import cn.ict.jwdsj.datapool.datastat.entity.StatTable;
import cn.ict.jwdsj.datapool.datastat.service.DictClient;
import cn.ict.jwdsj.datapool.datastat.service.StatTableService;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatTableServiceImpl implements StatTableService{
    @Autowired private StatTableRepo statTableRepo;
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private DictClient dictClient;

    @Override
    public void save(StatTable statTable) {
        statTableRepo.save(statTable);
    }

    @Override
    public void saveAll(List<StatTable> statTables) {
        statTableRepo.saveAll(statTables);
    }

    @Override
    public void update(StatTable statTable) {
        statTableRepo.save(statTable);
    }

    @Override
    public List<StatTable> listAll() {
        return statTableRepo.findAll();
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

        List<StatTable> tablesNotAdd =  jpaQueryFactory
                .selectDistinct(dictTable.dictDatabase.id, dictTable.id)
                .from(dictTable)
                .leftJoin(statTable)
                .on(dictTable.eq(statTable.dictTable))
                .where(statTable.isNull())
                .fetch()
                .stream()
                .map(tuple -> new StatTable()
                        .dictDatabase(tuple.get(dictTable.dictDatabase.id))
                        .dictTable(tuple.get(dictTable.id))
                        .updateDate(getTableCreateTime(tuple.get(dictTable.id)))
                )
                .collect(Collectors.toList());

        statTableRepo.saveAll(tablesNotAdd);

    }

    @Override
    public void deleteTablesNotExist() {
        QDictTable dictTable = QDictTable.dictTable;
        QStatTable statTable = QStatTable.statTable;

        List<StatTable> statTables = jpaQueryFactory
                .selectDistinct(statTable.id)
                .from(statTable)
                .leftJoin(dictTable)
                .on(statTable.dictTable.eq(dictTable))
                .where(dictTable.isNull())
                .fetch()
                .stream()
                .map(id -> new StatTable().id(id))
                .collect(Collectors.toList());
        statTableRepo.deleteAll(statTables);
    }

}
