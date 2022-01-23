package com.rlchat.server.service.dto;

import com.rlchat.server.domain.Message;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {

    private String message;
    private long toUser;
    private String toUserName;
    private long fromUser;
    private String fromUserName;
    private Long messageObjectId;

    public static MessageDTO map(Message message) {
        return MessageDTO.builder()
                .message(message.getMessage())
                .toUser(message.getUserTo())
                .toUserName(message.getUserToName())
                .fromUserName(message.getUserFromName())
                .fromUser(message.getUserFrom())
                .build();
    }

}
