package com.DPRIHKAT.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Vérifier si les données existent déjà
        if (dataExists()) {
            System.out.println("Les données sont déjà présentes dans la base de données.");
            return;
        }

        // Première étape : nettoyage des types enum existants
        executeScript("db/seed/0-cleanup.sql");

        // Deuxième étape : création du schéma
        executeScript("db/seed/0-schema.sql");

        // Troisième étape : chargement des données
        List<String> dataScripts = Arrays.asList(
            "db/seed/1-utilisateurs.sql",
            "db/seed/2-contribuables.sql",
            "db/seed/3-biens.sql",
            "db/seed/4-declarations.sql",
            "db/seed/5-recouvrement.sql",
            "db/seed/6-paiements.sql"
        );

        for (String script : dataScripts) {
            executeScript(script);
        }

        System.out.println("Initialisation de la base de données terminée avec succès");
    }

    private void executeScript(String scriptPath) {
        try {
            System.out.println("Exécution du script : " + scriptPath);
            ClassPathResource resource = new ClassPathResource(scriptPath);
            
            if (!resource.exists()) {
                System.out.println("Script non trouvé : " + scriptPath);
                return;
            }

            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(resource);
            populator.setSeparator(";");
            populator.setIgnoreFailedDrops(true);
            
            populator.execute(dataSource);
            
            System.out.println("Script exécuté avec succès : " + scriptPath);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution du script " + scriptPath + " : " + e.getMessage());
            throw new RuntimeException("Erreur d'initialisation de la base de données", e);
        }
    }

    private boolean dataExists() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM utilisateur", Integer.class);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
