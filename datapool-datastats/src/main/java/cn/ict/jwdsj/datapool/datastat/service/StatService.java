package cn.ict.jwdsj.datapool.datastat.service;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;

import java.util.Date;

/**
 * 统计相关信息
 */
public interface StatService {

    /**
     * 获取数据库更新日期
     * @param dictDatabase
     * @return
     */
    Date getDatabaseUpdateDate(DictDatabase dictDatabase);

    /**
     * 获取表的记录数
     * @param dictTable
     * @return
     */
    long countTableRecords(DictTable dictTable);

    /**
     * 获取数据库的记录数
     * @param dictDatabase
     * @return
     */
    long countDatabaseRecords(DictDatabase dictDatabase);

    /**
     * 获取某库的表数
     * @param dictDatabase
     * @return
     */
    int countTablesInDatabase(DictDatabase dictDatabase);
}
