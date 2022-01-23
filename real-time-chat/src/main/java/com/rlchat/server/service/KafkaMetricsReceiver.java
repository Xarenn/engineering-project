package com.rlchat.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMetricsReceiver {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public void getKafkaMetrics() {
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
                .getListenerContainers()) {
            Map<String, Map<MetricName, ? extends Metric>> metrics = messageListenerContainer.metrics();
            metrics.forEach((clientid, metricMap) -> {
                System.out.println("------------------------For client id : " + clientid);
                metricMap.forEach((metricName, metricValue) -> {
                    //if(metricName.name().contains("lag"))
                    System.out.println("------------Metric name: " + metricName.name() + "-----------Metric value: " + metricValue.metricValue());
                });
            });
        }
    }

}