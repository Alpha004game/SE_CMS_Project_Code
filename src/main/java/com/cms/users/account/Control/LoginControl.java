package com.cms.users.account.Control;

import com.cms.App;
import com.cms.users.Commons.DBMSBoundary;
import com.cms.users.Commons.ErrorScreen;
import com.cms.users.Commons.HomeScreen;
import com.cms.users.account.Interface.LoginScreen;
import com.cms.users.Entity.UtenteE;

/**
 * <<control>>
 * LoginControl
 */
public class LoginControl {
    
    // Attributi
    private String username;
    private String password;
    
    // Metodi
    public void LoginControl() {
        // Implementazione da definire
    }

    public boolean accedi(LoginScreen loginScreen)
    {
        this.username=loginScreen.getUsername();
        this.password=loginScreen.getPassword();

        DBMSBoundary dbms=App.dbms;
        if(dbms.checkUsername(username))
        {
            UtenteE u=dbms.checkPassword(username, password);
            if(u!=null)
            {
                App.utenteAccesso=u;
                loginScreen.dispose();
                new HomeScreen(u.getId()).show();

                return true; 
            }
            else
            {
                new ErrorScreen("Password errata!").show();
                return false; // Password incorrect
            }
        }
        new ErrorScreen("Username Non esistente!").show();
        return false;
    }
    
}
