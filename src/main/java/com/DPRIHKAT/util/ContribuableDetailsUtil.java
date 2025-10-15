package com.DPRIHKAT.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Utilitaire pour récupérer les détails d'un contribuable directement via SQL
 */
@Component
public class ContribuableDetailsUtil {

    private static final Logger logger = LoggerFactory.getLogger(ContribuableDetailsUtil.class);
    private final JdbcTemplate jdbcTemplate;
    private String detailsQuery;

    public ContribuableDetailsUtil(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        try {
            // Charger la requête SQL depuis le fichier
            ClassPathResource resource = new ClassPathResource("query_contribuable_details.sql");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                detailsQuery = reader.lines().collect(Collectors.joining(" "));
            }
        } catch (IOException e) {
            logger.error("Erreur lors du chargement de la requête SQL", e);
            detailsQuery = null;
        }
    }

    /**
     * Récupère les détails d'un contribuable avec ses propriétés et véhicules
     * @param contribuableId ID du contribuable
     * @return Détails du contribuable au format JSON
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getContribuableDetails(UUID contribuableId) {
        if (detailsQuery == null) {
            throw new RuntimeException("La requête SQL n'a pas été chargée correctement");
        }
        
        try {
            // Exécuter la requête SQL avec les paramètres
            return jdbcTemplate.queryForObject(
                detailsQuery,
                new Object[]{contribuableId, contribuableId, contribuableId, contribuableId},
                (rs, rowNum) -> {
                    String jsonResult = rs.getString("details");
                    return JsonUtil.fromJson(jsonResult, Map.class);
                }
            );
        } catch (Exception e) {
            logger.error("Erreur lors de l'exécution de la requête SQL", e);
            throw new RuntimeException("Erreur lors de la récupération des détails du contribuable", e);
        }
    }
}
