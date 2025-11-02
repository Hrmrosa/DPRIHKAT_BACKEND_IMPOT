package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.Devise;
import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeImpot;

import java.util.Date;
import java.util.UUID;

/**
 * DTO pour les données d'impression d'une note de taxation
 * 
 * @author amateur
 */
public class NoteTaxationDTO {
    
    private UUID id;
    private String numeroTaxation;
    private Date dateTaxation;
    private Date dateEcheance;
    private Double montant;
    private Devise devise;
    private String exercice;
    private StatutTaxation statut;
    private TypeImpot typeImpot;
    private String codeQR;
    
    // Informations du contribuable
    private String contribuableNom;
    private String contribuableNRC;
    private String contribuableIdNat;
    private String contribuableAdresse;
    private String contribuableTelephone;
    private String contribuableEmail;
    
    // Informations du véhicule (si applicable)
    private String vehiculeMarque;
    private String vehiculeModele;
    private Integer vehiculeAnnee;
    private String vehiculeNumeroChassis;
    private String vehiculeGenre;
    private String vehiculeCategorie;
    private Double vehiculePuissanceFiscale;
    
    // Informations bancaires
    private String nomBanque;
    private String numeroCompte;
    private String intituleCompte;
    
    // Informations de l'agent taxateur
    private String agentNom;
    private String agentMatricule;
    private String bureauNom;
    private String divisionNom;
    
    // Constructeurs
    public NoteTaxationDTO() {
    }
    
    // Getters et Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getNumeroTaxation() {
        return numeroTaxation;
    }
    
    public void setNumeroTaxation(String numeroTaxation) {
        this.numeroTaxation = numeroTaxation;
    }
    
    public Date getDateTaxation() {
        return dateTaxation;
    }
    
    public void setDateTaxation(Date dateTaxation) {
        this.dateTaxation = dateTaxation;
    }
    
    public Date getDateEcheance() {
        return dateEcheance;
    }
    
    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }
    
    public Double getMontant() {
        return montant;
    }
    
    public void setMontant(Double montant) {
        this.montant = montant;
    }
    
    public Devise getDevise() {
        return devise;
    }
    
    public void setDevise(Devise devise) {
        this.devise = devise;
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
    
    public String getCodeQR() {
        return codeQR;
    }
    
    public void setCodeQR(String codeQR) {
        this.codeQR = codeQR;
    }
    
    public String getContribuableNom() {
        return contribuableNom;
    }
    
    public void setContribuableNom(String contribuableNom) {
        this.contribuableNom = contribuableNom;
    }
    
    public String getContribuableNRC() {
        return contribuableNRC;
    }
    
    public void setContribuableNRC(String contribuableNRC) {
        this.contribuableNRC = contribuableNRC;
    }
    
    public String getContribuableIdNat() {
        return contribuableIdNat;
    }
    
    public void setContribuableIdNat(String contribuableIdNat) {
        this.contribuableIdNat = contribuableIdNat;
    }
    
    public String getContribuableAdresse() {
        return contribuableAdresse;
    }
    
    public void setContribuableAdresse(String contribuableAdresse) {
        this.contribuableAdresse = contribuableAdresse;
    }
    
    public String getContribuableTelephone() {
        return contribuableTelephone;
    }
    
    public void setContribuableTelephone(String contribuableTelephone) {
        this.contribuableTelephone = contribuableTelephone;
    }
    
    public String getContribuableEmail() {
        return contribuableEmail;
    }
    
    public void setContribuableEmail(String contribuableEmail) {
        this.contribuableEmail = contribuableEmail;
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
    
    public Integer getVehiculeAnnee() {
        return vehiculeAnnee;
    }
    
    public void setVehiculeAnnee(Integer vehiculeAnnee) {
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
    
    public String getBureauNom() {
        return bureauNom;
    }
    
    public void setBureauNom(String bureauNom) {
        this.bureauNom = bureauNom;
    }
    
    public String getDivisionNom() {
        return divisionNom;
    }
    
    public void setDivisionNom(String divisionNom) {
        this.divisionNom = divisionNom;
    }
}
