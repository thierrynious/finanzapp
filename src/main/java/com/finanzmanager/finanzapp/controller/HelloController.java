package com.finanzmanager.finanzapp.controller;

import com.finanzmanager.finanzapp.models.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Willkommen zur FinanzApp";
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transaction);
    }

}
