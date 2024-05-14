package com.api.TennisTourney;

import com.api.TennisTourney.controller.RegistrationTourController;
import com.api.TennisTourney.model.Tournament;
import com.api.TennisTourney.model.User;
import com.api.TennisTourney.service.RegistrationTourService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationTourControllerTest {

    @InjectMocks
    private RegistrationTourController registrationTourController;

    @Mock
    private RegistrationTourService registrationTourService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Test
    void testShowAvailableTournaments() {
        User user = new User();
        user.setId(1L);
        when(session.getAttribute("user")).thenReturn(user);
        List<Tournament> tournaments = List.of(new Tournament());
        when(registrationTourService.getUnregisteredTournaments(user.getId())).thenReturn(tournaments);

        // Execute
        String view = registrationTourController.showAvailableTournaments(session, model);

        // Verify
        verify(model).addAttribute("tournaments", tournaments);
        assertEquals("register-tournament", view);
    }

    @Test
    void testRegisterForTournament() {
        User user = new User();
        user.setId(1L);
        when(session.getAttribute("user")).thenReturn(user);
        Long tournamentId = 2L;

        // Execute
        String view = registrationTourController.registerForTournament(tournamentId, session, redirectAttributes);

        // Verify
        verify(registrationTourService).registerUserToTournament(user.getId(), tournamentId);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Registered successfully!");
        assertEquals("redirect:/register-tournament", view);
    }
}
