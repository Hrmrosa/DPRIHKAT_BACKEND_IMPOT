package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.TaxationDTO;
import com.DPRIHKAT.dto.TaxationRequestDTO;
import com.DPRIHKAT.entity.Agent;
import com.DPRIHKAT.entity.NatureImpot;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.ProprieteImpot;
import com.DPRIHKAT.entity.Taxation;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.DPRIHKAT.repository.AgentRepository;
import com.DPRIHKAT.repository.NatureImpotRepository;
import com.DPRIHKAT.repository.ProprieteImpotRepository;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.service.TaxationService;
import com.DPRIHKAT.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contrôleur pour gérer les taxations
 * @author amateur
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/taxations")
public class TaxationController {

    private static final Logger logger = LoggerFactory.getLogger(TaxationController.class);

    @Autowired
    private TaxationService taxationService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ProprieteRepository proprieteRepository;

    @Autowired
    private ProprieteImpotRepository proprieteImpotRepository;

    @Autowired
    private NatureImpotRepository natureImpotRepository;

    /**
     * Récupère toutes les taxations
     * @param page Numéro de page (commence à 0)
     * @param size Nombre d'éléments par page
     * @param sortBy Champ de tri
     * @param sortDir Direction du tri (asc/desc)
     * @return Liste paginée de taxations
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getAllTaxations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateTaxation") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            logger.info("Récupération des taxations paginées - page: {}, size: {}", page, size);
            
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Taxation> taxationPage = taxationService.getAllTaxationsPaginated(pageable);
            
            List<TaxationDTO> content = taxationPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("taxations", content);
            response.put("currentPage", taxationPage.getNumber());
            response.put("totalItems", taxationPage.getTotalElements());
            response.put("totalPages", taxationPage.getTotalPages());
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taxations", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération des taxations", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère toutes les taxations actives
     * @return Liste des taxations actives
     */
    @GetMapping("/actives")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getAllActiveTaxations() {
        try {
            logger.info("Récupération des taxations actives");
            List<Taxation> taxations = taxationService.getAllActiveTaxations();
            List<TaxationDTO> taxationsDTO = taxations.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "taxations", taxationsDTO
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taxations actives", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération des taxations actives", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère une taxation par son ID
     * @param id L'ID de la taxation
     * @return La taxation correspondante
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationById(@PathVariable UUID id) {
        try {
            logger.info("Récupération de la taxation avec l'ID: {}", id);
            return taxationService.getTaxationById(id)
                    .map(taxation -> ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                            "taxation", convertToDTO(taxation)
                    ))))
                    .orElse(ResponseEntity
                            .notFound()
                            .build());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la taxation avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération de la taxation", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère toutes les taxations pour une propriété donnée
     * @param proprieteId L'ID de la propriété
     * @return Liste des taxations pour cette propriété
     */
    @GetMapping("/by-propriete/{proprieteId}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationsByProprieteId(@PathVariable UUID proprieteId) {
        try {
            logger.info("Récupération des taxations pour la propriété avec l'ID: {}", proprieteId);
            List<Taxation> taxations = taxationService.getTaxationsByProprieteId(proprieteId);
            List<TaxationDTO> taxationsDTO = taxations.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "taxations", taxationsDTO
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taxations pour la propriété avec l'ID: {}", proprieteId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération des taxations pour la propriété", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère toutes les taxations pour un exercice donné
     * @param exercice L'exercice (année fiscale)
     * @return Liste des taxations pour cet exercice
     */
    @GetMapping("/by-exercice/{exercice}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationsByExercice(@PathVariable Integer exercice) {
        try {
            logger.info("Récupération des taxations pour l'exercice: {}", exercice);
            List<Taxation> taxations = taxationService.getTaxationsByExercice(exercice);
            List<TaxationDTO> taxationsDTO = taxations.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "taxations", taxationsDTO
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taxations pour l'exercice: {}", exercice, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération des taxations pour l'exercice", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère toutes les taxations pour un type d'impôt donné
     * @param typeImpot Le type d'impôt
     * @return Liste des taxations pour ce type d'impôt
     */
    @GetMapping("/by-type-impot/{typeImpot}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationsByTypeImpot(@PathVariable TypeImpot typeImpot) {
        try {
            logger.info("Récupération des taxations pour le type d'impôt: {}", typeImpot);
            List<Taxation> taxations = taxationService.getTaxationsByTypeImpot(typeImpot);
            List<TaxationDTO> taxationsDTO = taxations.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "taxations", taxationsDTO
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taxations pour le type d'impôt: {}", typeImpot, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération des taxations pour le type d'impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère toutes les taxations pour un statut donné
     * @param statut Le statut de la taxation
     * @return Liste des taxations pour ce statut
     */
    @GetMapping("/by-statut/{statut}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationsByStatut(@PathVariable StatutTaxation statut) {
        try {
            logger.info("Récupération des taxations pour le statut: {}", statut);
            List<Taxation> taxations = taxationService.getTaxationsByStatut(statut);
            List<TaxationDTO> taxationsDTO = taxations.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "taxations", taxationsDTO
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taxations pour le statut: {}", statut, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération des taxations pour le statut", 
                            e.getMessage()));
        }
    }

    /**
     * Génère une taxation pour une propriété
     * @param request Les informations de la taxation à générer
     * @param authentication L'authentification de l'utilisateur
     * @return La taxation générée
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> generateTaxation(@Valid @RequestBody TaxationRequestDTO request, Authentication authentication) {
        try {
            // Récupérer l'agent authentifié
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null || utilisateur.getAgent() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", 
                                "Seuls les agents peuvent générer des taxations"));
            }

            // Générer la taxation
            Taxation taxation = taxationService.generateTaxationForProperty(
                    request.getProprieteId(),
                    request.getProprieteImpotId(),
                    request.getExercice(),
                    utilisateur.getAgent().getId()
            );

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Taxation générée avec succès",
                    "taxation", convertToDTO(taxation)
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la génération de la taxation", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_GENERATION_ERROR", 
                            "Erreur lors de la génération de la taxation", 
                            e.getMessage()));
        }
    }

    /**
     * Calcule le montant de la taxation pour une propriété
     * @param proprieteId L'ID de la propriété
     * @return Le montant de la taxation
     */
    @GetMapping("/calculate/property/{proprieteId}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'CONTRIBUABLE', 'ADMIN')")
    public ResponseEntity<?> calculateTaxForProperty(@PathVariable UUID proprieteId) {
        try {
            // Calculer le montant de la taxation
            Propriete propriete = proprieteRepository.findById(proprieteId)
                    .orElseThrow(() -> new Exception("Propriété non trouvée avec l'ID: " + proprieteId));
            
            double taxAmount = taxationService.calculateTaxForProperty(propriete);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Calcul de la taxation effectué avec succès",
                    "montant", taxAmount
            )));
        } catch (Exception e) {
            logger.error("Erreur lors du calcul de la taxation pour la propriété avec l'ID: {}", proprieteId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_CALCULATION_ERROR", 
                            "Erreur lors du calcul de la taxation", 
                            e.getMessage()));
        }
    }

    /**
     * Met à jour le statut d'une taxation
     * @param id L'ID de la taxation
     * @param statut Le nouveau statut
     * @return La taxation mise à jour
     */
    @PutMapping("/{id}/statut/{statut}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> updateTaxationStatus(@PathVariable UUID id, @PathVariable StatutTaxation statut) {
        try {
            logger.info("Mise à jour du statut de la taxation avec l'ID: {} au statut: {}", id, statut);
            return taxationService.updateTaxationStatus(id, statut)
                    .map(taxation -> ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                            "message", "Statut de la taxation mis à jour avec succès",
                            "taxation", convertToDTO(taxation)
                    ))))
                    .orElse(ResponseEntity
                            .notFound()
                            .build());
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du statut de la taxation avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_UPDATE_ERROR", 
                            "Erreur lors de la mise à jour du statut de la taxation", 
                            e.getMessage()));
        }
    }

    /**
     * Accorde une exonération pour une taxation
     * @param id L'ID de la taxation
     * @param motif Le motif de l'exonération
     * @return La taxation mise à jour
     */
    @PutMapping("/{id}/exoneration")
    @PreAuthorize("hasAnyRole('CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> grantExemption(@PathVariable UUID id, @RequestParam String motif) {
        try {
            logger.info("Accord d'une exonération pour la taxation avec l'ID: {}", id);
            return taxationService.grantExemption(id, motif)
                    .map(taxation -> ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                            "message", "Exonération accordée avec succès",
                            "taxation", convertToDTO(taxation)
                    ))))
                    .orElse(ResponseEntity
                            .notFound()
                            .build());
        } catch (Exception e) {
            logger.error("Erreur lors de l'accord d'une exonération pour la taxation avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_EXEMPTION_ERROR", 
                            "Erreur lors de l'accord d'une exonération", 
                            e.getMessage()));
        }
    }

    /**
     * Désactive une taxation (suppression logique)
     * @param id L'ID de la taxation à désactiver
     * @return Statut de la désactivation
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> deactivateTaxation(@PathVariable UUID id) {
        try {
            logger.info("Désactivation de la taxation avec l'ID: {}", id);
            boolean deactivated = taxationService.deactivateTaxation(id);
            if (deactivated) {
                return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                        "message", "Taxation désactivée avec succès"
                )));
            } else {
                return ResponseEntity
                        .notFound()
                        .build();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la désactivation de la taxation avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_DEACTIVATION_ERROR", 
                            "Erreur lors de la désactivation de la taxation", 
                            e.getMessage()));
        }
    }

    /**
     * Active une taxation
     * @param id L'ID de la taxation à activer
     * @return Statut de l'activation
     */
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> activateTaxation(@PathVariable UUID id) {
        try {
            logger.info("Activation de la taxation avec l'ID: {}", id);
            boolean activated = taxationService.activateTaxation(id);
            if (activated) {
                return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                        "message", "Taxation activée avec succès"
                )));
            } else {
                return ResponseEntity
                        .notFound()
                        .build();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'activation de la taxation avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_ACTIVATION_ERROR", 
                            "Erreur lors de l'activation de la taxation", 
                            e.getMessage()));
        }
    }

    /**
     * Convertit une entité Taxation en TaxationDTO
     * @param taxation L'entité Taxation à convertir
     * @return Le DTO TaxationDTO correspondant
     */
    private TaxationDTO convertToDTO(Taxation taxation) {
        Propriete propriete = taxation.getDeclaration().getPropriete();
        ProprieteImpot proprieteImpot = taxation.getProprieteImpot();
        Agent agentTaxateur = taxation.getAgent();

        return new TaxationDTO(
                taxation.getId(),
                taxation.getDateTaxation(),
                taxation.getMontant(),
                taxation.getExercice(),
                taxation.getStatut(),
                taxation.getTypeImpot(),
                taxation.isExoneration(),
                taxation.getMotifExoneration(),
                taxation.getDateEcheance(),
                taxation.isActif(),
                propriete != null ? propriete.getId() : null,
                propriete != null ? propriete.getAdresse() : null,
                proprieteImpot != null ? proprieteImpot.getId() : null,
                proprieteImpot != null && proprieteImpot.getNatureImpot() != null ? proprieteImpot.getNatureImpot().getCode() : null,
                proprieteImpot != null && proprieteImpot.getNatureImpot() != null ? proprieteImpot.getNatureImpot().getNom() : null,
                agentTaxateur != null ? agentTaxateur.getId() : null,
                agentTaxateur != null ? agentTaxateur.getNom() : null,
                taxation.getPaiement() != null ? taxation.getPaiement().getId() : null,
                null // Ne pas charger les apurements pour éviter l'erreur PostgreSQL "target lists can have at most 1664 entries"
        );
    }
}
