package com.finanzmanager.finanzapp.service;

import com.finanzmanager.finanzapp.models.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties = "spring.jpa.properties.javax.persistence.validation.mode=none")
@ExtendWith(OutputCaptureExtension.class)
class TransactionServiceLoggingTest {

    @Autowired
    TransactionService service;

    @Test
    void should_log_warning_for_invalid_amount(CapturedOutput output) {

        Transaction tx = Transaction.builder()
                .title("Testbuchung")
                .amount(BigDecimal.ZERO) // INVALID -> expected exception
                .date(LocalDate.now())
                .build();

        // ✔ WICHTIG: Exception MUSS erwartet werden
        assertThrows(IllegalArgumentException.class, () -> service.save(tx));

        // ✔ Testet zusätzlich das Logging
        assertThat(output.getOut())
                .contains("WARN")
                .contains("Ungültiger oder fehlender Betrag");
    }
}
