package com.cms.users.notifications.Interface;

import javax.swing.*;
import java.awt.*;
import com.cms.users.notifications.Control.NotificationControl;

/**
 * <<boundary>>
 * DetailsNotificationScreen - Schermata per la visualizzazione dei dettagli di una notifica
 */
public class DetailsNotificationScreen extends JFrame {
    
    // Enumerazione per i tipi di notifica
    public enum NotificationType {
        PRESA_VISIONE,    // Notifica che richiede solo conferma di lettura
        ACCETTA_RIFIUTA   // Notifica che richiede una scelta (accetta/rifiuta)
    }
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    private JLabel typeLabel;
    private JTextArea contentArea;
    private JButton presaVisioneButton;
    private JButton accettaButton;
    private JButton rifiutaButton;
    private JPanel buttonPanel;
    
    // Dati
    private NotificationType currentType;
    private String notificationContent;
    private String notificationTitle;
    private NotificationControl notificationControl;
    
    /**
     * Costruttore con tipo di notifica e control
     */
    public DetailsNotificationScreen(NotificationType type, NotificationControl control) {
        this.currentType = type;
        this.notificationTitle = "Tipo notifica";
        this.notificationContent = "Testo notifica";
        this.notificationControl = control;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateButtonsForType();
    }
    
    /**
     * Costruttore con tipo di notifica (senza control)
     */
    public DetailsNotificationScreen(NotificationType type) {
        this(type, null);
    }
    
    /**
     * Costruttore di default (tipo presa visione, senza control)
     */
    public DetailsNotificationScreen() {
        this(NotificationType.PRESA_VISIONE, null);
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        setTitle("Dettagli Notifica");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Bottoni header
        homeButton = createHeaderButton("🏠");
        notificheButton = createHeaderButton("🔔");
        profiloButton = createHeaderButton("👤");
        
        // Highlight del bottone notifiche (è attivo)
        notificheButton.setBackground(new Color(255, 140, 0));
        
        // Label per il tipo di notifica
        typeLabel = new JLabel(notificationTitle);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Area di testo per il contenuto della notifica
        contentArea = new JTextArea(notificationContent);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        contentArea.setEditable(false);
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Bottoni di azione
        presaVisioneButton = new JButton("Conferma presa visione");
        styleActionButton(presaVisioneButton);
        
        accettaButton = new JButton("Accetta");
        styleActionButton(accettaButton);
        
        rifiutaButton = new JButton("Rifiuta");
        styleActionButton(rifiutaButton);
        
        // Pannello per i bottoni
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
    }
    
