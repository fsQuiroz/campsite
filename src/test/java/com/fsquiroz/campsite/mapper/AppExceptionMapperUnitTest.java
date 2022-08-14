package com.fsquiroz.campsite.mapper;

import com.fsquiroz.campsite.api.ErrorResponse;
import com.fsquiroz.campsite.exception.AppException;
import com.fsquiroz.campsite.exception.ErrorCode;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AppExceptionMapperUnitTest {

    @Test
    public void givenSimpleAppExceptionWhenMappingThenSimpleMapped() {
        ErrorCode code = ErrorCode.NF_BY_ID;
        String message = "Not found by id";
        String param = "id";
        Long id = 100L;

        AppException exception = givenAppException(code, message, param, id);
        AppExceptionMapper mapper = new AppExceptionMapper();

        ErrorResponse response = mapper.map(exception);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("status", null)
                .hasFieldOrPropertyWithValue("error", null)
                .hasFieldOrPropertyWithValue("message", message)
                .hasFieldOrPropertyWithValue("path", null)
                .hasFieldOrPropertyWithValue("meta.id", id)
                .hasFieldOrPropertyWithValue("code", code);
    }

    @Test
    public void givenSimpleAppExceptionWhenMappingThenCorrectTimestamp() {
        ErrorCode code = ErrorCode.NF_BY_ID;
        String message = "Not found by id";
        String param = "id";
        Long id = 100L;

        AppException exception = givenAppException(code, message, param, id);
        AppExceptionMapper mapper = new AppExceptionMapper();

        ErrorResponse response = mapper.map(exception);

        assertThat(response)
                .isNotNull()
                .hasFieldOrProperty("timestamp")
                .extracting("timestamp")
                .asInstanceOf(InstanceOfAssertFactories.INSTANT)
                .isCloseTo(
                        Instant.now(),
                        new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS)
                );
    }

    @Test
    public void givenAppExceptionWithHttpStatusWhenMappingThenMappedWithStatus() {
        ErrorCode code = ErrorCode.BR_MISSING_PARAM;
        String message = "Missing param";
        String param = "param";
        String paramName = "name";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        AppException exception = givenAppException(code, message, param, paramName);
        AppExceptionMapper mapper = new AppExceptionMapper();

        ErrorResponse response = mapper.map(exception, status, null);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("status", 400)
                .hasFieldOrPropertyWithValue("error", "Bad Request")
                .hasFieldOrPropertyWithValue("message", message)
                .hasFieldOrPropertyWithValue("path", null)
                .hasFieldOrPropertyWithValue("meta.param", paramName)
                .hasFieldOrPropertyWithValue("code", code);
    }

    @Test
    public void givenAppExceptionWithWebRequestWhenMappingThenMappedWithPath() {
        ErrorCode code = ErrorCode.BR_INVALID_ID;
        String message = "Invalid id";
        WebRequest request = givenWebRequest("/reservations/abc");

        AppException exception = givenAppException(code, message, null, null);
        AppExceptionMapper mapper = new AppExceptionMapper();

        ErrorResponse response = mapper.map(exception, null, request);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("status", null)
                .hasFieldOrPropertyWithValue("error", null)
                .hasFieldOrPropertyWithValue("message", message)
                .hasFieldOrPropertyWithValue("path", "/reservations/abc")
                .hasFieldOrPropertyWithValue("meta", null)
                .hasFieldOrPropertyWithValue("code", code);
    }

    @Test
    public void givenCompleteAppExceptionWhenMappingThenCompleteMapped() {
        ErrorCode code = ErrorCode.ISE;
        String message = "There has been an unexpected error";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        WebRequest request = givenWebRequest("/reservations/error");

        AppException exception = givenAppException(code, message, null, null);
        AppExceptionMapper mapper = new AppExceptionMapper();

        ErrorResponse response = mapper.map(exception, status, request);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("status", 500)
                .hasFieldOrPropertyWithValue("error", "Internal Server Error")
                .hasFieldOrPropertyWithValue("message", message)
                .hasFieldOrPropertyWithValue("path", "/reservations/error")
                .hasFieldOrPropertyWithValue("meta", null)
                .hasFieldOrPropertyWithValue("code", code);
    }

    private AppException givenAppException(ErrorCode code, String message, String key, Object value) {
        Map<String, Object> meta = null;
        if (StringUtils.isNotBlank(key)) {
            meta = new LinkedHashMap<>();
            meta.put(key, value);
        }
        return new TestException(code, meta, message);
    }

    private WebRequest givenWebRequest(String contextPath) {
        WebRequest request = null;
        if (StringUtils.isNotBlank(contextPath)) {
            request = Mockito.mock(WebRequest.class);
            Mockito.when(request.getContextPath()).thenReturn(contextPath);
        }
        return request;
    }

    private static class TestException extends AppException {
        TestException(ErrorCode code, Map<String, Object> meta, String message) {
            super(code, meta, message);
        }
    }
}
