package com.fsquiroz.campsite.mapper;

import com.fsquiroz.campsite.api.DayAvailabilityDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DayAvailabilityMapperUnitTest {

    @Test
    public void giveAvailabilityWhenMapThenMapped() {
        Map<LocalDate, DayAvailabilityDTO> availability = new LinkedHashMap<>();
        availability.put(LocalDate.parse("2022-08-01"), DayAvailabilityDTO.builder().available(false).build());
        availability.put(LocalDate.parse("2022-08-02"), DayAvailabilityDTO.builder().available(true).build());
        availability.put(LocalDate.parse("2022-08-03"), DayAvailabilityDTO.builder().available(false).build());

        DayAvailabilityMapper mapper = new DayAvailabilityMapper();

        Map<String, DayAvailabilityDTO> mapped = mapper.map(availability);

        assertThat(mapped)
                .isNotNull()
                .hasSize(3);

        assertThat(mapped.get("2022-08-01"))
                .hasFieldOrPropertyWithValue("available", false);
        assertThat(mapped.get("2022-08-02"))
                .hasFieldOrPropertyWithValue("available", true);
        assertThat(mapped.get("2022-08-03"))
                .hasFieldOrPropertyWithValue("available", false);
    }
}
