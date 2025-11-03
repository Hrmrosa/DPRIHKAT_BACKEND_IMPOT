package com.DPRIHKAT.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO pour les données des taxations dans le Dashboard
 * 
 * @author amateur
 */
public class DonneesDashboardTaxationsDTO {
    
    private Long total;
    private Long payees;
    private Long enAttente;
    private Long enRetard;
    private Long partiellementPayees;
    private Double montantTotal;
    private Double montantPaye;
    private Double montantRestant;
    
    // Répartition par type d'impôt
    private Map<String, Long> repartitionParType;
    private Map<String, Double> montantsParType;
    
    // Répartition par statut
    private Map<String, Long> repartitionParStatut;
    
    // Taxations récentes
    private List<TaxationResumeDTO> taxationsRecentes;
    
    // Taxations en retard
    private List<TaxationResumeDTO> taxationsEnRetard;
    
    // Top agents taxateurs
    private List<AgentPerformanceDTO> topAgents;
    
    public DonneesDashboardTaxationsDTO() {
    }
    
    // Getters et Setters
    public Long getTotal() {
        return total;
    }
    
    public void setTotal(Long total) {
        this.total = total;
    }
    
    public Long getPayees() {
        return payees;
    }
    
    public void setPayees(Long payees) {
        this.payees = payees;
    }
    
    public Long getEnAttente() {
        return enAttente;
    }
    
    public void setEnAttente(Long enAttente) {
        this.enAttente = enAttente;
    }
    
    public Long getEnRetard() {
        return enRetard;
    }
    
    public void setEnRetard(Long enRetard) {
        this.enRetard = enRetard;
    }
    
    public Long getPartiellementPayees() {
        return partiellementPayees;
    }
    
    public void setPartiellementPayees(Long partiellementPayees) {
        this.partiellementPayees = partiellementPayees;
    }
    
    public Double getMontantTotal() {
        return montantTotal;
    }
    
    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }
    
    public Double getMontantPaye() {
        return montantPaye;
    }
    
    public void setMontantPaye(Double montantPaye) {
        this.montantPaye = montantPaye;
    }
    
    public Double getMontantRestant() {
        return montantRestant;
    }
    
    public void setMontantRestant(Double montantRestant) {
        this.montantRestant = montantRestant;
    }
    
    public Map<String, Long> getRepartitionParType() {
        return repartitionParType;
    }
    
    public void setRepartitionParType(Map<String, Long> repartitionParType) {
        this.repartitionParType = repartitionParType;
    }
    
    public Map<String, Double> getMontantsParType() {
        return montantsParType;
    }
    
    public void setMontantsParType(Map<String, Double> montantsParType) {
        this.montantsParType = montantsParType;
    }
    
    public Map<String, Long> getRepartitionParStatut() {
        return repartitionParStatut;
    }
    
    public void setRepartitionParStatut(Map<String, Long> repartitionParStatut) {
        this.repartitionParStatut = repartitionParStatut;
    }
    
    public List<TaxationResumeDTO> getTaxationsRecentes() {
        return taxationsRecentes;
    }
    
    public void setTaxationsRecentes(List<TaxationResumeDTO> taxationsRecentes) {
        this.taxationsRecentes = taxationsRecentes;
    }
    
    public List<TaxationResumeDTO> getTaxationsEnRetard() {
        return taxationsEnRetard;
    }
    
    public void setTaxationsEnRetard(List<TaxationResumeDTO> taxationsEnRetard) {
        this.taxationsEnRetard = taxationsEnRetard;
    }
    
    public List<AgentPerformanceDTO> getTopAgents() {
        return topAgents;
    }
    
    public void setTopAgents(List<AgentPerformanceDTO> topAgents) {
        this.topAgents = topAgents;
    }
    
    /**
     * Classe interne pour le résumé d'une taxation
     */
    public static class TaxationResumeDTO {
        private String id;
        private String numeroTaxation;
        private String typeImpot;
        private Double montant;
        private String statut;
        private String contribuableNom;
        private String agentNom;
        private String dateCreation;
        private String dateEcheance;
        
        public TaxationResumeDTO() {
        }
        
        // Getters et Setters
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getNumeroTaxation() {
            return numeroTaxation;
        }
        
        public void setNumeroTaxation(String numeroTaxation) {
            this.numeroTaxation = numeroTaxation;
        }
        
        public String getTypeImpot() {
            return typeImpot;
        }
        
        public void setTypeImpot(String typeImpot) {
            this.typeImpot = typeImpot;
        }
        
        public Double getMontant() {
            return montant;
        }
        
        public void setMontant(Double montant) {
            this.montant = montant;
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
        
        public String getAgentNom() {
            return agentNom;
        }
        
        public void setAgentNom(String agentNom) {
            this.agentNom = agentNom;
        }
        
        public String getDateCreation() {
            return dateCreation;
        }
        
        public void setDateCreation(String dateCreation) {
            this.dateCreation = dateCreation;
        }
        
        public String getDateEcheance() {
            return dateEcheance;
        }
        
        public void setDateEcheance(String dateEcheance) {
            this.dateEcheance = dateEcheance;
        }
    }
    
    /**
     * Classe interne pour la performance d'un agent
     */
    public static class AgentPerformanceDTO {
        private String id;
        private String nom;
        private String matricule;
        private Long nombreTaxations;
        private Double montantTotal;
        private Double pourcentage;
        
        public AgentPerformanceDTO() {
        }
        
        // Getters et Setters
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getNom() {
            return nom;
        }
        
        public void setNom(String nom) {
            this.nom = nom;
        }
        
        public String getMatricule() {
            return matricule;
        }
        
        public void setMatricule(String matricule) {
            this.matricule = matricule;
        }
        
        public Long getNombreTaxations() {
            return nombreTaxations;
        }
        
        public void setNombreTaxations(Long nombreTaxations) {
            this.nombreTaxations = nombreTaxations;
        }
        
        public Double getMontantTotal() {
            return montantTotal;
        }
        
        public void setMontantTotal(Double montantTotal) {
            this.montantTotal = montantTotal;
        }
        
        public Double getPourcentage() {
            return pourcentage;
        }
        
        public void setPourcentage(Double pourcentage) {
            this.pourcentage = pourcentage;
        }
    }
}
