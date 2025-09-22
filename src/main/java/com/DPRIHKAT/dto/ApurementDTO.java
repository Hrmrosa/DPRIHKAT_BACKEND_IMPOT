package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.StatutApurement;
import com.DPRIHKAT.entity.enums.TypeApurement;

import java.util.Date;
import java.util.UUID;

/**
 * DTO pour les informations d'apurement
 */
public class ApurementDTO {
    private UUID id;
    private Date dateDemande;
    private Date dateValidation;
    private TypeApurement type;
    private Double montantApure;
    private String motif;
    private String motifRejet;
    private StatutApurement statut;
    private boolean provisoire;
    private boolean declarationPayee;
    private UUID agentId;
    private String nomAgent;
    private UUID agentValidateurId;
    private String nomAgentValidateur;

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }

    public Date getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }

    public TypeApurement getType() {
        return type;
    }

    public void setType(TypeApurement type) {
        this.type = type;
    }

    public Double getMontantApure() {
        return montantApure;
    }

    public void setMontantApure(Double montantApure) {
        this.montantApure = montantApure;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getMotifRejet() {
        return motifRejet;
    }

    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }

    public StatutApurement getStatut() {
        return statut;
    }

    public void setStatut(StatutApurement statut) {
        this.statut = statut;
    }

    public boolean isProvisoire() {
        return provisoire;
    }

    public void setProvisoire(boolean provisoire) {
        this.provisoire = provisoire;
    }

    public boolean isDeclarationPayee() {
        return declarationPayee;
    }

    public void setDeclarationPayee(boolean declarationPayee) {
        this.declarationPayee = declarationPayee;
    }

    public UUID getAgentId() {
        return agentId;
    }

    public void setAgentId(UUID agentId) {
        this.agentId = agentId;
    }

    public String getNomAgent() {
        return nomAgent;
    }

    public void setNomAgent(String nomAgent) {
        this.nomAgent = nomAgent;
    }

    public UUID getAgentValidateurId() {
        return agentValidateurId;
    }

    public void setAgentValidateurId(UUID agentValidateurId) {
        this.agentValidateurId = agentValidateurId;
    }

    public String getNomAgentValidateur() {
        return nomAgentValidateur;
    }

    public void setNomAgentValidateur(String nomAgentValidateur) {
        this.nomAgentValidateur = nomAgentValidateur;
    }
}
