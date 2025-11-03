package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.PeriodeRapport;
import com.DPRIHKAT.entity.enums.TypeRapport;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DTO pour la réponse complète d'un rapport
 * 
 * @author amateur
 */
public class RapportResponseDTO {
    
    private TypeRapport typeRapport;
    private PeriodeRapport periode;
    private Date dateDebut;
    private Date dateFin;
    private Date dateGeneration;
    private String agentNom;
    private String agentMatricule;
    
    // Statistiques globales
    private StatistiquesGlobalesDTO statistiquesGlobales;
    
    // Données détaillées par type
    private List<RapportTaxationDTO> taxations;
    private List<RapportPaiementDTO> paiements;
    private List<RapportRelanceDTO> relances;
    private List<RapportRecouvrementDTO> recouvrements;
    private List<RapportCollecteDTO> collectes;
    
    // Données pour graphiques
    private DonneesGraphiqueDTO donneesGraphiques;
    
    public RapportResponseDTO() {
        this.dateGeneration = new Date();
    }
    
    // Getters et Setters
    public TypeRapport getTypeRapport() {
        return typeRapport;
    }
    
    public void setTypeRapport(TypeRapport typeRapport) {
        this.typeRapport = typeRapport;
    }
    
    public PeriodeRapport getPeriode() {
        return periode;
    }
    
    public void setPeriode(PeriodeRapport periode) {
        this.periode = periode;
    }
    
    public Date getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public Date getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
    
    public Date getDateGeneration() {
        return dateGeneration;
    }
    
    public void setDateGeneration(Date dateGeneration) {
        this.dateGeneration = dateGeneration;
    }
    
    public String getAgentNom() {
        return agentNom;
    }
    
    public void setAgentNom(String agentNom) {
        this.agentNom = agentNom;
    }
    
    public String getAgentMatricule() {
        return agentMatricule;
    }
    
    public void setAgentMatricule(String agentMatricule) {
        this.agentMatricule = agentMatricule;
    }
    
    public StatistiquesGlobalesDTO getStatistiquesGlobales() {
        return statistiquesGlobales;
    }
    
    public void setStatistiquesGlobales(StatistiquesGlobalesDTO statistiquesGlobales) {
        this.statistiquesGlobales = statistiquesGlobales;
    }
    
    public List<RapportTaxationDTO> getTaxations() {
        return taxations;
    }
    
    public void setTaxations(List<RapportTaxationDTO> taxations) {
        this.taxations = taxations;
    }
    
    public List<RapportPaiementDTO> getPaiements() {
        return paiements;
    }
    
    public void setPaiements(List<RapportPaiementDTO> paiements) {
        this.paiements = paiements;
    }
    
    public List<RapportRelanceDTO> getRelances() {
        return relances;
    }
    
    public void setRelances(List<RapportRelanceDTO> relances) {
        this.relances = relances;
    }
    
    public List<RapportRecouvrementDTO> getRecouvrements() {
        return recouvrements;
    }
    
    public void setRecouvrements(List<RapportRecouvrementDTO> recouvrements) {
        this.recouvrements = recouvrements;
    }
    
    public List<RapportCollecteDTO> getCollectes() {
        return collectes;
    }
    
    public void setCollectes(List<RapportCollecteDTO> collectes) {
        this.collectes = collectes;
    }
    
    public DonneesGraphiqueDTO getDonneesGraphiques() {
        return donneesGraphiques;
    }
    
    public void setDonneesGraphiques(DonneesGraphiqueDTO donneesGraphiques) {
        this.donneesGraphiques = donneesGraphiques;
    }
    
    /**
     * Classe interne pour les statistiques globales
     */
    public static class StatistiquesGlobalesDTO {
        private Long nombreTaxations;
        private Double montantTotalTaxations;
        private Long nombrePaiements;
        private Double montantTotalPaiements;
        private Long nombreRelances;
        private Long nombreActesRecouvrement;
        private Double montantTotalRecouvrement;
        private Long nombreCollectes;
        private Map<String, Long> repartitionParType;
        private Map<String, Double> repartitionMontantsParType;
        
        public StatistiquesGlobalesDTO() {
        }
        
        // Getters et Setters
        public Long getNombreTaxations() {
            return nombreTaxations;
        }
        
        public void setNombreTaxations(Long nombreTaxations) {
            this.nombreTaxations = nombreTaxations;
        }
        
        public Double getMontantTotalTaxations() {
            return montantTotalTaxations;
        }
        
        public void setMontantTotalTaxations(Double montantTotalTaxations) {
            this.montantTotalTaxations = montantTotalTaxations;
        }
        
        public Long getNombrePaiements() {
            return nombrePaiements;
        }
        
        public void setNombrePaiements(Long nombrePaiements) {
            this.nombrePaiements = nombrePaiements;
        }
        
        public Double getMontantTotalPaiements() {
            return montantTotalPaiements;
        }
        
        public void setMontantTotalPaiements(Double montantTotalPaiements) {
            this.montantTotalPaiements = montantTotalPaiements;
        }
        
        public Long getNombreRelances() {
            return nombreRelances;
        }
        
        public void setNombreRelances(Long nombreRelances) {
            this.nombreRelances = nombreRelances;
        }
        
        public Long getNombreActesRecouvrement() {
            return nombreActesRecouvrement;
        }
        
        public void setNombreActesRecouvrement(Long nombreActesRecouvrement) {
            this.nombreActesRecouvrement = nombreActesRecouvrement;
        }
        
        public Double getMontantTotalRecouvrement() {
            return montantTotalRecouvrement;
        }
        
        public void setMontantTotalRecouvrement(Double montantTotalRecouvrement) {
            this.montantTotalRecouvrement = montantTotalRecouvrement;
        }
        
        public Long getNombreCollectes() {
            return nombreCollectes;
        }
        
        public void setNombreCollectes(Long nombreCollectes) {
            this.nombreCollectes = nombreCollectes;
        }
        
        public Map<String, Long> getRepartitionParType() {
            return repartitionParType;
        }
        
        public void setRepartitionParType(Map<String, Long> repartitionParType) {
            this.repartitionParType = repartitionParType;
        }
        
        public Map<String, Double> getRepartitionMontantsParType() {
            return repartitionMontantsParType;
        }
        
        public void setRepartitionMontantsParType(Map<String, Double> repartitionMontantsParType) {
            this.repartitionMontantsParType = repartitionMontantsParType;
        }
    }
}
