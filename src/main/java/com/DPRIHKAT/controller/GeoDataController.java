package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.ContribuableSimpleDTO;
import com.DPRIHKAT.dto.ProprieteGeoDTO;
import com.DPRIHKAT.dto.CollecteContribuableRequest;
import com.DPRIHKAT.dto.BiensPaginationDTO;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.enums.TypeContribuable;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.repository.ContribuableRepository;
import com.DPRIHKAT.util.ResponseUtil;
import org.locationtech.jts.geom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.UUID;

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

    @Autowired 
    private ContribuableRepository contribuableRepository;

    /**
     * Récupère toutes les propriétés avec leurs coordonnées géographiques et les informations
     * sur leurs propriétaires pour l'affichage sur une carte
     * 
     * @param page Numéro de page
     * @param size Nombre d'éléments par page
     * @return Liste des propriétés avec leurs coordonnées géographiques
     */
    @GetMapping("/proprietes")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION')")
    public ResponseEntity<?> getAllProprietesWithGeoData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Récupération de toutes les propriétés avec données géographiques");
            
            Pageable pageable = PageRequest.of(page, size);
            Page<Propriete> pageProprietes = proprieteRepository.findAll(pageable);
            
            // Conversion et traitement géographique
            Page<ProprieteGeoDTO> result = pageProprietes.map(this::convertToGeoDTO);
            
            logger.debug("Nombre de propriétés avec coordonnées géographiques: {}", result.getSize());
            
            return ResponseEntity.ok(ResponseUtil.createPaginatedResponse(result, "proprietes"));
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
     * Crée un contribuable avec ses biens
     * 
     * @param request Requête de collecte
     * @return Réponse de la collecte
     */
    @PostMapping("/collecte")
    @PreAuthorize("hasAnyRole('CONTROLLEUR','ADMIN','INFORMATICIEN')")
    public ResponseEntity<?> creerContribuableAvecBiens(@RequestBody CollecteContribuableRequest request) {
        // Implémentation existante de la collecte
        try {
            Contribuable c = new Contribuable();
            
            if (request.getType() != null) {
                try {
                    // Conversion explicite du String vers l'enum TypeContribuable
                    TypeContribuable typeContribuable = TypeContribuable.valueOf(request.getType().toUpperCase());
                    c.setType(typeContribuable);
                } catch (IllegalArgumentException e) {
                    logger.error("Type de contribuable invalide: " + request.getType());
                    return ResponseEntity.badRequest().body(ResponseUtil.createErrorResponse(
                            "INVALID_CONTRIBUABLE_TYPE",
                            "Type de contribuable non valide",
                            "Types valides: " + Arrays.toString(TypeContribuable.values())));
                }
            }
            
            // TODO: Implémenter la logique de collecte
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "message", "Collecte réussie"
            )));
        } catch (Exception e) {
            logger.error("Erreur lors de la collecte", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("COLLECTE_ERROR", 
                            "Erreur lors de la collecte", 
                            e.getMessage()));
        }
    }

    /**
     * Récupère les biens paginés pour un propriétaire
     * 
     * @param proprietaireId Identifiant du propriétaire
     * @param page Numéro de page
     * @param size Nombre d'éléments par page
     * @return Liste des biens paginés
     */
    @GetMapping("/proprietes/{proprietaireId}/biens")
    public ResponseEntity<BiensPaginationDTO> getBiensPagines(
            @PathVariable UUID proprietaireId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Propriete> pageBiens = proprieteRepository.findByProprietaire_Id(proprietaireId, pageable);
        
        BiensPaginationDTO response = new BiensPaginationDTO();
        response.setCurrentPage(pageBiens.getNumber());
        response.setTotalPages(pageBiens.getTotalPages());
        response.setTotalItems(pageBiens.getTotalElements());
        response.setBiens(pageBiens.getContent());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Convertit une entité Propriete en ProprieteGeoDTO
     * 
     * @param propriete L'entité Propriete à convertir
     * @return Le DTO ProprieteGeoDTO correspondant
     */
    private ProprieteGeoDTO convertToGeoDTO(Propriete propriete) {
        ContribuableSimpleDTO proprietaireDTO = null;
        
        if (propriete.getProprietaire() != null) {
            proprietaireDTO = new ContribuableSimpleDTO();
            proprietaireDTO.setId(propriete.getProprietaire().getId());
            proprietaireDTO.setNom(propriete.getProprietaire().getNom());
            proprietaireDTO.setAdressePrincipale(propriete.getProprietaire().getAdressePrincipale());
            proprietaireDTO.setAdresseSecondaire(propriete.getProprietaire().getAdresseSecondaire());
            proprietaireDTO.setTelephonePrincipal(propriete.getProprietaire().getTelephonePrincipal());
            proprietaireDTO.setTelephoneSecondaire(propriete.getProprietaire().getTelephoneSecondaire());
            proprietaireDTO.setEmail(propriete.getProprietaire().getEmail());
            proprietaireDTO.setType(propriete.getProprietaire().getType());
            proprietaireDTO.setIdNat(propriete.getProprietaire().getIdNat());
            proprietaireDTO.setNrc(propriete.getProprietaire().getNRC());
            proprietaireDTO.setSigle(propriete.getProprietaire().getSigle());
            
            // Pagination des biens
            Pageable biensPageable = PageRequest.of(0, 10); // Valeurs par défaut
            Page<Propriete> pageBiens = proprieteRepository.findByProprietaire_Id(propriete.getProprietaire().getId(), biensPageable);
            
            BiensPaginationDTO biensPagination = new BiensPaginationDTO();
            biensPagination.setCurrentPage(pageBiens.getNumber());
            biensPagination.setTotalPages(pageBiens.getTotalPages());
            biensPagination.setTotalItems(pageBiens.getTotalElements());
            biensPagination.setBiens(pageBiens.getContent());
            
            proprietaireDTO.setBiens(biensPagination);
        }
        
        return new ProprieteGeoDTO(
            propriete.getId(),
            propriete.getAdresse(),
            propriete.getLocalite(),
            propriete.getType().toString(),
            propriete.getSuperficie(),
            propriete.getLatitude(),
            propriete.getLongitude(),
            proprietaireDTO
        );
    }
}
