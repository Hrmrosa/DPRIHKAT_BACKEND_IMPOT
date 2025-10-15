package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.service.CollecteService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CollecteController {

    @Autowired
    private ProprieteRepository proprieteRepository;

    @Autowired
    private CollecteService collecteService;

    /**
     * Récupère toutes les propriétés avec leurs déclarations et paiements associés
     * 
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @return liste paginée des propriétés avec informations de déclaration et paiement
     */
    @GetMapping("/collectes")
    @PreAuthorize("hasAnyRole('ROLE_CONTROLLEUR', 'ROLE_ADMIN', 'ROLE_INFORMATICIEN')")
    public ResponseEntity<?> getAllCollectes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Propriete> pageProprietes = proprieteRepository.findAll(pageable);
            
            // Enrichir les propriétés avec les informations de déclaration et paiement
            List<Map<String, Object>> proprietesEnrichies = 
                collecteService.enrichirProprietesAvecDeclarations(pageProprietes.getContent());
            
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", pageProprietes.getTotalElements());
            data.put("totalPages", pageProprietes.getTotalPages());
            data.put("currentPage", pageProprietes.getNumber());
            data.put("proprietes", proprietesEnrichies);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.createErrorResponse(
                    "COLLECTE_FETCH_ERROR", "Erreur lors de la récupération des collectes", e.getMessage()));
        }
    }
    
    /**
     * Récupère toutes les propriétés d'un contribuable spécifique avec leurs déclarations et paiements
     * 
     * @param contribuableId ID du contribuable
     * @return liste des propriétés du contribuable avec informations de déclaration et paiement
     */
    @GetMapping("/collectes/contribuable/{contribuableId}")
    @PreAuthorize("hasAnyRole('ROLE_CONTROLLEUR', 'ROLE_ADMIN', 'ROLE_INFORMATICIEN', 'ROLE_CONTRIBUABLE')")
    public ResponseEntity<?> getCollectesByContribuable(@PathVariable UUID contribuableId) {
        try {
            // Récupérer et enrichir les propriétés du contribuable
            List<Map<String, Object>> proprietesEnrichies = 
                collecteService.getProprietesEnrichiesParContribuable(contribuableId);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "proprietes", proprietesEnrichies
            )));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.createErrorResponse(
                    "COLLECTE_CONTRIBUABLE_FETCH_ERROR", 
                    "Erreur lors de la récupération des collectes du contribuable", 
                    e.getMessage()));
        }
    }
}
