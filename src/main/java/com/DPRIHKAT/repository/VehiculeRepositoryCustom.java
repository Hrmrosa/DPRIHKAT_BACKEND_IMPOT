package com.DPRIHKAT.repository;

import com.DPRIHKAT.dto.VehiculeDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface pour les méthodes personnalisées du repository VehiculeRepository
 * Utilise des DTOs pour limiter le nombre de colonnes dans les requêtes SQL
 */
public interface VehiculeRepositoryCustom {
    
    /**
     * Récupère un véhicule par son ID sous forme de DTO
     * @param id ID du véhicule
     * @return Le véhicule sous forme de DTO
     */
    Optional<VehiculeDTO> findVehiculeDTOById(UUID id);
    
    /**
     * Récupère tous les véhicules sous forme de DTOs
     * @return Liste des véhicules sous forme de DTOs
     */
    List<VehiculeDTO> findAllVehiculeDTOs();
    
    /**
     * Récupère les véhicules d'un propriétaire sous forme de DTOs
     * @param proprietaireId ID du propriétaire
     * @return Liste des véhicules du propriétaire sous forme de DTOs
     */
    List<VehiculeDTO> findVehiculeDTOsByProprietaireId(UUID proprietaireId);
    
    /**
     * Compte le nombre de véhicules
     * @return Le nombre de véhicules
     */
    long count();
    
    /**
     * Vérifie si un véhicule existe par son immatriculation
     * @param immatriculation Immatriculation du véhicule
     * @return True si le véhicule existe, False sinon
     */
    boolean existsByImmatriculation(String immatriculation);
    
    /**
     * Change le propriétaire d'un véhicule
     * @param vehiculeId ID du véhicule
     * @param nouveauProprietaireId ID du nouveau propriétaire
     * @return Nombre de véhicules mis à jour (1 si succès, 0 sinon)
     */
    int changerProprietaire(UUID vehiculeId, UUID nouveauProprietaireId);
}
