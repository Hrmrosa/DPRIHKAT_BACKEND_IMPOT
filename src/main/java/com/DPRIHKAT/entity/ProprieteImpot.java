package com.DPRIHKAT.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Entité représentant le lien entre une propriété et un type d'impôt
 * @author amateur
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProprieteImpot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "propriete_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Propriete propriete;

    @ManyToOne
    @JoinColumn(name = "nature_impot_id")
    @JsonIdentityReference(alwaysAsId = true)
    private NatureImpot natureImpot;

    private Date dateCreation;

    private Date dateModification;

    private boolean actif = true;

    // Champs supplémentaires pour la gestion des impôts
    private Double tauxImposition;
    
    private String commentaire;

    public ProprieteImpot() {
        this.dateCreation = new Date();
        this.dateModification = new Date();
    }

    public ProprieteImpot(Propriete propriete, NatureImpot natureImpot) {
        this.propriete = propriete;
        this.natureImpot = natureImpot;
        this.dateCreation = new Date();
        this.dateModification = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Propriete getPropriete() {
        return propriete;
    }

    public void setPropriete(Propriete propriete) {
        this.propriete = propriete;
    }

    public NatureImpot getNatureImpot() {
        return natureImpot;
    }

    public void setNatureImpot(NatureImpot natureImpot) {
        this.natureImpot = natureImpot;
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

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
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
