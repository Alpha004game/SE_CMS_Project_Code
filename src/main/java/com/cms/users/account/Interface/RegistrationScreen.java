package com.cms.users.account.Interface;

import javax.swing.*;

import com.cms.users.account.Control.RegistrationControl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <<boundary>>
 * RegistrationScreen
 */
public class RegistrationScreen extends JFrame {
    
    // Componenti Swing
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confermaPasswordField;
    private JButton registratiButton;
    private JLabel titleLabel;
    private JLabel emailLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel confermaPasswordLabel;
    
    // Attributi originali
    private String campoEmail;
    private String campoUsername;
    private String campoPassword;
    private String campoConfermaPassword;

    private RegistrationControl registrationControl;
    
    public RegistrationScreen(RegistrationControl registrationControl) {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setTitle("4.4.2 RegistrationScreen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 750);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);
        this.registrationControl = registrationControl;
    }
    
    private void initializeComponents() {
        // Titolo
        titleLabel = new JLabel("Inserisci le tue credenziali");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Labels
        emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        confermaPasswordLabel = new JLabel("Conferma Password:");
        confermaPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Text fields - grigio chiaro come nell'immagine
        emailField = new JTextField(25);
        emailField.setBackground(new Color(220, 220, 220));
        emailField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        usernameField = new JTextField(25);
        usernameField.setBackground(new Color(220, 220, 220));
        usernameField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        passwordField = new JPasswordField(25);
        passwordField.setBackground(new Color(220, 220, 220));
        passwordField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        confermaPasswordField = new JPasswordField(25);
        confermaPasswordField.setBackground(new Color(220, 220, 220));
        confermaPasswordField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // Bottone arancione
        registratiButton = new JButton("Registrati");
        registratiButton.setBackground(new Color(255, 140, 0));
        registratiButton.setForeground(Color.WHITE);
        registratiButton.setFont(new Font("Arial", Font.BOLD, 14));
        registratiButton.setPreferredSize(new Dimension(120, 35));
        registratiButton.setFocusPainted(false);
        registratiButton.setBorderPainted(false);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 50, 50, 50));
        
        // Titolo
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        
        // Email
        JPanel emailLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailLabelPanel.setBackground(Color.WHITE);
        emailLabelPanel.add(emailLabel);
        
        JPanel emailFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        emailFieldPanel.setBackground(Color.WHITE);
        emailFieldPanel.add(emailField);
        emailFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Username
        JPanel usernameLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernameLabelPanel.setBackground(Color.WHITE);
        usernameLabelPanel.add(usernameLabel);
        
        JPanel usernameFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernameFieldPanel.setBackground(Color.WHITE);
        usernameFieldPanel.add(usernameField);
        usernameFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Password
        JPanel passwordLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordLabelPanel.setBackground(Color.WHITE);
        passwordLabelPanel.add(passwordLabel);
        
        JPanel passwordFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordFieldPanel.setBackground(Color.WHITE);
        passwordFieldPanel.add(passwordField);
        passwordFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Conferma Password
        JPanel confermaLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        confermaLabelPanel.setBackground(Color.WHITE);
        confermaLabelPanel.add(confermaPasswordLabel);
        
        JPanel confermaFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        confermaFieldPanel.setBackground(Color.WHITE);
        confermaFieldPanel.add(confermaPasswordField);
        confermaFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Bottone
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(registratiButton);
        
        // Aggiungi tutti i panel
        mainPanel.add(titlePanel);
        mainPanel.add(emailLabelPanel);
        mainPanel.add(emailFieldPanel);
        mainPanel.add(usernameLabelPanel);
        mainPanel.add(usernameFieldPanel);
        mainPanel.add(passwordLabelPanel);
        mainPanel.add(passwordFieldPanel);
        mainPanel.add(confermaLabelPanel);
        mainPanel.add(confermaFieldPanel);
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrationButton();
            }
        });
        
        // Enter key per registrazione
        confermaPasswordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrationButton();
            }
        });
    }
    
    // Metodi originali implementati
    public void create() {
        SwingUtilities.invokeLater(() -> {
            mostraRegistrationScreen();
        });
    }
    
    public String inserisciCredenziali(String email, String username, String password, String confermaPassword) {
        emailField.setText(email);
        usernameField.setText(username);
        passwordField.setText(password);
        confermaPasswordField.setText(confermaPassword);
        return email + ":" + username + ":" + password + ":" + confermaPassword;
    }
    
    public void registrationButton() {
        campoEmail = emailField.getText().trim();
        campoUsername = usernameField.getText().trim();
        campoPassword = new String(passwordField.getPassword());
        campoConfermaPassword = new String(confermaPasswordField.getPassword());
        
        // Validazioni
        if (campoEmail.isEmpty() || campoUsername.isEmpty() || 
            campoPassword.isEmpty() || campoConfermaPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Per favore compila tutti i campi", 
                "Campi obbligatori", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validazione email
        if (!campoEmail.contains("@") || !campoEmail.contains(".")) {
            JOptionPane.showMessageDialog(this, 
                "Inserisci un indirizzo email valido", 
                "Email non valida", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validazione password COMPLETARE VALIDAZIONE
        if (campoPassword.length() < 8) {
            JOptionPane.showMessageDialog(this, 
                "La password deve essere di almeno 6 caratteri", 
                "Password troppo corta", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        registrationControl.registra();
        
        
        clearFields();
    }
    
    public String getCredenziali() {
        return campoEmail + ":" + campoUsername + ":" + campoPassword + ":" + campoConfermaPassword;
    }
    
    public void mostraRegistrationScreen() {
        setVisible(true);
    }
    
    // Metodi di utilità
    public String getEmail() {
        return emailField.getText();
    }
    
    public String getUsername() {
        return usernameField.getText();
    }
    
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    
    public String getConfermaPassword() {
        return new String(confermaPasswordField.getPassword());
    }
    
    public void clearFields() {
        emailField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        confermaPasswordField.setText("");
    }
    
    public void setEmail(String email) {
        emailField.setText(email);
    }
    
    public void setUsername(String username) {
        usernameField.setText(username);
    }
    
    
    
}
