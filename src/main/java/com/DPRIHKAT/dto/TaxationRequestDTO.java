package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.TypeImpot;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

/**
 * DTO pour les requêtes de création ou de mise à jour de taxations
 * @author amateur
 */
public class TaxationRequestDTO {
    
    @NotNull(message = "L'ID de la propriété est obligatoire")
    private UUID proprieteId;
    
    @NotNull(message = "L'ID de la nature d'impôt est obligatoire")
    private UUID proprieteImpotId;
    
    @NotNull(message = "L'exercice est obligatoire")
    private Integer exercice;
    
    @NotNull(message = "L'ID de l'agent taxateur est obligatoire")
    private UUID agentTaxateurId;
    
    private Double montant;
    
    private TypeImpot typeImpot;
    
    private boolean exoneration;
    
    private String motifExoneration;
    
    private Date dateEcheance;
    
    public TaxationRequestDTO() {
    }
    
    public TaxationRequestDTO(UUID proprieteId, UUID proprieteImpotId, Integer exercice, UUID agentTaxateurId, 
                             Double montant, TypeImpot typeImpot, boolean exoneration, String motifExoneration, 
                             Date dateEcheance) {
        this.proprieteId = proprieteId;
        this.proprieteImpotId = proprieteImpotId;
        this.exercice = exercice;
        this.agentTaxateurId = agentTaxateurId;
        this.montant = montant;
        this.typeImpot = typeImpot;
        this.exoneration = exoneration;
        this.motifExoneration = motifExoneration;
        this.dateEcheance = dateEcheance;
    }

    public UUID getProprieteId() {
        return proprieteId;
    }

    public void setProprieteId(UUID proprieteId) {
        this.proprieteId = proprieteId;
    }

    public UUID getProprieteImpotId() {
        return proprieteImpotId;
    }

    public void setProprieteImpotId(UUID proprieteImpotId) {
        this.proprieteImpotId = proprieteImpotId;
    }

    public Integer getExercice() {
        return exercice;
    }

    public void setExercice(Integer exercice) {
        this.exercice = exercice;
    }

    public UUID getAgentTaxateurId() {
        return agentTaxateurId;
    }

    public void setAgentTaxateurId(UUID agentTaxateurId) {
        this.agentTaxateurId = agentTaxateurId;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
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
}
