package com.api.TennisTourney.controller;

import com.api.TennisTourney.exceptions.ResourceAlreadyExists;
import com.api.TennisTourney.model.User;
import com.api.TennisTourney.service.AuthService;
import com.api.TennisTourney.service.TournamentService;
import com.api.TennisTourney.service.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    private final TournamentService tournamentService;

    @Autowired
    public UserController(UserService userService, AuthService authService, TournamentService tournamentService) {
        this.userService = userService;
        this.authService = authService;
        this.tournamentService = tournamentService;
    }

    @PostMapping
    public void registerUser(User user, RedirectAttributes redirectAttributes) {
        try {
            userService.addNewUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Registered successfully!");
        } catch (IllegalStateException e) {
            throw e;  // Ensure it propagates for the test to catch
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed: " + e.getMessage());
        }
    }



    @DeleteMapping(path="{userId}")
    public void deleteStudent(@PathVariable("userId") Long id) {
        // TODO enforce it is not null
        userService.deleteUserById(id);
    }

    @GetMapping("/update-account")
    public String showUpdateForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "update-account";
    }

    @PostMapping("/update-account")
    public String updateAccount(@ModelAttribute User updatedUser, @RequestParam("id") Long userId, RedirectAttributes redirectAttributes) {
        try {
            authService.updateUser(userId, updatedUser);
            redirectAttributes.addFlashAttribute("updateSuccess", "Account updated successfully!");
        } catch (ResourceAlreadyExists e) {
            redirectAttributes.addFlashAttribute("updateError", e.getMessage());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("updateError", "User not found.");
        }
        return "redirect:/update-account";
    }

    // Add an exception handler for ResourceAlreadyExists exception
    @ExceptionHandler(ResourceAlreadyExists.class)
    public String handleResourceAlreadyExists(ResourceAlreadyExists e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("signupError", e.getMessage());
        return "redirect:/signup";
    }

    // --------------------------- PLAYER CONTROLLER ---------------------------

    @GetMapping("/player/home")
    public String displayPlayerHomePage (HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if(user!=null) {
            model.addAttribute("user", user);
            return "/player/home";
        } else {
            return "redirect:/login";
        }
    }

    // --------------------------- REFEREE CONTROLLER ---------------------------

    @GetMapping("/referee/home")
    public String displayRefereeHomePage (HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if(user!=null) {
            model.addAttribute("user", user);

            return "/referee/home";
        } else {
            return "redirect:/login";
        }
    }

    // --------------------------- ADMIN CONTROLLER ---------------------------

    @GetMapping("/admin/home")
    public String displayAdminHomePage (HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if(user!=null) {
            model.addAttribute("user", user);
            return "/admin/home";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/see-users")
    public String displayAllUsersForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<User> users = userService.getUsers();
        model.addAttribute("users", users);
        return "see-users";
    }



    @PostMapping("/admin/update-user/{userId}")
    public String updateUser(@ModelAttribute User user, @PathVariable Long userId, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(userId, user);
            redirectAttributes.addFlashAttribute("updateSuccess", "User updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("updateError", e.getMessage());
        }
        return "redirect:/see-users";
    }

    @GetMapping("/admin/delete-user/{userId}")
    public String deleteUser(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUserById(userId);
            redirectAttributes.addFlashAttribute("deleteSuccess", "User deleted successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("deleteError", e.getMessage());
        }
        return "redirect:/see-users";
    }

    @GetMapping("/users/referees")
    @ResponseBody
    public List<User> getReferees() {
        return userService.getReferees();
    }

}
