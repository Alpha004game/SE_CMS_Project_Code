package com.cms.users.submissions.Control;

import java.util.ArrayList;
import java.util.LinkedList;
import com.cms.users.Entity.ArticoloE;
import com.cms.App;
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
            SubmissionScreen submissionScreen = new SubmissionScreen(idUtente, idConferenza);
            submissionScreen.mostraListaSottomissioni(dbmsBoundary.getListaSottomissioni(idUtente, idConferenza));
            submissionScreen.setVisible(true);
            
        } catch (Exception e) {
            System.err.println("Errore durante il salvataggio della sottomissione: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Modifica una sottomissione esistente nel database
     * @param idArticolo ID dell'articolo da modificare
     * @param articolo Oggetto ArticoloE con i nuovi dati
     * @param status Nuovo stato dell'articolo
     * @return true se la modifica è riuscita, false altrimenti
     */
    public boolean modificaSottomissione(int idArticolo, ArticoloE articolo, String status) {
        try {
            // Valida i parametri
            if (idArticolo <= 0) {
                System.err.println("ID articolo non valido: " + idArticolo);
                return false;
            }
            
            if (articolo == null) {
                System.err.println("Articolo non può essere null");
                return false;
            }
            
            // Verifica che l'articolo esista
            ArticoloE articoloEsistente = dbmsBoundary.getArticolo(idArticolo);
            if (articoloEsistente == null) {
                System.err.println("Articolo con ID " + idArticolo + " non trovato");
                return false;
            }
            
            // Imposta l'ID dell'articolo se non è già impostato
            if (articolo.getId() == 0) {
                articolo.setId(idArticolo);
            }
            
            // Imposta lo stato se non fornito
            String statoFinale = status != null ? status : "Modificato";
            
            // Chiama il DBMSBoundary per modificare la sottomissione
            dbmsBoundary.modificaSottomissione(idArticolo, articolo, statoFinale);
            
            System.out.println("Sottomissione modificata con successo - ID: " + idArticolo);

            return true;
            
        } catch (Exception e) {
            System.err.println("Errore durante la modifica della sottomissione: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        finally
        {
            SubmissionScreen submissionScreen = new SubmissionScreen(App.utenteAccesso.getId(), articolo.getIdConferenza());
            submissionScreen.mostraListaSottomissioni(dbmsBoundary.getListaSottomissioni(App.utenteAccesso.getId(), articolo.getIdConferenza()));
            submissionScreen.setVisible(true);
        }

        
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
                System.out.println("DEBUG: Creazione ViewDetailsSubmissionScreen con ID articolo: " + idArticolo);
                ViewDetailsSubmissionScreen viewDetailsScreen = new ViewDetailsSubmissionScreen(idArticolo);
                
                // La schermata ora carica automaticamente i dati dal database, incluse le keywords
                System.out.println("DEBUG: Dati caricati automaticamente dal database");
                
                // Passo 3: Mostra la schermata
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
    
    /**
     * Carica i dati di un articolo dal database per la visualizzazione
     * @param idArticolo ID dell'articolo da caricare
     * @return ArticoloE oggetto con i dati dell'articolo, null se non trovato
     */
    public ArticoloE caricaArticolo(int idArticolo) {
        try {
            ArticoloE articolo = dbmsBoundary.getArticolo(idArticolo);
            if (articolo != null) {
                // Carica anche le keywords associate all'articolo
                ArrayList<String> keywords = dbmsBoundary.getKeywordsArticolo(idArticolo);
                if (keywords != null && !keywords.isEmpty()) {
                    LinkedList<String> keywordsList = new LinkedList<>(keywords);
                    articolo.setKeywords(keywordsList);
                }
                System.out.println("DEBUG: Articolo caricato con successo - ID: " + idArticolo);
            } else {
                System.err.println("Articolo non trovato con ID: " + idArticolo);
            }
            return articolo;
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento dell'articolo: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Ottiene le keywords associate a un articolo
     * @param idArticolo ID dell'articolo
     * @return ArrayList<String> lista delle keywords
     */
    public ArrayList<String> ottieniKeywordsArticolo(int idArticolo) {
        try {
            return dbmsBoundary.getKeywordsArticolo(idArticolo);
        } catch (Exception e) {
            System.err.println("Errore durante il recupero delle keywords: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Ottiene le keywords disponibili per una conferenza
     * @param idConferenza ID della conferenza
     * @return ArrayList<String> lista delle keywords della conferenza
     */
    public ArrayList<String> ottieniKeywordsConferenza(int idConferenza) {
        try {
            return dbmsBoundary.getKeywords(idConferenza);
        } catch (Exception e) {
            System.err.println("Errore durante il recupero delle keywords della conferenza: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Gestisce l'operazione di ritiro di una sottomissione
     * @param idArticolo ID dell'articolo da ritirare
     * @return true se l'operazione è riuscita, false altrimenti
     */
    public boolean ritiraSottomissione(int idArticolo) {
        try {
            // Qui andrà la logica per ritirare la sottomissione dal database
            System.out.println("DEBUG: Ritiro sottomissione ID: " + idArticolo);
            // TODO: Implementare la logica di ritiro nel DBMSBoundary
            dbmsBoundary.deleteSubmission(idArticolo);
            return true;
        } catch (Exception e) {
            System.err.println("Errore durante il ritiro della sottomissione: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Gestisce il download del rapporto di valutazione
     * @param idArticolo ID dell'articolo
     * @return String path del file generato o null se errore
     */
    public String scaricaRapportoValutazione(int idArticolo) {
        try {
            // Qui andrà la logica per generare e scaricare il rapporto
            System.out.println("DEBUG: Generazione rapporto valutazione per articolo ID: " + idArticolo);
            // TODO: Implementare la logica nel DBMSBoundary
            return "rapporto_valutazione_" + idArticolo + ".pdf";
        } catch (Exception e) {
            System.err.println("Errore durante la generazione del rapporto: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Verifica se una sottomissione può essere modificata
     * @param idArticolo ID dell'articolo
     * @return true se può essere modificata, false altrimenti
     */
    public boolean puoModificareArticolo(int idArticolo) {
        try {
            // Logica per determinare se l'articolo può essere modificato
            // (es. controllo stato, deadline, ecc.)
            ArticoloE articolo = dbmsBoundary.getArticolo(idArticolo);
            if (articolo != null) {
                String stato = articolo.getStato();
                // Esempio: può essere modificato solo se in stato "Inviato"
                return "Inviato".equals(stato);
            }
            return false;
        } catch (Exception e) {
            System.err.println("Errore durante la verifica permessi modifica: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica se una sottomissione può essere ritirata
     * @param idArticolo ID dell'articolo
     * @return true se può essere ritirata, false altrimenti
     */
    public boolean puoRitirareArticolo(int idArticolo) {
        try {
            // Logica per determinare se l'articolo può essere ritirato
            ArticoloE articolo = dbmsBoundary.getArticolo(idArticolo);
            if (articolo != null) {
                String stato = articolo.getStato();
                // Esempio: può essere ritirato se non ancora pubblicato
                return !"Pubblicato".equals(stato) && !"Rifiutato".equals(stato);
            }
            return false;
        } catch (Exception e) {
            System.err.println("Errore durante la verifica permessi ritiro: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica se è disponibile un rapporto di valutazione
     * @param idArticolo ID dell'articolo
     * @return true se il rapporto è disponibile, false altrimenti
     */
    public boolean haRapportoValutazione(int idArticolo) {
        try {
            // Logica per verificare se esiste un rapporto di valutazione
            ArticoloE articolo = dbmsBoundary.getArticolo(idArticolo);
            if (articolo != null) {
                String stato = articolo.getStato();
                // Esempio: rapporto disponibile se in revisione o completato
                return "In Revisione".equals(stato) || "Accettato".equals(stato) || "Rifiutato".equals(stato);
            }
            return false;
        } catch (Exception e) {
            System.err.println("Errore durante la verifica rapporto: " + e.getMessage());
            return false;
        }
    }
    
}
