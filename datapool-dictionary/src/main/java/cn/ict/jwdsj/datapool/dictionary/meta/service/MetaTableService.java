package cn.ict.jwdsj.datapool.dictionary.meta.service;

import cn.ict.jwdsj.datapool.dictionary.meta.entity.MetaTable;

import java.util.List;

public interface MetaTableService {
    List<MetaTable> listByDatabase(String database);
}
