package com.DPRIHKAT.dto;

import java.util.UUID;

public class AgentSimpleDTO {
    private UUID id;
    private String nom;

    public AgentSimpleDTO() {}

    public AgentSimpleDTO(UUID id, String nom) {
        this.id = id;
        this.nom = nom;
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
}
