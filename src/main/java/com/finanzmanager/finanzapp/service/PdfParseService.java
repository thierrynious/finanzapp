package com.finanzmanager.finanzapp.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@Slf4j
public class PdfParseService {

    public String extractText(InputStream inputStream) {
        try (PDDocument doc = PDDocument.load(inputStream)) {

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);

            String text = stripper.getText(doc);

            return text.replace("\r", "");
        }
        catch (Exception e) {
            log.error("❌ Fehler beim PDF-Parsen: {}", e.getMessage());
            return "";
        }
    }

}
