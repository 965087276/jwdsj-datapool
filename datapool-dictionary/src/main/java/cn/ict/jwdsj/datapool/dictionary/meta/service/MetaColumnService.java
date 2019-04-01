package cn.ict.jwdsj.datapool.dictionary.meta.service;

import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaColumn;

import java.util.List;

public interface MetaColumnService {

    /**
     * 查找某库某表的所有字段
     * @param database
     * @param table
     * @return
     */
    List<MetaColumn> listByDatabaseAndTable(String database, String table);

    /**
     * 查找某库下多个表的字段
     * @param database
     * @param tables
     * @return
     */
    List<MetaColumn> listByDatabaseAndTableIn(String database, List<String> tables);

}
