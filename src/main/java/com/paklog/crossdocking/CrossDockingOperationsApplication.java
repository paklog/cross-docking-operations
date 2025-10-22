package com.paklog.crossdocking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
@EnableMongoAuditing
public class CrossDockingOperationsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrossDockingOperationsApplication.class, args);
    }
}
