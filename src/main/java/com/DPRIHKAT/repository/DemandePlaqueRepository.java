package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.DemandePlaque;
import com.DPRIHKAT.entity.enums.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository pour l'entité DemandePlaque
 * 
 * @author amateur
 */
@Repository
public interface DemandePlaqueRepository extends JpaRepository<DemandePlaque, UUID> {
    
    /**
     * Trouve toutes les demandes de plaque pour un contribuable donné
     * 
     * @param contribuableId ID du contribuable
     * @return Liste des demandes de plaque
     */
    List<DemandePlaque> findByContribuableId(UUID contribuableId);
    
    /**
     * Trouve toutes les demandes de plaque pour un véhicule donné
     * 
     * @param vehiculeId ID du véhicule
     * @return Liste des demandes de plaque
     */
    List<DemandePlaque> findByVehiculeId(UUID vehiculeId);
    
    /**
     * Trouve toutes les demandes de plaque avec un statut donné
     * 
     * @param statut Statut de la demande
     * @return Liste des demandes de plaque
     */
    List<DemandePlaque> findByStatut(StatutDemande statut);
    
    /**
     * Trouve toutes les demandes de plaque pour un contribuable avec un statut donné
     * 
     * @param contribuableId ID du contribuable
     * @param statut Statut de la demande
     * @return Liste des demandes de plaque
     */
    List<DemandePlaque> findByContribuableIdAndStatut(UUID contribuableId, StatutDemande statut);
    
    /**
     * Trouve toutes les demandes de plaque validées par un utilisateur donné
     * 
     * @param validateurId ID du validateur
     * @return Liste des demandes de plaque
     */
    List<DemandePlaque> findByValidateurId(UUID validateurId);
}
