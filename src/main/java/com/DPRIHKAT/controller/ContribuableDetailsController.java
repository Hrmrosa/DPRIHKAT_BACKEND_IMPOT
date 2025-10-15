package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.repository.ContribuableRepository;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import com.DPRIHKAT.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contrôleur pour gérer les détails des contribuables
 */
@RestController
@RequestMapping("/api/contribuables-details")
public class ContribuableDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(ContribuableDetailsController.class);
    private final ContribuableRepository contribuableRepository;
    private final ProprieteRepository proprieteRepository;
    private final VehiculeRepository vehiculeRepository;

    public ContribuableDetailsController(ContribuableRepository contribuableRepository,
                                     ProprieteRepository proprieteRepository,
                                     VehiculeRepository vehiculeRepository) {
        this.contribuableRepository = contribuableRepository;
        this.proprieteRepository = proprieteRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    /**
     * Récupère les détails complets d'un contribuable (avec propriétés et véhicules)
     * @param id ID du contribuable
     * @return Détails complets du contribuable
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR')")
    public ResponseEntity<?> getContribuableDetails(@PathVariable UUID id) {
        try {
            logger.info("Récupération des détails complets du contribuable avec ID: {}", id);
            
            // Récupérer le contribuable
            Optional<Contribuable> contribuableOpt = contribuableRepository.findById(id);
            if (contribuableOpt.isEmpty()) {
                return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_NOT_FOUND", 
                            "Contribuable non trouvé", 
                            "Aucun contribuable trouvé avec l'ID: " + id));
            }
            
            Contribuable contribuable = contribuableOpt.get();
            
            // Récupérer les propriétés
            List<Propriete> proprietes = proprieteRepository.findByProprietaire_Id(id);
            List<Map<String, Object>> proprietesSimplifiees = proprietes.stream()
                .map(p -> {
                    Map<String, Object> proprieteMap = new HashMap<>();
                    proprieteMap.put("id", p.getId());
                    proprieteMap.put("adresse", p.getAdresse());
                    proprieteMap.put("valeur", p.getMontantImpot() != null ? p.getMontantImpot() : 0.0);
                    return proprieteMap;
                })
                .collect(Collectors.toList());
            
            // Récupérer les véhicules
            List<Vehicule> vehiculesProprietaire = vehiculeRepository.findByProprietaireId(id);
            List<Vehicule> vehiculesContribuable = vehiculeRepository.findByContribuableId(id);
            
            // Fusionner les deux listes sans doublons
            Set<Vehicule> allVehicules = new HashSet<>();
            allVehicules.addAll(vehiculesProprietaire);
            allVehicules.addAll(vehiculesContribuable);
            
            List<Map<String, Object>> vehiculesSimplifies = allVehicules.stream()
                .map(v -> {
                    Map<String, Object> vehiculeMap = new HashMap<>();
                    vehiculeMap.put("id", v.getId());
                    vehiculeMap.put("immatriculation", v.getImmatriculation());
                    vehiculeMap.put("marque", v.getMarque());
                    vehiculeMap.put("modele", v.getModele());
                    return vehiculeMap;
                })
                .collect(Collectors.toList());
            
            // Construire la réponse
            Map<String, Object> data = new HashMap<>();
            data.put("id", contribuable.getId());
            data.put("nom", contribuable.getNom());
            data.put("proprietes", proprietesSimplifiees);
            data.put("vehicules", vehiculesSimplifies);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", data);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des détails du contribuable avec ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_DETAILS_ERROR", 
                            "Erreur lors de la récupération des détails du contribuable", 
                            e.getMessage()));
        }
    }
}
