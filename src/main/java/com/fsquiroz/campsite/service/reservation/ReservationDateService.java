package com.fsquiroz.campsite.service.reservation;

import com.fsquiroz.campsite.util.Range;

import java.time.LocalDate;

public interface ReservationDateService {
    Range<LocalDate> getDefaultSearchRange();

    Range<LocalDate> getValidRangeToReserve();

    void stayRangeIsValid(Range<LocalDate> stayRange, Range<LocalDate> validRange);
}
