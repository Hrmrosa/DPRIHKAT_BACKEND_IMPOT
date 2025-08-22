package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Certificat;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.service.CertificatService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/certificats")
public class CertificatController {

    @Autowired
    private CertificatService certificatService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping("/property/{declarationId}")
    @PreAuthorize("hasAnyRole('AGENT_CERTIFICAT')")
    public ResponseEntity<?> generatePropertyCertificate(
            @PathVariable UUID declarationId,
            Authentication authentication) {
        try {
            // Get the authenticated user
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null || utilisateur.getAgent() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les agents peuvent générer des certificats"));
            }

            Certificat certificat = certificatService.generatePropertyCertificate(declarationId, utilisateur.getId());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "certificat", certificat,
                    "message", "Certificat généré avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CERTIFICATE_GENERATION_ERROR", "Erreur lors de la génération du certificat", e.getMessage()));
        }
    }

    @PostMapping("/vehicle/{vehiculeId}")
    @PreAuthorize("hasAnyRole('AGENT_CERTIFICAT')")
    public ResponseEntity<?> generateVehicleCertificate(
            @PathVariable UUID vehiculeId,
            Authentication authentication) {
        try {
            // Get the authenticated user
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null || utilisateur.getAgent() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les agents peuvent générer des certificats"));
            }

            Certificat certificat = certificatService.generateVehicleCertificate(vehiculeId, utilisateur.getId());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "certificat", certificat,
                    "message", "Certificat généré avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CERTIFICATE_GENERATION_ERROR", "Erreur lors de la génération du certificat", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT_CERTIFICAT', 'TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'CONTRIBUABLE')")
    public ResponseEntity<?> getCertificatById(@PathVariable UUID id) {
        try {
            Certificat certificat = certificatService.getCertificatById(id);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("certificat", certificat)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CERTIFICATE_FETCH_ERROR", "Erreur lors de la récupération du certificat", e.getMessage()));
        }
    }

    @GetMapping("/declaration/{declarationId}")
    @PreAuthorize("hasAnyRole('AGENT_CERTIFICAT', 'TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'CONTRIBUABLE')")
    public ResponseEntity<?> getCertificatsByDeclaration(
            @PathVariable UUID declarationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Certificat> certificats = certificatService.getCertificatsByDeclaration(declarationId);

            Map<String, Object> response = new HashMap<>();
            response.put("certificats", certificats);
            response.put("currentPage", page);
            response.put("totalItems", certificats.size());
            response.put("totalPages", (int) Math.ceil((double) certificats.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CERTIFICATE_FETCH_ERROR", "Erreur lors de la récupération des certificats", e.getMessage()));
        }
    }

    @GetMapping("/vehicle/{vehiculeId}")
    @PreAuthorize("hasAnyRole('AGENT_CERTIFICAT', 'TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'CONTRIBUABLE')")
    public ResponseEntity<?> getCertificatsByVehicle(
            @PathVariable UUID vehiculeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Certificat> certificats = certificatService.getCertificatsByVehicle(vehiculeId);

            Map<String, Object> response = new HashMap<>();
            response.put("certificats", certificats);
            response.put("currentPage", page);
            response.put("totalItems", certificats.size());
            response.put("totalPages", (int) Math.ceil((double) certificats.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CERTIFICATE_FETCH_ERROR", "Erreur lors de la récupération des certificats", e.getMessage()));
        }
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('AGENT_CERTIFICAT', 'TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR')")
    public ResponseEntity<?> getActiveCertificats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Certificat> certificats = certificatService.getActiveCertificats();

            Map<String, Object> response = new HashMap<>();
            response.put("certificats", certificats);
            response.put("currentPage", page);
            response.put("totalItems", certificats.size());
            response.put("totalPages", (int) Math.ceil((double) certificats.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CERTIFICATE_FETCH_ERROR", "Erreur lors de la récupération des certificats", e.getMessage()));
        }
    }

    @GetMapping("/expired")
    @PreAuthorize("hasAnyRole('AGENT_CERTIFICAT', 'TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR')")
    public ResponseEntity<?> getExpiredCertificats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Certificat> certificats = certificatService.getExpiredCertificats();

            Map<String, Object> response = new HashMap<>();
            response.put("certificats", certificats);
            response.put("currentPage", page);
            response.put("totalItems", certificats.size());
            response.put("totalPages", (int) Math.ceil((double) certificats.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CERTIFICATE_FETCH_ERROR", "Erreur lors de la récupération des certificats", e.getMessage()));
        }
    }
}
