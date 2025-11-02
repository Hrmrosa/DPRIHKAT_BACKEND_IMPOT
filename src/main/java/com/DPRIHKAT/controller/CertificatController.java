package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.response.ApiResponse;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.service.CertificatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/certificats")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CertificatController {

    @Autowired
    private CertificatService certificatService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping("/emettre")
    @PreAuthorize("hasRole('RECEVEUR_DES_IMPOTS') or hasRole('APUREUR')")
    public ResponseEntity<?> emettreCertificat(Principal principal) {
        try {
            Utilisateur agent = utilisateurRepository.findByLogin(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Agent non trouvé"));

            if (!agent.getRole().equals(Role.RECEVEUR_DES_IMPOTS) && !agent.getRole().equals(Role.APUREUR)) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Seuls les receveurs des impôts et les apureurs peuvent émettre des certificats"));
            }

            String certificat = certificatService.emettreCertificat(agent);
            return ResponseEntity.ok(new ApiResponse(true, "Certificat émis avec succès", certificat));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Erreur lors de l'émission du certificat: " + e.getMessage()));
        }
    }

    @GetMapping("/verifier/{numeroCertificat}")
    public ResponseEntity<?> verifierCertificat(@PathVariable String numeroCertificat) {
        try {
            boolean estValide = certificatService.verifierCertificat(numeroCertificat);
            return ResponseEntity.ok(new ApiResponse(true, "Vérification effectuée", estValide));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Erreur lors de la vérification du certificat: " + e.getMessage()));
        }
    }
}
