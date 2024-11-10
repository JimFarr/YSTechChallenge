package com.james.ystechchallenge.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.james.ystechchallenge.service.abstraction",
    "com.james.ystechchallenge.service.impl",
    "com.james.ystechchallenge.service.mapping",
    "com.james.ystechchallenge.data.abstraction",
    "com.james.ystechchallenge.data.impl",
    "com.james.ystechchallenge.web.controller",
    "com.james.ystechchallenge.web.mapping",
    "com.james.ystechchallenge.web.exception",
    "com.james.ystechchallenge.web.config",
    "com.james.ystechchallenge.web.validation"
})
public class Web {
    public static void main(String[] args) {
        SpringApplication.run(Web.class, args);
    }
}
