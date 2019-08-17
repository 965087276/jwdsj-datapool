package cn.ict.jwdsj.datapool.delete.mapper.primary;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StatsMapper {

    @Delete("delete from stat_database where database_id = #{databaseId}")
    void deleteStatsDatabaseByDatabaseId(@Param("databaseId") long databaseId);

    @Delete("delete from stat_table where database_id = #{databaseId}")
    void deleteStatsTableByDatabaseId(@Param("databaseId") long databaseId);
    @Delete("delete from stat_table where table_id = #{tableId}")
    void deleteStatsTableByTableId(@Param("tableId") long tableId);


    @Delete("delete from stat_column where database_id = #{databaseId}")
    void deleteStatsColumnByDatabaseId(@Param("databaseId") long databaseId);
    @Delete("delete from stat_column where table_id = #{tableId}")
    void deleteStatsColumnByTableId(@Param("tableId") long tableId);
}
