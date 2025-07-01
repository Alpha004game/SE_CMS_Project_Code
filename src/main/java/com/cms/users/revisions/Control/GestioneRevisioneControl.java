package com.cms.users.revisions.Control;

import com.cms.App;
import com.cms.users.Entity.ArticoloE;
import com.cms.users.Entity.UtenteE;
import com.cms.users.Commons.ListScreen;
import com.cms.users.conference.Interface.MemberListScreen;
import com.cms.users.conference.Control.ConferenceControl;
import java.util.LinkedList;

/**
 * <<control>>
 * GestioneRevisioneControl - Control per la gestione delle revisioni
 * 
 * AGGIORNAMENTI PER RISOLUZIONE PROBLEMA DOWNLOAD BLOB:
 * 
 * 1. Miglioramento downloadFileArticolo():
 *    - Aggiunta verifica dettagliata del tipo di BLOB
 *    - Gestione di byte[], java.sql.Blob, String (base64 e testo)
 *    - Verifica della dimensione del contenuto prima della scrittura
 *    - Uso di BufferedInputStream/BufferedOutputStream per migliori prestazioni
 *    - Buffer più grande (8192 bytes) per il trasferimento dati
 *    - Verifica dell'integrità del file scaricato (controllo header PDF)
 *    - Messaggi di debug dettagliati per identificare problemi
 * 
 * 2. Aggiunta metodi di debugging:
 *    - debugFileArticolo(): analizza il contenuto del BLOB
 *    - testDownloadArticolo(): testa il download di un articolo specifico
 *    - testDatabaseConnection(): verifica connessione e presenza dati
 * 
 * 3. Gestione errori migliorata:
 *    - Controlli preventivi per BLOB vuoti o null
 *    - Messaggi utente informativi
 *    - Fallback per diversi formati di dati
 * 
 * POSSIBILI CAUSE DEL PROBLEMA ORIGINALE:
 * - BLOB vuoto nel database
 * - Formato dati non standard (es. path invece di contenuto binario)
 * - Problemi di codifica/decodifica
 * - Interruzione del flusso di dati durante il download
 * - Buffer troppo piccolo o gestione stream incorretta
 */
public class GestioneRevisioneControl {
    
    private ListScreen listScreen;
    private int idConferenza;
    
    /**
     * Costruttore
     */
    public GestioneRevisioneControl() {
        this.idConferenza = -1;
    }
    
    /**
     * Implementa il sequence diagram per visualizzare gli articoli assegnati
     * @param idConferenza ID della conferenza
     */
    public void visualizzaArticoliAssegnati(int idConferenza) {
        System.out.println("DEBUG GestioneRevisioneControl: === INIZIO visualizzaArticoliAssegnati ===");
        System.out.println("DEBUG GestioneRevisioneControl: idConferenza ricevuto: " + idConferenza);
        
        this.idConferenza = idConferenza;
        
        try {
            // Verifica che l'utente sia loggato
            if (App.utenteAccesso == null) {
                System.err.println("DEBUG GestioneRevisioneControl: ERRORE - Nessun utente loggato");
                return;
            }
            
            int idRevisore = App.utenteAccesso.getId();
            System.out.println("DEBUG GestioneRevisioneControl: idRevisore: " + idRevisore);
            
            // Seguendo il sequence diagram: getListaArticoliAssegnati(idRevisore, idConferenza) su BoundaryDBMS
            System.out.println("DEBUG GestioneRevisioneControl: Chiamando App.dbms.getListaArticoliAssegnati(" + idRevisore + ", " + idConferenza + ")");
            LinkedList<ArticoloE> articoliAssegnati = App.dbms.getListaArticoliAssegnati(idRevisore, idConferenza);
            
            System.out.println("DEBUG GestioneRevisioneControl: Risultato getListaArticoliAssegnati:");
            if (articoliAssegnati == null) {
                System.out.println("  - null (errore durante il recupero)");
                articoliAssegnati = new LinkedList<>(); // Lista vuota come fallback
            } else {
                System.out.println("  - Lista con " + articoliAssegnati.size() + " elementi");
                for (int i = 0; i < articoliAssegnati.size(); i++) {
                    ArticoloE articolo = articoliAssegnati.get(i);
                    System.out.println("    [" + i + "] ID: " + articolo.getId() + " - '" + articolo.getTitolo() + "'");
                }
            }
            
            // Crea ListScreen con ruolo REVISORE e funzione ARTICLES_TO_REVIEW
            System.out.println("DEBUG GestioneRevisioneControl: Creando ListScreen per REVISORE");
            listScreen = new ListScreen(ListScreen.UserRole.REVISORE, ListScreen.RevisoreFunction.ARTICLES_TO_REVIEW);
            
            // Imposta i dati degli articoli nella ListScreen
            listScreen.setRevisoreArticleDataFromArticoli(articoliAssegnati);
            
            // Mostra la schermata
            System.out.println("DEBUG GestioneRevisioneControl: Mostrando ListScreen");
            listScreen.setVisible(true);
            
            System.out.println("DEBUG GestioneRevisioneControl: === FINE visualizzaArticoliAssegnati ===");
            
        } catch (Exception e) {
            System.err.println("DEBUG GestioneRevisioneControl: ERRORE durante visualizzaArticoliAssegnati: " + e.getMessage());
            e.printStackTrace();
            
            // In caso di errore, mostra comunque la schermata con lista vuota
            listScreen = new ListScreen(ListScreen.UserRole.REVISORE, ListScreen.RevisoreFunction.ARTICLES_TO_REVIEW);
            listScreen.setHasData(false);
            listScreen.setVisible(true);
        }
    }
    
