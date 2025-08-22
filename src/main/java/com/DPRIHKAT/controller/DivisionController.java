package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Division;
import com.DPRIHKAT.service.DivisionService;
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
@RequestMapping("/api/divisions")
public class DivisionController {

    @Autowired
    private DivisionService divisionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> getAllDivisions() {
        try {
            List<Division> divisions = divisionService.findAll();
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("divisions", divisions)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DIVISIONS_FETCH_ERROR", "Erreur lors de la récupération des divisions", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION')")
    public ResponseEntity<?> getDivisionById(@PathVariable UUID id) {
        try {
            Division division = divisionService.findById(id);
            if (division == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("DIVISION_NOT_FOUND", "Division non trouvée", "Aucune division trouvée avec l'ID fourni"));
            }
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("division", division)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DIVISION_FETCH_ERROR", "Erreur lors de la récupération de la division", e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> createDivision(@RequestBody Division division) {
        try {
            Division createdDivision = divisionService.save(division);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "division", createdDivision,
                    "message", "Division créée avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DIVISION_CREATE_ERROR", "Erreur lors de la création de la division", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> updateDivision(@PathVariable UUID id, @RequestBody Division division) {
        try {
            Division updatedDivision = divisionService.update(id, division);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "division", updatedDivision,
                    "message", "Division mise à jour avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DIVISION_UPDATE_ERROR", "Erreur lors de la mise à jour de la division", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> deleteDivision(@PathVariable UUID id) {
        try {
            divisionService.deleteById(id);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("message", "Division supprimée avec succès")));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DIVISION_DELETE_ERROR", "Erreur lors de la suppression de la division", e.getMessage()));
        }
    }
}
