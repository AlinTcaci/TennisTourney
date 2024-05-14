package com.api.TennisTourney;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.api.TennisTourney.controller.EmailController;
import com.api.TennisTourney.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class EmailControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private EmailController emailController;

    @Mock
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
    }

    @Test
    public void testSendEmail() throws Exception {
        mockMvc.perform(post("/sendEmail")
                        .param("to", "test@example.com")
                        .param("subject", "Subject")
                        .param("text", "Message body"))
                .andExpect(status().isOk())
                .andExpect(content().string("Email sent successfully"));

        verify(emailService).sendingMail("test@example.com", "Subject", "Message body");
    }
}
