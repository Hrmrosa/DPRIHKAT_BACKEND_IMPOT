package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.service.ControleFiscalService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/controle-fiscal")
public class ControleFiscalController {

    @Autowired
    private ControleFiscalService controleFiscalService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping("/anomalies")
    @PreAuthorize("hasAnyRole('CONTROLLEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR')")
    public ResponseEntity<?> findAnomalies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        try {
            // Get the authenticated user
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Utilisateur non trouvé"));
            }

            List<Declaration> anomalies = controleFiscalService.findAnomalies(utilisateur.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("anomalies", anomalies);
            response.put("currentPage", page);
            response.put("totalItems", anomalies.size());
            response.put("totalPages", (int) Math.ceil((double) anomalies.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("ANOMALY_FETCH_ERROR", "Erreur lors de la récupération des anomalies", e.getMessage()));
        }
    }

    @GetMapping("/rapport")
    @PreAuthorize("hasAnyRole('CONTROLLEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR')")
    public ResponseEntity<?> generateFiscalReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            Authentication authentication) {
        try {
            // Get the authenticated user
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Utilisateur non trouvé"));
            }

            Map<String, Object> report = controleFiscalService.generateFiscalReport(utilisateur.getId(), startDate, endDate);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "rapport", report,
                    "message", "Rapport fiscal généré avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("REPORT_GENERATION_ERROR", "Erreur lors de la génération du rapport fiscal", e.getMessage()));
        }
    }

    @GetMapping("/top-contributors")
    @PreAuthorize("hasAnyRole('CONTROLLEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR')")
    public ResponseEntity<?> getTopContributors(
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication) {
        try {
            // Get the authenticated user
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Utilisateur non trouvé"));
            }

            List<Map<String, Object>> topContributors = controleFiscalService.getTopContributors(utilisateur.getId(), limit);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "topContributors", topContributors,
                    "message", "Top contributeurs récupérés avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TOP_CONTRIBUTORS_ERROR", "Erreur lors de la récupération des top contributeurs", e.getMessage()));
        }
    }

    @GetMapping("/delinquents")
    @PreAuthorize("hasAnyRole('CONTROLLEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR')")
    public ResponseEntity<?> getDelinquentContributors(Authentication authentication) {
        try {
            // Get the authenticated user
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Utilisateur non trouvé"));
            }

            List<Map<String, Object>> delinquents = controleFiscalService.getDelinquentContributors(utilisateur.getId());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "delinquents", delinquents,
                    "message", "Contributeurs délinquants récupérés avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DELINQUENTS_ERROR", "Erreur lors de la récupération des contributeurs délinquants", e.getMessage()));
        }
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('CONTROLLEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN', 'INFORMATICIEN')")
    public ResponseEntity<?> getDashboardStats(Authentication authentication) {
        try {
            // Get the authenticated user
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Utilisateur non trouvé"));
            }

            Map<String, Object> stats = controleFiscalService.getDashboardStats(utilisateur.getId());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "dashboard", stats,
                    "message", "Statistiques du tableau de bord récupérées avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DASHBOARD_ERROR", "Erreur lors de la récupération des statistiques du tableau de bord", e.getMessage()));
        }
    }
}
