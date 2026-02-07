package com.finanzmanager.finanzapp.exception;

public class BankAccountNotFoundException extends RuntimeException {

    public BankAccountNotFoundException(Long id) {
        super("Bank account with id " + id + " not found");
    }
}
