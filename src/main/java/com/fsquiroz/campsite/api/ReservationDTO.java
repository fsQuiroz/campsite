package com.fsquiroz.campsite.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDTO {

    private Long id;

    private Instant created;

    private Instant updated;

    private Instant deleted;

    private String name;

    private String email;

    private LocalDate arrival;

    private LocalDate departure;
}
