package com.rlchat.server;

import com.rlchat.server.security.properties.ServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(ServerProperties.class)
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }
}
