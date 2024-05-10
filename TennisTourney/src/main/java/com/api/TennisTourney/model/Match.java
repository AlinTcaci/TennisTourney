package com.api.TennisTourney.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", referencedColumnName = "tournament_id")
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "player1_id", referencedColumnName = "user_id", nullable = false)
    private User player1;

    @ManyToOne
    @JoinColumn(name = "player2_id", referencedColumnName = "user_id", nullable = false)
    private User player2;

    @ManyToOne
    @JoinColumn(name = "referee_id", referencedColumnName = "user_id", nullable = false)
    private User referee;

    @Column(name = "match_date", nullable = false)
    private LocalDate matchDate;

    @Column(name = "score", nullable = false)
    private String score;

}
