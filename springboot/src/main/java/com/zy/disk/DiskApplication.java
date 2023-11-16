package com.zy.disk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@MapperScan("com.zy.disk.mapper")
public class DiskApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiskApplication.class, args);
    }

}
