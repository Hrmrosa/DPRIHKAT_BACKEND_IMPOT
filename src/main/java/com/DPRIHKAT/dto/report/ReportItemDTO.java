package com.DPRIHKAT.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour un élément individuel dans un rapport
 */
public class ReportItemDTO {
    private String reference;
    private LocalDate date;
    private String type;
    private String contribuableNom;
    private String contribuableIdentifiant;
    private String description;
    private BigDecimal montant;
    private String statut;
    private String agentNom;
    private String bureau;
    
    // Getters et Setters
    public String getReference() {
        return reference;
    }
    
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getContribuableNom() {
        return contribuableNom;
    }

    public void setContribuableNom(String contribuableNom) {
        this.contribuableNom = contribuableNom;
    }

    public String getContribuableIdentifiant() {
        return contribuableIdentifiant;
    }

    public void setContribuableIdentifiant(String contribuableIdentifiant) {
        this.contribuableIdentifiant = contribuableIdentifiant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getAgentNom() {
        return agentNom;
    }

    public void setAgentNom(String agentNom) {
        this.agentNom = agentNom;
    }

    public String getBureau() {
        return bureau;
    }

    public void setBureau(String bureau) {
        this.bureau = bureau;
    }
}
