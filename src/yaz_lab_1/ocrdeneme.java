/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yaz_lab_1;

/**
 *
 * @author mert2
 */

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.Loader;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ocrdeneme {
    
     public static void main(String[] args) {
        try {
            // PDFBox ile PDF dosyasını açın
            PDDocument document = Loader.loadPDF(new File("C:\\users\\mert2\\Desktop\\transkript.pdf"));

            // PDFBox ile PDF dosyasının içeriğini okuyun
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String content = pdfStripper.getText(document);

            // Ekrana PDF içeriğini yazdırın
            System.out.println("PDF İçeriği:");
            System.out.println(content);

            // Tesseract ile PDF dosyasının içeriğini okuyun
            Tesseract tesseract = new Tesseract();
            String tesseractContent = tesseract.doOCR(new File("C:\\users\\mert2\\Desktop\\transkript.pdf"));

            // Ekrana Tesseract ile okunan içeriği yazdırın
            System.out.println("\nTesseract ile Okunan Metin:");
            System.out.println(tesseractContent);

            // PDFBox ile okunan içerikten çıkan metinleri kullanarak ders kodu, ders adı ve harf notunu alın
            String textToProcess = content; // PDFBox ile okunan içerik olarak varsayalım

            // Ders kodunu ve adını ayıklamak için düzenli ifade
            String pattern = "(\\S+)\\s([^\\(]+)\\s\\([^\\)]+\\)";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(textToProcess);

            if (m.find()) {
                String dersKodu = m.group(1);
                String dersAdi = m.group(2);

                System.out.printf("Ders Kodu: %s%n", dersKodu);
                System.out.printf("Ders Adı: %s%n", dersAdi);
            }

            // Ders harf notunu ayıklamak için düzenli ifade
            pattern = "([A-Z]{2})\\s";
            p = Pattern.compile(pattern);
            m = p.matcher(textToProcess);

            if (m.find()) {
                String dersHarfNotu = m.group(1);

                System.out.printf("Ders Harf Notu: %s%n", dersHarfNotu);
            }

            // PDFBox ile açılan dosyayı kapatın
            document.close();
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
        }
    }
    
}
