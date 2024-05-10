package com.api.TennisTourney.controller;

import com.api.TennisTourney.model.RegistrationTour;
import com.api.TennisTourney.model.Tournament;
import com.api.TennisTourney.model.User;
import com.api.TennisTourney.service.RegistrationTourService;
import com.api.TennisTourney.service.TournamentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class TournamentController {

    private final TournamentService tournamentService;

    private final RegistrationTourService registrationTourService;

    @Autowired
    public TournamentController(TournamentService tournamentService, RegistrationTourService registrationTourService) {
        this.tournamentService = tournamentService;
        this.registrationTourService = registrationTourService;
    }

    @GetMapping("/create-tournament")
    public String showCreateTournamentPage(Model model) {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        model.addAttribute("tournament", new Tournament());  // for the form
        model.addAttribute("tournaments", tournaments);       // for displaying the table
        return "create-tournament";
    }


    @PostMapping("/create-tournament")
    public String createTournament(@ModelAttribute("tournament") Tournament tournament, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Return back to the form if there are errors
            return "create-tournament";
        }
        tournamentService.saveTournament(tournament);
        redirectAttributes.addFlashAttribute("successMessage", "Tournament created successfully!");
        return "redirect:/create-tournament";
    }

    @GetMapping("/tournament/{tournamentId}/registrations")
    @ResponseBody
    public List<User> getRegisteredUsers(@PathVariable Long tournamentId) {
        return registrationTourService.getUsersByTournament(tournamentId);
    }

}
