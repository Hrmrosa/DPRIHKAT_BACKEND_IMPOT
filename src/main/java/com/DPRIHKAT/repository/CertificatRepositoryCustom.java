package com.DPRIHKAT.repository;

import com.DPRIHKAT.dto.CertificatDetailDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface pour les méthodes personnalisées du repository CertificatRepository
 * Utilise des DTOs pour récupérer les informations détaillées des certificats
 */
public interface CertificatRepositoryCustom {
    
    /**
     * Récupère un certificat détaillé par son ID
     * @param id ID du certificat
     * @return Le certificat détaillé avec les informations du contribuable, propriété/véhicule, agent, paiement et taxation
     */
    Optional<CertificatDetailDTO> findCertificatDetailById(UUID id);
    
    /**
     * Récupère tous les certificats détaillés
     * @return Liste des certificats détaillés avec les informations complètes
     */
    List<CertificatDetailDTO> findAllCertificatDetails();
    
    /**
     * Récupère les certificats détaillés d'un contribuable
     * @param contribuableId ID du contribuable
     * @return Liste des certificats détaillés du contribuable
     */
    List<CertificatDetailDTO> findCertificatDetailsByContribuableId(UUID contribuableId);
    
    /**
     * Récupère les certificats détaillés pour un véhicule
     * @param vehiculeId ID du véhicule
     * @return Liste des certificats détaillés pour le véhicule
     */
    List<CertificatDetailDTO> findCertificatDetailsByVehiculeId(UUID vehiculeId);
    
    /**
     * Récupère les certificats détaillés pour une propriété
     * @param proprieteId ID de la propriété
     * @return Liste des certificats détaillés pour la propriété
     */
    List<CertificatDetailDTO> findCertificatDetailsByProprieteId(UUID proprieteId);
    
    /**
     * Récupère les certificats détaillés liés à une taxation
     * @param taxationId ID de la taxation
     * @return Liste des certificats détaillés liés à la taxation
     */
    List<CertificatDetailDTO> findCertificatDetailsByTaxationId(UUID taxationId);
}
