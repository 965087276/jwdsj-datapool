package cn.ict.jwdsj.datapool.delete.service.impl;

import cn.ict.jwdsj.datapool.delete.service.ElasticRestService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class ElasticRestServiceImpl implements ElasticRestService {

    @Autowired
    private RestHighLevelClient client;

    @Value("${elasticsearch.index-prefix}")
    private String indexPrefix;
    @Value("${elasticsearch.alias-prefix}")
    private String aliasPrefix;

    /**
     * 删除某表的索引别名
     *
     * @param indexName 该表所在的索引
     * @param tableId   表id
     */
    @Override
    public void deleteAliasByIndexNameAndTableId(String indexName, long tableId) throws IOException {
        IndicesAliasesRequest request = new IndicesAliasesRequest();
        IndicesAliasesRequest.AliasActions removeAction =
                new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.REMOVE)
                        .index(indexName)
                        .alias(aliasPrefix + tableId);
        request.addAliasAction(removeAction);
        client.indices().updateAliases(request, RequestOptions.DEFAULT);
    }

    /**
     * 删除某表的数据
     *
     * @param indexName
     * @param tableId   表id
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
}
