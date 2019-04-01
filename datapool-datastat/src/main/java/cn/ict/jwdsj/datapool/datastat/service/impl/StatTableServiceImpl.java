package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.datastat.repo.StatTableRepo;
import cn.ict.jwdsj.datapool.datastat.entity.QStatTable;
import cn.ict.jwdsj.datapool.datastat.entity.StatTable;
import cn.ict.jwdsj.datapool.datastat.service.StatTableService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatTableServiceImpl implements StatTableService{
    @Autowired private StatTableRepo statTableRepo;
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @Autowired private JdbcTemplate jdbcTemplate;

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
    public void saveTablesNotAdd() {
        QDictTable dictTable = QDictTable.dictTable;
        QStatTable statTable = QStatTable.statTable;

        List<Long> dictTablesNotAdd =  jpaQueryFactory
                .selectDistinct(dictTable.id)
                .from(dictTable)
                .leftJoin(statTable)
                .on(dictTable.eq(statTable.dictTable))
                .where(statTable.isNull())
                .fetch();

        statTableRepo.saveAll(
                dictTablesNotAdd.stream()
                        .map(StatTable::builtByTableId)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void deleteTablesNotExist() {
        QDictTable dictTable = QDictTable.dictTable;
        QStatTable statTable = QStatTable.statTable;

        List<StatTable> statTables = jpaQueryFactory
                .selectDistinct(statTable)
                .from(statTable)
                .leftJoin(dictTable)
                .on(statTable.dictTable.eq(dictTable))
                .where(dictTable.isNull())
                .fetch();

        statTableRepo.deleteAll(statTables);
    }

}
