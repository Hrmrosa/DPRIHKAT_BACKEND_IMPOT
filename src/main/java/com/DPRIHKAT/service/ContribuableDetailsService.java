package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.repository.ContribuableRepository;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service pour gérer les détails des contribuables
 */
@Service
public class ContribuableDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(ContribuableDetailsService.class);
    private final ContribuableRepository contribuableRepository;
    private final ProprieteRepository proprieteRepository;
    private final VehiculeRepository vehiculeRepository;

    public ContribuableDetailsService(ContribuableRepository contribuableRepository,
                                ProprieteRepository proprieteRepository,
                                VehiculeRepository vehiculeRepository) {
        this.contribuableRepository = contribuableRepository;
        this.proprieteRepository = proprieteRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    /**
     * Récupère un contribuable par son ID
     * @param id ID du contribuable
     * @return Contribuable ou empty si non trouvé
     */
    public Optional<Contribuable> getContribuableById(UUID id) {
        return contribuableRepository.findById(id);
    }
    
    /**
     * Récupère les propriétés d'un contribuable
     * @param contribuableId ID du contribuable
     * @return Liste des propriétés simplifiées
     */
    public List<Map<String, Object>> getProprietesByContribuable(UUID contribuableId) {
        List<Propriete> proprietes = proprieteRepository.findByProprietaire_Id(contribuableId);
        return proprietes.stream()
            .map(p -> {
                Map<String, Object> proprieteMap = new HashMap<>();
                proprieteMap.put("id", p.getId());
                proprieteMap.put("adresse", p.getAdresse());
                proprieteMap.put("valeur", p.getMontantImpot() != null ? p.getMontantImpot() : 0.0);
                return proprieteMap;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Récupère les véhicules d'un contribuable
     * @param contribuableId ID du contribuable
     * @return Liste des véhicules simplifiés
     */
    public List<Map<String, Object>> getVehiculesByContribuable(UUID contribuableId) {
        // Récupérer les véhicules (à la fois comme propriétaire et comme contribuable)
        List<Vehicule> vehiculesProprietaire = vehiculeRepository.findByProprietaire_Id(contribuableId);
        List<Vehicule> vehiculesContribuable = vehiculeRepository.findByContribuable_Id(contribuableId);
        
        // Fusionner les deux listes sans doublons
        Set<Vehicule> allVehicules = new HashSet<>();
        allVehicules.addAll(vehiculesProprietaire);
        allVehicules.addAll(vehiculesContribuable);
        
        // Transformer en Map
        return allVehicules.stream()
            .map(v -> {
                Map<String, Object> vehiculeMap = new HashMap<>();
                vehiculeMap.put("id", v.getId());
                vehiculeMap.put("immatriculation", v.getImmatriculation());
                vehiculeMap.put("marque", v.getMarque());
                vehiculeMap.put("modele", v.getModele());
                return vehiculeMap;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Récupère les détails complets d'un contribuable
     * @param id ID du contribuable
     * @return Détails complets du contribuable
     */
    public Map<String, Object> getContribuableDetails(UUID id) {
        logger.info("Récupération des détails complets du contribuable avec ID: {}", id);
        
        // Récupérer le contribuable
        Optional<Contribuable> contribuableOpt = getContribuableById(id);
        if (contribuableOpt.isEmpty()) {
            throw new RuntimeException("Contribuable non trouvé avec ID: " + id);
        }
        
        Contribuable contribuable = contribuableOpt.get();
        
        // Récupérer les propriétés
        List<Map<String, Object>> proprietes = getProprietesByContribuable(id);
        
        // Récupérer les véhicules
        List<Map<String, Object>> vehicules = getVehiculesByContribuable(id);
        
        // Construire la réponse
        Map<String, Object> data = new HashMap<>();
        data.put("id", contribuable.getId());
        data.put("nom", contribuable.getNom());
        data.put("proprietes", proprietes);
        data.put("vehicules", vehicules);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        
        return response;
    }
}
