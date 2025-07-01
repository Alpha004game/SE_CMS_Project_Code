package com.cms.users.publications.Control;

import com.cms.users.Commons.DBMSBoundary;
import com.cms.users.Commons.ListScreen;
import com.cms.users.Commons.ListScreen.EditoreFunction;
import com.cms.users.Commons.ListScreen.UserRole;
import com.cms.users.Commons.UtilsControl;
import com.cms.users.Entity.ConferenzaE;
import com.cms.users.Entity.ArticoloE;
import com.cms.users.Entity.UtenteE;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <<control>>
 * PubblicazioneControl - Gestisce le operazioni di pubblicazione
 */
public class PubblicazioneControl {
    
    private DBMSBoundary dbmsBoundary;
    
    public PubblicazioneControl() {
        this.dbmsBoundary = new DBMSBoundary();
    }
    
    /**
     * Apre l'interfaccia editoriale per visualizzare gli articoli accettati
     * Seguendo il sequence diagram: HomeScreen -> PubblicazioneControl -> DBMSBoundary -> ListScreen
     */
    public void apriInterfacciaEditoriale(int idConferenza) {
        System.out.println("DEBUG: PubblicazioneControl.apriInterfacciaEditoriale - ID Conferenza: " + idConferenza);
        
        try {
            // 1. Ottieni le informazioni della conferenza
            ConferenzaE conferenza = (ConferenzaE) dbmsBoundary.getConferenceInfo(idConferenza);
            if (conferenza == null) {
                JOptionPane.showMessageDialog(null, 
                    "Errore: Conferenza non trovata.", 
                    "Errore", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("DEBUG: Conferenza trovata: " + conferenza.getTitolo());
            
            // 2. Verifica se la deadline di pubblicazione è passata
            // Per ora assumiamo che sia sempre passata, ma in una implementazione reale
            // si dovrebbe controllare: if (isAfterPublicationDeadline(conferenza))
            
            // 3. Ottieni gli articoli accettati dal database
            LinkedList<ArticoloE> articoliAccettatiRaw = dbmsBoundary.getAcceptedArticles(idConferenza);
            
            // 4. Converti e prepara i dati per la visualizzazione
            List<ListScreen.EditoreArticleData> articoliAccettati = new ArrayList<>();
            
            if (articoliAccettatiRaw != null && !articoliAccettatiRaw.isEmpty()) {
                for (Object obj : articoliAccettatiRaw) {
                    if (obj instanceof ArticoloE) {
                        ArticoloE articolo = (ArticoloE) obj;
                        ListScreen.EditoreArticleData articleData = new ListScreen.EditoreArticleData(
                            String.valueOf(articolo.getId()),
                            articolo.getTitolo(),
                            "Autori non specificati" // Placeholder per gli autori
                        );
                        articoliAccettati.add(articleData);
                        System.out.println("DEBUG: Articolo accettato caricato: " + articolo.getTitolo());
                    }
                }
            }
            
            // 5. Crea e mostra il ListScreen per l'editore
            ListScreen editorScreen = new ListScreen(UserRole.EDITORE, EditoreFunction.ACCEPTED_ARTICLES);
            
            // Imposta l'ID della conferenza per il download
            editorScreen.setConferenceId(idConferenza);
            
            if (articoliAccettati.isEmpty()) {
                System.out.println("DEBUG: Nessun articolo accettato trovato per la conferenza");
                JOptionPane.showMessageDialog(null, 
                    "Non ci sono articoli accettati per questa conferenza.", 
                    "Informazione", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("DEBUG: Caricamento di " + articoliAccettati.size() + " articoli accettati");
                editorScreen.setAcceptedArticles(articoliAccettati);
            }
            
            editorScreen.setVisible(true);
            
        } catch (Exception e) {
            System.err.println("ERRORE in apriInterfacciaEditoriale: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Errore nell'apertura dell'interfaccia editoriale: " + e.getMessage(), 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Verifica se la deadline di pubblicazione è passata
     * In una implementazione reale, questo controllerebbe la data effettiva
     */
    @SuppressWarnings("unused")
    private boolean isAfterPublicationDeadline(ConferenzaE conferenza) {
        // Per ora restituisce sempre true
        // In futuro: return new Date().after(conferenza.getDeadlinePubblicazione());
        return true;
    }
    
    // Metodi esistenti
    public void create() {
        // Implementazione da definire
    }
    
    public String downloadArticle(String idArticolo) {
        System.out.println("DEBUG: PubblicazioneControl.downloadArticle - ID Articolo: " + idArticolo);
        
        try {
            int articleId = Integer.parseInt(idArticolo);
            
            // 1. Ottieni il file dell'articolo dal DBMSBoundary
            System.out.println("DEBUG: Chiamando getArticleFile per articolo: " + articleId);
            LinkedList<Object> articleFileRaw = dbmsBoundary.getArticleFile(articleId);
            
            if (articleFileRaw == null || articleFileRaw.isEmpty()) {
                System.out.println("DEBUG: Nessun file trovato per l'articolo " + articleId);
                return "Errore: Nessun file trovato per questo articolo.";
            }
            
            // 2. Processa il file e salvalo
            String filePath = saveArticleFileSingle(articleFileRaw.get(0), articleId);
            
            if (filePath != null) {
                System.out.println("DEBUG: Articolo scaricato con successo: " + filePath);
                return "Articolo scaricato con successo: " + filePath;
            } else {
                System.err.println("ERRORE: Impossibile salvare l'articolo");
                return "Errore: Impossibile salvare l'articolo.";
            }
            
        } catch (NumberFormatException e) {
            System.err.println("ERRORE: ID articolo non valido: " + idArticolo);
            return "Errore: ID articolo non valido.";
        } catch (Exception e) {
            System.err.println("ERRORE in downloadArticle: " + e.getMessage());
            e.printStackTrace();
            return "Errore durante il download: " + e.getMessage();
        }
    }
    
    public String downloadAllArticle(String idConferenza) {
        System.out.println("DEBUG: PubblicazioneControl.downloadAllArticle - ID Conferenza: " + idConferenza);
        
        try {
            int conferenceId = Integer.parseInt(idConferenza);
            
            // 1. Ottieni tutti i file degli articoli accettati dal DBMSBoundary
            System.out.println("DEBUG: Chiamando getAllArticleFile per conferenza: " + conferenceId);
            Object articleFilesRaw = dbmsBoundary.getAllArticleFile(conferenceId);
            
            if (articleFilesRaw == null) {
                System.out.println("DEBUG: Nessun file articolo trovato per la conferenza");
                return "Errore: Nessun file articolo trovato per questa conferenza.";
            }
            
            // 2. Processa i file e crea il PDF unificato
            String filePath = saveArticleFile(articleFilesRaw, conferenceId);
            
            if (filePath != null) {
                System.out.println("DEBUG: PDF unificato creato con successo: " + filePath);
                return "PDF scaricato con successo: " + filePath;
            } else {
                System.err.println("ERRORE: Impossibile creare il PDF unificato");
                return "Errore: Impossibile creare il PDF unificato.";
            }
            
        } catch (NumberFormatException e) {
            System.err.println("ERRORE: ID conferenza non valido: " + idConferenza);
            return "Errore: ID conferenza non valido.";
        } catch (Exception e) {
            System.err.println("ERRORE in downloadAllArticle: " + e.getMessage());
            e.printStackTrace();
            return "Errore durante il download: " + e.getMessage();
        }
    }
    
    /**
     * Invia comunicazioni agli autori di un articolo specifico
     * Segue il sequence diagram per la gestione delle notifiche personalizzate
     */
    public void sendNotice(String idArticolo) {
        System.out.println("DEBUG: PubblicazioneControl.sendNotice - ID Articolo: " + idArticolo);
        
        try {
            int articleId = Integer.parseInt(idArticolo);
            
            // 1. Ottieni gli autori dell'articolo dal DBMSBoundary (getArticleAuthors)
            System.out.println("DEBUG: Chiamando getArticleAuthors per articolo: " + articleId);
            LinkedList<UtenteE> authorsList = dbmsBoundary.getArticleAuthors(articleId);
            
            if (authorsList == null || authorsList.isEmpty()) {
                System.out.println("DEBUG: Nessun autore trovato per l'articolo " + articleId);
                JOptionPane.showMessageDialog(null, 
                    "Nessun autore trovato per questo articolo.", 
                    "Informazione", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            System.out.println("DEBUG: Trovati " + authorsList.size() + " autori per l'articolo");
            
            // 2. Crea e mostra la SendCustomNotificationScreen
            createAndShowNotificationScreen(authorsList, articleId);
            
        } catch (NumberFormatException e) {
            System.err.println("ERRORE: ID articolo non valido: " + idArticolo);
            JOptionPane.showMessageDialog(null, 
                "Errore: ID articolo non valido.", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("ERRORE in sendNotice: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Errore nell'invio della comunicazione: " + e.getMessage(), 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Crea e mostra la schermata di notifica personalizzata
     * Implementa la SendCustomNotificationScreen del sequence diagram
     */
    private void createAndShowNotificationScreen(LinkedList<UtenteE> authorsList, int articleId) {
        // Crea il dialog per l'invio della notifica
        JDialog notificationDialog = new JDialog((JDialog) null, "Invia Comunicazione agli Autori", true);
        notificationDialog.setSize(600, 400);
        notificationDialog.setLocationRelativeTo(null);
        notificationDialog.setLayout(new BorderLayout(10, 10));
        
        // Panel delle informazioni superiori
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        infoPanel.add(new JLabel("Destinatari: " + getAuthorsDisplayString(authorsList)));
        infoPanel.add(new JLabel("Oggetto:"));
        
        JTextField subjectField = new JTextField("Comunicazione riguardo l'articolo ID: " + articleId);
        infoPanel.add(subjectField);
        
        // Panel del messaggio
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(new JLabel("Messaggio:"), BorderLayout.NORTH);
        
        JTextArea messageArea = new JTextArea(10, 50);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setText("Gentili Autori,\n\nVi scriviamo in merito al vostro articolo sottomesso alla conferenza.\n\nCordiali saluti,\nL'Editore");
        
        JScrollPane scrollPane = new JScrollPane(messageArea);
        messagePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel dei pulsanti
        JPanel buttonPanel = new JPanel();
        JButton sendButton = new JButton("Invia");
        JButton cancelButton = new JButton("Annulla");
        buttonPanel.add(sendButton);
        buttonPanel.add(cancelButton);
        
        // Aggiungi i panel al dialog
        notificationDialog.add(infoPanel, BorderLayout.NORTH);
        notificationDialog.add(messagePanel, BorderLayout.CENTER);
        notificationDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Event handlers
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendNotificationsToAuthors(authorsList, articleId, subjectField.getText(), messageArea.getText());
                notificationDialog.dispose();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notificationDialog.dispose();
            }
        });
        
        // Mostra il dialog
        notificationDialog.setVisible(true);
    }
    
    /**
     * Crea una stringa per mostrare i nomi degli autori
     * Utilizza username e email dato che UtenteE non ha getNome/getCognome
     */
    private String getAuthorsDisplayString(LinkedList<UtenteE> authorsList) {
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < authorsList.size(); i++) {
            UtenteE author = authorsList.get(i);
            display.append(author.getUsername()).append(" (").append(author.getEmail()).append(")");
            if (i < authorsList.size() - 1) {
                display.append(", ");
            }
        }
        return display.toString();
    }
    
    /**
     * Invia le notifiche a tutti gli autori
     * Implementa il loop del sequence diagram: sendmail + insertNotification
     */
    private void sendNotificationsToAuthors(LinkedList<UtenteE> authorsList, int articleId, String subject, String message) {
        if (subject.trim().isEmpty() || message.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Oggetto e messaggio sono obbligatori.", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int successCount = 0;
        int totalCount = authorsList.size();
        
        // Loop attraverso tutti gli autori (come specificato nel sequence diagram)
        for (UtenteE author : authorsList) {
            try {
                System.out.println("DEBUG: Invio notifica all'autore: " + author.getUsername() + " (" + author.getEmail() + ")");
                
                // 1. Invia email utilizzando UtilsControl.sendMail (seguendo il sequence diagram)
                boolean emailSent = UtilsControl.sendMail(author.getEmail(), subject, message);
                
                if (emailSent) {
                    System.out.println("DEBUG: Email inviata con successo a: " + author.getEmail());
                    
                    // 2. Inserisci notifica nel database tramite DBMSBoundary.insertNotification
                    String dettagli = "Articolo ID: " + articleId + " - " + subject;
                    // Tipo 1 = notifica generale agli autori
                    dbmsBoundary.insertNotifica(message, getConferenceIdFromArticle(articleId), author.getId(), 1, dettagli);
                    
                    System.out.println("DEBUG: Notifica inserita nel database per utente ID: " + author.getId());
                    successCount++;
                } else {
                    System.err.println("ERRORE: Impossibile inviare email a: " + author.getEmail());
                }
                
            } catch (Exception e) {
                System.err.println("ERRORE nell'invio notifica all'autore " + author.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // Mostra risultato finale
        if (successCount == totalCount) {
            JOptionPane.showMessageDialog(null, 
                "Comunicazione inviata con successo a tutti gli autori (" + successCount + "/" + totalCount + ").", 
                "Successo", 
                JOptionPane.INFORMATION_MESSAGE);
        } else if (successCount > 0) {
            JOptionPane.showMessageDialog(null, 
                "Comunicazione inviata parzialmente: " + successCount + "/" + totalCount + " autori raggiunti.", 
                "Attenzione", 
                JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                "Impossibile inviare la comunicazione a nessun autore.", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Ottiene l'ID della conferenza a partire dall'ID dell'articolo
     * Utilizza DBMSBoundary per recuperare le informazioni dell'articolo
     */
    private int getConferenceIdFromArticle(int articleId) {
        try {
            // Cerca di ottenere l'articolo per recuperare l'ID della conferenza
            ArticoloE articolo = dbmsBoundary.getArticolo(articleId);
            if (articolo != null) {
                // Assumiamo che ArticoloE abbia un metodo per ottenere l'ID conferenza
                // Per ora restituiamo 1 come placeholder
                System.out.println("DEBUG: Recuperato articolo, usando conferenza di default");
                return 1; // Placeholder - in una implementazione reale si userebbe articolo.getIdConferenza()
            }
        } catch (Exception e) {
            System.err.println("ERRORE nel recupero ID conferenza: " + e.getMessage());
        }
        
        // Valore di fallback
        return 1;
    }
    
    /**
     * Salva i file degli articoli unendoli in un unico PDF
     * Segue il sequence diagram per la gestione dei file
     */
    private String saveArticleFile(Object articleFilesRaw, int conferenceId) {
        System.out.println("DEBUG: PubblicazioneControl.saveArticleFile - Inizio elaborazione");
        
        try {
            // 1. Ottieni la directory Downloads dell'utente
            String userHome = System.getProperty("user.home");
            Path downloadsPath = Paths.get(userHome, "Downloads");
            
            // Assicurati che la directory Downloads esista
            if (!Files.exists(downloadsPath)) {
                Files.createDirectories(downloadsPath);
            }
            
            // 2. Crea il nome del file con timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "Articoli_Conferenza_" + conferenceId + "_" + timestamp + ".pdf";
            Path outputPath = downloadsPath.resolve(fileName);
            
            // 3. Processa i file degli articoli
            List<byte[]> pdfFiles = extractPdfFiles(articleFilesRaw);
            
            if (pdfFiles.isEmpty()) {
                System.out.println("DEBUG: Nessun file PDF valido trovato");
                return null;
            }
            
            // 4. Per ora, salva i file separatamente (in futuro si potrebbe implementare l'unione PDF)
            if (pdfFiles.size() == 1) {
                // Un solo file, salvalo direttamente
                Files.write(outputPath, pdfFiles.get(0), StandardOpenOption.CREATE);
                System.out.println("DEBUG: File singolo salvato: " + outputPath.toString());
            } else {
                // Più file - per ora creiamo un ZIP o salviamo il primo
                // In una implementazione reale si userebbe una libreria PDF come iText
                Files.write(outputPath, pdfFiles.get(0), StandardOpenOption.CREATE);
                System.out.println("DEBUG: Primo file di " + pdfFiles.size() + " salvato: " + outputPath.toString());
                System.out.println("NOTA: In una implementazione completa tutti i PDF verrebbero uniti");
            }
            
            return outputPath.toString();
            
        } catch (IOException e) {
            System.err.println("ERRORE IO in saveArticleFile: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("ERRORE GENERICO in saveArticleFile: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Estrae i file PDF dai dati grezzi del database
     */
    private List<byte[]> extractPdfFiles(Object articleFilesRaw) {
        List<byte[]> pdfFiles = new ArrayList<>();
        
        try {
            if (articleFilesRaw instanceof List<?>) {
                List<?> filesList = (List<?>) articleFilesRaw;
                
                for (Object fileObj : filesList) {
                    if (fileObj instanceof byte[]) {
                        byte[] fileData = (byte[]) fileObj;
                        if (isPdfFile(fileData)) {
                            pdfFiles.add(fileData);
                            System.out.println("DEBUG: File PDF valido aggiunto, dimensione: " + fileData.length + " bytes");
                        }
                    } else if (fileObj instanceof Object[]) {
                        // Se i file sono in un array di oggetti
                        Object[] fileArray = (Object[]) fileObj;
                        for (Object file : fileArray) {
                            if (file instanceof byte[]) {
                                byte[] fileData = (byte[]) file;
                                if (isPdfFile(fileData)) {
                                    pdfFiles.add(fileData);
                                    System.out.println("DEBUG: File PDF valido aggiunto da array, dimensione: " + fileData.length + " bytes");
                                }
                            }
                        }
                    }
                }
            } else if (articleFilesRaw instanceof byte[]) {
                // Un singolo file
                byte[] fileData = (byte[]) articleFilesRaw;
                if (isPdfFile(fileData)) {
                    pdfFiles.add(fileData);
                    System.out.println("DEBUG: File PDF singolo aggiunto, dimensione: " + fileData.length + " bytes");
                }
            } else {
                System.out.println("DEBUG: Tipo di dati file non riconosciuto: " + 
                    (articleFilesRaw != null ? articleFilesRaw.getClass().getName() : "null"));
            }
            
        } catch (Exception e) {
            System.err.println("ERRORE in extractPdfFiles: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("DEBUG: Estratti " + pdfFiles.size() + " file PDF validi");
        return pdfFiles;
    }
    
    /**
     * Verifica se i dati rappresentano un file PDF valido
     */
    private boolean isPdfFile(byte[] fileData) {
        if (fileData == null || fileData.length < 4) {
            return false;
        }
        
        // Controlla la signature PDF (%PDF)
        return fileData[0] == 0x25 && // %
               fileData[1] == 0x50 && // P
               fileData[2] == 0x44 && // D
               fileData[3] == 0x46;   // F
    }
    
    /**
     * Salva un singolo file articolo
     * Segue il sequence diagram per la gestione del singolo file
     */
    private String saveArticleFileSingle(Object articleFileRaw, int articleId) {
        System.out.println("DEBUG: PubblicazioneControl.saveArticleFileSingle - Inizio elaborazione articolo " + articleId);
        
        try {
            // 1. Ottieni la directory Downloads dell'utente
            String userHome = System.getProperty("user.home");
            Path downloadsPath = Paths.get(userHome, "Downloads");
            
            // Assicurati che la directory Downloads esista
            if (!Files.exists(downloadsPath)) {
                Files.createDirectories(downloadsPath);
            }
            
            // 2. Estrai i dati del file
            byte[] fileData = extractSingleFileData(articleFileRaw);
            
            if (fileData == null || fileData.length == 0) {
                System.out.println("DEBUG: Nessun dato file valido per l'articolo " + articleId);
                return null;
            }
            
            // 3. Crea il nome del file con timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "Articolo_" + articleId + "_" + timestamp + ".pdf";
            Path outputPath = downloadsPath.resolve(fileName);
            
            // 4. Salva il file
            Files.write(outputPath, fileData, StandardOpenOption.CREATE);
            System.out.println("DEBUG: Articolo salvato: " + outputPath.toString());
            
            return outputPath.toString();
            
        } catch (IOException e) {
            System.err.println("ERRORE IO in saveArticleFileSingle: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("ERRORE GENERICO in saveArticleFileSingle: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Estrae i dati di un singolo file dall'oggetto del database
     */
    private byte[] extractSingleFileData(Object fileRaw) {
        try {
            if (fileRaw instanceof byte[]) {
                byte[] fileData = (byte[]) fileRaw;
                if (isPdfFile(fileData)) {
                    System.out.println("DEBUG: File PDF valido trovato, dimensione: " + fileData.length + " bytes");
                    return fileData;
                } else {
                    System.out.println("DEBUG: File non è un PDF valido");
                    // Restituisci comunque i dati se non è un PDF (potrebbe essere un altro formato)
                    return fileData;
                }
            } else if (fileRaw instanceof String) {
                // Se il file è memorizzato come stringa (base64 o altro)
                System.out.println("DEBUG: File memorizzato come stringa, conversione necessaria");
                // Per ora restituisci null, in futuro si potrebbe implementare la decodifica
                return null;
            } else {
                System.out.println("DEBUG: Tipo di dato file non riconosciuto: " + 
                    (fileRaw != null ? fileRaw.getClass().getName() : "null"));
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("ERRORE in extractSingleFileData: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
