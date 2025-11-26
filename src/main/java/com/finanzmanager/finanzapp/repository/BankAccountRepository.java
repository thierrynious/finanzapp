package com.finanzmanager.finanzapp.repository;

import com.finanzmanager.finanzapp.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
