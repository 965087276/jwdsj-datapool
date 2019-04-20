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

import java.time.LocalDate;
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
                .select(dictDatabase.id, dictDatabase.enDatabase, dictDatabase.chDatabase)
                .from(dictDatabase)
                .leftJoin(statsDatabase)
                .on(dictDatabase.id.eq(statsDatabase.dictDatabaseId))
                .where(statsDatabase.isNull())
                .fetch()
                .stream()
                .map(tuple -> StatsDatabase.builder()
                        .dictDatabaseId(tuple.get(dictDatabase.id))
                        .enDatabase(tuple.get(dictDatabase.enDatabase))
                        .chDatabase(tuple.get(dictDatabase.chDatabase))
                        .updateDate(LocalDate.of(2019, 1, 1))
                        .build()
                )
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
                .on(statsDatabase.dictDatabaseId.eq(dictDatabase.id))
                .where(dictDatabase.isNull())
                .fetch();
        statsDatabaseRepo.deleteAll(statsDatabases);
    }

    @Override
    public List<StatsDatabase> listByDictDatabaseIds(String ids) {

        List<Long> dictDatabaseIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        return statsDatabaseRepo.findByDictDatabaseIdIn(dictDatabaseIds);
    }
}
