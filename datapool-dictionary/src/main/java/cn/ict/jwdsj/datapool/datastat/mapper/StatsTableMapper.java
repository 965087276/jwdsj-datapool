package cn.ict.jwdsj.datapool.datastat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Mapper
@Repository
public interface StatsTableMapper {

    @Select("select max(update_date) from stat_table where database_id = #{databaseId}")
    LocalDate getDatabaseUpdateDate(long databaseId);

    @Select("select sum(total_records) from stat_table where database_id = #{databaseId}")
    long countDatabaseRecords(long databaseId);

    @Select("select count(*) from stat_table where database_id = #{databaseId}")
    int countTablesByDatabaseId(long databaseId);
}
