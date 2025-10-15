/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DPRIHKAT.entity;

/**
 *
 * @author amateur
 */
import com.DPRIHKAT.entity.enums.MotifPenalite;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Penalite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private MotifPenalite motif;

    private Double montant;

    private Date dateApplication;

    private String justification;

    @ManyToOne
    @JoinColumn(name = "declaration_id", nullable = true)
    @JsonIdentityReference(alwaysAsId = true)
    private Declaration declaration;

    @ManyToOne
    @JoinColumn(name = "paiement_id", nullable = true)
    @JsonIdentityReference(alwaysAsId = true)
    private Paiement paiement;

    public Penalite() {
    }

    public Penalite(MotifPenalite motif, Double montant, Date dateApplication) {
        this.motif = motif;
        this.montant = montant;
        this.dateApplication = dateApplication;
    }

    // Méthodes
    public void appliquerPenalite() {
        // Automatisation: 2% par mois pour retard, 25% pour non-déclaration
        if (motif == MotifPenalite.RETARD) {
            montant = declaration.getMontant() * 0.02; // Par mois
        } else if (motif == MotifPenalite.FAUSSE_DECLARATION || motif == MotifPenalite.OUBLI) {
            montant = declaration.getMontant() * 0.25;
        }
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MotifPenalite getMotif() {
        return motif;
    }

    public void setMotif(MotifPenalite motif) {
        this.motif = motif;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Date getDateApplication() {
        return dateApplication;
    }

    public void setDateApplication(Date dateApplication) {
        this.dateApplication = dateApplication;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }

    public Paiement getPaiement() {
        return paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public Date getDatePenalite() {
        return this.dateApplication;
    }
    
    /**
     * Alias pour getDateApplication, utilisé dans certains services
     * @return La date d'application de la pénalité
     */
    public Date getDate() {
        return this.dateApplication;
    }
}