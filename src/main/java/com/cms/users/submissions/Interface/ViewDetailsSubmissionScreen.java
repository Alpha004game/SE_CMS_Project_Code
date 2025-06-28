package com.cms.users.submissions.Interface;

import javax.swing.*;
import java.awt.*;

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
    
    // Keywords (8 campi checkbox - readonly)
    private JCheckBox[] keywordCheckboxes;
    private String[] keywordOptions = {
        "Machine Learning", "Software Engineering", "Artificial Intelligence", "Cloud Computing",
        "Data Science", "Cybersecurity", "Mobile Development", "Web Development"
    };
    
    // Sezione file (readonly)
    private JButton formatoPdfButton;
    private JButton caricaFileButton;
    private JCheckBox dichiaraOriginalitaCheckbox;
    
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
    }
    
    /**
     * Carica i dati della sottomissione (simulato)
     */
    private void loadSubmissionData() {
        // Simula il caricamento dei dati dal database
        initializeWithDefaultData(); // Per ora usa dati di default
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Dettagli Sottomissione");
        setSize(700, 800);
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
        titoloField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titoloField.setPreferredSize(new Dimension(400, 35));
        titoloField.setEditable(false);
        
        // Abstract (readonly)
        abstractTextArea = new JTextArea(4, 30);
        abstractTextArea.setBackground(fieldColor);
        abstractTextArea.setFont(fieldFont);
        abstractTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        abstractTextArea.setLineWrap(true);
        abstractTextArea.setWrapStyleWord(true);
        abstractTextArea.setEditable(false);
        
        // Co-autori (readonly)
        coAutoriField = new JTextField();
        coAutoriField.setBackground(fieldColor);
        coAutoriField.setFont(fieldFont);
        coAutoriField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        coAutoriField.setPreferredSize(new Dimension(400, 35));
        coAutoriField.setEditable(false);
        
        // Keywords (8 checkbox readonly)
        keywordCheckboxes = new JCheckBox[keywordOptions.length];
        for (int i = 0; i < keywordOptions.length; i++) {
            keywordCheckboxes[i] = new JCheckBox(keywordOptions[i]);
            keywordCheckboxes[i].setBackground(fieldColor);
            keywordCheckboxes[i].setFont(new Font("Arial", Font.PLAIN, 12));
            keywordCheckboxes[i].setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            keywordCheckboxes[i].setPreferredSize(new Dimension(160, 30));
            keywordCheckboxes[i].setEnabled(false); // Readonly
        }
        
        // Checkbox originalità (readonly)
        dichiaraOriginalitaCheckbox = new JCheckBox("Dichiara l'originalità");
        dichiaraOriginalitaCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        dichiaraOriginalitaCheckbox.setBackground(Color.WHITE);
        dichiaraOriginalitaCheckbox.setEnabled(false); // Readonly
    }
    
    /**
     * Inizializza i bottoni per i file
     */
    private void initializeFileButtons() {
        // Bottone formato PDF (readonly)
        formatoPdfButton = new JButton("Formato PDF");
        formatoPdfButton.setBackground(Color.WHITE);
        formatoPdfButton.setForeground(Color.BLACK);
        formatoPdfButton.setFont(new Font("Arial", Font.PLAIN, 12));
        formatoPdfButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formatoPdfButton.setFocusPainted(false);
        formatoPdfButton.setPreferredSize(new Dimension(120, 50));
        formatoPdfButton.setEnabled(false); // Readonly
        
        // Bottone carica file (readonly)
        caricaFileButton = new JButton("Carica file");
        caricaFileButton.setBackground(Color.WHITE);
        caricaFileButton.setForeground(Color.BLACK);
        caricaFileButton.setFont(new Font("Arial", Font.PLAIN, 12));
        caricaFileButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        caricaFileButton.setFocusPainted(false);
        caricaFileButton.setPreferredSize(new Dimension(120, 50));
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
        ritiraSottomissioneButton.setFont(new Font("Arial", Font.BOLD, 12));
        ritiraSottomissioneButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        ritiraSottomissioneButton.setFocusPainted(false);
        ritiraSottomissioneButton.setPreferredSize(new Dimension(160, 35));
        
        // Bottone scarica rapporto
        scaricaRapportoButton = new JButton("Scarica rapporto valutazione");
        scaricaRapportoButton.setBackground(Color.ORANGE);
        scaricaRapportoButton.setForeground(Color.WHITE);
        scaricaRapportoButton.setFont(new Font("Arial", Font.BOLD, 12));
        scaricaRapportoButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        scaricaRapportoButton.setFocusPainted(false);
        scaricaRapportoButton.setPreferredSize(new Dimension(200, 35));
        
        // Bottone modifica sottomissione
        modificaSottomissioneButton = new JButton("Modifica sottomissione");
        modificaSottomissioneButton.setBackground(Color.ORANGE);
        modificaSottomissioneButton.setForeground(Color.WHITE);
        modificaSottomissioneButton.setFont(new Font("Arial", Font.BOLD, 12));
        modificaSottomissioneButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        modificaSottomissioneButton.setFocusPainted(false);
        modificaSottomissioneButton.setPreferredSize(new Dimension(180, 35));
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
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Titolo
        JLabel titleLabel = new JLabel("Visualizza sottomissione");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(titleLabel);
        
        // Sezione titolo articolo
        mainPanel.add(createTitoloSection());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Sezione abstract
        mainPanel.add(createAbstractSection());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Sezione keywords
        mainPanel.add(createKeywordsSection());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Sezione co-autori
        mainPanel.add(createCoAutoriSection());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Sezione file
        mainPanel.add(createFileSection());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
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
        titoloField.setMaximumSize(new Dimension(500, 35));
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
        scrollPane.setMaximumSize(new Dimension(500, 100));
        scrollPane.setPreferredSize(new Dimension(500, 100));
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
        
        JLabel label = new JLabel("Seleziona keywords:");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Griglia 2x4 per i checkbox
        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setMaximumSize(new Dimension(500, 80));
        
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
        coAutoriField.setMaximumSize(new Dimension(500, 35));
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
        
        // Riga per carica articolo e allegato
        JPanel fileButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        fileButtonsPanel.setBackground(Color.WHITE);
        
        // Carica articolo
        JPanel articoloPanel = new JPanel();
        articoloPanel.setLayout(new BoxLayout(articoloPanel, BoxLayout.Y_AXIS));
        articoloPanel.setBackground(Color.WHITE);
        JLabel articoloLabel = new JLabel("Carica articolo:");
        articoloLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        articoloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        articoloPanel.add(articoloLabel);
        articoloPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formatoPdfButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        articoloPanel.add(formatoPdfButton);
        
        // Carica allegato
        JPanel allegatoPanel = new JPanel();
        allegatoPanel.setLayout(new BoxLayout(allegatoPanel, BoxLayout.Y_AXIS));
        allegatoPanel.setBackground(Color.WHITE);
        JLabel allegatoLabel = new JLabel("Carica allegato:");
        allegatoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        allegatoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        allegatoPanel.add(allegatoLabel);
        allegatoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        caricaFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        allegatoPanel.add(caricaFileButton);
        
        // Checkbox originalità
        JPanel originalitaPanel = new JPanel();
        originalitaPanel.setLayout(new BoxLayout(originalitaPanel, BoxLayout.Y_AXIS));
        originalitaPanel.setBackground(Color.WHITE);
        originalitaPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        dichiaraOriginalitaCheckbox.setAlignmentX(Component.CENTER_ALIGNMENT);
        originalitaPanel.add(dichiaraOriginalitaCheckbox);
        
        fileButtonsPanel.add(articoloPanel);
        fileButtonsPanel.add(allegatoPanel);
        fileButtonsPanel.add(originalitaPanel);
        
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
        
        // Prima riga: Ritira sottomissione
        JPanel firstRowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        firstRowPanel.setBackground(Color.WHITE);
        firstRowPanel.add(ritiraSottomissioneButton);
        
        // Seconda riga: Scarica rapporto e Modifica
        JPanel secondRowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        secondRowPanel.setBackground(Color.WHITE);
        secondRowPanel.add(scaricaRapportoButton);
        secondRowPanel.add(modificaSottomissioneButton);
        
        panel.add(firstRowPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(secondRowPanel);
        
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
        if (keywords != null && !keywords.trim().isEmpty()) {
            String[] keywordArray = keywords.split(",");
            for (String keyword : keywordArray) {
                String cleanKeyword = keyword.trim();
                for (int i = 0; i < keywordOptions.length; i++) {
                    if (keywordOptions[i].equalsIgnoreCase(cleanKeyword)) {
                        keywordCheckboxes[i].setSelected(true);
                        break;
                    }
                }
            }
        }
        
        // Imposta file
        if (filePath != null && !filePath.trim().isEmpty()) {
            formatoPdfButton.setText("✓ " + filePath);
            formatoPdfButton.setBackground(new Color(200, 255, 200));
        }
        
        if (allegatoPath != null && !allegatoPath.trim().isEmpty()) {
            caricaFileButton.setText("✓ " + allegatoPath);
            caricaFileButton.setBackground(new Color(200, 255, 200));
        }
        
        dichiaraOriginalitaCheckbox.setSelected(originalitaDichiarata);
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
}
