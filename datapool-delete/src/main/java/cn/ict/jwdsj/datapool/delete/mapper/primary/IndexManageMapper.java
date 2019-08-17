package cn.ict.jwdsj.datapool.delete.mapper.primary;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IndexManageMapper {

    @Delete("delete from se_table where database_id = #{databaseId}")
    void deleteSeTableByDatabaseId(@Param("databaseId") long databaseId);
    @Delete("delete from se_table where table_id = #{tableId}")
    void deleteSeTableByTableId(@Param("tableId") long tableId);

    @Delete("delete from mapping_table where database_id = #{databaseId}")
    void deleteMappingTableByDatabaseId(@Param("databaseId") long databaseId);
    @Delete("delete from mapping_table where table_id = #{tableId}")
    void deleteMappingTableByTableId(@Param("tableId") long tableId);

    @Delete("delete from mapping_column where database_id = #{databaseId}")
    void deleteMappingColumnByDatabaseId(@Param("databaseId") long databaseId);
    @Delete("delete from mapping_column where table_id = #{tableId}")
    void deleteMappingColumnByTableId(@Param("tableId") long tableId);

    @Select("select index_name from mapping_table where table_id = #{tableId}")
    String getIndexNameByTableId(@Param("tableId") long tableId);

    /**
     * 获取某个库下“已经同步数据至搜索引擎”的表的信息
     * @param databaseId
     * @return
     */
    @Select("select table_id from mapping_table where database_id = #{databaseId}")
    List<Long> getMappingTableIdByDatabaseId(@Param("databaseId") long databaseId);

    @Select("select count(*) from mapping_table where table_id = #{tableId}")
    int existsInMappingTable(@Param("tableId") long tableId);

}
