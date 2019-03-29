package cn.ict.jwdsj.datapool.dictionary.service.meta;

import cn.ict.jwdsj.datapool.dictionary.entity.meta.MetaTable;

import java.util.List;

public interface MetaTableService {
    List<MetaTable> listByDatabase(String database);
}
