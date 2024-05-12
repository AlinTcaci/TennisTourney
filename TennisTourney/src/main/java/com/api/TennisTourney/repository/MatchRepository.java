package com.api.TennisTourney.repository;

import com.api.TennisTourney.model.Match;
import com.api.TennisTourney.model.Tournament;
import com.api.TennisTourney.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    @Query("SELECT m FROM Match m WHERE m.player1.id = :userId OR m.player2.id = :userId")
    List<Match> findMatchByPlayerId(Long userId);
    List<Match> findMatchesByRefereeId(Long refereeId);

    @Query("SELECT m FROM Match m WHERE m.referee.id = :refereeId AND " +
            "(m.tournament.tournamentID = :tournamentId OR :tournamentId IS NULL) AND " +
            "(m.matchDate >= :fromDate OR :fromDate IS NULL) AND " +
            "(m.matchDate <= :toDate OR :toDate IS NULL) AND " +
            "(m.player1.id = :playerId OR m.player2.id = :playerId OR :playerId IS NULL)")
    List<Match> findFilteredMatchesByRefereeId(Long refereeId, LocalDate fromDate, LocalDate toDate, Long tournamentId, Long playerId);

    @Query("SELECT DISTINCT m.tournament FROM Match m WHERE m.referee.id = :refereeId")
    List<Tournament> findTournamentsByRefereeId(Long refereeId);

    @Query("SELECT DISTINCT u FROM Match m JOIN m.player1 u WHERE m.referee.id = :refereeId " +
            "UNION SELECT DISTINCT u FROM Match m JOIN m.player2 u WHERE m.referee.id = :refereeId")
    List<User> findPlayersByRefereeId(Long refereeId);
}
