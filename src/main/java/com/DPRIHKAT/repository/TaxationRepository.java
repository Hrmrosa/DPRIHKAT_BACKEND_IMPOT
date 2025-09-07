package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.Taxation;
import com.DPRIHKAT.entity.enums.StatutTaxation;
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
     * Trouve toutes les taxations actives avec pagination
     * @param pageable informations de pagination
     * @return page de taxations actives
     */
    Page<Taxation> findByActifTrue(Pageable pageable);
    
    /**
     * Trouve toutes les taxations associées à une déclaration
     * @param declaration la déclaration
     * @return la liste des taxations associées à la déclaration
     */
    List<Taxation> findByDeclarationAndActifTrue(Declaration declaration);
    
    /**
     * Trouve toutes les taxations associées à une déclaration avec pagination
     * @param declaration la déclaration
     * @param pageable informations de pagination
     * @return page de taxations associées à la déclaration
     */
    Page<Taxation> findByDeclarationAndActifTrue(Declaration declaration, Pageable pageable);
    
    /**
     * Trouve toutes les taxations par statut
     * @param statut le statut des taxations
     * @return la liste des taxations avec le statut spécifié
     */
    List<Taxation> findByStatutAndActifTrue(StatutTaxation statut);
    
    /**
     * Trouve toutes les taxations par statut avec pagination
     * @param statut le statut des taxations
     * @param pageable informations de pagination
     * @return page de taxations avec le statut spécifié
     */
    Page<Taxation> findByStatutAndActifTrue(StatutTaxation statut, Pageable pageable);
    
    /**
     * Trouve toutes les taxations par type d'impôt
     * @param typeImpot le type d'impôt
     * @return la liste des taxations avec le type d'impôt spécifié
     */
    List<Taxation> findByTypeImpotAndActifTrue(TypeImpot typeImpot);
    
    /**
     * Trouve toutes les taxations par type d'impôt avec pagination
     * @param typeImpot le type d'impôt
     * @param pageable informations de pagination
     * @return page de taxations avec le type d'impôt spécifié
     */
    Page<Taxation> findByTypeImpotAndActifTrue(TypeImpot typeImpot, Pageable pageable);
    
    /**
     * Trouve toutes les taxations par agent taxateur
     * @param agentTaxateurId l'ID de l'agent taxateur
     * @return la liste des taxations effectuées par l'agent taxateur
     */
    @Query("SELECT t FROM Taxation t WHERE t.agentTaxateur.id = :agentId AND t.actif = true")
    List<Taxation> findByAgentTaxateurIdAndActifTrue(@Param("agentId") UUID agentTaxateurId);
    
    /**
     * Trouve toutes les taxations par agent taxateur avec pagination
     * @param agentTaxateurId l'ID de l'agent taxateur
     * @param pageable informations de pagination
     * @return page de taxations effectuées par l'agent taxateur
     */
    @Query("SELECT t FROM Taxation t WHERE t.agentTaxateur.id = :agentId AND t.actif = true")
    Page<Taxation> findByAgentTaxateurIdAndActifTrue(@Param("agentId") UUID agentTaxateurId, Pageable pageable);
    
    /**
     * Trouve toutes les taxations par agent validateur
     * @param agentValidateurId l'ID de l'agent validateur
     * @return la liste des taxations validées par l'agent validateur
     */
    @Query("SELECT t FROM Taxation t WHERE t.agentValidateur.id = :agentId AND t.actif = true")
    List<Taxation> findByAgentValidateurIdAndActifTrue(@Param("agentId") UUID agentValidateurId);
    
    /**
     * Trouve toutes les taxations par agent validateur avec pagination
     * @param agentValidateurId l'ID de l'agent validateur
     * @param pageable informations de pagination
     * @return page de taxations validées par l'agent validateur
     */
    @Query("SELECT t FROM Taxation t WHERE t.agentValidateur.id = :agentId AND t.actif = true")
    Page<Taxation> findByAgentValidateurIdAndActifTrue(@Param("agentId") UUID agentValidateurId, Pageable pageable);
    
    /**
     * Trouve toutes les taxations par exercice
     * @param exercice l'exercice fiscal
     * @return la liste des taxations pour l'exercice spécifié
     */
    List<Taxation> findByExerciceAndActifTrue(String exercice);
    
    /**
     * Trouve toutes les taxations par exercice avec pagination
     * @param exercice l'exercice fiscal
     * @param pageable informations de pagination
     * @return page de taxations pour l'exercice spécifié
     */
    Page<Taxation> findByExerciceAndActifTrue(String exercice, Pageable pageable);
    
    /**
     * Trouve toutes les taxations entre deux dates
     * @param dateDebut la date de début
     * @param dateFin la date de fin
     * @return la liste des taxations entre les deux dates
     */
    @Query("SELECT t FROM Taxation t WHERE t.dateTaxation BETWEEN :dateDebut AND :dateFin AND t.actif = true")
    List<Taxation> findByDateTaxationBetweenAndActifTrue(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin);
    
    /**
     * Trouve toutes les taxations entre deux dates avec pagination
     * @param dateDebut la date de début
     * @param dateFin la date de fin
     * @param pageable informations de pagination
     * @return page de taxations entre les deux dates
     */
    @Query("SELECT t FROM Taxation t WHERE t.dateTaxation BETWEEN :dateDebut AND :dateFin AND t.actif = true")
    Page<Taxation> findByDateTaxationBetweenAndActifTrue(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin, Pageable pageable);
    
    /**
     * Trouve toutes les taxations non payées
     * @return la liste des taxations non payées
     */
    @Query("SELECT t FROM Taxation t WHERE t.statut != 'PAYEE' AND t.actif = true")
    List<Taxation> findNonPayeesAndActifTrue();
    
    /**
     * Trouve toutes les taxations non payées avec pagination
     * @param pageable informations de pagination
     * @return page de taxations non payées
     */
    @Query("SELECT t FROM Taxation t WHERE t.statut != 'PAYEE' AND t.actif = true")
    Page<Taxation> findNonPayeesAndActifTrue(Pageable pageable);
    
    /**
     * Trouve toutes les taxations payées mais non apurées
     * @return la liste des taxations payées mais non apurées
     */
    @Query("SELECT t FROM Taxation t WHERE t.statut = 'PAYEE' AND t.statut != 'APUREE' AND t.actif = true")
    List<Taxation> findPayeesNonApureesAndActifTrue();
    
    /**
     * Trouve toutes les taxations payées mais non apurées avec pagination
     * @param pageable informations de pagination
     * @return page de taxations payées mais non apurées
     */
    @Query("SELECT t FROM Taxation t WHERE t.statut = 'PAYEE' AND t.statut != 'APUREE' AND t.actif = true")
    Page<Taxation> findPayeesNonApureesAndActifTrue(Pageable pageable);
    
    /**
     * Trouve toutes les taxations par déclaration, type d'impôt et exercice
     * @param declaration la déclaration
     * @param typeImpot le type d'impôt
     * @param exercice l'exercice fiscal
     * @return la liste des taxations correspondantes
     */
    @Query("SELECT t FROM Taxation t WHERE t.declaration = :declaration AND t.typeImpot = :typeImpot AND t.exercice = :exercice AND t.actif = true")
    List<Taxation> findByDeclarationAndTypeImpotAndExerciceAndActifTrue(
            @Param("declaration") Declaration declaration, 
            @Param("typeImpot") TypeImpot typeImpot, 
            @Param("exercice") String exercice);
    
    /**
     * Trouve toutes les taxations par déclaration, type d'impôt et exercice avec pagination
     * @param declaration la déclaration
     * @param typeImpot le type d'impôt
     * @param exercice l'exercice fiscal
     * @param pageable informations de pagination
     * @return page de taxations correspondantes
     */
    @Query("SELECT t FROM Taxation t WHERE t.declaration = :declaration AND t.typeImpot = :typeImpot AND t.exercice = :exercice AND t.actif = true")
    Page<Taxation> findByDeclarationAndTypeImpotAndExerciceAndActifTrue(
            @Param("declaration") Declaration declaration, 
            @Param("typeImpot") TypeImpot typeImpot, 
            @Param("exercice") String exercice, 
            Pageable pageable);
}
