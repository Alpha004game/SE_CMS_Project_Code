package com.cms.users.Commons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <<boundary>>
 * SuccessScreen - Schermata di conferma per operazioni completate con successo
 */
public class SuccessScreen extends JFrame {
    
    // Componenti dell'interfaccia
    private JLabel messageLabel;
    private JButton okButton;
    private Timer redirectTimer;
    
    // Attributi
    private String successMessage;
    private String operationType;
    private boolean autoRedirect;
    private int redirectDelay;
    private Runnable redirectAction;
    
    /**
     * Costruttore di default
     */
    public SuccessScreen() {
        this.successMessage = "Operazione eseguita\ncon successo";
        this.operationType = "Operazione";
        this.autoRedirect = false;
        this.redirectDelay = 3000; // 3 secondi
        this.redirectAction = null;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Costruttore con messaggio personalizzato
     */
    public SuccessScreen(String message) {
        this();
        setMessage(message);
    }
    
    /**
     * Costruttore con messaggio e tipo di operazione
     */
    public SuccessScreen(String message, String operation) {
        this();
        setMessage(message);
        this.operationType = operation;
    }
    
    /**
     * Inizializza i componenti dell'interfaccia
     */
    private void initializeComponents() {
        setTitle("Operazione Completata");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Rimuove la barra del titolo per un look più pulito
        setUndecorated(false);
        
        // Label per il messaggio di successo
        messageLabel = new JLabel();
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
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
        
        // Timer per redirect automatico
        redirectTimer = new Timer(redirectDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRedirect();
            }
        });
        redirectTimer.setRepeats(false);
    }
    
    /**
     * Aggiorna la visualizzazione del messaggio
     */
    private void updateMessageDisplay() {
        if (successMessage.contains("\n")) {
            // Messaggio multilinea usando HTML
            String htmlMessage = "<html><div style='text-align: center;'>" + 
                               successMessage.replace("\n", "<br>") + 
                               "</div></html>";
            messageLabel.setText(htmlMessage);
        } else {
            messageLabel.setText(successMessage);
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Messaggio al centro
        mainPanel.add(messageLabel, BorderLayout.CENTER);
        
        // Bottone in basso
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
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
            closeScreen();
        });
        
        // Escape key per chiudere
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeScreen();
            }
        });
        
        // Enter key per confermare
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke("ENTER");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enterKeyStroke, "ENTER");
        getRootPane().getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeScreen();
            }
        });
    }
    
    // Metodi dell'interfaccia originale
    
    /**
     * Mostra il messaggio di successo
     */
    public void displaySuccessMessage() {
        setVisible(true);
        
        if (autoRedirect) {
            redirectTimer.start();
        }
    }
    
    /**
     * Imposta il messaggio di successo
     */
    public void setMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            this.successMessage = message;
            if (messageLabel != null) {
                updateMessageDisplay();
            }
        }
    }
    
    /**
     * Mostra i dettagli dell'operazione
     */
    public void showOperationDetails() {
        String details = "Dettagli operazione:\n\n" +
                        "Tipo: " + operationType + "\n" +
                        "Stato: Completata con successo\n" +
                        "Data: " + java.time.LocalDateTime.now().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        
        JOptionPane.showMessageDialog(this, details, "Dettagli Operazione", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Reindirizza alla home
     */
    public void redirectToHome() {
        dispose();
        try {
            Class<?> homeScreenClass = Class.forName("com.cms.users.Commons.HomeScreen");
            Object homeScreen = homeScreenClass.getDeclaredConstructor().newInstance();
            java.lang.reflect.Method displayMethod = homeScreenClass.getMethod("displayUserDashboard");
            displayMethod.invoke(homeScreen);
        } catch (Exception ex) {
            // Fallback: mostra messaggio
            JOptionPane.showMessageDialog(null, "Reindirizzamento alla Home...");
        }
    }
    
    /**
     * Reindirizza alla pagina precedente
     */
    public void redirectToPreviousPage() {
        if (redirectAction != null) {
            dispose();
            redirectAction.run();
        } else {
            closeScreen();
        }
    }
    
    /**
     * Chiude la schermata
     */
    public void closeScreen() {
        if (redirectTimer != null && redirectTimer.isRunning()) {
            redirectTimer.stop();
        }
        dispose();
    }
    
    /**
     * Ottiene il messaggio corrente
     */
    public String getMessage() {
        return successMessage;
    }
    
    // Metodi di utilità aggiuntivi
    
    /**
     * Imposta il tipo di operazione
     */
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    
    /**
     * Abilita il redirect automatico
     */
    public void setAutoRedirect(boolean autoRedirect, int delayMillis) {
        this.autoRedirect = autoRedirect;
        this.redirectDelay = delayMillis;
        
        if (redirectTimer != null) {
            redirectTimer.setDelay(delayMillis);
        }
    }
    
    /**
     * Imposta l'azione di redirect personalizzata
     */
    public void setRedirectAction(Runnable action) {
        this.redirectAction = action;
    }
    
    /**
     * Esegue il redirect
     */
    private void performRedirect() {
        if (redirectAction != null) {
            redirectToPreviousPage();
        } else {
            redirectToHome();
        }
    }
    
    /**
     * Mostra una success screen con messaggio di default
     */
    public static void showSuccess() {
        SuccessScreen screen = new SuccessScreen();
        screen.displaySuccessMessage();
    }
    
    /**
     * Mostra una success screen con messaggio personalizzato
     */
    public static void showSuccess(String message) {
        SuccessScreen screen = new SuccessScreen(message);
        screen.displaySuccessMessage();
    }
    
    /**
     * Mostra una success screen con redirect automatico
     */
    public static void showSuccess(String message, int autoRedirectDelay) {
        SuccessScreen screen = new SuccessScreen(message);
        screen.setAutoRedirect(true, autoRedirectDelay);
        screen.displaySuccessMessage();
    }
    
    /**
     * Mostra una success screen con azione personalizzata
     */
    public static void showSuccess(String message, Runnable redirectAction) {
        SuccessScreen screen = new SuccessScreen(message);
        screen.setRedirectAction(redirectAction);
        screen.setAutoRedirect(true, 3000);
        screen.displaySuccessMessage();
    }
    
    /**
     * Metodo main per test standalone
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test base
            SuccessScreen screen1 = new SuccessScreen();
            screen1.setLocation(0, 0);
            screen1.displaySuccessMessage();
            
            // Test con messaggio personalizzato
            SuccessScreen screen2 = new SuccessScreen("Dati salvati\ncon successo");
            screen2.setLocation(400, 0);
            screen2.displaySuccessMessage();
            
            // Test con auto-redirect
            SuccessScreen screen3 = new SuccessScreen("Configurazione\ncompletata");
            screen3.setAutoRedirect(true, 5000);
            screen3.setLocation(0, 300);
            screen3.displaySuccessMessage();
        });
    }
}
