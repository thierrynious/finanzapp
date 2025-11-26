package com.finanzmanager.finanzapp.dto;

import com.finanzmanager.finanzapp.dto.TransactionDTO;
import com.finanzmanager.finanzapp.models.Transaction;

public class TransactionMapper {
    public TransactionDTO convert(Transaction t) {
        return TransactionDTO.builder()
                .id(t.getId())
                .title(t.getTitle())
                .amount(t.getAmount())
                .date(t.getDate())
                .isIncome(t.isIncome())
                .build();
    }
}
