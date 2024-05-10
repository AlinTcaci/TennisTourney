package com.api.TennisTourney.service;

import com.api.TennisTourney.model.Tournament;
import com.api.TennisTourney.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public void saveTournament(Tournament tournament) {
        tournamentRepository.save(tournament);
    }

}
