package cn.ict.jwdsj.datapool.dictionary.dao.mapper.secondary.meta;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface MetaColumnMapper {

    @Select("show full columns from `${database}`.`${table}`")
    List<Map<String, Object>> listByDatabaseAndTable(String database, String table);

}
