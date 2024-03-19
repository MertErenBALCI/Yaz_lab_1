/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yaz_lab_1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.swing.JButton;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class MasaustuUygulamasi {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Masaüstü Uygulaması");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        // YÖNETİCİ İŞLEMLERİ
        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new GridLayout(3, 2));

        JLabel noLabelAdmin = new JLabel("Yönetici No: ");
        JTextField noFieldAdmin = new JTextField(20);

        JLabel passwordLabelAdmin = new JLabel("Yönetici Şifre: ");
        JPasswordField passwordFieldAdmin = new JPasswordField(20);

        JButton loginButtonAdmin = new JButton("Giriş Yap");
        loginButtonAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = noFieldAdmin.getText();
                String password = new String(passwordFieldAdmin.getPassword());

               
                try {
                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                    String query1 = "SELECT admin_no, admin_sifre, admin_ad_soyad FROM admin WHERE admin_no = ? AND admin_sifre = ? ";
                    PreparedStatement statement = conn.prepareStatement(query1);

                    statement.setString(1, name);
                    statement.setString(2, password);

                    ResultSet result3 = statement.executeQuery();

                    if (result3.next()) {

                        String adminNo = result3.getString("admin_no");
                        String adminadsoyad = result3.getString("admin_ad_soyad");

                        JOptionPane.showMessageDialog(frame, "Giriş Başarılı! Hoş geldiniz, " + adminadsoyad);

                        JFrame adminActionsFrame = new JFrame("Yönetici İşlemleri");
                        adminActionsFrame.setSize(600, 300);
                        adminActionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        JButton dersSecimButton = new JButton("Ders Seçim 2. Aşama");

                        dersSecimButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JFrame ogretmenFrame = new JFrame("Öğretmenler");
                                ogretmenFrame.setSize(400, 300);
                                ogretmenFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                DefaultListModel<String> ogretmen_listesi_modeli = new DefaultListModel<>();
                                JList<String> ogretmen_listesi = new JList<>(ogretmen_listesi_modeli);
                                JScrollPane teacherScrollPane = new JScrollPane(ogretmen_listesi);

                                try {
                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                    String ogretmen_Query = "SELECT hoca_sicil_no, hoca_ad_soyad FROM hoca";
                                    PreparedStatement ogretmen_Statement = conn.prepareStatement(ogretmen_Query);
                                    ResultSet ogretmen_sonuc = ogretmen_Statement.executeQuery();

                                    while (ogretmen_sonuc.next()) {
                                        String sicilNo = ogretmen_sonuc.getString("hoca_sicil_no");
                                        String adSoyad = ogretmen_sonuc.getString("hoca_ad_soyad");
                                        ogretmen_listesi_modeli.addElement(sicilNo + " - " + adSoyad);
                                    }
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }

                                ogretmen_listesi.addListSelectionListener(new ListSelectionListener() {
                                    @Override
                                    public void valueChanged(ListSelectionEvent e) {
                                        if (!e.getValueIsAdjusting()) {
                                            String secilen_ogretmen = ogretmen_listesi.getSelectedValue();
                                            String[] parca = secilen_ogretmen.split(" - ");
                                            String selectedSicilNo = parca[0];

                                            JFrame ogretmen_verdigi_derslerFrame = new JFrame("Öğretmenin Verdiği Dersler");
                                            ogretmen_verdigi_derslerFrame.setSize(400, 300);
                                            ogretmen_verdigi_derslerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                            DefaultListModel<String> ders_listesi_modeli = new DefaultListModel<>();
                                            JList<String> ders_listesi = new JList<>(ders_listesi_modeli);
                                            JScrollPane coursesScrollPane = new JScrollPane(ders_listesi);

                                            try {
                                                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                                String coursesQuery = "SELECT ders_kodu, ders_adi FROM ders WHERE hoca_sicil_no = ?";
                                                PreparedStatement coursesStatement = conn.prepareStatement(coursesQuery);
                                                coursesStatement.setString(1, selectedSicilNo);
                                                ResultSet ders_sonuc = coursesStatement.executeQuery();

                                                while (ders_sonuc.next()) {
                                                    String dersKodu = ders_sonuc.getString("ders_kodu");
                                                    String dersAdi = ders_sonuc.getString("ders_adi");
                                                    ders_listesi_modeli.addElement(dersKodu + " - " + dersAdi);
                                                }
                                            } catch (SQLException ex) {
                                                ex.printStackTrace();
                                            }

                                            ders_listesi.addListSelectionListener(new ListSelectionListener() {
                                                @Override
                                                public void valueChanged(ListSelectionEvent e) {
                                                    if (!e.getValueIsAdjusting()) {
                                                        String selectedCourse = ders_listesi.getSelectedValue();
                                                     
                                                        JButton rastgeleAtamaButton = new JButton("R_atama");
                                                        rastgeleAtamaButton.addActionListener(new ActionListener() {
                                                            @Override
                                                            public void actionPerformed(ActionEvent e) {
                                                                try {
                                                                   
                                                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");

                                                                  
                                                                    String query1 = "SELECT ogrenci_no FROM ogrenci";
                                                                    PreparedStatement statement1 = conn.prepareStatement(query1);
                                                                    ResultSet result1 = statement1.executeQuery();

                                                                   
                                                                    String insertQuery = "INSERT INTO talep (ogrenci_no, ders_kodu, onay_durumu) VALUES (?, ?, 1)";
                                                                    PreparedStatement insertStatement = conn.prepareStatement(insertQuery);

                                                                    while (result1.next()) {
                                                                        String ogrenciNo = result1.getString("ogrenci_no");

                                                                        
                                                                        String checkQuery = "SELECT * FROM talep WHERE ogrenci_no = ?";

                                                                        PreparedStatement checkStatement = conn.prepareStatement(checkQuery);
                                                                        checkStatement.setString(1, ogrenciNo);

                                                                        ResultSet checkResult = checkStatement.executeQuery();

                                                                        if (!checkResult.next()) {
                                                                           
                                                                            String randomCourseQuery = "SELECT ders_kodu FROM ders WHERE ders_kontenjan > 0 ORDER BY RANDOM() LIMIT 1";
                                                                            PreparedStatement randomCourseStatement = conn.prepareStatement(randomCourseQuery);
                                                                            ResultSet randomCourseResult = randomCourseStatement.executeQuery();

                                                                            if (randomCourseResult.next()) {
                                                                                String dersKodu = randomCourseResult.getString("ders_kodu");

                                                                               
                                                                                insertStatement.setString(1, ogrenciNo);
                                                                                insertStatement.setString(2, dersKodu);
                                                                                insertStatement.executeUpdate();

                                                                              
                                                                                String decreaseKontenjanQuery = "UPDATE ders SET ders_kontenjan = ders_kontenjan - 1 WHERE ders_kodu = ?";
                                                                                PreparedStatement decreaseKontenjanStatement = conn.prepareStatement(decreaseKontenjanQuery);
                                                                                decreaseKontenjanStatement.setString(1, dersKodu);
                                                                                decreaseKontenjanStatement.executeUpdate();
                                                                            }
                                                                        }
                                                                    }

                                                                  
                                                                    conn.close();
                                                                } catch (SQLException ex) {
                                                                    ex.printStackTrace();
                                                                }
                                                            }
                                                        });

                                                        JFrame courseDetailsFrame = new JFrame("Ders Detayları");
                                                        courseDetailsFrame.setSize(400, 300);
                                                        courseDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                                        courseDetailsFrame.add(rastgeleAtamaButton);
                                                        courseDetailsFrame.setVisible(true);
                                                    }
                                                }
                                            });

                                            ogretmen_verdigi_derslerFrame.add(coursesScrollPane);
                                            ogretmen_verdigi_derslerFrame.setVisible(true);
                                        }
                                    }
                                });

                                ogretmenFrame.add(teacherScrollPane);
                                ogretmenFrame.setVisible(true);
                            }
                        });

                        JButton dersKontenjanButton = new JButton("Ders Kontenjan Belirleme");
                        dersKontenjanButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                
                                JFrame dersSecimFrame = new JFrame("Ders Seçimi");
                                dersSecimFrame.setSize(400, 300);

                               
                                DefaultListModel<String> dersModel = new DefaultListModel<>();
                                try {
                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                    String query = "SELECT ders_kodu, ders_adi FROM ders";
                                    PreparedStatement statement = conn.prepareStatement(query);
                                    ResultSet result = statement.executeQuery();

                                    
                                    while (result.next()) {
                                        String dersKodu = result.getString("ders_kodu");
                                        String dersAdi = result.getString("ders_adi");
                                        dersModel.addElement(dersKodu + " - " + dersAdi);
                                    }
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }

                                JList<String> ders_listesi = new JList<>(dersModel);
                                ders_listesi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                               
                                JButton secButton = new JButton("Ders Seç");
                                secButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String secilen_ders = ders_listesi.getSelectedValue();
                                        if (secilen_ders != null) {
                                           
                                            JFrame kontenjanGuncellemeFrame = new JFrame("Kontenjan Güncelleme");
                                            kontenjanGuncellemeFrame.setSize(400, 200);

                                           
                                            JTextField dersBilgisi = new JTextField(secilen_ders);
                                            dersBilgisi.setEditable(false);

                                          
                                            JTextField yeni_kontenjanField = new JTextField(10);

                                           
                                            JButton guncelleButton = new JButton("Kontenjanı Güncelle");
                                            guncelleButton.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    String[] dersBilgisiParts = secilen_ders.split(" - ");
                                                    String secilen_ders_kodu = dersBilgisiParts[0];
                                                    int yeniKontenjan = Integer.parseInt(yeni_kontenjanField.getText());

                                                   
                                                    try {
                                                        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                                        String updateQuery = "UPDATE ders SET ders_kontenjan = ? WHERE ders_kodu = ?";
                                                        PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                                                        updateStatement.setInt(1, yeniKontenjan);
                                                        updateStatement.setString(2, secilen_ders_kodu );
                                                        updateStatement.executeUpdate();
                                                        JOptionPane.showMessageDialog(frame, "Kontenjan güncellendi!");
                                                        
                                                        kontenjanGuncellemeFrame.dispose();
                                                    } catch (SQLException ex) {
                                                        ex.printStackTrace();
                                                    }
                                                }
                                            });

                                           
                                            JPanel kontenjanPanel = new JPanel(new FlowLayout());
                                            kontenjanPanel.add(dersBilgisi);
                                            kontenjanPanel.add(new JLabel("Yeni Kontenjan:"));
                                            kontenjanPanel.add(yeni_kontenjanField);
                                            kontenjanPanel.add(guncelleButton);

                                            kontenjanGuncellemeFrame.add(kontenjanPanel);
                                            kontenjanGuncellemeFrame.setVisible(true);
                                        }
                                    }
                                });

                           
                                JPanel dersSecimPanel = new JPanel(new BorderLayout());
                                dersSecimPanel.add(new JScrollPane(ders_listesi), BorderLayout.CENTER);
                                dersSecimPanel.add(secButton, BorderLayout.SOUTH);

                                dersSecimFrame.add(dersSecimPanel);
                                dersSecimFrame.setVisible(true);
                            }
                        });

                        JButton ogretmen_islemleriButton = new JButton("Öğretmen İşlemleri");
                        ogretmen_islemleriButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                                JFrame ogretmen_islemleriFrame = new JFrame("Öğretmen İşlemleri");
                                ogretmen_islemleriFrame.setSize(600, 300);
                                ogretmen_islemleriFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                JButton addOgretmenButton = new JButton("ÖğretmenEkle");
                                JButton updateOgretmenButton = new JButton("Öğretmen Güncelle");
                                JButton deleteOgretmenButton = new JButton("Öğretmen Sil");

                                addOgretmenButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        
                                        JFrame addOgretmenFrame = new JFrame("Öğretmen Ekle");
                                        addOgretmenFrame.setSize(400, 200);

                                       
                                        JPanel addOgretmenPanel = new JPanel();
                                        addOgretmenPanel.setLayout(new GridLayout(10, 5));

                                        JLabel hocaNameLabel = new JLabel("Öğretmen Ad-Soyad: ");
                                        JTextField hoca_ad_soyadField = new JTextField(20);

                                        JLabel hoca_noLabel = new JLabel("Öğretmen No: ");
                                        JTextField hoca_noField = new JTextField(20);

                                        JLabel hoca_sifreLabel = new JLabel("Öğretmen Şifre: ");
                                        JPasswordField hoca_sifreField = new JPasswordField(20);

                                        JLabel hoca_ilgi_alanlariLabel = new JLabel("Öğretmen İlgi Alanları: ");
                                        JTextField hoca_ilgi_alanlariField = new JTextField(20);

                                        JButton saveButton = new JButton("Kaydet");
                                        saveButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String name = hoca_ad_soyadField.getText();
                                                String no = hoca_noField.getText();
                                                String password = new String(hoca_sifreField.getPassword());
                                                String hoca_ilgi_alanlari = hoca_ilgi_alanlariField.getText();

                                              
                                                try {
                                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                                    String query = "INSERT INTO hoca(hoca_sicil_no, hoca_ad_soyad, hoca_sifre, hoca_ilgi_alanlari) VALUES (?, ?, ?, ?)";
                                                    PreparedStatement statement = conn.prepareStatement(query);
                                                    statement.setString(2, name);
                                                    statement.setString(1, no);
                                                    statement.setString(3, password);
                                                    statement.setString(4, hoca_ilgi_alanlari);

                                                    int rowCount = statement.executeUpdate();
                                                    if (rowCount > 0) {
                                                        JOptionPane.showMessageDialog(addOgretmenFrame, "Öğretmen eklendi.");
                                                    } else {
                                                        JOptionPane.showMessageDialog(addOgretmenFrame, "Öğretmen eklenemedi.");
                                                    }

                                                    conn.close();
                                                } catch (SQLException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        });

                                        addOgretmenPanel.add(hocaNameLabel);
                                        addOgretmenPanel.add(hoca_ad_soyadField);
                                        addOgretmenPanel.add(hoca_noLabel);
                                        addOgretmenPanel.add(hoca_noField);
                                        addOgretmenPanel.add(hoca_sifreLabel);
                                        addOgretmenPanel.add(hoca_sifreField);
                                        addOgretmenPanel.add(hoca_ilgi_alanlariLabel);
                                        addOgretmenPanel.add(hoca_ilgi_alanlariField);

                                        addOgretmenPanel.add(saveButton);

                                        addOgretmenFrame.add(addOgretmenPanel);
                                        addOgretmenFrame.setVisible(true);
                                    }
                                });

                               
                                updateOgretmenButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                      
                                        JFrame updateOgretmenFrame = new JFrame("Öğretmen Güncelle");
                                        updateOgretmenFrame.setSize(400, 200);

                                       
                                        JPanel updateOgretmenPanel = new JPanel();
                                        updateOgretmenPanel.setLayout(new GridLayout(4, 2));

                                        JLabel hoca_ad_soyadLabel = new JLabel("Öğretmen Ad-Soyad: ");
                                        JTextField hoca_ad_soyadField = new JTextField(20);

                                        JLabel hoca_noLabel = new JLabel("Öğretmen No: ");
                                        JTextField hoca_sicil_noField = new JTextField(20);

                                        JLabel yeni_hoca_sifreLabel = new JLabel("Yeni Öğretmen Şifre: ");
                                        JPasswordField yeni_hoca_sifreField = new JPasswordField(20);

                                        JLabel yeni_hoca_ilgi_alanlariLabel = new JLabel("Öğretmen İlgi Alanları: ");
                                        JTextField yeni_hoca_ilgi_alanlariField = new JTextField(20);

                                        JButton updateButton = new JButton("Güncelle");
                                        updateButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String hoca_ad_soyad = hoca_ad_soyadField.getText();
                                                String hoca_sicil_no = hoca_sicil_noField.getText();
                                                String hoca_yeni_sifre = new String(yeni_hoca_sifreField.getPassword());
                                                String yeni_hoca_ilgi_alanlari = yeni_hoca_ilgi_alanlariField.getText();

                                                
                                                try {
                                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                                    String query = "UPDATE hoca SET hoca_sifre = ? WHERE hoca_ad_soyad = ? AND hoca_sicil_no = ? AND hoca_ilgi_alanlari = ?";
                                                    PreparedStatement statement = conn.prepareStatement(query);
                                                    statement.setString(1, hoca_yeni_sifre);
                                                    statement.setString(2, hoca_ad_soyad);
                                                    statement.setString(3, hoca_sicil_no);
                                                    statement.setString(4, yeni_hoca_ilgi_alanlari);

                                                    int rowCount = statement.executeUpdate();
                                                    if (rowCount > 0) {
                                                        JOptionPane.showMessageDialog(updateOgretmenFrame, "Öğretmen güncellendi.");
                                                    } else {
                                                        JOptionPane.showMessageDialog(updateOgretmenFrame, "Öğretmen güncellenemedi. Bilgileri kontrol edin.");
                                                    }

                                                    conn.close();
                                                } catch (SQLException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        });

                                        updateOgretmenPanel.add(hoca_ad_soyadLabel);
                                        updateOgretmenPanel.add(hoca_ad_soyadField);
                                        updateOgretmenPanel.add(hoca_noLabel);
                                        updateOgretmenPanel.add(hoca_sicil_noField);
                                        updateOgretmenPanel.add(yeni_hoca_sifreLabel);
                                        updateOgretmenPanel.add(yeni_hoca_sifreField);
                                        updateOgretmenPanel.add(yeni_hoca_ilgi_alanlariLabel);
                                        updateOgretmenPanel.add(yeni_hoca_ilgi_alanlariField);
                                        updateOgretmenPanel.add(updateButton);

                                        updateOgretmenFrame.add(updateOgretmenPanel);
                                        updateOgretmenFrame.setVisible(true);
                                    }
                                });

                               
                                deleteOgretmenButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        
                                        JFrame deleteOgretmenFrame = new JFrame("Öğretmen Sil");
                                        deleteOgretmenFrame.setSize(400, 200);

                                        JPanel deleteOgretmenPanel = new JPanel();
                                        deleteOgretmenPanel.setLayout(new GridLayout(10, 5));

                                        JLabel hoca_ad_soyadLabel = new JLabel("Öğretmen Ad-Soyad: ");
                                        JTextField hoca_ad_soyadField = new JTextField(20);

                                        JLabel hoca_sicil_noLabel = new JLabel("Öğretmen No: ");
                                        JTextField hoca_sicil_noField = new JTextField(20);

                                        JLabel hoca_sifreLabel = new JLabel("Öğretmen Şifre: ");
                                        JPasswordField hoca_sifreField = new JPasswordField(20);

                                        JLabel hoca_ilgi_alanlariLabel = new JLabel("Hoca İlgi Alanları: ");
                                        JTextField hoca_ilgi_alanlariField = new JTextField(20);

                                        JButton deleteButton = new JButton("Sil");
                                        deleteButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String hoca_ad_soyad = hoca_ad_soyadField.getText();
                                                String hoca_sicil_no = hoca_sicil_noField.getText();
                                                String hoca_sifre = new String(hoca_sifreField.getPassword());
                                                String hoca_ilgi_alanlari = hoca_ilgi_alanlariField.getText();

                                               
                                                try {
                                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                                    String query = "DELETE FROM hoca WHERE hoca_ad_soyad = ? AND hoca_sicil_no = ? AND hoca_sifre = ? AND hoca_ilgi_alanlari = ?";
                                                    PreparedStatement statement = conn.prepareStatement(query);
                                                    statement.setString(1, hoca_ad_soyad);
                                                    statement.setString(2, hoca_sicil_no);
                                                    statement.setString(3, hoca_sifre);
                                                    statement.setString(4, hoca_ilgi_alanlari);

                                                    int rowCount = statement.executeUpdate();
                                                    if (rowCount > 0) {
                                                        JOptionPane.showMessageDialog(deleteOgretmenFrame, "Öğretmen silindi.");
                                                    } else {
                                                        JOptionPane.showMessageDialog(deleteOgretmenFrame, "Öğretmen silinemedi. Bilgileri kontrol edin.");
                                                    }

                                                    conn.close();
                                                } catch (SQLException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        });

                                        deleteOgretmenPanel.add(hoca_ad_soyadLabel);
                                        deleteOgretmenPanel.add(hoca_ad_soyadField);
                                        deleteOgretmenPanel.add(hoca_sicil_noLabel);
                                        deleteOgretmenPanel.add(hoca_sicil_noField);
                                        deleteOgretmenPanel.add(hoca_sifreLabel);
                                        deleteOgretmenPanel.add(hoca_sifreField);
                                        deleteOgretmenPanel.add(hoca_ilgi_alanlariLabel);
                                        deleteOgretmenPanel.add(hoca_ilgi_alanlariField);
                                        deleteOgretmenPanel.add(deleteButton);

                                        deleteOgretmenFrame.add(deleteOgretmenPanel);
                                        deleteOgretmenFrame.setVisible(true);
                                    }
                                });

                                ogretmen_islemleriFrame.setLayout(new FlowLayout());
                                ogretmen_islemleriFrame.add(addOgretmenButton);
                                ogretmen_islemleriFrame.add(updateOgretmenButton);
                                ogretmen_islemleriFrame.add(deleteOgretmenButton);


                                ogretmen_islemleriFrame.setVisible(true);

                            }
                        });

                        adminActionsFrame.add(ogretmen_islemleriButton);
                      

                        JButton studentOperationsButton = new JButton("Öğrenci İşlemleri");
                        studentOperationsButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                                JFrame studentOperationsFrame = new JFrame("Öğrenci İşlemleri");
                                studentOperationsFrame.setSize(600, 300);
                                studentOperationsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                JButton addStudentButton = new JButton("Öğrenci Ekle");
                                JButton updateStudentButton = new JButton("Öğrenci Güncelle");
                                JButton deleteStudentButton = new JButton("Öğrenci Sil");

                                addStudentButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        
                                        JFrame addStudentFrame = new JFrame("Öğrenci Ekle");
                                        addStudentFrame.setSize(400, 200);

                                      
                                        JPanel addStudentPanel = new JPanel();
                                        addStudentPanel.setLayout(new GridLayout(10, 5));

                                        JLabel nameLabel = new JLabel("Öğrenci Ad-Soyad: ");
                                        JTextField ogrenci_ad_soyadField = new JTextField(20);

                                        JLabel noLabel = new JLabel("Öğrenci No: ");
                                        JTextField ogrenci_noField = new JTextField(20);

                                        JLabel passwordLabel = new JLabel("Öğrenci Şifre: ");
                                        JPasswordField ogrenci_sifreField = new JPasswordField(20);

                                        JLabel ogrenci_ilgi_alanlariLabel = new JLabel("Öğrenci İlgi Alanları: ");
                                        JTextField ogrenci_ilgi_alanlariField = new JTextField(20);

                                        JLabel ogrenci_agnoLabel = new JLabel("Öğrenci AGNO: ");
                                        JTextField ogrenci_agnoField = new JTextField(20);

                                        JButton saveButton = new JButton("Kaydet");
                                        saveButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String name = ogrenci_ad_soyadField.getText();
                                                String no = ogrenci_noField.getText();
                                                String password = new String(ogrenci_sifreField.getPassword());
                                                String ogrenci_ilgi_alanlari = ogrenci_ilgi_alanlariField.getText();
                                                String ogrenci_agno = ogrenci_agnoField.getText();

                                                
                                                try {
                                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                                    String query = "INSERT INTO ogrenci(ogrenci_no, ogrenci_ad_soyad, ogrenci_sifre, ogrenci_ilgi_alanlari, agno) VALUES (?, ?, ?, ?, ?)";
                                                    PreparedStatement statement = conn.prepareStatement(query);
                                                    statement.setString(1, name);
                                                    statement.setString(2, no);
                                                    statement.setString(3, password);
                                                    statement.setString(4, ogrenci_ilgi_alanlari);
                                                    statement.setString(5, ogrenci_agno);

                                                    int rowCount = statement.executeUpdate();
                                                    if (rowCount > 0) {
                                                        JOptionPane.showMessageDialog(addStudentFrame, "Öğrenci eklendi.");
                                                    } else {
                                                        JOptionPane.showMessageDialog(addStudentFrame, "Öğrenci eklenemedi.");
                                                    }

                                                    conn.close();
                                                } catch (SQLException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        });

                                        addStudentPanel.add(nameLabel);
                                        addStudentPanel.add(ogrenci_ad_soyadField);
                                        addStudentPanel.add(noLabel);
                                        addStudentPanel.add(ogrenci_noField);
                                        addStudentPanel.add(passwordLabel);
                                        addStudentPanel.add(ogrenci_sifreField);
                                        addStudentPanel.add(ogrenci_ilgi_alanlariLabel);
                                        addStudentPanel.add(ogrenci_ilgi_alanlariField);
                                        addStudentPanel.add(ogrenci_agnoLabel);
                                        addStudentPanel.add(ogrenci_agnoField);
                                        addStudentPanel.add(saveButton);

                                        addStudentFrame.add(addStudentPanel);
                                        addStudentFrame.setVisible(true);
                                    }
                                });

                               
                                updateStudentButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                       
                                        JFrame updateStudentFrame = new JFrame("Öğrenci Güncelle");
                                        updateStudentFrame.setSize(400, 200);

                                      
                                        JPanel updateStudentPanel = new JPanel();
                                        updateStudentPanel.setLayout(new GridLayout(4, 2));

                                        JLabel nameLabel = new JLabel("Öğrenci Ad-Soyad: ");
                                        JTextField ogrenci_ad_soyadField = new JTextField(20);

                                        JLabel noLabel = new JLabel("Öğrenci No: ");
                                        JTextField ogrenci_noField = new JTextField(20);

                                        JLabel passwordLabel = new JLabel("Yeni Öğrenci Şifre: ");
                                        JPasswordField yeni_ogrenci_sifreField = new JPasswordField(20);

                                        JLabel ogrenci_ilgi_alanlariLabel = new JLabel("Öğrenci İlgi Alanları: ");
                                        JTextField yeni_ogrenci_ilgi_alanlariField = new JTextField(20);

                                        JButton updateButton = new JButton("Güncelle");
                                        updateButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String name = ogrenci_ad_soyadField.getText();
                                                String no = ogrenci_noField.getText();
                                                String newPassword = new String(yeni_ogrenci_sifreField.getPassword());
                                                String ogrenci_ilgi_alanlari = yeni_ogrenci_ilgi_alanlariField.getText();

                                               
                                                try {
                                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                                    String query = "UPDATE ogrenci SET ogrenci_sifre = ? WHERE ogrenci_ad_soyad = ? AND ogrenci_no = ? AND ogrenci_ilgi_alanlari = ?";
                                                    PreparedStatement statement = conn.prepareStatement(query);
                                                    statement.setString(1, newPassword);
                                                    statement.setString(2, name);
                                                    statement.setString(3, no);
                                                    statement.setString(4, ogrenci_ilgi_alanlari);

                                                    int rowCount = statement.executeUpdate();
                                                    if (rowCount > 0) {
                                                        JOptionPane.showMessageDialog(updateStudentFrame, "Öğrenci güncellendi.");
                                                    } else {
                                                        JOptionPane.showMessageDialog(updateStudentFrame, "Öğrenci güncellenemedi. Bilgileri kontrol edin.");
                                                    }

                                                    conn.close();
                                                } catch (SQLException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        });

                                        updateStudentPanel.add(nameLabel);
                                        updateStudentPanel.add(ogrenci_ad_soyadField);
                                        updateStudentPanel.add(noLabel);
                                        updateStudentPanel.add(ogrenci_noField);
                                        updateStudentPanel.add(passwordLabel);
                                        updateStudentPanel.add(yeni_ogrenci_sifreField);
                                        updateStudentPanel.add(ogrenci_ilgi_alanlariLabel);
                                        updateStudentPanel.add(yeni_ogrenci_ilgi_alanlariField);
                                        updateStudentPanel.add(updateButton);

                                        updateStudentFrame.add(updateStudentPanel);
                                        updateStudentFrame.setVisible(true);
                                    }
                                });

                             
                                deleteStudentButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                      
                                        JFrame deleteStudentFrame = new JFrame("Öğrenci Sil");
                                        deleteStudentFrame.setSize(400, 200);

                                       
                                        JPanel deleteStudentPanel = new JPanel();
                                        deleteStudentPanel.setLayout(new GridLayout(10, 5));

                                        JLabel nameLabel = new JLabel("Öğrenci Ad-Soyad: ");
                                        JTextField ogrenci_ad_soyadField = new JTextField(20);

                                        JLabel noLabel = new JLabel("Öğrenci No: ");
                                        JTextField ogrenci_noField = new JTextField(20);

                                        JLabel passwordLabel = new JLabel("Öğrenci Şifre: ");
                                        JPasswordField ogrenci_sifreField = new JPasswordField(20);

                                        JLabel ogrenci_ilgi_alanlariLabel = new JLabel("Öğrenci İlgi Alanları: ");
                                        JTextField ogrenci_ilgi_alanlariField = new JTextField(20);

                                        JButton deleteButton = new JButton("Sil");
                                        deleteButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String name = ogrenci_ad_soyadField.getText();
                                                String no = ogrenci_noField.getText();
                                                String password = new String(ogrenci_sifreField.getPassword());
                                                String ogrenci_ilgi_alanlari = ogrenci_ilgi_alanlariField.getText();

                                               
                                                try {
                                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                                    String query = "DELETE FROM ogrenci WHERE ogrenci_ad_soyad = ? AND ogrenci_no = ? AND ogrenci_sifre = ? AND ogrenci_ilgi_alanlari = ?";
                                                    PreparedStatement statement = conn.prepareStatement(query);
                                                    statement.setString(1, name);
                                                    statement.setString(2, no);
                                                    statement.setString(3, password);
                                                    statement.setString(4, ogrenci_ilgi_alanlari);

                                                    int rowCount = statement.executeUpdate();
                                                    if (rowCount > 0) {
                                                        JOptionPane.showMessageDialog(deleteStudentFrame, "Öğrenci silindi.");
                                                    } else {
                                                        JOptionPane.showMessageDialog(deleteStudentFrame, "Öğrenci silinemedi. Bilgileri kontrol edin.");
                                                    }

                                                    conn.close();
                                                } catch (SQLException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        });

                                        deleteStudentPanel.add(nameLabel);
                                        deleteStudentPanel.add(ogrenci_ad_soyadField);
                                        deleteStudentPanel.add(noLabel);
                                        deleteStudentPanel.add(ogrenci_noField);
                                        deleteStudentPanel.add(passwordLabel);
                                        deleteStudentPanel.add(ogrenci_sifreField);
                                        deleteStudentPanel.add(ogrenci_ilgi_alanlariLabel);
                                        deleteStudentPanel.add(ogrenci_ilgi_alanlariField);
                                        deleteStudentPanel.add(deleteButton);

                                        deleteStudentFrame.add(deleteStudentPanel);
                                        deleteStudentFrame.setVisible(true);
                                    }
                                });

                                studentOperationsFrame.setLayout(new FlowLayout());
                                studentOperationsFrame.add(addStudentButton);
                                studentOperationsFrame.add(updateStudentButton);
                                studentOperationsFrame.add(deleteStudentButton);


                                studentOperationsFrame.setVisible(true);

                            }
                        });

                        adminActionsFrame.add(studentOperationsButton);

                       
                        JButton generateStudentsButton = new JButton("Otomatik Öğrenci Üret");
                        generateStudentsButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");

                                    
                                    String insertQuery = "INSERT INTO ogrenci(ogrenci_no, ogrenci_ad_soyad, ogrenci_sifre, agno) VALUES(?, ?, ?, ?)";
                                    PreparedStatement insertStatement = conn.prepareStatement(insertQuery);

                                    for (int i = 0; i < 50; i++) {
                                       
                                        String ogrenciAdSoyad = generateRandomName();

                                        
                                        String ogrenciNo = generateRandomStudentNumber();

                                        
                                        String ogrenciSifre = generateRandomPassword();

                                       
                                        double ogrenciAGNO = Math.random() * 4;

                                       
                                        insertStatement.setString(1, ogrenciNo);
                                        insertStatement.setString(2, ogrenciAdSoyad);
                                        insertStatement.setString(3, ogrenciSifre);
                                        insertStatement.setDouble(4, ogrenciAGNO);

                                       
                                        insertStatement.executeUpdate();
                                    }

                                    JOptionPane.showMessageDialog(frame, "Otomatik öğrenci üretme işlemi tamamlandı.");
                                    conn.close();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        adminActionsFrame.add(generateStudentsButton);

                        adminActionsFrame.setLayout(new FlowLayout());

                        frame.setLayout(new FlowLayout());
                        adminActionsFrame.add(dersKontenjanButton);

                        adminActionsFrame.add(dersSecimButton);

                        adminActionsFrame.setVisible(true);

                    } else {

                        JOptionPane.showMessageDialog(frame, "Giriş Başarısız. Geçersiz Öğrenci No veya Şifre.");

                    }

                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        adminPanel.add(noLabelAdmin);
        adminPanel.add(noFieldAdmin);
        adminPanel.add(passwordLabelAdmin);
        adminPanel.add(passwordFieldAdmin);
        adminPanel.add(loginButtonAdmin);

        tabbedPane.addTab("Yönetici", adminPanel);

        // ÖĞRENCİ İŞLEMLERİ
        JPanel studentPanel = new JPanel();
        studentPanel.setLayout(new GridLayout(3, 2));

        JLabel noLabelStudent = new JLabel("Öğrenci no: ");
        JTextField noFieldStudent = new JTextField(20);

        JLabel passwordLabelStudent = new JLabel("Öğrenci Şifre: ");
        JPasswordField passwordFieldStudent = new JPasswordField(20);

        JButton loginButtonStudent = new JButton("Giriş Yap");

        loginButtonStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String no = noFieldStudent.getText();
                String password = new String(passwordFieldStudent.getPassword());

              
                try {
                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                    String query1 = "SELECT ogrenci_no, ogrenci_sifre, ogrenci_ad_soyad FROM ogrenci WHERE ogrenci_no = ? AND ogrenci_sifre = ?";
                    PreparedStatement statement = conn.prepareStatement(query1);
                    statement.setString(1, no);
                    statement.setString(2, password);

                    ResultSet result1 = statement.executeQuery();

                    if (result1.next()) {
                        String ogrenciNo = result1.getString("ogrenci_no");
                        String ogrenciAdSoyad = result1.getString("ogrenci_ad_soyad");

                        JOptionPane.showMessageDialog(frame, "Giriş Başarılı! Hoş geldiniz, " + ogrenciAdSoyad);

                        JButton myCoursesButton = new JButton("Onaylanan Derslerim");
                        myCoursesButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                               
                                try {
                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                    String query = "SELECT t.ders_kodu, d.ders_adi "
                                            + "FROM talep AS t "
                                            + "JOIN ders AS d ON t.ders_kodu = d.ders_kodu "
                                            + "WHERE t.ogrenci_no = ? AND t.onay_durumu = 2";
                                    PreparedStatement statement = conn.prepareStatement(query);
                                    statement.setString(1, ogrenciNo); // ogrenciNo, giriş yapan öğrencinin numarasıdır.

                                    ResultSet result = statement.executeQuery();

                                    JFrame myCoursesFrame = new JFrame("Onaylanan Derslerim");
                                    myCoursesFrame.setSize(800, 600);
                                    myCoursesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                    DefaultTableModel tableModel = new DefaultTableModel();
                                    JTable table = new JTable(tableModel);

                                    tableModel.addColumn("Ders Kodu");
                                    tableModel.addColumn("Ders Adı");

                                    while (result.next()) {
                                        String dersKodu = result.getString("ders_kodu");
                                        String dersAdi = result.getString("ders_adi");
                                        tableModel.addRow(new Object[]{dersKodu, dersAdi});
                                    }

                                    JScrollPane scrollPane = new JScrollPane(table);
                                    myCoursesFrame.add(scrollPane);

                                    myCoursesFrame.setVisible(true);

                                    conn.close();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        JFrame studentActionsFrame = new JFrame("Öğrenci İşlemleri");
                        studentActionsFrame.setSize(600, 300);

                        studentActionsFrame.setLayout(new GridLayout(5, 2));
                        studentActionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        JButton transkriptButton = new JButton("Transkript");

                        transkriptButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                
                                JFrame transkriptFrame = new JFrame("Transkript");
                                transkriptFrame.setSize(600, 400);
                                transkriptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Yalnızca bu pencereyi kapat, ana pencereyi kapatma
                                transkriptFrame.setLayout(new BorderLayout());

                                try {
                                   
                                    PDDocument document = Loader.loadPDF(new File("C:\\users\\mert2\\Desktop\\transkript.pdf")); // PDF dosyasının yolunu belirtin
                                    PDFTextStripper textStripper = new PDFTextStripper();
                                    String pdfText = textStripper.getText(document);
                                    document.close();

                                    
                                    String[] lines = pdfText.split("\\n");
                                    int temp = 0;

                                    String dersKodu = null; 

                                    for (String line : lines) {
                                       
                                        String pattern = "^(AIT|BLM|BLM|BLM|ESD|ESD|FEF|FEF|TDB|YDB)(\\d+)(.*(\\(UE\\))?)$";
                                        Pattern p = Pattern.compile(pattern);
                                        Matcher m = p.matcher(line);

                                        if (m.find()) {
                                            dersKodu = m.group(1) + m.group(2);
                                            String dersAdi = m.group(3);
                                            temp = 1;

                                            System.out.printf("Ders Kodu: %s%n", dersKodu);
                                            System.out.printf("Ders Adı: %s%n", dersAdi);

                                           
                                            try {
                                                Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                                String insertQuery = "INSERT INTO transkript (ders_id, ders_adi, ders_harf_notu) VALUES (?, ?, ?)";
                                                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                                                insertStatement.setString(1, dersKodu);
                                                insertStatement.setString(2, dersAdi);
                                                insertStatement.setString(3, null); 

                                                int rowsInserted = insertStatement.executeUpdate();
                                                if (rowsInserted > 0) {
                                                    
                                                    System.out.println("Ders bilgisi başarıyla eklendi.");
                                                } else {
                                                   
                                                    System.out.println("Ders bilgisi eklenirken bir hata oluştu.");
                                                }

                                                connection.close();
                                            } catch (SQLException ex) {
                                                ex.printStackTrace();
                                            }
                                        }

                                       
                                        pattern = "(\\d+)([A-Z]{2})";
                                        p = Pattern.compile(pattern);
                                        m = p.matcher(line);

                                        if (m.find()) {
                                            if (temp == 1) {
                                                String dersHarfNotu = m.group(2);
                                                System.out.printf("Ders Harf Notu: %s%n", dersHarfNotu);
                                                temp = 0;

                                                
                                                try {
                                                    Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                                    String updateQuery = "UPDATE transkript SET ders_harf_notu = ? WHERE ders_id = ?";
                                                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                                                    updateStatement.setString(1, dersHarfNotu);
                                                    updateStatement.setString(2, dersKodu);

                                                    int rowsUpdated = updateStatement.executeUpdate();
                                                    if (rowsUpdated > 0) {
                                                       
                                                        System.out.println("Ders harf notu başarıyla güncellendi.");
                                                    } else {
                                                       
                                                        System.out.println("Ders harf notu güncellenirken bir hata oluştu.");
                                                    }

                                                    connection.close();
                                                } catch (SQLException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }

                               
                                transkriptFrame.setVisible(true);
                            }
                        });

                        JButton onceden_alinan_derslerButton = new JButton("Önceden Alınan Dersler");

                        onceden_alinan_derslerButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                    String query2 = "SELECT ders_id, ders_adi, ders_harf_notu FROM transkript ";
                                    PreparedStatement statement2 = conn.prepareStatement(query2);

                                    ResultSet result2 = statement2.executeQuery();

                                   
                                    DefaultTableModel tableModel = new DefaultTableModel();
                                    tableModel.addColumn("Ders ID");
                                    tableModel.addColumn("Ders Adı");
                                    tableModel.addColumn("Ders Harf Notu");

                                    while (result2.next()) {
                                        String dersId = result2.getString("ders_id");
                                        String dersAdi = result2.getString("ders_adi");
                                        String dersHarfNotu = result2.getString("ders_harf_notu");

                                        
                                        tableModel.addRow(new Object[]{dersId, dersAdi, dersHarfNotu});
                                    }

                                   
                                    JTable jTable = new JTable(tableModel);

                                   
                                    JScrollPane scrollPane = new JScrollPane(jTable);

                                  
                                    JFrame derslerFrame = new JFrame("Önceden Alınan Dersler");
                                    derslerFrame.add(scrollPane);
                                    derslerFrame.pack();
                                    derslerFrame.setVisible(true);

                                    conn.close();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                       
                        JButton mesajYazButton = new JButton("Mesaj Yaz");

                        mesajYazButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JFrame mesajFrame = new JFrame("Mesaj Gönderme Ekranı");
                                mesajFrame.setSize(400, 300);
                                mesajFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                JComboBox<String> mesaj_icin_hocaListesi = new JComboBox<>();

                                try {
                                    Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                    String query = "SELECT hoca_sicil_no, hoca_ad_soyad FROM hoca";
                                    PreparedStatement statement = connection.prepareStatement(query);
                                    ResultSet resultSet = statement.executeQuery();

                                    
                                    while (resultSet.next()) {
                                        String hocaSicilNo = resultSet.getString("hoca_sicil_no");
                                        String hocaAdSoyad = resultSet.getString("hoca_ad_soyad");
                                        mesaj_icin_hocaListesi.addItem(hocaSicilNo + " - " + hocaAdSoyad);
                                    }
                                    connection.close();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }

                               
                                JTextArea mesajTextArea = new JTextArea(5, 20);
                                mesajTextArea.setLineWrap(true);
                                mesajTextArea.setWrapStyleWord(true);

                                
                                JButton gonderButton = new JButton("Gönder");

                                gonderButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String selectedHoca = mesaj_icin_hocaListesi.getSelectedItem().toString();
                                        String mesajMetni = mesajTextArea.getText();

                                       
                                        String[] hocaInfo = selectedHoca.split(" - ");
                                        String hocaSicilNo = hocaInfo[0];

                                        
                                        try {
                                            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                            String insertQuery = "INSERT INTO mesaj (hoca_sicil_no, ogrenci_no, ogrenci_mesaj) VALUES (?, ?, ?)";
                                            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                                            insertStatement.setString(1, hocaSicilNo);
                                            insertStatement.setInt(2, Integer.valueOf(ogrenciNo));
                                            insertStatement.setString(3, mesajMetni);

                                            int rowsInserted = insertStatement.executeUpdate();
                                            if (rowsInserted > 0) {
                                               
                                                JOptionPane.showMessageDialog(null, "Mesaj başarıyla gönderildi.");
                                            } else {
                                               
                                                JOptionPane.showMessageDialog(null, "Mesaj gönderilirken bir hata oluştu.");
                                            }

                                            connection.close();
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                              
                                JPanel panel = new JPanel();
                                panel.add(new JLabel("Hoca Seçin:"));
                                panel.add(mesaj_icin_hocaListesi);
                                panel.add(new JLabel("Mesaj Metni:"));
                                panel.add(new JScrollPane(mesajTextArea));
                                panel.add(gonderButton);

                                mesajFrame.add(panel);
                                mesajFrame.setVisible(true);
                            }
                        });

                        JButton derse_gore_ogretmen_listeleButton = new JButton("Derse Göre Öğretmen Listeleme");

                        derse_gore_ogretmen_listeleButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                

                                JFrame dersListesiFrame = new JFrame("Ders Listesi");
                                dersListesiFrame.setSize(600, 400);
                                dersListesiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                JFrame ilgiAlanlariFrame = new JFrame("Hoca İlgi Alanları");
                                ilgiAlanlariFrame.setSize(400, 300);
                                ilgiAlanlariFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

                                JLabel ilgiAlanlariLabel = new JLabel();

                                
                                JComboBox<String> hocaListesi = new JComboBox<>();
                                
                                try {
                                    Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                    String query = "SELECT hoca_ad_soyad, hoca_sicil_no FROM hoca";
                                    PreparedStatement statement = connection.prepareStatement(query);
                                    ResultSet resultSet = statement.executeQuery();

                                  
                                    while (resultSet.next()) {
                                        String hocaAdSoyad = resultSet.getString("hoca_ad_soyad");
                                        String hocaSicilNo = resultSet.getString("hoca_sicil_no");
                                        hocaListesi.addItem(hocaAdSoyad + " - " + hocaSicilNo);
                                    }
                                    connection.close();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }

                               
                                hocaListesi.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                       
                                        String selectedHoca = hocaListesi.getSelectedItem().toString();
                                        String[] hocaInfo = selectedHoca.split(" - ");
                                        String selectedHocaSicilNo = hocaInfo[1]; 

                                        try {
                                            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                            String query = "SELECT hoca_ilgi_alanlari FROM hoca WHERE hoca_sicil_no = ?";
                                            PreparedStatement statement = conn.prepareStatement(query);
                                            statement.setString(1, selectedHocaSicilNo);
                                            ResultSet result = statement.executeQuery();

                                           
                                            if (result.next()) {
                                                String ilgiAlanlari = result.getString("hoca_ilgi_alanlari");
                                                ilgiAlanlariLabel.setText("Hoca İlgi Alanları: " + ilgiAlanlari);
                                            } else {
                                                ilgiAlanlariLabel.setText("Hoca ilgi alanları bulunamadı.");
                                            }

                                            
                                            ilgiAlanlariFrame.add(ilgiAlanlariLabel);
                                            ilgiAlanlariFrame.setVisible(true);

                                            conn.close();
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                                JButton dersleriListeleButton = new JButton("Dersleri Listele");

                               
                                dersleriListeleButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                       
                                        String selectedHoca = hocaListesi.getSelectedItem().toString();
                                        String[] hocaInfo = selectedHoca.split(" - ");
                                        String selectedHocaSicilNo = hocaInfo[1]; 

                                        JFrame dersFrame = new JFrame("Hoca Dersleri");
                                        dersFrame.setSize(400, 300);
                                        dersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                        
                                        try {
                                            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                            String query = "SELECT ders_adi FROM ders WHERE hoca_sicil_no = ?";
                                            PreparedStatement statement = conn.prepareStatement(query);
                                            statement.setString(1, selectedHocaSicilNo);
                                            ResultSet result = statement.executeQuery();

                                          
                                            JPanel dersPanel = new JPanel();
                                            while (result.next()) {
                                                String dersAdi = result.getString("ders_adi");
                                                JLabel dersLabel = new JLabel(dersAdi);
                                                JButton ekleButton = new JButton("Ekle");

                                                ekleButton.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                       

                                                        String selectedDersAdi = dersAdi; 
                                                        String selectedHocaSicilNo = hocaInfo[1]; 

                                                        try {

                                                            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");

                                                            
                                                            String insertQuery = "INSERT INTO talep (ders_kodu, ogrenci_no, onay_durumu, hoca_sicil_no) VALUES ((SELECT ders_kodu FROM ders WHERE ders_adi = ? AND hoca_sicil_no = ?),?,?,?)";
                                                            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                                                            insertStatement.setInt(3, Integer.valueOf(ogrenciNo)); 
                                                            insertStatement.setString(1, selectedDersAdi);
                                                            insertStatement.setString(2, selectedHocaSicilNo);
                                                            insertStatement.setInt(4, 1);
                                                            insertStatement.setString(5, selectedHocaSicilNo);

                                                            int rowsInserted = insertStatement.executeUpdate();
                                                            if (rowsInserted > 0) {
                                                                
                                                                JOptionPane.showMessageDialog(null, "Ders başarıyla eklendi.");
                                                            } else {
                                                                
                                                                JOptionPane.showMessageDialog(null, "Ders eklenirken bir hata oluştu.");
                                                            }

                                                            connection.close();
                                                        } catch (SQLException ex) {
                                                            ex.printStackTrace();
                                                        }
                                                    }
                                                });

                                               
                                                dersPanel.add(dersLabel);
                                                dersPanel.add(ekleButton);
                                            }

                                           
                                            JScrollPane dersScrollPane = new JScrollPane(dersPanel);
                                            dersFrame.add(dersScrollPane);
                                            dersFrame.setVisible(true);
                                            conn.close();
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                                JPanel panel = new JPanel();
                                panel.add(hocaListesi);
                                panel.add(dersleriListeleButton);

                                dersListesiFrame.add(panel);
                                dersListesiFrame.setVisible(true);

                            }
                        });

                        studentActionsFrame.add(derse_gore_ogretmen_listeleButton);
                        studentActionsFrame.add(mesajYazButton);
                        studentActionsFrame.add(transkriptButton);
                        studentActionsFrame.add(onceden_alinan_derslerButton);
                        studentActionsFrame.add(myCoursesButton);

                        studentActionsFrame.setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(frame, "Giriş Başarısız. Geçersiz Öğrenci No veya Şifre.");
                    }

                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        studentPanel.add(noLabelStudent);
        studentPanel.add(noFieldStudent);
        studentPanel.add(passwordLabelStudent);
        studentPanel.add(passwordFieldStudent);
        studentPanel.add(loginButtonStudent);

        tabbedPane.addTab("Öğrenci", studentPanel);

        // HOCA İŞLEMLERİ
        JPanel teacherPanel = new JPanel();
        teacherPanel.setLayout(new GridLayout(3, 2));

        JLabel sicilLabelTeacher = new JLabel("Öğretmen Sicil No: ");
        JTextField sicilFieldTeacher = new JTextField(20);

        JLabel passwordLabelTeacher = new JLabel("Öğretmen Şifre: ");
        JPasswordField passwordFieldTeacher = new JPasswordField(20);

        JButton loginButtonTeacher = new JButton("Giriş Yap");
        loginButtonTeacher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = sicilFieldTeacher.getText();
                String password = new String(passwordFieldTeacher.getPassword());

                try {
                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                    String query2 = "SELECT hoca_sicil_no, hoca_ad_soyad, hoca_sifre FROM hoca WHERE hoca_sicil_no = ? AND hoca_sifre = ?";
                    PreparedStatement statement = conn.prepareStatement(query2);

                    statement.setString(1, name);
                    statement.setString(2, password);

                    ResultSet result2 = statement.executeQuery();

                    if (result2.next()) {

                        String ogretmenNo = result2.getString("hoca_sicil_no");
                        String ogretmenadsoyad = result2.getString("hoca_ad_soyad");

                        JOptionPane.showMessageDialog(frame, "Giriş Başarılı! Hoş geldiniz, " + ogretmenadsoyad);

                        JFrame teacherOperationsFrame = new JFrame("Öğretmen İşlemleri");

                        teacherOperationsFrame.setSize(800, 600);
                        teacherOperationsFrame.setLayout(new GridLayout(2, 3));
                        teacherOperationsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                       
                        JButton interestAreaButton = new JButton("İlgi Alan Seçme");
                        interestAreaButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JFrame interestAreaFrame = new JFrame("İlgi Alanı Seçme");
                                interestAreaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                interestAreaFrame.setSize(400, 150);
                                interestAreaFrame.setLayout(new BorderLayout());

                                JTextField newInterestAreaField = new JTextField();
                                JButton addInterestAreaButton = new JButton("Ekle");

                                
                                String hocaSicilNo = sicilFieldTeacher.getText();

                                addInterestAreaButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String newInterestArea = newInterestAreaField.getText();

                                        try {
                                            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");

                                            
                                            String updateInterestAreaQuery = "UPDATE hoca SET hoca_ilgi_alanlari = ? WHERE hoca_sicil_no = ?";
                                            PreparedStatement updateStatement = conn.prepareStatement(updateInterestAreaQuery);
                                            updateStatement.setString(1, newInterestArea);
                                            updateStatement.setString(2, hocaSicilNo);
                                            updateStatement.executeUpdate();

                                            JOptionPane.showMessageDialog(interestAreaFrame, "İlgi Alanı başarıyla güncellendi!");
                                            conn.close();
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                                interestAreaFrame.add(newInterestAreaField, BorderLayout.CENTER);
                                interestAreaFrame.add(addInterestAreaButton, BorderLayout.EAST);

                                interestAreaFrame.setVisible(true);
                            }
                        });

                        JButton talep_onayButton = new JButton("Ders Onaylama");
                        talep_onayButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                    String query = "SELECT ders_kodu, ogrenci_no, onay_durumu FROM talep WHERE onay_durumu = 1";
                                    PreparedStatement statement = conn.prepareStatement(query);
                                    ResultSet result = statement.executeQuery();

                                    JFrame studentRequestsFrame = new JFrame("Talepler");
                                    studentRequestsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                    studentRequestsFrame.setSize(600, 400);
                                    studentRequestsFrame.setLayout(new BorderLayout());

                                    DefaultListModel<String> listModel = new DefaultListModel<>();
                                    JList<String> requestList = new JList<>(listModel);

                                    while (result.next()) {
                                        String dersKodu = result.getString("ders_kodu");
                                        int ogrenciNo = result.getInt("ogrenci_no");
                                        listModel.addElement("Ders Kodu: " + dersKodu + " - Öğrenci No: " + ogrenciNo);
                                    }

                                    JScrollPane scrollPane = new JScrollPane(requestList);
                                    studentRequestsFrame.add(scrollPane, BorderLayout.CENTER);

                                    JButton approveButton = new JButton("Talebi Onayla");
                                    JButton rejectButton = new JButton("Talebi Reddet");

                                    approveButton.setEnabled(false);
                                    rejectButton.setEnabled(false);

                                    requestList.addListSelectionListener(new ListSelectionListener() {
                                        public void valueChanged(ListSelectionEvent event) {
                                            if (!event.getValueIsAdjusting()) {
                                                boolean isTalepSelected = requestList.getSelectedValue() != null;
                                                approveButton.setEnabled(isTalepSelected);
                                                rejectButton.setEnabled(isTalepSelected);
                                            }
                                        }
                                    });

                                    approveButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            String selectedDersKodu = requestList.getSelectedValue().split(" ")[2];
                                            updateTalepDurumu(selectedDersKodu, 2);
                                        }
                                    });

                                    rejectButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            String selectedDersKodu = requestList.getSelectedValue().split(" ")[2];
                                            updateTalepDurumu(selectedDersKodu, 3); 
                                        }
                                    });

                                    studentRequestsFrame.add(approveButton, BorderLayout.WEST);
                                    studentRequestsFrame.add(rejectButton, BorderLayout.EAST);

                                    studentRequestsFrame.setVisible(true);
                                    conn.close();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        JButton studentGradingButton = new JButton("Öğrenci Puanlama Sistemi");
                        studentGradingButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                               
                            }
                        });

                        JButton listStudentsByFormulaButton = new JButton("Formüle Göre Öğrenci Listele");
                        listStudentsByFormulaButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                
                            }
                        });

                        JButton talep_olusturan_ogrenciButton = new JButton("Talep Oluşturan Öğrenci Görme");
                        talep_olusturan_ogrenciButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                   
                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                    String query1 = "SELECT ders_kodu, ogrenci_no FROM talep WHERE hoca_sicil_no = ? AND onay_durumu = 1";
                                    PreparedStatement statement1 = conn.prepareStatement(query1);
                                    statement1.setString(1, ogretmenNo); 

                                    ResultSet result1 = statement1.executeQuery();

                                    JFrame ogrenciListesiFrame = new JFrame("Öğrenci Talepleri");
                                    ogrenciListesiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                    ogrenciListesiFrame.setSize(800, 600);
                                    ogrenciListesiFrame.setLayout(new GridLayout(10, 3));

                                    while (result1.next()) {
                                        String dersKodu2 = result1.getString("ders_kodu");
                                        String ogrenciNo = result1.getString("ogrenci_no");

                                        JButton ogrenciButton = new JButton("Öğrenci: " + ogrenciNo);
                                        ogrenciButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                try {
                                                    
                                                    String query2 = "SELECT ders_kodu FROM talep WHERE ogrenci_no = ? AND onay_durumu = 2";
                                                    PreparedStatement statement2 = conn.prepareStatement(query2);
                                                    statement2.setString(1, ogrenciNo);

                                                    ResultSet result2 = statement2.executeQuery();

                                                    JFrame taleplerFrame = new JFrame("Öğrenci Talepleri");
                                                    taleplerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                                    taleplerFrame.setSize(800, 600);
                                                    taleplerFrame.setLayout(new GridLayout(10, 3));

                                                    while (result2.next()) {
                                                        String dersKodu2 = result2.getString("ders_kodu");

                                                        
                                                        JLabel dersKoduLabel = new JLabel("Ders Kodu: " + dersKodu2);
                                                        taleplerFrame.add(dersKoduLabel);
                                                    }

                                                    taleplerFrame.setVisible(true);
                                                } catch (SQLException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        });

                                        ogrenciListesiFrame.add(ogrenciButton);
                                    }

                                    ogrenciListesiFrame.setVisible(true);
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        
                        JButton messageButton = new JButton("Mesaj");
                        messageButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JFrame messageFrame = new JFrame("Mesajlar");
                                messageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
                                messageFrame.setSize(600, 400);
                                messageFrame.setLayout(new BorderLayout());

                                
                                try {
                                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");
                                    String hocaSicilNo = sicilFieldTeacher.getText(); 

                                   
                                    String messageQuery = "SELECT ogrenci.ogrenci_ad_soyad, mesaj.ogrenci_mesaj FROM mesaj "
                                            + "INNER JOIN ogrenci ON mesaj.ogrenci_no = ogrenci.ogrenci_no "
                                            + "WHERE mesaj.hoca_sicil_no = ?";
                                    PreparedStatement messageStatement = conn.prepareStatement(messageQuery);
                                    messageStatement.setString(1, hocaSicilNo);

                                    ResultSet messageResult = messageStatement.executeQuery();

                                   
                                    JTextArea messageTextArea = new JTextArea();
                                    messageTextArea.setEditable(false); 

                                  
                                    while (messageResult.next()) {
                                        String ogrenciAdSoyad = messageResult.getString("ogrenci_ad_soyad");
                                        String ogrenciMesaj = messageResult.getString("ogrenci_mesaj");
                                        messageTextArea.append("Öğrenci Adı: " + ogrenciAdSoyad + "\n");
                                        messageTextArea.append("Mesaj: " + ogrenciMesaj + "\n\n");
                                    }

                                   
                                    JScrollPane scrollPane = new JScrollPane(messageTextArea);

                                 
                                    messageFrame.add(scrollPane, BorderLayout.CENTER);
                                    messageFrame.setLayout(new GridLayout(3, 3));

                                   
                                    JTextField newMessageField = new JTextField();

                                    JButton sendMessageButton = new JButton("Gönder");
                                    sendMessageButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            String newMessage = newMessageField.getText();

                                            try {

                                                Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");

                                                String insertMessageQuery = "INSERT INTO mesaj (hoca_sicil_no, ogrenci_no, hoca_mesaj) VALUES (?, ?, ?)";
                                                PreparedStatement insertStatement = connection.prepareStatement(insertMessageQuery);
                                                insertStatement.setString(1, hocaSicilNo);
                                                
                                                insertStatement.setString(2, "22");
                                                insertStatement.setString(3, newMessage);
                                                insertStatement.executeUpdate();
                                                JOptionPane.showMessageDialog(messageFrame, "Mesaj başarıyla gönderildi!");
                                            } catch (SQLException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    });

                                    
                                    messageFrame.add(newMessageField, BorderLayout.SOUTH);
                                    messageFrame.add(sendMessageButton, BorderLayout.SOUTH);

                                    conn.close();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }

                                messageFrame.setVisible(true);
                            }
                        });

                        
                        teacherOperationsFrame.add(interestAreaButton);
                        teacherOperationsFrame.add(talep_onayButton);
                        teacherOperationsFrame.add(studentGradingButton);
                        teacherOperationsFrame.add(listStudentsByFormulaButton);
                        teacherOperationsFrame.add(talep_olusturan_ogrenciButton);
                        teacherOperationsFrame.add(messageButton);

                        teacherOperationsFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Giriş Başarısız. Geçersiz Sicil Nuamrası veya Şifre.");
                    }

                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        teacherPanel.add(sicilLabelTeacher);
        teacherPanel.add(sicilFieldTeacher);
        teacherPanel.add(passwordLabelTeacher);
        teacherPanel.add(passwordFieldTeacher);
        teacherPanel.add(loginButtonTeacher);

        tabbedPane.addTab("Hoca", teacherPanel);

        frame.add(tabbedPane);

        frame.setVisible(true);
    }

   
    private static String generateRandomName() {
        String[] firstNames = {"Ali", "Ayşe", "Mehmet", "Zeynep", "Emir", "Ezgi", "Can", "Elif", "Cem", "Leyla"};
        String[] lastNames = {"Yılmaz", "Demir", "Kaya", "Öztürk", "Kara", "Arslan", "Koç", "Güner", "Yıldırım", "Sarı"};

        String firstName = firstNames[(int) (Math.random() * firstNames.length)];
        String lastName = lastNames[(int) (Math.random() * lastNames.length)];

        return firstName + " " + lastName;
    }


    private static Set<String> generatedStudentNumbers = new HashSet<>();
    private static String generateRandomStudentNumber() {
        String baseNumber = "2102020";
        String studentNumber;

        do {
            int randomDigits = (int) (Math.random() * 100);
            studentNumber = baseNumber + String.format("%02d", randomDigits);
        } while (generatedStudentNumbers.contains(studentNumber));

        generatedStudentNumbers.add(studentNumber);
        return studentNumber;
    }


    private static String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        int length = 8;

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    private static void updateTalepDurumu(String dersKodu, int durum) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Yaz_lab_1", "postgres", "mert2001");

            
            String updateTalepQuery = "UPDATE talep SET onay_durumu = ? WHERE ders_kodu = ?";
            PreparedStatement updateTalepStatement = conn.prepareStatement(updateTalepQuery);
            updateTalepStatement.setInt(1, durum);
            updateTalepStatement.setString(2, dersKodu);
            int rowsUpdatedTalep = updateTalepStatement.executeUpdate();

            if (rowsUpdatedTalep > 0) {
                JOptionPane.showMessageDialog(null, "Talep başarıyla güncellendi!");

               
                String updateDersQuery = "UPDATE ders SET ders_kontenjan = ders_kontenjan - 1 WHERE ders_kodu = ?";
                PreparedStatement updateDersStatement = conn.prepareStatement(updateDersQuery);
                updateDersStatement.setString(1, dersKodu);
                int rowsUpdatedDers = updateDersStatement.executeUpdate();

                if (rowsUpdatedDers > 0) {
                    JOptionPane.showMessageDialog(null, "Ders kontenjanı güncellendi!");
                } else {
                    JOptionPane.showMessageDialog(null, "Ders kontenjanı güncellenemedi.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Talep güncellenemedi.");
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
