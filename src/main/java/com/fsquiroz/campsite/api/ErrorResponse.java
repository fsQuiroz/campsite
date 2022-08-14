package com.fsquiroz.campsite.api;

import com.fsquiroz.campsite.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private Instant timestamp;

    private Integer status;

    private String error;

    private String message;

    private String path;

    private Map<String, Object> meta;

    private ErrorCode code;
}
