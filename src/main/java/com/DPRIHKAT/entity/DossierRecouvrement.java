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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DossierRecouvrement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Double totalDu;

    private Double totalRecouvre;

    private Date dateOuverture;

    private Date dateCloture;

    private String codeQR;

    @OneToOne
    @JoinColumn(name = "contribuable_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Contribuable contribuable;

    @OneToMany(mappedBy = "dossierRecouvrement")
    private List<Relance> relances = new ArrayList<>();

    @OneToMany(mappedBy = "dossierRecouvrement")
    private List<Poursuite> poursuites = new ArrayList<>();

    @OneToOne(mappedBy = "dossierRecouvrement")
    private Apurement apurement;

    public DossierRecouvrement() {
    }

    public DossierRecouvrement(Double totalDu, Double totalRecouvre, Date dateOuverture, Date dateCloture) {
        this.totalDu = totalDu;
        this.totalRecouvre = totalRecouvre;
        this.dateOuverture = dateOuverture;
        this.dateCloture = dateCloture;
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getTotalDu() {
        return totalDu;
    }

    public void setTotalDu(Double totalDu) {
        this.totalDu = totalDu;
    }

    public Double getTotalRecouvre() {
        return totalRecouvre;
    }

    public void setTotalRecouvre(Double totalRecouvre) {
        this.totalRecouvre = totalRecouvre;
    }

    public Date getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(Date dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public Date getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(Date dateCloture) {
        this.dateCloture = dateCloture;
    }

    public String getCodeQR() {
        return codeQR;
    }

    public void setCodeQR(String codeQR) {
        this.codeQR = codeQR;
    }

    public Contribuable getContribuable() {
        return contribuable;
    }

    public void setContribuable(Contribuable contribuable) {
        this.contribuable = contribuable;
    }

    public List<Relance> getRelances() {
        return relances;
    }

    public void setRelances(List<Relance> relances) {
        this.relances = relances;
    }

    public List<Poursuite> getPoursuites() {
        return poursuites;
    }

    public void setPoursuites(List<Poursuite> poursuites) {
        this.poursuites = poursuites;
    }

    public Apurement getApurement() {
        return apurement;
    }

    public void setApurement(Apurement apurement) {
        this.apurement = apurement;
    }
}