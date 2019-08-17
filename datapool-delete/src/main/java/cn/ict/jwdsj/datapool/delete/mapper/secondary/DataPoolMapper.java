package cn.ict.jwdsj.datapool.delete.mapper.secondary;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DataPoolMapper {

    @Select("show databases")
    List<String> listDatabase();

    @Select("show tables from `${database}`")
    List<String> listTableByDatabase(@Param("database") String database);

}
