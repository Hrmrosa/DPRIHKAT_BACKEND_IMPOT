package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.RelanceDetailDTO;
import com.DPRIHKAT.service.RelanceDetailService;
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
 * Contrôleur pour gérer les relances détaillées avec les informations du contribuable,
 * ses biens et les impôts assignés
 */
@RestController
@RequestMapping("/api/relances/details")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RelanceDetailController {
    
    private static final Logger logger = LoggerFactory.getLogger(RelanceDetailController.class);
    
    @Autowired
    private RelanceDetailService relanceDetailService;
    
    /**
     * Récupère une relance détaillée par son ID
     * @param id ID de la relance
     * @return La relance détaillée avec les informations du contribuable, ses biens et les impôts
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT_RECOUVREMENT', 'ADMIN')")
    public ResponseEntity<?> getRelanceDetailById(@PathVariable UUID id) {
        try {
            logger.info("Récupération de la relance détaillée avec l'ID: {}", id);
            return relanceDetailService.getRelanceDetailById(id)
                    .map(relanceDetail -> ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                            "relanceDetail", relanceDetail
                    ))))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la relance détaillée avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("RELANCE_DETAIL_ERROR", 
                            "Erreur lors de la récupération de la relance détaillée", 
                            e.getMessage()));
        }
    }
    
    /**
     * Récupère toutes les relances détaillées
     * @return Liste des relances détaillées avec les informations du contribuable, ses biens et les impôts
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('AGENT_RECOUVREMENT', 'ADMIN')")
    public ResponseEntity<?> getAllRelanceDetails() {
        try {
            logger.info("Récupération de toutes les relances détaillées");
            List<RelanceDetailDTO> relanceDetails = relanceDetailService.getAllRelanceDetails();
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "relanceDetails", relanceDetails,
                    "count", relanceDetails.size()
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de toutes les relances détaillées", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("RELANCE_DETAILS_ERROR", 
                            "Erreur lors de la récupération de toutes les relances détaillées", 
                            e.getMessage()));
        }
    }
    
    /**
     * Récupère les relances détaillées d'un contribuable
     * @param contribuableId ID du contribuable
     * @return Liste des relances détaillées du contribuable
     */
    @GetMapping("/contribuable/{contribuableId}")
    @PreAuthorize("hasAnyRole('AGENT_RECOUVREMENT', 'ADMIN')")
    public ResponseEntity<?> getRelanceDetailsByContribuableId(@PathVariable UUID contribuableId) {
        try {
            logger.info("Récupération des relances détaillées pour le contribuable avec l'ID: {}", contribuableId);
            List<RelanceDetailDTO> relanceDetails = relanceDetailService.getRelanceDetailsByContribuableId(contribuableId);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "relanceDetails", relanceDetails,
                    "count", relanceDetails.size(),
                    "contribuableId", contribuableId
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des relances détaillées pour le contribuable avec l'ID: {}", contribuableId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("RELANCE_DETAILS_CONTRIBUABLE_ERROR", 
                            "Erreur lors de la récupération des relances détaillées pour le contribuable", 
                            e.getMessage()));
        }
    }
}
