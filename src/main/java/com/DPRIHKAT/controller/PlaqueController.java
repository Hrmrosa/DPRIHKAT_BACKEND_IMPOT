package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Plaque;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.service.PlaqueService;
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
@RequestMapping("/api/plaques")
public class PlaqueController {

    @Autowired
    private PlaqueService plaqueService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping("/assign/{vehiculeId}")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES')")
    public ResponseEntity<?> assignPlaqueToVehicle(
            @PathVariable UUID vehiculeId,
            Authentication authentication) {
        try {
            // Get the authenticated user
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null || utilisateur.getAgent() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les agents peuvent assigner des plaques"));
            }

            Plaque plaque = plaqueService.assignPlaqueToVehicle(vehiculeId, utilisateur.getId());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "plaque", plaque,
                    "message", "Plaque assignée avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_ASSIGNMENT_ERROR", "Erreur lors de l'assignation de la plaque", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR')")
    public ResponseEntity<?> getPlaqueById(@PathVariable UUID id) {
        try {
            Plaque plaque = plaqueService.getPlaqueById(id);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("plaque", plaque)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_FETCH_ERROR", "Erreur lors de la récupération de la plaque", e.getMessage()));
        }
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR')")
    public ResponseEntity<?> getAllPlaques(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean disponible) {
        try {
            List<Plaque> plaques;
            if (disponible != null) {
                if (disponible) {
                    plaques = plaqueService.getAvailablePlaques();
                } else {
                    plaques = plaqueService.getAssignedPlaques();
                }
            } else {
                plaques = plaqueService.getAllPlaques();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("plaques", plaques);
            response.put("currentPage", page);
            response.put("totalItems", plaques.size());
            response.put("totalPages", (int) Math.ceil((double) plaques.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_FETCH_ERROR", "Erreur lors de la récupération des plaques", e.getMessage()));
        }
    }

    @GetMapping("/vehicule/{vehiculeId}")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'CONTRIBUABLE')")
    public ResponseEntity<?> getPlaquesByVehicle(
            @PathVariable UUID vehiculeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Plaque> plaques = plaqueService.getPlaquesByVehicle(vehiculeId);

            Map<String, Object> response = new HashMap<>();
            response.put("plaques", plaques);
            response.put("currentPage", page);
            response.put("totalItems", plaques.size());
            response.put("totalPages", (int) Math.ceil((double) plaques.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_FETCH_ERROR", "Erreur lors de la récupération des plaques", e.getMessage()));
        }
    }

    @GetMapping("/stock")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR')")
    public ResponseEntity<?> checkPlaqueStock() {
        try {
            boolean available = plaqueService.isPlaqueAvailable();

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "available", available,
                    "message", available ? "Plaques disponibles en stock" : "Aucune plaque disponible en stock"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("STOCK_CHECK_ERROR", "Erreur lors de la vérification du stock", e.getMessage()));
        }
    }
}
