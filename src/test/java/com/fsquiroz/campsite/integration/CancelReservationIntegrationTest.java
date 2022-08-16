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
public class CancelReservationIntegrationTest {

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
    public void givenValidReservationWhenCancelThenCancelled() throws Exception {
        mockMvc.perform(delete(validReservation))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("Ok")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Reservation cancelled successfully")));
    }

    @Test
    public void givenCancelledReservationWhenCancelThenBadRequest() throws Exception {
        mockMvc.perform(delete(cancelledReservation))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Reservation has already been cancelled, can not be modified")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_MODIFYING_CANCELLED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.cancellationDate", Matchers.startsWith(LocalDateTime.ofInstant(cancelledReservation.getDeleted(), ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS).toString())));
    }

    @Test
    public void givenNoReservationWhenCancelThenNotFound() throws Exception {
        Reservation notFound = new Reservation();
        notFound.setId(cancelledReservation.getId() + 10);

        mockMvc.perform(delete(notFound))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Entity not found by id")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("NF_BY_ID")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.entity", Matchers.is("Reservation")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.id", Matchers.is(notFound.getId().intValue())));
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

    private MockHttpServletRequestBuilder delete(Reservation reservation) {
        return MockMvcRequestBuilders
                .delete("/reservations/{reservationId}", reservation.getId())
                .accept(MediaType.APPLICATION_JSON);
    }
}
