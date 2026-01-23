// src/main/java/com/finanzmanager/finanzapp/controller/AuthController.java
package com.finanzmanager.finanzapp.controller;

import com.finanzmanager.finanzapp.dto.JwtResponse;
import com.finanzmanager.finanzapp.dto.LoginRequest;
import com.finanzmanager.finanzapp.models.AppUser;
import com.finanzmanager.finanzapp.repository.UserRepository;
import com.finanzmanager.finanzapp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {

        AppUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new JwtResponse(token);
    }
}

