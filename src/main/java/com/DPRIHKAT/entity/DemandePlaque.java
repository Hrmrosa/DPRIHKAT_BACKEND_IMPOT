package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutDemande;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

/**
 * Entité représentant une demande de plaque d'immatriculation
 * 
 * @author amateur
 */
@Entity
@Table(name = "demandes_plaque")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DemandePlaque {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    private StatutDemande statut;
    
    @Column(name = "date_demande")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDemande;
    
    @Column(name = "date_validation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValidation;
    
    @Column(name = "date_paiement")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePaiement;
    
    @Column(name = "date_livraison")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLivraison;
    
    @Column(name = "facture_path")
    private String facturePath;
    
    @Column(name = "motif_rejet")
    private String motifRejet;
    
    @ManyToOne
    @JoinColumn(name = "contribuable_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Contribuable contribuable;
    
    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Vehicule vehicule;
    
    @OneToOne
    @JoinColumn(name = "taxation_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Taxation taxation;
    
    @OneToOne(mappedBy = "demande")
    private Plaque plaque;
    
    @ManyToOne
    @JoinColumn(name = "validateur_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Utilisateur validateur;
    
    // Constructeurs
    public DemandePlaque() {
    }
    
    public DemandePlaque(Contribuable contribuable, Vehicule vehicule, String facturePath) {
        this.contribuable = contribuable;
        this.vehicule = vehicule;
        this.facturePath = facturePath;
        this.statut = StatutDemande.SOUMISE;
        this.dateDemande = new Date();
    }
    
    // Getters et Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public StatutDemande getStatut() {
        return statut;
    }
    
    public void setStatut(StatutDemande statut) {
        this.statut = statut;
    }
    
    public Date getDateDemande() {
        return dateDemande;
    }
    
    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }
    
    public Date getDateValidation() {
        return dateValidation;
    }
    
    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }
    
    public Date getDatePaiement() {
        return datePaiement;
    }
    
    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }
    
    public Date getDateLivraison() {
        return dateLivraison;
    }
    
    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }
    
    public String getFacturePath() {
        return facturePath;
    }
    
    public void setFacturePath(String facturePath) {
        this.facturePath = facturePath;
    }
    
    public String getMotifRejet() {
        return motifRejet;
    }
    
    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }
    
    public Contribuable getContribuable() {
        return contribuable;
    }
    
    public void setContribuable(Contribuable contribuable) {
        this.contribuable = contribuable;
    }
    
    public Vehicule getVehicule() {
        return vehicule;
    }
    
    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }
    
    public Taxation getTaxation() {
        return taxation;
    }
    
    public void setTaxation(Taxation taxation) {
        this.taxation = taxation;
    }
    
    public Plaque getPlaque() {
        return plaque;
    }
    
    public void setPlaque(Plaque plaque) {
        this.plaque = plaque;
    }
    
    public Utilisateur getValidateur() {
        return validateur;
    }
    
    public void setValidateur(Utilisateur validateur) {
        this.validateur = validateur;
    }
}
