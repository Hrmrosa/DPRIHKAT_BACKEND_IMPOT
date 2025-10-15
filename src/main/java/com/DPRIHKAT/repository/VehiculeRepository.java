package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Interface de repository pour les véhicules
 */
@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, UUID>, VehiculeRepositoryCustom {
    // Méthodes existantes
    
    /**
     * Trouve tous les véhicules d'un propriétaire
     * @param proprietaireId ID du propriétaire
     * @return liste des véhicules du propriétaire
     */
    List<Vehicule> findByProprietaireId(UUID proprietaireId);
    
    /**
     * Trouve tous les véhicules d'un contribuable
     * @param contribuableId ID du contribuable
     * @return liste des véhicules du contribuable
     */
    List<Vehicule> findByContribuableId(UUID contribuableId);
}
