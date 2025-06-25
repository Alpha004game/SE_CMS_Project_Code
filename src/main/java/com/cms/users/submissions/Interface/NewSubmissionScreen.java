package com.cms.users.submissions.Interface;

/**
 * <<boundary>>
 * NewSubmissionScreen
 */
public class NewSubmissionScreen {
    
    // Attributi
    private String titolo; // TextField
    private String abstractText; // TextField
    private String conflitti; // Checkbox
    private String keywords; // Checkbox
    private String coAutori; // TextField
    private String dichiarazioneOriginalita; // Checkbox
    private String file; // FileChooser
    private String allegato; // FileChooser
    
    // Metodi
    public void create() {
        // Implementazione da definire
    }
    
    public void compilaCampi(String titolo, String abstractText, String keywords, String coAutori, String dichiarazioneOriginalita) {
        // Implementazione da definire
    }
    
    public void caricaFile(String file) {
        // Implementazione da definire
    }
    
    public String caricaSottomissioneButton() {
        // Implementazione da definire
        return null;
    }
    
    public String getDati() {
        // Implementazione da definire
        return null;
    }
    
    public void caricaAllegato(String file) {
        // Implementazione da definire
    }
    
}
