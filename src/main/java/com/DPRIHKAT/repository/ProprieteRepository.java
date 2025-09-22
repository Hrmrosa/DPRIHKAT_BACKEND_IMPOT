package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Propriete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProprieteRepository extends JpaRepository<Propriete, UUID>, ProprieteRepositoryCustom {
    List<Propriete> findByProprietaire_Id(UUID proprietaireId);
    
    /**
     * Trouve toutes les propriétés d'un propriétaire avec pagination
     * @param proprietaireId ID du propriétaire
     * @param pageable informations de pagination
     * @return page de propriétés du propriétaire
     */
    Page<Propriete> findByProprietaire_Id(UUID proprietaireId, Pageable pageable);
    
    /**
     * Trouve toutes les propriétés actives avec pagination
     * @param pageable informations de pagination
     * @return page de propriétés actives
     */
    Page<Propriete> findByActifTrue(Pageable pageable);
}
