package cn.ict.jwdsj.datapool.common.entity.datasync;

import lombok.Data;

@Data
public class TableSyncMsg {

    private long databaseId;
    private String enDatabase;

    private long tableId;
    private String enTable;

    private long indexId;
    private String indexName;
}
