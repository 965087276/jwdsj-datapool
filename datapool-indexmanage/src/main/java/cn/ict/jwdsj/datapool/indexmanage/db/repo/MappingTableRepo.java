package cn.ict.jwdsj.datapool.indexmanage.db.repo;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MappingTableRepo extends JpaRepository<MappingTable, Long>, QuerydslPredicateExecutor<MappingTable> {

    /**
     * 更新记录数
     * @param tableId 表id
     * @param indexRecords 索引中该表的记录数
     * @param tableRecords 数据库中该表的记录数
     */
    @Transactional
    @Modifying
    @Query("update MappingTable r set r.indexRecords = :indexRecords, r.tableRecords = :tableRecords where r.tableId = :tableId")
    void updateRecords(@Param("tableId") long tableId, @Param("indexRecords") long indexRecords, @Param("tableRecords") long tableRecords);

    /**
     * 将更新日期更新为当前时间
     * @param tableId 表id
     */
    @Transactional
    @Modifying
    @Query("update MappingTable r set r.updateDate = current_date where r.tableId = :tableId")
    void updateUpdateDate(@Param("tableId") long tableId);

    /**
     * 判断某索引下是否有表存在
     * @param indexId
     * @return
     */
    boolean existsByIndexId(long indexId);

    @Transactional
    void deleteByTableId(long tableId);

    MappingTable findByTableId(long tableId);

    @Query("select t.tableId from MappingTable t")
    List<Long> listTableId();

    @Query(value = "select current_date ", nativeQuery = true)
    LocalDate getLocalDate();
}
