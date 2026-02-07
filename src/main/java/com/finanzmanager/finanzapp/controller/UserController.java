package com.finanzmanager.finanzapp.controller;

import com.finanzmanager.finanzapp.model.User;
import com.finanzmanager.finanzapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User saved = service.register(user);
        return ResponseEntity.ok(saved);
    }
}
