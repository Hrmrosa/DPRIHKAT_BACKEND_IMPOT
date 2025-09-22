package com.DPRIHKAT.dto;

import java.util.Date;
import java.util.UUID;

/**
 * DTO pour les véhicules avec les informations essentielles
 * Utilisé pour limiter le nombre de colonnes dans les requêtes SQL
 */
public class VehiculeDTO {
    
    private UUID id;
    private String immatriculation;
    private String marque;
    private String modele;
    private int annee;
    private String numeroChassis;
    private String genre;
    private String categorie;
    private Double puissanceFiscale;
    private String unitePuissance;
    private Date dateEnregistrement;
    private UUID proprietaireId;
    private String proprietaireNom;
    
    // Constructeur par défaut
    public VehiculeDTO() {}
    
    // Constructeur avec tous les champs
    public VehiculeDTO(UUID id, String immatriculation, String marque, String modele, int annee, 
                      String numeroChassis, String genre, String categorie, Double puissanceFiscale, 
                      String unitePuissance, Date dateEnregistrement, UUID proprietaireId, String proprietaireNom) {
        this.id = id;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.annee = annee;
        this.numeroChassis = numeroChassis;
        this.genre = genre;
        this.categorie = categorie;
        this.puissanceFiscale = puissanceFiscale;
        this.unitePuissance = unitePuissance;
        this.dateEnregistrement = dateEnregistrement;
        this.proprietaireId = proprietaireId;
        this.proprietaireNom = proprietaireNom;
    }
    
    // Getters et Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getImmatriculation() {
        return immatriculation;
    }
    
    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
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
    
    public String getNumeroChassis() {
        return numeroChassis;
    }
    
    public void setNumeroChassis(String numeroChassis) {
        this.numeroChassis = numeroChassis;
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
    
    public Date getDateEnregistrement() {
        return dateEnregistrement;
    }
    
    public void setDateEnregistrement(Date dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }
    
    public UUID getProprietaireId() {
        return proprietaireId;
    }
    
    public void setProprietaireId(UUID proprietaireId) {
        this.proprietaireId = proprietaireId;
    }
    
    public String getProprietaireNom() {
        return proprietaireNom;
    }
    
    public void setProprietaireNom(String proprietaireNom) {
        this.proprietaireNom = proprietaireNom;
    }
}
