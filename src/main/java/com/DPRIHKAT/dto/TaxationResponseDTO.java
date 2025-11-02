package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.DPRIHKAT.entity.enums.Devise;

import java.util.Date;
import java.util.UUID;

public class TaxationResponseDTO {
    private UUID id;
    private Date dateTaxation;
    private Double montant;
    private String exercice;
    private StatutTaxation statut;
    private TypeImpot typeImpot;
    private boolean exoneration;
    private String motifExoneration;
    private Date dateEcheance;
    private boolean actif;
    private String codeQR;
    private String nomAgent;
    private String numeroTaxation;
    private Devise devise;
    private String nomBanque;
    private String numeroCompte;
    private String intituleCompte;
    private String rib;
    private String iban;
    private String bic;
    private String motifAnnulation;
    
    // Détails du contribuable
    private ContribuableDTO contribuable;
    
    // Détails de la propriété
    private ProprieteDTO propriete;
    
    // Détails de la déclaration
    private DeclarationDTO declaration;
    
    // Détails du véhicule (pour les taxations de plaques/vignettes)
    private VehiculeDTO vehicule;
    
    // Détails de l'agent taxateur
    private AgentDTO agent;
    
    // Getters et Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Date getDateTaxation() {
        return dateTaxation;
    }
    
    public void setDateTaxation(Date dateTaxation) {
        this.dateTaxation = dateTaxation;
    }
    
    public Double getMontant() {
        return montant;
    }
    
    public void setMontant(Double montant) {
        this.montant = montant;
    }
    
    public String getExercice() {
        return exercice;
    }
    
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }
    
    public StatutTaxation getStatut() {
        return statut;
    }
    
    public void setStatut(StatutTaxation statut) {
        this.statut = statut;
    }
    
    public TypeImpot getTypeImpot() {
        return typeImpot;
    }
    
    public void setTypeImpot(TypeImpot typeImpot) {
        this.typeImpot = typeImpot;
    }
    
    public boolean isExoneration() {
        return exoneration;
    }
    
    public void setExoneration(boolean exoneration) {
        this.exoneration = exoneration;
    }
    
    public String getMotifExoneration() {
        return motifExoneration;
    }
    
    public void setMotifExoneration(String motifExoneration) {
        this.motifExoneration = motifExoneration;
    }
    
    public Date getDateEcheance() {
        return dateEcheance;
    }
    
    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }
    
    public boolean isActif() {
        return actif;
    }
    
    public void setActif(boolean actif) {
        this.actif = actif;
    }
    
    public String getCodeQR() {
        return codeQR;
    }
    
    public void setCodeQR(String codeQR) {
        this.codeQR = codeQR;
    }
    
    public String getNomAgent() {
        return nomAgent;
    }
    
    public void setNomAgent(String nomAgent) {
        this.nomAgent = nomAgent;
    }
    
    public String getNumeroTaxation() {
        return numeroTaxation;
    }
    
    public void setNumeroTaxation(String numeroTaxation) {
        this.numeroTaxation = numeroTaxation;
    }
    
    public Devise getDevise() {
        return devise;
    }
    
    public void setDevise(Devise devise) {
        this.devise = devise;
    }
    
    public String getNomBanque() {
        return nomBanque;
    }
    
    public void setNomBanque(String nomBanque) {
        this.nomBanque = nomBanque;
    }
    
    public String getNumeroCompte() {
        return numeroCompte;
    }
    
    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }
    
    public String getIntituleCompte() {
        return intituleCompte;
    }
    
    public void setIntituleCompte(String intituleCompte) {
        this.intituleCompte = intituleCompte;
    }
    
    public String getRib() {
        return rib;
    }
    
    public void setRib(String rib) {
        this.rib = rib;
    }
    
    public String getIban() {
        return iban;
    }
    
    public void setIban(String iban) {
        this.iban = iban;
    }
    
    public String getBic() {
        return bic;
    }
    
    public void setBic(String bic) {
        this.bic = bic;
    }
    
    public String getMotifAnnulation() {
        return motifAnnulation;
    }
    
    public void setMotifAnnulation(String motifAnnulation) {
        this.motifAnnulation = motifAnnulation;
    }
    
    public ContribuableDTO getContribuable() {
        return contribuable;
    }
    
    public void setContribuable(ContribuableDTO contribuable) {
        this.contribuable = contribuable;
    }
    
    public ProprieteDTO getPropriete() {
        return propriete;
    }
    
    public void setPropriete(ProprieteDTO propriete) {
        this.propriete = propriete;
    }
    
    public DeclarationDTO getDeclaration() {
        return declaration;
    }
    
    public void setDeclaration(DeclarationDTO declaration) {
        this.declaration = declaration;
    }
    
    public VehiculeDTO getVehicule() {
        return vehicule;
    }
    
    public void setVehicule(VehiculeDTO vehicule) {
        this.vehicule = vehicule;
    }
    
    public AgentDTO getAgent() {
        return agent;
    }
    
    public void setAgent(AgentDTO agent) {
        this.agent = agent;
    }
    
    // Classes DTO internes
    public static class ContribuableDTO {
        private UUID id;
        private String nom;
        private String adressePrincipale;
        private String telephonePrincipal;
        private String email;
        
        // Getters et Setters
        public UUID getId() {
            return id;
        }
        
        public void setId(UUID id) {
            this.id = id;
        }
        
        public String getNom() {
            return nom;
        }
        
        public void setNom(String nom) {
            this.nom = nom;
        }
        
        public String getAdressePrincipale() {
            return adressePrincipale;
        }
        
        public void setAdressePrincipale(String adressePrincipale) {
            this.adressePrincipale = adressePrincipale;
        }
        
        public String getTelephonePrincipal() {
            return telephonePrincipal;
        }
        
        public void setTelephonePrincipal(String telephonePrincipal) {
            this.telephonePrincipal = telephonePrincipal;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
    }
    
    public static class ProprieteDTO {
        private UUID id;
        private String type;
        private String localite;
        private double superficie;
        private String adresse;
        
        // Getters et Setters
        public UUID getId() {
            return id;
        }
        
        public void setId(UUID id) {
            this.id = id;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public String getLocalite() {
            return localite;
        }
        
        public void setLocalite(String localite) {
            this.localite = localite;
        }
        
        public double getSuperficie() {
            return superficie;
        }
        
        public void setSuperficie(double superficie) {
            this.superficie = superficie;
        }
        
        public String getAdresse() {
            return adresse;
        }
        
        public void setAdresse(String adresse) {
            this.adresse = adresse;
        }
    }
    
    public static class DeclarationDTO {
        private UUID id;
        private Date dateDeclaration;
        private String statut;
        
        // Getters et Setters
        public UUID getId() {
            return id;
        }
        
        public void setId(UUID id) {
            this.id = id;
        }
        
        public Date getDateDeclaration() {
            return dateDeclaration;
        }
        
        public void setDateDeclaration(Date dateDeclaration) {
            this.dateDeclaration = dateDeclaration;
        }
        
        public String getStatut() {
            return statut;
        }
        
        public void setStatut(String statut) {
            this.statut = statut;
        }
    }
    
    public static class VehiculeDTO {
        private UUID id;
        private String marque;
        private String modele;
        private String numeroChassis;
        private String immatriculation;
        private String genre;
        
        // Getters et Setters
        public UUID getId() {
            return id;
        }
        
        public void setId(UUID id) {
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
        
        public String getNumeroChassis() {
            return numeroChassis;
        }
        
        public void setNumeroChassis(String numeroChassis) {
            this.numeroChassis = numeroChassis;
        }
        
        public String getImmatriculation() {
            return immatriculation;
        }
        
        public void setImmatriculation(String immatriculation) {
            this.immatriculation = immatriculation;
        }
        
        public String getGenre() {
            return genre;
        }
        
        public void setGenre(String genre) {
            this.genre = genre;
        }
    }
    
    public static class AgentDTO {
        private UUID id;
        private String nom;
        private String matricule;
        
        // Getters et Setters
        public UUID getId() {
            return id;
        }
        
        public void setId(UUID id) {
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
    }
}
