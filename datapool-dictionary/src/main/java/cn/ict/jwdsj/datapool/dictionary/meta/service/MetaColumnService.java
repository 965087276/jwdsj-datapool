package cn.ict.jwdsj.datapool.dictionary.meta.service;

import cn.ict.jwdsj.datapool.dictionary.meta.entity.MetaColumn;

import java.util.List;

public interface MetaColumnService {

    List<MetaColumn> listByDatabaseAndTable(String database, String table);

}
