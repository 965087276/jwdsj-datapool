package cn.ict.jwdsj.datapool.search.entity.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class SearchTableVO {
    /**
     * 搜索耗时
     */
    private String took;
    /**
     * 命中结果数
     */
    private long totalHit;
    /**
     * 字段中英对照
     */
    private Map<String, String> fields;
    /**
     * 搜索结果
     */
    private List<JSONObject> contents;

}
