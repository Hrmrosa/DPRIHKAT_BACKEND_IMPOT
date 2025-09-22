package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.ModePaiement;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import com.DPRIHKAT.entity.enums.Devise;
import com.DPRIHKAT.entity.enums.TypeImpot;

import java.util.Date;
import java.util.UUID;

/**
 * DTO pour la réponse des paiements avec informations enrichies
 * 
 * @author amateur
 */
public class PaiementResponseDTO {
    private UUID id;
    private Date date;
    private Double montant;
    private ModePaiement mode;
    private StatutPaiement statut;
    private String bordereauBancaire;
    private boolean actif;
    
    // Informations supplémentaires demandées
    private String nomContribuable;
    private Date dateTaxation;
    private Devise devise;
    private String nomTaxateur;
    private TypeImpot typeImpot;
    private String exerciceFiscal;
    
    // ID de la déclaration associée
    private UUID declarationId;
    
    // Détails de la taxation
    private TaxationDTO taxation;
    
    // Détails de l'apurement associé
    private ApurementDTO apurement;
    
    // Getters et Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public Double getMontant() {
        return montant;
    }
    
    public void setMontant(Double montant) {
        this.montant = montant;
    }
    
    public ModePaiement getMode() {
        return mode;
    }
    
    public void setMode(ModePaiement mode) {
        this.mode = mode;
    }
    
    public StatutPaiement getStatut() {
        return statut;
    }
    
    public void setStatut(StatutPaiement statut) {
        this.statut = statut;
    }
    
    public String getBordereauBancaire() {
        return bordereauBancaire;
    }
    
    public void setBordereauBancaire(String bordereauBancaire) {
        this.bordereauBancaire = bordereauBancaire;
    }
    
    public boolean isActif() {
        return actif;
    }
    
    public void setActif(boolean actif) {
        this.actif = actif;
    }
    
    public String getNomContribuable() {
        return nomContribuable;
    }
    
    public void setNomContribuable(String nomContribuable) {
        this.nomContribuable = nomContribuable;
    }
    
    public Date getDateTaxation() {
        return dateTaxation;
    }
    
    public void setDateTaxation(Date dateTaxation) {
        this.dateTaxation = dateTaxation;
    }
    
    public Devise getDevise() {
        return devise;
    }
    
    public void setDevise(Devise devise) {
        this.devise = devise;
    }
    
    public String getNomTaxateur() {
        return nomTaxateur;
    }
    
    public void setNomTaxateur(String nomTaxateur) {
        this.nomTaxateur = nomTaxateur;
    }
    
    public TypeImpot getTypeImpot() {
        return typeImpot;
    }
    
    public void setTypeImpot(TypeImpot typeImpot) {
        this.typeImpot = typeImpot;
    }
    
    public String getExerciceFiscal() {
        return exerciceFiscal;
    }
    
    public void setExerciceFiscal(String exerciceFiscal) {
        this.exerciceFiscal = exerciceFiscal;
    }
    
    public TaxationDTO getTaxation() {
        return taxation;
    }
    
    public void setTaxation(TaxationDTO taxation) {
        this.taxation = taxation;
    }
    
    public ApurementDTO getApurement() {
        return apurement;
    }
    
    public void setApurement(ApurementDTO apurement) {
        this.apurement = apurement;
    }
    
    public UUID getDeclarationId() {
        return declarationId;
    }
    
    public void setDeclarationId(UUID declarationId) {
        this.declarationId = declarationId;
    }
    
    // Classe DTO interne pour les informations de taxation
    public static class TaxationDTO {
        private UUID id;
        private String numeroTaxation;
        
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
    }
}
