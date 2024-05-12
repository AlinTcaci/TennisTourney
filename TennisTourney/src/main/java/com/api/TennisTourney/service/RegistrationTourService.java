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
    private final TournamentRepository tournamentRepository;
    private final UserService userService;

    private final EmailService emailService;

    @Autowired
    public RegistrationTourService(RegistrationTourRepository registrationTourRepository, TournamentRepository tournamentRepository, UserService userService, EmailService emailService) {
        this.registrationTourRepository = registrationTourRepository;
        this.tournamentRepository = tournamentRepository;
        this.userService = userService;
        this.emailService = emailService;

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
            registration.setStatus("pending");  // Explicitly setting the status to pending
            registrationTourRepository.save(registration);
        } else {
            throw new IllegalStateException("User already registered for this tournament");
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

    public List<RegistrationTour> findRegistrationsByStatus(String pending) {
        return registrationTourRepository.findByStatus(pending);
    }

    public void updateRegistrationStatus(Long registrationId, String status) {
        RegistrationTour registration = registrationTourRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found for ID: " + registrationId));

        String oldStatus = registration.getStatus();
        registration.setStatus(status);
        registrationTourRepository.save(registration);

        // Prepare email notification
        String subject = "Tournament Registration Status Update";
        String message = String.format("Dear %s,\n\nYour registration for the tournament '%s' has been %s.",
                registration.getUser().getName(),
                registration.getTournament().getTournamentName(),
                status.toLowerCase());

        // Send email
        emailService.sendingMail(registration.getUser().getEmail(), subject, message);
    }



    public List<User> getAcceptedUsersByTournament(Long tournamentId) {
        return registrationTourRepository.findByTournament_TournamentIDAndStatus(tournamentId, "accepted").stream()
                .map(RegistrationTour::getUser)
                .collect(Collectors.toList());
    }
}
