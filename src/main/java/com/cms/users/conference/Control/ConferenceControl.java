package com.cms.users.conference.Control;

import com.cms.users.Commons.DBMSBoundary;
import com.cms.users.Entity.ConferenzaE;
import com.cms.users.Entity.UtenteE;
import com.cms.users.conference.Interface.ConferenceManagementScreen;
import com.cms.users.conference.Interface.MemberListScreen;
import java.util.LinkedList;

/**
 * <<control>>
 * ConferenceControl
 */
public class ConferenceControl {
    
    private DBMSBoundary dbmsBoundary;
    
    public ConferenceControl() {
        this.dbmsBoundary = new DBMSBoundary();
    }
    
    /**
     * Carica i dati di una conferenza e apre la schermata di gestione
     * Segue il sequence diagram: HomeScreen -> ConferenceControl -> DBMSBoundary
     */
    public void apriGestioneConferenza(int idConferenza) {
        // Chiama DBMSBoundary per ottenere le informazioni della conferenza
        ConferenzaE conferenza = (ConferenzaE) dbmsBoundary.getConferenceInfo(idConferenza);
        
        if (conferenza != null) {
            // Crea e mostra la schermata di gestione conferenza con i dati caricati
            ConferenceManagementScreen screen = new ConferenceManagementScreen();
            screen.loadConferenceData(conferenza);
            screen.setVisible(true);
        } else {
            // Gestione errore nel caricamento
            System.err.println("Errore: impossibile caricare i dati della conferenza con ID " + idConferenza);
        }
    }
    
    // Metodi
    public void create() {
        // Implementazione da definire
    }
    
    public void invitaCoChair() {
        // Implementazione da definire
    }
    
    public void addReviewer() {
        // Implementazione da definire
    }
    
    /**
     * Gestisce l'aggiunta di un revisore seguendo il sequence diagram
     * ConferenceManagementScreen -> ConferenceControl -> DBMSBoundary -> MemberListScreen
     */
    public void addReviewer(int idConferenza) {
        // Ottieni tutti gli utenti dal database
        LinkedList<UtenteE> tuttiGliUtenti = dbmsBoundary.getUsersInfo();
        
        // Crea la MemberListScreen per selezionare un revisore
        MemberListScreen memberListScreen = new MemberListScreen(
            MemberListScreen.UserRole.CHAIR, 
            MemberListScreen.Action.ADD_REVIEWER
        );
        
        if (tuttiGliUtenti != null && !tuttiGliUtenti.isEmpty()) {
            // Imposta la lista degli utenti disponibili
            memberListScreen.setUserList(tuttiGliUtenti);
        } else {
            // Nessun utente disponibile
            memberListScreen.setHasData(false);
        }
        
        // Mostra la schermata
        memberListScreen.setVisible(true);
    }
    
    public int calculateReviewerNumber() {
        // Implementazione da definire
        return 0;
    }
    
    public void rimuoviRevisore() {
        // Implementazione da definire
    }
    
    public void assegnaRevisore() {
        // Implementazione da definire
    }
    
    public void asegnaRevisoriAutomaticamente(String idConferenza) {
        // Implementazione da definire
    }
    
    public void assegnaRevisoriPerPreferenze(String idConferenza) {
        // Implementazione da definire
    }
    
    public void conferenzaAssegnazione(String idArticolo, String idRevisore) {
        // Implementazione da definire
    }
    
    public void rimuoviArticoloRevisore() {
        // Implementazione da definire
    }
    
    public void rimuoviRevisore(String idRevisore) {
        // Implementazione da definire
    }
    
    public void visualizzaStatoRevisioni() {
        // Implementazione da definire
    }
    
    public void visualizzaStato(String idArticolo) {
        // Implementazione da definire
    }
    
    public void sendCommunication() {
        // Implementazione da definire
    }
    
    public String getLog() {
        // Implementazione da definire
        return null;
    }
    
    public void compilaLog() {
        // Implementazione da definire
    }
    
    public void salvaLog() {
        // Implementazione da definire
    }
    
    public void revisionaArticolo(String idArticolo) {
        // Implementazione da definire
    }
    
}
