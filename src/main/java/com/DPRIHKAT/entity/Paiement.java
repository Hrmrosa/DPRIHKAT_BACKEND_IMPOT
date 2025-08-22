/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DPRIHKAT.entity;

/**
 *
 * @author amateur
 */

import com.DPRIHKAT.entity.enums.ModePaiement;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Date date;

    private Double montant;

    @Enumerated(EnumType.STRING)
    private ModePaiement mode;

    @Enumerated(EnumType.STRING)
    private StatutPaiement statut;

    private String bordereauBancaire; // Informations de paiement bancaire

    @OneToMany(mappedBy = "paiement")
    private List<Penalite> penalites = new ArrayList<>();

    @OneToOne(mappedBy = "paiement")
    @JsonIdentityReference(alwaysAsId = true)
    private Declaration declaration;

    public Paiement() {
    }

    public Paiement(Date date, Double montant, ModePaiement mode, StatutPaiement statut, String bordereauBancaire) {
        this.date = date;
        this.montant = montant;
        this.mode = mode;
        this.statut = statut;
        this.bordereauBancaire = bordereauBancaire;
    }

    // Méthodes
        public void genererQuittance() {
        if (statut != StatutPaiement.VALIDE) {
            throw new IllegalStateException("Le paiement doit être validé pour générer une quittance.");
        }
        String quittanceId = "QUITTANCE-" + UUID.randomUUID().toString();
        // TODO: Générer document PDF ou autre format pour la quittance
        System.out.println("Quittance générée avec ID : " + quittanceId);
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public ModePaiement getMode() {
        return mode;
    }

    public void setMode(ModePaiement mode) {
        this.mode = mode;
    }

    public StatutPaiement getStatut() {
        return statut;
    }

    public void setStatut(StatutPaiement statut) {
        this.statut = statut;
    }

    public String getBordereauBancaire() {
        return bordereauBancaire;
    }

    public void setBordereauBancaire(String bordereauBancaire) {
        this.bordereauBancaire = bordereauBancaire;
    }

    public List<Penalite> getPenalites() {
        return penalites;
    }

    public void setPenalites(List<Penalite> penalites) {
        this.penalites = penalites;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }
}