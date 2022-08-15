package com.fsquiroz.campsite.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created", nullable = false)
    private Instant created;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "deleted")
    private Instant deleted;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "arrival", nullable = false)
    private LocalDate arrival;

    @Column(name = "departure", nullable = false)
    private LocalDate departure;
}
