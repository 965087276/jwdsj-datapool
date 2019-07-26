package cn.ict.jwdsj.datapool.search.entity.vo;

import lombok.Data;
import org.elasticsearch.common.unit.TimeValue;

import java.io.Serializable;
import java.util.List;

/**
 * 库聚合界面（仅是库的聚合，没有表的聚合）
 */
@Data
public class AggDatabasePageVO implements Serializable {
    /**
     * 耗时
     */
    private String took = "";
    /**
     * 命中的文档数
     */
    private long docHit = 0L;

    /**
     * 命中的数据库数
     */
    private long databaseHit = 0L;

    /**
     * 库信息列表
     */
    private List<AggDatabaseVO> aggDatabases = null;
}
