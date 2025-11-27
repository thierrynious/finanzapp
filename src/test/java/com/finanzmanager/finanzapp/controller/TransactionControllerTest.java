package com.finanzmanager.finanzapp.controller;

import com.finanzmanager.finanzapp.config.TestSecurityConfig;
import com.finanzmanager.finanzapp.models.Transaction;
import com.finanzmanager.finanzapp.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)  // Security komplett aus
@Import(TestSecurityConfig.class)          // eigenes Security-Mock-Konfig
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private Transaction buildTransaction(Long id, String title, double amount, boolean income) {
        return Transaction.builder()
                .id(id)
                .title(title)
                .amount(BigDecimal.valueOf(amount))
                .date(LocalDate.of(2023, 10, 1))
                .isIncome(income)
                .build();
    }

    @Test
    void should_create_transaction_successfully() throws Exception {
        Transaction tx = buildTransaction(1L, "Miete", 1200.0, false);
        when(transactionService.save(any())).thenReturn(tx);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Miete",
                                  "amount": 1200.0,
                                  "date": "2023-10-01",
                                  "isIncome": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Miete"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void should_return_400_when_input_invalid() throws Exception {
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "",
                                  "amount": -5,
                                  "date": "2026-12-01"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_all_transactions() throws Exception {
        List<Transaction> dummyList = List.of(buildTransaction(1L, "Miete", 1200.0, false));
        when(transactionService.getAll()).thenReturn(dummyList);

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Miete"));
    }

    @Test
    void should_return_transaction_by_id() throws Exception {
        Transaction tx = buildTransaction(1L, "Essen", 20.0, false);
        when(transactionService.getById(1L)).thenReturn(Optional.of(tx));

        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Essen"));
    }

    @Test
    void should_return_404_if_transaction_not_found() throws Exception {
        when(transactionService.getById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/transactions/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_filtered_transactions_by_title() throws Exception {
        List<Transaction> results = List.of(buildTransaction(2L, "Miete Januar", 1000.0, false));
        when(transactionService.searchByTitle("Miete")).thenReturn(results);

        mockMvc.perform(get("/api/transactions/search?title=Miete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Miete Januar"));
    }

    @Test
    void should_return_income_transactions() throws Exception {
        Transaction income = buildTransaction(3L, "Gehalt", 2500.0, true);
        when(transactionService.getIncomes()).thenReturn(List.of(income));

        mockMvc.perform(get("/api/transactions/incomes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Gehalt"));
    }

    @Test
    void should_return_expense_transactions() throws Exception {
        Transaction expense = buildTransaction(4L, "Strom", 100.0, false);
        when(transactionService.getExpenses()).thenReturn(List.of(expense));

        mockMvc.perform(get("/api/transactions/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Strom"));
    }

    @Test
    void should_return_transactions_between_dates() throws Exception {
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);
        Transaction tx = buildTransaction(5L, "Auto", 800.0, false);

        when(transactionService.filterByDate(from, to)).thenReturn(List.of(tx));

        mockMvc.perform(get("/api/transactions/between?from=2023-01-01&to=2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Auto"));
    }
}
