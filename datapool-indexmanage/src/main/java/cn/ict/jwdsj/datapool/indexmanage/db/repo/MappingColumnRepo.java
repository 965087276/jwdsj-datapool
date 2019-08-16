package cn.ict.jwdsj.datapool.indexmanage.db.repo;


import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.dto.ColumnTypeDTO;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MappingColumnRepo extends JpaRepository<MappingColumn, Long> {

    List<MappingColumn> findByTableId(long tableId);

    List<MappingColumn> findByTableIdAndDisplayed(long tableId, boolean displayed);

    void deleteByTableId(long tableId);

    MappingColumn findByColumnId(long columnId);

    @Query("select new cn.ict.jwdsj.datapool.common.entity.indexmanage.dto.ColumnTypeDTO(t.columnId, t.esColumn, t.type) from MappingColumn t where t.tableId = ?1")
    List<ColumnTypeDTO> listColumnTypeDTOByTableId(long tableId);

    @Query(value = "select new map(t.type, count(t)) from MappingColumn t where t.tableId = ?1 group by t.type ")
    List<Map<String, String>> groupWithTypeAndCountByTableId(long tableId);
}
