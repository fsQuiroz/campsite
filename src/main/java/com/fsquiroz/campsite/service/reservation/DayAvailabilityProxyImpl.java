package com.fsquiroz.campsite.service.reservation;

import com.fsquiroz.campsite.api.DayAvailabilityDTO;
import com.fsquiroz.campsite.configuration.CacheConfig;
import com.fsquiroz.campsite.mapper.DayAvailabilityMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class DayAvailabilityProxyImpl implements DayAvailabilityProxy {

    private final ReservationService reservationService;
    private final DayAvailabilityMapper mapper;

    public DayAvailabilityProxyImpl(ReservationService reservationService, DayAvailabilityMapper mapper) {
        this.reservationService = reservationService;
        this.mapper = mapper;
    }

    @Override
    @Cacheable(CacheConfig.AVAILABLE_DAYS_CACHE)
    public Map<String, DayAvailabilityDTO> getAvailableDays(LocalDate from, LocalDate to) {
        Map<LocalDate, DayAvailabilityDTO> availability = reservationService.getAvailableDays(from, to);
        return mapper.map(availability);
    }
}
