package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Paiement;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, UUID> {
    @Query("SELECT p FROM Paiement p WHERE p.taxation.declaration.id = :declarationId")
    Paiement findByDeclarationId(@Param("declarationId") UUID declarationId);
    List<Paiement> findByStatut(StatutPaiement statut);
    List<Paiement> findByDateBetween(Date startDate, Date endDate);
    
    @Query("SELECT p.taxation.declaration.propriete.proprietaire.id, p.taxation.declaration.propriete.proprietaire.nom, SUM(p.montant) " +
          "FROM Paiement p " +
          "GROUP BY p.taxation.declaration.propriete.proprietaire.id, p.taxation.declaration.propriete.proprietaire.nom " +
          "ORDER BY SUM(p.montant) DESC")
    List<Object[]> findTopContributors(int limit);
}
