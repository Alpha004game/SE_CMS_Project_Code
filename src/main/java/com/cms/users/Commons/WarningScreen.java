package com.cms.users.Commons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <<boundary>>
 * WarningScreen - Schermata di avviso per operazioni che richiedono conferma
 */
public class WarningScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JLabel titleLabel;
    private JLabel messageLabel;
    private JButton confirmButton;
    private JButton cancelButton;
    private Timer autoCloseTimer;
    
    // Attributes
    private String warningMessage;
    private String warningType;
    private boolean requiresConfirmation;
    private String confirmButtonText;
    private String cancelButtonText;
    private boolean userChoice;
    private boolean choiceMade;
    private int autoCloseDelay;
    private Runnable confirmAction;
    private Runnable cancelAction;
    
    /**
     * Costruttore di default
     */
    public WarningScreen() {
        this.warningMessage = "Sei sicuro di voler procedere\ncon l'operazione?";
        this.warningType = "Attenzione";
        this.requiresConfirmation = true;
        this.confirmButtonText = "Sì";
        this.cancelButtonText = "No";
        this.userChoice = false;
        this.choiceMade = false;
        this.autoCloseDelay = 0; // 0 = no auto-close
        this.confirmAction = null;
        this.cancelAction = null;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con messaggio personalizzato
     */
    public WarningScreen(String message) {
        this();
        setWarningMessage(message);
    }
    
    /**
     * Costruttore con messaggio e tipo di warning
     */
    public WarningScreen(String message, String type) {
        this();
        setWarningMessage(message);
        this.warningType = type;
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        setTitle("Attenzione");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Label per il titolo "Attenzione"
        titleLabel = new JLabel("Attenzione");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        // Label per il messaggio di warning
        messageLabel = new JLabel();
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setVerticalAlignment(SwingConstants.CENTER);
        updateMessageDisplay();
        
        // Bottone Conferma (Sì)
        confirmButton = new JButton(confirmButtonText);
        confirmButton.setBackground(Color.ORANGE);
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        confirmButton.setPreferredSize(new Dimension(80, 40));
        
        // Bottone Cancella (No)
        cancelButton = new JButton(cancelButtonText);
        cancelButton.setBackground(Color.ORANGE);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        cancelButton.setPreferredSize(new Dimension(80, 40));
        
        // Timer per chiusura automatica (se configurato)
        autoCloseTimer = new Timer(autoCloseDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performAutoClose();
            }
        });
        autoCloseTimer.setRepeats(false);
    }
    
    /**
     * Aggiorna la visualizzazione del messaggio
     */
    private void updateMessageDisplay() {
        if (warningMessage.contains("\n")) {
            // Messaggio multilinea usando HTML
            String htmlMessage = "<html><div style='text-align: center;'>" + 
                               warningMessage.replace("\n", "<br>") + 
                               "</div></html>";
            messageLabel.setText(htmlMessage);
        } else {
            messageLabel.setText(warningMessage);
        }
    }
    
    /**
     * Configura il layout dell'interfaccia
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        
        // Pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Titolo in alto
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Messaggio al centro
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(Color.WHITE);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        
        // Bottoni in basso
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        if (requiresConfirmation) {
            buttonPanel.add(confirmButton);
            buttonPanel.add(cancelButton);
        } else {
            // Solo bottone di chiusura se non richiede conferma
            JButton okButton = new JButton("OK");
            styleButton(okButton);
            okButton.addActionListener(e -> closeWarning());
            buttonPanel.add(okButton);
        }
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Applica lo stile ai bottoni
     */
    private void styleButton(JButton button) {
        button.setBackground(Color.ORANGE);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setPreferredSize(new Dimension(80, 40));
    }
    
    /**
     * Configura i gestori di eventi
     */
    private void setupEventHandlers() {
        // Bottone conferma
        confirmButton.addActionListener(e -> {
            userChoice = true;
            choiceMade = true;
            redirectOnConfirm();
        });
        
        // Bottone cancella
        cancelButton.addActionListener(e -> {
            userChoice = false;
            choiceMade = true;
            redirectOnCancel();
        });
        
        // Escape key per cancellare
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userChoice = false;
                choiceMade = true;
                redirectOnCancel();
            }
        });
        
        // Enter key per confermare
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke("ENTER");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enterKeyStroke, "ENTER");
        getRootPane().getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userChoice = true;
                choiceMade = true;
                redirectOnConfirm();
            }
        });
    }
    
    // Methods from original interface
    
    /**
     * Mostra il messaggio di warning
     */
    public void displayWarningMessage() {
        choiceMade = false;
        setVisible(true);
        
        if (autoCloseDelay > 0) {
            autoCloseTimer.setDelay(autoCloseDelay);
            autoCloseTimer.start();
        }
    }
    
    /**
     * Imposta il messaggio di warning
     */
    public void setWarningMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            this.warningMessage = message;
            if (messageLabel != null) {
                updateMessageDisplay();
            }
        }
    }
    
    /**
     * Mostra i bottoni di conferma
     */
    public void showConfirmationButtons() {
        this.requiresConfirmation = true;
        // Ricostruisce il layout per mostrare i bottoni
        getContentPane().removeAll();
        setupLayout();
        revalidate();
        repaint();
    }
    
    /**
     * Ottiene la conferma dell'utente
     */
    public boolean getUserConfirmation() {
        // Aspetta che l'utente faccia una scelta
        while (!choiceMade && isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return userChoice;
    }
    
    /**
     * Chiude il warning
     */
    public void closeWarning() {
        if (autoCloseTimer != null && autoCloseTimer.isRunning()) {
            autoCloseTimer.stop();
        }
        dispose();
    }
    
    /**
     * Azione da eseguire alla conferma
     */
    public void redirectOnConfirm() {
        closeWarning();
        if (confirmAction != null) {
            confirmAction.run();
        }
    }
    
    /**
     * Azione da eseguire alla cancellazione
     */
    public void redirectOnCancel() {
        closeWarning();
        if (cancelAction != null) {
            cancelAction.run();
        }
    }
    
    /**
     * Ottiene il tipo di warning
     */
    public String getWarningType() {
        return warningType;
    }
    
    // Metodi aggiuntivi
    
    /**
     * Imposta il tipo di warning
     */
    public void setWarningType(String type) {
        this.warningType = type;
        titleLabel.setText(type);
    }
    
    /**
     * Imposta i testi dei bottoni
     */
    public void setButtonTexts(String confirmText, String cancelText) {
        this.confirmButtonText = confirmText;
        this.cancelButtonText = cancelText;
        
        if (confirmButton != null) {
            confirmButton.setText(confirmText);
        }
        if (cancelButton != null) {
            cancelButton.setText(cancelText);
        }
    }
    
    /**
     * Imposta se richiede conferma
     */
    public void setRequiresConfirmation(boolean requires) {
        this.requiresConfirmation = requires;
    }
    
    /**
     * Imposta l'azione da eseguire alla conferma
     */
    public void setConfirmAction(Runnable action) {
        this.confirmAction = action;
    }
    
    /**
     * Imposta l'azione da eseguire alla cancellazione
     */
    public void setCancelAction(Runnable action) {
        this.cancelAction = action;
    }
    
    /**
     * Imposta la chiusura automatica
     */
    public void setAutoClose(int delayMillis) {
        this.autoCloseDelay = delayMillis;
    }
    
    /**
     * Esegue la chiusura automatica
     */
    private void performAutoClose() {
        userChoice = false;
        choiceMade = true;
        redirectOnCancel();
    }
    
    /**
     * Ottiene se l'utente ha fatto una scelta
     */
    public boolean isChoiceMade() {
        return choiceMade;
    }
    
    /**
     * Ottiene la scelta dell'utente
     */
    public boolean getUserChoice() {
        return userChoice;
    }
    
    // Metodi statici di utilità
    
    /**
     * Mostra un warning con messaggio di default
     */
    public static boolean showWarning() {
        WarningScreen screen = new WarningScreen();
        screen.displayWarningMessage();
        return screen.getUserConfirmation();
    }
    
    /**
     * Mostra un warning con messaggio personalizzato
     */
    public static boolean showWarning(String message) {
        WarningScreen screen = new WarningScreen(message);
        screen.displayWarningMessage();
        return screen.getUserConfirmation();
    }
    
    /**
     * Mostra un warning di eliminazione
     */
    public static boolean showDeleteWarning(String itemName) {
        String message = "Sei sicuro di voler eliminare\n" + itemName + "?";
        WarningScreen screen = new WarningScreen(message);
        screen.setButtonTexts("Elimina", "Annulla");
        screen.displayWarningMessage();
        return screen.getUserConfirmation();
    }
    
    /**
     * Mostra un warning di sovrascrittura
     */
    public static boolean showOverwriteWarning(String fileName) {
        String message = "Il file " + fileName + " esiste già.\nVuoi sovrascriverlo?";
        WarningScreen screen = new WarningScreen(message);
        screen.setButtonTexts("Sovrascrivi", "Annulla");
        screen.displayWarningMessage();
        return screen.getUserConfirmation();
    }
    
    /**
     * Mostra un warning con azioni personalizzate
     */
    public static void showWarning(String message, Runnable confirmAction, Runnable cancelAction) {
        WarningScreen screen = new WarningScreen(message);
        screen.setConfirmAction(confirmAction);
        screen.setCancelAction(cancelAction);
        screen.displayWarningMessage();
    }
    
    /**
     * Metodo main per test standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test base
            WarningScreen screen1 = new WarningScreen();
            screen1.setLocation(0, 0);
            screen1.displayWarningMessage();
            
            // Test con messaggio personalizzato
            WarningScreen screen2 = new WarningScreen("Questa operazione non può\nessere annullata. Continuare?");
            screen2.setLocation(450, 0);
            screen2.displayWarningMessage();
            
            // Test warning di eliminazione
            WarningScreen screen3 = new WarningScreen("Sei sicuro di voler eliminare\nquesto elemento?");
            screen3.setButtonTexts("Elimina", "Annulla");
            screen3.setLocation(0, 300);
            screen3.displayWarningMessage();
        });
    }
}
