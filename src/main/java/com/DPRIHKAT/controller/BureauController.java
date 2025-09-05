package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Bureau;
import com.DPRIHKAT.entity.Division;
import com.DPRIHKAT.service.BureauService;
import com.DPRIHKAT.service.DivisionService;
import com.DPRIHKAT.util.ResponseUtil;
import com.DPRIHKAT.dto.AgentSimpleDTO;
import com.DPRIHKAT.dto.BureauRequestDTO;
import com.DPRIHKAT.dto.BureauResponseDTO;
import com.DPRIHKAT.dto.DivisionSimpleDTO;
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
@RequestMapping("/api/bureaux")
public class BureauController {

    @Autowired
    private BureauService bureauService;
    
    @Autowired
    private DivisionService divisionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAllBureaux(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Bureau> bureauPage = bureauService.findAll(pageable);

            List<BureauResponseDTO> content = bureauPage.getContent().stream()
                    .map(this::mapToBureauResponseDTO)
                    .collect(Collectors.toList());
            Map<String, Object> response = new HashMap<>();
            response.put("bureaux", content);
            response.put("currentPage", bureauPage.getNumber());
            response.put("totalItems", bureauPage.getTotalElements());
            response.put("totalPages", bureauPage.getTotalPages());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("BUREAUX_FETCH_ERROR", "Erreur lors de la récupération des bureaux", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION')")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getBureauById(@PathVariable UUID id) {
        try {
            Bureau bureau = bureauService.findById(id);
            if (bureau == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("BUREAU_NOT_FOUND", "Bureau non trouvé", "Aucun bureau trouvé avec l'ID fourni"));
            }
            BureauResponseDTO dto = mapToBureauResponseDTO(bureau);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("bureau", dto)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("BUREAU_FETCH_ERROR", "Erreur lors de la récupération du bureau", e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> createBureau(@RequestBody BureauRequestDTO bureauDTO) {
        try {
            // Récupérer la division à partir de l'ID
            Division division = null;
            if (bureauDTO.getDivisionId() != null) {
                division = divisionService.findById(bureauDTO.getDivisionId());
                if (division == null) {
                    return ResponseEntity
                            .badRequest()
                            .body(ResponseUtil.createErrorResponse("DIVISION_NOT_FOUND", "Division non trouvée", "Aucune division trouvée avec l'ID fourni"));
                }
            }
            
            // Créer le bureau avec la division
            Bureau bureau = new Bureau();
            bureau.setNom(bureauDTO.getNom());
            bureau.setCode(bureauDTO.getCode());
            bureau.setDivision(division);
            
            Bureau createdBureau = bureauService.save(bureau);
            BureauResponseDTO dto = mapToBureauResponseDTO(createdBureau);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("bureau", dto)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("BUREAU_CREATE_ERROR", "Erreur lors de la création du bureau", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> updateBureau(@PathVariable UUID id, @RequestBody BureauRequestDTO bureauDTO) {
        try {
            // Vérifier si le bureau existe
            Bureau existingBureau = bureauService.findById(id);
            if (existingBureau == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("BUREAU_NOT_FOUND", "Bureau non trouvé", "Aucun bureau trouvé avec l'ID fourni"));
            }
            
            // Récupérer la division à partir de l'ID
            Division division = null;
            if (bureauDTO.getDivisionId() != null) {
                division = divisionService.findById(bureauDTO.getDivisionId());
                if (division == null) {
                    return ResponseEntity
                            .badRequest()
                            .body(ResponseUtil.createErrorResponse("DIVISION_NOT_FOUND", "Division non trouvée", "Aucune division trouvée avec l'ID fourni"));
                }
            }
            
            // Mettre à jour le bureau
            existingBureau.setNom(bureauDTO.getNom());
            existingBureau.setCode(bureauDTO.getCode());
            existingBureau.setDivision(division);
            
            Bureau updatedBureau = bureauService.update(id, existingBureau);
            BureauResponseDTO dto = mapToBureauResponseDTO(updatedBureau);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("bureau", dto)));
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

    private BureauResponseDTO mapToBureauResponseDTO(Bureau bureau) {
        if (bureau == null) return null;
        DivisionSimpleDTO divisionDTO = null;
        if (bureau.getDivision() != null) {
            divisionDTO = new DivisionSimpleDTO(
                    bureau.getDivision().getId(),
                    bureau.getDivision().getNom(),
                    bureau.getDivision().getCode()
            );
        }

        List<AgentSimpleDTO> agents = bureau.getAgents() == null ? List.of() : bureau.getAgents().stream()
                .map(a -> new AgentSimpleDTO(a.getId(), a.getNom()))
                .collect(Collectors.toList());

        return new BureauResponseDTO(
                bureau.getId(),
                bureau.getNom(),
                bureau.getCode(),
                divisionDTO,
                agents
        );
    }
}
