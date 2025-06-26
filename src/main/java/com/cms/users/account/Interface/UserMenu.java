package com.cms.users.account.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * <<boundary>>
 * UserMenu - Menu popup dell'utente che si apre dalla campanella delle notifiche
 */
public class UserMenu extends JPopupMenu {
    
    // Componenti del menu
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JButton gestioneAccountButton;
    private JButton logoutButton;
    
    // Dati utente
    private String username;
    private String email;
    
    // Riferimento al componente parent per positioning
    private Component parentComponent;
    
    /**
     * Costruttore
     */
    public UserMenu() {
        this.username = "Username"; // Default
        this.email = "Email"; // Default
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con dati utente
     */
    public UserMenu(String username, String email) {
        this.username = username != null ? username : "Username";
        this.email = email != null ? email : "Email";
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Inizializza i componenti del menu
     */
    private void initializeComponents() {
        // Configurazione popup
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setPreferredSize(new Dimension(250, 200));
        
        // Header del menu
        titleLabel = new JLabel("User Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(80, 80, 80)); // Grigio scuro
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Informazioni utente
        usernameLabel = new JLabel("Username: \"" + username + "\"");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        emailLabel = new JLabel("Email: \"" + email + "\"");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        
        // Bottoni
        gestioneAccountButton = new JButton("Gestione Account");
        gestioneAccountButton.setBackground(Color.ORANGE);
        gestioneAccountButton.setForeground(Color.WHITE);
        gestioneAccountButton.setFont(new Font("Arial", Font.BOLD, 12));
        gestioneAccountButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        gestioneAccountButton.setFocusPainted(false);
        gestioneAccountButton.setPreferredSize(new Dimension(180, 35));
        
        logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.ORANGE);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.setFocusPainted(false);
        logoutButton.setPreferredSize(new Dimension(180, 35));
    }
    
    /**
     * Configura il layout del menu
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Pannello principale con sfondo grigio scuro
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(80, 80, 80)); // Grigio scuro come nel mockup
        
        // Header
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        
        // Spazio
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Informazioni utente 
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(usernameLabel);
        
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(emailLabel);
        
        // Spazio prima dei bottoni
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Pannello bottoni
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(80, 80, 80));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 35, 20, 35));
        
        gestioneAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(gestioneAccountButton);
        
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(logoutButton);
        
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Configura i gestori degli eventi
     */
    private void setupEventHandlers() {
        gestioneAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gestioneAccountButton();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutButton();
            }
        });
        
        // Chiudi il menu quando si clicca fuori
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                // Nascondi il menu se il mouse esce dall'area
                if (!contains(e.getPoint())) {
                    setVisible(false);
                }
            }
        });
    }
    
    // Metodi originali implementati
    
    /**
     * Crea e mostra il menu popup
     */
    public void create() {
        // Il menu è già creato nel costruttore
        // Questo metodo può essere usato per aggiornare i dati se necessario
        updateUserInfo();
    }
    
    /**
     * Mostra il menu popup relativo a un componente
     */
    public void showMenu(Component parent, int x, int y) {
        this.parentComponent = parent;
        show(parent, x, y);
    }
    
    /**
     * Mostra il menu popup sotto un bottone (tipicamente la campanella)
     */
    public void showMenuBelowButton(JButton button) {
        this.parentComponent = button;
        // Calcola la posizione sotto il bottone
        Point buttonLocation = button.getLocationOnScreen();
        Point parentLocation = button.getParent().getLocationOnScreen();
        
        int x = buttonLocation.x - parentLocation.x;
        int y = buttonLocation.y - parentLocation.y + button.getHeight() + 5;
        
        show(button.getParent(), x, y);
    }
    
    /**
     * Gestisce l'azione del bottone Logout
     */
    public void logoutButton() {
        setVisible(false); // Nascondi il menu
        
        int result = JOptionPane.showConfirmDialog(
            parentComponent,
            "Sei sicuro di voler effettuare il logout?",
            "Conferma Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(
                parentComponent,
                "Logout effettuato con successo!",
                "Logout",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Qui andrà la logica per il logout reale
            performLogout();
        }
    }
    
    /**
     * Gestisce l'azione del bottone Gestione Account
     */
    public void gestioneAccountButton() {
        setVisible(false); // Nascondi il menu
        
        // Apri direttamente la schermata di gestione account
        openAccountManagement();
    }
    
    /**
     * Distrugge il menu
     */
    public void destroy() {
        setVisible(false);
        removeAll();
    }
    
    // Metodi di utilità
    
    /**
     * Imposta i dati dell'utente
     */
    public void setUserData(String username, String email) {
        this.username = username != null ? username : "Username";
        this.email = email != null ? email : "Email";
        updateUserInfo();
    }
    
    /**
     * Aggiorna le informazioni utente visualizzate
     */
    private void updateUserInfo() {
        if (usernameLabel != null) {
            usernameLabel.setText("Username: \"" + username + "\"");
        }
        if (emailLabel != null) {
            emailLabel.setText("Email: \"" + email + "\"");
        }
        repaint();
    }
    
    /**
     * Esegue il logout (placeholder per la logica reale)
     */
    private void performLogout() {
        // Qui andrà la logica reale per il logout
        System.out.println("Performing logout for user: " + username);
        
        // Esempio: chiudi tutte le finestre e torna al login
        SwingUtilities.invokeLater(() -> {
            // Chiudi tutte le finestre aperte
            for (Window window : Window.getWindows()) {
                if (window instanceof JFrame && window.isDisplayable()) {
                    window.dispose();
                }
            }
            
            // Apri la schermata di login
            try {
                Class<?> loginClass = Class.forName("com.cms.users.account.Interface.LoginScreen");
                Object loginScreen = loginClass.getDeclaredConstructor().newInstance();
                java.lang.reflect.Method createMethod = loginClass.getMethod("create");
                createMethod.invoke(loginScreen);
            } catch (Exception e) {
                System.out.println("Could not open LoginScreen: " + e.getMessage());
            }
        });
    }
    
    /**
     * Apre la gestione account (placeholder per la logica reale)
     */
    private void openAccountManagement() {
        // Qui andrà l'apertura della schermata di gestione account
        System.out.println("Opening account management for user: " + username);
        
        // Apri la UserInfoScreen
        SwingUtilities.invokeLater(() -> {
            UserInfoScreen userInfoScreen = new UserInfoScreen(username, email, this);
            userInfoScreen.create();
        });
    }
    
    /**
     * Ottiene l'username corrente
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Ottiene l'email corrente
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Metodo main per testing standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Crea una finestra di test
            JFrame testFrame = new JFrame("Test UserMenu");
            testFrame.setSize(400, 300);
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setLocationRelativeTo(null);
            testFrame.setLayout(new FlowLayout());
            
            // Crea un bottone per testare il menu
            JButton testButton = new JButton("🔔 Notifiche");
            testButton.setBackground(Color.ORANGE);
            testButton.setForeground(Color.WHITE);
            testButton.setFont(new Font("Arial", Font.BOLD, 14));
            
            // Crea il menu utente
            UserMenu userMenu = new UserMenu("TestUser", "test@example.com");
            
            // Aggiungi l'azione al bottone
            testButton.addActionListener(e -> {
                userMenu.showMenuBelowButton(testButton);
            });
            
            testFrame.add(testButton);
            testFrame.setVisible(true);
        });
    }
}
