package com.finanzmanager.finanzapp.repository;

import com.finanzmanager.finanzapp.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByTitleContainingIgnoreCase(String keyword);

    List<Transaction> findByDateBetween(LocalDate start, LocalDate end);

    List<Transaction> findByIsIncome(boolean isIncome);
}
