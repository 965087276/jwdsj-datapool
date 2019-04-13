package cn.ict.jwdsj.datapool.datasync.fullread.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaTableFullReadProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic-name.table-full-read}")
    private String tableFullReadTopic;

    public void send(String record) {
        kafkaTemplate.send(tableFullReadTopic, record);
    }
}
