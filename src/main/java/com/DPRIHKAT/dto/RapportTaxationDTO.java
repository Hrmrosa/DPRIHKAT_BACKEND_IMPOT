package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeImpot;
import java.util.Date;
import java.util.UUID;

/**
 * DTO pour les données de taxation dans un rapport
 * 
 * @author amateur
 */
public class RapportTaxationDTO {
    
    private UUID id;
    private String numeroTaxation;
    private Date dateTaxation;
    private Double montant;
    private String devise;
    private StatutTaxation statut;
    private TypeImpot typeImpot;
    private String exercice;
    private String nomAgent;
    private String matriculeAgent;
    private String nomContribuable;
    private String numeroContribuable;
    private boolean exoneration;
    private String motifExoneration;
    
    public RapportTaxationDTO() {
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
    
    public Double getMontant() {
        return montant;
    }
    
    public void setMontant(Double montant) {
        this.montant = montant;
    }
    
    public String getDevise() {
        return devise;
    }
    
    public void setDevise(String devise) {
        this.devise = devise;
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
    
    public String getExercice() {
        return exercice;
    }
    
    public void setExercice(String exercice) {
        this.exercice = exercice;
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
}
