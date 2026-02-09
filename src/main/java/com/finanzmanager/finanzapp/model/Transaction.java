package com.finanzmanager.finanzapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Transaction {

    public Transaction() {
    }

    public Transaction(String title, double amount, LocalDate date) {
        this.title = title;
        this.amount = amount;
        this.date = date;
    }

    public Transaction(Long id, String title, double amount, LocalDate date) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Titel darf nicht leer sein")
    private String title;

    @Positive(message = "Betrag muss positiv sein")
    private double amount;

    @NotNull(message = "Datum darf nicht null sein")
    @PastOrPresent(message = "Datum darf nicht in der Zukunft liegen")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

}
