package com.storeops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StoreOpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreOpsApplication.class, args);
    }
}
