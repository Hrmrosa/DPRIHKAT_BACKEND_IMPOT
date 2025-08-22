/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DPRIHKAT.entity;

/**
 *
 * @author amateur
 */
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Plaque {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String numeroSerie;

    private String numplaque; // Nouveau champ ajouté

    private boolean disponible;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "propriete_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Propriete propriete;

    public Plaque() {
    }

    public Plaque(String numeroSerie, String numplaque, boolean disponible) {
        this.numeroSerie = numeroSerie;
        this.numplaque = numplaque;
        this.disponible = disponible;
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getNumplaque() {
        return numplaque;
    }

    public void setNumplaque(String numplaque) {
        this.numplaque = numplaque;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Propriete getPropriete() {
        return propriete;
    }

    public void setPropriete(Propriete propriete) {
        this.propriete = propriete;
    }
}