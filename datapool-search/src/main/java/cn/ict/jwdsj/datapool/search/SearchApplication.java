package cn.ict.jwdsj.datapool.search;

import cn.ict.jwdsj.datapool.api.ApiCommonConfig;
import cn.ict.jwdsj.datapool.common.DataPoolCommonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableCaching
@SpringBootApplication
@EnableFeignClients
@EnableAsync
@ComponentScan(basePackageClasses = {
        DataPoolCommonConfig.class,
        SearchApplication.class,
        ApiCommonConfig.class})
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }
}
