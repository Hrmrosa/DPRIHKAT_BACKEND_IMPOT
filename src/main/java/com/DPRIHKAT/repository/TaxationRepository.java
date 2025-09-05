package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Agent;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.ProprieteImpot;
import com.DPRIHKAT.entity.Taxation;
import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeImpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Repository pour l'entité Taxation
 * @author amateur
 */
@Repository
public interface TaxationRepository extends JpaRepository<Taxation, UUID> {
    
    /**
     * Trouve toutes les taxations actives
     * @return Liste des taxations actives
     */
    List<Taxation> findByActifTrue();
    
    /**
     * Trouve toutes les taxations pour une propriété donnée
     * @param propriete La propriété
     * @return Liste des taxations pour cette propriété
     */
    List<Taxation> findByPropriete(Propriete propriete);
    
    /**
     * Trouve toutes les taxations actives pour une propriété donnée
     * @param propriete La propriété
     * @return Liste des taxations actives pour cette propriété
     */
    List<Taxation> findByProprieteAndActifTrue(Propriete propriete);
    
    /**
     * Trouve toutes les taxations pour un lien propriété-impôt donné
     * @param proprieteImpot Le lien propriété-impôt
     * @return Liste des taxations pour ce lien
     */
    List<Taxation> findByProprieteImpot(ProprieteImpot proprieteImpot);
    
    /**
     * Trouve toutes les taxations actives pour un lien propriété-impôt donné
     * @param proprieteImpot Le lien propriété-impôt
     * @return Liste des taxations actives pour ce lien
     */
    List<Taxation> findByProprieteImpotAndActifTrue(ProprieteImpot proprieteImpot);
    
    /**
     * Trouve toutes les taxations pour un agent taxateur donné
     * @param agentTaxateur L'agent taxateur
     * @return Liste des taxations créées par cet agent
     */
    List<Taxation> findByAgentTaxateur(Agent agentTaxateur);
    
    /**
     * Trouve toutes les taxations actives pour un agent taxateur donné
     * @param agentTaxateur L'agent taxateur
     * @return Liste des taxations actives créées par cet agent
     */
    List<Taxation> findByAgentTaxateurAndActifTrue(Agent agentTaxateur);
    
    /**
     * Trouve toutes les taxations pour un type d'impôt donné
     * @param typeImpot Le type d'impôt
     * @return Liste des taxations pour ce type d'impôt
     */
    List<Taxation> findByTypeImpot(TypeImpot typeImpot);
    
    /**
     * Trouve toutes les taxations actives pour un type d'impôt donné
     * @param typeImpot Le type d'impôt
     * @return Liste des taxations actives pour ce type d'impôt
     */
    List<Taxation> findByTypeImpotAndActifTrue(TypeImpot typeImpot);
    
    /**
     * Trouve toutes les taxations pour un exercice donné
     * @param exercice L'exercice (année fiscale)
     * @return Liste des taxations pour cet exercice
     */
    List<Taxation> findByExercice(Integer exercice);
    
    /**
     * Trouve toutes les taxations actives pour un exercice donné
     * @param exercice L'exercice (année fiscale)
     * @return Liste des taxations actives pour cet exercice
     */
    List<Taxation> findByExerciceAndActifTrue(Integer exercice);
    
    /**
     * Trouve toutes les taxations pour un statut donné
     * @param statut Le statut de la taxation
     * @return Liste des taxations avec ce statut
     */
    List<Taxation> findByStatut(StatutTaxation statut);
    
    /**
     * Trouve toutes les taxations actives pour un statut donné
     * @param statut Le statut de la taxation
     * @return Liste des taxations actives avec ce statut
     */
    List<Taxation> findByStatutAndActifTrue(StatutTaxation statut);
    
    /**
     * Trouve toutes les taxations avec exonération
     * @return Liste des taxations avec exonération
     */
    List<Taxation> findByExonerationTrue();
    
    /**
     * Trouve toutes les taxations actives avec exonération
     * @return Liste des taxations actives avec exonération
     */
    List<Taxation> findByExonerationTrueAndActifTrue();
    
    /**
     * Trouve toutes les taxations dont la date d'échéance est avant une date donnée
     * @param date La date limite
     * @return Liste des taxations dont la date d'échéance est avant la date donnée
     */
    List<Taxation> findByDateEcheanceBefore(Date date);
    
    /**
     * Trouve toutes les taxations actives dont la date d'échéance est avant une date donnée
     * @param date La date limite
     * @return Liste des taxations actives dont la date d'échéance est avant la date donnée
     */
    List<Taxation> findByDateEcheanceBeforeAndActifTrue(Date date);
    
    /**
     * Trouve toutes les taxations pour une propriété et un exercice donnés
     * @param propriete La propriété
     * @param exercice L'exercice (année fiscale)
     * @return Liste des taxations pour cette propriété et cet exercice
     */
    List<Taxation> findByProprieteAndExercice(Propriete propriete, Integer exercice);
    
    /**
     * Trouve toutes les taxations actives pour une propriété et un exercice donnés
     * @param propriete La propriété
     * @param exercice L'exercice (année fiscale)
     * @return Liste des taxations actives pour cette propriété et cet exercice
     */
    List<Taxation> findByProprieteAndExerciceAndActifTrue(Propriete propriete, Integer exercice);
    
    /**
     * Trouve toutes les taxations pour une propriété, un type d'impôt et un exercice donnés
     * @param propriete La propriété
     * @param typeImpot Le type d'impôt
     * @param exercice L'exercice (année fiscale)
     * @return Liste des taxations pour cette propriété, ce type d'impôt et cet exercice
     */
    List<Taxation> findByProprieteAndTypeImpotAndExercice(Propriete propriete, TypeImpot typeImpot, Integer exercice);
    
    /**
     * Trouve toutes les taxations actives pour une propriété, un type d'impôt et un exercice donnés
     * @param propriete La propriété
     * @param typeImpot Le type d'impôt
     * @param exercice L'exercice (année fiscale)
     * @return Liste des taxations actives pour cette propriété, ce type d'impôt et cet exercice
     */
    List<Taxation> findByProprieteAndTypeImpotAndExerciceAndActifTrue(Propriete propriete, TypeImpot typeImpot, Integer exercice);
}
