# Conference Management System (CMS) - Implementazione Java

Questo repository contiene il codice sorgente di un'applicazione desktop per la gestione di conferenze accademiche (Conference Management System), sviluppata in **Java** come parte del corso di "Ingegneria del Software".

L'applicazione è il risultato finale della fase di implementazione di un ciclo di vita di sviluppo software completo e la sua architettura e le sue funzionalità sono state guidate da specifici documenti di progettazione:
*   *Requirement Analysis Document (RAD)*
*   *System Design Document (SDD)*
*   *Object Design Document (ODD)*

Il focus di questo repository è l'implementazione pratica di tali design in un'applicazione funzionante.

## Descrizione dell'Applicazione

Il software è un'applicazione desktop stand-alone, con un'interfaccia grafica sviluppata utilizzando la libreria **Java Swing**. Permette la gestione completa del processo di sottomissione e revisione di articoli scientifici per una conferenza accademica.

Il sistema è progettato per supportare diversi ruoli utente, ognuno con permessi e funzionalità specifiche per interagire con il sistema.

## Tecnologie Utilizzate

*   **Linguaggio di Programmazione:** Java
*   **Interfaccia Grafica (GUI):** Java Swing

## Funzionalità Principali

L'applicazione implementa un flusso di lavoro logico basato sui ruoli degli utenti all'interno di una conferenza.

### Ruoli Gestiti

1.  **Autore (Author):**
    *   Può sottomettere un articolo (paper) per la revisione.
    *   Può visualizzare lo stato di avanzamento e i risultati della revisione dei propri articoli.

2.  **Revisore (Reviewer):**
    *   Può visualizzare gli articoli a lui assegnati dal Chair.
    *   Può accettare o rifiutare l'incarico di revisione.
    *   Può assegnare un articolo a un Sotto-Revisore di sua fiducia.
    *   Può sottomettere la propria revisione (voto e commento) per un articolo.

3.  **Sotto-Revisore (Sub-Reviewer):**
    *   Un ruolo delegato da un Revisore.
    *   Ha il compito di revisionare un articolo specifico e fornire il suo feedback al Revisore principale.

4.  **Chair:**
    *   È il responsabile della conferenza.
    *   Assegna gli articoli sottomessi ai Revisori.
    *   Supervisiona il processo di revisione.
    *   Prende la decisione finale sull'accettazione o il rifiuto di un articolo, basandosi sulle revisioni ricevute.

5.  **Editore (Editor):**
    *   Ha privilegi di supervisione e può visualizzare lo stato di tutte le sottomissioni e revisioni.
    *   Gestisce la raccolta finale degli articoli accettati per la pubblicazione negli atti della conferenza.
