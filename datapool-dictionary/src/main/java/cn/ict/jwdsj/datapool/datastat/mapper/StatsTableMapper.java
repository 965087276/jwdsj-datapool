package cn.ict.jwdsj.datapool.datastat.mapper;

import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface StatsTableMapper {

    /**
     * 从dictTables中批量插入（使用xml方式实现）
     * @param currentTime 插入时的时间
     */
    void insertAll(String currentTime);

    /**
     * 从dictTables中批量插入（使用xml方式实现）
     */
    void insertFromDictTable();

    @Select("select max(update_date) from stat_table where database_id = #{databaseId}")
    LocalDate getDatabaseUpdateDate(long databaseId);

    @Select("select sum(total_records) from stat_table where database_id = #{databaseId}")
    long countDatabaseRecords(long databaseId);

    @Select("select count(*) from stat_table where database_id = #{databaseId}")
    int countTablesByDatabaseId(long databaseId);
}
