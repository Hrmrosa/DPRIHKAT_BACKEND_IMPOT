/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DPRIHKAT.entity;

/**
 *
 * @author amateur
 */
import com.DPRIHKAT.entity.enums.StatutPlaque;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Plaque {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String numeroSerie;

    private String numplaque; // Nouveau champ ajouté

    private boolean disponible;

    private String codeQR;
    
    @Column(name = "document", nullable = false)
    private String document;
    
    @Enumerated(EnumType.STRING)
    private StatutPlaque statut = StatutPlaque.STOCK;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "propriete_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Propriete propriete;
    
    @OneToOne
    @JoinColumn(name = "demande_id")
    @JsonIdentityReference(alwaysAsId = true)
    private DemandePlaque demande;

    @Transient
    private UUID vehiculeId;

    @Transient
    private UUID proprieteId;

    @Transient
    private UUID demandeId;

    public Plaque() {
    }

    public Plaque(String numeroSerie, String numplaque, boolean disponible) {
        this.numeroSerie = numeroSerie;
        this.numplaque = numplaque;
        this.disponible = disponible;
        this.statut = StatutPlaque.STOCK;
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

    public String getCodeQR() {
        return codeQR;
    }

    public void setCodeQR(String codeQR) {
        this.codeQR = codeQR;
    }
    
    public String getDocument() {
        return document;
    }
    
    public void setDocument(String document) {
        this.document = document;
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
    
    public StatutPlaque getStatut() {
        return statut;
    }
    
    public void setStatut(StatutPlaque statut) {
        this.statut = statut;
    }
    
    public DemandePlaque getDemande() {
        return demande;
    }
    
    public void setDemande(DemandePlaque demande) {
        this.demande = demande;
    }

    public UUID getVehiculeId() {
        return vehiculeId;
    }

    public void setVehiculeId(UUID vehiculeId) {
        this.vehiculeId = vehiculeId;
    }

    public UUID getProprieteId() {
        return proprieteId;
    }

    public void setProprieteId(UUID proprieteId) {
        this.proprieteId = proprieteId;
    }

    public UUID getDemandeId() {
        return demandeId;
    }

    public void setDemandeId(UUID demandeId) {
        this.demandeId = demandeId;
    }
}