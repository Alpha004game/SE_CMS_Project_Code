package com.cms.users.account.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * <<boundary>>
 * SkillsSelectionScreen - Schermata per la selezione delle keywords/competenze
 */
public class SkillsSelectionScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    private JPanel keywordsPanel;
    private JButton salvaButton;
    private JScrollPane scrollPane;
    
    // Dati
    private List<String> availableKeywords;
    private List<String> selectedKeywords;
    private List<JButton> keywordButtons;
    
    /**
     * Costruttore
     */
    public SkillsSelectionScreen() {
        this.availableKeywords = new ArrayList<>();
        this.selectedKeywords = new ArrayList<>();
        this.keywordButtons = new ArrayList<>();
        
        // Popola con keywords di esempio
        populateExampleKeywords();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        createKeywordButtons();
    }
    
    /**
     * Popola con keywords di esempio
     */
    private void populateExampleKeywords() {
        availableKeywords.add("Java");
        availableKeywords.add("Python");
        availableKeywords.add("Machine Learning");
        availableKeywords.add("Artificial Intelligence");
        availableKeywords.add("Data Science");
        availableKeywords.add("Web Development");
        availableKeywords.add("Software Engineering");
        availableKeywords.add("Database");
        availableKeywords.add("Cloud Computing");
        availableKeywords.add("Cybersecurity");
        availableKeywords.add("Mobile Development");
        availableKeywords.add("DevOps");
        availableKeywords.add("Blockchain");
        availableKeywords.add("Internet of Things");
        availableKeywords.add("Computer Vision");
        availableKeywords.add("Natural Language Processing");
        availableKeywords.add("Big Data");
        availableKeywords.add("Robotics");
        availableKeywords.add("Quantum Computing");
        availableKeywords.add("Algorithm Design");
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        setTitle("Seleziona le keywords");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Bottoni header
        homeButton = createHeaderButton("🏠");
        notificheButton = createHeaderButton("🔔");
        profiloButton = createHeaderButton("👤");
        
        // Pannello per le keywords
        keywordsPanel = new JPanel();
        keywordsPanel.setBackground(Color.WHITE);
        keywordsPanel.setLayout(new GridBagLayout());
        
        // ScrollPane per il pannello keywords
        scrollPane = new JScrollPane(keywordsPanel);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Bottone Salva
        salvaButton = new JButton("Salva");
        salvaButton.setBackground(Color.ORANGE);
        salvaButton.setForeground(Color.BLACK);
        salvaButton.setFont(new Font("Arial", Font.BOLD, 14));
        salvaButton.setFocusPainted(false);
        salvaButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        salvaButton.setPreferredSize(new Dimension(120, 45));
    }
    
    /**
     * Crea un bottone per l'header
     */
    private JButton createHeaderButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.ORANGE);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setPreferredSize(new Dimension(50, 40));
        return button;
    }
    
    /**
     * Crea un bottone per le keywords
     */
    private JButton createKeywordButton(String keyword) {
        JButton button = new JButton(keyword);
        button.setBackground(new Color(200, 200, 200));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        
        // Dimensione fissa per tutti i bottoni
        button.setPreferredSize(new Dimension(140, 35));
        button.setMinimumSize(new Dimension(140, 35));
        button.setMaximumSize(new Dimension(140, 35));
        
        // Aggiunge listener per la selezione
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleKeywordSelection(keyword, button);
            }
        });
        
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        // Titolo
        JLabel titleLabel = new JLabel("Seleziona le keywords");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Pannello centrale con keywords
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottone Salva in basso
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        bottomPanel.add(salvaButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Crea i bottoni delle keywords in griglia
     */
    private void createKeywordButtons() {
        keywordsPanel.removeAll();
        keywordButtons.clear();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        
        int columns = 7; // 7 bottoni per riga come nel mockup
        int row = 0;
        int col = 0;
        
        for (String keyword : availableKeywords) {
            JButton keywordButton = createKeywordButton(keyword);
            keywordButtons.add(keywordButton);
            
            gbc.gridx = col;
            gbc.gridy = row;
            keywordsPanel.add(keywordButton, gbc);
            
            col++;
            if (col >= columns) {
                col = 0;
                row++;
            }
        }
        
        keywordsPanel.revalidate();
        keywordsPanel.repaint();
    }
    
    /**
     * Gestisce la selezione/deselezione di una keyword
     */
    private void toggleKeywordSelection(String keyword, JButton button) {
        if (selectedKeywords.contains(keyword)) {
            // Deseleziona
            selectedKeywords.remove(keyword);
            button.setBackground(new Color(200, 200, 200));
            button.setForeground(Color.BLACK);
        } else {
            // Seleziona
            selectedKeywords.add(keyword);
            button.setBackground(Color.ORANGE);
            button.setForeground(Color.BLACK);
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
            JOptionPane.showMessageDialog(this, "Apertura menu notifiche...");
        });
        
        // Bottone profilo
        profiloButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Apertura profilo utente...");
        });
        
        // Bottone Salva
        salvaButton.addActionListener(e -> {
            salvaButton();
        });
    }
    
    // Metodi pubblici dell'interfaccia
    
    /**
     * Crea e mostra la schermata
     */
    public void create() {
        setVisible(true);
    }
    
    /**
     * Gestisce il salvataggio delle keywords selezionate
     */
    public void salvaButton() {
        if (selectedKeywords.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Seleziona almeno una keyword prima di salvare.",
                "Selezione richiesta",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Conferma salvataggio
        String message = "Salvare le seguenti keywords?\n\n" + 
                        String.join(", ", selectedKeywords);
        
        int result = JOptionPane.showConfirmDialog(this,
            message,
            "Conferma Salvataggio",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            // Chiudi questa finestra
            dispose();
            
            // Mostra success screen
            try {
                Class<?> successScreenClass = Class.forName("com.cms.users.Commons.SuccessScreen");
                Object successScreen = successScreenClass.getDeclaredConstructor(String.class)
                    .newInstance("Keywords salvate\ncon successo");
                
                java.lang.reflect.Method displayMethod = successScreenClass.getMethod("displaySuccessMessage");
                displayMethod.invoke(successScreen);
            } catch (Exception ex) {
                // Fallback: usa dialog semplice
                JOptionPane.showMessageDialog(null,
                    "Keywords salvate con successo:\n" + String.join("\n", selectedKeywords),
                    "Salvataggio completato",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    // Metodi di utilità
    
    /**
     * Imposta le keywords disponibili
     */
    public void setAvailableKeywords(List<String> keywords) {
        if (keywords != null) {
            this.availableKeywords.clear();
            this.availableKeywords.addAll(keywords);
            createKeywordButtons();
        }
    }
    
    /**
     * Imposta le keywords preselezionate
     */
    public void setSelectedKeywords(List<String> keywords) {
        if (keywords != null) {
            this.selectedKeywords.clear();
            this.selectedKeywords.addAll(keywords);
            updateButtonStates();
        }
    }
    
    /**
     * Ottiene le keywords selezionate
     */
    public List<String> getSelectedKeywords() {
        return new ArrayList<>(selectedKeywords);
    }
    
    /**
     * Aggiunge una keyword alla lista disponibile
     */
    public void addKeyword(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty() && !availableKeywords.contains(keyword)) {
            availableKeywords.add(keyword);
            createKeywordButtons();
        }
    }
    
    /**
     * Rimuove una keyword dalla lista disponibile
     */
    public void removeKeyword(String keyword) {
        if (availableKeywords.remove(keyword)) {
            selectedKeywords.remove(keyword);
            createKeywordButtons();
        }
    }
    
    /**
     * Aggiorna lo stato dei bottoni in base alle selezioni
     */
    private void updateButtonStates() {
        for (int i = 0; i < keywordButtons.size() && i < availableKeywords.size(); i++) {
            JButton button = keywordButtons.get(i);
            String keyword = availableKeywords.get(i);
            
            if (selectedKeywords.contains(keyword)) {
                button.setBackground(Color.ORANGE);
                button.setForeground(Color.BLACK);
            } else {
                button.setBackground(new Color(200, 200, 200));
                button.setForeground(Color.BLACK);
            }
        }
    }
    
    /**
     * Pulisce tutte le selezioni
     */
    public void clearSelections() {
        selectedKeywords.clear();
        updateButtonStates();
    }
    
    /**
     * Seleziona tutte le keywords
     */
    public void selectAllKeywords() {
        selectedKeywords.clear();
        selectedKeywords.addAll(availableKeywords);
        updateButtonStates();
    }
    
    /**
     * Metodo main per test standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SkillsSelectionScreen screen = new SkillsSelectionScreen();
            screen.setVisible(true);
        });
    }
}
