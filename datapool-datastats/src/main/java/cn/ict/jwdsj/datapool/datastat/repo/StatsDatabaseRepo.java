package cn.ict.jwdsj.datapool.datastat.repo;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatsDatabaseRepo extends JpaRepository<StatsDatabase, Long> {
    void deleteAllByIdIn(List<Long> ids);
    List<StatsDatabase> findByDictDatabaseIn(List<DictDatabase> dictDatabases);
}
