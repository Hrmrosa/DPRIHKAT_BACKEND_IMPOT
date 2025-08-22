package com.DPRIHKAT.dto;

public class BienDTO {
    private String type; // TypePropriete enum name (AP, VI, AT, CITERNE, DEPOT, CH, TE)
    private String localite;
    private Integer rangLocalite;
    private Double superficie;
    private String adresse;
    private Double latitude;
    private Double longitude;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public Integer getRangLocalite() {
        return rangLocalite;
    }

    public void setRangLocalite(Integer rangLocalite) {
        this.rangLocalite = rangLocalite;
    }

    public Double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
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
}
