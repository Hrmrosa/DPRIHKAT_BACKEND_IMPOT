package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Penalite;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.service.PenaliteService;
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
@RequestMapping("/api/penalites")
public class PenaliteController {

    @Autowired
    private PenaliteService penaliteService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping("/calculer/{declarationId}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS','ADMIN')")
    public ResponseEntity<?> calculatePenalties(@PathVariable UUID declarationId) {
        try {
            penaliteService.calculateAndApplyPenalties(declarationId);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Pénalités calculées et appliquées avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PENALTY_CALCULATION_ERROR", "Erreur lors du calcul des pénalités", e.getMessage()));
        }
    }

    @PostMapping("/ajuster/{penaltyId}")
    @PreAuthorize("hasAnyRole('CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR','ADMIN')")
    public ResponseEntity<?> adjustPenalty(
            @PathVariable UUID penaltyId,
            @RequestParam double newAmount,
            @RequestParam String justification) {
        try {
            Penalite penalite = penaliteService.adjustPenalty(penaltyId, newAmount, justification);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "penalite", penalite,
                    "message", "Pénalité ajustée avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PENALTY_ADJUSTMENT_ERROR", "Erreur lors de l'ajustement de la pénalité", e.getMessage()));
        }
    }

    @GetMapping("/declaration/{declarationId}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'CONTRIBUABLE','ADMIN')")
    public ResponseEntity<?> getPenaltiesForDeclaration(
            @PathVariable UUID declarationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Penalite> penalties = penaliteService.getPenaltiesForDeclaration(declarationId);

            Map<String, Object> response = new HashMap<>();
            response.put("penalties", penalties);
            response.put("currentPage", page);
            response.put("totalItems", penalties.size());
            response.put("totalPages", (int) Math.ceil((double) penalties.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PENALTY_FETCH_ERROR", "Erreur lors de la récupération des pénalités", e.getMessage()));
        }
    }

    @GetMapping("/contribuable")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'CONTRIBUABLE','ADMIN')")
    public ResponseEntity<?> getPenaltiesForContribuable(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // Get the authenticated user
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null || utilisateur.getContribuable() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les contribuables peuvent consulter leurs pénalités"));
            }

            List<Penalite> penalties = penaliteService.getPenaltiesForContribuable(utilisateur.getContribuable().getId());

            Map<String, Object> response = new HashMap<>();
            response.put("penalties", penalties);
            response.put("currentPage", page);
            response.put("totalItems", penalties.size());
            response.put("totalPages", (int) Math.ceil((double) penalties.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PENALTY_FETCH_ERROR", "Erreur lors de la récupération des pénalités", e.getMessage()));
        }
    }
}
