package com.finanzmanager.finanzapp.service;

import com.finanzmanager.finanzapp.models.BankAccount;
import com.finanzmanager.finanzapp.repository.BankAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {

    private final BankAccountRepository repo;

    public BankAccountService(BankAccountRepository repo) {
        this.repo = repo;
    }

    public List<BankAccount>findAll(){ return repo.findAll();
    }

    public Optional<BankAccount> findById(Long id){
        return repo.findById(id);
    }

    public BankAccount create(BankAccount account){
        return repo.save(account);
    }

    public Optional<BankAccount> deposit(Long id, double amount){
        return repo.findById(id).map(account->{account.deposit(amount);
            return repo.save(account);
        });
    }

    public Optional<BankAccount> withdraw(Long id, double amount){
        return repo.findById(id).map(account->{account.withdraw(amount);
        return repo.save(account);
        });
    }

    public boolean delete(Long id){
        if(repo.existsById(id)){
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public BankAccount getOrThrow(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Konto mit ID " + id + " existiert nicht"));
    }

    @ExceptionHandler
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
