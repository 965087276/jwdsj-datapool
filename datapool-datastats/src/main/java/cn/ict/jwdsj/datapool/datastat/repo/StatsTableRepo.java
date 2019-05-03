package cn.ict.jwdsj.datapool.datastat.repo;


import cn.ict.jwdsj.datapool.common.entity.datastats.StatsTable;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface StatsTableRepo extends JpaRepository<StatsTable, Long>, QuerydslPredicateExecutor<StatsTable> {
    StatsTable findByDictTableId(long dictTableId);

    Page<StatsTable> findAll(Predicate predicate, Pageable pageable);
}
