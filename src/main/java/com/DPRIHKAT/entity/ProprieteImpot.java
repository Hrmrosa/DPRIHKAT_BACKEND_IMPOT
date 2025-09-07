package com.DPRIHKAT.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Entité représentant le lien entre une propriété et une nature d'impôt
 * avec le taux d'imposition applicable
 * 
 * @author amateur
 */
@Entity
@Table(name = "propriete_impot")
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

    @Column(name = "taux_imposition")
    private Double tauxImposition;

    @Column(name = "actif")
    private boolean actif = true;

    // Constructeurs
    public ProprieteImpot() {
    }

    public ProprieteImpot(Propriete propriete, NatureImpot natureImpot, Double tauxImposition) {
        this.propriete = propriete;
        this.natureImpot = natureImpot;
        this.tauxImposition = tauxImposition;
        this.actif = true;
    }

    // Getters et Setters
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

    public Double getTauxImposition() {
        return tauxImposition;
    }

    public void setTauxImposition(Double tauxImposition) {
        this.tauxImposition = tauxImposition;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
}
