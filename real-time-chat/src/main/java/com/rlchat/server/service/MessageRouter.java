package com.rlchat.server.service;

import com.rlchat.server.service.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.internals.KafkaConsumerMetrics;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Component
@RequiredArgsConstructor
public class MessageRouter {

    private final KafkaTemplate<String, MessageDTO> kafkaTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final KafkaMetricsReceiver kafkaMetricsReceiver;

    @GetMapping("/test-send")
    public ResponseEntity<String> sendMessageRequest() {
        kafkaTemplate.send("messages-sent",
                MessageDTO.builder().message("A chujnia").fromUser(1).toUser(2).messageObjectId(3L).build());
        kafkaMetricsReceiver.getKafkaMetrics();
        return ResponseEntity.ok("siema");
    }


    @MessageMapping("/room/message/{room}")
    @SendTo("/topic/messages/get")
    public MessageDTO greet(@DestinationVariable String room, MessageDTO message) throws Exception {
        messagingTemplate.convertAndSend("/topic/messages/get", MessageDTO.builder()
                .message(message.getMessage())
                .fromUser(message.getFromUser())
                .toUser(message.getToUser())
                .messageObjectId(message.getMessageObjectId())
                        .toUserName(message.getToUserName())
                        .fromUserName(message.getFromUserName())
                .build());
        log.info("MESSAGE ARRIVED ROOM - {} FROM - {} TO - {} MSG - {}", room, message.getFromUser(), message.getToUser(), message.getMessage());
        return MessageDTO.builder()
                .message(message.getMessage())
                .fromUser(message.getFromUser())
                .toUser(message.getToUser())
                .toUserName(message.getToUserName())
                .fromUserName(message.getFromUserName())
                .messageObjectId(message.getMessageObjectId())
                .build();
    }

    @MessageMapping("/topic/messages/get")
    public void getMessages(MessageDTO message) throws Exception {
        log.info("MESSAGE ARRIVED TO GET FROM - {} TO - {} MSG - {}", message.getFromUser(), message.getToUser(), message.getMessage());
    }

}
