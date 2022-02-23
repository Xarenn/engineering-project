package com.rlchat.server;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Slf4j
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

    // Usuwamy tego beana w calosci czyli od linijki 26 do 31 - tak aby uzywac lokalnego srodowiska.
    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "real-time-chat-kafka:9092");
        return new KafkaAdmin(configs);
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
