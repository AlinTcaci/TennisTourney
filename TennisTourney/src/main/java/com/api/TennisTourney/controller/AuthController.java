package com.api.TennisTourney.controller;

import com.api.TennisTourney.exceptions.ResourceAlreadyExists;
import com.api.TennisTourney.model.User;
import com.api.TennisTourney.repository.UserRepository;
import com.api.TennisTourney.service.AuthService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(UserRepository userRepository) {
        this.authService = AuthService.getInstance(userRepository);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupSubmit(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            authService.signupSubmit(user);
            return "redirect:/login";
        } catch (ResourceAlreadyExists e) {
            redirectAttributes.addFlashAttribute("signupError", "This email already has an account.");
            return "redirect:/signup";
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute User loginUser, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = authService.validateUser(loginUser.getEmail(), loginUser.getPassword());
        if (user != null) {
            session.setAttribute("user", user);
            // Redirect based on the role of the user
            return "redirect:/" + user.getRole().toLowerCase() + "/home";
        } else {
            redirectAttributes.addFlashAttribute("loginError", "Invalid credentials");
            return "redirect:/login";
        }
    }

}
