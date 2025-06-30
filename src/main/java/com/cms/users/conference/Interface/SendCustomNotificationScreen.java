package com.cms.users.conference.Interface;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.cms.users.conference.Control.ConferenceControl;

/**
 * <<boundary>>
 * SendCustomNotificationScreen - Schermata per l'invio di notifiche personalizzate
 */
public class SendCustomNotificationScreen extends JFrame {
    
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
    private List<String> availableRecipients;
    private List<String> selectedRecipients;
    private ConferenceControl conferenceControl;
    
    /**
     * Costruttore con riferimento al controllo
     */
    public SendCustomNotificationScreen(ConferenceControl control) {
        this.conferenceControl = control;
        this.availableRecipients = new ArrayList<>();
        this.selectedRecipients = new ArrayList<>();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadAvailableRecipients();
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
        
        // ComboBox per selezione destinatari
        recipientsComboBox = new JComboBox<>();
        recipientsComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        recipientsComboBox.setBackground(Color.WHITE);
        recipientsComboBox.setPreferredSize(new Dimension(200, 30));
        
        // Lista destinatari selezionati
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
        setupRecipientsPanel();
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
        centerPanel.add(recipientsPanel, BorderLayout.EAST);
        
        return centerPanel;
    }
    
    /**
     * Configura il pannello destinatari
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
        
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        headerPanel.add(recipientsComboBox, BorderLayout.SOUTH);
        
        recipientsPanel.add(headerPanel, BorderLayout.NORTH);
        recipientsPanel.add(recipientsScrollPane, BorderLayout.CENTER);
        
        recipientsPanel.revalidate();
        recipientsPanel.repaint();
    }
    
    /**
     * Carica i destinatari disponibili
     */
    private void loadAvailableRecipients() {
        availableRecipients.clear();
        availableRecipients.add("Tutti gli autori");
        availableRecipients.add("Tutti i revisori");
        availableRecipients.add("Tutti i partecipanti");
        availableRecipients.add("Seleziona utenti specifici...");
        
        // Popola la ComboBox
        recipientsComboBox.removeAllItems();
        recipientsComboBox.addItem("Seleziona destinatari...");
        
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
        homeButton.addActionListener(e -> handleHomeAction());
        
        // Bottone notifiche
        notificheButton.addActionListener(e -> handleNotificheAction());
        
        // Bottone profilo
        profiloButton.addActionListener(e -> handleProfiloAction());
        
        // ComboBox destinatari
        recipientsComboBox.addActionListener(e -> {
            int selectedIndex = recipientsComboBox.getSelectedIndex();
            if (selectedIndex > 0) { // Salta "Seleziona destinatari..."
                String selectedRecipient = (String) recipientsComboBox.getSelectedItem();
                
                if ("Seleziona utenti specifici...".equals(selectedRecipient)) {
                    // Apri finestra di selezione utenti specifici
                    showSpecificUsersSelectionDialog();
                } else if (!selectedRecipients.contains(selectedRecipient)) {
                    selectedRecipients.add(selectedRecipient);
                    updateSelectedRecipientsList();
                }
                recipientsComboBox.setSelectedIndex(0);
            }
        });
        
        // Lista destinatari - rimozione con doppio click
        recipientsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String selectedValue = recipientsList.getSelectedValue();
                    if (selectedValue != null) {
                        selectedRecipients.remove(selectedValue);
                        updateSelectedRecipientsList();
                    }
                }
            }
        });
        
        // Bottone Invia
        inviaButton.addActionListener(e -> handleInviaAction());
    }
    
    /**
     * Gestisce l'azione del bottone Home
     */
    private void handleHomeAction() {
        dispose();
        // Torna alla home
    }
    
    /**
     * Gestisce l'azione del bottone Notifiche
     */
    private void handleNotificheAction() {
        // Implementazione navigazione notifiche
    }
    
    /**
     * Gestisce l'azione del bottone Profilo
     */
    private void handleProfiloAction() {
        // Implementazione navigazione profilo
    }
    
    /**
     * Gestisce l'invio della comunicazione
     */
    private void handleInviaAction() {
        String message = messageArea.getText().trim();
        
        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Il messaggio non può essere vuoto!",
                "Errore",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (selectedRecipients.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Seleziona almeno un destinatario!",
                "Errore",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Conferma invio
        String recipientsList = String.join(", ", selectedRecipients);
        String confirmMessage = "Sei sicuro di voler inviare la comunicazione a:\n" + 
                               recipientsList + "\n\n" +
                               "Messaggio: " + message.substring(0, Math.min(50, message.length())) + 
                               (message.length() > 50 ? "..." : "");
        
        int result = JOptionPane.showConfirmDialog(this,
            confirmMessage,
            "Conferma Invio",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            // Delega al controllo l'invio effettivo
            if (conferenceControl != null) {
                conferenceControl.inviaMessaggioPersonalizzato(message, selectedRecipients);
                
                JOptionPane.showMessageDialog(this,
                    "Comunicazione inviata con successo!",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Errore: controllo non disponibile",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Mostra finestra per selezionare utenti specifici della conferenza
     */
    private void showSpecificUsersSelectionDialog() {
        try {
            if (conferenceControl == null || ConferenceControl.getConferenza() == null) {
                JOptionPane.showMessageDialog(this, 
                    "Errore: nessuna conferenza selezionata", 
                    "Errore", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Ottieni tutti gli utenti della conferenza
            java.util.LinkedList<com.cms.users.Entity.UtenteE> autori = com.cms.App.dbms.getListaAutori(ConferenceControl.getConferenza().getId());
            java.util.LinkedList<com.cms.users.Entity.UtenteE> revisori = com.cms.App.dbms.getRevisori(ConferenceControl.getConferenza().getId());
            java.util.LinkedList<com.cms.users.Entity.UtenteE> chair = com.cms.App.dbms.getChair(ConferenceControl.getConferenza().getId());
            
            // Crea lista unica di utenti
            java.util.Set<Integer> idUtentiGiaAggiunti = new java.util.HashSet<>();
            java.util.List<com.cms.users.Entity.UtenteE> tuttiGliUtenti = new ArrayList<>();
            
            if (autori != null) {
                for (com.cms.users.Entity.UtenteE utente : autori) {
                    if (!idUtentiGiaAggiunti.contains(utente.getId())) {
                        tuttiGliUtenti.add(utente);
                        idUtentiGiaAggiunti.add(utente.getId());
                    }
                }
            }
            
            if (revisori != null) {
                for (com.cms.users.Entity.UtenteE utente : revisori) {
                    if (!idUtentiGiaAggiunti.contains(utente.getId())) {
                        tuttiGliUtenti.add(utente);
                        idUtentiGiaAggiunti.add(utente.getId());
                    }
                }
            }
            
            if (chair != null) {
                for (com.cms.users.Entity.UtenteE utente : chair) {
                    if (!idUtentiGiaAggiunti.contains(utente.getId())) {
                        tuttiGliUtenti.add(utente);
                        idUtentiGiaAggiunti.add(utente.getId());
                    }
                }
            }
            
            if (tuttiGliUtenti.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Nessun utente trovato per questa conferenza", 
                    "Informazione", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Crea array di nomi utenti per la selezione
            String[] nomiUtenti = new String[tuttiGliUtenti.size()];
            for (int i = 0; i < tuttiGliUtenti.size(); i++) {
                com.cms.users.Entity.UtenteE utente = tuttiGliUtenti.get(i);
                nomiUtenti[i] = utente.getUsername() + " (" + utente.getEmail() + ")";
            }
            
            // Mostra finestra di selezione multipla
            JList<String> userSelectionList = new JList<>(nomiUtenti);
            userSelectionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            userSelectionList.setVisibleRowCount(10);
            
            JScrollPane scrollPane = new JScrollPane(userSelectionList);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            int result = JOptionPane.showConfirmDialog(this, 
                scrollPane, 
                "Seleziona utenti specifici", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                java.util.List<String> valoriSelezionati = userSelectionList.getSelectedValuesList();
                for (String valore : valoriSelezionati) {
                    if (!selectedRecipients.contains(valore)) {
                        selectedRecipients.add(valore);
                    }
                }
                updateSelectedRecipientsList();
            }
            
        } catch (Exception e) {
            System.err.println("Errore durante la selezione degli utenti specifici: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Errore durante il caricamento degli utenti", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crea e mostra la schermata
     */
    public void create() {
        setVisible(true);
    }
    
    /**
     * Imposta il messaggio predefinito
     */
    public void setMessage(String text) {
        if (text != null) {
            messageArea.setText(text);
        }
    }
    
    /**
     * Ottiene il messaggio corrente
     */
    public String getMessage() {
        return messageArea.getText();
    }
    
    /**
     * Ottiene i destinatari selezionati
     */
    public List<String> getSelectedRecipients() {
        return new ArrayList<>(selectedRecipients);
    }
}
