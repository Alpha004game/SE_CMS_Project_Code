package com.cms.users.submissions.Control;

import java.util.ArrayList;
import java.util.LinkedList;
import com.cms.users.Entity.ArticoloE;
import com.cms.users.Commons.DBMSBoundary;
import com.cms.users.submissions.Interface.SubmissionScreen;
import com.cms.users.submissions.Interface.NewSubmissionScreen;

/**
 * <<control>>
 * GestioneArticoliControl
 */
public class GestioneArticoliControl {
    
    private DBMSBoundary dbmsBoundary;
    
    public GestioneArticoliControl() {
        this.dbmsBoundary = new DBMSBoundary();
    }
    
    // Metodi
    public void create() {
        // Implementazione da definire
    }
    
    /**
     * Visualizza le sottomissioni dell'utente per una conferenza specifica
     * Segue il sequence diagram "Visualizza Sottomissioni"
     */
    public void visualizzaSottomissioni(int idUtente, int idConferenza) {
        try {
            // Richiede la lista delle sottomissioni al DBMSBoundary
            LinkedList<ArticoloE> listaSottomissioni = dbmsBoundary.getListaSottomissioni(idUtente, idConferenza);
            
            // Crea e mostra la SubmissionScreen con i risultati e le informazioni per l'integrazione
            SubmissionScreen submissionScreen = new SubmissionScreen(idUtente, idConferenza);
            submissionScreen.mostraListaSottomissioni(listaSottomissioni);
            submissionScreen.setVisible(true);
            
        } catch (Exception e) {
            System.err.println("Errore durante la visualizzazione delle sottomissioni: " + e.getMessage());
            e.printStackTrace();
            
            // Mostra una schermata di errore o messaggio all'utente
            SubmissionScreen errorScreen = new SubmissionScreen(idUtente, idConferenza);
            errorScreen.mostraErrore("Errore durante il caricamento delle sottomissioni");
            errorScreen.setVisible(true);
        }
    }
    
    public void creaNuovaSottomissione() {
        // Implementazione da definire
    }
    
    /**
     * Crea una nuova sottomissione seguendo il sequence diagram
     */
    public void creaNuovaSottomissione(int idUtente, int idConferenza) {
        try {
            // Ottiene le keywords della conferenza usando il metodo corretto del DBMSBoundary
            ArrayList<String> keywordsConferenza = dbmsBoundary.getKeywords(idConferenza);
            
            // Crea e mostra la NewSubmissionScreen con le keywords della conferenza
            NewSubmissionScreen newSubmissionScreen = new NewSubmissionScreen(idUtente, idConferenza, keywordsConferenza);
            newSubmissionScreen.create();
            
        } catch (Exception e) {
            System.err.println("Errore durante la creazione della nuova sottomissione: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Salva la sottomissione nel database
     */
    public void salvaSottomissione(ArticoloE articolo, int idUtente, int idConferenza) {
        try {
            // Chiama il DBMSBoundary per salvare la sottomissione
            dbmsBoundary.setSottomissione(articolo, idUtente, idConferenza);
            
        } catch (Exception e) {
            System.err.println("Errore durante il salvataggio della sottomissione: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public void modificaSottomissione() {
        // Implementazione da definire
    }
    
    public void ritiraSottomissione() {
        // Implementazione da definire
    }
    
    public void visualizzaDettagli(String idArticolo) {
        // Implementazione da definire
    }
    
    public String scaricaRapportoValutazione() {
        // Implementazione da definire
        return null;
    }
    
    public void elaboraPDF(String revisioni) {
        // Implementazione da definire
    }
    
    public String downloadPDF() {
        // Implementazione da definire
        return null;
    }
    
}
