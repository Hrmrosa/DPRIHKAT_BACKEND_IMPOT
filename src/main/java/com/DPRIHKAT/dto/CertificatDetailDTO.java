package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.StatutCertificat;
import com.DPRIHKAT.entity.enums.StatutDeclaration;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeContribuable;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.DPRIHKAT.entity.enums.TypePropriete;

import java.util.Date;
import java.util.UUID;

/**
 * DTO pour les certificats détaillés avec informations sur le contribuable,
 * les propriétés/véhicules, l'agent, le paiement et la taxation
 */
public class CertificatDetailDTO {
    
    // Informations de base du certificat
    private UUID id;
    private String numero;
    private Date dateEmission;
    private Date dateExpiration;
    private double montant;
    private StatutCertificat statut;
    private boolean actif;
    private String codeQR;
    private String motifAnnulation;
    
    // Informations sur la déclaration
    private UUID declarationId;
    private Date dateDeclaration;
    private StatutDeclaration statutDeclaration;
    private String numeroDeclaration;
    
    // Informations sur le contribuable
    private UUID contribuableId;
    private String contribuableNom;
    private String contribuableAdressePrincipale;
    private String contribuableAdresseSecondaire;
    private String contribuableTelephonePrincipal;
    private String contribuableTelephoneSecondaire;
    private String contribuableEmail;
    private TypeContribuable contribuableType;
    private String contribuableIdentifiant;
    
    // Informations sur le véhicule (si applicable)
    private UUID vehiculeId;
    private String vehiculeImmatriculation;
    private String vehiculeMarque;
    private String vehiculeModele;
    private int vehiculeAnnee;
    private String vehiculeNumeroChassis;
    private String vehiculeGenre;
    private String vehiculeCategorie;
    private Double vehiculePuissanceFiscale;
    
    // Informations sur la propriété (si applicable)
    private UUID proprieteId;
    private TypePropriete proprieteType;
    private String proprieteLocalite;
    private Integer proprieteRangLocalite;
    private Double proprieteSuperficie;
    private String proprieteAdresse;
    
    // Informations sur l'agent
    private UUID agentId;
    private String agentNom;
    private String agentMatricule;
    private String agentBureau;
    
    // Informations sur le paiement
    private UUID paiementId;
    private Date datePaiement;
    private double montantPaiement;
    private StatutPaiement statutPaiement;
    private String referencePaiement;
    private String modePaiement;
    
    // Informations sur la taxation
    private UUID taxationId;
    private Date dateTaxation;
    private double montantTaxation;
    private String exercice;
    private StatutTaxation statutTaxation;
    private TypeImpot typeImpot;
    private String numeroTaxation;
    
    // Constructeurs
    public CertificatDetailDTO() {}
    
