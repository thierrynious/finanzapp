package com.finanzmanager.finanzapp.controller;

import com.finanzmanager.finanzapp.models.BankAccount;
import com.finanzmanager.finanzapp.service.BankAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    private final BankAccountService service;

    public BankAccountController(BankAccountService service) {
        this.service = service;
    }

    @GetMapping
    public List<BankAccount> getAllAccounts() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> get(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Konto nicht gefunden"));
    }

    @PostMapping
    public ResponseEntity<BankAccount> createAccount(@Valid @RequestBody BankAccount account) {
        return ResponseEntity.ok(service.create(account));
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<BankAccount> deposit(@PathVariable Long id, @RequestParam double amount) {
        return service.deposit(id, amount)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<BankAccount> withdraw(@PathVariable Long id, @RequestParam double amount) {
        return service.withdraw(id, amount)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/fail")
    public BankAccount failExample() {
        throw new IllegalArgumentException("Das war keine gültige Eingabe");
    }
}
