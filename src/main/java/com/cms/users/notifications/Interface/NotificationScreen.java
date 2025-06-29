package com.cms.users.notifications.Interface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <<boundary>>
 * NotificationScreen - Schermata per la visualizzazione delle notifiche
 */
public class NotificationScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    private JButton mostraTutteButton;
    private JTable notificationTable;
    private DefaultTableModel tableModel;
    private JPanel centerPanel;
    private JLabel emptyLabel;
    
    // Dati
    private List<NotificationData> notifications;
    private boolean hasNotifications;
    private NotificationData selectedNotification;
    
    /**
     * Classe per rappresentare i dati di una notifica
     */
    public static class NotificationData {
        public String id;
        public String message;
        public String date;
        public boolean isRead;
        
        public NotificationData(String id, String message, String date, boolean isRead) {
            this.id = id;
            this.message = message;
            this.date = date;
            this.isRead = isRead;
        }
    }
    
    /**
     * Costruttore
     */
    public NotificationScreen() {
        this.notifications = new ArrayList<>();
        this.hasNotifications = true;
        this.selectedNotification = null;
        
        // Popola con dati di esempio
        populateExampleData();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateDisplay();
    }
    
    /**
     * Popola con dati di esempio
     */
    private void populateExampleData() {
        notifications.add(new NotificationData("1", "Nuova submission ricevuta per la conferenza AI 2025", "2025-06-28", false));
        notifications.add(new NotificationData("2", "Revisione assegnata: Articolo 'Machine Learning Trends'", "2025-06-27", true));
        notifications.add(new NotificationData("3", "Scadenza submission: 5 giorni rimanenti", "2025-06-26", false));
        notifications.add(new NotificationData("4", "Conferenza 'Data Science Summit' creata con successo", "2025-06-25", true));
        notifications.add(new NotificationData("5", "Nuovo revisore aggiunto alla conferenza", "2025-06-24", true));
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        setTitle("Notifiche Ricevute");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Bottoni header
        homeButton = createHeaderButton("🏠");
        notificheButton = createHeaderButton("🔔");
        profiloButton = createHeaderButton("👤");
        
        // Highlight del bottone notifiche (è attivo)
        notificheButton.setBackground(new Color(255, 140, 0));
        
        // Bottone "Mostra tutte le notifiche"
        mostraTutteButton = new JButton("Mostra tutte le notifiche");
        mostraTutteButton.setBackground(Color.ORANGE);
        mostraTutteButton.setForeground(Color.BLACK);
        mostraTutteButton.setFont(new Font("Arial", Font.BOLD, 14));
        mostraTutteButton.setFocusPainted(false);
        mostraTutteButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Label per stato vuoto
        emptyLabel = new JLabel("Nessuna nuova notifica trovata");
        emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emptyLabel.setVerticalAlignment(SwingConstants.CENTER);
        emptyLabel.setBackground(new Color(230, 230, 230));
        emptyLabel.setOpaque(true);
        emptyLabel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Pannello superiore con titolo e bottone
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        // Titolo
        JLabel titleLabel = new JLabel("Notifiche Ricevute:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Bottone "Mostra tutte le notifiche"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        buttonPanel.add(mostraTutteButton);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Pannello centrale (tabella o messaggio vuoto)
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    /**
     * Aggiorna la visualizzazione in base ai dati
     */
    private void updateDisplay() {
        centerPanel.removeAll();
        
        if (!hasNotifications || notifications.isEmpty()) {
            // Mostra messaggio vuoto
            centerPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            // Mostra tabella notifiche
            createNotificationTable();
            JScrollPane scrollPane = new JScrollPane(notificationTable);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            scrollPane.setPreferredSize(new Dimension(750, 400));
            centerPanel.add(scrollPane, BorderLayout.CENTER);
        }
        
        centerPanel.revalidate();
        centerPanel.repaint();
    }
    
    /**
     * Crea la tabella delle notifiche
     */
    private void createNotificationTable() {
        String[] columnNames = {"Notifica"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabella di sola visualizzazione
            }
        };
        
        // Popola la tabella
        for (NotificationData notification : notifications) {
            Object[] row = {notification.message};
            tableModel.addRow(row);
        }
        
        notificationTable = new JTable(tableModel);
        notificationTable.setFont(new Font("Arial", Font.PLAIN, 14));
        notificationTable.setRowHeight(50);
        notificationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Imposta larghezza colonne
        notificationTable.getColumnModel().getColumn(0).setPreferredWidth(750);
        
        // Aggiungi listener per selezione
        notificationTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = notificationTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < notifications.size()) {
                    selectedNotification = notifications.get(selectedRow);
                }
            }
        });
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
        
        // Bottone notifiche (già attivo)
        notificheButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Sei già nella schermata notifiche");
        });
        
        // Bottone profilo
        profiloButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Apertura profilo utente...");
        });
        
        // Bottone "Mostra tutte le notifiche"
        mostraTutteButton.addActionListener(e -> {
            MostraTutteLeNotificheButton();
        });
        
        // Double click sulla tabella per selezionare notifica
        if (notificationTable != null) {
            notificationTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        selezionaNotificaButton();
                    }
                }
            });
        }
    }
    
    // Metodi dell'interfaccia originale
    
    /**
     * Crea e mostra la schermata
     */
    public void create() {
        setVisible(true);
    }
    
    /**
     * Imposta la lista delle notifiche
     */
    public void setNotification(String listNotification) {
        // Per compatibilità con l'interfaccia originale
        // In una implementazione reale, questo dovrebbe parsare la stringa
        if (listNotification == null || listNotification.trim().isEmpty()) {
            hasNotifications = false;
            notifications.clear();
        } else {
            hasNotifications = true;
            // Qui si potrebbe implementare il parsing della stringa
        }
        updateDisplay();
    }
    
    /**
     * Gestisce il click sul bottone "Mostra tutte le notifiche"
     */
    public void MostraTutteLeNotificheButton() {
        if (!hasNotifications || notifications.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nessuna notifica da visualizzare.", 
                "Notifiche", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Visualizzazione di tutte le " + notifications.size() + " notifiche.", 
                "Notifiche", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Gestisce la selezione di una notifica
     */
    public void selezionaNotificaButton() {
        if (notificationTable == null || notificationTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, 
                "Seleziona una notifica dalla lista.", 
                "Selezione richiesta", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedRow = notificationTable.getSelectedRow();
        NotificationData notification = notifications.get(selectedRow);
        
        // Marca come letta
        notification.isRead = true;
        
        // Apri la schermata dettagli notifica
        dispose();
        try {
            Class<?> detailsScreenClass = Class.forName("com.cms.users.notifications.Interface.DetailsNotificationScreen");
            Object detailsScreen = detailsScreenClass.getDeclaredConstructor().newInstance();
            
            // Imposta il contenuto della notifica
            java.lang.reflect.Method setTitleMethod = detailsScreenClass.getMethod("setNotificationTitle", String.class);
            setTitleMethod.invoke(detailsScreen, "Tipo notifica");
            
            java.lang.reflect.Method setContentMethod = detailsScreenClass.getMethod("setNotificationContent", String.class);
            setContentMethod.invoke(detailsScreen, notification.message);
            
            // Determina il tipo di notifica in base al contenuto
            String type = determineNotificationType(notification.message);
            java.lang.reflect.Method setTypeMethod = detailsScreenClass.getMethod("setType", String.class);
            setTypeMethod.invoke(detailsScreen, type);
            
            java.lang.reflect.Method createMethod = detailsScreenClass.getMethod("create");
            createMethod.invoke(detailsScreen);
        } catch (Exception ex) {
            // Fallback: mostra dettagli in dialog
            String details = "Dettagli notifica:\n\n" + 
                            "ID: " + notification.id + "\n" +
                            "Messaggio: " + notification.message + "\n" +
                            "Data: " + notification.date;
            
            JOptionPane.showMessageDialog(this, details, "Dettagli Notifica", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Determina il tipo di notifica in base al contenuto
     */
    private String determineNotificationType(String message) {
        if (message.toLowerCase().contains("revisione") || 
            message.toLowerCase().contains("convoca") ||
            message.toLowerCase().contains("invito")) {
            return "accetta_rifiuta";
        } else {
            return "presa_visione";
        }
    }
    
    /**
     * Ottiene la notifica selezionata
     */
    public String getNotificaSelezionata() {
        if (selectedNotification != null) {
            return selectedNotification.message;
        }
        return null;
    }
    
    // Metodi di utilità
    
    /**
     * Imposta se ci sono notifiche o meno
     */
    public void setHasNotifications(boolean hasNotifications) {
        this.hasNotifications = hasNotifications;
        updateDisplay();
    }
    
    /**
     * Aggiunge una notifica
     */
    public void addNotification(String id, String message, String date, boolean isRead) {
        notifications.add(new NotificationData(id, message, date, isRead));
        hasNotifications = true;
        updateDisplay();
    }
    
    /**
     * Pulisce tutte le notifiche
     */
    public void clearNotifications() {
        notifications.clear();
        hasNotifications = false;
        updateDisplay();
    }
    
    /**
     * Metodo main per test standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test con notifiche
            NotificationScreen screen1 = new NotificationScreen();
            screen1.setLocation(0, 0);
            screen1.setVisible(true);
            
            // Test senza notifiche (empty state)
            NotificationScreen screen2 = new NotificationScreen();
            screen2.setHasNotifications(false);
            screen2.setLocation(450, 0);
            screen2.setVisible(true);
        });
    }
}
