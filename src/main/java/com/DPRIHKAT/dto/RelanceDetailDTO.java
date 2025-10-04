package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.StatutRelance;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.DPRIHKAT.entity.enums.TypePropriete;
import com.DPRIHKAT.entity.enums.TypeRelance;
import com.DPRIHKAT.entity.enums.TypeContribuable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DTO pour les relances avec informations détaillées sur le contribuable, ses biens et les impôts
 */
public class RelanceDetailDTO {
    
    // Informations de base de la relance
    private UUID id;
    private Date dateEnvoi;
    private TypeRelance type;
    private StatutRelance statut;
    private String contenu;
    private String codeQR;
    
    // Informations du contribuable
    private UUID contribuableId;
    private String contribuableNom;
    private String contribuableAdressePrincipale;
    private String contribuableAdresseSecondaire;
    private String contribuableTelephonePrincipal;
    private String contribuableTelephoneSecondaire;
    private String contribuableEmail;
    private String contribuableNationalite;
    private TypeContribuable contribuableType;
    private String contribuableIdentifiant; // Numéro d'identification contribuable
    
    // Informations sur les biens du contribuable
    private List<BienDTO> biens = new ArrayList<>();
    
    // Informations sur le dossier de recouvrement
    private UUID dossierRecouvrementId;
    private Double totalDette;
    private Double totalRecouvre;
    private Date dateOuverture;
    
    // Constructeurs
    public RelanceDetailDTO() {}
    
