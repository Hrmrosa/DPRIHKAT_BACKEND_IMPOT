/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.TypeConcession;
import com.DPRIHKAT.entity.enums.TypeContribuable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.locationtech.jts.geom.Geometry;

/**
 *
 * @author amateur
 */

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ConcessionMinier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Double nombreCarresMinier;

    private Double superficie;

    private Date dateAcquisition;

    @Enumerated(EnumType.STRING)
    private TypeConcession type;

    private String annexe;

    @Column(columnDefinition = "geometry")
    private Geometry location;

    @ManyToOne
    @JoinColumn(name = "contribuable_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Contribuable titulaire;

    @OneToMany(mappedBy = "concession")
    @JsonIgnore
    private List<Declaration> declarations = new ArrayList<>();

    private Double montantImpot;
    
    // Relation avec la taxation
    @OneToOne
    @JoinColumn(name = "taxation_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Taxation taxation;

    public ConcessionMinier() {
    }

    public ConcessionMinier(Double nombreCarresMinier, Double superficie, Date dateAcquisition, TypeConcession type, String annexe, Geometry location, Contribuable titulaire) {
        this.nombreCarresMinier = nombreCarresMinier;
        this.superficie = superficie;
        this.dateAcquisition = dateAcquisition;
        this.type = type;
        this.annexe = annexe;
        this.location = location;
        this.titulaire = titulaire;
    }

    // Méthodes
    public void calculerImpôt() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = new File("taux_icm.json"); // Chemin vers le fichier JSON
            Map<String, Object> tauxData = mapper.readValue(jsonFile, Map.class);
            
            String typeConcession = type == TypeConcession.RECHERCHE ? "recherche" : "exploitation";
            Map<String, Map<String, Double>> tauxMap = (Map<String, Map<String, Double>>) tauxData.get(typeConcession);
            Map<String, Double> tauxAnnexe = tauxMap.get(annexe);
            
            boolean isPersonneMorale = titulaire.getType() == TypeContribuable.PERSONNE_MORALE;
            double taux = isPersonneMorale ? tauxAnnexe.get("morale") : tauxAnnexe.get("physique");
            
            montantImpot = nombreCarresMinier * 84.955 * taux;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier JSON des taux ICM", e);
        }
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getNombreCarresMinier() {
        return nombreCarresMinier;
    }

    public void setNombreCarresMinier(Double nombreCarresMinier) {
        this.nombreCarresMinier = nombreCarresMinier;
    }

    public Double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public Date getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(Date dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public TypeConcession getType() {
        return type;
    }

    public void setType(TypeConcession type) {
        this.type = type;
    }

    public String getAnnexe() {
        return annexe;
    }

    public void setAnnexe(String annexe) {
        this.annexe = annexe;
    }

    public Geometry getLocation() {
        return location;
    }

    public void setLocation(Geometry location) {
        this.location = location;
    }

    public Contribuable getTitulaire() {
        return titulaire;
    }

    public void setTitulaire(Contribuable titulaire) {
        this.titulaire = titulaire;
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<Declaration> declarations) {
        this.declarations = declarations;
    }

    public Double getMontantImpot() {
        return montantImpot;
    }

    public void setMontantImpot(Double montantImpot) {
        this.montantImpot = montantImpot;
    }
    
    public Taxation getTaxation() {
        return taxation;
    }

    public void setTaxation(Taxation taxation) {
        this.taxation = taxation;
    }
}