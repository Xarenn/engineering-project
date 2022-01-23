package com.rlchat.server.service.dto;

import com.rlchat.server.domain.MessageObject;
import lombok.*;
import org.postgresql.util.Base64;

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
    private String toUserName;
    private String fromUserName;
    private String b64;
    private long messageObjectId;

    public static MessageObjectDTO map(MessageObject messageObject) {
        String b64 = Base64.encodeBytes((messageObject.getFromUser().getId()
                + messageObject.getFromUser().getName()
                + messageObject.getId() + messageObject.getToUser()).getBytes());
        return MessageObjectDTO.builder()
                .toUser(messageObject.getToUser())
                .toUserName(messageObject.getToUserName())
                .fromUser(messageObject.getFromUser().getId())
                .fromUserName(messageObject.getFromUser().getName())
                .lastMessage(messageObject.getLastMessage())
                .lastMessageDate(messageObject.getLastMessageDate())
                .b64(b64)
                .messageObjectId(messageObject.getId())
                .build();
    }

}
