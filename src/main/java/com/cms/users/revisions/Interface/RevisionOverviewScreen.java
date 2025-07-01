package com.cms.users.revisions.Interface;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <<boundary>>
 * RevisionOverviewScreen - Schermata di panoramica delle revisioni
 * Cambia layout in base al ruolo dell'utente (Chair/Revisore)
 */
public class RevisionOverviewScreen extends JFrame {
    
    // Enumerazione per i ruoli utente
    public enum UserRole {
        CHAIR, REVISORE
    }
    
    // Classe per i dati di una revisione
    public static class RevisionData {
        public String revisionId;
        public String reviewerName;
        public String comment;
        public String status;
        public boolean isClickable;
        public int articleId;
        
        public RevisionData(String revisionId, String reviewerName, String comment, String status) {
            this.revisionId = revisionId;
            this.reviewerName = reviewerName;
            this.comment = comment;
            this.status = status;
            this.isClickable = false;
            this.articleId = -1;
        }
        
        public RevisionData(String revisionId, String reviewerName, String comment, String status, boolean isClickable, int articleId) {
            this.revisionId = revisionId;
            this.reviewerName = reviewerName;
            this.comment = comment;
            this.status = status;
            this.isClickable = isClickable;
            this.articleId = articleId;
        }
    }
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    private JButton revisioneArticoloButton; // Solo per Chair
    
    // Dati della schermata
    private UserRole userRole;
    private String articleTitle;
    private String articleId;
    private List<RevisionData> revisions;
    private int conferenceId = -1; // ID della conferenza per il doppio click
    
    // Pannello per le revisioni
    private JPanel revisionsPanel;
    
    /**
     * Costruttore di default (ruolo Chair)
     */
    public RevisionOverviewScreen() {
        this(UserRole.CHAIR, "Titolo articolo", "ART" + (int)(Math.random() * 10000));
    }
    
