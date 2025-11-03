package com.DPRIHKAT.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO pour les données de recouvrement dans le Dashboard
 * Inclut ATD, MED, Commandements, etc.
 * 
 * @author amateur
 */
public class DonneesDashboardRecouvrementDTO {
    
    private Long totalDossiers;
    private Long nombreATD; // Avis de Taxation Directe
    private Long nombreMED; // Mise en Demeure
    private Long nombreCommandements;
    private Long nombreSaisies;
    private Double montantTotal;
    private Double montantRecouvre;
    private Double montantRestant;
    
    // Répartition par type de document
    private Map<String, Long> repartitionParType;
    private Map<String, Double> montantsParType;
    
    // Répartition par statut
    private Map<String, Long> repartitionParStatut;
    
    // Documents récents
    private List<DocumentRecouvrementResumeDTO> documentsRecents;
    
    // Dossiers en cours
    private List<DossierRecouvrementResumeDTO> dossiersEnCours;
    
    // Taux de recouvrement
    private Double tauxRecouvrement;
    
    public DonneesDashboardRecouvrementDTO() {
    }
    
    // Getters et Setters
    public Long getTotalDossiers() {
        return totalDossiers;
    }
    
    public void setTotalDossiers(Long totalDossiers) {
        this.totalDossiers = totalDossiers;
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
    
    public Long getNombreSaisies() {
        return nombreSaisies;
    }
    
    public void setNombreSaisies(Long nombreSaisies) {
        this.nombreSaisies = nombreSaisies;
    }
    
    public Double getMontantTotal() {
        return montantTotal;
    }
    
    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }
    
    public Double getMontantRecouvre() {
        return montantRecouvre;
    }
    
    public void setMontantRecouvre(Double montantRecouvre) {
        this.montantRecouvre = montantRecouvre;
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
    
    public List<DocumentRecouvrementResumeDTO> getDocumentsRecents() {
        return documentsRecents;
    }
    
    public void setDocumentsRecents(List<DocumentRecouvrementResumeDTO> documentsRecents) {
        this.documentsRecents = documentsRecents;
    }
    
    public List<DossierRecouvrementResumeDTO> getDossiersEnCours() {
        return dossiersEnCours;
    }
    
    public void setDossiersEnCours(List<DossierRecouvrementResumeDTO> dossiersEnCours) {
        this.dossiersEnCours = dossiersEnCours;
    }
    
    public Double getTauxRecouvrement() {
        return tauxRecouvrement;
    }
    
    public void setTauxRecouvrement(Double tauxRecouvrement) {
        this.tauxRecouvrement = tauxRecouvrement;
    }
    
    /**
     * Classe interne pour le résumé d'un document de recouvrement
     */
    public static class DocumentRecouvrementResumeDTO {
        private String id;
        private String type;
        private String reference;
        private String statut;
        private Double montant;
        private String contribuableNom;
        private String dateGeneration;
        private String dateNotification;
        
        public DocumentRecouvrementResumeDTO() {
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
        
        public String getReference() {
            return reference;
        }
        
        public void setReference(String reference) {
            this.reference = reference;
        }
        
        public String getStatut() {
            return statut;
        }
        
        public void setStatut(String statut) {
            this.statut = statut;
        }
        
        public Double getMontant() {
            return montant;
        }
        
        public void setMontant(Double montant) {
            this.montant = montant;
        }
        
        public String getContribuableNom() {
            return contribuableNom;
        }
        
        public void setContribuableNom(String contribuableNom) {
            this.contribuableNom = contribuableNom;
        }
        
        public String getDateGeneration() {
            return dateGeneration;
        }
        
        public void setDateGeneration(String dateGeneration) {
            this.dateGeneration = dateGeneration;
        }
        
        public String getDateNotification() {
            return dateNotification;
        }
        
        public void setDateNotification(String dateNotification) {
            this.dateNotification = dateNotification;
        }
    }
    
    /**
     * Classe interne pour le résumé d'un dossier de recouvrement
     */
    public static class DossierRecouvrementResumeDTO {
        private String id;
        private String reference;
        private String statut;
        private String contribuableNom;
        private Double montantTotal;
        private Double montantRecouvre;
        private Integer nombreDocuments;
        private String dateOuverture;
        
        public DossierRecouvrementResumeDTO() {
        }
        
        // Getters et Setters
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getReference() {
            return reference;
        }
        
        public void setReference(String reference) {
            this.reference = reference;
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
        
        public Double getMontantTotal() {
            return montantTotal;
        }
        
        public void setMontantTotal(Double montantTotal) {
            this.montantTotal = montantTotal;
        }
        
        public Double getMontantRecouvre() {
            return montantRecouvre;
        }
        
        public void setMontantRecouvre(Double montantRecouvre) {
            this.montantRecouvre = montantRecouvre;
        }
        
        public Integer getNombreDocuments() {
            return nombreDocuments;
        }
        
        public void setNombreDocuments(Integer nombreDocuments) {
            this.nombreDocuments = nombreDocuments;
        }
        
        public String getDateOuverture() {
            return dateOuverture;
        }
        
        public void setDateOuverture(String dateOuverture) {
            this.dateOuverture = dateOuverture;
        }
    }
}
