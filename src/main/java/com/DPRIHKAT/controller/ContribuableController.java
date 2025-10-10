package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.service.ContribuableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/contribuables")
public class ContribuableController {

    private final ContribuableService contribuableService;

    public ContribuableController(ContribuableService contribuableService) {
        this.contribuableService = contribuableService;
    }

    // 1. Récupérer tous les contribuables
    @GetMapping
    public ResponseEntity<?> getAllContribuables() {
        try {
            List<Contribuable> contribuables = contribuableService.getAllContribuables();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", Map.of("contribuables", contribuables)
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", Map.of(
                                    "code", "CONTRIBUABLES_FETCH_ERROR",
                                    "message", "Erreur lors de la récupération des contribuables",
                                    "details", e.getMessage()
                            )
                    ));
        }
    }

    // 2. Récupérer un contribuable par ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getContribuableById(@PathVariable UUID id) {
        try {
            Contribuable contribuable = contribuableService.getContribuableById(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", Map.of("contribuable", contribuable)
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", Map.of(
                                    "code", "CONTRIBUABLE_NOT_FOUND",
                                    "message", "Contribuable non trouvé",
                                    "details", e.getMessage()
                            )
                    ));
        }
    }

    // 3. Créer un nouveau contribuable
    @PostMapping
    public ResponseEntity<?> createContribuable(@RequestBody Contribuable contribuable) {
        try {
            Contribuable created = contribuableService.createContribuable(contribuable);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", Map.of("contribuable", created)
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", Map.of(
                                    "code", "CONTRIBUABLE_CREATE_ERROR",
                                    "message", "Erreur lors de la création du contribuable",
                                    "details", e.getMessage()
                            )
                    ));
        }
    }

    // 4. Mettre à jour un contribuable
    @PutMapping("/{id}")
    public ResponseEntity<?> updateContribuable(@PathVariable UUID id, @RequestBody Contribuable contribuable) {
        try {
            Contribuable updated = contribuableService.updateContribuable(id, contribuable);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", Map.of("contribuable", updated)
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", Map.of(
                                    "code", "CONTRIBUABLE_UPDATE_ERROR",
                                    "message", "Erreur lors de la mise à jour du contribuable",
                                    "details", e.getMessage()
                            )
                    ));
        }
    }

    // 5. Récupérer les détails complets d'un contribuable
    @GetMapping("/{id}/details")
    public ResponseEntity<?> getContribuableDetails(@PathVariable UUID id) {
        try {
            Map<String, Object> details = contribuableService.getContribuableDetails(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", details
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", Map.of(
                                    "code", "CONTRIBUABLE_DETAILS_ERROR",
                                    "message", "Erreur lors de la récupération des détails",
                                    "details", e.getMessage()
                            )
                    ));
        }
    }

    // 6. Désactiver un contribuable (suppression logique)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateContribuable(@PathVariable UUID id) {
        try {
            Contribuable deactivated = contribuableService.deactivateContribuable(id);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", Map.of(
                                    "message", "Contribuable désactivé avec succès",
                                    "contribuable", deactivated
                            )
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", Map.of(
                                    "code", "CONTRIBUABLE_DEACTIVATION_ERROR",
                                    "message", "Erreur lors de la désactivation du contribuable",
                                    "details", e.getMessage()
                            )
                    ));
        }
    }
}
