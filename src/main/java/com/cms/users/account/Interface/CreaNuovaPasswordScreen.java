package com.cms.users.account.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * <<boundary>>
 * CreaNuovaPasswordScreen - Interfaccia grafica per la creazione di una nuova password
 */
public class CreaNuovaPasswordScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JPasswordField passwordField;
    private JPasswordField confermaPasswordField;
    private JButton confermaButton;
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    
    // Costruttore
    public CreaNuovaPasswordScreen() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Crea Nuova Password");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Campi password
        passwordField = new JPasswordField(20);
        passwordField.setBackground(Color.LIGHT_GRAY);
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        confermaPasswordField = new JPasswordField(20);
        confermaPasswordField.setBackground(Color.LIGHT_GRAY);
        confermaPasswordField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        confermaPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Bottone conferma
        confermaButton = new JButton("Conferma");
        confermaButton.setBackground(Color.ORANGE);
        confermaButton.setForeground(Color.WHITE);
        confermaButton.setFont(new Font("Arial", Font.BOLD, 14));
        confermaButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        confermaButton.setFocusPainted(false);
        
        // Bottoni header
        homeButton = new JButton("🏠 HOME");
        homeButton.setBackground(Color.ORANGE);
        homeButton.setForeground(Color.WHITE);
        homeButton.setFont(new Font("Arial", Font.BOLD, 12));
        homeButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        homeButton.setFocusPainted(false);
        
        notificheButton = new JButton("🔔");
        notificheButton.setBackground(Color.ORANGE);
        notificheButton.setForeground(Color.WHITE);
        notificheButton.setFont(new Font("Arial", Font.BOLD, 14));
        notificheButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        notificheButton.setFocusPainted(false);
        
        profiloButton = new JButton("👤");
        profiloButton.setBackground(Color.ORANGE);
        profiloButton.setForeground(Color.WHITE);
        profiloButton.setFont(new Font("Arial", Font.BOLD, 14));
        profiloButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        profiloButton.setFocusPainted(false);
    }
    
    /**
     * Configura il layout dell'interfaccia
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header arancione
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.ORANGE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Pannello bottoni header a destra
        JPanel headerButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        headerButtonsPanel.setBackground(Color.ORANGE);
        headerButtonsPanel.add(homeButton);
        headerButtonsPanel.add(notificheButton);
        headerButtonsPanel.add(profiloButton);
        
        headerPanel.add(headerButtonsPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        // Pannello centrale
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        // Titolo
        JLabel titleLabel = new JLabel("Crea una nuova Password", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        centerPanel.add(titleLabel);
        
        // Campo password
        JLabel passwordLabel = new JLabel("Nuova Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(passwordLabel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(300, 40));
        centerPanel.add(passwordField);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Campo conferma password
        JLabel confermaPasswordLabel = new JLabel("Conferma Password:");
        confermaPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        confermaPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(confermaPasswordLabel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        confermaPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        confermaPasswordField.setMaximumSize(new Dimension(300, 40));
        centerPanel.add(confermaPasswordField);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Bottone conferma
        confermaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confermaButton.setMaximumSize(new Dimension(120, 40));
        centerPanel.add(confermaButton);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    /**
     * Configura i gestori degli eventi
     */
    private void setupEventHandlers() {
        // Gestore per il bottone conferma
        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleConfermaAction();
            }
        });
        
        // Gestore per il tasto Enter sui campi password
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleConfermaAction();
                }
            }
        };
        
        passwordField.addKeyListener(enterKeyListener);
        confermaPasswordField.addKeyListener(enterKeyListener);
        
        // Gestori per i bottoni header
        homeButton.addActionListener(e -> handleHomeAction());
        notificheButton.addActionListener(e -> handleNotificheAction());
        profiloButton.addActionListener(e -> handleProfiloAction());
    }
    
    /**
     * Gestisce l'azione di conferma della nuova password
     */
    private void handleConfermaAction() {
        String password = new String(passwordField.getPassword());
        String confermaPassword = new String(confermaPasswordField.getPassword());
        
        // Validazione dei campi
        if (password.isEmpty() || confermaPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Tutti i campi sono obbligatori!", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validazione lunghezza password
        if (password.length() < 8) {
            JOptionPane.showMessageDialog(this, 
                "La password deve essere di almeno 8 caratteri!", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        // Validazione corrispondenza password
        if (!password.equals(confermaPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Le password non corrispondono!", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            confermaPasswordField.setText("");
            confermaPasswordField.requestFocus();
            return;
        }
        
        // Validazione sicurezza password (almeno una maiuscola, una minuscola, un numero)
        if (!isPasswordSecure(password)) {
            JOptionPane.showMessageDialog(this, 
                "La password deve contenere almeno:\n" +
                "- Una lettera maiuscola\n" +
                "- Una lettera minuscola\n" +
                "- Un numero", 
                "Password non sicura", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Se tutto è valido, procedi con la creazione della password
        JOptionPane.showMessageDialog(this, 
            "Password creata con successo!", 
            "Successo", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Qui dovresti chiamare la logica di controllo per salvare la nuova password
        // Esempio: controllerInstance.salvaPassword(password);
        
        clearFields();
        
        // Naviga alla schermata di login o home
        navigateToHome();
    }
    
    /**
     * Verifica se la password rispetta i criteri di sicurezza
     */
    private boolean isPasswordSecure(String password) {
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        
        return hasUpper && hasLower && hasDigit;
    }
    
    /**
     * Gestisce l'azione del bottone Home
     */
    private void handleHomeAction() {
        // Placeholder per navigazione alla home
        JOptionPane.showMessageDialog(this, "Navigazione alla Home");
    }
    
    /**
     * Gestisce l'azione del bottone Notifiche
     */
    private void handleNotificheAction() {
        // Placeholder per navigazione alle notifiche
        JOptionPane.showMessageDialog(this, "Apertura Notifiche");
    }
    
    /**
     * Gestisce l'azione del bottone Profilo
     */
    private void handleProfiloAction() {
        // Crea e mostra il menu utente
        com.cms.users.account.Interface.UserMenu userMenu = 
            new com.cms.users.account.Interface.UserMenu("Utente", "utente@cms.com");
        userMenu.showMenuBelowButton(profiloButton);
    }
    
    /**
     * Naviga alla schermata Home dopo il reset della password
     */
    private void navigateToHome() {
        // Chiudi questa finestra
        dispose();
        
        // Apri la HomeScreen
        SwingUtilities.invokeLater(() -> {
            try {
                // Assumendo che HomeScreen sia nel package Commons
                Class<?> homeScreenClass = Class.forName("com.cms.users.Commons.HomeScreen");
                Object homeScreen = homeScreenClass.getDeclaredConstructor().newInstance();
                
                // Chiama il metodo displayUserDashboard() se esiste
                java.lang.reflect.Method displayMethod = homeScreenClass.getMethod("displayUserDashboard");
                displayMethod.invoke(homeScreen);
                
            } catch (Exception e) {
                // Se non riesce a caricare dinamicamente, mostra un messaggio
                JOptionPane.showMessageDialog(null, 
                    "Password cambiata con successo!\n" +
                    "Accesso alla Home Screen...", 
                    "Successo", 
                    JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Navigazione alla HomeScreen: " + e.getMessage());
            }
        });
    }

    // Metodi pubblici per l'integrazione con la logica di controllo
    
    /**
     * Implementazione del metodo originale per inserire password
     */
    public String inserisciPassword(String password, String confermaPassword) {
        passwordField.setText(password);
        confermaPasswordField.setText(confermaPassword);
        return password.equals(confermaPassword) ? password : null;
    }
    
    /**
     * Restituisce la password inserita
     */
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    
    /**
     * Restituisce la conferma password inserita
     */
    public String getConfermaPassword() {
        return new String(confermaPasswordField.getPassword());
    }
    
    /**
     * Crea e mostra la schermata
     */
    public void create() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            passwordField.requestFocus();
        });
    }
    
    /**
     * Mostra la schermata di creazione nuova password
     */
    public void mostraCreaNuovaPasswordScreen() {
        create();
    }
    
    /**
     * Chiude e distrugge la schermata
     */
    public void destroy() {
        SwingUtilities.invokeLater(() -> {
            setVisible(false);
            dispose();
        });
    }
    
    /**
     * Pulisce i campi della form
     */
    public void clearFields() {
        passwordField.setText("");
        confermaPasswordField.setText("");
    }
    
    /**
     * Imposta il focus sul primo campo
     */
    public void setFocusOnFirstField() {
        passwordField.requestFocus();
    }
    
    /**
     * Valida le password inserite
     */
    public boolean validatePasswords() {
        String password = getPassword();
        String confermaPassword = getConfermaPassword();
        
        return !password.isEmpty() && 
               !confermaPassword.isEmpty() && 
               password.equals(confermaPassword) && 
               password.length() >= 8 &&
               isPasswordSecure(password);
    }
    
    /**
     * Metodo main per testing standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CreaNuovaPasswordScreen screen = new CreaNuovaPasswordScreen();
            screen.create();
        });
    }
}
