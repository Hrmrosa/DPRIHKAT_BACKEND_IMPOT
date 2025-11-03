package com.DPRIHKAT.dto;

import java.util.List;

/**
 * DTO pour les données des contribuables dans le Dashboard
 * 
 * @author amateur
 */
public class DonneesDashboardContribuablesDTO {
    
    private Long total;
    private Long actifs;
    private Long inactifs;
    private Long avecProprietes;
    private Long avecVehicules;
    private Long avecConcessionsMinières;
    
    // Top contribuables
    private List<ContribuableResumeDTO> topContribuables;
    
    // Nouveaux contribuables (ce mois)
    private List<ContribuableResumeDTO> nouveauxContribuables;
    
    // Contribuables en retard
    private List<ContribuableResumeDTO> contribuablesEnRetard;
    
    public DonneesDashboardContribuablesDTO() {
    }
    
    // Getters et Setters
    public Long getTotal() {
        return total;
    }
    
    public void setTotal(Long total) {
        this.total = total;
    }
    
    public Long getActifs() {
        return actifs;
    }
    
    public void setActifs(Long actifs) {
        this.actifs = actifs;
    }
    
    public Long getInactifs() {
        return inactifs;
    }
    
    public void setInactifs(Long inactifs) {
        this.inactifs = inactifs;
    }
    
    public Long getAvecProprietes() {
        return avecProprietes;
    }
    
    public void setAvecProprietes(Long avecProprietes) {
        this.avecProprietes = avecProprietes;
    }
    
    public Long getAvecVehicules() {
        return avecVehicules;
    }
    
    public void setAvecVehicules(Long avecVehicules) {
        this.avecVehicules = avecVehicules;
    }
    
    public Long getAvecConcessionsMinières() {
        return avecConcessionsMinières;
    }
    
    public void setAvecConcessionsMinières(Long avecConcessionsMinières) {
        this.avecConcessionsMinières = avecConcessionsMinières;
    }
    
    public List<ContribuableResumeDTO> getTopContribuables() {
        return topContribuables;
    }
    
    public void setTopContribuables(List<ContribuableResumeDTO> topContribuables) {
        this.topContribuables = topContribuables;
    }
    
    public List<ContribuableResumeDTO> getNouveauxContribuables() {
        return nouveauxContribuables;
    }
    
    public void setNouveauxContribuables(List<ContribuableResumeDTO> nouveauxContribuables) {
        this.nouveauxContribuables = nouveauxContribuables;
    }
    
    public List<ContribuableResumeDTO> getContribuablesEnRetard() {
        return contribuablesEnRetard;
    }
    
    public void setContribuablesEnRetard(List<ContribuableResumeDTO> contribuablesEnRetard) {
        this.contribuablesEnRetard = contribuablesEnRetard;
    }
    
    /**
     * Classe interne pour le résumé d'un contribuable
     */
    public static class ContribuableResumeDTO {
        private String id;
        private String nom;
        private String numeroContribuable;
        private String typeContribuable;
        private Long nombreProprietes;
        private Long nombreVehicules;
        private Double montantTotalTaxe;
        private Double montantTotalPaye;
        private Double montantRestant;
        private String statut;
        
        public ContribuableResumeDTO() {
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
        
        public String getNumeroContribuable() {
            return numeroContribuable;
        }
        
        public void setNumeroContribuable(String numeroContribuable) {
            this.numeroContribuable = numeroContribuable;
        }
        
        public String getTypeContribuable() {
            return typeContribuable;
        }
        
        public void setTypeContribuable(String typeContribuable) {
            this.typeContribuable = typeContribuable;
        }
        
        public Long getNombreProprietes() {
            return nombreProprietes;
        }
        
        public void setNombreProprietes(Long nombreProprietes) {
            this.nombreProprietes = nombreProprietes;
        }
        
        public Long getNombreVehicules() {
            return nombreVehicules;
        }
        
        public void setNombreVehicules(Long nombreVehicules) {
            this.nombreVehicules = nombreVehicules;
        }
        
        public Double getMontantTotalTaxe() {
            return montantTotalTaxe;
        }
        
        public void setMontantTotalTaxe(Double montantTotalTaxe) {
            this.montantTotalTaxe = montantTotalTaxe;
        }
        
        public Double getMontantTotalPaye() {
            return montantTotalPaye;
        }
        
        public void setMontantTotalPaye(Double montantTotalPaye) {
            this.montantTotalPaye = montantTotalPaye;
        }
        
        public Double getMontantRestant() {
            return montantRestant;
        }
        
        public void setMontantRestant(Double montantRestant) {
            this.montantRestant = montantRestant;
        }
        
        public String getStatut() {
            return statut;
        }
        
        public void setStatut(String statut) {
            this.statut = statut;
        }
    }
}
