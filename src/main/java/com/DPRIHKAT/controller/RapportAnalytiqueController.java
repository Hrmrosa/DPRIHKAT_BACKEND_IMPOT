package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.RapportRequestDTO;
import com.DPRIHKAT.dto.RapportResponseDTO;
import com.DPRIHKAT.entity.enums.PeriodeRapport;
import com.DPRIHKAT.entity.enums.TypeRapport;
import com.DPRIHKAT.service.RapportAnalytiqueService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

/**
 * Contrôleur REST pour la génération de rapports analytiques
 * 
 * @author amateur
 */
@RestController
@RequestMapping("/api/rapports")
@CrossOrigin(origins = "*")
public class RapportAnalytiqueController {
    
    private final RapportAnalytiqueService rapportAnalytiqueService;
    
    public RapportAnalytiqueController(RapportAnalytiqueService rapportAnalytiqueService) {
        this.rapportAnalytiqueService = rapportAnalytiqueService;
    }
    
    /**
     * Génère un rapport selon les critères spécifiés
     * 
     * @param request Les critères du rapport
     * @return Le rapport généré
     */
    @PostMapping("/generer")
    @PreAuthorize("hasAnyRole('CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN')")
    public ResponseEntity<RapportResponseDTO> genererRapport(@RequestBody RapportRequestDTO request) {
        try {
            // Validation des paramètres
            if (request.getTypeRapport() == null) {
                return ResponseEntity.badRequest().build();
            }
            
            if (request.getPeriode() == null) {
                request.setPeriode(PeriodeRapport.MOIS); // Par défaut : mensuel
            }
            
            // Si période personnalisée, vérifier que les dates sont fournies
            if (request.getPeriode() == PeriodeRapport.PERSONNALISEE) {
                if (request.getDateDebut() == null || request.getDateFin() == null) {
                    return ResponseEntity.badRequest().build();
                }
            }
            
            RapportResponseDTO rapport = rapportAnalytiqueService.genererRapport(request);
            return ResponseEntity.ok(rapport);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Génère un rapport sur les taxations
     * 
     * @param periode La période du rapport (JOUR, SEMAINE, MOIS, etc.)
     * @param dateDebut Date de début (optionnel, pour période personnalisée)
     * @param dateFin Date de fin (optionnel, pour période personnalisée)
     * @param agentId ID de l'agent (optionnel, pour filtrer par agent)
     * @return Le rapport sur les taxations
     */
    @GetMapping("/taxations")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN')")
    public ResponseEntity<RapportResponseDTO> genererRapportTaxations(
            @RequestParam(required = false, defaultValue = "MOIS") PeriodeRapport periode,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin,
            @RequestParam(required = false) UUID agentId) {
        
        try {
            RapportRequestDTO request = new RapportRequestDTO();
            request.setTypeRapport(TypeRapport.TAXATION);
            request.setPeriode(periode);
            request.setDateDebut(dateDebut);
            request.setDateFin(dateFin);
            request.setAgentId(agentId);
            
            RapportResponseDTO rapport = rapportAnalytiqueService.genererRapport(request);
            return ResponseEntity.ok(rapport);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Génère un rapport sur les paiements
     * 
     * @param periode La période du rapport
     * @param dateDebut Date de début (optionnel)
     * @param dateFin Date de fin (optionnel)
     * @param agentId ID de l'agent (optionnel)
     * @return Le rapport sur les paiements
     */
    @GetMapping("/paiements")
    @PreAuthorize("hasAnyRole('RECEVEUR_DES_IMPOTS', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN')")
    public ResponseEntity<RapportResponseDTO> genererRapportPaiements(
            @RequestParam(required = false, defaultValue = "MOIS") PeriodeRapport periode,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin,
            @RequestParam(required = false) UUID agentId) {
        
        try {
            RapportRequestDTO request = new RapportRequestDTO();
            request.setTypeRapport(TypeRapport.PAIEMENT);
            request.setPeriode(periode);
            request.setDateDebut(dateDebut);
            request.setDateFin(dateFin);
            request.setAgentId(agentId);
            
            RapportResponseDTO rapport = rapportAnalytiqueService.genererRapport(request);
            return ResponseEntity.ok(rapport);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Génère un rapport sur les relances
     * 
     * @param periode La période du rapport
     * @param dateDebut Date de début (optionnel)
     * @param dateFin Date de fin (optionnel)
     * @return Le rapport sur les relances
     */
    @GetMapping("/relances")
    @PreAuthorize("hasAnyRole('CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN')")
    public ResponseEntity<RapportResponseDTO> genererRapportRelances(
            @RequestParam(required = false, defaultValue = "MOIS") PeriodeRapport periode,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin) {
        
        try {
            RapportRequestDTO request = new RapportRequestDTO();
            request.setTypeRapport(TypeRapport.RELANCE);
            request.setPeriode(periode);
            request.setDateDebut(dateDebut);
            request.setDateFin(dateFin);
            
            RapportResponseDTO rapport = rapportAnalytiqueService.genererRapport(request);
            return ResponseEntity.ok(rapport);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Génère un rapport sur les actes de recouvrement
     * 
     * @param periode La période du rapport
     * @param dateDebut Date de début (optionnel)
     * @param dateFin Date de fin (optionnel)
     * @param agentId ID de l'agent (optionnel)
     * @return Le rapport sur les actes de recouvrement
     */
    @GetMapping("/recouvrements")
    @PreAuthorize("hasAnyRole('CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN')")
    public ResponseEntity<RapportResponseDTO> genererRapportRecouvrements(
            @RequestParam(required = false, defaultValue = "MOIS") PeriodeRapport periode,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin,
            @RequestParam(required = false) UUID agentId) {
        
        try {
            RapportRequestDTO request = new RapportRequestDTO();
            request.setTypeRapport(TypeRapport.RECOUVREMENT);
            request.setPeriode(periode);
            request.setDateDebut(dateDebut);
            request.setDateFin(dateFin);
            request.setAgentId(agentId);
            
            RapportResponseDTO rapport = rapportAnalytiqueService.genererRapport(request);
            return ResponseEntity.ok(rapport);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Génère un rapport sur la collecte de données
     * 
     * @param periode La période du rapport
     * @param dateDebut Date de début (optionnel)
     * @param dateFin Date de fin (optionnel)
     * @param agentId ID de l'agent (optionnel)
     * @return Le rapport sur la collecte
     */
    @GetMapping("/collectes")
    @PreAuthorize("hasAnyRole('TAXATEUR', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN')")
    public ResponseEntity<RapportResponseDTO> genererRapportCollectes(
            @RequestParam(required = false, defaultValue = "MOIS") PeriodeRapport periode,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin,
            @RequestParam(required = false) UUID agentId) {
        
        try {
            RapportRequestDTO request = new RapportRequestDTO();
            request.setTypeRapport(TypeRapport.COLLECTE);
            request.setPeriode(periode);
            request.setDateDebut(dateDebut);
            request.setDateFin(dateFin);
            request.setAgentId(agentId);
            
            RapportResponseDTO rapport = rapportAnalytiqueService.genererRapport(request);
            return ResponseEntity.ok(rapport);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Génère un rapport global incluant tous les types
     * 
     * @param periode La période du rapport
     * @param dateDebut Date de début (optionnel)
     * @param dateFin Date de fin (optionnel)
     * @param agentId ID de l'agent (optionnel)
     * @return Le rapport global
     */
    @GetMapping("/global")
    @PreAuthorize("hasAnyRole('CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN')")
    public ResponseEntity<RapportResponseDTO> genererRapportGlobal(
            @RequestParam(required = false, defaultValue = "MOIS") PeriodeRapport periode,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin,
            @RequestParam(required = false) UUID agentId) {
        
        try {
            RapportRequestDTO request = new RapportRequestDTO();
            request.setTypeRapport(TypeRapport.GLOBAL);
            request.setPeriode(periode);
            request.setDateDebut(dateDebut);
            request.setDateFin(dateFin);
            request.setAgentId(agentId);
            
            RapportResponseDTO rapport = rapportAnalytiqueService.genererRapport(request);
            return ResponseEntity.ok(rapport);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Génère un rapport personnalisé avec des dates spécifiques
     * 
     * @param typeRapport Le type de rapport
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @param agentId ID de l'agent (optionnel)
     * @return Le rapport personnalisé
     */
    @GetMapping("/personnalise")
    @PreAuthorize("hasAnyRole('CHEF_DE_BUREAU', 'CHEF_DE_DIVISION', 'DIRECTEUR', 'ADMIN')")
    public ResponseEntity<RapportResponseDTO> genererRapportPersonnalise(
            @RequestParam TypeRapport typeRapport,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin,
            @RequestParam(required = false) UUID agentId) {
        
        try {
            RapportRequestDTO request = new RapportRequestDTO();
            request.setTypeRapport(typeRapport);
            request.setPeriode(PeriodeRapport.PERSONNALISEE);
            request.setDateDebut(dateDebut);
            request.setDateFin(dateFin);
            request.setAgentId(agentId);
            
            RapportResponseDTO rapport = rapportAnalytiqueService.genererRapport(request);
            return ResponseEntity.ok(rapport);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
