package cn.ict.jwdsj.datapool.datastat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface StatsMapper {

    /**
     * 返回某个表的状态
     * @param database
     * @param table
     * @return
     */
    @Select("show table status from `${database}` like #{table}")
    Map<String, Object> getTableStatus(@Param("database") String database, @Param("table") String table);

    /**
     * 返回某个字段的20条数据
     * @param database 所在库
     * @param table 所在表
     * @param column 字段名
     * @return
     */
    @Select("select `${column}` from `${database}`.`${table}` where `${column}` is not null and `${column}` != '' limit 20")
    List<String> getColumnData(@Param("database") String database, @Param("table") String table, @Param("column") String column);

    /**
     * 返回某个表的400条数据
     * @param database 所在库
     * @param table 表名
     * @return
     */
    @Select("select * from `${database}`.`${table}` limit 400")
    List<Map<String, Object>> getTableData(@Param("database") String database, @Param("table") String table);

    /**
     * 获取某个表的数据条数
     * @param database 库名
     * @param table 表名
     * @return
     */
    @Select("select count(*) from `${database}`.`${table}`")
    long countTableRecords(@Param("database") String database, @Param("table") String table);

}
