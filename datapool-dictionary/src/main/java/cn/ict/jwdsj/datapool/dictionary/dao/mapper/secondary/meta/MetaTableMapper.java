package cn.ict.jwdsj.datapool.dictionary.dao.mapper.secondary.meta;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MetaTableMapper {
    @Select("show tables from `${database}`")
    List<String> listByDatabase(@Param("database") String database);
}
