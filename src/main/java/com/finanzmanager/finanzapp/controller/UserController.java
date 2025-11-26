package com.finanzmanager.finanzapp.controller;

import com.finanzmanager.finanzapp.models.User;
import com.finanzmanager.finanzapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // POST /api/users
    @PostMapping
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        service.register(user);
        return ResponseEntity.ok("Registrierung erfolgreich");
    }

    // GET /api/users?username=testuser
    @GetMapping
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        return service.getUserByUsername(username)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Benutzer nicht gefunden"));
    }
}
