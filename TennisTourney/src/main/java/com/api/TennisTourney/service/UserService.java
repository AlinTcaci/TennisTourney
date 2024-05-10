package com.api.TennisTourney.service;

import com.api.TennisTourney.model.User;
import com.api.TennisTourney.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not exist"));
    }

    public void addNewUser(User user)
    {
        Optional<User> userOpt = userRepository.findUserByEmail(user.getEmail());
        if (userOpt.isPresent()) {
            throw new IllegalStateException("email taken");
        }

        userRepository.save(user);
    }

    public void deleteUserById(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("User with id " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void updateUser(Long userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not exist"));

        if (user.getName() != null && !Objects.equals(existingUser.getName(), user.getName())) {
            existingUser.setName(user.getName());
        }

        if (user.getEmail() != null && !Objects.equals(existingUser.getEmail(), user.getEmail())) {
            // Check if the new email is not taken
            Optional<User> userByEmail = userRepository.findUserByEmail(user.getEmail());
            if (userByEmail.isPresent()) {
                throw new IllegalStateException("Email already in use.");
            }
            existingUser.setEmail(user.getEmail());
        }

        if (user.getPassword() != null && !Objects.equals(existingUser.getPassword(), user.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getRole() != null && !Objects.equals(existingUser.getRole(), user.getRole())) {
            existingUser.setRole(user.getRole());
        }

        userRepository.save(existingUser);
    }

    public List<User> getReferees() {
        return userRepository.findByRole("REFEREE");
    }
}
