package cn.ict.jwdsj.datapool.dictionary.dao.repo.column;

import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictColumnRepo extends JpaRepository<DictColumn, Long> {
    List<DictColumn> findByTableId(long tableId);
    void deleteAllByTableId(long tableId);
}
