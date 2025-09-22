package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.Taxation;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.repository.ContribuableRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.service.ContribuableService;
import com.DPRIHKAT.service.TaxationService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.DPRIHKAT.entity.enums.Role.CONTRIBUABLE;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/contribuables")
public class ContribuableController {

    @Autowired
    private ContribuableService contribuableService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ContribuableRepository contribuableRepository;

    @Autowired
    private TaxationService taxationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR', 'CHEF_DE_BUREAU')")
    public ResponseEntity<?> getAllContribuables() {
        try {
            List<Contribuable> contribuables = contribuableService.findAll();
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("contribuables", contribuables)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLES_FETCH_ERROR", "Erreur lors de la récupération des contribuables", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTROLLEUR', 'CHEF_DE_BUREAU', 'CONTRIBUABLE')")
    public ResponseEntity<?> getContribuableById(@PathVariable UUID id) {
        try {
            Contribuable contribuable = contribuableService.findById(id);
            if (contribuable == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_NOT_FOUND", "Contribuable non trouvé", "Aucun contribuable trouvé avec l'ID fourni"));
            }
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("contribuable", contribuable)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_FETCH_ERROR", "Erreur lors de la récupération du contribuable", e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> createContribuable(@RequestBody Contribuable contribuable) {
        try {
            Contribuable createdContribuable = contribuableService.save(contribuable);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("contribuable", createdContribuable)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_CREATE_ERROR", "Erreur lors de la création du contribuable", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CONTRIBUABLE')")
    public ResponseEntity<?> updateContribuable(@PathVariable UUID id, @RequestBody Contribuable contribuable) {
        try {
            Contribuable updatedContribuable = contribuableService.update(id, contribuable);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("contribuable", updatedContribuable)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_UPDATE_ERROR", "Erreur lors de la mise à jour du contribuable", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> deleteContribuable(@PathVariable UUID id) {
        try {
            Contribuable contribuable = contribuableService.deactivateContribuable(id);
            if (contribuable == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_NOT_FOUND", "Contribuable non trouvé", "Aucun contribuable trouvé avec l'ID fourni"));
            }
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("message", "Contribuable désactivé avec succès", "contribuable", contribuable)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_DEACTIVATION_ERROR", "Erreur lors de la désactivation du contribuable", e.getMessage()));
        }
    }

    /**
     * Active un contribuable précédemment désactivé
     * @param id L'ID du contribuable à activer
     * @return Le contribuable activé
     */
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> activateContribuable(@PathVariable UUID id) {
        try {
            Contribuable contribuable = contribuableService.activateContribuable(id);
            if (contribuable == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_NOT_FOUND", "Contribuable non trouvé", "Aucun contribuable trouvé avec l'ID fourni"));
            }
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("message", "Contribuable activé avec succès", "contribuable", contribuable)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRIBUABLE_ACTIVATION_ERROR", "Erreur lors de l'activation du contribuable", e.getMessage()));
        }
    }

    @GetMapping("/{id}/taxations")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN', 'CONTRIBUABLE')")
    public ResponseEntity<?> getContribuableTaxations(
            @PathVariable UUID id,
            Authentication authentication) {
        try {
            // Vérifier les permissions
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login).orElse(null);
            
            // Si c'est un contribuable, il ne peut voir que ses propres taxations
            if (utilisateur != null && utilisateur.getRole() == CONTRIBUABLE) {
                if (utilisateur.getContribuable() == null || !utilisateur.getContribuable().getId().equals(id)) {
                    return ResponseEntity
                            .status(HttpStatus.FORBIDDEN)
                            .body(ResponseUtil.createErrorResponse("ACCESS_DENIED", "Accès refusé", "Vous ne pouvez consulter que vos propres taxations"));
                }
            }
            
            // Récupérer les taxations du contribuable
            List<Taxation> taxations = taxationService.getTaxationsByContribuableId(id);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "taxations", taxations,
                    "count", taxations.size()
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATIONS_FETCH_ERROR", "Erreur lors de la récupération des taxations", e.getMessage()));
        }
    }

    @GetMapping("/me/taxations")
    @PreAuthorize("hasRole('CONTRIBUABLE')")
    public ResponseEntity<?> getMyTaxations(Authentication authentication) {
        try {
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login).orElse(null);
            
            if (utilisateur == null || utilisateur.getContribuable() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Utilisateur non trouvé ou n'est pas un contribuable"));
            }
            
            // Récupérer les taxations du contribuable connecté
            List<Taxation> taxations = taxationService.getTaxationsByContribuableId(utilisateur.getContribuable().getId());
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "taxations", taxations,
                    "count", taxations.size()
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATIONS_FETCH_ERROR", "Erreur lors de la récupération des taxations", e.getMessage()));
        }
    }
}
