package com.api.TennisTourney;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.api.TennisTourney.exceptions.ResourceAlreadyExists;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.TennisTourney.controller.UserController;
import com.api.TennisTourney.model.User;
import com.api.TennisTourney.service.AuthService;
import com.api.TennisTourney.service.TournamentService;
import com.api.TennisTourney.service.UserService;

import java.util.Arrays;
import java.util.List;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    private HttpSession session;
    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setPassword("123456");
        newUser.setName("Test User");

        doNothing().when(userService).addNewUser(any(User.class));

        userController.registerUser(newUser, redirectAttributes);

        verify(userService).addNewUser(any(User.class));
        verify(redirectAttributes).addFlashAttribute("successMessage", "Registered successfully!");
    }


    @Test
    void testRegisterUser_UserAlreadyExists() {
        User newUser = new User();
        newUser.setEmail("existing@example.com");
        newUser.setPassword("123456");
        newUser.setName("Existing User");

        // Ensure this matches the condition expected to throw the exception
        doThrow(new IllegalStateException("Email already taken")).when(userService).addNewUser(any(User.class));

        // Test exception throwing
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            userController.registerUser(newUser, redirectAttributes);
        });

        assertEquals("Email already taken", exception.getMessage());
        verify(redirectAttributes, never()).addFlashAttribute("successMessage", "Registered successfully!");
    }

    @Test
    void testUpdateAccount_Success() throws ResourceAlreadyExists {
        // Setup
        User updatedUser = new User();
        updatedUser.setEmail("user@example.com");
        updatedUser.setPassword("newpassword");
        updatedUser.setName("Updated Name");

        // Mock behavior
        doNothing().when(authService).updateUser(anyLong(), any(User.class));

        // Execute
        String view = userController.updateAccount(updatedUser, 1L, redirectAttributes);

        // Verify
        verify(authService).updateUser(1L, updatedUser);
        verify(redirectAttributes).addFlashAttribute("updateSuccess", "Account updated successfully!");
        assertEquals("redirect:/update-account", view);
    }

    @Test
    void testUpdateAccount_DuplicateEmail() throws ResourceAlreadyExists {
        // Setup
        User updatedUser = new User();
        updatedUser.setEmail("duplicate@example.com");
        updatedUser.setPassword("newpassword");
        updatedUser.setName("Updated Name");

        // Mock behavior
        doThrow(new ResourceAlreadyExists("Email already taken.")).when(authService).updateUser(anyLong(), any(User.class));

        // Execute
        String view = userController.updateAccount(updatedUser, 1L, redirectAttributes);

        // Verify
        verify(authService).updateUser(1L, updatedUser);
        verify(redirectAttributes).addFlashAttribute("updateError", "Email already taken.");
        assertEquals("redirect:/update-account", view);
    }

    @Test
    void testDisplayAllUsersForm_Success() {
        // Setup
        User adminUser = new User();  // Assuming there's a user object that represents an admin
        adminUser.setEmail("admin@example.com");
        List<User> allUsers = Arrays.asList(new User(), new User()); // Mock some users

        when(session.getAttribute("user")).thenReturn(adminUser);
        when(userService.getUsers()).thenReturn(allUsers);

        // Execute
        String view = userController.displayAllUsersForm(session, model);

        // Verify
        verify(session).getAttribute("user");
        verify(userService).getUsers();
        verify(model).addAttribute("users", allUsers);
        assertEquals("see-users", view);
    }

    @Test
    void testDeleteUser_Success() {
        // Given
        Long userId = 1L;
        doNothing().when(userService).deleteUserById(userId);

        // When
        String view = userController.deleteUser(userId, redirectAttributes);

        // Then
        verify(userService).deleteUserById(userId);
        verify(redirectAttributes).addFlashAttribute("deleteSuccess", "User deleted successfully!");
        assertEquals("redirect:/see-users", view);
    }


}
