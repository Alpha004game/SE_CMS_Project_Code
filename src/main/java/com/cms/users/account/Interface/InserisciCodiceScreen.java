package com.cms.users.account.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <<boundary>>
 * InserisciCodiceScreen
 */
public class InserisciCodiceScreen extends JFrame {
    
    // Componenti Swing
    private JTextField codiceField;
    private JButton verificaButton;
    private JLabel titleLabel;
    
    // Attributi originali
    private int codice;
    
    public InserisciCodiceScreen() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setTitle("Inserisci codice di verifica");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);
    }
    
    private void initializeComponents() {
        // Titolo
        titleLabel = new JLabel("Inserisci il codice ricevuto via email");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Campo codice - grigio chiaro come nell'immagine, più largo
        codiceField = new JTextField(30);
        codiceField.setBackground(new Color(220, 220, 220));
        codiceField.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        codiceField.setFont(new Font("Arial", Font.PLAIN, 16));
        codiceField.setHorizontalAlignment(SwingConstants.CENTER); // Centra il testo
        
        // Bottone arancione
        verificaButton = new JButton("Verifica");
        verificaButton.setBackground(new Color(255, 140, 0));
        verificaButton.setForeground(Color.WHITE);
        verificaButton.setFont(new Font("Arial", Font.BOLD, 14));
        verificaButton.setPreferredSize(new Dimension(150, 45));
        verificaButton.setMinimumSize(new Dimension(150, 45));
        verificaButton.setMaximumSize(new Dimension(150, 45));
        verificaButton.setFocusPainted(false);
        verificaButton.setBorderPainted(false);
        verificaButton.setOpaque(true);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Titolo
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Campo codice con spazio ridotto
        JPanel codiceFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        codiceFieldPanel.setBackground(Color.WHITE);
        codiceFieldPanel.add(codiceField);
        codiceFieldPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        
        // Bottone con margine ridotto
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(verificaButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Aggiungi tutti i panel
        mainPanel.add(titlePanel);
        mainPanel.add(codiceFieldPanel);
        mainPanel.add(buttonPanel);
        
        // Spazio aggiuntivo in basso per assicurarci che il bottone sia visibile
        mainPanel.add(Box.createVerticalGlue());
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Debug: verifica che il bottone sia stato aggiunto
        System.out.println("Bottone verificaButton aggiunto: " + (verificaButton != null));
        System.out.println("Dimensioni bottone: " + verificaButton.getPreferredSize());
    }
    
    private void setupEventHandlers() {
        verificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificaButton();
            }
        });
        
        // Enter key per verificare
        codiceField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificaButton();
            }
        });
    }
    
    // Metodi originali implementati
    public void create() {
        SwingUtilities.invokeLater(() -> {
            mostraInserisciCodiceScreen();
        });
    }
    
    public String inserisciCodiceVerifica(int codice) {
        codiceField.setText(String.valueOf(codice));
        this.codice = codice;
        return String.valueOf(codice);
    }
    
    public void verificaButton() {
        String codiceText = codiceField.getText().trim();
        
        // Validazione codice vuoto
        if (codiceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Per favore inserisci il codice di verifica", 
                "Codice richiesto", 
                JOptionPane.WARNING_MESSAGE);
            codiceField.requestFocus();
            return;
        }
        
        // Validazione formato numerico
        try {
            codice = Integer.parseInt(codiceText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Il codice deve contenere solo numeri", 
                "Formato non valido", 
                JOptionPane.ERROR_MESSAGE);
            codiceField.requestFocus();
            codiceField.selectAll();
            return;
        }
        
        // Validazione lunghezza codice (assumiamo 6 cifre)
        if (codiceText.length() != 6) {
            JOptionPane.showMessageDialog(this, 
                "Il codice deve essere di 6 cifre", 
                "Lunghezza non valida", 
                JOptionPane.ERROR_MESSAGE);
            codiceField.requestFocus();
            codiceField.selectAll();
            return;
        }
        
        // Qui andrà la logica di verifica del codice tramite control
        System.out.println("Verifica codice: " + codice);
        
        // Simulazione verifica codice
        if (verificaCodice(codice)) {
            JOptionPane.showMessageDialog(this, 
                "Codice verificato con successo!\n" +
                "Ora puoi procedere con il reset della password.", 
                "Verifica completata", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Qui apriresti la schermata per inserire la nuova password
            aprireCreaNuovaPasswordScreen();
            
        } else {
            JOptionPane.showMessageDialog(this, 
                "Il codice inserito non è valido o è scaduto.\n" +
                "Riprova o richiedi un nuovo codice.", 
                "Codice non valido", 
                JOptionPane.ERROR_MESSAGE);
            
            codiceField.setText("");
            codiceField.requestFocus();
        }
    }
    
    public int getCodiceVerifica() {
        try {
            return Integer.parseInt(codiceField.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public void mostraInserisciCodiceScreen() {
        setVisible(true);
        codiceField.requestFocus(); // Focus automatico sul campo
    }
    
    // Metodi di utilità aggiuntivi
    public void setCodice(int codice) {
        codiceField.setText(String.valueOf(codice));
        this.codice = codice;
    }
    
    public void setCodice(String codice) {
        codiceField.setText(codice);
    }
    
    public void clearCodice() {
        codiceField.setText("");
        this.codice = 0;
    }
    
    public String getCodiceText() {
        return codiceField.getText().trim();
    }
    
    public boolean isValidCodice(String codice) {
        if (codice == null || codice.trim().isEmpty()) {
            return false;
        }
        try {
            int cod = Integer.parseInt(codice.trim());
            return codice.trim().length() == 6 && cod > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public void focusCodiceField() {
        codiceField.requestFocus();
    }
    
    public void selectAllCodice() {
        codiceField.selectAll();
    }
    
    // Simulazione verifica codice (da sostituire con logica reale)
    private boolean verificaCodice(int codice) {
        // Qui andrà la logica reale di verifica del codice
        // Per ora simula una verifica semplice
        return codice == 123456 || codice == 654321; // Codici di test
    }
    
    // Metodo per aprire la schermata di creazione nuova password
    private void aprireCreaNuovaPasswordScreen() {
        // Qui andrà l'apertura della CreaNuovaPasswordScreen
        System.out.println("Apertura schermata creazione nuova password...");
        
        // Opzionalmente chiudi questa finestra
        dispose();
        
        // Apertura della schermata reale
        SwingUtilities.invokeLater(() -> {
            CreaNuovaPasswordScreen creaNuovaPasswordScreen = new CreaNuovaPasswordScreen();
            creaNuovaPasswordScreen.create();
        });
    }
    
    // Main per testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InserisciCodiceScreen().create();
        });
    }
    
}
