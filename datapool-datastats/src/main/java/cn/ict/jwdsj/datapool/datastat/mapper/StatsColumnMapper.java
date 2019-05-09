package cn.ict.jwdsj.datapool.datastat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StatsColumnMapper {
    @Select("select `${column}` from `${database}`.`${table}` where `${column}` is not null and `${column}` != '' limit 20")
    List<String> getColumnData(@Param("database") String database, @Param("table") String table, @Param("column") String column);
}
