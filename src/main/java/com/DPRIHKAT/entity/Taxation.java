package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Entité représentant une taxation d'un bien
 * La taxation est différente de la déclaration :
 * - La déclaration est le fait d'ajouter un bien à un contribuable
 * - La taxation est le fait de taxer un bien
 * @author amateur
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Taxation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Date dateTaxation;

    private Double montant;

    private Integer exercice; // Année fiscale

    @Enumerated(EnumType.STRING)
    private StatutTaxation statut;

    @Enumerated(EnumType.STRING)
    private TypeImpot typeImpot; // IF, IRL, ICM, etc.

    private boolean exoneration; // Gestion des exonérations

    private String motifExoneration; // Motif de l'exonération, si applicable

    private Date dateEcheance; // Date limite de paiement

    private boolean actif = true; // Champ pour la suppression logique

    @ManyToOne
    @JoinColumn(name = "propriete_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Propriete propriete;

    @ManyToOne
    @JoinColumn(name = "propriete_impot_id")
    @JsonIdentityReference(alwaysAsId = true)
    private ProprieteImpot proprieteImpot;

    @ManyToOne
    @JoinColumn(name = "agent_taxateur_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Agent agentTaxateur;

    @OneToOne
    @JoinColumn(name = "paiement_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Paiement paiement;

    @OneToOne
    @JoinColumn(name = "apurement_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Apurement apurement;

    public Taxation() {
    }

    public Taxation(Date dateTaxation, Double montant, Integer exercice, StatutTaxation statut, TypeImpot typeImpot, 
                   boolean exoneration, String motifExoneration, Date dateEcheance, Propriete propriete, 
                   ProprieteImpot proprieteImpot, Agent agentTaxateur) {
        this.dateTaxation = dateTaxation;
        this.montant = montant;
        this.exercice = exercice;
        this.statut = statut;
        this.typeImpot = typeImpot;
        this.exoneration = exoneration;
        this.motifExoneration = motifExoneration;
        this.dateEcheance = dateEcheance;
        this.propriete = propriete;
        this.proprieteImpot = proprieteImpot;
        this.agentTaxateur = agentTaxateur;
        this.actif = true;
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDateTaxation() {
        return dateTaxation;
    }

    public void setDateTaxation(Date dateTaxation) {
        this.dateTaxation = dateTaxation;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Integer getExercice() {
        return exercice;
    }

    public void setExercice(Integer exercice) {
        this.exercice = exercice;
    }

    public StatutTaxation getStatut() {
        return statut;
    }

    public void setStatut(StatutTaxation statut) {
        this.statut = statut;
    }

    public TypeImpot getTypeImpot() {
        return typeImpot;
    }

    public void setTypeImpot(TypeImpot typeImpot) {
        this.typeImpot = typeImpot;
    }

    public boolean isExoneration() {
        return exoneration;
    }

    public void setExoneration(boolean exoneration) {
        this.exoneration = exoneration;
    }

    public String getMotifExoneration() {
        return motifExoneration;
    }

    public void setMotifExoneration(String motifExoneration) {
        this.motifExoneration = motifExoneration;
    }

    public Date getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Propriete getPropriete() {
        return propriete;
    }

    public void setPropriete(Propriete propriete) {
        this.propriete = propriete;
    }

    public ProprieteImpot getProprieteImpot() {
        return proprieteImpot;
    }

    public void setProprieteImpot(ProprieteImpot proprieteImpot) {
        this.proprieteImpot = proprieteImpot;
    }

    public Agent getAgentTaxateur() {
        return agentTaxateur;
    }

    public void setAgentTaxateur(Agent agentTaxateur) {
        this.agentTaxateur = agentTaxateur;
    }

    public Paiement getPaiement() {
        return paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public Apurement getApurement() {
        return apurement;
    }

    public void setApurement(Apurement apurement) {
        this.apurement = apurement;
    }
}
