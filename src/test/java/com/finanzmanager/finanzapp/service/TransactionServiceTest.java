package com.finanzmanager.finanzapp.service;

import com.finanzmanager.finanzapp.models.Transaction;
import com.finanzmanager.finanzapp.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void should_return_all_transactions() {
        // GIVEN
        List<Transaction> dummy = List.of(
                Transaction.builder().title("Miete").amount(new BigDecimal("1200.00")).date(LocalDate.now()).build(),
                Transaction.builder().title("Gehalt").amount(new BigDecimal("2500.00")).date(LocalDate.now()).build()
        );
        when(transactionRepository.findAll()).thenReturn(dummy);

        // WHEN
        List<Transaction> result = transactionService.getAll();

        // THEN
        assertThat(result).hasSize(2).contains(dummy.get(0));
        verify(transactionRepository).findAll();
    }

    @Test
    void should_save_transaction() {
        // GIVEN
        Transaction tx = Transaction.builder()
                .title("Test")
                .amount(new BigDecimal("99.99"))
                .date(LocalDate.now())
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(tx);

        // WHEN
        Transaction saved = transactionService.save(tx);

        // THEN
        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction captured = captor.getValue();
        assertThat(captured.getTitle()).isEqualTo("Test");
        assertThat(saved).isNotNull();
    }

    @Test
    void should_return_transaction_by_id() {
        // GIVEN
        Transaction tx = Transaction.builder()
                .id(1L)
                .title("Essen")
                .amount(new BigDecimal("20.00"))
                .date(LocalDate.now())
                .build();

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(tx));

        // WHEN
        Optional<Transaction> result = transactionService.getById(1L);

        // THEN
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Essen");
    }

    @Test
    void should_delete_transaction_by_id() {
        // WHEN
        transactionService.delete(5L);

        // THEN
        verify(transactionRepository, times(1)).deleteById(5L);
    }

    @Test
    void should_search_transactions_by_title() {
        // GIVEN
        Transaction tx = Transaction.builder()
                .title("Miete August")
                .amount(new BigDecimal("800.00"))
                .date(LocalDate.now())
                .build();

        when(transactionRepository.findByTitleContainingIgnoreCase("miete"))
                .thenReturn(List.of(tx));

        // WHEN
        List<Transaction> result = transactionService.searchByTitle("miete");

        // THEN
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains("Miete");
    }

    @Test
    void should_filter_transactions_by_date_range() {
        // GIVEN
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);
        Transaction tx = Transaction.builder()
                .title("Auto")
                .amount(new BigDecimal("300.00"))
                .date(LocalDate.of(2023, 5, 5))
                .build();

        when(transactionRepository.findByDateBetween(from, to)).thenReturn(List.of(tx));

        // WHEN
        List<Transaction> result = transactionService.filterByDate(from, to);

        // THEN
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Auto");
    }

    @Test
    void should_get_incomes_and_expenses() {
        // GIVEN
        Transaction income = Transaction.builder()
                .title("Gehalt")
                .amount(new BigDecimal("3000.00"))
                .date(LocalDate.now())
                .isIncome(true)
                .build();

        Transaction expense = Transaction.builder()
                .title("Miete")
                .amount(new BigDecimal("1000.00"))
                .date(LocalDate.now())
                .isIncome(false)
                .build();

        when(transactionRepository.findByIsIncome(true)).thenReturn(List.of(income));
        when(transactionRepository.findByIsIncome(false)).thenReturn(List.of(expense));

        // WHEN
        List<Transaction> incomes = transactionService.getIncomes();
        List<Transaction> expenses = transactionService.getExpenses();

        // THEN
        assertThat(incomes).hasSize(1);
        assertThat(expenses).hasSize(1);
        assertThat(incomes.get(0).getTitle()).isEqualTo("Gehalt");
        assertThat(expenses.get(0).getTitle()).isEqualTo("Miete");
    }
}
