package com.cms.users.account.test;

import com.cms.users.account.Control.GestionePCControl;
import com.cms.users.account.Interface.SkillsSelectionScreen;
import java.util.Arrays;

/**
 * Test per verificare il funzionamento del flusso "Aggiungi Competenze"
 */
public class TestSpecificaCompetenze {
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            System.out.println("=== TEST SPECIFICA COMPETENZE ===");
            
            // Test 1: SkillsSelectionScreen standalone (senza control)
            System.out.println("Test 1: SkillsSelectionScreen standalone");
            SkillsSelectionScreen skillsScreen1 = new SkillsSelectionScreen();
            skillsScreen1.setTitle("Test Standalone");
            skillsScreen1.setLocation(100, 100);
            skillsScreen1.setVisible(true);
            
            // Test 2: SkillsSelectionScreen con GestionePCControl
            System.out.println("Test 2: SkillsSelectionScreen con GestionePCControl");
            GestionePCControl control = new GestionePCControl();
            SkillsSelectionScreen skillsScreen2 = new SkillsSelectionScreen(control);
            
            // Simula keywords della conferenza
            skillsScreen2.setAvailableKeywords(Arrays.asList(
                "Java", "Machine Learning", "Software Engineering", "Database"
            ));
            
            skillsScreen2.setTitle("Test con Control");
            skillsScreen2.setLocation(600, 100);
            skillsScreen2.setVisible(true);
            
            System.out.println("=== TEST AVVIATI ===");
            System.out.println("Nota: Il salvataggio nel DB richiede:");
            System.out.println("1. Connessione al database");
            System.out.println("2. Utente loggato (App.utenteAccesso)");
            System.out.println("3. ID conferenza valido");
            System.out.println("Testare con dati reali nell'applicazione completa.");
        });
    }
}
