package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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
    List<Vehicule> findByProprietaire_Id(UUID proprietaireId);
    
    /**
     * Trouve tous les véhicules d'un contribuable
     * @param contribuableId ID du contribuable
     * @return liste des véhicules du contribuable
     */
    List<Vehicule> findByContribuable_Id(UUID contribuableId);
    
    /**
     * Vérifie si un véhicule existe avec l'immatriculation spécifiée
     * @param immatriculation immatriculation à vérifier
     * @return véhicule trouvé ou vide
     */
    Optional<Vehicule> findByImmatriculation(String immatriculation);
    
    /**
     * Vérifie si un véhicule existe avec l'immatriculation spécifiée
     * @param immatriculation immatriculation à vérifier
     * @return true si un véhicule existe avec cette immatriculation
     */
    boolean existsByImmatriculation(String immatriculation);
    
    /**
     * Vérifie si un véhicule existe avec le numéro de chassis spécifié
     * @param numeroChassis numéro de chassis à vérifier
     * @return véhicule trouvé ou vide
     */
    Optional<Vehicule> findByNumeroChassis(String numeroChassis);
}
