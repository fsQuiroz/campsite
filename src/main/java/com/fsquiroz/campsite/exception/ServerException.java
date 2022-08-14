package com.fsquiroz.campsite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerException extends AppException {

    public ServerException(String message) {
        this(message, null);
    }

    public ServerException(String message, Throwable cause) {
        super(ErrorCode.ISE, null, message, cause);
    }
}
