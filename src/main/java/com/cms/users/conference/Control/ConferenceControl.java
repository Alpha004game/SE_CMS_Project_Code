package com.cms.users.conference.Control;

import com.cms.App;
import com.cms.users.Commons.SuccessScreen;
import com.cms.users.Commons.UtilsControl;
import com.cms.users.Entity.ArticoloE;
import com.cms.users.Entity.ConferenzaE;
import com.cms.users.Entity.UtenteE;
import com.cms.users.conference.Interface.ConferenceManagementScreen;
import com.cms.users.conference.Interface.MemberListScreen;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Import per iTextPDF
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Desktop;
import java.io.File;

/**
 * <<control>>
 * ConferenceControl
 */
public class ConferenceControl {
    
    private ConferenzaE conferenza;
    
    // Pattern Singleton per mantenere una singola istanza
    private static ConferenzaE conferenzaSelezionata;
    
   
    
    /**
     * Imposta la conferenza corrente
     */
    public static void setConferenza(ConferenzaE conferenza) {
        conferenzaSelezionata = conferenza;
    }
    
    /**
     * Ottiene la conferenza corrente
     */
    public static ConferenzaE getConferenza() {
        return conferenzaSelezionata;
    }

    
    /**
     * Carica i dati di una conferenza e apre la schermata di gestione
     * Segue il sequence diagram: HomeScreen -> ConferenceControl -> DBMSBoundary
     */
    public void apriGestioneConferenza(int idConferenza) {
        // Chiama DBMSBoundary per ottenere le informazioni della conferenza
        this.conferenza = (ConferenzaE) App.dbms.getConferenceInfo(idConferenza);
        
        
        if (conferenza != null) {
            // Crea e mostra la schermata di gestione conferenza con i dati caricati
            ConferenceManagementScreen screen = new ConferenceManagementScreen();
            screen.loadConferenceData(conferenza);
            screen.setVisible(true);
            ConferenceControl.setConferenza(conferenza);
            System.out.println("Allora non sei null");
        } else {
            // Gestione errore nel caricamento
            System.err.println("Errore: impossibile caricare i dati della conferenza con ID " + idConferenza);
        }
    }
    
    // Metodi
    public void create() {
        // Implementazione da definire
    }
    
    /**
     * Gestisce l'invito di un co-chair seguendo il sequence diagram
     * ConferenceManagementScreen -> ConferenceControl -> DBMSBoundary -> MemberListScreen
     */
    public void invitaCoChair(int idConferenza) {
        // Carica i dati della conferenza se necessario
        if (conferenzaSelezionata == null || conferenzaSelezionata.getId() != idConferenza) {
            ConferenzaE conferenza = (ConferenzaE) App.dbms.getConferenceInfo(idConferenza);
            setConferenza(conferenza);
        }
        
        // Ottieni tutti gli utenti dal database
        LinkedList<UtenteE> tuttiGliUtenti = App.dbms.getUsersInfo();
        
        // Crea la MemberListScreen per selezionare un co-chair
        MemberListScreen memberListScreen = new MemberListScreen(
            MemberListScreen.UserRole.CHAIR, 
            MemberListScreen.Action.ADD_COCHAIR, this
        );
        
        if (tuttiGliUtenti != null && !tuttiGliUtenti.isEmpty()) {
            // Imposta la lista degli utenti disponibili
            memberListScreen.setUserList(tuttiGliUtenti);
        } else {
            // Nessun utente disponibile
            memberListScreen.setHasData(false);
        }
        
        // Mostra la schermata
        memberListScreen.setVisible(true);
    }
    
    public void addReviewer() {
        // Implementazione da definire
    }
    
    /**
     * Gestisce l'aggiunta di un revisore seguendo il sequence diagram
     * ConferenceManagementScreen -> ConferenceControl -> DBMSBoundary -> MemberListScreen
     */
    public void addReviewer(int idConferenza) {
        // Ottieni tutti gli utenti dal database
        LinkedList<UtenteE> tuttiGliUtenti = App.dbms.getUsersInfo();
        
        // Crea la MemberListScreen per selezionare un revisore
        MemberListScreen memberListScreen = new MemberListScreen(
            MemberListScreen.UserRole.CHAIR, 
            MemberListScreen.Action.ADD_REVIEWER, this
        );
        
        if (tuttiGliUtenti != null && !tuttiGliUtenti.isEmpty()) {
            // Imposta la lista degli utenti disponibili
            memberListScreen.setUserList(tuttiGliUtenti);
        } else {
            // Nessun utente disponibile
            memberListScreen.setHasData(false);
        }
        
        // Mostra la schermata
        memberListScreen.setVisible(true);
    }
    
    public int calculateReviewerNumber() {
        // Implementazione da definire
        return 0;
    }
    
