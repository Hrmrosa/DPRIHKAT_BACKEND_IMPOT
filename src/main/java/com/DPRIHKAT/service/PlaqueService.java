package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Plaque;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.entity.enums.StatutPlaque;
import com.DPRIHKAT.repository.PlaqueRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PlaqueService {

    private static final Logger logger = LoggerFactory.getLogger(PlaqueService.class);

    @Autowired
    private PlaqueRepository plaqueRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private VignetteService vignetteService;

    /**
     * Check if there is at least one plaque available in stock
     */
    public boolean isPlaqueAvailable() {
        return plaqueRepository.findFirstByDisponibleTrue().isPresent();
    }

    /**
     * Assign a plaque to a vehicle
     */
    @Transactional
    public Plaque assignPlaqueToVehicle(UUID vehiculeId, UUID userId) {
        logger.info("Assignation d'une plaque au véhicule {}", vehiculeId);
        
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé avec ID: " + vehiculeId));

        // Ensure user exists (and is an agent) even if we don't store it on Plaque
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec ID: " + userId));
        if (utilisateur.getAgent() == null) {
            throw new RuntimeException("Seuls les agents peuvent assigner des plaques");
        }

        Plaque plaque = plaqueRepository.findFirstByDisponibleTrue()
                .orElseThrow(() -> new RuntimeException("Aucune plaque disponible en stock"));

        plaque.setVehicule(vehicule);
        plaque.setDisponible(false);
        plaque.setStatut(StatutPlaque.ATTRIBUEE);

        logger.info("Plaque assignée avec succès au véhicule {}", vehiculeId);
        return plaqueRepository.save(plaque);
    }
    
    /**
     * Assign a plaque to a vehicle (version simplifiée pour DemandePlaqueService)
     */
    @Transactional
    public Plaque assignPlaqueToVehicle(UUID vehiculeId) {
        logger.info("Assignation d'une plaque au véhicule {}", vehiculeId);
        
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé avec ID: " + vehiculeId));

        Plaque plaque = plaqueRepository.findFirstByDisponibleTrue()
                .orElseThrow(() -> new RuntimeException("Aucune plaque disponible en stock"));

        plaque.setVehicule(vehicule);
        plaque.setDisponible(false);
        plaque.setStatut(StatutPlaque.ATTRIBUEE);

        logger.info("Plaque assignée avec succès au véhicule {}", vehiculeId);
        return plaqueRepository.save(plaque);
    }

    /**
     * Get plaque by ID
     */
    public Plaque getPlaqueById(UUID id) {
        return plaqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plaque non trouvée avec ID: " + id));
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
    
    /**
     * Get plaques by status
     */
    public List<Plaque> getPlaquesByStatut(StatutPlaque statut) {
        return plaqueRepository.findByStatut(statut);
    }
    
    /**
     * Save plaque
     */
    public Plaque save(Plaque plaque) {
        return plaqueRepository.save(plaque);
    }
    
    /**
     * Create a new plaque in stock
     */
    @Transactional
    public Plaque createPlaque(String numeroSerie, String numplaque) {
        logger.info("Création d'une nouvelle plaque avec numéro de série {} et numéro de plaque {}", numeroSerie, numplaque);
        
        Plaque plaque = new Plaque(numeroSerie, numplaque, true);
        plaque.setStatut(StatutPlaque.STOCK);
        
        logger.info("Plaque créée avec succès");
        return plaqueRepository.save(plaque);
    }
    
    /**
     * Deliver a plaque to a vehicle
     */
    @Transactional
    public Plaque deliverPlaque(UUID plaqueId, String numeroPlaque) {
        logger.info("Livraison de la plaque {} avec numéro {}", plaqueId, numeroPlaque);
        
        Plaque plaque = plaqueRepository.findById(plaqueId)
                .orElseThrow(() -> new RuntimeException("Plaque non trouvée avec ID: " + plaqueId));
        
        if (plaque.getStatut() != StatutPlaque.ATTRIBUEE) {
            throw new RuntimeException("Cette plaque ne peut pas être livrée car elle n'est pas en statut ATTRIBUEE");
        }
        
        plaque.setNumplaque(numeroPlaque);
        plaque.setStatut(StatutPlaque.LIVREE);
        
        logger.info("Plaque livrée avec succès");
        return plaqueRepository.save(plaque);
    }

    /**
     * Crée directement une plaque pour un véhicule (sans demande)
     */
    @Transactional
    public Plaque creerPlaqueDirectement(UUID vehiculeId, String numeroPlaque, UUID agentId) {
        logger.info("Création directe d'une plaque pour le véhicule {}", vehiculeId);
        
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
                
        Utilisateur agent = utilisateurRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé"));
        
        if (agent.getAgent() == null) {
            throw new RuntimeException("Seuls les agents peuvent créer des plaques directement");
        }
        
        Plaque plaque = new Plaque();
        plaque.setNumeroSerie(UUID.randomUUID().toString());
        plaque.setNumplaque(numeroPlaque);
        plaque.setDisponible(false);
        plaque.setStatut(StatutPlaque.LIVREE);
        plaque.setVehicule(vehicule);
        
        // Générer automatiquement une vignette
        vignetteService.genererVignettePourVehicule(vehiculeId);
        
        return plaqueRepository.save(plaque);
    }
}
