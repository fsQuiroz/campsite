package com.fsquiroz.campsite;

import com.fsquiroz.campsite.api.DayAvailabilityDTO;
import com.fsquiroz.campsite.api.GenericResponseDTO;
import com.fsquiroz.campsite.api.ReservationDTO;
import com.fsquiroz.campsite.mapper.DayAvailabilityMapper;
import com.fsquiroz.campsite.mapper.ReservationMapper;
import com.fsquiroz.campsite.persistence.entity.Reservation;
import com.fsquiroz.campsite.service.reservation.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private static final String OK_STATUS = "Ok";
    private static final String CANCELLED_MESSAGE = "Reservation cancelled successfully";
    private final ReservationService reservationService;

    private final ReservationMapper reservationMapper;

    private final DayAvailabilityMapper dayAvailabilityMapper;

    public ReservationController(
            ReservationService reservationService,
            ReservationMapper reservationMapper,
            DayAvailabilityMapper dayAvailabilityMapper
    ) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
        this.dayAvailabilityMapper = dayAvailabilityMapper;
    }

    @GetMapping("/availability")
    public ResponseEntity<Map<String, DayAvailabilityDTO>> getAvailability(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        Map<LocalDate, DayAvailabilityDTO> availabilityDTOMap = reservationService.getAvailableDays(from, to);
        return ResponseEntity.ok(
                dayAvailabilityMapper.map(availabilityDTOMap)
        );
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> create(
            @RequestBody ReservationDTO body
    ) {
        Reservation reservation = reservationService.create(body);
        return new ResponseEntity<>(
                reservationMapper.map(reservation),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> modify(
            @PathVariable Long reservationId,
            @RequestBody ReservationDTO body
    ) {
        Reservation reservation = reservationService.get(reservationId);
        reservation = reservationService.modify(reservation, body);
        return ResponseEntity.ok(
                reservationMapper.map(reservation)
        );
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<GenericResponseDTO> cancel(
            @PathVariable Long reservationId
    ) {
        Reservation reservation = reservationService.get(reservationId);
        reservationService.cancel(reservation);
        return ResponseEntity.ok(
                new GenericResponseDTO(Instant.now(), OK_STATUS, CANCELLED_MESSAGE)
        );
    }
}
