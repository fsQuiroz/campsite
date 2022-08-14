package com.fsquiroz.campsite.service.validate;

import com.fsquiroz.campsite.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ValidateServiceUnitTest {

    @Test
    public void givenValidIdWhenValidatingThenNoException() {
        Long id = 1L;

        ValidateServiceImpl validateService = new ValidateServiceImpl();

        Exception exception = catchException(() -> validateService.isValidId(id));

        assertThat(exception)
                .isNull();
    }

    @Test
    public void givenInvalidIdWhenValidatingThenException() {
        Long id = null;

        ValidateServiceImpl validateService = new ValidateServiceImpl();

        Exception exception = catchException(() -> validateService.isValidId(id));

        assertThat(exception)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Invalid id value");
    }

    @Test
    public void givenNonNullParamWhenValidatingThenNoException() {
        String param = "param";
        Object value = new Object();

        ValidateServiceImpl validateService = new ValidateServiceImpl();

        Exception exception = catchException(() -> validateService.isNotNull(param, value));

        assertThat(exception)
                .isNull();
    }

    @Test
    public void givenNullParamWhenValidatingThenException() {
        String param = "param";
        Object value = null;

        ValidateServiceImpl validateService = new ValidateServiceImpl();

        Exception exception = catchException(() -> validateService.isNotNull(param, value));

        assertThat(exception)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Missing param");
    }

    @Test
    public void givenNullStringWhenValidatingThenException() {
        String param = "param";
        String text = null;

        ValidateServiceImpl validateService = new ValidateServiceImpl();

        Exception exception = catchException(() -> validateService.isNotEmpty(param, text));

        assertThat(exception)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Missing param");
    }

    @Test
    public void givenEmptyStringWhenValidatingThenException() {
        String param = "param";
        String text = "";

        ValidateServiceImpl validateService = new ValidateServiceImpl();

        Exception exception = catchException(() -> validateService.isNotEmpty(param, text));

        assertThat(exception)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Missing param");
    }

    @Test
    public void givenOnlySpacesStringWhenValidatingThenException() {
        String param = "param";
        String text = "   ";

        ValidateServiceImpl validateService = new ValidateServiceImpl();

        Exception exception = catchException(() -> validateService.isNotEmpty(param, text));

        assertThat(exception)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Missing param");
    }

    @Test
    public void givenValidStringWhenValidatingThenNoException() {
        String param = "param";
        String text = "text";

        ValidateServiceImpl validateService = new ValidateServiceImpl();

        Exception exception = catchException(() -> validateService.isNotEmpty(param, text));

        assertThat(exception)
                .isNull();
    }

    @Test
    public void givenValidStringWithinSizeWhenValidatingThenNoException() {
        String param = "param";
        String text = "abcdefghijklmnopqrstuvwxyz";
        int maxSize = 30;

        ValidateServiceImpl validateService = new ValidateServiceImpl();

        Exception exception = catchException(() -> validateService.isNotEmptyWithinSize(param, maxSize, text));

        assertThat(exception)
                .isNull();
    }

    @Test
    public void givenValidStringBiggerThanMaxSizeWhenValidatingThenNoException() {
        String param = "param";
        String text = "abcdefghijklmnopqrstuvwxyz";
        int maxSize = 20;

        ValidateServiceImpl validateService = new ValidateServiceImpl();

        Exception exception = catchException(() -> validateService.isNotEmptyWithinSize(param, maxSize, text));

        assertThat(exception)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Text is too long");
    }

    @Test
    public void givenStartAfterEndWhenValidatingThenNoException() {
        String startParam = "start";
        String endParam = "end";
        LocalDate start = LocalDate.parse("2022-08-01");
        LocalDate end = LocalDate.parse("2022-08-02");

        ValidateServiceImpl validateService = new ValidateServiceImpl();

        Exception exception = catchException(() -> validateService.isValidRange(startParam, start, endParam, end));

        assertThat(exception)
                .isNull();
    }

    @Test
    public void givenStartEqualToEndWhenValidatingThenNoException() {
        String startParam = "start";
        String endParam = "end";
        LocalDate start = LocalDate.parse("2022-08-01");
        LocalDate end = LocalDate.parse("2022-08-01");

        ValidateServiceImpl validateService = new ValidateServiceImpl();

        Exception exception = catchException(() -> validateService.isValidRange(startParam, start, endParam, end));

        assertThat(exception)
                .isNull();
    }

    @Test
    public void givenStartBeforeEndWhenValidatingThenNoException() {
        String startParam = "start";
        String endParam = "end";
        LocalDate start = LocalDate.parse("2022-08-02");
        LocalDate end = LocalDate.parse("2022-08-01");

        ValidateServiceImpl validateService = new ValidateServiceImpl();

        Exception exception = catchException(() -> validateService.isValidRange(startParam, start, endParam, end));

        assertThat(exception)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Invalid range. Start date can not be after end date");
    }

    private Exception catchException(Runnable r) {
        try {
            r.run();
        } catch (Exception e) {
            return e;
        }
        return null;
    }
}
