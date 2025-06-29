package com.cms.users.account.Interface;

import javax.swing.*;
import java.awt.*;
import com.cms.users.account.Control.GestioneUtenteControl;

/**
 * <<boundary>>
 * UserInfoScreen - Schermata per la gestione delle informazioni del profilo utente
 */
public class UserInfoScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    private JLabel usernameValueLabel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton salvaModificheButton;
    
    // Attributi originali
    private String username;
    private String email;
    private String password;
    
    // Riferimento al controllo per la gestione delle operazioni
    private GestioneUtenteControl gestioneUtenteControl;
    
    // Riferimento al menu utente per aggiornamenti
    private UserMenu parentUserMenu;
    
    /**
     * Costruttore
     */
    public UserInfoScreen() {
        this.username = "Username";
        this.email = "Email";
        this.password = "";
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con dati utente
     */
    public UserInfoScreen(String username, String email) {
        this.username = username != null ? username : "Username";
        this.email = email != null ? email : "Email";
        this.password = "";
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con riferimento al UserMenu
     */
    public UserInfoScreen(String username, String email, UserMenu parentUserMenu) {
        this.username = username != null ? username : "Username";
        this.email = email != null ? email : "Email";
        this.password = "";
        this.parentUserMenu = parentUserMenu;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Informazioni Profilo");
        setSize(500, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Bottoni header
        homeButton = new JButton("🏠");
        homeButton.setBackground(Color.ORANGE);
        homeButton.setForeground(Color.WHITE);
        homeButton.setFont(new Font("Arial", Font.BOLD, 16));
        homeButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        homeButton.setFocusPainted(false);
        
        notificheButton = new JButton("🔔•");
        notificheButton.setBackground(Color.ORANGE);
        notificheButton.setForeground(Color.WHITE);
        notificheButton.setFont(new Font("Arial", Font.BOLD, 16));
        notificheButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        notificheButton.setFocusPainted(false);
        
        profiloButton = new JButton("👤");
        profiloButton.setBackground(Color.ORANGE);
        profiloButton.setForeground(Color.WHITE);
        profiloButton.setFont(new Font("Arial", Font.BOLD, 16));
        profiloButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        profiloButton.setFocusPainted(false);
        
        // Campo Username (solo lettura)
        usernameValueLabel = new JLabel("\"" + username + "\"");
        usernameValueLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        usernameValueLabel.setForeground(Color.DARK_GRAY);
        
        // Campo Email (editabile)
        emailField = new JTextField(email, 20);
        emailField.setBackground(Color.LIGHT_GRAY);
        emailField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Campo Password (editabile)
        passwordField = new JPasswordField(20);
        passwordField.setBackground(Color.LIGHT_GRAY);
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setText(password); // Placeholder o password attuale
        
        // Bottone Salva modifiche
        salvaModificheButton = new JButton("Salva modifiche");
        salvaModificheButton.setBackground(Color.ORANGE);
        salvaModificheButton.setForeground(Color.WHITE);
        salvaModificheButton.setFont(new Font("Arial", Font.BOLD, 14));
        salvaModificheButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        salvaModificheButton.setFocusPainted(false);
        salvaModificheButton.setPreferredSize(new Dimension(150, 40));
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
        
        // Bottone home a sinistra
        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftHeaderPanel.setBackground(Color.ORANGE);
        leftHeaderPanel.add(homeButton);
        
        // Bottoni notifiche e profilo a destra
        JPanel rightHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightHeaderPanel.setBackground(Color.ORANGE);
        rightHeaderPanel.add(notificheButton);
        rightHeaderPanel.add(profiloButton);
        
        headerPanel.add(leftHeaderPanel, BorderLayout.WEST);
        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        // Pannello principale
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 50, 20, 50));
        
        // Titolo
        JLabel titleLabel = new JLabel("Informazioni del tuo profilo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        mainPanel.add(titleLabel);
        
        // Campo Username
        JPanel usernamePanel = createFieldPanel("Username:", usernameValueLabel);
        mainPanel.add(usernamePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Campo Email
        JPanel emailPanel = createFieldPanel("Email:", emailField);
        mainPanel.add(emailPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Campo Password
        JPanel passwordPanel = createFieldPanel("Password:", passwordField);
        mainPanel.add(passwordPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Bottone Salva modifiche
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(salvaModificheButton);
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Crea un pannello per un campo con label
     */
    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Label
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Campo
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (field instanceof JTextField || field instanceof JPasswordField) {
            field.setMaximumSize(new Dimension(300, 40));
        }
        panel.add(field);
        
        return panel;
    }
    
    /**
     * Configura i gestori degli eventi
     */
    private void setupEventHandlers() {
        homeButton.addActionListener(e -> handleHomeAction());
        notificheButton.addActionListener(e -> handleNotificheAction());
        profiloButton.addActionListener(e -> handleProfiloAction());
        salvaModificheButton.addActionListener(e -> salvaModifiche());
        
        // Enter key per salvare
        emailField.addActionListener(e -> salvaModifiche());
        passwordField.addActionListener(e -> salvaModifiche());
    }
    
    // Gestori degli eventi
    
    private void handleHomeAction() {
        dispose();
        // Qui andrà l'apertura della HomeScreen
        try {
            Class<?> homeScreenClass = Class.forName("com.cms.users.Commons.HomeScreen");
            Object homeScreen = homeScreenClass.getDeclaredConstructor().newInstance();
            java.lang.reflect.Method displayMethod = homeScreenClass.getMethod("displayUserDashboard");
            displayMethod.invoke(homeScreen);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Navigazione alla Home...");
        }
    }
    
    private void handleNotificheAction() {
        // Crea e mostra il menu utente
        UserMenu userMenu = new UserMenu(username, emailField.getText());
        userMenu.showMenuBelowButton(notificheButton);
    }
    
    private void handleProfiloAction() {
        JOptionPane.showMessageDialog(this, "Sei già nella schermata del profilo!");
    }
    
    // Metodi originali implementati
    
    /**
     * Crea e mostra la schermata
     */
    public void create() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            emailField.requestFocus();
        });
    }
    
    /**
     * Placeholder per modificare informazioni
     */
    public void modificaInfo() {
        // Abilita la modifica dei campi (già abilitata di default)
        emailField.setEditable(true);
        passwordField.setEditable(true);
    }
    
    /**
     * Imposta il riferimento al controllo di gestione utente
     */
    public void setGestioneUtenteControl(GestioneUtenteControl control) {
        this.gestioneUtenteControl = control;
    }
    
    /**
     * Salva le modifiche effettuate
     */
    public void salvaModifiche() {
        String newEmail = emailField.getText().trim();
        String newPassword = new String(passwordField.getPassword());
        
        // Validazione email
        if (newEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "L'email non può essere vuota!", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return;
        }
        
        if (!isValidEmail(newEmail)) {
            JOptionPane.showMessageDialog(this, 
                "Inserisci un indirizzo email valido!", 
                "Email non valida", 
                JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return;
        }
        
        // Validazione password (se inserita)
        if (!newPassword.isEmpty() && newPassword.length() < 8) {
            JOptionPane.showMessageDialog(this, 
                "La password deve essere di almeno 8 caratteri!", 
                "Password non sicura", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        // Usa GestioneUtenteControl per salvare come da sequence diagram
        if (gestioneUtenteControl != null) {
            gestioneUtenteControl.salvaModifiche(newEmail, newPassword.isEmpty() ? null : newPassword);
        } else {
            // Fallback se il controllo non è impostato
            JOptionPane.showMessageDialog(this, 
                "Errore: Controllo di gestione non disponibile!", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Chiude la schermata
     */
    public void destroy() {
        SwingUtilities.invokeLater(() -> {
            setVisible(false);
            dispose();
        });
    }
    
    // Metodi di utilità
    
    /**
     * Valida il formato dell'email
     */
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }
    
    /**
     * Imposta i dati utente
     */
    public void setUserData(String username, String email) {
        this.username = username != null ? username : "Username";
        this.email = email != null ? email : "Email";
        
        if (usernameValueLabel != null) {
            usernameValueLabel.setText("\"" + this.username + "\"");
        }
        if (emailField != null) {
            emailField.setText(this.email);
        }
    }
    
    /**
     * Ottiene l'email corrente
     */
    public String getEmail() {
        return emailField != null ? emailField.getText() : email;
    }
    
    /**
     * Ottiene la password corrente
     */
    public String getPassword() {
        return passwordField != null ? new String(passwordField.getPassword()) : password;
    }
    
    /**
     * Ottiene l'username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Imposta il riferimento al menu utente parent
     */
    public void setParentUserMenu(UserMenu parentUserMenu) {
        this.parentUserMenu = parentUserMenu;
    }
    
    /**
     * Pulisce i campi password
     */
    public void clearPassword() {
        if (passwordField != null) {
            passwordField.setText("");
        }
    }
    
    /**
     * Metodo main per testing standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserInfoScreen screen = new UserInfoScreen("Mario Rossi", "mario.rossi@universita.it");
            screen.create();
        });
    }
}
