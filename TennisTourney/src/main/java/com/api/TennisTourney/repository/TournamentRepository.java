package com.api.TennisTourney.repository;

import com.api.TennisTourney.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    Tournament getTournamentByTournamentID(Long tournamentId);
}
