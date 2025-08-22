package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Poursuite;
import com.DPRIHKAT.service.PoursuiteService;
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
@RequestMapping("/api/poursuites")
public class PoursuiteController {

    @Autowired
    private PoursuiteService poursuiteService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR')")
    public ResponseEntity<?> getAllPoursuites() {
        try {
            List<Poursuite> poursuites = poursuiteService.findAll();
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("poursuites", poursuites)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("POURSUITES_FETCH_ERROR", "Erreur lors de la récupération des poursuites", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR', 'CHEF_DE_BUREAU')")
    public ResponseEntity<?> getPoursuiteById(@PathVariable UUID id) {
        try {
            Poursuite poursuite = poursuiteService.findById(id);
            if (poursuite == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("POURSUITE_NOT_FOUND", "Poursuite non trouvée", "Aucune poursuite trouvée avec l'ID fourni"));
            }
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("poursuite", poursuite)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("POURSUITE_FETCH_ERROR", "Erreur lors de la récupération de la poursuite", e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR')")
    public ResponseEntity<?> createPoursuite(@RequestBody Poursuite poursuite) {
        try {
            Poursuite createdPoursuite = poursuiteService.save(poursuite);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("poursuite", createdPoursuite)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("POURSUITE_CREATE_ERROR", "Erreur lors de la création de la poursuite", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR')")
    public ResponseEntity<?> updatePoursuite(@PathVariable UUID id, @RequestBody Poursuite poursuite) {
        try {
            Poursuite updatedPoursuite = poursuiteService.update(id, poursuite);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("poursuite", updatedPoursuite)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("POURSUITE_UPDATE_ERROR", "Erreur lors de la mise à jour de la poursuite", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> deletePoursuite(@PathVariable UUID id) {
        try {
            poursuiteService.deleteById(id);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("message", "Poursuite supprimée avec succès")));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("POURSUITE_DELETE_ERROR", "Erreur lors de la suppression de la poursuite", e.getMessage()));
        }
    }
}

