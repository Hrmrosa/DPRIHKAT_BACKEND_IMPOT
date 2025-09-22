package com.DPRIHKAT.service;

import com.DPRIHKAT.dto.CertificatDetailDTO;
import com.DPRIHKAT.repository.CertificatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour gérer les certificats détaillés avec les informations du contribuable,
 * des propriétés/véhicules, de l'agent, du paiement et de la taxation
 */
@Service
public class CertificatDetailService {
    
    private static final Logger logger = LoggerFactory.getLogger(CertificatDetailService.class);
    
    @Autowired
    private CertificatRepository certificatRepository;
    
    /**
     * Récupère un certificat détaillé par son ID
     * @param id ID du certificat
     * @return Le certificat détaillé avec toutes les informations associées
     */
    public Optional<CertificatDetailDTO> getCertificatDetailById(UUID id) {
        logger.info("Récupération du certificat détaillé avec l'ID: {}", id);
        return certificatRepository.findCertificatDetailById(id);
    }
    
    /**
     * Récupère tous les certificats détaillés
     * @return Liste des certificats détaillés avec toutes les informations associées
     */
    public List<CertificatDetailDTO> getAllCertificatDetails() {
        logger.info("Récupération de tous les certificats détaillés");
        return certificatRepository.findAllCertificatDetails();
    }
    
    /**
     * Récupère les certificats détaillés d'un contribuable
     * @param contribuableId ID du contribuable
     * @return Liste des certificats détaillés du contribuable
     */
    public List<CertificatDetailDTO> getCertificatDetailsByContribuableId(UUID contribuableId) {
        logger.info("Récupération des certificats détaillés pour le contribuable avec l'ID: {}", contribuableId);
        return certificatRepository.findCertificatDetailsByContribuableId(contribuableId);
    }
    
    /**
     * Récupère les certificats détaillés pour un véhicule
     * @param vehiculeId ID du véhicule
     * @return Liste des certificats détaillés pour le véhicule
     */
    public List<CertificatDetailDTO> getCertificatDetailsByVehiculeId(UUID vehiculeId) {
        logger.info("Récupération des certificats détaillés pour le véhicule avec l'ID: {}", vehiculeId);
        return certificatRepository.findCertificatDetailsByVehiculeId(vehiculeId);
    }
    
    /**
     * Récupère les certificats détaillés pour une propriété
     * @param proprieteId ID de la propriété
     * @return Liste des certificats détaillés pour la propriété
     */
    public List<CertificatDetailDTO> getCertificatDetailsByProprieteId(UUID proprieteId) {
        logger.info("Récupération des certificats détaillés pour la propriété avec l'ID: {}", proprieteId);
        return certificatRepository.findCertificatDetailsByProprieteId(proprieteId);
    }
    
    /**
     * Récupère les certificats détaillés liés à une taxation
     * @param taxationId ID de la taxation
     * @return Liste des certificats détaillés liés à la taxation
     */
    public List<CertificatDetailDTO> getCertificatDetailsByTaxationId(UUID taxationId) {
        logger.info("Récupération des certificats détaillés pour la taxation avec l'ID: {}", taxationId);
        return certificatRepository.findCertificatDetailsByTaxationId(taxationId);
    }
}
