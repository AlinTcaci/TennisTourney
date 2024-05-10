package com.api.TennisTourney.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tournaments")
public class Tournament {
    @Id
    @Column(name = "tournament_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tournamentID;

    @Column(name = "tournament_name", nullable = false)
    private String tournamentName;

    @Column(name = "tournament_location", nullable = false)
    private String tournamentLocation;

}
