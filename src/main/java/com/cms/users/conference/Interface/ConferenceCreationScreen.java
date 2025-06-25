package com.cms.users.conference.Interface;

/**
 * <<boundary>>
 * ConferenceCreationScreen
 */
public class ConferenceCreationScreen {
    
    // Attributi
    private String titolo; // TextField
    private String annoEdizione; // TextField
    private String Abstract; // TextField
    private String dataInizio; // Calendar
    private String dataFine; // Calendar
    private String deadlineSottomissione; // Calendar
    private String deadlineNotifica; // Calendar
    private String deadlineRevisioni; // Calendar
    private String deadlineVersioneFinale; // Calendar
    private String deadlineVersionePubblicazione; // Calendar
    private String luogo; // TextField
    private String numRevisorPerArticolo; // TextField
    private String numeroArticoliPrevisti; // TextField
    private String tassoAccettazione; // TextField
    private String keywords; // ArrayList<TextField>
    private int id;
    
    // Metodi
    public void create() {
        // Implementazione da definire
    }
    
    public String creaConferenza(String titolo, String abstractText, String anno, String dataInizio, String dataFine,
                                String dataSottomissione, String dataRitiro, String dataRevisioni,
                                String dataVersioneFinale, String dataVersioneDelPubblicare, String luogo,
                                String temi, String keyword, String numeroRevisori, String numeroArticoliMassimo,
                                String numeroArticoliPrevisti, String tassoAccettazione) {
        // Implementazione da definire
        return null;
    }
    
    public void saveButton() {
        // Implementazione da definire
    }
    
}
