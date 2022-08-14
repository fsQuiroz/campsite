package com.fsquiroz.campsite.exception;

import com.fsquiroz.campsite.persistence.entity.Reservation;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends AppException {

    private static final String PARAM_KEY = "param";
    private static final String VALUE_KEY = "value";
    private static final String REQUIRED_TYPE_KEY = "requiredType";
    private static final String ORIGINAL_MESSAGE_KEY = "originalMessage";
    private static final String TEXT_KEY = "text";
    private static final String TEXT_LENGTH_KEY = "textLength";
    private static final String MAX_TEXT_LENGTH_KEY = "maxTextLength";
    private static final String CANCELLATION_DATE_KEY = "cancellationDate";
    private static final String ARRIVAL_KEY = "arrival";
    private static final String DEPARTURE_KEY = "departure";
    private static final String MIN_STAY_KEY = "minStay";
    private static final String MAX_STAY_KEY = "maxStay";

    private static final String MISSING_PARAM_MSG = "Missing param";
    private static final String INVALID_ID = "Invalid id value";
    private static final String TEXT_TOO_LONG = "Text is too long";
    private static final String RESERVATION_CANCELLED = "Reservation has already been cancelled, can not be modified";
    private static final String INVALID_RANGE = "Invalid range. Arrival con no be after departure";
    private static final String RANGE_NOT_ALLOWED_TOO_LOW = "Range not allowed. It does not reach minimum stay days";
    private static final String RANGE_NOT_ALLOWED_TOO_HIGH = "Range not allowed. It surpass maximum stay days";
    private static final String MALFORMED_BODY = "Unable to parse body content";
    private static final String MALFORMED_PARAM = "Unable to parse param";

    private BadRequestException(ErrorCode code, Map<String, Object> meta, String message) {
        this(code, meta, message, null);
    }

    private BadRequestException(ErrorCode code, Map<String, Object> meta, String message, Throwable cause) {
        super(code, meta, message, cause);
    }

    public static BadRequestException byMissingParam(@NonNull String param) {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put(PARAM_KEY, param);
        return new BadRequestException(ErrorCode.BR_MISSING_PARAM, meta, MISSING_PARAM_MSG);
    }

    public static BadRequestException byInvalidId() {
        return new BadRequestException(ErrorCode.BR_INVALID_ID, null, INVALID_ID);
    }

    public static BadRequestException byTextTooLong(@NonNull String param, @NonNull String text, int maxSize) {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put(PARAM_KEY, param);
        meta.put(TEXT_KEY, text);
        meta.put(TEXT_LENGTH_KEY, text.length());
        meta.put(MAX_TEXT_LENGTH_KEY, maxSize);
        return new BadRequestException(ErrorCode.BR_TEXT_TOO_LONG, meta, TEXT_TOO_LONG);
    }

    public static BadRequestException byModifyingCancelled(@NonNull Reservation reservation) {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put(CANCELLATION_DATE_KEY, reservation.getDeleted());
        return new BadRequestException(ErrorCode.BR_MODIFYING_CANCELLED, meta, RESERVATION_CANCELLED);
    }

    public static BadRequestException byInvalidRange(@NonNull LocalDate arrival, @NonNull LocalDate departure) {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put(ARRIVAL_KEY, arrival);
        meta.put(DEPARTURE_KEY, departure);
        return new BadRequestException(ErrorCode.BR_INVALID_RANGE, meta, INVALID_RANGE);
    }

    public static BadRequestException byRangeNotAllowedTooLow(@NonNull LocalDate arrival, @NonNull LocalDate departure, int minStay, int maxStay) {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put(ARRIVAL_KEY, arrival);
        meta.put(DEPARTURE_KEY, departure);
        meta.put(MIN_STAY_KEY, minStay);
        meta.put(MAX_STAY_KEY, maxStay);
        return new BadRequestException(ErrorCode.BR_RANGE_NOT_ALLOWED_TOO_LOW, meta, RANGE_NOT_ALLOWED_TOO_LOW);
    }

    public static BadRequestException byRangeNotAllowedTooHigh(@NonNull LocalDate arrival, @NonNull LocalDate departure, int minStay, int maxStay) {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put(ARRIVAL_KEY, arrival);
        meta.put(DEPARTURE_KEY, departure);
        meta.put(MIN_STAY_KEY, minStay);
        meta.put(MAX_STAY_KEY, maxStay);
        return new BadRequestException(ErrorCode.BR_RANGE_NOT_ALLOWED_TOO_HIGH, meta, RANGE_NOT_ALLOWED_TOO_HIGH);
    }

    public static BadRequestException byMalformedParam(MethodArgumentTypeMismatchException e) {
        Map<String, Object> meta = null;
        if (e != null) {
            meta = new LinkedHashMap<>();
            meta.put(PARAM_KEY, e.getName());
            meta.put(VALUE_KEY, e.getValue());
            meta.put(REQUIRED_TYPE_KEY, e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "UNKNOWN");
            meta.put(ORIGINAL_MESSAGE_KEY, e.getMessage());
        }
        return new BadRequestException(ErrorCode.BR_MALFORMED_PARAM, meta, MALFORMED_PARAM, e);
    }

    public static BadRequestException byMalformedBody(HttpMessageNotReadableException cause) {
        return new BadRequestException(ErrorCode.BR_MALFORMED_BODY, null, MALFORMED_BODY, cause);
    }
}
