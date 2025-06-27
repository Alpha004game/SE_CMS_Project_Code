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
        this.id=id;
        this.text=text;
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
    
    public void getId() {
        
    }
    
    public void updateNotificationStatus(int id, Object notification, String status) {
        
    }
    
}
