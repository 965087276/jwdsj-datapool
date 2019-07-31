package cn.ict.jwdsj.datapool.indexmanage.elastic.service;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.EsIndexDTO;

import java.io.IOException;
import java.util.List;

public interface ElasticRestService {

    /**
     * 在Elasticsearch中创建索引
     * @param indexDTO
     */
    void createIndex(EsIndexDTO indexDTO) throws IOException;

    /**
     * 添加字段
     */
    void addFields(EsIndex esIndex, List<EsColumn> esColumns) throws IOException;

    /**
     * 添加别名
     * @param indexName 索引名
     * @param tableId 表id
     */
    void addAlias(String indexName, long tableId) throws IOException;

    /**
     * 查询某表在搜索引擎中的记录数（使用索引别名查找）
     * @param tableId 表id
     * @return
     */
    long getRecordsByTableIdInAlias(long tableId) throws IOException;

    /**
     * 查询某表在搜索引擎中的记录数（使用索引名查找）
     * @param tableId 表id
     * @return
     */
    long getRecordsByTableIdInIndex(String indexName, long tableId) throws IOException;

    /**
     * 删除某表的索引别名和表id
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

    /**
     * 删除索引
     * @param indexName 索引名
     */
    void deleteIndex(String indexName) throws IOException;
}
