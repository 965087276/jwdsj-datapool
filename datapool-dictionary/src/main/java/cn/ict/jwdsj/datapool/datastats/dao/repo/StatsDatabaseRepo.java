package cn.ict.jwdsj.datapool.datastats.dao.repo;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import com.querydsl.core.types.Predicate;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StatsDatabaseRepo extends JpaRepository<StatsDatabase, Long>, QuerydslPredicateExecutor<StatsDatabase> {

    List<StatsDatabase> findByDatabaseIdIn(List<Long> databaseIds);

    Page<StatsDatabase> findAll(Predicate predicate, Pageable pageable);

    void deleteByDatabaseId(long databaseId);

    @Transactional
    @Modifying
    @Query("update StatsDatabase r set r.enDatabase = :enDatabase, r.chDatabase = :chDatabase where r.databaseId = :databaseId")
    void updateDatabasesInfo(@Param("databaseId") long databaseId, @Param("enDatabase") String enDatabase, @Param("chDatabase") String chDatabase);
}
