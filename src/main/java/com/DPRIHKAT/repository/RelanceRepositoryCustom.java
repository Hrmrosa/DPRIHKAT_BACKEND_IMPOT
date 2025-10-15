package com.DPRIHKAT.repository;

import com.DPRIHKAT.dto.RelanceDetailDTO;
import com.DPRIHKAT.entity.Relance;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface pour les méthodes personnalisées du repository RelanceRepository
 * Utilise des DTOs pour récupérer les informations détaillées des relances
 */
public interface RelanceRepositoryCustom {
    
    /**
     * Récupère une relance détaillée par son ID
     * @param id ID de la relance
     * @return La relance détaillée avec les informations du contribuable, ses biens et les impôts
     */
    Optional<RelanceDetailDTO> findRelanceDetailById(UUID id);
    
    /**
     * Récupère toutes les relances détaillées
     * @return Liste des relances détaillées avec les informations du contribuable, ses biens et les impôts
     */
    List<RelanceDetailDTO> findAllRelanceDetails();
    
    /**
     * Récupère les relances détaillées d'un contribuable
     * @param contribuableId ID du contribuable
     * @return Liste des relances détaillées du contribuable
     */
    List<RelanceDetailDTO> findRelanceDetailsByContribuableId(UUID contribuableId);
    
    /**
     * Compte le nombre de relances par statut et date d'envoi
     * @param statut Statut des relances
     * @param startDate Date de début de la période
     * @param endDate Date de fin de la période
     * @return Nombre de relances correspondant aux critères
     */
    long countByStatutAndDateEnvoiBetween(String statut, LocalDate startDate, LocalDate endDate);
    
    /**
     * Récupère les relances associées à un dossier de recouvrement
     * @param dossierRecouvrementId ID du dossier de recouvrement
     * @return Liste des relances associées au dossier de recouvrement
     */
    List<Relance> findByDossierRecouvrementId(UUID dossierRecouvrementId);
}
