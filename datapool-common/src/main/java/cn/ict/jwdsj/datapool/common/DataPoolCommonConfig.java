package cn.ict.jwdsj.datapool.common;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackageClasses = DataPoolCommonConfig.class)
public class DataPoolCommonConfig {
}
