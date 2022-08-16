package com.fsquiroz.campsite.integration;

import com.fsquiroz.campsite.persistence.entity.Reservation;
import com.fsquiroz.campsite.persistence.repositroy.ReservationRepository;
import com.fsquiroz.campsite.service.reservation.ReservationParams;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ModifyReservationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationParams reservationParams;

    @Autowired
    private ReservationRepository repository;

    private Reservation validReservation;

    private Reservation cancelledReservation;

    @BeforeEach
    public void setup() {
        validReservation = repository.save(givenValidReservation());
        cancelledReservation = repository.save(givenCancelledReservation());
    }

    @AfterEach
    public void cleanup() {
        repository.delete(validReservation);
        repository.delete(cancelledReservation);
    }

    @Test
    public void givenValidReservationAndNewValidArrivalAndDepartureWhenModifyThenModified() throws Exception {
        LocalDate arrival = LocalDate.now().plus(reservationParams.maxAheadArrivalReservationDays(), ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = givenReservation(arrival, departure);

        mockMvc.perform(put(validReservation, body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(validReservation.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted", Matchers.is(Matchers.nullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.arrival", Matchers.is(arrival.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.departure", Matchers.is(departure.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(validReservation.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(validReservation.getEmail())));
    }

    @Test
    public void givenCancelledReservationAndNewValidArrivalAndDepartureWhenModifyThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(reservationParams.maxAheadArrivalReservationDays(), ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = givenReservation(arrival, departure);

        mockMvc.perform(put(cancelledReservation, body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Reservation has already been cancelled, can not be modified")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_MODIFYING_CANCELLED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.cancellationDate", Matchers.startsWith(LocalDateTime.ofInstant(cancelledReservation.getDeleted(), ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS).toString())));
    }

    @Test
    public void givenNoReservationAndNewValidArrivalAndDepartureWhenModifyThenNotFound() throws Exception {
        LocalDate arrival = LocalDate.now().plus(reservationParams.maxAheadArrivalReservationDays(), ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = givenReservation(arrival, departure);
        Reservation notFound = new Reservation();
        notFound.setId(cancelledReservation.getId() + 10);

        mockMvc.perform(put(notFound, body))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Entity not found by id")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("NF_BY_ID")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.entity", Matchers.is("Reservation")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.id", Matchers.is(notFound.getId().intValue())));
    }

    @Test
    public void givenValidReservationAndNewFlippedArrivalAndDepartureWhenModifyThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(reservationParams.maxAheadArrivalReservationDays(), ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = givenReservation(departure, arrival);

        mockMvc.perform(put(validReservation, body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Invalid range. Start date can not be after end date")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_INVALID_RANGE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.arrival", Matchers.is(departure.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.departure", Matchers.is(arrival.toString())));
    }

    @Test
    public void givenValidReservationAndNewDepartureWithoutArrivalWhenModifyThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = """
                {
                	"departure": "${departure}"
                }"""
                .replace("${departure}", departure.toString());

        mockMvc.perform(put(validReservation, body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Missing param")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_MISSING_PARAM")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.param", Matchers.is("arrival")));
    }

    @Test
    public void givenValidReservationAndNewArrivalWithoutDepartureWhenModifyThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        String body = """
                {
                	"arrival": "${arrival}"
                }"""
                .replace("${arrival}", arrival.toString());

        mockMvc.perform(put(validReservation, body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Missing param")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_MISSING_PARAM")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.param", Matchers.is("departure")));
    }

    @Test
    public void givenValidReservationAndNewArrivalAndDepartureExceedingMaxStayDaysWhenModifyThenBadRequestException() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays() * 2, ChronoUnit.DAYS);
        String body = givenReservation(arrival, departure);

        mockMvc.perform(put(validReservation, body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Range not allowed. It surpass maximum stay days")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_RANGE_NOT_ALLOWED_TOO_HIGH")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.arrival", Matchers.is(arrival.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.departure", Matchers.is(departure.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.minStay", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.maxStay", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.actualStay", Matchers.is(6)));
    }

    @Test
    public void givenValidReservationAndNewArrivalWithSameDepartureWhenModifyThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        String body = givenReservation(arrival, arrival);

        mockMvc.perform(put(validReservation, body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Range not allowed. It does not reach minimum stay days")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_RANGE_NOT_ALLOWED_TOO_LOW")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.arrival", Matchers.is(arrival.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.departure", Matchers.is(arrival.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.minStay", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.maxStay", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.actualStay", Matchers.is(0)));
    }

    @Test
    public void givenValidReservationAndNewArrivalTooEarlyWhenModifyThenBadRequest() throws Exception {
        LocalDate minArrival = LocalDate.now().plus(reservationParams.minAheadArrivalReservationDays(), ChronoUnit.DAYS);
        LocalDate arrival = LocalDate.now().plus(reservationParams.minAheadArrivalReservationDays() - 1, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = givenReservation(arrival, departure);

        mockMvc.perform(put(validReservation, body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Arrival is too early")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_ARRIVAL_TOO_EARLY")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.arrival", Matchers.is(arrival.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.minArrival", Matchers.is(minArrival.toString())));
    }

    @Test
    public void givenValidReservationAndNewArrivalAfterMaxAheadDaysWhenModifyThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(reservationParams.maxAheadArrivalReservationDays() + 1, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = givenReservation(arrival, departure);

        mockMvc.perform(put(validReservation, body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("There is no more reservations available for the provided date range")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_NO_MORE_RESERVATION_AVAILABLE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.arrival", Matchers.is(arrival.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.departure", Matchers.is(departure.toString())));
    }

    @Test
    public void givenValidReservationAndNullArrivalAndDepartureWhenModifyThenBadRequest() throws Exception {
        mockMvc.perform(put(validReservation, null))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Unable to parse body content")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_MALFORMED_BODY")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta", Matchers.is(Matchers.nullValue())));
    }

    private String givenReservation(LocalDate arrival, LocalDate departure) {
        return """
                {
                	"arrival": "${arrival}",
                	"departure": "${departure}"
                }"""
                .replace("${arrival}", arrival.toString())
                .replace("${departure}", departure.toString());
    }

    private Reservation givenValidReservation() {
        Reservation r = new Reservation();
        r.setCreated(Instant.now().minus(1, ChronoUnit.HOURS));
        r.setName("Random Name");
        r.setEmail("sample@email.com");
        r.setArrival(LocalDate.now().plus(reservationParams.minAheadArrivalReservationDays(), ChronoUnit.DAYS));
        r.setDeparture(r.getArrival().plus(reservationParams.maxStayDays(), ChronoUnit.DAYS));
        return r;
    }

    private Reservation givenCancelledReservation() {
        Reservation r = givenValidReservation();
        r.setDeleted(Instant.now().minus(1, ChronoUnit.MINUTES));
        return r;
    }

    private MockHttpServletRequestBuilder put(Reservation reservation, String body) {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/reservations/{reservationId}", reservation.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        if (body != null) {
            request.content(body);
        }
        return request;
    }
}
