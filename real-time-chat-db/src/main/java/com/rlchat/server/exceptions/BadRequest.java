package com.rlchat.server.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequest extends ServerException {

    private static String errorCodeMessage = "BAD_REQ";

    private static Integer code = 400;

    private static String message = "illegal argument or empty";

    public BadRequest() {
        super(code, message, errorCodeMessage);
    }

    public BadRequest(String message) {
        super(code, message, errorCodeMessage);
    }
}