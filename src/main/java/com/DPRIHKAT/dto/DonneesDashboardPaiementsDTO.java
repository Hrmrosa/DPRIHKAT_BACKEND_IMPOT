package com.DPRIHKAT.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO pour les données des paiements dans le Dashboard
 * 
 * @author amateur
 */
public class DonneesDashboardPaiementsDTO {
    
    private Long total;
    private Double montantTotal;
    private Double montantAujourdhui;
    private Double montantCeMois;
    private Double montantCetteAnnee;
    
    // Répartition par mode de paiement
    private Map<String, Long> repartitionParMode;
    private Map<String, Double> montantsParMode;
    
    // Paiements récents
    private List<PaiementResumeDTO> paiementsRecents;
    
    // Évolution mensuelle
    private Map<String, Double> evolutionMensuelle;
    
    public DonneesDashboardPaiementsDTO() {
    }
    
    // Getters et Setters
    public Long getTotal() {
        return total;
    }
    
    public void setTotal(Long total) {
        this.total = total;
    }
    
    public Double getMontantTotal() {
        return montantTotal;
    }
    
    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }
    
    public Double getMontantAujourdhui() {
        return montantAujourdhui;
    }
    
    public void setMontantAujourdhui(Double montantAujourdhui) {
        this.montantAujourdhui = montantAujourdhui;
    }
    
    public Double getMontantCeMois() {
        return montantCeMois;
    }
    
    public void setMontantCeMois(Double montantCeMois) {
        this.montantCeMois = montantCeMois;
    }
    
    public Double getMontantCetteAnnee() {
        return montantCetteAnnee;
    }
    
    public void setMontantCetteAnnee(Double montantCetteAnnee) {
        this.montantCetteAnnee = montantCetteAnnee;
    }
    
    public Map<String, Long> getRepartitionParMode() {
        return repartitionParMode;
    }
    
    public void setRepartitionParMode(Map<String, Long> repartitionParMode) {
        this.repartitionParMode = repartitionParMode;
    }
    
    public Map<String, Double> getMontantsParMode() {
        return montantsParMode;
    }
    
    public void setMontantsParMode(Map<String, Double> montantsParMode) {
        this.montantsParMode = montantsParMode;
    }
    
    public List<PaiementResumeDTO> getPaiementsRecents() {
        return paiementsRecents;
    }
    
    public void setPaiementsRecents(List<PaiementResumeDTO> paiementsRecents) {
        this.paiementsRecents = paiementsRecents;
    }
    
    public Map<String, Double> getEvolutionMensuelle() {
        return evolutionMensuelle;
    }
    
    public void setEvolutionMensuelle(Map<String, Double> evolutionMensuelle) {
        this.evolutionMensuelle = evolutionMensuelle;
    }
    
    /**
     * Classe interne pour le résumé d'un paiement
     */
    public static class PaiementResumeDTO {
        private String id;
        private Double montant;
        private String mode;
        private String statut;
        private String contribuableNom;
        private String datePaiement;
        private String reference;
        
        public PaiementResumeDTO() {
        }
        
        // Getters et Setters
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public Double getMontant() {
            return montant;
        }
        
        public void setMontant(Double montant) {
            this.montant = montant;
        }
        
        public String getMode() {
            return mode;
        }
        
        public void setMode(String mode) {
            this.mode = mode;
        }
        
        public String getStatut() {
            return statut;
        }
        
        public void setStatut(String statut) {
            this.statut = statut;
        }
        
        public String getContribuableNom() {
            return contribuableNom;
        }
        
        public void setContribuableNom(String contribuableNom) {
            this.contribuableNom = contribuableNom;
        }
        
        public String getDatePaiement() {
            return datePaiement;
        }
        
        public void setDatePaiement(String datePaiement) {
            this.datePaiement = datePaiement;
        }
        
        public String getReference() {
            return reference;
        }
        
        public void setReference(String reference) {
            this.reference = reference;
        }
    }
}
