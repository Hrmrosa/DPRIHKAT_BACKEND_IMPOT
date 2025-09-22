package com.DPRIHKAT.dto;

import java.util.UUID;

/**
 * DTO pour les informations détaillées d'un contribuable
 */
public class ContribuableDTO {
    private UUID id;
    private String nom;
    private String prenom;
    private String raisonSociale;
    private String numeroIdentification;
    private String adresse;
    private String telephone;
    private String email;
    private boolean actif;
    private String type;
    
    // Statistiques
    private long nombreVehicules;
    private long nombreProprietes;
    private long nombreDeclarations;
    private long nombrePaiements;
    
    // Getters et Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getRaisonSociale() {
        return raisonSociale;
    }
    
    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }
    
    public String getNumeroIdentification() {
        return numeroIdentification;
    }
    
    public void setNumeroIdentification(String numeroIdentification) {
        this.numeroIdentification = numeroIdentification;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public boolean isActif() {
        return actif;
    }
    
    public void setActif(boolean actif) {
        this.actif = actif;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public long getNombreVehicules() {
        return nombreVehicules;
    }
    
    public void setNombreVehicules(long nombreVehicules) {
        this.nombreVehicules = nombreVehicules;
    }
    
    public long getNombreProprietes() {
        return nombreProprietes;
    }
    
    public void setNombreProprietes(long nombreProprietes) {
        this.nombreProprietes = nombreProprietes;
    }
    
    public long getNombreDeclarations() {
        return nombreDeclarations;
    }
    
    public void setNombreDeclarations(long nombreDeclarations) {
        this.nombreDeclarations = nombreDeclarations;
    }
    
    public long getNombrePaiements() {
        return nombrePaiements;
    }
    
    public void setNombrePaiements(long nombrePaiements) {
        this.nombrePaiements = nombrePaiements;
    }
}
