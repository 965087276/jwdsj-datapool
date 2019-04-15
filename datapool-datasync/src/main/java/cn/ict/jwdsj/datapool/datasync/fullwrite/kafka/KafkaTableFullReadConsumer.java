package cn.ict.jwdsj.datapool.datasync.fullwrite.kafka;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class KafkaTableFullReadConsumer {

    @Autowired
    private BulkProcessor bulkProcessor;

//    @KafkaListener(topics = "#{'${kafka.topic-name.table-full-read}'}", groupId = "table-full-read", concurrency = "4", containerFactory = "batchFactory")
//    public void listen(List<ConsumerRecord<?, ?>> records) throws InterruptedException, SQLException {
//        for (ConsumerRecord<?, ?> record : records) {
//            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
//            if (kafkaMessage.isPresent()) {
//                Object message = record.value();
//                ++cnt;
//                if (cnt % 20000 == 0)
//                log.info("cnt = {}", cnt);
//            }
//        }
//    }
//    @KafkaListener(topics = "#{'${kafka.topic-name.table-full-read}'}", groupId = "table-full-read", concurrency = "4")
//    public void listen(ConsumerRecord<?, String> record)  {
//
//            Optional<String> kafkaMessage = Optional.ofNullable(record.value());
//            if (kafkaMessage.isPresent()) {
//
//                JSONObject doc = JSONObject.parseObject(record.value());
//                String index = doc.getString("elastic_index_name");
//                String type = "doc";
//                String docId = doc.getString("elastic_table_id") + "-" + doc.getString("md5_id");
//                IndexRequest request = new IndexRequest(index, "doc", docId).source(doc);
////                System.out.println(request);
//                bulkProcessor.add(request);
//            }
//
//    }
}
