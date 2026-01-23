package com.finanzmanager.finanzapp.controller;

import com.finanzmanager.finanzapp.dto.DashboardDTO;
import com.finanzmanager.finanzapp.dto.TransactionDTO;
import com.finanzmanager.finanzapp.models.Transaction;
import com.finanzmanager.finanzapp.repository.TransactionRepository;
import com.finanzmanager.finanzapp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionRepository repo;
    private final TransactionService transactionService;

    @GetMapping("/dashboard")
    public DashboardDTO dashboard() {
        return transactionService.getDashboard();
    }

    @GetMapping
    public Page<TransactionDTO> getTransactions(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) Boolean income,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        Page<Transaction> pageResult;

        if (income != null && !search.isEmpty()) {
            pageResult = repo.findByTitleContainingIgnoreCaseAndIncome(search, income, pageable);
        } else if (income != null) {
            pageResult = repo.findByIncome(income, pageable);
        } else if (!search.isEmpty()) {
            pageResult = repo.findByTitleContainingIgnoreCase(search, pageable);
        } else {
            pageResult = repo.findAll(pageable);
        }

        return pageResult.map(tx -> TransactionDTO.builder()
                .id(tx.getId())
                .title(tx.getTitle())
                .amount(tx.getAmount())
                .date(tx.getDate())
                .income(tx.isIncome()) // 🔥 kommt als isIncome im JSON
                .note(tx.getNote())
                .createdBy(tx.getCreatedBy())
                .updatedBy(tx.getUpdatedBy())
                .createdAt(tx.getCreatedAt())
                .updatedAt(tx.getUpdatedAt())
                .build());
    }


    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody Transaction transaction,
            @RequestParam(defaultValue = "false") boolean force
    ) {
        if (transaction.getAmount() == null) {
            return ResponseEntity.badRequest().body("Amount darf nicht null sein");
        }

        // ❗ amount bleibt wie gesendet (+ / -)
        transaction.setIncome(transaction.getAmount().signum() > 0);

        var duplicates = transactionService.findPossibleDuplicates(transaction);

        if (!duplicates.isEmpty() && !force) {
            return ResponseEntity.status(409).body(Map.of(
                    "message", "Mögliches Duplikat gefunden",
                    "duplicates", duplicates
            ));
        }

        return ResponseEntity.ok(transactionService.save(transaction));
    }


    @PutMapping("/{id}")
    public Transaction update(
            @PathVariable Long id,
            @RequestBody Transaction updated
    ) {
        return repo.findById(id)
                .map(tx -> {
                    tx.setTitle(updated.getTitle());
                    tx.setAmount(updated.getAmount().abs());
                    tx.setIncome(updated.isIncome());
                    tx.setDate(updated.getDate());
                    tx.setNote(updated.getNote());
                    return repo.save(tx);
                })
                .orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
