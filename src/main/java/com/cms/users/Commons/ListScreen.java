package com.cms.users.Commons;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.cms.users.Entity.ArticoloE;

/**
 * <<boundary>>
 * ListScreen - Schermata lista generica che cambia in base al ruolo e alla funzionalità
 */
public class ListScreen extends JFrame {
    
    // Enumerazioni per ruoli e funzionalità
    public enum UserRole {
        CHAIR, REVISORE, AUTORE, SOTTOREVISORE
    }
    
    public enum ChairFunction {
        REMOVE_REVIEWER,    // Rimuovi revisore da articolo
        REVIEW_ARTICLE,     // Revisiona articolo chair
        VIEW_SUBMISSIONS    // Visualizza sottomissioni
    }
    
    public enum RevisoreFunction {
        ARTICLES_TO_REVIEW  // Articoli da revisionare
    }
    
    public enum SottoRevisoreFunction {
        ARTICLES_TO_REVIEW  // Articoli da revisionare (sottorevisore)
    }
    
    public enum EditoreFunction {
        ACCEPTED_ARTICLES   // Articoli accettati per la pubblicazione
    }
    
    // Classe per dati articolo-revisore (per funzione REMOVE_REVIEWER)
    public static class ArticleReviewerData {
        public String articleTitle;
        public String articleAbstract;
        public List<String> reviewers;
        
        public ArticleReviewerData(String title, String abstractText) {
            this.articleTitle = title;
            this.articleAbstract = abstractText;
            this.reviewers = new ArrayList<>();
        }
        
        public void addReviewer(String reviewer) {
            this.reviewers.add(reviewer);
        }
    }
    
    // Classe per dati articolo semplice (per funzione REVIEW_ARTICLE)
    public static class ArticleData {
        public String id;
        public String title;
        public String status;
        
        public ArticleData(String id, String title, String status) {
            this.id = id;
            this.title = title;
            this.status = status;
        }
    }
    
    // Classe per dati articolo revisore (per revisori)
    public static class RevisoreArticleData {
        public String id;
        public String title;
        public String status;
        public boolean hasDelegatedReview;
        public int totalReviews;
        
        public RevisoreArticleData(String id, String title, String status) {
            this.id = id;
            this.title = title;
            this.status = status;
            this.hasDelegatedReview = Math.random() > 0.5; // Simula presenza di revisioni delegate
            this.totalReviews = (int)(Math.random() * 5) + 1; // 1-5 revisioni
        }
    }
    
    // Classe per dati articolo editore (per editori)
    public static class EditoreArticleData {
        public String id;
        public String title;
        public String authors;
        public String filePath;
        public boolean notificationSent;
        
        public EditoreArticleData(String id, String title, String authors) {
            this.id = id;
            this.title = title;
            this.authors = authors;
            this.filePath = "path/to/" + id + ".pdf";
            this.notificationSent = Math.random() > 0.6; // Simula se l'avviso è già stato inviato
        }
    }
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    private JButton scaricaTuttiButton; // Solo per Editore
    
    // Pannello centrale
    private JPanel centerPanel;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JLabel emptyLabel;
    
    // Dati della schermata
    private UserRole userRole;
    private ChairFunction chairFunction;
    private RevisoreFunction revisoreFunction;
    private SottoRevisoreFunction sottoRevisoreFunction;
    private EditoreFunction editoreFunction;
    private String screenTitle;
    private List<ArticleReviewerData> articleReviewerData;
    private List<ArticleData> articleData;
    private List<RevisoreArticleData> revisoreArticleData;
    private List<EditoreArticleData> editoreArticleData;
    private boolean hasData;
    
    /**
     * Costruttore di default
     */
    public ListScreen() {
        this(UserRole.CHAIR, ChairFunction.REMOVE_REVIEWER);
    }
    
    /**
     * Costruttore con ruolo Chair e funzione
     */
    public ListScreen(UserRole userRole, ChairFunction chairFunction) {
        this.userRole = userRole != null ? userRole : UserRole.CHAIR;
        this.chairFunction = chairFunction != null ? chairFunction : ChairFunction.REMOVE_REVIEWER;
        this.revisoreFunction = null;
        this.sottoRevisoreFunction = null;
        this.editoreFunction = null;
        
        setupScreenTitle();
        initializeData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateDisplay();
    }
    
