package com.rlchat.server.controllers;

import com.rlchat.server.persistence.MessageManagement;
import com.rlchat.server.security.SpringSecurityConfig;
import com.rlchat.server.service.dto.MessageDTO;
import com.rlchat.server.service.dto.MessageObjectDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class MessageController {

    private final MessageManagement messageManagement;

    @GetMapping("/messages/object")
    public Mono<Page<MessageObjectDTO>> getMessageObjectsByUser(Principal principal, @RequestParam("page") Integer page) {
        return messageManagement.getMessageObjectsByUser(principal, page)
                .onErrorMap(SpringSecurityConfig::mapError);
    }

    @GetMapping("/messages/data/{messageObjectId}")
    public Mono<Page<MessageDTO>> getMessagesByIdAndPage(Principal principal,
                                                         @PathVariable("messageObjectId") Integer messageObjectId,
                                                         @RequestParam("page") Integer page) {
        return messageManagement.getMessagesByIdAndPage(principal, messageObjectId, page)
                .onErrorMap(SpringSecurityConfig::mapError);
    }

}
