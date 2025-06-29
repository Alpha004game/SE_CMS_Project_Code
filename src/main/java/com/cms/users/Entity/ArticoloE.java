package com.cms.users.Entity;

import java.util.LinkedList;
import java.time.LocalDate;

/**
 * <<entity>>
 * ArticoloE
 */
public class ArticoloE {
    
    // Attributes
    private int id;
    private String titolo;
    private String abstractText; // Note: 'abstract' is a reserved word, using 'abstractText'
    private Object fileArticolo; // Blob per il file dell'articolo
    private Object allegato; // Blob per allegati aggiuntivi
    private String stato; // Stato della sottomissione (Inviato, Accettato, Rifiutato, etc.)
    private int idConferenza; // ID della conferenza a cui è sottomesso
    private LocalDate ultimaModifica; // Data dell'ultima modifica
    
    // Attributes aggiuntivi (potrebbero essere in altre tabelle)
    private boolean conflitti;
    private LinkedList<String> keywords;
    private LinkedList<String> coAutori;
    private boolean dichiarazioneOriginalita;
    
    // Constructor
    public ArticoloE() {
        this.keywords = new LinkedList<>();
        this.coAutori = new LinkedList<>();
        this.stato = "Inviato"; // Default stato
    }
    
    // Constructor with main parameters (based on DB table structure)
    public ArticoloE(int id, String titolo, String abstractText, Object fileArticolo, 
                     Object allegato, String stato, int idConferenza, LocalDate ultimaModifica) {
        this.id = id;
        this.titolo = titolo;
        this.abstractText = abstractText;
        this.fileArticolo = fileArticolo;
        this.allegato = allegato;
        this.stato = stato;
        this.idConferenza = idConferenza;
        this.ultimaModifica = ultimaModifica;
        this.keywords = new LinkedList<>();
        this.coAutori = new LinkedList<>();
    }
    
    // Constructor with additional parameters (backwards compatibility)
    public ArticoloE(int id, String titolo, String abstractText, boolean conflitti, 
                     LinkedList<String> keywords, LinkedList<String> coAutori, 
                     boolean dichiarazioneOriginalita, Object allegato) {
        this.id = id;
        this.titolo = titolo;
        this.abstractText = abstractText;
        this.conflitti = conflitti;
        this.keywords = keywords != null ? keywords : new LinkedList<>();
        this.coAutori = coAutori != null ? coAutori : new LinkedList<>();
        this.dichiarazioneOriginalita = dichiarazioneOriginalita;
        this.allegato = allegato;
        this.stato = "Inviato"; // Default stato
    }
    
    // Methods
    public void create() {
        // Implementazione da definire
    }
    
    @Override
    public String toString() {
        return "ArticoloE{" +
                "id=" + id +
                ", titolo='" + titolo + '\'' +
                ", abstractText='" + abstractText + '\'' +
                ", stato='" + stato + '\'' +
                ", idConferenza=" + idConferenza +
                ", ultimaModifica=" + ultimaModifica +
                ", conflitti=" + conflitti +
                ", keywords=" + keywords +
                ", coAutori=" + coAutori +
                ", dichiarazioneOriginalita=" + dichiarazioneOriginalita +
                '}';
    }
    
    // Getter methods
    public int getId() {
        return id;
    }
    
    public String getTitolo() {
        return titolo;
    }
    
    public String getAbstractText() {
        return abstractText;
    }
    
    public Object getFileArticolo() {
        return fileArticolo;
    }
    
    public Object getAllegato() {
        return allegato;
    }
    
    public String getStato() {
        return stato;
    }
    
    public int getIdConferenza() {
        return idConferenza;
    }
    
    public LocalDate getUltimaModifica() {
        return ultimaModifica;
    }
    
    public boolean isConflitti() {
        return conflitti;
    }
    
    public LinkedList<String> getKeywords() {
        return keywords;
    }
    
    public LinkedList<String> getCoAutori() {
        return coAutori;
    }
    
    public boolean isDichiarazioneOriginalita() {
        return dichiarazioneOriginalita;
    }
    
    // Setter methods
    public void setId(int id) {
        this.id = id;
    }
    
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    
    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }
    
    public void setFileArticolo(Object fileArticolo) {
        this.fileArticolo = fileArticolo;
    }
    
    public void setAllegato(Object allegato) {
        this.allegato = allegato;
    }
    
    public void setStato(String stato) {
        this.stato = stato;
    }
    
    public void setIdConferenza(int idConferenza) {
        this.idConferenza = idConferenza;
    }
    
    public void setUltimaModifica(LocalDate ultimaModifica) {
        this.ultimaModifica = ultimaModifica;
    }
    
    public void setConflitti(boolean conflitti) {
        this.conflitti = conflitti;
    }
    
    public void setKeywords(LinkedList<String> keywords) {
        this.keywords = keywords;
    }
    
    public void setCoAutori(LinkedList<String> coAutori) {
        this.coAutori = coAutori;
    }
    
    public void setDichiarazioneOriginalita(boolean dichiarazioneOriginalita) {
        this.dichiarazioneOriginalita = dichiarazioneOriginalita;
    }
    
}
