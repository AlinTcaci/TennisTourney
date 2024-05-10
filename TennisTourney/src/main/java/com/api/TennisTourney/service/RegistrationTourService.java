package com.api.TennisTourney.service;

import com.api.TennisTourney.model.RegistrationTour;
import com.api.TennisTourney.model.Tournament;
import com.api.TennisTourney.model.User;
import com.api.TennisTourney.repository.RegistrationTourRepository;
import com.api.TennisTourney.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistrationTourService {

    private final RegistrationTourRepository registrationTourRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private UserService userService;

    @Autowired
    public RegistrationTourService(RegistrationTourRepository registrationTourRepository, TournamentRepository tournamentRepository, UserService userService) {
        this.registrationTourRepository = registrationTourRepository;

    }

    public List<Tournament> getUnregisteredTournaments(Long userId) {
        List<Tournament> allTournaments = tournamentRepository.findAll();
        return allTournaments.stream()
                .filter(t -> !registrationTourRepository.existsByTournament_TournamentIDAndUser_Id( t.getTournamentID(), userId))
                .collect(Collectors.toList());
    }

    public void registerUserToTournament(Long userId, Long tournamentId) {
        if (userId == null || tournamentId == null) {
            throw new IllegalArgumentException("User ID or Tournament ID cannot be null");
        }
        if (!registrationTourRepository.existsByTournament_TournamentIDAndUser_Id(tournamentId, userId)) {
            RegistrationTour registration = new RegistrationTour();
            registration.setUser(userService.getUserById(userId));
            registration.setTournament(tournamentRepository.getTournamentByTournamentID(tournamentId));
            registrationTourRepository.save(registration);
        }
    }

    public Object getAllRegistrations() {
        return registrationTourRepository.findAll();
    }

    public List<User> getUsersByTournament(Long tournamentId) {
        return registrationTourRepository.findByTournament_TournamentID(tournamentId).stream()
                .map(RegistrationTour::getUser)
                .collect(Collectors.toList());
    }

}
