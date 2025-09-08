package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.NatureImpot;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.Taxation;
import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeImpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Repository pour l'entité Taxation
 */
@Repository
public interface TaxationRepository extends JpaRepository<Taxation, UUID> {
    
    /**
     * Trouve toutes les taxations actives
     * @return la liste des taxations actives
     */
    List<Taxation> findByActifTrue();
    
    /**
     * Trouve toutes les taxations pour une déclaration donnée
     * @param declaration la déclaration
     * @return la liste des taxations pour cette déclaration
     */
    List<Taxation> findByDeclarationAndActifTrue(Declaration declaration);
    
    /**
     * Trouve toutes les taxations pour un type d'impôt donné
     * @param typeImpot le type d'impôt
     * @return la liste des taxations pour ce type d'impôt
     */
    List<Taxation> findByTypeImpotAndActifTrue(TypeImpot typeImpot);
    
    /**
     * Trouve toutes les taxations pour une nature d'impôt donnée
     * @param natureImpot la nature d'impôt
     * @return la liste des taxations pour cette nature d'impôt
     */
    List<Taxation> findByNatureImpotAndActifTrue(NatureImpot natureImpot);
    
    /**
     * Trouve toutes les taxations avec un statut donné
     * @param statut le statut
     * @return la liste des taxations avec ce statut
     */
    List<Taxation> findByStatutAndActifTrue(StatutTaxation statut);
    
    /**
     * Trouve toutes les taxations pour un exercice donné
     * @param exercice l'exercice
     * @return la liste des taxations pour cet exercice
     */
    List<Taxation> findByExerciceAndActifTrue(String exercice);
    
    /**
     * Trouve toutes les taxations créées entre deux dates
     * @param debut la date de début
     * @param fin la date de fin
     * @return la liste des taxations créées entre ces deux dates
     */
    List<Taxation> findByDateTaxationBetweenAndActifTrue(Date debut, Date fin);
    
    /**
     * Trouve toutes les taxations pour une propriété donnée
     * @param propriete la propriété
     * @return la liste des taxations pour cette propriété
     */
    @Query("SELECT t FROM Taxation t WHERE t.declaration.propriete = :propriete AND t.actif = true")
    List<Taxation> findByProprieteAndActifTrue(@Param("propriete") com.DPRIHKAT.entity.Propriete propriete);
    
    /**
     * Trouve toutes les taxations pour une propriété, un type d'impôt et un exercice donnés
     * @param propriete la propriété
     * @param typeImpot le type d'impôt
     * @param exercice l'exercice
     * @return la liste des taxations correspondantes
     */
    @Query("SELECT t FROM Taxation t WHERE t.declaration.propriete = :propriete AND t.typeImpot = :typeImpot AND t.exercice = :exercice AND t.actif = true")
    List<Taxation> findByProprieteAndTypeImpotAndExerciceAndActifTrue(
        @Param("propriete") com.DPRIHKAT.entity.Propriete propriete,
        @Param("typeImpot") com.DPRIHKAT.entity.enums.TypeImpot typeImpot, 
        @Param("exercice") String exercice);
}
