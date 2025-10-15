package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Poursuite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PoursuiteRepository extends JpaRepository<Poursuite, UUID> {
    /**
     * Trouve toutes les poursuites liées aux dossiers de recouvrement d'un contribuable
     * @param contribuableId ID du contribuable
     * @return Liste des poursuites du contribuable
     */
    @Query("SELECT p FROM Poursuite p WHERE p.dossierRecouvrement.contribuable.id = :contribuableId")
    List<Poursuite> findByDossierRecouvrementContribuableId(@Param("contribuableId") UUID contribuableId);
    
    /**
     * Trouve toutes les poursuites d'un dossier de recouvrement
     * @param dossierRecouvrementId ID du dossier de recouvrement
     * @return Liste des poursuites du dossier
     */
    List<Poursuite> findByDossierRecouvrement_Id(UUID dossierRecouvrementId);
}
