package com.DPRIHKAT.dto;

import java.util.UUID;

/**
 * DTO pour représenter une nature d'impôt
 * @author amateur
 */
public class NatureImpotDTO {
    
    private UUID id;
    private String code;
    private String nom;
    private String description;
    private boolean actif;
    
    public NatureImpotDTO() {
    }
    
    public NatureImpotDTO(UUID id, String code, String nom, String description, boolean actif) {
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.description = description;
        this.actif = actif;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
}
