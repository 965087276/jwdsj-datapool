package cn.ict.jwdsj.datapool.datastat.repo;


import cn.ict.jwdsj.datapool.common.entity.datastats.StatsTable;
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


@Repository
public interface StatsTableRepo extends JpaRepository<StatsTable, Long>, QuerydslPredicateExecutor<StatsTable> {
    StatsTable findByDictTableId(long dictTableId);

    Page<StatsTable> findAll(Predicate predicate, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update StatsTable r set r.enTable = :enTable, r.chTable = :chTable where r.dictTableId = :tableId")
    void updateTableInfo(@Param("tableId") long tableId, @Param("enTable") String enTable, @Param("chTable") String chTable);
}
