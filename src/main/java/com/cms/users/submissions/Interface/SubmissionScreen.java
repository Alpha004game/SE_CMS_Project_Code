package com.cms.users.submissions.Interface;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import com.cms.users.Entity.ArticoloE;
import com.cms.users.submissions.Control.GestioneArticoliControl;

/**
 * <<boundary>>
 * SubmissionScreen - Schermata per la visualizzazione degli articoli sottomessi
 */
public class SubmissionScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    private JButton creaNuovaSottomissioneButton;
    
    // Pannello principale dinamico
    private JPanel contentPanel;
    
    // Dati degli articoli
    private List<String> articoli;
    private LinkedList<ArticoloE> articoliOriginali; // Lista originale per mantenere gli ID
    
    // Informazioni per l'integrazione con il database
    private int idUtente;
    private int idConferenza;
    private GestioneArticoliControl gestioneArticoliControl;
    
    /**
     * Costruttore di default - mostra "Nessun Articolo Trovato"
     */
    public SubmissionScreen() {
        this.articoli = new ArrayList<>();
        this.articoliOriginali = new LinkedList<>();
        this.idUtente = -1;
        this.idConferenza = -1;
        this.gestioneArticoliControl = new GestioneArticoliControl();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con lista di articoli
     */
    public SubmissionScreen(List<String> articoli) {
        this.articoli = articoli != null ? new ArrayList<>(articoli) : new ArrayList<>();
        this.articoliOriginali = new LinkedList<>();
        this.idUtente = -1;
        this.idConferenza = -1;
        this.gestioneArticoliControl = new GestioneArticoliControl();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con informazioni complete per l'integrazione con il database
     */
    public SubmissionScreen(int idUtente, int idConferenza) {
        this.articoli = new ArrayList<>();
        this.articoliOriginali = new LinkedList<>();
        this.idUtente = idUtente;
        this.idConferenza = idConferenza;
        this.gestioneArticoliControl = new GestioneArticoliControl();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con singola stringa (per compatibilità)
     */
    public SubmissionScreen(String listaSottomissioni) {
        this.articoli = new ArrayList<>();
        this.idUtente = -1;
        this.idConferenza = -1;
        this.gestioneArticoliControl = new GestioneArticoliControl();
        
        // Se la stringa contiene articoli, li parsifica
        if (listaSottomissioni != null && !listaSottomissioni.trim().isEmpty()) {
            String[] articoliArray = listaSottomissioni.split("\n");
            for (String articolo : articoliArray) {
                if (!articolo.trim().isEmpty()) {
                    this.articoli.add(articolo.trim());
                }
            }
        }
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Articoli Sottomessi");
        setSize(800, 600);
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
        
        // Bottone crea nuova sottomissione
        creaNuovaSottomissioneButton = new JButton("Crea nuova sottomissione");
        creaNuovaSottomissioneButton.setBackground(Color.ORANGE);
        creaNuovaSottomissioneButton.setForeground(Color.WHITE);
        creaNuovaSottomissioneButton.setFont(new Font("Arial", Font.BOLD, 12));
        creaNuovaSottomissioneButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        creaNuovaSottomissioneButton.setFocusPainted(false);
        creaNuovaSottomissioneButton.setPreferredSize(new Dimension(180, 35));
        
        // Pannello principale inizializzato nel layout
        contentPanel = new JPanel();
    }
    
    /**
     * Configura il layout dell'interfaccia
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header arancione
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
        
        // Pannello principale
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Header con titolo e bottone
        JPanel headerSection = createHeaderSection();
        mainPanel.add(headerSection, BorderLayout.NORTH);
        
        // Content panel dinamico
        updateContentPanel();
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    /**
     * Crea la sezione header con titolo e bottone
     */
    private JPanel createHeaderSection() {
        JPanel headerSection = new JPanel(new BorderLayout());
        headerSection.setBackground(Color.WHITE);
        headerSection.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Titolo
        JLabel titleLabel = new JLabel("Articoli sottomessi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerSection.add(titleLabel, BorderLayout.CENTER);
        
        // Bottone crea nuova sottomissione
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(creaNuovaSottomissioneButton);
        headerSection.add(buttonPanel, BorderLayout.EAST);
        
        return headerSection;
    }
    
    /**
     * Aggiorna il pannello del contenuto in base agli articoli disponibili
     */
    private void updateContentPanel() {
        contentPanel.removeAll();
        contentPanel.setBackground(Color.WHITE);
        
        if (articoli.isEmpty()) {
            // Mostra "Nessun Articolo Trovato"
            createEmptyStatePanel();
        } else {
            // Mostra la lista degli articoli
            createArticleListPanel();
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * Crea il pannello per lo stato vuoto
     */
    private void createEmptyStatePanel() {
        contentPanel.setLayout(new BorderLayout());
        
        JLabel emptyLabel = new JLabel("Nessun Articolo Trovato");
        emptyLabel.setFont(new Font("Arial", Font.BOLD, 18));
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emptyLabel.setForeground(Color.GRAY);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(emptyLabel);
        
        contentPanel.add(centerPanel, BorderLayout.CENTER);
    }
    
    /**
     * Crea il pannello con la lista degli articoli
     */
    private void createArticleListPanel() {
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        for (int i = 0; i < articoli.size(); i++) {
            String articolo = articoli.get(i);
            JPanel articlePanel = createArticlePanel(articolo, i);
            contentPanel.add(articlePanel);
            
            if (i < articoli.size() - 1) {
                contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        // Aggiunge spazio alla fine
        contentPanel.add(Box.createVerticalGlue());
    }
    
    /**
     * Crea un pannello per un singolo articolo
     */
    private JPanel createArticlePanel(String articolo, int index) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        panel.setPreferredSize(new Dimension(700, 60));
        
        // Label dell'articolo
        JLabel articleLabel = new JLabel(articolo);
        articleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        articleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.add(articleLabel, BorderLayout.CENTER);
        
        // Bottone visualizza dettagli
        JButton detailsButton = new JButton("Visualizza dettagli");
        detailsButton.setBackground(Color.ORANGE);
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFont(new Font("Arial", Font.BOLD, 12));
        detailsButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        detailsButton.setFocusPainted(false);
        detailsButton.setPreferredSize(new Dimension(140, 35));
        
        // Event handler per il bottone dettagli
        final int articleIndex = index;
        final String articleTitle = articolo;
        detailsButton.addActionListener(e -> handleVisualizzaDettagli(articleTitle, articleIndex));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(detailsButton);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Configura i gestori degli eventi
     */
    private void setupEventHandlers() {
        homeButton.addActionListener(e -> handleHomeAction());
        notificheButton.addActionListener(e -> handleNotificheAction());
        profiloButton.addActionListener(e -> handleProfiloAction());
        creaNuovaSottomissioneButton.addActionListener(e -> handleCreaNuovaSottomissione());
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
    
    private void handleCreaNuovaSottomissione() {
        try {
            // Segue il sequence diagram per la creazione di una nuova sottomissione
            if (idUtente != -1 && idConferenza != -1) {
                // Usa il control per aprire la schermata di nuova sottomissione con le keywords corrette
                gestioneArticoliControl.creaNuovaSottomissione(idUtente, idConferenza);
                dispose();
            } else {
                // Fallback per quando le informazioni non sono disponibili
                NewSubmissionScreen newSubmissionScreen = new NewSubmissionScreen();
                newSubmissionScreen.create();
                dispose();
            }
        } catch (Exception e) {
            System.err.println("Errore durante l'apertura della schermata nuova sottomissione: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Errore durante l'apertura della schermata nuova sottomissione.\nRiprovare più tardi.", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleVisualizzaDettagli(String articleTitle, int index) {
        // Seguendo il sequence diagram: SubmissionScreen -> GestioneArticoliControl.visualizzaDettagli(idArticolo)
        System.out.println("DEBUG: handleVisualizzaDettagli chiamato per: " + articleTitle + ", indice: " + index);
        System.out.println("DEBUG: articoliOriginali size: " + (articoliOriginali != null ? articoliOriginali.size() : "null"));
        System.out.println("DEBUG: articoli size: " + (articoli != null ? articoli.size() : "null"));
        
        try {
            // Ottiene l'ID dell'articolo dalla lista originale
            if (articoliOriginali != null && index >= 0 && index < articoliOriginali.size()) {
                ArticoloE articolo = articoliOriginali.get(index);
                int idArticolo = articolo.getId();
                
                System.out.println("DEBUG: Trovato articolo con ID: " + idArticolo);
                System.out.println("DEBUG: Chiamata al controller...");
                
                // Chiama il metodo del controller seguendo il sequence diagram
                gestioneArticoliControl.visualizzaDettagli(idArticolo);
                
                System.out.println("DEBUG: Controller chiamato con successo");
                
            } else {
                // Fallback per compatibilità con codice esistente
                System.err.println("ERRORE: Impossibile ottenere l'ID dell'articolo per l'indice: " + index);
                System.err.println("DEBUG: articoliOriginali è null? " + (articoliOriginali == null));
                if (articoliOriginali != null) {
                    System.err.println("DEBUG: Size articoliOriginali: " + articoliOriginali.size());
                }
                
                // Per ora, creiamo un articolo di test per verificare che il resto funzioni
                System.out.println("DEBUG: Creazione di un articolo di test...");
                ArticoloE articoloTest = new ArticoloE();
                articoloTest.setId(999); // ID di test
                articoloTest.setTitolo("Articolo di Test");
                articoloTest.setAbstractText("Questo è un articolo di test per verificare il funzionamento della visualizzazione dettagli.");
                
                gestioneArticoliControl.visualizzaDettagli(999);
                
                // Mostra messaggio di errore all'utente
                JOptionPane.showMessageDialog(this, 
                    "Attenzione: usando dati di test perché l'articolo originale non è disponibile.", 
                    "Debug", 
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("ERRORE durante la visualizzazione dei dettagli: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Errore durante la visualizzazione dei dettagli dell'articolo.\nDettagli: " + e.getMessage(), 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
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
     * Mostra la schermata con una lista di sottomissioni
     */
    public void mostraSubmissionScreen(String listaSottomissioni) {
        // Parsifica la stringa in una lista di articoli
        this.articoli.clear();
        if (listaSottomissioni != null && !listaSottomissioni.trim().isEmpty()) {
            String[] articoliArray = listaSottomissioni.split("\n");
            for (String articolo : articoliArray) {
                if (!articolo.trim().isEmpty()) {
                    this.articoli.add(articolo.trim());
                }
            }
        }
        
        // Aggiorna il contenuto
        updateContentPanel();
        
        // Mostra la schermata
        create();
    }
    
    /**
     * Gestisce il bottone crea nuova sottomissione
     */
    public String creaNuovaSottomissioneButton() {
        handleCreaNuovaSottomissione();
        return "Apertura schermata nuova sottomissione";
    }
    
    /**
     * Gestisce il bottone visualizza dettagli
     */
    public String visualizzaDettagliButton() {
        if (!articoli.isEmpty()) {
            handleVisualizzaDettagli(articoli.get(0), 0);
            return "Dettagli visualizzati per: " + articoli.get(0);
        } else {
            JOptionPane.showMessageDialog(this, "Nessun articolo disponibile per visualizzare i dettagli.");
            return "Nessun articolo disponibile";
        }
    }
    
    // Metodi di utilità
    
    /**
     * Aggiunge un articolo alla lista
     */
    public void addArticle(String articolo) {
        if (articolo != null && !articolo.trim().isEmpty()) {
            articoli.add(articolo.trim());
            updateContentPanel();
        }
    }
    
    /**
     * Rimuove un articolo dalla lista
     */
    public void removeArticle(String articolo) {
        articoli.remove(articolo);
        updateContentPanel();
    }
    
    /**
     * Rimuove un articolo per indice
     */
    public void removeArticle(int index) {
        if (index >= 0 && index < articoli.size()) {
            articoli.remove(index);
            updateContentPanel();
        }
    }
    
    /**
     * Imposta la lista degli articoli
     */
    public void setArticles(List<String> nuoviArticoli) {
        this.articoli = nuoviArticoli != null ? new ArrayList<>(nuoviArticoli) : new ArrayList<>();
        updateContentPanel();
    }
    
    /**
     * Ottiene la lista degli articoli
     */
    public List<String> getArticles() {
        return new ArrayList<>(articoli);
    }
    
    /**
     * Verifica se ci sono articoli
     */
    public boolean hasArticles() {
        return !articoli.isEmpty();
    }
    
    /**
     * Ottiene il numero di articoli
     */
    public int getArticleCount() {
        return articoli.size();
    }
    
    /**
     * Pulisce tutti gli articoli
     */
    public void clearArticles() {
        articoli.clear();
        updateContentPanel();
    }
    
    /**
     * Aggiorna il display
     */
    public void refresh() {
        updateContentPanel();
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
            // Test con articoli reali (ArticoloE) invece di stringhe
            LinkedList<ArticoloE> articoliTest = new LinkedList<>();
            
            // Crea alcuni articoli di test
            ArticoloE articolo1 = new ArticoloE();
            articolo1.setId(1);
            articolo1.setTitolo("Machine Learning Algorithms for Software Testing");
            articolo1.setAbstractText("Questo articolo presenta algoritmi di machine learning per il testing del software...");
            LinkedList<String> keywords1 = new LinkedList<>();
            keywords1.add("Machine Learning");
            keywords1.add("Software Testing");
            keywords1.add("Algorithms");
            articolo1.setKeywords(keywords1);
            LinkedList<String> coAutori1 = new LinkedList<>();
            coAutori1.add("Mario Rossi");
            coAutori1.add("Luigi Verdi");
            articolo1.setCoAutori(coAutori1);
            articolo1.setDichiarazioneOriginalita(true);
            
            ArticoloE articolo2 = new ArticoloE();
            articolo2.setId(2);
            articolo2.setTitolo("Agile Methodologies in Large Scale Projects");
            articolo2.setAbstractText("Studio delle metodologie agili applicato a progetti di larga scala...");
            LinkedList<String> keywords2 = new LinkedList<>();
            keywords2.add("Agile");
            keywords2.add("Software Engineering");
            keywords2.add("Project Management");
            articolo2.setKeywords(keywords2);
            LinkedList<String> coAutori2 = new LinkedList<>();
            coAutori2.add("Anna Bianchi");
            coAutori2.add("Giuseppe Neri");
            articolo2.setCoAutori(coAutori2);
            articolo2.setDichiarazioneOriginalita(true);
            
            ArticoloE articolo3 = new ArticoloE();
            articolo3.setId(3);
            articolo3.setTitolo("Cloud Computing Architecture Patterns");
            articolo3.setAbstractText("Analisi dei pattern architetturali per il cloud computing...");
            LinkedList<String> keywords3 = new LinkedList<>();
            keywords3.add("Cloud Computing");
            keywords3.add("Architecture");
            keywords3.add("Design Patterns");
            articolo3.setKeywords(keywords3);
            LinkedList<String> coAutori3 = new LinkedList<>();
            coAutori3.add("Francesco Blu");
            articolo3.setCoAutori(coAutori3);
            articolo3.setDichiarazioneOriginalita(true);
            
            articoliTest.add(articolo1);
            articoliTest.add(articolo2);
            articoliTest.add(articolo3);
            
            // Crea la schermata e popola con articoli reali
            SubmissionScreen screenWithArticles = new SubmissionScreen(1, 1); // ID utente e conferenza di test
            screenWithArticles.mostraListaSottomissioni(articoliTest);
            screenWithArticles.create();
            
            System.out.println("DEBUG MAIN: SubmissionScreen creata con " + articoliTest.size() + " articoli di test");
            
            // Test senza articoli (commentato per non aprire due finestre)
            // SubmissionScreen emptyScreen = new SubmissionScreen();
            // emptyScreen.create();
        });
    }
    
    /**
     * Mostra la lista delle sottomissioni (ArticoloE) 
     * Questo metodo è chiamato dal GestioneArticoliControl
     */
    public void mostraListaSottomissioni(LinkedList<ArticoloE> listaSottomissioni) {
        // Converte la lista di ArticoloE in lista di stringhe per la visualizzazione
        this.articoli.clear();
        this.articoliOriginali.clear();
        
        if (listaSottomissioni != null && !listaSottomissioni.isEmpty()) {
            // Salva la lista originale per poter accedere agli ID
            this.articoliOriginali.addAll(listaSottomissioni);
            
            for (ArticoloE articolo : listaSottomissioni) {
                String displayText = articolo.getTitolo();
                if (displayText == null || displayText.isEmpty()) {
                    displayText = "Articolo senza titolo (ID: " + articolo.getId() + ")";
                }
                this.articoli.add(displayText);
            }
        }
        
        // Aggiorna il contenuto del pannello
        updateContentPanel();
        
        // Se la finestra non è ancora visibile, la crea
        if (!isDisplayable()) {
            create();
        }
    }
    
    /**
     * Mostra un messaggio di errore
     */
    public void mostraErrore(String messaggioErrore) {
        this.articoli.clear();
        
        // Imposta un messaggio di errore nel content panel
        SwingUtilities.invokeLater(() -> {
            contentPanel.removeAll();
            contentPanel.setLayout(new BorderLayout());
            
            JLabel errorLabel = new JLabel("<html><div style='text-align: center;'>" +
                                          "<h2>Errore</h2>" +
                                          "<p>" + messaggioErrore + "</p>" +
                                          "</div></html>");
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            errorLabel.setForeground(Color.RED);
            
            contentPanel.add(errorLabel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });
        
        // Se la finestra non è ancora visibile, la crea
        if (!isDisplayable()) {
            create();
        }
    }
}
