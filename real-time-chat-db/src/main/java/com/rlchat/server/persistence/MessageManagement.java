package com.rlchat.server.persistence;

import com.rlchat.server.domain.Message;
import com.rlchat.server.domain.MessageObject;
import com.rlchat.server.domain.UserObject;
import com.rlchat.server.exceptions.BadRequest;
import com.rlchat.server.persistence.repositories.MessageObjectRepository;
import com.rlchat.server.persistence.repositories.MessageRepository;
import com.rlchat.server.persistence.repositories.UserObjectRepository;
import com.rlchat.server.service.dto.MessageDTO;
import com.rlchat.server.service.dto.MessageObjectDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageManagement {

    private final UserObjectRepository userObjectRepository;
    private final MessageObjectRepository messageObjectRepository;
    private final MessageRepository messageRepository;

    public Mono<Page<MessageObjectDTO>> getMessageObjectsByUser(Principal principal, Integer page) {
        final Long id = Long.parseLong(principal.getName());
        final Optional<UserObject> object = userObjectRepository.findById(id);
        if(object.isEmpty()) {
            return Mono.error(new BadRequest("User not found with id: " + id));
        }

        return Mono.justOrEmpty(messageObjectRepository
                .getAllByFromUserAndToUser(object.get(), object.get().getId(), PageRequest.of(page, 3)))
                .flatMap(messageObjects -> Mono.justOrEmpty(messageObjects.map(MessageObjectDTO::map)));
    }

    public Mono<Page<MessageDTO>> getMessagesByIdAndPage(Principal principal, Integer messageObjectId, Integer page) {
        final Long id = Long.parseLong(principal.getName());
        final Optional<UserObject> object = userObjectRepository.findById(id);
        if(object.isEmpty()) {
            return Mono.error(new BadRequest("User not found with id: " + id));
        }

        Page<Message> messages = messageRepository.getAllByMessageObject(MessageObject.builder()
                .id(messageObjectId).build(),
                PageRequest.of(page, 20));

        return Mono.justOrEmpty(messages.map(MessageDTO::map));
    }

}
