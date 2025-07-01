package com.cms.users.publications.Control;

import com.cms.users.Commons.DBMSBoundary;
import com.cms.users.Commons.ListScreen;
import com.cms.users.Commons.ListScreen.EditoreFunction;
import com.cms.users.Commons.ListScreen.UserRole;
import com.cms.users.Entity.ConferenzaE;
import com.cms.users.Entity.ArticoloE;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

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
        // Implementazione da definire
        return null;
    }
    
    public String downloadAllArticle(String idConferenza) {
        // Implementazione da definire
        return null;
    }
    
    public void sendNotice(String idArticolo) {
        // Implementazione da definire
    }
    
}
