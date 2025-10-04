package com.DPRIHKAT.repository;

import com.DPRIHKAT.dto.ContribuableDTO;
import com.DPRIHKAT.entity.Contribuable;

import java.util.List;
import java.util.UUID;

public interface ContribuableRepositoryCustom {
    List<ContribuableDTO> findAllWithDetails();
    
    /**
     * Compte le nombre de contribuables actifs
     * @return nombre de contribuables actifs
     */
    long countByActifTrue();
    
    /**
     * Compte le nombre de contribuables inactifs
     * @return nombre de contribuables inactifs
     */
    long countByActifFalse();
    
    /**
     * Trouve un contribuable par son ID avec tous ses biens (immobiliers, véhicules, concessions minières)
     * @param id L'ID du contribuable
     * @return Le contribuable avec tous ses biens
     */
    Contribuable findByIdWithAllProperties(UUID id);
}
