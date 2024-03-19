package yaz_lab_1;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.Loader;

public class deneme1 {

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
         
                          
            // PDFBox ile okunan içerikten çıkan metinleri ekrana yazdırın
            System.out.println("\nPDFBox ile Okunan Metin:");
            System.out.println(content);
            
            // PDFBox ile açılan dosyayı kapatın
            document.close();
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
        }
    }
}
