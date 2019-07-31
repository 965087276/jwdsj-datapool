package cn.ict.jwdsj.datapool.datastat.service;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 统计相关信息
 */
public interface StatsService {


    /**
     * 获取表的记录数
     * @param dictTableId
     * @return
     */
    long countTableRecords(long dictTableId);


    /**
     * 返回某个字段的20条数据
     * @param database 所在库
     * @param table 所在表
     * @param column 字段名
     * @return
     */
    List<String> getColumnData(@Param("database") String database, @Param("table") String table, @Param("column") String column);

    /**
     * 获取表的创建日期
     * @param tableId 表id
     * @return
     */
    LocalDate getTableCreateTime(long tableId);

    /**
     * 返回某个表的400条数据
     * @param database 所在库
     * @param table 表名
     * @return
     */
    @Select("select * from `${database}`.`${table}` limit 400")
    List<Map<String, Object>> getTableData(@Param("database") String database, @Param("table") String table);




}
