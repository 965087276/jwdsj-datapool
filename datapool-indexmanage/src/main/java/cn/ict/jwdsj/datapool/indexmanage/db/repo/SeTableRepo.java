package cn.ict.jwdsj.datapool.indexmanage.db.repo;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.SeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SeTableRepo extends JpaRepository<SeTable, Long>, QuerydslPredicateExecutor<SeTable> {
    SeTable findByDictTableId(long dictTableId);

    void deleteByDictTableId(long dictTableId);
}
