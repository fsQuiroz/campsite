package com.fsquiroz.campsite.mapper;

import com.fsquiroz.campsite.api.ReservationDTO;
import com.fsquiroz.campsite.persistence.entity.Reservation;
import org.springframework.stereotype.Service;

@Service
public class ReservationMapper extends Mapper<Reservation, ReservationDTO> {

    @Override
    public ReservationDTO map(Reservation reservation) {
        return reservation == null ? null : ReservationDTO.builder()
                .id(reservation.getId())
                .created(reservation.getCreated())
                .updated(reservation.getUpdated())
                .deleted(reservation.getDeleted())
                .name(reservation.getName())
                .email(reservation.getEmail())
                .arrival(reservation.getArrival())
                .departure(reservation.getDeparture())
                .build();
    }
}
