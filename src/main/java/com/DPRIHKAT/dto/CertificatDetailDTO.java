package com.DPRIHKAT.dto;

import java.util.Date;
import java.util.UUID;

public class CertificatDetailDTO {
    private UUID id;
    private String numero;
    private Date dateEmission;
    private Date dateExpiration;
    private boolean valide;
    private String contenu;
    private UUID agentEmetteurId;
    private String agentEmetteurNom;
    private String agentEmetteurMatricule;

    public CertificatDetailDTO() {
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public Date getDateEmission() {
        return dateEmission;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public boolean isValide() {
        return valide;
    }

    public String getContenu() {
        return contenu;
    }

    public UUID getAgentEmetteurId() {
        return agentEmetteurId;
    }

    public String getAgentEmetteurNom() {
        return agentEmetteurNom;
    }

    public String getAgentEmetteurMatricule() {
        return agentEmetteurMatricule;
    }

    // Setters
    public void setId(UUID id) {
        this.id = id;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setDateEmission(Date dateEmission) {
        this.dateEmission = dateEmission;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public void setAgentEmetteurId(UUID agentEmetteurId) {
        this.agentEmetteurId = agentEmetteurId;
    }

    public void setAgentEmetteurNom(String agentEmetteurNom) {
        this.agentEmetteurNom = agentEmetteurNom;
    }

    public void setAgentEmetteurMatricule(String agentEmetteurMatricule) {
        this.agentEmetteurMatricule = agentEmetteurMatricule;
    }
}
