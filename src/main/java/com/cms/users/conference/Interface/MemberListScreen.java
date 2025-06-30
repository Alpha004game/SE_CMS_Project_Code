package com.cms.users.conference.Interface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

import com.cms.App;
import com.cms.users.Entity.UtenteE;
import com.cms.users.conference.Control.ConferenceControl;

/**
 * <<boundary>>
 * MemberListScreen - Schermata di selezione membri che cambia in base al ruolo e all'azione
 */
public class MemberListScreen extends JFrame {
    
    // Enumerazioni per ruoli e azioni
    public enum UserRole {
        CHAIR, REVISORE
    }
    
    public enum Action {
        ADD_REVIEWER,       // Chair - Aggiungi revisore
        REMOVE_REVIEWER,    // Chair - Rimuovi revisore
        SUMMON_SUB_REVIEWER, // Revisore - Convoca sotto revisore
        ADD_COCHAIR         // Chair - Aggiungi co-chair
    }
    
    // Classe per dati utente generico
    public static class UserData {
        public String id;
        public String name;
        public String type; // "Utente" o "Revisore"
        
        public UserData(String id, String name, String type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }
    }
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    
    // Componenti del contenuto
    private JPanel centerPanel;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JLabel emptyLabel;
    private JButton actionButton;
    
    // Pannello statistiche per Chair
    private JPanel statsPanel;
    private JLabel totalReviewersLabel;
    private JLabel neededReviewersLabel;
    private JLabel toSummonReviewersLabel;
    
    // Dati della schermata
    private UserRole userRole;
    private Action action;
    private String screenTitle;
    private List<UserData> userData;
    private boolean hasData;
    
    // Statistiche per Chair
    private int totalReviewers = 15;
    private int neededReviewers = 20;
    private int toSummonReviewers = 5;
    private ConferenceControl conferenceControl;
    /**
     * Costruttore di default
     */
    public MemberListScreen(ConferenceControl control) {
        this(UserRole.REVISORE, Action.SUMMON_SUB_REVIEWER, control);
    }
    
    /**
     * Costruttore con ruolo e azione
     */
    public MemberListScreen(UserRole userRole, Action action, ConferenceControl control) {
        this.userRole = userRole != null ? userRole : UserRole.REVISORE;
        this.action = action != null ? action : Action.SUMMON_SUB_REVIEWER;
        this.conferenceControl = control;
        
        setupScreenTitle();
        initializeData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateDisplay();
    }
    
    /**
     * Imposta il titolo in base al ruolo e all'azione
     */
    private void setupScreenTitle() {
        switch (action) {
            case ADD_REVIEWER:
                screenTitle = "Seleziona utente";
                break;
            case REMOVE_REVIEWER:
                screenTitle = "Seleziona revisore";
                break;
            case SUMMON_SUB_REVIEWER:
                screenTitle = "Seleziona utente";
                break;
            case ADD_COCHAIR:
                screenTitle = "Seleziona Co-Chair";
                break;
            default:
                screenTitle = "Seleziona utente";
                break;
        }
    }
    
    /**
     * Inizializza i dati di esempio o reali dal database
     */
    private void initializeData() {
        userData = new ArrayList<>();
        
        if (action == Action.REMOVE_REVIEWER) {
            // Carica i revisori reali dal database
            loadReviewersFromDatabase();
        } else {
            // Simula se ci sono dati o meno (70% probabilità di avere dati)
            hasData = Math.random() > 0.3;
            
            if (hasData) {
                switch (action) {
                    case ADD_REVIEWER:
                    case SUMMON_SUB_REVIEWER:
                    case ADD_COCHAIR:
                        // Lista di utenti generici
                        userData.add(new UserData("U001", "Utente", "Utente"));
                        userData.add(new UserData("U002", "Utente", "Utente"));
                        userData.add(new UserData("U003", "Utente", "Utente"));
                        userData.add(new UserData("U004", "Utente", "Utente"));
                        userData.add(new UserData("U005", "Utente", "Utente"));
                        break;
                    case REMOVE_REVIEWER:
                        // Questo caso è gestito sopra
                        break;
                }
            }
        }
    }
    
