package com.DPRIHKAT.dto;

import java.util.Map;

/**
 * DTO pour les statistiques globales du Dashboard
 * 
 * @author amateur
 */
public class StatistiquesDashboardDTO {
    
    // Contribuables
    private Long nombreTotalContribuables;
    private Long nombreContribuablesActifs;
    private Long nombreContribuablesInactifs;
    
    // Taxations
    private Long nombreTotalTaxations;
    private Double montantTotalTaxations;
    private Long nombreTaxationsPayees;
    private Long nombreTaxationsEnAttente;
    private Long nombreTaxationsEnRetard;
    
    // Paiements
    private Long nombreTotalPaiements;
    private Double montantTotalPaiements;
    private Double montantPaiementsAujourdhui;
    private Double montantPaiementsCeMois;
    
    // Relances
    private Long nombreTotalRelances;
    private Long nombreRelancesEnvoyees;
    private Long nombreRelancesEnAttente;
    
    // Recouvrement
    private Long nombreDossiersRecouvrement;
    private Long nombreATD; // Avis de Taxation Directe
    private Long nombreMED; // Mise en Demeure
    private Long nombreCommandements;
    private Double montantTotalRecouvrement;
    
    // Propriétés (immobilier + véhicules + concessions)
    private Long nombreTotalProprietes;
    private Long nombreProprietesImmobilieres;
    private Long nombreVehicules;
    private Long nombrePlaques;
    private Long nombreConcessionsMinières;
    
    // Répartitions
    private Map<String, Long> repartitionParTypeImpot;
    private Map<String, Double> repartitionMontantsParType;
    private Map<String, Long> repartitionParStatut;
    
    public StatistiquesDashboardDTO() {
    }
    
    // Getters et Setters
    public Long getNombreTotalContribuables() {
        return nombreTotalContribuables;
    }
    
    public void setNombreTotalContribuables(Long nombreTotalContribuables) {
        this.nombreTotalContribuables = nombreTotalContribuables;
    }
    
    public Long getNombreContribuablesActifs() {
        return nombreContribuablesActifs;
    }
    
    public void setNombreContribuablesActifs(Long nombreContribuablesActifs) {
        this.nombreContribuablesActifs = nombreContribuablesActifs;
    }
    
    public Long getNombreContribuablesInactifs() {
        return nombreContribuablesInactifs;
    }
    
    public void setNombreContribuablesInactifs(Long nombreContribuablesInactifs) {
        this.nombreContribuablesInactifs = nombreContribuablesInactifs;
    }
    
    public Long getNombreTotalTaxations() {
        return nombreTotalTaxations;
    }
    
    public void setNombreTotalTaxations(Long nombreTotalTaxations) {
        this.nombreTotalTaxations = nombreTotalTaxations;
    }
    
    public Double getMontantTotalTaxations() {
        return montantTotalTaxations;
    }
    
    public void setMontantTotalTaxations(Double montantTotalTaxations) {
        this.montantTotalTaxations = montantTotalTaxations;
    }
    
    public Long getNombreTaxationsPayees() {
        return nombreTaxationsPayees;
    }
    
    public void setNombreTaxationsPayees(Long nombreTaxationsPayees) {
        this.nombreTaxationsPayees = nombreTaxationsPayees;
    }
    
    public Long getNombreTaxationsEnAttente() {
        return nombreTaxationsEnAttente;
    }
    
    public void setNombreTaxationsEnAttente(Long nombreTaxationsEnAttente) {
        this.nombreTaxationsEnAttente = nombreTaxationsEnAttente;
    }
    
    public Long getNombreTaxationsEnRetard() {
        return nombreTaxationsEnRetard;
    }
    
    public void setNombreTaxationsEnRetard(Long nombreTaxationsEnRetard) {
        this.nombreTaxationsEnRetard = nombreTaxationsEnRetard;
    }
    
    public Long getNombreTotalPaiements() {
        return nombreTotalPaiements;
    }
    
    public void setNombreTotalPaiements(Long nombreTotalPaiements) {
        this.nombreTotalPaiements = nombreTotalPaiements;
    }
    
    public Double getMontantTotalPaiements() {
        return montantTotalPaiements;
    }
    
    public void setMontantTotalPaiements(Double montantTotalPaiements) {
        this.montantTotalPaiements = montantTotalPaiements;
    }
    
    public Double getMontantPaiementsAujourdhui() {
        return montantPaiementsAujourdhui;
    }
    
