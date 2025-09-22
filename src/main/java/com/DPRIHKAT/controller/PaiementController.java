package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.ApurementDTO;
import com.DPRIHKAT.dto.PaiementResponseDTO;
import com.DPRIHKAT.entity.Apurement;
import com.DPRIHKAT.entity.Paiement;
import com.DPRIHKAT.entity.Taxation;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import com.DPRIHKAT.repository.ApurementRepository;
import com.DPRIHKAT.service.PaiementService;
import com.DPRIHKAT.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/paiements")
public class PaiementController {

    private static final Logger logger = LoggerFactory.getLogger(PaiementController.class);

    @Autowired
    private PaiementService paiementService;
    
    @Autowired
    private ApurementRepository apurementRepository;

    @PostMapping("/process/{declarationId}")
    @PreAuthorize("hasAnyRole('RECEVEUR_DES_IMPOTS','ADMIN')")
    public ResponseEntity<?> processPayment(
            @PathVariable UUID declarationId,
            @RequestParam String bordereauBancaire,
            @RequestParam double montant) {
        try {
            Paiement paiement = paiementService.processPayment(declarationId, bordereauBancaire, montant);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "paiement", convertToResponseDTO(paiement),
                    "message", "Paiement traité avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PAYMENT_PROCESSING_ERROR", "Erreur lors du traitement du paiement", e.getMessage()));
        }
    }

    @GetMapping("/declaration/{declarationId}")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'CONTRIBUABLE','ADMIN')")
    public ResponseEntity<?> getPaymentByDeclarationId(@PathVariable UUID declarationId) {
        try {
            Paiement paiement = paiementService.getPaymentByDeclarationId(declarationId);

            if (paiement == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("PAYMENT_NOT_FOUND", "Paiement non trouvé", "Aucun paiement trouvé pour cette déclaration"));
            }

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("paiement", convertToResponseDTO(paiement))));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PAYMENT_FETCH_ERROR", "Erreur lors de la récupération du paiement", e.getMessage()));
        }
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR','ADMIN')")
    public ResponseEntity<?> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) StatutPaiement statut) {
        try {
            List<Paiement> paiements;
            if (statut != null) {
                paiements = paiementService.getPaymentsByStatus(statut);
            } else {
                paiements = paiementService.getAllPayments();
            }

            // Convertir les paiements en DTO
            List<PaiementResponseDTO> paiementDTOs = paiements.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("paiements", paiementDTOs);
            response.put("currentPage", page);
            response.put("totalItems", paiements.size());
            response.put("totalPages", (int) Math.ceil((double) paiements.size() / size));

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PAYMENT_FETCH_ERROR", "Erreur lors de la récupération des paiements", e.getMessage()));
        }
    }

    @GetMapping("/paginated")
    @PreAuthorize("hasAnyRole('RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU','APUREUR', 'ADMIN')")
    public ResponseEntity<?> getPaginatedPayments(
            @RequestParam(required = false) StatutPaiement statut,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<Paiement> paiementPage = paiementService.getPaginatedPayments(
                    statut, startDate, endDate, pageable);

            List<PaiementResponseDTO> paiementDTOs = paiementPage.getContent().stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("paiements", paiementDTOs);
            response.put("currentPage", paiementPage.getNumber());
            response.put("totalItems", paiementPage.getTotalElements());
            response.put("totalPages", paiementPage.getTotalPages());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PAYMENTS_RETRIEVAL_ERROR",
                            "Erreur lors de la récupération des paiements paginés",
                            e.getMessage()));
        }
    }

    /**
     * Convertit une entité Paiement en DTO avec les informations enrichies
     * 
     * @param paiement L'entité Paiement à convertir
     * @return Le DTO enrichi
     */
    private PaiementResponseDTO convertToResponseDTO(Paiement paiement) {
        try {
            PaiementResponseDTO dto = new PaiementResponseDTO();
            dto.setId(paiement.getId());
            dto.setDate(paiement.getDate());
            dto.setMontant(paiement.getMontant());
            dto.setMode(paiement.getMode());
            dto.setStatut(paiement.getStatut());
            dto.setBordereauBancaire(paiement.getBordereauBancaire());
            dto.setActif(paiement.isActif());
            
            // Ajouter les informations enrichies de la taxation
            Taxation taxation = paiement.getTaxation();
            if (taxation != null) {
                try {
                    // Informations de la taxation
                    dto.setDateTaxation(taxation.getDateTaxation());
                    dto.setDevise(taxation.getDevise());
                    dto.setTypeImpot(taxation.getTypeImpot());
                    dto.setExerciceFiscal(taxation.getExercice());
                    
                    // Nom du taxateur (agent)
                    if (taxation.getAgent() != null) {
                        dto.setNomTaxateur(taxation.getNomAgent());
                    }
                    
                    // Nom du contribuable
                    if (taxation.getContribuableDirect() != null) {
                        dto.setNomContribuable(taxation.getContribuableDirect().getNom());
                    } else if (taxation.getDeclaration() != null && 
                              taxation.getDeclaration().getContribuable() != null) {
                        dto.setNomContribuable(taxation.getDeclaration().getContribuable().getNom());
                    }
                    
                    // Détails de la taxation
                    PaiementResponseDTO.TaxationDTO taxationDTO = new PaiementResponseDTO.TaxationDTO();
                    taxationDTO.setId(taxation.getId());
                    taxationDTO.setNumeroTaxation(taxation.getNumeroTaxation());
                    dto.setTaxation(taxationDTO);
                } catch (Exception e) {
                    logger.error("Erreur lors de la récupération des informations de taxation: {}", e.getMessage());
                    // Ne pas bloquer la conversion si une erreur se produit avec la taxation
                }
            }
            
            // Ajouter les informations d'apurement
            if (paiement.getId() != null) {
                try {
                    Apurement apurement = apurementRepository.findByPaiement_Id(paiement.getId());
                    if (apurement != null) {
                        ApurementDTO apurementDTO = new ApurementDTO();
                        apurementDTO.setId(apurement.getId());
                        apurementDTO.setDateDemande(apurement.getDateDemande());
                        apurementDTO.setDateValidation(apurement.getDateValidation());
                        apurementDTO.setType(apurement.getType());
                        apurementDTO.setMontantApure(apurement.getMontantApure());
                        apurementDTO.setMotif(apurement.getMotif());
                        apurementDTO.setMotifRejet(apurement.getMotifRejet());
                        apurementDTO.setStatut(apurement.getStatut());
                        apurementDTO.setProvisoire(apurement.isProvisoire());
                        apurementDTO.setDeclarationPayee(apurement.isDeclarationPayee());
                        
                        // Informations sur l'agent qui a initié l'apurement
                        if (apurement.getAgent() != null) {
                            apurementDTO.setAgentId(apurement.getAgent().getId());
                            apurementDTO.setNomAgent(apurement.getAgent().getNom());
                        }
                        
                        // Informations sur l'agent validateur
                        if (apurement.getAgentValidateur() != null) {
                            apurementDTO.setAgentValidateurId(apurement.getAgentValidateur().getId());
                            apurementDTO.setNomAgentValidateur(apurement.getAgentValidateur().getNom());
                        }
                        
                        dto.setApurement(apurementDTO);
                    }
                } catch (Exception e) {
                    logger.error("Erreur lors de la récupération des informations d'apurement: {}", e.getMessage());
                    // Ne pas bloquer la conversion si une erreur se produit avec l'apurement
                }
            }
            
            // Ajouter l'ID de la déclaration
            if (paiement.getDeclaration() != null) {
                dto.setDeclarationId(paiement.getDeclaration().getId());
            }
            
            return dto;
        } catch (Exception e) {
            logger.error("Erreur lors de la conversion du paiement en DTO: {}", e.getMessage());
            // En cas d'erreur, retourner un DTO minimal avec l'ID du paiement
            PaiementResponseDTO fallbackDto = new PaiementResponseDTO();
            if (paiement != null) {
                fallbackDto.setId(paiement.getId());
            }
            return fallbackDto;
        }
    }
}
