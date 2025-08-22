/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DPRIHKAT.entity;

/**
 *
 * @author amateur
 */

import com.DPRIHKAT.entity.enums.StatutRelance;
import com.DPRIHKAT.entity.enums.TypeRelance;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Relance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Date dateEnvoi;

    @Enumerated(EnumType.STRING)
    private TypeRelance type;

    @Enumerated(EnumType.STRING)
    private StatutRelance statut;

    private String contenu;

    @ManyToOne
    @JoinColumn(name = "dossier_recouvrement_id")
    @JsonIdentityReference(alwaysAsId = true)
    private DossierRecouvrement dossierRecouvrement;

    public Relance() {
    }

    public Relance(Date dateEnvoi, TypeRelance type, StatutRelance statut, String contenu) {
        this.dateEnvoi = dateEnvoi;
        this.type = type;
        this.statut = statut;
        this.contenu = contenu;
    }

    // Méthodes
    public void envoyerRelance() {
        // TODO: Implémentation
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public TypeRelance getType() {
        return type;
    }

    public void setType(TypeRelance type) {
        this.type = type;
    }

    public StatutRelance getStatut() {
        return statut;
    }

    public void setStatut(StatutRelance statut) {
        this.statut = statut;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public DossierRecouvrement getDossierRecouvrement() {
        return dossierRecouvrement;
    }

    public void setDossierRecouvrement(DossierRecouvrement dossierRecouvrement) {
        this.dossierRecouvrement = dossierRecouvrement;
    }
}