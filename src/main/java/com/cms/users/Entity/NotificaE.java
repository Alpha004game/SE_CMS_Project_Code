package com.cms.users.Entity;

/**
 * <<entity>>
 * NotificaE
 */
public class NotificaE {
    
    // Attributes
    private int id;
    private int idConferenza;
    private int idUtente;
    private String text;
    private int tipo;
    private String dettagli;
    private String status;
    
    // Constructor
    public NotificaE(int id, int idConferenza, int idUtente, String text, int tipo, String dettagli, String esito) {
        this.id = id;
        this.idConferenza = idConferenza;
        this.idUtente = idUtente;
        this.text = text;
        this.tipo = tipo;
        this.dettagli = dettagli;
        
        switch(esito)
        {
            case "1":
                this.status="accettato";
                break;
            case "2":
                this.status="rifiutato";
                break;
            case "3":
                this.status="presaVisione";
                break;
            default:
                this.status=null;
                break;
        }
    }
    
    // Methods
    public void create() {
        
    }
    
    public void updateNotificationStatus(int id, Object notification, String status) {
        
    }
    
    // Getter methods
    public int getId() {
        return id;
    }
    
    public int getIdConferenza() {
        return idConferenza;
    }
    
    public int getIdUtente() {
        return idUtente;
    }
    
    public String getText() {
        return text;
    }
    
    public int getTipo() {
        return tipo;
    }
    
    public String getDettagli() {
        return dettagli;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
}
