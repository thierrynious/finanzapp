package com.finanzmanager.finanzapp.dto;

import com.finanzmanager.finanzapp.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class BankStatementResponse {
    private List<Transaction> transactions;
    private BigDecimal balance;
}
