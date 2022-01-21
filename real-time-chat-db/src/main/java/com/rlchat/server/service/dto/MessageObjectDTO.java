package com.rlchat.server.service.dto;

import com.rlchat.server.domain.MessageObject;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageObjectDTO {

    private long fromUser;
    private long toUser;
    private String lastMessage;
    private LocalDateTime lastMessageDate;

    public static MessageObjectDTO map(MessageObject messageObject) {
        return MessageObjectDTO.builder()
                .toUser(messageObject.getToUser())
                .fromUser(messageObject.getFromUser().getId())
                .lastMessage(messageObject.getLastMessage())
                .lastMessageDate(messageObject.getLastMessageDate())
                .build();
    }

}
