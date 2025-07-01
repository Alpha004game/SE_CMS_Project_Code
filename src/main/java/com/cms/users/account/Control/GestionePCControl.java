package com.cms.users.account.Control;

import com.cms.App;
import com.cms.users.account.Interface.SkillsSelectionScreen;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

/**
 * <<control>>
 * GestionePCControl - Control per la gestione della selezione delle competenze specifiche
 */
public class GestionePCControl {
    
    private SkillsSelectionScreen skillsSelectionScreen;
    private int idConferenza;
    
    /**
     * Costruttore
     */
    public GestionePCControl() {
        this.idConferenza = -1;
    }
    
    /**
     * Implementa il sequence diagram per la specifica delle competenze
     * @param idConferenza ID della conferenza per cui specificare le competenze
     */
    public void specificaCompetenze(int idConferenza) {
        System.out.println("DEBUG GestionePCControl: === INIZIO specificaCompetenze ===");
        System.out.println("DEBUG GestionePCControl: idConferenza ricevuto: " + idConferenza);
        
        this.idConferenza = idConferenza;
        
        try {
            // Seguendo il sequence diagram: getKeywordsList(idConferenza) su BoundaryDBMS
            System.out.println("DEBUG GestionePCControl: Chiamando App.dbms.getKeywords(" + idConferenza + ")");
            ArrayList<String> keywordsList = App.dbms.getKeywords(idConferenza);
            
            System.out.println("DEBUG GestionePCControl: Risultato getKeywords:");
            if (keywordsList == null) {
                System.out.println("  - null (errore durante il recupero)");
                keywordsList = new ArrayList<>(); // Lista vuota come fallback
            } else {
                System.out.println("  - Lista con " + keywordsList.size() + " elementi");
                for (int i = 0; i < keywordsList.size(); i++) {
                    System.out.println("    [" + i + "] '" + keywordsList.get(i) + "'");
                }
            }
            
            // Crea SkillsSelectionScreen con riferimento al control
            System.out.println("DEBUG GestionePCControl: Creando SkillsSelectionScreen con control");
            skillsSelectionScreen = new SkillsSelectionScreen(this);
            
            // Imposta le keywords disponibili dalla conferenza
            System.out.println("DEBUG GestionePCControl: Impostando keywords disponibili");
            skillsSelectionScreen.setAvailableKeywords(keywordsList);
            
            // Mostra la schermata
            System.out.println("DEBUG GestionePCControl: Mostrando SkillsSelectionScreen");
            skillsSelectionScreen.create();
            
            System.out.println("DEBUG GestionePCControl: === FINE specificaCompetenze ===");
            
        } catch (Exception e) {
            System.err.println("DEBUG GestionePCControl: ERRORE durante specificaCompetenze: " + e.getMessage());
            e.printStackTrace();
            
            // In caso di errore, mostra comunque la schermata con keywords vuote
            skillsSelectionScreen = new SkillsSelectionScreen(this);
            skillsSelectionScreen.setAvailableKeywords(new ArrayList<>());
            skillsSelectionScreen.create();
        }
    }
    
    /**
     * Salva le competenze selezionate dal revisore
     * Implementa il flusso del sequence diagram per il salvataggio delle competenze
     * @param selectedKeywords Lista delle keywords selezionate dal revisore
     */
    public void salvaCompetenze(List<String> selectedKeywords) {
        System.out.println("DEBUG GestionePCControl: === INIZIO salvaCompetenze ===");
        System.out.println("DEBUG GestionePCControl: idConferenza: " + idConferenza);
        System.out.println("DEBUG GestionePCControl: selectedKeywords: " + selectedKeywords);
        
        try {
            // Seguendo il sequence diagram: getId() dall'utente corrente
            if (App.utenteAccesso == null) {
                System.err.println("DEBUG GestionePCControl: ERRORE - Nessun utente loggato");
                return;
            }
            
            int idUtente = App.utenteAccesso.getId();
            System.out.println("DEBUG GestionePCControl: idUtente ottenuto: " + idUtente);
            
            if (idConferenza == -1) {
                System.err.println("DEBUG GestionePCControl: ERRORE - Nessuna conferenza specificata");
                return;
            }
            
            // Seguendo il sequence diagram: sendNewSkillInformation(idUtente, idConferenza, skills)
            System.out.println("DEBUG GestionePCControl: Chiamando App.dbms.sendNewSkillInformation(" + 
                              idUtente + ", " + idConferenza + ", selectedKeywords)");
            
            App.dbms.sendNewSkillInformation(idUtente, idConferenza, selectedKeywords);
            
            System.out.println("DEBUG GestionePCControl: Competenze salvate con successo");
            
        } catch (Exception e) {
            System.err.println("DEBUG GestionePCControl: ERRORE durante salvaCompetenze: " + e.getMessage());
            e.printStackTrace();
            throw e; // Rilancia l'eccezione per gestirla nell'interfaccia
        }
        
        System.out.println("DEBUG GestionePCControl: === FINE salvaCompetenze ===");
    }
    
    /**
     * Ottiene il riferimento alla SkillsSelectionScreen
     */
    public SkillsSelectionScreen getSkillsSelectionScreen() {
        return skillsSelectionScreen;
    }
    
    /**
     * Ottiene l'ID della conferenza corrente
     */
    public int getIdConferenza() {
        return idConferenza;
    }
}
