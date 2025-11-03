package com.DPRIHKAT.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO pour les données des propriétés dans le Dashboard
 * Inclut immobilier, véhicules, plaques et concessions minières
 * 
 * @author amateur
 */
public class DonneesDashboardProprietesDTO {
    
    // Propriétés immobilières
    private Long nombreProprietesImmobilieres;
    private Long nombreParcelles;
    private Long nombreBatiments;
    private Double superficieTotale;
    
    // Véhicules et plaques
    private Long nombreVehicules;
    private Long nombrePlaques;
    private Long nombrePlaquesActives;
    private Long nombrePlaquesExpirees;
    
    // Concessions minières
    private Long nombreConcessionsMinières;
    private Long nombreConcessionsActives;
    private Double superficieConcessionsTotal;
    
    // Répartition par type de propriété
    private Map<String, Long> repartitionParType;
    
    // Répartition par zone géographique
    private Map<String, Long> repartitionParZone;
    
    // Propriétés récentes
    private List<ProprieteResumeDTO> proprietesRecentes;
    
    // Véhicules récents
    private List<VehiculeResumeDTO> vehiculesRecents;
    
    // Concessions récentes
    private List<ConcessionResumeDTO> concessionsRecentes;
    
    public DonneesDashboardProprietesDTO() {
    }
    
    // Getters et Setters
    public Long getNombreProprietesImmobilieres() {
        return nombreProprietesImmobilieres;
    }
    
    public void setNombreProprietesImmobilieres(Long nombreProprietesImmobilieres) {
        this.nombreProprietesImmobilieres = nombreProprietesImmobilieres;
    }
    
    public Long getNombreParcelles() {
        return nombreParcelles;
    }
    
    public void setNombreParcelles(Long nombreParcelles) {
        this.nombreParcelles = nombreParcelles;
    }
    
    public Long getNombreBatiments() {
        return nombreBatiments;
    }
    
    public void setNombreBatiments(Long nombreBatiments) {
        this.nombreBatiments = nombreBatiments;
    }
    
    public Double getSuperficieTotale() {
        return superficieTotale;
    }
    
