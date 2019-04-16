package cn.ict.jwdsj.datapool.indexmanage.elastic.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.EsIndexDTO;
import cn.ict.jwdsj.datapool.indexmanage.elastic.service.ElasticRestService;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
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
     * @param databaseId 库id
     * @param tableId    表id
     */
    @Override
    public void addAlias(String indexName, long databaseId, long tableId) throws IOException {
        IndicesAliasesRequest request = new IndicesAliasesRequest();

        IndicesAliasesRequest.AliasActions aliasActions =
                new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                .index(indexName)
                .alias(aliasPrefix + databaseId + "-" + tableId)
                .filter(QueryBuilders.termQuery("elastic_table_id", tableId));
        request.addAliasAction(aliasActions);

        client.indices().updateAliases(request, RequestOptions.DEFAULT);

    }
}
