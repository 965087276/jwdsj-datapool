package cn.ict.jwdsj.datapool.datasync;

import cn.ict.jwdsj.datapool.common.DataPoolCommonConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableAsync
@ComponentScan(basePackageClasses = {DataPoolCommonConfig.class,
        DataSyncApplication.class})
public class DataSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataSyncApplication.class, args);
    }

    //让Spring管理JPAQueryFactory
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager){
        return new JPAQueryFactory(entityManager);
    }
}
