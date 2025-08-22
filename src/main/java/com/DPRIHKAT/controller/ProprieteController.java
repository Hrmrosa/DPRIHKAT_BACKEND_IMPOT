package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
 import org.locationtech.jts.geom.Coordinate;
 import org.locationtech.jts.geom.GeometryFactory;
 import org.locationtech.jts.geom.Point;
 import org.locationtech.jts.geom.PrecisionModel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/proprietes")
public class ProprieteController {

    @Autowired
    private ProprieteRepository proprieteRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN', 'INFORMATICIEN')")
    public ResponseEntity<?> getAllProprietes() {
        try {
            List<Propriete> proprietes = proprieteRepository.findAll();
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("proprietes", proprietes)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETES_FETCH_ERROR", "Erreur lors de la récupération des propriétés", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN', 'INFORMATICIEN')")
    public ResponseEntity<?> getProprieteById(@PathVariable UUID id) {
        try {
            Propriete propriete = proprieteRepository.findById(id).orElse(null);
            if (propriete == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("PROPRIETE_NOT_FOUND", "Propriété non trouvée", "Aucune propriété avec l'ID fourni"));
            }
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("propriete", propriete)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETE_FETCH_ERROR", "Erreur lors de la récupération de la propriété", e.getMessage()));
        }
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('CONTRIBUABLE')")
    public ResponseEntity<?> getMyProprietes(Authentication authentication) {
        try {
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login).orElse(null);
            if (utilisateur == null || utilisateur.getContribuable() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les contribuables peuvent voir leurs propriétés"));
            }

            List<Propriete> proprietes = proprieteRepository.findByProprietaire_Id(utilisateur.getContribuable().getId());
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("proprietes", proprietes)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETES_MINE_ERROR", "Erreur lors de la récupération des propriétés de l'utilisateur", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/location")
    @PreAuthorize("hasRole('CONTROLLEUR')")
    public ResponseEntity<?> updateProprieteLocation(@PathVariable UUID id, @RequestBody Map<String, Object> payload) {
        try {
            Propriete propriete = proprieteRepository.findById(id).orElse(null);
            if (propriete == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("PROPRIETE_NOT_FOUND", "Propriété non trouvée", "Aucune propriété avec l'ID fourni"));
            }

            if (payload == null || !payload.containsKey("latitude") || !payload.containsKey("longitude")) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_PAYLOAD", "Coordonnées manquantes", "Veuillez fournir 'latitude' et 'longitude'"));
            }

            double latitude;
            double longitude;
            try {
                latitude = ((Number) payload.get("latitude")).doubleValue();
                longitude = ((Number) payload.get("longitude")).doubleValue();
            } catch (Exception ex) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_COORDINATES", "Coordonnées invalides", "'latitude' et 'longitude' doivent être numériques"));
            }

            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
            Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
            propriete.setLocation(point);
            proprieteRepository.save(propriete);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("propriete", propriete)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETE_LOCATION_UPDATE_ERROR", "Erreur lors de la mise à jour de la localisation", e.getMessage()));
        }
    }

    @GetMapping("/by-contribuable/{contribuableId}")
    @PreAuthorize("hasRole('CONTROLLEUR')")
    public ResponseEntity<?> getProprietesByContribuable(@PathVariable UUID contribuableId) {
        try {
            List<Propriete> proprietes = proprieteRepository.findByProprietaire_Id(contribuableId);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("proprietes", proprietes)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETES_BY_CONTRIBUABLE_ERROR", "Erreur lors de la récupération des propriétés du contribuable", e.getMessage()));
        }
    }
}
