package com.fsquiroz.campsite.mapper;

import com.fsquiroz.campsite.api.ErrorResponse;
import com.fsquiroz.campsite.exception.AppException;
import com.fsquiroz.campsite.exception.ErrorCode;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

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
    public void givenCompleteAppExceptionWhenMappingThenCompleteMapped() {
        ErrorCode code = ErrorCode.ISE;
        String message = "There has been an unexpected error";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        AppException exception = givenAppException(code, message, null, null);
        AppExceptionMapper mapper = new AppExceptionMapper();

        ErrorResponse response = mapper.map(exception, status);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("status", 500)
                .hasFieldOrPropertyWithValue("error", "Internal Server Error")
                .hasFieldOrPropertyWithValue("message", message)
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

    private static class TestException extends AppException {
        TestException(ErrorCode code, Map<String, Object> meta, String message) {
            super(code, meta, message);
        }
    }
}
