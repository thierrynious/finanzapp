package com.finanzmanager.finanzapp.controller;

import com.finanzmanager.finanzapp.models.Transaction;
import com.finanzmanager.finanzapp.service.BankStatementAnalysisService;
import com.finanzmanager.finanzapp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upload")
public class BankStatementUploadController {

    private final BankStatementAnalysisService analysisService;
    private final TransactionService transactionService;

    @PostMapping("/bankstatement")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Keine Datei erhalten");
        }
        try {
            List<Transaction> parsed =
                    analysisService.parseCsv(file.getInputStream());

            List<Transaction> saved =
                    transactionService.saveAllUnique(parsed);

            Map<String, Object> response = new HashMap<>();
            response.put("totalParsed", parsed.size());
            response.put("imported", saved.size());
            response.put("duplicates", parsed.size() - saved.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Fehler beim CSV Upload: " + e.getMessage());
        }
    }
}
