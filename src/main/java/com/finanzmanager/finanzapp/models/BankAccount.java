package com.finanzmanager.finanzapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class BankAccount {
    @Id
    private Long id;
    private String accountName;
    private String accountNumber;
    private double balance;

    public BankAccount(String name, String number) {
        this.accountName = name;
        this.accountNumber = number;
        this.balance = 0.0;
    }

    public BankAccount() {}

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
        }
    }
}
