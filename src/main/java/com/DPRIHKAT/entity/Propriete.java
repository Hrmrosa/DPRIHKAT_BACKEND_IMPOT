package com.DPRIHKAT.entity;

/**
 *
 * @author amateur
 */

import com.DPRIHKAT.entity.enums.TypePropriete;
import com.DPRIHKAT.entity.enums.TypeContribuable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import java.io.File;
import java.io.IOException;
import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Propriete {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TypePropriete type;

    private String localite;

    private Integer rangLocalite;

    private Double superficie;

    private String adresse;

    @Column(columnDefinition = "geometry")
    @JsonIgnore
    private Geometry location;
    
    private boolean actif = true; // Champ pour la suppression logique
    
    private boolean declare = false; // Indique si le bien a été déclaré
    
    private boolean declarationEnLigne = false; // Indique si la déclaration a été faite en ligne
    
    @ManyToMany
    @JoinTable(
        name = "propriete_nature_impot",
        joinColumns = @JoinColumn(name = "propriete_id"),
        inverseJoinColumns = @JoinColumn(name = "nature_impot_id")
    )
    private List<NatureImpot> naturesImpot = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proprietaire_id")
    @JsonIdentityReference(alwaysAsId = false)
    private Contribuable proprietaire;

    @OneToMany(mappedBy = "propriete")
    @JsonIgnore
    private List<Declaration> declarations = new ArrayList<>();

    private Double montantImpot;

    public Propriete() {
    }

    public Propriete(TypePropriete type, String localite, Integer rangLocalite, Double superficie, String adresse, Geometry location, Contribuable proprietaire) {
        this.type = type;
        this.localite = localite;
        this.rangLocalite = rangLocalite;
        this.superficie = superficie;
        this.adresse = adresse;
        this.location = location;
        this.proprietaire = proprietaire;
        this.actif = true;
        this.declare = false;
        this.declarationEnLigne = false;
    }

    // Méthodes
    public void calculerImpôt() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = new File("taux_if.json"); // Chemin vers le fichier JSON
            Map<String, Object> tauxData = mapper.readValue(jsonFile, Map.class);

            String typeKey;
            switch (type) {
                case VI -> typeKey = "villas";
                case AP -> typeKey = "appartements";
                case CH -> typeKey = "commercial";
                case DEPOT -> typeKey = "domestic";
                default -> {
                    montantImpot = 0.0; return;
                }
            }

            String rangKey = "rang" + rangLocalite;
            Map<String, Map<String, Double>> tauxMap = (Map<String, Map<String, Double>>) tauxData.get(typeKey);
            Map<String, Double> tauxRang = tauxMap.get(rangKey);

            boolean isPersonneMorale = proprietaire.getType() == TypeContribuable.PERSONNE_MORALE;
            double taux = isPersonneMorale ? tauxRang.get("morale") : tauxRang.get("physique");

            if (type == TypePropriete.VI) {
                montantImpot = superficie * taux;
            } else {
                montantImpot = taux;
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier JSON des taux IF", e);
        }
    }
    
    /**
     * Déclare le bien
     * @param enLigne true si la déclaration est faite en ligne, false si elle est faite à l'administration
     */
    public void declarer(boolean enLigne) {
        this.declare = true;
        this.declarationEnLigne = enLigne;
    }
    
    /**
     * Ajoute une nature d'impôt au bien
     * @param natureImpot la nature d'impôt à ajouter
     */
    public void ajouterNatureImpot(NatureImpot natureImpot) {
        if (!this.naturesImpot.contains(natureImpot)) {
            this.naturesImpot.add(natureImpot);
            if (!natureImpot.getProprietes().contains(this)) {
                natureImpot.getProprietes().add(this);
            }
        }
    }
    
    /**
     * Retire une nature d'impôt du bien
     * @param natureImpot la nature d'impôt à retirer
     */
    public void retirerNatureImpot(NatureImpot natureImpot) {
        if (this.naturesImpot.contains(natureImpot)) {
            this.naturesImpot.remove(natureImpot);
            if (natureImpot.getProprietes().contains(this)) {
                natureImpot.getProprietes().remove(this);
            }
        }
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TypePropriete getType() {
        return type;
    }

    public void setType(TypePropriete type) {
        this.type = type;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public Integer getRangLocalite() {
        return rangLocalite;
    }

    public void setRangLocalite(Integer rangLocalite) {
        this.rangLocalite = rangLocalite;
    }

    public Double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Geometry getLocation() {
        return location;
    }

    public void setLocation(Geometry location) {
        this.location = location;
    }

    @Transient
    public Double getLatitude() {
        try {
            return location != null ? location.getCoordinate().y : null;
        } catch (Exception e) {
            return null;
        }
    }

    @Transient
    public Double getLongitude() {
        try {
            return location != null ? location.getCoordinate().x : null;
        } catch (Exception e) {
            return null;
        }
    }

    public Contribuable getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Contribuable proprietaire) {
        this.proprietaire = proprietaire;
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
    
    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
    
    public boolean isDeclare() {
        return declare;
    }

    public void setDeclare(boolean declare) {
        this.declare = declare;
    }

    public boolean isDeclarationEnLigne() {
        return declarationEnLigne;
    }

    public void setDeclarationEnLigne(boolean declarationEnLigne) {
        this.declarationEnLigne = declarationEnLigne;
    }
    
    public List<NatureImpot> getNaturesImpot() {
        return naturesImpot;
    }

    public void setNaturesImpot(List<NatureImpot> naturesImpot) {
        this.naturesImpot = naturesImpot;
    }
}