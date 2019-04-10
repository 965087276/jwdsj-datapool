package cn.ict.jwdsj.datapool.datasync;

import cn.ict.jwdsj.datapool.common.DataPoolCommonConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;

@SpringBootApplication
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
