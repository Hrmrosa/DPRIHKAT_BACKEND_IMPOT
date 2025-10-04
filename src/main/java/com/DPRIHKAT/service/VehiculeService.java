package com.DPRIHKAT.service;

import com.DPRIHKAT.model.Vehicule;
import com.DPRIHKAT.repository.ContribuableRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

/**
 * Service pour gérer les données des véhicules
 * 
 * @author amateur
 */
@Service
public class VehiculeService {
    
    private static final Logger logger = LoggerFactory.getLogger(VehiculeService.class);
    private final Map<String, List<String>> marquesModeles = new HashMap<>();
    
    @Autowired
    private VehiculeRepository vehiculeRepository;
    
    @Autowired
    private ContribuableRepository contribuableRepository;
    
    public VehiculeService() {
        chargerVehicules();
    }
    
    /**
     * Charge les données des véhicules depuis le fichier voiture.json
     */
    @SuppressWarnings("unchecked")
    private void chargerVehicules() {
        try {
            logger.info("Chargement des données des véhicules depuis le fichier voiture.json");
            ClassPathResource resource = new ClassPathResource("voiture.json");
            
            try (InputStream is = resource.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> root = mapper.readValue(is, new TypeReference<Map<String, Object>>() {});
                
                Object brandsObj = root.get("car_brands");
                if (brandsObj instanceof List<?> list) {
                    marquesModeles.clear();
                    
                    for (Object o : list) {
                        if (o instanceof Map<?, ?> m) {
                            Object nameObj = m.get("name");
                            Object modelsObj = m.get("models");
                            
                            if (nameObj instanceof String name && modelsObj instanceof List<?> mods) {
                                List<String> models = new ArrayList<>();
                                for (Object mo : mods) {
                                    if (mo instanceof String s) models.add(s);
                                }
                                marquesModeles.put(name, models);
                            }
                        }
                    }
                    
                    logger.info("Données des véhicules chargées avec succès: {} marques trouvées", marquesModeles.size());
                }
            }
        } catch (Exception e) {
            logger.error("Erreur lors du chargement des données des véhicules", e);
        }
    }
    
    /**
     * Recherche une clé dans un ensemble de clés en ignorant la casse
     * 
     * @param keys Ensemble des clés
     * @param wanted Clé recherchée
     * @return La clé trouvée ou null si non trouvée
     */
    private String findKeyIgnoreCase(Set<String> keys, String wanted) {
        if (wanted == null) return null;
        for (String k : keys) {
            if (k.equalsIgnoreCase(wanted)) return k;
        }
        return null;
    }
    
    /**
     * Récupère la liste de toutes les marques de véhicules
     * 
     * @return Liste des marques de véhicules
     */
    public List<String> getMarques() {
        List<String> marques = new ArrayList<>(marquesModeles.keySet());
        marques.sort(String::compareToIgnoreCase);
        return marques;
    }
    
    /**
     * Récupère la liste des modèles pour une marque donnée
     * 
     * @param marque Nom de la marque
     * @return Liste des modèles ou null si la marque n'existe pas
     */
    public List<String> getModelesByMarque(String marque) {
        String realMarque = findKeyIgnoreCase(marquesModeles.keySet(), marque);
        if (realMarque == null) return null;
        
        List<String> modeles = marquesModeles.get(realMarque);
        modeles.sort(String::compareToIgnoreCase);
        return modeles;
    }
    
    /**
     * Vérifie si un modèle existe pour une marque donnée
     * 
     * @param marque Nom de la marque
     * @param modele Nom du modèle
     * @return true si le modèle existe pour la marque, false sinon
     */
    public boolean existsModele(String marque, String modele) {
        String realMarque = findKeyIgnoreCase(marquesModeles.keySet(), marque);
        if (realMarque == null) return false;
        
        List<String> modeles = marquesModeles.get(realMarque);
        for (String m : modeles) {
            if (m.equalsIgnoreCase(modele)) return true;
        }
        
        return false;
    }
    
    /**
     * Recharge les données des véhicules depuis le fichier voiture.json
     */
    public void rechargerVehicules() {
        chargerVehicules();
    }
    
    /**
     * Change le propriétaire d'un véhicule
     * @param vehiculeId ID du véhicule
     * @param nouveauProprietaireId ID du nouveau propriétaire
     * @return true si la mutation a réussi, false sinon
     */
    @Transactional
    public boolean changerProprietaire(UUID vehiculeId, UUID nouveauProprietaireId) {
        // Vérifier que le nouveau propriétaire existe
        if (!contribuableRepository.existsById(nouveauProprietaireId)) {
            return false;
        }
        
        // Effectuer la mutation
        int updated = vehiculeRepository.changerProprietaire(vehiculeId, nouveauProprietaireId);
        return updated == 1;
    }
}
