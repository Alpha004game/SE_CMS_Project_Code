package com.cms.users.submissions.Interface;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import com.cms.users.Entity.ArticoloE;
import com.cms.users.submissions.Control.GestioneArticoliControl;

/**
 * <<boundary>>
 * ModifySubmissionScreen - Schermata per la modifica di una sottomissione esistente
 */
public class ModifySubmissionScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    
    // Campi del form
    private JTextField titoloField;
    private JTextArea abstractTextArea;
    private JTextField coAutoriField;
    
    // Keywords (dinamiche dal database)
    private JCheckBox[] keywordCheckboxes;
    private ArrayList<String> keywordOptions; // Caricato dinamicamente
    
    // File upload
    private JButton caricaArticoloButton;
    private JButton caricaAllegatoButton;
    private JButton modificaSottomissioneButton;
    
    // Attributi per i file selezionati
    private File articoloPdfFile;
    private File allegatoFile;
    
    // Dati della sottomissione esistente
    private String submissionId;
    private int idArticolo; // ID dell'articolo per caricare keywords specifiche
    private String originalTitolo;
    private String originalAbstract;
    private String originalKeywords;
    private String originalCoAutori;
    private String originalFile;
    private String originalAllegato;
    
    // Control per gestire le operazioni
    private GestioneArticoliControl gestioneArticoliControl;
    
    // Attributi per compatibilità
    private String titolo;
    private String abstractText;
    private String keywords;
    private String coAutori;
    private String file;
    private String allegato;
    
    /**
     * Costruttore di default
     */
    public ModifySubmissionScreen() {
        this.submissionId = "SUB" + (int)(Math.random() * 10000);
        this.idArticolo = 0;
        this.gestioneArticoliControl = new GestioneArticoliControl();
        this.keywordOptions = getDefaultKeywords(); // Usa keywords di default
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con ID articolo - carica keywords dinamiche dal database
     */
    public ModifySubmissionScreen(int idArticolo) {
        this.idArticolo = idArticolo;
        this.submissionId = "SUB" + idArticolo;
        this.gestioneArticoliControl = new GestioneArticoliControl();
        loadArticleData(); // Carica dati e keywords dal database
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
    }
    
    /**
     * Costruttore con dati esistenti (aggiornato per supportare keywords dinamiche)
     */
    public ModifySubmissionScreen(String submissionId, String titolo, String abstractText, 
                                 String keywords, String coAutori, String file, String allegato) {
        this.submissionId = submissionId != null ? submissionId : "SUB" + (int)(Math.random() * 10000);
        this.idArticolo = extractArticleIdFromSubmissionId(submissionId);
        this.gestioneArticoliControl = new GestioneArticoliControl();
        this.originalTitolo = titolo;
        this.originalAbstract = abstractText;
        this.originalKeywords = keywords;
        this.originalCoAutori = coAutori;
        this.originalFile = file;
        this.originalAllegato = allegato;
        
        // Se abbiamo un ID articolo valido, carica le keywords dal database
        if (this.idArticolo > 0) {
            try {
                // Carica l'articolo per ottenere l'ID della conferenza
                ArticoloE articolo = gestioneArticoliControl.caricaArticolo(this.idArticolo);
                if (articolo != null) {
                    int idConferenza = articolo.getIdConferenza();
                    // Carica tutte le keywords della conferenza (per le opzioni)
                    this.keywordOptions = gestioneArticoliControl.ottieniKeywordsConferenza(idConferenza);
                    System.out.println("DEBUG: Caricate " + keywordOptions.size() + " keywords dalla conferenza " + idConferenza);
                } else {
                    this.keywordOptions = getDefaultKeywords(); // Fallback
                }
            } catch (Exception e) {
                System.err.println("Errore nel caricamento keywords conferenza: " + e.getMessage());
                this.keywordOptions = getDefaultKeywords(); // Fallback
            }
            
            if (this.keywordOptions.isEmpty()) {
                this.keywordOptions = getDefaultKeywords(); // Fallback
            }
        } else {
            this.keywordOptions = getDefaultKeywords(); // Usa keywords di default
        }
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        // Popola i campi con i dati esistenti
        populateFields();
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Modifica Sottomissione");
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
        
        // Campi del form
        initializeFormFields();
        
        // Bottoni per i file
        initializeFileButtons();
        
        // Bottone principale
        modificaSottomissioneButton = new JButton("Modifica sottomissione");
        modificaSottomissioneButton.setBackground(Color.ORANGE);
        modificaSottomissioneButton.setForeground(Color.WHITE);
        modificaSottomissioneButton.setFont(new Font("Arial", Font.BOLD, 14));
        modificaSottomissioneButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        modificaSottomissioneButton.setFocusPainted(false);
        modificaSottomissioneButton.setPreferredSize(new Dimension(200, 45));
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
        
        // Keywords (checkbox dinamici basati sulle keywords caricate)
        if (keywordOptions != null && !keywordOptions.isEmpty()) {
            keywordCheckboxes = new JCheckBox[keywordOptions.size()];
            for (int i = 0; i < keywordOptions.size(); i++) {
                keywordCheckboxes[i] = new JCheckBox(keywordOptions.get(i));
                keywordCheckboxes[i].setBackground(fieldColor);
                keywordCheckboxes[i].setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                keywordCheckboxes[i].setPreferredSize(new Dimension(120, 30));
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
        JLabel titleLabel = new JLabel("Modifica sottomissione");
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
        
        // Bottone modifica sottomissione
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(modificaSottomissioneButton);
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
        
        // Layout dinamico per le keywords
        if (keywordCheckboxes != null && keywordCheckboxes.length > 0) {
            // Calcola il numero di righe e colonne ottimale
            int numKeywords = keywordCheckboxes.length;
            int cols = Math.min(4, numKeywords); // Massimo 4 colonne
            int rows = (int) Math.ceil((double) numKeywords / cols);
            
            JPanel gridPanel = new JPanel(new GridLayout(rows, cols, 10, 10));
            gridPanel.setBackground(Color.WHITE);
            gridPanel.setMaximumSize(new Dimension(500, rows * 40));
            
            for (JCheckBox checkbox : keywordCheckboxes) {
                gridPanel.add(checkbox);
            }
            
            // Aggiungi pannelli vuoti se necessario per riempire la griglia
            int totalCells = rows * cols;
            for (int i = numKeywords; i < totalCells; i++) {
                gridPanel.add(new JPanel()); // Pannello vuoto
            }
            
            gridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(gridPanel);
        } else {
            // Se non ci sono keywords, mostra un messaggio
            JLabel noKeywordsLabel = new JLabel("Nessuna keyword disponibile");
            noKeywordsLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            noKeywordsLabel.setForeground(Color.GRAY);
            noKeywordsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(noKeywordsLabel);
        }
        
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
        
        fileButtonsPanel.add(articoloPanel);
        fileButtonsPanel.add(allegatoPanel);
        
        panel.add(fileButtonsPanel);
        
        return panel;
    }
    
    /**
     * Popola i campi con i dati esistenti
     */
    private void populateFields() {
        if (originalTitolo != null) {
            titoloField.setText(originalTitolo);
        }
        
        if (originalAbstract != null) {
            abstractTextArea.setText(originalAbstract);
        }
        
        if (originalCoAutori != null) {
            coAutoriField.setText(originalCoAutori);
        }
        
        // Imposta keywords esistenti
        if (originalKeywords != null && !originalKeywords.trim().isEmpty() && keywordOptions != null) {
            String[] keywordArray = originalKeywords.split(",");
            for (String keyword : keywordArray) {
                String cleanKeyword = keyword.trim();
                for (int i = 0; i < keywordOptions.size(); i++) {
                    if (keywordOptions.get(i).equalsIgnoreCase(cleanKeyword)) {
                        keywordCheckboxes[i].setSelected(true);
                        break;
                    }
                }
            }
        }
        
        // Imposta file esistenti
        if (originalFile != null && !originalFile.trim().isEmpty()) {
            this.articoloPdfFile = new File(originalFile);
            caricaArticoloButton.setText("✓ " + articoloPdfFile.getName());
            caricaArticoloButton.setBackground(new Color(200, 255, 200));
        }
        
        if (originalAllegato != null && !originalAllegato.trim().isEmpty()) {
            this.allegatoFile = new File(originalAllegato);
            caricaAllegatoButton.setText("✓ " + allegatoFile.getName());
            caricaAllegatoButton.setBackground(new Color(200, 255, 200));
        }
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
    
    private void handleCaricaArticolo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleziona Nuovo Articolo PDF");
        
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
        fileChooser.setDialogTitle("Seleziona Nuovo Allegato");
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            allegatoFile = fileChooser.getSelectedFile();
            caricaAllegatoButton.setText("✓ " + allegatoFile.getName());
            caricaAllegatoButton.setBackground(new Color(200, 255, 200)); // Verde chiaro
            
            // Aggiorna attributo per compatibilità
            allegato = allegatoFile.getAbsolutePath();
        }
    }
    
    private void handleModificaSottomissione() {
        // Validazione campi obbligatori
        if (!validateForm()) {
            return;
        }
        
        // Raccoglie i dati modificati
        collectFormData();
        
        // Controlla se ci sono state modifiche
        if (!hasChanges()) {
            JOptionPane.showMessageDialog(this,
                "Nessuna modifica rilevata.\nNon è necessario salvare.",
                "Informazione",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Conferma modifica
        StringBuilder summary = new StringBuilder();
        summary.append("Conferma modifica sottomissione:\n\n");
        summary.append("ID: ").append(submissionId).append("\n");
        summary.append("Titolo: ").append(titolo).append("\n");
        summary.append("Abstract: ").append(abstractText.substring(0, Math.min(50, abstractText.length()))).append("...\n");
        summary.append("Keywords: ").append(keywords).append("\n");
        summary.append("Co-autori: ").append(coAutori).append("\n");
        summary.append("Articolo PDF: ").append(articoloPdfFile != null ? articoloPdfFile.getName() : "Non modificato").append("\n");
        summary.append("Allegato: ").append(allegatoFile != null ? allegatoFile.getName() : "Non modificato").append("\n");
        
        int result = JOptionPane.showConfirmDialog(this,
            summary.toString(),
            "Conferma Modifica",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            // Crea un oggetto ArticoloE con i dati modificati
            ArticoloE articoloModificato = new ArticoloE();
            articoloModificato.setId(idArticolo);
            articoloModificato.setTitolo(titolo);
            articoloModificato.setAbstractText(abstractText);
            
            // Gestisce i file (se modificati)
            if (articoloPdfFile != null) {
                articoloModificato.setFileArticolo(articoloPdfFile.getAbsolutePath());
            }
            if (allegatoFile != null) {
                articoloModificato.setAllegato(allegatoFile.getAbsolutePath());
            }
            
            // Converte le keywords da stringa a LinkedList
            LinkedList<String> keywordsList = new LinkedList<>();
            if (keywords != null && !keywords.trim().isEmpty()) {
                String[] keywordArray = keywords.split(",");
                for (String keyword : keywordArray) {
                    String cleanKeyword = keyword.trim();
                    if (!cleanKeyword.isEmpty()) {
                        keywordsList.add(cleanKeyword);
                    }
                }
            }
            articoloModificato.setKeywords(keywordsList);
            
            // Converte i co-autori da stringa a LinkedList
            LinkedList<String> coAutoriList = new LinkedList<>();
            if (coAutori != null && !coAutori.trim().isEmpty()) {
                String[] coAutoriArray = coAutori.split(",");
                for (String coAutore : coAutoriArray) {
                    String cleanCoAutore = coAutore.trim();
                    if (!cleanCoAutore.isEmpty()) {
                        coAutoriList.add(cleanCoAutore);
                    }
                }
            }
            articoloModificato.setCoAutori(coAutoriList);
            
            try {
                // Chiama il control per modificare la sottomissione
                boolean success = gestioneArticoliControl.modificaSottomissione(idArticolo, articoloModificato, "Modificato");
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Sottomissione modificata con successo!\n" +
                        "ID Sottomissione: " + submissionId + "\n" +
                        "Le modifiche sono state salvate e saranno riviste.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    System.out.println("Sottomissione modificata con successo: " + submissionId + " - " + titolo);
                    
                    // Chiude la schermata
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Errore durante il salvataggio delle modifiche.\n" +
                        "Riprova più tardi o contatta l'amministratore.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception e) {
                System.err.println("Errore durante la modifica della sottomissione: " + e.getMessage());
                e.printStackTrace();
                
                JOptionPane.showMessageDialog(this,
                    "Errore durante il salvataggio delle modifiche.\n" +
                    "Dettagli: " + e.getMessage(),
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
     * Verifica se ci sono state modifiche rispetto ai dati originali
     */
    private boolean hasChanges() {
        collectFormData();
        
        boolean titleChanged = !safeEquals(titolo, originalTitolo);
        boolean abstractChanged = !safeEquals(abstractText, originalAbstract);
        boolean keywordsChanged = !safeEquals(keywords, originalKeywords);
        boolean coAutoriChanged = !safeEquals(coAutori, originalCoAutori);
        boolean fileChanged = (articoloPdfFile != null && !safeEquals(file, originalFile));
        boolean allegatoChanged = (allegatoFile != null && !safeEquals(allegato, originalAllegato));
        
        return titleChanged || abstractChanged || keywordsChanged || coAutoriChanged || fileChanged || allegatoChanged;
    }
    
    /**
     * Confronto sicuro di stringhe (gestisce null)
     */
    private boolean safeEquals(String str1, String str2) {
        if (str1 == null && str2 == null) return true;
        if (str1 == null || str2 == null) return false;
        return str1.equals(str2);
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
     * Compila i campi del form con dati esistenti
     */
    public void compilaCampi(String titolo, String abstractText, String conflitti, String keywords, String coautori) {
        this.originalTitolo = titolo;
        this.originalAbstract = abstractText;
        this.originalKeywords = keywords;
        this.originalCoAutori = coautori;
        
        populateFields();
    }
    
    /**
     * Carica un file esistente
     */
    public void caricaFile(String file) {
        this.originalFile = file;
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
     * Gestisce il bottone modifica sottomissione
     */
    public String modificaButton() {
        handleModificaSottomissione();
        return "Modifica sottomissione elaborata";
    }
    
    /**
     * Ottiene i dati della sottomissione modificata
     */
    public String getDati() {
        collectFormData();
        return "ID: " + submissionId + "\n" +
               "Titolo: " + titolo + "\n" +
               "Abstract: " + abstractText + "\n" +
               "Keywords: " + keywords + "\n" +
               "Co-autori: " + coAutori + "\n" +
               "File: " + file + "\n" +
               "Allegato: " + allegato;
    }
    
    /**
     * Carica tutti i dati esistenti (metodo originale)
     */
    public void caricatoAll() {
        populateFields();
    }
    
    /**
     * Carica un allegato esistente
     */
    public void caricaAllegato(String file) {
        this.originalAllegato = file;
        if (file != null && !file.isEmpty()) {
            this.allegato = file;
            this.allegatoFile = new File(file);
            if (caricaAllegatoButton != null) {
                caricaAllegatoButton.setText("✓ " + allegatoFile.getName());
                caricaAllegatoButton.setBackground(new Color(200, 255, 200));
            }
        }
    }
    
    // Metodi aggiuntivi per la gestione delle modifiche
    
    /**
     * Imposta l'ID della sottomissione
     */
    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }
    
    /**
     * Ottiene l'ID della sottomissione
     */
    public String getSubmissionId() {
        return submissionId;
    }
    
    /**
     * Verifica se il form è valido
     */
    public boolean isFormValid() {
        return validateForm();
    }
    
    /**
     * Ripristina i dati originali
     */
    public void resetToOriginal() {
        populateFields();
    }
    
    /**
     * Verifica se ci sono modifiche non salvate
     */
    public boolean hasUnsavedChanges() {
        return hasChanges();
    }
    
    /**
     * Metodo main per testing standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test con dati esistenti
            ModifySubmissionScreen screen = new ModifySubmissionScreen(
                "SUB12345",
                "Machine Learning per Software Testing Automatico",
                "Questo articolo presenta un nuovo approccio per l'utilizzo di algoritmi di machine learning nel testing automatico del software...",
                "Machine Learning, Software Engineering, Artificial Intelligence",
                "Mario Rossi, Luigi Verdi",
                "c:/documents/paper.pdf",
                "c:/documents/attachment.zip"
            );
            screen.create();
        });
    }
    
    /**
     * Carica i dati dell'articolo dal database includendo le keywords specifiche
     */
    private void loadArticleData() {
        try {
            ArticoloE articolo = gestioneArticoliControl.caricaArticolo(idArticolo);
            if (articolo != null) {
                this.originalTitolo = articolo.getTitolo();
                this.originalAbstract = articolo.getAbstractText();
                
                // Ottieni l'ID della conferenza dall'articolo
                int idConferenza = articolo.getIdConferenza();
                
                // Carica tutte le keywords disponibili per la conferenza (per le opzioni da mostrare)
                this.keywordOptions = gestioneArticoliControl.ottieniKeywordsConferenza(idConferenza);
                
                // Carica le keywords specifiche dell'articolo (per pre-selezionarle)
                ArrayList<String> keywordsArticolo = gestioneArticoliControl.ottieniKeywordsArticolo(idArticolo);
                this.originalKeywords = String.join(", ", keywordsArticolo);
                
                // Co-autori
                if (articolo.getCoAutori() != null && !articolo.getCoAutori().isEmpty()) {
                    this.originalCoAutori = String.join(", ", articolo.getCoAutori());
                } else {
                    this.originalCoAutori = "";
                }
                
                // File (BLOB)
                this.originalFile = articolo.getFileArticolo() != null ? "Articolo_" + idArticolo + ".pdf" : "";
                this.originalAllegato = articolo.getAllegato() != null ? "Allegato_" + idArticolo + ".zip" : "";
                
                System.out.println("DEBUG: Caricati dati articolo " + idArticolo + 
                    " con " + keywordOptions.size() + " keywords della conferenza " + idConferenza +
                    " e " + keywordsArticolo.size() + " keywords specifiche dell'articolo");
            } else {
                System.err.println("Articolo non trovato con ID: " + idArticolo);
                this.keywordOptions = getDefaultKeywords();
                initializeWithDefaultData();
            }
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento dell'articolo: " + e.getMessage());
            this.keywordOptions = getDefaultKeywords();
            initializeWithDefaultData();
        }
    }
    
    /**
     * Restituisce le keywords di default
     */
    private ArrayList<String> getDefaultKeywords() {
        ArrayList<String> defaults = new ArrayList<>();
        defaults.add("Machine Learning");
        defaults.add("Software Engineering");
        defaults.add("Artificial Intelligence");
        defaults.add("Cloud Computing");
        defaults.add("Data Science");
        defaults.add("Cybersecurity");
        defaults.add("Mobile Development");
        defaults.add("Web Development");
        return defaults;
    }
    
    /**
     * Estrae l'ID dell'articolo dal submissionId
     */
    private int extractArticleIdFromSubmissionId(String submissionId) {
        try {
            if (submissionId != null && submissionId.startsWith("SUB")) {
                return Integer.parseInt(submissionId.substring(3));
            }
            return 0;
        } catch (NumberFormatException e) {
            System.err.println("Errore nell'estrazione dell'ID articolo da: " + submissionId);
            return 0;
        }
    }
    
    /**
     * Inizializza con dati di default
     */
    private void initializeWithDefaultData() {
        this.originalTitolo = "Titolo dell'articolo";
        this.originalAbstract = "Abstract dell'articolo...";
        this.originalKeywords = "";
        this.originalCoAutori = "";
        this.originalFile = "";
        this.originalAllegato = "";
    }
}
