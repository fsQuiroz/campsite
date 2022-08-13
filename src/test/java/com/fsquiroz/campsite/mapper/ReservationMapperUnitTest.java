package com.fsquiroz.campsite.mapper;

import com.fsquiroz.campsite.api.ReservationDTO;
import com.fsquiroz.campsite.persistence.entity.Reservation;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReservationMapperUnitTest {

    @Test
    public void givenReservationWhenMappingThenDto() {
        Instant created = Instant.parse("2022-08-12T13:50:59.168Z");
        LocalDate arrival = LocalDate.parse("2022-08-20");
        LocalDate departure = LocalDate.parse("2022-08-22");
        Reservation reservation = givenReservation(1L, created, null, null, "Name", "example@email.com", arrival, departure);

        ReservationMapper mapper = new ReservationMapper();

        ReservationDTO mapped = mapper.map(reservation);

        assertThat(mapped)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrProperty("created")
                .hasFieldOrPropertyWithValue("updated", null)
                .hasFieldOrPropertyWithValue("deleted", null)
                .hasFieldOrPropertyWithValue("name", "Name")
                .hasFieldOrPropertyWithValue("email", "example@email.com")
                .hasFieldOrProperty("arrival")
                .hasFieldOrProperty("departure");
    }

    @Test
    public void givenReservationWhenMappingThenMappedCreated() {
        Instant created = Instant.parse("2022-08-12T13:50:59.168Z");
        LocalDate arrival = LocalDate.parse("2022-08-20");
        LocalDate departure = LocalDate.parse("2022-08-22");
        Reservation reservation = givenReservation(2L, created, null, null, "Name", "example@email.com", arrival, departure);

        ReservationMapper mapper = new ReservationMapper();

        ReservationDTO mapped = mapper.map(reservation);

        assertThat(mapped.getCreated())
                .isNotNull()
                .isCloseTo(
                        Instant.parse("2022-08-12T13:50:59.168Z"),
                        new TemporalUnitWithinOffset(1, ChronoUnit.MILLIS)
                );
    }

    @Test
    public void givenReservationWhenMappingThenMappedArrivalAndDeparture() {
        Instant created = Instant.parse("2022-08-12T13:50:59.168Z");
        LocalDate arrival = LocalDate.parse("2022-08-20");
        LocalDate departure = LocalDate.parse("2022-08-22");
        Reservation reservation = givenReservation(1L, created, null, null, "Name", "example@email.com", arrival, departure);

        ReservationMapper mapper = new ReservationMapper();

        ReservationDTO mapped = mapper.map(reservation);

        assertThat(mapped.getArrival())
                .isNotNull()
                .isCloseTo(
                        LocalDate.parse("2022-08-20"),
                        new TemporalUnitWithinOffset(1, ChronoUnit.DAYS)
                );

        assertThat(mapped.getDeparture())
                .isNotNull()
                .isCloseTo(
                        LocalDate.parse("2022-08-22"),
                        new TemporalUnitWithinOffset(1, ChronoUnit.DAYS)
                );
    }

    private Reservation givenReservation(Long id, Instant created, Instant updated, Instant deleted, String name, String email, LocalDate arrival, LocalDate departure) {
        return new Reservation(id, created, updated, deleted, name, email, arrival, departure);
    }
}
