package cn.ict.jwdsj.datapool.dictionary.dao.mapper.secondary.meta;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MetaDatabaseMapper {

    @Select("select count(*) from information_schema.schemata where schema_name = #{database}")
    int existsByDatabase(String database);

    @Select("show databases")
    List<String> listDatabase();
}
