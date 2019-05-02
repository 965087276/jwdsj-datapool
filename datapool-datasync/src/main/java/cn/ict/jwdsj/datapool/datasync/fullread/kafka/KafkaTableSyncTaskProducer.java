package cn.ict.jwdsj.datapool.datasync.fullread.kafka;

import cn.ict.jwdsj.datapool.api.feign.IndexManageClient;
import cn.ict.jwdsj.datapool.common.entity.datasync.TableSyncMsg;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaTableSyncTaskProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Value("${kafka.topic-name.table-sync-task}")
    private String syncTableTaskTopic;

    public void sendSyncMsg(TableSyncMsg msg)  {

        kafkaTemplate.send(syncTableTaskTopic, JSON.toJSONString(msg));
        log.info("the msg have sent to kafka, table is {}.{}", msg.getTableName(), msg.getDatabaseName());

    }
}
