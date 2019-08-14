package cn.ict.jwdsj.datapool.dictionary.service.meta;

import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaTable;

import java.util.List;

public interface MetaTableService {
    List<MetaTable> listByDatabase(String database);

    List<String> listTablesNotAdd(long databaseId);
}
