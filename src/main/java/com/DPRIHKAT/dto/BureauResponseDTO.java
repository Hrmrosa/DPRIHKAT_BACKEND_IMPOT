package com.DPRIHKAT.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BureauResponseDTO {
    private UUID id;
    private String nom;
    private String code;
    private DivisionSimpleDTO division;
    private List<AgentSimpleDTO> agents = new ArrayList<>();

    public BureauResponseDTO() {}

    public BureauResponseDTO(UUID id, String nom, String code, DivisionSimpleDTO division, List<AgentSimpleDTO> agents) {
        this.id = id;
        this.nom = nom;
        this.code = code;
        this.division = division;
        this.agents = agents;
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

    public DivisionSimpleDTO getDivision() {
        return division;
    }

    public void setDivision(DivisionSimpleDTO division) {
        this.division = division;
    }

    public List<AgentSimpleDTO> getAgents() {
        return agents;
    }

    public void setAgents(List<AgentSimpleDTO> agents) {
        this.agents = agents;
    }
}
