package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.service.ContribuableService;
import com.DPRIHKAT.service.VehiculeService;
import com.DPRIHKAT.service.VehiculeEntityService;
import com.DPRIHKAT.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Contrôleur pour gérer les données des véhicules
 *
 * @author amateur
 */
@RestController
@RequestMapping("/api/vehicules")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VehiculeController {

    private static final Logger logger = LoggerFactory.getLogger(VehiculeController.class);

    @Autowired
    private VehiculeService vehiculeService;

    @Autowired
    private VehiculeEntityService vehiculeEntityService;

    @Autowired
    private ContribuableService contribuableService;

    /**
     * Récupère la liste de toutes les marques de véhicules
     *
     * @return Liste des marques de véhicules
     */
    @GetMapping("/marques")
    public ResponseEntity<?> getMarques() {
        try {
            logger.info("Récupération de la liste des marques de véhicules");
            List<String> marques = vehiculeService.getMarques();
            logger.debug("Nombre de marques trouvées: {}", marques.size());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "marques", marques
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des marques de véhicules", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("VEHICULES_MARQUES_ERROR",
                            "Erreur lors de la récupération des marques de véhicules",
                            e.getMessage()));
        }
    }

    /**
     * Récupère la liste des modèles pour une marque donnée
     *
     * @param marque Nom de la marque
     * @return Liste des modèles pour la marque spécifiée
     */
    @GetMapping("/marques/{marque}/modeles")
    public ResponseEntity<?> getModelesByMarque(@PathVariable String marque) {
        try {
            logger.info("Récupération des modèles pour la marque: {}", marque);
            List<String> modeles = vehiculeService.getModelesByMarque(marque);

            if (modeles == null) {
                logger.warn("Marque non trouvée: {}", marque);
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("VEHICULES_MARQUE_NOT_FOUND",
                                "Marque non trouvée",
                                "La marque spécifiée n'existe pas: " + marque));
            }

            logger.debug("Nombre de modèles trouvés pour {}: {}", marque, modeles.size());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "marque", marque,
                    "modeles", modeles
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des modèles pour la marque: {}", marque, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("VEHICULES_MODELES_ERROR",
                            "Erreur lors de la récupération des modèles",
                            e.getMessage()));
        }
    }

    /**
     * Vérifie si un modèle existe pour une marque donnée
     *
     * @param marque Nom de la marque
     * @param modele Nom du modèle
     * @return true si le modèle existe pour la marque, false sinon
     */
    @GetMapping("/marques/{marque}/modeles/{modele}/exists")
    public ResponseEntity<?> existsModele(@PathVariable String marque, @PathVariable String modele) {
        try {
            logger.info("Vérification de l'existence du modèle {} pour la marque {}", modele, marque);
            boolean exists = vehiculeService.existsModele(marque, modele);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "marque", marque,
                    "modele", modele,
                    "exists", exists
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de l'existence du modèle {} pour la marque {}", modele, marque, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("VEHICULES_VERIFICATION_ERROR",
                            "Erreur lors de la vérification du modèle",
                            e.getMessage()));
        }
    }

    /**
     * Recharge les données des véhicules depuis le fichier voiture.json
     */
    @PostMapping("/reload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> reloadVehicules() {
        try {
            logger.info("Rechargement des données des véhicules");
            vehiculeService.rechargerVehicules();

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Données des véhicules rechargées avec succès"
            )));
        } catch (Exception e) {
            logger.error("Erreur lors du rechargement des données des véhicules", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("VEHICULES_RELOAD_ERROR",
                            "Erreur lors du rechargement des données des véhicules",
                            e.getMessage()));
        }
    }

    /**
     * Crée un nouveau véhicule
     *
     * @param vehicule Les données du véhicule à créer
     * @return Le véhicule créé
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'TAXATEUR', 'ADMIN')")
    public ResponseEntity<?> createVehicule(@RequestBody Vehicule vehicule) {
        try {
            logger.info("Création d'un nouveau véhicule: {}", vehicule);

            // Validation des champs obligatoires
            if (vehicule.getImmatriculation() == null || vehicule.getImmatriculation().isEmpty()) {
                throw new IllegalArgumentException("L'immatriculation est obligatoire");
            }

            if (vehicule.getProprietaireId() == null) {
                throw new IllegalArgumentException("Le proprietaireId est obligatoire");
            }

            // Charger le propriétaire
            Contribuable proprietaire = contribuableService.findById(vehicule.getProprietaireId());
            if (proprietaire == null) {
                throw new IllegalArgumentException("Propriétaire non trouvé avec l'ID: " + vehicule.getProprietaireId());
            }
            vehicule.setProprietaire(proprietaire);

            // Si contribuableId est fourni, charger le contribuable, sinon utiliser le propriétaire
            if (vehicule.getContribuableId() != null) {
                Contribuable contribuable = contribuableService.findById(vehicule.getContribuableId());
                if (contribuable == null) {
                    throw new IllegalArgumentException("Contribuable non trouvé avec l'ID: " + vehicule.getContribuableId());
                }
                vehicule.setContribuable(contribuable);
            } else {
                vehicule.setContribuable(proprietaire);
            }

            Vehicule nouveauVehicule = vehiculeEntityService.createVehicule(vehicule);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "vehicule", nouveauVehicule,
                    "message", "Véhicule créé avec succès"
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la création du véhicule", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("VEHICULE_CREATION_ERROR",
                            "Erreur lors de la création du véhicule",
                            e.getMessage()));
        }
    }

    /**
     * Met à jour le genre et la catégorie d'un véhicule
     *
     * @param id ID du véhicule
     * @param genre Genre du véhicule
     * @param categorie Catégorie du véhicule
     * @return Véhicule mis à jour
     */
    @PutMapping("/{id}/classification")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'TAXATEUR', 'ADMIN')")
    public ResponseEntity<?> updateVehiculeClassification(
            @PathVariable UUID id,
            @RequestParam String genre,
            @RequestParam String categorie) {
        try {
            logger.info("Mise à jour de la classification du véhicule {}: genre={}, catégorie={}", id, genre, categorie);

            Vehicule vehicule = vehiculeEntityService.updateClassification(id, genre, categorie);

            if (vehicule == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("VEHICULE_NOT_FOUND",
                                "Véhicule non trouvé",
                                "Aucun véhicule trouvé avec l'ID: " + id));
            }

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "vehicule", vehicule,
                    "message", "Classification du véhicule mise à jour avec succès"
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la classification du véhicule {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("VEHICULE_UPDATE_ERROR",
                            "Erreur lors de la mise à jour de la classification du véhicule",
                            e.getMessage()));
        }
    }

    /**
     * Récupère tous les véhicules d'un contribuable
     *
     * @param contribuableId ID du contribuable
     * @return Liste des véhicules du contribuable
     */
    @GetMapping("/contribuable/{contribuableId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR', 'TAXATEUR', 'CONTRIBUABLE')")
    public ResponseEntity<?> getVehiculesByContribuable(@PathVariable UUID contribuableId) {
        try {
            logger.info("Récupération des véhicules du contribuable avec ID: {}", contribuableId);

            List<Vehicule> vehicules = vehiculeEntityService.findByContribuableId(contribuableId);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "vehicules", vehicules
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des véhicules du contribuable {}", contribuableId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("VEHICULES_FETCH_ERROR",
                            "Erreur lors de la récupération des véhicules du contribuable",
                            e.getMessage()));
        }
    }

    /**
     * Récupère un véhicule par son ID
     *
     * @param id ID du véhicule
     * @return Détails du véhicule
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR', 'TAXATEUR', 'CONTRIBUABLE', 'AGENT_DE_PLAQUES')")
    public ResponseEntity<?> getVehiculeById(@PathVariable UUID id) {
        try {
            logger.info("Récupération du véhicule avec ID: {}", id);

            Vehicule vehicule = vehiculeEntityService.findById(id);

            if (vehicule == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("VEHICULE_NOT_FOUND",
                                "Véhicule non trouvé",
                                "Aucun véhicule trouvé avec l'ID: " + id));
            }

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "vehicule", vehicule
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du véhicule {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("VEHICULE_FETCH_ERROR",
                            "Erreur lors de la récupération du véhicule",
                            e.getMessage()));
        }
    }

    /**
     * Récupère tous les véhicules
     *
     * @return Liste de tous les véhicules
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR', 'TAXATEUR')")
    public ResponseEntity<?> getAllVehicules() {
        try {
            logger.info("Récupération de tous les véhicules");

            List<Vehicule> vehicules = vehiculeEntityService.findAll();

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "vehicules", vehicules
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de tous les véhicules", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("VEHICULES_FETCH_ERROR",
                            "Erreur lors de la récupération de tous les véhicules",
                            e.getMessage()));
        }
    }
}