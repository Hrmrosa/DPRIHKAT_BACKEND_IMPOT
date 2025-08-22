package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutCertificat;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "certificats")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Certificat {
    
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
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutCertificat statut;
    
    @Column(name = "actif")
    private boolean actif;
    
    @ManyToOne
    @JoinColumn(name = "declaration_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Declaration declaration;
    
    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Vehicule vehicule;
    
    @ManyToOne
    @JoinColumn(name = "agent_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Agent agent;
    
    @Column(name = "motif_annulation")
    private String motifAnnulation;
    
    // Constructors
    public Certificat() {}
    
    public Certificat(String numero, Date dateExpiration, double montant, Declaration declaration, Vehicule vehicule, Agent agent) {
        this.numero = numero;
        this.dateEmission = new Date();
        this.dateExpiration = dateExpiration;
        this.montant = montant;
        this.statut = StatutCertificat.ACTIF;
        this.actif = true;
        this.declaration = declaration;
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
    
    public StatutCertificat getStatut() {
        return statut;
    }
    
    public void setStatut(StatutCertificat statut) {
        this.statut = statut;
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
    
    public String getMotifAnnulation() {
        return motifAnnulation;
    }
    
    public void setMotifAnnulation(String motifAnnulation) {
        this.motifAnnulation = motifAnnulation;
    }
}
