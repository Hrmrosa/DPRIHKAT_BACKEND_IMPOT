package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Paiement;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, UUID>, PaiementRepositoryCustom {
    @Query("SELECT p FROM Paiement p JOIN Declaration d ON d.paiement.id = p.id WHERE d.id = :declarationId")
    Paiement findByDeclarationId(@Param("declarationId") UUID declarationId);
    
    List<Paiement> findByStatut(StatutPaiement statut);
    
    List<Paiement> findByDateBetween(Date startDate, Date endDate);
    
    // Méthodes avec pagination
    Page<Paiement> findByStatut(StatutPaiement statut, Pageable pageable);
    
    Page<Paiement> findByDateBetween(Date startDate, Date endDate, Pageable pageable);
    
    // Méthode corrigée pour éviter l'erreur de type de paramètre
    @Query("SELECT p FROM Paiement p WHERE " +
           "(:statut IS NULL OR p.statut = :statut) AND " +
           "(:startDateParam = false OR p.date >= :startDate) AND " +
           "(:endDateParam = false OR p.date <= :endDate)")
    Page<Paiement> findPaiementsWithFilters(
            @Param("statut") StatutPaiement statut,
            @Param("startDateParam") boolean hasStartDate,
            @Param("startDate") Date startDate,
            @Param("endDateParam") boolean hasEndDate,
            @Param("endDate") Date endDate,
            Pageable pageable);
    
    @Query("SELECT p.taxation.declaration.propriete.proprietaire.id, p.taxation.declaration.propriete.proprietaire.nom, SUM(p.montant) " +
          "FROM Paiement p " +
          "GROUP BY p.taxation.declaration.propriete.proprietaire.id, p.taxation.declaration.propriete.proprietaire.nom " +
          "ORDER BY SUM(p.montant) DESC")
    List<Object[]> findTopContributors(int limit);
}