    public void setMontantPaiementsAujourdhui(Double montantPaiementsAujourdhui) {
        this.montantPaiementsAujourdhui = montantPaiementsAujourdhui;
    }
    
    public Double getMontantPaiementsCeMois() {
        return montantPaiementsCeMois;
    }
    
    public void setMontantPaiementsCeMois(Double montantPaiementsCeMois) {
        this.montantPaiementsCeMois = montantPaiementsCeMois;
    }
    
    public Long getNombreTotalRelances() {
        return nombreTotalRelances;
    }
    
    public void setNombreTotalRelances(Long nombreTotalRelances) {
        this.nombreTotalRelances = nombreTotalRelances;
    }
    
    public Long getNombreRelancesEnvoyees() {
        return nombreRelancesEnvoyees;
    }
    
    public void setNombreRelancesEnvoyees(Long nombreRelancesEnvoyees) {
        this.nombreRelancesEnvoyees = nombreRelancesEnvoyees;
    }
    
    public Long getNombreRelancesEnAttente() {
        return nombreRelancesEnAttente;
    }
    
    public void setNombreRelancesEnAttente(Long nombreRelancesEnAttente) {
        this.nombreRelancesEnAttente = nombreRelancesEnAttente;
    }
    
    public Long getNombreDossiersRecouvrement() {
        return nombreDossiersRecouvrement;
    }
    
    public void setNombreDossiersRecouvrement(Long nombreDossiersRecouvrement) {
        this.nombreDossiersRecouvrement = nombreDossiersRecouvrement;
    }
    
    public Long getNombreATD() {
        return nombreATD;
    }
    
    public void setNombreATD(Long nombreATD) {
        this.nombreATD = nombreATD;
    }
    
    public Long getNombreMED() {
        return nombreMED;
    }
    
    public void setNombreMED(Long nombreMED) {
        this.nombreMED = nombreMED;
    }
    
    public Long getNombreCommandements() {
        return nombreCommandements;
    }
    
    public void setNombreCommandements(Long nombreCommandements) {
        this.nombreCommandements = nombreCommandements;
    }
    
    public Double getMontantTotalRecouvrement() {
        return montantTotalRecouvrement;
    }
    
    public void setMontantTotalRecouvrement(Double montantTotalRecouvrement) {
        this.montantTotalRecouvrement = montantTotalRecouvrement;
    }
    
    public Long getNombreTotalProprietes() {
        return nombreTotalProprietes;
    }
    
    public void setNombreTotalProprietes(Long nombreTotalProprietes) {
        this.nombreTotalProprietes = nombreTotalProprietes;
    }
    
    public Long getNombreProprietesImmobilieres() {
        return nombreProprietesImmobilieres;
    }
    
    public void setNombreProprietesImmobilieres(Long nombreProprietesImmobilieres) {
        this.nombreProprietesImmobilieres = nombreProprietesImmobilieres;
    }
    
    public Long getNombreVehicules() {
        return nombreVehicules;
    }
    
    public void setNombreVehicules(Long nombreVehicules) {
        this.nombreVehicules = nombreVehicules;
    }
    
    public Long getNombrePlaques() {
        return nombrePlaques;
    }
    
    public void setNombrePlaques(Long nombrePlaques) {
        this.nombrePlaques = nombrePlaques;
    }
    
    public Long getNombreConcessionsMinières() {
        return nombreConcessionsMinières;
    }
    
    public void setNombreConcessionsMinières(Long nombreConcessionsMinières) {
        this.nombreConcessionsMinières = nombreConcessionsMinières;
    }
    
    public Map<String, Long> getRepartitionParTypeImpot() {
        return repartitionParTypeImpot;
    }
    
    public void setRepartitionParTypeImpot(Map<String, Long> repartitionParTypeImpot) {
        this.repartitionParTypeImpot = repartitionParTypeImpot;
    }
    
    public Map<String, Double> getRepartitionMontantsParType() {
        return repartitionMontantsParType;
    }
    
    public void setRepartitionMontantsParType(Map<String, Double> repartitionMontantsParType) {
        this.repartitionMontantsParType = repartitionMontantsParType;
    }
    
    public Map<String, Long> getRepartitionParStatut() {
        return repartitionParStatut;
    }
    
    public void setRepartitionParStatut(Map<String, Long> repartitionParStatut) {
        this.repartitionParStatut = repartitionParStatut;
    }
}
