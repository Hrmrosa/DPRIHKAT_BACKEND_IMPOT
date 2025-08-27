package com.DPRIHKAT.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DivisionResponseDTO {
    private UUID id;
    private String nom;
    private String code;
    private List<BureauSimpleDTO> bureaux = new ArrayList<>();

    public DivisionResponseDTO() {}

    public DivisionResponseDTO(UUID id, String nom, String code, List<BureauSimpleDTO> bureaux) {
        this.id = id;
        this.nom = nom;
        this.code = code;
        this.bureaux = bureaux;
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

    public List<BureauSimpleDTO> getBureaux() {
        return bureaux;
    }

    public void setBureaux(List<BureauSimpleDTO> bureaux) {
        this.bureaux = bureaux;
    }
}
