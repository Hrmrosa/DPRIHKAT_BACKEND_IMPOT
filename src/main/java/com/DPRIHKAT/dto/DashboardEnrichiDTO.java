package com.DPRIHKAT.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DTO enrichi pour le Dashboard avec données détaillées
 * Similaire au module de rapports avec données pour graphiques
 * 
 * @author amateur
 */
public class DashboardEnrichiDTO {
    
    private Date dateGeneration;
    
    // Statistiques globales
    private StatistiquesDashboardDTO statistiquesGlobales;
    
    // Données détaillées par entité
    private DonneesDashboardContribuablesDTO contribuables;
    private DonneesDashboardTaxationsDTO taxations;
    private DonneesDashboardPaiementsDTO paiements;
    private DonneesDashboardRelancesDTO relances;
    private DonneesDashboardRecouvrementDTO recouvrements;
    private DonneesDashboardProprietesDTO proprietes;
    
    // Données pour graphiques
    private DonneesGraphiqueDTO donneesGraphiques;
    
    public DashboardEnrichiDTO() {
        this.dateGeneration = new Date();
    }
    
    // Getters et Setters
    public Date getDateGeneration() {
        return dateGeneration;
    }
    
    public void setDateGeneration(Date dateGeneration) {
        this.dateGeneration = dateGeneration;
    }
    
    public StatistiquesDashboardDTO getStatistiquesGlobales() {
        return statistiquesGlobales;
    }
    
    public void setStatistiquesGlobales(StatistiquesDashboardDTO statistiquesGlobales) {
        this.statistiquesGlobales = statistiquesGlobales;
    }
    
    public DonneesDashboardContribuablesDTO getContribuables() {
        return contribuables;
    }
    
    public void setContribuables(DonneesDashboardContribuablesDTO contribuables) {
        this.contribuables = contribuables;
    }
    
    public DonneesDashboardTaxationsDTO getTaxations() {
        return taxations;
    }
    
    public void setTaxations(DonneesDashboardTaxationsDTO taxations) {
        this.taxations = taxations;
    }
    
    public DonneesDashboardPaiementsDTO getPaiements() {
        return paiements;
    }
    
    public void setPaiements(DonneesDashboardPaiementsDTO paiements) {
        this.paiements = paiements;
    }
    
    public DonneesDashboardRelancesDTO getRelances() {
        return relances;
    }
    
    public void setRelances(DonneesDashboardRelancesDTO relances) {
        this.relances = relances;
    }
    
    public DonneesDashboardRecouvrementDTO getRecouvrements() {
        return recouvrements;
    }
    
    public void setRecouvrements(DonneesDashboardRecouvrementDTO recouvrements) {
        this.recouvrements = recouvrements;
    }
    
    public DonneesDashboardProprietesDTO getProprietes() {
        return proprietes;
    }
    
    public void setProprietes(DonneesDashboardProprietesDTO proprietes) {
        this.proprietes = proprietes;
    }
    
    public DonneesGraphiqueDTO getDonneesGraphiques() {
        return donneesGraphiques;
    }
    
    public void setDonneesGraphiques(DonneesGraphiqueDTO donneesGraphiques) {
        this.donneesGraphiques = donneesGraphiques;
    }
}
