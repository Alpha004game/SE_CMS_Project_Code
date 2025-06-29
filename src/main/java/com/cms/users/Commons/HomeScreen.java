package com.cms.users.Commons;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * <<boundary>>
 * HomeScreen - Interfaccia grafica principale per la gestione delle conferenze
 */
public class HomeScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private HeaderScreen headerScreen;
    private JButton creaNuovaConferenzaButton;
    private JButton mostraTutteConferenzeButton;
    private JTable conferenzeTable;
    private DefaultTableModel tableModel;
    
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
        
        // Inizializza HeaderScreen
        this.headerScreen = new HeaderScreen();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadSampleData();
    }
    
    /**
     * Costruttore con dati utente
     */
    public HomeScreen(String userName, String userRole) {
        this.availableFeatures = new ArrayList<>();
        this.conferenze = new ArrayList<>();
        this.isUserLoggedIn = true;
        
        // Inizializza HeaderScreen con dati utente
        this.headerScreen = new HeaderScreen(userName, userRole);
        
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
        conferenzeTable.setRowHeight(95);
        conferenzeTable.setFont(new Font("Arial", Font.PLAIN, 12));
        conferenzeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        conferenzeTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
        conferenzeTable.setGridColor(new Color(230, 230, 230));
        conferenzeTable.setShowVerticalLines(true);
        conferenzeTable.setShowHorizontalLines(true);
        
        // Renderer personalizzato per i bottoni nelle celle
        conferenzeTable.getColumn("Azioni").setCellRenderer(new ButtonRenderer());
        conferenzeTable.getColumn("Azioni").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        // Migliora la larghezza delle colonne
        conferenzeTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Conferenza
        conferenzeTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Ruolo  
        conferenzeTable.getColumnModel().getColumn(2).setPreferredWidth(300); // Azioni
    }
    
    /**
     * Configura il layout dell'interfaccia
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Usa HeaderScreen come header
        add(headerScreen, BorderLayout.NORTH);
        
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
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Le tue conferenze",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 14),
                new Color(80, 80, 80)
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
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
        // Gestori per i bottoni specifici di HomeScreen
        creaNuovaConferenzaButton.addActionListener(e -> handleCreaNuovaConferenzaAction());
        mostraTutteConferenzeButton.addActionListener(e -> handleMostraTutteConferenzeAction());
        
        // Imposta listener personalizzati per HeaderScreen se necessario
        headerScreen.setHomeActionListener(e -> handleHomeAction());
        // Le notifiche e il profilo sono gestiti automaticamente da HeaderScreen
    }
    
    /**
     * Carica dati di esempio per le conferenze
     */
    private void loadSampleData() {
        // Chair: "Revisore", "Chair", "Selezione specifiche competenze", "Modifica Preferenze Articolo"
        conferenze.add(new ConferenzaData("Conferenza AI 2025", "Chair", 
            new String[]{"Revisore", "Chair", "Selezione specifiche competenze", "Modifica Preferenze Articolo"}));
        
        // Editore: "Editore"
        conferenze.add(new ConferenzaData("Conferenza Software Engineering", "Editore", 
            new String[]{"Editore"}));
        
        // Revisore: "Revisore", "Selezione Specifiche Competenze", "Modifica preferenze articolo"
        conferenze.add(new ConferenzaData("Conferenza Machine Learning", "Revisore", 
            new String[]{"Revisore", "Selezione specifiche competenze", "Modifica preferenze articolo"}));
        
        // Sotto-revisore: "Sotto-revisore"
        conferenze.add(new ConferenzaData("Conferenza Data Science", "Sotto-revisore", 
            new String[]{"Sotto-revisore"}));
        
        // Autore: "Autore"
        conferenze.add(new ConferenzaData("Conferenza Cybersecurity", "Autore", 
            new String[]{"Autore"}));
        
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
    
    // Gestori degli eventi specifici di HomeScreen
    private void handleHomeAction() {
        JOptionPane.showMessageDialog(this, "Sei già nella Home Screen");
    }
    
    private void handleCreaNuovaConferenzaAction() {
        JOptionPane.showMessageDialog(this, "Apertura schermata creazione nuova conferenza...");
    }
    
    private void handleMostraTutteConferenzeAction() {
        JOptionPane.showMessageDialog(this, "Mostra tutte le conferenze disponibili...");
    }
    
    /**
     * Gestisce le azioni dei bottoni nella tabella delle conferenze
     */
    private void handleButtonAction(String action, int row) {
        ConferenzaData conferenza = conferenze.get(row);
        String message = "Azione: " + action + "\nConferenza: " + conferenza.nome + "\nRuolo: " + conferenza.ruolo;
        
        switch (action.toLowerCase()) {
            case "chair":
                JOptionPane.showMessageDialog(this, message + "\n\nApertura funzionalità Chair...");
                break;
            case "revisore":
                JOptionPane.showMessageDialog(this, message + "\n\nApertura funzionalità Revisore...");
                break;
            case "editore":
                JOptionPane.showMessageDialog(this, message + "\n\nApertura funzionalità Editore...");
                break;
            case "sotto-revisore":
                JOptionPane.showMessageDialog(this, message + "\n\nApertura funzionalità Sotto-revisore...");
                break;
            case "autore":
                JOptionPane.showMessageDialog(this, message + "\n\nApertura funzionalità Autore...");
                break;
            case "selezione specifiche competenze":
                JOptionPane.showMessageDialog(this, message + "\n\nApertura selezione competenze...");
                break;
            case "modifica preferenze articolo":
                JOptionPane.showMessageDialog(this, message + "\n\nApertura modifica preferenze...");
                break;
            default:
                JOptionPane.showMessageDialog(this, message);
                break;
        }
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
    
    /**
     * Aggiunge una conferenza con generazione automatica dei bottoni in base al ruolo
     */
    public void addConferenza(String nome, String ruolo) {
        String[] azioni = generateActionsForRole(ruolo);
        conferenze.add(new ConferenzaData(nome, ruolo, azioni));
        refreshTable();
    }
    
    /**
     * Genera i bottoni appropriati in base al ruolo dell'utente
     */
    private String[] generateActionsForRole(String ruolo) {
        switch (ruolo.toLowerCase()) {
            case "chair":
                return new String[]{"Revisore", "Chair", "Selezione specifiche competenze", "Modifica Preferenze Articolo"};
            case "editore":
                return new String[]{"Editore"};
            case "revisore":
                return new String[]{"Revisore", "Selezione specifiche competenze", "Modifica preferenze articolo"};
            case "sotto-revisore":
                return new String[]{"Sotto-revisore"};
            case "autore":
                return new String[]{"Autore"};
            default:
                return new String[]{}; // Nessun bottone per ruoli non riconosciuti
        }
    }
    
    /**
     * Aggiorna il ruolo di una conferenza esistente e rigenera i bottoni
     */
    public void updateConferenceRole(int index, String newRole) {
        if (index >= 0 && index < conferenze.size()) {
            ConferenzaData conf = conferenze.get(index);
            conf.ruolo = newRole;
            conf.azioni = generateActionsForRole(newRole);
            refreshTable();
        }
    }
    
    /**
     * Ottiene i ruoli disponibili nel sistema
     */
    public String[] getAvailableRoles() {
        return new String[]{"Chair", "Editore", "Revisore", "Sotto-revisore", "Autore"};
    }
    
    /**
     * Imposta i dati dell'utente per l'header
     */
    public void setUserData(String username, String email) {
        if (headerScreen != null) {
            // Aggiorna i dati utente nell'HeaderScreen
            // HeaderScreen gestirà automaticamente l'aggiornamento del UserMenu
            System.out.println("Setting user data: " + username + ", " + email);
        }
    }
    
    /**
     * Ottiene il riferimento all'HeaderScreen
     */
    public HeaderScreen getHeaderScreen() {
        return headerScreen;
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
                
                // Usa GridBagLayout per una disposizione più controllata
                setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(3, 3, 3, 3);
                gbc.anchor = GridBagConstraints.CENTER;
                
                int buttonsPerRow = 2; // Massimo 2 bottoni per riga
                int buttonCount = 0;
                
                for (String action : actions) {
                    if (!action.isEmpty()) {
                        JButton button = HomeScreen.this.createStyledButton(action);
                        
                        gbc.gridx = buttonCount % buttonsPerRow;
                        gbc.gridy = buttonCount / buttonsPerRow;
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.weightx = 1.0;
                        add(button, gbc);
                        buttonCount++;
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
                
                // Usa GridBagLayout per una disposizione più controllata
                panel.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(3, 3, 3, 3);
                gbc.anchor = GridBagConstraints.CENTER;
                
                int buttonsPerRow = 2; // Massimo 2 bottoni per riga
                int buttonCount = 0;
                
                for (String action : currentActions) {
                    if (!action.isEmpty()) {
                        JButton button = HomeScreen.this.createStyledButton(action);
                        
                        button.addActionListener(e -> {
                            HomeScreen.this.handleButtonAction(action, row);
                            fireEditingStopped();
                        });
                        
                        gbc.gridx = buttonCount % buttonsPerRow;
                        gbc.gridy = buttonCount / buttonsPerRow;
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.weightx = 1.0;
                        panel.add(button, gbc);
                        buttonCount++;
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
     * Crea un bottone con stile moderno e accattivante
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Colore di sfondo con sfumatura
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 165, 0), // Arancione chiaro
                    0, getHeight(), new Color(255, 140, 0) // Arancione scuro
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Bordo sottile
                g2d.setColor(new Color(200, 120, 0));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                
                g2d.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                // Non disegnare il bordo di default
            }
        };
        
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 9));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(145, 28));
        button.setMinimumSize(new Dimension(145, 28));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effetto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 180, 50));
                button.repaint();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.ORANGE);
                button.repaint();
            }
        });
        
        return button;
    }

    /**
     * Metodo main per testing standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test con dati utente
            HomeScreen homeScreen = new HomeScreen("Mario Rossi", "Chair");
            homeScreen.setWelcomeMessage("Benvenuto nel sistema CMS!");
            homeScreen.addAvailableFeature("Gestione Conferenze");
            homeScreen.addAvailableFeature("Revisione Articoli");
            homeScreen.addAvailableFeature("Pubblicazione");
            
            // Test aggiunta conferenza con generazione automatica bottoni
            homeScreen.addConferenza("Conferenza Test Automatico", "Chair");
            
            homeScreen.displayUserDashboard();
        });
    }
}
