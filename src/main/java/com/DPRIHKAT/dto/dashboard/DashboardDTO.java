package com.DPRIHKAT.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * DTO pour les données du dashboard
 */
public class DashboardDTO {
    private LocalDate dateReference;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    
    // Statistiques globales
    private long totalContribuables;
    private long totalVehicules;
    private long totalProprietes;
    private long totalDeclarations;
    
    // Données financières
    private BigDecimal montantTotalTaxations;
    private BigDecimal montantTotalPaiements;
    private BigDecimal montantTotalImpayes;
    
    // Données d'apurements
    private long totalApurements;
    private BigDecimal montantTotalApurements;
    private Map<String, Long> statsParTypeApurement = new HashMap<>();
    private Map<String, BigDecimal> montantParTypeApurement = new HashMap<>();
    
    // Données de collecte
    private long totalCollectes;
    private BigDecimal montantTotalCollectes;
    private Map<String, Long> statsParModeCollecte = new HashMap<>();
    
    // Répartition par type
    private Map<String, Long> statsParTypeImpot = new HashMap<>();
    private Map<String, Long> statsParStatutPaiement = new HashMap<>();
    private Map<String, Long> statsParStatutDeclaration = new HashMap<>();
    
    // Alertes et indicateurs
    private long declarationsEnRetard;
    private long paiementsEnRetard;
    private long relancesEnCours;
    private long relancesEffectuees;
    
    // Tendances
    private Map<String, BigDecimal> tendanceMensuelleRecettes = new HashMap<>();
    private Map<String, Long> tendanceMensuelleDeclarations = new HashMap<>();
    
    // Données spécifiques par rôle
    private Map<String, Object> donneesSpecifiques = new HashMap<>();
    
    // Getters et Setters
    public LocalDate getDateReference() {
        return dateReference;
    }
    
    public void setDateReference(LocalDate dateReference) {
        this.dateReference = dateReference;
    }
    
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public LocalDate getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
    
    public long getTotalContribuables() {
        return totalContribuables;
    }
    
    public void setTotalContribuables(long totalContribuables) {
        this.totalContribuables = totalContribuables;
    }
    
    public long getTotalVehicules() {
        return totalVehicules;
    }
    
    public void setTotalVehicules(long totalVehicules) {
        this.totalVehicules = totalVehicules;
    }
    
    public long getTotalProprietes() {
        return totalProprietes;
    }
    
    public void setTotalProprietes(long totalProprietes) {
        this.totalProprietes = totalProprietes;
    }
    
    public long getTotalDeclarations() {
        return totalDeclarations;
    }
    
    public void setTotalDeclarations(long totalDeclarations) {
        this.totalDeclarations = totalDeclarations;
    }
    
    public BigDecimal getMontantTotalTaxations() {
        return montantTotalTaxations;
    }
    
    public void setMontantTotalTaxations(BigDecimal montantTotalTaxations) {
        this.montantTotalTaxations = montantTotalTaxations;
    }
    
    public BigDecimal getMontantTotalPaiements() {
        return montantTotalPaiements;
    }
    
    public void setMontantTotalPaiements(BigDecimal montantTotalPaiements) {
        this.montantTotalPaiements = montantTotalPaiements;
    }
    
    public BigDecimal getMontantTotalImpayes() {
        return montantTotalImpayes;
    }
    
    public void setMontantTotalImpayes(BigDecimal montantTotalImpayes) {
        this.montantTotalImpayes = montantTotalImpayes;
    }
    
    public Map<String, Long> getStatsParTypeImpot() {
        return statsParTypeImpot;
    }
    
    public void setStatsParTypeImpot(Map<String, Long> statsParTypeImpot) {
        this.statsParTypeImpot = statsParTypeImpot;
    }
    
    public Map<String, Long> getStatsParStatutPaiement() {
        return statsParStatutPaiement;
    }
    
    public void setStatsParStatutPaiement(Map<String, Long> statsParStatutPaiement) {
        this.statsParStatutPaiement = statsParStatutPaiement;
    }
    
