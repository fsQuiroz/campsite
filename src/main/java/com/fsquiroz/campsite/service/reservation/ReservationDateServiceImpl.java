package com.fsquiroz.campsite.service.reservation;

import com.fsquiroz.campsite.exception.BadRequestException;
import com.fsquiroz.campsite.service.validate.ValidateService;
import com.fsquiroz.campsite.util.Range;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class ReservationDateServiceImpl implements ReservationDateService {

    private final ReservationParams reservationParams;

    private final ValidateService validateService;

    public ReservationDateServiceImpl(
            ReservationParams reservationParams,
            ValidateService validateService
    ) {
        this.reservationParams = reservationParams;
        this.validateService = validateService;
    }

    @Override
    public Range<LocalDate> getDefaultSearchRange() {
        LocalDate start = LocalDate.now().plus(1L, ChronoUnit.DAYS);
        LocalDate end = LocalDate.now().plus(reservationParams.maxDefaultDaysToSearch(), ChronoUnit.DAYS);
        return new Range<>(start, end);
    }

    @Override
    public Range<LocalDate> getValidRangeToReserve() {
        LocalDate start = LocalDate.now().plus(reservationParams.minAheadArrivalReservationDays(), ChronoUnit.DAYS);
        LocalDate end = LocalDate.now()
                .plus(reservationParams.maxAheadArrivalReservationDays(), ChronoUnit.DAYS)
                .plus(reservationParams.maxStayDays() - 1, ChronoUnit.DAYS);
        return new Range<>(start, end);
    }

    @Override
    public void stayRangeIsValid(Range<LocalDate> stayRange, Range<LocalDate> validRange) {
        validateService.isValidRange("arrival", stayRange.start(), "departure", stayRange.end());
        long stayInDays = ChronoUnit.DAYS.between(stayRange.start(), stayRange.end());
        if (stayInDays > reservationParams.maxStayDays()) {
            throw BadRequestException.byRangeNotAllowedTooHigh(stayRange.start(), stayRange.end(), reservationParams.minStayDays(), reservationParams.maxStayDays());
        } else if (stayInDays < reservationParams.minStayDays()) {
            throw BadRequestException.byRangeNotAllowedTooLow(stayRange.start(), stayRange.end(), reservationParams.minStayDays(), reservationParams.maxStayDays());
        } else if (stayRange.start().isBefore(validRange.start())) {
            throw BadRequestException.byArrivalTooEarly(stayRange.start(), validRange.start());
        }
    }
}
