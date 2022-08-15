package com.fsquiroz.campsite.service.reservation;

import com.fsquiroz.campsite.api.DayAvailabilityDTO;
import com.fsquiroz.campsite.api.ReservationDTO;
import com.fsquiroz.campsite.exception.BadRequestException;
import com.fsquiroz.campsite.exception.NotFoundException;
import com.fsquiroz.campsite.persistence.entity.Reservation;
import com.fsquiroz.campsite.persistence.repositroy.ReservationRepository;
import com.fsquiroz.campsite.service.validate.ValidateService;
import com.fsquiroz.campsite.util.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final String BODY_PARAM = "body";
    private static final String ARRIVAL_PARAM = "arrival";
    private static final String DEPARTURE_PARAM = "departure";
    private static final String NAME_PARAM = "name";
    private static final String EMAIL_PARAM = "email";
    private static final String FROM_PARAM = "from";
    private static final String TO_PARAM = "tp";

    private final int maxNameSize;
    private final int maxEmailSize;
    private final ValidateService validateService;

    private final ReservationDateService reservationDateService;

    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(
            @Value("${reservation.service.maxNameSize:255}") int maxNameSize,
            @Value("${reservation.service.maxEmailSize:255}") int maxEmailSize,
            ValidateService validateService,
            ReservationDateService reservationDateService,
            ReservationRepository reservationRepository
    ) {
        this.maxNameSize = maxNameSize;
        this.maxEmailSize = maxEmailSize;
        this.validateService = validateService;
        this.reservationDateService = reservationDateService;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Map<LocalDate, DayAvailabilityDTO> getAvailableDays(LocalDate from, LocalDate to) {
        Range<LocalDate> rangeToSearch;
        if (from != null && to != null) {
            validateService.isValidRange(FROM_PARAM, from, TO_PARAM, to);
            rangeToSearch = new Range<>(from, to);
        } else {
            rangeToSearch = reservationDateService.getDefaultSearchRange();
        }
        Range<LocalDate> availableRange = reservationDateService.getValidRangeToReserve();
        Map<LocalDate, DayAvailabilityDTO> days = new LinkedHashMap<>();
        for (LocalDate toEval = rangeToSearch.start(); toEval.isBefore(rangeToSearch.end()) || toEval.isEqual(rangeToSearch.end()); toEval = toEval.plus(1, ChronoUnit.DAYS)) {
            boolean available = (toEval.isAfter(availableRange.start()) || toEval.isEqual(availableRange.start())) &&
                    (toEval.isBefore(availableRange.end()) || toEval.isEqual(availableRange.end()));
            DayAvailabilityDTO dad = DayAvailabilityDTO.builder().available(available).build();
            days.put(toEval, dad);
        }
        return days;
    }

    @Override
    public Reservation get(Long id) {
        validateService.isValidId(id);
        return reservationRepository.findById(id)
                .orElseThrow(() -> NotFoundException.byId(Reservation.class, id));
    }

    @Override
    public Reservation create(ReservationDTO toCreate) {
        validateService.isNotNull(BODY_PARAM, toCreate);
        validateService.isNotEmptyWithinSize(NAME_PARAM, maxNameSize, toCreate.getName());
        validateService.isNotEmptyWithinSize(EMAIL_PARAM, maxEmailSize, toCreate.getEmail());
        validateReservationDates(toCreate);
        Reservation r = new Reservation();
        r.setCreated(Instant.now());
        r.setArrival(toCreate.getArrival());
        r.setDeparture(toCreate.getDeparture());
        r.setName(toCreate.getName());
        r.setEmail(toCreate.getEmail());
        return reservationRepository.save(r);
    }

    @Override
    public Reservation modify(@NonNull Reservation toModify, ReservationDTO modifications) {
        validateReservationIsNotCancelled(toModify);
        validateService.isNotNull(BODY_PARAM, modifications);
        validateReservationDates(modifications);
        toModify.setUpdated(Instant.now());
        toModify.setArrival(modifications.getArrival());
        toModify.setDeparture(modifications.getDeparture());
        return reservationRepository.save(toModify);
    }

    @Override
    public void cancel(@NonNull Reservation toCancel) {
        validateReservationIsNotCancelled(toCancel);
        toCancel.setDeleted(Instant.now());
        reservationRepository.save(toCancel);
    }

    private void validateReservationDates(ReservationDTO reservation) {
        validateService.isNotNull(ARRIVAL_PARAM, reservation.getArrival());
        validateService.isNotNull(DEPARTURE_PARAM, reservation.getDeparture());
        validateService.isValidRange(ARRIVAL_PARAM, reservation.getArrival(), DEPARTURE_PARAM, reservation.getDeparture());
        Range<LocalDate> stayRange = new Range<>(reservation.getArrival(), reservation.getDeparture());
        Range<LocalDate> validRange = reservationDateService.getValidRangeToReserve();
        reservationDateService.stayRangeIsValid(stayRange, validRange);
    }

    public void validateReservationIsNotCancelled(Reservation reservation) {
        if (reservation.getDeleted() != null) {
            throw BadRequestException.byModifyingCancelled(reservation);
        }
    }
}
