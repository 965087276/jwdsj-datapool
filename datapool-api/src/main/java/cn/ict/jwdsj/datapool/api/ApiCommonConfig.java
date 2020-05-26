package cn.ict.jwdsj.datapool.api;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackageClasses = ApiCommonConfig.class)
public class ApiCommonConfig {
}
