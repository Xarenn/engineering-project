package com.rlchat.server.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class ServerException extends RuntimeException {

    private Integer code;

    private String message;

    private String errorCodeMessage;


}