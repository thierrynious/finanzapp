package com.finanzmanager.finanzapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthDemoController {

    @GetMapping("/api/auth/me")
    public String getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return """
                      User:%s
                      Authorities:%s
                """.formatted(
                auth.getName(),
                auth.getAuthorities(),
                auth.isAuthenticated()
        );
    }

    @GetMapping("/api/auth/fake")
    public String fakeLogin() {

        Authentication fakeAuth = new org.springframework.security.authentication.
                UsernamePasswordAuthenticationToken("demo-user@finanzapp.com",
                "nopassword",
                java.util.List.of()
        );

        SecurityContextHolder.getContext().setAuthentication(fakeAuth);

        return "Fake User gesetzt!";
    }
}
