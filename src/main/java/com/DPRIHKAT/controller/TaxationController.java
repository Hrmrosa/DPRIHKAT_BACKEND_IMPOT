package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.TaxationDTO;
import com.DPRIHKAT.dto.TaxationRequestDTO;
import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.DPRIHKAT.repository.AgentRepository;
import com.DPRIHKAT.repository.DeclarationRepository;
import com.DPRIHKAT.repository.NatureImpotRepository;
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
    private DeclarationRepository declarationRepository;

    @Autowired
    private NatureImpotRepository natureImpotRepository;

    /**
     * Récupère toutes les taxations
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @return Page de taxations
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getAllTaxations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Taxation> pageTaxations = taxationService.getAllTaxationsPaginated(pageable);
            
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", pageTaxations.getTotalElements());
            data.put("totalPages", pageTaxations.getTotalPages());
            data.put("currentPage", pageTaxations.getNumber());
            data.put("taxations", pageTaxations.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", "Erreur lors de la récupération des taxations", e.getMessage()));
        }
    }
    
    /**
     * Récupère toutes les taxations avec pagination
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @param sortBy champ de tri
     * @param sortDir direction du tri (asc ou desc)
     * @return Page de taxations
     */
    @GetMapping("/paginated")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getAllTaxationsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            logger.info("Récupération des taxations paginées (page={}, size={})", page, size);
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Taxation> taxationsPage = taxationService.getAllTaxationsPaginated(pageable);
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", taxationsPage.getTotalElements());
            data.put("totalPages", taxationsPage.getTotalPages());
            data.put("currentPage", taxationsPage.getNumber());
            data.put("taxations", taxationsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taxations paginées", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération des taxations paginées", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère toutes les taxations actives
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @return Page de taxations actives
     */
    @GetMapping("/actives")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getAllActiveTaxations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Récupération des taxations actives");
            Pageable pageable = PageRequest.of(page, size);
            Page<Taxation> pageTaxations = taxationService.getAllActiveTaxationsPaginated(pageable);
            
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", pageTaxations.getTotalElements());
            data.put("totalPages", pageTaxations.getTotalPages());
            data.put("currentPage", pageTaxations.getNumber());
            data.put("taxations", pageTaxations.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
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
     * Récupère toutes les taxations actives avec pagination
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @param sortBy champ de tri
     * @param sortDir direction du tri (asc ou desc)
     * @return Page de taxations actives
     */
    @GetMapping("/actives/paginated")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getAllActiveTaxationsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            logger.info("Récupération des taxations actives paginées (page={}, size={})", page, size);
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Taxation> taxationsPage = taxationService.getAllActiveTaxationsPaginated(pageable);
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", taxationsPage.getTotalElements());
            data.put("totalPages", taxationsPage.getTotalPages());
            data.put("currentPage", taxationsPage.getNumber());
            data.put("taxations", taxationsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taxations actives paginées", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération des taxations actives paginées", 
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
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @return Page de taxations pour cette propriété
     */
    @GetMapping("/by-propriete/{proprieteId}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationsByProprieteId(@PathVariable UUID proprieteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Récupération des taxations pour la propriété avec l'ID: {}", proprieteId);
            Pageable pageable = PageRequest.of(page, size);
            Page<Taxation> taxationsPage = taxationService.getTaxationsByProprieteIdPaginated(proprieteId, pageable);
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", taxationsPage.getTotalElements());
            data.put("totalPages", taxationsPage.getTotalPages());
            data.put("currentPage", taxationsPage.getNumber());
            data.put("taxations", taxationsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
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
     * Récupère toutes les taxations pour une propriété donnée avec pagination
     * @param proprieteId L'ID de la propriété
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @param sortBy champ de tri
     * @param sortDir direction du tri (asc ou desc)
     * @return Page de taxations pour cette propriété
     */
    @GetMapping("/by-propriete/{proprieteId}/paginated")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationsByProprieteIdPaginated(
            @PathVariable UUID proprieteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            logger.info("Récupération des taxations pour la propriété avec l'ID: {} paginées (page={}, size={})", proprieteId, page, size);
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Taxation> taxationsPage = taxationService.getTaxationsByProprieteIdPaginated(proprieteId, pageable);
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", taxationsPage.getTotalElements());
            data.put("totalPages", taxationsPage.getTotalPages());
            data.put("currentPage", taxationsPage.getNumber());
            data.put("taxations", taxationsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taxations pour la propriété avec l'ID: {} paginées", proprieteId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération des taxations pour la propriété paginées", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère toutes les taxations pour une déclaration donnée
     * @param declarationId L'ID de la déclaration
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @return Page de taxations pour cette déclaration
     */
    @GetMapping("/by-declaration/{declarationId}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationsByDeclarationId(@PathVariable UUID declarationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Récupération des taxations pour la déclaration avec l'ID: {}", declarationId);
            Pageable pageable = PageRequest.of(page, size);
            Page<Taxation> taxationsPage = taxationService.getTaxationsByDeclarationIdPaginated(declarationId, pageable);
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", taxationsPage.getTotalElements());
            data.put("totalPages", taxationsPage.getTotalPages());
            data.put("currentPage", taxationsPage.getNumber());
            data.put("taxations", taxationsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taxations pour la déclaration avec l'ID: {}", declarationId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération des taxations pour la déclaration", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère toutes les taxations pour une déclaration donnée avec pagination
     * @param declarationId L'ID de la déclaration
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @param sortBy champ de tri
     * @param sortDir direction du tri (asc ou desc)
     * @return Page de taxations pour cette déclaration
     */
    @GetMapping("/by-declaration/{declarationId}/paginated")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationsByDeclarationIdPaginated(
            @PathVariable UUID declarationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            logger.info("Récupération des taxations pour la déclaration avec l'ID: {} paginées (page={}, size={})", declarationId, page, size);
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Taxation> taxationsPage = taxationService.getTaxationsByDeclarationIdPaginated(declarationId, pageable);
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", taxationsPage.getTotalElements());
            data.put("totalPages", taxationsPage.getTotalPages());
            data.put("currentPage", taxationsPage.getNumber());
            data.put("taxations", taxationsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taxations pour la déclaration avec l'ID: {} paginées", declarationId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération des taxations pour la déclaration paginées", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère toutes les taxations pour un exercice donné
     * @param exercice L'exercice (année fiscale)
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @return Page de taxations pour cet exercice
     */
    @GetMapping("/by-exercice/{exercice}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationsByExercice(@PathVariable String exercice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Récupération des taxations pour l'exercice: {}", exercice);
            Pageable pageable = PageRequest.of(page, size);
            Page<Taxation> taxationsPage = taxationService.getTaxationsByExercicePaginated(exercice, pageable);
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", taxationsPage.getTotalElements());
            data.put("totalPages", taxationsPage.getTotalPages());
            data.put("currentPage", taxationsPage.getNumber());
            data.put("taxations", taxationsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
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
     * Récupère toutes les taxations pour un exercice donné avec pagination
     * @param exercice L'exercice (année fiscale)
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @param sortBy champ de tri
     * @param sortDir direction du tri (asc ou desc)
     * @return Page de taxations pour cet exercice
     */
    @GetMapping("/by-exercice/{exercice}/paginated")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationsByExercicePaginated(
            @PathVariable String exercice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            logger.info("Récupération des taxations pour l'exercice: {} paginées (page={}, size={})", exercice, page, size);
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Taxation> taxationsPage = taxationService.getTaxationsByExercicePaginated(exercice, pageable);
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", taxationsPage.getTotalElements());
            data.put("totalPages", taxationsPage.getTotalPages());
            data.put("currentPage", taxationsPage.getNumber());
            data.put("taxations", taxationsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taxations pour l'exercice: {} paginées", exercice, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération des taxations pour l'exercice paginées", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère toutes les taxations pour un type d'impôt donné
     * @param typeImpot Le type d'impôt
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @return Page de taxations pour ce type d'impôt
     */
    @GetMapping("/by-type-impot/{typeImpot}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationsByTypeImpot(@PathVariable TypeImpot typeImpot,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Récupération des taxations pour le type d'impôt: {}", typeImpot);
            Pageable pageable = PageRequest.of(page, size);
            Page<Taxation> taxationsPage = taxationService.getTaxationsByTypeImpotPaginated(typeImpot, pageable);
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", taxationsPage.getTotalElements());
            data.put("totalPages", taxationsPage.getTotalPages());
            data.put("currentPage", taxationsPage.getNumber());
            data.put("taxations", taxationsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
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
     * Récupère toutes les taxations pour un type d'impôt donné avec pagination
     * @param typeImpot Le type d'impôt
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @param sortBy champ de tri
     * @param sortDir direction du tri (asc ou desc)
     * @return Page de taxations pour ce type d'impôt
     */
    @GetMapping("/by-type-impot/{typeImpot}/paginated")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationsByTypeImpotPaginated(
            @PathVariable TypeImpot typeImpot,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            logger.info("Récupération des taxations pour le type d'impôt: {} paginées (page={}, size={})", typeImpot, page, size);
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Taxation> taxationsPage = taxationService.getTaxationsByTypeImpotPaginated(typeImpot, pageable);
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", taxationsPage.getTotalElements());
            data.put("totalPages", taxationsPage.getTotalPages());
            data.put("currentPage", taxationsPage.getNumber());
            data.put("taxations", taxationsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taxations pour le type d'impôt: {} paginées", typeImpot, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération des taxations pour le type d'impôt paginées", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère toutes les taxations pour un statut donné
     * @param statut Le statut de la taxation
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @return Page de taxations pour ce statut
     */
    @GetMapping("/by-statut/{statut}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationsByStatut(@PathVariable StatutTaxation statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Récupération des taxations pour le statut: {}", statut);
            Pageable pageable = PageRequest.of(page, size);
            Page<Taxation> taxationsPage = taxationService.getTaxationsByStatutPaginated(statut, pageable);
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", taxationsPage.getTotalElements());
            data.put("totalPages", taxationsPage.getTotalPages());
            data.put("currentPage", taxationsPage.getNumber());
            data.put("taxations", taxationsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
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
     * Récupère toutes les taxations pour un statut donné avec pagination
     * @param statut Le statut de la taxation
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @param sortBy champ de tri
     * @param sortDir direction du tri (asc ou desc)
     * @return Page de taxations pour ce statut
     */
    @GetMapping("/by-statut/{statut}/paginated")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> getTaxationsByStatutPaginated(
            @PathVariable StatutTaxation statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            logger.info("Récupération des taxations pour le statut: {} paginées (page={}, size={})", statut, page, size);
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Taxation> taxationsPage = taxationService.getTaxationsByStatutPaginated(statut, pageable);
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", taxationsPage.getTotalElements());
            data.put("totalPages", taxationsPage.getTotalPages());
            data.put("currentPage", taxationsPage.getNumber());
            data.put("taxations", taxationsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taxations pour le statut: {} paginées", statut, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_FETCH_ERROR", 
                            "Erreur lors de la récupération des taxations pour le statut paginées", 
                            e.getMessage()));
        }
    }

    /**
     * Génère une taxation pour une déclaration
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
            Taxation taxation = taxationService.generateTaxationForDeclaration(
                    request.getDeclarationId(),
                    request.getNatureImpotId(),
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
     * Valide une taxation
     * @param id L'ID de la taxation
     * @param authentication L'authentification de l'utilisateur
     * @return La taxation validée
     */
    @PutMapping("/{id}/valider")
    @PreAuthorize("hasAnyRole('CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN')")
    public ResponseEntity<?> validerTaxation(@PathVariable UUID id, Authentication authentication) {
        try {
            // Récupérer l'agent authentifié
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null || utilisateur.getAgent() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", 
                                "Seuls les agents peuvent valider des taxations"));
            }

            // Valider la taxation
            return taxationService.validerTaxation(id, utilisateur.getAgent().getId())
                    .map(taxation -> ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                            "message", "Taxation validée avec succès",
                            "taxation", convertToDTO(taxation)
                    ))))
                    .orElse(ResponseEntity
                            .notFound()
                            .build());
        } catch (Exception e) {
            logger.error("Erreur lors de la validation de la taxation avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_VALIDATION_ERROR", 
                            "Erreur lors de la validation de la taxation", 
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
    public ResponseEntity<?> accorderExoneration(@PathVariable UUID id, @RequestParam String motif) {
        try {
            logger.info("Accord d'une exonération pour la taxation avec l'ID: {}", id);
            return taxationService.accorderExoneration(id, motif)
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
    public ResponseEntity<?> desactiverTaxation(@PathVariable UUID id) {
        try {
            logger.info("Désactivation de la taxation avec l'ID: {}", id);
            boolean deactivated = taxationService.desactiverTaxation(id);
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
     * Convertit une entité Taxation en TaxationDTO
     * @param taxation L'entité Taxation à convertir
     * @return Le DTO TaxationDTO correspondant
     */
    private TaxationDTO convertToDTO(Taxation taxation) {
        Declaration declaration = taxation.getDeclaration();
        Propriete propriete = declaration != null ? declaration.getPropriete() : null;
        Contribuable contribuable = declaration != null ? declaration.getContribuable() : null;
        
        // Déterminer la nature d'impôt à partir du type d'impôt
        NatureImpot natureImpot = null;
        if (taxation.getTypeImpot() != null) {
            Optional<NatureImpot> natureImpotOpt = natureImpotRepository.findByCode(taxation.getTypeImpot().name());
            natureImpot = natureImpotOpt.orElse(null);
        }
        
        return new TaxationDTO(
                taxation.getId(),
                taxation.getDateTaxation(),
                taxation.getMontant(),
                taxation.getExercice(),
                taxation.getStatut(),
                taxation.getTypeImpot(),
                taxation.isExoneration(),
                taxation.isActif(),
                declaration != null ? declaration.getId() : null,
                declaration != null ? declaration.getDateDeclaration() : null,
                propriete != null ? propriete.getId() : null,
                propriete != null ? propriete.getAdresse() : null,
                contribuable != null ? contribuable.getId() : null,
                contribuable != null ? contribuable.getNom() : null,
                natureImpot != null ? natureImpot.getId() : null,
                natureImpot != null ? natureImpot.getCode() : null,
                natureImpot != null ? natureImpot.getNom() : null,
                taxation.getAgentTaxateur() != null ? taxation.getAgentTaxateur().getId() : null,
                taxation.getAgentTaxateur() != null ? taxation.getAgentTaxateur().getNom() : null,
                taxation.getAgentValidateur() != null ? taxation.getAgentValidateur().getId() : null,
                taxation.getAgentValidateur() != null ? taxation.getAgentValidateur().getNom() : null,
                taxation.getPaiement() != null ? taxation.getPaiement().getId() : null,
                null // Apurement ID (à implémenter si nécessaire)
        );
    }
}
