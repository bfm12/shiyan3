package com.chm.bag_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.chm.bag_system.mapper")
@SpringBootApplication
public class BagSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BagSystemApplication.class, args);
    }

}
