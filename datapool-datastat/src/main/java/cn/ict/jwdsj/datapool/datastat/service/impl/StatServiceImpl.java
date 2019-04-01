package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.datastat.entity.QStatTable;
import cn.ict.jwdsj.datapool.datastat.service.StatService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class StatServiceImpl implements StatService {

    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private JPAQueryFactory jpaQueryFactory;

    @Override
    public long countTableRecords(DictTable dictTable) {
        String database = dictTable.getDictDatabase().getEnDatabase();
        String table = dictTable.getEnTable();
        String sql = String.format("select count(*) from `%s`.`%s`", database, table);
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public long countDatabaseRecords(DictDatabase dictDb) {
        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;
        QStatTable statTable = QStatTable.statTable;
        QDictTable dictTable = QDictTable.dictTable;
        Long count = jpaQueryFactory
                .select(statTable.totalRecords.sum())
                .from(statTable)
                .innerJoin(dictTable)
                .on(statTable.dictTable.eq(dictTable))
                .innerJoin(dictDatabase)
                .on(dictDatabase.eq(dictTable.dictDatabase))
                .where(dictDatabase.eq(dictDb))
                .fetchOne();

        return count == null ? 0L : count;

    }

    @Override
    public int countTablesInDatabase(DictDatabase dictDb) {
        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;
        QStatTable statTable = QStatTable.statTable;
        QDictTable dictTable = QDictTable.dictTable;

        Long count = jpaQueryFactory
                .select(statTable.count())
                .from(dictDatabase, dictTable, statTable)
                .where(
                        dictDatabase.eq(dictDb)
                                .and(dictTable.dictDatabase.eq(dictDatabase))
                                .and(statTable.dictTable.eq(dictTable))
                ).fetchOne();
        return count == null ? 0 : count.intValue();
    }
}
