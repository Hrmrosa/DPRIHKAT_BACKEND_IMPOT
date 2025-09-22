package com.DPRIHKAT.model;

import java.util.List;

/**
 * Modèle pour représenter les données des véhicules
 * 
 * @author amateur
 */
public class Vehicule {
    
    private String marque;
    private String modele;
    private String annee;
    private String type;
    private String puissance;
    private String carburant;
    
    // Constructeurs
    public Vehicule() {
    }
    
    public Vehicule(String marque, String modele) {
        this.marque = marque;
        this.modele = modele;
    }
    
    public Vehicule(String marque, String modele, String annee, String type, String puissance, String carburant) {
        this.marque = marque;
        this.modele = modele;
        this.annee = annee;
        this.type = type;
        this.puissance = puissance;
        this.carburant = carburant;
    }
    
    // Getters et Setters
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
    
    public String getAnnee() {
        return annee;
    }
    
    public void setAnnee(String annee) {
        this.annee = annee;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getPuissance() {
        return puissance;
    }
    
    public void setPuissance(String puissance) {
        this.puissance = puissance;
    }
    
    public String getCarburant() {
        return carburant;
    }
    
    public void setCarburant(String carburant) {
        this.carburant = carburant;
    }
}
