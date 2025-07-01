package com.cms.users.account.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.cms.users.account.Control.RecuperoCredenzialiControl;

/**
 * <<boundary>>
 * RecuperoCredenzialiScreen
 */
public class RecuperoCredenzialiScreen extends JFrame {
    
    // Componenti Swing
    private JTextField emailField;
    private JButton continuaButton;
    private JLabel titleLabel;
    private JLabel emailLabel;
    
    // Attributi originali
    private String email;
    
    // Control per gestire la logica
    private RecuperoCredenzialiControl recuperoControl;
    
    public RecuperoCredenzialiScreen() {
        // Inizializza il control
        this.recuperoControl = new RecuperoCredenzialiControl();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setTitle("Recupera credenziali");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);
    }
    
    private void initializeComponents() {
        // Titolo
        titleLabel = new JLabel("Recupera credenziali");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Label per email
        emailLabel = new JLabel("Inserisci la tua Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Campo email - grigio chiaro come nell'immagine
        emailField = new JTextField(30);
        emailField.setBackground(new Color(220, 220, 220));
        emailField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Bottone arancione
        continuaButton = new JButton("Continua");
        continuaButton.setBackground(new Color(255, 140, 0));
        continuaButton.setForeground(Color.WHITE);
        continuaButton.setFont(new Font("Arial", Font.BOLD, 14));
        continuaButton.setPreferredSize(new Dimension(120, 40));
        continuaButton.setFocusPainted(false);
        continuaButton.setBorderPainted(false);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 50, 60, 50));
        
        // Titolo
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        
        // Email label
        JPanel emailLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailLabelPanel.setBackground(Color.WHITE);
        emailLabelPanel.add(emailLabel);
        emailLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Email field
        JPanel emailFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        emailFieldPanel.setBackground(Color.WHITE);
        emailFieldPanel.add(emailField);
        emailFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        
        // Bottone
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(continuaButton);
        
        // Aggiungi tutti i panel
        mainPanel.add(titlePanel);
        mainPanel.add(emailLabelPanel);
        mainPanel.add(emailFieldPanel);
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        continuaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recuperoPasswordButton();
            }
        });
        
        // Enter key per continuare
        emailField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recuperoPasswordButton();
            }
        });
    }
    
    // Metodi originali implementati
    public void create() {
        SwingUtilities.invokeLater(() -> {
            mostraRecuperoCredenzialiScreen();
        });
    }
    
    public String inserisciEmail(String email) {
        emailField.setText(email);
        this.email = email;
        return email;
    }
    
    public void recuperoPasswordButton() {
        email = emailField.getText().trim();
        
        // Validazione email vuota
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Per favore inserisci la tua email", 
                "Email richiesta", 
                JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return;
        }
        
        // Validazione formato email
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, 
                "Inserisci un indirizzo email valido", 
                "Email non valida", 
                JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return;
        }
        
        // Disabilita il bottone durante l'elaborazione
        continuaButton.setEnabled(false);
        continuaButton.setText("Invio in corso...");
        
        // Esegui il recupero credenziali in un thread separato per non bloccare l'UI
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return recuperoControl.recuperaCredenziali(email);
            }
            
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    
                    // Riabilita il bottone
                    continuaButton.setEnabled(true);
                    continuaButton.setText("Continua");
                    
                    if (success) {
                        JOptionPane.showMessageDialog(RecuperoCredenzialiScreen.this, 
                            "Le istruzioni per il recupero delle credenziali\n" +
                            "sono state inviate all'indirizzo:\n" + email + "\n\n" +
                            "Controlla la tua casella di posta elettronica\n" +
                            "(inclusa la cartella spam).\n\n" +
                            "Si aprirà ora la schermata per inserire il codice.", 
                            "Email inviata", 
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Chiudi questa finestra
                        dispose();
                        
                    } else {
                        JOptionPane.showMessageDialog(RecuperoCredenzialiScreen.this, 
                            "Indirizzo email non trovato nel sistema\n" +
                            "o errore durante l'invio dell'email.\n\n" +
                            "Verifica l'indirizzo email e riprova.", 
                            "Errore", 
                            JOptionPane.ERROR_MESSAGE);
                        emailField.requestFocus();
                        emailField.selectAll();
                    }
                    
                } catch (Exception e) {
                    // Riabilita il bottone in caso di errore
                    continuaButton.setEnabled(true);
                    continuaButton.setText("Continua");
                    
                    System.err.println("Errore durante il recupero credenziali: " + e.getMessage());
                    e.printStackTrace();
                    
                    JOptionPane.showMessageDialog(RecuperoCredenzialiScreen.this, 
                        "Errore durante il recupero delle credenziali.\n" +
                        "Riprova più tardi.", 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    public String getEmail() {
        return emailField.getText().trim();
    }
    
    public void mostraRecuperoCredenzialiScreen() {
        setVisible(true);
    }
    
    // Metodi di utilità aggiuntivi
    public void setEmail(String email) {
        emailField.setText(email);
        this.email = email;
    }
    
    public void clearEmail() {
        emailField.setText("");
        this.email = "";
    }
    
    public boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".") && !email.trim().isEmpty();
    }
    
    public void focusEmailField() {
        emailField.requestFocus();
    }
    
    // Main per testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RecuperoCredenzialiScreen().create();
        });
    }
    
}
