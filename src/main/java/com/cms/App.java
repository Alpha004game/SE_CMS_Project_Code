package com.cms;

import com.cms.users.Commons.DBMSBoundary;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello gays!" );
        DBMSBoundary dbms = new DBMSBoundary();
        System.out.println(dbms.debug());
    }
}
