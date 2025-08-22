package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.DeclarationRequest;
import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.StatutDeclaration;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.DPRIHKAT.repository.DeclarationRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.service.DeclarationService;
import com.DPRIHKAT.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/declarations")
public class DeclarationController {

    @Autowired
    private DeclarationService declarationService;

    @Autowired
    private DeclarationRepository declarationRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping("/soumettre")
    @PreAuthorize("hasRole('CONTRIBUABLE')")
    public ResponseEntity<?> soumettreDeclaration(@Valid @RequestBody DeclarationRequest declarationRequest, Authentication authentication) {
        try {
            // Check if the submission period is valid (2 Jan to 1 Feb)
            LocalDate currentDate = LocalDate.now();
            LocalDate startDate = LocalDate.of(currentDate.getYear(), 1, 2);
            LocalDate endDate = LocalDate.of(currentDate.getYear(), 2, 1);

            if (currentDate.isBefore(startDate) || currentDate.isAfter(endDate)) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_PERIOD", "Période de déclaration invalide", "Les déclarations en ligne ne peuvent être soumises qu'entre le 2 janvier et le 1er février"));
            }

            // Get the authenticated user
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null || utilisateur.getContribuable() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les contribuables peuvent soumettre des déclarations"));
            }

            // Check if geolocation is provided for IF, IRL, RL, ICM, IRV
            if ((declarationRequest.getTypeImpot() == TypeImpot.IF ||
                    declarationRequest.getTypeImpot() == TypeImpot.IRL ||
                    declarationRequest.getTypeImpot() == TypeImpot.RL ||
                    declarationRequest.getTypeImpot() == TypeImpot.ICM ||
                    declarationRequest.getTypeImpot() == TypeImpot.IRV) &&
                    declarationRequest.getLocation() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("MISSING_LOCATION", "Géolocalisation requise", "La géolocalisation est obligatoire pour ce type de déclaration"));
            }

            // Create and save the declaration
            Declaration declaration = declarationService.createDeclaration(declarationRequest, utilisateur.getContribuable());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("declaration", declaration)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DECLARATION_SUBMIT_ERROR", "Erreur lors de la soumission de la déclaration", e.getMessage()));
        }
    }

    @PostMapping("/manuelle")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> enregistrerDeclarationManuelle(@Valid @RequestBody DeclarationRequest declarationRequest, Authentication authentication) {
        try {
            // Get the authenticated agent
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null || utilisateur.getAgent() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les agents peuvent enregistrer des déclarations manuelles"));
            }

            // Create and save the declaration
            Declaration declaration = declarationService.createManualDeclaration(declarationRequest, utilisateur.getAgent());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("declaration", declaration)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DECLARATION_MANUAL_ERROR", "Erreur lors de l'enregistrement de la déclaration manuelle", e.getMessage()));
        }
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'CONTRIBUABLE')")
    public ResponseEntity<?> getAllDeclarations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Authentication authentication) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Declaration> declarationPage;

            // If user is a contributor, only show their declarations
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur != null && utilisateur.getContribuable() != null) {
                declarationPage = declarationRepository.findByContribuableId(utilisateur.getContribuable().getId(), pageable);
            } else {
                declarationPage = declarationRepository.findAll(pageable);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("declarations", declarationPage.getContent());
            response.put("currentPage", declarationPage.getNumber());
            response.put("totalItems", declarationPage.getTotalElements());
            response.put("totalPages", declarationPage.getTotalPages());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DECLARATION_FETCH_ERROR", "Erreur lors de la récupération des déclarations", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'CONTRIBUABLE')")
    public ResponseEntity<?> getDeclarationById(@PathVariable UUID id, Authentication authentication) {
        try {
            Declaration declaration = declarationRepository.findById(id)
                    .orElse(null);

            if (declaration == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("DECLARATION_NOT_FOUND", "Déclaration non trouvée", "Aucune déclaration avec cet ID n'existe"));
            }

            // Check if user is authorized to view this declaration
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur != null && utilisateur.getContribuable() != null &&
                    !declaration.getPropriete().getProprietaire().getId().equals(utilisateur.getContribuable().getId())) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("UNAUTHORIZED", "Non autorisé", "Vous n'êtes pas autorisé à voir cette déclaration"));
            }

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("declaration", declaration)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DECLARATION_FETCH_ERROR", "Erreur lors de la récupération de la déclaration", e.getMessage()));
        }
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR')")
    public ResponseEntity<?> getDeclarationsByType(
            @PathVariable TypeImpot type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Declaration> declarationPage = declarationRepository.findByTypeImpot(type, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("declarations", declarationPage.getContent());
            response.put("currentPage", declarationPage.getNumber());
            response.put("totalItems", declarationPage.getTotalElements());
            response.put("totalPages", declarationPage.getTotalPages());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DECLARATION_FETCH_ERROR", "Erreur lors de la récupération des déclarations par type", e.getMessage()));
        }
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR')")
    public ResponseEntity<?> getDeclarationsByStatut(
            @PathVariable StatutDeclaration statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Declaration> declarationPage = declarationRepository.findByStatut(statut, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("declarations", declarationPage.getContent());
            response.put("currentPage", declarationPage.getNumber());
            response.put("totalItems", declarationPage.getTotalElements());
            response.put("totalPages", declarationPage.getTotalPages());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DECLARATION_FETCH_ERROR", "Erreur lors de la récupération des déclarations par statut", e.getMessage()));
        }
    }
}
