package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Paiement;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import com.DPRIHKAT.service.PaiementService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/paiements")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    @PostMapping("/process/{declarationId}")
    @PreAuthorize("hasAnyRole('RECEVEUR_DES_IMPOTS','ADMIN')")
    public ResponseEntity<?> processPayment(
            @PathVariable UUID declarationId,
            @RequestParam String bordereauBancaire,
            @RequestParam double montant) {
        try {
            Paiement paiement = paiementService.processPayment(declarationId, bordereauBancaire, montant);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "paiement", paiement,
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

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("paiement", paiement)));
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

            Map<String, Object> response = new HashMap<>();
            response.put("paiements", paiements);
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
}
