package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Apurement;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.StatutApurement;
import com.DPRIHKAT.entity.enums.TypeApurement;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.service.ApurementService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/apurements")
public class ApurementController {

    @Autowired
    private ApurementService apurementService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping("/create/{declarationId}")
    @PreAuthorize("hasAnyRole('APUREUR', 'RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> createApurement(
            @PathVariable UUID declarationId,
            @RequestParam TypeApurement type,
            Authentication authentication) {
        try {
            // Get the authenticated user
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null || utilisateur.getAgent() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les agents peuvent créer des apurements"));
            }

            Apurement apurement = apurementService.createApurement(declarationId, utilisateur.getId(), type);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "apurement", apurement,
                    "message", "Apurement créé avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("APUREMENT_CREATION_ERROR", "Erreur lors de la création de l'apurement", e.getMessage()));
        }
    }

    @PostMapping("/validate/{apurementId}")
    @PreAuthorize("hasAnyRole('APUREUR', 'RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> validateApurement(
            @PathVariable UUID apurementId,
            Authentication authentication) {
        try {
            // Get the authenticated user
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null || utilisateur.getAgent() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les agents peuvent valider des apurements"));
            }

            Apurement apurement = apurementService.validateApurement(apurementId, utilisateur.getId());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "apurement", apurement,
                    "message", "Apurement validé avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("APUREMENT_VALIDATION_ERROR", "Erreur lors de la validation de l'apurement", e.getMessage()));
        }
    }

    @GetMapping("/declaration/{declarationId}")
    @PreAuthorize("hasAnyRole('APUREUR', 'RECEVEUR_DES_IMPOTS', 'TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR')")
    public ResponseEntity<?> getApurementByDeclarationId(@PathVariable UUID declarationId) {
        try {
            Apurement apurement = apurementService.getApurementByDeclarationId(declarationId);

            if (apurement == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("APUREMENT_NOT_FOUND", "Apurement non trouvé", "Aucun apurement trouvé pour cette déclaration"));
            }

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("apurement", apurement)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("APUREMENT_FETCH_ERROR", "Erreur lors de la récupération de l'apurement", e.getMessage()));
        }
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('APUREUR', 'RECEVEUR_DES_IMPOTS', 'TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR')")
    public ResponseEntity<?> getAllApurements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) StatutApurement statut,
            @RequestParam(required = false) TypeApurement type) {
        try {
            List<Apurement> apurements;
            if (statut != null) {
                apurements = apurementService.getApurementsByStatus(statut);
            } else if (type != null) {
                apurements = apurementService.getApurementsByType(type);
            } else {
                apurements = apurementService.getAllApurements();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("apurements", apurements);
            response.put("currentPage", page);
            response.put("totalItems", apurements.size());
            response.put("totalPages", (int) Math.ceil((double) apurements.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("APUREMENT_FETCH_ERROR", "Erreur lors de la récupération des apurements", e.getMessage()));
        }
    }
}
