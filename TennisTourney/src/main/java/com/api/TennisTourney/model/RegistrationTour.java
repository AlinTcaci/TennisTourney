package com.api.TennisTourney.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.Mapping;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "registration_tours")
public class RegistrationTour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", referencedColumnName = "tournament_id") // Foreign key to Tournament
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id") // Foreign key to User
    private User user;

}
