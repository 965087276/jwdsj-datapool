package cn.ict.jwdsj.datapool.delete;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DeleteApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeleteApplication.class, args);
    }
}
