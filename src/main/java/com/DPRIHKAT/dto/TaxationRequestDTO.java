package com.DPRIHKAT.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO pour les requêtes de création de taxation
 * 
 * @author amateur
 */
public class TaxationRequestDTO {
    
    @NotNull
    private UUID proprieteId;
    
    @NotNull
    private UUID proprieteImpotId;
    
    @NotNull
    private String exercice;
    
    public TaxationRequestDTO() {
    }
    
    public TaxationRequestDTO(UUID proprieteId, UUID proprieteImpotId, String exercice) {
        this.proprieteId = proprieteId;
        this.proprieteImpotId = proprieteImpotId;
        this.exercice = exercice;
    }
    
    // Getters et Setters
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
    
    public String getExercice() {
        return exercice;
    }
    
    public void setExercice(String exercice) {
        this.exercice = exercice;
    }
}
