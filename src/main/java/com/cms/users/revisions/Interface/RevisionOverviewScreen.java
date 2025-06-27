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
        
        public RevisionData(String revisionId, String reviewerName, String comment, String status) {
            this.revisionId = revisionId;
            this.reviewerName = reviewerName;
            this.comment = comment;
            this.status = status;
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
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Panoramica Revisioni");
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
        
        return panel;
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
            Class<?> reviewScreenClass = Class.forName("com.cms.users.revisions.Interface.ReviewSubmissionScreen");
            Object reviewScreen = reviewScreenClass.getDeclaredConstructor(String.class, String.class, String.class)
                                                   .newInstance(articleId, articleTitle, "CHAIR_001");
            java.lang.reflect.Method createMethod = reviewScreenClass.getMethod("create");
            createMethod.invoke(reviewScreen);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Apertura schermata di revisione per l'articolo: " + articleTitle, 
                "Revisione Articolo", 
                JOptionPane.INFORMATION_MESSAGE);
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
