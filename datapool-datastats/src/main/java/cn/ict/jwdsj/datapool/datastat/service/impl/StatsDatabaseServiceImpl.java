package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.datastat.entity.QStatDatabase;
import cn.ict.jwdsj.datapool.datastat.repo.StatsDatabaseRepo;
import cn.ict.jwdsj.datapool.datastat.service.StatsDatabaseService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsDatabaseServiceImpl implements StatsDatabaseService {
    @Autowired private StatsDatabaseRepo statsDatabaseRepo;
    @Autowired private JPAQueryFactory jpaQueryFactory;

    @Override
    public void save(StatsDatabase statsDatabase) {
        statsDatabaseRepo.save(statsDatabase);
    }

    @Override
    public void saveAll(List<StatsDatabase> statsDatabases) {
        statsDatabaseRepo.saveAll(statsDatabases);
    }

    @Override
    public void update(StatsDatabase statsDatabase) {
        statsDatabaseRepo.save(statsDatabase);
    }

    @Override
    public List<StatsDatabase> listAll() {
        return statsDatabaseRepo.findAll();
    }

    @Override
    public void saveDatabasesNotAdd() {
        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;
        QStatDatabase statDatabase = QStatDatabase.statDatabase;

        List<StatsDatabase> statsDatabases = jpaQueryFactory
                .selectDistinct(dictDatabase.id)
                .from(dictDatabase)
                .leftJoin(statDatabase)
                .on(dictDatabase.eq(statDatabase.dictDatabase))
                .where(statDatabase.isNull())
                .fetch()
                .stream()
                .map(StatsDatabase::builtByDatabaseId)
                .collect(Collectors.toList());
        statsDatabaseRepo.saveAll(statsDatabases);

    }

    @Override
    public void deleteDatabasesNotExist() {
        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;
        QStatDatabase statDatabase = QStatDatabase.statDatabase;

        List<StatsDatabase> statsDatabases = jpaQueryFactory
                .selectDistinct(statDatabase)
                .from(statDatabase)
                .leftJoin(dictDatabase)
                .on(statDatabase.dictDatabase.eq(dictDatabase))
                .where(dictDatabase.isNull())
                .fetch();
        statsDatabaseRepo.deleteAll(statsDatabases);
    }
}
