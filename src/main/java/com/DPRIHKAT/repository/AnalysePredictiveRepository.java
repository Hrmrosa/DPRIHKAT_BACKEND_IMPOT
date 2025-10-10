package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.AnalysePredictive;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.TypeAnalyse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface AnalysePredictiveRepository extends JpaRepository<AnalysePredictive, UUID> {
    List<AnalysePredictive> findByUtilisateur(Utilisateur utilisateur);
    List<AnalysePredictive> findByUtilisateurAndActive(Utilisateur utilisateur, boolean active);
    List<AnalysePredictive> findByTypeAnalyseAndActive(TypeAnalyse typeAnalyse, boolean active);
    
    @Query("SELECT a FROM AnalysePredictive a WHERE a.active = true AND a.executionAutomatique = true AND a.prochaineExecution <= ?1")
    List<AnalysePredictive> findAnalysesAExecuter(Date dateReference);
}
