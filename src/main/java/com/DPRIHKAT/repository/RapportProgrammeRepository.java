package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.RapportProgramme;
import com.DPRIHKAT.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface RapportProgrammeRepository extends JpaRepository<RapportProgramme, UUID> {
    List<RapportProgramme> findByUtilisateur(Utilisateur utilisateur);
    List<RapportProgramme> findByUtilisateurAndActif(Utilisateur utilisateur, boolean actif);
    
    @Query("SELECT r FROM RapportProgramme r WHERE r.actif = true AND r.prochaineExecution <= ?1")
    List<RapportProgramme> findRapportsAExecuter(Date dateReference);
}
