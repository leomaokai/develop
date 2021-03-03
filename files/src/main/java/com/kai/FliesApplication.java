package com.kai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kai.mapper")
public class FliesApplication {

    public static void main(String[] args) {
        SpringApplication.run(FliesApplication.class, args);
    }

}
