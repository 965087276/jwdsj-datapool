package cn.ict.jwdsj.datapool.datasync.fullread.kafka;

import cn.ict.jwdsj.datapool.api.feign.IndexManageClient;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingTable;
import cn.ict.jwdsj.datapool.datasync.fullread.entity.TableSyncMsg;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class KafkaTableSyncTaskProducer {

    @Autowired
    private IndexManageClient indexManageClient;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static int id = 0;

    @Value("${kafka.topic-name.table-sync-task}")
    private String syncTableTaskTopic;

    @Scheduled(cron = "0/10 * * * * ?")
    public void getTablesNeedToUpdate() throws InterruptedException {
        List<MappingTable> mappingTables = indexManageClient.getTableNeedToUpdate();

        for (MappingTable mappingTable : mappingTables) {
            TableSyncMsg msg = new TableSyncMsg();
            msg.setDatabaseId(mappingTable.getDictDatabaseId());
            String dbSql = "select en_database from dict_database where id = " + mappingTable.getDictDatabaseId();
            String tbSql = "select en_table from dict_table where id = " + mappingTable.getDictTableId();
            msg.setDatabaseName(jdbcTemplate.queryForObject(dbSql, String.class));
            msg.setTableId(mappingTable.getDictTableId());
            msg.setTableName(jdbcTemplate.queryForObject(tbSql, String.class));
            msg.setIndexName(mappingTable.getEsIndex().getIndexName());
            msg.setId(++id);
            kafkaTemplate.send(syncTableTaskTopic, JSON.toJSONString(msg));
            log.info("the msg have sent to kafka, {}", msg.getId());
            // 更新update_date
            jdbcTemplate.update("update mapping_table set update_date = current_date where table_id = ?", mappingTable.getDictTableId());
            // 设置个间隔缓冲一下
            Thread.sleep(10000);
        }
    }
}
