package cn.ict.jwdsj.datapool.datastat.repo;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatsDatabaseRepo extends JpaRepository<StatsDatabase, Long>, QuerydslPredicateExecutor<StatsDatabase> {
    void deleteAllByIdIn(List<Long> ids);

    List<StatsDatabase> findByDictDatabaseIdIn(List<Long> dictDatabaseIds);

    Page<StatsDatabase> findAll(Predicate predicate, Pageable pageable);
}
