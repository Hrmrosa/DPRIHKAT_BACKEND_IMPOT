package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.StatutDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeRedressement;
import com.DPRIHKAT.service.DocumentRecouvrementService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/documents-recouvrement")
public class DocumentRecouvrementController {

    @Autowired
    private DocumentRecouvrementService documentRecouvrementService;

    /**
     * Récupère tous les documents de recouvrement
     * @return Liste des documents de recouvrement
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTEUR', 'ROLE_INFORMATICIEN', 'ROLE_CONTROLLEUR', 'ROLE_RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> getAllDocuments(
            @RequestParam(required = false) TypeDocumentRecouvrement type,
            @RequestParam(required = false) StatutDocumentRecouvrement statut,
            @RequestParam(required = false) UUID contribuableId,
            @RequestParam(required = false) UUID dossierRecouvrementId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateGeneration") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<DocumentRecouvrement> documents = documentRecouvrementService.findWithFilters(
                    type, statut, contribuableId, dossierRecouvrementId, dateDebut, dateFin, pageable);
            
            Map<String, Object> data = new HashMap<>();
            data.put("totalItems", documents.getTotalElements());
            data.put("totalPages", documents.getTotalPages());
            data.put("currentPage", documents.getNumber());
            data.put("documents", documents.getContent());
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("data", data)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DOCUMENTS_FETCH_ERROR", "Erreur lors de la récupération des documents", e.getMessage()));
        }
    }

    /**
     * Récupère un document de recouvrement par son ID
     * @param id ID du document
     * @return Document de recouvrement
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTEUR', 'ROLE_INFORMATICIEN', 'ROLE_CONTROLLEUR', 'ROLE_RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> getDocumentById(@PathVariable UUID id) {
        try {
            DocumentRecouvrement document = documentRecouvrementService.findById(id);
            if (document == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("DOCUMENT_NOT_FOUND", "Document non trouvé", "Aucun document trouvé avec l'ID fourni"));
            }
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("document", document)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DOCUMENT_FETCH_ERROR", "Erreur lors de la récupération du document", e.getMessage()));
        }
    }

    /**
     * Récupère les documents de recouvrement par type
     * @param type Type de document
     * @return Liste des documents du type spécifié
     */
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTEUR', 'ROLE_INFORMATICIEN', 'ROLE_CONTROLLEUR', 'ROLE_RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> getDocumentsByType(@PathVariable TypeDocumentRecouvrement type) {
        try {
            List<DocumentRecouvrement> documents = documentRecouvrementService.findByType(type);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("documents", documents)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DOCUMENTS_FETCH_ERROR", "Erreur lors de la récupération des documents", e.getMessage()));
        }
    }

    /**
     * Récupère les documents de recouvrement par statut
     * @param statut Statut des documents
     * @return Liste des documents avec le statut spécifié
     */
    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTEUR', 'ROLE_INFORMATICIEN', 'ROLE_CONTROLLEUR', 'ROLE_RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> getDocumentsByStatut(@PathVariable StatutDocumentRecouvrement statut) {
        try {
            List<DocumentRecouvrement> documents = documentRecouvrementService.findByStatut(statut);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("documents", documents)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DOCUMENTS_FETCH_ERROR", "Erreur lors de la récupération des documents", e.getMessage()));
        }
    }

    /**
     * Récupère les documents de recouvrement pour un contribuable
     * @param contribuableId ID du contribuable
     * @return Liste des documents du contribuable
     */
    @GetMapping("/contribuable/{contribuableId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTEUR', 'ROLE_INFORMATICIEN', 'ROLE_CONTROLLEUR', 'ROLE_RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> getDocumentsByContribuable(@PathVariable UUID contribuableId) {
        try {
            List<DocumentRecouvrement> documents = documentRecouvrementService.findByContribuableId(contribuableId);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("documents", documents)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DOCUMENTS_FETCH_ERROR", "Erreur lors de la récupération des documents", e.getMessage()));
        }
    }

    /**
     * Récupère les documents de recouvrement pour un dossier
     * @param dossierRecouvrementId ID du dossier de recouvrement
     * @return Liste des documents du dossier
     */
    @GetMapping("/dossier/{dossierRecouvrementId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTEUR', 'ROLE_INFORMATICIEN', 'ROLE_CONTROLLEUR', 'ROLE_RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> getDocumentsByDossier(@PathVariable UUID dossierRecouvrementId) {
        try {
            List<DocumentRecouvrement> documents = documentRecouvrementService.findByDossierRecouvrementId(dossierRecouvrementId);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("documents", documents)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DOCUMENTS_FETCH_ERROR", "Erreur lors de la récupération des documents", e.getMessage()));
        }
    }

