package com.cms.users.account.Control;

import com.cms.users.account.Interface.UserMenu;
import com.cms.App;

/**
 * <<control>>
 * HeaderControl
 */
public class HeaderControl {
    
    private UserMenu userMenu;
    
    // Metodi
    public void create() {
        // Inizializza il controllo dell'header
        this.userMenu = null;
    }
    
    public void userSelectMenu() {
        // Crea il menu utente con i dati dell'utente attualmente loggato
        if (App.utenteAccesso != null) {
            userMenu = new UserMenu(App.utenteAccesso.getUsername(), App.utenteAccesso.getEmail());
        } else {
            userMenu = new UserMenu("Guest", "guest@example.com");
        }
        userMenu.create();
    }
    
    public UserMenu getUserMenu() {
        return userMenu;
    }
    
    public void logout() {
        // Delegato al LogoutControl come da sequence diagram
        LogoutControl logoutControl = new LogoutControl();
        logoutControl.logout();
    }
    
    public void editUser() {
        // Implementazione da definire per la gestione account
    }
    
}
