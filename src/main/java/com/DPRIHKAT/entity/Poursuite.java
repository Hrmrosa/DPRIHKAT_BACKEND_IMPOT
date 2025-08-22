/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DPRIHKAT.entity;

/**
 *
 * @author amateur
 */
import com.DPRIHKAT.entity.enums.StatutPoursuite;
import com.DPRIHKAT.entity.enums.TypePoursuite;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Poursuite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TypePoursuite type;

    private Date dateLancement;

    @Enumerated(EnumType.STRING)
    private StatutPoursuite statut;

    private Double montantRecouvre;

    @ManyToOne
    @JoinColumn(name = "agent_initiateur_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Agent agentInitiateur; // Assumé basé sur la relation

    @ManyToOne
    @JoinColumn(name = "dossier_recouvrement_id")
    @JsonIdentityReference(alwaysAsId = true)
    private DossierRecouvrement dossierRecouvrement;

    public Poursuite() {
    }

    public Poursuite(TypePoursuite type, Date dateLancement, StatutPoursuite statut, Double montantRecouvre) {
        this.type = type;
        this.dateLancement = dateLancement;
        this.statut = statut;
        this.montantRecouvre = montantRecouvre;
    }

    // Méthodes
    public void lancerPoursuite() {
        // TODO: Implémentation
    }

    public void genererPV() {
        // TODO: Implémentation
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TypePoursuite getType() {
        return type;
    }

    public void setType(TypePoursuite type) {
        this.type = type;
    }

    public Date getDateLancement() {
        return dateLancement;
    }

    public void setDateLancement(Date dateLancement) {
        this.dateLancement = dateLancement;
    }

    public StatutPoursuite getStatut() {
        return statut;
    }

    public void setStatut(StatutPoursuite statut) {
        this.statut = statut;
    }

    public Double getMontantRecouvre() {
        return montantRecouvre;
    }

    public void setMontantRecouvre(Double montantRecouvre) {
        this.montantRecouvre = montantRecouvre;
    }

    public Agent getAgentInitiateur() {
        return agentInitiateur;
    }

    public void setAgentInitiateur(Agent agentInitiateur) {
        this.agentInitiateur = agentInitiateur;
    }

    public DossierRecouvrement getDossierRecouvrement() {
        return dossierRecouvrement;
    }

    public void setDossierRecouvrement(DossierRecouvrement dossierRecouvrement) {
        this.dossierRecouvrement = dossierRecouvrement;
    }
}
