package com.cms.users.Entity;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * <<entity>>
 * ConferenzaE
 */
public class ConferenzaE {
    
    // Attributes
    private int id;
    private String titolo;
    private int annoEdizione;
    private String abstractText; // Note: 'abstract' is a reserved word, using 'abstractText'
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private LocalDate deadlineSottomissione;
    private LocalDate deadlineRitiro;
    private LocalDate deadlineRevisioni;
    private LocalDate deadlineVersioneFinale;
    private LocalDate deadlinePubblicazione;
    private String luogo;
    private int numeroRevisoriPerArticolo;
    private int numeroArticoliPrevisti;
    private boolean assegnazioneAutomatica;
    private ArrayList<String> keywords;
    
    // Constructor
    public ConferenzaE() {
        this.keywords = new ArrayList<>();
    }
    
    // Constructor with all parameters
    public ConferenzaE(int id, String titolo, int annoEdizione, String abstractText, LocalDate dataInizio, 
                      LocalDate dataFine, LocalDate deadlineSottomissione, LocalDate deadlineRitiro, 
                      LocalDate deadlineRevisioni, LocalDate deadlineVersioneFinale, LocalDate deadlinePubblicazione, 
                      String luogo, int numeroRevisoriPerArticolo, int numeroArticoliPrevisti, 
                      boolean assegnazioneAutomatica, ArrayList<String> keywords) {
        this.id = id;
        this.titolo = titolo;
        this.annoEdizione = annoEdizione;
        this.abstractText = abstractText;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.deadlineSottomissione = deadlineSottomissione;
        this.deadlineRitiro = deadlineRitiro;
        this.deadlineRevisioni = deadlineRevisioni;
        this.deadlineVersioneFinale = deadlineVersioneFinale;
        this.deadlinePubblicazione = deadlinePubblicazione;
        this.luogo = luogo;
        this.numeroRevisoriPerArticolo = numeroRevisoriPerArticolo;
        this.numeroArticoliPrevisti = numeroArticoliPrevisti;
        this.assegnazioneAutomatica = assegnazioneAutomatica;
        this.keywords = keywords != null ? keywords : new ArrayList<>();
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getTitolo() {
        return titolo;
    }
    
    public int getAnnoEdizione() {
        return annoEdizione;
    }
    
    public String getAbstractText() {
        return abstractText;
    }
    
    public LocalDate getDataInizio() {
        return dataInizio;
    }
    
    public LocalDate getDataFine() {
        return dataFine;
    }
    
    public LocalDate getDeadlineSottomissione() {
        return deadlineSottomissione;
    }
    
    public LocalDate getDeadlineRitiro() {
        return deadlineRitiro;
    }
    
    public LocalDate getDeadlineRevisioni() {
        return deadlineRevisioni;
    }
    
    public LocalDate getDeadlineVersioneFinale() {
        return deadlineVersioneFinale;
    }
    
    public LocalDate getDeadlinePubblicazione() {
        return deadlinePubblicazione;
    }
    
    public String getLuogo() {
        return luogo;
    }
    
    public int getNumeroRevisoriPerArticolo() {
        return numeroRevisoriPerArticolo;
    }
    
    public int getNumeroArticoliPrevisti() {
        return numeroArticoliPrevisti;
    }
    
    public boolean isAssegnazioneAutomatica() {
        return assegnazioneAutomatica;
    }
    
    public ArrayList<String> getKeywords() {
        return keywords;
    }
    
    // Methods
    public void create() {
        
    }
    
    public String getName() {
        return this.titolo;
    }
    
}
