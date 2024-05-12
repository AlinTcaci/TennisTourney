package com.api.TennisTourney.service;

import com.api.TennisTourney.model.Match;
import com.api.TennisTourney.model.Tournament;
import com.api.TennisTourney.model.User;
import com.api.TennisTourney.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class MatchService {
    private final MatchRepository matchRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }


    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public void saveMatch(Match match) {
        matchRepository.save(match);
    }

    public List<Match> getMatchesByUserId(Long userId) {
        return matchRepository.findMatchByPlayerId(userId);
    }

    public List<Match> getMatchesForReferee(Long refereeId) {
        return matchRepository.findMatchesByRefereeId(refereeId);
    }

    public void updateMatchScore(Long matchId, String newScore) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid match ID: " + matchId));
        match.setScore(newScore);
        matchRepository.save(match);
    }

    public List<Match> getFilteredMatchesForReferee(Long refereeId, LocalDate fromDate, LocalDate toDate, Long tournamentId, Long playerId) {
        return matchRepository.findFilteredMatchesByRefereeId(refereeId, fromDate, toDate, tournamentId, playerId);
    }

    public List<Tournament> getTournamentsForReferee(Long refereeId) {
        return matchRepository.findTournamentsByRefereeId(refereeId);
    }

    public List<User> getPlayersForReferee(Long refereeId) {
        return matchRepository.findPlayersByRefereeId(refereeId);
    }

}
