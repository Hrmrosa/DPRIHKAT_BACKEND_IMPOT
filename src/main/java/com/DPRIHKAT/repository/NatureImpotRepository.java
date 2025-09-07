package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.NatureImpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité NatureImpot
 */
@Repository
public interface NatureImpotRepository extends JpaRepository<NatureImpot, UUID> {
    
    /**
     * Trouve une nature d'impôt par son code
     * @param code le code de la nature d'impôt
     * @return la nature d'impôt correspondante, si elle existe
     */
    Optional<NatureImpot> findByCode(String code);
    
    /**
     * Trouve toutes les natures d'impôt actives
     * @return la liste des natures d'impôt actives
     */
    List<NatureImpot> findByActifTrue();
    
    /**
     * Vérifie si une nature d'impôt existe avec le code donné
     * @param code le code à vérifier
     * @return true si une nature d'impôt existe avec ce code, false sinon
     */
    boolean existsByCode(String code);
}
