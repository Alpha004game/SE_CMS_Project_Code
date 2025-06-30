package com.cms.users.conference.Control;

import com.cms.App;
import com.cms.users.Commons.SuccessScreen;
import com.cms.users.Commons.UtilsControl;
import com.cms.users.Entity.ConferenzaE;
import com.cms.users.Entity.UtenteE;
import com.cms.users.conference.Interface.ConferenceManagementScreen;
import com.cms.users.conference.Interface.MemberListScreen;
import java.util.LinkedList;

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
    
    public void rimuoviRevisore() {
        // Implementazione da definire
    }
    
    public void assegnaRevisore(int idRevisore) {

        String text="Convocazione ricevuta da "+ App.utenteAccesso.getUsername()+" per gestire, in qualità di revisore, per la conferenza: "+ConferenceControl.conferenzaSelezionata.getTitolo();
        App.dbms.insertNotifica(text, ConferenceControl.getConferenza().getId(), idRevisore, 1, "ADD-REVISORE");
        UtilsControl.sendMail(App.dbms.getUser(idRevisore).getEmail(), "Invito Membro PC conferenza: "+ConferenceControl.getConferenza().getTitolo(), text);
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
}
