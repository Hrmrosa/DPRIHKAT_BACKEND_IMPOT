package com.DPRIHKAT.dto;

import java.util.List;
import java.util.UUID;

/**
 * DTO pour représenter les détails complets d'un contribuable
 */
public class ContribuableDetailsDTO {
    private UUID id;
    private String nom;
    private List<ProprieteDTO> proprietes;
    private List<VehiculeDTO> vehicules;

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

    public List<ProprieteDTO> getProprietes() {
        return proprietes;
    }

    public void setProprietes(List<ProprieteDTO> proprietes) {
        this.proprietes = proprietes;
    }

    public List<VehiculeDTO> getVehicules() {
        return vehicules;
    }

    public void setVehicules(List<VehiculeDTO> vehicules) {
        this.vehicules = vehicules;
    }

    /**
     * DTO pour représenter une propriété simplifiée
     */
    public static class ProprieteDTO {
        private UUID id;
        private String adresse;
        private Double valeur;

        public ProprieteDTO() {
        }

        public ProprieteDTO(UUID id, String adresse, Double valeur) {
            this.id = id;
            this.adresse = adresse;
            this.valeur = valeur;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getAdresse() {
            return adresse;
        }

        public void setAdresse(String adresse) {
            this.adresse = adresse;
        }

        public Double getValeur() {
            return valeur;
        }

        public void setValeur(Double valeur) {
            this.valeur = valeur;
        }
    }

    /**
     * DTO pour représenter un véhicule simplifié
     */
    public static class VehiculeDTO {
        private UUID id;
        private String immatriculation;
        private String marque;
        private String modele;

        public VehiculeDTO() {
        }

        public VehiculeDTO(UUID id, String immatriculation, String marque, String modele) {
            this.id = id;
            this.immatriculation = immatriculation;
            this.marque = marque;
            this.modele = modele;
        }

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
    }
}
