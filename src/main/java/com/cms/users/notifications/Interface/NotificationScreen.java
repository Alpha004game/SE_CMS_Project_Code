package com.cms.users.notifications.Interface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import com.cms.users.Entity.NotificaE;
import com.cms.users.notifications.Control.NotificationControl;

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
    private NotificationControl notificationControl;
    
    /**
     * Classe per rappresentare i dati di una notifica
     */
    public static class NotificationData {
        public int id;
        public String message;
        public boolean isRead;
        
        public NotificationData(int id, String message, boolean isRead) {
            this.id = id;
            this.message = message;
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
        this.notificationControl = new NotificationControl();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateDisplay();
    }
    
    /**
     * Costruttore con control esterno
     */
    public NotificationScreen(NotificationControl control) {
        this.notifications = new ArrayList<>();
        this.hasNotifications = true;
        this.selectedNotification = null;
        this.notificationControl = control;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateDisplay();
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
        System.out.println("DEBUG NotificationScreen: === INIZIO updateDisplay ===");
        System.out.println("DEBUG NotificationScreen: hasNotifications = " + hasNotifications);
        System.out.println("DEBUG NotificationScreen: notifications.isEmpty() = " + notifications.isEmpty());
        System.out.println("DEBUG NotificationScreen: notifications.size() = " + notifications.size());
        
        centerPanel.removeAll();
        
        if (!hasNotifications || notifications.isEmpty()) {
            System.out.println("DEBUG NotificationScreen: Mostrando messaggio vuoto - nessuna tabella creata");
            // Mostra messaggio vuoto
            centerPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            System.out.println("DEBUG NotificationScreen: Creando tabella notifiche - chiamando createNotificationTable()");
            // Mostra tabella notifiche
            createNotificationTable();
            JScrollPane scrollPane = new JScrollPane(notificationTable);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            scrollPane.setPreferredSize(new Dimension(750, 400));
            centerPanel.add(scrollPane, BorderLayout.CENTER);
            System.out.println("DEBUG NotificationScreen: Tabella aggiunta al centerPanel");
        }
        
        centerPanel.revalidate();
        centerPanel.repaint();
        System.out.println("DEBUG NotificationScreen: === FINE updateDisplay ===");
    }
    
    /**
     * Crea la tabella delle notifiche
     */
    private void createNotificationTable() {
        System.out.println("DEBUG NotificationScreen: === INIZIO createNotificationTable ===");
        System.out.println("DEBUG NotificationScreen: Creando tabella con " + notifications.size() + " notifiche");
        
        String[] columnNames = {"Notifica"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabella di sola visualizzazione
            }
        };
        
        // Popola la tabella
        for (int i = 0; i < notifications.size(); i++) {
            NotificationData notification = notifications.get(i);
            Object[] row = {notification.message};
            tableModel.addRow(row);
            System.out.println("DEBUG NotificationScreen: Aggiunta riga " + i + " - ID:" + notification.id);
        }
        
        notificationTable = new JTable(tableModel);
        notificationTable.setFont(new Font("Arial", Font.PLAIN, 14));
        notificationTable.setRowHeight(50);
        notificationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Imposta larghezza colonne
        notificationTable.getColumnModel().getColumn(0).setPreferredWidth(750);
        
        System.out.println("DEBUG NotificationScreen: Tabella creata - configurando event listeners...");
        
        // Aggiungi listener per selezione
        notificationTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = notificationTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < notifications.size()) {
                    selectedNotification = notifications.get(selectedRow);
                    System.out.println("DEBUG NotificationScreen: Notifica selezionata: ID=" + selectedNotification.id);
                }
            }
        });
        
        // Aggiungi listener per doppio click
        notificationTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("DEBUG NotificationScreen: Mouse click rilevato - clickCount=" + e.getClickCount());
                if (e.getClickCount() == 2) {
                    System.out.println("DEBUG NotificationScreen: Doppio click rilevato - chiamando selezionaNotificaButton()");
                    selezionaNotificaButton();
                }
            }
        });
        
        System.out.println("DEBUG NotificationScreen: Event listeners configurati per la tabella");
        System.out.println("DEBUG NotificationScreen: === FINE createNotificationTable ===");
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
        // NOTA: La tabella viene creata dinamicamente, quindi questo listener verrà configurato in createNotificationTable()
        System.out.println("DEBUG NotificationScreen: setupEventHandlers - notificationTable è " + (notificationTable != null ? "non null" : "null"));
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
    
    
    /**
     * Imposta la lista delle notifiche da NotificaE entities
     */
    public void setNotificationList(LinkedList<NotificaE> notificheList) {
        System.out.println("DEBUG NotificationScreen: === INIZIO setNotificationList ===");
        System.out.println("DEBUG NotificationScreen: notificheList ricevuto: " + (notificheList != null ? "lista con " + notificheList.size() + " elementi" : "null"));
        
        notifications.clear();
        
        if (notificheList != null && !notificheList.isEmpty()) {
            System.out.println("DEBUG NotificationScreen: Lista NON vuota, conversione in corso");
            hasNotifications = true;
            
            // Converti NotificaE in NotificationData
            for (int i = 0; i < notificheList.size(); i++) {
                NotificaE notifica = notificheList.get(i);
                System.out.println("DEBUG NotificationScreen: Convertendo notifica " + i + ":");
                System.out.println("  - ID: " + notifica.getId());
                System.out.println("  - Testo: '" + notifica.getText() + "'");
                System.out.println("  - Status: '" + notifica.getStatus() + "'");
                System.out.println("  - isRead: " + (notifica.getStatus() != null));
                
                notifications.add(new NotificationData(notifica.getId(), notifica.getText(), notifica.getStatus()!=null));
            }
            System.out.println("DEBUG NotificationScreen: Conversione completata. notifications.size() = " + notifications.size());
        } else {
            System.out.println("DEBUG NotificationScreen: Lista vuota o null");
            hasNotifications = false;
        }
        
        System.out.println("DEBUG NotificationScreen: hasNotifications = " + hasNotifications);
        System.out.println("DEBUG NotificationScreen: Chiamando updateDisplay()");
        updateDisplay();
        System.out.println("DEBUG NotificationScreen: === FINE setNotificationList ===");
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
     * Gestisce la selezione di una notifica seguendo il sequence diagram
     */
    public void selezionaNotificaButton() {
        System.out.println("DEBUG NotificationScreen: === INIZIO selezionaNotificaButton ===");
        System.out.println("DEBUG NotificationScreen: notificationTable = " + (notificationTable != null ? "non null" : "null"));
        
        if (notificationTable == null) {
            System.err.println("DEBUG NotificationScreen: ERRORE - notificationTable è null!");
            JOptionPane.showMessageDialog(this, 
                "Errore: tabella notifiche non inizializzata.", 
                "Errore interno", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int selectedRow = notificationTable.getSelectedRow();
        System.out.println("DEBUG NotificationScreen: selectedRow = " + selectedRow);
        System.out.println("DEBUG NotificationScreen: notifications.size() = " + notifications.size());
        
        if (selectedRow == -1) {
            System.out.println("DEBUG NotificationScreen: Nessuna riga selezionata - auto-selezione della prima riga se disponibile");
            if (notifications.size() > 0) {
                notificationTable.setRowSelectionInterval(0, 0);
                selectedRow = 0;
                System.out.println("DEBUG NotificationScreen: Auto-selezionata riga 0");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Nessuna notifica disponibile per la selezione.", 
                    "Selezione richiesta", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        if (selectedRow >= notifications.size()) {
            System.err.println("DEBUG NotificationScreen: ERRORE - selectedRow (" + selectedRow + ") >= notifications.size (" + notifications.size() + ")");
            return;
        }
        
        NotificationData notification = notifications.get(selectedRow);
        System.out.println("DEBUG NotificationScreen: Notifica selezionata - ID: " + notification.id + ", Message: '" + notification.message + "'");
        
        try {
            // 1. Chiama selezionaNotifica(idNotifica) sul control
            System.out.println("DEBUG NotificationScreen: Chiamando notificationControl.selezionaNotifica(" + notification.id + ")");
            notificationControl.selezionaNotifica(notification.id);
            
            // 2. Ottiene la notifica selezionata dal control
            NotificaE notificaCompleta = notificationControl.getNotificaSelezionata();
            
            if (notificaCompleta == null) {
                System.err.println("DEBUG NotificationScreen: ERRORE - Notifica non recuperata dal control");
                JOptionPane.showMessageDialog(this, 
                    "Errore nel recupero dei dettagli della notifica.", 
                    "Errore", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("DEBUG NotificationScreen: Notifica recuperata con successo:");
            System.out.println("  - ID: " + notificaCompleta.getId());
            System.out.println("  - Tipo: " + notificaCompleta.getTipo());
            
            // Determina se richiede accettazione utilizzando il control
            boolean richiedeAccettazione = notificationControl.richiedeAccettazione();
            System.out.println("  - Richiede accettazione: " + richiedeAccettazione);
            
            // 3. Determina il tipo di schermata da aprire
            DetailsNotificationScreen.NotificationType tipo;
            if (richiedeAccettazione) {
                tipo = DetailsNotificationScreen.NotificationType.ACCETTA_RIFIUTA;
                System.out.println("DEBUG NotificationScreen: Tipo determinato: ACCETTA_RIFIUTA");
            } else {
                tipo = DetailsNotificationScreen.NotificationType.PRESA_VISIONE;
                System.out.println("DEBUG NotificationScreen: Tipo determinato: PRESA_VISIONE");
            }
            
            // 4. Apri DetailsNotificationScreen con i dati corretti e il control
            System.out.println("DEBUG NotificationScreen: Aprendo DetailsNotificationScreen con control");
            dispose();
            
            DetailsNotificationScreen detailsScreen = new DetailsNotificationScreen(tipo, notificationControl);
            
            // Converte il tipo numerico in stringa per il titolo
            String titoloTipo;
            switch (notificaCompleta.getTipo()) {
                case 1:
                    titoloTipo = "Richiesta di Accettazione";
                    break;
                case 2:
                    titoloTipo = "Notifica Informativa";
                    break;
                default:
                    titoloTipo = "Notifica";
                    break;
            }
            
            detailsScreen.setNotificationTitle(titoloTipo);
            detailsScreen.setNotificationContent(notificaCompleta.getText());
            detailsScreen.create();
            
            System.out.println("DEBUG NotificationScreen: DetailsNotificationScreen aperto con successo");
            
        } catch (Exception ex) {
            System.err.println("DEBUG NotificationScreen: ERRORE durante l'apertura dei dettagli: " + ex.getMessage());
            ex.printStackTrace();
            
            // Fallback: mostra dettagli in dialog
            String details = "Dettagli notifica:\n\n" + 
                            "ID: " + notification.id + "\n" +
                            "Messaggio: " + notification.message + "\n";
            
            JOptionPane.showMessageDialog(this, details, "Dettagli Notifica", JOptionPane.INFORMATION_MESSAGE);
        }
        
        System.out.println("DEBUG NotificationScreen: === FINE selezionaNotificaButton ===");
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
}
