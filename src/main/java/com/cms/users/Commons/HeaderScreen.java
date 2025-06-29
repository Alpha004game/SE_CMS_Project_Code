package com.cms.users.Commons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

/**
 * <<boundary>>
 * HeaderScreen - Componente header riutilizzabile per tutte le interfacce
 */
public class HeaderScreen extends JPanel {
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificationButton;
    private JButton profileButton;
    
    // Attributi originali
    private String userRole;
    private String userName;
    private List<String> navigationMenu;
    private boolean notificationBadge;
    private int unreadNotifications;
    
    // Listener per le azioni
    private ActionListener homeActionListener;
    private ActionListener notificationActionListener;
    private ActionListener profileActionListener;
    
    // Constructor
    public HeaderScreen() {
        this.navigationMenu = new ArrayList<>();
        this.notificationBadge = true;
        this.unreadNotifications = 0;
        this.userName = "Utente";
        this.userRole = "User";
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con dati utente
     */
    public HeaderScreen(String userName, String userRole) {
        this.navigationMenu = new ArrayList<>();
        this.notificationBadge = true;
        this.unreadNotifications = 0;
        this.userName = userName != null ? userName : "Utente";
        this.userRole = userRole != null ? userRole : "User";
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Inizializza i componenti dell'header
     */
    private void initializeComponents() {
        // Configurazione del pannello header
        setBackground(Color.ORANGE);
        setPreferredSize(new Dimension(0, 60));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Bottone Home
        homeButton = new JButton("🏠");
        homeButton.setBackground(Color.ORANGE);
        homeButton.setForeground(Color.BLACK);
        homeButton.setFont(new Font("Arial", Font.BOLD, 20));
        homeButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        homeButton.setFocusPainted(false);
        homeButton.setContentAreaFilled(false);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.setToolTipText("Torna alla Home");
        
        // Bottone Notifiche con pallino rosso
        updateNotificationButton();
        
        // Bottone Profilo
        profileButton = new JButton("👤");
        profileButton.setBackground(Color.ORANGE);
        profileButton.setForeground(Color.BLACK);
        profileButton.setFont(new Font("Arial", Font.BOLD, 18));
        profileButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        profileButton.setFocusPainted(false);
        profileButton.setContentAreaFilled(false);
        profileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profileButton.setToolTipText("Profilo Utente");
    }
    
    /**
     * Aggiorna il bottone delle notifiche
     */
    private void updateNotificationButton() {
        String notificationText = notificationBadge && unreadNotifications > 0 ? "🔔•" : "🔔";
        
        if (notificationButton == null) {
            notificationButton = new JButton(notificationText);
            notificationButton.setBackground(Color.ORANGE);
            notificationButton.setForeground(Color.BLACK);
            notificationButton.setFont(new Font("Arial", Font.BOLD, 18));
            notificationButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            notificationButton.setFocusPainted(false);
            notificationButton.setContentAreaFilled(false);
            notificationButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            notificationButton.setText(notificationText);
        }
        
        // Aggiorna tooltip
        if (unreadNotifications > 0) {
            notificationButton.setToolTipText("Notifiche (" + unreadNotifications + " non lette)");
        } else {
            notificationButton.setToolTipText("Notifiche");
        }
    }
    
    /**
     * Configura il layout dell'header
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Pannello sinistro - Home button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(Color.ORANGE);
        leftPanel.add(homeButton);
        
        // Pannello destro - Notifiche e Profilo
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightPanel.setBackground(Color.ORANGE);
        rightPanel.add(notificationButton);
        rightPanel.add(profileButton);
        
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }
    
    /**
     * Configura i gestori degli eventi di default
     */
    private void setupEventHandlers() {
        homeButton.addActionListener(e -> {
            if (homeActionListener != null) {
                homeActionListener.actionPerformed(e);
            } else {
                navigateToHome();
            }
        });
        
        notificationButton.addActionListener(e -> {
            if (notificationActionListener != null) {
                notificationActionListener.actionPerformed(e);
            } else {
                showNotificationScreen();
            }
        });
        
        profileButton.addActionListener(e -> {
            if (profileActionListener != null) {
                profileActionListener.actionPerformed(e);
            } else {
                navigateToProfile();
            }
        });
    }
    
    // Metodi per impostare listener personalizzati
    
    /**
     * Imposta il listener per il bottone home
     */
    public void setHomeActionListener(ActionListener listener) {
        this.homeActionListener = listener;
    }
    
    /**
     * Imposta il listener per il bottone notifiche
     */
    public void setNotificationActionListener(ActionListener listener) {
        this.notificationActionListener = listener;
    }
    
    /**
     * Imposta il listener per il bottone profilo
     */
    public void setProfileActionListener(ActionListener listener) {
        this.profileActionListener = listener;
    }
    
    // Implementazione metodi originali
    
    
    // Methods
    public void displayUserInfo() {
        JOptionPane.showMessageDialog(this, 
            "Utente: " + userName + "\nRuolo: " + userRole, 
            "Informazioni Utente", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showNavigationMenu() {
        if (navigationMenu.isEmpty()) {
            navigationMenu.add("Home");
            navigationMenu.add("Conferenze");
            navigationMenu.add("Notifiche");
            navigationMenu.add("Profilo");
        }
        
        String[] options = navigationMenu.toArray(new String[0]);
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Seleziona una destinazione:",
            "Menu di Navigazione",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (selected != null) {
            JOptionPane.showMessageDialog(this, "Navigazione a: " + selected);
        }
    }
    
    public void displayNotificationBadge() {
        this.notificationBadge = true;
        updateNotificationButton();
        repaint();
    }
    
    public void navigateToProfile() {
        // Implementazione di default - apre UserMenu sotto il bottone profilo
        try {
            Class<?> userMenuClass = Class.forName("com.cms.users.account.Interface.UserMenu");
            Object userMenu = userMenuClass.getDeclaredConstructor(String.class, String.class)
                .newInstance(userName, userName + "@cms.com");
            java.lang.reflect.Method showMenuMethod = userMenuClass.getMethod("showMenuBelowButton", JButton.class);
            showMenuMethod.invoke(userMenu, profileButton);
        } catch (Exception e) {
            // Fallback se UserMenu non è disponibile - mostra menu semplificato
            showSimpleProfileMenu();
        }
    }
    
    /**
     * Mostra un menu profilo semplificato come fallback
     */
    private void showSimpleProfileMenu() {
        String[] options = {"Informazioni Profilo", "Impostazioni", "Logout"};
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Seleziona un'opzione:",
            "Menu Profilo",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (selected != null) {
            switch (selected) {
                case "Informazioni Profilo":
                    openUserInfoScreen();
                    break;
                case "Impostazioni":
                    JOptionPane.showMessageDialog(this, "Apertura impostazioni...");
                    break;
                case "Logout":
                    logout();
                    break;
            }
        }
    }
    
    /**
     * Apre la schermata delle informazioni profilo
     */
    private void openUserInfoScreen() {
        try {
            Class<?> userInfoClass = Class.forName("com.cms.users.account.Interface.UserInfoScreen");
            Object userInfoScreen = userInfoClass.getDeclaredConstructor(String.class, String.class)
                .newInstance(userName, userName + "@cms.com");
            java.lang.reflect.Method createMethod = userInfoClass.getMethod("create");
            createMethod.invoke(userInfoScreen);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Apertura profilo utente...");
        }
    }
    
    /**
     * Naviga alla home screen
     */
    private void navigateToHome() {
        // Implementazione di default - apre HomeScreen
        try {
            Class<?> homeScreenClass = Class.forName("com.cms.users.Commons.HomeScreen");
            Object homeScreen = homeScreenClass.getDeclaredConstructor().newInstance();
            java.lang.reflect.Method displayMethod = homeScreenClass.getMethod("displayUserDashboard");
            displayMethod.invoke(homeScreen);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Navigazione alla Home...");
        }
    }
    
    /**
     * Mostra la schermata delle notifiche
     */
    private void showNotificationScreen() {
        // Implementazione di default - apre NotificationScreen
        try {
            Class<?> notificationClass = Class.forName("com.cms.users.notifications.Interface.NotificationScreen");
            Object notificationScreen = notificationClass.getDeclaredConstructor().newInstance();
            java.lang.reflect.Method createMethod = notificationClass.getMethod("create");
            createMethod.invoke(notificationScreen);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Apertura notifiche...");
        }
    }
    
    public void logout() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Sei sicuro di voler effettuare il logout?",
            "Conferma Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            // Qui andrà la logica di logout
            JOptionPane.showMessageDialog(this, "Logout effettuato con successo!");
            
            // Apertura LoginScreen
            try {
                Class<?> loginClass = Class.forName("com.cms.users.account.Interface.LoginScreen");
                Object loginScreen = loginClass.getDeclaredConstructor().newInstance();
                java.lang.reflect.Method createMethod = loginClass.getMethod("create");
                createMethod.invoke(loginScreen);
            } catch (Exception e) {
                System.err.println("Errore nell'apertura LoginScreen: " + e.getMessage());
            }
        }
    }
    
    public void updateNotificationCount() {
        updateNotificationButton();
        repaint();
    }
    
    public void refreshHeader() {
        updateNotificationButton();
        repaint();
    }
    
    // Getter e Setter
    
    public String getUserRole() {
        return userRole;
    }
    
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public int getUnreadNotifications() {
        return unreadNotifications;
    }
    
    public void setUnreadNotifications(int unreadNotifications) {
        this.unreadNotifications = unreadNotifications;
        updateNotificationCount();
    }
    
    public boolean isNotificationBadge() {
        return notificationBadge;
    }
    
    public void setNotificationBadge(boolean notificationBadge) {
        this.notificationBadge = notificationBadge;
        displayNotificationBadge();
    }
    
    public List<String> getNavigationMenu() {
        return navigationMenu;
    }
    
    public void setNavigationMenu(List<String> navigationMenu) {
        this.navigationMenu = navigationMenu;
    }
    
    public void addNavigationItem(String item) {
        if (navigationMenu == null) {
            navigationMenu = new ArrayList<>();
        }
        navigationMenu.add(item);
    }
    
    // Metodi di utilità
    
    /**
     * Nasconde il pallino delle notifiche
     */
    public void hideNotificationBadge() {
        this.notificationBadge = false;
        updateNotificationButton();
        repaint();
    }
    
    /**
     * Incrementa il contatore delle notifiche non lette
     */
    public void incrementUnreadNotifications() {
        this.unreadNotifications++;
        updateNotificationCount();
    }
    
    /**
     * Resetta il contatore delle notifiche non lette
     */
    public void resetUnreadNotifications() {
        this.unreadNotifications = 0;
        updateNotificationCount();
    }
    
    /**
     * Imposta i dati utente
     */
    public void setUserData(String userName, String userRole) {
        this.userName = userName;
        this.userRole = userRole;
        
        // Aggiorna i tooltip
        profileButton.setToolTipText("Profilo: " + userName);
    }
    
    /**
     * Metodo main per testing standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame("Test HeaderScreen");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setSize(800, 150);
            testFrame.setLocationRelativeTo(null);
            
            HeaderScreen header = new HeaderScreen("Mario Rossi", "Chair");
            header.setUnreadNotifications(3);
            
            testFrame.add(header, BorderLayout.NORTH);
            
            // Pannello di test
            JPanel testPanel = new JPanel();
            testPanel.setBackground(Color.WHITE);
            testPanel.add(new JLabel("Contenuto della pagina sotto l'header"));
            testFrame.add(testPanel, BorderLayout.CENTER);
            
            testFrame.setVisible(true);
        });
    }
    
}
