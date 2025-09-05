package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.ContribuableSimpleDTO;
import com.DPRIHKAT.dto.ProprieteGeoDTO;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Contrôleur pour fournir des données géographiques pour l'affichage sur une carte
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/geo")
public class GeoDataController {

    private static final Logger logger = LoggerFactory.getLogger(GeoDataController.class);

    @Autowired
    private ProprieteRepository proprieteRepository;

    /**
     * Récupère toutes les propriétés avec leurs coordonnées géographiques et les informations
     * sur leurs propriétaires pour l'affichage sur une carte
     * 
     * @return Liste des propriétés avec leurs coordonnées géographiques
     */
    @GetMapping("/proprietes")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION')")
    public ResponseEntity<?> getAllProprietesWithGeoData() {
        try {
            logger.info("Récupération de toutes les propriétés avec données géographiques");
            
            List<Propriete> proprietes = proprieteRepository.findAll();
            
            // Filtrer les propriétés qui ont des coordonnées géographiques
            List<ProprieteGeoDTO> proprietesGeo = proprietes.stream()
                .filter(p -> p.getLatitude() != null && p.getLongitude() != null)
                .map(this::mapToProprieteGeoDTO)
                .collect(Collectors.toList());
            
            logger.debug("Nombre de propriétés avec coordonnées géographiques: {}", proprietesGeo.size());
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "proprietes", proprietesGeo
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des propriétés avec données géographiques", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("GEO_DATA_FETCH_ERROR", 
                            "Erreur lors de la récupération des données géographiques", 
                            e.getMessage()));
        }
    }
    
    /**
     * Convertit une entité Propriete en ProprieteGeoDTO
     * 
     * @param propriete L'entité Propriete à convertir
     * @return Le DTO ProprieteGeoDTO correspondant
     */
    private ProprieteGeoDTO mapToProprieteGeoDTO(Propriete propriete) {
        ContribuableSimpleDTO proprietaireDTO = null;
        
        if (propriete.getProprietaire() != null) {
            proprietaireDTO = new ContribuableSimpleDTO(
                propriete.getProprietaire().getId(),
                propriete.getProprietaire().getNom(),
                propriete.getProprietaire().getAdressePrincipale(),
                propriete.getProprietaire().getTelephonePrincipal(),
                propriete.getProprietaire().getEmail()
            );
        }
        
        return new ProprieteGeoDTO(
            propriete.getId(),
            propriete.getAdresse(),
            propriete.getLocalite(),
            propriete.getType().toString(), // Convertir TypePropriete en String
            propriete.getSuperficie(),
            propriete.getLatitude(),
            propriete.getLongitude(),
            proprietaireDTO
        );
    }
}
