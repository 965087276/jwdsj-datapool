package cn.ict.jwdsj.datapool.dictionary.meta.repo;

import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaColumn;

import java.util.List;

public interface MetaColumnRepo {
    List<MetaColumn> findByDatabaseAndTable(String database, String table);
    List<MetaColumn> findByDatabaseAndTableIn(String database, List<String> tables);
}
