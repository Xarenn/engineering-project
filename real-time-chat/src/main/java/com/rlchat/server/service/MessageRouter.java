package com.rlchat.server.service;

import com.rlchat.server.service.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RestController
@Component
@RequiredArgsConstructor
public class MessageRouter {

    private final KafkaTemplate<String, MessageDTO> kafkaTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @Bean
    public RouterFunction<ServerResponse> start() {
        return route()
                .GET("send", accept(APPLICATION_JSON),
                        this::sendMessage)
                .build();
    }

    @GetMapping("/test-send")
    public ResponseEntity<String> sendMessageRequest() {
        kafkaTemplate.send("messages-sent", MessageDTO.builder().message("A chujnia").fromUser(1).toUser(2).messageObjectId(3L).build());
        return ResponseEntity.ok("siema");
    }

    public Mono<ServerResponse> sendMessage(ServerRequest serverRequest) {
        kafkaTemplate.send("messages-sent", MessageDTO.builder().message("Siema").fromUser(2).toUser(1).messageObjectId(3L).build());
        return ok()
                .contentType(APPLICATION_JSON)
                .body(Mono.just(serverRequest.pathVariable("id")), String.class);
    }

    @MessageMapping("/room/greeting/{room}")
    public void greet(@DestinationVariable String room, MessageDTO message) throws Exception {
        messagingTemplate.convertAndSend("/room/messages/"+room, MessageDTO.builder()
                .message(message.getMessage())
                .fromUser(message.getFromUser())
                .toUser(message.getToUser())
                .messageObjectId(message.getMessageObjectId())
                .build());
    }

}
