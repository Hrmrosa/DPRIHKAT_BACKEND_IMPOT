package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.DossierRecouvrement;
import com.DPRIHKAT.repository.DossierRecouvrementRepository;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RecouvrementService {

    @Autowired
    private DossierRecouvrementRepository dossierRecouvrementRepository;

    public ResponseEntity<?> getDossierComplet(UUID id) {
        try {
            DossierRecouvrement dossier = dossierRecouvrementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dossier non trouvé"));
            
            // Charger les relations nécessaires
            dossier.getContribuable();
            dossier.getDeclarations().forEach(d -> d.getImpot());
            dossier.getPaiements().size();
            dossier.getHistorique().size();
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("dossier", dossier)));
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(ResponseUtil.createErrorResponse("DOSSIER_FETCH_ERROR", "Erreur de récupération", e.getMessage()));
        }
    }

    public ResponseEntity<?> searchDossiers(UUID contribuableId, String statut, Date dateDebut, Date dateFin) {
        try {
            List<DossierRecouvrement> dossiers = dossierRecouvrementRepository
                .findByCriteria(contribuableId, statut, dateDebut, dateFin);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("dossiers", dossiers)));
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(ResponseUtil.createErrorResponse("DOSSIER_SEARCH_ERROR", "Erreur de recherche", e.getMessage()));
        }
    }

    public ResponseEntity<?> exportDossier(UUID id) {
        try {
            // Implémentation de l'export PDF à compléter
            byte[] pdf = generatePdfFromDossier(id);
            return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .body(pdf);
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(ResponseUtil.createErrorResponse("DOSSIER_EXPORT_ERROR", "Erreur d'export", e.getMessage()));
        }
    }

    private byte[] generatePdfFromDossier(UUID id) {
        // TODO: Implémenter la génération PDF
        return new byte[0];
    }
}
