package com.cms.users.account.Control;

import com.cms.App;
import com.cms.users.account.Interface.LoginScreen;
import javax.swing.*;
import java.awt.Window;

/**
 * <<control>>
 * LogoutControl
 */
public class LogoutControl {
    
    // Metodi
    public void create() {
        // Inizializzazione del controllo logout se necessario
    }
    
    public void logout() {
        // Esegue le operazioni di logout
        logoutOperations();
        
        // Distrugge la sessione corrente
        destroy();
    }
    
    public void logoutOperations() {
        // Pulisce i dati dell'utente corrente
        App.utenteAccesso = null;
        
        // Chiude tutte le finestre aperte eccetto la prima
        SwingUtilities.invokeLater(() -> {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof JFrame && window.isDisplayable()) {
                    window.dispose();
                }
            }
            
            // Crea e mostra la schermata di login
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.create();
        });
    }
    
    public void destroy() {
        // Pulizia finale delle risorse se necessario
        System.out.println("Logout completato. Sessione distrutta.");
    }
    
}
