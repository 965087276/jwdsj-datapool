package cn.ict.jwdsj.datapool.delete.mapper.primary;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
@Repository
public interface DictMapper {

    @Delete("delete from dict_database where id = #{id}")
    void deleteDictDatabaseById(@Param("id") long id);

    @Delete("delete from dict_table where database_id = #{databaseId}")
    void deleteDictTableByDatabaseId(@Param("databaseId") long databaseId);
    @Delete("delete from dict_table where id = #{id}")
    void deleteDictTableById(@Param("id") long id);

    @Delete("delete from dict_column where database_id = #{databaseId}")
    void deleteDictColumnByDatabaseId(@Param("databaseId") long databaseId);
    @Delete("delete from dict_column where table_id = #{tableId}")
    void deleteDictColumnByTableId(@Param("tableId") long tableId);

    @Select("select en_database from dict_database")
    List<String> listEnDatabase();

    @Select("select en_table from dict_table where en_database = #{enDatabase}")
    List<String> listEnTableByEnDatabase(@Param("enDatabase") String enDatabase);

    @Select("select id from dict_database where en_database = #{enDatabase}")
    long findIdByEnDatabase(@Param("enDatabase") String enDatabase);

    @Select("select id from dict_table where en_database = #{enDatabase} and en_table = #{enTable}")
    long findIdByEnDatabaseAndEnTable(@Param("enDatabase") String enDatabase, @Param("enTable") String enTable);

}
