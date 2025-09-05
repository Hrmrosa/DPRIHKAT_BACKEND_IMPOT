package com.DPRIHKAT.controller;

import com.DPRIHKAT.service.ReferenceDataService;
import com.DPRIHKAT.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ref")
public class ReferenceDataController {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceDataController.class);

    @Autowired
    private ReferenceDataService referenceDataService;

    // Communes
    @GetMapping("/communes")
    public ResponseEntity<?> listCommunes() {
        logger.info("Accès à la liste des communes");
        List<String> communes = referenceDataService.listCommunes();
        logger.debug("Nombre de communes trouvées: {}", communes.size());
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "communes", communes
        )));
    }

    @GetMapping("/communes/{commune}/quartiers")
    public ResponseEntity<?> listQuartiers(@PathVariable String commune) {
        logger.info("Accès aux quartiers de la commune: {}", commune);
        List<String> quartiers = referenceDataService.listQuartiers(commune);
        if (quartiers == null) {
            logger.warn("Commune non trouvée: {}", commune);
            return ResponseEntity.status(404).body(ResponseUtil.createErrorResponse(
                    "COMMUNE_NOT_FOUND",
                    "Commune introuvable",
                    "Aucune commune: " + commune
            ));
        }
        logger.debug("Nombre de quartiers trouvés pour {}: {}", commune, quartiers.size());
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "commune", commune,
                "quartiers", quartiers
        )));
    }

    @GetMapping("/communes/{commune}/quartiers/{quartier}/avenues")
    public ResponseEntity<?> listAvenues(@PathVariable String commune, @PathVariable String quartier) {
        logger.info("Accès aux avenues de la commune {} et quartier {}", commune, quartier);
        List<String> avenues = referenceDataService.listAvenues(commune, quartier);
        if (avenues == null) {
            logger.warn("Quartier {} non trouvé dans la commune {}", quartier, commune);
            return ResponseEntity.status(404).body(ResponseUtil.createErrorResponse(
                    "QUARTIER_NOT_FOUND",
                    "Quartier introuvable",
                    "Aucun quartier: " + quartier + " dans la commune: " + commune
            ));
        }
        logger.debug("Nombre d'avenues trouvées pour {} - {}: {}", commune, quartier, avenues.size());
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "commune", commune,
                "quartier", quartier,
                "avenues", avenues
        )));
    }

    // Voitures
    @GetMapping("/voitures/marques")
    public ResponseEntity<?> listMarques() {
        logger.info("Accès à la liste des marques de voitures");
        List<String> marques = referenceDataService.listMarques();
        logger.debug("Nombre de marques trouvées: {}", marques.size());
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "marques", marques
        )));
    }

    @GetMapping("/voitures/marques/{marque}/models")
    public ResponseEntity<?> listModels(@PathVariable String marque) {
        logger.info("Accès aux modèles de la marque {}", marque);
        List<String> models = referenceDataService.listModelsByMarque(marque);
        if (models == null) {
            logger.warn("Marque non trouvée: {}", marque);
            return ResponseEntity.status(404).body(ResponseUtil.createErrorResponse(
                    "MARQUE_NOT_FOUND",
                    "Marque introuvable",
                    "Aucune marque: " + marque
            ));
        }
        logger.debug("Nombre de modèles trouvés pour {}: {}", marque, models.size());
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "marque", marque,
                "models", models
        )));
    }
}
