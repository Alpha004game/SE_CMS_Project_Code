package com.cms.users.notifications.Control;

import com.cms.App;
import com.cms.users.Entity.UtenteE;
import com.cms.users.Entity.NotificaE;
import com.cms.users.notifications.Interface.NotificationScreen;
import java.util.LinkedList;

/**
 * <<control>>
 * NotificationControl
 */
public class NotificationControl {
    
    private NotificationScreen notificationScreen;
    private NotificaE notificaSelezionata;
    
    // Metodi
    public void create() {
        // Implementazione del sequence diagram: visualizza notifiche attive
        visualizzaNotificheAttive();
    }
    
    /**
     * Implementa il flusso del sequence diagram per visualizzare le notifiche attive
     */
    public void visualizzaNotificheAttive() {
        System.out.println("DEBUG NotificationControl: === INIZIO visualizzaNotificheAttive ===");
        
        try {
            // 1. Ottiene l'username dall'utente corrente (getUsername())
            UtenteE utenteCorrente = App.utenteAccesso;
            System.out.println("DEBUG NotificationControl: App.utenteAccesso = " + (utenteCorrente != null ? utenteCorrente.getUsername() + " (ID: " + utenteCorrente.getId() + ")" : "null"));
            
            if (utenteCorrente == null) {
                System.err.println("Errore: Nessun utente loggato");
                return;
            }
            
            String username = utenteCorrente.getUsername();
            System.out.println("DEBUG NotificationControl: Username estratto: '" + username + "'");
            
            // 2. Chiama ottieniNotificheAttive(username) su DBMSBoundary
            System.out.println("DEBUG NotificationControl: Chiamando App.dbms.ottieniNotificheAttive('" + username + "')");
            
            // Debug extra: verifica tutte le notifiche nel database
            System.out.println("DEBUG NotificationControl: === DEBUG EXTRA: tutte le notifiche nel DB ===");
            App.dbms.debugTutteLeNotifiche();
            
            LinkedList<NotificaE> notificheAttive = App.dbms.ottieniNotificheAttive(username);
            
            System.out.println("DEBUG NotificationControl: Risultato ottieniNotificheAttive:");
            if (notificheAttive == null) {
                System.out.println("  - null (errore durante il recupero)");
            } else {
                System.out.println("  - Lista con " + notificheAttive.size() + " elementi");
                for (int i = 0; i < notificheAttive.size(); i++) {
                    NotificaE n = notificheAttive.get(i);
                    System.out.println("    [" + i + "] ID:" + n.getId() + " Testo:'" + n.getText() + "'");
                }
            }
            
            // 3. Crea NotificationScreen con riferimento al control
            System.out.println("DEBUG NotificationControl: Creando NotificationScreen");
            notificationScreen = new NotificationScreen(this);
            
            // 4. Gestisce il ciclo alt [ListNotEmpty] / [false]
            if (notificheAttive != null && !notificheAttive.isEmpty()) {
                System.out.println("DEBUG NotificationControl: Lista NON vuota - chiamando setNotificationList");
                // Lista non vuota: setNotificationList(Notification)
                notificationScreen.setNotificationList(notificheAttive);
            } else {
                System.out.println("DEBUG NotificationControl: Lista vuota o null - chiamando setNotificationList(null)");
                // Lista vuota: setNotification(null)
                notificationScreen.setNotificationList(null);
            }
            
            // Mostra la schermata
            System.out.println("DEBUG NotificationControl: Mostrando NotificationScreen");
            notificationScreen.create();
            
            System.out.println("DEBUG NotificationControl: === FINE visualizzaNotificheAttive ===");
            
        } catch (Exception e) {
            System.err.println("Errore durante la visualizzazione delle notifiche: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void mostraTutteLeNotifiche() {
        // Implementazione per mostrare tutte le notifiche (non solo quelle attive)
        try {
            UtenteE utenteCorrente = App.utenteAccesso;
            if (utenteCorrente == null) {
                System.err.println("Errore: Nessun utente loggato");
                return;
            }
            
            String username = utenteCorrente.getUsername();
            LinkedList<NotificaE> tutteLeNotifiche = App.dbms.ottieniTutteLeNotifiche(username);
            
            notificationScreen = new NotificationScreen(this);
            
            if (tutteLeNotifiche != null && !tutteLeNotifiche.isEmpty()) {
                notificationScreen.setNotificationList(tutteLeNotifiche);
            } else {
                notificationScreen.setNotification(null);
            }
            
            notificationScreen.create();
            
        } catch (Exception e) {
            System.err.println("Errore durante la visualizzazione di tutte le notifiche: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void selezionaNotifica(int idNotifica) {
        System.out.println("DEBUG NotificationControl: === INIZIO selezionaNotifica ===");
        System.out.println("DEBUG NotificationControl: idNotifica ricevuto: " + idNotifica);
        
        try {
            // Recupera la notifica dal database tramite DBMSBoundary
            System.out.println("DEBUG NotificationControl: Chiamando App.dbms.getNotifica(" + idNotifica + ")");
            notificaSelezionata = App.dbms.getNotifica(idNotifica);
            
            if (notificaSelezionata != null) {
                System.out.println("DEBUG NotificationControl: Notifica recuperata con successo:");
                System.out.println("  - ID: " + notificaSelezionata.getId());
                System.out.println("  - Testo: '" + notificaSelezionata.getText() + "'");
                System.out.println("  - ID Utente: " + notificaSelezionata.getIdUtente());
                System.out.println("  - Status: '" + notificaSelezionata.getStatus() + "'");
                System.out.println("  - Tipo: " + notificaSelezionata.getTipo());
                
                // Determina se richiede accettazione (tipo 1 = accetta/rifiuta, altri = presa visione)
                boolean richiedeAccettazione = (notificaSelezionata.getTipo() == 1);
                System.out.println("  - Richiede accettazione: " + richiedeAccettazione);
            } else {
                System.err.println("DEBUG NotificationControl: ERRORE - Notifica non trovata per ID: " + idNotifica);
            }
        } catch (Exception e) {
            System.err.println("DEBUG NotificationControl: ERRORE durante il recupero della notifica: " + e.getMessage());
            e.printStackTrace();
            notificaSelezionata = null;
        }
        
        System.out.println("DEBUG NotificationControl: === FINE selezionaNotifica ===");
    }
    
    /**
     * Ottiene la notifica attualmente selezionata
     */
    public NotificaE getNotificaSelezionata() {
        return notificaSelezionata;
    }
    
    /**
     * Determina se la notifica selezionata richiede accettazione
     */
    public boolean richiedeAccettazione() {
        if (notificaSelezionata == null) {
            return false;
        }
        // Tipo 1 = accetta/rifiuta, altri tipi = presa visione
        return notificaSelezionata.getTipo() == 1;
    }
    
    public void presaVisione() {
        System.out.println("DEBUG NotificationControl: === INIZIO presaVisione ===");
        
        if (notificaSelezionata == null) {
            System.err.println("DEBUG NotificationControl: ERRORE - Nessuna notifica selezionata");
            return;
        }
        
        try {
            int idNotifica = notificaSelezionata.getId();
            System.out.println("DEBUG NotificationControl: Chiamando updateNotificationStatus(" + idNotifica + ", '3') per presa visione");
            
            // Aggiorna lo status nel database (3 = presa visione)
            App.dbms.updateNotificationStatus(idNotifica, "3");
            
            // Aggiorna anche l'oggetto locale
            notificaSelezionata.setStatus("presaVisione");
            
            System.out.println("DEBUG NotificationControl: Presa visione registrata con successo");
            
        } catch (Exception e) {
            System.err.println("DEBUG NotificationControl: ERRORE durante presa visione: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("DEBUG NotificationControl: === FINE presaVisione ===");
    }
    
    public void accettazione() {
        System.out.println("DEBUG NotificationControl: === INIZIO accettazione ===");
        
        if (notificaSelezionata == null) {
            System.err.println("DEBUG NotificationControl: ERRORE - Nessuna notifica selezionata");
            return;
        }
        
        try {
            int idNotifica = notificaSelezionata.getId();
            System.out.println("DEBUG NotificationControl: Chiamando updateNotificationStatus(" + idNotifica + ", '1') per accettazione");
            
            // Aggiorna lo status nel database (1 = accettato)
            App.dbms.updateNotificationStatus(idNotifica, "1");
            
            // Aggiorna anche l'oggetto locale
            notificaSelezionata.setStatus("accettato");
            
            // Verifica se si tratta di una notifica per aggiungere un sotto-revisore
            String dettagli = notificaSelezionata.getDettagli();
            System.out.println("DEBUG NotificationControl: Dettagli notifica: '" + dettagli + "'");
            
            if ("add-subRev".equals(dettagli)) {
                System.out.println("DEBUG NotificationControl: Rilevata notifica add-subRev, aggiornamento ruolo utente");
                
                // Ottieni i dati necessari per l'aggiornamento del ruolo
                int idUtente = notificaSelezionata.getIdUtente();
                int idConferenza = notificaSelezionata.getIdConferenza();
                int nuovoRuolo = 4; // 4 = sotto-revisore
                
                System.out.println("DEBUG NotificationControl: Chiamata aggiornaRuoloUtente(" + idUtente + ", " + idConferenza + ", " + nuovoRuolo + ")");
                
                // Aggiorna il ruolo nel database
                boolean aggiornamentoRiuscito = App.dbms.aggiornaRuoloUtente(idUtente, idConferenza, nuovoRuolo);
                
                if (aggiornamentoRiuscito) {
                    System.out.println("DEBUG NotificationControl: Ruolo aggiornato con successo");
                    
                    // Mostra un messaggio di conferma all'utente
                    javax.swing.JOptionPane.showMessageDialog(
                        null, 
                        "Congratulazioni! Sei stato nominato sotto-revisore per questa conferenza.\n" +
                        "Ora potrai accedere alle funzionalità di revisione degli articoli.",
                        "Ruolo Aggiornato", 
                        javax.swing.JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    System.err.println("DEBUG NotificationControl: ERRORE - Aggiornamento ruolo fallito");
                    
                    // Mostra un messaggio di errore
                    javax.swing.JOptionPane.showMessageDialog(
                        null, 
                        "Errore durante l'aggiornamento del ruolo. Contatta l'amministratore.",
                        "Errore", 
                        javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            
            System.out.println("DEBUG NotificationControl: Accettazione registrata con successo");
            
        } catch (Exception e) {
            System.err.println("DEBUG NotificationControl: ERRORE durante accettazione: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("DEBUG NotificationControl: === FINE accettazione ===");
    }
    
    public void rifiuto() {
        System.out.println("DEBUG NotificationControl: === INIZIO rifiuto ===");
        
        if (notificaSelezionata == null) {
            System.err.println("DEBUG NotificationControl: ERRORE - Nessuna notifica selezionata");
            return;
        }
        
        try {
            int idNotifica = notificaSelezionata.getId();
            System.out.println("DEBUG NotificationControl: Chiamando updateNotificationStatus(" + idNotifica + ", '2') per rifiuto");
            
            // Aggiorna lo status nel database (2 = rifiutato)
            App.dbms.updateNotificationStatus(idNotifica, "2");
            
            // Aggiorna anche l'oggetto locale
            notificaSelezionata.setStatus("rifiutato");
            
            System.out.println("DEBUG NotificationControl: Rifiuto registrato con successo");
            
        } catch (Exception e) {
            System.err.println("DEBUG NotificationControl: ERRORE durante rifiuto: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("DEBUG NotificationControl: === FINE rifiuto ===");
    }
    
    /**
     * Ottiene il riferimento alla NotificationScreen
     */
    public NotificationScreen getNotificationScreen() {
        return notificationScreen;
    }
    
}
