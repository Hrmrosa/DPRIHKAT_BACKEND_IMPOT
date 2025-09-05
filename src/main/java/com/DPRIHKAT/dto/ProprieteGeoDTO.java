package com.DPRIHKAT.dto;

import java.util.UUID;

/**
 * DTO pour représenter les propriétés avec leurs coordonnées géographiques
 * et les informations sur leurs propriétaires pour l'affichage sur une carte
 */
public class ProprieteGeoDTO {
    private UUID id;
    private String adresse;
    private String localite;
    private String type; // Utiliser String au lieu de TypePropriete
    private Double superficie;
    private Double latitude;
    private Double longitude;
    private ContribuableSimpleDTO proprietaire;

    public ProprieteGeoDTO() {
    }

    public ProprieteGeoDTO(UUID id, String adresse, String localite, String type, 
                          Double superficie, Double latitude, Double longitude, 
                          ContribuableSimpleDTO proprietaire) {
        this.id = id;
        this.adresse = adresse;
        this.localite = localite;
        this.type = type;
        this.superficie = superficie;
        this.latitude = latitude;
        this.longitude = longitude;
        this.proprietaire = proprietaire;
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

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public ContribuableSimpleDTO getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(ContribuableSimpleDTO proprietaire) {
        this.proprietaire = proprietaire;
    }
}
