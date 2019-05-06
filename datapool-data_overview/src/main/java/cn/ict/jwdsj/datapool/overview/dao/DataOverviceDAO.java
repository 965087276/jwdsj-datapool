package cn.ict.jwdsj.datapool.overview.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface DataOverviceDAO {

    @Select("select * from `${database}`.`${table}` limit #{from}, #{size}")
    List<Map<String, Object>> getAllRecords(@Param("database") String databasze, @Param("table") String table, @Param("from") int from, @Param("size") int size);
}
