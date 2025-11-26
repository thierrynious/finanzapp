package com.finanzmanager.finanzapp.controller;

import com.finanzmanager.finanzapp.dto.TransactionDTO;
import com.finanzmanager.finanzapp.models.Transaction;
import com.finanzmanager.finanzapp.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;          // 🟢 importiert
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;
    private final ModelMapper modelMapper;

    @Autowired
    public TransactionController(TransactionService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    // 🟢 Swagger-Doku für Endpunkt "getPaged"
    @Operation(summary = "Liefert paginierte Transaktionen",
            description = "Zeigt Transaktionen mit Paging und Sortierung an.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Erfolgreich geladen")
    })
    @GetMapping("/paged")
    public ResponseEntity<Page<Transaction>> getPaged(Pageable pageable) {
        return ResponseEntity.ok(service.getPaged(pageable));
    }

    // 🟢 Swagger-Doku für "create"
    @Operation(summary = "Erstellt eine neue Transaktion",
    description = "Fügt eine neue Transaktion in die FinanzApp-Datenbank ein.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaktion erfolgreich erstellt"),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TransactionDTO dto) {
        Transaction entity = modelMapper.map(dto, Transaction.class);
        Transaction saved = service.save(entity);
        TransactionDTO result = modelMapper.map(saved, TransactionDTO.class);
        return ResponseEntity.ok(result);
    }

    // 🟢 Swagger-Doku für "getAll"
    @Operation(summary = "Liefert alle Transaktionen", description = "Gibt eine Liste aller gespeicherten Transaktionen zurück.")
    @ApiResponse(responseCode = "200", description = "Liste erfolgreich geladen")
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAll() {
        List<TransactionDTO> dtos = service.getAll().stream()
                .map(tx -> modelMapper.map(tx, TransactionDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Findet eine Transaktion nach ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaktion gefunden"),
            @ApiResponse(responseCode = "404", description = "Nicht gefunden")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Aktualisiert eine Transaktion")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaktion aktualisiert"),
            @ApiResponse(responseCode = "404", description = "Transaktion nicht gefunden")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody TransactionDTO dto) {
        return service.getById(id)
                .map(existing -> {
                    Transaction entity = modelMapper.map(dto, Transaction.class);
                    entity.setId(id);
                    Transaction updated = service.save(entity);
                    TransactionDTO result = modelMapper.map(updated, TransactionDTO.class);
                    return ResponseEntity.ok(result);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Löscht eine Transaktion")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Erfolgreich gelöscht"),
            @ApiResponse(responseCode = "404", description = "Nicht gefunden")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (service.getById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Sucht Transaktionen nach Titel")
    @ApiResponse(responseCode = "200", description = "Suchergebnisse gefunden")
    @GetMapping("/search")
    public ResponseEntity<List<Transaction>> search(@RequestParam String title) {
        return ResponseEntity.ok(service.searchByTitle(title));
    }

    @GetMapping("/incomes")
    public ResponseEntity<List<TransactionDTO>> incomes() {
        List<TransactionDTO> dtos = service.getIncomes().stream()
                .map(tx -> modelMapper.map(tx, TransactionDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }


    @Operation(summary = "Gibt alle Ausgaben zurück")
    @GetMapping("/expenses")
    public ResponseEntity<List<Transaction>> expenses() {
        return ResponseEntity.ok(service.getExpenses());
    }

    @Operation(summary = "Filtert Transaktionen nach Zeitraum")
    @ApiResponse(responseCode = "200", description = "Gefilterte Liste erfolgreich geladen")
    @GetMapping("/between")
    public ResponseEntity<List<Transaction>> filterByDate(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {
        return ResponseEntity.ok(service.filterByDate(from, to));
    }
}