    public void setSuperficieTotale(Double superficieTotale) {
        this.superficieTotale = superficieTotale;
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
    
    public Long getNombrePlaquesActives() {
        return nombrePlaquesActives;
    }
    
    public void setNombrePlaquesActives(Long nombrePlaquesActives) {
        this.nombrePlaquesActives = nombrePlaquesActives;
    }
    
    public Long getNombrePlaquesExpirees() {
        return nombrePlaquesExpirees;
    }
    
    public void setNombrePlaquesExpirees(Long nombrePlaquesExpirees) {
        this.nombrePlaquesExpirees = nombrePlaquesExpirees;
    }
    
    public Long getNombreConcessionsMinières() {
        return nombreConcessionsMinières;
    }
    
    public void setNombreConcessionsMinières(Long nombreConcessionsMinières) {
        this.nombreConcessionsMinières = nombreConcessionsMinières;
    }
    
    public Long getNombreConcessionsActives() {
        return nombreConcessionsActives;
    }
    
    public void setNombreConcessionsActives(Long nombreConcessionsActives) {
        this.nombreConcessionsActives = nombreConcessionsActives;
    }
    
    public Double getSuperficieConcessionsTotal() {
        return superficieConcessionsTotal;
    }
    
    public void setSuperficieConcessionsTotal(Double superficieConcessionsTotal) {
        this.superficieConcessionsTotal = superficieConcessionsTotal;
    }
    
    public Map<String, Long> getRepartitionParType() {
        return repartitionParType;
    }
    
    public void setRepartitionParType(Map<String, Long> repartitionParType) {
        this.repartitionParType = repartitionParType;
    }
    
    public Map<String, Long> getRepartitionParZone() {
        return repartitionParZone;
    }
    
    public void setRepartitionParZone(Map<String, Long> repartitionParZone) {
        this.repartitionParZone = repartitionParZone;
    }
    
    public List<ProprieteResumeDTO> getProprietesRecentes() {
        return proprietesRecentes;
    }
    
    public void setProprietesRecentes(List<ProprieteResumeDTO> proprietesRecentes) {
        this.proprietesRecentes = proprietesRecentes;
    }
    
    public List<VehiculeResumeDTO> getVehiculesRecents() {
        return vehiculesRecents;
    }
    
    public void setVehiculesRecents(List<VehiculeResumeDTO> vehiculesRecents) {
        this.vehiculesRecents = vehiculesRecents;
    }
    
    public List<ConcessionResumeDTO> getConcessionsRecentes() {
        return concessionsRecentes;
    }
    
    public void setConcessionsRecentes(List<ConcessionResumeDTO> concessionsRecentes) {
        this.concessionsRecentes = concessionsRecentes;
    }
    
    /**
     * Classe interne pour le résumé d'une propriété immobilière
     */
    public static class ProprieteResumeDTO {
        private String id;
        private String numeroParcelle;
        private String adresse;
        private String type;
        private Double superficie;
        private String proprietaireNom;
        private String commune;
        private String quartier;
        
        public ProprieteResumeDTO() {
        }
        
        // Getters et Setters
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getNumeroParcelle() {
            return numeroParcelle;
        }
        
        public void setNumeroParcelle(String numeroParcelle) {
            this.numeroParcelle = numeroParcelle;
        }
        
        public String getAdresse() {
            return adresse;
        }
        
        public void setAdresse(String adresse) {
            this.adresse = adresse;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public Double getSuperficie() {
            return superficie;
        }
        
        public void setSuperficie(Double superficie) {
            this.superficie = superficie;
        }
        
        public String getProprietaireNom() {
            return proprietaireNom;
        }
        
        public void setProprietaireNom(String proprietaireNom) {
            this.proprietaireNom = proprietaireNom;
        }
        
        public String getCommune() {
            return commune;
        }
        
        public void setCommune(String commune) {
            this.commune = commune;
        }
        
        public String getQuartier() {
            return quartier;
        }
        
        public void setQuartier(String quartier) {
            this.quartier = quartier;
        }
    }
    
    /**
     * Classe interne pour le résumé d'un véhicule
     */
    public static class VehiculeResumeDTO {
        private String id;
        private String marque;
        private String modele;
        private String numeroImmatriculation;
        private String numeroChassis;
        private String proprietaireNom;
        private String typePlaque;
        private String dateExpiration;
        
        public VehiculeResumeDTO() {
        }
        
        // Getters et Setters
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getMarque() {
            return marque;
        }
        
        public void setMarque(String marque) {
            this.marque = marque;
        }
        
        public String getModele() {
            return modele;
        }
        
        public void setModele(String modele) {
            this.modele = modele;
        }
        
        public String getNumeroImmatriculation() {
            return numeroImmatriculation;
        }
        
        public void setNumeroImmatriculation(String numeroImmatriculation) {
            this.numeroImmatriculation = numeroImmatriculation;
        }
        
        public String getNumeroChassis() {
            return numeroChassis;
        }
        
        public void setNumeroChassis(String numeroChassis) {
            this.numeroChassis = numeroChassis;
        }
        
        public String getProprietaireNom() {
            return proprietaireNom;
        }
        
        public void setProprietaireNom(String proprietaireNom) {
            this.proprietaireNom = proprietaireNom;
        }
        
        public String getTypePlaque() {
            return typePlaque;
        }
        
        public void setTypePlaque(String typePlaque) {
            this.typePlaque = typePlaque;
        }
        
        public String getDateExpiration() {
            return dateExpiration;
        }
        
        public void setDateExpiration(String dateExpiration) {
            this.dateExpiration = dateExpiration;
        }
    }
    
    /**
     * Classe interne pour le résumé d'une concession minière
     */
    public static class ConcessionResumeDTO {
        private String id;
        private String numeroConcession;
        private String typeConcession;
        private String titulaire;
        private Double superficie;
        private String localisation;
        private String dateOctroi;
        private String dateExpiration;
        private String statut;
        
        public ConcessionResumeDTO() {
        }
        
        // Getters et Setters
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getNumeroConcession() {
            return numeroConcession;
        }
        
        public void setNumeroConcession(String numeroConcession) {
            this.numeroConcession = numeroConcession;
        }
        
        public String getTypeConcession() {
            return typeConcession;
        }
        
        public void setTypeConcession(String typeConcession) {
            this.typeConcession = typeConcession;
        }
        
        public String getTitulaire() {
            return titulaire;
        }
        
        public void setTitulaire(String titulaire) {
            this.titulaire = titulaire;
        }
        
        public Double getSuperficie() {
            return superficie;
        }
        
        public void setSuperficie(Double superficie) {
            this.superficie = superficie;
        }
        
        public String getLocalisation() {
            return localisation;
        }
        
        public void setLocalisation(String localisation) {
            this.localisation = localisation;
        }
        
        public String getDateOctroi() {
            return dateOctroi;
        }
        
        public void setDateOctroi(String dateOctroi) {
            this.dateOctroi = dateOctroi;
        }
        
        public String getDateExpiration() {
            return dateExpiration;
        }
        
        public void setDateExpiration(String dateExpiration) {
            this.dateExpiration = dateExpiration;
        }
        
        public String getStatut() {
            return statut;
        }
        
        public void setStatut(String statut) {
            this.statut = statut;
        }
    }
}
