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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresqlConnectionTest {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/Yaz_lab_1"; // Veritabanı URL'si
        String kullaniciAdi = "postgres";
        String sifre = "mert2001";

        try {
            // PostgreSQL JDBC sürücüsünü yükles
            Class.forName("org.postgresql.Driver");

            // Veritabanına bağlan
            Connection connection = DriverManager.getConnection(jdbcUrl, kullaniciAdi, sifre);
            System.out.println("Bağlantı başarılı.");

            // Bağlantıyı kapat
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Bağlanamadı: " + e.getMessage());
        }
    }
}
