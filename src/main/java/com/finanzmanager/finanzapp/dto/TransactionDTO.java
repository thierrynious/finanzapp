package com.finanzmanager.finanzapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {

    private Long id;
    private String title;
    private BigDecimal amount;
    private LocalDate date;

    // ✅ Fix: JSON heißt sicher "isIncome" (Frontend nutzt tx.isIncome)
    @JsonProperty("isIncome")
    private boolean income;

    private String note;

    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