    // Getters et Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public Date getDateEmission() {
        return dateEmission;
    }
    
    public void setDateEmission(Date dateEmission) {
        this.dateEmission = dateEmission;
    }
    
    public Date getDateExpiration() {
        return dateExpiration;
    }
    
    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
    
    public double getMontant() {
        return montant;
    }
    
    public void setMontant(double montant) {
        this.montant = montant;
    }
    
    public StatutCertificat getStatut() {
        return statut;
    }
    
    public void setStatut(StatutCertificat statut) {
        this.statut = statut;
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
    
    public String getMotifAnnulation() {
        return motifAnnulation;
    }
    
    public void setMotifAnnulation(String motifAnnulation) {
        this.motifAnnulation = motifAnnulation;
    }
    
    public UUID getDeclarationId() {
        return declarationId;
    }
    
    public void setDeclarationId(UUID declarationId) {
        this.declarationId = declarationId;
    }
    
    public Date getDateDeclaration() {
        return dateDeclaration;
    }
    
    public void setDateDeclaration(Date dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }
    
    public StatutDeclaration getStatutDeclaration() {
        return statutDeclaration;
    }
    
    public void setStatutDeclaration(StatutDeclaration statutDeclaration) {
        this.statutDeclaration = statutDeclaration;
    }
    
    public String getNumeroDeclaration() {
        return numeroDeclaration;
    }
    
    public void setNumeroDeclaration(String numeroDeclaration) {
        this.numeroDeclaration = numeroDeclaration;
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
    
    public UUID getVehiculeId() {
        return vehiculeId;
    }
    
    public void setVehiculeId(UUID vehiculeId) {
        this.vehiculeId = vehiculeId;
    }
    
    public String getVehiculeImmatriculation() {
        return vehiculeImmatriculation;
    }
    
    public void setVehiculeImmatriculation(String vehiculeImmatriculation) {
        this.vehiculeImmatriculation = vehiculeImmatriculation;
    }
    
    public String getVehiculeMarque() {
        return vehiculeMarque;
    }
    
    public void setVehiculeMarque(String vehiculeMarque) {
        this.vehiculeMarque = vehiculeMarque;
    }
    
    public String getVehiculeModele() {
        return vehiculeModele;
    }
    
    public void setVehiculeModele(String vehiculeModele) {
        this.vehiculeModele = vehiculeModele;
    }
    
    public int getVehiculeAnnee() {
        return vehiculeAnnee;
    }
    
    public void setVehiculeAnnee(int vehiculeAnnee) {
        this.vehiculeAnnee = vehiculeAnnee;
    }
    
    public String getVehiculeNumeroChassis() {
        return vehiculeNumeroChassis;
    }
    
    public void setVehiculeNumeroChassis(String vehiculeNumeroChassis) {
        this.vehiculeNumeroChassis = vehiculeNumeroChassis;
    }
    
    public String getVehiculeGenre() {
        return vehiculeGenre;
    }
    
    public void setVehiculeGenre(String vehiculeGenre) {
        this.vehiculeGenre = vehiculeGenre;
    }
    
    public String getVehiculeCategorie() {
        return vehiculeCategorie;
    }
    
    public void setVehiculeCategorie(String vehiculeCategorie) {
        this.vehiculeCategorie = vehiculeCategorie;
    }
    
    public Double getVehiculePuissanceFiscale() {
        return vehiculePuissanceFiscale;
    }
    
    public void setVehiculePuissanceFiscale(Double vehiculePuissanceFiscale) {
        this.vehiculePuissanceFiscale = vehiculePuissanceFiscale;
    }
    
    public UUID getProprieteId() {
        return proprieteId;
    }
    
    public void setProprieteId(UUID proprieteId) {
        this.proprieteId = proprieteId;
    }
    
    public TypePropriete getProprieteType() {
        return proprieteType;
    }
    
    public void setProprieteType(TypePropriete proprieteType) {
        this.proprieteType = proprieteType;
    }
    
    public String getProprieteLocalite() {
        return proprieteLocalite;
    }
    
    public void setProprieteLocalite(String proprieteLocalite) {
        this.proprieteLocalite = proprieteLocalite;
    }
    
    public Integer getProprieteRangLocalite() {
        return proprieteRangLocalite;
    }
    
    public void setProprieteRangLocalite(Integer proprieteRangLocalite) {
        this.proprieteRangLocalite = proprieteRangLocalite;
    }
    
    public Double getProprieteSuperficie() {
        return proprieteSuperficie;
    }
    
    public void setProprieteSuperficie(Double proprieteSuperficie) {
        this.proprieteSuperficie = proprieteSuperficie;
    }
    
    public String getProprieteAdresse() {
        return proprieteAdresse;
    }
    
    public void setProprieteAdresse(String proprieteAdresse) {
        this.proprieteAdresse = proprieteAdresse;
    }
    
    public UUID getAgentId() {
        return agentId;
    }
    
    public void setAgentId(UUID agentId) {
        this.agentId = agentId;
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
    
    public String getAgentBureau() {
        return agentBureau;
    }
    
    public void setAgentBureau(String agentBureau) {
        this.agentBureau = agentBureau;
    }
    
    public UUID getPaiementId() {
        return paiementId;
    }
    
    public void setPaiementId(UUID paiementId) {
        this.paiementId = paiementId;
    }
    
    public Date getDatePaiement() {
        return datePaiement;
    }
    
    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }
    
    public double getMontantPaiement() {
        return montantPaiement;
    }
    
    public void setMontantPaiement(double montantPaiement) {
        this.montantPaiement = montantPaiement;
    }
    
    public StatutPaiement getStatutPaiement() {
        return statutPaiement;
    }
    
    public void setStatutPaiement(StatutPaiement statutPaiement) {
        this.statutPaiement = statutPaiement;
    }
    
    public String getReferencePaiement() {
        return referencePaiement;
    }
    
    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }
    
    public String getModePaiement() {
        return modePaiement;
    }
    
    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }
    
    public UUID getTaxationId() {
        return taxationId;
    }
    
    public void setTaxationId(UUID taxationId) {
        this.taxationId = taxationId;
    }
    
    public Date getDateTaxation() {
        return dateTaxation;
    }
    
    public void setDateTaxation(Date dateTaxation) {
        this.dateTaxation = dateTaxation;
    }
    
    public double getMontantTaxation() {
        return montantTaxation;
    }
    
    public void setMontantTaxation(double montantTaxation) {
        this.montantTaxation = montantTaxation;
    }
    
    public String getExercice() {
        return exercice;
    }
    
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }
    
    public StatutTaxation getStatutTaxation() {
        return statutTaxation;
    }
    
    public void setStatutTaxation(StatutTaxation statutTaxation) {
        this.statutTaxation = statutTaxation;
    }
    
    public TypeImpot getTypeImpot() {
        return typeImpot;
    }
    
    public void setTypeImpot(TypeImpot typeImpot) {
        this.typeImpot = typeImpot;
    }
    
    public String getNumeroTaxation() {
        return numeroTaxation;
    }
    
    public void setNumeroTaxation(String numeroTaxation) {
        this.numeroTaxation = numeroTaxation;
    }
}
