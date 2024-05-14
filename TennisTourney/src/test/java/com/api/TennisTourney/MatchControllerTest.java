package com.api.TennisTourney;

import com.api.TennisTourney.controller.MatchController;
import com.api.TennisTourney.model.Match;
import com.api.TennisTourney.model.User;
import com.api.TennisTourney.service.MatchService;
import com.api.TennisTourney.service.TournamentService;
import com.api.TennisTourney.service.UserService;
import com.api.TennisTourney.service.RegistrationTourService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MatchControllerTest {

    @InjectMocks
    private MatchController matchController;

    @Mock
    private MatchService matchService;

    @Mock
    private TournamentService tournamentService;

    @Mock
    private UserService userService;

    @Mock
    private RegistrationTourService registrationTourService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Test
    void testCreateMatch() {
        Match match = new Match();

        // Execute
        String view = matchController.createMatch(match, redirectAttributes);

        // Verify
        verify(matchService).saveMatch(match);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Match created successfully!");
        assertEquals("redirect:/create-match", view);
    }

    @Test
    void testViewSchedulePlayer() {
        User user = new User();
        user.setId(1L);
        when(session.getAttribute("user")).thenReturn(user);
        List<Match> matches = Collections.singletonList(new Match());
        when(matchService.getMatchesByUserId(user.getId())).thenReturn(matches);

        // Execute
        String view = matchController.viewSchedule(session, model);

        // Verify
        verify(model).addAttribute("matches", matches);
        assertEquals("view-schedule-player", view);
    }

    @Test
    void testViewRefereeSchedule() {
        User user = new User();
        user.setId(1L);
        when(session.getAttribute("user")).thenReturn(user);
        List<Match> matches = Collections.singletonList(new Match());
        when(matchService.getFilteredMatchesForReferee(eq(user.getId()), any(), any(), any(), any())).thenReturn(matches);

        // Execute
        String view = matchController.viewRefereeSchedule(session, model, null, null, null, null);

        // Verify
        verify(model).addAttribute("matches", matches);
        assertEquals("view-schedule-referee", view);
    }

    @Test
    void testUpdateMatchScore_Success() {
        Map<String, String> params = Map.of("score-1", "3-2");
        Long matchId = 1L;

        // Execute
        String view = matchController.updateMatchScore(matchId, params, redirectAttributes);

        // Verify
        verify(matchService).updateMatchScore(matchId, "3-2");
        verify(redirectAttributes).addFlashAttribute("successMessage", "Score updated successfully!");
        assertEquals("redirect:/view-schedule-referee", view);
    }

    @Test
    void testUpdateMatchScore_Fail() {
        Map<String, String> params = Map.of();
        Long matchId = 1L;
        doThrow(new RuntimeException("Database error")).when(matchService).updateMatchScore(eq(matchId), any());

        // Execute
        String view = matchController.updateMatchScore(matchId, params, redirectAttributes);

        // Verify
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Failed to update score: Database error");
        assertEquals("redirect:/view-schedule-referee", view);
    }


}