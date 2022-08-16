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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CreateReservationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationParams reservationParams;

    @Test
    public void givenValidReservationWhenCreateThenCreated() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = givenReservation(arrival, departure);

        mockMvc.perform(post(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated", Matchers.is(Matchers.nullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted", Matchers.is(Matchers.nullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.arrival", Matchers.is(arrival.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.departure", Matchers.is(departure.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Random Name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("sample@email.com")));
    }

    @Test
    public void givenReservationOnLastAvailableDayWhenCreateThenCreated() throws Exception {
        LocalDate arrival = LocalDate.now().plus(reservationParams.maxAheadArrivalReservationDays(), ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = givenReservation(arrival, departure);

        mockMvc.perform(post(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated", Matchers.is(Matchers.nullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted", Matchers.is(Matchers.nullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.arrival", Matchers.is(arrival.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.departure", Matchers.is(departure.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Random Name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("sample@email.com")));
    }

    @Test
    public void givenReservationWithFlippedArrivalAndDepartureWhenCreateThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(reservationParams.maxAheadArrivalReservationDays(), ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = givenReservation(departure, arrival);

        mockMvc.perform(post(body))
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
    public void givenReservationWithoutNameWhenCreateThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = """
                {
                	"email": "sample@email.com",
                	"arrival": "${arrival}",
                	"departure": "${departure}"
                }"""
                .replace("${arrival}", arrival.toString())
                .replace("${departure}", departure.toString());

        mockMvc.perform(post(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Missing param")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_MISSING_PARAM")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.param", Matchers.is("name")));
    }

    @Test
    public void givenReservationWithEmptyNameWhenCreateThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = """
                {
                    "name": "   ",
                	"email": "sample@email.com",
                	"arrival": "${arrival}",
                	"departure": "${departure}"
                }"""
                .replace("${arrival}", arrival.toString())
                .replace("${departure}", departure.toString());

        mockMvc.perform(post(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Missing param")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_MISSING_PARAM")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.param", Matchers.is("name")));
    }

    @Test
    public void givenReservationWithLongNameWhenCreateThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = """
                {
                    "name": "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
                	"email": "sample@email.com",
                	"arrival": "${arrival}",
                	"departure": "${departure}"
                }"""
                .replace("${arrival}", arrival.toString())
                .replace("${departure}", departure.toString());

        mockMvc.perform(post(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Text is too long")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_TEXT_TOO_LONG")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.param", Matchers.is("name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.text", Matchers.is("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.textLength", Matchers.is(342)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.maxTextLength", Matchers.is(reservationParams.maxNameSize())));
    }

    @Test
    public void givenReservationWithoutEmailWhenCreateThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = """
                {
                    "name": "Random Name",
                	"arrival": "${arrival}",
                	"departure": "${departure}"
                }"""
                .replace("${arrival}", arrival.toString())
                .replace("${departure}", departure.toString());

        mockMvc.perform(post(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Missing param")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_MISSING_PARAM")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.param", Matchers.is("email")));
    }

    @Test
    public void givenReservationWithEmptyEmailWhenCreateThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = """
                {
                    "name": "Random Name",
                	"email": "",
                	"arrival": "${arrival}",
                	"departure": "${departure}"
                }"""
                .replace("${arrival}", arrival.toString())
                .replace("${departure}", departure.toString());

        mockMvc.perform(post(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Missing param")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_MISSING_PARAM")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.param", Matchers.is("email")));
    }

    @Test
    public void givenReservationWithLongEmailWhenCreateThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = """
                {
                    "name": "Random Name",
                	"email": "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
                	"arrival": "${arrival}",
                	"departure": "${departure}"
                }"""
                .replace("${arrival}", arrival.toString())
                .replace("${departure}", departure.toString());

        mockMvc.perform(post(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Text is too long")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_TEXT_TOO_LONG")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.param", Matchers.is("email")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.text", Matchers.is("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.textLength", Matchers.is(342)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.maxTextLength", Matchers.is(reservationParams.maxNameSize())));
    }

    @Test
    public void givenReservationWithoutArrivalWhenCreateThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = """
                {
                    "name": "Random Name",
                	"email": "sample@email.com",
                	"departure": "${departure}"
                }"""
                .replace("${departure}", departure.toString());

        mockMvc.perform(post(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Missing param")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_MISSING_PARAM")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.param", Matchers.is("arrival")));
    }

    @Test
    public void givenReservationWithoutDepartureWhenCreateThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        String body = """
                {
                    "name": "Random Name",
                	"email": "sample@email.com",
                	"arrival": "${arrival}"
                }"""
                .replace("${arrival}", arrival.toString());

        mockMvc.perform(post(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Missing param")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("BR_MISSING_PARAM")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meta.param", Matchers.is("departure")));
    }

    @Test
    public void givenReservationExceedingMaxStayDaysWhenCreateThenBadRequestException() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays() * 2, ChronoUnit.DAYS);
        String body = givenReservation(arrival, departure);

        mockMvc.perform(post(body))
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
    public void givenReservationWithSameArrivalAndDepartureWhenCreateThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(3, ChronoUnit.DAYS);
        String body = givenReservation(arrival, arrival);

        mockMvc.perform(post(body))
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
    public void givenReservationWithArrivalTooEarlyWhenCreateThenBadRequest() throws Exception {
        LocalDate minArrival = LocalDate.now().plus(reservationParams.minAheadArrivalReservationDays(), ChronoUnit.DAYS);
        LocalDate arrival = LocalDate.now().plus(reservationParams.minAheadArrivalReservationDays() - 1, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = givenReservation(arrival, departure);

        mockMvc.perform(post(body))
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
    public void givenReservationWithArrivalAfterMaxAheadDaysWhenCreateThenBadRequest() throws Exception {
        LocalDate arrival = LocalDate.now().plus(reservationParams.maxAheadArrivalReservationDays() + 1, ChronoUnit.DAYS);
        LocalDate departure = arrival.plus(reservationParams.maxStayDays(), ChronoUnit.DAYS);
        String body = givenReservation(arrival, departure);

        mockMvc.perform(post(body))
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
    public void givenNullReservationWhenCreateThenBadRequest() throws Exception {
        mockMvc.perform(post(null))
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
                	"name": "Random Name",
                	"email": "sample@email.com",
                	"arrival": "${arrival}",
                	"departure": "${departure}"
                }"""
                .replace("${arrival}", arrival.toString())
                .replace("${departure}", departure.toString());
    }

    private MockHttpServletRequestBuilder post(String body) {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        if (body != null) {
            request.content(body);
        }
        return request;
    }
}
