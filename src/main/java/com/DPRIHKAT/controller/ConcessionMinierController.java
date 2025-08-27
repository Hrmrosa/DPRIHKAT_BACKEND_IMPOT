package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.ConcessionMinier;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.repository.ConcessionMinierRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/concessions")
public class ConcessionMinierController {

    @Autowired
    private ConcessionMinierRepository concessionMinierRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN', 'INFORMATICIEN','ADMIN')")
    public ResponseEntity<?> getAllConcessions() {
        try {
            List<ConcessionMinier> concessions = concessionMinierRepository.findAll();
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("concessions", concessions)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONCESSIONS_FETCH_ERROR", "Erreur lors de la récupération des concessions", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN', 'INFORMATICIEN','ADMIN')")
    public ResponseEntity<?> getConcessionById(@PathVariable UUID id) {
        try {
            ConcessionMinier concession = concessionMinierRepository.findById(id).orElse(null);
            if (concession == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("CONCESSION_NOT_FOUND", "Concession non trouvée", "Aucune concession avec l'ID fourni"));
            }
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("concession", concession)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONCESSION_FETCH_ERROR", "Erreur lors de la récupération de la concession", e.getMessage()));
        }
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('CONTRIBUABLE','ADMIN')")
    public ResponseEntity<?> getMyConcessions(Authentication authentication) {
        try {
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login).orElse(null);
            if (utilisateur == null || utilisateur.getContribuable() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les contribuables peuvent voir leurs concessions"));
            }

            List<ConcessionMinier> concessions = concessionMinierRepository.findByTitulaire_Id(utilisateur.getContribuable().getId());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("concessions", concessions)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONCESSIONS_MINE_ERROR", "Erreur lors de la récupération des concessions de l'utilisateur", e.getMessage()));
        }
    }
}
