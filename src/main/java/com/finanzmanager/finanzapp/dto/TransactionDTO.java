package com.finanzmanager.finanzapp.dto;

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
    private boolean isIncome;
    private String note;

    // 🔹 Audit-Felder für Swagger sichtbar
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
