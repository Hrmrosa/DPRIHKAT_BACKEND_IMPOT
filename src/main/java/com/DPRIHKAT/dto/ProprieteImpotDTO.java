package com.DPRIHKAT.dto;

import java.util.Date;
import java.util.UUID;

/**
 * DTO pour représenter un lien entre une propriété et une nature d'impôt
 * @author amateur
 */
public class ProprieteImpotDTO {
    
    private UUID id;
    private UUID proprieteId;
    private String proprieteAdresse;
    private UUID natureImpotId;
    private String natureImpotCode;
    private String natureImpotNom;
    private Date dateCreation;
    private Date dateModification;
    private Double tauxImposition;
    private String commentaire;
    private boolean actif;
    
    public ProprieteImpotDTO() {
    }
    
    public ProprieteImpotDTO(UUID id, UUID proprieteId, String proprieteAdresse, UUID natureImpotId, 
                            String natureImpotCode, String natureImpotNom, Date dateCreation, 
                            Date dateModification, Double tauxImposition, String commentaire, boolean actif) {
        this.id = id;
        this.proprieteId = proprieteId;
        this.proprieteAdresse = proprieteAdresse;
        this.natureImpotId = natureImpotId;
        this.natureImpotCode = natureImpotCode;
        this.natureImpotNom = natureImpotNom;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.tauxImposition = tauxImposition;
        this.commentaire = commentaire;
        this.actif = actif;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProprieteId() {
        return proprieteId;
    }

    public void setProprieteId(UUID proprieteId) {
        this.proprieteId = proprieteId;
    }

    public String getProprieteAdresse() {
        return proprieteAdresse;
    }

    public void setProprieteAdresse(String proprieteAdresse) {
        this.proprieteAdresse = proprieteAdresse;
    }

    public UUID getNatureImpotId() {
        return natureImpotId;
    }

    public void setNatureImpotId(UUID natureImpotId) {
        this.natureImpotId = natureImpotId;
    }

    public String getNatureImpotCode() {
        return natureImpotCode;
    }

    public void setNatureImpotCode(String natureImpotCode) {
        this.natureImpotCode = natureImpotCode;
    }

    public String getNatureImpotNom() {
        return natureImpotNom;
    }

    public void setNatureImpotNom(String natureImpotNom) {
        this.natureImpotNom = natureImpotNom;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
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

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
}
