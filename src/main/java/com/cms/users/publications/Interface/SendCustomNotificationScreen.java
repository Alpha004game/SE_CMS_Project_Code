package com.cms.users.publications.Interface;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <<boundary>>
 * SendCustomNotificationScreen - Schermata per l'invio di notifiche personalizzate
 */
public class SendCustomNotificationScreen extends JFrame {
    
    // Enumerazione per i ruoli utente
    public enum UserRole {
        CHAIR,    // Chair con selezione destinatari
        EDITORE   // Editore senza selezione destinatari
    }
    
    // Componenti dell'interfaccia
    private JButton homeButton;
    private JButton notificheButton;
    private JButton profiloButton;
    private JTextArea messageArea;
    private JComboBox<String> recipientsComboBox;
    private JList<String> recipientsList;
    private JScrollPane recipientsScrollPane;
    private JButton inviaButton;
    private JPanel recipientsPanel;
    
    // Dati
    private UserRole currentRole;
    private List<String> availableRecipients;
    private List<String> selectedRecipients;
    
    /**
     * Costruttore con ruolo utente
     */
    public SendCustomNotificationScreen(UserRole role) {
        this.currentRole = role;
        this.availableRecipients = new ArrayList<>();
        this.selectedRecipients = new ArrayList<>();
        
        // Popola con dati di esempio
        populateExampleRecipients();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateInterfaceForRole();
    }
    
    /**
     * Costruttore di default (Editore)
     */
    public SendCustomNotificationScreen() {
        this(UserRole.EDITORE);
    }
    
    /**
     * Popola con destinatari di esempio
     */
    private void populateExampleRecipients() {
        if (currentRole == UserRole.CHAIR) {
            availableRecipients.add("Tutti gli autori");
            availableRecipients.add("Tutti i revisori");
            availableRecipients.add("Membro 1");
            availableRecipients.add("Membro 2");
            availableRecipients.add("Membro 3");
            availableRecipients.add("Membro 4");
        }
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        setTitle("Scrivi testo della notifica");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Bottoni header
        homeButton = createHeaderButton("🏠");
        notificheButton = createHeaderButton("🔔");
        profiloButton = createHeaderButton("👤");
        
        // Area di testo per il messaggio
        messageArea = new JTextArea(10, 40);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        messageArea.setBackground(new Color(200, 200, 200));
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        
        // ComboBox per selezione destinatari (Chair)
        recipientsComboBox = new JComboBox<>();
        recipientsComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        recipientsComboBox.setBackground(Color.WHITE);
        recipientsComboBox.setPreferredSize(new Dimension(200, 30));
        
        // Lista destinatari selezionati (Chair)
        recipientsList = new JList<>();
        recipientsList.setFont(new Font("Arial", Font.PLAIN, 14));
        recipientsList.setBackground(new Color(200, 200, 200));
        recipientsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        recipientsScrollPane = new JScrollPane(recipientsList);
        recipientsScrollPane.setPreferredSize(new Dimension(200, 300));
        recipientsScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // Bottone Invia
        inviaButton = new JButton("Invia");
        inviaButton.setBackground(Color.ORANGE);
        inviaButton.setForeground(Color.BLACK);
        inviaButton.setFont(new Font("Arial", Font.BOLD, 14));
        inviaButton.setFocusPainted(false);
        inviaButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        inviaButton.setPreferredSize(new Dimension(120, 45));
        
        // Pannello destinatari
        recipientsPanel = new JPanel();
        recipientsPanel.setBackground(Color.WHITE);
        recipientsPanel.setLayout(new BorderLayout());
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
        JLabel titleLabel = new JLabel("Scrivi testo della notifica");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Pannello centrale con contenuto
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Bottone Invia in basso
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        bottomPanel.add(inviaButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Crea il pannello centrale con il contenuto
     */
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        
        // Pannello sinistro con messaggio
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        
        JLabel messageLabel = new JLabel("Messaggio:");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        leftPanel.add(messageLabel, BorderLayout.NORTH);
        
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messageScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        leftPanel.add(messageScrollPane, BorderLayout.CENTER);
        
        centerPanel.add(leftPanel, BorderLayout.CENTER);
        
        // Pannello destro con destinatari (solo per Chair)
        if (currentRole == UserRole.CHAIR) {
            centerPanel.add(recipientsPanel, BorderLayout.EAST);
        }
        
        return centerPanel;
    }
    
    /**
     * Aggiorna l'interfaccia in base al ruolo
     */
    private void updateInterfaceForRole() {
        if (currentRole == UserRole.CHAIR) {
            setupRecipientsPanel();
            recipientsPanel.setVisible(true);
        } else {
            recipientsPanel.setVisible(false);
        }
    }
    
    /**
     * Configura il pannello destinatari per il Chair
     */
    private void setupRecipientsPanel() {
        recipientsPanel.removeAll();
        recipientsPanel.setLayout(new BorderLayout());
        
        // Header del pannello destinatari
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(200, 200, 200));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel headerLabel = new JLabel("Destinatari");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // ComboBox per aggiungere destinatari
        populateRecipientsComboBox();
        
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        headerPanel.add(recipientsComboBox, BorderLayout.SOUTH);
        
        recipientsPanel.add(headerPanel, BorderLayout.NORTH);
        recipientsPanel.add(recipientsScrollPane, BorderLayout.CENTER);
        
        recipientsPanel.revalidate();
        recipientsPanel.repaint();
    }
    
