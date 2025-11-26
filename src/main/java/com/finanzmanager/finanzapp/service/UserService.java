package com.finanzmanager.finanzapp.service;

import com.finanzmanager.finanzapp.models.User;
import com.finanzmanager.finanzapp.repository.UserRepository;
import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Validated
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> getUserByUsername(String username) {
        log.debug("Versuche Benutzer mit Username '{}' zu finden", username);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            log.info("Benutzer '{}' gefunden", username);
        } else {
            log.warn("Benutzer '{}' nicht gefunden", username);
        }
        return user;
    }

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(@Valid User user) {
        log.info("Registriere Benutzer: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
