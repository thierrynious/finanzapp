package com.finanzmanager.finanzapp.repository;

import com.finanzmanager.finanzapp.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    boolean existsByDateAndAmountAndTitle(
            LocalDate date,
            BigDecimal amount,
            String title
    );

    Page<Transaction> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );

    Page<Transaction> findByIncome(
            boolean income,
            Pageable pageable
    );

    Page<Transaction> findByTitleContainingIgnoreCaseAndIncome(
            String title,
            boolean income,
            Pageable pageable
    );

    Page<Transaction> findByDateBetween(
            LocalDate from,
            LocalDate to,
            Pageable pageable
    );

    @Query("""
               SELECT t FROM Transaction t
               WHERE t.date = :date
                 AND t.amount = :amount
            """)
    List<Transaction> findPossibleDuplicates(
            @Param("date") LocalDate date,
            @Param("amount") BigDecimal amount
    );

}
