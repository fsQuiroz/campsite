package com.fsquiroz.campsite.service.reservation;

import com.fsquiroz.campsite.TestUtils;
import com.fsquiroz.campsite.api.DayAvailabilityDTO;
import com.fsquiroz.campsite.api.ReservationDTO;
import com.fsquiroz.campsite.exception.BadRequestException;
import com.fsquiroz.campsite.exception.NotFoundException;
import com.fsquiroz.campsite.persistence.entity.Reservation;
import com.fsquiroz.campsite.persistence.repositroy.ReservationRepository;
import com.fsquiroz.campsite.service.validate.ValidateService;
import com.fsquiroz.campsite.util.Range;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class ReservationServiceUnitTest {

    private static final int MIN_STAY_DAYS = 1;
    private static final int MAX_STAY_DAYS = 3;
    private static final int MAX_DEFAULT_DAYS_TO_SEARCH = 31;
    private static final int MIN_AHEAD_ARRIVAL_DAYS = 1;
    private static final int MAX_AHEAD_ARRIVAL_DAYS = 31;
    private static final int MAX_NAME_SIZE = 20;
    private static final int MAX_EMAIL_SIZE = 20;

    @Captor
    private ArgumentCaptor<Reservation> reservationCaptor;

    @Test
    public void givenSearchRangeFullyWithinAvailableDaysWhenGetAvailableDaysThenUnavailableAtStartAndEndAndAvailableInMiddle() {
        LocalDate from = LocalDate.parse("2022-08-10");
        LocalDate to = LocalDate.parse("2022-08-18");

        LocalDate availabilityStarts = LocalDate.parse("2022-08-12");
        LocalDate availabilityEnds = LocalDate.parse("2022-08-15");

        ReservationParams reservationParams = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();
        ReservationDateService reservationDateService = givenDefaultReservationDateService();
        ReservationRepository reservationRepository = givenDefaultReservationRepository();

        mockReservationDateServiceToReturnGivenValidRange(reservationDateService, availabilityStarts, availabilityEnds);

        ReservationServiceImpl service = new ReservationServiceImpl(reservationParams, validateService, reservationDateService, reservationRepository);

        Map<LocalDate, DayAvailabilityDTO> availableDays = service.getAvailableDays(from, to);

        assertThat(availableDays)
                .isNotNull()
                .hasSize(9);

        assertThat(availableDays.get(LocalDate.parse("2022-08-10")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-11")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-12")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-13")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-14")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-15")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-16")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-17")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-18")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
    }

    @Test
    public void givenSearchRangeEmptyWithDefaultSearchRangeWithinAvailableDaysWhenGetAvailableDaysThenUnavailableAtStartAndEndAndAvailableInMiddle() {
        LocalDate defaultFrom = LocalDate.parse("2022-08-10");
        LocalDate defaultTo = LocalDate.parse("2022-08-18");

        LocalDate availabilityStarts = LocalDate.parse("2022-08-12");
        LocalDate availabilityEnds = LocalDate.parse("2022-08-15");

        ReservationParams reservationParams = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();
        ReservationDateService reservationDateService = givenDefaultReservationDateService();
        ReservationRepository reservationRepository = givenDefaultReservationRepository();

        mockReservationDateServiceToReturnGivenValidRange(reservationDateService, availabilityStarts, availabilityEnds);
        mockReservationDateServiceToReturnDefaultSearchRange(reservationDateService, defaultFrom, defaultTo);

        ReservationServiceImpl service = new ReservationServiceImpl(reservationParams, validateService, reservationDateService, reservationRepository);

        Map<LocalDate, DayAvailabilityDTO> availableDays = service.getAvailableDays(null, null);

        assertThat(availableDays)
                .isNotNull()
                .hasSize(9);

        assertThat(availableDays.get(LocalDate.parse("2022-08-10")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-11")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-12")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-13")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-14")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-15")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-16")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-17")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-18")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
    }

    @Test
    public void givenSearchRangeOnlyFromWithDefaultSearchRangeWithinAvailableDaysWhenGetAvailableDaysThenUnavailableAtStartAndEndAndAvailableInMiddle() {
        LocalDate from = LocalDate.parse("2022-09-01");
        LocalDate defaultFrom = LocalDate.parse("2022-08-10");
        LocalDate defaultTo = LocalDate.parse("2022-08-18");

        LocalDate availabilityStarts = LocalDate.parse("2022-08-12");
        LocalDate availabilityEnds = LocalDate.parse("2022-08-15");

        ReservationParams reservationParams = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();
        ReservationDateService reservationDateService = givenDefaultReservationDateService();
        ReservationRepository reservationRepository = givenDefaultReservationRepository();

        mockReservationDateServiceToReturnGivenValidRange(reservationDateService, availabilityStarts, availabilityEnds);
        mockReservationDateServiceToReturnDefaultSearchRange(reservationDateService, defaultFrom, defaultTo);

        ReservationServiceImpl service = new ReservationServiceImpl(reservationParams, validateService, reservationDateService, reservationRepository);

        Map<LocalDate, DayAvailabilityDTO> availableDays = service.getAvailableDays(from, null);

        assertThat(availableDays)
                .isNotNull()
                .hasSize(9);

        assertThat(availableDays.get(LocalDate.parse("2022-08-10")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-11")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-12")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-13")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-14")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-15")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-16")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-17")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-18")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
    }

    @Test
    public void givenSearchRangeOnlyToWithDefaultSearchRangeWithinAvailableDaysWhenGetAvailableDaysThenUnavailableAtStartAndEndAndAvailableInMiddle() {
        LocalDate to = LocalDate.parse("2022-09-01");
        LocalDate defaultFrom = LocalDate.parse("2022-08-10");
        LocalDate defaultTo = LocalDate.parse("2022-08-18");

        LocalDate availabilityStarts = LocalDate.parse("2022-08-12");
        LocalDate availabilityEnds = LocalDate.parse("2022-08-15");

        ReservationParams reservationParams = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();
        ReservationDateService reservationDateService = givenDefaultReservationDateService();
        ReservationRepository reservationRepository = givenDefaultReservationRepository();

        mockReservationDateServiceToReturnGivenValidRange(reservationDateService, availabilityStarts, availabilityEnds);
        mockReservationDateServiceToReturnDefaultSearchRange(reservationDateService, defaultFrom, defaultTo);

        ReservationServiceImpl service = new ReservationServiceImpl(reservationParams, validateService, reservationDateService, reservationRepository);

        Map<LocalDate, DayAvailabilityDTO> availableDays = service.getAvailableDays(null, to);

        assertThat(availableDays)
                .isNotNull()
                .hasSize(9);

        assertThat(availableDays.get(LocalDate.parse("2022-08-10")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-11")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-12")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-13")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-14")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-15")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-16")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-17")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-18")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
    }

    @Test
    public void givenSearchRangePartlyWithinAvailableDaysWhenGetAvailableDaysThenUnavailableAtStartAndEndAndAvailableInMiddle() {
        LocalDate from = LocalDate.parse("2022-08-10");
        LocalDate to = LocalDate.parse("2022-08-18");

        LocalDate availabilityStarts = LocalDate.parse("2022-07-13");
        LocalDate availabilityEnds = LocalDate.parse("2022-08-13");

        ReservationParams reservationParams = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();
        ReservationDateService reservationDateService = givenDefaultReservationDateService();
        ReservationRepository reservationRepository = givenDefaultReservationRepository();

        mockReservationDateServiceToReturnGivenValidRange(reservationDateService, availabilityStarts, availabilityEnds);

        ReservationServiceImpl service = new ReservationServiceImpl(reservationParams, validateService, reservationDateService, reservationRepository);

        Map<LocalDate, DayAvailabilityDTO> availableDays = service.getAvailableDays(from, to);

        assertThat(availableDays)
                .isNotNull()
                .hasSize(9);

        assertThat(availableDays.get(LocalDate.parse("2022-08-10")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-11")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-12")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-13")))
                .hasFieldOrPropertyWithValue("validForArrival", true)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-14")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-15")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", true);
        assertThat(availableDays.get(LocalDate.parse("2022-08-16")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-17")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-18")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
    }

    @Test
    public void givenSearchRangeFullyOutsideAvailableDaysWhenGetAvailableDaysThenUnavailableAtStartAndEndAndAvailableInMiddle() {
        LocalDate from = LocalDate.parse("2022-08-10");
        LocalDate to = LocalDate.parse("2022-08-18");

        LocalDate availabilityStarts = LocalDate.parse("2022-07-01");
        LocalDate availabilityEnds = LocalDate.parse("2022-07-31");

        ReservationParams reservationParams = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();
        ReservationDateService reservationDateService = givenDefaultReservationDateService();
        ReservationRepository reservationRepository = givenDefaultReservationRepository();

        mockReservationDateServiceToReturnGivenValidRange(reservationDateService, availabilityStarts, availabilityEnds);

        ReservationServiceImpl service = new ReservationServiceImpl(reservationParams, validateService, reservationDateService, reservationRepository);

        Map<LocalDate, DayAvailabilityDTO> availableDays = service.getAvailableDays(from, to);

        assertThat(availableDays)
                .isNotNull()
                .hasSize(9);

        assertThat(availableDays.get(LocalDate.parse("2022-08-10")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-11")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-12")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-13")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-14")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-15")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-16")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-17")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
        assertThat(availableDays.get(LocalDate.parse("2022-08-18")))
                .hasFieldOrPropertyWithValue("validForArrival", false)
                .hasFieldOrPropertyWithValue("validForDeparture", false);
    }

    @Test
    public void givenValidIdWhenGetThenReturnReservation() {
        Reservation r = new Reservation();
        r.setId(1L);
        ReservationParams reservationParams = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();
        ReservationDateService reservationDateService = givenDefaultReservationDateService();
        ReservationRepository reservationRepository = givenDefaultReservationRepository();

        mockReservationRepositoryToReturnGivenAny(reservationRepository, r);

        ReservationServiceImpl service = new ReservationServiceImpl(reservationParams, validateService, reservationDateService, reservationRepository);

        Reservation reservation = service.get(1L);

        assertThat(reservation)
                .isEqualTo(r);
    }

    @Test
    public void givenMissingIdWhenGetThenThrowNotFoundException() {
        Reservation r = null;
        ReservationParams reservationParams = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();
        ReservationDateService reservationDateService = givenDefaultReservationDateService();
        ReservationRepository reservationRepository = givenDefaultReservationRepository();

        mockReservationRepositoryToReturnGivenAny(reservationRepository, r);

        ReservationServiceImpl service = new ReservationServiceImpl(reservationParams, validateService, reservationDateService, reservationRepository);

        Exception exception = TestUtils.catchException(() -> service.get(1L));

        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Entity not found by id");
    }

    @Test
    public void givenReservationWhenCreateThenReservationSavedWithParams() {
        ReservationDTO toCreate = new ReservationDTO();
        toCreate.setArrival(LocalDate.parse("2022-08-10"));
        toCreate.setDeparture(LocalDate.parse("2022-08-12"));
        toCreate.setName("Name Lastname");
        toCreate.setEmail("example@email.com");

        ReservationParams reservationParams = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();
        ReservationDateService reservationDateService = givenDefaultReservationDateService();
        ReservationRepository reservationRepository = givenDefaultReservationRepository();

        ReservationServiceImpl service = new ReservationServiceImpl(reservationParams, validateService, reservationDateService, reservationRepository);

        service.create(toCreate);

        Mockito.verify(reservationRepository, Mockito.times(1)).save(reservationCaptor.capture());

        Reservation saved = reservationCaptor.getValue();

        assertThat(saved)
                .isNotNull()
                .hasFieldOrPropertyWithValue("arrival", LocalDate.parse("2022-08-10"))
                .hasFieldOrPropertyWithValue("departure", LocalDate.parse("2022-08-12"))
                .hasFieldOrPropertyWithValue("name", "Name Lastname")
                .hasFieldOrPropertyWithValue("email", "example@email.com");

        assertThat(saved.getCreated())
                .isCloseTo(
                        Instant.now(),
                        new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS)
                );
    }

    @Test
    public void givenNonCancelledReservationAndModificationsWhenModifyThenModifiedReservation() {
        Reservation created = new Reservation();
        created.setId(1L);
        created.setCreated(Instant.now().minus(1, ChronoUnit.DAYS));
        created.setArrival(LocalDate.parse("2022-08-10"));
        created.setDeparture(LocalDate.parse("2022-08-12"));
        created.setName("Original Name");
        created.setEmail("original@email.com");

        ReservationDTO modifications = new ReservationDTO();
        modifications.setArrival(LocalDate.parse("2022-08-20"));
        modifications.setDeparture(LocalDate.parse("2022-08-22"));

        ReservationParams reservationParams = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();
        ReservationDateService reservationDateService = givenDefaultReservationDateService();
        ReservationRepository reservationRepository = givenDefaultReservationRepository();

        ReservationServiceImpl service = new ReservationServiceImpl(reservationParams, validateService, reservationDateService, reservationRepository);

        service.modify(created, modifications);

        Mockito.verify(reservationRepository, Mockito.times(1)).save(reservationCaptor.capture());

        Reservation saved = reservationCaptor.getValue();

        assertThat(saved)
                .isNotNull()
                .hasFieldOrPropertyWithValue("arrival", LocalDate.parse("2022-08-20"))
                .hasFieldOrPropertyWithValue("departure", LocalDate.parse("2022-08-22"))
                .hasFieldOrPropertyWithValue("name", "Original Name")
                .hasFieldOrPropertyWithValue("email", "original@email.com");

        assertThat(saved.getCreated())
                .asInstanceOf(InstanceOfAssertFactories.INSTANT)
                .isEqualTo(created.getCreated());

        assertThat(saved.getUpdated())
                .asInstanceOf(InstanceOfAssertFactories.INSTANT)
                .isCloseTo(
                        Instant.now(),
                        new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS)
                );
    }

    @Test
    public void givenCancelledReservationAndModificationsWhenModifyThenThrowModifyingCancelled() {
        Reservation created = new Reservation();
        created.setId(1L);
        created.setCreated(Instant.now().minus(1, ChronoUnit.DAYS));
        created.setDeleted(Instant.now());

        ReservationDTO modifications = new ReservationDTO();
        modifications.setArrival(LocalDate.parse("2022-08-20"));
        modifications.setDeparture(LocalDate.parse("2022-08-22"));

        ReservationParams reservationParams = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();
        ReservationDateService reservationDateService = givenDefaultReservationDateService();
        ReservationRepository reservationRepository = givenDefaultReservationRepository();

        ReservationServiceImpl service = new ReservationServiceImpl(reservationParams, validateService, reservationDateService, reservationRepository);

        Exception exception = TestUtils.catchException(() -> service.modify(created, modifications));

        assertThat(exception)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Reservation has already been cancelled, can not be modified");
    }

    @Test
    public void givenNonCancelledReservationWhenCancelThenReservationCancelled() {
        Reservation created = new Reservation();
        created.setId(1L);
        created.setCreated(Instant.now().minus(1, ChronoUnit.DAYS));

        ReservationParams reservationParams = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();
        ReservationDateService reservationDateService = givenDefaultReservationDateService();
        ReservationRepository reservationRepository = givenDefaultReservationRepository();

        ReservationServiceImpl service = new ReservationServiceImpl(reservationParams, validateService, reservationDateService, reservationRepository);

        service.cancel(created);

        Mockito.verify(reservationRepository, Mockito.times(1)).save(reservationCaptor.capture());

        Reservation saved = reservationCaptor.getValue();

        assertThat(saved)
                .isNotNull();

        assertThat(saved.getDeleted())
                .isCloseTo(
                        Instant.now(),
                        new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS)
                );
    }

    @Test
    public void givenCancelledReservationWhenCancelThenThrowModifyingCancelled() {
        Reservation created = new Reservation();
        created.setId(1L);
        created.setCreated(Instant.now().minus(1, ChronoUnit.DAYS));
        created.setDeleted(Instant.now());

        ReservationParams reservationParams = givenDefaultParams();
        ValidateService validateService = givenDefaultValidateService();
        ReservationDateService reservationDateService = givenDefaultReservationDateService();
        ReservationRepository reservationRepository = givenDefaultReservationRepository();

        ReservationServiceImpl service = new ReservationServiceImpl(reservationParams, validateService, reservationDateService, reservationRepository);

        Exception exception = TestUtils.catchException(() -> service.cancel(created));

        assertThat(exception)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Reservation has already been cancelled, can not be modified");
    }

    private ReservationParams givenDefaultParams() {
        return new ReservationParams(MIN_STAY_DAYS, MAX_STAY_DAYS, MAX_DEFAULT_DAYS_TO_SEARCH, MIN_AHEAD_ARRIVAL_DAYS, MAX_AHEAD_ARRIVAL_DAYS, MAX_NAME_SIZE, MAX_EMAIL_SIZE);
    }

    private ValidateService givenDefaultValidateService() {
        return Mockito.mock(ValidateService.class);
    }

    private ReservationDateService givenDefaultReservationDateService() {
        return Mockito.mock(ReservationDateService.class);
    }

    private ReservationRepository givenDefaultReservationRepository() {
        return Mockito.mock(ReservationRepository.class);
    }

    private void mockReservationDateServiceToReturnGivenValidRange(ReservationDateService reservationDateService, LocalDate start, LocalDate end) {
        Mockito.when(reservationDateService.getValidRangeToReserve()).thenReturn(new Range<>(start, end));
    }

    private void mockReservationDateServiceToReturnDefaultSearchRange(ReservationDateService reservationDateService, LocalDate start, LocalDate end) {
        Mockito.when(reservationDateService.getDefaultSearchRange()).thenReturn(new Range<>(start, end));
    }

    private void mockReservationRepositoryToReturnGivenAny(ReservationRepository repository, Reservation toReturn) {
        Mockito.when(repository.findById(any())).thenReturn(Optional.ofNullable(toReturn));
    }
}
