package com.DPRIHKAT.service;

import com.DPRIHKAT.dto.dashboard.DashboardDTO;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.entity.enums.StatutDeclaration;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import com.DPRIHKAT.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private ContribuableRepository contribuableRepository;
    
    @Autowired
    private VehiculeRepository vehiculeRepository;
    
    @Autowired
    private ProprieteRepository proprieteRepository;
    
    @Autowired
    private TaxationRepository taxationRepository;
    
    @Autowired
    private PaiementRepository paiementRepository;
    
    @Autowired
    private DeclarationRepository declarationRepository;
    
    @Autowired
    private RelanceRepository relanceRepository;
    
    @Autowired
    private ApurementRepository apurementRepository;
    
    public DashboardDTO generateDashboardData(Role role, LocalDate dateDebut, LocalDate dateFin) {
        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setDateDebut(dateDebut);
        dashboard.setDateFin(dateFin);
        dashboard.setDateReference(LocalDate.now());
        
        // Données communes à tous les rôles
        dashboard.setTotalContribuables(contribuableRepository.count());
        dashboard.setTotalDeclarations(declarationRepository.count());
        
        // Données spécifiques au rôle
        if (role == Role.DIRECTEUR || role == Role.CHEF_DE_DIVISION || role == Role.ADMIN) {
            generateAdminDashboard(dashboard, dateDebut, dateFin);
        } else if (role == Role.RECEVEUR_DES_IMPOTS || role == Role.APUREUR) {
            generateRecouvrementDashboard(dashboard, dateDebut, dateFin);
        } else if (role == Role.CHEF_DE_BUREAU || role == Role.TAXATEUR) {
            generateCertificatDashboard(dashboard, dateDebut, dateFin);
        } else if (role == Role.CONTROLLEUR || role == Role.VERIFICATEUR) {
            generateContribuableDashboard(dashboard, dateDebut, dateFin);
        } else {
            generateBasicDashboard(dashboard, dateDebut, dateFin);
        }
        
        return dashboard;
    }
    
    private void generateAdminDashboard(DashboardDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        // Statistiques générales
        dashboard.setTotalVehicules(vehiculeRepository.count());
        dashboard.setTotalProprietes(proprieteRepository.count());
        
        // Données financières
        BigDecimal totalTaxations = taxationRepository.sumMontantByDateBetween(dateDebut, dateFin);
        dashboard.setMontantTotalTaxations(totalTaxations != null ? totalTaxations : BigDecimal.ZERO);
        
        BigDecimal totalPaiements = paiementRepository.sumMontantByDateBetween(dateDebut, dateFin);
        dashboard.setMontantTotalPaiements(totalPaiements != null ? totalPaiements : BigDecimal.ZERO);
        
        dashboard.setMontantTotalImpayes(
            dashboard.getMontantTotalTaxations().subtract(dashboard.getMontantTotalPaiements())
        );
        
        // Données d'apurements
        BigDecimal totalApurements = apurementRepository.sumMontantBetweenDates(dateDebut, dateFin);
        dashboard.setMontantTotalApurements(totalApurements != null ? totalApurements : BigDecimal.ZERO);
        dashboard.setTotalApurements(apurementRepository.count());
        
        // Statistiques par type
        dashboard.setStatsParTypeImpot(taxationRepository.countByTypeImpotBetweenDates(dateDebut, dateFin));
        
        // Statistiques par statut
        Map<String, Long> statsDeclaration = new HashMap<>();
        for (StatutDeclaration statut : StatutDeclaration.values()) {
            statsDeclaration.put(statut.name(), declarationRepository.countByStatut(statut.name()));
        }
        dashboard.setStatsParStatutDeclaration(statsDeclaration);
        
        Map<String, Long> statsPaiement = new HashMap<>();
        for (StatutPaiement statut : StatutPaiement.values()) {
            statsPaiement.put(statut.name(), paiementRepository.countByStatut(statut.name()));
        }
        dashboard.setStatsParStatutPaiement(statsPaiement);
        
        // Données des apurements par type
        List<Map<String, Object>> apurementsByType = apurementRepository.countByTypeBetweenDates(dateDebut, dateFin);
        Map<String, Long> statsApurements = new HashMap<>();
        for (Map<String, Object> entry : apurementsByType) {
            statsApurements.put(entry.get("type").toString(), ((Number) entry.get("count")).longValue());
        }
        dashboard.setStatsParTypeApurement(statsApurements);
        
        // Montants des apurements par type
        List<Map<String, Object>> montantsByType = apurementRepository.sumMontantByTypeBetweenDates(dateDebut, dateFin);
        Map<String, BigDecimal> montantsApurements = new HashMap<>();
        for (Map<String, Object> entry : montantsByType) {
            montantsApurements.put(entry.get("type").toString(), (BigDecimal) entry.get("montant"));
        }
        dashboard.setMontantParTypeApurement(montantsApurements);
        
        // Alertes et indicateurs
        dashboard.setDeclarationsEnRetard(declarationRepository.countByStatut(StatutDeclaration.EN_ATTENTE.name()));
        dashboard.setPaiementsEnRetard(paiementRepository.countByStatut(StatutPaiement.EN_ATTENTE.name()));
        dashboard.setRelancesEnCours(relanceRepository.countByStatutAndDateEnvoiBetween("EN_COURS", dateDebut, dateFin));
        dashboard.setRelancesEffectuees(relanceRepository.countByStatutAndDateEnvoiBetween("TERMINEE", dateDebut, dateFin));
        
        // Tendances mensuelles
        generateMonthlyTrends(dashboard, dateDebut, dateFin);
    }
    
    private void generateRecouvrementDashboard(DashboardDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        // Données financières
        BigDecimal totalTaxations = taxationRepository.sumMontantByDateBetween(dateDebut, dateFin);
        dashboard.setMontantTotalTaxations(totalTaxations != null ? totalTaxations : BigDecimal.ZERO);
        
        BigDecimal totalPaiements = paiementRepository.sumMontantByDateBetween(dateDebut, dateFin);
        dashboard.setMontantTotalPaiements(totalPaiements != null ? totalPaiements : BigDecimal.ZERO);
        
        dashboard.setMontantTotalImpayes(
            dashboard.getMontantTotalTaxations().subtract(dashboard.getMontantTotalPaiements())
        );
        
        // Données d'apurements
        BigDecimal totalApurements = apurementRepository.sumMontantBetweenDates(dateDebut, dateFin);
        dashboard.setMontantTotalApurements(totalApurements != null ? totalApurements : BigDecimal.ZERO);
        dashboard.setTotalApurements(apurementRepository.count());
        
        // Statistiques par type d'apurement
        List<Map<String, Object>> apurementsByType = apurementRepository.countByTypeBetweenDates(dateDebut, dateFin);
        Map<String, Long> statsApurements = new HashMap<>();
        for (Map<String, Object> entry : apurementsByType) {
            statsApurements.put(entry.get("type").toString(), ((Number) entry.get("count")).longValue());
        }
        dashboard.setStatsParTypeApurement(statsApurements);
        
        // Statistiques par statut
        Map<String, Long> statsPaiement = new HashMap<>();
        for (StatutPaiement statut : StatutPaiement.values()) {
            statsPaiement.put(statut.name(), paiementRepository.countByStatut(statut.name()));
        }
        dashboard.setStatsParStatutPaiement(statsPaiement);
        
        // Alertes et indicateurs
        dashboard.setPaiementsEnRetard(paiementRepository.countByStatut(StatutPaiement.EN_ATTENTE.name()));
        dashboard.setRelancesEnCours(relanceRepository.countByStatutAndDateEnvoiBetween("EN_COURS", dateDebut, dateFin));
        dashboard.setRelancesEffectuees(relanceRepository.countByStatutAndDateEnvoiBetween("TERMINEE", dateDebut, dateFin));
        
        // Données spécifiques pour le rôle de recouvrement
        Map<String, Object> donneesSpecifiques = new HashMap<>();
        donneesSpecifiques.put("tauxRecouvrement", calculerTauxRecouvrement(totalTaxations, totalPaiements));
        donneesSpecifiques.put("tauxApurement", calculerTauxApurement(totalTaxations, totalApurements));
        dashboard.setDonneesSpecifiques(donneesSpecifiques);
    }
    
    private void generateCertificatDashboard(DashboardDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        dashboard.setTotalVehicules(vehiculeRepository.count());
        dashboard.setTotalProprietes(proprieteRepository.count());
        
        // Statistiques par type d'impôt
        dashboard.setStatsParTypeImpot(taxationRepository.countByTypeImpotBetweenDates(dateDebut, dateFin));
        
        // Statistiques par statut de déclaration
        Map<String, Long> statsDeclaration = new HashMap<>();
        for (StatutDeclaration statut : StatutDeclaration.values()) {
            statsDeclaration.put(statut.name(), declarationRepository.countByStatut(statut.name()));
        }
        dashboard.setStatsParStatutDeclaration(statsDeclaration);
        
        // Données spécifiques pour le rôle de certificat
        Map<String, Object> donneesSpecifiques = new HashMap<>();
        donneesSpecifiques.put("declarationsTraitees", declarationRepository.countByStatut(StatutDeclaration.VALIDEE.name()));
        donneesSpecifiques.put("declarationsEnAttente", declarationRepository.countByStatut(StatutDeclaration.EN_ATTENTE.name()));
        dashboard.setDonneesSpecifiques(donneesSpecifiques);
    }
    
    private void generateContribuableDashboard(DashboardDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        // Statistiques générales
        dashboard.setTotalVehicules(vehiculeRepository.count());
        dashboard.setTotalProprietes(proprieteRepository.count());
        
        // Statistiques par type d'impôt
        dashboard.setStatsParTypeImpot(taxationRepository.countByTypeImpotBetweenDates(dateDebut, dateFin));
        
        // Données spécifiques pour le rôle de contrôle
        Map<String, Object> donneesSpecifiques = new HashMap<>();
        donneesSpecifiques.put("contribuablesActifs", contribuableRepository.countByActifTrue());
        donneesSpecifiques.put("contribuablesInactifs", contribuableRepository.countByActifFalse());
        dashboard.setDonneesSpecifiques(donneesSpecifiques);
    }
    
    private void generateBasicDashboard(DashboardDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        // Données de base pour les autres rôles
        BigDecimal totalTaxations = taxationRepository.sumMontantByDateBetween(dateDebut, dateFin);
        dashboard.setMontantTotalTaxations(totalTaxations != null ? totalTaxations : BigDecimal.ZERO);
        
        BigDecimal totalPaiements = paiementRepository.sumMontantByDateBetween(dateDebut, dateFin);
        dashboard.setMontantTotalPaiements(totalPaiements != null ? totalPaiements : BigDecimal.ZERO);
    }
    
    /**
     * Génère les tendances mensuelles pour les recettes et les déclarations
     */
    private void generateMonthlyTrends(DashboardDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        // Initialiser les maps pour les tendances
        Map<String, BigDecimal> tendanceRecettes = new HashMap<>();
        Map<String, Long> tendanceDeclarations = new HashMap<>();
        
        // Générer les données pour chaque mois entre dateDebut et dateFin
        LocalDate current = dateDebut;
        while (!current.isAfter(dateFin)) {
            String monthKey = current.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH) + " " + current.getYear();
            
            // Recettes du mois
            LocalDate firstDayOfMonth = current.withDayOfMonth(1);
            LocalDate lastDayOfMonth = current.withDayOfMonth(current.lengthOfMonth());
            
            BigDecimal recettesMois = paiementRepository.sumMontantByDateBetween(firstDayOfMonth, lastDayOfMonth);
            tendanceRecettes.put(monthKey, recettesMois != null ? recettesMois : BigDecimal.ZERO);
            
            // Déclarations du mois
            Long declarationsMois = declarationRepository.countByDateDeclarationBetween(firstDayOfMonth, lastDayOfMonth);
            tendanceDeclarations.put(monthKey, declarationsMois != null ? declarationsMois : 0L);
            
            // Passer au mois suivant
            current = current.plusMonths(1);
        }
        
        dashboard.setTendanceMensuelleRecettes(tendanceRecettes);
        dashboard.setTendanceMensuelleDeclarations(tendanceDeclarations);
    }
    
    /**
     * Calcule le taux de recouvrement (paiements / taxations)
     */
    private double calculerTauxRecouvrement(BigDecimal totalTaxations, BigDecimal totalPaiements) {
        if (totalTaxations == null || totalTaxations.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return totalPaiements.divide(totalTaxations, 4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
    }
    
    /**
     * Calcule le taux d'apurement (apurements / taxations)
     */
    private double calculerTauxApurement(BigDecimal totalTaxations, BigDecimal totalApurements) {
        if (totalTaxations == null || totalTaxations.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return totalApurements.divide(totalTaxations, 4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
    }
}
