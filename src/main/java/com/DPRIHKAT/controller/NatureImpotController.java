package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.NatureImpotDTO;
import com.DPRIHKAT.entity.NatureImpot;
import com.DPRIHKAT.service.NatureImpotService;
import com.DPRIHKAT.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contrôleur pour gérer les natures d'impôt
 * @author amateur
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/natures-impot")
public class NatureImpotController {

    private static final Logger logger = LoggerFactory.getLogger(NatureImpotController.class);

    @Autowired
    private NatureImpotService natureImpotService;

    /**
     * Récupère toutes les natures d'impôt
     * @return Liste de toutes les natures d'impôt
     */
    @GetMapping
    public ResponseEntity<?> getAllNaturesImpot() {
        try {
            logger.info("Récupération de toutes les natures d'impôt");
            List<NatureImpot> naturesImpot = natureImpotService.getAllNaturesImpot();
            List<NatureImpotDTO> naturesImpotDTO = naturesImpot.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "naturesImpot", naturesImpotDTO
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des natures d'impôt", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_FETCH_ERROR", 
                            "Erreur lors de la récupération des natures d'impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère toutes les natures d'impôt actives
     * @return Liste des natures d'impôt actives
     */
    @GetMapping("/actives")
    public ResponseEntity<?> getAllActiveNaturesImpot() {
        try {
            logger.info("Récupération des natures d'impôt actives");
            List<NatureImpot> naturesImpot = natureImpotService.getAllActiveNaturesImpot();
            List<NatureImpotDTO> naturesImpotDTO = naturesImpot.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "naturesImpot", naturesImpotDTO
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des natures d'impôt actives", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_FETCH_ERROR", 
                            "Erreur lors de la récupération des natures d'impôt actives", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère une nature d'impôt par son ID
     * @param id L'ID de la nature d'impôt
     * @return La nature d'impôt correspondante
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getNatureImpotById(@PathVariable UUID id) {
        try {
            logger.info("Récupération de la nature d'impôt avec l'ID: {}", id);
            return natureImpotService.getNatureImpotById(id)
                    .map(natureImpot -> ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                            "natureImpot", convertToDTO(natureImpot)
                    ))))
                    .orElse(ResponseEntity
                            .notFound()
                            .build());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la nature d'impôt avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_FETCH_ERROR", 
                            "Erreur lors de la récupération de la nature d'impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Crée une nouvelle nature d'impôt
     * @param natureImpotDTO Les informations de la nature d'impôt à créer
     * @return La nature d'impôt créée
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createNatureImpot(@Valid @RequestBody NatureImpotDTO natureImpotDTO) {
        try {
            logger.info("Création d'une nouvelle nature d'impôt: {}", natureImpotDTO.getCode());
            NatureImpot natureImpot = new NatureImpot();
            natureImpot.setCode(natureImpotDTO.getCode());
            natureImpot.setNom(natureImpotDTO.getNom());
            natureImpot.setDescription(natureImpotDTO.getDescription());
            natureImpot.setActif(true);
            
            NatureImpot createdNatureImpot = natureImpotService.createNatureImpot(natureImpot);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "natureImpot", convertToDTO(createdNatureImpot)
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la nature d'impôt", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_CREATE_ERROR", 
                            "Erreur lors de la création de la nature d'impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Met à jour une nature d'impôt existante
     * @param id L'ID de la nature d'impôt à mettre à jour
     * @param natureImpotDTO Les nouvelles informations de la nature d'impôt
     * @return La nature d'impôt mise à jour
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateNatureImpot(@PathVariable UUID id, @Valid @RequestBody NatureImpotDTO natureImpotDTO) {
        try {
            logger.info("Mise à jour de la nature d'impôt avec l'ID: {}", id);
            NatureImpot natureImpot = new NatureImpot();
            natureImpot.setNom(natureImpotDTO.getNom());
            natureImpot.setDescription(natureImpotDTO.getDescription());
            
            return natureImpotService.updateNatureImpot(id, natureImpot)
                    .map(updatedNatureImpot -> ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                            "natureImpot", convertToDTO(updatedNatureImpot)
                    ))))
                    .orElse(ResponseEntity
                            .notFound()
                            .build());
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la nature d'impôt avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_UPDATE_ERROR", 
                            "Erreur lors de la mise à jour de la nature d'impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Désactive une nature d'impôt (suppression logique)
     * @param id L'ID de la nature d'impôt à désactiver
     * @return Statut de la désactivation
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateNatureImpot(@PathVariable UUID id) {
        try {
            logger.info("Désactivation de la nature d'impôt avec l'ID: {}", id);
            boolean deactivated = natureImpotService.deactivateNatureImpot(id);
            if (deactivated) {
                return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                        "message", "Nature d'impôt désactivée avec succès"
                )));
            } else {
                return ResponseEntity
                        .notFound()
                        .build();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la désactivation de la nature d'impôt avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_DEACTIVATE_ERROR", 
                            "Erreur lors de la désactivation de la nature d'impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Active une nature d'impôt
     * @param id L'ID de la nature d'impôt à activer
     * @return Statut de l'activation
     */
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateNatureImpot(@PathVariable UUID id) {
        try {
            logger.info("Activation de la nature d'impôt avec l'ID: {}", id);
            boolean activated = natureImpotService.activateNatureImpot(id);
            if (activated) {
                return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                        "message", "Nature d'impôt activée avec succès"
                )));
            } else {
                return ResponseEntity
                        .notFound()
                        .build();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'activation de la nature d'impôt avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_ACTIVATE_ERROR", 
                            "Erreur lors de l'activation de la nature d'impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Convertit une entité NatureImpot en NatureImpotDTO
     * @param natureImpot L'entité NatureImpot à convertir
     * @return Le DTO NatureImpotDTO correspondant
     */
    private NatureImpotDTO convertToDTO(NatureImpot natureImpot) {
        return new NatureImpotDTO(
                natureImpot.getId(),
                natureImpot.getCode(),
                natureImpot.getNom(),
                natureImpot.getDescription(),
                natureImpot.isActif()
        );
    }
}
