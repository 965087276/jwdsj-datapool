package cn.ict.jwdsj.datapool.common.dto.indexmanage;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 全量读取数据时的字段信息
 */
@Data
public class TableFullReadDTO {
    /**
     * 需加入到搜索引擎中的字段列表
     */
    List<String> columns;

    /**
     * 表字段到索引字段的映射
     */
    Map<String, String> colAndEsColMap;
}
