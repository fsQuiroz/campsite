package com.fsquiroz.campsite.mapper;

import com.fsquiroz.campsite.api.ErrorResponse;
import com.fsquiroz.campsite.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AppExceptionMapper extends Mapper<AppException, ErrorResponse> {

    @Override
    public ErrorResponse map(AppException exception) {
        return ErrorResponse.builder()
                .timestamp(exception.getTimestamp())
                .message(exception.getMessage())
                .meta(exception.getMeta())
                .code(exception.getCode())
                .build();
    }

    public ErrorResponse map(AppException exception, HttpStatus status) {
        ErrorResponse response = map(exception);
        response.setStatus(status != null ? status.value() : null);
        response.setError(status != null ? status.getReasonPhrase() : null);
        return response;
    }
}
