package com.finanzmanager.finanzapp.service;

import com.finanzmanager.finanzapp.dto.DashboardDTO;
import com.finanzmanager.finanzapp.dto.TransactionDTO;
import com.finanzmanager.finanzapp.models.Transaction;
import com.finanzmanager.finanzapp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository repository;

    // ==========================
    // DASHBOARD
    // ==========================
    public DashboardDTO getDashboard() {

        List<Transaction> list = repository.findAll();

        BigDecimal income = list.stream()
                .filter(Transaction::isIncome)
                .map(Transaction::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expenses = list.stream()
                .filter(tx -> !tx.isIncome())
                .map(Transaction::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = income.subtract(expenses);

        List<TransactionDTO> latest = list.stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .limit(5)
                .map(tx -> TransactionDTO.builder()
                        .id(tx.getId())
                        .title(tx.getTitle())
                        .amount(tx.getAmount())
                        .date(tx.getDate())
                        .income(tx.isIncome())
                        .note(tx.getNote())
                        .createdBy(tx.getCreatedBy())
                        .updatedBy(tx.getUpdatedBy())
                        .createdAt(tx.getCreatedAt())
                        .updatedAt(tx.getUpdatedAt())
                        .build()
                )
                .collect(Collectors.toList());

        return DashboardDTO.builder()
                .balance(balance)
                .totalIncome(income)
                .totalExpenses(expenses)
                .latestTransactions(latest)
                .build();
    }

    // ==========================
    // SAVE
    // ==========================
    public Transaction save(Transaction tx) {

        if (tx.getAmount() == null) {
            throw new IllegalArgumentException("Amount darf nicht null sein");
        }

        // ✅ EINZIGE REGEL
        tx.setIncome(tx.getAmount().signum() > 0);

        if (tx.getCreatedAt() == null) {
            tx.setCreatedAt(LocalDateTime.now());
        }

        tx.setUpdatedAt(LocalDateTime.now());

        return repository.save(tx);
    }


    // ==========================
    // DUPLIKATE
    // ==========================
    public List<Transaction> saveAllUnique(List<Transaction> parsedList) {

        List<Transaction> saved = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Transaction tx : parsedList) {

            if (tx.getDate() == null || tx.getAmount() == null || tx.getTitle() == null) {
                continue;
            }

            boolean exists = repository.existsByDateAndAmountAndTitle(
                    tx.getDate(),
                    tx.getAmount().abs(),
                    tx.getTitle()
            );

            if (exists) continue;

            tx.setAmount(tx.getAmount().abs());
            tx.setCreatedAt(now);
            tx.setUpdatedAt(now);

            saved.add(repository.save(tx));
        }
        return saved;
    }

    public List<Transaction> findPossibleDuplicates(Transaction tx) {
        return repository.findPossibleDuplicates(
                tx.getDate(),
                tx.getAmount().abs()
        );
    }
}