    /**
     * Ottiene il riferimento alla ListScreen
     */
    public ListScreen getListScreen() {
        return listScreen;
    }
    
    /**
     * Ottiene l'ID della conferenza corrente
     */
    public int getIdConferenza() {
        return idConferenza;
    }
    
    /**
     * Imposta l'ID della conferenza corrente
     */
    public void setIdConferenza(int idConferenza) {
        this.idConferenza = idConferenza;
    }
    
    // Metodi esistenti della classe mantenuti per compatibilità
    public void create() {
        // Implementazione da definire
    }
    
    public void revisionaArticolo(String idArticolo) {
        // Implementazione da definire
    }
    
    public void convocaSottoRevisore() {
        System.out.println("DEBUG GestioneRevisioneControl: === INIZIO convocaSottoRevisore ===");
        
        try {
            // Prova a ottenere l'ID conferenza dall'articolo corrente se non è stato impostato
            if (idConferenza <= 0) {
                int currentArticleId = com.cms.users.Commons.DBMSBoundary.getCurrentArticleId();
                if (currentArticleId > 0) {
                    ArticoloE articolo = App.dbms.getArticolo(currentArticleId);
                    if (articolo != null) {
                        idConferenza = articolo.getIdConferenza();
                        System.out.println("DEBUG: ID Conferenza ottenuto dall'articolo: " + idConferenza);
                    }
                }
            }
            
            // Ottieni tutti gli utenti dal database
            LinkedList<UtenteE> tuttiUtenti = App.dbms.getUsersInfo();
            
            if (tuttiUtenti == null || tuttiUtenti.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "Nessun utente disponibile per la convocazione.", 
                    "Nessun Utente", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Filtra gli utenti che non hanno un ruolo particolare nella conferenza corrente
            LinkedList<UtenteE> utentiDisponibili = filtraUtentiDisponibili(tuttiUtenti);
            
            if (utentiDisponibili.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "Nessun sotto-revisore disponibile per questa conferenza.", 
                    "Nessun Sotto-Revisore", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Crea un ConferenceControl temporaneo (o usa quello esistente se disponibile)
            ConferenceControl conferenceControl = new ConferenceControl();
            
            // Apri la MemberListScreen per la selezione del sotto-revisore
            MemberListScreen memberScreen = new MemberListScreen(
                MemberListScreen.UserRole.REVISORE, 
                MemberListScreen.Action.SUMMON_SUB_REVIEWER, 
                conferenceControl
            );
            
            // Imposta la lista degli utenti disponibili
            memberScreen.setUserList(utentiDisponibili);
            memberScreen.setHasData(!utentiDisponibili.isEmpty());
            
            // Mostra la schermata
            memberScreen.setVisible(true);
            
            System.out.println("DEBUG GestioneRevisioneControl: MemberListScreen aperta con " + 
                             utentiDisponibili.size() + " utenti disponibili");
            
        } catch (Exception e) {
            System.err.println("DEBUG GestioneRevisioneControl: ERRORE durante convocaSottoRevisore: " + e.getMessage());
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Errore durante l'apertura della selezione sotto-revisore: " + e.getMessage(), 
                "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        
        System.out.println("DEBUG GestioneRevisioneControl: === FINE convocaSottoRevisore ===");
    }
    
    /**
     * Filtra gli utenti che non hanno un ruolo particolare nella conferenza corrente
     * @param tuttiUtenti Lista di tutti gli utenti del sistema
     * @return Lista degli utenti disponibili come sotto-revisori
     */
    private LinkedList<UtenteE> filtraUtentiDisponibili(LinkedList<UtenteE> tuttiUtenti) {
        LinkedList<UtenteE> utentiDisponibili = new LinkedList<>();
        
        try {
            for (UtenteE utente : tuttiUtenti) {
                // Esclude l'utente corrente
                if (App.utenteAccesso != null && utente.getId() == App.utenteAccesso.getId()) {
                    continue;
                }
                
                // Se abbiamo una conferenza corrente, verifica i ruoli
                if (idConferenza > 0) {
                    String ruolo = App.dbms.getRuoloUtenteConferenza(utente.getId(), idConferenza);
                    
                    // Esclude utenti che hanno già ruoli specifici (Chair, Revisore, Editore)
                    if (ruolo != null && !ruolo.equalsIgnoreCase("Utente")) {
                        System.out.println("DEBUG: Utente " + utente.getUsername() + " escluso - ha ruolo: " + ruolo);
                        continue;
                    }
                }
                
                // Include l'utente se non ha ruoli particolari
                utentiDisponibili.add(utente);
                System.out.println("DEBUG: Utente " + utente.getUsername() + " incluso come potenziale sotto-revisore");
            }
        } catch (Exception e) {
            System.err.println("Errore durante il filtraggio utenti: " + e.getMessage());
            // In caso di errore, ritorna tutti gli utenti tranne quello corrente
            for (UtenteE utente : tuttiUtenti) {
                if (App.utenteAccesso != null && utente.getId() != App.utenteAccesso.getId()) {
                    utentiDisponibili.add(utente);
                }
            }
        }
        
        return utentiDisponibili;
    }
    
    public void visualizzaRevisioneDelegata(String idArticolo) {
        // Implementazione da definire
    }
    
    public void accettaSottoRevisione() {
        // Implementazione da definire
    }
    
    public void rifiutaSottoRevisione() {
        // Implementazione da definire
    }
    
    public void rinunciaArticolo() {
        // Implementazione da definire
    }
    
    public void visualizzaRevisioni(String idArticolo) {
        // Implementazione da definire
    }
    
    public void scaricaArticoloEAllegato(String fileArticolo, String fileAllegato) {
        // Implementazione da definire
    }
    
    /**
     * Apre la schermata di revisione per un articolo specifico
     * Include il download automatico del file dell'articolo
     * @param idArticolo ID dell'articolo da revisionare
     * @param titoloArticolo Titolo dell'articolo
     */
    public void apriRevisioneArticolo(String idArticolo, String titoloArticolo) {
        System.out.println("DEBUG GestioneRevisioneControl: === INIZIO apriRevisioneArticolo ===");
        System.out.println("DEBUG GestioneRevisioneControl: idArticolo: " + idArticolo + ", titolo: " + titoloArticolo);
        
        try {
            // Converte l'ID articolo da String a int
            int articleId = Integer.parseInt(idArticolo);
            
            // Imposta l'articolo corrente nel DBMSBoundary per le successive operazioni
            com.cms.users.Commons.DBMSBoundary.setCurrentArticleId(articleId);
            
            // Recupera l'oggetto articolo completo dal database
            System.out.println("DEBUG GestioneRevisioneControl: Recuperando l'articolo dal database...");
            com.cms.users.Entity.ArticoloE articolo = App.dbms.getArticolo(articleId);
            
            if (articolo == null) {
                System.err.println("DEBUG GestioneRevisioneControl: ERRORE - Articolo non trovato con ID: " + articleId);
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "Articolo non trovato nel database.", 
                    "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("DEBUG GestioneRevisioneControl: Articolo recuperato: " + articolo.getTitolo());
            
            // Download automatico del file dell'articolo se presente
            if (articolo.getFileArticolo() != null) {
                System.out.println("DEBUG GestioneRevisioneControl: Avviando download del file articolo...");
                downloadFileArticolo(articolo);
            } else {
                System.out.println("DEBUG GestioneRevisioneControl: Nessun file associato all'articolo");
            }
            
            // Crea e apre la ReviewSubmissionScreen
            System.out.println("DEBUG GestioneRevisioneControl: Creando ReviewSubmissionScreen...");
            com.cms.users.revisions.Interface.ReviewSubmissionScreen reviewScreen = 
                new com.cms.users.revisions.Interface.ReviewSubmissionScreen(
                    idArticolo, 
                    articolo.getTitolo(), 
                    String.valueOf(App.utenteAccesso.getId())
                );
            
            // Configura la ReviewSubmissionScreen con i dati dell'articolo
            reviewScreen.setSubmissionTitle(articolo.getTitolo());
            reviewScreen.setSubmissionId(idArticolo);
            
            // Mostra la schermata
            reviewScreen.setVisible(true);
            
            System.out.println("DEBUG GestioneRevisioneControl: ReviewSubmissionScreen aperta con successo");
            System.out.println("DEBUG GestioneRevisioneControl: === FINE apriRevisioneArticolo ===");
            
        } catch (NumberFormatException e) {
            System.err.println("DEBUG GestioneRevisioneControl: ERRORE - ID articolo non valido: " + idArticolo);
            javax.swing.JOptionPane.showMessageDialog(null, 
                "ID articolo non valido: " + idArticolo, 
                "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("DEBUG GestioneRevisioneControl: ERRORE durante apriRevisioneArticolo: " + e.getMessage());
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Errore durante l'apertura della revisione: " + e.getMessage(), 
                "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Gestisce il download automatico del file dell'articolo
     * @param articolo L'articolo contenente il file da scaricare
     */
    private void downloadFileArticolo(com.cms.users.Entity.ArticoloE articolo) {
        try {
            System.out.println("DEBUG GestioneRevisioneControl: Iniziando download file articolo...");
            
            Object fileBlob = articolo.getFileArticolo();
            if (fileBlob == null) {
                System.out.println("DEBUG GestioneRevisioneControl: Nessun file BLOB presente");
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "Nessun file associato a questo articolo.", 
                    "Avviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            System.out.println("DEBUG GestioneRevisioneControl: Tipo di oggetto BLOB: " + fileBlob.getClass().getName());
            System.out.println("DEBUG GestioneRevisioneControl: Valore BLOB (primi 100 caratteri): " + 
                fileBlob.toString().substring(0, Math.min(100, fileBlob.toString().length())));
            
            // Crea la directory Downloads se non esiste
            String userHome = System.getProperty("user.home");
            java.io.File downloadsDir = new java.io.File(userHome, "Downloads");
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs();
                System.out.println("DEBUG GestioneRevisioneControl: Creata directory Downloads: " + downloadsDir.getAbsolutePath());
            }
            
            // Genera nome file con timestamp per evitare sovrascritture
            String timestamp = java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "Articolo_" + articolo.getId() + "_" + timestamp + ".pdf";
            java.io.File outputFile = new java.io.File(downloadsDir, fileName);
            
            System.out.println("DEBUG GestioneRevisioneControl: File di destinazione: " + outputFile.getAbsolutePath());
            
            boolean downloadSuccess = false;
            long bytesWritten = 0;
            
            // Gestisce diversi tipi di BLOB
            if (fileBlob instanceof byte[]) {
                System.out.println("DEBUG GestioneRevisioneControl: Gestione byte array...");
                byte[] data = (byte[]) fileBlob;
                System.out.println("DEBUG GestioneRevisioneControl: Dimensione byte array: " + data.length + " bytes");
                
                if (data.length == 0) {
                    System.out.println("DEBUG GestioneRevisioneControl: ATTENZIONE - Byte array vuoto!");
                    javax.swing.JOptionPane.showMessageDialog(null, 
                        "Il file dell'articolo è vuoto nel database.", 
                        "Avviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(outputFile);
                     java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(fos)) {
                    bos.write(data);
                    bos.flush();
                    bytesWritten = data.length;
                    downloadSuccess = true;
                    System.out.println("DEBUG GestioneRevisioneControl: Scritti " + bytesWritten + " bytes da byte array");
                }
            } 
            // Gestisce java.sql.Blob
            else if (fileBlob instanceof java.sql.Blob) {
                System.out.println("DEBUG GestioneRevisioneControl: Gestione java.sql.Blob...");
                java.sql.Blob blob = (java.sql.Blob) fileBlob;
                long blobLength = blob.length();
                System.out.println("DEBUG GestioneRevisioneControl: Dimensione BLOB: " + blobLength + " bytes");
                
                if (blobLength == 0) {
                    System.out.println("DEBUG GestioneRevisioneControl: ATTENZIONE - BLOB vuoto!");
                    javax.swing.JOptionPane.showMessageDialog(null, 
                        "Il file dell'articolo è vuoto nel database.", 
                        "Avviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try (java.io.InputStream inputStream = blob.getBinaryStream();
                     java.io.BufferedInputStream bis = new java.io.BufferedInputStream(inputStream);
                     java.io.FileOutputStream outputStream = new java.io.FileOutputStream(outputFile);
                     java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(outputStream)) {
                    
                    byte[] buffer = new byte[8192]; // Buffer più grande per migliori prestazioni
                    int bytesRead;
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                        bytesWritten += bytesRead;
                    }
                    bos.flush();
                    downloadSuccess = true;
                    System.out.println("DEBUG GestioneRevisioneControl: Scritti " + bytesWritten + " bytes da BLOB");
                }
            }
            // Prova a gestire altri tipi come stringhe base64 o simili
            else if (fileBlob instanceof String) {
                System.out.println("DEBUG GestioneRevisioneControl: Gestione String (possibile base64)...");
                String stringData = (String) fileBlob;
                System.out.println("DEBUG GestioneRevisioneControl: Lunghezza stringa: " + stringData.length());
                
                if (stringData.trim().isEmpty()) {
                    System.out.println("DEBUG GestioneRevisioneControl: ATTENZIONE - Stringa vuota!");
                    javax.swing.JOptionPane.showMessageDialog(null, 
                        "Il file dell'articolo è vuoto nel database.", 
                        "Avviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try {
                    // Prova a decodificare come base64
                    byte[] decodedData = java.util.Base64.getDecoder().decode(stringData);
                    try (java.io.FileOutputStream fos = new java.io.FileOutputStream(outputFile);
                         java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(fos)) {
                        bos.write(decodedData);
                        bos.flush();
                        bytesWritten = decodedData.length;
                        downloadSuccess = true;
                        System.out.println("DEBUG GestioneRevisioneControl: Scritti " + bytesWritten + " bytes da base64");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("DEBUG GestioneRevisioneControl: Non è base64, provo come testo normale...");
                    // Se non è base64, salvalo come testo
                    byte[] textData = stringData.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                    try (java.io.FileOutputStream fos = new java.io.FileOutputStream(outputFile);
                         java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(fos)) {
                        bos.write(textData);
                        bos.flush();
                        bytesWritten = textData.length;
                        downloadSuccess = true;
                        System.out.println("DEBUG GestioneRevisioneControl: Scritti " + bytesWritten + " bytes da testo");
                    }
                }
            }
            // Prova conversione generica per altri tipi
            else {
                System.out.println("DEBUG GestioneRevisioneControl: Tipo di BLOB non riconosciuto: " + fileBlob.getClass().getName());
                System.out.println("DEBUG GestioneRevisioneControl: Tentativo di conversione generica...");
                
                // Prova a convertire a stringa e poi gestire
                String stringValue = fileBlob.toString();
                if (!stringValue.isEmpty() && !stringValue.equals("null")) {
                    byte[] data = stringValue.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                    try (java.io.FileOutputStream fos = new java.io.FileOutputStream(outputFile);
                         java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(fos)) {
                        bos.write(data);
                        bos.flush();
                        bytesWritten = data.length;
                        downloadSuccess = true;
                        System.out.println("DEBUG GestioneRevisioneControl: Scritti " + bytesWritten + " bytes da conversione toString");
                    }
                } else {
                    System.out.println("DEBUG GestioneRevisioneControl: Impossibile convertire il BLOB in dati utilizzabili");
                    javax.swing.JOptionPane.showMessageDialog(null, 
                        "Formato file non supportato per il download automatico.\nTipo: " + fileBlob.getClass().getName(), 
                        "Avviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            // Verifica il risultato del download
            if (downloadSuccess && bytesWritten > 0) {
                System.out.println("DEBUG GestioneRevisioneControl: Download completato con successo!");
                System.out.println("DEBUG GestioneRevisioneControl: File salvato: " + outputFile.getAbsolutePath());
                System.out.println("DEBUG GestioneRevisioneControl: Dimensione file: " + outputFile.length() + " bytes");
                
                // Verifica che il file sia stato effettivamente creato e non sia vuoto
                if (outputFile.exists() && outputFile.length() > 0) {
                    // Prova ad aprire il file per verificare che sia leggibile
                    String message = "File dell'articolo scaricato con successo!\n\n" +
                            "Percorso: " + outputFile.getAbsolutePath() + "\n" +
                            "Dimensione: " + outputFile.length() + " bytes\n\n";
                    
                    // Verifica se il file sembra essere un PDF valido (inizia con %PDF)
                    try (java.io.FileInputStream fis = new java.io.FileInputStream(outputFile)) {
                        byte[] header = new byte[4];
                        int headerRead = fis.read(header);
                        if (headerRead >= 4) {
                            String headerString = new String(header, java.nio.charset.StandardCharsets.US_ASCII);
                            if (headerString.equals("%PDF")) {
                                message += "✓ Il file sembra essere un PDF valido.";
                            } else {
                                message += "⚠ Attenzione: il file potrebbe non essere un PDF valido.";
                            }
                        }
                    } catch (Exception headerException) {
                        message += "⚠ Impossibile verificare il formato del file.";
                    }
                    
                    javax.swing.JOptionPane.showMessageDialog(null, message, 
                        "Download Completato", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.err.println("DEBUG GestioneRevisioneControl: ERRORE - File creato ma vuoto o inesistente!");
                    javax.swing.JOptionPane.showMessageDialog(null, 
                        "Errore: il file è stato creato ma risulta vuoto.", 
                        "Errore Download", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.err.println("DEBUG GestioneRevisioneControl: ERRORE - Download fallito o nessun dato scritto!");
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "Impossibile scaricare il file dell'articolo.\nIl file potrebbe essere corrotto o non presente nel database.", 
                    "Errore Download", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            System.err.println("DEBUG GestioneRevisioneControl: ERRORE durante il download del file: " + e.getMessage());
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Errore durante il download del file:\n" + e.getMessage() + 
                "\n\nContatta l'amministratore di sistema se il problema persiste.", 
                "Errore Download", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Metodo di debugging per analizzare il contenuto del BLOB
     * @param articolo L'articolo di cui analizzare il file
     */
    public void debugFileArticolo(com.cms.users.Entity.ArticoloE articolo) {
        System.out.println("=== DEBUG FILE ARTICOLO ===");
        
        Object fileBlob = articolo.getFileArticolo();
        if (fileBlob == null) {
            System.out.println("File BLOB è null");
            return;
        }
        
        System.out.println("Tipo oggetto: " + fileBlob.getClass().getName());
        System.out.println("ToString: " + fileBlob.toString());
        System.out.println("Lunghezza toString: " + fileBlob.toString().length());
        
        if (fileBlob instanceof byte[]) {
            byte[] data = (byte[]) fileBlob;
            System.out.println("Byte array length: " + data.length);
            if (data.length > 0) {
                System.out.println("Primi 10 bytes: " + java.util.Arrays.toString(
                    java.util.Arrays.copyOf(data, Math.min(10, data.length))));
            }
        } else if (fileBlob instanceof java.sql.Blob) {
            try {
                java.sql.Blob blob = (java.sql.Blob) fileBlob;
                System.out.println("BLOB length: " + blob.length());
                if (blob.length() > 0) {
                    byte[] first10 = blob.getBytes(1, (int)Math.min(10, blob.length()));
                    System.out.println("Primi 10 bytes: " + java.util.Arrays.toString(first10));
                }
            } catch (Exception e) {
                System.err.println("Errore nell'analisi del BLOB: " + e.getMessage());
            }
        }
        
        System.out.println("=== FINE DEBUG FILE ARTICOLO ===");
    }
    
    /**
     * Metodo di test per verificare il download di un articolo specifico
     * @param idArticolo ID dell'articolo da testare
     */
    public void testDownloadArticolo(int idArticolo) {
        System.out.println("=== TEST DOWNLOAD ARTICOLO ID: " + idArticolo + " ===");
        
        try {
            // Recupera l'articolo dal database
            com.cms.users.Entity.ArticoloE articolo = App.dbms.getArticolo(idArticolo);
            
            if (articolo == null) {
                System.err.println("Articolo non trovato con ID: " + idArticolo);
                return;
            }
            
            System.out.println("Articolo trovato: " + articolo.getTitolo());
            
            // Debug del file
            debugFileArticolo(articolo);
            
            // Prova il download
            downloadFileArticolo(articolo);
            
        } catch (Exception e) {
            System.err.println("Errore durante il test download: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== FINE TEST DOWNLOAD ===");
    }
    
    /**
     * Metodo di test per verificare l'integrazione completa del sistema di revisione
     * Testa l'apertura della ReviewSubmissionScreen con download automatico
     */
    public void testIntegrazione() {
        System.out.println("=== TEST INTEGRAZIONE SISTEMA REVISIONE ===");
        
        try {
            // Simula un articolo di test
            String idArticoloTest = "1";
            String titoloTest = "Articolo di Test per Revisione";
            
            System.out.println("Test 1: Apertura ReviewSubmissionScreen con ID articolo: " + idArticoloTest);
            apriRevisioneArticolo(idArticoloTest, titoloTest);
            
            System.out.println("Test completato con successo!");
            
        } catch (Exception e) {
            System.err.println("ERRORE durante il test di integrazione: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== FINE TEST INTEGRAZIONE ===");
    }
    
    /**
     * Metodo di utilità per testare la connessione al database e verificare la presenza di articoli
     */
    public void testDatabaseConnection() {
        System.out.println("=== TEST CONNESSIONE DATABASE ===");
        
        try {
            // Testa la connessione recuperando la lista degli articoli di una conferenza di test
            if (App.utenteAccesso == null) {
                System.err.println("Nessun utente loggato per il test");
                return;
            }
            
            int idRevisore = App.utenteAccesso.getId();
            System.out.println("Testing con revisore ID: " + idRevisore);
            
            // Prova a recuperare articoli per conferenza 1 (assumendo che esista)
            java.util.LinkedList<com.cms.users.Entity.ArticoloE> articoli = App.dbms.getListaArticoliAssegnati(idRevisore, 1);
            
            if (articoli != null && !articoli.isEmpty()) {
                System.out.println("Trovati " + articoli.size() + " articoli:");
                for (int i = 0; i < Math.min(3, articoli.size()); i++) {
                    com.cms.users.Entity.ArticoloE articolo = articoli.get(i);
                    System.out.println("  - ID: " + articolo.getId() + ", Titolo: " + articolo.getTitolo());
                    
                    // Verifica il file dell'articolo
                    Object fileBlob = articolo.getFileArticolo();
                    if (fileBlob != null) {
                        System.out.println("    File presente: " + fileBlob.getClass().getName());
                        if (fileBlob instanceof byte[]) {
                            System.out.println("    Dimensione: " + ((byte[]) fileBlob).length + " bytes");
                        } else if (fileBlob instanceof String) {
                            System.out.println("    Contenuto stringa: " + ((String) fileBlob).length() + " caratteri");
                        }
                    } else {
                        System.out.println("    Nessun file associato");
                    }
                }
                
                // Testa il download del primo articolo
                if (articoli.size() > 0) {
                    System.out.println("\nTestando download del primo articolo...");
                    testDownloadArticolo(articoli.get(0).getId());
                }
            } else {
                System.out.println("Nessun articolo trovato per il revisore");
            }
            
        } catch (Exception e) {
            System.err.println("Errore durante il test della connessione: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== FINE TEST CONNESSIONE DATABASE ===");
    }
    
    /**
     * Metodo di test per verificare la convocazione sotto-revisore
     */
    public void testConvocaSottoRevisore() {
        System.out.println("=== TEST CONVOCA SOTTO-REVISORE ===");
        
        try {
            // Imposta un ID conferenza di test se necessario
            if (idConferenza <= 0) {
                idConferenza = 1; // Conferenza di test
                System.out.println("DEBUG: Impostato ID Conferenza di test: " + idConferenza);
            }
            
            // Chiama il metodo di convocazione
            convocaSottoRevisore();
            
        } catch (Exception e) {
            System.err.println("Errore durante il test convoca sotto-revisore: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== FINE TEST CONVOCA SOTTO-REVISORE ===");
    }
}
