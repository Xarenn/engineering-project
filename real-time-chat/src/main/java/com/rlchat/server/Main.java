package com.rlchat.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;


@Slf4j
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

    @Bean
    public NewTopic messagesSent() {
        return TopicBuilder.name("messages-sent")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic messagesSaveProduce() {
        return TopicBuilder.name("messages-produce")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
