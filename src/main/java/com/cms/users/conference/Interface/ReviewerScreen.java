package com.cms.users.conference.Interface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <<boundary>>
 * ReviewerScreen - Schermata per l'assegnazione degli articoli ai revisori
 */
public class ReviewerScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    
    // Tabella per l'assegnazione
    private JTable assignmentTable;
    private DefaultTableModel tableModel;
    
    // Bottoni di azione
    private JButton assegnaPerPreferenzeButton;
    private JButton assegnaAutomaticamenteButton;
    private JButton assegnaButton;
    
    // Dati per la gestione
    private String conferenceId;
    private List<ReviewerData> reviewers;
    private List<ArticleData> articles;
    private boolean[][] assignments; // Matrice revisore-articolo
    
    /**
     * Classe per i dati del revisore
     */
    public static class ReviewerData {
        public String id;
        public String name;
        public String email;
        public String expertise;
        
        public ReviewerData(String id, String name, String email, String expertise) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.expertise = expertise;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
    
    /**
     * Classe per i dati dell'articolo
     */
    public static class ArticleData {
        public String id;
        public String title;
        public String authors;
        public String keywords;
        
        public ArticleData(String id, String title, String authors, String keywords) {
            this.id = id;
            this.title = title;
            this.authors = authors;
            this.keywords = keywords;
        }
        
        @Override
        public String toString() {
            return title;
        }
    }
    
    /**
     * Costruttore di default
     */
    public ReviewerScreen() {
        this.conferenceId = "CONF" + (int)(Math.random() * 10000);
        initializeData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateTable();
    }
    
    /**
     * Costruttore con ID conferenza
     */
    public ReviewerScreen(String conferenceId) {
        this.conferenceId = conferenceId != null ? conferenceId : "CONF" + (int)(Math.random() * 10000);
        initializeData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateTable();
    }
    
    /**
     * Inizializza i dati di esempio
     */
    private void initializeData() {
        // Revisori di esempio
        reviewers = new ArrayList<>();
        reviewers.add(new ReviewerData("REV001", "Prof. Mario Rossi", "mario.rossi@univ.it", "Machine Learning"));
        reviewers.add(new ReviewerData("REV002", "Dr. Luigi Verdi", "luigi.verdi@univ.it", "Software Engineering"));
        reviewers.add(new ReviewerData("REV003", "Prof.ssa Anna Bianchi", "anna.bianchi@univ.it", "Artificial Intelligence"));
        reviewers.add(new ReviewerData("REV004", "Dr. Marco Neri", "marco.neri@univ.it", "Data Science"));
        
        // Articoli di esempio
        articles = new ArrayList<>();
        articles.add(new ArticleData("ART001", "Articolo 1", "Smith, J.", "ML, Testing"));
        articles.add(new ArticleData("ART002", "Articolo 2", "Brown, A.", "Agile, Management"));
        articles.add(new ArticleData("ART003", "Articolo 3", "Johnson, M.", "AI, Code Review"));
        articles.add(new ArticleData("ART004", "Articolo 4", "Davis, L.", "Cloud, Security"));
        
        // Inizializza matrice assegnazioni
        assignments = new boolean[reviewers.size()][articles.size()];
        // Alcune assegnazioni di esempio
        if (reviewers.size() > 0 && articles.size() > 0) {
            assignments[0][0] = true; // Mario Rossi -> Articolo 1
            assignments[1][1] = true; // Luigi Verdi -> Articolo 2
        }
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        // Configurazione finestra principale
        setTitle("CMS - Assegna Articoli ai Revisori");
        setSize(1000, 700);
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
        
        // Tabella
        initializeTable();
        
        // Bottoni di azione
        initializeActionButtons();
    }
    
    /**
     * Inizializza la tabella
     */
    private void initializeTable() {
        // Crea le colonne: "Revisore" + una colonna per ogni articolo
        List<String> columnNames = new ArrayList<>();
        columnNames.add("Revisore");
        for (int i = 0; i < articles.size(); i++) {
            columnNames.add("Articolo"); // Come nel mockup, tutte le colonne articolo hanno lo stesso nome
        }
        
        tableModel = new DefaultTableModel(columnNames.toArray(new String[0]), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0; // Solo le colonne degli articoli sono editabili
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return String.class; // Prima colonna: nome revisore
                } else {
                    return Boolean.class; // Altre colonne: checkbox per assegnazione
                }
            }
        };
        
        assignmentTable = new JTable(tableModel);
        assignmentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        assignmentTable.setRowHeight(30);
        assignmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Imposta larghezza colonne
        assignmentTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Revisore
        for (int i = 1; i < assignmentTable.getColumnCount(); i++) {
            assignmentTable.getColumnModel().getColumn(i).setPreferredWidth(100); // Articoli
        }
        
        // Renderer personalizzato per le celle grigie degli articoli
        for (int i = 1; i < assignmentTable.getColumnCount(); i++) {
            assignmentTable.getColumnModel().getColumn(i).setCellRenderer(new GrayCheckboxRenderer());
        }
    }
    
    /**
     * Renderer per checkbox grigie nelle colonne degli articoli
     */
    private class GrayCheckboxRenderer extends DefaultTableCellRenderer {
        private JCheckBox checkbox;
        
        public GrayCheckboxRenderer() {
            checkbox = new JCheckBox();
            checkbox.setHorizontalAlignment(JCheckBox.CENTER);
            checkbox.setBackground(new Color(240, 240, 240)); // Grigio chiaro
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            checkbox.setSelected(value != null && (Boolean) value);
            checkbox.setBackground(isSelected ? table.getSelectionBackground() : new Color(240, 240, 240));
            
            return checkbox;
        }
    }
    
    /**
     * Inizializza i bottoni di azione
     */
    private void initializeActionButtons() {
        // Bottone assegna per preferenze
        assegnaPerPreferenzeButton = new JButton("Assegna revisori per preferenze");
        assegnaPerPreferenzeButton.setBackground(Color.ORANGE);
        assegnaPerPreferenzeButton.setForeground(Color.WHITE);
        assegnaPerPreferenzeButton.setFont(new Font("Arial", Font.BOLD, 12));
        assegnaPerPreferenzeButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        assegnaPerPreferenzeButton.setFocusPainted(false);
        assegnaPerPreferenzeButton.setPreferredSize(new Dimension(220, 35));
        
        // Bottone assegna automaticamente
        assegnaAutomaticamenteButton = new JButton("Assegna automaticamente revisori");
        assegnaAutomaticamenteButton.setBackground(Color.ORANGE);
        assegnaAutomaticamenteButton.setForeground(Color.WHITE);
        assegnaAutomaticamenteButton.setFont(new Font("Arial", Font.BOLD, 12));
        assegnaAutomaticamenteButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        assegnaAutomaticamenteButton.setFocusPainted(false);
        assegnaAutomaticamenteButton.setPreferredSize(new Dimension(220, 35));
        
        // Bottone assegna
        assegnaButton = new JButton("Assegna");
        assegnaButton.setBackground(Color.ORANGE);
        assegnaButton.setForeground(Color.WHITE);
        assegnaButton.setFont(new Font("Arial", Font.BOLD, 12));
        assegnaButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        assegnaButton.setFocusPainted(false);
        assegnaButton.setPreferredSize(new Dimension(100, 35));
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
        JLabel titleLabel = new JLabel("Assegna articoli");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Tabella con scroll
        JScrollPane scrollPane = new JScrollPane(assignmentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottoni di azione
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Crea il pannello con i bottoni di azione
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Prima riga: bottoni assegnazione automatica
        JPanel firstRowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        firstRowPanel.setBackground(Color.WHITE);
        firstRowPanel.add(assegnaPerPreferenzeButton);
        firstRowPanel.add(assegnaAutomaticamenteButton);
        
        // Seconda riga: bottone assegna
        JPanel secondRowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        secondRowPanel.setBackground(Color.WHITE);
        secondRowPanel.add(assegnaButton);
        
        panel.add(firstRowPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(secondRowPanel);
        
        return panel;
    }
    
    /**
     * Configura i gestori di eventi
     */
    private void setupEventHandlers() {
        // Bottone home
        homeButton.addActionListener(e -> {
            dispose();
            // Simulazione navigazione verso HomeScreen
            JOptionPane.showMessageDialog(this, "Navigazione verso HomeScreen", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Bottone notifiche
        notificheButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Apertura menu notifiche", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Bottone profilo
        profiloButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Apertura menu profilo", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Bottone assegna per preferenze
        assegnaPerPreferenzeButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Assegnare automaticamente i revisori basandosi sulle loro preferenze?",
                "Conferma Assegnazione",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                assignByPreferences();
                JOptionPane.showMessageDialog(this, "Revisori assegnati per preferenze con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Bottone assegna automaticamente
        assegnaAutomaticamenteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Assegnare automaticamente i revisori in modo casuale?",
                "Conferma Assegnazione",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                assignAutomatically();
                JOptionPane.showMessageDialog(this, "Revisori assegnati automaticamente con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Bottone assegna
        assegnaButton.addActionListener(e -> {
            saveAssignments();
            JOptionPane.showMessageDialog(this, "Assegnazioni salvate con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    /**
     * Popola la tabella con i dati di assegnazione
     */
    private void populateTable() {
        tableModel.setRowCount(0);
        
        for (int i = 0; i < reviewers.size(); i++) {
            Object[] row = new Object[articles.size() + 1];
            row[0] = reviewers.get(i).name; // Nome revisore
            
            // Stato assegnazioni per ogni articolo
            for (int j = 0; j < articles.size(); j++) {
                row[j + 1] = assignments[i][j];
            }
            
            tableModel.addRow(row);
        }
    }
    
    /**
     * Assegna revisori per preferenze (algoritmo simulato)
     */
    private void assignByPreferences() {
        // Reset assegnazioni
        for (int i = 0; i < assignments.length; i++) {
            for (int j = 0; j < assignments[i].length; j++) {
                assignments[i][j] = false;
            }
        }
        
        // Algoritmo simulato basato su expertise
        for (int i = 0; i < reviewers.size() && i < articles.size(); i++) {
            assignments[i][i % articles.size()] = true;
        }
        
        populateTable();
    }
    
    /**
     * Assegna revisori automaticamente
     */
    private void assignAutomatically() {
        // Reset assegnazioni
        for (int i = 0; i < assignments.length; i++) {
            for (int j = 0; j < assignments[i].length; j++) {
                assignments[i][j] = false;
            }
        }
        
        // Assegnazione casuale
        for (int i = 0; i < reviewers.size(); i++) {
            int randomArticle = (int) (Math.random() * articles.size());
            assignments[i][randomArticle] = true;
        }
        
        populateTable();
    }
    
    /**
     * Salva le assegnazioni correnti
     */
    private void saveAssignments() {
        // Aggiorna la matrice con i valori dalla tabella
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 1; j < tableModel.getColumnCount(); j++) {
                Boolean value = (Boolean) tableModel.getValueAt(i, j);
                assignments[i][j - 1] = value != null && value;
            }
        }
        
        // Qui si potrebbe implementare il salvataggio su database
        System.out.println("Assegnazioni salvate per conferenza: " + conferenceId);
        for (int i = 0; i < reviewers.size(); i++) {
            for (int j = 0; j < articles.size(); j++) {
                if (assignments[i][j]) {
                    System.out.println("Revisore " + reviewers.get(i).name + " -> Articolo " + articles.get(j).title);
                }
            }
        }
    }
    
    /**
     * Metodo main per test standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ReviewerScreen().setVisible(true);
        });
    }
}