    public Map<String, Long> getStatsParStatutDeclaration() {
        return statsParStatutDeclaration;
    }
    
    public void setStatsParStatutDeclaration(Map<String, Long> statsParStatutDeclaration) {
        this.statsParStatutDeclaration = statsParStatutDeclaration;
    }
    
    public long getDeclarationsEnRetard() {
        return declarationsEnRetard;
    }
    
    public void setDeclarationsEnRetard(long declarationsEnRetard) {
        this.declarationsEnRetard = declarationsEnRetard;
    }
    
    public long getPaiementsEnRetard() {
        return paiementsEnRetard;
    }
    
    public void setPaiementsEnRetard(long paiementsEnRetard) {
        this.paiementsEnRetard = paiementsEnRetard;
    }
    
    public long getRelancesEnCours() {
        return relancesEnCours;
    }
    
    public void setRelancesEnCours(long relancesEnCours) {
        this.relancesEnCours = relancesEnCours;
    }
    
    public long getRelancesEffectuees() {
        return relancesEffectuees;
    }
    
    public void setRelancesEffectuees(long relancesEffectuees) {
        this.relancesEffectuees = relancesEffectuees;
    }
    
    public long getTotalApurements() {
        return totalApurements;
    }
    
    public void setTotalApurements(long totalApurements) {
        this.totalApurements = totalApurements;
    }
    
    public BigDecimal getMontantTotalApurements() {
        return montantTotalApurements;
    }
    
    public void setMontantTotalApurements(BigDecimal montantTotalApurements) {
        this.montantTotalApurements = montantTotalApurements;
    }
    
    public Map<String, Long> getStatsParTypeApurement() {
        return statsParTypeApurement;
    }
    
    public void setStatsParTypeApurement(Map<String, Long> statsParTypeApurement) {
        this.statsParTypeApurement = statsParTypeApurement;
    }
    
    public Map<String, BigDecimal> getMontantParTypeApurement() {
        return montantParTypeApurement;
    }
    
    public void setMontantParTypeApurement(Map<String, BigDecimal> montantParTypeApurement) {
        this.montantParTypeApurement = montantParTypeApurement;
    }
    
    public long getTotalCollectes() {
        return totalCollectes;
    }
    
    public void setTotalCollectes(long totalCollectes) {
        this.totalCollectes = totalCollectes;
    }
    
    public BigDecimal getMontantTotalCollectes() {
        return montantTotalCollectes;
    }
    
    public void setMontantTotalCollectes(BigDecimal montantTotalCollectes) {
        this.montantTotalCollectes = montantTotalCollectes;
    }
    
    public Map<String, Long> getStatsParModeCollecte() {
        return statsParModeCollecte;
    }
    
    public void setStatsParModeCollecte(Map<String, Long> statsParModeCollecte) {
        this.statsParModeCollecte = statsParModeCollecte;
    }
    
    public Map<String, BigDecimal> getTendanceMensuelleRecettes() {
        return tendanceMensuelleRecettes;
    }
    
    public void setTendanceMensuelleRecettes(Map<String, BigDecimal> tendanceMensuelleRecettes) {
        this.tendanceMensuelleRecettes = tendanceMensuelleRecettes;
    }
    
    public Map<String, Long> getTendanceMensuelleDeclarations() {
        return tendanceMensuelleDeclarations;
    }
    
    public void setTendanceMensuelleDeclarations(Map<String, Long> tendanceMensuelleDeclarations) {
        this.tendanceMensuelleDeclarations = tendanceMensuelleDeclarations;
    }
    
    public Map<String, Object> getDonneesSpecifiques() {
        return donneesSpecifiques;
    }
    
    public void setDonneesSpecifiques(Map<String, Object> donneesSpecifiques) {
        this.donneesSpecifiques = donneesSpecifiques;
    }
    
    public void addDonneeSpecifique(String key, Object value) {
        this.donneesSpecifiques.put(key, value);
    }
}
