package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Division;
import com.DPRIHKAT.service.DivisionService;
import com.DPRIHKAT.util.ResponseUtil;
import com.DPRIHKAT.dto.BureauSimpleDTO;
import com.DPRIHKAT.dto.DivisionResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/divisions")
public class DivisionController {

    @Autowired
    private DivisionService divisionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAllDivisions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Division> divisionPage = divisionService.findAll(pageable);

            List<DivisionResponseDTO> content = divisionPage.getContent().stream()
                    .map(this::mapToDivisionResponseDTO)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("divisions", content);
            response.put("currentPage", divisionPage.getNumber());
            response.put("totalItems", divisionPage.getTotalElements());
            response.put("totalPages", divisionPage.getTotalPages());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DIVISIONS_FETCH_ERROR", "Erreur lors de la récupération des divisions", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION')")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getDivisionById(@PathVariable UUID id) {
        try {
            Division division = divisionService.findById(id);
            if (division == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("DIVISION_NOT_FOUND", "Division non trouvée", "Aucune division trouvée avec l'ID fourni"));
            }
            DivisionResponseDTO dto = mapToDivisionResponseDTO(division);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("division", dto)));
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
            DivisionResponseDTO dto = mapToDivisionResponseDTO(createdDivision);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "division", dto,
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
            DivisionResponseDTO dto = mapToDivisionResponseDTO(updatedDivision);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "division", dto,
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

    private DivisionResponseDTO mapToDivisionResponseDTO(Division division) {
        if (division == null) return null;

        List<BureauSimpleDTO> bureaux = division.getBureaux() == null ? List.of() : division.getBureaux().stream()
                .map(b -> new BureauSimpleDTO(b.getId(), b.getNom(), b.getCode()))
                .collect(Collectors.toList());

        return new DivisionResponseDTO(
                division.getId(),
                division.getNom(),
                division.getCode(),
                bureaux
        );
    }
}
