package cn.ict.jwdsj.datapool.datastats.dao.mapper.primary;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface StatsColumnMapper {

    /**
     * 从dictColumns中批量插入（使用xml方式实现）
     * @param currentTime 插入时的时间
     */
    void insertAll(String currentTime);

    /**
     * 从dictColumns中批量插入（使用xml方式实现）
     */
    void insertFromDictColumn();


    /**
     * 计算某个表的字段数目
     * @param tableId 表id
     * @return
     */
    @Select("select count(*) from stat_column where table_id = #{tableId}")
    int countColumnsByTableId(long tableId);

    /**
     * 返回某个表的缺陷字段数
     * @param tableId 表id
     * @return
     */
    @Select("select count(*) from stat_column where table_id = #{tableId} and is_defect = 1")
    int countDefectedColumnsByTableId(long tableId);
}
