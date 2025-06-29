package com.cms.users.conference.Control;

import com.cms.users.conference.Interface.ConferenceCreationScreen;
import com.cms.users.Commons.DBMSBoundary;
import java.time.LocalDate;
import java.util.Date;

/**
 * <<control>>
 * CreaConferenzaControl
 */
public class CreaConferenzaControl {
    
    private ConferenceCreationScreen conferenceCreationScreen;
    private DBMSBoundary dbmsBoundary;
    
    /**
     * Costruttore
     */
    public CreaConferenzaControl() {
        this.dbmsBoundary = new DBMSBoundary();
    }
    
    /**
     * Crea e mostra la schermata di creazione conferenza
     * Segue il sequence diagram: User -> CreaConferenzaControl -> ConferenceCreationScreen
     */
    public void creaConferenza() {
        // Passo 1: Istanzia ConferenceCreationScreen
        this.conferenceCreationScreen = new ConferenceCreationScreen(this);
        
        // Passo 2: Chiama create() sulla ConferenceCreationScreen
        this.conferenceCreationScreen.create();
    }
    
    /**
     * Metodo chiamato da ConferenceCreationScreen per salvare la conferenza
     * Segue il sequence diagram: ConferenceCreationScreen -> CreaConferenzaControl -> DBMSBoundary
     */
    public boolean salvaConferenza(String titolo, String abstractText, String annoEdizione, 
                                  Date dataInizio, Date dataFine, Date deadlineSottomissione, 
                                  Date deadlineRitiro, Date deadlineRevisioni, Date deadlineVersioneFinale, 
                                  Date deadlineVersionePubblicazione, String luogo, String keywords,
                                  String numeroRevisoriStr, String numeroArticoliPrevistiStr, 
                                  String tassoAccettazioneStr) {
        
        try {
            // Converti i parametri nei tipi richiesti da DBMSBoundary.setConferenza
            int anno = Integer.parseInt(annoEdizione);
            int numeroRevisori = Integer.parseInt(numeroRevisoriStr);
            int numeroArticoliPrevisti = Integer.parseInt(numeroArticoliPrevistiStr);
            int tassoAccettazione = Integer.parseInt(tassoAccettazioneStr);
            
            // Converti Date in LocalDate
            LocalDate ldDataInizio = convertToLocalDate(dataInizio);
            LocalDate ldDataFine = convertToLocalDate(dataFine);
            LocalDate ldDeadlineSottomissione = convertToLocalDate(deadlineSottomissione);
            LocalDate ldDeadlineRitiro = convertToLocalDate(deadlineRitiro);
            LocalDate ldDeadlineRevisioni = convertToLocalDate(deadlineRevisioni);
            LocalDate ldDeadlineVersioneFinale = convertToLocalDate(deadlineVersioneFinale);
            LocalDate ldDeadlineVersionePubblicazione = convertToLocalDate(deadlineVersionePubblicazione);
            
            // Chiama setConferenza del DBMSBoundary seguendo il sequence diagram
            dbmsBoundary.setConferenza(
                titolo, 
                anno, 
                abstractText, 
                ldDataInizio, 
                ldDataFine, 
                ldDeadlineSottomissione, 
                ldDeadlineRitiro, 
                ldDeadlineRevisioni, 
                ldDeadlineVersioneFinale, 
                ldDeadlineVersionePubblicazione, 
                luogo, 
                numeroRevisori, 
                numeroArticoliPrevisti, 
                tassoAccettazione, 
                keywords
            );
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Errore durante il salvataggio della conferenza: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Converte Date in LocalDate
     */
    private LocalDate convertToLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }
    
    /**
     * Ottiene l'istanza della ConferenceCreationScreen
     */
    public ConferenceCreationScreen getConferenceCreationScreen() {
        return this.conferenceCreationScreen;
    }
}
