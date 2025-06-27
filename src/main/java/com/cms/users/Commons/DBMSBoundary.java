package com.cms.users.Commons;

import java.sql.DriverManager;
import java.util.LinkedList;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <<boundary>>
 * DBMSBoundary
 */
public class DBMSBoundary {
    
    // Constructor
    private final String DB_URL = "jdbc:mariadb://192.168.1.28:3306/CMS";
    private final String DB_USER = "ids";
    private final String DB_PASSWORD = "IngegneriaDelSoftware";
    public DBMSBoundary() {
        
    }

    private Connection getConnection()
    {
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return connection;
        }
        catch(SQLException e)
        {
            e.printStackTrace(); //DEBUG
            return null;
        }
        
    }

    public Connection debug()
    {
        return this.getConnection();
    }
    
    // User Management Methods
    public boolean checkUsername(String username) {
        return false;
    }
    
    public boolean checkPassword(String password) {
        return false;
    }
    
    public LinkedList<String> getListaUsername() {
        return null;
    }
    
    public void registraCredenziali(String username, String password) {
        
    }
    
    public void getUsername(String email) {
        
    }
    
    public void updatePassword(String username, String password) {
        
    }
    
    public void sendNewInformation(Object utente, Object infos) {
        
    }
    
    public Object ottieniTutteLeNotifiche(Object linkedListedNotifica) {
        return null;
    }
    
    public void ottieniTutteLNotifiche(String username) {
        
    }
    
    public Object getNotificaId(Object notification) {
        return null;
    }
    
    public void updateNotificationStatus(Object notification, String status) {
        
    }
    
    public Object getConferenceAttivi() {
        return null;
    }
    
    public LinkedList<Object> getConferenze(String username) {
        return null;
    }
    
    public void creaConferenza() {
        
    }
    
    public Object getConferenceInfo(Object conferenza) {
        return null;
    }
    
    public Object getSettedId(Object linkedListUtente) {
        return null;
    }
    
    public LinkedList<Object> getRevisori(Object conferenza) {
        return null;
    }
    
    public void setRevisoriArticolo(Object articolo, Object idRevisore) {
        
    }
    
    public LinkedList<Object> getListaSubmissions(Object conferenza) {
        return null;
    }
    
    public void rimuoviConferenza(Object conferenza, Object idRevisore) {
        
    }
    
    public LinkedList<Object> getListaSubmissioni(Object conferenza) {
        return null;
    }
    
    public Object getInfoArticolo(Object articolo) {
        return null;
    }
    
    public Object getKeywordsList(Object conferenza) {
        return null;
    }
    
    public void setKeywordsList(Object utente, Object skills) {
        
    }
    
    public LinkedList<Object> getListaArticolo(Object conferenza) {
        return null;
    }
    
    public void newConflitto(Object conferenza, Object utente, Object articolo) {
        
    }
    
    public void newInteresse(Object conferenza, Object utente, Object articolo) {
        
    }
    
    public LinkedList<Object> getListaArticolo(Object conferenza, Object utente) {
        return null;
    }
    
    public Object getConferenza(Object conferenza) {
        return null;
    }
    
    public Object getReviewStatus(Object articolo, Object utente) {
        return null;
    }
    
    public Object getInfoReview(Object articolo) {
        return null;
    }
    
    public Object getArticolo(Object articolo) {
        return null;
    }
    
    public Object getInfoSottorevisione(Object articolo) {
        return null;
    }
    
    public void accettaRevisione(Object articolo, Object utente) {
        
    }
    
    public Object ottieniInfoSottorevisore(Object articolo, Object idRevisore) {
        return null;
    }
    
    public Object rinunciaRevisore(Object utente) {
        return null;
    }
    
    public void getClearInformation(Object conferenza) {
        
    }
    
    public void getInfoRevisioneAssegnato(Object sottorevisore, Object articolo) {
        
    }
    
    // Additional methods from UML diagram
    public void rinunciaSottoRevisioneArticolo(Object utente, Object articolo) {
        
    }
    
    public LinkedList<Object> getListaSottomissioni(Object utente, Object conferenza) {
        return null;
    }
    
    public void setSottomissione(Object articolo, Object data, String status) {
        
    }
    
    public void modificaSottomissione(Object articolo, Object datiArticolo, Object data, String status) {
        
    }
    
    public void deleteSubmission(Object articolo) {
        
    }
    
    public Object getRevisionStatus(Object conferenza) {
        return null;
    }
    
    public LinkedList<Object> getRevisioni(Object articolo) {
        return null;
    }
    
    public LinkedList<Object> getAcceptedSubmission(Object conferenza) {
        return null;
    }
    
    public Object getArticoloFile(Object articolo) {
        return null;
    }
    
    public LinkedList<Object> getArticleFile(Object conferenza) {
        return null;
    }
    
    public LinkedList<Object> getArticleFileNameList(Object utente) {
        return null;
    }
    
    public LinkedList<Object> getListaAutoriConferenza(Object conferenza) {
        return null;
    }
    
    public LinkedList<Object> getListaAutoriVersioneFinaleMancante(Object conferenza) {
        return null;
    }
    
    public LinkedList<Object> getListaChairSenzaRevisori(Object conferenza) {
        return null;
    }
    
    public LinkedList<Object> getAutoriVersioneFinalePassata(Object conferenza) {
        return null;
    }
    
    public int getNumeroRevisori(Object conferenza) {
        return 0;
    }
    
    public LinkedList<Object> getChairConferenza(Object utente) {
        return null;
    }
    
    public Object getEditore(Object conferenza) {
        return null;
    }
    
    public int getNumeroArticoliNotificazione(Object utente) {
        return 0;
    }
    
    public LinkedList<Object> getConferenzaNotAttivi() {
        return null;
    }
    
    public void insertNotificazione(Object record) {
        
    }
    
    public LinkedList<Object> getListaRevisori() {
        return null;
    }
    
    public LinkedList<String> getInfonotificazioniRevisioriFinaleMancare(Object conferenza) {
        return null;
    }
    
    public LinkedList<Object> getListaAutoriArticolo(Object articolo) {
        return null;
    }
    
    public void automaticEmailSubmission(Object articolo) {
        
    }
    
    public LinkedList<Object> getListaArticoliSottoRevisioni(Object sottoRevisore, Object conferenza, Object articolo) {
        return null;
    }
    
    public void setSottomissioneArticoloTitolo(String abstractText, String anno, Object dataInizio, Object dataFine, Object dataSubmissioni, Object dataVersioneFinale, Object dataPubblicazione, Object luogo, Object temi, Object conferenza, int numeroArticoliMassimo, int numeroArticoliPrevisti, boolean assegnazioneAutomatica) {
        
    }
    
    public LinkedList<Object> getKeywords(Object conferenza) {
        return null;
    }
    
}
