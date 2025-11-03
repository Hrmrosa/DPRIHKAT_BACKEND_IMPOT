package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.ModePaiement;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import java.util.Date;
import java.util.UUID;

/**
 * DTO pour les données de paiement dans un rapport
 * 
 * @author amateur
 */
public class RapportPaiementDTO {
    
    private UUID id;
    private Date datePaiement;
    private Double montant;
    private StatutPaiement statut;
    private ModePaiement mode;
    private String bordereauBancaire;
    private String nomBanque;
    private String numeroCompte;
    private String nomContribuable;
    private String numeroContribuable;
    private String numeroTaxation;
    
    public RapportPaiementDTO() {
    }
    
    // Getters et Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Date getDatePaiement() {
        return datePaiement;
    }
    
    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }
    
    public Double getMontant() {
        return montant;
    }
    
    public void setMontant(Double montant) {
        this.montant = montant;
    }
    
    public StatutPaiement getStatut() {
        return statut;
    }
    
    public void setStatut(StatutPaiement statut) {
        this.statut = statut;
    }
    
    public ModePaiement getMode() {
        return mode;
    }
    
    public void setMode(ModePaiement mode) {
        this.mode = mode;
    }
    
    public String getBordereauBancaire() {
        return bordereauBancaire;
    }
    
    public void setBordereauBancaire(String bordereauBancaire) {
        this.bordereauBancaire = bordereauBancaire;
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
    
    public String getNomContribuable() {
        return nomContribuable;
    }
    
    public void setNomContribuable(String nomContribuable) {
        this.nomContribuable = nomContribuable;
    }
    
    public String getNumeroContribuable() {
        return numeroContribuable;
    }
    
    public void setNumeroContribuable(String numeroContribuable) {
        this.numeroContribuable = numeroContribuable;
    }
    
    public String getNumeroTaxation() {
        return numeroTaxation;
    }
    
    public void setNumeroTaxation(String numeroTaxation) {
        this.numeroTaxation = numeroTaxation;
    }
}