    /**
     * Récupère les documents en retard de paiement
     * @param type Type de document
     * @return Liste des documents en retard
     */
    @GetMapping("/en-retard/{type}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTEUR', 'ROLE_INFORMATICIEN', 'ROLE_CONTROLLEUR', 'ROLE_RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> getDocumentsEnRetard(@PathVariable TypeDocumentRecouvrement type) {
        try {
            List<DocumentRecouvrement> documents = documentRecouvrementService.findDocumentsEnRetard(type);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("documents", documents)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DOCUMENTS_FETCH_ERROR", "Erreur lors de la récupération des documents", e.getMessage()));
        }
    }

    /**
     * Crée un Avis de Mise en Recouvrement (AMR)
     * @param payload Données de l'AMR
     * @return AMR créé
     */
    @PostMapping("/amr")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTEUR', 'ROLE_INFORMATICIEN', 'ROLE_RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> creerAMR(@RequestBody Map<String, Object> payload) {
        try {
            UUID dossierRecouvrementId = UUID.fromString((String) payload.get("dossierRecouvrementId"));
            UUID contribuableId = UUID.fromString((String) payload.get("contribuableId"));
            UUID agentId = UUID.fromString((String) payload.get("agentId"));
            Double montantPrincipal = Double.valueOf(payload.get("montantPrincipal").toString());
            Double montantPenalites = Double.valueOf(payload.get("montantPenalites").toString());
            TypeRedressement typeRedressement = TypeRedressement.valueOf((String) payload.get("typeRedressement"));
            String baseImposable = (String) payload.get("baseImposable");
            
            UUID declarationId = null;
            if (payload.containsKey("declarationId") && payload.get("declarationId") != null) {
                declarationId = UUID.fromString((String) payload.get("declarationId"));
            }
            
            AvisMiseRecouvrement amr = documentRecouvrementService.creerAMR(
                    dossierRecouvrementId, contribuableId, agentId, montantPrincipal, 
                    montantPenalites, typeRedressement, baseImposable, declarationId);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "document", amr,
                    "message", "Avis de Mise en Recouvrement créé avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("AMR_CREATE_ERROR", "Erreur lors de la création de l'AMR", e.getMessage()));
        }
    }

    /**
     * Crée une Mise En Demeure de payer (MED)
     * @param payload Données de la MED
     * @return MED créée
     */
    @PostMapping("/med")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTEUR', 'ROLE_INFORMATICIEN', 'ROLE_RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> creerMED(@RequestBody Map<String, Object> payload) {
        try {
            UUID dossierRecouvrementId = UUID.fromString((String) payload.get("dossierRecouvrementId"));
            UUID contribuableId = UUID.fromString((String) payload.get("contribuableId"));
            UUID agentId = UUID.fromString((String) payload.get("agentId"));
            Double montantPrincipal = Double.valueOf(payload.get("montantPrincipal").toString());
            Double montantPenalites = Double.valueOf(payload.get("montantPenalites").toString());
            boolean paiementInsuffisant = (boolean) payload.get("paiementInsuffisant");
            Double montantPaye = payload.containsKey("montantPaye") ? 
                    Double.valueOf(payload.get("montantPaye").toString()) : 0.0;
            
            UUID declarationId = null;
            if (payload.containsKey("declarationId") && payload.get("declarationId") != null) {
                declarationId = UUID.fromString((String) payload.get("declarationId"));
            }
            
            MiseEnDemeure med = documentRecouvrementService.creerMED(
                    dossierRecouvrementId, contribuableId, agentId, montantPrincipal, 
                    montantPenalites, declarationId, paiementInsuffisant, montantPaye);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "document", med,
                    "message", "Mise En Demeure créée avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("MED_CREATE_ERROR", "Erreur lors de la création de la MED", e.getMessage()));
        }
    }

