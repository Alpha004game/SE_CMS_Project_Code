package com.cms.users.Commons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <<boundary>>
 * ErrorScreen - Schermata di errore per operazioni fallite
 */
public class ErrorScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JLabel titleLabel;
    private JLabel messageLabel;
    private JButton okButton;
    private Timer autoCloseTimer;
    
    // Attributes
    private String errorMessage;
    private String errorCode;
    private String errorType;
    private boolean autoClose;
    private int closeDelay;
    private Runnable closeAction;
    
    /**
     * Costruttore di default
     */
    public ErrorScreen() {
        this.errorMessage = "Tipologia di errore";
        this.errorCode = "ERR_001";
        this.errorType = "Errore Generico";
        this.autoClose = false;
        this.closeDelay = 5000; // 5 secondi
        this.closeAction = null;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con messaggio personalizzato
     */
    public ErrorScreen(String message) {
        this();
        setErrorMessage(message);
    }
    
    /**
     * Costruttore con messaggio e tipo di errore
     */
    public ErrorScreen(String message, String type) {
        this();
        setErrorMessage(message);
        this.errorType = type;
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        setTitle("Errore");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Label per il titolo "Errore"
        titleLabel = new JLabel("Errore");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        // Label per il messaggio di errore
        messageLabel = new JLabel();
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setVerticalAlignment(SwingConstants.CENTER);
        updateMessageDisplay();
        
        // Bottone OK
        okButton = new JButton("OK");
        okButton.setBackground(Color.ORANGE);
        okButton.setForeground(Color.BLACK);
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        okButton.setPreferredSize(new Dimension(80, 40));
        
        // Timer per chiusura automatica
        autoCloseTimer = new Timer(closeDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performCloseAction();
            }
        });
        autoCloseTimer.setRepeats(false);
    }
    
    /**
     * Aggiorna la visualizzazione del messaggio
     */
    private void updateMessageDisplay() {
        if (errorMessage.contains("\n")) {
            // Messaggio multilinea usando HTML
            String htmlMessage = "<html><div style='text-align: center;'>" + 
                               errorMessage.replace("\n", "<br>") + 
                               "</div></html>";
            messageLabel.setText(htmlMessage);
        } else {
            messageLabel.setText(errorMessage);
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
        
        // Bottone in basso
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(okButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Configura i gestori di eventi
     */
    private void setupEventHandlers() {
        // Bottone OK
        okButton.addActionListener(e -> {
            okButton();
        });
        
        // Escape key per chiudere
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okButton();
            }
        });
        
        // Enter key per confermare
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke("ENTER");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enterKeyStroke, "ENTER");
        getRootPane().getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okButton();
            }
        });
    }
    
    // Methods from original interface
    
    /**
     * Crea e mostra la schermata di errore
     */
    public void create() {
        setVisible(true);
        
        if (autoClose) {
            autoCloseTimer.start();
        }
    }
    
    /**
     * Gestisce il click sul bottone OK
     */
    public void okButton() {
        destroy();
    }
    
    /**
     * Chiude e distrugge la schermata
     */
    public void destroy() {
        if (autoCloseTimer != null && autoCloseTimer.isRunning()) {
            autoCloseTimer.stop();
        }
        dispose();
        
        if (closeAction != null) {
            closeAction.run();
        }
    }
    
    // Metodi aggiuntivi
    
    /**
     * Imposta il messaggio di errore
     */
    public void setErrorMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            this.errorMessage = message;
            if (messageLabel != null) {
                updateMessageDisplay();
            }
        }
    }
    
    /**
     * Imposta il codice di errore
     */
    public void setErrorCode(String code) {
        this.errorCode = code;
    }
    
    /**
     * Imposta il tipo di errore
     */
    public void setErrorType(String type) {
        this.errorType = type;
    }
    
    /**
     * Abilita la chiusura automatica
     */
    public void setAutoClose(boolean autoClose, int delayMillis) {
        this.autoClose = autoClose;
        this.closeDelay = delayMillis;
        
        if (autoCloseTimer != null) {
            autoCloseTimer.setDelay(delayMillis);
        }
    }
    
    /**
     * Imposta l'azione di chiusura personalizzata
     */
    public void setCloseAction(Runnable action) {
        this.closeAction = action;
    }
    
    /**
     * Esegue l'azione di chiusura
     */
    private void performCloseAction() {
        destroy();
    }
    
    /**
     * Mostra i dettagli dell'errore
     */
    public void showErrorDetails() {
        String details = "Dettagli errore:\n\n" +
                        "Tipo: " + errorType + "\n" +
                        "Codice: " + errorCode + "\n" +
                        "Messaggio: " + errorMessage + "\n" +
                        "Data: " + java.time.LocalDateTime.now().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        
        JOptionPane.showMessageDialog(this, details, "Dettagli Errore", JOptionPane.ERROR_MESSAGE);
    }
    
    // Metodi statici di utilità
    
    /**
     * Mostra una error screen con messaggio di default
     */
    public static void showError() {
        ErrorScreen screen = new ErrorScreen();
        screen.create();
    }
    
    /**
     * Mostra una error screen con messaggio personalizzato
     */
    public static void showError(String message) {
        ErrorScreen screen = new ErrorScreen(message);
        screen.create();
    }
    
    /**
     * Mostra una error screen con tipo di errore
     */
    public static void showError(String message, String errorType) {
        ErrorScreen screen = new ErrorScreen(message, errorType);
        screen.create();
    }
    
    /**
     * Mostra una error screen con chiusura automatica
     */
    public static void showError(String message, int autoCloseDelay) {
        ErrorScreen screen = new ErrorScreen(message);
        screen.setAutoClose(true, autoCloseDelay);
        screen.create();
    }
    
    /**
     * Mostra errore di validazione
     */
    public static void showValidationError(String fieldName) {
        String message = "Errore di validazione:\n" + fieldName + " non è valido";
        showError(message, "Validazione");
    }
    
    /**
     * Mostra errore di connessione
     */
    public static void showConnectionError() {
        showError("Impossibile connettersi\nal server", "Connessione");
    }
    
    /**
     * Mostra errore di permessi
     */
    public static void showPermissionError() {
        showError("Non hai i permessi\nnecessari per questa operazione", "Autorizzazione");
    }
    
    /**
     * Mostra errore di file
     */
    public static void showFileError(String fileName) {
        String message = "Errore nel file:\n" + fileName;
        showError(message, "File");
    }
    
    /**
     * Metodo main per test standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test base
            ErrorScreen screen1 = new ErrorScreen();
            screen1.setLocation(0, 0);
            screen1.create();
            
            // Test con messaggio personalizzato
            ErrorScreen screen2 = new ErrorScreen("Connessione al database\nfallita");
            screen2.setLocation(400, 0);
            screen2.create();
            
            // Test con auto-close
            ErrorScreen screen3 = new ErrorScreen("File non trovato");
            screen3.setAutoClose(true, 4000);
            screen3.setLocation(0, 300);
            screen3.create();
        });
    }
}
