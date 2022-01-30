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

import java.security.Principal;

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
    public MessageDTO prepareAndSendMessage(@DestinationVariable String room, MessageDTO message) {
        final MessageDTO messageDTO = MessageDTO.builder()
                .message(message.getMessage())
                .fromUser(message.getFromUser())
                .toUser(message.getToUser())
                .messageObjectId(message.getMessageObjectId())
                .toUserName(message.getToUserName())
                .fromUserName(message.getFromUserName())
                .build();

        kafkaTemplate.send("messages-sent", messageDTO);
        log.info("MESSAGE ARRIVED ROOM - {} FROM - {} TO - {} MSG - {}", room,
                 message.getFromUser(), message.getToUser(), message.getMessage());
        messagingTemplate.convertAndSend("/topic/messages/get/" + message.getMessageObjectId(),
                                         message);
        return messageDTO;
    }
}
