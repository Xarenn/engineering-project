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
    private long fromUser;
    private Long messageObjectId;

}
