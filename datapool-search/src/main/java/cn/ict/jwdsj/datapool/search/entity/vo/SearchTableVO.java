package cn.ict.jwdsj.datapool.search.entity.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SearchTableVO {
    private String took;
    private long totalHit;
    List<String> columns;
    private List<JSONObject> contents;

    public void setContents(List<JSONObject> contents) {
        this.contents = contents;
        this.columns = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(contents)) {
            this.columns = contents.get(0).keySet().stream().collect(Collectors.toList());
        }
    }
}
