package com.cms.users.revisions.Control;

import com.cms.App;
import com.cms.users.Entity.ArticoloE;
import com.cms.users.Commons.ListScreen;
import java.util.LinkedList;

/**
 * <<control>>
 * GestioneRevisioneControl - Control per la gestione delle revisioni
 */
public class GestioneRevisioneControl {
    
    private ListScreen listScreen;
    private int idConferenza;
    
    /**
     * Costruttore
     */
    public GestioneRevisioneControl() {
        this.idConferenza = -1;
    }
    
    /**
     * Implementa il sequence diagram per visualizzare gli articoli assegnati
     * @param idConferenza ID della conferenza
     */
    public void visualizzaArticoliAssegnati(int idConferenza) {
        System.out.println("DEBUG GestioneRevisioneControl: === INIZIO visualizzaArticoliAssegnati ===");
        System.out.println("DEBUG GestioneRevisioneControl: idConferenza ricevuto: " + idConferenza);
        
        this.idConferenza = idConferenza;
        
        try {
            // Verifica che l'utente sia loggato
            if (App.utenteAccesso == null) {
                System.err.println("DEBUG GestioneRevisioneControl: ERRORE - Nessun utente loggato");
                return;
            }
            
            int idRevisore = App.utenteAccesso.getId();
            System.out.println("DEBUG GestioneRevisioneControl: idRevisore: " + idRevisore);
            
            // Seguendo il sequence diagram: getListaArticoliAssegnati(idRevisore, idConferenza) su BoundaryDBMS
            System.out.println("DEBUG GestioneRevisioneControl: Chiamando App.dbms.getListaArticoliAssegnati(" + idRevisore + ", " + idConferenza + ")");
            LinkedList<ArticoloE> articoliAssegnati = App.dbms.getListaArticoliAssegnati(idRevisore, idConferenza);
            
            System.out.println("DEBUG GestioneRevisioneControl: Risultato getListaArticoliAssegnati:");
            if (articoliAssegnati == null) {
                System.out.println("  - null (errore durante il recupero)");
                articoliAssegnati = new LinkedList<>(); // Lista vuota come fallback
            } else {
                System.out.println("  - Lista con " + articoliAssegnati.size() + " elementi");
                for (int i = 0; i < articoliAssegnati.size(); i++) {
                    ArticoloE articolo = articoliAssegnati.get(i);
                    System.out.println("    [" + i + "] ID: " + articolo.getId() + " - '" + articolo.getTitolo() + "'");
                }
            }
            
            // Crea ListScreen con ruolo REVISORE e funzione ARTICLES_TO_REVIEW
            System.out.println("DEBUG GestioneRevisioneControl: Creando ListScreen per REVISORE");
            listScreen = new ListScreen(ListScreen.UserRole.REVISORE, ListScreen.RevisoreFunction.ARTICLES_TO_REVIEW);
            
            // Imposta i dati degli articoli nella ListScreen
            listScreen.setRevisoreArticleDataFromArticoli(articoliAssegnati);
            
            // Mostra la schermata
            System.out.println("DEBUG GestioneRevisioneControl: Mostrando ListScreen");
            listScreen.setVisible(true);
            
            System.out.println("DEBUG GestioneRevisioneControl: === FINE visualizzaArticoliAssegnati ===");
            
        } catch (Exception e) {
            System.err.println("DEBUG GestioneRevisioneControl: ERRORE durante visualizzaArticoliAssegnati: " + e.getMessage());
            e.printStackTrace();
            
            // In caso di errore, mostra comunque la schermata con lista vuota
            listScreen = new ListScreen(ListScreen.UserRole.REVISORE, ListScreen.RevisoreFunction.ARTICLES_TO_REVIEW);
            listScreen.setHasData(false);
            listScreen.setVisible(true);
        }
    }
    
    /**
     * Ottiene il riferimento alla ListScreen
     */
    public ListScreen getListScreen() {
        return listScreen;
    }
    
    /**
     * Ottiene l'ID della conferenza corrente
     */
    public int getIdConferenza() {
        return idConferenza;
    }
    
    // Metodi esistenti della classe mantenuti per compatibilità
    public void create() {
        // Implementazione da definire
    }
    
    public void revisionaArticolo(String idArticolo) {
        // Implementazione da definire
    }
    
    public void convocaSottoRevisore() {
        // Implementazione da definire
    }
    
    public void visualizzaRevisioneDelegata(String idArticolo) {
        // Implementazione da definire
    }
    
    public void accettaSottoRevisione() {
        // Implementazione da definire
    }
    
    public void rifiutaSottoRevisione() {
        // Implementazione da definire
    }
    
    public void rinunciaArticolo() {
        // Implementazione da definire
    }
    
    public void visualizzaRevisioni(String idArticolo) {
        // Implementazione da definire
    }
    
    public void scaricaArticoloEAllegato(String fileArticolo, String fileAllegato) {
        // Implementazione da definire
    }
}
