package com.cms.users.submissions.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.LinkedList;
import java.util.ArrayList;
import com.cms.users.Entity.ArticoloE;
import com.cms.users.Commons.DBMSBoundary;

/**
 * <<boundary>>
 * ViewDetailsSubmissionScreen - Schermata per visualizzare i dettagli di una sottomissione
 */
public class ViewDetailsSubmissionScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    
    // Campi di visualizzazione (readonly)
    private JTextField titoloField;
    private JTextArea abstractTextArea;
    private JTextField coAutoriField;
    
    // Keywords (checkbox dinamici - readonly)
    private JCheckBox[] keywordCheckboxes;
    private ArrayList<String> keywordOptions;
    
    // Sezione file (readonly)
    private JButton formatoPdfButton;
    private JButton caricaFileButton;
    
    // Bottoni azione
    private JButton ritiraSottomissioneButton;
    private JButton scaricaRapportoButton;
    private JButton modificaSottomissioneButton;
    
    // Dati della sottomissione
    private String submissionId;
    private String titolo;
    private String abstractText;
    private String keywords;
    private String coAutori;
    private String filePath;
    private String allegatoPath;
    private boolean originalitaDichiarata;
    
    // BLOB data per i file
    private Object fileArticoloBLOB;
    private Object allegatoBLOB;
    
    /**
     * Costruttore di default
     */
    public ViewDetailsSubmissionScreen() {
        this.submissionId = "SUB" + (int)(Math.random() * 10000);
        initializeWithDefaultData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con ID sottomissione
     */
    public ViewDetailsSubmissionScreen(String submissionId) {
        this.submissionId = submissionId != null ? submissionId : "SUB" + (int)(Math.random() * 10000);
        loadSubmissionData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
    }
    
    /**
     * Costruttore con tutti i dati
     */
    public ViewDetailsSubmissionScreen(String submissionId, String titolo, String abstractText, 
                                     String keywords, String coAutori, String filePath, 
                                     String allegatoPath, boolean originalitaDichiarata) {
        this.submissionId = submissionId;
        this.titolo = titolo;
        this.abstractText = abstractText;
        this.keywords = keywords;
        this.coAutori = coAutori;
        this.filePath = filePath;
        this.allegatoPath = allegatoPath;
        this.originalitaDichiarata = originalitaDichiarata;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
    }
    
    /**
     * Costruttore con ID articolo (carica dati dal database)
     */
    public ViewDetailsSubmissionScreen(int idArticolo) {
        this.submissionId = "SUB" + idArticolo;
        loadSubmissionDataFromDatabase(idArticolo);
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
    }
    
    /**
     * Inizializza con dati di default per il testing
     */
    private void initializeWithDefaultData() {
        this.titolo = "Machine Learning per Software Testing Automatico";
        this.abstractText = "Questo articolo presenta un nuovo approccio per l'utilizzo di algoritmi di machine learning nel testing automatico del software, migliorando significativamente l'efficacia dei test e riducendo i tempi di sviluppo.";
        this.keywords = "Machine Learning, Software Engineering, Artificial Intelligence";
        this.coAutori = "Mario Rossi, Luigi Verdi, Anna Bianchi";
        this.filePath = "paper_ml_testing.pdf";
        this.allegatoPath = "supplementary_material.zip";
        this.originalitaDichiarata = true;
        
        // Inizializza keywords di default per i checkbox
        this.keywordOptions = new ArrayList<>();
        this.keywordOptions.add("Machine Learning");
        this.keywordOptions.add("Software Engineering");
        this.keywordOptions.add("Artificial Intelligence");
        
        // Per i test, imposta BLOB come null (nessun file di default)
        this.fileArticoloBLOB = null;
        this.allegatoBLOB = null;
    }
    
    /**
     * Carica i dati della sottomissione (simulato)
     */
    private void loadSubmissionData() {
        // Simula il caricamento dei dati dal database
        initializeWithDefaultData(); // Per ora usa dati di default
    }
    
    /**
     * Carica i dati della sottomissione dal database
     */
    private void loadSubmissionDataFromDatabase(int idArticolo) {
        DBMSBoundary dbms = new DBMSBoundary();
        
        try {
            // Carica i dati dell'articolo dal database
            ArticoloE articolo = dbms.getArticolo(idArticolo);
            
            if (articolo != null) {
                this.titolo = articolo.getTitolo();
                this.abstractText = articolo.getAbstractText();
                
                // Carica le keywords dal database e salva come string per la visualizzazione
                ArrayList<String> keywordsList = dbms.getKeywordsArticolo(idArticolo);
                this.keywords = String.join(", ", keywordsList);
                
                // Salva anche la lista per i checkbox
                this.keywordOptions = keywordsList;
                
                // Costruisci la stringa dei co-autori
                if (articolo.getCoAutori() != null && !articolo.getCoAutori().isEmpty()) {
                    this.coAutori = String.join(", ", articolo.getCoAutori());
                } else {
                    this.coAutori = "";
                }
                
                // Carica i BLOB dei file dal database
                this.fileArticoloBLOB = articolo.getFileArticolo();
                this.allegatoBLOB = articolo.getAllegato();
                
                // Imposta nomi descrittivi per l'interfaccia utente
                if (this.fileArticoloBLOB != null) {
                    this.filePath = "Articolo_" + idArticolo + ".pdf";
                } else {
                    this.filePath = "";
                }
                
                if (this.allegatoBLOB != null) {
                    this.allegatoPath = "Allegato_" + idArticolo + ".zip";
                } else {
                    this.allegatoPath = "";
                }
                this.originalitaDichiarata = articolo.isDichiarazioneOriginalita();
                
                System.out.println("DEBUG: Caricati dati per articolo " + idArticolo + 
                                 " - Keywords: " + this.keywords);
            } else {
                System.err.println("Articolo con ID " + idArticolo + " non trovato nel database");
                // Fallback ai dati di default
                initializeWithDefaultData();
            }
            
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento dell'articolo: " + e.getMessage());
            e.printStackTrace();
            // Fallback ai dati di default
            initializeWithDefaultData();
        }
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Dettagli Sottomissione");
        setSize(800, 700); // Più largo e meno alto
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(750, 650)); // Dimensione minima
        
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
        
        // Campi di visualizzazione
        initializeDisplayFields();
        
        // Bottoni file
        initializeFileButtons();
        
        // Bottoni azione
        initializeActionButtons();
    }
    
    /**
     * Inizializza i campi di visualizzazione
     */
    private void initializeDisplayFields() {
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Color fieldColor = Color.LIGHT_GRAY;
        
        // Titolo dell'articolo (readonly)
        titoloField = new JTextField();
        titoloField.setBackground(fieldColor);
        titoloField.setFont(fieldFont);
        titoloField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // Padding ridotto
        titoloField.setPreferredSize(new Dimension(350, 30)); // Dimensioni ridotte
        titoloField.setEditable(false);
        
        // Abstract (readonly)
        abstractTextArea = new JTextArea(3, 25); // Righe ridotte
        abstractTextArea.setBackground(fieldColor);
        abstractTextArea.setFont(fieldFont);
        abstractTextArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // Padding ridotto
        abstractTextArea.setLineWrap(true);
        abstractTextArea.setWrapStyleWord(true);
        abstractTextArea.setEditable(false);
        
        // Co-autori (readonly)
        coAutoriField = new JTextField();
        coAutoriField.setBackground(fieldColor);
        coAutoriField.setFont(fieldFont);
        coAutoriField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // Padding ridotto
        coAutoriField.setPreferredSize(new Dimension(350, 30)); // Dimensioni ridotte
        coAutoriField.setEditable(false);
        
        // Keywords (checkbox dinamici readonly)
        if (keywordOptions != null && !keywordOptions.isEmpty()) {
            keywordCheckboxes = new JCheckBox[keywordOptions.size()];
            for (int i = 0; i < keywordOptions.size(); i++) {
                keywordCheckboxes[i] = new JCheckBox(keywordOptions.get(i));
                keywordCheckboxes[i].setBackground(fieldColor);
                keywordCheckboxes[i].setFont(new Font("Arial", Font.PLAIN, 12));
                keywordCheckboxes[i].setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                keywordCheckboxes[i].setPreferredSize(new Dimension(160, 30));
                keywordCheckboxes[i].setEnabled(false); // Readonly
            }
        } else {
            // Se non ci sono keywords, crea un array vuoto
            keywordCheckboxes = new JCheckBox[0];
        }
    }
    
    /**
     * Inizializza i bottoni per i file
     */
    private void initializeFileButtons() {
        // Bottone formato PDF (readonly)
        formatoPdfButton = new JButton("Formato PDF");
        formatoPdfButton.setBackground(Color.WHITE);
        formatoPdfButton.setForeground(Color.BLACK);
        formatoPdfButton.setFont(new Font("Arial", Font.PLAIN, 11)); // Font più piccolo
        formatoPdfButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formatoPdfButton.setFocusPainted(false);
        formatoPdfButton.setPreferredSize(new Dimension(140, 35)); // Dimensioni ridotte
        
        // Bottone carica file (readonly)
        caricaFileButton = new JButton("Carica file");
        caricaFileButton.setBackground(Color.WHITE);
        caricaFileButton.setForeground(Color.BLACK);
        caricaFileButton.setFont(new Font("Arial", Font.PLAIN, 11)); // Font più piccolo
        caricaFileButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        caricaFileButton.setFocusPainted(false);
        caricaFileButton.setPreferredSize(new Dimension(140, 35)); // Dimensioni ridotte
        caricaFileButton.setEnabled(false); // Readonly
    }
    
    /**
     * Inizializza i bottoni di azione
     */
    private void initializeActionButtons() {
        // Bottone ritira sottomissione
        ritiraSottomissioneButton = new JButton("Ritira sottomissione");
        ritiraSottomissioneButton.setBackground(Color.ORANGE);
        ritiraSottomissioneButton.setForeground(Color.WHITE);
        ritiraSottomissioneButton.setFont(new Font("Arial", Font.BOLD, 11)); // Font più piccolo
        ritiraSottomissioneButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12)); // Padding ridotto
        ritiraSottomissioneButton.setFocusPainted(false);
        ritiraSottomissioneButton.setPreferredSize(new Dimension(140, 30)); // Dimensioni ridotte
        
        // Bottone scarica rapporto
        scaricaRapportoButton = new JButton("Scarica rapporto valutazione");
        scaricaRapportoButton.setBackground(Color.ORANGE);
        scaricaRapportoButton.setForeground(Color.WHITE);
        scaricaRapportoButton.setFont(new Font("Arial", Font.BOLD, 11)); // Font più piccolo
        scaricaRapportoButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12)); // Padding ridotto
        scaricaRapportoButton.setFocusPainted(false);
        scaricaRapportoButton.setPreferredSize(new Dimension(180, 30)); // Dimensioni ridotte
        
        // Bottone modifica sottomissione
        modificaSottomissioneButton = new JButton("Modifica sottomissione");
        modificaSottomissioneButton.setBackground(Color.ORANGE);
        modificaSottomissioneButton.setForeground(Color.WHITE);
        modificaSottomissioneButton.setFont(new Font("Arial", Font.BOLD, 11)); // Font più piccolo
        modificaSottomissioneButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12)); // Padding ridotto
        modificaSottomissioneButton.setFocusPainted(false);
        modificaSottomissioneButton.setPreferredSize(new Dimension(150, 30)); // Dimensioni ridotte
    }
    
    /**
     * Configura il layout dell'interfaccia
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header arancione
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
        
        // Pannello principale con scroll
        JPanel mainPanel = createMainPanel();
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Crea il pannello header
     */
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.ORANGE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); // Padding ridotto
        
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
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Margini ridotti
        
        // Titolo
        JLabel titleLabel = new JLabel("Visualizza sottomissione");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Font più piccolo
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // Margine ridotto
        mainPanel.add(titleLabel);
        
        // Usa un layout a due colonne per ottimizzare lo spazio
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(Color.WHITE);
        
        // Colonna sinistra
        JPanel leftColumn = new JPanel();
        leftColumn.setLayout(new BoxLayout(leftColumn, BoxLayout.Y_AXIS));
        leftColumn.setBackground(Color.WHITE);
        
        // Sezione titolo articolo
        leftColumn.add(createTitoloSection());
        leftColumn.add(Box.createRigidArea(new Dimension(0, 15))); // Spazio ridotto
        
        // Sezione abstract
        leftColumn.add(createAbstractSection());
        leftColumn.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Sezione co-autori
        leftColumn.add(createCoAutoriSection());
        
        // Colonna destra
        JPanel rightColumn = new JPanel();
        rightColumn.setLayout(new BoxLayout(rightColumn, BoxLayout.Y_AXIS));
        rightColumn.setBackground(Color.WHITE);
        
        // Sezione keywords
        rightColumn.add(createKeywordsSection());
        rightColumn.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Sezione file
        rightColumn.add(createFileSection());
        
        contentPanel.add(leftColumn);
        contentPanel.add(rightColumn);
        
        mainPanel.add(contentPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Bottoni azione
        mainPanel.add(createActionButtonsPanel());
        
        return mainPanel;
    }
    
    /**
     * Crea la sezione titolo
     */
    private JPanel createTitoloSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel("Titolo dell'articolo:");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        titoloField.setAlignmentX(Component.LEFT_ALIGNMENT);
        titoloField.setMaximumSize(new Dimension(350, 30)); // Dimensioni ridotte
        titoloField.setPreferredSize(new Dimension(350, 30));
        panel.add(titoloField);
        
        return panel;
    }
    
    /**
     * Crea la sezione abstract
     */
    private JPanel createAbstractSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel("Abstract:");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JScrollPane scrollPane = new JScrollPane(abstractTextArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(350, 80)); // Dimensioni ridotte
        scrollPane.setPreferredSize(new Dimension(350, 80));
        panel.add(scrollPane);
        
        return panel;
    }
    
    /**
     * Crea la sezione keywords
     */
    private JPanel createKeywordsSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel("Keywords articolo:");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Layout più compatto per le keywords
        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 5, 5)); // 2 colonne invece di 4
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setMaximumSize(new Dimension(350, 120)); // Dimensioni ridotte
        
        for (int i = 0; i < keywordCheckboxes.length; i++) {
            gridPanel.add(keywordCheckboxes[i]);
        }
        
        gridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(gridPanel);
        
        return panel;
    }
    
    /**
     * Crea la sezione co-autori
     */
    private JPanel createCoAutoriSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel("Co-autori:");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        coAutoriField.setAlignmentX(Component.LEFT_ALIGNMENT);
        coAutoriField.setMaximumSize(new Dimension(350, 30)); // Dimensioni ridotte
        coAutoriField.setPreferredSize(new Dimension(350, 30));
        panel.add(coAutoriField);
        
        return panel;
    }
    
    /**
     * Crea la sezione file
     */
    private JPanel createFileSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Layout più compatto per i file
        JPanel fileButtonsPanel = new JPanel();
        fileButtonsPanel.setLayout(new BoxLayout(fileButtonsPanel, BoxLayout.Y_AXIS));
        fileButtonsPanel.setBackground(Color.WHITE);
        
        // Carica articolo
        JPanel articoloPanel = new JPanel();
        articoloPanel.setLayout(new BoxLayout(articoloPanel, BoxLayout.Y_AXIS));
        articoloPanel.setBackground(Color.WHITE);
        JLabel articoloLabel = new JLabel("Carica articolo:");
        articoloLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Font più piccolo
        articoloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        articoloPanel.add(articoloLabel);
        articoloPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        formatoPdfButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        formatoPdfButton.setPreferredSize(new Dimension(140, 35)); // Dimensioni ridotte
        articoloPanel.add(formatoPdfButton);
        
        // Carica allegato
        JPanel allegatoPanel = new JPanel();
        allegatoPanel.setLayout(new BoxLayout(allegatoPanel, BoxLayout.Y_AXIS));
        allegatoPanel.setBackground(Color.WHITE);
        JLabel allegatoLabel = new JLabel("Carica allegato:");
        allegatoLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Font più piccolo
        allegatoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        allegatoPanel.add(allegatoLabel);
        allegatoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        caricaFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        caricaFileButton.setPreferredSize(new Dimension(140, 35)); // Dimensioni ridotte
        allegatoPanel.add(caricaFileButton);
        
        fileButtonsPanel.add(articoloPanel);
        fileButtonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        fileButtonsPanel.add(allegatoPanel);
        
        panel.add(fileButtonsPanel);
        
        return panel;
    }
    
    /**
     * Crea il pannello con i bottoni di azione
     */
    private JPanel createActionButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Layout più compatto per i bottoni
        JPanel buttonRowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonRowPanel.setBackground(Color.WHITE);
        
        // Ridimensiona i bottoni
        ritiraSottomissioneButton.setPreferredSize(new Dimension(140, 30));
        scaricaRapportoButton.setPreferredSize(new Dimension(180, 30));
        modificaSottomissioneButton.setPreferredSize(new Dimension(150, 30));
        
        buttonRowPanel.add(ritiraSottomissioneButton);
        buttonRowPanel.add(scaricaRapportoButton);
        buttonRowPanel.add(modificaSottomissioneButton);
        
        panel.add(buttonRowPanel);
        
        return panel;
    }
    
    /**
     * Popola i campi con i dati della sottomissione
     */
    private void populateFields() {
        if (titolo != null) {
            titoloField.setText(titolo);
        }
        
        if (abstractText != null) {
            abstractTextArea.setText(abstractText);
        }
        
        if (coAutori != null) {
            coAutoriField.setText(coAutori);
        }
        
        // Imposta keywords
        if (keywords != null && !keywords.trim().isEmpty() && keywordOptions != null) {
            String[] keywordArray = keywords.split(",");
            for (String keyword : keywordArray) {
                String cleanKeyword = keyword.trim();
                for (int i = 0; i < keywordOptions.size(); i++) {
                    if (keywordOptions.get(i).equalsIgnoreCase(cleanKeyword)) {
                        if (i < keywordCheckboxes.length) {
                            keywordCheckboxes[i].setSelected(true);
                        }
                        break;
                    }
                }
            }
        }
        
        // Imposta file PDF dell'articolo
        if (fileArticoloBLOB != null) {
            formatoPdfButton.setText("📄 " + (filePath != null ? filePath : "Articolo.pdf"));
            formatoPdfButton.setBackground(new Color(200, 255, 200));
            formatoPdfButton.setEnabled(true);
            formatoPdfButton.setToolTipText("Clicca per scaricare l'articolo");
            
            // Aggiungi listener per il download
            formatoPdfButton.addActionListener(e -> {
                String filename = filePath != null ? filePath : "Articolo_" + submissionId + ".pdf";
                downloadBLOB(fileArticoloBLOB, filename);
            });
        } else {
            formatoPdfButton.setText("Nessun file PDF");
            formatoPdfButton.setBackground(Color.LIGHT_GRAY);
            formatoPdfButton.setEnabled(false);
            formatoPdfButton.setToolTipText("Nessun file disponibile");
        }
        
        // Imposta file allegato
        if (allegatoBLOB != null) {
            caricaFileButton.setText("📎 " + (allegatoPath != null ? allegatoPath : "Allegato.zip"));
            caricaFileButton.setBackground(new Color(200, 255, 200));
            caricaFileButton.setEnabled(true);
            caricaFileButton.setToolTipText("Clicca per scaricare l'allegato");
            
            // Aggiungi listener per il download
            caricaFileButton.addActionListener(e -> {
                String filename = allegatoPath != null ? allegatoPath : "Allegato_" + submissionId + ".zip";
                downloadBLOB(allegatoBLOB, filename);
            });
        } else if (allegatoPath != null && !allegatoPath.trim().isEmpty()) {
            // Caso di fallback per visualizzazione legacy
            caricaFileButton.setText("✓ " + allegatoPath);
            caricaFileButton.setBackground(new Color(200, 255, 200));
            caricaFileButton.setEnabled(false);
        } else {
            caricaFileButton.setText("Nessun allegato");
            caricaFileButton.setBackground(Color.LIGHT_GRAY);
            caricaFileButton.setEnabled(false);
            caricaFileButton.setToolTipText("Nessun allegato disponibile");
        }
    }
    
    /**
     * Configura i gestori degli eventi
     */
    private void setupEventHandlers() {
        homeButton.addActionListener(e -> handleHomeAction());
        notificheButton.addActionListener(e -> handleNotificheAction());
        profiloButton.addActionListener(e -> handleProfiloAction());
        
        ritiraSottomissioneButton.addActionListener(e -> handleRitiraSottomissione());
        scaricaRapportoButton.addActionListener(e -> handleScaricaRapporto());
        modificaSottomissioneButton.addActionListener(e -> handleModificaSottomissione());
    }
    
    // Gestori degli eventi
    
    private void handleHomeAction() {
        dispose();
        // Navigazione alla HomeScreen
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
        try {
            Class<?> userMenuClass = Class.forName("com.cms.users.account.Interface.UserMenu");
            Object userMenu = userMenuClass.getDeclaredConstructor(String.class, String.class)
                                          .newInstance("Utente", "utente@cms.com");
            java.lang.reflect.Method showMethod = userMenuClass.getMethod("showMenuBelowButton", JButton.class);
            showMethod.invoke(userMenu, notificheButton);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Apertura menu utente...");
        }
    }
    
    private void handleProfiloAction() {
        // Apri UserInfoScreen
        try {
            Class<?> userInfoClass = Class.forName("com.cms.users.account.Interface.UserInfoScreen");
            Object userInfoScreen = userInfoClass.getDeclaredConstructor(String.class, String.class)
                                                 .newInstance("Utente", "utente@cms.com");
            java.lang.reflect.Method createMethod = userInfoClass.getMethod("create");
            createMethod.invoke(userInfoScreen);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Apertura profilo utente...");
        }
    }
    
    private void handleRitiraSottomissione() {
        // Conferma ritiro
        String[] options = {"Ritira", "Annulla"};
        int result = JOptionPane.showOptionDialog(this,
            "Sei sicuro di voler ritirare questa sottomissione?\n\n" +
            "ID: " + submissionId + "\n" +
            "Titolo: " + titolo + "\n\n" +
            "ATTENZIONE: Questa azione non può essere annullata.\n" +
            "La sottomissione sarà rimossa dal sistema.",
            "Ritira Sottomissione",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[1]);
        
        if (result == JOptionPane.YES_OPTION) {
            // Simula il ritiro
            JOptionPane.showMessageDialog(this,
                "Sottomissione ritirata con successo!\n\n" +
                "ID: " + submissionId + "\n" +
                "La sottomissione è stata rimossa dal sistema\n" +
                "e non sarà più visibile nella lista.",
                "Ritiro Completato",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Qui andrà la logica per rimuovere la sottomissione dal database
            System.out.println("Sottomissione ritirata: " + submissionId);
            
            // Chiude la schermata
            dispose();
        }
    }
    
    private void handleScaricaRapporto() {
        // Simula il download del rapporto di valutazione
        JOptionPane.showMessageDialog(this,
            "Download del rapporto di valutazione...\n\n" +
            "ID Sottomissione: " + submissionId + "\n" +
            "File: rapporto_valutazione_" + submissionId + ".pdf\n\n" +
            "Il rapporto contiene:\n" +
            "• Commenti dei revisori\n" +
            "• Valutazioni ricevute\n" +
            "• Suggerimenti per miglioramenti\n" +
            "• Stato della revisione",
            "Download Rapporto",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Qui andrà la logica per generare e scaricare il rapporto PDF
        System.out.println("Download rapporto per sottomissione: " + submissionId);
    }
    
    private void handleModificaSottomissione() {
        dispose();
        // Apri ModifySubmissionScreen
        try {
            Class<?> modifyScreenClass = Class.forName("com.cms.users.submissions.Interface.ModifySubmissionScreen");
            Object modifyScreen = modifyScreenClass.getDeclaredConstructor(
                String.class, String.class, String.class, String.class, 
                String.class, String.class, String.class)
                .newInstance(submissionId, titolo, abstractText, keywords, 
                           coAutori, filePath, allegatoPath);
            java.lang.reflect.Method createMethod = modifyScreenClass.getMethod("create");
            createMethod.invoke(modifyScreen);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Apertura schermata modifica...");
        }
    }
    
    // Metodi originali implementati
    
    /**
     * Crea e mostra la schermata
     */
    public void create() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }
    
    /**
     * Gestisce il bottone modifica sottomissione
     */
    public String modificaSottomissioneButton() {
        handleModificaSottomissione();
        return "Apertura schermata modifica sottomissione";
    }
    
    /**
     * Mostra la schermata dei dettagli
     */
    public void mostraViewDetailsSubmissionScreen() {
        create();
    }
    
    /**
     * Gestisce il bottone ritira sottomissione
     */
    public String ritraSottomissioneButton() {
        handleRitiraSottomissione();
        return "Sottomissione ritirata";
    }
    
    /**
     * Gestisce il bottone scarica rapporto valutazione
     */
    public String scaricaRapportoValutazioneButton() {
        handleScaricaRapporto();
        return "Download rapporto valutazione avviato";
    }
    
    // Metodi aggiuntivi per la gestione dei dati
    
    /**
     * Imposta i dati della sottomissione
     */
    public void setSubmissionData(String submissionId, String titolo, String abstractText, 
                                String keywords, String coAutori, String filePath, 
                                String allegatoPath, boolean originalitaDichiarata) {
        this.submissionId = submissionId;
        this.titolo = titolo;
        this.abstractText = abstractText;
        this.keywords = keywords;
        this.coAutori = coAutori;
        this.filePath = filePath;
        this.allegatoPath = allegatoPath;
        this.originalitaDichiarata = originalitaDichiarata;
        
        if (titoloField != null) {
            populateFields();
        }
    }
    
    /**
     * Ottiene l'ID della sottomissione
     */
    public String getSubmissionId() {
        return submissionId;
    }
    
    /**
     * Imposta l'ID della sottomissione
     */
    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }
    
    /**
     * Ottiene i dati della sottomissione
     */
    public String getSubmissionData() {
        return "ID: " + submissionId + "\n" +
               "Titolo: " + titolo + "\n" +
               "Abstract: " + abstractText + "\n" +
               "Keywords: " + keywords + "\n" +
               "Co-autori: " + coAutori + "\n" +
               "File: " + filePath + "\n" +
               "Allegato: " + allegatoPath + "\n" +
               "Originalità: " + originalitaDichiarata;
    }
    
    /**
     * Verifica se la sottomissione può essere modificata
     */
    public boolean canModify() {
        // Logica per determinare se la sottomissione può essere modificata
        // (es. non ancora in revisione, non pubblicata, ecc.)
        return true; // Per ora sempre true
    }
    
    /**
     * Verifica se la sottomissione può essere ritirata
     */
    public boolean canWithdraw() {
        // Logica per determinare se la sottomissione può essere ritirata
        return true; // Per ora sempre true
    }
    
    /**
     * Verifica se il rapporto di valutazione è disponibile
     */
    public boolean hasEvaluationReport() {
        // Logica per determinare se esiste un rapporto di valutazione
        return true; // Per ora sempre true
    }
    
    /**
     * Aggiorna i bottoni in base allo stato della sottomissione
     */
    public void updateButtonStates() {
        modificaSottomissioneButton.setEnabled(canModify());
        ritiraSottomissioneButton.setEnabled(canWithdraw());
        scaricaRapportoButton.setEnabled(hasEvaluationReport());
    }
    
    /**
     * Imposta i dati dell'articolo nella schermata utilizzando un oggetto ArticoloE
     * Questo metodo è chiamato dal GestioneArticoliControl per popolare i campi con dati reali
     */
    public void setArticoloData(ArticoloE articolo) {
        if (articolo == null) {
            System.err.println("Articolo nullo passato a setArticoloData");
            return;
        }
        
        try {
            // Imposta i dati dell'articolo nei campi dell'interfaccia
            this.submissionId = String.valueOf(articolo.getId());
            this.titolo = articolo.getTitolo() != null ? articolo.getTitolo() : "";
            this.abstractText = articolo.getAbstractText() != null ? articolo.getAbstractText() : "";
            
            // Converte la lista di keywords in stringa separata da virgole
            LinkedList<String> keywordsList = articolo.getKeywords();
            if (keywordsList != null && !keywordsList.isEmpty()) {
                this.keywords = String.join(", ", keywordsList);
            } else {
                this.keywords = "";
            }
            
            // Converte la lista di co-autori in stringa separata da virgole
            LinkedList<String> coAutoriList = articolo.getCoAutori();
            if (coAutoriList != null && !coAutoriList.isEmpty()) {
                this.coAutori = String.join(", ", coAutoriList);
            } else {
                this.coAutori = "";
            }
            
            // Carica i BLOB dei file dall'articolo
            this.fileArticoloBLOB = articolo.getFileArticolo();
            this.allegatoBLOB = articolo.getAllegato();
            
            // Imposta nomi descrittivi per l'interfaccia utente
            if (this.fileArticoloBLOB != null) {
                this.filePath = "Articolo_" + articolo.getId() + ".pdf";
            } else {
                this.filePath = "";
            }
            
            if (this.allegatoBLOB != null) {
                this.allegatoPath = "Allegato_" + articolo.getId() + ".zip";
            } else {
                this.allegatoPath = "";
            }
            
            // Imposta keywordOptions per i checkbox
            this.keywordOptions = articolo.getKeywords() != null ? 
                new ArrayList<>(articolo.getKeywords()) : new ArrayList<>();
            
            // Usa il metodo corretto per la dichiarazione di originalità
            this.originalitaDichiarata = articolo.isDichiarazioneOriginalita();
            
            // Popola i campi dell'interfaccia utente
            populateFields();
            
        } catch (Exception e) {
            System.err.println("Errore durante l'impostazione dei dati dell'articolo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Metodo main per testing standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test con dati di esempio
            ViewDetailsSubmissionScreen screen = new ViewDetailsSubmissionScreen(
                "SUB12345",
                "Machine Learning per Software Testing Automatico",
                "Questo articolo presenta un nuovo approccio per l'utilizzo di algoritmi di machine learning nel testing automatico del software, migliorando significativamente l'efficacia dei test e riducendo i tempi di sviluppo.",
                "Machine Learning, Software Engineering, Artificial Intelligence",
                "Mario Rossi, Luigi Verdi, Anna Bianchi",
                "paper_ml_testing.pdf",
                "supplementary_material.zip",
                true
            );
            screen.create();
        });
    }
    
    /**
     * Scarica un BLOB come file
     */
    private void downloadBLOB(Object blobData, String filename) {
        if (blobData == null) {
            JOptionPane.showMessageDialog(this, "Nessun file disponibile per il download.", 
                                        "File non trovato", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Apri un file chooser per selezionare dove salvare
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(filename));
            
            int userSelection = fileChooser.showSaveDialog(this);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                
                // Gestisci diversi tipi di BLOB
                if (blobData instanceof Blob) {
                    // Se è un java.sql.Blob
                    Blob sqlBlob = (Blob) blobData;
                    try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                        byte[] bytes = sqlBlob.getBytes(1, (int) sqlBlob.length());
                        fos.write(bytes);
                    }
                } else if (blobData instanceof byte[]) {
                    // Se è un array di byte
                    byte[] bytes = (byte[]) blobData;
                    try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                        fos.write(bytes);
                    }
                } else {
                    // Altri tipi - prova a convertire a stringa e poi a byte
                    String data = blobData.toString();
                    try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                        fos.write(data.getBytes());
                    }
                }
                
                JOptionPane.showMessageDialog(this, "File salvato con successo in:\n" + fileToSave.getAbsolutePath(),
                                            "Download completato", JOptionPane.INFORMATION_MESSAGE);
                
                System.out.println("DEBUG: File scaricato: " + fileToSave.getAbsolutePath());
                
            }
        } catch (Exception e) {
            System.err.println("Errore durante il download del file: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante il download del file:\n" + e.getMessage(),
                                        "Errore download", JOptionPane.ERROR_MESSAGE);
        }
    }
}
