package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.enums.StatutDeclaration;
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
public interface DeclarationRepository extends JpaRepository<Declaration, UUID>, DeclarationRepositoryCustom {
    Page<Declaration> findByStatut(StatutDeclaration statut, Pageable pageable);
    
    @Query("SELECT d FROM Declaration d WHERE d.propriete.proprietaire.id = :contribuableId")
    Page<Declaration> findByContribuableId(@Param("contribuableId") UUID contribuableId, Pageable pageable);
    
    @Query("SELECT d FROM Declaration d WHERE d.dateDeclaration BETWEEN :startDate AND :endDate")
    List<Declaration> findByDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    /**
     * Compte le nombre de déclarations entre deux dates
     * @param startDate Date de début
     * @param endDate Date de fin
     * @return Nombre de déclarations dans la période
     */
    @Query("SELECT COUNT(d) FROM Declaration d WHERE d.dateDeclaration BETWEEN :startDate AND :endDate")
    Long countByDateDeclarationBetween(
            @Param("startDate") Date startDate, 
            @Param("endDate") Date endDate);
    
    /**
     * Trouve toutes les déclarations sans paiement associé
     * @return Liste des déclarations sans paiement
     */
    List<Declaration> findByPaiementIsNull();
    
    List<Declaration> findByPaiementIsNullAndDateDeclarationBefore(Date date);
    
    List<Declaration> findByContribuableAndPaiementIsNull(Contribuable contribuable);
}
