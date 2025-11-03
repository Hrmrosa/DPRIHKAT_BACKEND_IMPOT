package com.DPRIHKAT.dto;

import java.util.Date;
import java.util.UUID;

/**
 * DTO pour les données de collecte dans un rapport
 * 
 * @author amateur
 */
public class RapportCollecteDTO {
    
    private UUID id;
    private Date dateCollecte;
    private String typeCollecte; // Declaration, Taxation, Paiement, etc.
    private String nomAgent;
    private String matriculeAgent;
    private String nomContribuable;
    private String numeroContribuable;
    private String typeImpot;
    private Double montant;
    private String reference;
    private String statut;
    
    public RapportCollecteDTO() {
    }
    
    // Getters et Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Date getDateCollecte() {
        return dateCollecte;
    }
    
    public void setDateCollecte(Date dateCollecte) {
        this.dateCollecte = dateCollecte;
    }
    
    public String getTypeCollecte() {
        return typeCollecte;
    }
    
    public void setTypeCollecte(String typeCollecte) {
        this.typeCollecte = typeCollecte;
    }
    
    public String getNomAgent() {
        return nomAgent;
    }
    
    public void setNomAgent(String nomAgent) {
        this.nomAgent = nomAgent;
    }
    
    public String getMatriculeAgent() {
        return matriculeAgent;
    }
    
    public void setMatriculeAgent(String matriculeAgent) {
        this.matriculeAgent = matriculeAgent;
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
    
    public String getTypeImpot() {
        return typeImpot;
    }
    
    public void setTypeImpot(String typeImpot) {
        this.typeImpot = typeImpot;
    }
    
    public Double getMontant() {
        return montant;
    }
    
    public void setMontant(Double montant) {
        this.montant = montant;
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
}
