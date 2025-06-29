package com.cms.users.conference.Interface;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import com.cms.App;
import com.cms.users.conference.Control.CreaConferenzaControl;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;

/**
 * <<boundary>>
 * ConferenceCreationScreen - Schermata per la creazione di una nuova conferenza
 */
public class ConferenceCreationScreen extends JFrame {
    
    // Riferimento al control (seguendo il sequence diagram)
    private CreaConferenzaControl creaConferenzaControl;
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    private JButton salvaButton;
    
    // Campi del form
    private JTextField titoloField;
    private JTextField annoEdizioneField;
    private JTextArea abstractField;
    private JSpinner dataInizioField;
    private JSpinner dataFineField;
    private JSpinner deadlineSottomissioneField;
    private JSpinner deadlineRitiroField;
    private JSpinner deadlineRevisioniField;
    private JSpinner deadlineVersioneFinaleField;
    private JSpinner deadlineVersionePubblicazioneField;
    private JTextField luogoField;
    private JTextField keywordsField;
    private JTextField numeroArticoliPrevistiField;
    private JTextField numeroMinimoRevisoriField;
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
     * Costruttore con riferimento al control (seguendo il sequence diagram)
     */
    public ConferenceCreationScreen(CreaConferenzaControl creaConferenzaControl) {
        this.creaConferenzaControl = creaConferenzaControl;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore di default (per compatibilità)
     */
    public ConferenceCreationScreen() {
        this.creaConferenzaControl = null;
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
        setSize(1200, 900);
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
        keywordsField = createTextField(fieldFont, fieldColor);
        numeroArticoliPrevistiField = createTextField(fieldFont, fieldColor);
        numeroMinimoRevisoriField = createTextField(fieldFont, fieldColor);
        tassoAccettazioneField = createTextField(fieldFont, fieldColor);
        
        // Campo Abstract (più grande)
        abstractField = new JTextArea(3, 30);
        abstractField.setBackground(fieldColor);
        abstractField.setFont(fieldFont);
        abstractField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        abstractField.setLineWrap(true);
        abstractField.setWrapStyleWord(true);
        
        // Campi data (con JSpinner)
        dataInizioField = createDateField();
        dataFineField = createDateField();
        deadlineSottomissioneField = createDateField();
        deadlineRitiroField = createDateField();
        deadlineRevisioniField = createDateField();
        deadlineVersioneFinaleField = createDateField();
        deadlineVersionePubblicazioneField = createDateField();
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
     * Crea un campo data con JSpinner
     */
    private JSpinner createDateField() {
        // Crea un modello per la data con data corrente come default
        Date today = new Date();
        SpinnerDateModel dateModel = new SpinnerDateModel(today, null, null, Calendar.DAY_OF_MONTH);
        
        JSpinner dateSpinner = new JSpinner(dateModel);
        
        // Imposta il formato della data
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        
        // Imposta dimensioni e stile
        dateSpinner.setPreferredSize(new Dimension(150, 30));
        dateSpinner.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        
        // Imposta il background del campo di testo interno
        JFormattedTextField textField = ((JSpinner.DateEditor) dateSpinner.getEditor()).getTextField();
        textField.setBackground(Color.LIGHT_GRAY);
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
        
        return dateSpinner;
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
        
        // Settima riga: Luogo e Keywords
        addFormField(formPanel, gbc, "Luogo:", luogoField, 0, row, 1);
        addFormField(formPanel, gbc, "Keywords:", keywordsField, 2, row, 1);
        row++;
        
        // Ottava riga: Numero articoli previsti e Numero minimo revisori
        addFormField(formPanel, gbc, "Numero di articoli previsti:", numeroArticoliPrevistiField, 0, row, 1);
        addFormField(formPanel, gbc, "Numero minimo di revisori per articolo:", numeroMinimoRevisoriField, 2, row, 1);
        row++;
        
        // Nona riga: Tasso accettazione
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
        keywordsField.setText(keyword);
        numeroMinimoRevisoriField.setText(numeroRevisori);
        numeroArticoliPrevistiField.setText(numeroArticoliPrevisti);
        tassoAccettazioneField.setText(tassoAccettazione);
        
        // Qui andrà la logica per salvare la conferenza
        
        return "Conferenza creata: " + titolo;
    }
    
    /**
     * Gestisce il salvataggio della conferenza
     */
    /**
     * Gestisce il salvataggio della conferenza seguendo il sequence diagram
     */
    public void saveButton() {
        // Validazione dei campi obbligatori
        if (!validateForm()) {
            return;
        }
        
        // Verifica che il control sia disponibile
        if (creaConferenzaControl == null) {
            showError("Errore: sistema non inizializzato correttamente!");
            return;
        }
        
        // Raccoglie tutti i dati dai campi per passarli al control
        String titolo = titoloField.getText().trim();
        String abstractText = abstractField.getText().trim();
        String annoEdizione = annoEdizioneField.getText().trim();
        String luogo = luogoField.getText().trim();
        String keywords = keywordsField.getText().trim();
        String numeroRevisori = numeroMinimoRevisoriField.getText().trim();
        String numeroArticoliPrevisti = numeroArticoliPrevistiField.getText().trim();
        String tassoAccettazione = tassoAccettazioneField.getText().trim();
        
        // Raccoglie le date dai JSpinner
        Date dataInizio = (Date) dataInizioField.getValue();
        Date dataFine = (Date) dataFineField.getValue();
        Date deadlineSottomissione = (Date) deadlineSottomissioneField.getValue();
        Date deadlineRitiro = (Date) deadlineRitiroField.getValue();
        Date deadlineRevisioni = (Date) deadlineRevisioniField.getValue();
        Date deadlineVersioneFinale = (Date) deadlineVersioneFinaleField.getValue();
        Date deadlineVersionePubblicazione = (Date) deadlineVersionePubblicazioneField.getValue();
        
        // Conferma salvataggio
        int result = JOptionPane.showConfirmDialog(this,
            "Sei sicuro di voler creare la conferenza?\n" +
            "Titolo: " + titolo + "\n" +
            "Anno: " + annoEdizione,
            "Conferma Creazione",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            // Seguendo il sequence diagram: ConferenceCreationScreen -> CreaConferenzaControl -> DBMSBoundary
            boolean successo = creaConferenzaControl.salvaConferenza(
                titolo, abstractText, annoEdizione, 
                dataInizio, dataFine, deadlineSottomissione, deadlineRitiro, 
                deadlineRevisioni, deadlineVersioneFinale, deadlineVersionePubblicazione,
                luogo, keywords, numeroRevisori, numeroArticoliPrevisti, tassoAccettazione
            );
            
            if (successo) {
                JOptionPane.showMessageDialog(this,
                    "Conferenza creata con successo!\n" +
                    "Titolo: " + titolo,
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Torna alla home
                handleHomeAction();
            } else {
                showError("Errore durante il salvataggio della conferenza!");
            }
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
        
        // Validazione date (sempre valide con JSpinner)
        if (!isValidDateSpinner(dataInizioField)) {
            showError("La data di inizio non è valida!");
            return false;
        }
        
        if (!isValidDateSpinner(dataFineField)) {
            showError("La data di fine non è valida!");
            return false;
        }
        
        // Verifica che la data di fine sia successiva alla data di inizio
        Date dataInizio = (Date) dataInizioField.getValue();
        Date dataFine = (Date) dataFineField.getValue();
        
        if (dataFine.before(dataInizio)) {
            showError("La data di fine deve essere successiva alla data di inizio!");
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
        
        // Raccoglie le date usando JSpinner
        this.dataInizio = getDateFromSpinner(dataInizioField);
        this.dataFine = getDateFromSpinner(dataFineField);
        
        // Raccoglie le altre deadline se compilate
        this.deadlineSottomissione = getDateFromSpinner(deadlineSottomissioneField);
        this.deadlineRevisioni = getDateFromSpinner(deadlineRevisioniField);
        this.deadlineVersioneFinale = getDateFromSpinner(deadlineVersioneFinaleField);
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
        keywordsField.setText("");
        numeroArticoliPrevistiField.setText("");
        numeroMinimoRevisoriField.setText("");
        tassoAccettazioneField.setText("");
        
        // Reset date spinners to today's date
        Date today = new Date();
        dataInizioField.setValue(today);
        dataFineField.setValue(today);
        deadlineSottomissioneField.setValue(today);
        deadlineRitiroField.setValue(today);
        deadlineRevisioniField.setValue(today);
        deadlineVersioneFinaleField.setValue(today);
        deadlineVersionePubblicazioneField.setValue(today);
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
    
    /**
     * Ottiene la data formattata da un JSpinner
     */
    private String getDateFromSpinner(JSpinner dateSpinner) {
        Date date = (Date) dateSpinner.getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
    
    /**
     * Imposta una data in un JSpinner da una stringa
     */
    private void setDateToSpinner(JSpinner dateSpinner, String dateString) {
        if (dateString != null && !dateString.trim().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = sdf.parse(dateString);
                dateSpinner.setValue(date);
            } catch (ParseException e) {
                // Se il parsing fallisce, mantieni la data corrente
                System.err.println("Errore nel parsing della data: " + dateString);
            }
        }
    }
    
    /**
     * Controlla se una data JSpinner è valida
     */
    private boolean isValidDateSpinner(JSpinner dateSpinner) {
        try {
            Date date = (Date) dateSpinner.getValue();
            return date != null;
        } catch (Exception e) {
            return false;
        }
    }
}