    /**
     * Costruttore con ruolo Revisore e funzione
     */
    public ListScreen(UserRole userRole, RevisoreFunction revisoreFunction) {
        this.userRole = userRole != null ? userRole : UserRole.REVISORE;
        this.revisoreFunction = revisoreFunction != null ? revisoreFunction : RevisoreFunction.ARTICLES_TO_REVIEW;
        this.chairFunction = null;
        this.sottoRevisoreFunction = null;
        this.editoreFunction = null;
        
        setupScreenTitle();
        initializeData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateDisplay();
    }
    
    /**
     * Costruttore con ruolo SottoRevisore e funzione
     */
    public ListScreen(UserRole userRole, SottoRevisoreFunction sottoRevisoreFunction) {
        this.userRole = userRole != null ? userRole : UserRole.SOTTOREVISORE;
        this.sottoRevisoreFunction = sottoRevisoreFunction != null ? sottoRevisoreFunction : SottoRevisoreFunction.ARTICLES_TO_REVIEW;
        this.chairFunction = null;
        this.revisoreFunction = null;
        this.editoreFunction = null;
        
        setupScreenTitle();
        initializeData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateDisplay();
    }
    
    /**
     * Costruttore con ruolo Editore e funzione
     */
    public ListScreen(UserRole userRole, EditoreFunction editoreFunction) {
        this.userRole = userRole != null ? userRole : UserRole.AUTORE; // Uso AUTORE come placeholder per Editore
        this.editoreFunction = editoreFunction != null ? editoreFunction : EditoreFunction.ACCEPTED_ARTICLES;
        this.chairFunction = null;
        this.revisoreFunction = null;
        this.sottoRevisoreFunction = null;
        
        setupScreenTitle();
        initializeData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateDisplay();
    }
    
    /**
     * Imposta il titolo in base alla funzione
     */
    private void setupScreenTitle() {
        if (userRole == UserRole.CHAIR && chairFunction != null) {
            switch (chairFunction) {
                case REMOVE_REVIEWER:
                    screenTitle = "Selezione il revisore che vuoi rimuovere";
                    break;
                case REVIEW_ARTICLE:
                    screenTitle = "Selezione articolo da visionare";
                    break;
                default:
                    screenTitle = "Lista elementi";
                    break;
            }
        } else if (userRole == UserRole.REVISORE && revisoreFunction != null) {
            switch (revisoreFunction) {
                case ARTICLES_TO_REVIEW:
                    screenTitle = "Articoli da revisionare";
                    break;
                default:
                    screenTitle = "Lista articoli";
                    break;
            }
        } else if (userRole == UserRole.SOTTOREVISORE && sottoRevisoreFunction != null) {
            switch (sottoRevisoreFunction) {
                case ARTICLES_TO_REVIEW:
                    screenTitle = "Articoli da revisionare";
                    break;
                default:
                    screenTitle = "Lista articoli";
                    break;
            }
        } else if (editoreFunction != null) { // Editore
            switch (editoreFunction) {
                case ACCEPTED_ARTICLES:
                    screenTitle = "Articoli accettati per la pubblicazione";
                    break;
                default:
                    screenTitle = "Lista elementi";
            }
        } else {
            screenTitle = "Lista elementi";
        }
    }
    
    /**
     * Inizializza i dati di esempio
     */
    private void initializeData() {
        articleReviewerData = new ArrayList<>();
        articleData = new ArrayList<>();
        revisoreArticleData = new ArrayList<>();
        editoreArticleData = new ArrayList<>();
        
        // Simula se ci sono dati o meno (per testare stato vuoto)
        hasData = Math.random() > 0.3; // 70% probabilità di avere dati
        
        if (hasData) {
            if (userRole == UserRole.CHAIR && chairFunction != null) {
                switch (chairFunction) {
                    case REMOVE_REVIEWER:
                        initializeRemoveReviewerData();
                        break;
                    case REVIEW_ARTICLE:
                        initializeReviewArticleData();
                        break;
                    case VIEW_SUBMISSIONS:
                        // Da implementare se necessario
                        break;
                }
            } else if (userRole == UserRole.REVISORE && revisoreFunction != null) {
                switch (revisoreFunction) {
                    case ARTICLES_TO_REVIEW:
                        initializeRevisoreArticleData();
                        break;
                }
            } else if (userRole == UserRole.SOTTOREVISORE && sottoRevisoreFunction != null) {
                switch (sottoRevisoreFunction) {
                    case ARTICLES_TO_REVIEW:
                        initializeRevisoreArticleData(); // Usa gli stessi dati del revisore
                        break;
                }
            } else if (editoreFunction != null) { // Editore
                switch (editoreFunction) {
                    case ACCEPTED_ARTICLES:
                        initializeEditoreArticleData();
                        break;
                }
            }
        }
    }
    
