package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.Sexe;
import java.util.UUID;

public class AgentResponseDTO {
    private UUID id;
    private String nom;
    private Sexe sexe;
    private String matricule;
    private BureauSimpleDTO bureau;

    public AgentResponseDTO() {}

    public AgentResponseDTO(UUID id, String nom, Sexe sexe, String matricule, BureauSimpleDTO bureau) {
        this.id = id;
        this.nom = nom;
        this.sexe = sexe;
        this.matricule = matricule;
        this.bureau = bureau;
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

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public BureauSimpleDTO getBureau() {
        return bureau;
    }

    public void setBureau(BureauSimpleDTO bureau) {
        this.bureau = bureau;
    }
}
