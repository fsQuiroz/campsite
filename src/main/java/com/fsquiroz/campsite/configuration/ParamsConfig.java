package com.fsquiroz.campsite.configuration;

import com.fsquiroz.campsite.service.reservation.ReservationParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParamsConfig {

    @Bean
    public ReservationParams reservationParams(
            @Value("${reservation.params.minStayDays:1}") int minStayDays,
            @Value("${reservation.params.maxStayDays:3}") int maxStayDays,
            @Value("${reservation.params.maxDefaultDaysToSearch:31}") int maxDefaultDaysToSearch,
            @Value("${reservation.params.minAheadArrivalReservationDays:1}") int minAheadArrivalReservationDays,
            @Value("${reservation.params.maxAheadArrivalReservationDays:31}") int maxAheadArrivalReservationDays
    ) {
        return new ReservationParams(minStayDays, maxStayDays, maxDefaultDaysToSearch, minAheadArrivalReservationDays, maxAheadArrivalReservationDays);
    }
}
