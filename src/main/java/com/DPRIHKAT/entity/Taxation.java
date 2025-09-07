package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Entité représentant la taxation d'un bien déclaré
 * La taxation est le fait de taxer un bien déjà déclaré et assujetti à un type d'impôt
 * 
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

    private String exercice;

    @Enumerated(EnumType.STRING)
    private StatutTaxation statut;

    @Enumerated(EnumType.STRING)
    private TypeImpot typeImpot;

    private boolean exoneration;
    
    private boolean actif = true; // Champ pour la suppression logique

    // Bien déclaré concerné par la taxation
    @ManyToOne
    @JoinColumn(name = "declaration_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Declaration declaration;

    // Agent qui a effectué la taxation
    @ManyToOne
    @JoinColumn(name = "agent_taxateur_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Agent agentTaxateur;

    // Agent qui a validé la taxation
    @ManyToOne
    @JoinColumn(name = "agent_validateur_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Agent agentValidateur;

    // Paiement associé à la taxation
    @OneToOne(mappedBy = "taxation")
    private Paiement paiement;

    // Apurements associés à la taxation
    @OneToMany(mappedBy = "taxation")
    private List<Apurement> apurements = new ArrayList<>();

    public Taxation() {
    }

    public Taxation(Date dateTaxation, Double montant, String exercice, StatutTaxation statut, 
                   TypeImpot typeImpot, boolean exoneration, Declaration declaration, Agent agentTaxateur) {
        this.dateTaxation = dateTaxation;
        this.montant = montant;
        this.exercice = exercice;
        this.statut = statut;
        this.typeImpot = typeImpot;
        this.exoneration = exoneration;
        this.declaration = declaration;
        this.agentTaxateur = agentTaxateur;
        this.actif = true;
    }

    // Méthodes
    public void validerTaxation(Agent agentValidateur) {
        if (this.statut != StatutTaxation.SOUMISE) {
            throw new IllegalStateException("Seule une taxation soumise peut être validée.");
        }
        this.agentValidateur = agentValidateur;
        this.statut = StatutTaxation.VALIDEE;
    }

    public void associerPaiement(Paiement paiement) {
        if (this.statut != StatutTaxation.VALIDEE) {
            throw new IllegalStateException("Seule une taxation validée peut être associée à un paiement.");
        }
        this.paiement = paiement;
        this.statut = StatutTaxation.PAYEE;
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

    public String getExercice() {
        return exercice;
    }

    public void setExercice(String exercice) {
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

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }

    public Agent getAgentTaxateur() {
        return agentTaxateur;
    }

    public void setAgentTaxateur(Agent agentTaxateur) {
        this.agentTaxateur = agentTaxateur;
    }

    public Agent getAgentValidateur() {
        return agentValidateur;
    }

    public void setAgentValidateur(Agent agentValidateur) {
        this.agentValidateur = agentValidateur;
    }

    public Paiement getPaiement() {
        return paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public List<Apurement> getApurements() {
        return apurements;
    }

    public void setApurements(List<Apurement> apurements) {
        this.apurements = apurements;
    }
}
