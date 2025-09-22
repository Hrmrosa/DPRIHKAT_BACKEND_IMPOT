package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.ProprieteCreationDTO;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.service.ProprieteService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/proprietes")
public class ProprieteController {

    private static final Logger logger = LoggerFactory.getLogger(ProprieteController.class);

    @Autowired
    private ProprieteRepository proprieteRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ProprieteService proprieteService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_TAXATEUR', 'ROLE_RECEVEUR_DES_IMPOTS', 'ROLE_CHEF_DE_BUREAU', 'ROLE_CHEF_DE_DIVISION', 'ROLE_DIRECTEUR', 'ROLE_ADMIN', 'ROLE_INFORMATICIEN')")
    public ResponseEntity<?> getAllProprietes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Propriete> pageProprietes = proprieteRepository.findAll(pageable);
            
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", pageProprietes.getTotalElements());
            data.put("totalPages", pageProprietes.getTotalPages());
            data.put("currentPage", pageProprietes.getNumber());
            data.put("proprietes", pageProprietes.getContent());
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETES_FETCH_ERROR", "Erreur lors de la récupération des propriétés", e.getMessage()));
        }
    }
    
    @GetMapping("/paginated")
    @PreAuthorize("hasAnyRole('ROLE_TAXATEUR', 'ROLE_RECEVEUR_DES_IMPOTS', 'ROLE_CHEF_DE_BUREAU', 'ROLE_CHEF_DE_DIVISION', 'ROLE_DIRECTEUR', 'ROLE_ADMIN', 'ROLE_INFORMATICIEN')")
    public ResponseEntity<?> getAllProprietesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Propriete> proprietes = proprieteRepository.findAll(pageable);
            
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", proprietes.getTotalElements());
            data.put("totalPages", proprietes.getTotalPages());
            data.put("currentPage", proprietes.getNumber());
            data.put("proprietes", proprietes.getContent());
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETES_FETCH_ERROR", "Erreur lors de la récupération des propriétés paginées", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_TAXATEUR', 'ROLE_RECEVEUR_DES_IMPOTS', 'ROLE_CHEF_DE_BUREAU', 'ROLE_CHEF_DE_DIVISION', 'ROLE_DIRECTEUR', 'ROLE_ADMIN', 'ROLE_INFORMATICIEN')")
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
    @PreAuthorize("hasRole('ROLE_CONTRIBUABLE')")
    public ResponseEntity<?> getMyProprietes(Authentication authentication) {
        try {
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login).orElse(null);
            if (utilisateur == null || !utilisateur.isContribuable()) {
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
    
    @GetMapping("/mine/paginated")
    @PreAuthorize("hasRole('ROLE_CONTRIBUABLE')")
    public ResponseEntity<?> getMyProprietesPaginated(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login).orElse(null);
            if (utilisateur == null || utilisateur.getContribuable() == null || !utilisateur.isContribuable()) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les contribuables peuvent voir leurs propriétés"));
            }

            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Propriete> proprietes = proprieteRepository.findByProprietaire_Id(utilisateur.getContribuable().getId(), pageable);
            
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", proprietes.getTotalElements());
            data.put("totalPages", proprietes.getTotalPages());
            data.put("currentPage", proprietes.getNumber());
            data.put("proprietes", proprietes.getContent());
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETES_MINE_ERROR", "Erreur lors de la récupération des propriétés paginées de l'utilisateur", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/location")
    @PreAuthorize("hasRole('ROLE_CONTROLLEUR')")
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
    @PreAuthorize("hasRole('ROLE_CONTROLLEUR')")
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
    
    @GetMapping("/mine/with-tax-types")
    @PreAuthorize("hasRole('ROLE_CONTRIBUABLE')")
    public ResponseEntity<?> getMyProprietesWithTaxTypes(Authentication authentication) {
        try {
            logger.info("Début de la méthode getMyProprietesWithTaxTypes");
            String login = authentication.getName();
            logger.info("Utilisateur authentifié: {}", login);
            
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login).orElse(null);
            if (utilisateur == null) {
                logger.error("Utilisateur non trouvé: {}", login);
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Utilisateur non trouvé"));
            }
            
            logger.info("Utilisateur trouvé: {}, rôle: {}", utilisateur.getId(), utilisateur.getRole());
            logger.info("Contribuable associé: {}", utilisateur.getContribuable() != null ? utilisateur.getContribuable().getId() : "null");
            logger.info("Agent associé: {}", utilisateur.getAgent() != null ? utilisateur.getAgent().getId() : "null");
            logger.info("isContribuable(): {}", utilisateur.isContribuable());
            
            if (!utilisateur.isContribuable()) {
                logger.error("L'utilisateur n'est pas un contribuable valide: {}", login);
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les contribuables peuvent voir leurs propriétés"));
            }

            List<Propriete> proprietes = proprieteRepository.findByProprietaire_Id(utilisateur.getContribuable().getId());
            logger.info("Nombre de propriétés trouvées: {}", proprietes.size());
            
            // Ajout des types d'impôts applicables pour chaque propriété
            List<Map<String, Object>> proprietesAvecImpots = proprietes.stream().map(propriete -> {
                Map<String, Object> proprieteMap = new HashMap<>();
                proprieteMap.put("propriete", propriete);
                
                // Déterminer les types d'impôts applicables
                // Pour les propriétés, l'impôt foncier (IF) est toujours applicable
                // On peut également avoir IRL (Impôt sur les Revenus Locatifs) ou RL (Redevance Locative)
                // en fonction du type de propriété et d'autres facteurs
                
                // Par défaut, l'impôt foncier est applicable
                proprieteMap.put("impotsApplicables", List.of(
                    Map.of(
                        "code", "IF",
                        "libelle", "Impôt Foncier",
                        "description", "Impôt sur les propriétés immobilières"
                    )
                ));
                
                return proprieteMap;
            }).collect(Collectors.toList());
            
            logger.info("Fin de la méthode getMyProprietesWithTaxTypes avec succès");
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("proprietes", proprietesAvecImpots)));
        } catch (Exception e) {
            logger.error("Erreur dans getMyProprietesWithTaxTypes", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETES_MINE_ERROR", "Erreur lors de la récupération des propriétés de l'utilisateur", e.getMessage()));
        }
    }
    
    @GetMapping("/debug/with-tax-types")
    public ResponseEntity<?> getDebugProprietesWithTaxTypes(Authentication authentication) {
        try {
            logger.info("Début de la méthode getDebugProprietesWithTaxTypes");
            String login = authentication.getName();
            logger.info("Utilisateur authentifié: {}", login);
            
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login).orElse(null);
            if (utilisateur == null) {
                logger.error("Utilisateur non trouvé: {}", login);
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Utilisateur non trouvé"));
            }
            
            logger.info("Utilisateur trouvé: {}, rôle: {}", utilisateur.getId(), utilisateur.getRole());
            logger.info("Contribuable associé: {}", utilisateur.getContribuable() != null ? utilisateur.getContribuable().getId() : "null");
            logger.info("Agent associé: {}", utilisateur.getAgent() != null ? utilisateur.getAgent().getId() : "null");
            
            // Si l'utilisateur a un contribuable associé, utiliser ce contribuable
            UUID contribuableId = null;
            if (utilisateur.getContribuable() != null) {
                contribuableId = utilisateur.getContribuable().getId();
                logger.info("Utilisation du contribuable associé: {}", contribuableId);
            } 
            // Sinon, si l'utilisateur a un agent qui est en fait un contribuable, utiliser cet agent comme contribuable
            else if (utilisateur.getAgent() != null) {
                contribuableId = utilisateur.getAgent().getId();
                logger.info("Utilisation de l'agent comme contribuable: {}", contribuableId);
            } else {
                logger.error("Aucun contribuable ou agent associé à l'utilisateur: {}", login);
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Aucun contribuable associé à cet utilisateur"));
            }

            List<Propriete> proprietes = proprieteRepository.findByProprietaire_Id(contribuableId);
            logger.info("Nombre de propriétés trouvées: {}", proprietes.size());
            
            // Ajout des types d'impôts applicables pour chaque propriété
            List<Map<String, Object>> proprietesAvecImpots = proprietes.stream().map(propriete -> {
                Map<String, Object> proprieteMap = new HashMap<>();
                proprieteMap.put("propriete", propriete);
                
                // Par défaut, l'impôt foncier est applicable
                proprieteMap.put("impotsApplicables", List.of(
                    Map.of(
                        "code", "IF",
                        "libelle", "Impôt Foncier",
                        "description", "Impôt sur les propriétés immobilières"
                    )
                ));
                
                return proprieteMap;
            }).collect(Collectors.toList());
            
            logger.info("Fin de la méthode getDebugProprietesWithTaxTypes avec succès");
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("proprietes", proprietesAvecImpots)));
        } catch (Exception e) {
            logger.error("Erreur dans getDebugProprietesWithTaxTypes", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETES_DEBUG_ERROR", "Erreur lors de la récupération des propriétés de l'utilisateur", e.getMessage()));
        }
    }

    /**
     * Récupère toutes les propriétés d'un contribuable spécifique par son ID
     * Accessible à tous les rôles
     * 
     * @param contribuableId ID du contribuable
     * @param page numéro de page (commence à 0)
     * @param size nombre d'éléments par page
     * @param sortBy champ de tri
     * @param sortDir direction du tri (asc/desc)
     * @return liste paginée des propriétés du contribuable
     */
    @GetMapping("/contribuable/{contribuableId}")
    @PreAuthorize("hasAnyRole('ROLE_TAXATEUR', 'ROLE_RECEVEUR_DES_IMPOTS', 'ROLE_CHEF_DE_BUREAU', 'ROLE_CHEF_DE_DIVISION', 'ROLE_DIRECTEUR', 'ROLE_ADMIN', 'ROLE_INFORMATICIEN', 'ROLE_CONTRIBUABLE')")
    public ResponseEntity<?> getProprietesByContribuableId(
            @PathVariable UUID contribuableId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            logger.info("Récupération des propriétés du contribuable avec ID: {}", contribuableId);
            
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Propriete> proprietes = proprieteRepository.findByProprietaire_Id(contribuableId, pageable);
            
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", proprietes.getTotalElements());
            data.put("totalPages", proprietes.getTotalPages());
            data.put("currentPage", proprietes.getNumber());
            data.put("proprietes", proprietes.getContent());
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des propriétés du contribuable avec ID: {}", contribuableId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETES_FETCH_ERROR", 
                            "Erreur lors de la récupération des propriétés du contribuable", 
                            e.getMessage()));
        }
    }
    
    /**
     * Récupère toutes les propriétés d'un contribuable spécifique par son ID (sans pagination)
     * Accessible à tous les rôles
     * 
     * @param contribuableId ID du contribuable
     * @return liste des propriétés du contribuable
     */
    @GetMapping("/contribuable/{contribuableId}/all")
    @PreAuthorize("hasAnyRole('ROLE_TAXATEUR', 'ROLE_RECEVEUR_DES_IMPOTS', 'ROLE_CHEF_DE_BUREAU', 'ROLE_CHEF_DE_DIVISION', 'ROLE_DIRECTEUR', 'ROLE_ADMIN', 'ROLE_INFORMATICIEN', 'ROLE_CONTRIBUABLE')")
    public ResponseEntity<?> getAllProprietesByContribuableId(@PathVariable UUID contribuableId) {
        try {
            logger.info("Récupération de toutes les propriétés du contribuable avec ID: {}", contribuableId);
            
            List<Propriete> proprietes = proprieteRepository.findByProprietaire_Id(contribuableId);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("proprietes", proprietes)));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de toutes les propriétés du contribuable avec ID: {}", contribuableId, e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETES_FETCH_ERROR", 
                            "Erreur lors de la récupération de toutes les propriétés du contribuable", 
                            e.getMessage()));
        }
    }

    /**
     * Crée une nouvelle propriété avec les natures d'impôt associées
     * 
     * @param proprieteDTO DTO contenant les informations de la propriété et les IDs des natures d'impôt
     * @return La propriété créée
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_TAXATEUR', 'ROLE_CHEF_DE_BUREAU', 'ROLE_ADMIN')")
    public ResponseEntity<?> createPropriete(@RequestBody ProprieteCreationDTO proprieteDTO) {
        try {
            logger.info("Tentative de création d'une propriété - DTO reçu: {}", proprieteDTO);
            
            // Log des informations d'authentification
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                logger.info("Utilisateur authentifié: {}", auth.getName());
                logger.info("Rôles: {}", auth.getAuthorities());
            } else {
                logger.warn("Aucune authentification détectée");
            }
            
            Propriete propriete = proprieteService.createPropriete(proprieteDTO);
            logger.info("Propriété créée avec succès: {}", propriete.getId());
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "propriete", propriete, 
                    "message", "Propriété créée avec succès"
            )));
        } catch (IllegalArgumentException e) {
            logger.error("Erreur de validation: {}", e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("INVALID_DATA", "Données invalides", e.getMessage()));
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la propriété", e);
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PROPRIETE_CREATE_ERROR", "Erreur lors de la création de la propriété", e.getMessage()));
        }
    }
}
