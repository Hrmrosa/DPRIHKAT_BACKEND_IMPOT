package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutVignette;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "vignettes")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Vignette {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name = "numero")
    private String numero;
    
    @Column(name = "date_emission")
    private Date dateEmission;
    
    @Column(name = "date_expiration")
    private Date dateExpiration;
    
    @Column(name = "montant")
    private double montant;

    // Eclatement des montants selon le fichier de tarification
    @Column(name = "tscr_usd")
    private Double tscrUsd;

    @Column(name = "impot_reel_cdf")
    private Double impotReelCdf;

    @Column(name = "total_cdf")
    private Double totalCdf;

    // Métadonnées de tarification
    @Column(name = "puissance_fiscale_cv")
    private Double puissanceFiscale;

    @Column(name = "categorie_tarifaire")
    private String categorieTarifaire;

    @Column(name = "plage_tarifaire")
    private String plageTarifaire;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutVignette statut;
    
    @Column(name = "actif")
    private boolean actif;
    
    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Vehicule vehicule;
    
    @ManyToOne
    @JoinColumn(name = "agent_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Agent agent;
    
    // Constructors
    public Vignette() {}
    
    public Vignette(String numero, Date dateExpiration, double montant, Vehicule vehicule, Agent agent) {
        this.numero = numero;
        this.dateEmission = new Date();
        this.dateExpiration = dateExpiration;
        this.montant = montant;
        this.statut = StatutVignette.ACTIVE;
        this.actif = true;
        this.vehicule = vehicule;
        this.agent = agent;
    }
    
    // Getters and setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public Date getDateEmission() {
        return dateEmission;
    }
    
    public void setDateEmission(Date dateEmission) {
        this.dateEmission = dateEmission;
    }
    
    public Date getDateExpiration() {
        return dateExpiration;
    }
    
    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
    
    public double getMontant() {
        return montant;
    }
    
    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Double getTscrUsd() {
        return tscrUsd;
    }

    public void setTscrUsd(Double tscrUsd) {
        this.tscrUsd = tscrUsd;
    }

    public Double getImpotReelCdf() {
        return impotReelCdf;
    }

    public void setImpotReelCdf(Double impotReelCdf) {
        this.impotReelCdf = impotReelCdf;
    }

    public Double getTotalCdf() {
        return totalCdf;
    }

    public void setTotalCdf(Double totalCdf) {
        this.totalCdf = totalCdf;
    }

    public Double getPuissanceFiscale() {
        return puissanceFiscale;
    }

    public void setPuissanceFiscale(Double puissanceFiscale) {
        this.puissanceFiscale = puissanceFiscale;
    }

    public String getCategorieTarifaire() {
        return categorieTarifaire;
    }

    public void setCategorieTarifaire(String categorieTarifaire) {
        this.categorieTarifaire = categorieTarifaire;
    }

    public String getPlageTarifaire() {
        return plageTarifaire;
    }

    public void setPlageTarifaire(String plageTarifaire) {
        this.plageTarifaire = plageTarifaire;
    }
    
    public StatutVignette getStatut() {
        return statut;
    }
    
    public void setStatut(StatutVignette statut) {
        this.statut = statut;
    }
    
    public boolean isActif() {
        return actif;
    }
    
    public void setActif(boolean actif) {
        this.actif = actif;
    }
    
    public Vehicule getVehicule() {
        return vehicule;
    }
    
    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }
    
    public Agent getAgent() {
        return agent;
    }
    
    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
