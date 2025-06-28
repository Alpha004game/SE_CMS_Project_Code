package com.cms.users.submissions.Interface;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    
    // Keywords (8 campi checkbox)
    private JCheckBox[] keywordCheckboxes;
    private String[] keywordOptions = {
        "Machine Learning", "Software Engineering", "Artificial Intelligence", "Cloud Computing",
        "Data Science", "Cybersecurity", "Mobile Development", "Web Development"
    };
    
    // File upload
    private JButton caricaArticoloButton;
    private JButton caricaAllegatoButton;
    private JCheckBox dichiarazioneOriginalitaCheckbox;
    private JButton caricaSottomissioneButton;
    
    // Attributi per i file selezionati
    private File articoloPdfFile;
    private File allegatoFile;
    
    // Attributi originali (per compatibilità)
    private String titolo;
    private String abstractText;
    private String keywords;
    private String coAutori;
    private String dichiarazioneOriginalita;
    private String file;
    private String allegato;
    
    /**
     * Costruttore
     */
    public NewSubmissionScreen() {
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
        caricaSottomissioneButton = new JButton("Carica sottomissione");
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
        
        // Keywords (8 checkbox in griglia 2x4)
        keywordCheckboxes = new JCheckBox[keywordOptions.length];
        for (int i = 0; i < keywordOptions.length; i++) {
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
            // Simula il caricamento
            JOptionPane.showMessageDialog(this,
                "Sottomissione caricata con successo!\n" +
                "ID Sottomissione: SUB" + (int)(Math.random() * 10000) + "\n" +
                "La tua sottomissione è stata ricevuta e sarà valutata.",
                "Successo",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Qui andrà la logica per salvare nel database
            System.out.println("Sottomissione salvata: " + titolo);
            
            // Chiude la schermata
            dispose();
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
                selectedKeywords.add(keywordOptions[i]);
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
                for (int i = 0; i < keywordOptions.length; i++) {
                    if (keywordOptions[i].equals(cleanKeyword)) {
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
                    selected.add(keywordOptions[i]);
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
}
