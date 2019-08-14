package cn.ict.jwdsj.datapool.dictionary.dao.mapper.primary;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BaseMapper {

    @Select("select CURRENT_TIMESTAMP")
    String getCurrentTimeStamp();
}
