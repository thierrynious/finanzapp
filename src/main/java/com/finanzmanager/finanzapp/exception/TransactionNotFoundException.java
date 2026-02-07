package com.finanzmanager.finanzapp.exception;

public class TransactionNotFoundException extends RuntimeException{

    public TransactionNotFoundException(Long id) {
        super("Transaction not found: " + id);
    }
}
