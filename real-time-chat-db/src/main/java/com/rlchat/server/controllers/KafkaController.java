package com.rlchat.server.controllers;

import com.rlchat.server.controllers.util.KafkaMetricsReceiver;
import com.rlchat.server.security.SpringSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaMetricsReceiver kafkaMetricsReceiver;

    @GetMapping("/kafka/statistics")
    public Mono<Map<String, Map<MetricName, ? extends Metric>>> getMessageObjectsByUser() {
        return Mono.justOrEmpty(kafkaMetricsReceiver.getKafkaMetrics())
                .onErrorMap(SpringSecurityConfig::mapError);
    }

}
