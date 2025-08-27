package com.DPRIHKAT.dto;

import java.util.UUID;

public class DivisionSimpleDTO {
    private UUID id;
    private String nom;
    private String code;

    public DivisionSimpleDTO() {}

    public DivisionSimpleDTO(UUID id, String nom, String code) {
        this.id = id;
        this.nom = nom;
        this.code = code;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
