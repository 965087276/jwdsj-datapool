package cn.ict.jwdsj.datapool.indexmanage.db.repo;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.SeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SeTableRepo extends JpaRepository<SeTable, Long>, QuerydslPredicateExecutor<SeTable> {
    SeTable findByTableId(long tableId);

    void deleteByTableId(long tableId);

    /**
     * 修改sync字段
     * @param tableId 表id
     */
    @Transactional
    @Modifying
    @Query("update SeTable r set r.sync = :sync where r.tableId = :tableId")
    void updateSync(@Param("tableId") long tableId, @Param("sync") boolean sync);
}