    /**
     * Inizializza dati per la funzione "Rimuovi revisore"
     */
    private void initializeRemoveReviewerData() {
        ArticleReviewerData art1 = new ArticleReviewerData("Articolo", "Abstract");
        art1.addReviewer("Prof. Rossi");
        art1.addReviewer("Dr. Verdi");
        art1.addReviewer("Prof.ssa Bianchi");
        articleReviewerData.add(art1);
        
        ArticleReviewerData art2 = new ArticleReviewerData("Articolo", "Abstract");
        art2.addReviewer("Prof. Neri");
        art2.addReviewer("Dr. Gialli");
        art2.addReviewer("Prof. Rossi");
        articleReviewerData.add(art2);
        
        ArticleReviewerData art3 = new ArticleReviewerData("Articolo", "Abstract");
        art3.addReviewer("Dr. Blu");
        art3.addReviewer("Prof.ssa Viola");
        art3.addReviewer("Prof. Arancio");
        art3.addReviewer("Dr. Verde");
        articleReviewerData.add(art3);
        
        ArticleReviewerData art4 = new ArticleReviewerData("Articolo", "Abstract");
        art4.addReviewer("Prof. Marrone");
        art4.addReviewer("Dr. Rosa");
        art4.addReviewer("Prof. Grigio");
        articleReviewerData.add(art4);
    }
    
    /**
     * Inizializza dati per la funzione "Revisiona articolo"
     */
    private void initializeReviewArticleData() {
        articleData.add(new ArticleData("ART001", "Articolo", "In revisione"));
        articleData.add(new ArticleData("ART002", "Articolo", "Completato"));
        articleData.add(new ArticleData("ART003", "Articolo", "Da iniziare"));
        articleData.add(new ArticleData("ART004", "Articolo", "In corso"));
        articleData.add(new ArticleData("ART005", "Articolo", "Completato"));
    }
    
    /**
     * Inizializza dati per la funzione "Articoli da revisionare" (Revisore)
     */
    private void initializeRevisoreArticleData() {
        revisoreArticleData.add(new RevisoreArticleData("ART001", "Articolo", "Da revisionare"));
        revisoreArticleData.add(new RevisoreArticleData("ART002", "Articolo", "In corso"));
        revisoreArticleData.add(new RevisoreArticleData("ART003", "Articolo", "Completato"));
        revisoreArticleData.add(new RevisoreArticleData("ART004", "Articolo", "Da revisionare"));
        revisoreArticleData.add(new RevisoreArticleData("ART005", "Articolo", "In corso"));
    }
    
