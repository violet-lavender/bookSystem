package com.exp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class IntelligentBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelligentBookApplication.class, args);
    }

}
