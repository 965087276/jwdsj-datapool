package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.entity.datastats.QStatsDatabase;
import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.datastat.repo.StatsDatabaseRepo;
import cn.ict.jwdsj.datapool.datastat.service.StatsDatabaseService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        QStatsDatabase statsDatabase = QStatsDatabase.statsDatabase;

        List<StatsDatabase> statsDatabases = jpaQueryFactory
                .selectDistinct(dictDatabase.id)
                .from(dictDatabase)
                .leftJoin(statsDatabase)
                .on(dictDatabase.eq(statsDatabase.dictDatabase))
                .where(statsDatabase.isNull())
                .fetch()
                .stream()
                .map(StatsDatabase::builtByDatabaseId)
                .collect(Collectors.toList());
        statsDatabaseRepo.saveAll(statsDatabases);

    }

    @Override
    public void deleteDatabasesNotExist() {
        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;
        QStatsDatabase statsDatabase = QStatsDatabase.statsDatabase;

        List<StatsDatabase> statsDatabases = jpaQueryFactory
                .selectDistinct(statsDatabase)
                .from(statsDatabase)
                .leftJoin(dictDatabase)
                .on(statsDatabase.dictDatabase.eq(dictDatabase))
                .where(dictDatabase.isNull())
                .fetch();
        statsDatabaseRepo.deleteAll(statsDatabases);
    }

    @Override
    public List<StatsDatabase> listByDictDatabaseIds(String ids) {

        List<DictDatabase> dictDatabases = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .map(DictDatabase::buildById)
                .collect(Collectors.toList());

        return statsDatabaseRepo.findByDictDatabaseIn(dictDatabases);
    }
}
