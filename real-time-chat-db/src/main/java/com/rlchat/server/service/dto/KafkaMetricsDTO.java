package com.rlchat.server.service.dto;

import lombok.*;
import org.apache.kafka.common.MetricName;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class KafkaMetricsDTO {

    private String consumerName;
    private Map<String, Object> metrics;

}
