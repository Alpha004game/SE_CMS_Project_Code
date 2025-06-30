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
            
            // 3. Crea NotificationScreen
            System.out.println("DEBUG NotificationControl: Creando NotificationScreen");
            notificationScreen = new NotificationScreen();
            
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
            
            notificationScreen = new NotificationScreen();
            
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
    
    public void selezionaNotifica() {
        // Implementazione da definire
    }
    
    public void presaVisione() {
        // Implementazione da definire
    }
    
    public void accettazione() {
        // Implementazione da definire
    }
    
    public void rifiuto() {
        // Implementazione da definire
    }
    
    /**
     * Ottiene il riferimento alla NotificationScreen
     */
    public NotificationScreen getNotificationScreen() {
        return notificationScreen;
    }
    
}
