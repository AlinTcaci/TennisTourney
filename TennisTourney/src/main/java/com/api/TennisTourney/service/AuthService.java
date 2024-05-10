package com.api.TennisTourney.service;

import com.api.TennisTourney.exceptions.ResourceAlreadyExists;
import com.api.TennisTourney.model.User;
import com.api.TennisTourney.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static AuthService instance;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public static synchronized AuthService getInstance(UserRepository userRepository) {
        if (instance == null) {
            instance = new AuthService(userRepository);
        }
        return instance;
    }

    public void signupSubmit(User user) throws ResourceAlreadyExists {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new ResourceAlreadyExists("This email already has an account.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User validateUser(String email, String password) {
        return userRepository.findUserByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(null);
    }

    public void updateUser(Long userId, User newUserDetails) throws ResourceAlreadyExists {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (!newUserDetails.getEmail().equals(existingUser.getEmail())
                && userRepository.findUserByEmail(newUserDetails.getEmail()).isPresent()) {
            throw new ResourceAlreadyExists("This email is already used by another account.");
        }

        existingUser.setName(newUserDetails.getName());
        existingUser.setEmail(newUserDetails.getEmail());

        if (!newUserDetails.getPassword().isEmpty() && !passwordEncoder.matches(newUserDetails.getPassword(), existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(newUserDetails.getPassword()));
        }

        existingUser.setRole(newUserDetails.getRole());

        userRepository.save(existingUser);
    }


}