package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.repository.VehiculeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour gérer les entités Vehicule
 */
@Service
public class VehiculeEntityService {
    
    private static final Logger logger = LoggerFactory.getLogger(VehiculeEntityService.class);
    
    @Autowired
    private VehiculeRepository vehiculeRepository;
    
    /**
     * Crée un nouveau véhicule
     * 
     * @param vehicule Les données du véhicule à créer
     * @return Le véhicule créé
     */
    @Transactional
    public Vehicule createVehicule(Vehicule vehicule) {
        logger.info("Création d'un nouveau véhicule: {}", vehicule);
        
        // Vérifier si l'immatriculation existe déjà
        if (vehiculeRepository.existsByImmatriculation(vehicule.getImmatriculation())) {
            throw new IllegalArgumentException("Un véhicule avec cette immatriculation existe déjà");
        }
        
        // Initialiser la date d'enregistrement si elle n'est pas définie
        if (vehicule.getDateEnregistrement() == null) {
            vehicule.setDateEnregistrement(new Date());
        }
        
        // Conserver les valeurs de genre et catégorie envoyées par le client
        // Si elles ne sont pas définies, utiliser des valeurs par défaut
        if (vehicule.getGenre() == null || vehicule.getGenre().isEmpty()) {
            vehicule.setGenre("Véhicule particulier");
        }
        
        if (vehicule.getCategorie() == null || vehicule.getCategorie().isEmpty()) {
            vehicule.setCategorie("Personne Physique");
        }
        
        // Définir l'unité de puissance par défaut si elle n'est pas spécifiée
        if (vehicule.getUnitePuissance() == null || vehicule.getUnitePuissance().isEmpty()) {
            vehicule.setUnitePuissance("CV");
        }
        
        logger.info("Enregistrement du véhicule avec immatriculation: {}, marque: {}, modèle: {}, puissance: {} {}", 
                vehicule.getImmatriculation(), 
                vehicule.getMarque(), 
                vehicule.getModele(),
                vehicule.getPuissanceFiscale(),
                vehicule.getUnitePuissance());
        
        return vehiculeRepository.save(vehicule);
    }
    
    /**
     * Met à jour la classification (genre et catégorie) d'un véhicule
     * 
     * @param id ID du véhicule
     * @param genre Genre du véhicule
     * @param categorie Catégorie du véhicule
     * @return Véhicule mis à jour ou null si non trouvé
     */
    @Transactional
    public Vehicule updateClassification(UUID id, String genre, String categorie) {
        logger.info("Mise à jour de la classification du véhicule {}: genre={}, catégorie={}", id, genre, categorie);
        
        Optional<Vehicule> optVehicule = vehiculeRepository.findById(id);
        if (optVehicule.isEmpty()) {
            logger.warn("Véhicule non trouvé avec ID: {}", id);
            return null;
        }
        
        Vehicule vehicule = optVehicule.get();
        vehicule.setGenre(genre);
        vehicule.setCategorie(categorie);
        
        vehicule = vehiculeRepository.save(vehicule);
        logger.info("Classification du véhicule {} mise à jour avec succès", id);
        
        return vehicule;
    }
    
    /**
     * Récupère un véhicule par son ID
     * 
     * @param id ID du véhicule
     * @return Véhicule ou null si non trouvé
     */
    public Vehicule findById(UUID id) {
        logger.info("Recherche du véhicule avec ID: {}", id);
        return vehiculeRepository.findById(id).orElse(null);
    }
    
    /**
     * Récupère tous les véhicules
     * 
     * @return Liste de tous les véhicules
     */
    public List<Vehicule> findAll() {
        logger.info("Récupération de tous les véhicules");
        return vehiculeRepository.findAll();
    }
    
    /**
     * Récupère tous les véhicules d'un contribuable
     * 
     * @param contribuableId ID du contribuable
     * @return Liste des véhicules du contribuable
     */
    public List<Vehicule> findByContribuableId(UUID contribuableId) {
        logger.info("Récupération des véhicules du contribuable avec ID: {}", contribuableId);
        
        // Récupérer les véhicules où le contribuable est propriétaire
        List<Vehicule> vehiculesProprietaire = vehiculeRepository.findByProprietaireId(contribuableId);
        logger.debug("Nombre de véhicules où le contribuable est propriétaire: {}", vehiculesProprietaire.size());
        
        // Récupérer les véhicules où le contribuable est enregistré comme contribuable
        List<Vehicule> vehiculesContribuable = vehiculeRepository.findByContribuableId(contribuableId);
        logger.debug("Nombre de véhicules où le contribuable est enregistré comme contribuable: {}", vehiculesContribuable.size());
        
        // Fusionner les deux listes (sans doublons)
        vehiculesProprietaire.removeAll(vehiculesContribuable);
        vehiculesProprietaire.addAll(vehiculesContribuable);
        
        return vehiculesProprietaire;
    }
    
    /**
     * Récupère tous les véhicules d'un propriétaire
     * 
     * @param proprietaireId ID du propriétaire
     * @return Liste des véhicules du propriétaire
     */
    public List<Vehicule> findByProprietaireId(UUID proprietaireId) {
        logger.info("Récupération des véhicules du propriétaire avec ID: {}", proprietaireId);
        return vehiculeRepository.findByProprietaireId(proprietaireId);
    }
}
