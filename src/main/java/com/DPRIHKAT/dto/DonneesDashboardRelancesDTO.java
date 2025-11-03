package com.DPRIHKAT.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO pour les données des relances dans le Dashboard
 * 
 * @author amateur
 */
public class DonneesDashboardRelancesDTO {
    
    private Long total;
    private Long envoyees;
    private Long enAttente;
    private Long repondues;
    
    // Répartition par type
    private Map<String, Long> repartitionParType;
    
    // Relances récentes
    private List<RelanceResumeDTO> relancesRecentes;
    
    // Relances en attente
    private List<RelanceResumeDTO> relancesEnAttente;
    
    // Taux de réponse
    private Double tauxReponse;
    
    public DonneesDashboardRelancesDTO() {
    }
    
    // Getters et Setters
    public Long getTotal() {
        return total;
    }
    
    public void setTotal(Long total) {
        this.total = total;
    }
    
    public Long getEnvoyees() {
        return envoyees;
    }
    
    public void setEnvoyees(Long envoyees) {
        this.envoyees = envoyees;
    }
    
    public Long getEnAttente() {
        return enAttente;
    }
    
    public void setEnAttente(Long enAttente) {
        this.enAttente = enAttente;
    }
    
    public Long getRepondues() {
        return repondues;
    }
    
    public void setRepondues(Long repondues) {
        this.repondues = repondues;
    }
    
    public Map<String, Long> getRepartitionParType() {
        return repartitionParType;
    }
    
    public void setRepartitionParType(Map<String, Long> repartitionParType) {
        this.repartitionParType = repartitionParType;
    }
    
    public List<RelanceResumeDTO> getRelancesRecentes() {
        return relancesRecentes;
    }
    
    public void setRelancesRecentes(List<RelanceResumeDTO> relancesRecentes) {
        this.relancesRecentes = relancesRecentes;
    }
    
    public List<RelanceResumeDTO> getRelancesEnAttente() {
        return relancesEnAttente;
    }
    
    public void setRelancesEnAttente(List<RelanceResumeDTO> relancesEnAttente) {
        this.relancesEnAttente = relancesEnAttente;
    }
    
    public Double getTauxReponse() {
        return tauxReponse;
    }
    
    public void setTauxReponse(Double tauxReponse) {
        this.tauxReponse = tauxReponse;
    }
    
    /**
     * Classe interne pour le résumé d'une relance
     */
    public static class RelanceResumeDTO {
        private String id;
        private String type;
        private String statut;
        private String contribuableNom;
        private String dateEnvoi;
        private String contenu;
        
        public RelanceResumeDTO() {
        }
        
        // Getters et Setters
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
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
        
        public String getDateEnvoi() {
            return dateEnvoi;
        }
        
        public void setDateEnvoi(String dateEnvoi) {
            this.dateEnvoi = dateEnvoi;
        }
        
        public String getContenu() {
            return contenu;
        }
        
        public void setContenu(String contenu) {
            this.contenu = contenu;
        }
    }
}
