package com.DPRIHKAT.repository;

import com.DPRIHKAT.dto.ContribuableDTO;
import java.util.List;

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
}
