package com.rlchat.server.exceptions.util;


import com.rlchat.server.exceptions.ServerException;

import java.io.Serializable;

public class ErrorUtil {

    public static class ErrorOutputMessage implements Serializable {

        public Integer code;

        public String message;

        public String errorCodeMessage;

        public ErrorOutputMessage(ServerException genericException) {
            this.code = genericException.getCode();
            this.message = genericException.getMessage();
            this.errorCodeMessage = genericException.getErrorCodeMessage();
        }

    }

    public static ErrorOutputMessage errorToMessage(ServerException genericException) {
        return new ErrorOutputMessage(genericException);
    }
}