package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Relance;
import com.DPRIHKAT.service.RelanceService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/relances")
public class RelanceController {

    @Autowired
    private RelanceService relanceService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR')")
    public ResponseEntity<?> getAllRelances() {
        try {
            List<Relance> relances = relanceService.findAll();
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("relances", relances)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("RELANCES_FETCH_ERROR", "Erreur lors de la récupération des relances", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR', 'CHEF_DE_BUREAU')")
    public ResponseEntity<?> getRelanceById(@PathVariable UUID id) {
        try {
            Relance relance = relanceService.findById(id);
            if (relance == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("RELANCE_NOT_FOUND", "Relance non trouvée", "Aucune relance trouvée avec l'ID fourni"));
            }
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("relance", relance)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("RELANCE_FETCH_ERROR", "Erreur lors de la récupération de la relance", e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR')")
    public ResponseEntity<?> createRelance(@RequestBody Relance relance) {
        try {
            Relance createdRelance = relanceService.save(relance);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("relance", createdRelance)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("RELANCE_CREATE_ERROR", "Erreur lors de la création de la relance", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR')")
    public ResponseEntity<?> updateRelance(@PathVariable UUID id, @RequestBody Relance relance) {
        try {
            Relance updatedRelance = relanceService.update(id, relance);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("relance", updatedRelance)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("RELANCE_UPDATE_ERROR", "Erreur lors de la mise à jour de la relance", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> deleteRelance(@PathVariable UUID id) {
        try {
            relanceService.deleteById(id);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("message", "Relance supprimée avec succès")));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("RELANCE_DELETE_ERROR", "Erreur lors de la suppression de la relance", e.getMessage()));
        }
    }
}
