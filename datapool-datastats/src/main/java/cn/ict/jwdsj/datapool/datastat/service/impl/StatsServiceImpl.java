package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.ict.jwdsj.datapool.common.entity.datastats.QStatsTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.datastat.service.DictClient;
import cn.ict.jwdsj.datapool.datastat.service.StatsService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @Autowired private DictClient dictClient;

    @Override
    public LocalDate getDatabaseUpdateDate(long dictDatabaseId) {
        QStatsTable statsTable = QStatsTable.statsTable;
        LocalDate date = jpaQueryFactory
                .select(statsTable.updateDate.max())
                .from(statsTable)
                .where(statsTable.dictDatabaseId.eq(dictDatabaseId))
                .fetchOne();

         return date != null ? date : LocalDate.of(2019, 1, 1);

    }

    @Override
    public long countTableRecords(long dictTableId) {
        DictTable dictTable = dictClient.findDictTableById(dictTableId);
        String database = dictTable.getDictDatabase().getEnDatabase();
        String table = dictTable.getEnTable();
        String sql = String.format("select count(*) from `%s`.`%s`", database, table);
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public long countDatabaseRecords(long dictDatabaseId) {
        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;
        QStatsTable statsTable = QStatsTable.statsTable;

        Long count = jpaQueryFactory
                .select(statsTable.totalRecords.sum())
                .from(statsTable)
                .where(statsTable.dictDatabaseId.eq(dictDatabaseId))
                .fetchOne();

        return count == null ? 0L : count;

    }

    @Override
    public int countTablesInDatabase(long dictDatabaseId) {
        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;
        QStatsTable statsTable = QStatsTable.statsTable;

        Long count = jpaQueryFactory
                .select(statsTable.count())
                .from(statsTable)
                .where(statsTable.dictDatabaseId.eq(dictDatabaseId))
                .fetchOne();

        return count == null ? 0 : count.intValue();
    }

}
