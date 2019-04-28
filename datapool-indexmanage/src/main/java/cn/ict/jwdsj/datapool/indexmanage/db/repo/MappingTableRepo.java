package cn.ict.jwdsj.datapool.indexmanage.db.repo;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MappingTableRepo extends JpaRepository<MappingTable, Long>, QuerydslPredicateExecutor<MappingTable> {

    /**
     * 更新记录数
     * @param dictTableId 表id
     * @param indexRecords 索引中该表的记录数
     * @param tableRecords 数据库中该表的记录数
     */
    @Transactional
    @Modifying
    @Query("update MappingTable r set r.indexRecords = :indexRecords, r.tableRecords = :tableRecords where r.dictTableId = :dictTableId")
    void updateRecords(@Param("dictTableId") long dictTableId, @Param("indexRecords") long indexRecords, @Param("tableRecords") long tableRecords);

    void deleteByDictTableId(long dictTableId);

    MappingTable findByDictTableId(long dictTableId);
}
