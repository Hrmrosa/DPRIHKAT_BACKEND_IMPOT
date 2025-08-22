package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Bureau;
import com.DPRIHKAT.service.BureauService;
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
@RequestMapping("/api/bureaux")
public class BureauController {

    @Autowired
    private BureauService bureauService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> getAllBureaux() {
        try {
            List<Bureau> bureaux = bureauService.findAll();
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("bureaux", bureaux)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("BUREAUX_FETCH_ERROR", "Erreur lors de la récupération des bureaux", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION')")
    public ResponseEntity<?> getBureauById(@PathVariable UUID id) {
        try {
            Bureau bureau = bureauService.findById(id);
            if (bureau == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("BUREAU_NOT_FOUND", "Bureau non trouvé", "Aucun bureau trouvé avec l'ID fourni"));
            }
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("bureau", bureau)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("BUREAU_FETCH_ERROR", "Erreur lors de la récupération du bureau", e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> createBureau(@RequestBody Bureau bureau) {
        try {
            Bureau createdBureau = bureauService.save(bureau);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("bureau", createdBureau)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("BUREAU_CREATE_ERROR", "Erreur lors de la création du bureau", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> updateBureau(@PathVariable UUID id, @RequestBody Bureau bureau) {
        try {
            Bureau updatedBureau = bureauService.update(id, bureau);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("bureau", updatedBureau)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("BUREAU_UPDATE_ERROR", "Erreur lors de la mise à jour du bureau", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> deleteBureau(@PathVariable UUID id) {
        try {
            bureauService.deleteById(id);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("message", "Bureau supprimé avec succès")));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("BUREAU_DELETE_ERROR", "Erreur lors de la suppression du bureau", e.getMessage()));
        }
    }
}
