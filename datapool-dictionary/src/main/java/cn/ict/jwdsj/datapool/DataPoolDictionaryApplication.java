package cn.ict.jwdsj.datapool;

import cn.ict.jwdsj.datapool.common.DataPoolCommonConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;

@SpringBootApplication
@ComponentScan(basePackageClasses = {DataPoolCommonConfig.class,
    DataPoolDictionaryApplication.class})
public class DataPoolDictionaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataPoolDictionaryApplication.class, args);
    }

    //让Spring管理JPAQueryFactory
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager){
        return new JPAQueryFactory(entityManager);
    }

}