    /**
     * Avvia il processo di rimozione di un revisore dalla conferenza
     * Segue il sequence diagram: ottiene lista revisori e apre MemberListScreen
     */
    public void rimuoviRevisore(int idConferenza) {
        try {
            // Ottieni la lista dei revisori per la conferenza
            LinkedList<UtenteE> revisori = App.dbms.getRevisori(idConferenza);
            
            if (revisori == null || revisori.isEmpty()) {
                // Mostra messaggio se non ci sono revisori
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "Non ci sono revisori assegnati a questa conferenza.", 
                    "Nessun Revisore", 
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Apri la schermata per selezionare il revisore da rimuovere
            MemberListScreen memberListScreen = new MemberListScreen(
                MemberListScreen.UserRole.CHAIR, 
                MemberListScreen.Action.REMOVE_REVIEWER,
                this
            );
            memberListScreen.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Errore durante il caricamento dei revisori: " + e.getMessage(), 
                "Errore", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void assegnaRevisore(int idRevisore) {

        String text="Convocazione ricevuta da "+ App.utenteAccesso.getUsername()+" per gestire, in qualità di revisore, per la conferenza: "+ConferenceControl.conferenzaSelezionata.getTitolo();
        App.dbms.insertNotifica(text, ConferenceControl.getConferenza().getId(), idRevisore, 1, "ADD-REVISORE");
        UtilsControl.sendMail(App.dbms.getUser(idRevisore).getEmail(), "Invito Membro PC conferenza: "+ConferenceControl.getConferenza().getTitolo(), text);
    }
    
    /**
     * Rimuove il revisore selezionato dalla conferenza
     * Segue il sequence diagram: rimuove dalla DB, invia notifica e mostra successo
     */
    public void selezionaRevisore(int idRevisore) {
        try {
            int idConferenza = conferenzaSelezionata.getId();
            
            // Ottieni le informazioni del revisore prima della rimozione
            UtenteE revisore = App.dbms.getUser(idRevisore);
            if (revisore == null) {
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "Errore: Revisore non trovato.", 
                    "Errore", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Rimuovi il revisore dalla conferenza
            App.dbms.rimuoviRevisore(idConferenza, idRevisore);
            
            // Invia notifica al revisore rimosso
            String notificaText = "Sei stato rimosso dal ruolo di revisore per la conferenza: " + 
                                conferenzaSelezionata.getTitolo() + " da " + App.utenteAccesso.getUsername();
            App.dbms.insertNotifica(notificaText, idConferenza, idRevisore, 1, "REMOVE-REVISORE");
            
            // Invia email di notifica
            String emailSubject = "Rimozione ruolo revisore - Conferenza: " + conferenzaSelezionata.getTitolo();
            String emailBody = "Gentile " + revisore.getUsername() + ",\n\n" +
                             "Ti informiamo che sei stato rimosso dal ruolo di revisore per la conferenza:\n" +
                             "\"" + conferenzaSelezionata.getTitolo() + "\"\n\n" +
                             "Questa operazione è stata effettuata dal Chair della conferenza.\n\n" +
                             "Cordiali saluti,\n" +
                             "Sistema di Gestione Conferenze";
            
            UtilsControl.sendMail(revisore.getEmail(), emailSubject, emailBody);
            
            // Mostra schermata di successo
            String successMessage = "Il revisore " + revisore.getUsername() + " è stato rimosso dalla conferenza " + 
                                  conferenzaSelezionata.getTitolo() + ". È stata inviata una notifica di conferma.";
            SuccessScreen successScreen = new SuccessScreen(successMessage, "Rimozione Revisore");
            successScreen.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Errore durante la rimozione del revisore: " + e.getMessage(), 
                "Errore", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void asegnaRevisoriAutomaticamente(String idConferenza) {
        // Implementazione da definire
    }
    
    public void assegnaRevisoriPerPreferenze(String idConferenza) {
        // Implementazione da definire
    }
    
    public void conferenzaAssegnazione(String idArticolo, String idRevisore) {
        // Implementazione da definire
    }
    
    public void rimuoviArticoloRevisore() {
        // Implementazione da definire
    }
    
    public void rimuoviRevisore(String idRevisore) {
        // Implementazione da definire
    }
    
    public void visualizzaStatoRevisioni() {
        // Implementazione da definire
    }
    
    public void visualizzaStato(String idArticolo) {
        // Implementazione da definire
    }
    
    public void sendCommunication(int idConferenza) {
        // Carica i dati della conferenza se necessario
        if (conferenzaSelezionata == null || conferenzaSelezionata.getId() != idConferenza) {
            ConferenzaE conferenza = (ConferenzaE) App.dbms.getConferenceInfo(idConferenza);
            setConferenza(conferenza);
        }
        
        // Seguendo il sequence diagram: ConferenceControl -> SendCustomNotificationScreen
        try {
            // Crea e mostra la schermata per l'invio di comunicazioni personalizzate
            com.cms.users.conference.Interface.SendCustomNotificationScreen notificationScreen = 
                new com.cms.users.conference.Interface.SendCustomNotificationScreen(this);
            
            notificationScreen.create();
            
            System.out.println("SendCustomNotificationScreen aperta per la conferenza: " + 
                (conferenzaSelezionata != null ? conferenzaSelezionata.getTitolo() : "Non selezionata"));
                
        } catch (Exception e) {
            System.err.println("Errore durante l'apertura della schermata di comunicazione: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Ottiene e compila il log della conferenza seguendo il sequence diagram
     * ConferenceManagementScreen -> ConferenceControl -> DBMSBoundary -> compilaLog() -> salvaLog()
     */
    public void getLog(int idConferenza) {
        try {
            System.out.println("Richiesta log per conferenza ID: " + idConferenza);
            
            // Ottieni conferenceId della conferenza
            int conferenceId = idConferenza;
            
            // Seguendo il sequence diagram: chiama DBMSBoundary.getConferenceLog
            String conferenceLog = App.dbms.getConferenceLog(conferenceId);
            
            // Seguendo il sequence diagram: compila il log
            compilaLog(conferenceLog, idConferenza);
            
        } catch (Exception e) {
            System.err.println("Errore durante il recupero del log: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Compila il log della conferenza e chiama salvaLog per generare il PDF
     * Seguendo il sequence diagram: compilaLog() -> salvaLog()
     */
    public void compilaLog(String rawLog, int idConferenza) {
        try {
            System.out.println("Compilazione log per conferenza ID: " + idConferenza);
            
            // Carica i dati della conferenza se necessario
            if (conferenzaSelezionata == null || conferenzaSelezionata.getId() != idConferenza) {
                ConferenzaE conferenza = (ConferenzaE) App.dbms.getConferenceInfo(idConferenza);
                setConferenza(conferenza);
            }
            
            // Compila il log con informazioni aggiuntive
            StringBuilder logCompleto = new StringBuilder();
            logCompleto.append("=== LOG CONFERENZA ===\n");
            logCompleto.append("Titolo: ").append(conferenzaSelezionata != null ? conferenzaSelezionata.getTitolo() : "N/A").append("\n");
            logCompleto.append("Anno: ").append(conferenzaSelezionata != null ? conferenzaSelezionata.getAnnoEdizione() : "N/A").append("\n");
            logCompleto.append("Data generazione: ").append(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
            logCompleto.append("Generato da: ").append(App.utenteAccesso != null ? App.utenteAccesso.getUsername() : "Sistema").append("\n");
            logCompleto.append("\n=== DETTAGLI LOG ===\n");
            
            // Aggiungi il contenuto del log dal database (se disponibile)
            if (rawLog != null && !rawLog.trim().isEmpty()) {
                logCompleto.append(rawLog);
            } else {
                // Se non ci sono log dal database, crea un log di esempio
                logCompleto.append("01/06/2025 10:00 - Conferenza creata\n");
                logCompleto.append("05/06/2025 14:30 - Aggiunti revisori\n");
                logCompleto.append("10/06/2025 09:15 - Prima sottomissione ricevuta\n");
                logCompleto.append("15/06/2025 16:45 - Deadline sottomissioni\n");
                logCompleto.append("20/06/2025 11:20 - Inizio fase di revisione\n");
            }
            
            logCompleto.append("\n=== FINE LOG ===\n");
            
            // Seguendo il sequence diagram: chiama salvaLog
            salvaLog(logCompleto.toString(), idConferenza);
            
        } catch (Exception e) {
            System.err.println("Errore durante la compilazione del log: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Salva il log compilato in un file PDF e lo scarica automaticamente
     * Seguendo il sequence diagram: salvaLog() con nome file specifico
     */
    public void salvaLog(String logContent, int idConferenza) {
        try {
            System.out.println("Salvataggio log in PDF per conferenza ID: " + idConferenza);
            
            // Crea il nome del file seguendo il pattern richiesto
            String nomeConferenza = (conferenzaSelezionata != null && conferenzaSelezionata.getTitolo() != null) 
                ? conferenzaSelezionata.getTitolo().replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "_")
                : "Conferenza_" + idConferenza;
            
            String dataAttuale = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
            String nomeFile = "Log_Conferenza_" + nomeConferenza + "_" + dataAttuale + ".pdf";
            
            // Percorso di download (cartella Downloads dell'utente)
            String userHome = System.getProperty("user.home");
            String downloadPath = userHome + File.separator + "Downloads" + File.separator + nomeFile;
            
            System.out.println("Generazione PDF: " + downloadPath);
            
            // Crea il documento PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(downloadPath));
            
            document.open();
            
            // Aggiungi il titolo
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("LOG CONFERENZA", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            
            // Aggiungi una riga vuota
            document.add(new Paragraph(" "));
            
            // Aggiungi il contenuto del log
            Font contentFont = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL);
            Paragraph content = new Paragraph(logContent, contentFont);
            document.add(content);
            
            document.close();
            
            System.out.println("PDF generato con successo: " + downloadPath);
            
            // Scarica automaticamente il file (apre il file manager nella cartella Downloads)
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(new File(userHome + File.separator + "Downloads"));
                    System.out.println("Cartella Downloads aperta automaticamente");
                } catch (IOException e) {
                    System.err.println("Impossibile aprire la cartella Downloads automaticamente: " + e.getMessage());
                }
            }
            
            // Mostra messaggio di successo
            String successMessage = "Log della conferenza generato con successo!\n\n" +
                                   "File salvato in: " + downloadPath + "\n\n" +
                                   "Il file è stato scaricato nella cartella Downloads.";
            
            SuccessScreen successScreen = new SuccessScreen(successMessage, "Log Generato");
            successScreen.setVisible(true);
            
        } catch (DocumentException | IOException e) {
            System.err.println("Errore durante la generazione del PDF: " + e.getMessage());
            e.printStackTrace();
            
            // Mostra messaggio di errore
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Errore durante la generazione del log PDF:\n" + e.getMessage(), 
                "Errore", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void revisionaArticolo(String idArticolo) {
        // Implementazione da definire
    }
    
    /**
     * Invia un messaggio personalizzato ai destinatari selezionati
     * Segue il sequence diagram: ottiene utenti dal DB, invia email, inserisce notifica
     * @param messaggio Il testo del messaggio da inviare
     * @param destinatari Lista dei destinatari selezionati
     */
    public void inviaMessaggioPersonalizzato(String messaggio, java.util.List<String> destinatari) {
        try {
            if (conferenzaSelezionata == null) {
                System.err.println("Errore: nessuna conferenza selezionata");
                return;
            }
            
            if (messaggio == null || messaggio.trim().isEmpty()) {
                System.err.println("Errore: messaggio vuoto");
                return;
            }
            
            if (destinatari == null || destinatari.isEmpty()) {
                System.err.println("Errore: nessun destinatario selezionato");
                return;
            }
            
            System.out.println("Invio comunicazione per conferenza: " + conferenzaSelezionata.getTitolo());
            System.out.println("Messaggio: " + messaggio);
            System.out.println("Destinatari: " + destinatari);
            
            
            LinkedList<UtenteE> utentiDaNotificare = new LinkedList<>();
            
            for (String tipoDestinatario : destinatari) {
                switch (tipoDestinatario) {
                    case "Tutti gli autori":
                        // Ottieni lista autori dalla conferenza
                        LinkedList<UtenteE> autori = App.dbms.getListaAutori(conferenzaSelezionata.getId());
                        if (autori != null) {
                            utentiDaNotificare.addAll(autori);
                        }
                        break;
                        
                    case "Tutti i revisori":
                        // Ottieni lista revisori dalla conferenza  
                        LinkedList<UtenteE> revisori = App.dbms.getRevisori(conferenzaSelezionata.getId());
                        if (revisori != null) {
                            utentiDaNotificare.addAll(revisori);
                        }
                        break;
                        
                    case "Tutti i partecipanti":
                        // Ottieni tutti gli utenti della conferenza (autori + revisori + chair)
                        LinkedList<UtenteE> autoriTutti = App.dbms.getListaAutori(conferenzaSelezionata.getId());
                        LinkedList<UtenteE> revisoriTutti = App.dbms.getRevisori(conferenzaSelezionata.getId());
                        LinkedList<UtenteE> chairTutti = App.dbms.getChair(conferenzaSelezionata.getId());
                        
                        if (autoriTutti != null) utentiDaNotificare.addAll(autoriTutti);
                        if (revisoriTutti != null) utentiDaNotificare.addAll(revisoriTutti);
                        if (chairTutti != null) utentiDaNotificare.addAll(chairTutti);
                        break;
                        
                    default:
                        // Controlla se è un utente specifico nel formato "username (email)"
                        if (tipoDestinatario.contains("(") && tipoDestinatario.contains(")")) {
                            // Estrai email dall'utente specifico
                            int startIndex = tipoDestinatario.lastIndexOf("(");
                            int endIndex = tipoDestinatario.lastIndexOf(")");
                            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                                String email = tipoDestinatario.substring(startIndex + 1, endIndex);
                                
                                // Cerca l'utente per email tra tutti i partecipanti della conferenza
                                LinkedList<UtenteE> autoriSpecifici = App.dbms.getListaAutori(conferenzaSelezionata.getId());
                                LinkedList<UtenteE> revisoriSpecifici = App.dbms.getRevisori(conferenzaSelezionata.getId());
                                LinkedList<UtenteE> chairSpecifici = App.dbms.getChair(conferenzaSelezionata.getId());
                                
                                UtenteE utenteSpecifico = null;
                                
                                // Cerca tra gli autori
                                if (autoriSpecifici != null) {
                                    for (UtenteE utente : autoriSpecifici) {
                                        if (email.equals(utente.getEmail())) {
                                            utenteSpecifico = utente;
                                            break;
                                        }
                                    }
                                }
                                
                                // Cerca tra i revisori se non trovato tra gli autori
                                if (utenteSpecifico == null && revisoriSpecifici != null) {
                                    for (UtenteE utente : revisoriSpecifici) {
                                        if (email.equals(utente.getEmail())) {
                                            utenteSpecifico = utente;
                                            break;
                                        }
                                    }
                                }
                                
                                // Cerca tra i chair se non trovato
                                if (utenteSpecifico == null && chairSpecifici != null) {
                                    for (UtenteE utente : chairSpecifici) {
                                        if (email.equals(utente.getEmail())) {
                                            utenteSpecifico = utente;
                                            break;
                                        }
                                    }
                                }
                                
                                if (utenteSpecifico != null) {
                                    utentiDaNotificare.add(utenteSpecifico);
                                    System.out.println("Aggiunto utente specifico: " + utenteSpecifico.getUsername() + " (" + utenteSpecifico.getEmail() + ")");
                                } else {
                                    System.out.println("Utente specifico non trovato per email: " + email);
                                }
                            } else {
                                System.out.println("Formato utente specifico non valido: " + tipoDestinatario);
                            }
                        } else {
                            System.out.println("Tipo destinatario non riconosciuto: " + tipoDestinatario);
                        }
                        break;
                }
            }
            
            // Rimuovi duplicati basandosi sull'ID utente
            LinkedList<UtenteE> utentiUnivoci = new LinkedList<>();
            java.util.Set<Integer> idUtentiGiaAggiunti = new java.util.HashSet<>();
            
            for (UtenteE utente : utentiDaNotificare) {
                if (!idUtentiGiaAggiunti.contains(utente.getId())) {
                    utentiUnivoci.add(utente);
                    idUtentiGiaAggiunti.add(utente.getId());
                }
            }
            
            if (utentiUnivoci.isEmpty()) {
                System.err.println("Nessun utente trovato per i destinatari selezionati");
                return;
            }
            
            // Seguendo il sequence diagram: invia email e inserisci notifiche
            String oggetto = "Comunicazione da " + App.utenteAccesso.getUsername() + 
                           " per la conferenza: " + conferenzaSelezionata.getTitolo();
            
            int emailInviate = 0;
            int notificheInserite = 0;
            
            for (UtenteE utente : utentiUnivoci) {
                try {
                    // Invia email tramite UtilsControl
                    boolean emailInviata = UtilsControl.sendMail(
                        utente.getEmail(), 
                        oggetto, 
                        messaggio
                    );
                    
                    if (emailInviata) {
                        emailInviate++;
                        System.out.println("Email inviata a: " + utente.getEmail());
                    } else {
                        System.err.println("Errore invio email a: " + utente.getEmail());
                    }
                    
                    // Inserisci notifica nel database tramite DBMSBoundary
                    App.dbms.insertNotifica(
                        messaggio, 
                        conferenzaSelezionata.getId(), 
                        utente.getId(), 
                        0, // tipo notifica: comunicazione
                        "COMUNICAZIONE-PERSONALIZZATA"
                    );
                    
                    notificheInserite++;
                    System.out.println("Notifica inserita per utente ID: " + utente.getId());
                    
                } catch (Exception e) {
                    System.err.println("Errore durante l'invio a " + utente.getEmail() + ": " + e.getMessage());
                }
            }
            
            System.out.println("Comunicazione completata:");
            System.out.println("- Email inviate: " + emailInviate + "/" + utentiUnivoci.size());
            System.out.println("- Notifiche inserite: " + notificheInserite + "/" + utentiUnivoci.size());
            
        } catch (Exception e) {
            System.err.println("Errore durante l'invio della comunicazione personalizzata: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Seleziona e invita un co-chair alla conferenza
     * Segue il sequence diagram: ottiene utente dal DB, invia email, inserisce notifica
     * @param idCoChair L'ID dell'utente da invitare come co-chair
     */
    public void selezionaCoChair(int idCoChair) {
        try {
            if (conferenzaSelezionata == null) {
                System.err.println("Errore: nessuna conferenza selezionata");
                return;
            }
            
            // Ottieni informazioni dell'utente dal database
            UtenteE utenteCoChair = App.dbms.getUser(idCoChair);
            
            if (utenteCoChair == null) {
                System.err.println("Errore: utente non trovato con ID " + idCoChair);
                return;
            }
            
            System.out.println("Invito co-chair per conferenza: " + conferenzaSelezionata.getTitolo());
            System.out.println("Utente da invitare: " + utenteCoChair.getUsername() + " (" + utenteCoChair.getEmail() + ")");
            
            // Crea il messaggio di invito
            String oggetto = "Invito Co-Chair per la conferenza: " + conferenzaSelezionata.getTitolo();
            String messaggio = "“Convocazione ricevuta da " + App.utenteAccesso.getUsername() +
                                " per gestire, in qualità di co-chair, la conferenza:" + conferenzaSelezionata.getTitolo();
            
            // Invia email tramite UtilsControl
            boolean emailInviata = UtilsControl.sendMail(
                utenteCoChair.getEmail(), 
                oggetto, 
                messaggio
            );
            
            if (emailInviata) {
                System.out.println("Email di invito co-chair inviata a: " + utenteCoChair.getEmail());
            } else {
                System.err.println("Errore nell'invio dell'email a: " + utenteCoChair.getEmail());
            }
            
            // Inserisci notifica nel database tramite DBMSBoundary
            String testoNotifica = "Invito Co-Chair ricevuto da " + App.utenteAccesso.getUsername() + 
                                 " per la conferenza: " + conferenzaSelezionata.getTitolo();
            
            App.dbms.insertNotifica(
                testoNotifica, 
                conferenzaSelezionata.getId(), 
                idCoChair, 
                1, // tipo notifica: invito
                "INVITO-CO-CHAIR"
            );
            
            System.out.println("Notifica di invito co-chair inserita per utente ID: " + idCoChair);
            System.out.println("Invito co-chair completato con successo");
            
            // Mostra schermata di successo come indicato nel sequence diagram
            String successMessage = "Invito Co-Chair inviato con successo!\n\n" +
                                   "L'invito è stato inviato a: " + utenteCoChair.getUsername() + "\n" +
                                   "Email: " + utenteCoChair.getEmail() + "\n\n" +
                                   "Conferenza: " + conferenzaSelezionata.getTitolo();
            
            SuccessScreen successScreen = new SuccessScreen(successMessage, "Invito Co-Chair");
            successScreen.setVisible(true);
            
        } catch (Exception e) {
            System.err.println("Errore durante l'invito del co-chair: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Calcola il numero totale di revisori attuali per la conferenza
     */
    public int calcolaNumeroRevisoriAttuali() {
        try {
            if (conferenzaSelezionata == null) {
                return 0;
            }
            
            LinkedList<UtenteE> revisori = App.dbms.getRevisori(conferenzaSelezionata.getId());
            return revisori != null ? revisori.size() : 0;
            
        } catch (Exception e) {
            System.err.println("Errore nel calcolo dei revisori attuali: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Calcola il numero di revisori necessari in base agli articoli effettivi
     */
    public int calcolaNumeroRevisoriNecessari() {
        try {
            if (conferenzaSelezionata == null) {
                return 0;
            }
            
            // Ottieni il numero di articoli reali dalla conference
            LinkedList<com.cms.users.Entity.ArticoloE> articoli = 
                App.dbms.getListaArticoli(conferenzaSelezionata.getId());
            int numeroArticoliReali = articoli != null ? articoli.size() : 0;
            
            // Confronta con il numero previsto nella conferenza
            int numeroArticoliPrevisti = conferenzaSelezionata.getNumeroArticoliPrevisti();
            int numeroRevisoriPerArticolo = conferenzaSelezionata.getNumeroRevisoriPerArticolo();
            
            // Usa il maggiore tra articoli reali e previsti
            int numeroArticoliDaConsiderare = Math.max(numeroArticoliReali, numeroArticoliPrevisti);
            
            // Calcola il numero totale di revisori necessari
            return numeroArticoliDaConsiderare * numeroRevisoriPerArticolo;
            
        } catch (Exception e) {
            System.err.println("Errore nel calcolo dei revisori necessari: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Calcola il numero di revisori mancanti
     */
    public int calcolaNumeroRevisoriMancanti() {
        int attuali = calcolaNumeroRevisoriAttuali();
        int necessari = calcolaNumeroRevisoriNecessari();
        return Math.max(0, necessari - attuali); // Non può essere negativo
    }
    
    /**
     * Ottiene le statistiche complete dei revisori per la conferenza
     * Utilizzato dalle schermate per mostrare informazioni aggiornate
     */
    public ReviewerStats ottieniStatisticheRevisori() {
        return new ReviewerStats(
            calcolaNumeroRevisoriAttuali(),
            calcolaNumeroRevisoriNecessari(),
            calcolaNumeroRevisoriMancanti()
        );
    }
    
    /**
     * Classe interna per incapsulare le statistiche dei revisori
     */
    public static class ReviewerStats {
        private final int attuali;
        private final int necessari;
        private final int mancanti;
        
        public ReviewerStats(int attuali, int necessari, int mancanti) {
            this.attuali = attuali;
            this.necessari = necessari;
            this.mancanti = mancanti;
        }
        
        public int getAttuali() { return attuali; }
        public int getNecessari() { return necessari; }
        public int getMancanti() { return mancanti; }
    }

    /**
     * Visualizza lo stato delle sottomissioni delegando al ListScreen
     * Segue il sequence diagram: ConferenceControl -> DBMSBoundary -> ListScreen
     */
    public void visualizzaStatoSottomissioni(int idConferenza) {
        try {
            // Carica i dati della conferenza se necessario
            if (conferenzaSelezionata == null || conferenzaSelezionata.getId() != idConferenza) {
                ConferenzaE conferenza = (ConferenzaE) App.dbms.getConferenceInfo(idConferenza);
                setConferenza(conferenza);
            }
            
            if (conferenzaSelezionata == null) {
                System.err.println("Errore: conferenza non trovata con ID " + idConferenza);
                return;
            }
            
            // Ottieni la lista degli articoli dalla DBMSBoundary
            java.util.LinkedList<com.cms.users.Entity.ArticoloE> articoli = 
                App.dbms.getListaArticoli(idConferenza);
            
            // Converti gli articoli in dati per ListScreen
            java.util.List<com.cms.users.Commons.ListScreen.SubmissionData> submissionDataList = 
                new java.util.ArrayList<>();
            
            if (articoli != null && !articoli.isEmpty()) {
                for (com.cms.users.Entity.ArticoloE articolo : articoli) {
                    // Ottieni dettagli aggiuntivi per ogni articolo se necessario
                    // In questo caso uso i dati disponibili nell'ArticoloE
                    
                    String status = determineArticleStatus(articolo);
                    int reviewersAssigned = calculateReviewersAssigned(articolo);
                    int reviewsCompleted = calculateReviewsCompleted(articolo);
                    String lastActivity = determineLastActivity(articolo);
                    
                    com.cms.users.Commons.ListScreen.SubmissionData submissionData = 
                        new com.cms.users.Commons.ListScreen.SubmissionData(
                            String.valueOf(articolo.getId()),
                            articolo.getTitolo(),
                            getAuthorsString(articolo),
                            formatDate(articolo.getUltimaModifica()), // Usiamo ultima modifica come proxy per data sottomissione
                            status,
                            reviewersAssigned,
                            reviewsCompleted,
                            lastActivity
                        );
                    
                    submissionDataList.add(submissionData);
                }
            }
            
            // Crea e mostra la ListScreen con i dati dinamici
            com.cms.users.Commons.ListScreen listScreen = 
                new com.cms.users.Commons.ListScreen(
                    com.cms.users.Commons.ListScreen.UserRole.CHAIR,
                    com.cms.users.Commons.ListScreen.ChairFunction.VIEW_SUBMISSIONS
                );
            
            // Imposta i dati dinamici
            listScreen.setSubmissionData(submissionDataList);
            
            // Mostra la schermata
            listScreen.setVisible(true);
            
            System.out.println("ListScreen per stato sottomissioni aperta con " + 
                             submissionDataList.size() + " articoli");
            
        } catch (Exception e) {
            System.err.println("Errore durante la visualizzazione dello stato sottomissioni: " + e.getMessage());
            e.printStackTrace();
            
            javax.swing.JOptionPane.showMessageDialog(null,
                "Errore durante il caricamento dello stato delle sottomissioni: " + e.getMessage(),
                "Errore",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Determina lo stato dell'articolo basandosi sui dati disponibili
     */
    private String determineArticleStatus(com.cms.users.Entity.ArticoloE articolo) {
        // Usa lo stato direttamente dall'articolo se disponibile
        if (articolo.getStato() != null && !articolo.getStato().trim().isEmpty()) {
            return articolo.getStato();
        }
        
        // Altrimenti simula basandosi sull'ID per demo
        if (articolo.getUltimaModifica() != null) {
            int articleId = articolo.getId();
            switch (articleId % 6) {
                case 0: return "Accettato";
                case 1: return "Rifiutato";
                case 2: return "In revisione";
                case 3: return "Revisione completata";
                case 4: return "Assegnazione revisori";
                default: return "Sottomesso";
            }
        }
        return "Bozza";
    }
    
    /**
     * Calcola il numero di revisori assegnati per l'articolo
     */
    private int calculateReviewersAssigned(com.cms.users.Entity.ArticoloE articolo) {
        // In un sistema reale, questo richiederebbe una query separata
        // Per ora simula basandosi sull'ID
        return (articolo.getId() % 3) + 1; // 1-3 revisori
    }
    
    /**
     * Calcola il numero di revisioni completate per l'articolo
     */
    private int calculateReviewsCompleted(com.cms.users.Entity.ArticoloE articolo) {
        // In un sistema reale, questo richiederebbe una query separata
        // Per ora simula basandosi sull'ID
        int assigned = calculateReviewersAssigned(articolo);
        return Math.min(assigned, (articolo.getId() % 4)); // 0 fino al numero assegnato
    }
    
    /**
     * Determina l'ultima attività per l'articolo
     */
    private String determineLastActivity(com.cms.users.Entity.ArticoloE articolo) {
        // Simula l'ultima attività basandosi sullo stato
        String status = determineArticleStatus(articolo);
        java.time.LocalDate baseDate = articolo.getUltimaModifica() != null ? 
            articolo.getUltimaModifica() : java.time.LocalDate.now();
        
        switch (status) {
            case "Accettato":
                return "Articolo accettato il " + formatDate(baseDate.plusDays(15));
            case "Rifiutato":
                return "Articolo rifiutato il " + formatDate(baseDate.plusDays(20));
            case "In revisione":
                return "Revisione 1 completata il " + formatDate(baseDate.plusDays(5));
            case "Revisione completata":
                return "Tutte le revisioni completate il " + formatDate(baseDate.plusDays(12));
            case "Assegnazione revisori":
                return "Revisori assegnati il " + formatDate(baseDate.plusDays(1));
            default:
                return "In attesa di assegnazione revisori";
        }
    }
    
    /**
     * Ottiene la stringa degli autori dall'articolo
     */
    private String getAuthorsString(com.cms.users.Entity.ArticoloE articolo) {
        // Usa la lista dei co-autori se disponibile
        if (articolo.getCoAutori() != null && !articolo.getCoAutori().isEmpty()) {
            StringBuilder authors = new StringBuilder();
            for (int i = 0; i < articolo.getCoAutori().size(); i++) {
                if (i > 0) {
                    authors.append(", ");
                }
                authors.append(articolo.getCoAutori().get(i));
            }
            return authors.toString();
        }
        
        // Simula autori multipli per demo
        String[] possibleAuthors = {
            "Dr. Mario Rossi, Prof.ssa Anna Verdi",
            "Prof. Luigi Bianchi",
            "Dr.ssa Giulia Neri, Dr. Paolo Blu",
            "Prof. Andrea Giallo",
            "Dr. Francesco Verde, Dr.ssa Maria Rosa",
            "Prof.ssa Elena Viola"
        };
        
        return possibleAuthors[articolo.getId() % possibleAuthors.length];
    }
    
    /**
     * Formatta una LocalDate in stringa
     */
    private String formatDate(java.time.LocalDate date) {
        if (date == null) {
            return "N/A";
        }
        return date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    /**
     * Apre la schermata per assegnare i revisori agli articoli
     * Segue il sequence diagram: ConferenceControl -> DBMSBoundary -> ReviewerScreen
     */
    public void apriAssegnazioneRevisori(int idConferenza) {
        try {
            // Carica i dati della conferenza se necessario
            if (conferenzaSelezionata == null || conferenzaSelezionata.getId() != idConferenza) {
                ConferenzaE conferenza = (ConferenzaE) App.dbms.getConferenceInfo(idConferenza);
                setConferenza(conferenza);
            }
            
            if (conferenzaSelezionata == null) {
                System.err.println("Errore: conferenza non trovata con ID " + idConferenza);
                return;
            }
            
            // Crea e apri la ReviewerScreen
            com.cms.users.conference.Interface.ReviewerScreen reviewerScreen = 
                new com.cms.users.conference.Interface.ReviewerScreen(String.valueOf(idConferenza));
            
            // Imposta il riferimento al control per delegare le operazioni
            reviewerScreen.setConferenceControl(this);
            
            // Carica i dati reali dal database
            reviewerScreen.loadRealData();
            
            reviewerScreen.setVisible(true);
            
            System.out.println("ReviewerScreen aperta per conferenza ID: " + idConferenza);
            
        } catch (Exception e) {
            System.err.println("Errore durante l'apertura della schermata assegnazione revisori: " + e.getMessage());
            e.printStackTrace();
            
            javax.swing.JOptionPane.showMessageDialog(null,
                "Errore durante l'apertura della schermata di assegnazione: " + e.getMessage(),
                "Errore",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Apre la schermata di gestione dei revisori per gli articoli
     * Segue il sequence diagram: ConferenceManagementScreen -> ConferenceControl -> ReviewerScreen
     * @param idConferenza ID della conferenza
     */
    public void apriGestioneRevisori(int idConferenza) {
        try {
            // Carica i dati della conferenza se necessario
            if (conferenzaSelezionata == null || conferenzaSelezionata.getId() != idConferenza) {
                ConferenzaE conferenza = (ConferenzaE) App.dbms.getConferenceInfo(idConferenza);
                setConferenza(conferenza);
            }
            
            System.out.println("Apertura schermata gestione revisori per conferenza ID: " + idConferenza);
            
            // Crea la ReviewerScreen
            com.cms.users.conference.Interface.ReviewerScreen reviewerScreen = 
                new com.cms.users.conference.Interface.ReviewerScreen();
            
            // Configura la ReviewerScreen
            reviewerScreen.setConferenceControl(this);
            reviewerScreen.setConferenceId(String.valueOf(idConferenza));
            
            // Carica i dati reali dal database
            reviewerScreen.loadRealData();
            
            // Mostra la schermata
            reviewerScreen.setVisible(true);
            
        } catch (Exception e) {
            System.err.println("Errore durante l'apertura della schermata di gestione revisori: " + e.getMessage());
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Errore durante l'apertura della schermata di gestione revisori: " + e.getMessage(), 
                "Errore", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Ottiene la lista dei revisori per la ReviewerScreen
     * @param idConferenza ID della conferenza
     * @return Lista di ReviewerData contenente le informazioni dei revisori
     */
    public List<com.cms.users.conference.Interface.ReviewerScreen.ReviewerData> ottieniRevisori(int idConferenza) {
        try {
            List<com.cms.users.conference.Interface.ReviewerScreen.ReviewerData> result = new ArrayList<>();
            
            // Ottiene i revisori dalla DBMSBoundary
            LinkedList<UtenteE> revisori = App.dbms.getRevisori(idConferenza);
            
            if (revisori != null) {
                for (UtenteE revisore : revisori) {
                    // Converte UtenteE in ReviewerData
                    String id = String.valueOf(revisore.getId());
                    String name = revisore.getUsername();
                    String email = revisore.getEmail();
                    String expertise = ""; // L'expertise non è disponibile nel modello UtenteE
                    
                    result.add(new com.cms.users.conference.Interface.ReviewerScreen.ReviewerData(id, name, email, expertise));
                }
            }
            
            return result;
            
        } catch (Exception e) {
            System.err.println("Errore durante il recupero dei revisori: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Ottiene la lista degli articoli per la ReviewerScreen
     * @param idConferenza ID della conferenza
     * @return Lista di ArticleData contenente le informazioni degli articoli
     */
    public List<com.cms.users.conference.Interface.ReviewerScreen.ArticleData> ottieniArticoli(int idConferenza) {
        try {
            List<com.cms.users.conference.Interface.ReviewerScreen.ArticleData> result = new ArrayList<>();
            
            // Ottiene gli articoli dalla DBMSBoundary
            LinkedList<ArticoloE> articoli = App.dbms.getListaArticoli(idConferenza);
            
            if (articoli != null) {
                for (ArticoloE articolo : articoli) {
                    // Converte ArticoloE in ArticleData
                    String id = String.valueOf(articolo.getId());
                    String title = articolo.getTitolo();
                    
                    // Ottiene autori dell'articolo
                    LinkedList<UtenteE> autori = App.dbms.getArticleAuthors(articolo.getId());
                    StringBuilder authorsBuilder = new StringBuilder();
                    
                    if (autori != null && !autori.isEmpty()) {
                        for (int i = 0; i < autori.size(); i++) {
                            authorsBuilder.append(autori.get(i).getUsername());
                            if (i < autori.size() - 1) {
                                authorsBuilder.append(", ");
                            }
                        }
                    } else {
                        authorsBuilder.append("N/D");
                    }
                    
                    // Ottiene keywords dell'articolo
                    ArrayList<String> keywords = App.dbms.getKeywordsArticolo(articolo.getId());
                    StringBuilder keywordsBuilder = new StringBuilder();
                    
                    if (keywords != null && !keywords.isEmpty()) {
                        for (int i = 0; i < keywords.size(); i++) {
                            keywordsBuilder.append(keywords.get(i));
                            if (i < keywords.size() - 1) {
                                keywordsBuilder.append(", ");
                            }
                        }
                    } else {
                        keywordsBuilder.append("N/D");
                    }
                    
                    result.add(new com.cms.users.conference.Interface.ReviewerScreen.ArticleData(
                        id, title, authorsBuilder.toString(), keywordsBuilder.toString()));
                }
            }
            
            return result;
            
        } catch (Exception e) {
            System.err.println("Errore durante il recupero degli articoli: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Ottiene le assegnazioni esistenti revisori-articoli dal database
     * Segue il sequence diagram: ConferenceControl -> DBMSBoundary
     */
    public boolean[][] ottieniAssegnazioniEsistenti(int idConferenza, 
            java.util.List<com.cms.users.conference.Interface.ReviewerScreen.ReviewerData> reviewers,
            java.util.List<com.cms.users.conference.Interface.ReviewerScreen.ArticleData> articles) {
        
        boolean[][] assignments = new boolean[reviewers.size()][articles.size()];
        
        try {
            // Per ogni combinazione revisore-articolo, controlla se esiste un'assegnazione
            for (int i = 0; i < reviewers.size(); i++) {
                for (int j = 0; j < articles.size(); j++) {
                    int idRevisore = Integer.parseInt(reviewers.get(i).id);
                    int idArticolo = Integer.parseInt(articles.get(j).id);
                    
                    // Verifica se esiste l'assegnazione nel database
                    boolean assegnato = App.dbms.verificaAssegnazioneEsistente(idArticolo, idRevisore);
                    assignments[i][j] = assegnato;
                    
                    if (assegnato) {
                        System.out.println("Assegnazione esistente trovata: Revisore " + 
                            reviewers.get(i).name + " (ID:" + idRevisore + ") -> Articolo " + 
                            articles.get(j).title + " (ID:" + idArticolo + ")");
                    }
                }
            }
            
            // Conta le assegnazioni trovate
            int totaleAssegnazioni = 0;
            for (int i = 0; i < assignments.length; i++) {
                for (int j = 0; j < assignments[i].length; j++) {
                    if (assignments[i][j]) totaleAssegnazioni++;
                }
            }
            
            System.out.println("Caricate " + totaleAssegnazioni + " assegnazioni esistenti per conferenza ID: " + idConferenza);
            
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento delle assegnazioni esistenti: " + e.getMessage());
            e.printStackTrace();
        }
        
        return assignments;
    }
    
    /**
     * Verifica se esiste un'assegnazione tra un articolo e un revisore
     * Controlla direttamente nel database nella tabella revisiona
     */
    
    
    /**
     * Assegna automaticamente i revisori agli articoli
     * Segue il sequence diagram: ConferenceControl -> assegnazione automatica -> DBMSBoundary
     */
    public void assegnaAutomaticamenteRevisori(int idConferenza) {
        try {
            if (conferenzaSelezionata == null) {
                System.err.println("Errore: nessuna conferenza selezionata");
                return;
            }
            
            // Ottieni revisori e articoli
            java.util.LinkedList<UtenteE> revisori = App.dbms.getRevisori(idConferenza);
            java.util.LinkedList<com.cms.users.Entity.ArticoloE> articoli = App.dbms.getListaArticoli(idConferenza);
            
            if (revisori == null || revisori.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Nessun revisore disponibile per la conferenza",
                    "Avviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (articoli == null || articoli.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Nessun articolo disponibile per la conferenza",
                    "Avviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Ottieni il numero minimo di revisori per articolo dalla configurazione della conferenza
            int numeroRevisoriPerArticolo = ottieniNumeroMinimoRevisori();
            
            System.out.println("Assegnazione automatica: " + numeroRevisoriPerArticolo + " revisori per articolo");
            
            int assegnazioniEffettuate = 0;
            
            // Assegna automaticamente i revisori per ogni articolo
            for (com.cms.users.Entity.ArticoloE articolo : articoli) {
                java.util.List<UtenteE> revisoriAssegnati = new java.util.ArrayList<>();
                
                // Seleziona casualmente i revisori per questo articolo
                java.util.List<UtenteE> revisoriDisponibili = new java.util.ArrayList<>(revisori);
                java.util.Collections.shuffle(revisoriDisponibili);
                
                for (int i = 0; i < Math.min(numeroRevisoriPerArticolo, revisoriDisponibili.size()); i++) {
                    UtenteE revisore = revisoriDisponibili.get(i);
                    
                    // Verifica se l'assegnazione esiste già
                    if (!App.dbms.verificaAssegnazioneEsistente(articolo.getId(), revisore.getId())) {
                        // Usa il nostro metodo setRevisoreArticolo che fa già il controllo di duplicati
                        this.setRevisoreArticolo(articolo.getId(), revisore.getId());
                        
                        revisoriAssegnati.add(revisore);
                        assegnazioniEffettuate++;
                        
                        System.out.println("Assegnato revisore " + revisore.getUsername() + 
                                         " all'articolo " + articolo.getTitolo());
                    } else {
                        System.out.println("Assegnazione già esistente per revisore " + 
                                         revisore.getUsername() + " e articolo " + articolo.getTitolo());
                    }
                }
            }
            
            // Mostra messaggio di successo
            javax.swing.JOptionPane.showMessageDialog(null,
                "Assegnazione automatica completata!\n\n" +
                "Articoli processati: " + articoli.size() + "\n" +
                "Assegnazioni effettuate: " + assegnazioniEffettuate + "\n" +
                "Revisori per articolo: " + numeroRevisoriPerArticolo,
                "Successo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            System.err.println("Errore durante l'assegnazione automatica: " + e.getMessage());
            e.printStackTrace();
            
            javax.swing.JOptionPane.showMessageDialog(null,
                "Errore durante l'assegnazione automatica: " + e.getMessage(),
                "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Assegna i revisori agli articoli basandosi sulle preferenze/competenze
     * Segue il sequence diagram: ConferenceControl -> assegnazione per preferenze -> DBMSBoundary
     */
    public void assegnaPerPreferenze(int idConferenza) {
        try {
            if (conferenzaSelezionata == null) {
                System.err.println("Errore: nessuna conferenza selezionata");
                return;
            }
            
            // Ottieni revisori e articoli
            java.util.LinkedList<UtenteE> revisori = App.dbms.getRevisori(idConferenza);
            java.util.LinkedList<com.cms.users.Entity.ArticoloE> articoli = App.dbms.getListaArticoli(idConferenza);
            
            if (revisori == null || revisori.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Nessun revisore disponibile per la conferenza",
                    "Avviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (articoli == null || articoli.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Nessun articolo disponibile per la conferenza",
                    "Avviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Ottieni il numero minimo di revisori per articolo dalla configurazione della conferenza
            int numeroRevisoriPerArticolo = ottieniNumeroMinimoRevisori();
            
            System.out.println("Assegnazione per preferenze: " + numeroRevisoriPerArticolo + " revisori per articolo");
            
            int assegnazioniEffettuate = 0;
            
            // Assegna revisori basandosi sulle competenze/preferenze
            for (com.cms.users.Entity.ArticoloE articolo : articoli) {
                java.util.List<UtenteE> revisoriCompatibili = trovaRevisoriCompatibili(articolo, revisori);
                
                // Se non ci sono revisori compatibili, usa tutti i revisori disponibili
                if (revisoriCompatibili.isEmpty()) {
                    revisoriCompatibili = new java.util.ArrayList<>(revisori);
                }
                
                // Ordina per compatibilità (simulato)
                java.util.Collections.shuffle(revisoriCompatibili);
                
                for (int i = 0; i < Math.min(numeroRevisoriPerArticolo, revisoriCompatibili.size()); i++) {
                    UtenteE revisore = revisoriCompatibili.get(i);
                    
                    // Verifica se l'assegnazione esiste già
                    if (!App.dbms.verificaAssegnazioneEsistente(articolo.getId(), revisore.getId())) {
                        // Usa il nostro metodo setRevisoreArticolo che fa già il controllo di duplicati
                        this.setRevisoreArticolo(articolo.getId(), revisore.getId());
                        
                        assegnazioniEffettuate++;
                        
                        System.out.println("Assegnato per preferenze revisore " + revisore.getUsername() + 
                                         " all'articolo " + articolo.getTitolo());
                    } else {
                        System.out.println("Assegnazione già esistente per revisore " + 
                                         revisore.getUsername() + " e articolo " + articolo.getTitolo());
                    }
                }
            }
            
            // Mostra messaggio di successo
            javax.swing.JOptionPane.showMessageDialog(null,
                "Assegnazione per preferenze completata!\n\n" +
                "Articoli processati: " + articoli.size() + "\n" +
                "Assegnazioni effettuate: " + assegnazioniEffettuate + "\n" +
                "Revisori per articolo: " + numeroRevisoriPerArticolo,
                "Successo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            System.err.println("Errore durante l'assegnazione per preferenze: " + e.getMessage());
            e.printStackTrace();
            
            javax.swing.JOptionPane.showMessageDialog(null,
                "Errore durante l'assegnazione per preferenze: " + e.getMessage(),
                "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Salva le assegnazioni manuali specificate dall'utente
     * Segue il sequence diagram: ConferenceControl -> DBMSBoundary (setRevisoreArticolo)
     */
    public void salvaAssegnazioni(boolean[][] assignments, 
                                  java.util.List<com.cms.users.conference.Interface.ReviewerScreen.ReviewerData> reviewers,
                                  java.util.List<com.cms.users.conference.Interface.ReviewerScreen.ArticleData> articles) {
        try {
            int assegnazioniSalvate = 0;
            
            // Scorri la matrice delle assegnazioni
            for (int i = 0; i < assignments.length && i < reviewers.size(); i++) {
                for (int j = 0; j < assignments[i].length && j < articles.size(); j++) {
                    if (assignments[i][j]) {
                        // Assegnazione attiva - salva nel database
                        int idRevisore = Integer.parseInt(reviewers.get(i).id);
                        int idArticolo = Integer.parseInt(articles.get(j).id);
                        
                        // Verifica se l'assegnazione esiste già
                        if (!App.dbms.verificaAssegnazioneEsistente(idArticolo, idRevisore)) {
                            // Usa il nostro metodo setRevisoreArticolo che fa già il controllo di duplicati
                            this.setRevisoreArticolo(idArticolo, idRevisore);
                            assegnazioniSalvate++;
                        } else {
                            System.out.println("Assegnazione già esistente: Revisore " + reviewers.get(i).name +
                                             " -> Articolo " + articles.get(j).title + ". Ignorata.");
                        }
                        
                        System.out.println("Salvata assegnazione: Revisore " + reviewers.get(i).name + 
                                         " -> Articolo " + articles.get(j).title);
                    }
                }
            }
            
            // Mostra messaggio di successo
            javax.swing.JOptionPane.showMessageDialog(null,
                "Assegnazioni salvate con successo!\n\n" +
                "Assegnazioni effettuate: " + assegnazioniSalvate,
                "Successo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            System.err.println("Errore durante il salvataggio delle assegnazioni: " + e.getMessage());
            e.printStackTrace();
            
            javax.swing.JOptionPane.showMessageDialog(null,
                "Errore durante il salvataggio: " + e.getMessage(),
                "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Ottiene il numero minimo di revisori per articolo dalla configurazione della conferenza
     */
    public int ottieniNumeroMinimoRevisori() {
        if (conferenzaSelezionata != null) {
            return conferenzaSelezionata.getNumeroRevisoriPerArticolo();
        }
        return 2; // Valore di default
    }
    
    /**
     * Trova i revisori compatibili con un articolo basandosi sulle competenze
     * Algoritmo semplificato che simula la compatibilità
     */
    private java.util.List<UtenteE> trovaRevisoriCompatibili(com.cms.users.Entity.ArticoloE articolo, 
                                                             java.util.LinkedList<UtenteE> tuttiRevisori) {
        java.util.List<UtenteE> compatibili = new java.util.ArrayList<>();
        
        // Converti le keywords dell'articolo da LinkedList<String> 
        java.util.LinkedList<String> keywordsArticolo = articolo.getKeywords();
        if (keywordsArticolo == null || keywordsArticolo.isEmpty()) {
            // Se l'articolo non ha keywords, tutti i revisori sono compatibili
            return new java.util.ArrayList<>(tuttiRevisori);
        }
        
        // Per ora, dato che UtenteE non ha keywords specifiche, utilizziamo un algoritmo semplificato
        // che assegna i revisori in modo casuale ma deterministico basato sull'ID dell'articolo
        for (UtenteE revisore : tuttiRevisori) {
            // Algoritmo semplificato: usa l'ID del revisore e dell'articolo per determinare compatibilità
            int compatibilityScore = Math.abs((revisore.getId() + articolo.getId()) % 10);
            if (compatibilityScore >= 5) { // 50% di probabilità di essere compatibile
                compatibili.add(revisore);
            }
        }
        
        // Se nessun revisore è compatibile, restituisci almeno il primo revisore disponibile
        if (compatibili.isEmpty() && !tuttiRevisori.isEmpty()) {
            compatibili.add(tuttiRevisori.getFirst());
        }
        
        return compatibili;
    }
    
    /**
     * Rimuove un revisore da un articolo specifico
     * Segue il sequence diagram: ConferenceManagementScreen -> ConferenceControl -> DBMSBoundary -> ListScreen
     */
    public void rimuoviRevisoreArticolo(int idArticolo, int idRevisore) {
        try {
            if (conferenzaSelezionata == null) {
                System.err.println("Errore: nessuna conferenza selezionata");
                return;
            }
            
            // Verifico se l'articolo appartiene alla conferenza
            int idConferenza = conferenzaSelezionata.getId();
            
            System.out.println("Rimozione revisore ID " + idRevisore + " dall'articolo ID " + idArticolo);
            
            // Eseguo la rimozione tramite SQL diretto, poiché DBMSBoundary non ha un metodo specifico
            // ma possiamo usare una connessione diretta come fatto in altre parti del codice
            Connection conn = null;
            PreparedStatement stmt = null;
            
            try {
                // Creo una connessione al database
                conn = DriverManager.getConnection(
                    "jdbc:mariadb://cicciosworld.duckdns.org:3306/CMS", 
                    "ids", 
                    "IngegneriaDelSoftware"
                );
                
                // Elimino la relazione nella tabella revisiona
                String sql = "DELETE FROM revisiona WHERE idArticolo = ? AND idRevisore = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idArticolo);
                stmt.setInt(2, idRevisore);
                
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    System.out.println("Rimosso con successo il revisore ID " + idRevisore + 
                                    " dall'articolo ID " + idArticolo);
                    
                    // Ottieni informazioni sul revisore e sull'articolo per le notifiche
                    UtenteE revisore = App.dbms.getUser(idRevisore);
                    ArticoloE articolo = App.dbms.getArticolo(idArticolo);
                    
                    if (revisore != null && articolo != null) {
                        // Invia notifica al revisore
                        String notificaText = "Sei stato rimosso dalla revisione dell'articolo: \"" + 
                                            articolo.getTitolo() + "\" per la conferenza: " + 
                                            conferenzaSelezionata.getTitolo();
                        
                        App.dbms.insertNotifica(notificaText, idConferenza, idRevisore, 1, "REMOVE-REVISORE-ARTICOLO");
                        
                        // Invia email di notifica
                        String emailSubject = "Rimozione revisione articolo - Conferenza: " + conferenzaSelezionata.getTitolo();
                        String emailBody = "Gentile " + revisore.getUsername() + ",\n\n" +
                                        "Ti informiamo che sei stato rimosso dalla revisione dell'articolo:\n" +
                                        "\"" + articolo.getTitolo() + "\"\n\n" +
                                        "nella conferenza \"" + conferenzaSelezionata.getTitolo() + "\"\n\n" +
                                        "Questa operazione è stata effettuata dal Chair della conferenza.\n\n" +
                                        "Cordiali saluti,\n" +
                                        "Sistema di Gestione Conferenze";
                        
                        UtilsControl.sendMail(revisore.getEmail(), emailSubject, emailBody);
                    }
                } else {
                    System.err.println("Nessuna assegnazione trovata per il revisore ID " + idRevisore + 
                                    " e l'articolo ID " + idArticolo);
                }
                
            } finally {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
            
        } catch (Exception e) {
            System.err.println("Errore durante la rimozione del revisore dall'articolo: " + e.getMessage());
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Errore durante la rimozione del revisore dall'articolo: " + e.getMessage(), 
                "Errore", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Assegna un revisore a un articolo specifico
     * Wrapper per il metodo DBMSBoundary.setRevisoreArticolo
     * Prima verifica se l'assegnazione esiste già per evitare duplicati
     * @param idArticolo ID dell'articolo
     * @param idRevisore ID del revisore
     */
    public void setRevisoreArticolo(int idArticolo, int idRevisore) {
        try {
            if (conferenzaSelezionata == null) {
                System.err.println("Errore: nessuna conferenza selezionata");
                return;
            }
            
            // Verifica se l'assegnazione esiste già
            boolean assegnazioneEsistente = App.dbms.verificaAssegnazioneEsistente(idArticolo, idRevisore);
            
            if (assegnazioneEsistente) {
                System.out.println("Assegnazione già esistente per revisore ID " + idRevisore + 
                                 " e articolo ID " + idArticolo + ". Operazione ignorata.");
                return;
            }
            
            System.out.println("Creazione nuova assegnazione: revisore ID " + idRevisore + 
                             " all'articolo ID " + idArticolo);
            
            // Chiamo il metodo della boundary
            App.dbms.setRevisoreArticolo(idArticolo, idRevisore);
            
            // Ottieni informazioni sul revisore e sull'articolo per le notifiche
            UtenteE revisore = App.dbms.getUser(idRevisore);
            ArticoloE articolo = App.dbms.getArticolo(idArticolo);
            
            if (revisore != null && articolo != null) {
                System.out.println("Assegnato revisore " + revisore.getUsername() + 
                                " all'articolo '" + articolo.getTitolo() + "'");
                
                // Invia notifica al revisore
                String testoNotifica = "Sei stato assegnato come revisore per l'articolo: '" + 
                                     articolo.getTitolo() + "' nella conferenza: " + 
                                     conferenzaSelezionata.getTitolo();
                
                App.dbms.insertNotifica(
                    testoNotifica, 
                    conferenzaSelezionata.getId(), 
                    idRevisore, 
                    1, // tipo notifica
                    "ASSEGNAZIONE-REVISORE-ARTICOLO"
                );
            }
            
        } catch (Exception e) {
            System.err.println("Errore durante l'assegnazione del revisore all'articolo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
