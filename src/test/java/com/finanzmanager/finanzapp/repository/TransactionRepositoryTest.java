package com.finanzmanager.finanzapp.repository;

import com.finanzmanager.finanzapp.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @Test
    @DisplayName("Speichern und Laden einer Transaktion")
    void should_save_and_load_transaktion() {

        //given
        Transaction tx = new Transaction(
                "Miete",
                1000.0,
                LocalDate.of(2025,1,1)
        );

        //when
        repository.save(tx);
        List<Transaction> result = repository.findAll();

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Miete");
        assertThat(result.get(0).getAmount()).isEqualTo(1000.0);
    }

    @Test
    @DisplayName("Suche nach Titel (Ignore Case)")
    void should_find_by_title_ignorecase() {

        //given
        repository.save(new Transaction(
                "Gehalt Januar",
                3000.0,
                LocalDate.now()
        ));

        //when
        List<Transaction> result = repository.findByTitleContainingIgnoreCase("Gehalt");

        //then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).contains("Gehalt");
    }

}