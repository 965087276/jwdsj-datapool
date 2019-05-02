package cn.ict.jwdsj.datapool.datasync.fullread.kafka;

import cn.ict.jwdsj.datapool.common.entity.datasync.TableSyncMsg;
import cn.ict.jwdsj.datapool.datasync.fullread.service.TableFullReadService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Optional;

@Component
@Slf4j
public class KafkaTableSyncTaskConsumer {
    @Autowired
    private TableFullReadService fullReadService;


    @KafkaListener(topics = "#{'${kafka.topic-name.table-sync-task}'}", groupId = "table-sync-task")
    public void listen(ConsumerRecord<String, String> record) throws InterruptedException, SQLException {
        Optional<String> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            TableSyncMsg msg = JSONObject.parseObject(record.value(), TableSyncMsg.class);
            fullReadService.fullRead(msg);
        }
    }
}
