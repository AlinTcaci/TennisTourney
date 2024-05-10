package com.api.TennisTourney.controller;

import com.api.TennisTourney.model.Tournament;
import com.api.TennisTourney.model.User;
import com.api.TennisTourney.service.RegistrationTourService;
import com.api.TennisTourney.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RegistrationTourController {
    private final RegistrationTourService registrationTourService;

    public RegistrationTourController(RegistrationTourService registrationTourService) {
        this.registrationTourService = registrationTourService;
    }

    @GetMapping("/register-tournament")
    public String showAvailableTournaments(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Tournament> unregisteredTournaments = registrationTourService.getUnregisteredTournaments(user.getId());
        model.addAttribute("tournaments", unregisteredTournaments);
        return "register-tournament";
    }

    @PostMapping("/register-tournament")
    public String registerForTournament(@RequestParam("tournamentId") Long tournamentId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        registrationTourService.registerUserToTournament(user.getId(), tournamentId);
        redirectAttributes.addFlashAttribute("successMessage", "Registered successfully!");
        return "redirect:/register-tournament";
    }
}
