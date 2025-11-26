package com.finanzmanager.finanzapp.repository;

import com.finanzmanager.finanzapp.models.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY) // nutzt automatisch In-Memory-H2
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Speichern und Laden einer Transaktion")
    void should_save_and_load_transaktion() {
        Transaction tx = Transaction.builder()
                .title("Miete")
                .amount(new BigDecimal("1000.00"))
                .date(LocalDate.of(2025, 10, 1))
                .isIncome(false)
                .build();

        repository.save(tx);
        List<Transaction> result = repository.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Miete");
    }
}
