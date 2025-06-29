package com.cms.users.account.Control;

import com.cms.users.account.Interface.LoginScreen;
import com.cms.users.account.Interface.RegistrationScreen;
import com.cms.App;
import com.cms.users.Commons.ErrorScreen;
import com.cms.users.Commons.SuccessScreen;

/**
 * <<control>>
 * RegistrationControl
 */
public class RegistrationControl {
    
    // Attributi
    private String email;
    private String username;
    private String password;
    private String confermaPassword;

    private RegistrationScreen registrationScreen;
    
    // Metodi
    public void create() {
        this.registrationScreen= new RegistrationScreen(this);
        registrationScreen.create();
    }
    
    public boolean checkPassword() {
        this.password= registrationScreen.getPassword();
        this.confermaPassword= registrationScreen.getConfermaPassword();
        if (password.equals(confermaPassword))
        {
            return true;
        }
        else
        {
            new ErrorScreen("Password e conferma password non coincidono!").show();
            
            return false;
        }
    }

    public void registra()
    {
        this.username=registrationScreen.getUsername();
        this.email=registrationScreen.getEmail();
        System.out.println(App.dbms.getListaUsername());
        if(App.dbms.getListaUsername().contains(username))
        {
            new ErrorScreen("Username già esistente!").show();
            return;
        }
        else
        {
            if(this.checkPassword())
            {
                App.dbms.registraCredenziali(email, username, password);
                new SuccessScreen("Registrazione completata con successo!").show();
                registrationScreen.dispose();
                new LoginScreen().create();
            }
        }
        
    }
    
}
