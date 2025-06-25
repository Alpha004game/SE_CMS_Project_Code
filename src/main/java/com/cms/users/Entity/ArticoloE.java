package com.cms.users.Entity;

import java.util.LinkedList;

/**
 * <<entity>>
 * ArticoloE
 */
public class ArticoloE {
    
    // Attributes
    private int id;
    private String titolo;
    private String abstractText; // Note: 'abstract' is a reserved word, using 'abstractText'
    private boolean conflitti;
    private LinkedList<String> keywords;
    private LinkedList<String> coAutori;
    private boolean dichiarazioneOriginalita;
    private String fileName;
    private Object allegato; // Blob equivalent
    
    // Constructor
    public ArticoloE() {
        
    }
    
    // Methods
    public void create() {
        
    }
    
    public void getId() {
        
    }
    
}
