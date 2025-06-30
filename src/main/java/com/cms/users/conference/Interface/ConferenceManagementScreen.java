package com.cms.users.conference.Interface;

import javax.swing.*;
import java.awt.*;
import com.cms.users.Entity.ConferenzaE;
import com.cms.users.conference.Control.ConferenceControl;
import java.time.format.DateTimeFormatter;

/**
 * <<boundary>>
 * ConferenceManagementScreen - Schermata per la gestione di una conferenza esistente
 */
public class ConferenceManagementScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    
    // Campi informativi della conferenza (solo lettura)
    private JLabel titoloValueLabel;
    private JLabel annoEdizioneValueLabel;
    private JTextArea abstractValueArea;
    private JLabel dataInizioValueLabel;
    private JLabel dataFineValueLabel;
    private JLabel deadlineSottomissioneValueLabel;
    private JLabel deadlineRitiroValueLabel;
    private JLabel deadlineRevisioniValueLabel;
    private JLabel deadlineVersioneFinaleValueLabel;
    private JLabel deadlineVersionePubblicazioneValueLabel;
    private JLabel luogoValueLabel;
    private JLabel keywordsValueLabel;
    private JLabel numeroArticoliPrevistiValueLabel;
    private JLabel numeroMinimoRevisoriValueLabel;
    private JLabel tassoAccettazioneValueLabel;
    
    // Bottoni di azione
    private JButton invitaCoChairButton;
    private JButton aggiungiRevisoriButton;
    private JButton rimuoviRevisoreButton;
    private JButton assegnaRevisoreButton;
    private JButton inviaComunicazioneButton;
    private JButton ottieniLogButton;
    private JButton visualizzaStatoButton;
    private JButton rimuoviRevisoreArticoloButton;
    
    // Dati della conferenza
    private int id;
    private String conferenceId;
    private String titolo;
    private String annoEdizione;
    private String abstractConferenza;
    private String dataInizio;
    private String dataFine;
    private String deadlineSottomissione;
    private String deadlineRitiro;
    private String deadlineRevisioni;
    private String deadlineVersioneFinale;
    private String deadlineVersionePubblicazione;
    private String luogo;
    private String keywords;
    private String numeroArticoliPrevisti;
    private String numeroMinimoRevisori;
    private String tassoAccettazione;
    
    /**
     * Costruttore di default
     */
    public ConferenceManagementScreen() {
        // Dati di esempio
        this.id = 1;
        this.conferenceId = "CONF001";
        this.titolo = "International Conference on Software Engineering";
        this.annoEdizione = "2025";
        this.abstractConferenza = "Conferenza internazionale sui più recenti sviluppi nell'ingegneria del software, con focus su metodologie agili, architetture software e intelligenza artificiale.";
        this.dataInizio = "15/09/2025";
        this.dataFine = "17/09/2025";
        this.deadlineSottomissione = "15/06/2025";
        this.deadlineRitiro = "30/06/2025";
        this.deadlineRevisioni = "15/07/2025";
        this.deadlineVersioneFinale = "01/08/2025";
        this.deadlineVersionePubblicazione = "15/08/2025";
        this.luogo = "Milano, Italia";
        this.keywords = "software, engineering, artificial intelligence, agile";
        this.numeroArticoliPrevisti = "50";
        this.numeroMinimoRevisori = "3";
        this.tassoAccettazione = "30%";
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con dati della conferenza
     */
    public ConferenceManagementScreen(String conferenceId, String titolo, String annoEdizione) {
        this.conferenceId = conferenceId != null ? conferenceId : "CONF001";
        this.titolo = titolo != null ? titolo : "Conferenza";
        this.annoEdizione = annoEdizione != null ? annoEdizione : "2025";
        
        // Carica altri dati (simulato)
        loadConferenceData();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Simula il caricamento dei dati della conferenza
     */
    private void loadConferenceData() {
        // Qui andrà la logica per caricare i dati dal database
        this.abstractConferenza = "Abstract della conferenza " + titolo;
        this.dataInizio = "01/01/2025";
        this.dataFine = "03/01/2025";
        this.luogo = "Sede della conferenza";
        this.keywords = "keywords";
        this.numeroArticoliPrevisti = "10";
        this.numeroMinimoRevisori = "2";
        this.tassoAccettazione = "25%";
    }
    
    /**
     * Carica i dati della conferenza da un oggetto ConferenzaE
     * Utilizzato dal ConferenceControl per popolare la schermata con dati reali dal database
     */
    public void loadConferenceData(ConferenzaE conferenza) {
        if (conferenza == null) {
            return;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // Aggiorna tutti i campi con i dati della conferenza
        this.id = conferenza.getId();
        this.conferenceId = String.valueOf(conferenza.getId());
        this.titolo = conferenza.getTitolo();
        this.annoEdizione = String.valueOf(conferenza.getAnnoEdizione());
        this.abstractConferenza = conferenza.getAbstractText() != null ? conferenza.getAbstractText() : "";
        
        // Converte le date da LocalDate a String
        this.dataInizio = conferenza.getDataInizio() != null ? conferenza.getDataInizio().format(formatter) : "";
        this.dataFine = conferenza.getDataFine() != null ? conferenza.getDataFine().format(formatter) : "";
        this.deadlineSottomissione = conferenza.getDeadlineSottomissione() != null ? conferenza.getDeadlineSottomissione().format(formatter) : "";
        this.deadlineRitiro = conferenza.getDeadlineRitiro() != null ? conferenza.getDeadlineRitiro().format(formatter) : "";
        this.deadlineRevisioni = conferenza.getDeadlineRevisioni() != null ? conferenza.getDeadlineRevisioni().format(formatter) : "";
        this.deadlineVersioneFinale = conferenza.getDeadlineVersioneFinale() != null ? conferenza.getDeadlineVersioneFinale().format(formatter) : "";
        this.deadlineVersionePubblicazione = conferenza.getDeadlinePubblicazione() != null ? conferenza.getDeadlinePubblicazione().format(formatter) : "";
        
        this.luogo = conferenza.getLuogo() != null ? conferenza.getLuogo() : "";
        this.numeroArticoliPrevisti = String.valueOf(conferenza.getNumeroArticoliPrevisti());
        this.numeroMinimoRevisori = String.valueOf(conferenza.getNumeroRevisoriPerArticolo());
        
        // Unisce le keywords in una stringa
        if (conferenza.getKeywords() != null && !conferenza.getKeywords().isEmpty()) {
            this.keywords = String.join(", ", conferenza.getKeywords());
        } else {
            this.keywords = "";
        }
        
        // Aggiorna il titolo della finestra
        setTitle("CMS - Gestione Conferenza: " + this.titolo);
        
        // Aggiorna i componenti UI se sono già stati inizializzati
        if (titoloValueLabel != null) {
            updateUIComponents();
        }
    }
    
    /**
     * Aggiorna i componenti UI con i dati caricati
     */
    private void updateUIComponents() {
        titoloValueLabel.setText(titolo);
        annoEdizioneValueLabel.setText(annoEdizione);
        abstractValueArea.setText(abstractConferenza);
        dataInizioValueLabel.setText(dataInizio);
        dataFineValueLabel.setText(dataFine);
        deadlineSottomissioneValueLabel.setText(deadlineSottomissione);
        deadlineRitiroValueLabel.setText(deadlineRitiro);
        deadlineRevisioniValueLabel.setText(deadlineRevisioni);
        deadlineVersioneFinaleValueLabel.setText(deadlineVersioneFinale);
        deadlineVersionePubblicazioneValueLabel.setText(deadlineVersionePubblicazione);
        luogoValueLabel.setText(luogo);
        keywordsValueLabel.setText(keywords);
        numeroArticoliPrevistiValueLabel.setText(numeroArticoliPrevisti);
        numeroMinimoRevisoriValueLabel.setText(numeroMinimoRevisori);
        tassoAccettazioneValueLabel.setText(tassoAccettazione);
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Gestione Conferenza: " + titolo);
        setSize(1000, 800);
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
        
        // Inizializza le label informative
        initializeInformationLabels();
        
        // Inizializza i bottoni di azione
        initializeActionButtons();
    }
    
    /**
     * Inizializza le label con le informazioni della conferenza
     */
    private void initializeInformationLabels() {
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Color backgroundColor = Color.LIGHT_GRAY;
        
        // Crea le label con i valori
        titoloValueLabel = createInfoLabel(titolo, labelFont, backgroundColor);
        annoEdizioneValueLabel = createInfoLabel(annoEdizione, labelFont, backgroundColor);
        dataInizioValueLabel = createInfoLabel(dataInizio, labelFont, backgroundColor);
        dataFineValueLabel = createInfoLabel(dataFine, labelFont, backgroundColor);
        deadlineSottomissioneValueLabel = createInfoLabel(deadlineSottomissione, labelFont, backgroundColor);
        deadlineRitiroValueLabel = createInfoLabel(deadlineRitiro, labelFont, backgroundColor);
        deadlineRevisioniValueLabel = createInfoLabel(deadlineRevisioni, labelFont, backgroundColor);
        deadlineVersioneFinaleValueLabel = createInfoLabel(deadlineVersioneFinale, labelFont, backgroundColor);
        deadlineVersionePubblicazioneValueLabel = createInfoLabel(deadlineVersionePubblicazione, labelFont, backgroundColor);
        luogoValueLabel = createInfoLabel(luogo, labelFont, backgroundColor);
        keywordsValueLabel = createInfoLabel(keywords, labelFont, backgroundColor);
        numeroArticoliPrevistiValueLabel = createInfoLabel(numeroArticoliPrevisti, labelFont, backgroundColor);
        numeroMinimoRevisoriValueLabel = createInfoLabel(numeroMinimoRevisori, labelFont, backgroundColor);
        tassoAccettazioneValueLabel = createInfoLabel(tassoAccettazione, labelFont, backgroundColor);
        
        // Area di testo per l'abstract
        abstractValueArea = new JTextArea(abstractConferenza, 3, 30);
        abstractValueArea.setBackground(backgroundColor);
        abstractValueArea.setFont(labelFont);
        abstractValueArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        abstractValueArea.setLineWrap(true);
        abstractValueArea.setWrapStyleWord(true);
        abstractValueArea.setEditable(false);
    }
    
    /**
     * Crea una label informativa con stile uniforme
     */
    private JLabel createInfoLabel(String text, Font font, Color backgroundColor) {
        JLabel label = new JLabel(text != null ? text : "");
        label.setBackground(backgroundColor);
        label.setFont(font);
        label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(200, 30));
        return label;
    }
    
    /**
     * Inizializza i bottoni di azione
     */
    private void initializeActionButtons() {
        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        Color buttonColor = Color.ORANGE;
        Color textColor = Color.WHITE;
        
        invitaCoChairButton = createActionButton("Invita Co-Chair", buttonFont, buttonColor, textColor);
        aggiungiRevisoriButton = createActionButton("Aggiungi revisori", buttonFont, buttonColor, textColor);
        rimuoviRevisoreButton = createActionButton("Rimuovi revisore", buttonFont, buttonColor, textColor);
        assegnaRevisoreButton = createActionButton("Assegna revisore ad articolo", buttonFont, buttonColor, textColor);
        inviaComunicazioneButton = createActionButton("Invia comunicazione", buttonFont, buttonColor, textColor);
        ottieniLogButton = createActionButton("Ottieni log", buttonFont, buttonColor, textColor);
        visualizzaStatoButton = createActionButton("Visualizza stato revisione attuale", buttonFont, buttonColor, textColor);
        rimuoviRevisoreArticoloButton = createActionButton("Rimuovi revisore da articolo", buttonFont, buttonColor, textColor);
    }
    
    /**
     * Crea un bottone di azione con stile uniforme
     */
    private JButton createActionButton(String text, Font font, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFont(font);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 35));
        return button;
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
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Titolo
        JLabel titleLabel = new JLabel("Informazioni conferenza");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Pannello centrale con informazioni e bottoni
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        
        // Informazioni conferenza
        JPanel infoPanel = createInformationPanel();
        centerPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Bottoni di azione
        JPanel actionPanel = createActionPanel();
        centerPanel.add(actionPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    /**
     * Crea il pannello con le informazioni della conferenza
     */
    private JPanel createInformationPanel() {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Prima riga: Titolo e Anno edizione
        addInfoField(infoPanel, gbc, "Titolo della conferenza:", titoloValueLabel, 0, row, 1);
        addInfoField(infoPanel, gbc, "Anno edizione:", annoEdizioneValueLabel, 2, row, 1);
        row++;
        
        // Seconda riga: Abstract (spanning)
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(new JLabel("Abstract della conferenza:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        JScrollPane abstractScroll = new JScrollPane(abstractValueArea);
        abstractScroll.setPreferredSize(new Dimension(500, 80));
        infoPanel.add(abstractScroll, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        row++;
        
        // Terza riga: Date inizio e fine
        addInfoField(infoPanel, gbc, "Data di inizio:", dataInizioValueLabel, 0, row, 1);
        addInfoField(infoPanel, gbc, "Data di fine:", dataFineValueLabel, 2, row, 1);
        row++;
        
        // Quarta riga: Deadline sottomissione e ritiro
        addInfoField(infoPanel, gbc, "Data limite per la sottomissione degli articoli:", deadlineSottomissioneValueLabel, 0, row, 1);
        addInfoField(infoPanel, gbc, "Data limite per il ritiro delle sottomissioni:", deadlineRitiroValueLabel, 2, row, 1);
        row++;
        
        // Quinta riga: Deadline revisioni e versione finale
        addInfoField(infoPanel, gbc, "Data limite per la revisione:", deadlineRevisioniValueLabel, 0, row, 1);
        addInfoField(infoPanel, gbc, "Data limite per l'invio della versione finale:", deadlineVersioneFinaleValueLabel, 2, row, 1);
        row++;
        
        // Sesta riga: Deadline versione pubblicazione (spanning)
        addInfoField(infoPanel, gbc, "Data limite per l'invio della versione da pubblicare:", deadlineVersionePubblicazioneValueLabel, 0, row, 3);
        row++;
        
        // Settima riga: Luogo (rimuove temi principali)
        addInfoField(infoPanel, gbc, "Luogo:", luogoValueLabel, 0, row, 1);
        row++;
        
        // Ottava riga: Keywords e Numero articoli previsti
        addInfoField(infoPanel, gbc, "Keywords:", keywordsValueLabel, 0, row, 1);
        addInfoField(infoPanel, gbc, "Numero di articoli previsti:", numeroArticoliPrevistiValueLabel, 2, row, 1);
        row++;
        
        // Nona riga: Solo numero minimo revisori (rimuove numero massimo)
        addInfoField(infoPanel, gbc, "Numero minimo di revisori per articolo:", numeroMinimoRevisoriValueLabel, 0, row, 1);
        row++;
        
        // Decima riga: Tasso accettazione
        addInfoField(infoPanel, gbc, "Tasso di accettazione previsto:", tassoAccettazioneValueLabel, 0, row, 1);
        
        return infoPanel;
    }
    
    /**
     * Aggiunge un campo informativo al pannello
     */
    private void addInfoField(JPanel parent, GridBagConstraints gbc, String labelText, 
                             JComponent field, int x, int y, int width) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        parent.add(label, gbc);
        
        gbc.gridx = x + 1;
        gbc.gridwidth = width;
        parent.add(field, gbc);
        gbc.gridwidth = 1;
    }
    
    /**
     * Crea il pannello con i bottoni di azione
     */
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Prima riga di bottoni
        actionPanel.add(invitaCoChairButton);
        actionPanel.add(aggiungiRevisoriButton);
        actionPanel.add(rimuoviRevisoreButton);
        actionPanel.add(assegnaRevisoreButton);
        
        // Seconda riga di bottoni
        actionPanel.add(inviaComunicazioneButton);
        actionPanel.add(ottieniLogButton);
        actionPanel.add(visualizzaStatoButton);
        actionPanel.add(rimuoviRevisoreArticoloButton);
        
        return actionPanel;
    }
    
    /**
     * Configura i gestori degli eventi
     */
    private void setupEventHandlers() {
        homeButton.addActionListener(e -> handleHomeAction());
        notificheButton.addActionListener(e -> handleNotificheAction());
        profiloButton.addActionListener(e -> handleProfiloAction());
        
        // Gestori per i bottoni di azione
        invitaCoChairButton.addActionListener(e -> invitaCoChairButton());
        aggiungiRevisoriButton.addActionListener(e -> addReviewerButton());
        rimuoviRevisoreButton.addActionListener(e -> rimuoviReviewerButton());
        assegnaRevisoreButton.addActionListener(e -> assegnaReviewerButton());
        inviaComunicazioneButton.addActionListener(e -> sendCommunicationButton());
        ottieniLogButton.addActionListener(e -> getLogButton());
        visualizzaStatoButton.addActionListener(e -> visualizzaStatoRevisionButton());
        rimuoviRevisoreArticoloButton.addActionListener(e -> rimuoviArticoloReviewerButton());
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
        });
    }
    
    /**
     * Invita un Co-Chair alla conferenza
     * Implementa il sequence diagram: ConferenceManagementScreen -> ConferenceControl
     */
    public void invitaCoChairButton() {
        // Segue il sequence diagram: crea ConferenceControl e chiama invitaCoChair
        ConferenceControl conferenceControl = new ConferenceControl();
        conferenceControl.invitaCoChair(this.id);
    }
    
    /**
     * Ottiene i dati della conferenza
     */
    public String getDatiConferenza() {
        return "Conferenza: " + titolo + " (" + annoEdizione + ")\n" +
               "Luogo: " + luogo + "\n" +
               "Date: " + dataInizio + " - " + dataFine + "\n" +
               "Abstract: " + abstractConferenza;
    }
    
    /**
     * Aggiunge revisori alla conferenza
     * Implementa il sequence diagram: ConferenceManagementScreen -> ConferenceControl
     */
    public void addReviewerButton() {
        // Segue il sequence diagram: crea ConferenceControl e chiama addReviewer
        ConferenceControl conferenceControl = new ConferenceControl();
        conferenceControl.addReviewer(this.id);
    }
    
    /**
     * Mostra la schermata di gestione conferenza
     */
    public void mostraConferenceManagementScreen() {
        create();
    }
    
    /**
     * Rimuove un revisore dalla conferenza
     */
    public void rimuoviReviewerButton() {
        // Segue il sequence diagram: delega al ConferenceControl
        ConferenceControl conferenceControl = new ConferenceControl();
        conferenceControl.rimuoviRevisore(this.id);
    }
    
    /**
     * Assegna un revisore ad un articolo
     * Segue il sequence diagram: ConferenceManagementScreen -> ConferenceControl -> ReviewerScreen
     */
    public void assegnaReviewerButton() {
        try {
            // Delega al ConferenceControl per aprire la ReviewerScreen
            ConferenceControl conferenceControl = new ConferenceControl();
            
            // Imposta la conferenza attuale nel control
            if (id > 0) {
                conferenceControl.apriAssegnazioneRevisori(id);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Errore: ID conferenza non valido", 
                    "Errore", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            System.err.println("Errore durante l'apertura della schermata di assegnazione: " + e.getMessage());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, 
                "Errore durante l'apertura della schermata di assegnazione: " + e.getMessage(), 
                "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Rimuove un revisore da un articolo
     */
    public void rimuoviArticoloReviewerButton() {
        String articolo = JOptionPane.showInputDialog(this, 
            "Inserisci l'ID dell'articolo:", 
            "Rimuovi Revisore da Articolo", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (articolo != null && !articolo.trim().isEmpty()) {
            String revisore = JOptionPane.showInputDialog(this, 
                "Inserisci l'email del revisore da rimuovere dall'articolo " + articolo + ":", 
                "Rimuovi Revisore", 
                JOptionPane.QUESTION_MESSAGE);
            
            if (revisore != null && !revisore.trim().isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Sei sicuro di voler rimuovere " + revisore + " dall'articolo " + articolo + "?", 
                    "Conferma Rimozione", 
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, 
                        "Revisore " + revisore + " rimosso dall'articolo " + articolo, 
                        "Rimozione Completata", 
                        JOptionPane.INFORMATION_MESSAGE);
                    // Qui andrà la logica per rimuovere il revisore dall'articolo
                }
            }
        }
    }
    
    /**
     * Visualizza lo stato delle revisioni
     */
    public void visualizzaStatoRevisionButton() {
        // Delega al ConferenceControl per ottenere dati dinamici
        ConferenceControl conferenceControl = new ConferenceControl();
        conferenceControl.visualizzaStatoSottomissioni(this.id);
    }
    
    /**
     * Invia una comunicazione
     * Segue il sequence diagram: ConferenceManagementScreen -> ConferenceControl
     */
    public void sendCommunicationButton() {
        // Segue il sequence diagram: crea ConferenceControl e chiama sendCommunication
        ConferenceControl conferenceControl = new ConferenceControl();
        conferenceControl.sendCommunication(this.id);
    }
    
    /**
     * Ottiene il log della conferenza
     */
    public void getLogButton() {
        // Segue il sequence diagram: delega al ConferenceControl
        ConferenceControl conferenceControl = new ConferenceControl();
        conferenceControl.getLog(this.id);
    }
    
    // Metodi di utilità e getter
    
    /**
     * Aggiorna i dati della conferenza
     */
    public void updateConferenceData(String titolo, String annoEdizione, String abstractConferenza) {
        this.titolo = titolo;
        this.annoEdizione = annoEdizione;
        this.abstractConferenza = abstractConferenza;
        
        // Aggiorna i componenti dell'interfaccia
        if (titoloValueLabel != null) {
            titoloValueLabel.setText(titolo);
        }
        if (annoEdizioneValueLabel != null) {
            annoEdizioneValueLabel.setText(annoEdizione);
        }
        if (abstractValueArea != null) {
            abstractValueArea.setText(abstractConferenza);
        }
        
        setTitle("CMS - Gestione Conferenza: " + titolo);
    }
    
    /**
     * Ottiene l'ID della conferenza
     */
    public String getConferenceId() {
        return conferenceId;
    }
    
    /**
     * Ottiene il titolo della conferenza
     */
    public String getTitolo() {
        return titolo;
    }
    
    /**
     * Ottiene l'anno di edizione
     */
    public String getAnnoEdizione() {
        return annoEdizione;
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
            ConferenceManagementScreen screen = new ConferenceManagementScreen();
            screen.create();
        });
    }
}
