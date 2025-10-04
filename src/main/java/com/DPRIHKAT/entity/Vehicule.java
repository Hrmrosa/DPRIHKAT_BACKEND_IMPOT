package com.DPRIHKAT.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "vehicules")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Vehicule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name = "marque")
    private String marque;
    
    @Column(name = "modele")
    private String modele;
    
    @Column(name = "annee")
    private int annee;
    
    @Column(name = "immatriculation")
    private String immatriculation;
    
    @Column(name = "numero_chassis")
    private String numeroChassis;
    
    @Column(name = "date_enregistrement")
    private Date dateEnregistrement;
    
    @Column(name = "genre")
    private String genre;
    
    @Column(name = "categorie")
    private String categorie;
    
    @Column(name = "puissance_fiscale")
    private Double puissanceFiscale;
    
    @Column(name = "unite_puissance")
    private String unitePuissance;
    
    @Column(name = "valeur_locative")
    private Double valeurLocative;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietaire_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Contribuable proprietaire;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribuable_id")
    private Contribuable contribuable;
    
    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Plaque> plaques;
    
    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Certificat> certificats;
    
    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vignette> vignettes;
    
    // Constructors
    public Vehicule() {}
    
    public Vehicule(String marque, String modele, int annee, String immatriculation, String numeroChassis) {
        this.marque = marque;
        this.modele = modele;
        this.annee = annee;
        this.immatriculation = immatriculation;
        this.numeroChassis = numeroChassis;
        this.dateEnregistrement = new Date();
    }
    
    // Getters and setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getMarque() {
        return marque;
    }
    
    public void setMarque(String marque) {
        this.marque = marque;
    }
    
    public String getModele() {
        return modele;
    }
    
    public void setModele(String modele) {
        this.modele = modele;
    }
    
    public int getAnnee() {
        return annee;
    }
    
    public void setAnnee(int annee) {
        this.annee = annee;
    }
    
    public String getImmatriculation() {
        return immatriculation;
    }
    
    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }
    
    public String getNumeroChassis() {
        return numeroChassis;
    }

    public void setNumeroChassis(String numeroChassis) {
        this.numeroChassis = numeroChassis;
    }
    
    public Date getDateEnregistrement() {
        return dateEnregistrement;
    }
    
    public void setDateEnregistrement(Date dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }
    
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
    
    public Double getPuissanceFiscale() {
        return puissanceFiscale;
    }

    public void setPuissanceFiscale(Double puissanceFiscale) {
        this.puissanceFiscale = puissanceFiscale;
    }

    public String getUnitePuissance() {
        return unitePuissance;
    }

    public void setUnitePuissance(String unitePuissance) {
        this.unitePuissance = unitePuissance;
    }
    
    public Double getValeurLocative() {
        return valeurLocative;
    }

    public void setValeurLocative(Double valeurLocative) {
        this.valeurLocative = valeurLocative;
    }
    
    public Contribuable getProprietaire() {
        return proprietaire;
    }
    
    public void setProprietaire(Contribuable proprietaire) {
        this.proprietaire = proprietaire;
    }
    
    public Contribuable getContribuable() {
        return contribuable;
    }
    
    public void setContribuable(Contribuable contribuable) {
        this.contribuable = contribuable;
    }
    
    public List<Plaque> getPlaques() {
        return plaques;
    }
    
    public void setPlaques(List<Plaque> plaques) {
        this.plaques = plaques;
    }
    
    public List<Certificat> getCertificats() {
        return certificats;
    }
    
    public void setCertificats(List<Certificat> certificats) {
        this.certificats = certificats;
    }
    
    public List<Vignette> getVignettes() {
        return vignettes;
    }
    
    public void setVignettes(List<Vignette> vignettes) {
        this.vignettes = vignettes;
    }
    
    public Double getValeur() {
        // Logique de calcul de la valeur du véhicule
        // Par exemple, basée sur l'année, la marque et le modèle
        return this.valeurLocative;
    }
}
