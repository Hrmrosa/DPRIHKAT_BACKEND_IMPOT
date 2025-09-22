package com.DPRIHKAT.service;

import com.DPRIHKAT.dto.RelanceDetailDTO;
import com.DPRIHKAT.repository.RelanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour gérer les relances détaillées avec les informations du contribuable,
 * ses biens et les impôts assignés
 */
@Service
public class RelanceDetailService {
    
    private static final Logger logger = LoggerFactory.getLogger(RelanceDetailService.class);
    
    @Autowired
    private RelanceRepository relanceRepository;
    
    /**
     * Récupère une relance détaillée par son ID
     * @param id ID de la relance
     * @return La relance détaillée avec les informations du contribuable, ses biens et les impôts
     */
    public Optional<RelanceDetailDTO> getRelanceDetailById(UUID id) {
        logger.info("Récupération de la relance détaillée avec l'ID: {}", id);
        return relanceRepository.findRelanceDetailById(id);
    }
    
    /**
     * Récupère toutes les relances détaillées
     * @return Liste des relances détaillées avec les informations du contribuable, ses biens et les impôts
     */
    public List<RelanceDetailDTO> getAllRelanceDetails() {
        logger.info("Récupération de toutes les relances détaillées");
        return relanceRepository.findAllRelanceDetails();
    }
    
    /**
     * Récupère les relances détaillées d'un contribuable
     * @param contribuableId ID du contribuable
     * @return Liste des relances détaillées du contribuable
     */
    public List<RelanceDetailDTO> getRelanceDetailsByContribuableId(UUID contribuableId) {
        logger.info("Récupération des relances détaillées pour le contribuable avec l'ID: {}", contribuableId);
        return relanceRepository.findRelanceDetailsByContribuableId(contribuableId);
    }
}
