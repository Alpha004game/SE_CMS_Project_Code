package com.cms.users.submissions.Interface;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

import com.cms.users.Entity.ArticoloE;
import com.cms.users.submissions.Control.GestioneArticoliControl;

/**
 * <<boundary>>
 * NewSubmissionScreen - Schermata per la creazione di una nuova sottomissione
 */
public class NewSubmissionScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    
    // Campi del form
    private JTextField titoloField;
    private JTextArea abstractTextArea;
    private JTextField coAutoriField;
    
    // Keywords (checkbox dinamici basati sulla conferenza)
    private JCheckBox[] keywordCheckboxes;
    private ArrayList<String> keywordOptions;
    
    // File upload
    private JButton caricaArticoloButton;
    private JButton caricaAllegatoButton;
    private JCheckBox dichiarazioneOriginalitaCheckbox;
    private JButton caricaSottomissioneButton;
    
    // Attributi per i file selezionati
    private File articoloPdfFile;
    private File allegatoFile;
    
    // Attributi per l'integrazione con il database
    private int idConferenza;
    private int idUtente;
    private GestioneArticoliControl gestioneArticoliControl;
    
    // Attributi originali (per compatibilità)
    private String titolo;
    private String abstractText;
    private String keywords;
    private String coAutori;
    private String dichiarazioneOriginalita;
    private String file;
    private String allegato;
    
    /**
     * Costruttore di default (per compatibilità)
     */
    public NewSubmissionScreen() {
        // Inizializza con keywords di default
        this.keywordOptions = new ArrayList<>();
        this.keywordOptions.add("Machine Learning");
        this.keywordOptions.add("Software Engineering");
        this.keywordOptions.add("Artificial Intelligence");
        this.keywordOptions.add("Cloud Computing");
        this.keywordOptions.add("Data Science");
        this.keywordOptions.add("Cybersecurity");
        this.keywordOptions.add("Mobile Development");
        this.keywordOptions.add("Web Development");
        
        this.idConferenza = -1;
        this.idUtente = -1;
        this.gestioneArticoliControl = new GestioneArticoliControl();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore per sottomissione con keywords specifiche della conferenza
     */
    public NewSubmissionScreen(int idConferenza, ArrayList<String> keywordsConferenza) {
        this.idConferenza = idConferenza;
        this.idUtente = -1; // Sarà impostato quando necessario
        this.keywordOptions = new ArrayList<>(keywordsConferenza);
        this.gestioneArticoliControl = new GestioneArticoliControl();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore completo con utente, conferenza e keywords
     */
    public NewSubmissionScreen(int idUtente, int idConferenza, ArrayList<String> keywordsConferenza) {
        this.idUtente = idUtente;
        this.idConferenza = idConferenza;
        this.keywordOptions = new ArrayList<>(keywordsConferenza);
        this.gestioneArticoliControl = new GestioneArticoliControl();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Nuova Sottomissione");
        setSize(1400, 800);
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
        
        // Campi del form
        initializeFormFields();
        
        // Bottoni per i file
        initializeFileButtons();
        
        // Bottone principale
        caricaSottomissioneButton = new JButton("Sottometti");
        caricaSottomissioneButton.setBackground(Color.ORANGE);
        caricaSottomissioneButton.setForeground(Color.WHITE);
        caricaSottomissioneButton.setFont(new Font("Arial", Font.BOLD, 14));
        caricaSottomissioneButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        caricaSottomissioneButton.setFocusPainted(false);
        caricaSottomissioneButton.setPreferredSize(new Dimension(180, 45));
    }
    
    /**
     * Inizializza i campi del form
     */
    private void initializeFormFields() {
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Color fieldColor = Color.LIGHT_GRAY;
        
        // Titolo dell'articolo
        titoloField = new JTextField();
        titoloField.setBackground(fieldColor);
        titoloField.setFont(fieldFont);
        titoloField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titoloField.setPreferredSize(new Dimension(400, 35));
        
        // Abstract
        abstractTextArea = new JTextArea(4, 30);
        abstractTextArea.setBackground(fieldColor);
        abstractTextArea.setFont(fieldFont);
        abstractTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        abstractTextArea.setLineWrap(true);
        abstractTextArea.setWrapStyleWord(true);
        
        // Co-autori
        coAutoriField = new JTextField();
        coAutoriField.setBackground(fieldColor);
        coAutoriField.setFont(fieldFont);
        coAutoriField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        coAutoriField.setPreferredSize(new Dimension(400, 35));
        
        // Keywords (checkbox dinamici basati sulla conferenza)
        keywordCheckboxes = new JCheckBox[keywordOptions.size()];
        for (int i = 0; i < keywordOptions.size(); i++) {
            keywordCheckboxes[i] = new JCheckBox();
            keywordCheckboxes[i].setBackground(fieldColor);
            keywordCheckboxes[i].setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            keywordCheckboxes[i].setPreferredSize(new Dimension(120, 30));
        }
        
        // Checkbox dichiarazione originalità
        dichiarazioneOriginalitaCheckbox = new JCheckBox("Dichiara l'originalità");
        dichiarazioneOriginalitaCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        dichiarazioneOriginalitaCheckbox.setBackground(Color.WHITE);
    }
    
    /**
     * Inizializza i bottoni per i file
     */
    private void initializeFileButtons() {
        // Bottone carica articolo PDF
        caricaArticoloButton = new JButton("Formato PDF");
        caricaArticoloButton.setBackground(Color.WHITE);
        caricaArticoloButton.setForeground(Color.BLACK);
        caricaArticoloButton.setFont(new Font("Arial", Font.PLAIN, 12));
        caricaArticoloButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        caricaArticoloButton.setFocusPainted(false);
        caricaArticoloButton.setPreferredSize(new Dimension(120, 50));
        
        // Bottone carica allegato
        caricaAllegatoButton = new JButton("Carica file");
        caricaAllegatoButton.setBackground(Color.WHITE);
        caricaAllegatoButton.setForeground(Color.BLACK);
        caricaAllegatoButton.setFont(new Font("Arial", Font.PLAIN, 12));
        caricaAllegatoButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        caricaAllegatoButton.setFocusPainted(false);
        caricaAllegatoButton.setPreferredSize(new Dimension(120, 50));
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
        JLabel titleLabel = new JLabel("Nuova sottomissione");
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
        
        // Sezione upload file
        mainPanel.add(createFileUploadSection());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Bottone carica sottomissione
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(caricaSottomissioneButton);
        mainPanel.add(buttonPanel);
        
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
        
        // Calcola il numero di righe necessarie (massimo 4 colonne)
        int numKeywords = keywordOptions.size();
        int cols = Math.min(4, numKeywords);
        int rows = (int) Math.ceil((double) numKeywords / cols);
        
        // Griglia per i checkbox
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols, 10, 10));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setMaximumSize(new Dimension(500, rows * 30 + (rows - 1) * 10));
        
        for (int i = 0; i < keywordCheckboxes.length; i++) {
            JPanel keywordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            keywordPanel.setBackground(Color.WHITE);
            keywordPanel.add(keywordCheckboxes[i]);
            
            JLabel keywordLabel = new JLabel(keywordOptions.get(i));
            keywordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            keywordPanel.add(keywordLabel);
            
            gridPanel.add(keywordPanel);
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
     * Crea la sezione upload file
     */
    private JPanel createFileUploadSection() {
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
        caricaArticoloButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        articoloPanel.add(caricaArticoloButton);
        
        // Carica allegato
        JPanel allegatoPanel = new JPanel();
        allegatoPanel.setLayout(new BoxLayout(allegatoPanel, BoxLayout.Y_AXIS));
        allegatoPanel.setBackground(Color.WHITE);
        JLabel allegatoLabel = new JLabel("Carica allegato:");
        allegatoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        allegatoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        allegatoPanel.add(allegatoLabel);
        allegatoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        caricaAllegatoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        allegatoPanel.add(caricaAllegatoButton);
        
        // Checkbox originalità
        JPanel originalitaPanel = new JPanel();
        originalitaPanel.setLayout(new BoxLayout(originalitaPanel, BoxLayout.Y_AXIS));
        originalitaPanel.setBackground(Color.WHITE);
        originalitaPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        dichiarazioneOriginalitaCheckbox.setAlignmentX(Component.CENTER_ALIGNMENT);
        originalitaPanel.add(dichiarazioneOriginalitaCheckbox);
        
        fileButtonsPanel.add(articoloPanel);
        fileButtonsPanel.add(allegatoPanel);
        fileButtonsPanel.add(originalitaPanel);
        
        panel.add(fileButtonsPanel);
        
        return panel;
    }
    
    /**
     * Configura i gestori degli eventi
     */
    private void setupEventHandlers() {
        homeButton.addActionListener(e -> handleHomeAction());
        notificheButton.addActionListener(e -> handleNotificheAction());
        profiloButton.addActionListener(e -> handleProfiloAction());
        
        caricaArticoloButton.addActionListener(e -> handleCaricaArticolo());
        caricaAllegatoButton.addActionListener(e -> handleCaricaAllegato());
        caricaSottomissioneButton.addActionListener(e -> handleCaricaSottomissione());
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
    
    private void handleCaricaArticolo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleziona Articolo PDF");
        
        // Filtro per file PDF
        FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("File PDF (*.pdf)", "pdf");
        fileChooser.setFileFilter(pdfFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            articoloPdfFile = fileChooser.getSelectedFile();
            caricaArticoloButton.setText("✓ " + articoloPdfFile.getName());
            caricaArticoloButton.setBackground(new Color(200, 255, 200)); // Verde chiaro
            
            // Aggiorna attributo per compatibilità
            file = articoloPdfFile.getAbsolutePath();
        }
    }
    
    private void handleCaricaAllegato() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleziona Allegato");
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            allegatoFile = fileChooser.getSelectedFile();
            caricaAllegatoButton.setText("✓ " + allegatoFile.getName());
            caricaAllegatoButton.setBackground(new Color(200, 255, 200)); // Verde chiaro
            
            // Aggiorna attributo per compatibilità
            allegato = allegatoFile.getAbsolutePath();
        }
    }
    
    private void handleCaricaSottomissione() {
        // Validazione campi obbligatori
        if (!validateForm()) {
            return;
        }
        
        // Raccoglie i dati
        collectFormData();
        
        // Conferma sottomissione
        StringBuilder summary = new StringBuilder();
        summary.append("Conferma sottomissione:\n\n");
        summary.append("Titolo: ").append(titolo).append("\n");
        summary.append("Abstract: ").append(abstractText.substring(0, Math.min(50, abstractText.length()))).append("...\n");
        summary.append("Keywords: ").append(keywords).append("\n");
        summary.append("Co-autori: ").append(coAutori).append("\n");
        summary.append("Articolo PDF: ").append(articoloPdfFile != null ? articoloPdfFile.getName() : "Non caricato").append("\n");
        summary.append("Allegato: ").append(allegatoFile != null ? allegatoFile.getName() : "Nessuno").append("\n");
        summary.append("Originalità dichiarata: ").append(dichiarazioneOriginalita).append("\n");
        
        int result = JOptionPane.showConfirmDialog(this,
            summary.toString(),
            "Conferma Sottomissione",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                // Crea l'ArticoloE secondo il sequence diagram
                ArticoloE articolo = new ArticoloE();
                articolo.setTitolo(titolo);
                articolo.setAbstractText(abstractText);
                articolo.setStato("Inviato");
                articolo.setIdConferenza(idConferenza);
                articolo.setUltimaModifica(java.time.LocalDate.now());
                
                // Imposta keywords e co-autori
                List<String> selectedKeywords = getSelectedKeywords();
                LinkedList<String> keywordsList = new LinkedList<>(selectedKeywords);
                articolo.setKeywords(keywordsList);
                
                // Debug: Mostra le keywords selezionate
                System.out.println("Keywords selezionate per l'articolo: " + selectedKeywords);
                
                if (!coAutori.trim().isEmpty()) {
                    String[] coAutoriArray = coAutori.split(",");
                    LinkedList<String> coAutoriList = new LinkedList<>();
                    for (String coAutore : coAutoriArray) {
                        coAutoriList.add(coAutore.trim());
                    }
                    articolo.setCoAutori(coAutoriList);
                }
                
                articolo.setDichiarazioneOriginalita(dichiarazioneOriginalitaCheckbox.isSelected());
                
                // Gestisce i file convertendoli in BLOB per il database
                if (articoloPdfFile != null) {
                    try {
                        byte[] fileContent = readFileToByteArray(articoloPdfFile);
                        articolo.setFileArticolo(fileContent);
                        System.out.println("File articolo convertito in BLOB: " + fileContent.length + " bytes");
                    } catch (IOException e) {
                        System.err.println("Errore nella lettura del file articolo: " + e.getMessage());
                        JOptionPane.showMessageDialog(this,
                            "Errore durante la lettura del file articolo. Riprova.",
                            "Errore File",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                if (allegatoFile != null) {
                    try {
                        byte[] allegatoContent = readFileToByteArray(allegatoFile);
                        articolo.setAllegato(allegatoContent);
                        System.out.println("File allegato convertito in BLOB: " + allegatoContent.length + " bytes");
                    } catch (IOException e) {
                        System.err.println("Errore nella lettura del file allegato: " + e.getMessage());
                        JOptionPane.showMessageDialog(this,
                            "Errore durante la lettura del file allegato. Riprova.",
                            "Errore File",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                // Chiama il control per salvare la sottomissione (seguendo il SD)
                // Nota: per ora usa un ID utente di default, in futuro sarà preso dalla sessione
                int currentUserId = getCurrentUserId();
                gestioneArticoliControl.salvaSottomissione(articolo, currentUserId, idConferenza);
                
                // Mostra messaggio di successo
                JOptionPane.showMessageDialog(this,
                    "Sottomissione caricata con successo!\n" +
                    "La tua sottomissione è stata ricevuta e sarà valutata.",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Chiude la schermata
                dispose();
                
            } catch (Exception e) {
                // Gestisce errori durante il salvataggio
                System.err.println("Errore durante il salvataggio della sottomissione: " + e.getMessage());
                e.printStackTrace();
                
                JOptionPane.showMessageDialog(this,
                    "Errore durante il salvataggio della sottomissione.\n" +
                    "Riprova più tardi o contatta il supporto.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Valida i campi del form
     */
    private boolean validateForm() {
        if (titoloField.getText().trim().isEmpty()) {
            showError("Il titolo dell'articolo è obbligatorio!", titoloField);
            return false;
        }
        
        if (abstractTextArea.getText().trim().isEmpty()) {
            showError("L'abstract è obbligatorio!", abstractTextArea);
            return false;
        }
        
        if (articoloPdfFile == null) {
            showError("È obbligatorio caricare l'articolo in formato PDF!");
            return false;
        }
        
        if (!dichiarazioneOriginalitaCheckbox.isSelected()) {
            showError("È obbligatorio dichiarare l'originalità del lavoro!");
            return false;
        }
        
        // Verifica che almeno una keyword sia selezionata
        boolean hasKeyword = false;
        for (JCheckBox checkbox : keywordCheckboxes) {
            if (checkbox.isSelected()) {
                hasKeyword = true;
                break;
            }
        }
        
        if (!hasKeyword) {
            showError("Seleziona almeno una keyword!");
            return false;
        }
        
        return true;
    }
    
    /**
     * Raccoglie i dati dal form
     */
    private void collectFormData() {
        this.titolo = titoloField.getText().trim();
        this.abstractText = abstractTextArea.getText().trim();
        this.coAutori = coAutoriField.getText().trim();
        this.dichiarazioneOriginalita = dichiarazioneOriginalitaCheckbox.isSelected() ? "Sì" : "No";
        
        // Raccoglie le keywords selezionate
        List<String> selectedKeywords = new ArrayList<>();
        for (int i = 0; i < keywordCheckboxes.length; i++) {
            if (keywordCheckboxes[i].isSelected()) {
                selectedKeywords.add(keywordOptions.get(i));
            }
        }
        this.keywords = String.join(", ", selectedKeywords);
    }
    
    /**
     * Mostra un messaggio di errore
     */
    private void showError(String message, JComponent focusComponent) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
        if (focusComponent != null) {
            focusComponent.requestFocus();
        }
    }
    
    /**
     * Mostra un messaggio di errore senza focus
     */
    private void showError(String message) {
        showError(message, null);
    }
    
    // Metodi originali implementati
    
    /**
     * Crea e mostra la schermata
     */
    public void create() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            titoloField.requestFocus();
        });
    }
    
    /**
     * Compila i campi del form
     */
    public void compilaCampi(String titolo, String abstractText, String keywords, String coAutori, String dichiarazioneOriginalita) {
        if (titoloField != null) titoloField.setText(titolo != null ? titolo : "");
        if (abstractTextArea != null) abstractTextArea.setText(abstractText != null ? abstractText : "");
        if (coAutoriField != null) coAutoriField.setText(coAutori != null ? coAutori : "");
        
        // Imposta dichiarazione originalità
        if (dichiarazioneOriginalitaCheckbox != null) {
            dichiarazioneOriginalitaCheckbox.setSelected("Sì".equals(dichiarazioneOriginalita));
        }
        
        // Imposta keywords (se fornite come stringa separata da virgole)
        if (keywords != null && keywordCheckboxes != null) {
            String[] keywordArray = keywords.split(",");
            for (String keyword : keywordArray) {
                String cleanKeyword = keyword.trim();
                for (int i = 0; i < keywordOptions.size(); i++) {
                    if (keywordOptions.get(i).equals(cleanKeyword)) {
                        keywordCheckboxes[i].setSelected(true);
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Carica un file (articolo PDF)
     */
    public void caricaFile(String file) {
        if (file != null && !file.isEmpty()) {
            this.file = file;
            this.articoloPdfFile = new File(file);
            if (caricaArticoloButton != null) {
                caricaArticoloButton.setText("✓ " + articoloPdfFile.getName());
                caricaArticoloButton.setBackground(new Color(200, 255, 200));
            }
        }
    }
    
    /**
     * Carica un allegato
     */
    public void caricaAllegato(String file) {
        if (file != null && !file.isEmpty()) {
            this.allegato = file;
            this.allegatoFile = new File(file);
            if (caricaAllegatoButton != null) {
                caricaAllegatoButton.setText("✓ " + allegatoFile.getName());
                caricaAllegatoButton.setBackground(new Color(200, 255, 200));
            }
        }
    }
    
    /**
     * Gestisce il bottone carica sottomissione
     */
    public String caricaSottomissioneButton() {
        handleCaricaSottomissione();
        return "Sottomissione elaborata";
    }
    
    /**
     * Ottiene i dati della sottomissione
     */
    public String getDati() {
        collectFormData();
        return "Titolo: " + titolo + "\n" +
               "Abstract: " + abstractText + "\n" +
               "Keywords: " + keywords + "\n" +
               "Co-autori: " + coAutori + "\n" +
               "File: " + file + "\n" +
               "Allegato: " + allegato + "\n" +
               "Originalità: " + dichiarazioneOriginalita;
    }
    
    // Metodi di utilità
    
    /**
     * Pulisce tutti i campi del form
     */
    public void clearForm() {
        if (titoloField != null) titoloField.setText("");
        if (abstractTextArea != null) abstractTextArea.setText("");
        if (coAutoriField != null) coAutoriField.setText("");
        if (dichiarazioneOriginalitaCheckbox != null) dichiarazioneOriginalitaCheckbox.setSelected(false);
        
        if (keywordCheckboxes != null) {
            for (JCheckBox checkbox : keywordCheckboxes) {
                checkbox.setSelected(false);
            }
        }
        
        if (caricaArticoloButton != null) {
            caricaArticoloButton.setText("Formato PDF");
            caricaArticoloButton.setBackground(Color.WHITE);
        }
        
        if (caricaAllegatoButton != null) {
            caricaAllegatoButton.setText("Carica file");
            caricaAllegatoButton.setBackground(Color.WHITE);
        }
        
        articoloPdfFile = null;
        allegatoFile = null;
    }
    
    /**
     * Ottiene le keywords selezionate
     */
    public List<String> getSelectedKeywords() {
        List<String> selected = new ArrayList<>();
        if (keywordCheckboxes != null) {
            for (int i = 0; i < keywordCheckboxes.length; i++) {
                if (keywordCheckboxes[i].isSelected()) {
                    selected.add(keywordOptions.get(i));
                }
            }
        }
        return selected;
    }
    
    /**
     * Verifica se tutti i campi obbligatori sono compilati
     */
    public boolean isFormValid() {
        return !titoloField.getText().trim().isEmpty() &&
               !abstractTextArea.getText().trim().isEmpty() &&
               articoloPdfFile != null &&
               dichiarazioneOriginalitaCheckbox.isSelected() &&
               getSelectedKeywords().size() > 0;
    }
    
    /**
     * Chiude la schermata
     */
    public void destroy() {
        SwingUtilities.invokeLater(() -> {
            setVisible(false);
            dispose();
        });
    }
    
    /**
     * Metodo main per testing standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NewSubmissionScreen screen = new NewSubmissionScreen();
            screen.create();
        });
    }
    
    /**
     * Ottiene l'ID dell'utente corrente
     * In futuro dovrebbe essere ottenuto dalla sessione utente
     */
    private int getCurrentUserId() {
        if (idUtente != -1) {
            return idUtente;
        }
        // Per ora ritorna un ID di default, in futuro sarà gestito tramite sessione
        // TODO: Implementare gestione della sessione utente
        return 1; // ID di default per testing
    }
    
    /**
     * Imposta l'ID dell'utente corrente
     */
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }
    
    /**
     * Ottiene l'ID della conferenza
     */
    public int getIdConferenza() {
        return idConferenza;
    }
    
    /**
     * Imposta l'ID della conferenza
     */
    public void setIdConferenza(int idConferenza) {
        this.idConferenza = idConferenza;
    }
    
    /**
     * Imposta le keywords dinamicamente
     */
    public void setKeywordOptions(ArrayList<String> keywords) {
        if (keywords != null) {
            this.keywordOptions = new ArrayList<>(keywords);
            // Ricrea i checkbox se necessario
            if (keywordCheckboxes != null) {
                // In questo caso bisognerebbe ricreare l'interfaccia
                // Per ora, stampiamo le keywords
                System.out.println("Keywords aggiornate: " + keywords);
            }
        }
    }
    
    /**
     * Utility method to read file content as byte array for BLOB storage
     */
    private byte[] readFileToByteArray(File file) throws IOException {
        if (file == null || !file.exists()) {
            return null;
        }
        
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);
            return buffer;
        }
    }
    
    /**
     * Test method to verify file reading as byte array
     */
    public void testFileReading() {
        System.out.println("=== TEST FILE READING ===");
        
        // Test with a sample text file (create one if needed)
        try {
            // Create a small test file
            String testContent = "This is a test file content for BLOB testing.";
            File testFile = new File("test_sample.txt");
            
            try (java.io.FileWriter writer = new java.io.FileWriter(testFile)) {
                writer.write(testContent);
            }
            
            // Test reading the file as byte array
            byte[] fileBytes = readFileToByteArray(testFile);
            if (fileBytes != null) {
                String readContent = new String(fileBytes);
                System.out.println("File read successfully as BLOB:");
                System.out.println("Original content: " + testContent);
                System.out.println("Read content: " + readContent);
                System.out.println("Size: " + fileBytes.length + " bytes");
                System.out.println("Content matches: " + testContent.equals(readContent));
            } else {
                System.out.println("Failed to read file as byte array");
            }
            
            // Clean up test file
            testFile.delete();
            
        } catch (Exception e) {
            System.err.println("Error in file reading test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
