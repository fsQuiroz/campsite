package com.fsquiroz.campsite.service.validate;

import org.springframework.lang.NonNull;

import java.time.LocalDate;

public interface ValidateService {

    void isValidId(Long id);

    void isNotNull(@NonNull String param, Object o);

    void isNotEmpty(String param, String s);

    void isNotEmptyWithinSize(String param, int maxSize, String s);

    void isValidRange(@NonNull String startParam, @NonNull LocalDate start, @NonNull String endParam, @NonNull LocalDate end);
}
