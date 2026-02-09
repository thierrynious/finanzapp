package com.finanzmanager.finanzapp.service;

import com.finanzmanager.finanzapp.exception.TransactionNotFoundException;
import com.finanzmanager.finanzapp.model.Transaction;
import com.finanzmanager.finanzapp.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> getAll() {
        return repository.findAll();
    }

    public Transaction save(Transaction transaction) {
        log.info("Saving transaction {}", transaction.getTitle());
        if(transaction.getAmount()<=0){
            log.warn("Ungültiger Betrag: {}", transaction.getAmount());
        }
        return repository.save(transaction);
    }

    public Transaction getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public List<Transaction> searchByTitle(String title) {
        return repository.findByTitleContainingIgnoreCase(title);
    }
}
