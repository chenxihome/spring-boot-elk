package com.cx.log;

import com.cx.log.common.config.KafkaProperties;
import com.cx.log.kafka.KafkaConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootLogbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLogbackApplication.class, args);
        KafkaConsumer consumerThread = new KafkaConsumer(KafkaProperties.topic);
        consumerThread.start();
    }
}