    /**
     * Costruttore con ruolo specificato
     */
    public RevisionOverviewScreen(UserRole userRole, String articleTitle, String articleId) {
        this.userRole = userRole != null ? userRole : UserRole.CHAIR;
        this.articleTitle = articleTitle != null ? articleTitle : "Titolo articolo";
        this.articleId = articleId != null ? articleId : "ART" + (int)(Math.random() * 10000);
        
        initializeData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con dati reali delle revisioni dal database
     */
    public RevisionOverviewScreen(UserRole userRole, String articleTitle, String articleId, ArrayList<Object> revisionsData) {
        this.userRole = userRole != null ? userRole : UserRole.CHAIR;
        this.articleTitle = articleTitle != null ? articleTitle : "Titolo articolo";
        this.articleId = articleId != null ? articleId : "ART" + (int)(Math.random() * 10000);
        
        initializeDataFromDatabase(revisionsData);
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con dati reali delle revisioni dal database e ID conferenza per doppio click
     * Utilizzato dal ConferenceControl per mostrare tutte le revisioni con funzionalità interattiva
     */
    public RevisionOverviewScreen(UserRole userRole, String articleTitle, String articleId, ArrayList<Object> revisionsData, int conferenceId) {
        this.userRole = userRole != null ? userRole : UserRole.CHAIR;
        this.articleTitle = articleTitle != null ? articleTitle : "Titolo articolo";
        this.articleId = articleId != null ? articleId : "ART" + (int)(Math.random() * 10000);
        this.conferenceId = conferenceId;
        
        initializeDataFromDatabase(revisionsData);
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Inizializza i dati di esempio
     */
    private void initializeData() {
        revisions = new ArrayList<>();
        revisions.add(new RevisionData("REV001", "Prof. Mario Rossi", 
            "Ottimo lavoro con alcune osservazioni minori sulla metodologia...", 
            "Completata"));
        revisions.add(new RevisionData("REV002", "Dr. Luigi Verdi", 
            "L'articolo presenta interessanti spunti ma necessita di approfondimenti...", 
            "In corso"));
        revisions.add(new RevisionData("REV003", "Prof.ssa Anna Bianchi", 
            "Lavoro ben strutturato con bibliografia completa...", 
            "Da iniziare"));
    }
    
    /**
     * Inizializza i dati dalle revisioni del database
     */
    private void initializeDataFromDatabase(ArrayList<Object> revisionsData) {
        revisions = new ArrayList<>();
        
        if (revisionsData != null) {
            for (Object obj : revisionsData) {
                try {
                    if (obj instanceof String[]) {
                        // Gestione del formato array di stringhe (formato precedente)
                        String[] revisionArray = (String[]) obj;
                        if (revisionArray.length >= 4) {
                            revisions.add(new RevisionData(
                                revisionArray[0], // revisionId
                                revisionArray[1], // reviewerName
                                revisionArray[2], // comment
                                revisionArray[3]  // status
                            ));
                        }
                    } else if (obj instanceof java.util.Map) {
                        // Gestione del formato Map per dati complessi dal ConferenceControl
                        @SuppressWarnings("unchecked")
                        java.util.Map<String, Object> revisioneCompleta = (java.util.Map<String, Object>) obj;
                        
                        Object infoRevisioneObj = revisioneCompleta.get("infoRevisione");
                        String titoloArticolo = (String) revisioneCompleta.get("titoloArticolo");
                        Integer idArticolo = (Integer) revisioneCompleta.get("idArticolo");
                        String statoRevisione = (String) revisioneCompleta.get("statoRevisione");
                        Boolean isClickable = (Boolean) revisioneCompleta.get("isClickable");
                        
                        if (idArticolo == null) idArticolo = -1;
                        if (isClickable == null) isClickable = false;
                        if (statoRevisione == null) statoRevisione = "Sconosciuto";
                        
                        String revisionId = "ART" + idArticolo;
                        String reviewerName = "Sistema";
                        String comment = "Articolo: " + titoloArticolo;
                        
                        // Costruisci il commento in base alle info della revisione
                        if (infoRevisioneObj != null) {
                            if (infoRevisioneObj instanceof String) {
                                String infoStr = (String) infoRevisioneObj;
                                if (!infoStr.trim().isEmpty()) {
                                    comment = "Articolo: " + titoloArticolo + " - " + infoStr;
                                }
                            } else if (infoRevisioneObj instanceof java.util.Map) {
                                @SuppressWarnings("unchecked")
                                java.util.Map<String, Object> infoMap = (java.util.Map<String, Object>) infoRevisioneObj;
                                Object revisore = infoMap.get("revisore");
                                Object commenti = infoMap.get("commenti");
                                
                                if (revisore != null) {
                                    reviewerName = revisore.toString();
                                }
                                if (commenti != null) {
                                    comment = "Articolo: " + titoloArticolo + " - " + commenti.toString();
                                }
                            }
                        } else {
                            // Nessuna info revisione disponibile
                            if ("Da iniziare".equals(statoRevisione)) {
                                comment = "Articolo: " + titoloArticolo + " - Revisione non ancora iniziata";
                                reviewerName = "Nessun revisore";
                            }
                        }
                        
                        revisions.add(new RevisionData(revisionId, reviewerName, comment, statoRevisione, isClickable, idArticolo));
                    }
                } catch (Exception e) {
                    System.err.println("Errore nel processamento revisione: " + e.getMessage());
                    // Aggiungi una revisione di fallback
                    revisions.add(new RevisionData(
                        "ERR" + revisions.size(),
                        "Errore di caricamento",
                        "Errore nel caricamento dei dati di revisione",
                        "Errore",
                        false,
                        -1
                    ));
                }
            }
        }
        
        // Se non ci sono revisioni dal database, aggiungi un messaggio
        if (revisions.isEmpty()) {
            if (userRole == UserRole.CHAIR) {
                revisions.add(new RevisionData("", "Nessuna revisione", 
                    "Nessuna revisione disponibile per questa conferenza. Gli articoli potrebbero non essere ancora stati assegnati ai revisori.", 
                    "", false, -1));
            } else {
                revisions.add(new RevisionData("", "Nessuna revisione", 
                    "Nessuna revisione disponibile per questo articolo", 
                    "", false, -1));
            }
        }
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        String windowTitle = userRole == UserRole.CHAIR ? 
            "CMS - Stato Revisioni Conferenza" : 
            "CMS - Panoramica Revisioni";
        setTitle(windowTitle);
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
        
        // Bottone "Revisiona articolo" solo per Chair
        if (userRole == UserRole.CHAIR) {
            revisioneArticoloButton = new JButton("Revisiona articolo");
            revisioneArticoloButton.setBackground(Color.ORANGE);
            revisioneArticoloButton.setForeground(Color.WHITE);
            revisioneArticoloButton.setFont(new Font("Arial", Font.BOLD, 12));
            revisioneArticoloButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            revisioneArticoloButton.setFocusPainted(false);
            revisioneArticoloButton.setPreferredSize(new Dimension(150, 35));
        }
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
        
        // Pannello del titolo
        JPanel titlePanel = createTitlePanel();
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Pannello delle revisioni
        revisionsPanel = createRevisionsPanel();
        JScrollPane scrollPane = new JScrollPane(revisionsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    /**
     * Crea il pannello del titolo
     */
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Titolo dell'articolo
        JLabel titleLabel = new JLabel(articleTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // Bottone "Revisiona articolo" solo per Chair
        if (userRole == UserRole.CHAIR && revisioneArticoloButton != null) {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.add(revisioneArticoloButton);
            titlePanel.add(buttonPanel, BorderLayout.EAST);
        }
        
        return titlePanel;
    }
    
    /**
     * Crea il pannello delle revisioni
     */
    private JPanel createRevisionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Se è il Chair, aggiungi un messaggio informativo
        if (userRole == UserRole.CHAIR && revisions.size() > 1) {
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(Color.WHITE);
            infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            
            JLabel infoLabel = new JLabel("Stato di tutte le revisioni della conferenza (" + revisions.size() + " elementi)");
            infoLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            infoLabel.setForeground(Color.GRAY);
            infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(infoLabel);
            
            // Controlla se ci sono revisioni cliccabili
            boolean hasClickableRevisions = revisions.stream().anyMatch(r -> r.isClickable);
            if (hasClickableRevisions) {
                JLabel clickLabel = new JLabel("💡 Doppio click sulle revisioni con bordo arancione per revisionare gli articoli");
                clickLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                clickLabel.setForeground(new Color(255, 140, 0));
                clickLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                infoPanel.add(clickLabel);
            }
            
            panel.add(infoPanel);
        }
        
        // Crea una sezione per ogni revisione
        for (RevisionData revision : revisions) {
            JPanel revisionPanel = createRevisionPanel(revision);
            panel.add(revisionPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 5))); // Spazio tra le revisioni
        }
        
        return panel;
    }
    
    /**
     * Crea il pannello per una singola revisione
     */
    private JPanel createRevisionPanel(RevisionData revision) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        panel.setPreferredSize(new Dimension(700, 120));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        // Label "Revisione"
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        JLabel revisioneLabel = new JLabel("Revisione");
        revisioneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(revisioneLabel, gbc);
        
        // Label "Commento:"
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        JLabel commentoLabel = new JLabel("Commento:");
        commentoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(commentoLabel, gbc);
        
        // Area commento (troncata se troppo lunga)
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        String displayComment = revision.comment.length() > 80 ? 
            revision.comment.substring(0, 80) + "..." : revision.comment;
        JTextArea commentArea = new JTextArea(displayComment);
        commentArea.setFont(new Font("Arial", Font.PLAIN, 12));
        commentArea.setBackground(Color.WHITE);
        commentArea.setEditable(false);
        commentArea.setOpaque(false);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        panel.add(commentArea, gbc);
        
        // Label "Stato:"
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel statoLabel = new JLabel("Stato:");
        statoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(statoLabel, gbc);
        
        // Valore dello stato
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        JLabel statoValue = new JLabel(revision.status);
        statoValue.setFont(new Font("Arial", Font.BOLD, 12));
        // Colore stato
        switch (revision.status.toLowerCase()) {
            case "completata":
                statoValue.setForeground(new Color(0, 150, 0)); // Verde
                break;
            case "in corso":
                statoValue.setForeground(new Color(255, 140, 0)); // Arancione
                break;
            case "da iniziare":
                statoValue.setForeground(Color.RED);
                break;
            default:
                statoValue.setForeground(Color.BLACK);
        }
        panel.add(statoValue, gbc);
        
        // Aggiungi supporto per doppio click se è il Chair e la revisione è cliccabile
        if (userRole == UserRole.CHAIR && revision.isClickable) {
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            panel.setToolTipText("Doppio click per revisionare questo articolo");
            
            // Aggiungi un bordo più evidente per indicare che è cliccabile
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.ORANGE, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            
            panel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        handleDoubleClickRevisione(revision.articleId);
                    }
                }
                
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    panel.setBackground(new Color(255, 248, 220)); // Sfondo chiaro quando hover
                }
                
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    panel.setBackground(Color.WHITE);
                }
            });
        }
        
        return panel;
    }
    
    /**
     * Gestisce il doppio click su una revisione per aprire la schermata di revisione
     * @param articleId ID dell'articolo da revisionare
     */
    private void handleDoubleClickRevisione(int articleId) {
        System.out.println("DEBUG RevisionOverviewScreen: Doppio click su articolo ID: " + articleId);
        
        try {
            // Ottieni i dati dell'articolo dalla DBMSBoundary
            com.cms.users.Entity.ArticoloE articolo = com.cms.App.dbms.getArticolo(articleId);
            
            if (articolo == null) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Impossibile caricare i dati dell'articolo",
                    "Errore",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Imposta l'ID articolo corrente nella DBMSBoundary per il salvataggio
            com.cms.users.Commons.DBMSBoundary.setCurrentArticleId(articleId);
            System.out.println("DEBUG RevisionOverviewScreen: ID articolo impostato: " + articleId);
            
            // Verifica che esista un record nella tabella 'revisiona' per il Chair
            // Se non esiste, lo creiamo
            int chairId = com.cms.App.utenteAccesso.getId();
            if (!com.cms.App.dbms.verificaAssegnazioneEsistente(articleId, chairId)) {
                System.out.println("DEBUG RevisionOverviewScreen: Creando assegnazione Chair per articolo " + articleId);
                
                // Crea un'assegnazione temporanea per il Chair
                try {
                    com.cms.App.dbms.setRevisoreArticolo(articleId, chairId);
                    System.out.println("DEBUG RevisionOverviewScreen: Assegnazione Chair creata con successo");
                } catch (Exception assegnException) {
                    System.err.println("DEBUG RevisionOverviewScreen: Errore nella creazione assegnazione: " + assegnException.getMessage());
                    // Continua comunque, potrebbe già esistere
                }
            }
            
            // Crea la ReviewSubmissionScreen per il Chair
            com.cms.users.revisions.Interface.ReviewSubmissionScreen reviewScreen = 
                new com.cms.users.revisions.Interface.ReviewSubmissionScreen(
                    "ART" + articleId,
                    articolo.getTitolo(),
                    String.valueOf(chairId), // Usa l'ID reale del Chair
                    articolo
                );
            
            reviewScreen.setVisible(true);
            
            System.out.println("DEBUG RevisionOverviewScreen: ReviewSubmissionScreen aperta per Chair su articolo: " + articolo.getTitolo());
            
        } catch (Exception e) {
            System.err.println("Errore nell'apertura della schermata di revisione: " + e.getMessage());
            e.printStackTrace();
            
            javax.swing.JOptionPane.showMessageDialog(this,
                "Errore nell'apertura della schermata di revisione: " + e.getMessage(),
                "Errore",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Configura i gestori di eventi
     */
    private void setupEventHandlers() {
        // Bottone home
        homeButton.addActionListener(e -> {
            dispose();
            // Navigazione verso HomeScreen
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
                String userName = userRole == UserRole.CHAIR ? "Chair" : "Revisore";
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
                String userName = userRole == UserRole.CHAIR ? "Chair" : "Revisore";
                String userEmail = userName.toLowerCase() + "@cms.com";
                Object userInfoScreen = userInfoClass.getDeclaredConstructor(String.class, String.class)
                                                     .newInstance(userName, userEmail);
                java.lang.reflect.Method createMethod = userInfoClass.getMethod("create");
                createMethod.invoke(userInfoScreen);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Apertura profilo utente...");
            }
        });
        
        // Bottone "Revisiona articolo" (solo per Chair)
        if (userRole == UserRole.CHAIR && revisioneArticoloButton != null) {
            revisioneArticoloButton.addActionListener(e -> handleRevisioneArticolo());
        }
    }
    
    /**
     * Gestisce il click sul bottone "Revisiona articolo"
     */
    private void handleRevisioneArticolo() {
        // Apre la schermata di revisione per il Chair
        try {
            // Ottieni i dati dell'articolo dalla DBMSBoundary
            com.cms.users.Entity.ArticoloE articolo = null;
            try {
                // Estrai l'ID numerico dall'articleId (esempio: "ART1234" -> 1234)
                int idArticolo = Integer.parseInt(articleId.replaceAll("[^0-9]", ""));
                articolo = com.cms.App.dbms.getArticolo(idArticolo);
            } catch (NumberFormatException ex) {
                System.err.println("Errore nel parsing dell'ID articolo: " + articleId);
            }
            
            // Crea la ReviewSubmissionScreen con i dati dell'articolo
            com.cms.users.revisions.Interface.ReviewSubmissionScreen reviewScreen = 
                new com.cms.users.revisions.Interface.ReviewSubmissionScreen(
                    articleId, 
                    articolo != null ? articolo.getTitolo() : articleTitle, 
                    "CHAIR_" + com.cms.App.utenteAccesso.getId(),
                    articolo
                );
            
            reviewScreen.setVisible(true);
            
        } catch (Exception e) {
            System.err.println("Errore nell'apertura della schermata di revisione: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback: apri la schermata senza dati dell'articolo
            com.cms.users.revisions.Interface.ReviewSubmissionScreen reviewScreen = 
                new com.cms.users.revisions.Interface.ReviewSubmissionScreen(
                    articleId, 
                    articleTitle, 
                    "CHAIR_001"
                );
            reviewScreen.setVisible(true);
        }
    }
    
    // Metodi pubblici dell'interfaccia originale
    
    /**
     * Crea e mostra la schermata
     */
    public void create() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }
    
    /**
     * Gestisce il bottone revisione articolo
     */
    public void revisioneArticoloButton() {
        if (userRole == UserRole.CHAIR) {
            handleRevisioneArticolo();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Funzione non disponibile per il ruolo Revisore", 
                "Accesso Negato", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // Metodi di utilità
    
    /**
     * Imposta il ruolo dell'utente
     */
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
        // Ricostruisce l'interfaccia se necessario
        if (isDisplayable()) {
            SwingUtilities.invokeLater(() -> {
                getContentPane().removeAll();
                initializeComponents();
                setupLayout();
                setupEventHandlers();
                revalidate();
                repaint();
            });
        }
    }
    
    /**
     * Ottiene il ruolo dell'utente
     */
    public UserRole getUserRole() {
        return userRole;
    }
    
    /**
     * Imposta il titolo dell'articolo
     */
    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }
    
    /**
     * Ottiene il titolo dell'articolo
     */
    public String getArticleTitle() {
        return articleTitle;
    }
    
    /**
     * Imposta l'ID dell'articolo
     */
    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
    
    /**
     * Ottiene l'ID dell'articolo
     */
    public String getArticleId() {
        return articleId;
    }
    
    /**
     * Aggiunge una revisione alla lista
     */
    public void addRevision(String revisionId, String reviewerName, String comment, String status) {
        revisions.add(new RevisionData(revisionId, reviewerName, comment, status));
        // Aggiorna la visualizzazione se la finestra è visibile
        if (isDisplayable() && revisionsPanel != null) {
            SwingUtilities.invokeLater(() -> {
                Container parent = revisionsPanel.getParent();
                if (parent instanceof JViewport) {
                    parent = parent.getParent();
                    if (parent instanceof JScrollPane) {
                        ((JScrollPane) parent).setViewportView(createRevisionsPanel());
                        parent.revalidate();
                        parent.repaint();
                    }
                }
            });
        }
    }
    
    /**
     * Pulisce tutte le revisioni
     */
    public void clearRevisions() {
        revisions.clear();
        if (isDisplayable() && revisionsPanel != null) {
            SwingUtilities.invokeLater(() -> {
                Container parent = revisionsPanel.getParent();
                if (parent instanceof JViewport) {
                    parent = parent.getParent();
                    if (parent instanceof JScrollPane) {
                        ((JScrollPane) parent).setViewportView(createRevisionsPanel());
                        parent.revalidate();
                        parent.repaint();
                    }
                }
            });
        }
    }
    
    /**
     * Metodo main per test standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test per ruolo Chair
            RevisionOverviewScreen chairScreen = new RevisionOverviewScreen(
                UserRole.CHAIR, 
                "Machine Learning for Automated Software Testing", 
                "ART12345"
            );
            chairScreen.setVisible(true);
            
            // Test per ruolo Revisore (posizionata a destra)
            RevisionOverviewScreen reviewerScreen = new RevisionOverviewScreen(
                UserRole.REVISORE, 
                "AI-Driven Code Review Systems", 
                "ART67890"
            );
            reviewerScreen.setLocation(950, 100);
            reviewerScreen.setVisible(true);
        });
    }
}
