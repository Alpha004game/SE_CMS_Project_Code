package com.cms.users.submissions.Control;

import com.cms.App;
import com.cms.users.Entity.ArticoloE;
import com.cms.users.submissions.Interface.GeneralSubmissionScreen;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <<control>>
 * GestionePreferenzeControl - Control per la gestione delle preferenze sugli articoli
 */
public class GestionePreferenzeControl {
    
    private GeneralSubmissionScreen generalSubmissionScreen;
    private int idConferenza;
    private LinkedList<ArticoloE> articoliConferenza;
    
    /**
     * Costruttore
     */
    public GestionePreferenzeControl() {
        this.idConferenza = -1;
        this.articoliConferenza = new LinkedList<>();
    }
    
    /**
     * Apre la schermata per modificare le preferenze degli articoli
     * @param idConferenza ID della conferenza di cui modificare le preferenze
     */
    public void modificaPreferenze(int idConferenza) {
        System.out.println("DEBUG GestionePreferenzeControl: === INIZIO modificaPreferenze ===");
        System.out.println("DEBUG GestionePreferenzeControl: idConferenza ricevuto: " + idConferenza);
        
        this.idConferenza = idConferenza;
        
        try {
            // Recupera la lista degli articoli della conferenza dal DB
            System.out.println("DEBUG GestionePreferenzeControl: Chiamando App.dbms.getListaArticoli(" + idConferenza + ")");
            articoliConferenza = App.dbms.getListaArticoli(idConferenza);
            
            System.out.println("DEBUG GestionePreferenzeControl: Risultato getListaArticoli:");
            if (articoliConferenza == null) {
                System.out.println("  - null (errore durante il recupero)");
                articoliConferenza = new LinkedList<>(); // Lista vuota come fallback
            } else {
                System.out.println("  - Lista con " + articoliConferenza.size() + " articoli");
                for (int i = 0; i < articoliConferenza.size(); i++) {
                    ArticoloE articolo = articoliConferenza.get(i);
                    System.out.println("    [" + i + "] ID:" + articolo.getId() + " Titolo:'" + articolo.getTitolo() + "'");
                }
            }
            
            // Converte ArticoloE in stringhe per la GeneralSubmissionScreen
            List<String> titoliArticoli = new ArrayList<>();
            for (ArticoloE articolo : articoliConferenza) {
                titoliArticoli.add(articolo.getTitolo());
            }
            
            // Crea GeneralSubmissionScreen con la lista degli articoli
            System.out.println("DEBUG GestionePreferenzeControl: Creando GeneralSubmissionScreen con " + titoliArticoli.size() + " articoli");
            generalSubmissionScreen = new GeneralSubmissionScreen(titoliArticoli, this);
            
            // Mostra la schermata
            System.out.println("DEBUG GestionePreferenzeControl: Mostrando GeneralSubmissionScreen");
            generalSubmissionScreen.create();
            
            System.out.println("DEBUG GestionePreferenzeControl: === FINE modificaPreferenze ===");
            
        } catch (Exception e) {
            System.err.println("DEBUG GestionePreferenzeControl: ERRORE durante modificaPreferenze: " + e.getMessage());
            e.printStackTrace();
            
            // In caso di errore, mostra comunque la schermata con articoli vuoti
            generalSubmissionScreen = new GeneralSubmissionScreen(new ArrayList<>(), this);
            generalSubmissionScreen.create();
        }
    }
    
    /**
     * Salva le preferenze selezionate dall'utente
     * @param articlePreferences Lista delle preferenze selezionate
     */
    public void salvaPreferenze(List<GeneralSubmissionScreen.ArticlePreference> articlePreferences) {
        System.out.println("DEBUG GestionePreferenzeControl: === INIZIO salvaPreferenze ===");
        System.out.println("DEBUG GestionePreferenzeControl: idConferenza: " + idConferenza);
        System.out.println("DEBUG GestionePreferenzeControl: Ricevute " + articlePreferences.size() + " preferenze");
        
        try {
            // Ottieni l'ID dell'utente corrente
            if (App.utenteAccesso == null) {
                System.err.println("DEBUG GestionePreferenzeControl: ERRORE - Nessun utente loggato");
                return;
            }
            
            int idUtente = App.utenteAccesso.getId();
            System.out.println("DEBUG GestionePreferenzeControl: idUtente ottenuto: " + idUtente);
            
            if (idConferenza == -1) {
                System.err.println("DEBUG GestionePreferenzeControl: ERRORE - Nessuna conferenza specificata");
                return;
            }
            
            // Processa ogni preferenza
            for (GeneralSubmissionScreen.ArticlePreference pref : articlePreferences) {
                // Trova l'ID dell'articolo dal titolo
                int idArticolo = trovaIdArticoloDaTitolo(pref.getTitolo());
                
                if (idArticolo != -1) {
                    System.out.println("DEBUG GestionePreferenzeControl: Processando articolo '" + pref.getTitolo() + "' (ID: " + idArticolo + ")");
                    
                    // Salva interesse se selezionato
                    if (pref.isInteresse()) {
                        System.out.println("DEBUG GestionePreferenzeControl: Salvando INTERESSE per articolo " + idArticolo);
                        App.dbms.newInteresse(idConferenza, idUtente, idArticolo);
                    }
                    
                    // Salva conflitto se selezionato
                    if (pref.isConflittoInteresse()) {
                        System.out.println("DEBUG GestionePreferenzeControl: Salvando CONFLITTO per articolo " + idArticolo);
                        App.dbms.newConflitto(idConferenza, idUtente, idArticolo);
                    }
                } else {
                    System.err.println("DEBUG GestionePreferenzeControl: ERRORE - Impossibile trovare ID per articolo: '" + pref.getTitolo() + "'");
                }
            }
            
            System.out.println("DEBUG GestionePreferenzeControl: Preferenze salvate con successo");
            
        } catch (Exception e) {
            System.err.println("DEBUG GestionePreferenzeControl: ERRORE durante salvaPreferenze: " + e.getMessage());
            e.printStackTrace();
            throw e; // Rilancia l'eccezione per gestirla nell'interfaccia
        }
        
        System.out.println("DEBUG GestionePreferenzeControl: === FINE salvaPreferenze ===");
    }
    
    /**
     * Trova l'ID di un articolo dal suo titolo
     * @param titolo Titolo dell'articolo
     * @return ID dell'articolo o -1 se non trovato
     */
    private int trovaIdArticoloDaTitolo(String titolo) {
        for (ArticoloE articolo : articoliConferenza) {
            if (articolo.getTitolo().equals(titolo)) {
                return articolo.getId();
            }
        }
        return -1;
    }
    
    /**
     * Ottiene il riferimento alla GeneralSubmissionScreen
     */
    public GeneralSubmissionScreen getGeneralSubmissionScreen() {
        return generalSubmissionScreen;
    }
    
    /**
     * Ottiene l'ID della conferenza corrente
     */
    public int getIdConferenza() {
        return idConferenza;
    }
    
    /**
     * Ottiene la lista degli articoli della conferenza
     */
    public LinkedList<ArticoloE> getArticoliConferenza() {
        return articoliConferenza;
    }
}
