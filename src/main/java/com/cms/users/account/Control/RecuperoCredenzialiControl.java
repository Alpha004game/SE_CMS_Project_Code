package com.cms.users.account.Control;

import com.cms.App;
import com.cms.users.Commons.UtilsControl;
import com.cms.users.account.Interface.InserisciCodiceScreen;
import java.util.Random;

/**
 * <<control>>
 * RecuperoCredenzialiControl
 */
public class RecuperoCredenzialiControl {
    
    private String emailRecupero;
    private String codiceGenerato;
    
    // Metodi
    public void create() {
        // Implementazione da definire
    }
    
    /**
     * Gestisce il recupero delle credenziali tramite email
     * @param email L'indirizzo email per il recupero
     * @return true se l'email è stata inviata con successo, false altrimenti
     */
    public boolean recuperaCredenziali(String email) {
        System.out.println("DEBUG RecuperoCredenzialiControl: === INIZIO recuperaCredenziali ===");
        System.out.println("DEBUG RecuperoCredenzialiControl: Email ricevuta: " + email);
        
        try {
            // Verifica che l'email esista nel database
            if (!verificaEmailEsiste(email)) {
                System.err.println("DEBUG RecuperoCredenzialiControl: Email non trovata nel database: " + email);
                return false;
            }
            
            // Salva l'email per utilizzo successivo
            this.emailRecupero = email;
            
            // Genera codice a 6 cifre randomico
            this.codiceGenerato = generaCodiceRecupero();
            System.out.println("DEBUG RecuperoCredenzialiControl: Codice generato: " + codiceGenerato);
            
            // Prepara il messaggio email
            String subject = "Recupero Credenziali CMS";
            String text = "Il vostro codice per il recupero credenziali è " + codiceGenerato;
            
            // Invia email utilizzando UtilsControl
            System.out.println("DEBUG RecuperoCredenzialiControl: Invio email...");
            boolean emailInviata = UtilsControl.sendMail(email, subject, text);
            
            if (emailInviata) {
                System.out.println("DEBUG RecuperoCredenzialiControl: Email inviata con successo");
                
                // Apri la schermata di inserimento codice
                javax.swing.SwingUtilities.invokeLater(() -> {
                    InserisciCodiceScreen codiceScreen = new InserisciCodiceScreen(this);
                    codiceScreen.create();
                });
                
                return true;
            } else {
                System.err.println("DEBUG RecuperoCredenzialiControl: Errore durante l'invio dell'email");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("DEBUG RecuperoCredenzialiControl: ERRORE durante recuperaCredenziali: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            System.out.println("DEBUG RecuperoCredenzialiControl: === FINE recuperaCredenziali ===");
        }
    }
    
    /**
     * Verifica che l'email esista nel database
     * @param email L'email da verificare
     * @return true se l'email esiste, false altrimenti
     */
    private boolean verificaEmailEsiste(String email) {
        try {
            // Utilizza il metodo getUsername che restituisce lo username associato all'email
            // Se restituisce null, l'email non esiste
            String username = App.dbms.getUsername(email);
            boolean esiste = (username != null && !username.trim().isEmpty());
            
            System.out.println("DEBUG RecuperoCredenzialiControl: Email " + email + 
                             (esiste ? " TROVATA (username: " + username + ")" : " NON TROVATA"));
            
            return esiste;
            
        } catch (Exception e) {
            System.err.println("DEBUG RecuperoCredenzialiControl: Errore durante verifica email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Genera un codice di recupero a 6 cifre randomico
     * @return Il codice generato come stringa
     */
    private String generaCodiceRecupero() {
        Random random = new Random();
        // Genera un numero tra 100000 e 999999 (6 cifre)
        int codice = 100000 + random.nextInt(900000);
        return String.valueOf(codice);
    }
    
    /**
     * Verifica che il codice inserito corrisponda a quello generato
     * @param codiceInserito Il codice inserito dall'utente
     * @return true se il codice è corretto, false altrimenti
     */
    public boolean verificaCodice(String codiceInserito) {
        System.out.println("DEBUG RecuperoCredenzialiControl: === INIZIO verificaCodice ===");
        System.out.println("DEBUG RecuperoCredenzialiControl: Codice inserito: " + codiceInserito);
        System.out.println("DEBUG RecuperoCredenzialiControl: Codice generato: " + codiceGenerato);
        
        if (codiceGenerato == null || codiceInserito == null) {
            System.err.println("DEBUG RecuperoCredenzialiControl: Codice generato o inserito è null");
            return false;
        }
        
        boolean isValid = codiceGenerato.equals(codiceInserito.trim());
        System.out.println("DEBUG RecuperoCredenzialiControl: Codice " + (isValid ? "VALIDO" : "NON VALIDO"));
        System.out.println("DEBUG RecuperoCredenzialiControl: === FINE verificaCodice ===");
        
        return isValid;
    }
    
    /**
     * Aggiorna la password nel database per l'email specificata
     * @param nuovaPassword La nuova password
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public boolean aggiornaPassword(String nuovaPassword) {
        System.out.println("DEBUG RecuperoCredenzialiControl: === INIZIO aggiornaPassword ===");
        
        if (emailRecupero == null || emailRecupero.trim().isEmpty()) {
            System.err.println("DEBUG RecuperoCredenzialiControl: Email di recupero non impostata");
            return false;
        }
        
        try {
            // Ottieni lo username associato all'email
            String username = App.dbms.getUsername(emailRecupero);
            
            if (username == null || username.trim().isEmpty()) {
                System.err.println("DEBUG RecuperoCredenzialiControl: Username non trovato per email: " + emailRecupero);
                return false;
            }
            
            System.out.println("DEBUG RecuperoCredenzialiControl: Aggiornamento password per username: " + username);
            
            // Aggiorna la password nel database
            App.dbms.updatePassword(username, nuovaPassword);
            
            System.out.println("DEBUG RecuperoCredenzialiControl: Password aggiornata con successo");
            
            // Reset delle variabili
            emailRecupero = null;
            codiceGenerato = null;
            
            return true;
            
        } catch (Exception e) {
            System.err.println("DEBUG RecuperoCredenzialiControl: ERRORE durante aggiornamento password: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            System.out.println("DEBUG RecuperoCredenzialiControl: === FINE aggiornaPassword ===");
        }
    }
    
    /**
     * Getter per l'email di recupero (per testing)
     */
    public String getEmailRecupero() {
        return emailRecupero;
    }
    
    /**
     * Getter per il codice generato (per testing)
     */
    public String getCodiceGenerato() {
        return codiceGenerato;
    }
    
}