    /**
     * Popola la ComboBox dei destinatari
     */
    private void populateRecipientsComboBox() {
        recipientsComboBox.removeAllItems();
        recipientsComboBox.addItem("Destinatari");
        
        for (String recipient : availableRecipients) {
            recipientsComboBox.addItem(recipient);
        }
        
        recipientsComboBox.setSelectedIndex(0);
    }
    
    /**
     * Aggiorna la lista dei destinatari selezionati
     */
    private void updateSelectedRecipientsList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String recipient : selectedRecipients) {
            listModel.addElement(recipient);
        }
        recipientsList.setModel(listModel);
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
        
        // ComboBox destinatari (solo per Chair)
        if (currentRole == UserRole.CHAIR) {
            recipientsComboBox.addActionListener(e -> {
                int selectedIndex = recipientsComboBox.getSelectedIndex();
                if (selectedIndex > 0) { // Ignora "Destinatari"
                    String selectedRecipient = (String) recipientsComboBox.getSelectedItem();
                    if (!selectedRecipients.contains(selectedRecipient)) {
                        selectedRecipients.add(selectedRecipient);
                        updateSelectedRecipientsList();
                    }
                    recipientsComboBox.setSelectedIndex(0); // Reset alla voce "Destinatari"
                }
            });
        }
        
        // Bottone Invia
        inviaButton.addActionListener(e -> {
            sendButton();
        });
    }
    
    // Metodi dell'interfaccia originale
    
    /**
     * Crea e mostra la schermata
     */
    public void create() {
        setVisible(true);
    }
    
    /**
     * Compila i campi della notifica
     */
    public String compilaCampi(String utenti, String testo) {
        if (utenti != null && !utenti.trim().isEmpty()) {
            setSenders(utenti);
        }
        if (testo != null && !testo.trim().isEmpty()) {
            setMessage(testo);
        }
        
        return getInfos();
    }
    
    /**
     * Gestisce l'invio della notifica
     */
    public void sendButton() {
        String message = messageArea.getText().trim();
        
        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Inserisci il testo del messaggio.",
                "Campo obbligatorio",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (currentRole == UserRole.CHAIR && selectedRecipients.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Seleziona almeno un destinatario.",
                "Destinatari richiesti",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Conferma invio
        String confirmMessage;
        if (currentRole == UserRole.CHAIR) {
            confirmMessage = "Inviare la notifica a " + selectedRecipients.size() + " destinatari?";
        } else {
            confirmMessage = "Inviare la notifica?";
        }
        
        int result = JOptionPane.showConfirmDialog(this,
            confirmMessage,
            "Conferma Invio",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            String successMessage;
            if (currentRole == UserRole.CHAIR) {
                successMessage = "Notifica inviata con successo a:\n" + String.join("\n", selectedRecipients);
            } else {
                successMessage = "Notifica inviata con successo.";
            }
            
            JOptionPane.showMessageDialog(this,
                successMessage,
                "Invio completato",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Reset dei campi
            messageArea.setText("");
            selectedRecipients.clear();
            if (currentRole == UserRole.CHAIR) {
                updateSelectedRecipientsList();
            }
        }
    }
    
    /**
     * Imposta i destinatari
     */
    public void setSenders(String authorsList) {
        if (currentRole == UserRole.CHAIR && authorsList != null) {
            selectedRecipients.clear();
            String[] authors = authorsList.split(",");
            for (String author : authors) {
                String trimmedAuthor = author.trim();
                if (!trimmedAuthor.isEmpty() && !selectedRecipients.contains(trimmedAuthor)) {
                    selectedRecipients.add(trimmedAuthor);
                }
            }
            updateSelectedRecipientsList();
        }
    }
    
    /**
     * Imposta il messaggio
     */
    public void setMessage(String text) {
        if (text != null) {
            messageArea.setText(text);
        }
    }
    
    /**
     * Ottiene le informazioni della notifica
     */
    public String getInfos() {
        StringBuilder info = new StringBuilder();
        info.append("Messaggio: ").append(messageArea.getText());
        
        if (currentRole == UserRole.CHAIR && !selectedRecipients.isEmpty()) {
            info.append("\nDestinatari: ").append(String.join(", ", selectedRecipients));
        }
        
        return info.toString();
    }
    
    // Metodi di utilità
    
    /**
     * Imposta il ruolo utente
     */
    public void setUserRole(UserRole role) {
        this.currentRole = role;
        populateExampleRecipients();
        updateInterfaceForRole();
    }
    
    /**
     * Ottiene il ruolo utente corrente
     */
    public UserRole getUserRole() {
        return currentRole;
    }
    
    /**
     * Aggiunge un destinatario alla lista
     */
    public void addRecipient(String recipient) {
        if (currentRole == UserRole.CHAIR && recipient != null && !recipient.trim().isEmpty()) {
            String trimmedRecipient = recipient.trim();
            if (!selectedRecipients.contains(trimmedRecipient)) {
                selectedRecipients.add(trimmedRecipient);
                updateSelectedRecipientsList();
            }
        }
    }
    
    /**
     * Rimuove un destinatario dalla lista
     */
    public void removeRecipient(String recipient) {
        if (currentRole == UserRole.CHAIR) {
            selectedRecipients.remove(recipient);
            updateSelectedRecipientsList();
        }
    }
    
    /**
     * Metodo main per test standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test Chair
            SendCustomNotificationScreen screen1 = new SendCustomNotificationScreen(UserRole.CHAIR);
            screen1.setLocation(0, 0);
            screen1.setVisible(true);
            
            // Test Editore
            SendCustomNotificationScreen screen2 = new SendCustomNotificationScreen(UserRole.EDITORE);
            screen2.setLocation(450, 0);
            screen2.setVisible(true);
        });
    }
}
