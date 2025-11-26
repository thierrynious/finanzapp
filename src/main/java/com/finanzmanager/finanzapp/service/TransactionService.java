package com.finanzmanager.finanzapp.service;

import com.finanzmanager.finanzapp.config.AppProperties;
import com.finanzmanager.finanzapp.models.Transaction;
import com.finanzmanager.finanzapp.repository.TransactionRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final AppProperties appProperties;
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    // Konstruktor
    public TransactionService(TransactionRepository repository, AppProperties appProperties) {
        this.repository = repository;
        this.appProperties = appProperties;
    }

    // Wird direkt nach der Bean-Erstellung aufgerufen
    @PostConstruct
    public void init() {
        log.info("{}gestartet. Max. Transaktionen:{}, Währung:{}",
                appProperties.getName(),
                appProperties.getMaxTransactions(),
                appProperties.getDefaultCurrency());
    }

    // get paged
    public Page<Transaction> getPaged(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // get paged sorted
    public Page<Transaction> getPagedSorted(int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable);
    }

    public Transaction save(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction darf nicht null sein!");
        }

        if (transaction.getAmount() == null) {
            log.error("Transaction '{}' hat keinen Betrag! Abbruch.", transaction.getTitle());
            throw new IllegalArgumentException("Betrag darf nicht null sein!");
        }

        if (transaction.getAmount().signum() <= 0) {
            log.warn("Ungültiger oder fehlender Betrag: {}", transaction.getAmount());
            throw new IllegalArgumentException("Betrag darf nicht null oder negativ sein");
        }

        log.info("Speichere Transaktion: {}", transaction);
        return repository.save(transaction);
    }

    public List<Transaction> getAll() {
        return repository.findAll();
    }

    public Optional<Transaction> getById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Transaction> searchByTitle(String keyword) {
        return repository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Transaction> filterByDate(LocalDate from, LocalDate to) {
        return repository.findByDateBetween(from, to);
    }

    public List<Transaction> getIncomes() {
        return repository.findByIsIncome(true);
    }

    public List<Transaction> getExpenses() {
        return repository.findByIsIncome(false);
    }
}
