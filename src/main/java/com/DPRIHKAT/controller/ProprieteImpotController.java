package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.ProprieteImpotDTO;
import com.DPRIHKAT.dto.ProprieteImpotRequestDTO;
import com.DPRIHKAT.entity.NatureImpot;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.ProprieteImpot;
import com.DPRIHKAT.repository.NatureImpotRepository;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.service.ProprieteImpotService;
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
 * Contrôleur pour gérer les liens entre propriétés et impôts
 * @author amateur
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/proprietes-impots")
public class ProprieteImpotController {

    private static final Logger logger = LoggerFactory.getLogger(ProprieteImpotController.class);

    @Autowired
    private ProprieteImpotService proprieteImpotService;

    @Autowired
    private ProprieteRepository proprieteRepository;

    @Autowired
    private NatureImpotRepository natureImpotRepository;

    /**
     * Récupère tous les liens entre propriétés et impôts
     * @return Liste de tous les liens entre propriétés et impôts
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_DE_DIVISION', 'CHEF_DE_BUREAU', 'TAXATEUR')")
    public ResponseEntity<?> getAllProprietesImpots() {
        try {
            logger.info("Récupération de tous les liens entre propriétés et impôts");
            List<ProprieteImpot> proprietesImpots = proprieteImpotService.getAllProprietesImpots();
            List<ProprieteImpotDTO> proprietesImpotsDTO = proprietesImpots.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "proprietesImpots", proprietesImpotsDTO
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des liens entre propriétés et impôts", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETE_IMPOT_FETCH_ERROR", 
                            "Erreur lors de la récupération des liens entre propriétés et impôts", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère un lien entre propriété et impôt par son ID
     * @param id L'ID du lien
     * @return Le lien correspondant
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_DE_DIVISION', 'CHEF_DE_BUREAU', 'TAXATEUR')")
    public ResponseEntity<?> getProprieteImpotById(@PathVariable UUID id) {
        try {
            logger.info("Récupération du lien entre propriété et impôt avec l'ID: {}", id);
            return proprieteImpotService.getProprieteImpotById(id)
                    .map(proprieteImpot -> ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                            "proprieteImpot", convertToDTO(proprieteImpot)
                    ))))
                    .orElse(ResponseEntity
                            .notFound()
                            .build());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du lien entre propriété et impôt avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETE_IMPOT_FETCH_ERROR", 
                            "Erreur lors de la récupération du lien entre propriété et impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère tous les liens pour une propriété donnée
     * @param proprieteId L'ID de la propriété
     * @return Liste des liens pour cette propriété
     */
    @GetMapping("/by-propriete/{proprieteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_DE_DIVISION', 'CHEF_DE_BUREAU', 'TAXATEUR')")
    public ResponseEntity<?> getProprieteImpotsByProprieteId(@PathVariable UUID proprieteId) {
        try {
            logger.info("Récupération des liens pour la propriété avec l'ID: {}", proprieteId);
            List<ProprieteImpot> proprietesImpots = proprieteImpotService.getProprieteImpotsByProprieteId(proprieteId);
            List<ProprieteImpotDTO> proprietesImpotsDTO = proprietesImpots.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "proprietesImpots", proprietesImpotsDTO
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des liens pour la propriété avec l'ID: {}", proprieteId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETE_IMPOT_FETCH_ERROR", 
                            "Erreur lors de la récupération des liens pour la propriété", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère tous les liens pour une nature d'impôt donnée
     * @param natureImpotId L'ID de la nature d'impôt
     * @return Liste des liens pour cette nature d'impôt
     */
    @GetMapping("/by-nature-impot/{natureImpotId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_DE_DIVISION', 'CHEF_DE_BUREAU', 'TAXATEUR')")
    public ResponseEntity<?> getProprieteImpotsByNatureImpotId(@PathVariable UUID natureImpotId) {
        try {
            logger.info("Récupération des liens pour la nature d'impôt avec l'ID: {}", natureImpotId);
            List<ProprieteImpot> proprietesImpots = proprieteImpotService.getProprieteImpotsByNatureImpotId(natureImpotId);
            List<ProprieteImpotDTO> proprietesImpotsDTO = proprietesImpots.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "proprietesImpots", proprietesImpotsDTO
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des liens pour la nature d'impôt avec l'ID: {}", natureImpotId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETE_IMPOT_FETCH_ERROR", 
                            "Erreur lors de la récupération des liens pour la nature d'impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Crée un nouveau lien entre une propriété et une nature d'impôt
     * @param request Les informations du lien à créer
     * @return Le lien créé
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_DE_DIVISION', 'CHEF_DE_BUREAU', 'TAXATEUR')")
    public ResponseEntity<?> createProprieteImpot(@Valid @RequestBody ProprieteImpotRequestDTO request) {
        try {
            logger.info("Création d'un nouveau lien entre propriété et impôt");
            return proprieteImpotService.createProprieteImpot(
                    request.getProprieteId(),
                    request.getNatureImpotId(),
                    request.getTauxImposition(),
                    request.getCommentaire()
            )
                    .map(proprieteImpot -> ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                            "proprieteImpot", convertToDTO(proprieteImpot)
                    ))))
                    .orElse(ResponseEntity
                            .badRequest()
                            .body(ResponseUtil.createErrorResponse("PROPRIETE_IMPOT_CREATE_ERROR", 
                                    "Erreur lors de la création du lien entre propriété et impôt", 
                                    "Un lien actif existe déjà ou les entités référencées n'existent pas")));
        } catch (Exception e) {
            logger.error("Erreur lors de la création du lien entre propriété et impôt", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETE_IMPOT_CREATE_ERROR", 
                            "Erreur lors de la création du lien entre propriété et impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Met à jour un lien existant entre une propriété et une nature d'impôt
     * @param id L'ID du lien à mettre à jour
     * @param request Les nouvelles informations du lien
     * @return Le lien mis à jour
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_DE_DIVISION', 'CHEF_DE_BUREAU', 'TAXATEUR')")
    public ResponseEntity<?> updateProprieteImpot(@PathVariable UUID id, @Valid @RequestBody ProprieteImpotRequestDTO request) {
        try {
            logger.info("Mise à jour du lien entre propriété et impôt avec l'ID: {}", id);
            return proprieteImpotService.updateProprieteImpot(
                    id,
                    request.getTauxImposition(),
                    request.getCommentaire()
            )
                    .map(proprieteImpot -> ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                            "proprieteImpot", convertToDTO(proprieteImpot)
                    ))))
                    .orElse(ResponseEntity
                            .notFound()
                            .build());
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du lien entre propriété et impôt avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETE_IMPOT_UPDATE_ERROR", 
                            "Erreur lors de la mise à jour du lien entre propriété et impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Désactive un lien entre une propriété et une nature d'impôt (suppression logique)
     * @param id L'ID du lien à désactiver
     * @return Statut de la désactivation
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_DE_DIVISION')")
    public ResponseEntity<?> deactivateProprieteImpot(@PathVariable UUID id) {
        try {
            logger.info("Désactivation du lien entre propriété et impôt avec l'ID: {}", id);
            boolean deactivated = proprieteImpotService.deactivateProprieteImpot(id);
            if (deactivated) {
                return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                        "message", "Lien entre propriété et impôt désactivé avec succès"
                )));
            } else {
                return ResponseEntity
                        .notFound()
                        .build();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la désactivation du lien entre propriété et impôt avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETE_IMPOT_DEACTIVATE_ERROR", 
                            "Erreur lors de la désactivation du lien entre propriété et impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Active un lien entre une propriété et une nature d'impôt
     * @param id L'ID du lien à activer
     * @return Statut de l'activation
     */
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_DE_DIVISION')")
    public ResponseEntity<?> activateProprieteImpot(@PathVariable UUID id) {
        try {
            logger.info("Activation du lien entre propriété et impôt avec l'ID: {}", id);
            boolean activated = proprieteImpotService.activateProprieteImpot(id);
            if (activated) {
                return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                        "message", "Lien entre propriété et impôt activé avec succès"
                )));
            } else {
                return ResponseEntity
                        .notFound()
                        .build();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'activation du lien entre propriété et impôt avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETE_IMPOT_ACTIVATE_ERROR", 
                            "Erreur lors de l'activation du lien entre propriété et impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Convertit une entité ProprieteImpot en ProprieteImpotDTO
     * @param proprieteImpot L'entité ProprieteImpot à convertir
     * @return Le DTO ProprieteImpotDTO correspondant
     */
    private ProprieteImpotDTO convertToDTO(ProprieteImpot proprieteImpot) {
        Propriete propriete = proprieteImpot.getPropriete();
        NatureImpot natureImpot = proprieteImpot.getNatureImpot();
        
        return new ProprieteImpotDTO(
                proprieteImpot.getId(),
                propriete.getId(),
                propriete.getAdresse(),
                natureImpot.getId(),
                natureImpot.getCode(),
                natureImpot.getNom(),
                proprieteImpot.getDateCreation(),
                proprieteImpot.getDateModification(),
                proprieteImpot.getTauxImposition(),
                proprieteImpot.getCommentaire(),
                proprieteImpot.isActif()
        );
    }
}
