package com.cms.users.Commons;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import com.cms.users.Entity.ConferenzaE;
import com.cms.users.conference.Control.CreaConferenzaControl;
import com.cms.users.conference.Control.ConferenceControl;
import com.cms.users.submissions.Control.GestioneArticoliControl;
import com.cms.users.submissions.Control.GestionePreferenzeControl;
import com.cms.users.account.Control.GestionePCControl;
import com.cms.users.revisions.Control.GestioneRevisioneControl;
import com.cms.App;

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
    private int currentUserId; // ID dell'utente corrente
    
    // Componenti per il database
    private DBMSBoundary dbmsBoundary;
    
    // Dati delle conferenze
    private List<ConferenzaData> conferenze;
    
    // Costruttore
    public HomeScreen() {
        this.availableFeatures = new ArrayList<>();
        this.conferenze = new ArrayList<>();
        this.isUserLoggedIn = true;
        // Prova a ottenere l'ID dall'utente loggato, altrimenti usa 1 come fallback
        this.currentUserId = (App.utenteAccesso != null) ? App.utenteAccesso.getId() : 1;
        this.dbmsBoundary = new DBMSBoundary();
        
        // Inizializza HeaderScreen
        this.headerScreen = new HeaderScreen();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadConferenzeFromDatabase();
    }
    
    /**
     * Costruttore con dati utente
     */
    public HomeScreen(String userName, String userRole) {
        this.availableFeatures = new ArrayList<>();
        this.conferenze = new ArrayList<>();
        this.isUserLoggedIn = true;
        // Prova a ottenere l'ID dall'utente loggato, altrimenti usa 1 come fallback
        this.currentUserId = (App.utenteAccesso != null) ? App.utenteAccesso.getId() : 1;
        this.dbmsBoundary = new DBMSBoundary();
        
        // Inizializza HeaderScreen con dati utente
        this.headerScreen = new HeaderScreen(userName, userRole);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadConferenzeFromDatabase();
    }
    
    /**
     * Costruttore con ID utente
     */
    public HomeScreen(int userId) {
        this.availableFeatures = new ArrayList<>();
        this.conferenze = new ArrayList<>();
        this.isUserLoggedIn = true;
        this.currentUserId = userId;
        this.dbmsBoundary = new DBMSBoundary();
        
        // Inizializza HeaderScreen
        this.headerScreen = new HeaderScreen();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadConferenzeFromDatabase();
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
     * Carica le conferenze attive dal database
     */
    private void loadConferenzeFromDatabase() {
        try {
            conferenze.clear();
            
            // Debug: mostra quale utente è loggato e quale ID stiamo usando
            System.out.println("DEBUG: === INIZIO loadConferenzeFromDatabase ===");
            System.out.println("DEBUG: App.utenteAccesso: " + (App.utenteAccesso != null ? App.utenteAccesso.getUsername() + " (ID: " + App.utenteAccesso.getId() + ")" : "null"));
            System.out.println("DEBUG: currentUserId utilizzato: " + currentUserId);
            
            // Ottieni tutte le conferenze attive dal database
            LinkedList<ConferenzaE> conferenzeDB = dbmsBoundary.getConferenzeAttive();
            
            if (conferenzeDB != null && !conferenzeDB.isEmpty()) {
                System.out.println("DEBUG: Trovate " + conferenzeDB.size() + " conferenze attive");
                
                for (ConferenzaE conferenza : conferenzeDB) {
                    // Converti ConferenzaE in ConferenzaData per la tabella
                    String nomeCompleto = conferenza.getTitolo() + " " + conferenza.getAnnoEdizione();
                    
                    System.out.println("DEBUG: Elaborando conferenza: " + nomeCompleto + " (ID: " + conferenza.getId() + ")");
                    
                    // Ottieni il ruolo dell'utente per questa conferenza
                    String ruolo = getRuoloUtenteConferenza(currentUserId, conferenza.getId());
                    
                    System.out.println("DEBUG: Ruolo ottenuto dal DB: '" + ruolo + "'");
                    
                    // Se l'utente non ha un ruolo specifico nella conferenza, è un potenziale autore
                    if (ruolo == null || ruolo.isEmpty() || ruolo.equalsIgnoreCase("nessun ruolo") || ruolo.equalsIgnoreCase("utente")) {
                        System.out.println("DEBUG: Ruolo convertito a 'Potenziale Autore' (era: '" + ruolo + "')");
                        ruolo = "Potenziale Autore";
                    } else {
                        System.out.println("DEBUG: Ruolo mantenuto: '" + ruolo + "'");
                    }
                    
                    // Genera le azioni in base al ruolo effettivo
                    String[] azioni = generateActionsForRole(ruolo);
                    
                    System.out.println("DEBUG: Azioni generate per ruolo '" + ruolo + "': " + java.util.Arrays.toString(azioni));
                    
                    // Aggiungi sempre la conferenza alla lista (mostra tutte le conferenze)
                    conferenze.add(new ConferenzaData(conferenza.getId(), nomeCompleto, ruolo, azioni));
                }
            } else {
                System.out.println("DEBUG: Nessuna conferenza attiva trovata");
            }
            
            refreshTable();
            
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento delle conferenze dal database: " + e.getMessage());
            e.printStackTrace();
            
            // Mostra un messaggio di errore all'utente
            JOptionPane.showMessageDialog(this, 
                "Errore durante il caricamento delle conferenze.\nControllare la connessione al database.", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Ottiene il ruolo dell'utente per una specifica conferenza
     */
    private String getRuoloUtenteConferenza(int idUtente, int idConferenza) {
        try {
            System.out.println("DEBUG HomeScreen: Chiamando dbmsBoundary.getRuoloUtenteConferenza(" + idUtente + ", " + idConferenza + ")");
            String result = dbmsBoundary.getRuoloUtenteConferenza(idUtente, idConferenza);
            System.out.println("DEBUG HomeScreen: dbmsBoundary.getRuoloUtenteConferenza ha ritornato: '" + result + "'");
            return result;
        } catch (Exception e) {
            System.err.println("Errore nel recupero del ruolo utente: " + e.getMessage());
            e.printStackTrace();
            return "Utente";
        }
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
        // Seguendo il sequence diagram: HomeScreen -> CreaConferenzaControl
        CreaConferenzaControl creaConferenzaControl = new CreaConferenzaControl();
        creaConferenzaControl.creaConferenza();
        this.dispose();
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
        
        // Verifica che l'utente abbia il permesso di eseguire questa azione
        if (!isActionAllowedForUser(action, conferenza.ruolo)) {
            JOptionPane.showMessageDialog(this, 
                "Non hai il permesso di eseguire questa azione.\nRuolo richiesto: " + action + "\nTuo ruolo: " + conferenza.ruolo, 
                "Accesso Negato", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        switch (action.toLowerCase()) {
            case "chair":
                // Implementazione del sequence diagram: HomeScreen -> ConferenceControl -> DBMSBoundary
                ConferenceControl conferenceControl = new ConferenceControl();
                conferenceControl.apriGestioneConferenza(conferenza.id);
                break;
            case "revisore":
                // Implementazione del sequence diagram per visualizzare articoli assegnati
                visualizzaArticoliAssegnatiRevisore(conferenza.id);
                break;
            case "editore":
                JOptionPane.showMessageDialog(this, message + "\n\nApertura funzionalità Editore...");
                break;
            case "sotto-revisore":
                JOptionPane.showMessageDialog(this, message + "\n\nApertura funzionalità Sotto-revisore...");
                break;
            case "autore":
                // Implementazione del sequence diagram "Visualizza Sottomissioni"
                visualizzaSottomissioni(conferenza.id);
                break;
            case "selezione specifiche competenze":
                // Implementazione del sequence diagram "Aggiungi Competenze"
                specificaCompetenze(conferenza.id);
                break;
            case "modifica preferenze articolo":
                // Implementazione per modificare le preferenze degli articoli
                modificaPreferenzeArticolo(conferenza.id);
                break;
            default:
                JOptionPane.showMessageDialog(this, message);
                break;
        }
    }
    
    /**
     * Verifica se un'azione è permessa per il ruolo dell'utente
     */
    private boolean isActionAllowedForUser(String action, String userRole) {
        String[] allowedActions = generateActionsForRole(userRole);
        for (String allowedAction : allowedActions) {
            if (allowedAction.equalsIgnoreCase(action)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Visualizza le sottomissioni dell'utente per una conferenza specifica
     * Implementa il flusso del sequence diagram "Visualizza Sottomissioni"
     */
    private void visualizzaSottomissioni(int idConferenza) {
        try {
            // Ottieni l'ID dell'utente corrente dall'App (seguendo il sequence diagram)
            int idUtente = App.utenteAccesso.getId();
            
            // Crea il controller per la gestione degli articoli
            GestioneArticoliControl gestioneArticoliControl = new GestioneArticoliControl();
            
            // Invoca il metodo per visualizzare le sottomissioni
            gestioneArticoliControl.visualizzaSottomissioni(idUtente, idConferenza);
            this.dispose();
            
        } catch (Exception e) {
            System.err.println("Errore durante l'apertura delle sottomissioni: " + e.getMessage());
            e.printStackTrace();
            
            // Mostra un messaggio di errore all'utente
            JOptionPane.showMessageDialog(this, 
                "Errore durante l'apertura delle sottomissioni.\nRiprovare più tardi.", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Specifica le competenze per una conferenza specifica
     * Implementa il flusso del sequence diagram "Aggiungi Competenze"
     */
    private void specificaCompetenze(int idConferenza) {
        System.out.println("DEBUG HomeScreen: === INIZIO specificaCompetenze ===");
        System.out.println("DEBUG HomeScreen: idConferenza ricevuto: " + idConferenza);
        
        try {
            // Seguendo il sequence diagram: crea GestionePCControl
            System.out.println("DEBUG HomeScreen: Creando GestionePCControl");
            GestionePCControl gestionePCControl = new GestionePCControl();
            
            // Invoca il metodo per specificare le competenze
            System.out.println("DEBUG HomeScreen: Chiamando specificaCompetenze sul control");
            gestionePCControl.specificaCompetenze(idConferenza);
            
            // Chiudi questa finestra (l'utente passa alla SkillsSelectionScreen)
            this.dispose();
            
            System.out.println("DEBUG HomeScreen: === FINE specificaCompetenze ===");
            
        } catch (Exception e) {
            System.err.println("DEBUG HomeScreen: ERRORE durante specificaCompetenze: " + e.getMessage());
            e.printStackTrace();
            
            // Mostra un messaggio di errore all'utente
            JOptionPane.showMessageDialog(this, 
                "Errore durante l'apertura della selezione competenze.\nRiprovare più tardi.", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Gestisce la modifica delle preferenze degli articoli per un revisore
     * @param idConferenza ID della conferenza di cui modificare le preferenze
     */
    private void modificaPreferenzeArticolo(int idConferenza) {
        System.out.println("DEBUG HomeScreen: === INIZIO modificaPreferenzeArticolo ===");
        System.out.println("DEBUG HomeScreen: idConferenza ricevuto: " + idConferenza);
        
        try {
            // Crea il control per gestire le preferenze
            System.out.println("DEBUG HomeScreen: Creando GestionePreferenzeControl");
            GestionePreferenzeControl gestionePreferenzeControl = new GestionePreferenzeControl();
            
            // Invoca il metodo per modificare le preferenze
            System.out.println("DEBUG HomeScreen: Chiamando modificaPreferenze sul control");
            gestionePreferenzeControl.modificaPreferenze(idConferenza);
            
            // Chiudi questa finestra (l'utente passa alla GeneralSubmissionScreen)
            this.dispose();
            
            System.out.println("DEBUG HomeScreen: === FINE modificaPreferenzeArticolo ===");
            
        } catch (Exception e) {
            System.err.println("DEBUG HomeScreen: ERRORE durante modificaPreferenzeArticolo: " + e.getMessage());
            e.printStackTrace();
            
            // Mostra un messaggio di errore all'utente
            JOptionPane.showMessageDialog(this, 
                "Errore durante l'apertura della modifica preferenze.\nRiprovare più tardi.", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Gestisce la visualizzazione degli articoli assegnati a un revisore
     * @param idConferenza ID della conferenza per cui visualizzare gli articoli assegnati
     */
    private void visualizzaArticoliAssegnatiRevisore(int idConferenza) {
        System.out.println("DEBUG HomeScreen: === INIZIO visualizzaArticoliAssegnatiRevisore ===");
        System.out.println("DEBUG HomeScreen: idConferenza ricevuto: " + idConferenza);
        
        try {
            // Crea il control per gestire la revisione
            System.out.println("DEBUG HomeScreen: Creando GestioneRevisioneControl");
            GestioneRevisioneControl gestioneRevisioneControl = new GestioneRevisioneControl();
            
            // Invoca il metodo per visualizzare gli articoli assegnati
            System.out.println("DEBUG HomeScreen: Chiamando visualizzaArticoliAssegnati sul control");
            gestioneRevisioneControl.visualizzaArticoliAssegnati(idConferenza);
            
            // Chiudi questa finestra (l'utente passa alla ListScreen)
            this.dispose();
            
            System.out.println("DEBUG HomeScreen: === FINE visualizzaArticoliAssegnatiRevisore ===");
            
        } catch (Exception e) {
            System.err.println("DEBUG HomeScreen: ERRORE durante visualizzaArticoliAssegnatiRevisore: " + e.getMessage());
            e.printStackTrace();
            
            // Mostra un messaggio di errore all'utente
            JOptionPane.showMessageDialog(this, 
                "Errore durante l'apertura della visualizzazione articoli assegnati.\nRiprovare più tardi.", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
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
        loadConferenzeFromDatabase();
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
        conferenze.add(new ConferenzaData(-1, nome, ruolo, azioni)); // ID fittizio per compatibilità
        refreshTable();
    }
    
    /**
     * Aggiunge una conferenza con generazione automatica dei bottoni in base al ruolo
     */
    public void addConferenza(String nome, String ruolo) {
        String[] azioni = generateActionsForRole(ruolo);
        conferenze.add(new ConferenzaData(-1, nome, ruolo, azioni)); // ID fittizio per compatibilità
        refreshTable();
    }
    
    /**
     * Genera i bottoni appropriati in base al ruolo dell'utente per una specifica conferenza
     */
    private String[] generateActionsForRole(String ruolo) {
        System.out.println("DEBUG: generateActionsForRole chiamato con ruolo: '" + ruolo + "'");
        
        String[] result;
        switch (ruolo.toLowerCase()) {
            case "chair":
                // Chair vede i bottoni Chair e Autore
                result = new String[]{"Chair", "Autore"};
                break;
            case "editore":
                // Editore vede i bottoni Editore e Autore
                result = new String[]{"Editore", "Autore"};
                break;
            case "revisore":
                // Revisore vede i bottoni Revisore, selezione competenze, specifica preferenze e Autore
                result = new String[]{"Revisore", "Selezione specifiche competenze", "Modifica preferenze articolo", "Autore"};
                break;
            case "sotto-revisore":
                // Sotto-revisore vede i bottoni Sotto-revisore e Autore
                result = new String[]{"Sotto-revisore", "Autore"};
                break;
            case "autore":
                // Autore che ha già sottomesso vede solo il bottone Autore
                result = new String[]{"Autore"};
                break;
            case "potenziale autore":
                // Utenti senza ruolo nella conferenza (potenziali autori) vedono solo il bottone Autore
                result = new String[]{"Autore"};
                break;
            default:
                // Default: utenti non riconosciuti sono considerati potenziali autori
                System.out.println("DEBUG: Ruolo non riconosciuto '" + ruolo + "', uso default");
                result = new String[]{"Autore"};
                break;
        }
        
        System.out.println("DEBUG: generateActionsForRole ritorna: " + java.util.Arrays.toString(result));
        return result;
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
        int id;
        String nome;
        String ruolo;
        String[] azioni;
        
        ConferenzaData(int id, String nome, String ruolo, String[] azioni) {
            this.id = id;
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
            // Test con utente reale dal database
            HomeScreen homeScreen = new HomeScreen(); // Userà l'utente loggato o fallback a 1
            homeScreen.setWelcomeMessage("Benvenuto nel sistema CMS!");
            
            homeScreen.displayUserDashboard();
        });
    }
}
