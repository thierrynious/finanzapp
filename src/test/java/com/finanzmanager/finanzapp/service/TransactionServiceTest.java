package com.finanzmanager.finanzapp.service;

import com.finanzmanager.finanzapp.model.Transaction;
import com.finanzmanager.finanzapp.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void should_return_all_transactions() {

        //Given
        List<Transaction> dummy = List.of(
          new Transaction("Miete", 1200.0, LocalDate.now()),
          new Transaction("Gehalt", 2500.0, LocalDate.now())
        );
        when(transactionRepository.findAll()).thenReturn(dummy);

        //When
        List<Transaction> result = transactionService.getAll();

        //Then
        assertThat(result).hasSize(2);
        verify(transactionRepository).findAll();
    }

    @Test
    void should_save_transaction_with_timestamp() {

        //given
        Transaction tx = new Transaction("Test", 99.99, LocalDate.now());

        // when
        transactionService.save(tx);

        //then
        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();

        assertThat(saved.getTitle()).isEqualTo("Test");
        assertThat(saved.getAmount()).isEqualTo(99.99);
        assertThat(saved.getDate()).isNotNull();
    }
}