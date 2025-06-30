package com.cms.users.Commons;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.mail.*;

/**
 * <<control>>
 * UtilsControl
 */
public class UtilsControl {
    
    // Attributes
    private String applicationName;
    private String version;
    private Date lastUpdate;
    
    // Constructor
    public UtilsControl() {
        
    }

    public static boolean sendMail(String to, String subject, String messageText)
    {
        try
        {
            String email="cagl.cmsids@gmail.com";
            String password="IngegneriaDelSoftware0=";

            Properties p = new Properties();
            p.put("mail.smtp.auth", "true");
            p.put("mail.smtp.starttls.enable", "true");
            p.put("mail.smtp.starttls.required", "true");
            p.put("mail.smtp.host", "smtp.gmail.com");
            p.put("mail.smtp.port", "587");
            p.put("mail.smtp.ssl.protocols", "TLSv1.2");
            p.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            p.put("mail.smtp.ssl.checkserveridentity", "true");
            
            // Abilita il debug per diagnosticare problemi (rimuovi in produzione)
            p.put("mail.debug", "true");

            Session s = Session.getInstance(p, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email, password);
                }
            });

            Message msg = new MimeMessage(s);
            msg.setFrom(new InternetAddress(email));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setText(messageText);

            Transport.send(msg);
            System.out.println("Email inviata con successo.");
            return true;
        }
        catch(Exception e)
        {
            System.err.println("Errore durante l'invio dell'email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
    }
    
    /**
     * Metodo alternativo per l'invio email usando SSL sulla porta 465
     * Usa questo metodo se il precedente continua a dare problemi TLS
     */
    public static boolean sendMailSSL(String to, String subject, String messageText)
    {
        try
        {
            String email="cagl.cmsids@gmail.com";
            String password="IngegneriaDelSoftware0=";

            Properties p = new Properties();
            p.put("mail.smtp.auth", "true");
            p.put("mail.smtp.socketFactory.port", "465");
            p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            p.put("mail.smtp.host", "smtp.gmail.com");
            p.put("mail.smtp.port", "465");
            p.put("mail.smtp.ssl.protocols", "TLSv1.2");
            p.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            
            // Abilita il debug per diagnosticare problemi (rimuovi in produzione)
            p.put("mail.debug", "true");

            Session s = Session.getInstance(p, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email, password);
                }
            });

            Message msg = new MimeMessage(s);
            msg.setFrom(new InternetAddress(email));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setText(messageText);

            Transport.send(msg);
            System.out.println("Email inviata con successo tramite SSL.");
            return true;
        }
        catch(Exception e)
        {
            System.err.println("Errore durante l'invio dell'email tramite SSL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Test invio email con STARTTLS ===");
        boolean risultato1 = UtilsControl.sendMail("francesco.patti.04pa@gmail.com", "Prova email App STARTTLS", "Questa è una prova con STARTTLS");
        System.out.println("Risultato STARTTLS: " + risultato1);
        
        if (!risultato1) {
            System.out.println("\n=== Test invio email con SSL ===");
            boolean risultato2 = UtilsControl.sendMailSSL("francesco.patti.04pa@gmail.com", "Prova email App SSL", "Questa è una prova con SSL");
            System.out.println("Risultato SSL: " + risultato2);
        }
    }

    
    // Methods
    public String formatDate(Date date) {
        return null;
    }
    
    public boolean validateEmail(String email) {
        return false;
    }
    
    public boolean validatePassword(String password) {
        return false;
    }
    
    public String generateRandomCode() {
        return null;
    }
    
    public String encryptPassword(String password) {
        return null;
    }
    
    public boolean verifyPassword(String inputPassword, String storedPassword) {
        return false;
    }
    
    public void sendEmail(String recipient, String subject, String body) {
        
    }
    
    public String sanitizeInput(String input) {
        return null;
    }
    
    public boolean isValidInput(String input) {
        return false;
    }
    
    public void logActivity(String activity) {
        
    }
    
    public List<String> getSystemMessages() {
        return null;
    }
    
    public void clearCache() {
        
    }
    
    public String getApplicationVersion() {
        return null;
    }
    
}
