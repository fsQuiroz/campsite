package com.fsquiroz.campsite.service.reservation;

import com.fsquiroz.campsite.TestUtils;
import com.fsquiroz.campsite.exception.BadRequestException;
import com.fsquiroz.campsite.service.validate.ValidateService;
import com.fsquiroz.campsite.util.Range;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReservationDateServiceUnitTest {

    private static final int MIN_STAY_DAYS = 1;
    private static final int MAX_STAY_DAYS = 3;
    private static final int MAX_DEFAULT_DAYS_TO_SEARCH = 31;
    private static final int MIN_AHEAD_ARRIVAL_DAYS = 1;
    private static final int MAX_AHEAD_ARRIVAL_DAYS = 31;

    @Test
    public void givenDefaultParamsWhenGetDefaultRangeThenStartTodayPlusMinAheadArrivalDays() {
        ReservationParams params = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();

        ReservationDateServiceImpl service = new ReservationDateServiceImpl(params, validateService);

        Range<LocalDate> defaultRangeToSearch = service.getDefaultSearchRange();

        assertThat(defaultRangeToSearch)
                .isNotNull();

        assertThat(defaultRangeToSearch.start())
                .isEqualTo(LocalDate.now().plus(MIN_AHEAD_ARRIVAL_DAYS, ChronoUnit.DAYS));
    }

    @Test
    public void givenDefaultParamsWhenGetDefaultRangeThenTodayPlusMaxDefaultDaysToSearch() {
        ReservationParams params = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();

        ReservationDateServiceImpl service = new ReservationDateServiceImpl(params, validateService);

        Range<LocalDate> defaultRangeToSearch = service.getDefaultSearchRange();

        assertThat(defaultRangeToSearch)
                .isNotNull();

        assertThat(defaultRangeToSearch.end())
                .isEqualTo(LocalDate.now().plus(MAX_DEFAULT_DAYS_TO_SEARCH, ChronoUnit.DAYS));
    }

    @Test
    public void givenDefaultParamsWhenGetValidRangeToReserveThenTodayPlusMinAheadArrivalDays() {
        ReservationParams params = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();

        ReservationDateServiceImpl service = new ReservationDateServiceImpl(params, validateService);

        Range<LocalDate> validRangeToReserve = service.getValidRangeToReserve();

        assertThat(validRangeToReserve)
                .isNotNull();

        assertThat(validRangeToReserve.start())
                .isEqualTo(LocalDate.now().plus(MIN_AHEAD_ARRIVAL_DAYS, ChronoUnit.DAYS));
    }

    @Test
    public void givenDefaultParamsWhenGetValidRangeToReserveThenTodayPlusMaxAheadArrivalDaysPlusMaxStayDaysMinusOne() {
        ReservationParams params = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();

        ReservationDateServiceImpl service = new ReservationDateServiceImpl(params, validateService);

        Range<LocalDate> validRangeToReserve = service.getValidRangeToReserve();

        assertThat(validRangeToReserve)
                .isNotNull();

        assertThat(validRangeToReserve.end())
                .isEqualTo(
                        LocalDate.now()
                                .plus(MAX_AHEAD_ARRIVAL_DAYS, ChronoUnit.DAYS)
                                .plus(MAX_STAY_DAYS - 1, ChronoUnit.DAYS)
                );
    }

    @Test
    public void givenDefaultParamsAndStayRangeWithinValidRangeWhenStayRangeIsValidThenNoException() {
        Range<LocalDate> stayRange = new Range<>(LocalDate.parse("2022-08-10"), LocalDate.parse("2022-08-12"));
        Range<LocalDate> validRange = new Range<>(LocalDate.parse("2022-08-01"), LocalDate.parse("2022-08-31"));

        ReservationParams params = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();

        ReservationDateServiceImpl service = new ReservationDateServiceImpl(params, validateService);

        Exception exception = TestUtils.catchException(() -> service.stayRangeIsValid(stayRange, validRange));

        assertThat(exception)
                .isNull();
    }

    @Test
    public void givenDefaultParamsAndReservationBelowsMinStayDaysWhenStayRangeIsValidThenThrowsRangeNotRangeNotAllowedTooLow() {
        Range<LocalDate> stayRange = new Range<>(LocalDate.parse("2022-08-10"), LocalDate.parse("2022-08-10"));
        Range<LocalDate> validRange = new Range<>(LocalDate.parse("2022-08-01"), LocalDate.parse("2022-08-31"));

        ReservationParams params = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();

        ReservationDateServiceImpl service = new ReservationDateServiceImpl(params, validateService);

        Exception exception = TestUtils.catchException(() -> service.stayRangeIsValid(stayRange, validRange));

        assertThat(exception)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Range not allowed. It does not reach minimum stay days");
    }

    @Test
    public void givenDefaultParamsAndReservationExceedsMaxStayDaysWhenStayRangeIsValidThenThrowsRangeNotRangeNotAllowedTooLow() {
        Range<LocalDate> stayRange = new Range<>(LocalDate.parse("2022-08-10"), LocalDate.parse("2022-08-20"));
        Range<LocalDate> validRange = new Range<>(LocalDate.parse("2022-08-01"), LocalDate.parse("2022-08-31"));

        ReservationParams params = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();

        ReservationDateServiceImpl service = new ReservationDateServiceImpl(params, validateService);

        Exception exception = TestUtils.catchException(() -> service.stayRangeIsValid(stayRange, validRange));

        assertThat(exception)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Range not allowed. It surpass maximum stay days");
    }

    @Test
    public void givenDefaultParamsAndArrivalBeforeValidStartDateWhenStayRangeIsValidThenThrowsArrivalTooEarly() {
        Range<LocalDate> stayRange = new Range<>(LocalDate.parse("2022-07-31"), LocalDate.parse("2022-08-01"));
        Range<LocalDate> validRange = new Range<>(LocalDate.parse("2022-08-01"), LocalDate.parse("2022-08-31"));

        ReservationParams params = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();

        ReservationDateServiceImpl service = new ReservationDateServiceImpl(params, validateService);

        Exception exception = TestUtils.catchException(() -> service.stayRangeIsValid(stayRange, validRange));

        assertThat(exception)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Arrival is too early");
    }

    private ReservationParams givenDefaultParams() {
        return new ReservationParams(MIN_STAY_DAYS, MAX_STAY_DAYS, MAX_DEFAULT_DAYS_TO_SEARCH, MIN_AHEAD_ARRIVAL_DAYS, MAX_AHEAD_ARRIVAL_DAYS);
    }

    private ValidateService givenDefaultValidateService() {
        return Mockito.mock(ValidateService.class);
    }
}
