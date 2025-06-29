package com.cms.users.account.Control;

import com.cms.App;
import com.cms.users.Entity.UtenteE;
import com.cms.users.account.Interface.UserInfoScreen;
import com.cms.users.Commons.WarningScreen;
import com.cms.users.Commons.SuccessScreen;
import com.cms.users.Commons.ErrorScreen;

/**
 * <<control>>
 * GestioneUtenteControl
 */
public class GestioneUtenteControl {
    
    private UserInfoScreen userInfoScreen;
    private UtenteE currentUser;
    
    /**
     * Crea il controllo per la gestione utente
     */
    public void create() {
        // Ottiene le informazioni dell'utente attualmente loggato
        this.currentUser = App.utenteAccesso;
        
        if (currentUser != null) {
            // Crea la UserInfoScreen con i dati dell'utente
            userInfoScreen = new UserInfoScreen(currentUser.getUsername(), currentUser.getEmail());
            userInfoScreen.setGestioneUtenteControl(this); // Imposta il riferimento al controllo
            userInfoScreen.setVisible(true);
        } else {
            new ErrorScreen("Errore: Nessun utente collegato!").show();
        }
    }
    
    /**
     * Gestisce il salvataggio delle modifiche dell'utente
     */
    public void salvaModifiche(String nuovaEmail, String nuovaPassword) {
        // Mostra WarningScreen per conferma come da sequence diagram
        WarningScreen warningScreen = new WarningScreen("Sei sicuro di voler salvare\nle modifiche alle informazioni?");
        warningScreen.setButtonTexts("Salva", "Annulla");
        
        // Imposta le azioni per i bottoni
        warningScreen.setConfirmAction(() -> {
            // Se l'utente conferma, salva le modifiche
            eseguiSalvataggio(nuovaEmail, nuovaPassword);
        });
        
        warningScreen.setCancelAction(() -> {
            // Se l'utente annulla, non fare nulla
            System.out.println("Salvataggio annullato dall'utente");
        });
        
        warningScreen.displayWarningMessage();
    }
    
    /**
     * Esegue effettivamente il salvataggio nel database
     */
    private void eseguiSalvataggio(String nuovaEmail, String nuovaPassword) {
        try {
            if (currentUser != null) {
                int idUtente = currentUser.getIdUser();
                
                // Aggiorna email se è cambiata
                if (nuovaEmail != null && !nuovaEmail.equals(currentUser.getEmail())) {
                    // Aggiorna nel database tramite DBMS
                    App.dbms.sendNewInformation(idUtente, nuovaEmail);
                    
                    // Aggiorna l'oggetto utente corrente
                    currentUser = new UtenteE(idUtente, currentUser.getUsername(), nuovaEmail);
                    App.utenteAccesso = currentUser;
                }
                
                // Aggiorna password se è stata fornita
                if (nuovaPassword != null && !nuovaPassword.trim().isEmpty()) {
                    App.dbms.updatePassword(currentUser.getUsername(), nuovaPassword);
                }
                
                // Mostra SuccessScreen come da sequence diagram
                SuccessScreen successScreen = new SuccessScreen("Informazioni aggiornate\ncon successo!");
                successScreen.show();
                
                // Chiude la UserInfoScreen
                if (userInfoScreen != null) {
                    userInfoScreen.dispose();
                }
                
            } else {
                new ErrorScreen("Errore: Utente non trovato!").show();
            }
            
        } catch (Exception e) {
            new ErrorScreen("Errore durante il salvataggio:\n" + e.getMessage()).show();
            e.printStackTrace();
        }
    }
    
    /**
     * Ottiene le informazioni dell'utente dal database
     */
    public UtenteE getUserInfo() {
        if (App.utenteAccesso != null) {
            // Ricarica le informazioni dal database per essere sicuri che siano aggiornate
            return App.dbms.getUser(App.utenteAccesso.getIdUser());
        }
        return null;
    }
    
}