    /**
     * Inizializza dati per la funzione "Articoli accettati" (Editore)
     */
    private void initializeEditoreArticleData() {
        editoreArticleData.add(new EditoreArticleData("ART001", "Articolo", "Autore A, Autore B"));
        editoreArticleData.add(new EditoreArticleData("ART002", "Articolo", "Autore C"));
        editoreArticleData.add(new EditoreArticleData("ART003", "Articolo", "Autore D, Autore E"));
        editoreArticleData.add(new EditoreArticleData("ART004", "Articolo", "Autore F"));
        editoreArticleData.add(new EditoreArticleData("ART005", "Articolo", "Autore G, Autore H"));
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Lista");
        setSize(900, 600);
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
        
        notificheButton = new JButton("🔔");
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
        
        // Bottone "Scarica tutti" per Editore
        scaricaTuttiButton = new JButton("Scarica tutti");
        scaricaTuttiButton.setBackground(Color.ORANGE);
        scaricaTuttiButton.setForeground(Color.WHITE);
        scaricaTuttiButton.setFont(new Font("Arial", Font.BOLD, 12));
        scaricaTuttiButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        scaricaTuttiButton.setFocusPainted(false);
        
        // Label per stato vuoto
        String emptyMessage = "Nessun utente trovato";
        if (userRole == UserRole.SOTTOREVISORE) {
            emptyMessage = "Nessun articolo da revisionare trovato";
        }
        emptyLabel = new JLabel(emptyMessage);
        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emptyLabel.setVerticalAlignment(SwingConstants.CENTER);
        emptyLabel.setBackground(Color.LIGHT_GRAY);
        emptyLabel.setOpaque(true);
        emptyLabel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
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
        
        // Pannello superiore con titolo e bottone "Scarica tutti" (se Editore)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        // Titolo
        JLabel titleLabel = new JLabel(screenTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Aggiungi bottone "Scarica tutti" per Editore
        if (editoreFunction != null && editoreFunction == EditoreFunction.ACCEPTED_ARTICLES) {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.add(scaricaTuttiButton);
            topPanel.add(buttonPanel, BorderLayout.EAST);
        }
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Pannello centrale (tabella o messaggio vuoto)
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    /**
     * Aggiorna la visualizzazione in base ai dati
     */
    private void updateDisplay() {
        centerPanel.removeAll();
        
        if (!hasData) {
            // Mostra messaggio "Nessun utente trovato"
            centerPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            // Mostra tabella
            createTable();
            JScrollPane scrollPane = new JScrollPane(dataTable);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            centerPanel.add(scrollPane, BorderLayout.CENTER);
        }
        
        centerPanel.revalidate();
        centerPanel.repaint();
    }
    
    /**
     * Crea la tabella in base alla funzione
     */
    private void createTable() {
        if (userRole == UserRole.CHAIR && chairFunction != null) {
            switch (chairFunction) {
                case REMOVE_REVIEWER:
                    createRemoveReviewerTable();
                    break;
                case REVIEW_ARTICLE:
                    createReviewArticleTable();
                    break;
                case VIEW_SUBMISSIONS:
                    createDefaultTable();
                    break;
                default:
                    createDefaultTable();
                    break;
            }
        } else if (userRole == UserRole.REVISORE && revisoreFunction != null) {
            switch (revisoreFunction) {
                case ARTICLES_TO_REVIEW:
                    createRevisoreTable();
                    break;
                default:
                    createDefaultTable();
                    break;
            }
        } else if (userRole == UserRole.SOTTOREVISORE && sottoRevisoreFunction != null) {
            switch (sottoRevisoreFunction) {
                case ARTICLES_TO_REVIEW:
                    createSottoRevisoreTable();
                    break;
                default:
                    createDefaultTable();
                    break;
            }
        } else if (editoreFunction != null) {
            switch (editoreFunction) {
                case ACCEPTED_ARTICLES:
                    createEditoreTable();
                    break;
                default:
                    createDefaultTable();
                    break;
            }
        } else {
            createDefaultTable();
        }
    }
    
    /**
     * Crea tabella per "Rimuovi revisore"
     */
    private void createRemoveReviewerTable() {
        // Trova il numero massimo di revisori per determinare le colonne
        int maxReviewers = articleReviewerData.stream()
                .mapToInt(data -> data.reviewers.size())
                .max()
                .orElse(3);
        
        // Crea colonne
        List<String> columnNames = new ArrayList<>();
        columnNames.add("Articolo");
        for (int i = 0; i < maxReviewers; i++) {
            columnNames.add("Revisore");
        }
        
        tableModel = new DefaultTableModel(columnNames.toArray(new String[0]), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabella di sola visualizzazione
            }
        };
        
        // Popola la tabella
        for (ArticleReviewerData data : articleReviewerData) {
            Object[] row = new Object[columnNames.size()];
            row[0] = data.articleTitle + "\n" + data.articleAbstract;
            
            // Riempi le colonne dei revisori
            for (int i = 0; i < maxReviewers; i++) {
                if (i < data.reviewers.size()) {
                    row[i + 1] = data.reviewers.get(i);
                } else {
                    row[i + 1] = ""; // Cella vuota
                }
            }
            tableModel.addRow(row);
        }
        
        dataTable = new JTable(tableModel);
        dataTable.setFont(new Font("Arial", Font.PLAIN, 12));
        dataTable.setRowHeight(50);
        
        // Imposta larghezza colonne
        dataTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Articolo
        for (int i = 1; i < columnNames.size(); i++) {
            dataTable.getColumnModel().getColumn(i).setPreferredWidth(150); // Revisori
            dataTable.getColumnModel().getColumn(i).setCellRenderer(new GrayReviewerCellRenderer());
        }
    }
    
    /**
     * Crea tabella per "Revisiona articolo"
     */
    private void createReviewArticleTable() {
        String[] columnNames = {"Articolo", "Azione"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // Solo la colonna bottoni è editabile
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return JButton.class;
                } else {
                    return String.class;
                }
            }
        };
        
        // Popola la tabella
        for (ArticleData data : articleData) {
            Object[] row = {data.title, "Visualizza stato"};
            tableModel.addRow(row);
        }
        
        dataTable = new JTable(tableModel);
        dataTable.setFont(new Font("Arial", Font.PLAIN, 12));
        dataTable.setRowHeight(50);
        
        // Imposta larghezza colonne
        dataTable.getColumnModel().getColumn(0).setPreferredWidth(400); // Articolo
        dataTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Bottone
        
