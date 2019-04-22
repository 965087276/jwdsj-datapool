package cn.ict.jwdsj.datapool.dictionary.config;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSender {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, Object obj) {
        String objStr = JSON.toJSONString(obj);
        kafkaTemplate.send(topic, objStr);
    }
}
