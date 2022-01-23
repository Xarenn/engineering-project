package com.rlchat.server.service.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO implements Serializable {

    private String message;
    private long toUser;
    private String toUserName;
    private long fromUser;
    private String fromUserName;
    private Long messageObjectId;

}
