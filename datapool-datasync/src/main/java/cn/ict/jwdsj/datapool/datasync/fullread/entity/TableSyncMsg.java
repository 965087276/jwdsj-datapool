package cn.ict.jwdsj.datapool.datasync.fullread.entity;

import lombok.Data;

@Data
public class TableSyncMsg {
    private int id;

    private long databaseId;
    private String databaseName;

    private long tableId;
    private String tableName;

    private long indexId;
    private String indexName;
}
