package com.cms.users.Commons;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

import com.cms.users.Entity.ArticoloE;
import com.cms.users.Entity.ConferenzaE;
import com.cms.users.Entity.NotificaE;
import com.cms.users.Entity.UtenteE;


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
            //e.printStackTrace(); //DEBUG
            return null;
        }
        
    }
    private static String sha512(String input) {
        try {
            // Ottieni un'istanza dell'algoritmo SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // Calcola l'hash
            byte[] hashBytes = md.digest(input.getBytes());

            // Converti i byte in una stringa esadecimale
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0'); // Aggiunge uno 0 iniziale se necessario
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    
    // User Management Methods
    public boolean checkUsername(String username) { //return true if exists else return false
        try
        {
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("SELECT EXISTS (SELECT 1 FROM utenti WHERE username = ? ) AS esiste");
            stmt.setString(1, username);
            ResultSet ris=stmt.executeQuery();
            ris.next();
            int esito=ris.getInt("esiste");
            ris.close();
            stmt.close();
            con.close();
            return esito==1;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
    public UtenteE checkPassword(String username, String password) { //true if success, false if password not equal
        try
        {
            UtenteE u=null;
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("SELECT U.id, U.username, U.email FROM utenti as U WHERE U.username= ? AND U.password= ?");
            stmt.setString(1, username);
            stmt.setString(2, sha512(password));
            ResultSet ris=stmt.executeQuery();
            if(ris.next())
            {
                u=new UtenteE(ris.getInt("id"), ris.getString("username"), ris.getString("email"));
            }
            ris.close();
            stmt.close();
            con.close();
            return u;
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    public LinkedList<String> getListaUsername() {
        try
        {
            LinkedList<String> lista=new LinkedList<>();
            Connection conn=getConnection();
            PreparedStatement stmt=conn.prepareStatement("SELECT U.username FROM utenti as U");
            ResultSet ris=stmt.executeQuery();
            while(ris.next())
            {
                lista.add(ris.getString("username"));
            }
            ris.close();
            stmt.close();
            conn.close();
            return lista;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
    }

    
    
    public void registraCredenziali(String email, String username, String password) {
        try
        {
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("INSERT INTO utenti VALUES(NULL, ?, ?, ?, CURRENT_DATE)");
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, sha512(password));
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Utente inserito con successo."); //Debug print
            }
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        
    }

    public LinkedList<UtenteE> getUserList() //D
    {
        return null;
    }

    public UtenteE getUser(int idUtente) //D
    {
        try
        {
            UtenteE utente=null;
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("SELECT U.id, U.username, U.email FROM utenti U WHERE U.id= ?");
            stmt.setInt(1, idUtente);
            ResultSet ris=stmt.executeQuery();
            if(ris.next())
            {
                utente=new UtenteE(ris.getInt("id"), ris.getString("username"), ris.getString("email"));
            }
            ris.close();
            stmt.close();
            con.close();
            return utente;

        }
        catch(Exception e)
        {
            return null;
        }
    }

    public UtenteE getUserInfo(int conferenceId) //D
    {
        return null;
    }
    
    public String getUsername(String email) {

        try
        {
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("SELECT U.username AS username FROM utenti AS U WHERE U.email= ?");
            stmt.setString(1, email);

            ResultSet ris=stmt.executeQuery();
            ris.next();
            String username=ris.getString("username");
            ris.close();
            stmt.close();
            con.close();
            return username;
            
        }
        catch(Exception e)
        {
            return null;
        }
        
    }
    
    public void updatePassword(String username, String password) {

        try
        {
            Connection conn=getConnection();
            PreparedStatement stmt=conn.prepareStatement("UPDATE utenti SET password= ? WHERE username= ?");
            stmt.setString(1, sha512(password));
            stmt.setString(2, username);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Password aggiornata con successo.");
            }
            stmt.close();
            conn.close();    
        }
        catch(Exception e)
        {
            e.printStackTrace(); //DEBUG
        }
        
    }
    
    public void sendNewInformation(int idUtente, Object infos) {
        
    }

    public void sendNewSkillInformation(int idUtente, int idConferenza, Object Skills)
    {

    }

    //RIVEDERE
    public LinkedList<NotificaE> ottieniNotificheAttive(String username) {
        try
        {
            LinkedList<NotificaE> notifiche=new LinkedList<>();
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("SELECT N.id, N.idConferenza, N.idUtente, N.testo, N.tipo, N.dettagli, N.esito FROM notifiche AS N, utenti AS U WHERE N.idUtente=U.id AND U.username= ? AND N.esito IS NULL");
            stmt.setString(1, username);
            ResultSet ris=stmt.executeQuery();
            while(ris.next())
            {
                notifiche.add(new NotificaE(
                    ris.getInt("id"), 
                    ris.getInt("idConferenza"), 
                    ris.getInt("idUtente"), 
                    ris.getString("testo"), 
                    ris.getInt("tipo"), 
                    ris.getString("dettagli"), 
                    ris.getString("esito")
                ));
            }
            ris.close();
            stmt.close();
            con.close();
            return notifiche;
        }
        catch(Exception e)
        {
            return null;
        }
        
    }
    //RIVEDERE
    public LinkedList<NotificaE> ottieniTutteLeNotifiche(String username) { //RIVEDERE
        try
        {
            LinkedList<NotificaE> notifiche=new LinkedList<>();
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("SELECT N.id, N.idConferenza, N.idUtente, N.testo, N.tipo, N.dettagli, N.esito FROM notifiche AS N, utenti AS U WHERE N.idUtente=U.id AND U.username= ?");
            stmt.setString(1, username);
            ResultSet ris=stmt.executeQuery();
            while(ris.next())
            {
                notifiche.add(new NotificaE(
                    ris.getInt("id"), 
                    ris.getInt("idConferenza"), 
                    ris.getInt("idUtente"), 
                    ris.getString("testo"), 
                    ris.getInt("tipo"), 
                    ris.getString("dettagli"), 
                    ris.getString("esito")
                ));
            }
            ris.close();
            stmt.close();
            con.close();
            return notifiche;
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    
    
    public NotificaE getNotifica(int idNotifica) { //DA CONTOLLARE
        try
        {
            NotificaE notifica=null;
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("SELECT N.id, N.idConferenza, N.idUtente, N.testo, N.tipo, N.dettagli, N.esito FROM notifiche AS N WHERE N.id= ?");
            stmt.setInt(1, idNotifica);
            ResultSet ris=stmt.executeQuery();
            if(ris.next())
            {
                notifica=new NotificaE(
                    ris.getInt("id"), 
                    ris.getInt("idConferenza"), 
                    ris.getInt("idUtente"), 
                    ris.getString("testo"), 
                    ris.getInt("tipo"), 
                    ris.getString("dettagli"), 
                    ris.getString("esito"));
            }
            ris.close();
            stmt.close();
            con.close();
            return notifica;
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    public void updateNotificationStatus(int idNotification, String status) { //NOTA: LA LOGICA DELLE CONSEGUENZE DOVRA' ESSERE SVILUPPATO nelle funzioni invocanti del metodo
        try
        {
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("UPDATE notifiche SET status= ? WHERE id= ?");
            stmt.setString(1, status);
            stmt.setInt(2, idNotification);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("stato aggiornato con successo."); //DEBUG
            }
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }

    public int getNumberActiveNotification(int idUtente)
    {
        return -1;
    }

    public LinkedList<ConferenzaE> getConferenzeNonAttive()
    {
        return null;
    }
    
    public LinkedList<ConferenzaE> getConferenzeAttive() {
        try
        {
            LinkedList<ConferenzaE> conferenze = new LinkedList<>();
            Connection con = getConnection();
            if (con == null) {
                System.err.println("Errore: impossibile stabilire connessione al database");
                return null;
            }
            
            String sql = "SELECT DISTINCT c.id, c.titolo, c.abstract, c.edizione, c.dataInizio, c.dataFine, " +
                        "c.deadlineSottomissione, c.deadlineRitiro, c.deadlineRevisioni, c.deadlineVersioneFinale, " +
                        "c.deadlineVersionePubblicazione, c.luogo, c.numRevisoriPerArticolo, c.numeroArticoliPrevisti, " +
                        "c.tassoAccettazione " +
                        "FROM conferenze c "+
                        "WHERE c.dataFine >= CURRENT_DATE";
            
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                // Ottieni le keywords per questa conferenza
                ArrayList<String> keywordsConferenza = getKeywordsConferenza(con, rs.getInt("id"));
                
                // Crea la conferenza usando il costruttore con tutti i parametri
                ConferenzaE conferenza = new ConferenzaE(
                    rs.getInt("id"),
                    rs.getString("titolo"),
                    rs.getInt("edizione"),
                    rs.getString("abstract"),
                    rs.getDate("dataInizio").toLocalDate(),
                    rs.getDate("dataFine").toLocalDate(),
                    rs.getDate("deadlineSottomissione").toLocalDate(),
                    rs.getDate("deadlineRitiro").toLocalDate(),
                    rs.getDate("deadlineRevisioni").toLocalDate(),
                    rs.getDate("deadlineVersioneFinale").toLocalDate(),
                    rs.getDate("deadlineVersionePubblicazione").toLocalDate(),
                    rs.getString("luogo"),
                    rs.getInt("numRevisoriPerArticolo"),
                    rs.getInt("numeroArticoliPrevisti"),
                    false, // assegnazioneAutomatica (non presente nella tabella, default false)
                    keywordsConferenza
                );
                
                conferenze.add(conferenza);
            }
            
            rs.close();
            stmt.close();
            con.close();
            
            return conferenze;
            
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    public LinkedList<ConferenzaE> getConferenze() {
        try {
            LinkedList<ConferenzaE> conferenze = new LinkedList<>();
            Connection con = getConnection();
            if (con == null) {
                System.err.println("Errore: impossibile stabilire connessione al database");
                return null;
            }
            
            String sql = "SELECT DISTINCT c.id, c.titolo, c.abstract, c.edizione, c.dataInizio, c.dataFine, " +
                        "c.deadlineSottomissione, c.deadlineRitiro, c.deadlineRevisioni, c.deadlineVersioneFinale, " +
                        "c.deadlineVersionePubblicazione, c.luogo, c.numRevisoriPerArticolo, c.numeroArticoliPrevisti, " +
                        "c.tassoAccettazione " +
                        "FROM conferenze c ";
            
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                // Ottieni le keywords per questa conferenza
                ArrayList<String> keywordsConferenza = getKeywordsConferenza(con, rs.getInt("id"));
                
                // Crea la conferenza usando il costruttore con tutti i parametri
                ConferenzaE conferenza = new ConferenzaE(
                    rs.getInt("id"),
                    rs.getString("titolo"),
                    rs.getInt("edizione"),
                    rs.getString("abstract"),
                    rs.getDate("dataInizio").toLocalDate(),
                    rs.getDate("dataFine").toLocalDate(),
                    rs.getDate("deadlineSottomissione").toLocalDate(),
                    rs.getDate("deadlineRitiro").toLocalDate(),
                    rs.getDate("deadlineRevisioni").toLocalDate(),
                    rs.getDate("deadlineVersioneFinale").toLocalDate(),
                    rs.getDate("deadlineVersionePubblicazione").toLocalDate(),
                    rs.getString("luogo"),
                    rs.getInt("numRevisoriPerArticolo"),
                    rs.getInt("numeroArticoliPrevisti"),
                    false, // assegnazioneAutomatica (non presente nella tabella, default false)
                    keywordsConferenza
                );
                
                conferenze.add(conferenza);
            }
            
            rs.close();
            stmt.close();
            con.close();
            
            return conferenze;
            
        } catch (SQLException e) {
            System.err.println("Errore SQL durante il recupero delle conferenze: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Errore generico durante il recupero delle conferenze: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getKeywords(int idConferenza)
    {
        return this.getKeywordsConferenza(getConnection(), idConferenza);
    }
    
    private ArrayList<String> getKeywordsConferenza(Connection con, int idConferenza) {
        ArrayList<String> keywords = new ArrayList<>();
        try {
            String sql = "SELECT k.keyword " +
                        "FROM keywords k " +
                        "INNER JOIN keywordsConferenza kc ON k.id = kc.idKeyword " +
                        "WHERE kc.idConferenza = ?";
            
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idConferenza);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                keywords.add(rs.getString("keyword"));
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Errore nel recupero delle keywords: " + e.getMessage());
        }
        
        return keywords;
    }
    
    //VERIFICARE ANCHE CON SOTTOFUNZIONI
    public void setConferenza(String titolo, int annoEdizione, String abstractText, LocalDate dataInizio, LocalDate dataFine, LocalDate deadlineSottomissione, LocalDate deadlineRitiro, LocalDate deadlineRevisioni, LocalDate deadlineVersioneFinale, LocalDate deadlinePubblicazione, String luogo, int numeroRevisoriPerArticolo, int numeroArticoliPrevisti, int tassoAccettazione, String keywords)
    {
        try {
            Connection con = getConnection();
            if (con == null) {
                System.err.println("Errore: impossibile stabilire connessione al database");
                return;
            }
            
            // Query SQL basata sulla struttura della tabella conferenze
            String sql = "INSERT INTO conferenze (titolo, abstract, edizione, dataInizio, dataFine, " +
                        "deadlineSottomissione, deadlineRitiro, deadlineRevisioni, deadlineVersioneFinale, " +
                        "deadlineVersionePubblicazione, luogo, numRevisoriPerArticolo, numeroArticoliPrevisti, " +
                        "tassoAccettazione) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, titolo);
            stmt.setString(2, abstractText);
            stmt.setInt(3, annoEdizione);
            stmt.setDate(4, java.sql.Date.valueOf(dataInizio));
            stmt.setDate(5, java.sql.Date.valueOf(dataFine));
            stmt.setDate(6, java.sql.Date.valueOf(deadlineSottomissione));
            stmt.setDate(7, java.sql.Date.valueOf(deadlineRitiro));
            stmt.setDate(8, java.sql.Date.valueOf(deadlineRevisioni));
            stmt.setDate(9, java.sql.Date.valueOf(deadlineVersioneFinale));
            stmt.setDate(10, java.sql.Date.valueOf(deadlinePubblicazione));
            stmt.setString(11, luogo);
            stmt.setInt(12, numeroRevisoriPerArticolo);
            stmt.setInt(13, numeroArticoliPrevisti);
            stmt.setInt(14, tassoAccettazione);
            
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Conferenza creata con successo: " + titolo);
                
                // Se ci sono keywords, le gestiamo separatamente
                if (keywords != null && !keywords.trim().isEmpty()) {
                    int idConferenza = getLastInsertedConferenceId(con);
                    if (idConferenza > 0) {
                        inserisciKeywordsConferenza(con, idConferenza, keywords);
                    }
                }
            } else {
                System.err.println("Errore: nessuna riga inserita");
            }
            
            stmt.close();
            con.close();
            
        } catch (SQLException e) {
            System.err.println("Errore SQL durante la creazione della conferenza: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Errore generico durante la creazione della conferenza: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private int getLastInsertedConferenceId(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("SELECT LAST_INSERT_ID() as id");
        ResultSet rs = stmt.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("id");
        }
        rs.close();
        stmt.close();
        return id;
    }
    
    private void inserisciKeywordsConferenza(Connection con, int idConferenza, String keywords) {
        try {
            String[] keywordArray = keywords.split(",");
            
            for (String keyword : keywordArray) {
                keyword = keyword.trim();
                if (!keyword.isEmpty()) {
                    // Prima inserisci la keyword nella tabella keywords (se non esiste già)
                    int idKeyword = inserisciOOttieniKeyword(con, keyword);
                    
                    // Poi associa la keyword alla conferenza
                    if (idKeyword > 0) {
                        PreparedStatement stmt = con.prepareStatement(
                            "INSERT IGNORE INTO keywordsConferenza (idKeyword, idConferenza) VALUES (?, ?)"
                        );
                        stmt.setInt(1, idKeyword);
                        stmt.setInt(2, idConferenza);
                        stmt.executeUpdate();
                        stmt.close();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore nell'inserimento delle keywords: " + e.getMessage());
        }
    }
    
    private int inserisciOOttieniKeyword(Connection con, String keyword) throws SQLException {
        // Prima prova a inserire la keyword
        PreparedStatement insertStmt = con.prepareStatement(
            "INSERT IGNORE INTO keywords (keyword) VALUES (?)"
        );
        insertStmt.setString(1, keyword);
        insertStmt.executeUpdate();
        insertStmt.close();
        
        // Poi ottieni l'ID della keyword
        PreparedStatement selectStmt = con.prepareStatement(
            "SELECT id FROM keywords WHERE keyword = ?"
        );
        selectStmt.setString(1, keyword);
        ResultSet rs = selectStmt.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("id");
        }
        rs.close();
        selectStmt.close();
        return id;
    }

    public String getConferenceLog(int conferenceId)
    {
        return null;
    }
    
    public Object getConferenceInfo(int idConferenza) {
        try {
            Connection con = getConnection();
            
            
            // Query per ottenere tutte le informazioni di una conferenza specifica
            String sql = "SELECT id, titolo, abstract, edizione, dataInizio, dataFine, " +
                        "deadlineSottomissione, deadlineRitiro, deadlineRevisioni, deadlineVersioneFinale, " +
                        "deadlineVersionePubblicazione, luogo, numRevisoriPerArticolo, numeroArticoliPrevisti, " +
                        "tassoAccettazione " +
                        "FROM conferenze WHERE id = ?";
            
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idConferenza);
            ResultSet rs = stmt.executeQuery();
            
            ConferenzaE conferenza = null;
            if (rs.next()) {
                // Ottieni le keywords per questa conferenza
                ArrayList<String> keywordsConferenza = getKeywordsConferenza(con, idConferenza);
                
                // Crea la conferenza usando il costruttore con tutti i parametri
                conferenza = new ConferenzaE(
                    rs.getInt("id"),
                    rs.getString("titolo"),
                    rs.getInt("edizione"),
                    rs.getString("abstract"),
                    rs.getDate("dataInizio").toLocalDate(),
                    rs.getDate("dataFine").toLocalDate(),
                    rs.getDate("deadlineSottomissione").toLocalDate(),
                    rs.getDate("deadlineRitiro").toLocalDate(),
                    rs.getDate("deadlineRevisioni").toLocalDate(),
                    rs.getDate("deadlineVersioneFinale").toLocalDate(),
                    rs.getDate("deadlineVersionePubblicazione").toLocalDate(),
                    rs.getString("luogo"),
                    rs.getInt("numRevisoriPerArticolo"),
                    rs.getInt("numeroArticoliPrevisti"),
                    false, // assegnazioneAutomatica (non presente nella tabella, default false)
                    keywordsConferenza
                );
            }
            
            rs.close();
            stmt.close();
            con.close();
            
            return conferenza;
            
        } catch (SQLException e) {
            System.err.println("Errore SQL durante il recupero delle informazioni della conferenza: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Errore generico durante il recupero delle informazioni della conferenza: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public LinkedList<UtenteE> getRevisori(int idConferenza) {
        try
        {
            LinkedList<UtenteE> revisori=new LinkedList<>();
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("SELECT U.id, U.username, U.email FROM utenti AS U, ruoli AS R WHERE U.id=R.idUtente AND R.ruolo=2 AND R.idConferenza= ?");
            stmt.setInt(1, idConferenza);
            ResultSet ris=stmt.executeQuery();
            while(ris.next())
            {
                revisori.add(new UtenteE(ris.getInt("id"), ris.getString("username"), ris.getString("email")));
            }
            ris.close();
            stmt.close();
            con.close();
            return revisori;
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    public void setRevisoreArticolo(int idArticolo, int idRevisore) {
        
    }

    public void rimuoviRevisore(int idConferenza, int idRevisore)
    {
        
    }
    
    public LinkedList<ArticoloE> getListaSottomissioni(int idConferenza) {
        return null;
    }
    
    public Object getInfoArticolo(int idArticolo) {
        return null;
    }
    
    public Object getKeywordsList(int idConferenza) { //vedere se dublicato
        return null;
    }
    
    
    public LinkedList<ArticoloE> getListaArticoli(int idConferenza) {
        return null;
    }
    
    public void newConflitto(int idConferenza, int idUtente, int idArticolo) {
        
    }
    
    public void newInteresse(int idConferenza, int idUtente, int idArticolo) {
        
    }
    
    
    public Object getReviewStatus(int idArticolo, int idUtente) {
        return null;
    }
    
    public Object getInfoReview(int idArticolo, int idUtente) {
        return null;
    }

    public LinkedList<UtenteE> getInformazioniAutoriRevisioneFinalePassata(int idConferenza)
    {
        return null;
    }
    
    public ArticoloE getArticolo(int idArticolo) {
        return null;
    }

    public LinkedList<ArticoloE> getArticoliRevisioneFinalePassata(int idConferenza)
    {
        return null;
    }
    
    public Object getInfoSottorevisione(int idRevisore, int idArticolo) {
        return null;
    }
    
    public void accettaRevisione(int idArticolo, int idUtente) {
        
    }
    
    public UtenteE ottieniInfoSottorevisore(int idArticolo, int idRevisoreDelegante) {
        return null;
    }

    public void rinunciaArticolo(int idUtente, int idArticolo)
    {

    }
    
    public Object rinunciaRevisore(Object utente) {
        return null;
    }
    
    
    public void getInfoRevisoreAssegnatario(int idSottorevisore, int idArticolo) {
        
    }
    
    // Additional methods from UML diagram
    public void rinunciaSottoRevisioneArticolo(int idUtente, int idArticolo) {
        
    }
    
    public LinkedList<UtenteE> getListaSottomissioni(int idUtente, int idConferenza) {
        return null;
    }
    
    public void setSottomissione(Object articolo, Object data, String status) { //SPECIFICARE PARAMETRI
        
    }
    
    public void modificaSottomissione(int idArticolo, Object datiArticolo, Object data, String status) { //SPECIFICARE PARAMETRI
        
    }
    
    public void deleteSubmission(int idArticolo) {
        
    }
    
    public Object getRevisionsStatus(int idArticolo) {
        return null;
    }
    
    public LinkedList<Object> getRevisioni(int idArticolo) {
        return null;
    }
    
    public LinkedList<Object> getAcceptedArticles(int idConferenza) {
        return null;
    }
    
    public LinkedList<Object> getArticleFile(int idArticle) {
        return null;
    }

    public Object getAllArticleFile(int idConferenza)
    {
        return null;
    }

    public LinkedList<UtenteE> getArticleAuthors(int idArticle)
    {
        return null;
    }
    
    public LinkedList<UtenteE> getListaAutori(int idConferenza) {
        return null;
    }
    
    public LinkedList<UtenteE> getListaAutoriVersioneFinaleMancante(int idConferenza) {
        return null;
    }

    public LinkedList<UtenteE> getListaAutoriVersioneFinaleDaSistemare(int idConferenza)
    {
        return null;
    }
    
    public LinkedList<Object> getListaChairSenzaRevisori(int idConferenza) {
        return null;
    }
    
    public LinkedList<Object> getAutoriVersioneFinalePassata(Object conferenza) {
        return null;
    }
    
    public int getNumeroRevisori(int idConferenza) {
        try
        {
            int num=0;
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("SELECT COUNT(*) AS num FROM ruoli AS R WHERE R.idConferenza= ? AND R.ruolo=2");
            stmt.setInt(1, idConferenza);
            ResultSet ris=stmt.executeQuery();
            if(ris.next())
            {
                num=ris.getInt("num");
            }
            ris.close();
            stmt.close();
            con.close();
            return num;
        }
        catch(Exception e)
        {
            return -1; //ERRORE
        }
    }

    public String getEmail(int idUtente)
    {
        return null;
    }

    public LinkedList<String> getEmailAutori(int idArticolo)
    {
        return null;
    }
    
    public LinkedList<UtenteE> getChair(int idConferenza) {
        return null;
    }

    public LinkedList<UtenteE> getChairInformation(int idConferenza)
    {
        return null;
    }
    
    public UtenteE getEditore(int idConferenza) {
        return null;
    }

    public void setInfoReview(String puntiDiforza, String puntiDiDebolezza, int livelloDiCompetenzaDelRevisore, String commentiPerAutori, int valutazione)
    {

    }
    

    public ConferenzaE getConferenza(int idConferenza)
    {
        return null;
    }
    
    public void insertNotifica(String text, int receiverId, int type) {
        
    }
    
    public LinkedList<UtenteE> getListaRevisoriConLavoro(int idConferenza) {
        return null;
    }

    public LinkedList<UtenteE> getListaRevisori()
    {
        return null;
    }

    public LinkedList<UtenteE> getListaRevisoriArticoli(int idConferenza)
    {
        return null;
    }

    public LinkedList<Object> getListaRevisioniStato(int idConferenza)
    {
        return null;
    }
    
    public void automaticRetireSubmission(int idArticolo) {
        
    }

    public LinkedList<ArticoloE> getListaArticoliAssegnati(int idRevisore, int idConferenza)
    {
        return null;
    }
    
    public LinkedList<Object> getListaArticoliSottoRevisioni(Object sottoRevisore, Object conferenza, Object articolo) {
        return null;
    }

    public LinkedList<ArticoloE> getListaArticoliSotterevisore(int idSottorevisore, int idConferenza)
    {
        return null;
    }

    
}
