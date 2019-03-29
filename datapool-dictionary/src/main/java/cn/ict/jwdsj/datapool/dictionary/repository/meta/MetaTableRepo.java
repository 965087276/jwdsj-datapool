package cn.ict.jwdsj.datapool.dictionary.repository.meta;

import cn.ict.jwdsj.datapool.dictionary.entity.meta.MetaTable;
import cn.ict.jwdsj.datapool.dictionary.entity.meta.MetaTableMultiKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetaTableRepo extends JpaRepository<MetaTable, MetaTableMultiKeys> {
    List<MetaTable> findByDatabase(String database);
}
