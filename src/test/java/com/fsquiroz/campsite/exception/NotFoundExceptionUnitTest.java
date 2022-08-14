package com.fsquiroz.campsite.exception;

import com.fsquiroz.campsite.persistence.entity.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NotFoundExceptionUnitTest {

    @Test
    public void givenInvalidIdWhenNewNotFoundThenValidException() {
        Class<Reservation> clazz = Reservation.class;
        Long id = 100L;

        AppException exception = NotFoundException.byId(clazz, id);

        assertThat(exception)
                .isNotNull()
                .hasMessage("Entity not found by id")
                .hasFieldOrPropertyWithValue("code", ErrorCode.NF_BY_ID)
                .hasFieldOrPropertyWithValue("meta.entity", "Reservation")
                .hasFieldOrPropertyWithValue("meta.id", id);
    }
}
