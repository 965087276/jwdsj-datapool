package cn.ict.jwdsj.datapool.search.entity.vo;

import lombok.Data;
import org.elasticsearch.common.unit.TimeValue;

import java.util.List;

/**
 * 库聚合界面（仅是库的聚合，没有表的聚合）
 */
@Data
public class AggDatabasePageVO {
    /**
     * 耗时
     */
    private TimeValue took;
    /**
     * 命中的文档数
     */
    private long docHit;

    /**
     * 命中的数据库数
     */
    private long databaseHit;

    /**
     * 库信息列表
     */
    private List<AggDatabaseVO> aggDatabases;
}
