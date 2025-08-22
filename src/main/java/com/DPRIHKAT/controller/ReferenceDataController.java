package com.DPRIHKAT.controller;

import com.DPRIHKAT.service.ReferenceDataService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ref")
public class ReferenceDataController {

    @Autowired
    private ReferenceDataService referenceDataService;

    // Communes
    @GetMapping("/communes")
    public ResponseEntity<?> listCommunes() {
        List<String> communes = referenceDataService.listCommunes();
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "communes", communes
        )));
    }

    @GetMapping("/communes/{commune}/quartiers")
    public ResponseEntity<?> listQuartiers(@PathVariable String commune) {
        List<String> quartiers = referenceDataService.listQuartiers(commune);
        if (quartiers == null) {
            return ResponseEntity.status(404).body(ResponseUtil.createErrorResponse(
                    "COMMUNE_NOT_FOUND",
                    "Commune introuvable",
                    "Aucune commune: " + commune
            ));
        }
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "commune", commune,
                "quartiers", quartiers
        )));
    }

    @GetMapping("/communes/{commune}/quartiers/{quartier}/avenues")
    public ResponseEntity<?> listAvenues(@PathVariable String commune, @PathVariable String quartier) {
        List<String> avenues = referenceDataService.listAvenues(commune, quartier);
        if (avenues == null) {
            // Could be commune or quartier not found; give generic 404
            return ResponseEntity.status(404).body(ResponseUtil.createErrorResponse(
                    "QUARTIER_NOT_FOUND",
                    "Quartier ou commune introuvable",
                    "Commune: " + commune + ", Quartier: " + quartier
            ));
        }
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "commune", commune,
                "quartier", quartier,
                "avenues", avenues
        )));
    }

    // Voitures
    @GetMapping("/voitures/marques")
    public ResponseEntity<?> listMarques() {
        List<String> marques = referenceDataService.listMarques();
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "marques", marques
        )));
    }

    @GetMapping("/voitures/marques/{marque}/models")
    public ResponseEntity<?> listModels(@PathVariable String marque) {
        List<String> models = referenceDataService.listModelsByMarque(marque);
        if (models == null) {
            return ResponseEntity.status(404).body(ResponseUtil.createErrorResponse(
                    "MARQUE_NOT_FOUND",
                    "Marque introuvable",
                    "Aucune marque: " + marque
            ));
        }
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "marque", marque,
                "models", models
        )));
    }
}
