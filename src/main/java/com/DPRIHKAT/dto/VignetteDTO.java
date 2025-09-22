package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.StatutVignette;
import java.util.Date;
import java.util.UUID;

/**
 * DTO pour les vignettes avec les informations essentielles
 * Utilisé pour limiter le nombre de colonnes dans les requêtes SQL
 */
public class VignetteDTO {
    
    private UUID id;
    private String numero;
    private Date dateEmission;
    private Date dateExpiration;
    private double montant;
    private Double tscrUsd;
    private Double impotReelCdf;
    private Double totalCdf;
    private Double puissanceFiscale;
    private String categorieTarifaire;
    private String plageTarifaire;
    private StatutVignette statut;
    private String document;
    private UUID vehiculeId;
    private String vehiculeImmatriculation;
    private String vehiculeMarque;
    private String vehiculeModele;
    private UUID taxationId;
    
    // Constructeur par défaut
    public VignetteDTO() {}
    
    // Constructeur avec tous les champs
    public VignetteDTO(UUID id, String numero, Date dateEmission, Date dateExpiration, double montant,
                      Double tscrUsd, Double impotReelCdf, Double totalCdf, Double puissanceFiscale,
                      String categorieTarifaire, String plageTarifaire, StatutVignette statut,
                      String document, UUID vehiculeId, String vehiculeImmatriculation,
                      String vehiculeMarque, String vehiculeModele, UUID taxationId) {
        this.id = id;
        this.numero = numero;
        this.dateEmission = dateEmission;
        this.dateExpiration = dateExpiration;
        this.montant = montant;
        this.tscrUsd = tscrUsd;
        this.impotReelCdf = impotReelCdf;
        this.totalCdf = totalCdf;
        this.puissanceFiscale = puissanceFiscale;
        this.categorieTarifaire = categorieTarifaire;
        this.plageTarifaire = plageTarifaire;
        this.statut = statut;
        this.document = document;
        this.vehiculeId = vehiculeId;
        this.vehiculeImmatriculation = vehiculeImmatriculation;
        this.vehiculeMarque = vehiculeMarque;
        this.vehiculeModele = vehiculeModele;
        this.taxationId = taxationId;
    }
    
    // Getters et Setters
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
    
    public String getDocument() {
        return document;
    }
    
    public void setDocument(String document) {
        this.document = document;
    }
    
    public UUID getVehiculeId() {
        return vehiculeId;
    }
    
    public void setVehiculeId(UUID vehiculeId) {
        this.vehiculeId = vehiculeId;
    }
    
    public String getVehiculeImmatriculation() {
        return vehiculeImmatriculation;
    }
    
    public void setVehiculeImmatriculation(String vehiculeImmatriculation) {
        this.vehiculeImmatriculation = vehiculeImmatriculation;
    }
    
    public String getVehiculeMarque() {
        return vehiculeMarque;
    }
    
    public void setVehiculeMarque(String vehiculeMarque) {
        this.vehiculeMarque = vehiculeMarque;
    }
    
    public String getVehiculeModele() {
        return vehiculeModele;
    }
    
    public void setVehiculeModele(String vehiculeModele) {
        this.vehiculeModele = vehiculeModele;
    }
    
    public UUID getTaxationId() {
        return taxationId;
    }
    
    public void setTaxationId(UUID taxationId) {
        this.taxationId = taxationId;
    }
}