    /**
     * Crea un bottone per l'header
     */
    private JButton createHeaderButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.ORANGE);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setPreferredSize(new Dimension(50, 40));
        return button;
    }
    
    /**
     * Applica lo stile ai bottoni di azione
     */
    private void styleActionButton(JButton button) {
        button.setBackground(Color.ORANGE);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setPreferredSize(new Dimension(180, 45));
    }
    
    /**
     * Configura il layout dell'interfaccia
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header arancione
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
        
        // Pannello principale
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Crea il pannello header
     */
    private JPanel createHeader() {
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
        
        return headerPanel;
    }
    
    /**
     * Crea il pannello principale
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        // Pannello superiore con titolo
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        topPanel.add(typeLabel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Pannello centrale con contenuto notifica
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        centerPanel.add(contentArea, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Pannello bottoni in basso
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Aggiorna i bottoni in base al tipo di notifica
     */
    private void updateButtonsForType() {
        buttonPanel.removeAll();
        
        if (currentType == NotificationType.PRESA_VISIONE) {
            // Solo bottone "Conferma presa visione"
            buttonPanel.add(presaVisioneButton);
        } else if (currentType == NotificationType.ACCETTA_RIFIUTA) {
            // Bottoni "Accetta" e "Rifiuta"
            buttonPanel.add(accettaButton);
            buttonPanel.add(rifiutaButton);
        }
        
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }
    
    /**
     * Configura i gestori di eventi
     */
    private void setupEventHandlers() {
        // Bottone home
        homeButton.addActionListener(e -> {
            dispose();
            try {
                Class<?> homeScreenClass = Class.forName("com.cms.users.Commons.HomeScreen");
                Object homeScreen = homeScreenClass.getDeclaredConstructor().newInstance();
                java.lang.reflect.Method displayMethod = homeScreenClass.getMethod("displayUserDashboard");
                displayMethod.invoke(homeScreen);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Navigazione alla Home...");
            }
        });
        
        // Bottone notifiche
        notificheButton.addActionListener(e -> {
            dispose();
            try {
                Class<?> notificationScreenClass = Class.forName("com.cms.users.notifications.Interface.NotificationScreen");
                Object notificationScreen = notificationScreenClass.getDeclaredConstructor().newInstance();
                java.lang.reflect.Method createMethod = notificationScreenClass.getMethod("create");
                createMethod.invoke(notificationScreen);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Navigazione alle notifiche...");
            }
        });
        
        // Bottone profilo
        profiloButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Apertura profilo utente...");
        });
        
        // Bottone presa visione
        presaVisioneButton.addActionListener(e -> {
            presaVisioneButton();
        });
        
        // Bottone accetta
        accettaButton.addActionListener(e -> {
            accettazioneButton();
        });
        
        // Bottone rifiuta
        rifiutaButton.addActionListener(e -> {
            rifiutoButton();
        });
    }
    
    // Metodi dell'interfaccia originale
    
    /**
     * Crea e mostra la schermata
     */
    public void create() {
        setVisible(true);
    }
    
    /**
     * Imposta il tipo di notifica
     */
    public void setType(String type) {
        if (type == null) {
            currentType = NotificationType.PRESA_VISIONE;
        } else if (type.toLowerCase().contains("accetta") || type.toLowerCase().contains("rifiuta")) {
            currentType = NotificationType.ACCETTA_RIFIUTA;
        } else {
            currentType = NotificationType.PRESA_VISIONE;
        }
        updateButtonsForType();
    }
    
    /**
     * Gestisce il bottone "Conferma presa visione"
     */
    public void presaVisioneButton() {
        System.out.println("DEBUG DetailsNotificationScreen: === INIZIO presaVisioneButton ===");
        
        int result = JOptionPane.showConfirmDialog(this,
            "Confermi di aver preso visione della notifica?",
            "Conferma Presa Visione",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                if (notificationControl != null) {
                    System.out.println("DEBUG DetailsNotificationScreen: Chiamando notificationControl.presaVisione()");
                    notificationControl.presaVisione();
                    
                    JOptionPane.showMessageDialog(this,
                        "Presa visione confermata.",
                        "Confermato",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.err.println("DEBUG DetailsNotificationScreen: ERRORE - notificationControl è null");
                    JOptionPane.showMessageDialog(this,
                        "Errore: sistema non configurato correttamente.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                }
                
                // Torna alla schermata notifiche
                returnToNotifications();
                
            } catch (Exception ex) {
                System.err.println("DEBUG DetailsNotificationScreen: ERRORE in presaVisioneButton: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Errore durante la conferma: " + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        System.out.println("DEBUG DetailsNotificationScreen: === FINE presaVisioneButton ===");
    }
    
    /**
     * Gestisce il bottone "Accetta"
     */
    public void accettazioneButton() {
        System.out.println("DEBUG DetailsNotificationScreen: === INIZIO accettazioneButton ===");
        
        int result = JOptionPane.showConfirmDialog(this,
            "Sei sicuro di voler accettare questa richiesta?",
            "Conferma Accettazione",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                if (notificationControl != null) {
                    System.out.println("DEBUG DetailsNotificationScreen: Chiamando notificationControl.accettazione()");
                    notificationControl.accettazione();
                    
                    JOptionPane.showMessageDialog(this,
                        "Richiesta accettata con successo.",
                        "Accettata",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.err.println("DEBUG DetailsNotificationScreen: ERRORE - notificationControl è null");
                    JOptionPane.showMessageDialog(this,
                        "Errore: sistema non configurato correttamente.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                }
                
                // Torna alla schermata notifiche
                returnToNotifications();
                
            } catch (Exception ex) {
                System.err.println("DEBUG DetailsNotificationScreen: ERRORE in accettazioneButton: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Errore durante l'accettazione: " + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        System.out.println("DEBUG DetailsNotificationScreen: === FINE accettazioneButton ===");
    }
    
    /**
     * Gestisce il bottone "Rifiuta"
     */
    public void rifiutoButton() {
        System.out.println("DEBUG DetailsNotificationScreen: === INIZIO rifiutoButton ===");
        
        int result = JOptionPane.showConfirmDialog(this,
            "Sei sicuro di voler rifiutare questa richiesta?",
            "Conferma Rifiuto",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                if (notificationControl != null) {
                    System.out.println("DEBUG DetailsNotificationScreen: Chiamando notificationControl.rifiuto()");
                    notificationControl.rifiuto();
                    
                    JOptionPane.showMessageDialog(this,
                        "Richiesta rifiutata.",
                        "Rifiutata",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.err.println("DEBUG DetailsNotificationScreen: ERRORE - notificationControl è null");
                    JOptionPane.showMessageDialog(this,
                        "Errore: sistema non configurato correttamente.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                }
                
                // Torna alla schermata notifiche
                returnToNotifications();
                
            } catch (Exception ex) {
                System.err.println("DEBUG DetailsNotificationScreen: ERRORE in rifiutoButton: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Errore durante il rifiuto: " + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        System.out.println("DEBUG DetailsNotificationScreen: === FINE rifiutoButton ===");
    }
    
    /**
     * Torna alla schermata notifiche
     */
    private void returnToNotifications() {
        dispose();
        try {
            Class<?> notificationScreenClass = Class.forName("com.cms.users.notifications.Interface.NotificationScreen");
            Object notificationScreen = notificationScreenClass.getDeclaredConstructor().newInstance();
            java.lang.reflect.Method createMethod = notificationScreenClass.getMethod("create");
            createMethod.invoke(notificationScreen);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ritorno alle notifiche...");
        }
    }
    
    // Metodi di utilità
    
    /**
     * Imposta il titolo della notifica
     */
    public void setNotificationTitle(String title) {
        this.notificationTitle = title;
        typeLabel.setText(title);
    }
    
    /**
     * Imposta il contenuto della notifica
     */
    public void setNotificationContent(String content) {
        this.notificationContent = content;
        contentArea.setText(content);
    }
    
    /**
     * Imposta il tipo di notifica usando l'enum
     */
    public void setNotificationType(NotificationType type) {
        this.currentType = type;
        updateButtonsForType();
    }
    
    /**
     * Ottiene il tipo di notifica corrente
     */
    public NotificationType getNotificationType() {
        return currentType;
    }
    
    /**
     * Metodo main per test standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test tipo "Presa Visione"
            DetailsNotificationScreen screen1 = new DetailsNotificationScreen(NotificationType.PRESA_VISIONE);
            screen1.setNotificationTitle("Tipo notifica");
            screen1.setNotificationContent("Questa è una notifica che richiede solo la conferma di presa visione. " +
                    "Il contenuto può essere lungo e verrà visualizzato correttamente nell'area di testo.");
            screen1.setLocation(0, 0);
            screen1.setVisible(true);
            
            // Test tipo "Accetta/Rifiuta"
            DetailsNotificationScreen screen2 = new DetailsNotificationScreen(NotificationType.ACCETTA_RIFIUTA);
            screen2.setNotificationTitle("Tipo notifica");
            screen2.setNotificationContent("Questa è una notifica che richiede una scelta tra accettazione e rifiuto. " +
                    "Puoi scegliere una delle due opzioni disponibili nei bottoni sottostanti.");
            screen2.setLocation(400, 0);
            screen2.setVisible(true);
        });
    }
}
