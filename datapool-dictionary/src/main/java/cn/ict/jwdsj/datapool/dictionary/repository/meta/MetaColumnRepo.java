package cn.ict.jwdsj.datapool.dictionary.repository.meta;

import cn.ict.jwdsj.datapool.dictionary.entity.meta.MetaColumn;
import cn.ict.jwdsj.datapool.dictionary.entity.meta.MetaColumnMultiKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetaColumnRepo extends JpaRepository<MetaColumn, MetaColumnMultiKeys> {
    List<MetaColumn> findByDatabaseAndTable(String database, String table);
}
