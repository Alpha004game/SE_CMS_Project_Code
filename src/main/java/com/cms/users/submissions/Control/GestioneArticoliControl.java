package com.cms.users.submissions.Control;

import java.util.ArrayList;
import java.util.LinkedList;
import com.cms.users.Entity.ArticoloE;
import com.cms.users.Commons.DBMSBoundary;
import com.cms.users.submissions.Interface.SubmissionScreen;
import com.cms.users.submissions.Interface.NewSubmissionScreen;
import com.cms.users.submissions.Interface.ViewDetailsSubmissionScreen;

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
    
    /**
     * Visualizza i dettagli di un articolo seguendo il sequence diagram
     * Segue il flusso: GestioneArticoliControl -> DBMSBoundary.getArticolo() -> ViewDetailsSubmissionScreen
     */
    public void visualizzaDettagli(int idArticolo) {
        System.out.println("DEBUG GestioneArticoliControl: visualizzaDettagli chiamato con ID: " + idArticolo);
        
        try {
            // Passo 1: Chiama getArticolo(idArticolo) del DBMSBoundary seguendo il sequence diagram
            System.out.println("DEBUG: Chiamata a DBMSBoundary.getArticolo(" + idArticolo + ")");
            ArticoloE articolo = dbmsBoundary.getArticolo(idArticolo);
            
            if (articolo != null) {
                System.out.println("DEBUG: Articolo trovato: " + articolo.getTitolo());
                
                // Passo 2: Crea ViewDetailsSubmissionScreen seguendo il sequence diagram
                System.out.println("DEBUG: Creazione ViewDetailsSubmissionScreen");
                ViewDetailsSubmissionScreen viewDetailsScreen = new ViewDetailsSubmissionScreen();
                
                // Passo 3: Popola la schermata con i dati reali dell'articolo
                System.out.println("DEBUG: Popolamento dati articolo");
                viewDetailsScreen.setArticoloData(articolo);
                
                // Passo 4: Mostra la schermata
                System.out.println("DEBUG: Mostra schermata dettagli");
                viewDetailsScreen.setVisible(true);
                
                System.out.println("DEBUG: ViewDetailsSubmissionScreen mostrata con successo");
                
            } else {
                System.err.println("ERRORE: Articolo non trovato con ID: " + idArticolo);
                System.err.println("DEBUG: Creazione di un articolo di test per il debug...");
                
                // Crea un articolo di test per verificare che l'UI funzioni
                ArticoloE articoloTest = new ArticoloE();
                articoloTest.setId(idArticolo);
                articoloTest.setTitolo("Articolo di Test - ID " + idArticolo);
                articoloTest.setAbstractText("Questo è un articolo di test creato perché l'articolo originale non è stato trovato nel database.");
                
                // Aggiunge alcune keywords di test
                LinkedList<String> keywordsTest = new LinkedList<>();
                keywordsTest.add("Test");
                keywordsTest.add("Debug");
                keywordsTest.add("Software Engineering");
                articoloTest.setKeywords(keywordsTest);
                
                // Aggiunge co-autori di test
                LinkedList<String> coAutoriTest = new LinkedList<>();
                coAutoriTest.add("Mario Rossi");
                coAutoriTest.add("Luigi Verdi");
                articoloTest.setCoAutori(coAutoriTest);
                
                articoloTest.setDichiarazioneOriginalita(true);
                
                ViewDetailsSubmissionScreen viewDetailsScreen = new ViewDetailsSubmissionScreen();
                viewDetailsScreen.setArticoloData(articoloTest);
                viewDetailsScreen.setVisible(true);
                
                System.out.println("DEBUG: Mostrata schermata con articolo di test");
            }
            
        } catch (Exception e) {
            System.err.println("ERRORE durante la visualizzazione dei dettagli dell'articolo: " + e.getMessage());
            e.printStackTrace();
            
            // Mostra comunque una schermata di errore per il debug
            try {
                ViewDetailsSubmissionScreen errorScreen = new ViewDetailsSubmissionScreen();
                errorScreen.setVisible(true);
                System.out.println("DEBUG: Mostrata schermata vuota per debug errori");
            } catch (Exception e2) {
                System.err.println("ERRORE critico: impossibile mostrare anche la schermata di errore: " + e2.getMessage());
                e2.printStackTrace();
            }
        }
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
