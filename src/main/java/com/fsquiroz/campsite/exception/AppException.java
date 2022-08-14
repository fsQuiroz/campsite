package com.fsquiroz.campsite.exception;

import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
public abstract class AppException extends RuntimeException {

    private Instant timestamp = Instant.now();

    private ErrorCode code;

    private Map<String, Object> meta;

    protected AppException(ErrorCode code, Map<String, Object> meta, String message) {
        this(code, meta, message, null);
    }

    protected AppException(ErrorCode code, Map<String, Object> meta, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.meta = meta;
    }
}
