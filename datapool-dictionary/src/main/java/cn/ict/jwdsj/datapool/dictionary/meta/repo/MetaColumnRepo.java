package cn.ict.jwdsj.datapool.dictionary.meta.repo;

import cn.ict.jwdsj.datapool.dictionary.meta.entity.MetaColumn;
import cn.ict.jwdsj.datapool.dictionary.meta.entity.MetaColumnMultiKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetaColumnRepo extends JpaRepository<MetaColumn, MetaColumnMultiKeys> {
    List<MetaColumn> findByDatabaseAndTable(String database, String table);
}
