package com.fsquiroz.campsite.service.reservation;

import com.fsquiroz.campsite.api.DayAvailabilityDTO;

import java.time.LocalDate;
import java.util.Map;

public interface DayAvailabilityProxy {

    Map<String, DayAvailabilityDTO> getAvailableDays(LocalDate from, LocalDate to);
}
