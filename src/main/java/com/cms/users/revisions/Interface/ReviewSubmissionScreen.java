package com.cms.users.revisions.Interface;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <<boundary>>
 * ReviewSubmissionScreen - Schermata per la revisione di una sottomissione
 */
public class ReviewSubmissionScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    
    // Bottoni azione principali
    private JButton convocaSottoRevisoreButton;
    private JButton rinunciaRevisioneButton;
    
    // Campi del form di revisione
    private JTextArea puntiDiForzaTextArea;
    private JTextArea puntiDiDebolezzaTextArea;
    private JTextArea livelloCompetenzaTextArea;
    private JTextArea commentiAutoriTextArea;
    
    // Bottone invio
    private JButton inviaRevisioneButton;
    
    // Dati della sottomissione
    private String submissionId;
    private String submissionTitle;
    private String reviewerId;
    
    // Attributi per compatibilità
    private String puntiDiForza;
    private String puntiDiDebolezza;
    private String livelloDiCompetenzaDelRevisore;
    private String commentiPerAutori;
    private int valutazione;
    
    /**
     * Costruttore di default
     */
    public ReviewSubmissionScreen() {
        this.submissionId = "SUB" + (int)(Math.random() * 10000);
        this.submissionTitle = "Articolo da Revisionare";
        this.reviewerId = "REV" + (int)(Math.random() * 1000);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con parametri
     */
    public ReviewSubmissionScreen(String submissionId, String submissionTitle, String reviewerId) {
        this.submissionId = submissionId != null ? submissionId : "SUB" + (int)(Math.random() * 10000);
        this.submissionTitle = submissionTitle != null ? submissionTitle : "Articolo da Revisionare";
        this.reviewerId = reviewerId != null ? reviewerId : "REV" + (int)(Math.random() * 1000);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Revisione Sottomissione");
        setSize(800, 700);
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
        
        // Bottoni azione
        convocaSottoRevisoreButton = new JButton("Convoca sotto-revisore");
        convocaSottoRevisoreButton.setBackground(Color.ORANGE);
        convocaSottoRevisoreButton.setForeground(Color.WHITE);
        convocaSottoRevisoreButton.setFont(new Font("Arial", Font.BOLD, 12));
        convocaSottoRevisoreButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        convocaSottoRevisoreButton.setFocusPainted(false);
        convocaSottoRevisoreButton.setPreferredSize(new Dimension(180, 35));
        
        rinunciaRevisioneButton = new JButton("Rinuncia alla revisione");
        rinunciaRevisioneButton.setBackground(Color.ORANGE);
        rinunciaRevisioneButton.setForeground(Color.WHITE);
        rinunciaRevisioneButton.setFont(new Font("Arial", Font.BOLD, 12));
        rinunciaRevisioneButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        rinunciaRevisioneButton.setFocusPainted(false);
        rinunciaRevisioneButton.setPreferredSize(new Dimension(180, 35));
        
        // Campi del form
        initializeFormFields();
        
        // Bottone invio
        inviaRevisioneButton = new JButton("Invia revisione");
        inviaRevisioneButton.setBackground(Color.ORANGE);
        inviaRevisioneButton.setForeground(Color.WHITE);
        inviaRevisioneButton.setFont(new Font("Arial", Font.BOLD, 14));
        inviaRevisioneButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        inviaRevisioneButton.setFocusPainted(false);
        inviaRevisioneButton.setPreferredSize(new Dimension(150, 45));
    }
    
    /**
     * Inizializza i campi del form
     */
    private void initializeFormFields() {
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Color fieldColor = Color.LIGHT_GRAY;
        
        // Punti di forza
        puntiDiForzaTextArea = new JTextArea(3, 40);
        puntiDiForzaTextArea.setBackground(fieldColor);
        puntiDiForzaTextArea.setFont(fieldFont);
        puntiDiForzaTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        puntiDiForzaTextArea.setLineWrap(true);
        puntiDiForzaTextArea.setWrapStyleWord(true);
        
        // Punti di debolezza
        puntiDiDebolezzaTextArea = new JTextArea(3, 40);
        puntiDiDebolezzaTextArea.setBackground(fieldColor);
        puntiDiDebolezzaTextArea.setFont(fieldFont);
        puntiDiDebolezzaTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        puntiDiDebolezzaTextArea.setLineWrap(true);
        puntiDiDebolezzaTextArea.setWrapStyleWord(true);
        
        // Livello di competenza del revisore
        livelloCompetenzaTextArea = new JTextArea(3, 40);
        livelloCompetenzaTextArea.setBackground(fieldColor);
        livelloCompetenzaTextArea.setFont(fieldFont);
        livelloCompetenzaTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        livelloCompetenzaTextArea.setLineWrap(true);
        livelloCompetenzaTextArea.setWrapStyleWord(true);
        
        // Commenti per autori
        commentiAutoriTextArea = new JTextArea(3, 40);
        commentiAutoriTextArea.setBackground(fieldColor);
        commentiAutoriTextArea.setFont(fieldFont);
        commentiAutoriTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        commentiAutoriTextArea.setLineWrap(true);
        commentiAutoriTextArea.setWrapStyleWord(true);
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
        JLabel titleLabel = new JLabel("Compila i campi per la revisione");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(titleLabel);
        
        // Bottoni azione
        mainPanel.add(createActionButtonsPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Sezione punti di forza
        mainPanel.add(createFormSection("Punti di forza:", puntiDiForzaTextArea));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Sezione punti di debolezza
        mainPanel.add(createFormSection("Punti di debolezza:", puntiDiDebolezzaTextArea));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Sezione livello competenza
        mainPanel.add(createFormSection("Livello di competenza del revisore:", livelloCompetenzaTextArea));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Sezione commenti autori
        mainPanel.add(createFormSection("Commenti per autori:", commentiAutoriTextArea));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Bottone invia revisione
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(inviaRevisioneButton);
        mainPanel.add(buttonPanel);
        
        return mainPanel;
    }
    
    /**
     * Crea il pannello con i bottoni di azione
     */
    private JPanel createActionButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panel.setBackground(Color.WHITE);
        
        panel.add(convocaSottoRevisoreButton);
        panel.add(rinunciaRevisioneButton);
        
        return panel;
    }
    
    /**
     * Crea una sezione del form con label e text area
     */
    private JPanel createFormSection(String labelText, JTextArea textArea) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(600, 80));
        scrollPane.setPreferredSize(new Dimension(600, 80));
        panel.add(scrollPane);
        
        return panel;
    }
    
    /**
     * Configura i gestori degli eventi
     */
    private void setupEventHandlers() {
        homeButton.addActionListener(e -> handleHomeAction());
        notificheButton.addActionListener(e -> handleNotificheAction());
        profiloButton.addActionListener(e -> handleProfiloAction());
        
        convocaSottoRevisoreButton.addActionListener(e -> handleConvocaSottoRevisore());
        rinunciaRevisioneButton.addActionListener(e -> handleRinunciaRevisione());
        inviaRevisioneButton.addActionListener(e -> handleInviaRevisione());
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
                                          .newInstance("Revisore", "revisore@cms.com");
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
                                                 .newInstance("Revisore", "revisore@cms.com");
            java.lang.reflect.Method createMethod = userInfoClass.getMethod("create");
            createMethod.invoke(userInfoScreen);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Apertura profilo utente...");
        }
    }
    
    private void handleConvocaSottoRevisore() {
        // Dialog per convocazione sotto-revisore
        String[] options = {"Conferma", "Annulla"};
        int result = JOptionPane.showOptionDialog(this,
            "Vuoi convocare un sotto-revisore per questa sottomissione?\n\n" +
            "ID Sottomissione: " + submissionId + "\n" +
            "Titolo: " + submissionTitle + "\n\n" +
            "Il sotto-revisore riceverà una notifica e potrà fornire un parere aggiuntivo.",
            "Convoca Sotto-Revisore",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        if (result == JOptionPane.YES_OPTION) {
            // Simula la convocazione
            JOptionPane.showMessageDialog(this,
                "Sotto-revisore convocato con successo!\n\n" +
                "Un revisore esperto è stato notificato e contattato.\n" +
                "Riceverai una notifica quando sarà disponibile il suo parere.",
                "Convocazione Completata",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Qui andrà la logica per convocare effettivamente un sotto-revisore
            System.out.println("Sotto-revisore convocato per sottomissione: " + submissionId);
        }
    }
    
    private void handleRinunciaRevisione() {
        // Dialog di conferma rinuncia
        String[] options = {"Rinuncia", "Annulla"};
        int result = JOptionPane.showOptionDialog(this,
            "Sei sicuro di voler rinunciare alla revisione?\n\n" +
            "ID Sottomissione: " + submissionId + "\n" +
            "Titolo: " + submissionTitle + "\n\n" +
            "ATTENZIONE: Questa azione non può essere annullata.\n" +
            "La sottomissione sarà riassegnata ad un altro revisore.",
            "Rinuncia alla Revisione",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[1]);
        
        if (result == JOptionPane.YES_OPTION) {
            // Simula la rinuncia
            JOptionPane.showMessageDialog(this,
                "Rinuncia registrata.\n\n" +
                "La sottomissione è stata rimossa dal tuo elenco\n" +
                "e sarà riassegnata ad un altro revisore disponibile.",
                "Rinuncia Completata",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Qui andrà la logica per gestire la rinuncia
            System.out.println("Rinuncia alla revisione: " + submissionId + " da revisore: " + reviewerId);
            
            // Chiude la schermata
            dispose();
        }
    }
    
    private void handleInviaRevisione() {
        // Validazione campi
        if (!validateForm()) {
            return;
        }
        
        // Raccoglie i dati
        collectFormData();
        
        // Conferma invio
        StringBuilder summary = new StringBuilder();
        summary.append("Conferma invio revisione:\n\n");
        summary.append("ID Sottomissione: ").append(submissionId).append("\n");
        summary.append("Revisore: ").append(reviewerId).append("\n\n");
        summary.append("Punti di forza: ").append(puntiDiForza.substring(0, Math.min(30, puntiDiForza.length()))).append("...\n");
        summary.append("Punti di debolezza: ").append(puntiDiDebolezza.substring(0, Math.min(30, puntiDiDebolezza.length()))).append("...\n");
        summary.append("Livello competenza: ").append(livelloDiCompetenzaDelRevisore.substring(0, Math.min(30, livelloDiCompetenzaDelRevisore.length()))).append("...\n");
        summary.append("Commenti autori: ").append(commentiPerAutori.substring(0, Math.min(30, commentiPerAutori.length()))).append("...\n");
        
        int result = JOptionPane.showConfirmDialog(this,
            summary.toString(),
            "Conferma Invio Revisione",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            // Simula l'invio della revisione
            JOptionPane.showMessageDialog(this,
                "Revisione inviata con successo!\n\n" +
                "ID Revisione: REV" + (int)(Math.random() * 10000) + "\n" +
                "La tua revisione è stata ricevuta e sarà processata.\n" +
                "Gli autori riceveranno notifica dei feedback forniti.",
                "Invio Completato",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Qui andrà la logica per salvare la revisione nel database
            System.out.println("Revisione inviata per sottomissione: " + submissionId);
            
            // Chiude la schermata
            dispose();
        }
    }
    
    /**
     * Valida i campi del form
     */
    private boolean validateForm() {
        if (puntiDiForzaTextArea.getText().trim().isEmpty()) {
            showError("I punti di forza sono obbligatori!", puntiDiForzaTextArea);
            return false;
        }
        
        if (puntiDiDebolezzaTextArea.getText().trim().isEmpty()) {
            showError("I punti di debolezza sono obbligatori!", puntiDiDebolezzaTextArea);
            return false;
        }
        
        if (livelloCompetenzaTextArea.getText().trim().isEmpty()) {
            showError("Il livello di competenza del revisore è obbligatorio!", livelloCompetenzaTextArea);
            return false;
        }
        
        if (commentiAutoriTextArea.getText().trim().isEmpty()) {
            showError("I commenti per gli autori sono obbligatori!", commentiAutoriTextArea);
            return false;
        }
        
        return true;
    }
    
    /**
     * Raccoglie i dati dal form
     */
    private void collectFormData() {
        this.puntiDiForza = puntiDiForzaTextArea.getText().trim();
        this.puntiDiDebolezza = puntiDiDebolezzaTextArea.getText().trim();
        this.livelloDiCompetenzaDelRevisore = livelloCompetenzaTextArea.getText().trim();
        this.commentiPerAutori = commentiAutoriTextArea.getText().trim();
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
            puntiDiForzaTextArea.requestFocus();
        });
    }
    
    /**
     * Imposta le informazioni della revisione
     */
    public void setInfoReview(int puntiDiForza, int puntiDiDebolezza, int livelloDiCompetenzaDelRevisore, 
                             int commentiPerAutori, int valutazione) {
        // Nota: i parametri sono int nel metodo originale, ma li convertiamo in testo
        if (puntiDiForzaTextArea != null) {
            puntiDiForzaTextArea.setText("Punteggio punti di forza: " + puntiDiForza);
        }
        if (puntiDiDebolezzaTextArea != null) {
            puntiDiDebolezzaTextArea.setText("Punteggio punti di debolezza: " + puntiDiDebolezza);
        }
        if (livelloCompetenzaTextArea != null) {
            livelloCompetenzaTextArea.setText("Livello competenza: " + livelloDiCompetenzaDelRevisore);
        }
        if (commentiAutoriTextArea != null) {
            commentiAutoriTextArea.setText("Punteggio commenti: " + commentiPerAutori);
        }
        this.valutazione = valutazione;
    }
    
    /**
     * Gestisce il bottone invia revisione
     */
    public void inviaRevisioneButton() {
        handleInviaRevisione();
    }
    
    /**
     * Ottiene le informazioni della revisione
     */
    public String getInfoReview() {
        collectFormData();
        return "Punti di forza: " + puntiDiForza + "\n" +
               "Punti di debolezza: " + puntiDiDebolezza + "\n" +
               "Livello competenza: " + livelloDiCompetenzaDelRevisore + "\n" +
               "Commenti autori: " + commentiPerAutori + "\n" +
               "Valutazione: " + valutazione;
    }
    
    /**
     * Gestisce il bottone convoca sotto-revisore
     */
    public void convocaSottoRevisoreButton() {
        handleConvocaSottoRevisore();
    }
    
    /**
     * Gestisce il bottone rinuncia articolo
     */
    public void rinunciaArticoloButton() {
        handleRinunciaRevisione();
    }
    
    // Metodi aggiuntivi per la gestione dei dati
    
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
     * Imposta il titolo della sottomissione
     */
    public void setSubmissionTitle(String submissionTitle) {
        this.submissionTitle = submissionTitle;
    }
    
    /**
     * Ottiene il titolo della sottomissione
     */
    public String getSubmissionTitle() {
        return submissionTitle;
    }
    
    /**
     * Imposta l'ID del revisore
     */
    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }
    
    /**
     * Ottiene l'ID del revisore
     */
    public String getReviewerId() {
        return reviewerId;
    }
    
    /**
     * Imposta la valutazione
     */
    public void setValutazione(int valutazione) {
        this.valutazione = valutazione;
    }
    
    /**
     * Ottiene la valutazione
     */
    public int getValutazione() {
        return valutazione;
    }
    
    /**
     * Verifica se il form è valido
     */
    public boolean isFormValid() {
        return validateForm();
    }
    
    /**
     * Pulisce tutti i campi del form
     */
    public void clearForm() {
        if (puntiDiForzaTextArea != null) puntiDiForzaTextArea.setText("");
        if (puntiDiDebolezzaTextArea != null) puntiDiDebolezzaTextArea.setText("");
        if (livelloCompetenzaTextArea != null) livelloCompetenzaTextArea.setText("");
        if (commentiAutoriTextArea != null) commentiAutoriTextArea.setText("");
        this.valutazione = 0;
    }
    
    /**
     * Imposta i dati dei campi direttamente
     */
    public void setReviewData(String puntiDiForza, String puntiDiDebolezza, String livelloCompetenza, String commentiAutori) {
        if (puntiDiForzaTextArea != null && puntiDiForza != null) {
            puntiDiForzaTextArea.setText(puntiDiForza);
        }
        if (puntiDiDebolezzaTextArea != null && puntiDiDebolezza != null) {
            puntiDiDebolezzaTextArea.setText(puntiDiDebolezza);
        }
        if (livelloCompetenzaTextArea != null && livelloCompetenza != null) {
            livelloCompetenzaTextArea.setText(livelloCompetenza);
        }
        if (commentiAutoriTextArea != null && commentiAutori != null) {
            commentiAutoriTextArea.setText(commentiAutori);
        }
    }
    
    /**
     * Verifica se ci sono dati non salvati
     */
    public boolean hasUnsavedData() {
        return !puntiDiForzaTextArea.getText().trim().isEmpty() ||
               !puntiDiDebolezzaTextArea.getText().trim().isEmpty() ||
               !livelloCompetenzaTextArea.getText().trim().isEmpty() ||
               !commentiAutoriTextArea.getText().trim().isEmpty();
    }
    
    /**
     * Metodo main per testing standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test con dati di esempio
            ReviewSubmissionScreen screen = new ReviewSubmissionScreen(
                "SUB12345",
                "Machine Learning for Automated Software Testing",
                "REV789"
            );
            screen.create();
        });
    }
}
