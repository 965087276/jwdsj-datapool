package cn.ict.jwdsj.datapool.datasync.fullwrite.elasticsearch.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BulkWriteConfig {

    @Bean
    public BulkProcessor bulkProcessor(@Autowired RestHighLevelClient restHighLevelClient) {

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {
                log.info("准备发送{}条数据", bulkRequest.numberOfActions());
                log.info("发送至{}", bulkRequest.getDescription());
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                log.info("发送状态为{}", bulkResponse.status());
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) { }
        };

        BulkProcessor.Builder builder = BulkProcessor.builder(
                (bulkRequest, bulkListener) ->
                        restHighLevelClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, bulkListener),
                listener);

        builder.setBulkActions(1000);
        builder.setBulkSize(new ByteSizeValue(5L, ByteSizeUnit.MB));
        builder.setConcurrentRequests(1);

        return builder.build();
    }
}
