package com.fsquiroz.campsite.service.validate;

import com.fsquiroz.campsite.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ValidateServiceImpl implements ValidateService {

    @Override
    public void isValidId(Long id) {
        if (id == null) {
            throw BadRequestException.byInvalidId();
        }
    }

    @Override
    public void isNotNull(@NonNull String param, Object o) {
        if (o == null) {
            throw BadRequestException.byMissingParam(param);
        }
    }

    @Override
    public void isNotEmpty(String param, String s) {
        if (StringUtils.isBlank(s)) {
            throw BadRequestException.byMissingParam(param);
        }
    }

    @Override
    public void isNotEmptyWithinSize(String param, int maxSize, String s) {
        isNotEmpty(param, s);
        if (s.length() > maxSize) {
            throw BadRequestException.byTextTooLong(param, s, maxSize);
        }
    }

    @Override
    public void isValidRange(@NonNull String startParam, @NonNull LocalDate start, @NonNull String endParam, @NonNull LocalDate end) {
        if (start.isAfter(end)) {
            throw BadRequestException.byInvalidRange(startParam, start, endParam, end);
        }
    }
}
