package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Plaque;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.repository.PlaqueRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import com.DPRIHKAT.service.FileStorageService;
import com.DPRIHKAT.service.PlaqueService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Contrôleur pour la gestion des plaques d'immatriculation
 * 
 * <p>Ce contrôleur permet de :
 * <ul>
 *   <li>Créer, lire, mettre à jour et supprimer des plaques</li>
 *   <li>Gérer les associations entre plaques et véhicules</li>
 *   <li>Valider et vérifier les plaques</li>
 * </ul>
 * 
 * @author DPRIHKAT
 * @version 1.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/plaques")
public class PlaqueController {

    @Autowired
    private PlaqueService plaqueService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Assigner une plaque à un véhicule
     * 
     * @param vehiculeId L'ID du véhicule
     * @param document Le document de la plaque
     * @param authentication L'authentification de l'utilisateur
     * @return La plaque assignée avec succès
     */
    @PostMapping("/assign/{vehiculeId}")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES','ADMIN')")
    public ResponseEntity<?> assignPlaqueToVehicle(
            @PathVariable UUID vehiculeId,
            @RequestParam("document") MultipartFile document,
            Authentication authentication) {
        try {
            // Get the authenticated user
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null || utilisateur.getAgent() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_USER", "Utilisateur non valide", "Seuls les agents peuvent assigner des plaques"));
            }

            // Stocker le fichier et obtenir son nom
            String fileName = fileStorageService.storeFile(document);

            Plaque plaque = plaqueService.assignPlaqueToVehicle(vehiculeId, utilisateur.getId());
            
            // Mettre à jour le champ document avec le nom du fichier
            plaque.setDocument(fileName);
            plaqueService.save(plaque);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "plaque", plaque,
                    "message", "Plaque assignée avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_ASSIGNMENT_ERROR", "Erreur lors de l'assignation de la plaque", e.getMessage()));
        }
    }

    /**
     * Récupérer une plaque par son ID
     * 
     * @param id L'ID de la plaque
     * @return La plaque correspondante
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR','ADMIN')")
    public ResponseEntity<?> getPlaqueById(@PathVariable UUID id) {
        try {
            Plaque plaque = plaqueService.getPlaqueById(id);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("plaque", plaque)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_FETCH_ERROR", "Erreur lors de la récupération de la plaque", e.getMessage()));
        }
    }

    /**
     * Récupérer toutes les plaques
     * 
     * @param page La page actuelle
     * @param size La taille de la page
     * @param disponible La disponibilité des plaques
     * @return Liste de toutes les plaques
     */
    @GetMapping("")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR','ADMIN')")
    public ResponseEntity<?> getAllPlaques(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean disponible) {
        return plaqueService.getAllPlaques(page, size, disponible);
    }

    /**
     * Récupérer les plaques d'un véhicule
     * 
     * @param vehiculeId L'ID du véhicule
     * @param page La page actuelle
     * @param size La taille de la page
     * @return Liste des plaques du véhicule
     */
    @GetMapping("/vehicule/{vehiculeId}")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'CONTRIBUABLE','ADMIN')")
    public ResponseEntity<?> getPlaquesByVehicle(
            @PathVariable UUID vehiculeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Plaque> plaques = plaqueService.getPlaquesByVehicle(vehiculeId);

            Map<String, Object> response = new HashMap<>();
            response.put("plaques", plaques);
            response.put("currentPage", page);
            response.put("totalItems", plaques.size());
            response.put("totalPages", (int) Math.ceil((double) plaques.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_FETCH_ERROR", "Erreur lors de la récupération des plaques", e.getMessage()));
        }
    }

    /**
     * Vérifie si une plaque est disponible en stock
     * 
     * @return Un objet contenant :
     *   - available (boolean): true si des plaques sont disponibles
     *   - message (String): message descriptif
     */
    @GetMapping("/stock")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR','ADMIN')")
    public ResponseEntity<?> checkPlaqueStock() {
        try {
            boolean available = plaqueService.isPlaqueAvailable();

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "available", available,
                    "message", available ? "Plaques disponibles en stock" : "Aucune plaque disponible en stock"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_STOCK_ERROR", "Erreur lors de la vérification du stock", e.getMessage()));
        }
    }

    /**
     * Récupère les plaques disponibles (non attribuées)
     * 
     * @return Liste des plaques disponibles
     */
    @GetMapping("/available")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR','ADMIN')")
    public ResponseEntity<?> getAvailablePlaques() {
        try {
            List<Plaque> plaques = plaqueService.getAvailablePlaques();
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("plaques", plaques)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_FETCH_ERROR", "Erreur lors de la récupération des plaques disponibles", e.getMessage()));
        }
    }

    /**
     * Récupère les plaques attribuées
     * 
     * @return Liste des plaques attribuées
     */
    @GetMapping("/assigned")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR','ADMIN')")
    public ResponseEntity<?> getAssignedPlaques() {
        try {
            List<Plaque> plaques = plaqueService.getAssignedPlaques();
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("plaques", plaques)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_FETCH_ERROR", "Erreur lors de la récupération des plaques attribuées", e.getMessage()));
        }
    }

    /**
     * Télécharger un fichier
     * 
     * @param fileName Le nom du fichier
     * @return Le fichier téléchargé
     */
    @GetMapping("/download/{fileName:.+}")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'CONTRIBUABLE','ADMIN')")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            // Charger le fichier en tant que ressource
            Resource resource = fileStorageService.loadFileAsResource(fileName);

            // Déterminer le type de contenu
            String contentType = "application/octet-stream";
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(null);
        }
    }

    /**
     * Crée une plaque directement associée à un véhicule
     * 
     * @param vehiculeId L'ID du véhicule
     * @param numeroPlaque Le numéro de la plaque
     * @param authentication L'objet d'authentification Spring Security
     * @return La plaque créée avec son véhicule associé
     * @throws RuntimeException Si l'utilisateur n'est pas trouvé
     */
    @PostMapping("/creer-directement")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'ADMIN')")
    public ResponseEntity<?> creerPlaqueDirectement(
            @RequestParam UUID vehiculeId,
            @RequestParam String numeroPlaque,
            Authentication authentication) {
        try {
            // Récupérer l'utilisateur authentifié
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            Plaque plaque = plaqueService.creerPlaqueDirectement(
                vehiculeId, 
                numeroPlaque,
                utilisateur.getId()
            );
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                "plaque", plaque,
                "message", "Plaque créée et associée au véhicule avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_CREATION_ERROR", "Erreur lors de la création directe de la plaque", e.getMessage()));
        }
    }

    /**
     * Importe un fichier de plaques
     * 
     * @param file Le fichier à importer (format CSV ou Excel)
     * @return Résultat de l'importation avec statistiques
     */
    @PostMapping("/import")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN')")
    public ResponseEntity<?> importPlaques(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = plaqueService.importPlaques(file);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(result));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_IMPORT_ERROR", "Erreur lors de l'importation des plaques", e.getMessage()));
        }
    }

    /**
     * Exporte les plaques dans un fichier
     * 
     * @return Fichier contenant toutes les plaques
     */
    @GetMapping("/export")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN')")
    public ResponseEntity<?> exportPlaques() {
        try {
            Resource resource = plaqueService.exportPlaques();
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"plaques_export.xlsx\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_EXPORT_ERROR", "Erreur lors de l'exportation des plaques", e.getMessage()));
        }
    }

    /**
     * Met à jour une plaque existante
     * 
     * @param id L'ID de la plaque à mettre à jour
     * @param plaque Les nouvelles données de la plaque
     * @return La plaque mise à jour
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<?> updatePlaque(@PathVariable UUID id, @RequestBody Plaque plaque) {
        return plaqueService.updatePlaque(id, plaque);
    }

    /**
     * Supprime une plaque
     * 
     * @param id L'ID de la plaque à supprimer
     * @return Confirmation de la suppression
     */
    /**
     * Valide une plaque d'immatriculation
     * 
     * @param numeroPlaque Le numéro de plaque à valider
     * @return True si la plaque est valide, false sinon
     */
    @GetMapping("/validate/{numeroPlaque}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<?> validatePlaque(@PathVariable String numeroPlaque) {
        return plaqueService.validatePlaque(numeroPlaque);
    }

    /**
     * Vérifie si une plaque existe déjà
     * 
     * @param numeroPlaque Le numéro de plaque à vérifier
     * @return True si la plaque existe, false sinon
     */
    @GetMapping("/exists/{numeroPlaque}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<?> plaqueExists(@PathVariable String numeroPlaque) {
        return plaqueService.plaqueExists(numeroPlaque);
    }

    /**
     * Associe une plaque à un véhicule
     * 
     * @param plaqueId L'ID de la plaque
     * @param vehiculeId L'ID du véhicule
     * @return Confirmation de l'association
     */
    @PostMapping("/{plaqueId}/vehicules/{vehiculeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<?> assignPlaqueToVehicule(@PathVariable UUID plaqueId, @PathVariable UUID vehiculeId) {
        return plaqueService.assignPlaqueToVehicule(plaqueId, vehiculeId);
    }

    /**
     * Dissocie une plaque d'un véhicule
     * 
     * @param plaqueId L'ID de la plaque
     * @return Confirmation de la dissociation
     */
    @DeleteMapping("/{plaqueId}/vehicules")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<?> unassignPlaqueFromVehicule(@PathVariable UUID plaqueId) {
        return plaqueService.unassignPlaqueFromVehicule(plaqueId);
    }

    /**
     * Récupère le véhicule associé à une plaque
     * 
     * @param plaqueId L'ID de la plaque
     * @return Le véhicule associé
     */
    @GetMapping("/{plaqueId}/vehicules")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<?> getVehiculeByPlaque(@PathVariable UUID plaqueId) {
        return plaqueService.getVehiculeByPlaque(plaqueId);
    }
    
    /**
     * Marquer une plaque comme livrée
     * 
     * @param id L'ID de la plaque
     * @return La plaque mise à jour
     */
    @PutMapping("/{id}/livrer")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'ADMIN')")
    public ResponseEntity<?> livrerPlaque(@PathVariable UUID id) {
        try {
            Plaque plaque = plaqueService.livrerPlaque(id);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "plaque", plaque,
                    "message", "Plaque marquée comme livrée avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_LIVRAISON_ERROR", 
                            "Erreur lors de la livraison de la plaque", 
                            e.getMessage()));
        }
    }
    
    /**
     * Libérer une plaque (la remettre en stock)
     * 
     * @param id L'ID de la plaque
     * @return La plaque mise à jour
     */
    @PutMapping("/{id}/liberer")
    @PreAuthorize("hasAnyRole('AGENT_DE_PLAQUES', 'ADMIN')")
    public ResponseEntity<?> libererPlaque(@PathVariable UUID id) {
        try {
            Plaque plaque = plaqueService.libererPlaque(id);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "plaque", plaque,
                    "message", "Plaque libérée avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PLAQUE_LIBERATION_ERROR", 
                            "Erreur lors de la libération de la plaque", 
                            e.getMessage()));
        }
    }
}
