package com.cms.users.submissions.Interface;

/**
 * <<boundary>>
 * ModifySubmissionScreen
 */
public class ModifySubmissionScreen {
    
    // Attributi
    private String titolo; // TextField
    private String abstractText; // TextField
    private String keywords; // Checkbox
    private String coAutori; // TextField
    private String dichiarazioneOriginalita; // Checkbox
    private String file; // FileChooser
    private String allegato; // FileChooser
    
    // Metodi
    public void create() {
        // Implementazione da definire
    }
    
    public void compilaCampi(String titolo, String abstractText, String conflitti, String keywords, String coautori) {
        // Implementazione da definire
    }
    
    public void caricaFile(String file) {
        // Implementazione da definire
    }
    
    public String modificaButton() {
        // Implementazione da definire
        return null;
    }
    
    public String getDati() {
        // Implementazione da definire
        return null;
    }
    
    public void caricatoAll() {
        // Implementazione da definire
    }
    
    public void caricaAllegato(String file) {
        // Implementazione da definire
    }
    
}
