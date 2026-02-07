package com.finanzmanager.finanzapp.exception;

import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.finanzmanager.finanzapp.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler({BankAccountNotFoundException.class, CategoryNotFoundException.class, TransactionNotFoundException.class})
    public ProblemDetail handleNotFound(RuntimeException ex){
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Resssource nicht gefunden");
        problem.setDetail(ex.getMessage());

        return problem;
    }

}
