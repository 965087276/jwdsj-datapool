package cn.ict.jwdsj.datapool.search.entity.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class SearchTableVO {
    private long totalHit;
    private List<JSONObject> contents;
}
