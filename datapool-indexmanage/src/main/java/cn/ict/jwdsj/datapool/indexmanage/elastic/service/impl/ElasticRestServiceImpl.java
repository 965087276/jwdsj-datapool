package cn.ict.jwdsj.datapool.indexmanage.elastic.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.EsIndexDTO;
import cn.ict.jwdsj.datapool.indexmanage.elastic.service.ElasticRestService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest.AliasActions;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.ict.jwdsj.datapool.indexmanage.elastic.constant.EsColumnTypeEnum.*;

@Service
@Slf4j
public class ElasticRestServiceImpl implements ElasticRestService {

    @Autowired
    private RestHighLevelClient client;

    @Value("${elasticsearch.index-prefix}")
    private String indexPrefix;
    @Value("${elasticsearch.alias-prefix}")
    private String aliasPrefix;
    

    @Override
    public void createIndex(EsIndexDTO indexDTO) throws IOException {

        CreateIndexRequest request = new CreateIndexRequest(indexPrefix + indexDTO.getIndexName());

        request.settings(Settings.builder()
                .put("index.number_of_shards", indexDTO.getNumShards())
                .put("index.number_of_replicas", 1)
                .put("index.refresh_interval", "60s")
        );

        // 初始化索引配置
        request.mapping("doc", DOC.getTemplate(), XContentType.JSON);
        // 设置别名
        request.alias(new Alias(indexPrefix));

        client.indices().create(request, RequestOptions.DEFAULT);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFields(EsIndex esIndex, List<EsColumn> esColumns) throws IOException {
        PutMappingRequest request = new PutMappingRequest(esIndex.getIndexName());
        Map<String, Object> properties = new HashMap<>();
        for (EsColumn esColumn : esColumns) {
            switch (esColumn.getType()) {
                case "TEXT":
                    properties.put(esColumn.getName(), JSON.parseObject(TEXT.getTemplate()));
                    break;
                case "KEYWORD":
                    properties.put(esColumn.getName(), JSON.parseObject(KEYWORD.getTemplate()));
                    break;
                case "NOT_SEARCH":
                    properties.put(esColumn.getName(), JSON.parseObject(NOT_SEARCH.getTemplate()));
                    break;
            }
        }

        request.type("doc").source(MapUtil.of("properties", properties));
        client.indices().putMapping(request, RequestOptions.DEFAULT);

    }

    /**
     * 添加别名
     *
     * @param indexName  索引名
     * @param tableId    表id
     */
    @Override
    public void addAlias(String indexName, long tableId) throws IOException {
        IndicesAliasesRequest request = new IndicesAliasesRequest();

        AliasActions aliasActions =
                new AliasActions(AliasActions.Type.ADD)
                .index(indexName)
                .alias(aliasPrefix + tableId)
                .filter(QueryBuilders.termQuery("elastic_table_id", tableId));
        request.addAliasAction(aliasActions);

        client.indices().updateAliases(request, RequestOptions.DEFAULT);

    }

    /**
     * 查询某表在搜索引擎中的记录数（使用索引别名查找）
     *
     * @param tableId 表id
     * @return
     */
    @Override
    public long getRecordsByTableIdInAlias(long tableId) throws IOException {
        // 该表的索引别名
        String indexAlias = aliasPrefix + tableId;
        SearchRequest request = new SearchRequest(indexAlias);
        return client.search(request, RequestOptions.DEFAULT)
                .getHits()
                .totalHits;
    }

    /**
     * 查询某表在搜索引擎中的记录数（使用索引名查找）
     *
     * @param tableId 表id
     * @return
     */
    @Override
    public long getRecordsByTableIdInIndex(String indexName, long tableId) throws IOException {
        SearchRequest request = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(new TermQueryBuilder("elastic_table_id", tableId)).size(0);
        request.source(sourceBuilder);
        return client.search(request, RequestOptions.DEFAULT).getHits().totalHits;
    }

    /**
     * 删除某表的索引别名
     *
     * @param indexName 索引名
     * @param tableId 表id
     */
    @Override
    public void deleteAliasByIndexNameAndTableId(String indexName, long tableId) throws IOException {
        IndicesAliasesRequest request = new IndicesAliasesRequest();
        AliasActions removeAction =
                new AliasActions(AliasActions.Type.REMOVE)
                        .index(indexName)
                        .alias(aliasPrefix + tableId);
        request.addAliasAction(removeAction);
        client.indices().updateAliases(request, RequestOptions.DEFAULT);
    }

    /**
     * 删除某表的数据
     *
     * @param indexName
     * @param tableId 表id
     */
    @Override
    public void deleteDocsByTableId(String indexName, long tableId) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
        request.setQuery(new TermQueryBuilder("elastic_table_id", tableId));
        request.setConflicts("proceed");
        request.setBatchSize(2000);
        client.deleteByQueryAsync(request, RequestOptions.DEFAULT, new ActionListener<BulkByScrollResponse>() {
            @Override
            public void onResponse(BulkByScrollResponse bulkByScrollResponse) {
                log.info("删除索引数据成功，表id为{}", tableId);
            }

            @Override
            public void onFailure(Exception e) {
                log.error("删除索引数据失败，表id为{}，异常为{}", tableId, e.getMessage());
            }
        });
    }

    /**
     * 删除索引
     *
     * @param indexName 索引名
     */
    @Override
    public void deleteIndex(String indexName) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

}
