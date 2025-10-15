package com.DPRIHKAT.controller;

import com.DPRIHKAT.util.ContribuableDetailsUtil;
import com.DPRIHKAT.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Contrôleur pour récupérer les informations des contribuables
 */
@RestController
@RequestMapping("/api/contribuables-info")
public class ContribuableInfoController {

    private static final Logger logger = LoggerFactory.getLogger(ContribuableInfoController.class);
    private final ContribuableDetailsUtil contribuableDetailsUtil;

    public ContribuableInfoController(ContribuableDetailsUtil contribuableDetailsUtil) {
        this.contribuableDetailsUtil = contribuableDetailsUtil;
    }

    /**
     * Récupère les détails complets d'un contribuable (avec propriétés et véhicules)
     * @param id ID du contribuable
     * @return Détails complets du contribuable
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR')")
    public ResponseEntity<?> getContribuableInfo(@PathVariable UUID id) {
        try {
            logger.info("Récupération des informations du contribuable avec ID: {}", id);
            
            // Récupérer les détails du contribuable
            Map<String, Object> details = contribuableDetailsUtil.getContribuableDetails(id);
            
            // Construire la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", details);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des informations du contribuable avec ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_INFO_ERROR", 
                            "Erreur lors de la récupération des informations du contribuable", 
                            e.getMessage()));
        }
    }
}
