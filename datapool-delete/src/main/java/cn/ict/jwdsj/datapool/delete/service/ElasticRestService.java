package cn.ict.jwdsj.datapool.delete.service;

import java.io.IOException;

public interface ElasticRestService {

    /**
     * 删除某表的索引别名
     * @param indexName 该表所在的索引
     * @param tableId 表id
     */
    void deleteAliasByIndexNameAndTableId(String indexName, long tableId) throws IOException;

    /**
     * 删除某表的数据
     * @param indexName
     * @param tableId 表id
     */
    void deleteDocsByTableId(String indexName, long tableId);
}
