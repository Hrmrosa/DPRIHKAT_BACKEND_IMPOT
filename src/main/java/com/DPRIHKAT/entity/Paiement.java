package com.DPRIHKAT.entity;

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

/**
 * Entité représentant un paiement
 * Un paiement est associé à une taxation
 * 
 * @author amateur
 */
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
    
    private boolean actif = true; // Champ pour la suppression logique

    @OneToMany(mappedBy = "paiement")
    private List<Penalite> penalites = new ArrayList<>();

    // Taxation associée au paiement
    @OneToOne
    @JoinColumn(name = "taxation_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Taxation taxation;

    @ManyToOne
    @JoinColumn(name = "declaration_id")
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
        this.actif = true;
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
    
    /**
     * Associe ce paiement à une taxation
     * @param taxation la taxation à associer
     */
    public void associerTaxation(Taxation taxation) {
        this.taxation = taxation;
        taxation.setPaiement(this);
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
    
    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public List<Penalite> getPenalites() {
        return penalites;
    }

    public void setPenalites(List<Penalite> penalites) {
        this.penalites = penalites;
    }

    public Taxation getTaxation() {
        return taxation;
    }

    public void setTaxation(Taxation taxation) {
        this.taxation = taxation;
    }

    /**
     * Définit la déclaration associée à ce paiement
     * @param declaration La déclaration à associer
     */
    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }

    /**
     * Récupère la déclaration associée à ce paiement
     * @return La déclaration associée
     */
    public Declaration getDeclaration() {
        return declaration;
    }
}