        // Renderer e editor per bottoni
        dataTable.getColumnModel().getColumn(1).setCellRenderer(new ButtonRenderer());
        dataTable.getColumnModel().getColumn(1).setCellEditor(new ButtonEditor());
    }
    
    /**
     * Crea tabella per "Articoli da revisionare" (SottoRevisore)
     */
    private void createSottoRevisoreTable() {
        String[] columnNames = {"Articolo", "Visualizza revisioni", "Revisiona articolo"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0; // Solo le colonne bottoni sono editabili
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex > 0) {
                    return JButton.class;
                } else {
                    return String.class;
                }
            }
        };
        
        // Popola la tabella
        for (RevisoreArticleData data : revisoreArticleData) {
            Object[] row = {data.title, "Visualizza revisioni", "Revisiona articolo"};
            tableModel.addRow(row);
        }
        
        dataTable = new JTable(tableModel);
        dataTable.setFont(new Font("Arial", Font.PLAIN, 12));
        dataTable.setRowHeight(50);
        
        // Imposta larghezza colonne
        dataTable.getColumnModel().getColumn(0).setPreferredWidth(400); // Articolo
        dataTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Bottone 1
        dataTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Bottone 2
        
        // Renderer e editor per bottoni
        for (int i = 1; i < 3; i++) {
            dataTable.getColumnModel().getColumn(i).setCellRenderer(new ButtonRenderer());
            dataTable.getColumnModel().getColumn(i).setCellEditor(new SottoRevisoreButtonEditor());
        }
    }
    
    /**
     * Crea tabella di default
     */
    private void createDefaultTable() {
        String[] columnNames = {"Elemento"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(tableModel);
    }
    
    /**
     * Crea tabella per "Articoli da revisionare" (Revisore)
     */
    private void createRevisoreTable() {
        String[] columnNames = {"Articolo", "Visualizza revisione delegata", "Visualizza revisioni", "Revisiona articolo"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 1; // Solo le colonne bottoni sono editabili
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 1) {
                    return JButton.class;
                } else {
                    return String.class;
                }
            }
        };
        
        // Popola la tabella
        for (RevisoreArticleData data : revisoreArticleData) {
            Object[] row = {
                data.title, 
                "Visualizza revisione delegata", 
                "Visualizza revisioni", 
                "Revisiona articolo"
            };
            tableModel.addRow(row);
        }
        
        dataTable = new JTable(tableModel);
        dataTable.setFont(new Font("Arial", Font.PLAIN, 12));
        dataTable.setRowHeight(50);
        
        // Imposta larghezza colonne
        dataTable.getColumnModel().getColumn(0).setPreferredWidth(300); // Articolo
        dataTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Bottone 1
        dataTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Bottone 2
        dataTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Bottone 3
        
        // Renderer e editor per bottoni
        for (int i = 1; i < 4; i++) {
            dataTable.getColumnModel().getColumn(i).setCellRenderer(new ButtonRenderer());
            dataTable.getColumnModel().getColumn(i).setCellEditor(new RevisoreButtonEditor());
        }
    }
    
    /**
     * Crea tabella per "Articoli accettati" (Editore)
     */
    private void createEditoreTable() {
        String[] columnNames = {"Articolo", "Invia avviso", "Scarica"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 1; // Solo le colonne bottoni sono editabili
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 1) {
                    return JButton.class;
                } else {
                    return String.class;
                }
            }
        };
        
        // Popola la tabella
        for (EditoreArticleData data : editoreArticleData) {
            Object[] row = {data.title, "Invia avviso", "Scarica"};
            tableModel.addRow(row);
        }
        
        dataTable = new JTable(tableModel);
        dataTable.setFont(new Font("Arial", Font.PLAIN, 12));
        dataTable.setRowHeight(50);
        
        // Imposta larghezza colonne
        dataTable.getColumnModel().getColumn(0).setPreferredWidth(400); // Articolo
        dataTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Bottone 1
        dataTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Bottone 2
        
        // Renderer e editor per bottoni
        dataTable.getColumnModel().getColumn(1).setCellRenderer(new ButtonRenderer());
        dataTable.getColumnModel().getColumn(1).setCellEditor(new EditoreButtonEditor());
        dataTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        dataTable.getColumnModel().getColumn(2).setCellEditor(new EditoreButtonEditor());
    }
    
    /**
     * Renderer per celle grigie dei revisori
     */
    private class GrayReviewerCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (column > 0 && value != null && !value.toString().isEmpty()) {
                c.setBackground(Color.LIGHT_GRAY);
            } else {
                c.setBackground(Color.WHITE);
            }
            
            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
            }
            
            return c;
        }
    }
    
    /**
     * Renderer per bottoni arancioni
     */
    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(Color.ORANGE);
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 12));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setFocusPainted(false);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            return this;
        }
    }
    
    /**
     * Editor per bottoni arancioni
     */
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private int currentRow;
        
        public ButtonEditor() {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(Color.ORANGE);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setFocusPainted(false);
            
            button.addActionListener(e -> {
                clicked = true;
                fireEditingStopped();
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = value != null ? value.toString() : "";
            button.setText(label);
            clicked = false;
            currentRow = row;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                handleButtonClick(currentRow);
            }
            return label;
        }
        
        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
        
        private void handleButtonClick(int row) {
            if (chairFunction == ChairFunction.REVIEW_ARTICLE && row < articleData.size()) {
                ArticleData article = articleData.get(row);
                // Apri schermata di panoramica revisioni
                try {
                    Class<?> revisionOverviewClass = Class.forName("com.cms.users.revisions.Interface.RevisionOverviewScreen");
                    Class<?> userRoleClass = Class.forName("com.cms.users.revisions.Interface.RevisionOverviewScreen$UserRole");
                    Object chairRole = userRoleClass.getEnumConstants()[0]; // CHAIR
                    
                    Object revisionScreen = revisionOverviewClass.getDeclaredConstructor(userRoleClass, String.class, String.class)
                                                                .newInstance(chairRole, article.title, article.id);
                    java.lang.reflect.Method createMethod = revisionOverviewClass.getMethod("create");
                    createMethod.invoke(revisionScreen);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(ListScreen.this,
                        "Apertura stato revisioni per: " + article.title,
                        "Visualizza Stato",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
    
    /**
     * Editor per bottoni arancioni del Revisore
     */
    private class RevisoreButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private int currentRow;
        private int currentColumn;
        
        public RevisoreButtonEditor() {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(Color.ORANGE);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setFocusPainted(false);
            
            button.addActionListener(e -> {
                clicked = true;
                fireEditingStopped();
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = value != null ? value.toString() : "";
            button.setText(label);
            clicked = false;
            currentRow = row;
            currentColumn = column;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                handleRevisoreButtonClick(currentRow, currentColumn);
            }
            return label;
        }
        
        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
        
        private void handleRevisoreButtonClick(int row, int column) {
            if (row < revisoreArticleData.size()) {
                RevisoreArticleData article = revisoreArticleData.get(row);
                String action = "";
                
                switch (column) {
                    case 1:
                        action = "Visualizza revisione delegata per: " + article.title;
                        break;
                    case 2:
                        action = "Visualizza revisioni per: " + article.title;
                        break;
                    case 3:
                        action = "Revisiona articolo: " + article.title;
                        break;
                }
                
                JOptionPane.showMessageDialog(ListScreen.this, action, "Azione Revisore", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    /**
     * Editor per bottoni arancioni dell'Editore
     */
    private class EditoreButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private int currentRow;
        private int currentColumn;
        
        public EditoreButtonEditor() {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(Color.ORANGE);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setFocusPainted(false);
            
            button.addActionListener(e -> {
                clicked = true;
                fireEditingStopped();
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = value != null ? value.toString() : "";
            button.setText(label);
            clicked = false;
            currentRow = row;
            currentColumn = column;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                handleEditoreButtonClick(currentRow, currentColumn);
            }
            return label;
        }
        
        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
        
        private void handleEditoreButtonClick(int row, int column) {
            if (row < editoreArticleData.size()) {
                EditoreArticleData article = editoreArticleData.get(row);
                String action = "";
                
                switch (column) {
                    case 1:
                        action = "Invia avviso per: " + article.title;
                        article.notificationSent = true;
                        break;
                    case 2:
                        action = "Scarica articolo: " + article.title;
                        break;
                }
                
                JOptionPane.showMessageDialog(ListScreen.this, action, "Azione Editore", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    /**
     * Editor per bottoni arancioni del SottoRevisore
     */
    private class SottoRevisoreButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private int currentRow;
        private int currentColumn;
        
        public SottoRevisoreButtonEditor() {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(Color.ORANGE);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setFocusPainted(false);
            
            button.addActionListener(e -> {
                clicked = true;
                fireEditingStopped();
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = value != null ? value.toString() : "";
            button.setText(label);
            clicked = false;
            currentRow = row;
            currentColumn = column;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                handleSottoRevisoreButtonClick(currentRow, currentColumn);
            }
            return label;
        }
        
        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
        
        private void handleSottoRevisoreButtonClick(int row, int column) {
            if (row < revisoreArticleData.size()) {
                RevisoreArticleData article = revisoreArticleData.get(row);
                String action = "";
                
                switch (column) {
                    case 1:
                        action = "Visualizza revisioni per: " + article.title;
                        break;
                    case 2:
                        action = "Revisiona articolo: " + article.title;
                        break;
                }
                
                JOptionPane.showMessageDialog(ListScreen.this, action, "Azione SottoRevisore", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    /**
     * Configura i gestori di eventi
     */
    private void setupEventHandlers() {
        // Bottone home
        homeButton.addActionListener(e -> {
            dispose();
            try {
                Class<?> homeScreenClass = Class.forName("com.cms.users.Commons.HomeScreen");
                Object homeScreen = homeScreenClass.getDeclaredConstructor().newInstance();
                java.lang.reflect.Method displayMethod = homeScreenClass.getMethod("displayUserDashboard");
                displayMethod.invoke(homeScreen);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Navigazione alla Home...");
            }
        });
        
        // Bottone notifiche
        notificheButton.addActionListener(e -> {
            try {
                Class<?> userMenuClass = Class.forName("com.cms.users.account.Interface.UserMenu");
                String userName = userRole == UserRole.CHAIR ? "Chair" : "Utente";
                String userEmail = userName.toLowerCase() + "@cms.com";
                Object userMenu = userMenuClass.getDeclaredConstructor(String.class, String.class)
                                              .newInstance(userName, userEmail);
                java.lang.reflect.Method showMethod = userMenuClass.getMethod("showMenuBelowButton", JButton.class);
                showMethod.invoke(userMenu, notificheButton);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Apertura menu notifiche...");
            }
        });
        
        // Bottone profilo
        profiloButton.addActionListener(e -> {
            try {
                Class<?> userInfoClass = Class.forName("com.cms.users.account.Interface.UserInfoScreen");
                String userName = userRole == UserRole.CHAIR ? "Chair" : "Utente";
                String userEmail = userName.toLowerCase() + "@cms.com";
                Object userInfoScreen = userInfoClass.getDeclaredConstructor(String.class, String.class)
                                                     .newInstance(userName, userEmail);
                java.lang.reflect.Method createMethod = userInfoClass.getMethod("create");
                createMethod.invoke(userInfoScreen);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Apertura profilo utente...");
            }
        });
        
        // Bottone "Scarica tutti" per Editore
        if (scaricaTuttiButton != null) {
            scaricaTuttiButton.addActionListener(e -> {
                if (editoreArticleData != null && !editoreArticleData.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Scaricamento di tutti gli articoli accettati (" + editoreArticleData.size() + " articoli)...",
                        "Scarica Tutti",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Nessun articolo da scaricare.",
                        "Scarica Tutti",
                        JOptionPane.WARNING_MESSAGE);
                }
            });
        }
    }
    
    // Metodi pubblici dell'interfaccia originale
    
    /**
     * Visualizza la lista degli elementi
     */
    public void displayItemList() {
        setVisible(true);
    }
    
    /**
     * Cerca elementi nella lista
     */
    public void searchItems(String searchTerm) {
        // Implementazione della ricerca se necessaria
        JOptionPane.showMessageDialog(this, "Ricerca: " + searchTerm);
    }
    
    /**
     * Ordina gli elementi
     */
    public void sortItems(String criteria) {
        // Implementazione dell'ordinamento se necessaria
        JOptionPane.showMessageDialog(this, "Ordinamento: " + criteria);
    }
    
    /**
     * Filtra gli elementi
     */
    public void filterItems(String filter) {
        // Implementazione del filtro se necessaria
        JOptionPane.showMessageDialog(this, "Filtro: " + filter);
    }
    
    /**
     * Naviga a una pagina specifica
     */
    public void navigateToPage(int pageNumber) {
        // Implementazione della paginazione se necessaria
        JOptionPane.showMessageDialog(this, "Pagina: " + pageNumber);
    }
    
    /**
     * Aggiorna la lista
     */
    public void refreshList() {
        initializeData();
        updateDisplay();
    }
    
    /**
     * Seleziona un elemento
     */
    public void selectItem(Object item) {
        JOptionPane.showMessageDialog(this, "Elemento selezionato: " + item);
    }
    
    /**
     * Mostra dettagli di un elemento
     */
    public void showItemDetails(Object item) {
        JOptionPane.showMessageDialog(this, "Dettagli: " + item);
    }
    
    /**
     * Ottiene la lista degli elementi
     */
    public List<Object> getItemList() {
        List<Object> items = new ArrayList<>();
        if (chairFunction == ChairFunction.REMOVE_REVIEWER) {
            items.addAll(articleReviewerData);
        } else if (chairFunction == ChairFunction.REVIEW_ARTICLE) {
            items.addAll(articleData);
        }
        return items;
    }
    
    /**
     * Ottiene il numero totale di pagine
     */
    public int getTotalPages() {
        return 1; // Per ora una sola pagina
    }
    
    // Metodi di utilità
    
    /**
     * Imposta il ruolo utente
     */
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
    
    /**
     * Imposta la funzione per Chair
     */
    public void setChairFunction(ChairFunction chairFunction) {
        this.chairFunction = chairFunction;
        setupScreenTitle();
        initializeData();
        updateDisplay();
    }
    
    /**
     * Imposta se ci sono dati o meno
     */
    public void setHasData(boolean hasData) {
        this.hasData = hasData;
        updateDisplay();
    }
    
    /**
     * Imposta i dati degli articoli del revisore da una lista di ArticoloE
     * @param articoliAssegnati Lista degli articoli assegnati al revisore
     */
    public void setRevisoreArticleDataFromArticoli(LinkedList<ArticoloE> articoliAssegnati) {
        System.out.println("DEBUG ListScreen: === INIZIO setRevisoreArticleDataFromArticoli ===");
        System.out.println("DEBUG ListScreen: Ricevuti " + articoliAssegnati.size() + " articoli");
        
        if (revisoreArticleData == null) {
            revisoreArticleData = new ArrayList<>();
        }
        
        // Pulisce i dati esistenti
        revisoreArticleData.clear();
        
        // Converte gli ArticoloE in RevisoreArticleData
        for (ArticoloE articolo : articoliAssegnati) {
            RevisoreArticleData data = new RevisoreArticleData(
                String.valueOf(articolo.getId()),
                articolo.getTitolo(),
                articolo.getStato() != null ? articolo.getStato() : "Da revisionare"
            );
            revisoreArticleData.add(data);
            
            System.out.println("DEBUG ListScreen: Aggiunto articolo - ID: " + data.id + 
                             ", Titolo: '" + data.title + "', Stato: '" + data.status + "'");
        }
        
        // Aggiorna lo stato dei dati e la visualizzazione
        this.hasData = !revisoreArticleData.isEmpty();
        updateDisplay();
        
        System.out.println("DEBUG ListScreen: Impostati " + revisoreArticleData.size() + " articoli per il revisore");
        System.out.println("DEBUG ListScreen: === FINE setRevisoreArticleDataFromArticoli ===");
    }
    
    /**
     * Metodo main per test standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test funzione "Rimuovi revisore" con dati (Chair)
            ListScreen screen1 = new ListScreen(UserRole.CHAIR, ChairFunction.REMOVE_REVIEWER);
            screen1.setLocation(0, 0);
            screen1.setVisible(true);
            
            // Test funzione "Revisiona articolo" (Chair)
            ListScreen screen2 = new ListScreen(UserRole.CHAIR, ChairFunction.REVIEW_ARTICLE);
            screen2.setLocation(300, 0);
            screen2.setVisible(true);
            
            // Test funzione "Articoli da revisionare" (Revisore)
            ListScreen screen3 = new ListScreen(UserRole.REVISORE, RevisoreFunction.ARTICLES_TO_REVIEW);
            screen3.setLocation(600, 0);
            screen3.setVisible(true);
            
            // Test funzione "Articoli da revisionare" (SottoRevisore) con dati
            ListScreen screen4 = new ListScreen(UserRole.SOTTOREVISORE, SottoRevisoreFunction.ARTICLES_TO_REVIEW);
            screen4.setLocation(900, 0);
            screen4.setVisible(true);
            
            // Test funzione "Articoli accettati" (Editore)
            ListScreen screen5 = new ListScreen(UserRole.AUTORE, EditoreFunction.ACCEPTED_ARTICLES);
            screen5.setLocation(0, 350);
            screen5.setVisible(true);
            
            // Test senza dati (SottoRevisore)
            ListScreen screen6 = new ListScreen(UserRole.SOTTOREVISORE, SottoRevisoreFunction.ARTICLES_TO_REVIEW);
            screen6.setHasData(false);
            screen6.setLocation(300, 350);
            screen6.setVisible(true);
        });
    }
}
