package cn.ict.jwdsj.datapool.dictionary.table.repo;

import cn.ict.jwdsj.datapool.dictionary.database.entity.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.table.entity.DictTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictTableRepo extends JpaRepository<DictTable, Long> {

    List<DictTable> findByDictDatabase(DictDatabase dictDatabase);
}
