package com.cms.users.account.Interface;

import javax.swing.*;

import com.cms.users.account.Control.LoginControl;

import com.cms.users.account.Control.RegistrationControl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * <<boundary>>
 * LoginScreen
 */
public class LoginScreen extends JFrame {
    
    // Componenti Swing
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton accediButton;
    private JButton registratiButton;
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel recuperoLabel;
    
    // Attributi originali
    private String campoUsername;
    private String campoPassword;

    
    public LoginScreen() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setTitle("4.4.1 LoginScreen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

    }
    
    private void initializeComponents() {
        // Titolo
        titleLabel = new JLabel("Inserisci le tue credenziali");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Labels
        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Text fields - grigio chiaro come nell'immagine
        usernameField = new JTextField(20);
        usernameField.setBackground(new Color(220, 220, 220));
        usernameField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        passwordField = new JPasswordField(20);
        passwordField.setBackground(new Color(220, 220, 220));
        passwordField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // Bottoni arancioni
        accediButton = new JButton("Accedi");
        accediButton.setBackground(Color.ORANGE);
        accediButton.setForeground(Color.BLACK);
        accediButton.setFont(new Font("Arial", Font.BOLD, 14));
        accediButton.setPreferredSize(new Dimension(100, 35));
        accediButton.setMinimumSize(new Dimension(100, 35));
        accediButton.setMaximumSize(new Dimension(100, 35));
        accediButton.setFocusPainted(false);
        accediButton.setOpaque(true);
        accediButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        registratiButton = new JButton("Registrati");
        registratiButton.setBackground(Color.ORANGE);
        registratiButton.setForeground(Color.BLACK);
        registratiButton.setFont(new Font("Arial", Font.BOLD, 14));
        registratiButton.setPreferredSize(new Dimension(100, 35));
        registratiButton.setMinimumSize(new Dimension(100, 35));
        registratiButton.setMaximumSize(new Dimension(100, 35));
        registratiButton.setFocusPainted(false);
        registratiButton.setOpaque(true);
        registratiButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        // Link per recupero credenziali - blu come nell'immagine
        recuperoLabel = new JLabel("<html><u>Username o password dimenticata</u></html>");
        recuperoLabel.setForeground(new Color(0, 100, 200));
        recuperoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        recuperoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        recuperoLabel.setHorizontalAlignment(SwingConstants.CENTER);
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
        
        // Username
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernamePanel.setBackground(Color.WHITE);
        usernamePanel.add(usernameLabel);
        
        JPanel usernameFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernameFieldPanel.setBackground(Color.WHITE);
        usernameFieldPanel.add(usernameField);
        usernameFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Password
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.add(passwordLabel);
        
        JPanel passwordFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordFieldPanel.setBackground(Color.WHITE);
        passwordFieldPanel.add(passwordField);
        passwordFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Link recupero
        JPanel recuperoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        recuperoPanel.setBackground(Color.WHITE);
        recuperoPanel.add(recuperoLabel);
        recuperoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Bottoni
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Debug: verifica che i bottoni esistano
        System.out.println("Aggiungendo bottone Accedi: " + (accediButton != null));
        System.out.println("Aggiungendo bottone Registrati: " + (registratiButton != null));
        
        buttonPanel.add(accediButton);
        buttonPanel.add(registratiButton);
        
        // Forza il refresh del pannello
        buttonPanel.revalidate();
        buttonPanel.repaint();
        
        // Aggiungi tutti i panel
        mainPanel.add(titlePanel);
        mainPanel.add(usernamePanel);
        mainPanel.add(usernameFieldPanel);
        mainPanel.add(passwordPanel);
        mainPanel.add(passwordFieldPanel);
        mainPanel.add(recuperoPanel);
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        accediButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton();
            }
        });
        
        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrationButton();
            }
        });
        
        recuperoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                recuperoCredenzialiButton();
            }
        });
        
        // Enter key per login
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton();
            }
        });
    }
    
    // Metodi originali implementati
    public String inserisciCredenziali(String username, String password) {
        usernameField.setText(username);
        passwordField.setText(password);
        return username + ":" + password;
    }
    
    public void loginButton() {
        campoUsername = usernameField.getText().trim();
        campoPassword = new String(passwordField.getPassword());
        
        // Validazione base
        if (campoUsername.isEmpty() || campoPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Per favore inserisci username e password", 
                "Campi obbligatori", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // LOGICA LOGIN
        new LoginControl().accedi(this);
        
    }
    
    public String getCredenziali() {
        return campoUsername + ":" + campoPassword;
    }
    
    public void mostraLoginScreen() {
        setVisible(true);
    }
    
    public void create() {
        SwingUtilities.invokeLater(() -> {
            mostraLoginScreen();
        });
    }
    
    public void registrationButton() {
        System.out.println("Apertura schermata registrazione");
        new RegistrationControl().create();
        this.dispose();
    }
    
    public void recuperoCredenzialiButton() {
        System.out.println("DEBUG LoginScreen: Apertura schermata recupero credenziali");
        
        try {
            // Crea e mostra la RecuperoCredenzialiScreen
            com.cms.users.account.Interface.RecuperoCredenzialiScreen recuperoScreen = 
                new com.cms.users.account.Interface.RecuperoCredenzialiScreen();
            recuperoScreen.create();
            
        } catch (Exception e) {
            System.err.println("Errore durante l'apertura della schermata di recupero credenziali: " + e.getMessage());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, 
                "Errore durante l'apertura della schermata di recupero credenziali", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Metodi di utilità
    public String getUsername() {
        return usernameField.getText();
    }
    
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
    
    public void setUsername(String username) {
        usernameField.setText(username);
    }
    
    // Main per testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginScreen().create();
        });
    }
    
}
