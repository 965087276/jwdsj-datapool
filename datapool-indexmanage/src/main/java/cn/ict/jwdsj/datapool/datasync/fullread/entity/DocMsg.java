package cn.ict.jwdsj.datapool.datasync.fullread.entity;

import lombok.Data;

import java.util.Map;

@Data
public class DocMsg {
    private long indexId;
    private long databaseId;
    private long tableId;
    Map<String, Object> data;
}
