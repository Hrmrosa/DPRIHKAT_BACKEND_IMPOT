package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.enums.StatutDeclaration;
import com.DPRIHKAT.entity.enums.TypeImpot;
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
public interface DeclarationRepository extends JpaRepository<Declaration, UUID> {
    Page<Declaration> findByTypeImpot(TypeImpot typeImpot, Pageable pageable);
    Page<Declaration> findByStatut(StatutDeclaration statut, Pageable pageable);
    
    @Query("SELECT d FROM Declaration d WHERE d.propriete.proprietaire.id = :contribuableId")
    Page<Declaration> findByContribuableId(@Param("contribuableId") UUID contribuableId, Pageable pageable);
    
    @Query("SELECT COUNT(d) FROM Declaration d WHERE d.propriete.id = :proprieteId AND d.typeImpot = :typeImpot AND YEAR(d.date) = :annee")
    int countByProprieteAndTypeAndAnnee(@Param("proprieteId") UUID proprieteId, @Param("typeImpot") TypeImpot typeImpot, @Param("annee") int annee);
    
    // New methods for bureau-based filtering
    List<Declaration> findByPaiementIsNull();
    
    @Query("SELECT d FROM Declaration d WHERE d.date BETWEEN :startDate AND :endDate")
    List<Declaration> findByDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT COUNT(d) FROM Declaration d WHERE d.concession.id = :concessionId AND d.typeImpot = :typeImpot AND YEAR(d.date) = :annee")
    int countByConcessionAndTypeAndAnnee(@Param("concessionId") UUID concessionId, @Param("typeImpot") TypeImpot typeImpot, @Param("annee") int annee);
}
