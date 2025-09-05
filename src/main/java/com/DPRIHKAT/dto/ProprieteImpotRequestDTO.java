package com.DPRIHKAT.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO pour les requêtes de création ou de mise à jour de liens entre propriétés et impôts
 * @author amateur
 */
public class ProprieteImpotRequestDTO {
    
    @NotNull(message = "L'ID de la propriété est obligatoire")
    private UUID proprieteId;
    
    @NotNull(message = "L'ID de la nature d'impôt est obligatoire")
    private UUID natureImpotId;
    
    private Double tauxImposition;
    
    private String commentaire;
    
    public ProprieteImpotRequestDTO() {
    }
    
    public ProprieteImpotRequestDTO(UUID proprieteId, UUID natureImpotId, Double tauxImposition, String commentaire) {
        this.proprieteId = proprieteId;
        this.natureImpotId = natureImpotId;
        this.tauxImposition = tauxImposition;
        this.commentaire = commentaire;
    }

    public UUID getProprieteId() {
        return proprieteId;
    }

    public void setProprieteId(UUID proprieteId) {
        this.proprieteId = proprieteId;
    }

    public UUID getNatureImpotId() {
        return natureImpotId;
    }

    public void setNatureImpotId(UUID natureImpotId) {
        this.natureImpotId = natureImpotId;
    }

    public Double getTauxImposition() {
        return tauxImposition;
    }

    public void setTauxImposition(Double tauxImposition) {
        this.tauxImposition = tauxImposition;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