    // Getters et Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Date getDateEnvoi() {
        return dateEnvoi;
    }
    
    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
    
    public TypeRelance getType() {
        return type;
    }
    
    public void setType(TypeRelance type) {
        this.type = type;
    }
    
    public StatutRelance getStatut() {
        return statut;
    }
    
    public void setStatut(StatutRelance statut) {
        this.statut = statut;
    }
    
    public String getContenu() {
        return contenu;
    }
    
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    
    public String getCodeQR() {
        return codeQR;
    }
    
    public void setCodeQR(String codeQR) {
        this.codeQR = codeQR;
    }
    
    public UUID getContribuableId() {
        return contribuableId;
    }
    
    public void setContribuableId(UUID contribuableId) {
        this.contribuableId = contribuableId;
    }
    
    public String getContribuableNom() {
        return contribuableNom;
    }
    
    public void setContribuableNom(String contribuableNom) {
        this.contribuableNom = contribuableNom;
    }
    
    public String getContribuableAdressePrincipale() {
        return contribuableAdressePrincipale;
    }
    
    public void setContribuableAdressePrincipale(String contribuableAdressePrincipale) {
        this.contribuableAdressePrincipale = contribuableAdressePrincipale;
    }
    
    public String getContribuableAdresseSecondaire() {
        return contribuableAdresseSecondaire;
    }
    
    public void setContribuableAdresseSecondaire(String contribuableAdresseSecondaire) {
        this.contribuableAdresseSecondaire = contribuableAdresseSecondaire;
    }
    
    public String getContribuableTelephonePrincipal() {
        return contribuableTelephonePrincipal;
    }
    
    public void setContribuableTelephonePrincipal(String contribuableTelephonePrincipal) {
        this.contribuableTelephonePrincipal = contribuableTelephonePrincipal;
    }
    
    public String getContribuableTelephoneSecondaire() {
        return contribuableTelephoneSecondaire;
    }
    
    public void setContribuableTelephoneSecondaire(String contribuableTelephoneSecondaire) {
        this.contribuableTelephoneSecondaire = contribuableTelephoneSecondaire;
    }
    
    public String getContribuableEmail() {
        return contribuableEmail;
    }
    
    public void setContribuableEmail(String contribuableEmail) {
        this.contribuableEmail = contribuableEmail;
    }
    
    public String getContribuableNationalite() {
        return contribuableNationalite;
    }
    
    public void setContribuableNationalite(String contribuableNationalite) {
        this.contribuableNationalite = contribuableNationalite;
    }
    
    public TypeContribuable getContribuableType() {
        return contribuableType;
    }
    
    public void setContribuableType(TypeContribuable contribuableType) {
        this.contribuableType = contribuableType;
    }
    
    public String getContribuableIdentifiant() {
        return contribuableIdentifiant;
    }
    
    public void setContribuableIdentifiant(String contribuableIdentifiant) {
        this.contribuableIdentifiant = contribuableIdentifiant;
    }
    
    public List<BienDTO> getBiens() {
        return biens;
    }
    
    public void setBiens(List<BienDTO> biens) {
        this.biens = biens;
    }
    
    public UUID getDossierRecouvrementId() {
        return dossierRecouvrementId;
    }
    
    public void setDossierRecouvrementId(UUID dossierRecouvrementId) {
        this.dossierRecouvrementId = dossierRecouvrementId;
    }
    
    public Double getTotalDette() {
        return totalDette;
    }
    
    public void setTotalDette(Double totalDette) {
        this.totalDette = totalDette;
    }
    
    public Double getTotalRecouvre() {
        return totalRecouvre;
    }
    
    public void setTotalRecouvre(Double totalRecouvre) {
        this.totalRecouvre = totalRecouvre;
    }
    
    public Date getDateOuverture() {
        return dateOuverture;
    }
    
    public void setDateOuverture(Date dateOuverture) {
        this.dateOuverture = dateOuverture;
    }
    
    /**
     * Classe interne pour représenter les informations sur un bien du contribuable
     */
    public static class BienDTO {
        private UUID id;
        private TypePropriete type;
        private String localite;
        private Integer rangLocalite;
        private Double superficie;
        private String adresse;
        private Double montantImpot;
        private List<ImpotDTO> impots = new ArrayList<>();
        
        public BienDTO() {}
        
        public UUID getId() {
            return id;
        }
        
        public void setId(UUID id) {
            this.id = id;
        }
        
        public TypePropriete getType() {
            return type;
        }
        
        public void setType(TypePropriete type) {
            this.type = type;
        }
        
        public String getLocalite() {
            return localite;
        }
        
        public void setLocalite(String localite) {
            this.localite = localite;
        }
        
        public Integer getRangLocalite() {
            return rangLocalite;
        }
        
        public void setRangLocalite(Integer rangLocalite) {
            this.rangLocalite = rangLocalite;
        }
        
        public Double getSuperficie() {
            return superficie;
        }
        
        public void setSuperficie(Double superficie) {
            this.superficie = superficie;
        }
        
        public String getAdresse() {
            return adresse;
        }
        
        public void setAdresse(String adresse) {
            this.adresse = adresse;
        }
        
        public Double getMontantImpot() {
            return montantImpot;
        }
        
        public void setMontantImpot(Double montantImpot) {
            this.montantImpot = montantImpot;
        }
        
        public List<ImpotDTO> getImpots() {
            return impots;
        }
        
        public void setImpots(List<ImpotDTO> impots) {
            this.impots = impots;
        }
    }
    
    /**
     * Classe interne pour représenter les informations sur un impôt assigné à un bien
     */
    public static class ImpotDTO {
        private UUID id;
        private String libelle;
        private TypeImpot typeImpot;
        private Double montant;
        private Double tauxImposition;
        private String exercice;
        private Date dateEcheance;
        private UUID taxationId;
        
        public ImpotDTO() {}
        
        public UUID getId() {
            return id;
        }
        
        public void setId(UUID id) {
            this.id = id;
        }
        
        public String getLibelle() {
            return libelle;
        }
        
        public void setLibelle(String libelle) {
            this.libelle = libelle;
        }
        
        public TypeImpot getTypeImpot() {
            return typeImpot;
        }
        
        public void setTypeImpot(TypeImpot typeImpot) {
            this.typeImpot = typeImpot;
        }
        
        public Double getMontant() {
            return montant;
        }
        
        public void setMontant(Double montant) {
            this.montant = montant;
        }
        
        public Double getTauxImposition() {
            return tauxImposition;
        }
        
        public void setTauxImposition(Double tauxImposition) {
            this.tauxImposition = tauxImposition;
        }
        
        public String getExercice() {
            return exercice;
        }
        
        public void setExercice(String exercice) {
            this.exercice = exercice;
        }
        
        public Date getDateEcheance() {
            return dateEcheance;
        }
        
        public void setDateEcheance(Date dateEcheance) {
            this.dateEcheance = dateEcheance;
        }
        
        public UUID getTaxationId() {
            return taxationId;
        }
        
        public void setTaxationId(UUID taxationId) {
            this.taxationId = taxationId;
        }
    }
}
