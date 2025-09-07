package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.NatureImpot;
import com.DPRIHKAT.service.NatureImpotService;
import com.DPRIHKAT.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Contrôleur pour gérer les natures d'impôt
 */
@RestController
@RequestMapping("/api/natures-impot")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NatureImpotController {

    private static final Logger logger = LoggerFactory.getLogger(NatureImpotController.class);

    @Autowired
    private NatureImpotService natureImpotService;

    /**
     * Récupère toutes les natures d'impôt actives
     * @return la liste des natures d'impôt actives
     */
    @GetMapping
    public ResponseEntity<?> getAllNaturesImpot() {
        try {
            logger.info("Récupération de toutes les natures d'impôt actives");
            List<NatureImpot> naturesImpot = natureImpotService.getAllNaturesImpot();
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "naturesImpot", naturesImpot
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des natures d'impôt", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("NATURES_IMPOT_FETCH_ERROR", 
                            "Erreur lors de la récupération des natures d'impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère une nature d'impôt par son ID
     * @param id l'ID de la nature d'impôt
     * @return la nature d'impôt correspondante, si elle existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getNatureImpotById(@PathVariable String id) {
        try {
            logger.info("Récupération de la nature d'impôt avec l'ID: {}", id);
            Optional<NatureImpot> natureImpotOpt = natureImpotService.getNatureImpotById(id);
            if (natureImpotOpt.isPresent()) {
                return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                        "natureImpot", natureImpotOpt.get()
                )));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_NOT_FOUND", 
                                "Nature d'impôt non trouvée", 
                                "Aucune nature d'impôt avec l'ID: " + id));
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la nature d'impôt avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_FETCH_ERROR", 
                            "Erreur lors de la récupération de la nature d'impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère une nature d'impôt par son code
     * @param code le code de la nature d'impôt
     * @return la nature d'impôt correspondante, si elle existe
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<?> getNatureImpotByCode(@PathVariable String code) {
        try {
            logger.info("Récupération de la nature d'impôt avec le code: {}", code);
            Optional<NatureImpot> natureImpotOpt = natureImpotService.getNatureImpotByCode(code);
            if (natureImpotOpt.isPresent()) {
                return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                        "natureImpot", natureImpotOpt.get()
                )));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_NOT_FOUND", 
                                "Nature d'impôt non trouvée", 
                                "Aucune nature d'impôt avec le code: " + code));
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la nature d'impôt avec le code: {}", code, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_FETCH_ERROR", 
                            "Erreur lors de la récupération de la nature d'impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Crée une nouvelle nature d'impôt
     * @param natureImpot la nature d'impôt à créer
     * @return la nature d'impôt créée
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createNatureImpot(@RequestBody NatureImpot natureImpot) {
        try {
            logger.info("Création d'une nouvelle nature d'impôt: {}", natureImpot.getCode());
            if (natureImpotService.getNatureImpotByCode(natureImpot.getCode()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_ALREADY_EXISTS", 
                                "Nature d'impôt déjà existante", 
                                "Une nature d'impôt avec le code " + natureImpot.getCode() + " existe déjà"));
            }
            NatureImpot savedNatureImpot = natureImpotService.saveNatureImpot(natureImpot);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "natureImpot", savedNatureImpot,
                    "message", "Nature d'impôt créée avec succès"
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la nature d'impôt", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_CREATE_ERROR", 
                            "Erreur lors de la création de la nature d'impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Met à jour une nature d'impôt existante
     * @param id l'ID de la nature d'impôt à mettre à jour
     * @param natureImpot les nouvelles données de la nature d'impôt
     * @return la nature d'impôt mise à jour
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateNatureImpot(@PathVariable String id, @RequestBody NatureImpot natureImpot) {
        try {
            logger.info("Mise à jour de la nature d'impôt avec l'ID: {}", id);
            Optional<NatureImpot> natureImpotOpt = natureImpotService.getNatureImpotById(id);
            if (natureImpotOpt.isPresent()) {
                NatureImpot existingNatureImpot = natureImpotOpt.get();
                existingNatureImpot.setNom(natureImpot.getNom());
                existingNatureImpot.setDescription(natureImpot.getDescription());
                existingNatureImpot.setActif(natureImpot.isActif());
                NatureImpot updatedNatureImpot = natureImpotService.saveNatureImpot(existingNatureImpot);
                return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                        "natureImpot", updatedNatureImpot,
                        "message", "Nature d'impôt mise à jour avec succès"
                )));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_NOT_FOUND", 
                                "Nature d'impôt non trouvée", 
                                "Aucune nature d'impôt avec l'ID: " + id));
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la nature d'impôt avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_UPDATE_ERROR", 
                            "Erreur lors de la mise à jour de la nature d'impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Désactive une nature d'impôt (suppression logique)
     * @param id l'ID de la nature d'impôt à désactiver
     * @return un message de confirmation
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteNatureImpot(@PathVariable String id) {
        try {
            logger.info("Désactivation de la nature d'impôt avec l'ID: {}", id);
            boolean success = natureImpotService.desactiverNatureImpot(id);
            if (success) {
                return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                        "message", "Nature d'impôt désactivée avec succès"
                )));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_NOT_FOUND", 
                                "Nature d'impôt non trouvée", 
                                "Aucune nature d'impôt avec l'ID: " + id));
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la désactivation de la nature d'impôt avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("NATURE_IMPOT_DELETE_ERROR", 
                            "Erreur lors de la désactivation de la nature d'impôt", 
                            e.getMessage()));
        }
    }

    /**
     * Recharge les natures d'impôt depuis le fichier impots.json
     * @return la liste des natures d'impôt chargées
     */
    @PostMapping("/reload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> reloadNaturesImpot() {
        try {
            logger.info("Rechargement des natures d'impôt depuis le fichier impots.json");
            List<NatureImpot> naturesImpot = natureImpotService.chargerNaturesImpotDepuisFichier();
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "naturesImpot", naturesImpot,
                    "message", "Natures d'impôt rechargées avec succès"
            )));
        } catch (Exception e) {
            logger.error("Erreur lors du rechargement des natures d'impôt", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("NATURES_IMPOT_RELOAD_ERROR", 
                            "Erreur lors du rechargement des natures d'impôt", 
                            e.getMessage()));
        }
    }
}
