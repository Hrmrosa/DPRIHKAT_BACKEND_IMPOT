package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.DossierRecouvrement;
import com.DPRIHKAT.service.DossierRecouvrementService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/dossiers-recouvrement")
public class DossierRecouvrementController {

    @Autowired
    private DossierRecouvrementService dossierRecouvrementService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR')")
    public ResponseEntity<?> getAllDossiers() {
        try {
            List<DossierRecouvrement> dossiers = dossierRecouvrementService.findAll();
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("dossiers", dossiers)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DOSSIERS_FETCH_ERROR", "Erreur lors de la récupération des dossiers de recouvrement", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR', 'CHEF_DE_BUREAU')")
    public ResponseEntity<?> getDossierById(@PathVariable UUID id) {
        try {
            DossierRecouvrement dossier = dossierRecouvrementService.findByIdWithDetails(id);
            if (dossier == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("DOSSIER_NOT_FOUND", "Dossier non trouvé", "Aucun dossier trouvé avec l'ID fourni"));
            }
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("dossier", Map.of(
                "id", dossier.getId(),
                "dateOuverture", dossier.getDateOuverture(),
                "totalDette", dossier.getTotalDette(),
                "totalRecouvre", dossier.getTotalRecouvre()
            ));
            responseData.put("contribuable", dossier.getContribuable());
            responseData.put("declarations", dossier.getDeclarations());
            responseData.put("paiements", dossier.getPaiements());
            responseData.put("historique", dossier.getHistorique());
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseData));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DOSSIER_FETCH_ERROR", "Erreur lors de la récupération du dossier", e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR')")
    public ResponseEntity<?> createDossier(@RequestBody DossierRecouvrement dossier) {
        try {
            DossierRecouvrement createdDossier = dossierRecouvrementService.save(dossier);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("dossier", createdDossier)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DOSSIER_CREATE_ERROR", "Erreur lors de la création du dossier", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR')")
    public ResponseEntity<?> updateDossier(@PathVariable UUID id, @RequestBody DossierRecouvrement dossier) {
        try {
            DossierRecouvrement updatedDossier = dossierRecouvrementService.update(id, dossier);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("dossier", updatedDossier)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DOSSIER_UPDATE_ERROR", "Erreur lors de la mise à jour du dossier", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> deleteDossier(@PathVariable UUID id) {
        try {
            dossierRecouvrementService.deleteById(id);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("message", "Dossier supprimé avec succès")));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DOSSIER_DELETE_ERROR", "Erreur lors de la suppression du dossier", e.getMessage()));
        }
    }
}
