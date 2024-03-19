package yaz_lab_1;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class dersekleme {

     public static void main(String[] args) {
         
        String text = "AIT109 Atatürk İlkeleri ve İnkilap Tarihi I (UE)\n";

        // Ders kodunu ve adını ayıklamak için düzenli ifade
        String pattern = "(\\S+)\\s([^\\(]+)\\s\\([^\\)]+\\)\\n";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);

        if (m.find()) {
            String dersKodu = m.group(1);
            String dersAdi = m.group(2);

            System.out.printf("Ders Kodu: %s%n", dersKodu);
            System.out.printf("Ders Adı: %s%n", dersAdi);
        }

        // Ders harf notunu ayıklamak için düzenli ifade
        pattern = "([A-Z]{2})\\s";
        p = Pattern.compile(pattern);
        m = p.matcher(text);

        if (m.find()) {
            String dersHarfNotu = m.group(1);

            System.out.printf("Ders Harf Notu: %s%n", dersHarfNotu);
        }
    }
}
