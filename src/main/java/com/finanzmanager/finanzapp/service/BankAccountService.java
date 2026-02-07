package com.finanzmanager.finanzapp.service;

import com.finanzmanager.finanzapp.exception.BankAccountNotFoundException;
import com.finanzmanager.finanzapp.model.BankAccount;
import com.finanzmanager.finanzapp.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    private final BankAccountRepository repository;

    public BankAccountService(BankAccountRepository repository) {
        this.repository = repository;
    }

    public BankAccount create() {
        BankAccount account = new BankAccount();
        account.setBalance(0.0);
        return repository.save(account);
    }

    public BankAccount applyTransaction(Long id, double amount) {
        BankAccount account = repository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException(id));

        account.applyTransaction(amount);
        return repository.save(account);
    }

    public BankAccount getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BankAccountNotFoundException(id));
    }
}
