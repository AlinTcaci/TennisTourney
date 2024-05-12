package com.api.TennisTourney.controller;

import com.api.TennisTourney.service.EmailService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        emailService.sendingMail(to, subject, text);
        return "Email sent successfully";
    }
}
