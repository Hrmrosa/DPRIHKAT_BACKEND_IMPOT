package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.NatureImpot;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.ProprieteImpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository pour l'entité ProprieteImpot
 */
@Repository
public interface ProprieteImpotRepository extends JpaRepository<ProprieteImpot, UUID> {
    
    /**
     * Trouve tous les liens propriété-impôt pour une propriété donnée
     * @param propriete la propriété
     * @return la liste des liens propriété-impôt
     */
    List<ProprieteImpot> findByPropriete(Propriete propriete);
    
    /**
     * Trouve tous les liens propriété-impôt pour une nature d'impôt donnée
     * @param natureImpot la nature d'impôt
     * @return la liste des liens propriété-impôt
     */
    List<ProprieteImpot> findByNatureImpot(NatureImpot natureImpot);
    
    /**
     * Trouve tous les liens propriété-impôt actifs
     * @return la liste des liens propriété-impôt actifs
     */
    List<ProprieteImpot> findByActifTrue();
    
    /**
     * Trouve le lien propriété-impôt pour une propriété et une nature d'impôt données
     * @param propriete la propriété
     * @param natureImpot la nature d'impôt
     * @return le lien propriété-impôt, ou null s'il n'existe pas
     */
    ProprieteImpot findByProprieteAndNatureImpot(Propriete propriete, NatureImpot natureImpot);
}
