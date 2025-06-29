package com.cms.users.Commons;

import javax.swing.*;
import java.awt.*;

/**
 * Esempio di utilizzo dell'HeaderScreen integrato in una finestra
 */
public class ExampleWithHeaderScreen extends JFrame {
    
    private HeaderScreen header;
    
    public ExampleWithHeaderScreen() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setTitle("CMS - Esempio con Header");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Crea l'header con dati utente
        header = new HeaderScreen("Mario Rossi", "Chair");
        header.setUnreadNotifications(5);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Aggiungi l'header in cima
        add(header, BorderLayout.NORTH);
        
        // Contenuto principale
        JPanel mainContent = new JPanel();
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Contenuto della Pagina");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        mainContent.add(titleLabel);
        add(mainContent, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        // Personalizza le azioni dell'header
        header.setHomeActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Azione Home personalizzata!");
        });
        
        header.setNotificationActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Apertura notifiche personalizzata!");
            // Resetta il contatore dopo aver aperto le notifiche
            header.resetUnreadNotifications();
        });
        
        header.setProfileActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Apertura profilo personalizzata!");
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExampleWithHeaderScreen().setVisible(true);
        });
    }
}
