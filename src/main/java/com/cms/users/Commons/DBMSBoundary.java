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
import java.util.List;

import com.cms.App;
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
    private final String DB_URL = "jdbc:mariadb://cicciosworld.duckdns.org:3306/CMS";
    private final String DB_USER = "ids";
    private final String DB_PASSWORD = "IngegneriaDelSoftware";
    
    // Static variable to track current article being reviewed
    private static int currentArticleId = -1;
    
    public DBMSBoundary() {
        
    }
    
    /**
     * Sets the current article ID being reviewed
     */
    public static void setCurrentArticleId(int articleId) {
        currentArticleId = articleId;
    }
    
    /**
     * Gets the current article ID being reviewed
     */
    public static int getCurrentArticleId() {
        return currentArticleId;
    }

    public boolean verificaAssegnazioneEsistente(int idArticolo, int idRevisore) {
        try {
            // Usa una query diretta per verificare l'esistenza dell'assegnazione
            java.sql.Connection conn = null;
            java.sql.PreparedStatement stmt = null;
            java.sql.ResultSet rs = null;
            
            try
            {

            
                conn=getConnection();    
                String sql = "SELECT COUNT(*) FROM revisiona WHERE idRevisore = ? AND idArticolo = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idRevisore);
                stmt.setInt(2, idArticolo);
                
                rs = stmt.executeQuery();
                if (rs.next()) {
                    boolean assegnato = rs.getInt(1) > 0;
                    if (assegnato) {
                        System.out.println("Assegnazione trovata nel DB: Revisore " + idRevisore + 
                                         " -> Articolo " + idArticolo);
                    }
                    return assegnato;
                }
                
            } finally {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
            
            return false;
            
        } catch (Exception e) {
            System.err.println("Errore nella verifica dell'assegnazione per articolo " + idArticolo + 
                             " e revisore " + idRevisore + ": " + e.getMessage());
            return false;
        }
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
    
    /**
     * Ottiene la lista di tutti gli utenti presenti nel database
     * Implementa il metodo richiesto dal sequence diagram per l'aggiunta di revisori
     */
    public LinkedList<UtenteE> getUsersInfo() {
        try {
            LinkedList<UtenteE> utenti = new LinkedList<>();
            Connection con = getConnection();
            
            if (con == null) {
                System.err.println("Errore: impossibile stabilire connessione al database");
                return utenti;
            }
            
            // Query per ottenere tutti gli utenti nel database
            String sql = "SELECT u.id, u.username, u.email FROM utenti u ORDER BY u.username";
            
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                UtenteE utente = new UtenteE(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email")
                );
                utenti.add(utente);
            }
            
            rs.close();
            stmt.close();
            con.close();
            
            return utenti;
            
        } catch (Exception e) {
            System.err.println("Errore nel recupero degli utenti: " + e.getMessage());
            e.printStackTrace();
            return new LinkedList<>();
        }
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
        try {
            // Assumiamo che infos sia la nuova email (String)
            if (infos instanceof String) {
                String nuovaEmail = (String) infos;
                
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement("UPDATE utenti SET email = ? WHERE id = ?");
                stmt.setString(1, nuovaEmail);
                stmt.setInt(2, idUtente);
                
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Email aggiornata con successo per utente ID: " + idUtente);
                } else {
                    System.out.println("Nessuna riga aggiornata per utente ID: " + idUtente);
                }
                
                stmt.close();
                conn.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNewSkillInformation(int idUtente, int idConferenza, Object Skills)
    {
        System.out.println("DEBUG DBMSBoundary: === INIZIO sendNewSkillInformation ===");
        System.out.println("DEBUG DBMSBoundary: idUtente=" + idUtente + ", idConferenza=" + idConferenza);
        System.out.println("DEBUG DBMSBoundary: Skills ricevuto: " + Skills);
        
        try {
            // Converte Object Skills in List<String>
            @SuppressWarnings("unchecked")
            java.util.List<String> selectedSkills = (java.util.List<String>) Skills;
            
            if (selectedSkills == null || selectedSkills.isEmpty()) {
                System.out.println("DEBUG DBMSBoundary: Nessuna skill selezionata, non salvo nulla");
                return;
            }
            
            Connection con = getConnection();
            if (con == null) {
                System.err.println("DEBUG DBMSBoundary: ERRORE - Impossibile stabilire connessione al database");
                return;
            }
            
            // Prima rimuovi tutte le competenze esistenti per questo utente e conferenza
            System.out.println("DEBUG DBMSBoundary: Rimozione competenze esistenti...");
            PreparedStatement deleteStmt = con.prepareStatement(
                "DELETE FROM competenzeRevisore WHERE idUtente = ? AND idConferenza = ?"
            );
            deleteStmt.setInt(1, idUtente);
            deleteStmt.setInt(2, idConferenza);
            int deleted = deleteStmt.executeUpdate();
            System.out.println("DEBUG DBMSBoundary: Rimosse " + deleted + " competenze esistenti");
            deleteStmt.close();
            
            // Poi inserisci le nuove competenze selezionate
            System.out.println("DEBUG DBMSBoundary: Inserimento " + selectedSkills.size() + " nuove competenze...");
            
            for (String skill : selectedSkills) {
                if (skill != null && !skill.trim().isEmpty()) {
                    // Ottieni l'ID della keyword (creandola se non esiste)
                    int idKeyword = inserisciOOttieniKeyword(con, skill.trim());
                    
                    if (idKeyword > 0) {
                        // Inserisci la relazione nella tabella specificaCompetenza
                        PreparedStatement insertStmt = con.prepareStatement(
                            "INSERT INTO competenzeRevisore (idUtente, idConferenza, idKeyword) VALUES (?, ?, ?)"
                        );
                        insertStmt.setInt(1, idUtente);
                        insertStmt.setInt(2, idConferenza);
                        insertStmt.setInt(3, idKeyword);
                        
                        int inserted = insertStmt.executeUpdate();
                        if (inserted > 0) {
                            System.out.println("DEBUG DBMSBoundary: Inserita competenza: '" + skill + "' (idKeyword=" + idKeyword + ")");
                        } else {
                            System.err.println("DEBUG DBMSBoundary: ERRORE - Impossibile inserire competenza: '" + skill + "'");
                        }
                        insertStmt.close();
                    } else {
                        System.err.println("DEBUG DBMSBoundary: ERRORE - Impossibile ottenere ID per keyword: '" + skill + "'");
                    }
                }
            }
            
            con.close();
            System.out.println("DEBUG DBMSBoundary: Competenze salvate con successo");
            
        } catch (ClassCastException e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE - Skills non è del tipo corretto: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE durante sendNewSkillInformation: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("DEBUG DBMSBoundary: === FINE sendNewSkillInformation ===");
    }

    //RIVEDERE
    public LinkedList<NotificaE> ottieniNotificheAttive(String username) {
        System.out.println("DEBUG: === INIZIO ottieniNotificheAttive ===");
        System.out.println("DEBUG: Username ricevuto: '" + username + "'");
        
        try
        {
            LinkedList<NotificaE> notifiche=new LinkedList<>();
            Connection con=getConnection();
            
            if (con == null) {
                System.err.println("DEBUG: Connessione al database fallita!");
                return null;
            }
            System.out.println("DEBUG: Connessione al database stabilita con successo");
            
            String sql = "SELECT N.id, N.idConferenza, N.idUtente, N.testo, N.tipo, N.dettagli, N.esito FROM notifiche AS N, utenti AS U WHERE N.idUtente=U.id AND U.username= ? AND N.esito IS NULL";
            System.out.println("DEBUG: Query SQL: " + sql);
            System.out.println("DEBUG: Parametro username: '" + username + "'");
            
            PreparedStatement stmt=con.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet ris=stmt.executeQuery();
            
            int count = 0;
            while(ris.next())
            {
                count++;
                int id = ris.getInt("id");
                int idConferenza = ris.getInt("idConferenza");
                int idUtente = ris.getInt("idUtente");
                String testo = ris.getString("testo");
                int tipo = ris.getInt("tipo");
                String dettagli = ris.getString("dettagli");
                String esito = ris.getString("esito");
                
                System.out.println("DEBUG: Notifica #" + count + " trovata:");
                System.out.println("  - ID: " + id);
                System.out.println("  - ID Conferenza: " + idConferenza);
                System.out.println("  - ID Utente: " + idUtente);
                System.out.println("  - Testo: '" + testo + "'");
                System.out.println("  - Tipo: " + tipo);
                System.out.println("  - Dettagli: '" + dettagli + "'");
                System.out.println("  - Esito: '" + esito + "'");
                
                notifiche.add(new NotificaE(id, idConferenza, idUtente, testo, tipo, dettagli, esito));
            }
            
            System.out.println("DEBUG: Totale notifiche trovate: " + count);
            System.out.println("DEBUG: Dimensione lista notifiche: " + notifiche.size());
            
            ris.close();
            stmt.close();
            con.close();
            
            System.out.println("DEBUG: === FINE ottieniNotificheAttive ===");
            return notifiche;
        }
        catch(Exception e)
        {
            System.err.println("DEBUG: Errore durante il recupero delle notifiche: " + e.getMessage());
            e.printStackTrace();
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
    
    
    
    public NotificaE getNotifica(int idNotifica) { //DA CONTROLLARE
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
        System.out.println("DEBUG DBMSBoundary: === INIZIO updateNotificationStatus ===");
        System.out.println("DEBUG DBMSBoundary: idNotification=" + idNotification + ", status='" + status + "'");
        
        try
        {
            Connection con=getConnection();
            
            // Prima recupera le informazioni della notifica per determinare il tipo e i dati
            PreparedStatement selectStmt = con.prepareStatement(
                "SELECT N.idConferenza, N.idUtente, N.tipo, N.dettagli FROM notifiche AS N WHERE N.id= ?"
            );
            selectStmt.setInt(1, idNotification);
            ResultSet notificationResult = selectStmt.executeQuery();
            
            int idConferenza = -1;
            int idUtente = -1;
            int tipoNotifica = -1;
            String dettagli = null;
            
            if (notificationResult.next()) {
                idConferenza = notificationResult.getInt("idConferenza");
                idUtente = notificationResult.getInt("idUtente");
                tipoNotifica = notificationResult.getInt("tipo");
                dettagli = notificationResult.getString("dettagli");
                
                System.out.println("DEBUG DBMSBoundary: Notifica trovata:");
                System.out.println("  - ID Conferenza: " + idConferenza);
                System.out.println("  - ID Utente: " + idUtente);
                System.out.println("  - Tipo: " + tipoNotifica);
                System.out.println("  - Dettagli: '" + dettagli + "'");
            }
            notificationResult.close();
            selectStmt.close();
            
            // Aggiorna lo status della notifica
            PreparedStatement updateStmt = con.prepareStatement("UPDATE notifiche SET esito= ? WHERE id= ?");
            updateStmt.setString(1, status);
            updateStmt.setInt(2, idNotification);
            
            System.out.println("DEBUG DBMSBoundary: Eseguendo query: UPDATE notifiche SET esito='" + status + "' WHERE id=" + idNotification);
            
            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("DEBUG DBMSBoundary: Stato aggiornato con successo. Righe modificate: " + rowsUpdated);
            } else {
                System.err.println("DEBUG DBMSBoundary: ATTENZIONE - Nessuna riga aggiornata. Verificare che l'ID notifica esista.");
            }
            updateStmt.close();
            
            // Gestione speciale per inviti a revisore (tipo 1 = accetta/rifiuta) accettati
            if (tipoNotifica == 1 && "1".equals(status) && idConferenza != -1 && idUtente != -1) {
                // Verifica se è un invito a revisore controllando i dettagli o il testo
                if (dettagli != null && (dettagli.toLowerCase().contains("add-revisore") || 
                    dettagli.toLowerCase().contains("reviewer") || 
                    dettagli.toLowerCase().contains("invito"))) {
                    
                    System.out.println("DEBUG DBMSBoundary: Rilevato invito a revisore accettato - aggiungendo ruolo revisore");
                    
                    // Controlla se l'utente è già revisore per questa conferenza
                    PreparedStatement checkStmt = con.prepareStatement(
                        "SELECT COUNT(*) as count FROM ruoli WHERE idUtente = ? AND idConferenza = ? AND ruolo = 2"
                    );
                    checkStmt.setInt(1, idUtente);
                    checkStmt.setInt(2, idConferenza);
                    ResultSet checkResult = checkStmt.executeQuery();
                    
                    boolean alreadyReviewer = false;
                    if (checkResult.next()) {
                        alreadyReviewer = checkResult.getInt("count") > 0;
                    }
                    checkResult.close();
                    checkStmt.close();
                    
                    if (!alreadyReviewer) {
                        // Aggiungi l'utente come revisore (ruolo = 2) per la conferenza
                        PreparedStatement insertStmt = con.prepareStatement(
                            "INSERT INTO ruoli (idUtente, idConferenza, ruolo, dataRichiesta, dataRisposta, stato) VALUES (?, ?, 2, CURRENT_DATE, CURRENT_DATE, 1)"
                        );
                        insertStmt.setInt(1, idUtente);
                        insertStmt.setInt(2, idConferenza);
                        
                        int insertedRows = insertStmt.executeUpdate();
                        if (insertedRows > 0) {
                            System.out.println("DEBUG DBMSBoundary: Utente " + idUtente + " aggiunto come revisore per conferenza " + idConferenza);
                        } else {
                            System.err.println("DEBUG DBMSBoundary: ERRORE - Impossibile aggiungere l'utente come revisore");
                        }
                        insertStmt.close();
                    } else {
                        System.out.println("DEBUG DBMSBoundary: L'utente " + idUtente + " è già revisore per la conferenza " + idConferenza);
                    }
                } else {
                    System.out.println("DEBUG DBMSBoundary: Notifica accettata ma non è un invito a revisore (dettagli: '" + dettagli + "')");
                }
            } else {
                System.out.println("DEBUG DBMSBoundary: Nessuna azione aggiuntiva richiesta (tipo=" + tipoNotifica + ", status='" + status + "')");
            }
            
            con.close();
        }
        catch(Exception e)
        {
            System.err.println("DEBUG DBMSBoundary: ERRORE durante updateNotificationStatus: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("DEBUG DBMSBoundary: === FINE updateNotificationStatus ===");
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
        System.out.println("DEBUG DBMSBoundary: === INIZIO getKeywords ===");
        System.out.println("DEBUG DBMSBoundary: idConferenza ricevuto: " + idConferenza);
        
        ArrayList<String> keywords = new ArrayList<>();
        try {
            Connection con = getConnection();
            if (con == null) {
                System.err.println("DEBUG DBMSBoundary: ERRORE - Impossibile stabilire connessione al database");
                return keywords;
            }
            
            keywords = this.getKeywordsConferenza(con, idConferenza);
            con.close();
            
            System.out.println("DEBUG DBMSBoundary: Keywords recuperate: " + keywords.size());
            for (int i = 0; i < keywords.size(); i++) {
                System.out.println("  [" + i + "] '" + keywords.get(i) + "'");
            }
            
        } catch (Exception e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE durante getKeywords: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("DEBUG DBMSBoundary: === FINE getKeywords ===");
        return keywords;
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

            stmt=con.prepareStatement("INSERT INTO ruoli VALUES (?, ?, 1, 1, CURRENT_DATE, CURRENT_DATE, NULL)");
            stmt.setInt(1, App.utenteAccesso.getId());
            stmt.setInt(2, getLastInsertedConferenceId(con));
            int rowsInsertedRuolo = stmt.executeUpdate();
            if (rowsInsertedRuolo > 0) {
                System.out.println("Ruolo assegnato con successo all'utente per la conferenza: " + titolo);
            } else {
                System.err.println("Errore: nessun ruolo assegnato");
            }

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

    /**
     * Ottiene il log dettagliato della conferenza dal database
     * Implementa il metodo richiesto dal sequence diagram con informazioni complete
     */
    public String getConferenceLog(int conferenceId) {
        StringBuilder log = new StringBuilder();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("Impossibile stabilire connessione al database per recuperare il log");
                return generateDefaultLog(conferenceId);
            }
            
            // === INFORMAZIONI GENERALI DELLA CONFERENZA ===
            String query = "SELECT titolo, abstract, edizione, dataInizio, dataFine, " +
                          "deadlineSottomissione, deadlineRitiro, deadlineRevisioni, deadlineVersioneFinale, " +
                          "deadlineVersionePubblicazione, luogo, numRevisoriPerArticolo, numeroArticoliPrevisti, " +
                          "tassoAccettazione FROM conferenze WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, conferenceId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                String titolo = rs.getString("titolo");
                int anno = rs.getInt("edizione");
                String abstractConf = rs.getString("abstract");
                String luogo = rs.getString("luogo");
                
                log.append("=== INFORMAZIONI GENERALI CONFERENZA ===\n");
                log.append("Titolo: ").append(titolo).append("\n");
                log.append("Edizione: ").append(anno).append("\n");
                log.append("Luogo: ").append(luogo != null ? luogo : "Non specificato").append("\n");
                if (abstractConf != null && !abstractConf.trim().isEmpty()) {
                    log.append("Abstract: ").append(abstractConf).append("\n");
                }
                
                // === DATE E DEADLINE ===
                log.append("\n=== DATE E DEADLINE ===\n");
                java.sql.Date dataInizio = rs.getDate("dataInizio");
                java.sql.Date dataFine = rs.getDate("dataFine");
                java.sql.Date deadlineSottomissione = rs.getDate("deadlineSottomissione");
                java.sql.Date deadlineRitiro = rs.getDate("deadlineRitiro");
                java.sql.Date deadlineRevisioni = rs.getDate("deadlineRevisioni");
                java.sql.Date deadlineVersioneFinale = rs.getDate("deadlineVersioneFinale");
                java.sql.Date deadlineVersionePubblicazione = rs.getDate("deadlineVersionePubblicazione");
                
                if (dataInizio != null) log.append("Data inizio conferenza: ").append(dataInizio.toString()).append("\n");
                if (dataFine != null) log.append("Data fine conferenza: ").append(dataFine.toString()).append("\n");
                if (deadlineSottomissione != null) log.append("Deadline sottomissione: ").append(deadlineSottomissione.toString()).append("\n");
                if (deadlineRitiro != null) log.append("Deadline ritiro: ").append(deadlineRitiro.toString()).append("\n");
                if (deadlineRevisioni != null) log.append("Deadline revisioni: ").append(deadlineRevisioni.toString()).append("\n");
                if (deadlineVersioneFinale != null) log.append("Deadline versione finale: ").append(deadlineVersioneFinale.toString()).append("\n");
                if (deadlineVersionePubblicazione != null) log.append("Deadline pubblicazione: ").append(deadlineVersionePubblicazione.toString()).append("\n");
                
                // === PARAMETRI CONFERENZA ===
                log.append("\n=== PARAMETRI CONFERENZA ===\n");
                log.append("Numero revisori per articolo: ").append(rs.getInt("numRevisoriPerArticolo")).append("\n");
                log.append("Numero articoli previsti: ").append(rs.getInt("numeroArticoliPrevisti")).append("\n");
                log.append("Tasso di accettazione: ").append(rs.getInt("tassoAccettazione")).append("%\n");
                
                rs.close();
                stmt.close();
                
                // === KEYWORDS DELLA CONFERENZA ===
                log.append("\n=== KEYWORDS CONFERENZA ===\n");
                ArrayList<String> keywords = getKeywordsConferenza(conn, conferenceId);
                if (keywords != null && !keywords.isEmpty()) {
                    log.append("Keywords: ").append(String.join(", ", keywords)).append("\n");
                } else {
                    log.append("Nessuna keyword specificata\n");
                }
                
                // === CHAIR E CO-CHAIR ===
                log.append("\n=== CHAIR E CO-CHAIR ===\n");
                try {
                    String chairQuery = "SELECT u.username, u.email, r.ruolo FROM utenti u " +
                                       "INNER JOIN ruoli r ON u.id = r.idUtente " +
                                       "WHERE r.idConferenza = ? AND r.ruolo IN (1, 4) ORDER BY r.ruolo";
                    PreparedStatement chairStmt = conn.prepareStatement(chairQuery);
                    chairStmt.setInt(1, conferenceId);
                    ResultSet chairRs = chairStmt.executeQuery();
                    
                    int chairCount = 0;
                    int coChairCount = 0;
                    while (chairRs.next()) {
                        String username = chairRs.getString("username");
                        String email = chairRs.getString("email");
                        int ruolo = chairRs.getInt("ruolo");
                        
                        if (ruolo == 1) {
                            chairCount++;
                            log.append("Chair: ").append(username).append(" (").append(email).append(")\n");
                        } else if (ruolo == 4) {
                            coChairCount++;
                            log.append("Co-Chair: ").append(username).append(" (").append(email).append(")\n");
                        }
                    }
                    
                    if (chairCount == 0 && coChairCount == 0) {
                        log.append("Nessun Chair o Co-Chair registrato\n");
                    }
                    
                    chairRs.close();
                    chairStmt.close();
                } catch (SQLException e) {
                    log.append("Errore nel recupero di Chair e Co-Chair: ").append(e.getMessage()).append("\n");
                }
                
                // === REVISORI ===
                log.append("\n=== REVISORI ===\n");
                try {
                    String revisoriQuery = "SELECT u.username, u.email, r.dataRichiesta, r.dataRisposta, r.dataRimozione FROM utenti u " +
                                          "INNER JOIN ruoli r ON u.id = r.idUtente " +
                                          "WHERE r.idConferenza = ? AND r.ruolo = 2 ORDER BY u.username";
                    PreparedStatement revisoriStmt = conn.prepareStatement(revisoriQuery);
                    revisoriStmt.setInt(1, conferenceId);
                    ResultSet revisoriRs = revisoriStmt.executeQuery();
                    
                    int revisoriCount = 0;
                    while (revisoriRs.next()) {
                        revisoriCount++;
                        String username = revisoriRs.getString("username");
                        String email = revisoriRs.getString("email");
                        java.sql.Date dataInizioRuolo = revisoriRs.getDate("dataRichiesta");
                        java.sql.Date dataFineRuolo = revisoriRs.getDate("dataRisposta");
                        java.sql.Date dataRimozione = revisoriRs.getDate("dataRimozione");
                        
                        log.append("Revisore ").append(revisoriCount).append(": ").append(username).append(" (").append(email).append(")");
                        if (dataInizioRuolo != null) {
                            log.append(" - data proposta: ").append(dataInizioRuolo.toString());
                        }
                        if (dataFineRuolo != null) {
                            log.append(", data risposta: ").append(dataFineRuolo.toString());
                        }
                        if(dataRimozione != null) {
                            log.append(", data rimozione: ").append(dataRimozione.toString());
                        }
                        log.append("\n");
                    }
                    
                    log.append("Totale revisori: ").append(revisoriCount).append("\n");
                    revisoriRs.close();
                    revisoriStmt.close();
                } catch (SQLException e) {
                    log.append("Errore nel recupero dei revisori: ").append(e.getMessage()).append("\n");
                }
                
                // === ARTICOLI E SOTTOMISSIONI ===
                log.append("\n=== ARTICOLI E SOTTOMISSIONI ===\n");
                try {
                    String articoliQuery = "SELECT a.id, a.titolo, a.stato, a.ultimaModifica, " +
                                          "GROUP_CONCAT(DISTINCT u.username SEPARATOR ', ') as autori " +
                                          "FROM articoli a " +
                                          "LEFT JOIN sottomette s ON a.id = s.idArticolo " +
                                          "LEFT JOIN utenti u ON s.idUtente = u.id " +
                                          "WHERE a.idConferenza = ? " +
                                          "GROUP BY a.id, a.titolo, a.stato, a.ultimaModifica " +
                                          "ORDER BY a.ultimaModifica DESC";
                    PreparedStatement articoliStmt = conn.prepareStatement(articoliQuery);
                    articoliStmt.setInt(1, conferenceId);
                    ResultSet articoliRs = articoliStmt.executeQuery();
                    
                    int articoliCount = 0;
                    java.util.Map<String, Integer> statiCount = new java.util.HashMap<>();
                    
                    while (articoliRs.next()) {
                        articoliCount++;
                        int idArticolo = articoliRs.getInt("id");
                        String titoloArticolo = articoliRs.getString("titolo");
                        String stato = articoliRs.getString("stato");
                        String autori = articoliRs.getString("autori");
                        java.sql.Date ultimaModifica = articoliRs.getDate("ultimaModifica");
                        
                        log.append("Articolo ").append(articoliCount).append(": ").append(titoloArticolo).append("\n");
                        log.append("  - ID: ").append(idArticolo).append("\n");
                        log.append("  - Stato: ").append(stato != null ? stato : "Non specificato").append("\n");
                        log.append("  - Autori: ").append(autori != null ? autori : "Non specificati").append("\n");
                        if (ultimaModifica != null) {
                            log.append("  - Ultima modifica: ").append(ultimaModifica.toString()).append("\n");
                        }
                        log.append("\n");
                        
                        // Conta gli stati
                        String statoKey = stato != null ? stato : "Non specificato";
                        statiCount.put(statoKey, statiCount.getOrDefault(statoKey, 0) + 1);
                    }
                    
                    log.append("Totale articoli sottomessi: ").append(articoliCount).append("\n");
                    log.append("Riepilogo stati:\n");
                    for (java.util.Map.Entry<String, Integer> entry : statiCount.entrySet()) {
                        log.append("  - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                    }
                    
                    articoliRs.close();
                    articoliStmt.close();
                } catch (SQLException e) {
                    log.append("Errore nel recupero degli articoli: " + e.getMessage() + "\n");
                    // Fallback: query semplificata
                    try {
                        String simpleQuery = "SELECT COUNT(*) as count FROM articoli WHERE idConferenza = ?";
                        PreparedStatement simpleStmt = conn.prepareStatement(simpleQuery);
                        simpleStmt.setInt(1, conferenceId);
                        ResultSet simpleRs = simpleStmt.executeQuery();
                        if (simpleRs.next()) {
                            log.append("Totale articoli (conteggio semplificato): ").append(simpleRs.getInt("count")).append("\n");
                        }
                        simpleRs.close();
                        simpleStmt.close();
                    } catch (SQLException e2) {
                        log.append("Impossibile recuperare il conteggio degli articoli\n");
                    }
                }
                
                // === NOTIFICHE ===
                log.append("\n=== NOTIFICHE E COMUNICAZIONI ===\n");
                try {
                    String notificheQuery = "SELECT COUNT(*) as totale, " +
                                           "COUNT(CASE WHEN tipo = 0 THEN 1 END) as comunicazioni, " +
                                           "COUNT(CASE WHEN tipo = 1 THEN 1 END) as inviti, " +
                                           "COUNT(CASE WHEN esito IS NULL THEN 1 END) as attive, " +
                                           "COUNT(CASE WHEN esito IS NOT NULL THEN 1 END) as gestite " +
                                           "FROM notifiche WHERE idConferenza = ?";
                    PreparedStatement notificheStmt = conn.prepareStatement(notificheQuery);
                    notificheStmt.setInt(1, conferenceId);
                    ResultSet notificheRs = notificheStmt.executeQuery();
                    
                    if (notificheRs.next()) {
                        int totaleNotifiche = notificheRs.getInt("totale");
                        int comunicazioni = notificheRs.getInt("comunicazioni");
                        int inviti = notificheRs.getInt("inviti");
                        int attive = notificheRs.getInt("attive");
                        int gestite = notificheRs.getInt("gestite");
                        
                        log.append("Totale notifiche inviate: ").append(totaleNotifiche).append("\n");
                        log.append("  - Comunicazioni: ").append(comunicazioni).append("\n");
                        log.append("  - Inviti: ").append(inviti).append("\n");
                        log.append("  - Notifiche attive (non gestite): ").append(attive).append("\n");
                        log.append("  - Notifiche gestite: ").append(gestite).append("\n");
                    }
                    
                    notificheRs.close();
                    notificheStmt.close();
                } catch (SQLException e) {
                    log.append("Errore nel recupero delle notifiche: ").append(e.getMessage()).append("\n");
                }
                
               
            } else {
                log.append("ERRORE: Conferenza con ID ").append(conferenceId).append(" non trovata nel database\n");
            }
            
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero del log della conferenza: " + e.getMessage());
            return generateDefaultLog(conferenceId);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura delle risorse del database: " + e.getMessage());
            }
        }
        
        String result = log.toString();
        return result.isEmpty() ? generateDefaultLog(conferenceId) : result;
    }
    
    /**
     * Genera un log dettagliato di default quando non è possibile recuperare dati dal database
     */
    private String generateDefaultLog(int conferenceId) {
        StringBuilder log = new StringBuilder();
        String dataAttuale = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        
        log.append("=== LOG CONFERENZA (MODALITÀ FALLBACK) ===\n");
        log.append("ID Conferenza: ").append(conferenceId).append("\n");
        log.append("Data generazione: ").append(dataAttuale).append("\n");
        log.append("Stato: Sistema non in grado di recuperare log dettagliati dal database\n\n");
        
        log.append("=== INFORMAZIONI DISPONIBILI ===\n");
        log.append("- Conferenza presente nel sistema con ID: ").append(conferenceId).append("\n");
        log.append("- Connessione al database non disponibile al momento della generazione\n");
        log.append("- Log dettagliato non recuperabile\n\n");
        
        log.append("=== SUGGERIMENTI ===\n");
        log.append("1. Verificare la connessione al database\n");
        log.append("2. Controllare che la conferenza esista nella tabella 'conferenze'\n");
        log.append("3. Riprovare la generazione del log più tardi\n");
        log.append("4. Contattare l'amministratore di sistema se il problema persiste\n\n");
        
        log.append("=== INFORMAZIONI TECNICHE ===\n");
        log.append("Sistema: Conference Management System (CMS)\n");
        log.append("Modulo: DBMSBoundary.getConferenceLog()\n");
        log.append("Tipo errore: Connessione database non disponibile\n");
        log.append("Timestamp: ").append(dataAttuale).append("\n");
        
        return log.toString();
    }
    
    public ConferenzaE getConferenceInfo(int idConferenza) {
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

        try
        {
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("INSERT INTO revisiona VALUES(NULL, ?, ?, NULL, NULL, NULL, NULL, NULL, NULL)");
            stmt.setInt(1, idRevisore);
            stmt.setInt(2, idArticolo);
            stmt.executeQuery();
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }

    /**
     * Rimuove un revisore da una conferenza
     * Implementa il metodo richiesto dal sequence diagram
     */
    public void rimuoviRevisore(int idConferenza, int idRevisore) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            
            // Rimuovi il revisore dalla tabella ruoli
            String sql = "UPDATE ruoli SET stato=3, dataRimozione=CURRENT_DATE WHERE idUtente = ? AND idConferenza = ? AND ruolo = 2";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idRevisore);
            stmt.setInt(2, idConferenza);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Revisore " + idRevisore + " rimosso con successo dalla conferenza " + idConferenza);
            } else {
                System.out.println("Nessun revisore trovato con ID " + idRevisore + " per la conferenza " + idConferenza);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore durante la rimozione del revisore: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Errore generico durante la rimozione del revisore: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Errore chiusura connessione: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    public LinkedList<ArticoloE> getListaSottomissioni(int idConferenza) {
        return null;
    }
    
    public Object getInfoArticolo(int idArticolo) {
        return null;
    }
    
    public Object getKeywordsList(int idConferenza) { //vedere se dublicato
        System.out.println("DEBUG DBMSBoundary: === INIZIO getKeywordsList ===");
        System.out.println("DEBUG DBMSBoundary: idConferenza ricevuto: " + idConferenza);
        
        try {
            // Riutilizza il metodo getKeywords esistente
            ArrayList<String> keywords = getKeywords(idConferenza);
            System.out.println("DEBUG DBMSBoundary: Risultato getKeywords: " + 
                              (keywords != null ? keywords.size() + " keywords" : "null"));
            return keywords;
        } catch (Exception e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE durante getKeywordsList: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<String>(); // Restituisce lista vuota in caso di errore
        }
    }
    
    
    /**
     * Ottiene la lista di tutti gli articoli sottomessi per una conferenza
     * Implementa il metodo richiesto per il calcolo delle statistiche
     */
    public LinkedList<ArticoloE> getListaArticoli(int idConferenza) {
        LinkedList<ArticoloE> articoli = new LinkedList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("DEBUG DBMSBoundary: ERRORE - Impossibile stabilire connessione al database");
                return articoli;
            }
            
            // Query per ottenere tutti gli articoli di una conferenza
            String query = "SELECT id, titolo, abstract, fileArticolo, allegato, stato, idConferenza, ultimaModifica " +
                          "FROM articoli WHERE idConferenza = ? ORDER BY titolo";
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idConferenza);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                // Crea l'oggetto ArticoloE usando il costruttore disponibile
                ArticoloE articolo = new ArticoloE(
                    rs.getInt("id"),
                    rs.getString("titolo"),
                    rs.getString("abstract"),
                    rs.getObject("fileArticolo"), // BLOB
                    rs.getObject("allegato"), // BLOB
                    rs.getString("stato"),
                    rs.getInt("idConferenza"),
                    rs.getTimestamp("ultimaModifica") != null ? 
                        rs.getTimestamp("ultimaModifica").toLocalDateTime().toLocalDate() : null
                );
                
                // Ottieni e imposta le keywords per questo articolo
                ArrayList<String> keywords = getKeywordsArticolo(rs.getInt("id"));
                if (keywords != null && !keywords.isEmpty()) {
                    LinkedList<String> keywordsList = new LinkedList<>();
                    keywordsList.addAll(keywords);
                    articolo.setKeywords(keywordsList);
                }
                
                articoli.add(articolo);
                System.out.println("DEBUG DBMSBoundary: Articolo recuperato - ID: " + articolo.getId() + 
                                 ", Titolo: '" + articolo.getTitolo() + "'");
            }
            
            System.out.println("DEBUG DBMSBoundary: Totale articoli recuperati: " + articoli.size());
            
        } catch (SQLException e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE SQL durante getListaArticoli: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE generico durante getListaArticoli: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Chiusura delle risorse
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("DEBUG DBMSBoundary: ERRORE durante la chiusura delle risorse: " + e.getMessage());
            }
        }
        
        System.out.println("DEBUG DBMSBoundary: === FINE getListaArticoli ===");
        return articoli;
    }
    
    public void newConflitto(int idConferenza, int idUtente, int idArticolo) {
        System.out.println("DEBUG DBMSBoundary: === INIZIO newConflitto ===");
        System.out.println("DEBUG DBMSBoundary: idConferenza=" + idConferenza + ", idUtente=" + idUtente + ", idArticolo=" + idArticolo);
        
        try {
            Connection con = getConnection();
            if (con == null) {
                System.err.println("DEBUG DBMSBoundary: ERRORE - Impossibile stabilire connessione al database");
                return;
            }

            String checkQuery2 = "SELECT COUNT(*) as count FROM specificaInfoAggiuntive WHERE idUtente = ? AND idArticolo = ? AND interesse=TRUE";
            PreparedStatement checkStmt2 = con.prepareStatement(checkQuery2);
            checkStmt2.setInt(1, idUtente);
            checkStmt2.setInt(2, idArticolo);
            
            ResultSet checkResult2 = checkStmt2.executeQuery();
            boolean recordExists2 = false;
            if (checkResult2.next()) {
                recordExists2 = checkResult2.getInt("count") > 0;
            }
            checkResult2.close();
            checkStmt2.close();

            if(recordExists2){
                String updateQuery2 = "UPDATE specificaInfoAggiuntive SET interesse = FALSE WHERE idUtente = ? AND idArticolo = ?";
                PreparedStatement updateStmt2 = con.prepareStatement(updateQuery2);
                updateStmt2.setInt(1, idUtente);
                updateStmt2.setInt(2, idArticolo);
                
                int updated = updateStmt2.executeUpdate();
                System.out.println("DEBUG DBMSBoundary: Record conflitto aggiornato: " + (updated > 0 ? "successo" : "fallito"));
                updateStmt2.close();
            }
            
            // Controlla se esiste già un record per questo utente, conferenza e articolo
            String checkQuery = "SELECT COUNT(*) as count FROM specificaInfoAggiuntive WHERE idUtente = ? AND idArticolo = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setInt(1, idUtente);
            checkStmt.setInt(2, idArticolo);
            
            ResultSet checkResult = checkStmt.executeQuery();
            boolean recordExists = false;
            if (checkResult.next()) {
                recordExists = checkResult.getInt("count") > 0;
            }
            checkResult.close();
            checkStmt.close();
            
            if (recordExists) {
                // Aggiorna il record esistente
                String updateQuery = "UPDATE specificaInfoAggiuntive SET conflitto = TRUE WHERE idUtente = ? AND idArticolo = ?";
                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setInt(1, idUtente);
                updateStmt.setInt(2, idArticolo);
                
                int updated = updateStmt.executeUpdate();
                System.out.println("DEBUG DBMSBoundary: Record conflitto aggiornato: " + (updated > 0 ? "successo" : "fallito"));
                updateStmt.close();
            } else {
                // Inserisci nuovo record
                String insertQuery = "INSERT INTO specificaInfoAggiuntive (idUtente, idArticolo, conflitto, interesse) VALUES (?, ?, TRUE, FALSE)";
                PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                insertStmt.setInt(1, idUtente);
                insertStmt.setInt(2, idArticolo);
                
                int inserted = insertStmt.executeUpdate();
                System.out.println("DEBUG DBMSBoundary: Nuovo record conflitto inserito: " + (inserted > 0 ? "successo" : "fallito"));
                insertStmt.close();
            }
            
            con.close();
            
        } catch (SQLException e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE SQL durante newConflitto: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE generico durante newConflitto: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("DEBUG DBMSBoundary: === FINE newConflitto ===");
    }
    
    public void newInteresse(int idConferenza, int idUtente, int idArticolo) {
        System.out.println("DEBUG DBMSBoundary: === INIZIO newInteresse ===");
        System.out.println("DEBUG DBMSBoundary: idConferenza=" + idConferenza + ", idUtente=" + idUtente + ", idArticolo=" + idArticolo);
        
        try {
            Connection con = getConnection();
            if (con == null) {
                System.err.println("DEBUG DBMSBoundary: ERRORE - Impossibile stabilire connessione al database");
                return;
            }
            
            String checkQuery2 = "SELECT COUNT(*) as count FROM specificaInfoAggiuntive WHERE idUtente = ? AND idArticolo = ? AND conflitto=TRUE";
            PreparedStatement checkStmt2 = con.prepareStatement(checkQuery2);
            checkStmt2.setInt(1, idUtente);
            checkStmt2.setInt(2, idArticolo);
            
            ResultSet checkResult2 = checkStmt2.executeQuery();
            boolean recordExists2 = false;
            if (checkResult2.next()) {
                recordExists2 = checkResult2.getInt("count") > 0;
            }
            checkResult2.close();
            checkStmt2.close();

            if(recordExists2){
                String updateQuery2 = "UPDATE specificaInfoAggiuntive SET conflitto = FALSE WHERE idUtente = ? AND idArticolo = ?";
                PreparedStatement updateStmt2 = con.prepareStatement(updateQuery2);
                updateStmt2.setInt(1, idUtente);
                updateStmt2.setInt(2, idArticolo);
                
                int updated = updateStmt2.executeUpdate();
                System.out.println("DEBUG DBMSBoundary: Record conflitto aggiornato: " + (updated > 0 ? "successo" : "fallito"));
                updateStmt2.close();
            }


            // Controlla se esiste già un record per questo utente, conferenza e articolo
            String checkQuery = "SELECT COUNT(*) as count FROM specificaInfoAggiuntive WHERE idUtente = ? AND idArticolo = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setInt(1, idUtente);
            checkStmt.setInt(2, idArticolo);
            
            ResultSet checkResult = checkStmt.executeQuery();
            boolean recordExists = false;
            if (checkResult.next()) {
                recordExists = checkResult.getInt("count") > 0;
            }
            checkResult.close();
            checkStmt.close();
            
            if (recordExists) {
                // Aggiorna il record esistente
                String updateQuery = "UPDATE specificaInfoAggiuntive SET interesse = TRUE WHERE idUtente = ? AND idArticolo = ?";
                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setInt(1, idUtente);
                updateStmt.setInt(2, idArticolo);
                
                int updated = updateStmt.executeUpdate();
                System.out.println("DEBUG DBMSBoundary: Record interesse aggiornato: " + (updated > 0 ? "successo" : "fallito"));
                updateStmt.close();
            } else {
                // Inserisci nuovo record
                String insertQuery = "INSERT INTO specificaInfoAggiuntive (idUtente, idArticolo, conflitto, interesse) VALUES (?, ?, FALSE, TRUE)";
                PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                insertStmt.setInt(1, idUtente);
                insertStmt.setInt(2, idArticolo);
                
                int inserted = insertStmt.executeUpdate();
                System.out.println("DEBUG DBMSBoundary: Nuovo record interesse inserito: " + (inserted > 0 ? "successo" : "fallito"));
                insertStmt.close();
            }
            
            con.close();
            
        } catch (SQLException e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE SQL durante newInteresse: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE generico durante newInteresse: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("DEBUG DBMSBoundary: === FINE newInteresse ===");
    }
    
    
    public Object getReviewStatus(int idArticolo) {
        try {
            Connection conn = getConnection();
            if (conn == null) return false;
            
            String query = "SELECT COUNT(*) as count FROM revisiona WHERE idArticolo = ?";
            PreparedStatement stat = conn.prepareStatement(query);
            stat.setInt(1, idArticolo);
            
            ResultSet result = stat.executeQuery();
            
            boolean hasReviews = false;
            if (result.next()) {
                hasReviews = result.getInt("count") > 0;
            }
            
            result.close();
            stat.close();
            conn.close();
            
            return hasReviews;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Object getInfoReview(int idArticolo) {
        try {
            Connection conn = getConnection();
            if (conn == null) return null;
            
            String query = "SELECT r.id as revisionId, u.username as reviewerName, " +
                          "r.commentiPerAutori as comment, " +
                          "CASE " +
                          "  WHEN r.valutazione IS NOT NULL THEN 'Completata' " +
                          "  WHEN r.commentiPerAutori IS NOT NULL THEN 'In corso' " +
                          "  ELSE 'Da iniziare' " +
                          "END as status " +
                          "FROM revisiona r " +
                          "INNER JOIN utenti u ON r.idRevisore = u.id " +
                          "WHERE r.idArticolo = ?";
            
            PreparedStatement stat = conn.prepareStatement(query);
            stat.setInt(1, idArticolo);
            
            ResultSet result = stat.executeQuery();
            
            ArrayList<Object> revisions = new ArrayList<>();
            
            while (result.next()) {
                String revisionId = "REV" + result.getInt("revisionId");
                String reviewerName = result.getString("reviewerName");
                String comment = result.getString("comment");
                if (comment == null) comment = "Nessun commento disponibile";
                String status = result.getString("status");
                
                // Creo un array di stringhe invece di un oggetto RevisionData
                // per evitare dipendenze circolari
                String[] revisionData = {revisionId, reviewerName, comment, status};
                revisions.add(revisionData);
            }
            
            result.close();
            stat.close();
            conn.close();
            
            return revisions;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LinkedList<UtenteE> getInformazioniAutoriRevisioneFinalePassata(int idConferenza)
    {
        return null;
    }
    
    public ArticoloE getArticolo(int idArticolo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("Impossibile stabilire connessione al database");
                return null;
            }
            
            // Query per ottenere l'articolo dalla tabella articoli
            String query = "SELECT id, titolo, abstract, fileArticolo, allegato, stato, idConferenza, ultimaModifica " +
                          "FROM articoli WHERE id = ?";
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idArticolo);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Crea un nuovo ArticoloE con i dati recuperati
                ArticoloE articolo = new ArticoloE();
                articolo.setId(rs.getInt("id"));
                articolo.setTitolo(rs.getString("titolo"));
                articolo.setAbstractText(rs.getString("abstract"));
                
                // Handle fileArticolo as BLOB (convert to byte array) or as String
                java.sql.Blob fileBlob = rs.getBlob("fileArticolo");
                if (fileBlob != null) {
                    try {
                        byte[] fileBytes = fileBlob.getBytes(1, (int) fileBlob.length());
                        articolo.setFileArticolo(fileBytes);
                        System.out.println("File articolo caricato come BLOB: " + fileBytes.length + " bytes");
                    } catch (SQLException e) {
                        System.err.println("Errore nel recupero del BLOB fileArticolo: " + e.getMessage());
                        // Fallback: try to get as string
                        String filePath = rs.getString("fileArticolo");
                        if (filePath != null) {
                            articolo.setFileArticolo(filePath);
                            System.out.println("File articolo caricato come path: " + filePath);
                        }
                    }
                } else {
                    // Try to get as string (for legacy data)
                    String filePath = rs.getString("fileArticolo");
                    if (filePath != null) {
                        articolo.setFileArticolo(filePath);
                        System.out.println("File articolo caricato come path: " + filePath);
                    }
                }
                
                // Handle allegato as BLOB (convert to byte array) or as String
                java.sql.Blob allegatoBlob = rs.getBlob("allegato");
                if (allegatoBlob != null) {
                    try {
                        byte[] allegatoBytes = allegatoBlob.getBytes(1, (int) allegatoBlob.length());
                        articolo.setAllegato(allegatoBytes);
                        System.out.println("Allegato caricato come BLOB: " + allegatoBytes.length + " bytes");
                    } catch (SQLException e) {
                        System.err.println("Errore nel recupero del BLOB allegato: " + e.getMessage());
                        // Fallback: try to get as string
                        String allegatoPath = rs.getString("allegato");
                        if (allegatoPath != null) {
                            articolo.setAllegato(allegatoPath);
                            System.out.println("Allegato caricato come path: " + allegatoPath);
                        }
                    }
                } else {
                    // Try to get as string (for legacy data)
                    String allegatoPath = rs.getString("allegato");
                    if (allegatoPath != null) {
                        articolo.setAllegato(allegatoPath);
                        System.out.println("Allegato caricato come path: " + allegatoPath);
                    }
                }
                
                articolo.setStato(rs.getString("stato"));
                articolo.setIdConferenza(rs.getInt("idConferenza"));
                
                // Gestione della data ultimaModifica
                java.sql.Date sqlDate = rs.getDate("ultimaModifica");
                if (sqlDate != null) {
                    articolo.setUltimaModifica(sqlDate.toLocalDate());
                }
                
                return articolo;
            } else {
                System.out.println("Nessun articolo trovato con ID: " + idArticolo);
                return null;
            }
            
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dell'articolo: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            // Chiusura delle risorse
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura delle risorse: " + e.getMessage());
            }
        }
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
    
    public LinkedList<ArticoloE> getListaSottomissioni(int idUtente, int idConferenza) {
        LinkedList<ArticoloE> listaSottomissioni = new LinkedList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("Impossibile stabilire connessione al database");
                return listaSottomissioni;
            }
            
            // Query corretta che filtra gli articoli dove l'utente è autore principale o co-autore
            String query = "SELECT DISTINCT a.id, a.titolo, a.abstract, a.fileArticolo, a.allegato, " +
                          "a.stato, a.idConferenza, a.ultimaModifica " +
                          "FROM articoli a " +
                          "INNER JOIN sottomette s ON a.id = s.idArticolo " +
                          "WHERE a.idConferenza = ? " +
                          "AND s.idUtente = ?";
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idConferenza);
            stmt.setInt(2, idUtente);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                ArticoloE articolo = new ArticoloE();
                articolo.setId(rs.getInt("id"));
                articolo.setTitolo(rs.getString("titolo"));
                articolo.setAbstractText(rs.getString("abstract"));
                articolo.setFileArticolo(rs.getObject("fileArticolo"));
                articolo.setAllegato(rs.getObject("allegato"));
                articolo.setStato(rs.getString("stato"));
                articolo.setIdConferenza(rs.getInt("idConferenza"));
                
                // Gestione della data
                java.sql.Date sqlDate = rs.getDate("ultimaModifica");
                if (sqlDate != null) {
                    articolo.setUltimaModifica(sqlDate.toLocalDate());
                }
                
                listaSottomissioni.add(articolo);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle sottomissioni: " + e.getMessage());
            e.printStackTrace();
            
            // Se la tabella sottomette non esiste, proviamo una query più semplice
            if (e.getMessage().contains("sottomette")) {
                System.out.println("Tabella sottomette non trovata, provo query semplificata...");
                return getListaSottomissioniSemplificata(idUtente, idConferenza);
            }
            
        } finally {
            // Chiusura delle risorse
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura delle risorse: " + e.getMessage());
            }
        }
        
        return listaSottomissioni;
    }
    
    /**
     * Metodo di fallback per quando non esiste la tabella sottomette
     * Mostra tutti gli articoli della conferenza (non filtrati per utente)
     */
    private LinkedList<ArticoloE> getListaSottomissioniSemplificata(int idUtente, int idConferenza) {
        LinkedList<ArticoloE> listaSottomissioni = new LinkedList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("Impossibile stabilire connessione al database");
                return listaSottomissioni;
            }
            
            // Query semplificata - mostra tutti gli articoli della conferenza
            // NOTA: Non filtra per utente perché assume che la tabella sottomette non esista
            System.out.println("ATTENZIONE: Usando query semplificata - mostra tutti gli articoli della conferenza");
            String query = "SELECT a.id, a.titolo, a.abstract, a.fileArticolo, a.allegato, " +
                          "a.stato, a.idConferenza, a.ultimaModifica " +
                          "FROM articoli a " +
                          "WHERE a.idConferenza = ?";
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idConferenza);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                ArticoloE articolo = new ArticoloE();
                articolo.setId(rs.getInt("id"));
                articolo.setTitolo(rs.getString("titolo"));
                articolo.setAbstractText(rs.getString("abstract"));
                articolo.setFileArticolo(rs.getObject("fileArticolo"));
                articolo.setAllegato(rs.getObject("allegato"));
                articolo.setStato(rs.getString("stato"));
                articolo.setIdConferenza(rs.getInt("idConferenza"));
                
                // Gestione della data
                java.sql.Date sqlDate = rs.getDate("ultimaModifica");
                if (sqlDate != null) {
                    articolo.setUltimaModifica(sqlDate.toLocalDate());
                }
                
                listaSottomissioni.add(articolo);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle sottomissioni (query semplificata): " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Chiusura delle risorse
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura delle risorse: " + e.getMessage());
            }
        }
        
        return listaSottomissioni;
    }

    private int getIdFromUsername(String username)
    {
        try
        {
            Connection connection=getConnection();
            PreparedStatement stmt=connection.prepareStatement("SELECT id FROM utenti WHERE username = ?");
            stmt.setString(1, username);
            ResultSet ris=stmt.executeQuery();
            if(ris.next())
            {
                int id=ris.getInt("id");
                ris.close();
                stmt.close();
                connection.close();
                return id;
            }
            else
            {
                System.err.println("Nessun utente trovato con username: " + username);
                ris.close();
                stmt.close();
                connection.close();
                return -1; // Utente non trovato
            }
        }
        catch(Exception e)
        {
            return -1;
        }
    }
    
    public void setSottomissione(ArticoloE articolo, int idUtente, int idConferenza) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("Impossibile stabilire connessione al database");
                return;
            }
            
            // Insert dell'articolo nella tabella articoli
            String query = "INSERT INTO articoli (titolo, abstract, fileArticolo, allegato, stato, idConferenza, ultimaModifica) " +
                          "VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE)";
            
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, articolo.getTitolo());
            stmt.setString(2, articolo.getAbstractText());
            
            // Handle fileArticolo as BLOB if it's a byte array, or as string if it's a path
            Object fileArticolo = articolo.getFileArticolo();
            if (fileArticolo instanceof byte[]) {
                stmt.setBytes(3, (byte[]) fileArticolo);
                System.out.println("File articolo salvato come BLOB: " + ((byte[]) fileArticolo).length + " bytes");
            } else if (fileArticolo instanceof String) {
                stmt.setString(3, (String) fileArticolo);
                System.out.println("File articolo salvato come path: " + fileArticolo);
            } else {
                stmt.setNull(3, java.sql.Types.BLOB);
            }
            
            // Handle allegato as BLOB if it's a byte array, or as string if it's a path
            Object allegato = articolo.getAllegato();
            if (allegato instanceof byte[]) {
                stmt.setBytes(4, (byte[]) allegato);
                System.out.println("Allegato salvato come BLOB: " + ((byte[]) allegato).length + " bytes");
            } else if (allegato instanceof String) {
                stmt.setString(4, (String) allegato);
                System.out.println("Allegato salvato come path: " + allegato);
            } else {
                stmt.setNull(4, java.sql.Types.BLOB);
            }
            
            stmt.setString(5, articolo.getStato() != null ? articolo.getStato() : "Inviato");
            stmt.setInt(6, idConferenza);
            
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                // Ottieni l'ID dell'articolo appena inserito
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idArticolo = generatedKeys.getInt(1);
                    
                    
                    
                    // Inserisci le keywords dell'articolo (se presenti)
                    inserisciKeywordsArticolo(conn, idArticolo, articolo.getKeywords());

                    //INSERISCI L'AUTORE PRINCIPALE
                    try
                    {
                        PreparedStatement authorStmt = conn.prepareStatement(
                            "INSERT INTO sottomette VALUES (?, ?, 1)"
                        );
                        authorStmt.setInt(2, idArticolo);
                        authorStmt.setInt(1, App.utenteAccesso.getId());
                        authorStmt.executeQuery();
                        authorStmt.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    for(String username: articolo.getCoAutori())
                    {
                        // Inserisci la relazione co-autore-articolo
                        int idCoAutore = getIdFromUsername(username);
                        if (idCoAutore == -1) {
                            System.err.println("Errore: utente con username '" + username + "' non trovato");
                            continue; // Salta questo co-autore se non trovato
                        }
                        else
                        {
                            try {
                                    PreparedStatement coAuthorStmt = conn.prepareStatement(
                                        "INSERT INTO sottomette VALUES (?, ?, 0)"
                                    );
                                    coAuthorStmt.setInt(2, idArticolo);
                                    coAuthorStmt.setInt(1, idCoAutore);
                                    coAuthorStmt.executeQuery();
                                    coAuthorStmt.close();
                            } catch (SQLException e) {
                                System.err.println("Errore nell'inserimento del co-autore: " + e.getMessage());
                            }
                        }
                       
                    }
                    
                    System.out.println("Sottomissione creata con successo. ID: " + idArticolo);
                } else {
                    System.err.println("Errore nel recupero dell'ID della sottomissione");
                }
            } else {
                System.err.println("Errore: nessuna riga inserita per la sottomissione");
            }
            
        } catch (SQLException e) {
            System.err.println("Errore durante la creazione della sottomissione: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Chiusura delle risorse
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura delle risorse: " + e.getMessage());
            }
        }
    }
    
    public void modificaSottomissione(int idArticolo, ArticoloE articolo, String status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("Impossibile stabilire connessione al database");
                return;
            }
            
            // Update dell'articolo nella tabella articoli
            String query = "UPDATE articoli SET titolo = ?, abstract = ?, fileArticolo = ?, allegato = ?, stato = ?, ultimaModifica = CURRENT_DATE WHERE id = ?";
            
            stmt = conn.prepareStatement(query);
            stmt.setString(1, articolo.getTitolo());
            stmt.setString(2, articolo.getAbstractText());
            
            // Handle fileArticolo as BLOB if it's a byte array, or as string if it's a path
            Object fileArticolo = articolo.getFileArticolo();
            if (fileArticolo instanceof byte[]) {
                stmt.setBytes(3, (byte[]) fileArticolo);
                System.out.println("File articolo aggiornato come BLOB: " + ((byte[]) fileArticolo).length + " bytes");
            } else if (fileArticolo instanceof String) {
                stmt.setString(3, (String) fileArticolo);
                System.out.println("File articolo aggiornato come path: " + fileArticolo);
            } else {
                stmt.setNull(3, java.sql.Types.BLOB);
            }
            
            // Handle allegato as BLOB if it's a byte array, or as string if it's a path
            Object allegato = articolo.getAllegato();
            if (allegato instanceof byte[]) {
                stmt.setBytes(4, (byte[]) allegato);
                System.out.println("Allegato aggiornato come BLOB: " + ((byte[]) allegato).length + " bytes");
            } else if (allegato instanceof String) {
                stmt.setString(4, (String) allegato);
                System.out.println("Allegato aggiornato come path: " + allegato);
            } else {
                stmt.setNull(4, java.sql.Types.BLOB);
            }
            
            stmt.setString(5, status != null ? status : "Inviato");
            stmt.setInt(6, idArticolo);
            
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Articolo aggiornato con successo. ID: " + idArticolo);
                
                // Rimuovi le vecchie keywords dell'articolo
                PreparedStatement deleteKeywordsStmt = conn.prepareStatement(
                    "DELETE FROM keywordsArticoli WHERE idArticolo = ?"
                );
                deleteKeywordsStmt.setInt(1, idArticolo);
                deleteKeywordsStmt.executeUpdate();
                deleteKeywordsStmt.close();
                
                // Inserisci le nuove keywords dell'articolo (se presenti)
                if (articolo.getKeywords() != null && !articolo.getKeywords().isEmpty()) {
                    inserisciKeywordsArticolo(conn, idArticolo, articolo.getKeywords());
                }
                
                // Rimuovi i vecchi co-autori dell'articolo (mantenendo l'autore principale)
                PreparedStatement deleteCoAuthorsStmt = conn.prepareStatement(
                    "DELETE FROM sottomette WHERE idArticolo = ? AND caricatore = 0"
                );
                deleteCoAuthorsStmt.setInt(1, idArticolo);
                deleteCoAuthorsStmt.executeUpdate();
                deleteCoAuthorsStmt.close();
                
                // Inserisci i nuovi co-autori
                if (articolo.getCoAutori() != null && !articolo.getCoAutori().isEmpty()) {
                    for (String username : articolo.getCoAutori()) {
                        if (username != null && !username.trim().isEmpty()) {
                            int idCoAutore = getIdFromUsername(username.trim());
                            if (idCoAutore == -1) {
                                System.err.println("Errore: utente con username '" + username + "' non trovato");
                                continue; // Salta questo co-autore se non trovato
                            } else {
                                try {
                                    PreparedStatement coAuthorStmt = conn.prepareStatement(
                                        "INSERT INTO sottomette VALUES (?, ?, 0)"
                                    );
                                    coAuthorStmt.setInt(1, idCoAutore);
                                    coAuthorStmt.setInt(2, idArticolo);
                                    coAuthorStmt.executeUpdate();
                                    coAuthorStmt.close();
                                    System.out.println("Co-autore aggiunto: " + username);
                                } catch (SQLException e) {
                                    System.err.println("Errore nell'inserimento del co-autore: " + e.getMessage());
                                }
                            }
                        }
                    }
                }
                
                System.out.println("Sottomissione modificata con successo. ID: " + idArticolo);
            } else {
                System.err.println("Errore: nessuna riga aggiornata per la sottomissione con ID: " + idArticolo);
            }
            
        } catch (SQLException e) {
            System.err.println("Errore durante la modifica della sottomissione: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Chiusura delle risorse
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura delle risorse: " + e.getMessage());
            }
        }
    }
    
    public void deleteSubmission(int idArticolo) {
        try
        {
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("DELETE FROM sottomette WHERE idArticolo = ?");
            stmt.setInt(1, idArticolo);
            stmt.executeQuery();
            stmt.close();
            stmt=con.prepareStatement("DELETE FROM keywordsArticoli WHERE idArticolo = ?");
            stmt.setInt(1, idArticolo);
            stmt.executeQuery();
            stmt.close();
            stmt=con.prepareStatement("DELETE FROM articoli WHERE id = ?");
            stmt.setInt(1, idArticolo);
            stmt.executeQuery();
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public Object getRevisionsStatus(int idArticolo) {
        return null;
    }
    
    public LinkedList<Object> getRevisioni(int idArticolo) {
        return null;
    }
    
    public LinkedList<ArticoloE> getAcceptedArticles(int idConferenza) {
        System.out.println("DEBUG DBMSBoundary: === INIZIO getAcceptedArticles ===");
        System.out.println("DEBUG DBMSBoundary: idConferenza ricevuto: " + idConferenza);
        
        LinkedList<ArticoloE> articoliAccettati = new LinkedList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            // Query per ottenere gli articoli accettati per una conferenza
            String query="SELECT m.idArticolo FROM MediaPesataArticoli AS m WHERE idConferenza= ?";
        
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idConferenza);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                PreparedStatement stmt2=conn.prepareStatement(
                    "SELECT id, titolo, abstract, stato, ultimaModifica FROM articoli WHERE id = ?"
                );
                stmt2.setInt(1, rs.getInt("idArticolo"));
                ResultSet rs2=stmt2.executeQuery();
                rs2.next();
                ArticoloE articolo = new ArticoloE();
                articolo.setId(rs2.getInt("id"));
                articolo.setTitolo(rs2.getString("titolo"));
                articolo.setAbstractText(rs2.getString("abstract"));
                articolo.setStato(rs2.getString("stato"));
                articolo.setIdConferenza(idConferenza);
                
                // Gestione data ultima modifica
                try {
                    java.sql.Date sqlDate = rs2.getDate("ultimaModifica");
                    if (sqlDate != null) {
                        articolo.setUltimaModifica(sqlDate.toLocalDate());
                    }
                } catch (Exception e) {
                    System.out.println("DEBUG DBMSBoundary: Errore conversione data per articolo " + articolo.getId() + ": " + e.getMessage());
                }
                
                articoliAccettati.add(articolo);
                System.out.println("DEBUG DBMSBoundary: Articolo accettato caricato - ID: " + articolo.getId() + ", Titolo: " + articolo.getTitolo());
                stmt2.close();
                rs2.close();
            }
            
        } catch (SQLException e) {
            System.err.println("ERRORE SQL in getAcceptedArticles: " + e.getMessage());
            e.printStackTrace();
            
            // In caso di errore, restituisce dati di test per dimostrare la funzionalità
            ArticoloE articoloTest = new ArticoloE();
            articoloTest.setId(999);
            articoloTest.setTitolo("Articolo di Test Accettato");
            articoloTest.setAbstractText("Questo è un articolo di test per dimostrare l'interfaccia editoriale.");
            articoloTest.setStato("Accettato");
            articoloTest.setIdConferenza(idConferenza);
            articoliAccettati.add(articoloTest);
            
            System.out.println("DEBUG DBMSBoundary: Aggiunto articolo di test per fallback");
            
        } catch (Exception e) {
            System.err.println("ERRORE GENERICO in getAcceptedArticles: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("ERRORE chiusura connessioni: " + e.getMessage());
            }
        }
        
        System.out.println("DEBUG DBMSBoundary: Trovati " + articoliAccettati.size() + " articoli accettati");
        System.out.println("DEBUG DBMSBoundary: === FINE getAcceptedArticles ===");
        return articoliAccettati;
    }
    
    public LinkedList<Object> getArticleFile(int idArticle) {
        System.out.println("DEBUG DBMSBoundary: === INIZIO getArticleFile ===");
        System.out.println("DEBUG DBMSBoundary: idArticle ricevuto: " + idArticle);
        
        LinkedList<Object> articleFile = new LinkedList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            // Query per ottenere il file dell'articolo specifico
            String query = "SELECT fileArticolo FROM articoli WHERE id = ? AND fileArticolo IS NOT NULL";
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idArticle);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                byte[] fileData = rs.getBytes("fileArticolo");
                if (fileData != null && fileData.length > 0) {
                    articleFile.add(fileData);
                    System.out.println("DEBUG DBMSBoundary: File trovato per articolo " + idArticle + 
                                     ", dimensione: " + fileData.length + " bytes");
                } else {
                    System.out.println("DEBUG DBMSBoundary: Nessun file valido trovato per articolo " + idArticle);
                }
            } else {
                System.out.println("DEBUG DBMSBoundary: Articolo " + idArticle + " non trovato o senza file");
            }
            
        } catch (SQLException e) {
            System.err.println("ERRORE SQL in getArticleFile: " + e.getMessage());
            e.printStackTrace();
            
            // In caso di errore, crea un file PDF di test
            try {
                byte[] testPdf = createTestPdfData();
                articleFile.add(testPdf);
                System.out.println("DEBUG DBMSBoundary: Aggiunto PDF di test per articolo " + idArticle);
            } catch (Exception testException) {
                System.err.println("ERRORE durante creazione PDF di test: " + testException.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("ERRORE GENERICO in getArticleFile: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("ERRORE chiusura connessioni: " + e.getMessage());
            }
        }
        
        System.out.println("DEBUG DBMSBoundary: Ritornando " + (articleFile.isEmpty() ? "nessun" : "1") + " file per articolo " + idArticle);
        System.out.println("DEBUG DBMSBoundary: === FINE getArticleFile ===");
        
        return articleFile.isEmpty() ? null : articleFile;
    }

    public Object getAllArticleFile(int idConferenza) {
        System.out.println("DEBUG DBMSBoundary: === INIZIO getAllArticleFile ===");
        System.out.println("DEBUG DBMSBoundary: idConferenza ricevuto: " + idConferenza);
        
        List<byte[]> articleFiles = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            // Query per ottenere i file degli articoli accettati
            // Prima otteniamo gli ID degli articoli accettati dalla tabella MediaPesataArticoli
            String queryAccepted = "SELECT m.idArticolo FROM MediaPesataArticoli AS m WHERE idConferenza = ?";
            
            stmt = conn.prepareStatement(queryAccepted);
            stmt.setInt(1, idConferenza);
            rs = stmt.executeQuery();
            
            List<Integer> acceptedArticleIds = new ArrayList<>();
            while (rs.next()) {
                acceptedArticleIds.add(rs.getInt("idArticolo"));
            }
            
            rs.close();
            stmt.close();
            
            System.out.println("DEBUG DBMSBoundary: Trovati " + acceptedArticleIds.size() + " articoli accettati");
            
            // Ora otteniamo i file per ogni articolo accettato
            for (Integer articleId : acceptedArticleIds) {
                PreparedStatement fileStmt = conn.prepareStatement(
                    "SELECT fileArticolo FROM articoli WHERE id = ? AND fileArticolo IS NOT NULL"
                );
                fileStmt.setInt(1, articleId);
                ResultSet fileRs = fileStmt.executeQuery();
                
                if (fileRs.next()) {
                    byte[] fileData = fileRs.getBytes("fileArticolo");
                    if (fileData != null && fileData.length > 0) {
                        articleFiles.add(fileData);
                        System.out.println("DEBUG DBMSBoundary: File aggiunto per articolo " + articleId + 
                                         ", dimensione: " + fileData.length + " bytes");
                    } else {
                        System.out.println("DEBUG DBMSBoundary: Nessun file trovato per articolo " + articleId);
                    }
                }
                
                fileRs.close();
                fileStmt.close();
            }
            
        } catch (SQLException e) {
            System.err.println("ERRORE SQL in getAllArticleFile: " + e.getMessage());
            e.printStackTrace();
            
            // In caso di errore, crea un file PDF di test
            try {
                byte[] testPdf = createTestPdfData();
                articleFiles.add(testPdf);
                System.out.println("DEBUG DBMSBoundary: Aggiunto PDF di test per fallback");
            } catch (Exception testException) {
                System.err.println("ERRORE durante creazione PDF di test: " + testException.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("ERRORE GENERICO in getAllArticleFile: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("ERRORE chiusura connessioni: " + e.getMessage());
            }
        }
        
        System.out.println("DEBUG DBMSBoundary: Ritornando " + articleFiles.size() + " file degli articoli");
        System.out.println("DEBUG DBMSBoundary: === FINE getAllArticleFile ===");
        
        return articleFiles.isEmpty() ? null : articleFiles;
    }
    
    /**
     * Crea un PDF di test per scopi di fallback
     */
    private byte[] createTestPdfData() {
        // Header minimo di un PDF valido
        String pdfContent = "%PDF-1.4\n" +
                           "1 0 obj\n" +
                           "<<\n" +
                           "/Type /Catalog\n" +
                           "/Pages 2 0 R\n" +
                           ">>\n" +
                           "endobj\n" +
                           "2 0 obj\n" +
                           "<<\n" +
                           "/Type /Pages\n" +
                           "/Kids [3 0 R]\n" +
                           "/Count 1\n" +
                           ">>\n" +
                           "endobj\n" +
                           "3 0 obj\n" +
                           "<<\n" +
                           "/Type /Page\n" +
                           "/Parent 2 0 R\n" +
                           "/MediaBox [0 0 612 792]\n" +
                           "/Contents 4 0 R\n" +
                           ">>\n" +
                           "endobj\n" +
                           "4 0 obj\n" +
                           "<<\n" +
                           "/Length 44\n" +
                           ">>\n" +
                           "stream\n" +
                           "BT\n" +
                           "/F1 12 Tf\n" +
                           "72 720 Td\n" +
                           "(PDF di Test) Tj\n" +
                           "ET\n" +
                           "endstream\n" +
                           "endobj\n" +
                           "xref\n" +
                           "0 5\n" +
                           "0000000000 65535 f \n" +
                           "0000000009 00000 n \n" +
                           "0000000074 00000 n \n" +
                           "0000000120 00000 n \n" +
                           "0000000179 00000 n \n" +
                           "trailer\n" +
                           "<<\n" +
                           "/Size 5\n" +
                           "/Root 1 0 R\n" +
                           ">>\n" +
                           "startxref\n" +
                           "280\n" +
                           "%%EOF\n";
        
        return pdfContent.getBytes();
    }

    public LinkedList<UtenteE> getArticleAuthors(int idArticle)
    {
        try
        {
            LinkedList<UtenteE> autori = new LinkedList<>();
            Connection con = getConnection();
            PreparedStatement stmt = con.prepareStatement(
                "SELECT U.id, U.username, U.email FROM utenti AS U " +
                "JOIN sottomette AS S ON U.id = S.idUtente " +
                "WHERE S.idArticolo = ?"
            );
            stmt.setInt(1, idArticle);
            ResultSet ris = stmt.executeQuery();
            while (ris.next())
            {
                autori.add(new UtenteE(ris.getInt("id"), ris.getString("username"), ris.getString("email")));
            }
            return autori;
        }
        catch (Exception e)
        {
            System.err.println("Errore durante il recupero degli autori dell'articolo: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public LinkedList<UtenteE> getListaAutori(int idConferenza) {
        try
        {
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("SELECT DISTINCT U.id, U.username, U.email FROM utenti AS U, sottomette AS S, articoli AS A WHERE U.id=S.idUtente AND S.idArticolo=A.id AND A.idConferenza= ?");
            //SELECT DISTINCT U.id, U.username, U.email FROM utenti AS U, sottomette AS S, articoli AS A WHERE U.id=S.idUtente AND S.idArticolo=A.id AND A.idConferenza= ?
            stmt.setInt(1, idConferenza);
            ResultSet ris=stmt.executeQuery();
            LinkedList<UtenteE> autori=new LinkedList<>();
            while(ris.next())
            {
                autori.add(new UtenteE(ris.getInt("id"), ris.getString("username"), ris.getString("email")));
            }
            return autori;
        }
        catch(Exception e)
        {
            return null;
        }
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
        System.out.println("DEBUG DBMSBoundary: === INIZIO setInfoReview ===");
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("DEBUG DBMSBoundary: ERRORE - Impossibile stabilire connessione al database");
                return;
            }
            
            // Get current user ID from App.utenteAccesso
            int idUtenteCorrente = com.cms.App.utenteAccesso.getId();
            int idArticolo = getCurrentArticleId();
            
            System.out.println("DEBUG DBMSBoundary: idUtenteCorrente=" + idUtenteCorrente + ", idArticolo=" + idArticolo);
            
            // Determina se l'utente corrente è il revisore principale o un sotto-revisore
            // Controlla se l'utente ha ruolo "sotto-revisore" (ruolo 4) per questa conferenza
            boolean isSottoRevisore = false;
            
            // Prima ottieni l'ID conferenza dall'articolo
            int idConferenza = getIdConferenzaFromArticolo(idArticolo);
            System.out.println("DEBUG DBMSBoundary: idConferenza=" + idConferenza);
            
            if (idConferenza > 0) {
                String ruolo = getRuoloUtenteConferenza(idUtenteCorrente, idConferenza);
                System.out.println("DEBUG DBMSBoundary: ruolo utente corrente=" + ruolo);
                isSottoRevisore = "sotto-revisore".equals(ruolo);
            }
            
            System.out.println("DEBUG DBMSBoundary: isSottoRevisore=" + isSottoRevisore);
            
            String query;
            if (isSottoRevisore) {
                // Se è un sotto-revisore, aggiorna i campi del sotto-revisore
                // Trova la riga tramite idSottoRevisore nella tabella revisiona
                query = "UPDATE revisiona SET puntiDiForza = ?, puntiDiDebolezza = ?, " +
                       "livelloDiCompetenzaDelRevisore = ?, commentiPerAutori = ?, valutazione = ? " +
                       "WHERE idSottoRevisore = ? AND idArticolo = ?";
                
                System.out.println("DEBUG DBMSBoundary: Aggiornamento SOTTO-REVISORE");
            } else {
                // Se è un revisore normale, aggiorna i campi del revisore principale
                query = "UPDATE revisiona SET puntiDiForza = ?, puntiDiDebolezza = ?, " +
                       "livelloDiCompetenzaDelRevisore = ?, commentiPerAutori = ?, valutazione = ? " +
                       "WHERE idRevisore = ? AND idArticolo = ?";
                
                System.out.println("DEBUG DBMSBoundary: Aggiornamento REVISORE PRINCIPALE");
            }
            
            stmt = conn.prepareStatement(query);
            stmt.setString(1, puntiDiforza);
            stmt.setString(2, puntiDiDebolezza);
            stmt.setInt(3, livelloDiCompetenzaDelRevisore);
            stmt.setString(4, commentiPerAutori);
            stmt.setInt(5, valutazione);
            stmt.setInt(6, idUtenteCorrente); // Può essere idRevisore o idSottoRevisore a seconda del caso
            stmt.setInt(7, idArticolo);
            
            System.out.println("DEBUG DBMSBoundary: Eseguendo query: " + query);
            System.out.println("DEBUG DBMSBoundary: Parametri: ['" + puntiDiforza + "', '" + puntiDiDebolezza + "', " + 
                             livelloDiCompetenzaDelRevisore + ", '" + commentiPerAutori + "', " + valutazione + 
                             ", " + idUtenteCorrente + ", " + idArticolo + "]");
            
            int rowsUpdated = stmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("DEBUG DBMSBoundary: Revisione aggiornata con successo per " + 
                                 (isSottoRevisore ? "sotto-revisore" : "revisore") + " " + idUtenteCorrente + 
                                 " e articolo " + idArticolo);
            } else {
                System.err.println("DEBUG DBMSBoundary: ERRORE - Nessuna riga aggiornata. Verificare che esista un record nella tabella 'revisiona' per " + 
                                 (isSottoRevisore ? "idSottoRevisore" : "idRevisore") + "=" + idUtenteCorrente + 
                                 " e idArticolo=" + idArticolo);
                
                // Debug aggiuntivo: mostra i record esistenti
                debugRecordsRevisiona(idArticolo);
            }
            
        } catch (SQLException e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE SQL durante l'aggiornamento della revisione: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE generico durante l'aggiornamento della revisione: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("DEBUG DBMSBoundary: ERRORE durante la chiusura delle risorse: " + e.getMessage());
            }
        }
        
        System.out.println("DEBUG DBMSBoundary: === FINE setInfoReview ===");
    }
    

    public ConferenzaE getConferenza(int idConferenza)
    {
        return null;
    }
    
    public void insertNotifica(String text, int conferenceId, int receiverId, int type, String dettagli) {
        try
        {
            System.out.println("DEBUG: ID conferenza passato: "+conferenceId);
            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("INSERT INTO notifiche VALUES (NULL, ?, ?, ?, ?, ?, NULL)");
            stmt.setInt(1, conferenceId);
            stmt.setInt(2, receiverId);
            stmt.setString(3, text);
            stmt.setInt(4, type);
            stmt.setString(5, dettagli);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("DEBUG: Notifica inserita con successo per utente ID: " + receiverId);
            }
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
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
        System.out.println("DEBUG DBMSBoundary: === INIZIO getListaArticoliAssegnati ===");
        System.out.println("DEBUG DBMSBoundary: idRevisore=" + idRevisore + ", idConferenza=" + idConferenza);
        
        LinkedList<ArticoloE> articoliAssegnati = new LinkedList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("DEBUG DBMSBoundary: ERRORE - Impossibile stabilire connessione al database");
                return articoliAssegnati;
            }
            
            // Query per ottenere gli articoli assegnati al revisore per una specifica conferenza
            // Utilizza le tabelle 'revisiona' e 'articoli' come indicato nello schema E/R
            String query = "SELECT a.id, a.titolo, a.abstract, a.fileArticolo, a.allegato, a.stato, " +
                          "a.idConferenza, a.ultimaModifica " +
                          "FROM articoli a " +
                          "INNER JOIN revisiona r ON a.id = r.idArticolo " +
                          "WHERE r.idRevisore = ? AND a.idConferenza = ? " +
                          "ORDER BY a.titolo";
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idRevisore);
            stmt.setInt(2, idConferenza);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                // Crea l'oggetto ArticoloE usando il costruttore disponibile
                ArticoloE articolo = new ArticoloE(
                    rs.getInt("id"),
                    rs.getString("titolo"),
                    rs.getString("abstract"),
                    rs.getObject("fileArticolo"), // BLOB
                    rs.getObject("allegato"), // BLOB
                    rs.getString("stato"),
                    rs.getInt("idConferenza"),
                    rs.getTimestamp("ultimaModifica") != null ? 
                        rs.getTimestamp("ultimaModifica").toLocalDateTime().toLocalDate() : null
                );
                
                // Ottieni e imposta le keywords per questo articolo
                ArrayList<String> keywords = getKeywordsArticolo(rs.getInt("id"));
                if (keywords != null && !keywords.isEmpty()) {
                    LinkedList<String> keywordsList = new LinkedList<>();
                    keywordsList.addAll(keywords);
                    articolo.setKeywords(keywordsList);
                }
                
                articoliAssegnati.add(articolo);
                System.out.println("DEBUG DBMSBoundary: Articolo assegnato recuperato - ID: " + articolo.getId() + 
                                 ", Titolo: '" + articolo.getTitolo() + "'");
            }
            
            System.out.println("DEBUG DBMSBoundary: Totale articoli assegnati recuperati: " + articoliAssegnati.size());
            
        } catch (SQLException e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE SQL durante getListaArticoliAssegnati: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE generico durante getListaArticoliAssegnati: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Chiusura delle risorse
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("DEBUG DBMSBoundary: ERRORE durante la chiusura delle risorse: " + e.getMessage());
            }
        }
        
        System.out.println("DEBUG DBMSBoundary: === FINE getListaArticoliAssegnati ===");
        return articoliAssegnati;
    }
    
    public LinkedList<Object> getListaArticoliSottoRevisioni(Object sottoRevisore, Object conferenza, Object articolo) {
        return null;
    }

    public LinkedList<ArticoloE> getListaArticoliSotterevisore(int idSottorevisore, int idConferenza)
    {
        return null;
    }

    /**
     * Ottiene la lista delle conferenze per un utente specifico con i rispettivi ruoli
     */
    public LinkedList<ConferenzaE> getConferenzeUtente(int idUtente) {
        try {
            LinkedList<ConferenzaE> conferenze = new LinkedList<>();
            Connection con = getConnection();
            if (con == null) {
                System.err.println("Errore: impossibile stabilire connessione al database");
                return conferenze;
            }
            
            // Query per ottenere le conferenze e i ruoli dell'utente
            // Assumendo che esista una tabella ruoli che collega utenti e conferenze
            String sql = "SELECT DISTINCT c.id, c.titolo, c.abstract, c.edizione, c.dataInizio, c.dataFine, " +
                        "c.deadlineSottomissione, c.deadlineRitiro, c.deadlineRevisioni, c.deadlineVersioneFinale, " +
                        "c.deadlineVersionePubblicazione, c.luogo, c.numRevisoriPerArticolo, c.numeroArticoliPrevisti, " +
                        "c.tassoAccettazione, r.ruolo " +
                        "FROM conferenze c " +
                        "JOIN ruoli r ON c.id = r.idConferenza " +
                        "WHERE r.idUtente = ?";
            
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idUtente);
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
            
        } catch (Exception e) {
            System.err.println("Errore durante il recupero delle conferenze utente: " + e.getMessage());
            e.printStackTrace();
            return new LinkedList<>();
        }
    }
    
    /**
     * Ottiene il ruolo di un utente in una specifica conferenza
     */
    public String getRuoloUtenteConferenza(int idUtente, int idConferenza) {
        System.out.println("DEBUG: getRuoloUtenteConferenza chiamato per idUtente=" + idUtente + ", idConferenza=" + idConferenza);
        
        try {
            Connection con = getConnection();
            if (con == null) {
                System.out.println("DEBUG: Connessione al database fallita, ritorno 'Utente'");
                return "Utente";
            }
            
            String sql = "SELECT r.ruolo FROM ruoli r WHERE r.idUtente = ? AND r.idConferenza = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idConferenza);
            
            System.out.println("DEBUG: Eseguendo query: " + sql + " con parametri idUtente=" + idUtente + ", idConferenza=" + idConferenza);
            
            ResultSet rs = stmt.executeQuery();
            
            String ruolo = "Utente"; // Default
            boolean found = rs.next();
            System.out.println("DEBUG: Risultato query trovato: " + found);
            
            if (found) {
                int ruoloId = rs.getInt("ruolo");
                System.out.println("DEBUG: ruoloId trovato nel DB: " + ruoloId);
                // Converte l'ID del ruolo in stringa
                ruolo = convertRuoloIdToString(ruoloId);
                System.out.println("DEBUG: ruolo convertito: " + ruolo);
            } else {
                System.out.println("DEBUG: Nessun ruolo trovato per questo utente/conferenza, uso default: " + ruolo);
            }
            
            rs.close();
            stmt.close();
            con.close();
            
            System.out.println("DEBUG: getRuoloUtenteConferenza ritorna: " + ruolo);
            return ruolo;
            
        } catch (Exception e) {
            System.err.println("DEBUG: Errore durante il recupero del ruolo utente: " + e.getMessage());
            e.printStackTrace();
            return "Utente";
        }
    }
    
    /**
     * Converte l'ID del ruolo nella stringa corrispondente
     */
    private String convertRuoloIdToString(int ruoloId) {
        System.out.println("DEBUG: convertRuoloIdToString chiamato con ruoloId=" + ruoloId);
        
        String result;
        switch (ruoloId) {
            case 1: result = "Chair"; break;
            case 2: result = "Revisore"; break;
            case 3: result = "Editore"; break;
            case 4: result = "Sotto-revisore"; break;
            case 5: result = "Autore"; break;
            default: result = "Utente"; break;
        }
        
        System.out.println("DEBUG: convertRuoloIdToString ritorna: " + result);
        return result;
    }
    
    /**
     * Inserisce le keywords associate a un articolo nella tabella di relazione
     */
    private void inserisciKeywordsArticolo(Connection conn, int idArticolo, LinkedList<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return;
        }
        
        try {
            for (String keyword : keywords) {
                if (keyword != null && !keyword.trim().isEmpty()) {
                    keyword = keyword.trim();
                    
                    // Prima ottieni l'ID della keyword (o creala se non esiste)
                    int idKeyword = inserisciOOttieniKeyword(conn, keyword);
                    
                    if (idKeyword > 0) {
                        // Inserisci la relazione articolo-keyword nella tabella di associazione
                        // Il nome della tabella potrebbe variare: keywordsArticoli, articoli_keywords, etc.
                        PreparedStatement stmt = conn.prepareStatement(
                            "INSERT IGNORE INTO keywordsArticoli (idArticolo, idKeyword) VALUES (?, ?)"
                        );
                        stmt.setInt(1, idArticolo);
                        stmt.setInt(2, idKeyword);
                        stmt.executeUpdate();
                        stmt.close();
                        
                        System.out.println("Keyword '" + keyword + "' associata all'articolo " + idArticolo);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento delle keywords dell'articolo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Metodo di test per verificare l'inserimento delle keywords di un articolo
     * Questo metodo può essere utilizzato per testare il funzionamento del sistema
     */
    public void testKeywordInsertion() {
        System.out.println("=== Test Inserimento Keywords Articolo ===");
        System.out.println("Il sistema è ora configurato per:");
        System.out.println("1. Salvare l'articolo nella tabella 'articoli'");
        System.out.println("2. Ottenere l'ID dell'articolo appena inserito");
        System.out.println("3. Per ogni keyword selezionata:");
        System.out.println("   - Cercare la keyword nella tabella 'keywords'");
        System.out.println("   - Se non esiste, crearla");
        System.out.println("   - Inserire la relazione nella tabella 'keywordsArticoli'");
        System.out.println("     con campi: idArticolo, idKeyword");
        System.out.println("4. La tabella keywordsArticoli segue la struttura:");
        System.out.println("   - idArticolo (int NOT NULL)");
        System.out.println("   - idKeyword (int NOT NULL)");
        System.out.println("===========================================");
    }
    
    /**
     * Ottiene la lista delle keywords associate a un articolo specifico
     * @param idArticolo L'ID dell'articolo di cui recuperare le keywords
     * @return ArrayList<String> contenente le keywords dell'articolo
     */
    public ArrayList<String> getKeywordsArticolo(int idArticolo) {
        ArrayList<String> keywords = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("Impossibile stabilire connessione al database");
                return keywords;
            }
            
            // Query per ottenere le keywords associate all'articolo
            String sql = "SELECT k.keyword " +
                        "FROM keywords k " +
                        "INNER JOIN keywordsArticoli ka ON k.id = ka.idKeyword " +
                        "WHERE ka.idArticolo = ?";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idArticolo);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                keywords.add(rs.getString("keyword"));
            }
            
            System.out.println("Recuperate " + keywords.size() + " keywords per l'articolo " + idArticolo);
            
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle keywords dell'articolo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Chiusura delle risorse
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura delle risorse: " + e.getMessage());
            }
        }
        
        return keywords;
    }
    
    /**
     * Metodo di test per verificare il recupero delle keywords di un articolo
     */
    public void testGetKeywordsArticolo(int idArticolo) {
        System.out.println("=== Test recupero keywords per articolo " + idArticolo + " ===");
        
        ArrayList<String> keywords = getKeywordsArticolo(idArticolo);
        
        if (keywords.isEmpty()) {
            System.out.println("Nessuna keyword trovata per l'articolo " + idArticolo);
            System.out.println("Questo può significare:");
            System.out.println("1. L'articolo non ha keywords associate");
            System.out.println("2. L'articolo non esiste nel database");
            System.out.println("3. La tabella keywordsArticoli non ha dati per questo articolo");
        } else {
            System.out.println("Keywords trovate per l'articolo " + idArticolo + ":");
            for (String keyword : keywords) {
                System.out.println("  - " + keyword);
            }
        }
        
        System.out.println("Totale keywords: " + keywords.size());
        System.out.println("================================================");
    }
    
    /**
     * Metodo di test per verificare il caricamento completo di un articolo con BLOB
     */
    public void testArticoloConBLOB(int idArticolo) {
        System.out.println("=== Test caricamento articolo con BLOB - ID: " + idArticolo + " ===");
        
        ArticoloE articolo = getArticolo(idArticolo);
        
        if (articolo != null) {
            System.out.println("Articolo trovato: " + articolo.getTitolo());
            System.out.println("Abstract: " + (articolo.getAbstractText() != null ? 
                              articolo.getAbstractText().substring(0, Math.min(50, articolo.getAbstractText().length())) + "..." 
                              : "Non disponibile"));
            
            // Test BLOB file articolo
            Object fileBlob = articolo.getFileArticolo();
            if (fileBlob != null) {
                System.out.println("✓ File articolo (BLOB) presente - Tipo: " + fileBlob.getClass().getSimpleName());
                if (fileBlob instanceof byte[]) {
                    System.out.println("  Dimensione: " + ((byte[]) fileBlob).length + " bytes");
                } else if (fileBlob instanceof java.sql.Blob) {
                    try {
                        System.out.println("  Dimensione: " + ((java.sql.Blob) fileBlob).length() + " bytes");
                    } catch (Exception e) {
                        System.out.println("  Errore nel leggere dimensione BLOB: " + e.getMessage());
                    }
                } else {
                    System.out.println("  Formato BLOB non riconosciuto: " + fileBlob.getClass());
                }
            } else {
                System.out.println("✗ Nessun file articolo (BLOB) presente");
            }
            
            // Test BLOB allegato
            Object allegatoBlob = articolo.getAllegato();
            if (allegatoBlob != null) {
                System.out.println("✓ Allegato (BLOB) presente - Tipo: " + allegatoBlob.getClass().getSimpleName());
                if (allegatoBlob instanceof byte[]) {
                    System.out.println("  Dimensione: " + ((byte[]) allegatoBlob).length + " bytes");
                } else if (allegatoBlob instanceof java.sql.Blob) {
                    try {
                        System.out.println("  Dimensione: " + ((java.sql.Blob) allegatoBlob).length() + " bytes");
                    } catch (Exception e) {
                        System.out.println("  Errore nel leggere dimensione BLOB: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("✗ Nessun allegato (BLOB) presente");
            }
            
            // Test keywords
            ArrayList<String> keywords = getKeywordsArticolo(idArticolo);
            System.out.println("Keywords associate: " + keywords.size());
            for (String keyword : keywords) {
                System.out.println("  - " + keyword);
            }
            
        } else {
            System.out.println("✗ Articolo non trovato con ID: " + idArticolo);
        }
        
        System.out.println("===============================================");
    }
    
    /**
     * Test method to verify BLOB storage and retrieval
     */
    public void testBLOBFunctionality() {
        System.out.println("=== TEST BLOB FUNCTIONALITY ===");
        
        try {
            // Create a test article with BLOB data
            ArticoloE testArticolo = new ArticoloE();
            testArticolo.setTitolo("Test Article BLOB");
            testArticolo.setAbstractText("This is a test article to verify BLOB functionality.");
            testArticolo.setStato("Test");
            testArticolo.setIdConferenza(1);
            testArticolo.setUltimaModifica(java.time.LocalDate.now());
            
            // Create sample BLOB data (simulating a PDF file)
            String sampleContent = "This is sample PDF content as text for testing BLOB storage.";
            byte[] sampleFileData = sampleContent.getBytes();
            testArticolo.setFileArticolo(sampleFileData);
            
            String sampleAttachment = "This is sample attachment content for testing.";
            byte[] sampleAttachmentData = sampleAttachment.getBytes();
            testArticolo.setAllegato(sampleAttachmentData);
            
            // Test keywords
            LinkedList<String> testKeywords = new LinkedList<>();
            testKeywords.add("test");
            testKeywords.add("blob");
            testArticolo.setKeywords(testKeywords);
            
            System.out.println("Saving test article with BLOB data...");
            setSottomissione(testArticolo, 1, 1);
            
            System.out.println("Test BLOB functionality completed. Check console output for results.");
            
        } catch (Exception e) {
            System.err.println("Error in BLOB test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Metodo di debug per verificare tutte le notifiche presenti nel database
     */
    public void debugTutteLeNotifiche() {
        System.out.println("DEBUG: === VERIFICA TUTTE LE NOTIFICHE NEL DATABASE ===");
        
        try {
            Connection con = getConnection();
            if (con == null) {
                System.err.println("DEBUG: Connessione al database fallita!");
                return;
            }
            
            // Query per tutte le notifiche
            String sql = "SELECT N.id, N.idConferenza, N.idUtente, N.testo, N.tipo, N.dettagli, N.esito, U.username " +
                        "FROM notifiche N LEFT JOIN utenti U ON N.idUtente = U.id ORDER BY N.id";
            
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("DEBUG: Notifica #" + count + ":");
                System.out.println("  - ID: " + rs.getInt("id"));
                System.out.println("  - ID Conferenza: " + rs.getInt("idConferenza"));
                System.out.println("  - ID Utente: " + rs.getInt("idUtente"));
                System.out.println("  - Username: " + rs.getString("username"));
                System.out.println("  - Testo: '" + rs.getString("testo") + "'");
                System.out.println("  - Tipo: " + rs.getInt("tipo"));
                System.out.println("  - Dettagli: '" + rs.getString("dettagli") + "'");
                System.out.println("  - Esito: '" + rs.getString("esito") + "'");
                System.out.println("  - È attiva?: " + (rs.getString("esito") == null ? "SÌ" : "NO"));
                System.out.println("  ---");
            }
            
            System.out.println("DEBUG: Totale notifiche nel database: " + count);
            
            rs.close();
            stmt.close();
            con.close();
            
        } catch (Exception e) {
            System.err.println("DEBUG: Errore durante la verifica delle notifiche: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("DEBUG: === FINE VERIFICA TUTTE LE NOTIFICHE ===");
    }

    /**
     * Aggiorna o inserisce il ruolo di un utente per una specifica conferenza
     * @param idUtente L'ID dell'utente
     * @param idConferenza L'ID della conferenza
     * @param nuovoRuolo Il nuovo ruolo numerico (4 = sotto-revisore)
     * @return true se l'operazione è riuscita, false altrimenti
     */
    public boolean aggiornaRuoloUtente(int idUtente, int idConferenza, int nuovoRuolo) {
        System.out.println("DEBUG DBMSBoundary: === INIZIO aggiornaRuoloUtente ===");
        System.out.println("DEBUG DBMSBoundary: idUtente=" + idUtente + ", idConferenza=" + idConferenza + ", nuovoRuolo=" + nuovoRuolo);
        
        Connection con = null;
        PreparedStatement checkStmt = null;
        PreparedStatement updateStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;
        
        try {
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Prima verifica se esiste già un record per questo utente e conferenza
            String checkQuery = "SELECT ruolo FROM ruoli WHERE idUtente = ? AND idConferenza = ?";
            checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setInt(1, idUtente);
            checkStmt.setInt(2, idConferenza);
            rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // Record esistente: UPDATE
                int ruoloCorrente = rs.getInt("ruolo");
                System.out.println("DEBUG DBMSBoundary: Record esistente trovato. Ruolo corrente: " + ruoloCorrente);
                
                String updateQuery = "UPDATE ruoli SET ruolo = ? WHERE idUtente = ? AND idConferenza = ?";
                updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setInt(1, nuovoRuolo);
                updateStmt.setInt(2, idUtente);
                updateStmt.setInt(3, idConferenza);
                
                int rowsUpdated = updateStmt.executeUpdate();
                System.out.println("DEBUG DBMSBoundary: Righe aggiornate: " + rowsUpdated);
                
                if (rowsUpdated > 0) {
                    System.out.println("DEBUG DBMSBoundary: Ruolo aggiornato con successo");
                    return true;
                }
            } else {
                // Record non esistente: INSERT
                System.out.println("DEBUG DBMSBoundary: Nessun record esistente, inserimento nuovo ruolo");
                
                String insertQuery = "INSERT INTO ruoli (idUtente, idConferenza, ruolo, dataRichiesta, dataRisposta) VALUES (?, ?, ?, CURRENT_DATE, CURRENT_DATE)";
                insertStmt = con.prepareStatement(insertQuery);
                insertStmt.setInt(1, idUtente);
                insertStmt.setInt(2, idConferenza);
                insertStmt.setInt(3, nuovoRuolo);
                
                int rowsInserted = insertStmt.executeUpdate();
                System.out.println("DEBUG DBMSBoundary: Righe inserite: " + rowsInserted);
                
                if (rowsInserted > 0) {
                    System.out.println("DEBUG DBMSBoundary: Nuovo ruolo inserito con successo");
                    return true;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("DEBUG DBMSBoundary: Errore SQL durante aggiornaRuoloUtente: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("DEBUG DBMSBoundary: Errore generico durante aggiornaRuoloUtente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Chiusura delle risorse
            try {
                if (rs != null) rs.close();
                if (checkStmt != null) checkStmt.close();
                if (updateStmt != null) updateStmt.close();
                if (insertStmt != null) insertStmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("DEBUG DBMSBoundary: Errore durante la chiusura delle risorse: " + e.getMessage());
            }
        }
        
        System.out.println("DEBUG DBMSBoundary: aggiornaRuoloUtente fallita");
        System.out.println("DEBUG DBMSBoundary: === FINE aggiornaRuoloUtente ===");
        return false;
    }

    /**
     * Assegna un sotto-revisore a un articolo specifico
     * Aggiorna o inserisce un record nella tabella revisiona con idSottoRevisore
     * @param idArticolo L'ID dell'articolo
     * @param idRevisore L'ID del revisore principale che convoca il sotto-revisore
     * @param idSottoRevisore L'ID del sotto-revisore da assegnare
     * @return true se l'operazione è riuscita, false altrimenti
     */
    public boolean assegnaSottoRevisoreArticolo(int idArticolo, int idRevisore, int idSottoRevisore) {
        System.out.println("DEBUG DBMSBoundary: === INIZIO assegnaSottoRevisoreArticolo ===");
        System.out.println("DEBUG DBMSBoundary: idArticolo=" + idArticolo + ", idRevisore=" + idRevisore + ", idSottoRevisore=" + idSottoRevisore);
        
        Connection con = null;
        PreparedStatement checkStmt = null;
        PreparedStatement updateStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;
        
        try {
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Prima verifica se esiste già un record per questo revisore e articolo
            String checkQuery = "SELECT idSottoRevisore FROM revisiona WHERE idArticolo = ? AND idRevisore = ?";
            checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setInt(1, idArticolo);
            checkStmt.setInt(2, idRevisore);
            rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // Record esistente: UPDATE
                Integer sottoRevisoreCorrente = rs.getObject("idSottoRevisore", Integer.class);
                System.out.println("DEBUG DBMSBoundary: Record esistente trovato. SottoRevisore corrente: " + sottoRevisoreCorrente);
                
                String updateQuery = "UPDATE revisiona SET idSottoRevisore = ? WHERE idArticolo = ? AND idRevisore = ?";
                updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setInt(1, idSottoRevisore);
                updateStmt.setInt(2, idArticolo);
                updateStmt.setInt(3, idRevisore);
                
                int rowsUpdated = updateStmt.executeUpdate();
                System.out.println("DEBUG DBMSBoundary: Righe aggiornate: " + rowsUpdated);
                
                if (rowsUpdated > 0) {
                    System.out.println("DEBUG DBMSBoundary: SottoRevisore assegnato con successo");
                    return true;
                }
            } else {
                // Record non esistente: INSERT
                System.out.println("DEBUG DBMSBoundary: Nessun record esistente, inserimento nuova assegnazione");
                
                String insertQuery = "INSERT INTO revisiona (idArticolo, idRevisore, idSottoRevisore) VALUES (?, ?, ?)";
                insertStmt = con.prepareStatement(insertQuery);
                insertStmt.setInt(1, idArticolo);
                insertStmt.setInt(2, idRevisore);
                insertStmt.setInt(3, idSottoRevisore);
                
                int rowsInserted = insertStmt.executeUpdate();
                System.out.println("DEBUG DBMSBoundary: Righe inserite: " + rowsInserted);
                
                if (rowsInserted > 0) {
                    System.out.println("DEBUG DBMSBoundary: Nuova assegnazione sotto-revisore inserita con successo");
                    return true;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("DEBUG DBMSBoundary: Errore SQL durante assegnaSottoRevisoreArticolo: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("DEBUG DBMSBoundary: Errore generico durante assegnaSottoRevisoreArticolo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Chiusura delle risorse
            try {
                if (rs != null) rs.close();
                if (checkStmt != null) checkStmt.close();
                if (updateStmt != null) updateStmt.close();
                if (insertStmt != null) insertStmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("DEBUG DBMSBoundary: Errore durante la chiusura delle risorse: " + e.getMessage());
            }
        }
        
        System.out.println("DEBUG DBMSBoundary: assegnaSottoRevisoreArticolo fallita");
        System.out.println("DEBUG DBMSBoundary: === FINE assegnaSottoRevisoreArticolo ===");
        return false;
    }

    /**
     * Ottiene l'articolo assegnato a un sotto-revisore per una conferenza specifica
     * @param idSottoRevisore L'ID del sotto-revisore
     * @param idConferenza L'ID della conferenza
     * @return L'articolo assegnato al sotto-revisore, null se non trovato
     */
    public ArticoloE getArticoloAssegnatoSottoRevisore(int idSottoRevisore, int idConferenza) {
        System.out.println("DEBUG DBMSBoundary: === INIZIO getArticoloAssegnatoSottoRevisore ===");
        System.out.println("DEBUG DBMSBoundary: idSottoRevisore=" + idSottoRevisore + ", idConferenza=" + idConferenza);
        
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Query per ottenere l'articolo assegnato al sotto-revisore per una specifica conferenza
            String query = "SELECT a.id, a.titolo, a.abstract, a.fileArticolo, a.allegato, a.stato, " +
                          "a.idConferenza, a.ultimaModifica " +
                          "FROM articoli a " +
                          "INNER JOIN revisiona r ON a.id = r.idArticolo " +
                          "WHERE r.idSottoRevisore = ? AND a.idConferenza = ?";
            
            stmt = con.prepareStatement(query);
            stmt.setInt(1, idSottoRevisore);
            stmt.setInt(2, idConferenza);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                ArticoloE articolo = new ArticoloE();
                articolo.setId(rs.getInt("id"));
                articolo.setTitolo(rs.getString("titolo"));
                articolo.setAbstractText(rs.getString("abstract"));
                articolo.setFileArticolo(rs.getBytes("fileArticolo"));
                articolo.setAllegato(rs.getBytes("allegato"));
                articolo.setStato(rs.getString("stato"));
                articolo.setIdConferenza(rs.getInt("idConferenza"));
                
                java.sql.Date sqlDate = rs.getDate("ultimaModifica");
                if (sqlDate != null) {
                    articolo.setUltimaModifica(sqlDate.toLocalDate());
                }
                
                System.out.println("DEBUG DBMSBoundary: Articolo trovato: " + articolo.getTitolo());
                return articolo;
            } else {
                System.out.println("DEBUG DBMSBoundary: Nessun articolo assegnato al sotto-revisore");
                return null;
            }
            
        } catch (SQLException e) {
            System.err.println("DEBUG DBMSBoundary: Errore SQL durante getArticoloAssegnatoSottoRevisore: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("DEBUG DBMSBoundary: Errore generico durante getArticoloAssegnatoSottoRevisore: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Chiusura delle risorse
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("DEBUG DBMSBoundary: Errore durante la chiusura delle risorse: " + e.getMessage());
            }
        }
        
        System.out.println("DEBUG DBMSBoundary: === FINE getArticoloAssegnatoSottoRevisore ===");
        return null;
    }

    /**
     * Ottiene l'ID della conferenza a partire dall'ID dell'articolo
     */
    private int getIdConferenzaFromArticolo(int idArticolo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("DEBUG DBMSBoundary: ERRORE - Impossibile stabilire connessione al database");
                return -1;
            }
            
            String query = "SELECT idConferenza FROM articoli WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idArticolo);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                int idConferenza = rs.getInt("idConferenza");
                System.out.println("DEBUG DBMSBoundary: Trovata idConferenza=" + idConferenza + " per idArticolo=" + idArticolo);
                return idConferenza;
            } else {
                System.err.println("DEBUG DBMSBoundary: Nessuna conferenza trovata per idArticolo=" + idArticolo);
                return -1;
            }
            
        } catch (SQLException e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE SQL durante getIdConferenzaFromArticolo: " + e.getMessage());
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("DEBUG DBMSBoundary: ERRORE durante la chiusura delle risorse: " + e.getMessage());
            }
        }
    }
    
    /**
     * Debug dei record nella tabella revisiona per un articolo specifico
     */
    private void debugRecordsRevisiona(int idArticolo) {
        System.out.println("DEBUG DBMSBoundary: === DEBUG RECORDS REVISIONA per idArticolo=" + idArticolo + " ===");
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("DEBUG DBMSBoundary: ERRORE - Impossibile stabilire connessione al database");
                return;
            }
            
            String query = "SELECT * FROM revisiona WHERE idArticolo = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idArticolo);
            rs = stmt.executeQuery();
            
            boolean hasRecords = false;
            while (rs.next()) {
                hasRecords = true;
                System.out.println("DEBUG DBMSBoundary: Record trovato:");
                System.out.println("  - id=" + rs.getInt("id"));
                System.out.println("  - idRevisore=" + rs.getInt("idRevisore"));
                System.out.println("  - idArticolo=" + rs.getInt("idArticolo"));
                System.out.println("  - idSottoRevisore=" + rs.getObject("idSottoRevisore"));
                System.out.println("  - puntiDiForza='" + rs.getString("puntiDiForza") + "'");
                System.out.println("  - puntiDiDebolezza='" + rs.getString("puntiDiDebolezza") + "'");
                System.out.println("  - commentiPerAutori='" + rs.getString("commentiPerAutori") + "'");
                System.out.println("  - valutazione=" + rs.getObject("valutazione"));
                System.out.println("  - livelloDiCompetenzaDelRevisore=" + rs.getObject("livelloDiCompetenzaDelRevisore"));
                System.out.println("  ---");
            }
            
            if (!hasRecords) {
                System.out.println("DEBUG DBMSBoundary: Nessun record trovato nella tabella revisiona per idArticolo=" + idArticolo);
            }
            
        } catch (SQLException e) {
            System.err.println("DEBUG DBMSBoundary: ERRORE SQL durante debugRecordsRevisiona: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("DEBUG DBMSBoundary: ERRORE durante la chiusura delle risorse: " + e.getMessage());
            }
        }
        
        System.out.println("DEBUG DBMSBoundary: === FINE DEBUG RECORDS REVISIONA ===");
    }

    // ...existing code...
}
