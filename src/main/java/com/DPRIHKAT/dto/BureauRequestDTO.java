package com.DPRIHKAT.dto;

import java.util.UUID;

public class BureauRequestDTO {
    private String nom;
    private String code;
    private String adresse;
    private String telephone;
    private String email;
    private UUID divisionId;

    public BureauRequestDTO() {}

    public BureauRequestDTO(String nom, String code, String adresse, String telephone, String email, UUID divisionId) {
        this.nom = nom;
        this.code = code;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.divisionId = divisionId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public UUID getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(UUID divisionId) {
        this.divisionId = divisionId;
    }
}
