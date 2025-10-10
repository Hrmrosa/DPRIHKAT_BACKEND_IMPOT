package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.ComparaisonDonnees;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.TypeComparaison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface ComparaisonDonneesRepository extends JpaRepository<ComparaisonDonnees, UUID> {
    List<ComparaisonDonnees> findByUtilisateur(Utilisateur utilisateur);
    List<ComparaisonDonnees> findByUtilisateurAndActive(Utilisateur utilisateur, boolean active);
    List<ComparaisonDonnees> findByTypeComparaisonAndActive(TypeComparaison typeComparaison, boolean active);
    
    @Query("SELECT c FROM ComparaisonDonnees c WHERE c.active = true AND c.executionAutomatique = true AND c.prochaineExecution <= ?1")
    List<ComparaisonDonnees> findComparaisonsAExecuter(Date dateReference);
}
