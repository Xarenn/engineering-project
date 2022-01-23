package com.rlchat.server.controllers.util;

import com.rlchat.server.service.dto.KafkaMetricsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMetricsReceiver {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public List<KafkaMetricsDTO> getKafkaMetrics() {
        final List<KafkaMetricsDTO> metricsDTOS = new ArrayList<>();
        final Map<String, Map<MetricName, ? extends Metric>> metrics = kafkaListenerEndpointRegistry
                .getListenerContainers().iterator().next().metrics();

        metrics.forEach((clientid, metricMap) -> {
            final KafkaMetricsDTO kafkaMetricsDTO = KafkaMetricsDTO.builder()
                    .consumerName(clientid)
                    .metrics(metricMap.entrySet().stream().collect(
                            Collectors.toMap(
                                    key -> new Random().nextInt()+":"+key.getKey().name(), value -> value.getValue().metricValue()
                            )))
                    .build();
            System.out.println("------------------------For client id : " + clientid);
            metricMap.forEach((metricName, metricValue) -> {
                System.out.println("------------Metric name: " + metricName.name() + "-----------Metric value: " + metricValue.metricValue());

            });
            metricsDTOS.add(kafkaMetricsDTO);
        });
        return metricsDTOS;
    }
}
