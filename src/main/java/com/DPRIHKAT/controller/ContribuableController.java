package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.service.ContribuableService;
import com.DPRIHKAT.service.ContribuableDetailsService;
import com.DPRIHKAT.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * Contrôleur pour gérer les contribuables
 */
@RestController
@RequestMapping("/api/contribuables")
public class ContribuableController {

    private static final Logger logger = LoggerFactory.getLogger(ContribuableController.class);
    private final ContribuableService contribuableService;
    private final ContribuableDetailsService contribuableDetailsService;

    public ContribuableController(ContribuableService contribuableService, 
                                ContribuableDetailsService contribuableDetailsService) {
        this.contribuableService = contribuableService;
        this.contribuableDetailsService = contribuableDetailsService;
    }

    /**
     * Récupère la liste de tous les contribuables
     * @return Liste des contribuables
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR', 'CHEF_DE_BUREAU')")
    public ResponseEntity<?> getAllContribuables() {
        try {
            logger.info("Récupération de tous les contribuables");
            Map<String, Object> contribuables = contribuableService.getAllContribuables();
            return ResponseEntity.ok(contribuables);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des contribuables", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLES_FETCH_ERROR", 
                            "Erreur lors de la récupération des contribuables", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère un contribuable par son ID
     * @param id ID du contribuable
     * @return Détails du contribuable
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR', 'CHEF_DE_BUREAU', 'CONTRIBUABLE')")
    public ResponseEntity<?> getContribuable(@PathVariable UUID id) {
        try {
            logger.info("Récupération du contribuable avec ID: {}", id);
            Map<String, Object> contribuable = contribuableService.getContribuableDetails(id);
            return ResponseEntity.ok(contribuable);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du contribuable avec ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_NOT_FOUND", 
                            "Contribuable non trouvé", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère les détails complets d'un contribuable (avec propriétés et véhicules)
     * @param id ID du contribuable
     * @return Détails complets du contribuable
     */
    @GetMapping("/{id}/details")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR')")
    public ResponseEntity<?> getContribuableDetails(@PathVariable UUID id) {
        try {
            logger.info("Récupération des détails complets du contribuable avec ID: {}", id);
            Map<String, Object> details = contribuableDetailsService.getContribuableDetails(id);
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des détails du contribuable avec ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_DETAILS_ERROR", 
                            "Erreur lors de la récupération des détails du contribuable", 
                            e.getMessage()));
        }
    }

    /**
     * Crée un nouveau contribuable
     * @param contribuable Données du contribuable à créer
     * @return Contribuable créé
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> createContribuable(@RequestBody Contribuable contribuable) {
        try {
            logger.info("Création d'un nouveau contribuable: {}", contribuable.getNom());
            Map<String, Object> nouveauContribuable = contribuableService.createContribuable(contribuable);
            return ResponseEntity.ok(nouveauContribuable);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du contribuable", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_CREATE_ERROR", 
                            "Erreur lors de la création du contribuable", 
                            e.getMessage()));
        }
    }

    /**
     * Met à jour un contribuable existant
     * @param id ID du contribuable à mettre à jour
     * @param contribuable Nouvelles données du contribuable
     * @return Contribuable mis à jour
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTRIBUABLE')")
    public ResponseEntity<?> updateContribuable(@PathVariable UUID id, @RequestBody Contribuable contribuable) {
        try {
            logger.info("Mise à jour du contribuable avec ID: {}", id);
            Map<String, Object> contribuableMisAJour = contribuableService.updateContribuable(id, contribuable);
            return ResponseEntity.ok(contribuableMisAJour);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du contribuable avec ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_UPDATE_ERROR", 
                            "Erreur lors de la mise à jour du contribuable", 
                            e.getMessage()));
        }
    }

    /**
     * Désactive un contribuable (suppression logique)
     * @param id ID du contribuable à désactiver
     * @return Message de confirmation
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> deactivateContribuable(@PathVariable UUID id) {
        try {
            logger.info("Désactivation du contribuable avec ID: {}", id);
            Map<String, Object> resultat = contribuableService.deactivateContribuable(id);
            return ResponseEntity.ok(resultat);
        } catch (Exception e) {
            logger.error("Erreur lors de la désactivation du contribuable avec ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_DEACTIVATION_ERROR", 
                            "Erreur lors de la désactivation du contribuable", 
                            e.getMessage()));
        }
    }
}
