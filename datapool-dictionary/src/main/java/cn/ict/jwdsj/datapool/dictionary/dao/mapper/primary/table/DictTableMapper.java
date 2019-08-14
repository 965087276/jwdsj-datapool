package cn.ict.jwdsj.datapool.dictionary.dao.mapper.primary.table;

import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.dictionary.dao.mapper.primary.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DictTableMapper extends BaseMapper {

    /**
     * 批量插入
     * @param dictTables
     */
    void insertIgnore(List<DictTable> dictTables);

    @Select("select id from dict_table")
    List<Long> listTableId();

    @Select("select en_table from dict_table where database_id = #{databaseId}")
    List<String> listEnTableByDatabaseId(long databaseId);

    @Select("select en_table from dict_table where en_database = #{enDatabase}")
    List<String> listEnTableByEnDatabase(String enDatabase);
}
