package com.finanzmanager.finanzapp.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class DashboardDTO {
    private BigDecimal balance;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private List<TransactionDTO> latestTransactions;
}

