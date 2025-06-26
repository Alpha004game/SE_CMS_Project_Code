package com.cms.users.conference.Interface;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * <<boundary>>
 * ConferenceCreationScreen - Schermata per la creazione di una nuova conferenza
 */
public class ConferenceCreationScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    private JButton salvaButton;
    
    // Campi del form
    private JTextField titoloField;
    private JTextField annoEdizioneField;
    private JTextArea abstractField;
    private JTextField dataInizioField;
    private JTextField dataFineField;
    private JTextField deadlineSottomissioneField;
    private JTextField deadlineRitiroField;
    private JTextField deadlineRevisioniField;
    private JTextField deadlineVersioneFinaleField;
    private JTextField deadlineVersionePubblicazioneField;
    private JTextField luogoField;
    private JTextField temiPrincipaliField;
    private JTextField keywordsField;
    private JTextField numeroArticoliPrevistiField;
    private JTextField numeroMinimoRevisoriField;
    private JTextField numeroMassimoRevisoriField;
    private JTextField tassoAccettazioneField;
    
    // Attributi originali
    private String titolo;
    private String annoEdizione;
    private String Abstract;
    private String dataInizio;
    private String dataFine;
    private String deadlineSottomissione;
    private String deadlineNotifica;
    private String deadlineRevisioni;
    private String deadlineVersioneFinale;
    private String deadlineVersionePubblicazione;
    private String luogo;
    private String numRevisorPerArticolo;
    private String numeroArticoliPrevisti;
    private String tassoAccettazione;
    private String keywords;
    private int id;
    
    /**
     * Costruttore
     */
    public ConferenceCreationScreen() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Creazione Nuova Conferenza");
        setSize(900, 700);
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
        
        // Bottone Salva
        salvaButton = new JButton("Salva");
        salvaButton.setBackground(Color.ORANGE);
        salvaButton.setForeground(Color.WHITE);
        salvaButton.setFont(new Font("Arial", Font.BOLD, 14));
        salvaButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        salvaButton.setFocusPainted(false);
        salvaButton.setPreferredSize(new Dimension(100, 40));
        
        // Inizializza i campi del form
        initializeFormFields();
    }
    
    /**
     * Inizializza tutti i campi del form
     */
    private void initializeFormFields() {
        Font fieldFont = new Font("Arial", Font.PLAIN, 12);
        Color fieldColor = Color.LIGHT_GRAY;
        
        // Campi di testo semplici
        titoloField = createTextField(fieldFont, fieldColor);
        annoEdizioneField = createTextField(fieldFont, fieldColor);
        luogoField = createTextField(fieldFont, fieldColor);
        temiPrincipaliField = createTextField(fieldFont, fieldColor);
        keywordsField = createTextField(fieldFont, fieldColor);
        numeroArticoliPrevistiField = createTextField(fieldFont, fieldColor);
        numeroMinimoRevisoriField = createTextField(fieldFont, fieldColor);
        numeroMassimoRevisoriField = createTextField(fieldFont, fieldColor);
        tassoAccettazioneField = createTextField(fieldFont, fieldColor);
        
        // Campo Abstract (più grande)
        abstractField = new JTextArea(3, 30);
        abstractField.setBackground(fieldColor);
        abstractField.setFont(fieldFont);
        abstractField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        abstractField.setLineWrap(true);
        abstractField.setWrapStyleWord(true);
        
        // Campi data (con placeholder per formato)
        dataInizioField = createTextField(fieldFont, fieldColor);
        dataInizioField.setText("dd/MM/yyyy");
        dataInizioField.setForeground(Color.GRAY);
        
        dataFineField = createTextField(fieldFont, fieldColor);
        dataFineField.setText("dd/MM/yyyy");
        dataFineField.setForeground(Color.GRAY);
        
        deadlineSottomissioneField = createTextField(fieldFont, fieldColor);
        deadlineSottomissioneField.setText("dd/MM/yyyy");
        deadlineSottomissioneField.setForeground(Color.GRAY);
        
        deadlineRitiroField = createTextField(fieldFont, fieldColor);
        deadlineRitiroField.setText("dd/MM/yyyy");
        deadlineRitiroField.setForeground(Color.GRAY);
        
        deadlineRevisioniField = createTextField(fieldFont, fieldColor);
        deadlineRevisioniField.setText("dd/MM/yyyy");
        deadlineRevisioniField.setForeground(Color.GRAY);
        
        deadlineVersioneFinaleField = createTextField(fieldFont, fieldColor);
        deadlineVersioneFinaleField.setText("dd/MM/yyyy");
        deadlineVersioneFinaleField.setForeground(Color.GRAY);
        
        deadlineVersionePubblicazioneField = createTextField(fieldFont, fieldColor);
        deadlineVersionePubblicazioneField.setText("dd/MM/yyyy");
        deadlineVersionePubblicazioneField.setForeground(Color.GRAY);
        
        // Aggiungi focus listeners per gestire i placeholder
        setupDateFieldPlaceholders();
    }
    
    /**
     * Crea un campo di testo con stile uniforme
     */
    private JTextField createTextField(Font font, Color backgroundColor) {
        JTextField field = new JTextField();
        field.setBackground(backgroundColor);
        field.setFont(font);
        field.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        field.setPreferredSize(new Dimension(150, 30));
        return field;
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
     * Crea il pannello principale del form
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Titolo
        JLabel titleLabel = new JLabel("Inserisci informazioni della nuova conferenza");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form fields
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Bottone Salva
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(salvaButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Crea il pannello con tutti i campi del form
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Prima riga: Titolo e Anno edizione
        addFormField(formPanel, gbc, "Titolo della conferenza:", titoloField, 0, row, 1);
        addFormField(formPanel, gbc, "Anno edizione:", annoEdizioneField, 2, row, 1);
        row++;
        
        // Seconda riga: Abstract (spanning 2 columns)
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Abstract della conferenza:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        JScrollPane abstractScroll = new JScrollPane(abstractField);
        abstractScroll.setPreferredSize(new Dimension(400, 80));
        formPanel.add(abstractScroll, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        row++;
        
        // Terza riga: Date inizio e fine
        addFormField(formPanel, gbc, "Data di inizio:", dataInizioField, 0, row, 1);
        addFormField(formPanel, gbc, "Data di fine:", dataFineField, 2, row, 1);
        row++;
        
        // Quarta riga: Deadline sottomissione e ritiro
        addFormField(formPanel, gbc, "Data limite per la sottomissione degli articoli:", deadlineSottomissioneField, 0, row, 1);
        addFormField(formPanel, gbc, "Data limite per il ritiro delle sottomissioni:", deadlineRitiroField, 2, row, 1);
        row++;
        
        // Quinta riga: Deadline revisioni e versione finale
        addFormField(formPanel, gbc, "Data limite per la revisione:", deadlineRevisioniField, 0, row, 1);
        addFormField(formPanel, gbc, "Data limite per l'invio della versione finale:", deadlineVersioneFinaleField, 2, row, 1);
        row++;
        
        // Sesta riga: Deadline versione pubblicazione (spanning)
        addFormField(formPanel, gbc, "Data limite per l'invio della versione da pubblicare:", deadlineVersionePubblicazioneField, 0, row, 3);
        row++;
        
        // Settima riga: Luogo e Temi principali
        addFormField(formPanel, gbc, "Luogo:", luogoField, 0, row, 1);
        addFormField(formPanel, gbc, "Temi principali:", temiPrincipaliField, 2, row, 1);
        row++;
        
        // Ottava riga: Keywords e Numero articoli previsti
        addFormField(formPanel, gbc, "Keywords:", keywordsField, 0, row, 1);
        addFormField(formPanel, gbc, "Numero di articoli previsti:", numeroArticoliPrevistiField, 2, row, 1);
        row++;
        
        // Nona riga: Numero revisori min e max
        addFormField(formPanel, gbc, "Numero minimo di revisori per articolo:", numeroMinimoRevisoriField, 0, row, 1);
        addFormField(formPanel, gbc, "Numero massimo di articoli per revisore:", numeroMassimoRevisoriField, 2, row, 1);
        row++;
        
        // Decima riga: Tasso accettazione
        addFormField(formPanel, gbc, "Tasso di accettazione previsto:", tassoAccettazioneField, 0, row, 1);
        
        return formPanel;
    }
    
    /**
     * Aggiunge un campo al form con la sua label
     */
    private void addFormField(JPanel parent, GridBagConstraints gbc, String labelText, 
                             JComponent field, int x, int y, int width) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        parent.add(new JLabel(labelText), gbc);
        
        gbc.gridx = x + 1;
        gbc.gridwidth = width;
        parent.add(field, gbc);
        gbc.gridwidth = 1;
    }
    
    /**
     * Configura i gestori degli eventi
     */
    private void setupEventHandlers() {
        homeButton.addActionListener(e -> handleHomeAction());
        notificheButton.addActionListener(e -> handleNotificheAction());
        profiloButton.addActionListener(e -> handleProfiloAction());
        salvaButton.addActionListener(e -> saveButton());
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
     * Crea una nuova conferenza con i parametri specificati
     */
    public String creaConferenza(String titolo, String abstractText, String anno, String dataInizio, String dataFine,
                                String dataSottomissione, String dataRitiro, String dataRevisioni,
                                String dataVersioneFinale, String dataVersioneDelPubblicare, String luogo,
                                String temi, String keyword, String numeroRevisori, String numeroArticoliMassimo,
                                String numeroArticoliPrevisti, String tassoAccettazione) {
        
        // Imposta i valori nei campi
        titoloField.setText(titolo);
        abstractField.setText(abstractText);
        annoEdizioneField.setText(anno);
        luogoField.setText(luogo);
        temiPrincipaliField.setText(temi);
        keywordsField.setText(keyword);
        numeroMinimoRevisoriField.setText(numeroRevisori);
        numeroMassimoRevisoriField.setText(numeroArticoliMassimo);
        numeroArticoliPrevistiField.setText(numeroArticoliPrevisti);
        tassoAccettazioneField.setText(tassoAccettazione);
        
        // Qui andrà la logica per salvare la conferenza
        return "Conferenza creata: " + titolo;
    }
    
    /**
     * Gestisce il salvataggio della conferenza
     */
    public void saveButton() {
        // Validazione dei campi obbligatori
        if (!validateForm()) {
            return;
        }
        
        // Raccoglie tutti i dati dai campi
        collectFormData();
        
        // Conferma salvataggio
        int result = JOptionPane.showConfirmDialog(this,
            "Sei sicuro di voler creare la conferenza?\n" +
            "Titolo: " + titolo + "\n" +
            "Anno: " + annoEdizione,
            "Conferma Creazione",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            // Simula il salvataggio
            JOptionPane.showMessageDialog(this,
                "Conferenza creata con successo!\n" +
                "ID: " + generateConferenceId(),
                "Successo",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Qui andrà la logica per salvare nel database
            System.out.println("Conferenza salvata: " + titolo);
            
            // Torna alla home
            handleHomeAction();
        }
    }
    
    /**
     * Valida i campi del form
     */
    private boolean validateForm() {
        // Campi obbligatori
        if (titoloField.getText().trim().isEmpty()) {
            showError("Il titolo della conferenza è obbligatorio!", titoloField);
            return false;
        }
        
        if (annoEdizioneField.getText().trim().isEmpty()) {
            showError("L'anno di edizione è obbligatorio!", annoEdizioneField);
            return false;
        }
        
        if (abstractField.getText().trim().isEmpty()) {
            showError("L'abstract della conferenza è obbligatorio!", abstractField);
            return false;
        }
        
        if (dataInizioField.getText().trim().isEmpty() || dataInizioField.getText().equals("dd/MM/yyyy")) {
            showError("La data di inizio è obbligatoria!");
            return false;
        }
        
        if (dataFineField.getText().trim().isEmpty() || dataFineField.getText().equals("dd/MM/yyyy")) {
            showError("La data di fine è obbligatoria!");
            return false;
        }
        
        // Validazione formato date
        if (!isValidDate(dataInizioField.getText().trim())) {
            showError("La data di inizio deve essere nel formato dd/MM/yyyy!");
            return false;
        }
        
        if (!isValidDate(dataFineField.getText().trim())) {
            showError("La data di fine deve essere nel formato dd/MM/yyyy!");
            return false;
        }
        
        return true;
    }
    
    /**
     * Raccoglie i dati dai campi del form
     */
    private void collectFormData() {
        this.titolo = titoloField.getText().trim();
        this.annoEdizione = annoEdizioneField.getText().trim();
        this.Abstract = abstractField.getText().trim();
        this.luogo = luogoField.getText().trim();
        this.keywords = keywordsField.getText().trim();
        this.numeroArticoliPrevisti = numeroArticoliPrevistiField.getText().trim();
        this.tassoAccettazione = tassoAccettazioneField.getText().trim();
        
        // Raccoglie le date
        if (!dataInizioField.getText().equals("dd/MM/yyyy")) {
            this.dataInizio = dataInizioField.getText().trim();
        }
        if (!dataFineField.getText().equals("dd/MM/yyyy")) {
            this.dataFine = dataFineField.getText().trim();
        }
        
        // Raccoglie le altre deadline se compilate
        if (!deadlineSottomissioneField.getText().equals("dd/MM/yyyy")) {
            this.deadlineSottomissione = deadlineSottomissioneField.getText().trim();
        }
        if (!deadlineRevisioniField.getText().equals("dd/MM/yyyy")) {
            this.deadlineRevisioni = deadlineRevisioniField.getText().trim();
        }
        if (!deadlineVersioneFinaleField.getText().equals("dd/MM/yyyy")) {
            this.deadlineVersioneFinale = deadlineVersioneFinaleField.getText().trim();
        }
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
    
    /**
     * Genera un ID univoco per la conferenza
     */
    private int generateConferenceId() {
        this.id = (int) (Math.random() * 10000) + 1000;
        return this.id;
    }
    
    // Getter per i campi
    public String getTitolo() { return titolo; }
    public String getAnnoEdizione() { return annoEdizione; }
    public String getAbstract() { return Abstract; }
    public String getDataInizio() { return dataInizio; }
    public String getDataFine() { return dataFine; }
    public String getLuogo() { return luogo; }
    public String getKeywords() { return keywords; }
    public int getId() { return id; }
    
    /**
     * Pulisce tutti i campi del form
     */
    public void clearForm() {
        titoloField.setText("");
        annoEdizioneField.setText("");
        abstractField.setText("");
        luogoField.setText("");
        temiPrincipaliField.setText("");
        keywordsField.setText("");
        numeroArticoliPrevistiField.setText("");
        numeroMinimoRevisoriField.setText("");
        numeroMassimoRevisoriField.setText("");
        tassoAccettazioneField.setText("");
        
        dataInizioField.setText("dd/MM/yyyy");
        dataInizioField.setForeground(Color.GRAY);
        dataFineField.setText("dd/MM/yyyy");
        dataFineField.setForeground(Color.GRAY);
        deadlineSottomissioneField.setText("dd/MM/yyyy");
        deadlineSottomissioneField.setForeground(Color.GRAY);
        deadlineRitiroField.setText("dd/MM/yyyy");
        deadlineRitiroField.setForeground(Color.GRAY);
        deadlineRevisioniField.setText("dd/MM/yyyy");
        deadlineRevisioniField.setForeground(Color.GRAY);
        deadlineVersioneFinaleField.setText("dd/MM/yyyy");
        deadlineVersioneFinaleField.setForeground(Color.GRAY);
        deadlineVersionePubblicazioneField.setText("dd/MM/yyyy");
        deadlineVersionePubblicazioneField.setForeground(Color.GRAY);
    }
    
    /**
     * Metodo main per testing standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConferenceCreationScreen screen = new ConferenceCreationScreen();
            screen.create();
        });
    }
    
    /**
     * Configura i placeholder per i campi data
     */
    private void setupDateFieldPlaceholders() {
        JTextField[] dateFields = {
            dataInizioField, dataFineField, deadlineSottomissioneField,
            deadlineRitiroField, deadlineRevisioniField, 
            deadlineVersioneFinaleField, deadlineVersionePubblicazioneField
        };
        
        for (JTextField field : dateFields) {
            field.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (field.getText().equals("dd/MM/yyyy")) {
                        field.setText("");
                        field.setForeground(Color.BLACK);
                    }
                }
                
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (field.getText().isEmpty()) {
                        field.setText("dd/MM/yyyy");
                        field.setForeground(Color.GRAY);
                    }
                }
            });
        }
    }
    
    /**
     * Valida il formato della data
     */
    private boolean isValidDate(String date) {
        if (date == null || date.equals("dd/MM/yyyy")) {
            return false;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
