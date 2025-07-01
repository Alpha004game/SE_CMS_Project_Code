package com.cms.users.submissions.Interface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * <<boundary>>
 * GeneralSubmissionScreen - Schermata per la modifica delle preferenze sui singoli articoli
 * Permette ai revisori di indicare interesse o conflitto di interesse per ciascun articolo
 */
public class GeneralSubmissionScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    private JTable articoliTable;
    private DefaultTableModel tableModel;
    private JButton confermaButton;
    
    // Dati degli articoli
    private List<ArticlePreference> articlePreferences;
    
    /**
     * Classe interna per rappresentare le preferenze di un articolo
     */
    public static class ArticlePreference {
        private String titolo;
        private boolean interesse;
        private boolean conflittoInteresse;
        
        public ArticlePreference(String titolo) {
            this.titolo = titolo;
            this.interesse = false;
            this.conflittoInteresse = false;
        }
        
        public ArticlePreference(String titolo, boolean interesse, boolean conflittoInteresse) {
            this.titolo = titolo;
            this.interesse = interesse;
            this.conflittoInteresse = conflittoInteresse;
        }
        
        // Getters e setters
        public String getTitolo() { return titolo; }
        public void setTitolo(String titolo) { this.titolo = titolo; }
        public boolean isInteresse() { return interesse; }
        public void setInteresse(boolean interesse) { this.interesse = interesse; }
        public boolean isConflittoInteresse() { return conflittoInteresse; }
        public void setConflittoInteresse(boolean conflittoInteresse) { this.conflittoInteresse = conflittoInteresse; }
    }
    
    /**
     * Costruttore di default
     */
    public GeneralSubmissionScreen() {
        // Inizializza con dati di esempio
        initializeArticleData();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con lista di articoli
     */
    public GeneralSubmissionScreen(List<String> articoli) {
        // Inizializza con gli articoli forniti
        initializeArticleData(articoli);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Inizializza i dati degli articoli con valori di esempio
     */
    private void initializeArticleData() {
        articlePreferences = new ArrayList<>();
        articlePreferences.add(new ArticlePreference("Machine Learning Algorithms for Software Testing"));
        articlePreferences.add(new ArticlePreference("Agile Methodologies in Large Scale Projects"));
        articlePreferences.add(new ArticlePreference("Cloud Computing Architecture Patterns"));
        articlePreferences.add(new ArticlePreference("Artificial Intelligence in Code Review"));
        articlePreferences.add(new ArticlePreference("DevOps Best Practices and Tools"));
        articlePreferences.add(new ArticlePreference("Microservices Design Patterns"));
        articlePreferences.add(new ArticlePreference("Software Security in Modern Applications"));
    }
    
    /**
     * Inizializza i dati degli articoli con lista fornita
     */
    private void initializeArticleData(List<String> articoli) {
        articlePreferences = new ArrayList<>();
        for (String articolo : articoli) {
            articlePreferences.add(new ArticlePreference(articolo));
        }
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Modifica Preferenze Articolo");
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
        
        // Bottone Conferma
        confermaButton = new JButton("Conferma");
        confermaButton.setBackground(Color.ORANGE);
        confermaButton.setForeground(Color.WHITE);
        confermaButton.setFont(new Font("Arial", Font.BOLD, 14));
        confermaButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        confermaButton.setFocusPainted(false);
        confermaButton.setPreferredSize(new Dimension(120, 40));
        
        // Inizializza la tabella
        initializeTable();
    }
    
    /**
     * Inizializza la tabella degli articoli
     */
    private void initializeTable() {
        // Colonne della tabella
        String[] columnNames = {"Articolo", "Interesse", "Conflitto di interesse"};
        
        // Modello della tabella
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return String.class;
                    case 1: case 2: return Boolean.class;
                    default: return Object.class;
                }
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo le colonne dei checkbox sono editabili
                return column == 1 || column == 2;
            }
        };
        
        // Popola la tabella con i dati
        populateTable();
        
        // Crea la tabella
        articoliTable = new JTable(tableModel);
        articoliTable.setFont(new Font("Arial", Font.PLAIN, 14));
        articoliTable.setRowHeight(40);
        articoliTable.setGridColor(Color.LIGHT_GRAY);
        articoliTable.setShowGrid(true);
        
        // Configura le colonne
        articoliTable.getColumnModel().getColumn(0).setPreferredWidth(400); // Articolo
        articoliTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Interesse
        articoliTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Conflitto
        
        // Renderer personalizzato per i checkbox
        articoliTable.getColumnModel().getColumn(1).setCellRenderer(new CheckBoxRenderer());
        articoliTable.getColumnModel().getColumn(2).setCellRenderer(new CheckBoxRenderer());
        
        // Editor personalizzato per i checkbox
        articoliTable.getColumnModel().getColumn(1).setCellEditor(new CheckBoxEditor());
        articoliTable.getColumnModel().getColumn(2).setCellEditor(new CheckBoxEditor());
        
        // Header della tabella
        articoliTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        articoliTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
        articoliTable.getTableHeader().setForeground(Color.BLACK);
    }
    
    /**
     * Popola la tabella con i dati degli articoli
     */
    private void populateTable() {
        // Pulisce la tabella
        tableModel.setRowCount(0);
        
        // Aggiunge le righe
        for (ArticlePreference pref : articlePreferences) {
            Object[] row = {
                pref.getTitolo(),
                pref.isInteresse(),
                pref.isConflittoInteresse()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Renderer personalizzato per i checkbox
     */
    private class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        public CheckBoxRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setSelected(value != null && (Boolean) value);
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(Color.WHITE);
            }
            
            return this;
        }
    }
    
    /**
     * Editor personalizzato per i checkbox
     */
    private class CheckBoxEditor extends DefaultCellEditor {
        private JCheckBox checkBox;
        private int currentRow;
        private int currentColumn;
        
        public CheckBoxEditor() {
            super(new JCheckBox());
            checkBox = (JCheckBox) getComponent();
            checkBox.setHorizontalAlignment(JLabel.CENTER);
            checkBox.setOpaque(true);
            
            checkBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Aggiorna i dati quando il checkbox cambia
                    updateArticlePreference(currentRow, currentColumn, checkBox.isSelected());
                }
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            currentColumn = column;
            checkBox.setSelected(value != null && (Boolean) value);
            checkBox.setBackground(Color.WHITE);
            return checkBox;
        }
        
        @Override
        public Object getCellEditorValue() {
            return checkBox.isSelected();
        }
    }
    
    /**
     * Aggiorna le preferenze dell'articolo
     */
    private void updateArticlePreference(int row, int column, boolean value) {
        if (row < articlePreferences.size()) {
            ArticlePreference pref = articlePreferences.get(row);
            if (column == 1) { // Interesse
                pref.setInteresse(value);
                // Se si seleziona interesse, deseleziona conflitto
                if (value && pref.isConflittoInteresse()) {
                    pref.setConflittoInteresse(false);
                    tableModel.setValueAt(false, row, 2);
                }
            } else if (column == 2) { // Conflitto di interesse
                pref.setConflittoInteresse(value);
                // Se si seleziona conflitto, deseleziona interesse
                if (value && pref.isInteresse()) {
                    pref.setInteresse(false);
                    tableModel.setValueAt(false, row, 1);
                }
            }
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
        
        // Titolo
        JLabel titleLabel = new JLabel("Modifica preferenze articolo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Tabella con scroll
        JScrollPane tableScrollPane = new JScrollPane(articoliTable);
        tableScrollPane.setPreferredSize(new Dimension(700, 300));
        tableScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Bottone Conferma
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonPanel.add(confermaButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Configura i gestori degli eventi
     */
    private void setupEventHandlers() {
        homeButton.addActionListener(e -> handleHomeAction());
        notificheButton.addActionListener(e -> handleNotificheAction());
        profiloButton.addActionListener(e -> handleProfiloAction());
        confermaButton.addActionListener(e -> handleConfermaAction());
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
    
    private void handleConfermaAction() {
        // Ferma eventuali editing in corso
        if (articoliTable.isEditing()) {
            articoliTable.getCellEditor().stopCellEditing();
        }
        
        // Raccoglie le preferenze selezionate
        StringBuilder summary = new StringBuilder();
        summary.append("Preferenze confermate:\n\n");
        
        int interesseCount = 0;
        int conflittoCount = 0;
        
        for (ArticlePreference pref : articlePreferences) {
            if (pref.isInteresse()) {
                summary.append("✓ INTERESSE: ").append(pref.getTitolo()).append("\n");
                interesseCount++;
            } else if (pref.isConflittoInteresse()) {
                summary.append("✗ CONFLITTO: ").append(pref.getTitolo()).append("\n");
                conflittoCount++;
            }
        }
        
        summary.append("\nTotale: ").append(interesseCount).append(" interessi, ")
                .append(conflittoCount).append(" conflitti");
        
        // Conferma le modifiche
        int result = JOptionPane.showConfirmDialog(this,
            summary.toString(),
            "Conferma Preferenze",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            // Simula il salvataggio
            JOptionPane.showMessageDialog(this,
                "Preferenze salvate con successo!\n" +
                "Le tue preferenze sono state aggiornate per tutti gli articoli.",
                "Successo",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Qui andrà la logica per salvare nel database
            System.out.println("Preferenze salvate:");
            for (ArticlePreference pref : articlePreferences) {
                if (pref.isInteresse() || pref.isConflittoInteresse()) {
                    System.out.println(pref.getTitolo() + " - Interesse: " + 
                                     pref.isInteresse() + ", Conflitto: " + pref.isConflittoInteresse());
                }
            }
            
            // Chiude la schermata
            dispose();
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
     * Chiude la schermata
     */
    public void destroy() {
        SwingUtilities.invokeLater(() -> {
            setVisible(false);
            dispose();
        });
    }
    
    // Metodi di utilità
    
    /**
     * Aggiunge un nuovo articolo alla lista
     */
    public void addArticle(String titolo) {
        articlePreferences.add(new ArticlePreference(titolo));
        populateTable();
    }
    
    /**
     * Rimuove un articolo dalla lista
     */
    public void removeArticle(String titolo) {
        articlePreferences.removeIf(pref -> pref.getTitolo().equals(titolo));
        populateTable();
    }
    
    /**
     * Ottiene le preferenze per tutti gli articoli
     */
    public List<ArticlePreference> getArticlePreferences() {
        return new ArrayList<>(articlePreferences);
    }
    
    /**
     * Ottiene gli articoli con interesse
     */
    public List<String> getArticlesWithInterest() {
        List<String> result = new ArrayList<>();
        for (ArticlePreference pref : articlePreferences) {
            if (pref.isInteresse()) {
                result.add(pref.getTitolo());
            }
        }
        return result;
    }
    
    /**
     * Ottiene gli articoli con conflitto di interesse
     */
    public List<String> getArticlesWithConflict() {
        List<String> result = new ArrayList<>();
        for (ArticlePreference pref : articlePreferences) {
            if (pref.isConflittoInteresse()) {
                result.add(pref.getTitolo());
            }
        }
        return result;
    }
    
    /**
     * Imposta le preferenze per un articolo specifico
     */
    public void setArticlePreference(String titolo, boolean interesse, boolean conflittoInteresse) {
        for (ArticlePreference pref : articlePreferences) {
            if (pref.getTitolo().equals(titolo)) {
                pref.setInteresse(interesse);
                pref.setConflittoInteresse(conflittoInteresse);
                populateTable();
                break;
            }
        }
    }
    
    /**
     * Pulisce tutte le preferenze
     */
    public void clearAllPreferences() {
        for (ArticlePreference pref : articlePreferences) {
            pref.setInteresse(false);
            pref.setConflittoInteresse(false);
        }
        populateTable();
    }
    
    /**
     * Metodo main per testing standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GeneralSubmissionScreen screen = new GeneralSubmissionScreen();
            screen.create();
        });
    }
}
