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
        
    }
    
    // Methods
    public void create() {
        
    }
    
    public void getName() {
        
    }
    
    public void getId() {
        
    }
    
}
