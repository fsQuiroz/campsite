package com.fsquiroz.campsite.service.reservation;

import com.fsquiroz.campsite.api.DayAvailabilityDTO;
import com.fsquiroz.campsite.api.ReservationDTO;
import com.fsquiroz.campsite.persistence.entity.Reservation;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Map;

public interface ReservationService {

    Map<LocalDate, DayAvailabilityDTO> getAvailableDays(LocalDate from, LocalDate to);

    Reservation get(Long id);

    Reservation create(ReservationDTO toCreate);

    Reservation modify(@NonNull Reservation toModify, ReservationDTO modifications);

    void cancel(@NonNull Reservation toCancel);
}
