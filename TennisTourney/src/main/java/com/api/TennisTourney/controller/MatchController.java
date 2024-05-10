package com.api.TennisTourney.controller;

import com.api.TennisTourney.model.Match;
import com.api.TennisTourney.model.User;
import com.api.TennisTourney.service.MatchService;
import com.api.TennisTourney.service.RegistrationTourService;
import com.api.TennisTourney.service.TournamentService;
import com.api.TennisTourney.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class MatchController {
    private final MatchService matchService;
    private final TournamentService tournamentService;
    private final UserService userService;
    private final RegistrationTourService registrationTourService; // Assuming there is a RegistrationTourService

    public MatchController(MatchService matchService, TournamentService tournamentService, UserService userService, RegistrationTourService registrationTourService) {
        this.matchService = matchService;
        this.tournamentService = tournamentService;
        this.userService = userService;
        this.registrationTourService = registrationTourService;
    }

    @GetMapping("/create-match")
    public String showCreateMatchPage(Model model) {
        model.addAttribute("match", new Match());
        model.addAttribute("tournaments", tournamentService.getAllTournaments());
        model.addAttribute("matches", matchService.getAllMatches());
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("registrations", registrationTourService.getAllRegistrations());
        return "create-match";
    }

    @PostMapping("/create-match")
    public String createMatch(@ModelAttribute Match match, RedirectAttributes redirectAttributes) {
        matchService.saveMatch(match);
        redirectAttributes.addFlashAttribute("successMessage", "Match created successfully!");
        return "redirect:/create-match";
    }


    @GetMapping("/view-schedule-player")
    public String viewSchedule(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";  // Redirect to login if the user is not logged in
        }

        List<Match> matches = matchService.getMatchesByUserId(user.getId());
        model.addAttribute("matches", matches);
        return "view-schedule-player"; // the name of the HTML file to render
    }

    @GetMapping("/view-schedule-referee")
    public String viewRefereeSchedule(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";  // redirect to login if no user in session
        }
        List<Match> matches = matchService.getMatchesForReferee(user.getId());
        model.addAttribute("matches", matches);
        return "view-schedule-referee";  // the name of the HTML file to render
    }


    @PostMapping("/update-match-score")
    public String updateMatchScore(@RequestParam Long matchId, @RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes) {
        try {
            String scoreKey = "score-" + matchId;
            String newScore = allParams.get(scoreKey);
            matchService.updateMatchScore(matchId, newScore);
            redirectAttributes.addFlashAttribute("successMessage", "Score updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update score: " + e.getMessage());
        }
        return "redirect:/view-schedule-referee";
    }


}
