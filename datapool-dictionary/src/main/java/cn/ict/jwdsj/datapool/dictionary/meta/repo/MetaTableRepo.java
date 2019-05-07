package cn.ict.jwdsj.datapool.dictionary.meta.repo;

import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaTable;

import java.util.List;

public interface MetaTableRepo {
    List<MetaTable> findByDatabase(String database);
}
