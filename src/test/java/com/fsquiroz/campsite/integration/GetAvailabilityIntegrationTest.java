package com.fsquiroz.campsite.integration;

import com.fsquiroz.campsite.service.reservation.ReservationParams;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetAvailabilityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationParams reservationParams;

    @Test
    public void givenDefaultAvailabilitySearchWhenGetAvailabilityThenOk() throws Exception {
        LocalDate when = LocalDate.now();
        ResultActions result = mockMvc.perform(get(null, null))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.aMapWithSize(31)));

        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(false)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
    }

    @Test
    public void givenRangeFullyWithinAvailabilityWhenGetAvailabilityThenOkAndAllAvailable() throws Exception {
        LocalDate from = LocalDate.now().plus(7, ChronoUnit.DAYS);
        LocalDate to = from.plus(3, ChronoUnit.DAYS);
        LocalDate when = from;
        ResultActions result = mockMvc.perform(get(from.toString(), to.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.aMapWithSize(4)));

        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
    }

    @Test
    public void givenRangeAfterAvailabilityWhenGetAvailabilityThenOkAndNoneAvailable() throws Exception {
        LocalDate from = LocalDate.now().plus(2, ChronoUnit.MONTHS);
        LocalDate to = from.plus(3, ChronoUnit.DAYS);
        LocalDate when = from;
        ResultActions result = mockMvc.perform(get(from.toString(), to.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.aMapWithSize(4)));

        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(false)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(false)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(false)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(false)));
    }

    @Test
    public void givenRangeBeforeAvailabilityWhenGetAvailabilityThenOkAndNoneAvailable() throws Exception {
        LocalDate from = LocalDate.now().minus(2, ChronoUnit.MONTHS);
        LocalDate to = from.plus(3, ChronoUnit.DAYS);
        LocalDate when = from;
        ResultActions result = mockMvc.perform(get(from.toString(), to.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.aMapWithSize(4)));

        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(false)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(false)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(false)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(false)));
    }

    @Test
    public void givenRangeAtHeadOfAvailabilityWhenGetAvailabilityThenOkAndFirstsUnavailable() throws Exception {
        LocalDate from = LocalDate.now().minus(1, ChronoUnit.DAYS);
        LocalDate to = from.plus(3, ChronoUnit.DAYS);
        LocalDate when = from;
        ResultActions result = mockMvc.perform(get(from.toString(), to.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.aMapWithSize(4)));

        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(false)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(false)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(false)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
    }

    @Test
    public void givenRangeAtTailOfAvailabilityWhenGetAvailabilityThenOkAndFirstsAvailable() throws Exception {
        LocalDate from = LocalDate.now().plus(reservationParams.maxAheadArrivalReservationDays() - 2, ChronoUnit.DAYS);
        LocalDate to = from.plus(5, ChronoUnit.DAYS);
        LocalDate when = from;
        ResultActions result = mockMvc.perform(get(from.toString(), to.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.aMapWithSize(6)));

        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(true)));
        when = when.plus(1, ChronoUnit.DAYS);
        result.andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForArrival", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + when + ".validForDeparture", Matchers.is(false)));
    }

    @Test
    public void givenInvalidRangeWhenGetAvailabilityThenBadRequest() throws Exception {
        LocalDate from = LocalDate.now();
        LocalDate to = from.minus(3, ChronoUnit.DAYS);

        mockMvc.perform(get(from.toString(), to.toString()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Invalid range. Start date can not be after end date")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_INVALID_RANGE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.from", Matchers.is(from.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.to", Matchers.is(to.toString())));
    }

    @Test
    public void givenInvalidDateWhenGetAvailabilityThenBadRequest() throws Exception {
        String from = LocalDate.now().toString();
        String to = "notValidDate";

        mockMvc.perform(get(from, to))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Unable to parse param")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_MALFORMED_PARAM")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.param", Matchers.is("to")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.value", Matchers.is(to)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.requiredType", Matchers.is("LocalDate")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.originalMessage", Matchers.startsWith("Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDate'")));
    }

    private MockHttpServletRequestBuilder get(String from, String to) {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/reservations/availability")
                .accept(MediaType.APPLICATION_JSON);
        if (from != null) {
            request.param("from", from);
        }
        if (to != null) {
            request.param("to", to);
        }
        return request;
    }
}
