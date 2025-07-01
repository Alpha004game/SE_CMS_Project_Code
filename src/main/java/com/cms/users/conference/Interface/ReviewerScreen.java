package com.cms.users.conference.Interface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
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
    
    // Riferimento al ConferenceControl per delegare le operazioni
    private com.cms.users.conference.Control.ConferenceControl conferenceControl;
    
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
     * Imposta il riferimento al ConferenceControl
     */
    public void setConferenceControl(com.cms.users.conference.Control.ConferenceControl conferenceControl) {
        this.conferenceControl = conferenceControl;
    }
    
    /**
     * Imposta l'ID della conferenza
     * @param conferenceId ID della conferenza
     */
    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
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
        // Crea le colonne: "Revisore" + una colonna per ogni articolo con il suo titolo
        List<String> columnNames = new ArrayList<>();
        columnNames.add("Revisore");
        for (int i = 0; i < articles.size(); i++) {
            columnNames.add(articles.get(i).title); // Usa il titolo specifico dell'articolo
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
    private class GrayCheckboxRenderer extends JCheckBox implements TableCellRenderer {
        public GrayCheckboxRenderer() {
            setHorizontalAlignment(JCheckBox.CENTER);
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                    boolean isSelected, boolean hasFocus,
                                                    int row, int column) {
            if (value instanceof Boolean) {
                setSelected((Boolean) value);
            }
            
            // Se è una cella disabilitata, usa lo sfondo grigio
            if (!table.isCellEditable(row, column)) {
                setBackground(new Color(240, 240, 240));
                setEnabled(false);
            } else {
                setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                setEnabled(true);
            }
            
            return this;
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
        // Bottone assegna per preferenze
        assegnaPerPreferenzeButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Assegnare automaticamente i revisori basandosi sulle loro preferenze?",
                "Conferma Assegnazione",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (conferenceControl != null) {
                    try {
                        int idConferenza = Integer.parseInt(conferenceId);
                        conferenceControl.assegnaPerPreferenze(idConferenza);
                        // Ricarica i dati per mostrare le nuove assegnazioni
                        loadRealData();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, 
                            "Errore: ID conferenza non valido", 
                            "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Errore: ConferenceControl non disponibile", 
                        "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Bottone assegna automaticamente
        assegnaAutomaticamenteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Assegnare automaticamente i revisori in modo casuale?",
                "Conferma Assegnazione",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (conferenceControl != null) {
                    try {
                        int idConferenza = Integer.parseInt(conferenceId);
                        conferenceControl.assegnaAutomaticamenteRevisori(idConferenza);
                        // Ricarica i dati per mostrare le nuove assegnazioni
                        loadRealData();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, 
                            "Errore: ID conferenza non valido", 
                            "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Errore: ConferenceControl non disponibile", 
                        "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Bottone assegna
        assegnaButton.addActionListener(e -> {
            if (conferenceControl != null) {
                saveAssignments();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Errore: ConferenceControl non disponibile", 
                    "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Gestione eventi modifica checkbox
        assignmentTable.addPropertyChangeListener(evt -> {
            if ("tableCellEditor".equals(evt.getPropertyName()) && evt.getNewValue() == null) {
                int row = assignmentTable.getSelectedRow();
                int column = assignmentTable.getSelectedColumn();
                
                if (row >= 0 && column > 0) { // Escludi intestazione e prima colonna
                    boolean isSelected = (Boolean) tableModel.getValueAt(row, column);
                    handleAssignmentChange(row, column - 1, isSelected);
                }
            }
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
        
        // Aggiungiamo un item listener per tutti i checkbox nella tabella
        for (int i = 0; i < assignmentTable.getRowCount(); i++) {
            for (int j = 1; j < assignmentTable.getColumnCount(); j++) {
                final int rowIndex = i;
                final int colIndex = j;
                
                // Configuriamo l'editor con l'evento che gestisce sia selezione che deselezione
                JCheckBox checkBox = new JCheckBox();
                checkBox.setHorizontalAlignment(JCheckBox.CENTER);
                
                checkBox.addActionListener(e -> {
                    boolean selected = checkBox.isSelected();
                    tableModel.setValueAt(selected, rowIndex, colIndex);
                    handleAssignmentChange(rowIndex, colIndex - 1, selected);
                });
                
                assignmentTable.getColumnModel().getColumn(j).setCellEditor(new DefaultCellEditor(checkBox));
            }
        }
    }
    
    /**
     * Assegna revisori per preferenze (algoritmo simulato)
     */
    /**
     * Salva le assegnazioni correnti delegando al ConferenceControl
     */
    private void saveAssignments() {
        // Aggiorna la matrice con i valori dalla tabella
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 1; j < tableModel.getColumnCount(); j++) {
                Boolean value = (Boolean) tableModel.getValueAt(i, j);
                if (i < assignments.length && (j - 1) < assignments[i].length) {
                    assignments[i][j - 1] = value != null && value;
                }
            }
        }
        
        // Delega al ConferenceControl per salvare nel database
        if (conferenceControl != null) {
            conferenceControl.salvaAssegnazioni(assignments, reviewers, articles);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Errore: ConferenceControl non disponibile", 
                "Errore", JOptionPane.ERROR_MESSAGE);
        }
        this.dispose();
    }
    
    /**
     * Carica i dati reali dal database tramite il ConferenceControl
     */
    public void loadRealData() {
        if (conferenceControl != null) {
            try {
                int idConferenza = Integer.parseInt(conferenceId);
                
                // Carica revisori reali
                this.reviewers = conferenceControl.ottieniRevisori(idConferenza);
                
                // Carica articoli reali
                this.articles = conferenceControl.ottieniArticoli(idConferenza);
                
                // Carica le assegnazioni esistenti dal database
                if (!reviewers.isEmpty() && !articles.isEmpty()) {
                    assignments = conferenceControl.ottieniAssegnazioniEsistenti(idConferenza, reviewers, articles);
                    System.out.println("Caricate " + contaAssegnazioniAttive() + " assegnazioni esistenti");
                } else {
                    assignments = new boolean[reviewers.size()][articles.size()];
                }
                
                // Aggiorna la tabella con i nuovi dati
                updateTableStructure();
                populateTable();
                
                System.out.println("Dati reali caricati: " + reviewers.size() + " revisori, " + articles.size() + " articoli");
                
            } catch (NumberFormatException e) {
                System.err.println("Errore: ID conferenza non valido: " + conferenceId);
            } catch (Exception e) {
                System.err.println("Errore durante il caricamento dei dati reali: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("ConferenceControl non impostato, impossibile caricare dati reali");
        }
    }
    
    /**
     * Aggiorna la struttura della tabella quando cambiano i dati
     */
    private void updateTableStructure() {
        // Crea le colonne: "Revisore" + una colonna per ogni articolo con il suo titolo
        List<String> columnNames = new ArrayList<>();
        columnNames.add("Revisore");
        for (int i = 0; i < articles.size(); i++) {
            columnNames.add(articles.get(i).title); // Usa il titolo specifico dell'articolo
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
        
        assignmentTable.setModel(tableModel);
        
        // Imposta larghezza colonne
        if (assignmentTable.getColumnCount() > 0) {
            assignmentTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Revisore
            for (int i = 1; i < assignmentTable.getColumnCount(); i++) {
                assignmentTable.getColumnModel().getColumn(i).setPreferredWidth(100); // Articoli
                assignmentTable.getColumnModel().getColumn(i).setCellRenderer(new GrayCheckboxRenderer());
            }
        }
    }
    
    /**
     * Conta il numero di assegnazioni attive nella matrice
     */
    private int contaAssegnazioniAttive() {
        int count = 0;
        if (assignments != null) {
            for (int i = 0; i < assignments.length; i++) {
                for (int j = 0; j < assignments[i].length; j++) {
                    if (assignments[i][j]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    
    /**
     * Metodo main per test standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ReviewerScreen().setVisible(true);
        });
    }
    
    /**
     * Gestisce le modifiche alle assegnazioni quando un checkbox viene cliccato
     * @param revisoreIdx Indice del revisore
     * @param articoloIdx Indice dell'articolo
     * @param isSelected Stato del checkbox
     */
    private void handleAssignmentChange(int revisoreIdx, int articoloIdx, boolean isSelected) {
        try {
            if (conferenceControl == null) {
                JOptionPane.showMessageDialog(this, 
                    "Errore: Nessun controller configurato", 
                    "Errore", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int idRevisore = Integer.parseInt(reviewers.get(revisoreIdx).id);
            int idArticolo = Integer.parseInt(articles.get(articoloIdx).id);
            
            if (isSelected) {
                // Se il checkbox è stato selezionato, aggiungiamo il revisore all'articolo
                conferenceControl.setRevisoreArticolo(idArticolo, idRevisore);
                assignments[revisoreIdx][articoloIdx] = true;
                System.out.println("Revisore " + reviewers.get(revisoreIdx).name + 
                                " assegnato all'articolo: " + articles.get(articoloIdx).title);
            } else {
                // Se il checkbox è stato deselezionato, rimuoviamo il revisore dall'articolo
                conferenceControl.rimuoviRevisoreArticolo(idArticolo, idRevisore);
                assignments[revisoreIdx][articoloIdx] = false;
                System.out.println("Revisore " + reviewers.get(revisoreIdx).name + 
                                " rimosso dall'articolo: " + articles.get(articoloIdx).title);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Errore durante l'aggiornamento dell'assegnazione: " + e.getMessage(), 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}