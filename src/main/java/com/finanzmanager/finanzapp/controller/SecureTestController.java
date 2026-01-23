package com.finanzmanager.finanzapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureTestController {

    @GetMapping("/api/secure/test")
    public String securedEndpoint() {
        return "Zugriff erlaubt – JWT funktioniert!";
    }
}
