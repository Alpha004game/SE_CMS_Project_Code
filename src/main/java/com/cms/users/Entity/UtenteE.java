package com.cms.users.Entity;

/**
 * <<entity>>
 * UtenteE
 */
public class UtenteE {
    
    // Attributes
    private int id;
    private String username;
    private String email;
    
    // Constructor
    public UtenteE(int id, String username, String email) {
        this.id=id;
        this.username=username;
        this.email=email;
    }
    
    // Methods
    public void create() {
        
    }
    
    public void destroy() {
        
    }
    
    public void getUserInfo() {
        
    }
    
    public void setNewInformation(Object infos) {
        
    }
    
    public void setUserInfo(Object infos) {
        
    }
    
    public Object getUserInfoDetails() {
        return null;
    }
    
    public int getId() {
        return this.id;
    }
    
    // Getter methods
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public int getIdUser() {
        return id;
    }
}
