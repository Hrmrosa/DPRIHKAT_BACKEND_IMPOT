package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Plaque;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.repository.PlaqueRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlaqueService {

    @Autowired
    private PlaqueRepository plaqueRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * Check if there is at least one plaque available in stock
     */
    public boolean isPlaqueAvailable() {
        return plaqueRepository.findFirstByDisponibleTrue().isPresent();
    }

    /**
     * Assign a plaque to a vehicle
     */
    public Plaque assignPlaqueToVehicle(UUID vehiculeId, UUID userId) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));

        // Ensure user exists (and is an agent) even if we don't store it on Plaque
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        if (utilisateur.getAgent() == null) {
            throw new RuntimeException("Seuls les agents peuvent assigner des plaques");
        }

        Plaque plaque = plaqueRepository.findFirstByDisponibleTrue()
                .orElseThrow(() -> new RuntimeException("Aucune plaque disponible"));

        plaque.setVehicule(vehicule);
        plaque.setDisponible(false);

        return plaqueRepository.save(plaque);
    }

    /**
     * Get plaque by ID
     */
    public Plaque getPlaqueById(UUID id) {
        return plaqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plaque non trouvée"));
    }

    /**
     * Get all plaques
     */
    public List<Plaque> getAllPlaques() {
        return plaqueRepository.findAll();
    }

    /**
     * Get available plaques
     */
    public List<Plaque> getAvailablePlaques() {
        return plaqueRepository.findByDisponibleTrue();
    }

    /**
     * Get assigned plaques
     */
    public List<Plaque> getAssignedPlaques() {
        return plaqueRepository.findByDisponibleFalse();
    }

    /**
     * Get plaques by vehicle
     */
    public List<Plaque> getPlaquesByVehicle(UUID vehiculeId) {
        return plaqueRepository.findByVehiculeId(vehiculeId);
    }
}
