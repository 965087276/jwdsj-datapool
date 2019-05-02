package cn.ict.jwdsj.datapool.common.entity.datasync;

import lombok.Data;

@Data
public class TableSyncMsg {

    private long databaseId;
    private String databaseName;

    private long tableId;
    private String tableName;

    private long indexId;
    private String indexName;
}
