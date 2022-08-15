package com.fsquiroz.campsite.exception;

import com.fsquiroz.campsite.persistence.entity.Reservation;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BadRequestExceptionUnitTest {

    @Test
    public void givenMissingParamWhenNewBadRequestThenValidException() {
        String param = "param";
        AppException exception = BadRequestException.byMissingParam(param);

        assertThat(exception)
                .isNotNull()
                .hasMessage("Missing param")
                .hasFieldOrPropertyWithValue("code", ErrorCode.BR_MISSING_PARAM)
                .hasFieldOrPropertyWithValue("meta.param", param);
    }

    @Test
    public void givenInvalidIdWhenNewBadRequestThenValidException() {
        AppException exception = BadRequestException.byInvalidId();

        assertThat(exception)
                .isNotNull()
                .hasMessage("Invalid id value")
                .hasFieldOrPropertyWithValue("code", ErrorCode.BR_INVALID_ID)
                .hasFieldOrPropertyWithValue("meta", null);
    }

    @Test
    public void givenTextTooLongWhenNewBadRequestThenValidException() {
        String param = "param";
        String text = "abcdefghijklmnopqrstuvwxyz";
        int maxSize = 24;

        AppException exception = BadRequestException.byTextTooLong(param, text, maxSize);

        assertThat(exception)
                .isNotNull()
                .hasMessage("Text is too long")
                .hasFieldOrPropertyWithValue("code", ErrorCode.BR_TEXT_TOO_LONG)
                .hasFieldOrPropertyWithValue("meta.param", param)
                .hasFieldOrPropertyWithValue("meta.text", text)
                .hasFieldOrPropertyWithValue("meta.textLength", 26)
                .hasFieldOrPropertyWithValue("meta.maxTextLength", maxSize);
    }

    @Test
    public void givenModifyingCancelledWhenNewBadRequestThenValidException() {
        Reservation reservation = new Reservation();
        reservation.setDeleted(Instant.parse("2022-08-12T13:50:59.168Z"));

        AppException exception = BadRequestException.byModifyingCancelled(reservation);

        assertThat(exception)
                .isNotNull()
                .hasMessage("Reservation has already been cancelled, can not be modified")
                .hasFieldOrPropertyWithValue("code", ErrorCode.BR_MODIFYING_CANCELLED)
                .extracting("meta.cancellationDate")
                .asInstanceOf(InstanceOfAssertFactories.INSTANT)
                .isCloseTo(
                        Instant.parse("2022-08-12T13:50:59.168Z"),
                        new TemporalUnitWithinOffset(1, ChronoUnit.MILLIS)
                );
    }

    @Test
    public void givenInvalidRangeWhenNewBadRequestThenValidException() {
        String startParam = "arrival";
        String endParam = "departure";
        LocalDate arrival = LocalDate.parse("2022-08-22");
        LocalDate departure = LocalDate.parse("2022-08-20");

        AppException exception = BadRequestException.byInvalidRange(startParam, arrival, endParam, departure);

        assertThat(exception)
                .isNotNull()
                .hasMessage("Invalid range. Start date can not be after end date")
                .hasFieldOrPropertyWithValue("code", ErrorCode.BR_INVALID_RANGE);

        assertThat(exception.getMeta())
                .extracting(startParam)
                .asInstanceOf(InstanceOfAssertFactories.LOCAL_DATE)
                .isEqualTo(LocalDate.parse("2022-08-22"));

        assertThat(exception.getMeta())
                .extracting(endParam)
                .asInstanceOf(InstanceOfAssertFactories.LOCAL_DATE)
                .isEqualTo(LocalDate.parse("2022-08-20"));
    }

    @Test
    public void givenRangeNotAllowedTooLowWhenNewBadRequestThenValidException() {
        LocalDate arrival = LocalDate.parse("2022-08-20");
        LocalDate departure = LocalDate.parse("2022-08-20");
        int minStay = 1;
        int maxStay = 3;

        AppException exception = BadRequestException.byRangeNotAllowedTooLow(arrival, departure, minStay, maxStay);

        assertThat(exception)
                .isNotNull()
                .hasMessage("Range not allowed. It does not reach minimum stay days")
                .hasFieldOrPropertyWithValue("code", ErrorCode.BR_RANGE_NOT_ALLOWED_TOO_LOW)
                .hasFieldOrPropertyWithValue("meta.minStay", minStay)
                .hasFieldOrPropertyWithValue("meta.maxStay", maxStay)
                .hasFieldOrPropertyWithValue("meta.actualStay", 0L);

        assertThat(exception.getMeta())
                .extracting("arrival")
                .asInstanceOf(InstanceOfAssertFactories.LOCAL_DATE)
                .isEqualTo(LocalDate.parse("2022-08-20"));

        assertThat(exception.getMeta())
                .extracting("departure")
                .asInstanceOf(InstanceOfAssertFactories.LOCAL_DATE)
                .isEqualTo(LocalDate.parse("2022-08-20"));
    }

    @Test
    public void givenRangeNotAllowedTooHighWhenNewBadRequestThenValidException() {
        LocalDate arrival = LocalDate.parse("2022-08-20");
        LocalDate departure = LocalDate.parse("2022-08-30");
        int minStay = 1;
        int maxStay = 3;

        AppException exception = BadRequestException.byRangeNotAllowedTooHigh(arrival, departure, minStay, maxStay);

        assertThat(exception)
                .isNotNull()
                .hasMessage("Range not allowed. It surpass maximum stay days")
                .hasFieldOrPropertyWithValue("code", ErrorCode.BR_RANGE_NOT_ALLOWED_TOO_HIGH)
                .hasFieldOrPropertyWithValue("meta.minStay", minStay)
                .hasFieldOrPropertyWithValue("meta.maxStay", maxStay)
                .hasFieldOrPropertyWithValue("meta.actualStay", 10L);

        assertThat(exception.getMeta())
                .extracting("arrival")
                .asInstanceOf(InstanceOfAssertFactories.LOCAL_DATE)
                .isEqualTo(LocalDate.parse("2022-08-20"));

        assertThat(exception.getMeta())
                .extracting("departure")
                .asInstanceOf(InstanceOfAssertFactories.LOCAL_DATE)
                .isEqualTo(LocalDate.parse("2022-08-30"));
    }

    @Test
    public void givenMalformedBodyWhenNewBadRequestThenValidException() {
        HttpMessageNotReadableException cause = givenMalformedBody();

        AppException exception = BadRequestException.byMalformedBody(cause);

        assertThat(exception)
                .isNotNull()
                .hasMessage("Unable to parse body content")
                .hasFieldOrPropertyWithValue("code", ErrorCode.BR_MALFORMED_BODY)
                .hasCauseInstanceOf(HttpMessageNotReadableException.class);
    }

    @Test
    public void givenMalformedParamWithKnownTypeWhenNewBadRequestThenValidException() {
        String param = "name";
        Object value = 1L;
        Class<String> requiredType = String.class;
        String message = "Can not cast Long to String";

        MethodArgumentTypeMismatchException cause = givenMalformedParam(param, value, requiredType, message);

        AppException exception = BadRequestException.byMalformedParam(cause);

        assertThat(exception)
                .isNotNull()
                .hasMessage("Unable to parse param")
                .hasFieldOrPropertyWithValue("code", ErrorCode.BR_MALFORMED_PARAM)
                .hasFieldOrPropertyWithValue("meta.param", param)
                .hasFieldOrPropertyWithValue("meta.value", value)
                .hasFieldOrPropertyWithValue("meta.requiredType", "String")
                .hasFieldOrPropertyWithValue("meta.originalMessage", message)
                .hasCauseInstanceOf(MethodArgumentTypeMismatchException.class);
    }

    @Test
    public void givenMalformedParamWithUnknownTypeWhenNewBadRequestThenValidException() {
        String param = "name";
        Object value = 1L;
        Class<String> requiredType = null;
        String message = "Can not cast Long to String";

        MethodArgumentTypeMismatchException cause = givenMalformedParam(param, value, requiredType, message);

        AppException exception = BadRequestException.byMalformedParam(cause);

        assertThat(exception)
                .isNotNull()
                .hasMessage("Unable to parse param")
                .hasFieldOrPropertyWithValue("code", ErrorCode.BR_MALFORMED_PARAM)
                .hasFieldOrPropertyWithValue("meta.param", param)
                .hasFieldOrPropertyWithValue("meta.value", value)
                .hasFieldOrPropertyWithValue("meta.requiredType", "UNKNOWN")
                .hasFieldOrPropertyWithValue("meta.originalMessage", message)
                .hasCauseInstanceOf(MethodArgumentTypeMismatchException.class);
    }

    @Test
    public void givenArrivalTooEarlyWhenNewBadRequestThenValidException() {
        LocalDate arrival = LocalDate.parse("2022-08-10");
        LocalDate minArrival = LocalDate.parse("2022-08-11");

        AppException exception = BadRequestException.byArrivalTooEarly(arrival, minArrival);

        assertThat(exception)
                .isNotNull()
                .hasMessage("Arrival is too early")
                .hasFieldOrPropertyWithValue("code", ErrorCode.BR_ARRIVAL_TOO_EARLY);

        assertThat(exception.getMeta())
                .extracting("arrival")
                .asInstanceOf(InstanceOfAssertFactories.LOCAL_DATE)
                .isEqualTo(LocalDate.parse("2022-08-10"));

        assertThat(exception.getMeta())
                .extracting("minArrival")
                .asInstanceOf(InstanceOfAssertFactories.LOCAL_DATE)
                .isEqualTo(LocalDate.parse("2022-08-11"));
    }

    @Test
    public void givenNoMoreReservationAvailableWhenNewBadRequestThenValidException() {
        LocalDate arrival = LocalDate.parse("2022-08-10");
        LocalDate departure = LocalDate.parse("2022-08-12");

        AppException exception = BadRequestException.byNoMoreReservationAvailable(arrival, departure);

        assertThat(exception)
                .isNotNull()
                .hasMessage("There is no more reservations available for the provided date range")
                .hasFieldOrPropertyWithValue("code", ErrorCode.BR_NO_MORE_RESERVATION_AVAILABLE);

        assertThat(exception.getMeta())
                .extracting("arrival")
                .asInstanceOf(InstanceOfAssertFactories.LOCAL_DATE)
                .isEqualTo(LocalDate.parse("2022-08-10"));

        assertThat(exception.getMeta())
                .extracting("departure")
                .asInstanceOf(InstanceOfAssertFactories.LOCAL_DATE)
                .isEqualTo(LocalDate.parse("2022-08-12"));
    }

    private HttpMessageNotReadableException givenMalformedBody() {
        return Mockito.mock(HttpMessageNotReadableException.class);
    }

    private MethodArgumentTypeMismatchException givenMalformedParam(String param, Object value, Class<?> requiredType, String message) {
        MethodArgumentTypeMismatchException e = Mockito.mock(MethodArgumentTypeMismatchException.class);
        Mockito.when(e.getName()).thenReturn(param);
        Mockito.when(e.getValue()).thenReturn(value);
        Mockito.doReturn(requiredType).when(e).getRequiredType();
        Mockito.when(e.getMessage()).thenReturn(message);
        return e;
    }
}