    /**
     * Crée une Contrainte Fiscale
     * @param payload Données de la contrainte
     * @return Contrainte créée
     */
    @PostMapping("/contrainte")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTEUR', 'ROLE_INFORMATICIEN', 'ROLE_RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> creerContrainte(@RequestBody Map<String, Object> payload) {
        try {
            UUID dossierRecouvrementId = UUID.fromString((String) payload.get("dossierRecouvrementId"));
            UUID contribuableId = UUID.fromString((String) payload.get("contribuableId"));
            UUID agentId = UUID.fromString((String) payload.get("agentId"));
            UUID receveurId = UUID.fromString((String) payload.get("receveurId"));
            Double montantPrincipal = Double.valueOf(payload.get("montantPrincipal").toString());
            Double montantPenalites = Double.valueOf(payload.get("montantPenalites").toString());
            
            ContrainteFiscale contrainte = documentRecouvrementService.creerContrainte(
                    dossierRecouvrementId, contribuableId, agentId, receveurId, 
                    montantPrincipal, montantPenalites);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "document", contrainte,
                    "message", "Contrainte fiscale créée avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("CONTRAINTE_CREATE_ERROR", "Erreur lors de la création de la contrainte", e.getMessage()));
        }
    }

    /**
     * Crée un Commandement de Payer
     * @param payload Données du commandement
     * @return Commandement créé
     */
    @PostMapping("/commandement")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTEUR', 'ROLE_INFORMATICIEN', 'ROLE_RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> creerCommandement(@RequestBody Map<String, Object> payload) {
        try {
            UUID dossierRecouvrementId = UUID.fromString((String) payload.get("dossierRecouvrementId"));
            UUID contribuableId = UUID.fromString((String) payload.get("contribuableId"));
            UUID agentId = UUID.fromString((String) payload.get("agentId"));
            UUID huissierId = UUID.fromString((String) payload.get("huissierId"));
            Double montantPrincipal = Double.valueOf(payload.get("montantPrincipal").toString());
            Double montantPenalites = Double.valueOf(payload.get("montantPenalites").toString());
            
            CommandementPayer commandement = documentRecouvrementService.creerCommandement(
                    dossierRecouvrementId, contribuableId, agentId, huissierId, 
                    montantPrincipal, montantPenalites);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "document", commandement,
                    "message", "Commandement de payer créé avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("COMMANDEMENT_CREATE_ERROR", "Erreur lors de la création du commandement", e.getMessage()));
        }
    }

    /**
     * Crée un Avis à Tiers Détenteur (ATD)
     * @param payload Données de l'ATD
     * @return ATD créé
     */
    @PostMapping("/atd")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTEUR', 'ROLE_INFORMATICIEN', 'ROLE_RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> creerATD(@RequestBody Map<String, Object> payload) {
        try {
            UUID dossierRecouvrementId = UUID.fromString((String) payload.get("dossierRecouvrementId"));
            UUID contribuableId = UUID.fromString((String) payload.get("contribuableId"));
            UUID agentId = UUID.fromString((String) payload.get("agentId"));
            Double montantPrincipal = Double.valueOf(payload.get("montantPrincipal").toString());
            Double montantPenalites = Double.valueOf(payload.get("montantPenalites").toString());
            String nomTiersDetenteur = (String) payload.get("nomTiersDetenteur");
            String adresseTiersDetenteur = (String) payload.get("adresseTiersDetenteur");
            String qualiteTiersDetenteur = (String) payload.get("qualiteTiersDetenteur");
            
            AvisTiersDetenteur atd = documentRecouvrementService.creerATD(
                    dossierRecouvrementId, contribuableId, agentId, montantPrincipal, 
                    montantPenalites, nomTiersDetenteur, adresseTiersDetenteur, qualiteTiersDetenteur);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "document", atd,
                    "message", "Avis à Tiers Détenteur créé avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("ATD_CREATE_ERROR", "Erreur lors de la création de l'ATD", e.getMessage()));
        }
    }

    /**
     * Met à jour le statut d'un document de recouvrement
     * @param id ID du document
     * @param payload Données de mise à jour
     * @return Document mis à jour
     */
    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTEUR', 'ROLE_INFORMATICIEN', 'ROLE_RECEVEUR_DES_IMPOTS')")
    public ResponseEntity<?> updateStatut(@PathVariable UUID id, @RequestBody Map<String, Object> payload) {
        try {
            StatutDocumentRecouvrement statut = StatutDocumentRecouvrement.valueOf((String) payload.get("statut"));
            
            DocumentRecouvrement document = documentRecouvrementService.updateStatut(id, statut);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "document", document,
                    "message", "Statut du document mis à jour avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("DOCUMENT_UPDATE_ERROR", "Erreur lors de la mise à jour du statut", e.getMessage()));
        }
    }
}
