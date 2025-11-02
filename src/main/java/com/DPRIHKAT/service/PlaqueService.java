package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Plaque;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.enums.StatutPlaque;
import com.DPRIHKAT.entity.enums.StatutVehicule;
import com.DPRIHKAT.repository.PlaqueRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import com.DPRIHKAT.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import java.util.HashMap;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> getAllPlaques(int page, int size, Boolean disponible) {
        try {
            List<Plaque> plaques;
            if (disponible != null) {
                plaques = disponible ? plaqueRepository.findByDisponibleTrue() 
                                  : plaqueRepository.findByDisponibleFalse();
            } else {
                plaques = plaqueRepository.findAll();
            }

            // Convertir en DTO avec détails du véhicule
            List<Map<String, Object>> plaqueDTOs = plaques.stream().map(plaque -> {
                Map<String, Object> plaqueMap = new HashMap<>();
                plaqueMap.put("id", plaque.getId());
                plaqueMap.put("numplaque", plaque.getNumplaque());
                plaqueMap.put("numeroSerie", plaque.getNumeroSerie());
                plaqueMap.put("disponible", plaque.isDisponible());
                plaqueMap.put("statut", plaque.getStatut());
                
                if (plaque.getVehicule() != null) {
                    Vehicule vehicule = plaque.getVehicule();
                    Map<String, Object> vehiculeMap = new HashMap<>();
                    vehiculeMap.put("id", vehicule.getId());
                    vehiculeMap.put("marque", vehicule.getMarque());
                    vehiculeMap.put("modele", vehicule.getModele());
                    vehiculeMap.put("annee", vehicule.getAnnee());
                    
                    if (vehicule.getProprietaire() != null) {
                        Map<String, Object> proprietaireMap = new HashMap<>();
                        proprietaireMap.put("id", vehicule.getProprietaire().getId());
                        proprietaireMap.put("nomComplet", vehicule.getProprietaire().getNom()); 
                        vehiculeMap.put("proprietaire", proprietaireMap);
                    }
                    
                    plaqueMap.put("vehicule", vehiculeMap);
                }
                
                return plaqueMap;
            }).collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("plaques", plaqueDTOs);
            response.put("totalItems", plaques.size());
            response.put("currentPage", page);
            response.put("totalPages", (int) Math.ceil((double) plaques.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_FETCH_ERROR", "Erreur lors de la récupération des plaques", e.getMessage()));
        }
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
        return plaqueRepository.findByVehicule_Id(vehiculeId);
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

    /**
     * Importe des plaques à partir d'un fichier
     * @param file Fichier contenant les plaques à importer (CSV ou Excel)
     * @return Map contenant les statistiques de l'import
     */
    @Transactional
    public Map<String, Object> importPlaques(MultipartFile file) {
        // Implémentation basique - à compléter selon les besoins
        try {
            // TODO: Implémenter la logique d'import
            return Map.of(
                "success", true,
                "imported", 0,
                "errors", 0
            );
        } catch (Exception e) {
            logger.error("Erreur lors de l'import des plaques", e);
            throw new RuntimeException("Erreur lors de l'import des plaques", e);
        }
    }

    public Resource exportPlaques() {
        // TODO: Implémenter l'export
        throw new UnsupportedOperationException("Export non implémenté");
    }

    public ResponseEntity<?> updatePlaque(UUID id, Plaque plaque) {
        try {
            Plaque existingPlaque = plaqueRepository.findById(id).orElseThrow();
            existingPlaque.setNumplaque(plaque.getNumplaque());
            existingPlaque.setDisponible(plaque.isDisponible());
            plaqueRepository.save(existingPlaque);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("plaque", existingPlaque)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.createErrorResponse("PLAQUE_UPDATE_ERROR", "Erreur de mise à jour", e.getMessage()));
        }
    }

    public ResponseEntity<?> validatePlaque(String numeroPlaque) {
        // TODO: Implémenter la validation
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("valid", true)));
    }

    public ResponseEntity<?> plaqueExists(String numeroPlaque) {
        boolean exists = plaqueRepository.existsByNumplaque(numeroPlaque);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("exists", exists)));
    }

    public ResponseEntity<?> assignPlaqueToVehicule(UUID plaqueId, UUID vehiculeId) {
        try {
            Plaque plaque = plaqueRepository.findById(plaqueId).orElseThrow();
            Vehicule vehicule = vehiculeRepository.findById(vehiculeId).orElseThrow();
            plaque.setVehicule(vehicule);
            plaque.setDisponible(false);
            plaqueRepository.save(plaque);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("message", "Plaque assignée avec succès")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.createErrorResponse("PLAQUE_ASSIGN_ERROR", "Erreur d'assignation", e.getMessage()));
        }
    }

    public ResponseEntity<?> unassignPlaqueFromVehicule(UUID plaqueId) {
        try {
            Plaque plaque = plaqueRepository.findById(plaqueId).orElseThrow();
            plaque.setVehicule(null);
            plaque.setDisponible(true);
            plaqueRepository.save(plaque);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("message", "Plaque dissociée avec succès")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.createErrorResponse("PLAQUE_UNASSIGN_ERROR", "Erreur de dissociation", e.getMessage()));
        }
    }

    public ResponseEntity<?> getVehiculeByPlaque(UUID plaqueId) {
        try {
            Plaque plaque = plaqueRepository.findById(plaqueId)
                .orElseThrow(() -> new RuntimeException("Plaque non trouvée"));
            
            if (plaque.getVehicule() == null) {
                return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Aucun véhicule associé à cette plaque"
                )));
            }
            
            Vehicule vehicule = plaque.getVehicule();
            
            // Détails du véhicule
            Map<String, Object> vehiculeDetails = Map.of(
                "id", vehicule.getId(),
                "marque", vehicule.getMarque(),
                "modele", vehicule.getModele(),
                "annee", vehicule.getAnnee(),
                "immatriculation", vehicule.getImmatriculation(),
                "numeroChassis", vehicule.getNumeroChassis()
            );
            
            // Détails du contribuable si existant
            Map<String, Object> contribuableDetails = null;
            if (vehicule.getProprietaire() != null) {
                Contribuable proprietaire = vehicule.getProprietaire();
                contribuableDetails = Map.of(
                    "id", proprietaire.getId(),
                    "nom", proprietaire.getNom(),
                    "prenom", proprietaire.getNom(),
                    "numeroIdentification", proprietaire.getNumeroIdentificationContribuable()
                );
            }
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "vehicule", vehiculeDetails,
                "contribuable", contribuableDetails,
                "plaque", plaque.getNumplaque()
            )));
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(ResponseUtil.createErrorResponse("VEHICULE_FETCH_ERROR", "Erreur de récupération", e.getMessage()));
        }
    }
    
    /**
     * Marquer une plaque comme livrée
     * 
     * @param plaqueId ID de la plaque
     * @return La plaque mise à jour
     */
    @Transactional
    public Plaque livrerPlaque(UUID plaqueId) {
        logger.info("Livraison de la plaque {}", plaqueId);
        
        Plaque plaque = plaqueRepository.findById(plaqueId)
                .orElseThrow(() -> new RuntimeException("Plaque non trouvée avec ID: " + plaqueId));
        
        if (plaque.getStatut() != StatutPlaque.ATTRIBUEE) {
            throw new RuntimeException("Cette plaque ne peut pas être livrée car elle n'est pas en statut ATTRIBUEE");
        }
        
        // Mettre à jour le statut de la plaque
        plaque.setStatut(StatutPlaque.LIVREE);
        plaque = plaqueRepository.save(plaque);
        
        // Mettre à jour le statut du véhicule
        if (plaque.getVehicule() != null) {
            Vehicule vehicule = plaque.getVehicule();
            vehicule.setStatut(StatutVehicule.ACTIF);
            vehiculeRepository.save(vehicule);
        }
        
        logger.info("Plaque {} marquée comme livrée avec succès", plaqueId);
        return plaque;
    }
    
    /**
     * Libérer une plaque (la remettre en stock)
     * 
     * @param plaqueId ID de la plaque
     * @return La plaque mise à jour
     */
    @Transactional
    public Plaque libererPlaque(UUID plaqueId) {
        logger.info("Libération de la plaque {}", plaqueId);
        
        Plaque plaque = plaqueRepository.findById(plaqueId)
                .orElseThrow(() -> new RuntimeException("Plaque non trouvée avec ID: " + plaqueId));
        
        // Détacher la plaque du véhicule
        if (plaque.getVehicule() != null) {
            Vehicule vehicule = plaque.getVehicule();
            vehicule.setNumeroPlaque(null);
            vehicule.setStatut(StatutVehicule.ENREGISTRE);
            vehiculeRepository.save(vehicule);
        }
        
        // Remettre la plaque en stock
        plaque.setVehicule(null);
        plaque.setStatut(StatutPlaque.STOCK);
        plaque.setDisponible(true);
        plaque.setDemande(null);
        plaque = plaqueRepository.save(plaque);
        
        logger.info("Plaque {} libérée avec succès", plaqueId);
        return plaque;
    }
}
