package com.fsquiroz.campsite.mapper;

import com.fsquiroz.campsite.api.DayAvailabilityDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DayAvailabilityMapper extends Mapper<Map<LocalDate, DayAvailabilityDTO>, Map<String, DayAvailabilityDTO>> {

    @Override
    public Map<String, DayAvailabilityDTO> map(Map<LocalDate, DayAvailabilityDTO> availability) {
        Map<String, DayAvailabilityDTO> mapped = new LinkedHashMap<>();
        availability.forEach((d, a) -> mapped.put(d.toString(), a));
        return mapped;
    }
}
