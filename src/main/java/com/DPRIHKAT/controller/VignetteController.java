package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.Vignette;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.service.VignetteService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/vignettes")
public class VignetteController {

    @Autowired
    private VignetteService vignetteService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping("/generate/{vehiculeId}")
    @PreAuthorize("hasAnyRole('TAXATEUR','ADMIN')")
    public ResponseEntity<?> generateVignette(
            @PathVariable UUID vehiculeId,
            @RequestParam("dateExpirationMillis") long dateExpirationMillis,
            @RequestParam(name = "montant", required = false) Double montant,
            @RequestParam(name = "puissance", required = false) Double puissance,
            Authentication authentication) {
        try {
            // Get the authenticated user
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null || utilisateur.getAgent() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les agents peuvent générer des vignettes"));
            }

            Date dateExpiration = new Date(dateExpirationMillis);
            Vignette vignette = vignetteService.generateVignette(vehiculeId, utilisateur.getId(), dateExpiration, montant != null ? montant : 0d, puissance);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "vignette", vignette,
                    "message", "Vignette générée avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("VIGNETTE_GENERATION_ERROR", "Erreur lors de la génération de la vignette", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'CONTRIBUABLE','ADMIN')")
    public ResponseEntity<?> getVignetteById(@PathVariable UUID id) {
        try {
            Vignette vignette = vignetteService.findById(id);
            if (vignette == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("VIGNETTE_NOT_FOUND", "Vignette non trouvée", "Aucune vignette trouvée avec l'ID fourni"));
            }
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("vignette", vignette)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("VIGNETTE_FETCH_ERROR", "Erreur lors de la récupération de la vignette", e.getMessage()));
        }
    }

    @GetMapping("/vehicle/{vehiculeId}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'CONTRIBUABLE','ADMIN')")
    public ResponseEntity<?> getVignettesByVehicle(
            @PathVariable UUID vehiculeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Vignette> vignettes = vignetteService.getVignettesByVehicle(vehiculeId);

            Map<String, Object> response = new HashMap<>();
            response.put("vignettes", vignettes);
            response.put("currentPage", page);
            response.put("totalItems", vignettes.size());
            response.put("totalPages", (int) Math.ceil((double) vignettes.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("VIGNETTE_FETCH_ERROR", "Erreur lors de la récupération des vignettes", e.getMessage()));
        }
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR','ADMIN')")
    public ResponseEntity<?> getActiveVignettes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Vignette> vignettes = vignetteService.getActiveVignettes();

            Map<String, Object> response = new HashMap<>();
            response.put("vignettes", vignettes);
            response.put("currentPage", page);
            response.put("totalItems", vignettes.size());
            response.put("totalPages", (int) Math.ceil((double) vignettes.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("VIGNETTE_FETCH_ERROR", "Erreur lors de la récupération des vignettes", e.getMessage()));
        }
    }

    @GetMapping("/expired")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR','ADMIN')")
    public ResponseEntity<?> getExpiredVignettes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Vignette> vignettes = vignetteService.getExpiredVignettes();

            Map<String, Object> response = new HashMap<>();
            response.put("vignettes", vignettes);
            response.put("currentPage", page);
            response.put("totalItems", vignettes.size());
            response.put("totalPages", (int) Math.ceil((double) vignettes.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("VIGNETTE_FETCH_ERROR", "Erreur lors de la récupération des vignettes", e.getMessage()));
        }
    }
}
