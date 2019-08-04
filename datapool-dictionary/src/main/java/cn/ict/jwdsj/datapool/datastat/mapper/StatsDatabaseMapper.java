package cn.ict.jwdsj.datapool.datastat.mapper;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface StatsDatabaseMapper {

    /**
     * 从dictDatabases中批量插入（使用xml方式实现）
     * @param currentTime 插入时的时间
     */
    void insertAll(String currentTime);

}
