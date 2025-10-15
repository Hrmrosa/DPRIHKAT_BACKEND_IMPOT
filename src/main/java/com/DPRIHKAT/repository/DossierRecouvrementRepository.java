package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.DossierRecouvrement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface DossierRecouvrementRepository extends JpaRepository<DossierRecouvrement, UUID> {

    @Query("SELECT d FROM DossierRecouvrement d WHERE " +
           "(:contribuableId IS NULL OR d.contribuable.id = :contribuableId) AND " +
           "(:statut IS NULL OR d.statut = :statut) AND " +
           "(:dateDebut IS NULL OR d.dateOuverture >= :dateDebut) AND " +
           "(:dateFin IS NULL OR d.dateOuverture <= :dateFin)")
    List<DossierRecouvrement> findByCriteria(
            @Param("contribuableId") UUID contribuableId,
            @Param("statut") String statut,
            @Param("dateDebut") Date dateDebut,
            @Param("dateFin") Date dateFin);
            
    /**
     * Récupère tous les dossiers de recouvrement d'un contribuable
     * 
     * @param contribuableId ID du contribuable
     * @return Liste des dossiers de recouvrement du contribuable
     */
    List<DossierRecouvrement> findByContribuableId(UUID contribuableId);
}
