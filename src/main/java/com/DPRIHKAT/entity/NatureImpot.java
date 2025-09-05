package com.DPRIHKAT.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entité représentant la nature d'un impôt
 * @author amateur
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class NatureImpot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String code;

    private String nom;

    private String description;

    @OneToMany(mappedBy = "natureImpot")
    private List<ProprieteImpot> proprieteImpots = new ArrayList<>();

    private boolean actif = true;

    public NatureImpot() {
    }

    public NatureImpot(String code, String nom, String description) {
        this.code = code;
        this.nom = nom;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProprieteImpot> getProprieteImpots() {
        return proprieteImpots;
    }

    public void setProprieteImpots(List<ProprieteImpot> proprieteImpots) {
        this.proprieteImpots = proprieteImpots;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
}
