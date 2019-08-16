package cn.ict.jwdsj.datapool.dictionary.dao.repo.column;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictColumnRepo extends JpaRepository<DictColumn, Long> {
    List<DictColumn> findByTableId(long tableId);
    void deleteAllByTableId(long tableId);

    @Query(value = "select new cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO(t.id, t.enColumn, t.chColumn) from DictColumn t where t.tableId = ?1")
    List<ColumnNameDTO> listColumnNameDTOsByTableId(long tableId);
}
