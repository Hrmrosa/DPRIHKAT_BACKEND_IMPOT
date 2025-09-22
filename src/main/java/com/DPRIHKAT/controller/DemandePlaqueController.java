package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.DemandePlaque;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.StatutDemande;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.service.DemandePlaqueService;
import com.DPRIHKAT.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Contrôleur pour gérer les demandes de plaque d'immatriculation
 * 
 * @author amateur
 */
@RestController
@RequestMapping("/api/demandes-plaque")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DemandePlaqueController {
    
    private static final Logger logger = LoggerFactory.getLogger(DemandePlaqueController.class);
    
    @Autowired
    private DemandePlaqueService demandePlaqueService;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    /**
     * Soumettre une demande de plaque (par un contribuable)
     * 
     * @param vehiculeId ID du véhicule
     * @param facture Facture du véhicule
     * @param authentication Informations d'authentification
     * @return La demande créée
     */
    @PostMapping("/soumettre")
    @PreAuthorize("hasRole('ROLE_CONTRIBUABLE')")
    public ResponseEntity<?> soumettreDemande(
            @RequestParam UUID vehiculeId,
            @RequestParam("facture") MultipartFile facture,
            Authentication authentication) {
        try {
            logger.info("Soumission d'une demande de plaque pour le véhicule {}", vehiculeId);
            
            // Récupérer l'utilisateur authentifié
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            if (utilisateur.getContribuable() == null) {
                logger.error("L'utilisateur {} n'est pas un contribuable", login);
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", 
                                "Utilisateur non valide", 
                                "Seuls les contribuables peuvent soumettre des demandes"));
            }
            
            DemandePlaque demande = demandePlaqueService.soumettreDemande(
                    utilisateur.getContribuable().getId(),
                    vehiculeId,
                    facture);
            
            logger.info("Demande de plaque soumise avec succès: {}", demande.getId());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "demande", demande,
                    "message", "Demande soumise avec succès"
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la soumission de la demande", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DEMANDE_SUBMISSION_ERROR", 
                            "Erreur lors de la soumission de la demande", 
                            e.getMessage()));
        }
    }
    
    /**
     * Valider une demande (par un taxateur ou admin)
     * 
     * @param id ID de la demande
     * @param authentication Informations d'authentification
     * @return La demande validée
     */
    @PostMapping("/{id}/valider")
    @PreAuthorize("hasAnyRole('ROLE_TAXATEUR', 'ROLE_ADMIN')")
    public ResponseEntity<?> validerDemande(
            @PathVariable UUID id,
            Authentication authentication) {
        try {
            logger.info("Validation de la demande de plaque {}", id);
            
            // Récupérer l'utilisateur authentifié
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            DemandePlaque demande = demandePlaqueService.validerDemande(id, utilisateur.getId());
            
            logger.info("Demande de plaque validée avec succès: {}", demande.getId());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "demande", demande,
                    "message", "Demande validée avec succès"
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la validation de la demande", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DEMANDE_VALIDATION_ERROR", 
                            "Erreur lors de la validation de la demande", 
                            e.getMessage()));
        }
    }
    
    /**
     * Rejeter une demande (par un taxateur ou admin)
     * 
     * @param id ID de la demande
     * @param motifRejet Motif du rejet
     * @param authentication Informations d'authentification
     * @return La demande rejetée
     */
    @PostMapping("/{id}/rejeter")
    @PreAuthorize("hasAnyRole('ROLE_TAXATEUR', 'ROLE_ADMIN')")
    public ResponseEntity<?> rejeterDemande(
            @PathVariable UUID id,
            @RequestParam String motifRejet,
            Authentication authentication) {
        try {
            logger.info("Rejet de la demande de plaque {}", id);
            
            // Récupérer l'utilisateur authentifié
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            DemandePlaque demande = demandePlaqueService.rejeterDemande(id, utilisateur.getId(), motifRejet);
            
            logger.info("Demande de plaque rejetée avec succès: {}", demande.getId());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "demande", demande,
                    "message", "Demande rejetée avec succès"
            )));
        } catch (Exception e) {
            logger.error("Erreur lors du rejet de la demande", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DEMANDE_REJECTION_ERROR", 
                            "Erreur lors du rejet de la demande", 
                            e.getMessage()));
        }
    }
    
    /**
     * Marquer une demande comme payée
     * 
     * @param id ID de la demande
     * @return La demande payée
     */
    @PostMapping("/{id}/payer")
    @PreAuthorize("hasAnyRole('ROLE_RECEVEUR_DES_IMPOTS', 'ROLE_ADMIN')")
    public ResponseEntity<?> marquerCommePaye(@PathVariable UUID id) {
        try {
            logger.info("Marquage de la demande de plaque {} comme payée", id);
            
            DemandePlaque demande = demandePlaqueService.marquerCommePaye(id);
            
            logger.info("Demande de plaque marquée comme payée avec succès: {}", demande.getId());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "demande", demande,
                    "message", "Demande marquée comme payée avec succès"
            )));
        } catch (Exception e) {
            logger.error("Erreur lors du marquage de la demande comme payée", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DEMANDE_PAYMENT_ERROR", 
                            "Erreur lors du marquage de la demande comme payée", 
                            e.getMessage()));
        }
    }
    
    /**
     * Apurer et livrer une plaque
     * 
     * @param id ID de la demande
     * @param numeroPlaque Numéro de la plaque
     * @return La demande livrée
     */
    @PostMapping("/{id}/apurer")
    @PreAuthorize("hasAnyRole('ROLE_AGENT_DE_PLAQUES', 'ROLE_ADMIN')")
    public ResponseEntity<?> apurerEtLivrer(
            @PathVariable UUID id,
            @RequestParam String numeroPlaque) {
        try {
            logger.info("Apurement et livraison de la plaque pour la demande {}", id);
            
            DemandePlaque demande = demandePlaqueService.apurerEtLivrer(id, numeroPlaque);
            
            logger.info("Plaque livrée avec succès pour la demande: {}", demande.getId());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "demande", demande,
                    "message", "Plaque livrée avec succès"
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la livraison de la plaque", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DEMANDE_DELIVERY_ERROR", 
                            "Erreur lors de la livraison de la plaque", 
                            e.getMessage()));
        }
    }
    
    /**
     * Récupérer une demande par son ID
     * 
     * @param id ID de la demande
     * @return La demande
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_TAXATEUR', 'ROLE_RECEVEUR_DES_IMPOTS', 'ROLE_AGENT_DE_PLAQUES', 'ROLE_ADMIN', 'ROLE_CONTRIBUABLE')")
    public ResponseEntity<?> getDemandeById(@PathVariable UUID id, Authentication authentication) {
        try {
            logger.info("Récupération de la demande de plaque {}", id);
            
            DemandePlaque demande = demandePlaqueService.getDemandeById(id);
            
            // Si l'utilisateur est un contribuable, vérifier qu'il est bien le propriétaire de la demande
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            if (utilisateur.getContribuable() != null && !demande.getContribuable().getId().equals(utilisateur.getContribuable().getId())) {
                logger.error("L'utilisateur {} n'est pas autorisé à accéder à la demande {}", login, id);
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("UNAUTHORIZED", 
                                "Accès non autorisé", 
                                "Vous n'êtes pas autorisé à accéder à cette demande"));
            }
            
            logger.info("Demande de plaque récupérée avec succès: {}", demande.getId());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "demande", demande
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la demande", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DEMANDE_FETCH_ERROR", 
                            "Erreur lors de la récupération de la demande", 
                            e.getMessage()));
        }
    }
    
    /**
     * Récupérer toutes les demandes
     * 
     * @param statut Statut des demandes (optionnel)
     * @return Liste des demandes
     */
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_TAXATEUR', 'ROLE_RECEVEUR_DES_IMPOTS', 'ROLE_AGENT_DE_PLAQUES', 'ROLE_ADMIN')")
    public ResponseEntity<?> getAllDemandes(
            @RequestParam(required = false) StatutDemande statut) {
        try {
            logger.info("Récupération de toutes les demandes de plaque");
            
            List<DemandePlaque> demandes;
            if (statut != null) {
                logger.info("Filtrage par statut: {}", statut);
                demandes = demandePlaqueService.getDemandesByStatut(statut);
            } else {
                demandes = demandePlaqueService.getAllDemandes();
            }
            
            logger.info("{} demandes de plaque récupérées", demandes.size());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "demandes", demandes
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des demandes", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DEMANDES_FETCH_ERROR", 
                            "Erreur lors de la récupération des demandes", 
                            e.getMessage()));
        }
    }
    
    /**
     * Récupérer les demandes d'un contribuable
     * 
     * @param authentication Informations d'authentification
     * @return Liste des demandes
     */
    @GetMapping("/mes-demandes")
    @PreAuthorize("hasRole('ROLE_CONTRIBUABLE')")
    public ResponseEntity<?> getMesDemandes(Authentication authentication) {
        try {
            logger.info("Récupération des demandes de plaque du contribuable");
            
            // Récupérer l'utilisateur authentifié
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            if (utilisateur.getContribuable() == null) {
                logger.error("L'utilisateur {} n'est pas un contribuable", login);
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", 
                                "Utilisateur non valide", 
                                "Seuls les contribuables peuvent voir leurs demandes"));
            }
            
            List<DemandePlaque> demandes = demandePlaqueService.getDemandesByContribuable(utilisateur.getContribuable().getId());
            
            logger.info("{} demandes de plaque récupérées pour le contribuable {}", demandes.size(), utilisateur.getContribuable().getId());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "demandes", demandes
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des demandes", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DEMANDES_FETCH_ERROR", 
                            "Erreur lors de la récupération des demandes", 
                            e.getMessage()));
        }
    }
    
    /**
     * Récupérer les demandes par véhicule
     * 
     * @param vehiculeId ID du véhicule
     * @return Liste des demandes
     */
    @GetMapping("/vehicule/{vehiculeId}")
    @PreAuthorize("hasAnyRole('ROLE_TAXATEUR', 'ROLE_RECEVEUR_DES_IMPOTS', 'ROLE_AGENT_DE_PLAQUES', 'ROLE_ADMIN', 'ROLE_CONTRIBUABLE')")
    public ResponseEntity<?> getDemandesByVehicule(
            @PathVariable UUID vehiculeId,
            Authentication authentication) {
        try {
            logger.info("Récupération des demandes de plaque pour le véhicule {}", vehiculeId);
            
            // Si l'utilisateur est un contribuable, vérifier qu'il est bien le propriétaire du véhicule
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            List<DemandePlaque> demandes = demandePlaqueService.getDemandesByVehicule(vehiculeId);
            
            // Filtrer les demandes si l'utilisateur est un contribuable
            if (utilisateur.getContribuable() != null) {
                demandes = demandes.stream()
                        .filter(d -> d.getContribuable().getId().equals(utilisateur.getContribuable().getId()))
                        .toList();
            }
            
            logger.info("{} demandes de plaque récupérées pour le véhicule {}", demandes.size(), vehiculeId);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "demandes", demandes
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des demandes", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DEMANDES_FETCH_ERROR", 
                            "Erreur lors de la récupération des demandes", 
                            e.getMessage()));
        }
    }
}
