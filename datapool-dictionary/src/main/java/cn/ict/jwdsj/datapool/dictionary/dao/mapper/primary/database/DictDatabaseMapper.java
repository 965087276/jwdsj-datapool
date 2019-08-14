package cn.ict.jwdsj.datapool.dictionary.dao.mapper.primary.database;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.dao.mapper.primary.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DictDatabaseMapper extends BaseMapper {

    /**
     * 批量插入（忽略错误）
     * @param dictDatabases
     */
    void insertIgnore(List<DictDatabase> dictDatabases);

    @Select("select en_database from dict_database")
    List<String> listEnDatabase();
}
