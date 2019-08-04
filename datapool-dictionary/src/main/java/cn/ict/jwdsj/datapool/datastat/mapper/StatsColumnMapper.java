package cn.ict.jwdsj.datapool.datastat.mapper;

import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
