package com.james.ystechchallenge.datasyncdaemon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
    "com.james.ystechchallenge.service.abstraction",
    "com.james.ystechchallenge.service.impl",
    "com.james.ystechchallenge.service.mapping",
    "com.james.ystechchallenge.data.abstraction",
    "com.james.ystechchallenge.data.impl",
    "com.james.ystechchallenge.datasyncdaemon.abstractions",
    "com.james.ystechchallenge.datasyncdaemon.impl",
    "com.james.ystechchallenge.datasyncdaemon.config",
    "com.james.ystechchallenge.core.config"
})
@EnableScheduling
public class DataSyncDaemon {
    public static void main(String[] args) {
        SpringApplication.run(DataSyncDaemon.class, args);
    }
}
