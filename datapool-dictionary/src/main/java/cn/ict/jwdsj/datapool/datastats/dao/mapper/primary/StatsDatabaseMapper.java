package cn.ict.jwdsj.datapool.datastats.dao.mapper.primary;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface StatsDatabaseMapper {

    /**
     * 从dictDatabases中批量插入（使用xml方式实现）
     * @param currentTime 插入时的时间
     */
    void insertAll(String currentTime);

}
