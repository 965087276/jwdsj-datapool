package cn.ict.jwdsj.datapool.datastat.service;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;

import java.time.LocalDate;
import java.util.Date;

/**
 * 统计相关信息
 */
public interface StatsService {

    /**
     * 获取数据库更新日期
     * @param dictDatabaseId
     * @return
     */
    LocalDate getDatabaseUpdateDate(long dictDatabaseId);

    /**
     * 获取表的记录数
     * @param dictTableId
     * @return
     */
    long countTableRecords(long dictTableId);

    /**
     * 获取数据库的记录数
     * @param dictDatabaseId
     * @return
     */
    long countDatabaseRecords(long dictDatabaseId);

    /**
     * 获取某库的表数
     * @param dictDatabaseId
     * @return
     */
    int countTablesInDatabase(long dictDatabaseId);
}
