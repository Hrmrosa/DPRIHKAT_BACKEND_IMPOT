package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.ConcessionMinier;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.repository.ConcessionMinierRepository;
import com.DPRIHKAT.service.TaxationService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/taxation")
public class TaxationController {

    @Autowired
    private TaxationService taxationService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ProprieteRepository proprieteRepository;

    @Autowired
    private ConcessionMinierRepository concessionMinierRepository;

    @PostMapping("/property/{propertyId}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION','ADMIN')")
    public ResponseEntity<?> generateTaxNoteForProperty(@PathVariable UUID propertyId, Authentication authentication) {
        try {
            // Get the authenticated agent
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null || utilisateur.getAgent() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les agents peuvent générer des notes de taxation"));
            }

            // Generate tax note
            Propriete propriete = proprieteRepository.findById(propertyId).orElse(null);
            if (propriete == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("PROPERTY_NOT_FOUND", "Propriété non trouvée", "Aucune propriété avec cet ID"));
            }
            Declaration declaration = taxationService.generateTaxNoteForProperty(propriete, utilisateur.getAgent());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Note de taxation générée avec succès",
                    "declaration", declaration
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_ERROR", "Erreur lors de la génération de la note de taxation", e.getMessage()));
        }
    }

    @PostMapping("/concession/{concessionId}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION','ADMIN')")
    public ResponseEntity<?> generateTaxNoteForConcession(@PathVariable UUID concessionId, Authentication authentication) {
        try {
            // Get the authenticated agent
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null || utilisateur.getAgent() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les agents peuvent générer des notes de taxation"));
            }

            // Generate tax note
            ConcessionMinier concession = concessionMinierRepository.findById(concessionId).orElse(null);
            if (concession == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("CONCESSION_NOT_FOUND", "Concession non trouvée", "Aucune concession avec cet ID"));
            }
            Declaration declaration = taxationService.generateTaxNoteForConcession(concession, utilisateur.getAgent());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Note de taxation générée avec succès",
                    "declaration", declaration
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("TAXATION_ERROR", "Erreur lors de la génération de la note de taxation", e.getMessage()));
        }
    }

    @GetMapping("/calculate/if/property/{propertyId}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'CONTRIBUABLE','ADMIN')")
    public ResponseEntity<?> calculateIFTaxForProperty(@PathVariable UUID propertyId) {
        try {
            // Calculate IF tax
            Propriete propriete = proprieteRepository.findById(propertyId).orElse(null);
            if (propriete == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("PROPERTY_NOT_FOUND", "Propriété non trouvée", "Aucune propriété avec cet ID"));
            }
            double taxAmount = taxationService.calculateTaxForProperty(propriete);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Calcul de l'impôt foncier effectué avec succès",
                    "montant", taxAmount
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CALCULATION_ERROR", "Erreur lors du calcul de l'impôt foncier", e.getMessage()));
        }
    }

    @GetMapping("/calculate/icm/concession/{concessionId}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'CONTRIBUABLE','ADMIN')")
    public ResponseEntity<?> calculateICMTaxForConcession(@PathVariable UUID concessionId) {
        try {
            // Calculate ICM tax
            ConcessionMinier concession = concessionMinierRepository.findById(concessionId).orElse(null);
            if (concession == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("CONCESSION_NOT_FOUND", "Concession non trouvée", "Aucune concession avec cet ID"));
            }
            double taxAmount = taxationService.calculateTaxForConcession(concession);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Calcul de l'impôt sur les concessions minières effectué avec succès",
                    "montant", taxAmount
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CALCULATION_ERROR", "Erreur lors du calcul de l'impôt sur les concessions minières", e.getMessage()));
        }
    }

    @GetMapping("/calculate/irl/property/{propertyId}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'CONTRIBUABLE','ADMIN')")
    public ResponseEntity<?> calculateIRLForProperty(@PathVariable UUID propertyId) {
        try {
            // Calculate IRL tax
            Propriete propriete = proprieteRepository.findById(propertyId).orElse(null);
            if (propriete == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("PROPERTY_NOT_FOUND", "Propriété non trouvée", "Aucune propriété avec cet ID"));
            }
            double taxAmount = taxationService.calculateIRL(propriete);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Calcul de l'impôt sur les revenus locatifs effectué avec succès",
                    "montant", taxAmount
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CALCULATION_ERROR", "Erreur lors du calcul de l'impôt sur les revenus locatifs", e.getMessage()));
        }
    }

    @GetMapping("/calculate/irv")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'CONTRIBUABLE','ADMIN')")
    public ResponseEntity<?> calculateIRV(
            @RequestParam double puissanceCV,
            @RequestParam double poids) {
        try {
            // Calculate IRV tax
            double taxAmount = taxationService.calculateIRV(puissanceCV, poids);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Calcul de l'impôt sur les revenus véhicules effectué avec succès",
                    "montant", taxAmount
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CALCULATION_ERROR", "Erreur lors du calcul de l'impôt sur les revenus véhicules", e.getMessage()));
        }
    }
}
