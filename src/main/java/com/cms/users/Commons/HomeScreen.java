package com.cms.users.Commons;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import com.cms.users.account.Interface.UserMenu;

/**
 * <<boundary>>
 * HomeScreen - Interfaccia grafica principale per la gestione delle conferenze
 */
public class HomeScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    private JButton creaNuovaConferenzaButton;
    private JButton mostraTutteConferenzeButton;
    private JTable conferenzeTable;
    private DefaultTableModel tableModel;
    private UserMenu userMenu; // Menu utente
    
    // Attributi originali
    private String welcomeMessage;
    private List<String> availableFeatures;
    private boolean isUserLoggedIn;
    
    // Dati di esempio per le conferenze
    private List<ConferenzaData> conferenze;
    
    // Costruttore
    public HomeScreen() {
        this.availableFeatures = new ArrayList<>();
        this.conferenze = new ArrayList<>();
        this.isUserLoggedIn = true;
        
        // Inizializza il menu utente
        this.userMenu = new UserMenu("UtenteDemo", "utente@cms.com");
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadSampleData();
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Home Screen");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Bottoni header
        homeButton = new JButton("🏠");
        homeButton.setBackground(Color.ORANGE);
        homeButton.setForeground(Color.WHITE);
        homeButton.setFont(new Font("Arial", Font.BOLD, 16));
        homeButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        homeButton.setFocusPainted(false);
        
        notificheButton = new JButton("🔔");
        notificheButton.setBackground(Color.ORANGE);
        notificheButton.setForeground(Color.WHITE);
        notificheButton.setFont(new Font("Arial", Font.BOLD, 16));
        notificheButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        notificheButton.setFocusPainted(false);
        // Aggiunta del pallino rosso per le notifiche
        notificheButton.setText("🔔•");
        
        profiloButton = new JButton("👤");
        profiloButton.setBackground(Color.ORANGE);
        profiloButton.setForeground(Color.WHITE);
        profiloButton.setFont(new Font("Arial", Font.BOLD, 16));
        profiloButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        profiloButton.setFocusPainted(false);
        
        // Bottone crea nuova conferenza
        creaNuovaConferenzaButton = new JButton("Crea nuova conferenza");
        creaNuovaConferenzaButton.setBackground(Color.ORANGE);
        creaNuovaConferenzaButton.setForeground(Color.WHITE);
        creaNuovaConferenzaButton.setFont(new Font("Arial", Font.BOLD, 12));
        creaNuovaConferenzaButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        creaNuovaConferenzaButton.setFocusPainted(false);
        
        // Bottone mostra tutte le conferenze
        mostraTutteConferenzeButton = new JButton("Mostra tutte le conferenze");
        mostraTutteConferenzeButton.setBackground(Color.ORANGE);
        mostraTutteConferenzeButton.setForeground(Color.WHITE);
        mostraTutteConferenzeButton.setFont(new Font("Arial", Font.BOLD, 14));
        mostraTutteConferenzeButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        mostraTutteConferenzeButton.setFocusPainted(false);
        
        // Tabella conferenze
        String[] columnNames = {"Conferenza", "Ruolo", "Azioni"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Solo la colonna "Azioni" è editabile
            }
        };
        
        conferenzeTable = new JTable(tableModel);
        conferenzeTable.setRowHeight(50);
        conferenzeTable.setFont(new Font("Arial", Font.PLAIN, 12));
        conferenzeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        conferenzeTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
        
        // Renderer personalizzato per i bottoni nelle celle
        conferenzeTable.getColumn("Azioni").setCellRenderer(new ButtonRenderer());
        conferenzeTable.getColumn("Azioni").setCellEditor(new ButtonEditor(new JCheckBox()));
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
        
        // Bottoni header a sinistra (solo home in questo caso)
        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftHeaderPanel.setBackground(Color.ORANGE);
        leftHeaderPanel.add(homeButton);
        
        // Bottoni header a destra
        JPanel rightHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightHeaderPanel.setBackground(Color.ORANGE);
        rightHeaderPanel.add(notificheButton);
        rightHeaderPanel.add(profiloButton);
        
        headerPanel.add(leftHeaderPanel, BorderLayout.WEST);
        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        // Pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Pannello titolo e bottone crea conferenza
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Home Screen");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(creaNuovaConferenzaButton);
        
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Pannello tabella
        JScrollPane scrollPane = new JScrollPane(conferenzeTable);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Le tue conferenze"));
        
        // Pannello bottone in basso
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(mostraTutteConferenzeButton);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Configura i gestori degli eventi
     */
    private void setupEventHandlers() {
        homeButton.addActionListener(e -> handleHomeAction());
        notificheButton.addActionListener(e -> handleNotificheAction());
        profiloButton.addActionListener(e -> handleProfiloAction());
        creaNuovaConferenzaButton.addActionListener(e -> handleCreaNuovaConferenzaAction());
        mostraTutteConferenzeButton.addActionListener(e -> handleMostraTutteConferenzeAction());
    }
    
    /**
     * Carica dati di esempio per le conferenze
     */
    private void loadSampleData() {
        conferenze.add(new ConferenzaData("Conferenza AI 2025", "Revisore", 
            new String[]{"Chair", "Seleziona Specifiche competenze", "Modifica preferenze articolo"}));
        conferenze.add(new ConferenzaData("Conferenza Software Engineering", "Editore", new String[]{}));
        conferenze.add(new ConferenzaData("Conferenza Machine Learning", "Revisore", 
            new String[]{"Seleziona Specifiche competenze", "Modifica preferenze articolo"}));
        conferenze.add(new ConferenzaData("Conferenza Data Science", "Sotto-revisore", new String[]{}));
        conferenze.add(new ConferenzaData("Conferenza Cybersecurity", "Autore", new String[]{}));
        
        refreshTable();
    }
    
    /**
     * Aggiorna la tabella con i dati delle conferenze
     */
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (ConferenzaData conf : conferenze) {
            Object[] row = {conf.nome, conf.ruolo, conf.azioni};
            tableModel.addRow(row);
        }
    }
    
    // Gestori degli eventi
    private void handleHomeAction() {
        JOptionPane.showMessageDialog(this, "Sei già nella Home Screen");
    }
    
    private void handleNotificheAction() {
        // Mostra il menu utente sotto il bottone notifiche
        userMenu.showMenuBelowButton(notificheButton);
    }
    
    private void handleProfiloAction() {
        JOptionPane.showMessageDialog(this, "Apertura profilo utente...");
    }
    
    private void handleCreaNuovaConferenzaAction() {
        JOptionPane.showMessageDialog(this, "Apertura schermata creazione nuova conferenza...");
    }
    
    private void handleMostraTutteConferenzeAction() {
        JOptionPane.showMessageDialog(this, "Mostra tutte le conferenze disponibili...");
    }
    
    // Metodi originali implementati
    public void displayWelcomeMessage() {
        if (welcomeMessage != null) {
            JOptionPane.showMessageDialog(this, welcomeMessage, "Benvenuto", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void showAvailableFeatures() {
        if (availableFeatures != null && !availableFeatures.isEmpty()) {
            String features = String.join("\n", availableFeatures);
            JOptionPane.showMessageDialog(this, "Funzionalità disponibili:\n" + features);
        }
    }
    
    public void navigateToLogin() {
        JOptionPane.showMessageDialog(this, "Navigazione al login...");
        // Qui andrà l'apertura della LoginScreen
    }
    
    public void navigateToRegistration() {
        JOptionPane.showMessageDialog(this, "Navigazione alla registrazione...");
        // Qui andrà l'apertura della RegistrationScreen
    }
    
    public void displayUserDashboard() {
        setVisible(true);
    }
    
    public boolean checkUserAuthentication() {
        return isUserLoggedIn;
    }
    
    public void refreshScreen() {
        refreshTable();
        repaint();
    }
    
    // Metodi di utilità aggiuntivi
    public void setWelcomeMessage(String message) {
        this.welcomeMessage = message;
    }
    
    public void addAvailableFeature(String feature) {
        this.availableFeatures.add(feature);
    }
    
    public void setUserLoggedIn(boolean loggedIn) {
        this.isUserLoggedIn = loggedIn;
    }
    
    public void addConferenza(String nome, String ruolo, String[] azioni) {
        conferenze.add(new ConferenzaData(nome, ruolo, azioni));
        refreshTable();
    }
    
    public void show() {
        setVisible(true);
    }
    
    /**
     * Imposta i dati dell'utente per il menu
     */
    public void setUserData(String username, String email) {
        if (userMenu != null) {
            userMenu.setUserData(username, email);
        }
    }
    
    /**
     * Ottiene il menu utente
     */
    public UserMenu getUserMenu() {
        return userMenu;
    }

    // Classe interna per i dati delle conferenze
    private static class ConferenzaData {
        String nome;
        String ruolo;
        String[] azioni;
        
        ConferenzaData(String nome, String ruolo, String[] azioni) {
            this.nome = nome;
            this.ruolo = ruolo;
            this.azioni = azioni;
        }
    }
    
    // Renderer per i bottoni nella tabella
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            if (value instanceof String[]) {
                String[] actions = (String[]) value;
                removeAll();
                setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
                
                for (String action : actions) {
                    if (!action.isEmpty()) {
                        JButton button = new JButton(action);
                        button.setBackground(Color.ORANGE);
                        button.setForeground(Color.WHITE);
                        button.setFont(new Font("Arial", Font.PLAIN, 10));
                        button.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
                        button.setFocusPainted(false);
                        add(button);
                    }
                }
            }
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            
            return this;
        }
    }
    
    // Editor per i bottoni nella tabella
    class ButtonEditor extends DefaultCellEditor {
        protected JPanel panel;
        private String[] currentActions;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel();
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            
            if (value instanceof String[]) {
                currentActions = (String[]) value;
                panel.removeAll();
                panel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
                
                for (String action : currentActions) {
                    if (!action.isEmpty()) {
                        JButton button = new JButton(action);
                        button.setBackground(Color.ORANGE);
                        button.setForeground(Color.WHITE);
                        button.setFont(new Font("Arial", Font.PLAIN, 10));
                        button.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
                        button.setFocusPainted(false);
                        
                        button.addActionListener(e -> {
                            JOptionPane.showMessageDialog(panel, 
                                "Azione: " + action + " per la conferenza alla riga " + (row + 1));
                            fireEditingStopped();
                        });
                        
                        panel.add(button);
                    }
                }
            }
            
            return panel;
        }
        
        public Object getCellEditorValue() {
            return currentActions;
        }
    }
    
    /**
     * Metodo main per testing standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomeScreen homeScreen = new HomeScreen();
            homeScreen.setWelcomeMessage("Benvenuto nel sistema CMS!");
            homeScreen.addAvailableFeature("Gestione Conferenze");
            homeScreen.addAvailableFeature("Revisione Articoli");
            homeScreen.addAvailableFeature("Pubblicazione");
            
            // Imposta dati utente personalizzati
            homeScreen.setUserData("Mario Rossi", "mario.rossi@universita.it");
            
            homeScreen.displayUserDashboard();
        });
    }
}
