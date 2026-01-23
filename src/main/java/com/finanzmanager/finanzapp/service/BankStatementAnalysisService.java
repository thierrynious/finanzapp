package com.finanzmanager.finanzapp.service;

import com.finanzmanager.finanzapp.models.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BankStatementAnalysisService {

    private static final int MAX_TITLE_LENGTH = 255;
    private static final DateTimeFormatter[] DATE_FORMATS = {
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            DateTimeFormatter.ofPattern("dd.MM.yy")
    };
    public List<Transaction> parseCsv(InputStream inputStream) {
        try {
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            bis.mark(4);

            byte[] bom = new byte[4];
            int read = bis.read(bom);
            bis.reset();

            Charset charset = detectCharset(bom, read);
            log.info("📄 CSV Charset erkannt: {}", charset);

            return parseWithCharset(bis, charset);

        } catch (Exception e) {
            log.error("❌ CSV Parsing fehlgeschlagen", e);
            throw new RuntimeException(e);
        }
    }

    // =========================
    // CHARSET DETECTION ✅
    // =========================

    private Charset detectCharset(byte[] bom, int read) {
        if (read >= 2) {
            // UTF-16 LE
            if ((bom[0] & 0xFF) == 0xFF && (bom[1] & 0xFF) == 0xFE) {
                return Charset.forName("UTF-16LE");
            }
            // UTF-16 BE
            if ((bom[0] & 0xFF) == 0xFE && (bom[1] & 0xFF) == 0xFF) {
                return Charset.forName("UTF-16BE");
            }
        }
        if (read >= 3) {
            // UTF-8 BOM
            if ((bom[0] & 0xFF) == 0xEF
                    && (bom[1] & 0xFF) == 0xBB
                    && (bom[2] & 0xFF) == 0xBF) {
                return StandardCharsets.UTF_8;
            }
        }
        // Fallback (typisch für Banken)
        return Charset.forName("windows-1252");
    }

    // =========================
    // CSV PARSING
    // =========================
    private List<Transaction> parseWithCharset(InputStream inputStream, Charset charset) throws Exception {

        List<Transaction> result = new ArrayList<>();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, charset)
        );

        String headerLine = reader.readLine();
        if (headerLine == null) return result;

        headerLine = headerLine.replace("\uFEFF", "");
        String delimiter = headerLine.contains(";") ? ";" : ",";

        String[] headers = headerLine.split(delimiter);
        Map<String, Integer> index = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            index.put(headers[i].replace("\"", "").trim(), i);
        }

        Integer dateIdx = index.get("Buchungstag");
        Integer titleIdx = index.get("Verwendungszweck");
        Integer amountIdx = index.get("Betrag");

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isBlank()) continue;

            String[] cols = line.split(delimiter);

            LocalDate date = parseDate(cols[dateIdx].replace("\"", "").trim());

            String rawAmount = cols[amountIdx]
                    .replace("\"", "")
                    .replace(".", "")
                    .replace(",", ".");

            BigDecimal amount = new BigDecimal(rawAmount);

            boolean income = amount.compareTo(BigDecimal.ZERO) > 0;

            String rawTitle = cols[titleIdx].replace("\"", "").trim();
            if (rawTitle.isEmpty()) rawTitle = "Unbekannt";

            String title = rawTitle.length() > MAX_TITLE_LENGTH
                    ? rawTitle.substring(0, MAX_TITLE_LENGTH)
                    : rawTitle;

            result.add(Transaction.builder()
                    .date(date)
                    .title(title)
                    .amount(amount)     // ➕ / ➖ bleibt erhalten
                    .income(income)
                    .build());
        }

        log.info("✅ {} Transaktionen importiert", result.size());
        return result;
    }

    private LocalDate parseDate(String raw) {
        for (DateTimeFormatter fmt : DATE_FORMATS) {
            try {
                return LocalDate.parse(raw, fmt);
            } catch (Exception ignored) {}
        }
        throw new IllegalArgumentException("Unbekanntes Datumsformat: " + raw);
    }
}

