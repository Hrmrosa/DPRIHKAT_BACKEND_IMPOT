package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.service.ContribuableService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/contribuables")
public class ContribuableController {

    @Autowired
    private ContribuableService contribuableService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR', 'CHEF_DE_BUREAU')")
    public ResponseEntity<?> getAllContribuables(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Contribuable> pageContribuables = contribuableService.findAllPaginated(pageable);
            
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", pageContribuables.getTotalElements());
            data.put("totalPages", pageContribuables.getTotalPages());
            data.put("currentPage", pageContribuables.getNumber());
            data.put("contribuables", pageContribuables.getContent());
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLES_FETCH_ERROR", "Erreur lors de la récupération des contribuables", e.getMessage()));
        }
    }

    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR', 'CHEF_DE_BUREAU')")
    public ResponseEntity<?> searchContribuables(
            @RequestParam String nom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Contribuable> contribuables = contribuableService.searchByNamePaginated(nom, pageable);
            
            return ResponseEntity.ok(ResponseUtil.createPaginatedResponse(contribuables, "contribuables"));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLES_SEARCH_ERROR", "Erreur lors de la recherche des contribuables", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR', 'CHEF_DE_BUREAU', 'CONTRIBUABLE')")
    public ResponseEntity<?> getContribuableById(@PathVariable UUID id) {
        try {
            Contribuable contribuable = contribuableService.findById(id);
            if (contribuable == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_NOT_FOUND", "Contribuable non trouvé", "Aucun contribuable trouvé avec l'ID fourni"));
            }
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("contribuable", contribuable)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_FETCH_ERROR", "Erreur lors de la récupération du contribuable", e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> createContribuable(@RequestBody Contribuable contribuable) {
        try {
            Contribuable createdContribuable = contribuableService.createContribuable(contribuable);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("contribuable", createdContribuable)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_CREATE_ERROR", "Erreur lors de la création du contribuable", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTRIBUABLE')")
    public ResponseEntity<?> updateContribuable(@PathVariable UUID id, @RequestBody Contribuable contribuable) {
        try {
            Contribuable updatedContribuable = contribuableService.update(id, contribuable);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("contribuable", updatedContribuable)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_UPDATE_ERROR", "Erreur lors de la mise à jour du contribuable", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> deleteContribuable(@PathVariable UUID id) {
        try {
            contribuableService.deleteById(id);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("message", "Contribuable supprimé avec succès")));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_DELETE_ERROR", "Erreur lors de la suppression du contribuable", e.getMessage()));
        }
    }
}
