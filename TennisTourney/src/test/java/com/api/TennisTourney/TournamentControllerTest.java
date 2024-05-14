package com.api.TennisTourney;

import com.api.TennisTourney.controller.TournamentController;
import com.api.TennisTourney.model.Tournament;
import com.api.TennisTourney.service.RegistrationTourService;
import com.api.TennisTourney.service.TournamentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TournamentControllerTest {

    @InjectMocks
    private TournamentController tournamentController;

    @Mock
    private TournamentService tournamentService;

    @Mock
    private RegistrationTourService registrationTourService;

    @Mock
    private Model model;

    @Mock
    private BindingResult result;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Test
    void testCreateTournament_Success() {
        // Given
        Tournament tournament = new Tournament();
        when(result.hasErrors()).thenReturn(false);

        // When
        String view = tournamentController.createTournament(tournament, result, redirectAttributes);

        // Then
        verify(tournamentService).saveTournament(tournament);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Tournament created successfully!");
        assertEquals("redirect:/create-tournament", view);
    }

    @Test
    void testApproveRegistration() {
        // Given
        Long registrationId = 1L;

        // When
        String view = tournamentController.approveRegistration(registrationId, redirectAttributes);

        // Then
        verify(registrationTourService).updateRegistrationStatus(registrationId, "accepted");
        verify(redirectAttributes).addFlashAttribute("successMessage", "Registration approved and email notification sent.");
        assertEquals("redirect:/pending-registrations", view);
    }

    @Test
    void testDenyRegistration() {
        // Given
        Long registrationId = 1L;

        // When
        String view = tournamentController.denyRegistration(registrationId, redirectAttributes);

        // Then
        verify(registrationTourService).updateRegistrationStatus(registrationId, "denied");
        verify(redirectAttributes).addFlashAttribute("successMessage", "Registration denied and email notification sent.");
        assertEquals("redirect:/pending-registrations", view);
    }

}
