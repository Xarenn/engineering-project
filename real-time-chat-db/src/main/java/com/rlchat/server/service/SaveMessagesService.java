package com.rlchat.server.service;

import com.rlchat.server.domain.Message;
import com.rlchat.server.domain.MessageObject;
import com.rlchat.server.domain.UserObject;
import com.rlchat.server.persistence.repositories.MessageObjectRepository;
import com.rlchat.server.persistence.repositories.MessageRepository;
import com.rlchat.server.persistence.repositories.UserObjectRepository;
import com.rlchat.server.exceptions.BadRequest;
import com.rlchat.server.service.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveMessagesService {

    private final MessageRepository messageRepository;
    private final MessageObjectRepository messageObjectRepository;
    private final UserObjectRepository userObjectRepository;

    @KafkaListener(topics = "messages-sent", groupId = "0")
    public void listenMessagesFromGroup(MessageDTO message) {
        log.info("Message text {} to {} from {}", message.getMessage(), message.getFromUser(), message.getToUser());
        long messageObjectId = message.getMessageObjectId() == null ? 0 : message.getMessageObjectId();
        final Optional<MessageObject> messageObjectOptional = messageObjectRepository.findById(messageObjectId);
        if(messageObjectOptional.isEmpty()) {

            final MessageObject messageObject = MessageObject.builder()
                    .fromUser(getUserObjectById(message.getFromUser()))
                    .toUser(message.getToUser())
                    .toUserName(message.getToUserName())
                    .lastMessage(message.getMessage())
                    .lastMessageDate(LocalDateTime.now())
                    .build();

            final Message messageValue = Message.builder()
                    .message(message.getMessage())
                    .messageObject(messageObject)
                    .time(LocalDateTime.now())
                    .userFrom(message.getFromUser())
                    .userTo(message.getToUser())
                    .build();

            messageObject.setMessages(Set.of(messageValue));
            log.info("Save first message, from user {} to user {} ",
                    message.getFromUser(),
                    message.getToUser());

            messageObjectRepository.save(messageObject);
            messageValue.setMessageObject(messageObject);
            messageRepository.save(messageValue);
        }else {
            log.info("Save next message, from user {} to user {} ",
                    message.getFromUser(),
                    message.getToUser());
            saveMessageIntoMessageObject(messageObjectOptional.get(), message);
        }
    }

    private UserObject getUserObjectById(final Long id) {
        return userObjectRepository.findById(id)
                .orElseThrow(() -> new BadRequest("User with id: " + id + " not found"));
    }

    private void saveMessageIntoMessageObject(MessageObject messageObject, MessageDTO messageDTO) {
        final Message messageValue = Message.builder()
                .message(messageDTO.getMessage())
                .messageObject(messageObject)
                .time(LocalDateTime.now())
                .userTo(messageDTO.getToUser())
                .userFrom(messageDTO.getFromUser())
                .build();

        messageRepository.save(messageValue);

        messageObject.setLastMessage(messageValue.getMessage());
        messageObject.setLastMessageDate(messageValue.getTime());
        messageObjectRepository.save(messageObject);
    }

}
