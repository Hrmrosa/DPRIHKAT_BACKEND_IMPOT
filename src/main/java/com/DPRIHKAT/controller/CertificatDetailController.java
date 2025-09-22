package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.CertificatDetailDTO;
import com.DPRIHKAT.service.CertificatDetailService;
import com.DPRIHKAT.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Contrôleur pour gérer les certificats détaillés avec les informations du contribuable,
 * des propriétés/véhicules, de l'agent, du paiement et de la taxation
 */
@RestController
@RequestMapping("/api/certificats/details")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CertificatDetailController {
    
    private static final Logger logger = LoggerFactory.getLogger(CertificatDetailController.class);
    
    @Autowired
    private CertificatDetailService certificatDetailService;
    
    /**
     * Récupère un certificat détaillé par son ID
     * @param id ID du certificat
     * @return Le certificat détaillé avec les informations du contribuable, propriété/véhicule, agent, paiement et taxation
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT_CERTIFICAT', 'ADMIN')")
    public ResponseEntity<?> getCertificatDetailById(@PathVariable UUID id) {
        try {
            logger.info("Récupération du certificat détaillé avec l'ID: {}", id);
            return certificatDetailService.getCertificatDetailById(id)
                    .map(certificatDetail -> ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                            "certificatDetail", certificatDetail
                    ))))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du certificat détaillé avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CERTIFICAT_DETAIL_ERROR", 
                            "Erreur lors de la récupération du certificat détaillé", 
                            e.getMessage()));
        }
    }
    
    /**
     * Récupère tous les certificats détaillés
     * @return Liste des certificats détaillés avec les informations complètes
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('AGENT_CERTIFICAT', 'ADMIN')")
    public ResponseEntity<?> getAllCertificatDetails() {
        try {
            logger.info("Récupération de tous les certificats détaillés");
            List<CertificatDetailDTO> certificatDetails = certificatDetailService.getAllCertificatDetails();
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "certificatDetails", certificatDetails,
                    "count", certificatDetails.size()
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de tous les certificats détaillés", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CERTIFICAT_DETAILS_ERROR", 
                            "Erreur lors de la récupération de tous les certificats détaillés", 
                            e.getMessage()));
        }
    }
    
    /**
     * Récupère les certificats détaillés d'un contribuable
     * @param contribuableId ID du contribuable
     * @return Liste des certificats détaillés du contribuable
     */
    @GetMapping("/contribuable/{contribuableId}")
    @PreAuthorize("hasAnyRole('AGENT_CERTIFICAT', 'ADMIN')")
    public ResponseEntity<?> getCertificatDetailsByContribuableId(@PathVariable UUID contribuableId) {
        try {
            logger.info("Récupération des certificats détaillés pour le contribuable avec l'ID: {}", contribuableId);
            List<CertificatDetailDTO> certificatDetails = certificatDetailService.getCertificatDetailsByContribuableId(contribuableId);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "certificatDetails", certificatDetails,
                    "count", certificatDetails.size(),
                    "contribuableId", contribuableId
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des certificats détaillés pour le contribuable avec l'ID: {}", contribuableId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CERTIFICAT_DETAILS_CONTRIBUABLE_ERROR", 
                            "Erreur lors de la récupération des certificats détaillés pour le contribuable", 
                            e.getMessage()));
        }
    }
    
    /**
     * Récupère les certificats détaillés pour un véhicule
     * @param vehiculeId ID du véhicule
     * @return Liste des certificats détaillés pour le véhicule
     */
    @GetMapping("/vehicule/{vehiculeId}")
    @PreAuthorize("hasAnyRole('AGENT_CERTIFICAT', 'ADMIN')")
    public ResponseEntity<?> getCertificatDetailsByVehiculeId(@PathVariable UUID vehiculeId) {
        try {
            logger.info("Récupération des certificats détaillés pour le véhicule avec l'ID: {}", vehiculeId);
            List<CertificatDetailDTO> certificatDetails = certificatDetailService.getCertificatDetailsByVehiculeId(vehiculeId);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "certificatDetails", certificatDetails,
                    "count", certificatDetails.size(),
                    "vehiculeId", vehiculeId
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des certificats détaillés pour le véhicule avec l'ID: {}", vehiculeId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CERTIFICAT_DETAILS_VEHICULE_ERROR", 
                            "Erreur lors de la récupération des certificats détaillés pour le véhicule", 
                            e.getMessage()));
        }
    }
    
    /**
     * Récupère les certificats détaillés pour une propriété
     * @param proprieteId ID de la propriété
     * @return Liste des certificats détaillés pour la propriété
     */
    @GetMapping("/propriete/{proprieteId}")
    @PreAuthorize("hasAnyRole('AGENT_CERTIFICAT', 'ADMIN')")
    public ResponseEntity<?> getCertificatDetailsByProprieteId(@PathVariable UUID proprieteId) {
        try {
            logger.info("Récupération des certificats détaillés pour la propriété avec l'ID: {}", proprieteId);
            List<CertificatDetailDTO> certificatDetails = certificatDetailService.getCertificatDetailsByProprieteId(proprieteId);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "certificatDetails", certificatDetails,
                    "count", certificatDetails.size(),
                    "proprieteId", proprieteId
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des certificats détaillés pour la propriété avec l'ID: {}", proprieteId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CERTIFICAT_DETAILS_PROPRIETE_ERROR", 
                            "Erreur lors de la récupération des certificats détaillés pour la propriété", 
                            e.getMessage()));
        }
    }
    
    /**
     * Récupère les certificats détaillés liés à une taxation
     * @param taxationId ID de la taxation
     * @return Liste des certificats détaillés liés à la taxation
     */
    @GetMapping("/taxation/{taxationId}")
    @PreAuthorize("hasAnyRole('AGENT_CERTIFICAT', 'ADMIN')")
    public ResponseEntity<?> getCertificatDetailsByTaxationId(@PathVariable UUID taxationId) {
        try {
            logger.info("Récupération des certificats détaillés pour la taxation avec l'ID: {}", taxationId);
            List<CertificatDetailDTO> certificatDetails = certificatDetailService.getCertificatDetailsByTaxationId(taxationId);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "certificatDetails", certificatDetails,
                    "count", certificatDetails.size(),
                    "taxationId", taxationId
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des certificats détaillés pour la taxation avec l'ID: {}", taxationId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CERTIFICAT_DETAILS_TAXATION_ERROR", 
                            "Erreur lors de la récupération des certificats détaillés pour la taxation", 
                            e.getMessage()));
        }
    }
}
