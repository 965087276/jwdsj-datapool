package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.datastat.entity.QStatDatabase;
import cn.ict.jwdsj.datapool.common.entity.datastats.StatDatabase;
import cn.ict.jwdsj.datapool.datastat.repo.StatDatabaseRepo;
import cn.ict.jwdsj.datapool.datastat.service.StatDatabaseService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatDatabaseServiceImpl implements StatDatabaseService {
    @Autowired private StatDatabaseRepo statDatabaseRepo;
    @Autowired private JPAQueryFactory jpaQueryFactory;

    @Override
    public void save(StatDatabase statDatabase) {
        statDatabaseRepo.save(statDatabase);
    }

    @Override
    public void saveAll(List<StatDatabase> statDatabases) {
        statDatabaseRepo.saveAll(statDatabases);
    }

    @Override
    public void update(StatDatabase statDatabase) {
        statDatabaseRepo.save(statDatabase);
    }

    @Override
    public List<StatDatabase> listAll() {
        return statDatabaseRepo.findAll();
    }

    @Override
    public void saveDatabasesNotAdd() {
        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;
        QStatDatabase statDatabase = QStatDatabase.statDatabase;

        List<StatDatabase> statDatabases = jpaQueryFactory
                .selectDistinct(dictDatabase.id)
                .from(dictDatabase)
                .leftJoin(statDatabase)
                .on(dictDatabase.eq(statDatabase.dictDatabase))
                .where(statDatabase.isNull())
                .fetch()
                .stream()
                .map(StatDatabase::builtByDatabaseId)
                .collect(Collectors.toList());
        statDatabaseRepo.saveAll(statDatabases);

    }

    @Override
    public void deleteDatabasesNotExist() {
        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;
        QStatDatabase statDatabase = QStatDatabase.statDatabase;

        List<StatDatabase> statDatabases = jpaQueryFactory
                .selectDistinct(statDatabase)
                .from(statDatabase)
                .leftJoin(dictDatabase)
                .on(statDatabase.dictDatabase.eq(dictDatabase))
                .where(dictDatabase.isNull())
                .fetch();
        statDatabaseRepo.deleteAll(statDatabases);
    }
}
