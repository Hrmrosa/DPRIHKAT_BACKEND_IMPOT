package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.NatureImpot;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.ProprieteImpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité ProprieteImpot
 * @author amateur
 */
@Repository
public interface ProprieteImpotRepository extends JpaRepository<ProprieteImpot, UUID> {
    
    /**
     * Trouve tous les liens entre propriétés et impôts pour une propriété donnée
     * @param propriete La propriété
     * @return Liste des liens entre la propriété et ses impôts
     */
    List<ProprieteImpot> findByPropriete(Propriete propriete);
    
    /**
     * Trouve tous les liens entre propriétés et impôts pour une propriété donnée qui sont actifs
     * @param propriete La propriété
     * @return Liste des liens actifs entre la propriété et ses impôts
     */
    List<ProprieteImpot> findByProprieteAndActifTrue(Propriete propriete);
    
    /**
     * Trouve tous les liens entre propriétés et impôts pour une nature d'impôt donnée
     * @param natureImpot La nature d'impôt
     * @return Liste des liens entre les propriétés et cette nature d'impôt
     */
    List<ProprieteImpot> findByNatureImpot(NatureImpot natureImpot);
    
    /**
     * Trouve tous les liens entre propriétés et impôts pour une nature d'impôt donnée qui sont actifs
     * @param natureImpot La nature d'impôt
     * @return Liste des liens actifs entre les propriétés et cette nature d'impôt
     */
    List<ProprieteImpot> findByNatureImpotAndActifTrue(NatureImpot natureImpot);
    
    /**
     * Trouve un lien entre une propriété et une nature d'impôt
     * @param propriete La propriété
     * @param natureImpot La nature d'impôt
     * @return Le lien entre la propriété et la nature d'impôt, s'il existe
     */
    Optional<ProprieteImpot> findByProprieteAndNatureImpot(Propriete propriete, NatureImpot natureImpot);
    
    /**
     * Trouve un lien actif entre une propriété et une nature d'impôt
     * @param propriete La propriété
     * @param natureImpot La nature d'impôt
     * @return Le lien actif entre la propriété et la nature d'impôt, s'il existe
     */
    Optional<ProprieteImpot> findByProprieteAndNatureImpotAndActifTrue(Propriete propriete, NatureImpot natureImpot);
    
    /**
     * Vérifie si un lien existe entre une propriété et une nature d'impôt
     * @param propriete La propriété
     * @param natureImpot La nature d'impôt
     * @return true si un lien existe, false sinon
     */
    boolean existsByProprieteAndNatureImpot(Propriete propriete, NatureImpot natureImpot);
    
    /**
     * Vérifie si un lien actif existe entre une propriété et une nature d'impôt
     * @param propriete La propriété
     * @param natureImpot La nature d'impôt
     * @return true si un lien actif existe, false sinon
     */
    boolean existsByProprieteAndNatureImpotAndActifTrue(Propriete propriete, NatureImpot natureImpot);
}
