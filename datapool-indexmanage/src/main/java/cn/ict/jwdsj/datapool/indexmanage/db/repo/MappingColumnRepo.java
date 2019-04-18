package cn.ict.jwdsj.datapool.indexmanage.db.repo;


import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MappingColumnRepo extends JpaRepository<MappingColumn, Long> {
    List<MappingColumn> findByDictTableId(long dictTableId);
    List<MappingColumn> findByDictTableIdAndDisplayed(long dictTableId, boolean displayed);
}
