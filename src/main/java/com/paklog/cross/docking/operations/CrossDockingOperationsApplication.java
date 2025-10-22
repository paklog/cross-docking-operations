package com.paklog.cross.docking.operations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Cross Docking Operations
 *
 * Direct transfer workflows and load consolidation
 *
 * @author Paklog Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableKafka
@EnableMongoAuditing
public class CrossDockingOperationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrossDockingOperationsApplication.class, args);
    }
}