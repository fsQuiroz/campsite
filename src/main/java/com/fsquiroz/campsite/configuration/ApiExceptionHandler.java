package com.fsquiroz.campsite.configuration;

import com.fsquiroz.campsite.exception.AppException;
import com.fsquiroz.campsite.exception.BadRequestException;
import com.fsquiroz.campsite.exception.ServerException;
import com.fsquiroz.campsite.mapper.AppExceptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String ISE_MESSAGE = "There has been an unexpected error";
    private final AppExceptionMapper mapper;

    public ApiExceptionHandler(AppExceptionMapper mapper) {
        this.mapper = mapper;
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> appException(WebRequest request, AppException ae) {
        if (log.isDebugEnabled()) {
            log.debug(ae.getMessage(), ae);
        }
        return parseException(ae);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn(e.getMessage(), e);
        return parseException(BadRequestException.byMalformedBody(e));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> typeMismatchException(WebRequest request, MethodArgumentTypeMismatchException e) {
        log.warn(e.getMessage(), e);
        return parseException(BadRequestException.byMalformedParam(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> genericException(WebRequest request, Exception e) {
        log.error(e.getMessage(), e);
        return parseException(new ServerException(ISE_MESSAGE, e));
    }

    private ResponseEntity<Object> parseException(AppException ae) {
        ResponseStatus responseStatus = AnnotatedElementUtils.findMergedAnnotation(ae.getClass(), ResponseStatus.class);
        HttpStatus status = responseStatus != null ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(
                mapper.map(ae, status),
                status
        );
    }
}
