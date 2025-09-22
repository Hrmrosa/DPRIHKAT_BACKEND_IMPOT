package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Contribuable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContribuableRepository extends JpaRepository<Contribuable, UUID>, ContribuableRepositoryCustom {
    /**
     * Trouve tous les contribuables actifs avec pagination
     * @param pageable informations de pagination
     * @return page de contribuables actifs
     */
    Page<Contribuable> findByActifTrue(Pageable pageable);
    
    /**
     * Recherche de contribuables par nom avec pagination
     * @param nom nom ou partie du nom à rechercher
     * @param pageable informations de pagination
     * @return page de contribuables correspondant à la recherche
     */
    Page<Contribuable> findByNomContainingIgnoreCase(String nom, Pageable pageable);
}
