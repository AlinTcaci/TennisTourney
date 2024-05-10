package com.api.TennisTourney.repository;

import com.api.TennisTourney.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    @Query("SELECT m FROM Match m WHERE m.player1.id = :userId OR m.player2.id = :userId")
    List<Match> findMatchByPlayerId(Long userId);
    List<Match> findMatchesByRefereeId(Long refereeId);
}
