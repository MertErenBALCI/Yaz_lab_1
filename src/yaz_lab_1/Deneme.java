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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Deneme {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Okul Yönetim Uygulaması");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Yönetici Paneli (Önceki kod burada)
        
        

        // Öğrenci Paneli
        JPanel studentPanel = new JPanel();
        JLabel labelNo = new JLabel("Öğrenci No:");
        JTextField txtNo = new JTextField(10);
        JLabel labelPassword = new JLabel("Şifre:");
        JPasswordField txtPassword = new JPasswordField(10);
        JButton btnLogin = new JButton("Giriş Yap");

        studentPanel.setLayout(new FlowLayout());
        studentPanel.add(labelNo);
        studentPanel.add(txtNo);
        studentPanel.add(labelPassword);
        studentPanel.add(txtPassword);
        studentPanel.add(btnLogin);

        tabbedPane.addTab("Öğrenci", studentPanel);

        frame.add(tabbedPane);
        frame.setVisible(true);

        // Öğrenci Paneli için olay dinleyici ekleyin
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentNo = txtNo.getText();
                String password = new String(txtPassword.getPassword());

                // PostgreSQL veritabanına bağlanın ve sorguyu çalıştırın
                try {
                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                    String query = "SELECT ogrenci_no, ogrenci_sifre, ogrenci_ad_soyad FROM ogrenci";
                    PreparedStatement statement = conn.prepareStatement(query);
                 //   statement.setString(1, studentNo);
                  //  statement.setString(2, password);
                    ResultSet result = statement.executeQuery();
                    result.next();
                    String ogrenciNo = result.getString("ogrenci_no");
                    String ogrencisifre = result.getString("ogrenci_sifre");
                    String ogrenciadsoyad = result.getString("ogrenci_ad_soyad");
                   

                    System.out.print("\n"+ ogrenciNo+ "\t" + ogrencisifre + "\t"+ ogrenciadsoyad);


                    if (result.next()) {
                        JOptionPane.showMessageDialog(frame, "Giriş Başarılı!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Giriş Başarısız. Geçersiz Öğrenci No veya Şifre.");
                    }

                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}

