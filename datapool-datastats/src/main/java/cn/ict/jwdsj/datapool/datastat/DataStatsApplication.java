package cn.ict.jwdsj.datapool.datastat;

import cn.ict.jwdsj.datapool.api.ApiCommonConfig;
import cn.ict.jwdsj.datapool.common.DataPoolCommonConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.persistence.EntityManager;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@ComponentScan(basePackageClasses = {
        DataPoolCommonConfig.class,
        DataStatsApplication.class,
        ApiCommonConfig.class})
public class DataStatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataStatsApplication.class, args);
    }

    //让Spring管理JPAQueryFactory
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager){
        return new JPAQueryFactory(entityManager);
    }
}
