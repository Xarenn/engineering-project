package com.rlchat.server.controllers;

import com.rlchat.server.controllers.util.KafkaMetricsReceiver;
import com.rlchat.server.security.SpringSecurityConfig;
import com.rlchat.server.service.dto.KafkaMetricsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaMetricsReceiver kafkaMetricsReceiver;

    @GetMapping("/kafka/statistics")
    public Mono<List<KafkaMetricsDTO>> getMessageObjectsByUser() {
        return Mono.justOrEmpty(kafkaMetricsReceiver.getKafkaMetrics())
                .onErrorMap(SpringSecurityConfig::mapError);
    }

}
