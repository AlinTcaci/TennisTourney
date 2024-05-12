package com.api.TennisTourney.repository;

import com.api.TennisTourney.model.RegistrationTour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RegistrationTourRepository extends JpaRepository<RegistrationTour, Long> {
    boolean existsByTournament_TournamentIDAndUser_Id(Long tournamentID, Long userID);
    List<RegistrationTour> findByTournament_TournamentID(Long tournamentId);

    List<RegistrationTour> findByStatus(String pending);
    List<RegistrationTour> findByTournament_TournamentIDAndStatus(Long tournamentId, String status);

}
