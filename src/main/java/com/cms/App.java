package com.cms;

import com.cms.users.Commons.DBMSBoundary;
import com.cms.users.account.Interface.LoginScreen;
import com.cms.users.Entity.UtenteE;

/**
 * Hello world!
 *
 */
public class App 
{
    public final static DBMSBoundary dbms=new DBMSBoundary();
    public static UtenteE utenteAccesso;
    public static void main( String[] args )
    {
        System.out.println( "Hello gays!" );
        LoginScreen login=new LoginScreen();
        
        login.create();
    }
}