    /**
     * Carica i revisori reali dal database per la conferenza corrente
     */
    private void loadReviewersFromDatabase() {
        try {
            if (conferenceControl != null && ConferenceControl.getConferenza() != null) {
                java.util.LinkedList<com.cms.users.Entity.UtenteE> revisori = 
                    com.cms.App.dbms.getRevisori(ConferenceControl.getConferenza().getId());
                
                if (revisori != null && !revisori.isEmpty()) {
                    hasData = true;
                    for (com.cms.users.Entity.UtenteE revisore : revisori) {
                        userData.add(new UserData(
                            String.valueOf(revisore.getId()), 
                            revisore.getUsername(), 
                            "Revisore"
                        ));
                    }
                } else {
                    hasData = false;
                }
            } else {
                hasData = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            hasData = false;
        }
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Selezione Membri");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        
        profiloButton = new JButton("👤");
        profiloButton.setBackground(Color.ORANGE);
        profiloButton.setForeground(Color.WHITE);
        profiloButton.setFont(new Font("Arial", Font.BOLD, 16));
        profiloButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        profiloButton.setFocusPainted(false);
        
        // Label per stato vuoto
        String emptyMessage = "Nessun utente trovato";
        emptyLabel = new JLabel(emptyMessage);
        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emptyLabel.setVerticalAlignment(SwingConstants.CENTER);
        emptyLabel.setBackground(Color.LIGHT_GRAY);
        emptyLabel.setOpaque(true);
        emptyLabel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Bottone azione
        String buttonText = getActionButtonText();
        actionButton = new JButton(buttonText);
        actionButton.setBackground(Color.ORANGE);
        actionButton.setForeground(Color.WHITE);
        actionButton.setFont(new Font("Arial", Font.BOLD, 14));
        actionButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        actionButton.setFocusPainted(false);
        
        // Pannello statistiche per Chair
        if (userRole == UserRole.CHAIR && action == Action.ADD_REVIEWER) {
            createStatsPanel();
        }
    }
    
    /**
     * Ottiene il testo del bottone in base all'azione
     */
    private String getActionButtonText() {
        switch (action) {
            case ADD_REVIEWER:
                return "ADD_REVIEWER";
            case REMOVE_REVIEWER:
                return "REMOVE_REVIEWER";
            case SUMMON_SUB_REVIEWER:
                return "SUMMON_SUB_REVIEWER";
            case ADD_COCHAIR:
                return "ADD_COCHAIR";
            default:
                return "Conferma";
        }
    }
    
    /**
     * Crea il pannello statistiche per Chair
     */
    private void createStatsPanel() {
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        totalReviewersLabel = new JLabel("Numero revisori totali: " + totalReviewers);
        totalReviewersLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalReviewersLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        neededReviewersLabel = new JLabel("Numero revisori necessari: " + neededReviewers);
        neededReviewersLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        neededReviewersLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        toSummonReviewersLabel = new JLabel("Numero revisori da convocare: " + toSummonReviewers);
        toSummonReviewersLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        toSummonReviewersLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        statsPanel.add(totalReviewersLabel);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(neededReviewersLabel);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(toSummonReviewersLabel);
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
        
        // Pannello superiore con titolo e statistiche
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        // Titolo
        JLabel titleLabel = new JLabel(screenTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Aggiungi statistiche se necessario
        if (statsPanel != null) {
            topPanel.add(statsPanel, BorderLayout.CENTER);
        }
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Pannello centrale (tabella o messaggio vuoto)
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Pannello bottone in basso
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        bottomPanel.add(actionButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Aggiorna la visualizzazione in base ai dati
     */
    private void updateDisplay() {
        centerPanel.removeAll();
        
        if (!hasData) {
            // Mostra messaggio vuoto
            centerPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            // Mostra tabella
            createTable();
            JScrollPane scrollPane = new JScrollPane(dataTable);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            centerPanel.add(scrollPane, BorderLayout.CENTER);
        }
        
        centerPanel.revalidate();
        centerPanel.repaint();
    }
    
    /**
     * Crea la tabella in base all'azione
     */
    private void createTable() {
        String[] columnNames = {action == Action.REMOVE_REVIEWER ? "Revisore" : "Utente"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabella di sola visualizzazione
            }
        };
        
        // Popola la tabella
        for (UserData data : userData) {
            Object[] row = {data.name};
            tableModel.addRow(row);
        }
        
        dataTable = new JTable(tableModel);
        dataTable.setFont(new Font("Arial", Font.PLAIN, 14));
        dataTable.setRowHeight(40);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Imposta larghezza colonne
        dataTable.getColumnModel().getColumn(0).setPreferredWidth(600);
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
            JOptionPane.showMessageDialog(this, "Apertura menu notifiche...");
        });
        
        // Bottone profilo
        profiloButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Apertura profilo utente...");
        });
        
