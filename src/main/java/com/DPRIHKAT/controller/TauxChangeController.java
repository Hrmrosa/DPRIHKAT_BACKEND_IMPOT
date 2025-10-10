package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Agent;
import com.DPRIHKAT.entity.TauxChange;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.Devise;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.security.UserDetailsImpl;
import com.DPRIHKAT.service.TauxChangeService;
import com.DPRIHKAT.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Contrôleur pour la gestion des taux de change
 * @author amateur
 */
@RestController
@RequestMapping("/api/taux-change")
public class TauxChangeController {

    private static final Logger logger = LoggerFactory.getLogger(TauxChangeController.class);

    @Autowired
    private TauxChangeService tauxChangeService;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * Récupère tous les taux de change actifs
     * @return Liste de tous les taux de change actifs
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN', 'DIRECTEUR')")
    public ResponseEntity<?> getAllActiveTauxChange() {
        try {
            logger.info("Récupération de tous les taux de change actifs");
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "tauxChanges", tauxChangeService.getAllActiveTauxChange()
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des taux de change actifs", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAUX_CHANGE_FETCH_ERROR", 
                            "Erreur lors de la récupération des taux de change", 
                            e.getMessage()));
        }
    }
    
    /**
     * Récupère l'historique complet des taux de change (actifs et inactifs)
     * @return Liste de tous les taux de change triés par date effective décroissante
     */
    @GetMapping("/historique")
    @PreAuthorize("hasAnyRole('CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN', 'DIRECTEUR')")
    public ResponseEntity<?> getAllTauxChanges() {
        try {
            logger.info("Récupération de l'historique complet des taux de change");
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "tauxChanges", tauxChangeService.getAllTauxChanges()
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'historique des taux de change", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAUX_CHANGE_HISTORY_FETCH_ERROR", 
                            "Erreur lors de la récupération de l'historique des taux de change", 
                            e.getMessage()));
        }
    }
    
    /**
     * Récupère l'historique des taux de change pour une paire de devises
     * @param deviseSource La devise source
     * @param deviseDestination La devise destination
     * @return Liste des taux de change pour la paire de devises triés par date effective décroissante
     */
    @GetMapping("/historique/devises")
    @PreAuthorize("hasAnyRole('CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN', 'DIRECTEUR')")
    public ResponseEntity<?> getTauxChangesByDevises(
            @RequestParam Devise deviseSource,
            @RequestParam Devise deviseDestination) {
        try {
            logger.info("Récupération de l'historique des taux de change de {} vers {}", deviseSource, deviseDestination);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "tauxChanges", tauxChangeService.getTauxChangesByDevises(deviseSource, deviseDestination)
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'historique des taux de change par devises", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAUX_CHANGE_HISTORY_DEVISES_FETCH_ERROR", 
                            "Erreur lors de la récupération de l'historique des taux de change par devises", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère le taux de change actif le plus récent pour une paire de devises
     * @param deviseSource La devise source
     * @param deviseDestination La devise destination
     * @return Le taux de change actif le plus récent
     */
    @GetMapping("/actuel")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN', 'DIRECTEUR')")
    public ResponseEntity<?> getLatestTauxChange(
            @RequestParam Devise deviseSource,
            @RequestParam Devise deviseDestination) {
        try {
            logger.info("Récupération du taux de change actuel de {} vers {}", deviseSource, deviseDestination);
            Optional<TauxChange> tauxChange = tauxChangeService.getLatestTauxChange(deviseSource, deviseDestination);
            
            if (tauxChange.isPresent()) {
                return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                        "tauxChange", tauxChange.get()
                )));
            } else {
                return ResponseEntity
                        .notFound()
                        .build();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du taux de change actuel", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAUX_CHANGE_FETCH_ERROR", 
                            "Erreur lors de la récupération du taux de change actuel", 
                            e.getMessage()));
        }
    }

    /**
     * Crée un nouveau taux de change
     * @param request La requête contenant les informations du taux de change
     * @param authentication L'authentification de l'utilisateur
     * @return Le taux de change créé
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('CHEF_DE_DIVISION', 'ADMIN', 'DIRECTEUR')")
    public ResponseEntity<?> createTauxChange(
            @RequestBody TauxChangeRequest request,
            Authentication authentication) {
        try {
            logger.info("Création d'un nouveau taux de change de {} vers {}", request.getDeviseSource(), request.getDeviseDestination());
            
            // Récupérer l'agent à partir de l'authentification
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            if (utilisateur.getAgent() == null) {
                throw new RuntimeException("Agent non associé à l'utilisateur");
            }
            
            TauxChange tauxChange = tauxChangeService.createTauxChange(
                    request.getTaux(),
                    request.getDeviseSource(),
                    request.getDeviseDestination(),
                    utilisateur.getAgent().getId()
            );
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Taux de change créé avec succès",
                    "tauxChange", tauxChange
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la création du taux de change", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAUX_CHANGE_CREATION_ERROR", 
                            "Erreur lors de la création du taux de change", 
                            e.getMessage()));
        }
    }

    /**
     * Désactive un taux de change
     * @param id L'ID du taux de change à désactiver
     * @return Statut de la désactivation
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CHEF_DE_DIVISION', 'ADMIN', 'DIRECTEUR')")
    public ResponseEntity<?> deactivateTauxChange(@PathVariable UUID id) {
        try {
            logger.info("Désactivation du taux de change avec l'ID: {}", id);
            tauxChangeService.desactiverTauxChange(id);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Taux de change désactivé avec succès"
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la désactivation du taux de change avec l'ID: {}", id, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAUX_CHANGE_DEACTIVATION_ERROR", 
                            "Erreur lors de la désactivation du taux de change", 
                            e.getMessage()));
        }
    }

    /**
     * Convertit un montant d'une devise à une autre
     * @param montant Le montant à convertir
     * @param deviseSource La devise source
     * @param deviseDestination La devise destination
     * @return Le montant converti
     */
    @GetMapping("/convertir")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'ADMIN', 'DIRECTEUR', 'CONTRIBUABLE')")
    public ResponseEntity<?> convertirMontant(
            @RequestParam Double montant,
            @RequestParam Devise deviseSource,
            @RequestParam Devise deviseDestination) {
        try {
            logger.info("Conversion de {} {} en {}", montant, deviseSource, deviseDestination);
            
            // Si les devises sont identiques, pas besoin de conversion
            if (deviseSource == deviseDestination) {
                return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                        "montantOriginal", montant,
                        "deviseSource", deviseSource,
                        "montantConverti", montant,
                        "deviseDestination", deviseDestination,
                        "tauxApplique", 1.0,
                        "dateConversion", new Date()
                )));
            }
            
            // Récupérer le taux de change le plus récent
            Optional<TauxChange> tauxChangeOpt = tauxChangeService.getLatestTauxChange(deviseSource, deviseDestination);
            
            if (tauxChangeOpt.isPresent()) {
                TauxChange tauxChange = tauxChangeOpt.get();
                Double montantConverti = montant * tauxChange.getTaux();
                
                return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                        "montantOriginal", montant,
                        "deviseSource", deviseSource,
                        "montantConverti", montantConverti,
                        "deviseDestination", deviseDestination,
                        "tauxApplique", tauxChange.getTaux(),
                        "dateConversion", new Date()
                )));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("CONVERSION_ERROR", 
                                "Erreur lors de la conversion du montant", 
                                "Aucun taux de change actif trouvé pour la conversion de " + deviseSource + " vers " + deviseDestination));
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la conversion du montant", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONVERSION_ERROR", 
                            "Erreur lors de la conversion du montant", 
                            e.getMessage()));
        }
    }

    /**
     * Classe interne pour la requête de création d'un taux de change
     */
    public static class TauxChangeRequest {
        private Double taux;
        private Devise deviseSource;
        private Devise deviseDestination;

        public Double getTaux() {
            return taux;
        }

        public void setTaux(Double taux) {
            this.taux = taux;
        }

        public Devise getDeviseSource() {
            return deviseSource;
        }

        public void setDeviseSource(Devise deviseSource) {
            this.deviseSource = deviseSource;
        }

        public Devise getDeviseDestination() {
            return deviseDestination;
        }

        public void setDeviseDestination(Devise deviseDestination) {
            this.deviseDestination = deviseDestination;
        }
    }
}
