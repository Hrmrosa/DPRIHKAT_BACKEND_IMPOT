package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Relance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository pour l'entité Relance
 * Étend RelanceRepositoryCustom pour ajouter les méthodes personnalisées
 * permettant de récupérer les informations détaillées des relances
 */
@Repository
public interface RelanceRepository extends RelanceRepositoryCustom, JpaRepository<Relance, UUID> {
    // Méthodes existantes
    
    /**
     * Trouve toutes les relances liées aux dossiers de recouvrement d'un contribuable
     * @param contribuableId ID du contribuable
     * @return Liste des relances du contribuable
     */
    @Query("SELECT r FROM Relance r WHERE r.dossierRecouvrement.contribuable.id = :contribuableId")
    List<Relance> findByDossierRecouvrementContribuableId(@Param("contribuableId") UUID contribuableId);
    
    /**
     * Trouve toutes les relances d'un dossier de recouvrement
     * @param dossierRecouvrementId ID du dossier de recouvrement
     * @return Liste des relances du dossier
     */
    List<Relance> findByDossierRecouvrement_Id(UUID dossierRecouvrementId);
}