        // Bottone azione principale
        actionButton.addActionListener(e -> {
            handleActionButton();
        });
    }
    
    /**
     * Gestisce il click sul bottone azione
     */
    private void handleActionButton() {
        if (!hasData) {
            JOptionPane.showMessageDialog(this, 
                "Nessun elemento da elaborare.", 
                "Attenzione", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Seleziona un elemento dalla lista.", 
                "Selezione richiesta", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        UserData selectedUser = userData.get(selectedRow);
        String actionText = getActionButtonText();
        String message = actionText + " eseguito per: " + selectedUser.name;
        
        JOptionPane.showMessageDialog(this, message, actionText, JOptionPane.INFORMATION_MESSAGE);
        //logica
        switch(actionText)
        {
            case "ADD_REVIEWER":
                conferenceControl.assegnaRevisore(Integer.parseInt(selectedUser.id));
                break;
            case "ADD_COCHAIR":
                conferenceControl.selezionaCoChair(Integer.parseInt(selectedUser.id));
                break;
            case "REMOVE_REVIEWER":
                conferenceControl.selezionaRevisore(Integer.parseInt(selectedUser.id));
                break;
            case "SUMMON_SUB_REVIEWER":
                
                break;
            default:
                    //DEFINIRE
                break;
        }
        
        
        // Simula l'azione e chiudi la finestra
        dispose();
    }
    
    // Metodi dell'interfaccia originale
    
    /**
     * Crea e mostra la schermata
     */
    public void create() {
        setVisible(true);
    }
    
    /**
     * Seleziona un co-chair
     */
    public void selezionaCoChair(String idCoChair) {
        // Implementazione per selezione co-chair
        JOptionPane.showMessageDialog(this, "Co-Chair selezionato: " + idCoChair);
    }
    
    /**
     * Bottone convoca co-chair
     */
    public void convocaCoChairButton() {
        handleActionButton();
    }
    
    /**
     * Invia messaggio di lista vuota
     */
    public void sendNoListMessage() {
        JOptionPane.showMessageDialog(this, "Nessun utente trovato", "Lista vuota", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Mostra lista membri
     */
    public void mostraListaMembriLista() {
        updateDisplay();
    }
    
    /**
     * Bottone selezione membro
     */
    public void selectMemberButton() {
        if (dataTable != null && dataTable.getSelectedRow() != -1) {
            int selectedRow = dataTable.getSelectedRow();
            UserData selectedUser = userData.get(selectedRow);
            JOptionPane.showMessageDialog(this, "Membro selezionato: " + selectedUser.name);
        }
    }
    
    /**
     * Bottone convoca membri
     */
    public void summonMembersButton() {
        handleActionButton();
    }
    
    /**
     * Bottone rimuovi
     */
    public void rimuoviButton() {
        if (action == Action.REMOVE_REVIEWER) {
            handleActionButton();
        }
    }
    
    /**
     * Ottiene ID revisore selezionato
     */
    public int getIdRevisore() {
        if (dataTable != null && dataTable.getSelectedRow() != -1) {
            int selectedRow = dataTable.getSelectedRow();
            UserData selectedUser = userData.get(selectedRow);
            return Integer.parseInt(selectedUser.id.substring(1)); // Rimuove prefisso R/U
        }
        return 0;
    }
    
    /**
     * Seleziona utente
     */
    public void selectUtente() {
        selectMemberButton();
    }
    
    /**
     * Bottone convoca
     */
    public void convocaButton() {
        handleActionButton();
    }
    
    /**
     * Ottiene ID utente selezionato
     */
    public int getIdUtente() {
        if (dataTable != null && dataTable.getSelectedRow() != -1) {
            int selectedRow = dataTable.getSelectedRow();
            UserData selectedUser = userData.get(selectedRow);
            return Integer.parseInt(selectedUser.id.substring(1)); // Rimuove prefisso R/U
        }
        return 0;
    }
    
    // Metodi di utilità
    
    /**
     * Imposta se ci sono dati o meno
     */
    public void setHasData(boolean hasData) {
        this.hasData = hasData;
        updateDisplay();
    }
    
    /**
     * Imposta la lista degli utenti da visualizzare
     * Converte la lista di UtenteE in UserData per la visualizzazione
     */
    public void setUserList(LinkedList<UtenteE> utenti) {
        if (utenti != null && !utenti.isEmpty()) {
            this.userData = new ArrayList<>();
            for (UtenteE utente : utenti) {
                UserData userData = new UserData(
                    String.valueOf(utente.getId()),
                    utente.getUsername() + " (" + utente.getEmail() + ")",
                    "Utente"
                );
                this.userData.add(userData);
            }
            this.hasData = true;
        } else {
            this.userData = new ArrayList<>();
            this.hasData = false;
        }
        updateDisplay();
    }
    
    /**
     * Imposta le statistiche dei revisori (per Chair)
     */
    public void setReviewerStats(int total, int needed, int toSummon) {
        this.totalReviewers = total;
        this.neededReviewers = needed;
        this.toSummonReviewers = toSummon;
        
        if (statsPanel != null) {
            totalReviewersLabel.setText("Numero revisori totali: " + totalReviewers);
            neededReviewersLabel.setText("Numero revisori necessari: " + neededReviewers);
            toSummonReviewersLabel.setText("Numero revisori da convocare: " + toSummonReviewers);
        }
    }
    
    /**
     * Metodo main per test standalone
     */
    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test Revisore - Convoca sotto revisore (con dati)
            MemberListScreen screen1 = new MemberListScreen(UserRole.REVISORE, Action.SUMMON_SUB_REVIEWER, );
            screen1.setLocation(0, 0);
            screen1.setVisible(true);
            
            // Test Revisore - Convoca sotto revisore (senza dati)
            MemberListScreen screen2 = new MemberListScreen(UserRole.REVISORE, Action.SUMMON_SUB_REVIEWER);
            screen2.setHasData(false);
            screen2.setLocation(400, 0);
            screen2.setVisible(true);
            
            // Test Chair - Aggiungi revisore (con statistiche)
            MemberListScreen screen3 = new MemberListScreen(UserRole.CHAIR, Action.ADD_REVIEWER);
            screen3.setReviewerStats(15, 20, 5);
            screen3.setLocation(800, 0);
            screen3.setVisible(true);
            
            // Test Chair - Rimuovi revisore
            MemberListScreen screen4 = new MemberListScreen(UserRole.CHAIR, Action.REMOVE_REVIEWER);
            screen4.setLocation(0, 400);
            screen4.setVisible(true);
        });
    }*/
}
