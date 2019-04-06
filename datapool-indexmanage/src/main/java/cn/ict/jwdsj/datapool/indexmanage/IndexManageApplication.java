package cn.ict.jwdsj.datapool.indexmanage;

import cn.ict.jwdsj.datapool.common.DataPoolCommonConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackageClasses = {DataPoolCommonConfig.class,
        IndexManageApplication.class})
public class IndexManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(IndexManageApplication.class, args);
    }

    //让Spring管理JPAQueryFactory
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager){
        return new JPAQueryFactory(entityManager);
    }

}
