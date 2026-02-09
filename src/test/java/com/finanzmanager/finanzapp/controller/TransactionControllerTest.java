package com.finanzmanager.finanzapp.controller;

import com.finanzmanager.finanzapp.model.Transaction;
import com.finanzmanager.finanzapp.service.TransactionService;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void should_create_transaction_successfully() throws Exception {
        Transaction tx = new Transaction(1L, "Miete", 1200.0, LocalDate.of(2023,10,1));
        when(transactionService.save(any())).thenReturn(tx);

        mockMvc.perform(post("/api/transactions")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content("""
                        {"title":"Miete",
                        "amount":1200.0,
                        "date":"2023-10-01"
                        }
                        """)
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Miete"))
                .andExpect(jsonPath("$.id").value(1));

    }

}