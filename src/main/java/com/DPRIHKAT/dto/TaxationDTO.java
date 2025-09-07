package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeImpot;

import java.util.Date;
import java.util.UUID;

/**
 * DTO pour l'entité Taxation
 * Version adaptée à la nouvelle architecture où la taxation est liée à une déclaration
 */
public class TaxationDTO {

    private UUID id;
    private Date dateTaxation;
    private Double montant;
    private String exercice;
    private StatutTaxation statut;
    private TypeImpot typeImpot;
    private boolean exoneration;
    private boolean actif;
    
    // Informations sur la déclaration
    private UUID declarationId;
    private Date dateDeclaration;
    private UUID proprieteId;
    private String adressePropriete;
    private UUID contribuableId;
    private String nomContribuable;
    
    // Informations sur la nature d'impôt
    private UUID natureImpotId;
    private String codeNatureImpot;
    private String nomNatureImpot;
    
    // Informations sur les agents
    private UUID agentTaxateurId;
    private String nomAgentTaxateur;
    private UUID agentValidateurId;
    private String nomAgentValidateur;
    
    // Informations sur le paiement et l'apurement
    private UUID paiementId;
    private UUID apurementId;

    public TaxationDTO() {
    }

    public TaxationDTO(UUID id, Date dateTaxation, Double montant, String exercice, StatutTaxation statut,
                       TypeImpot typeImpot, boolean exoneration, boolean actif, UUID declarationId,
                       Date dateDeclaration, UUID proprieteId, String adressePropriete, UUID contribuableId,
                       String nomContribuable, UUID natureImpotId, String codeNatureImpot, String nomNatureImpot,
                       UUID agentTaxateurId, String nomAgentTaxateur, UUID agentValidateurId,
                       String nomAgentValidateur, UUID paiementId, UUID apurementId) {
        this.id = id;
        this.dateTaxation = dateTaxation;
        this.montant = montant;
        this.exercice = exercice;
        this.statut = statut;
        this.typeImpot = typeImpot;
        this.exoneration = exoneration;
        this.actif = actif;
        this.declarationId = declarationId;
        this.dateDeclaration = dateDeclaration;
        this.proprieteId = proprieteId;
        this.adressePropriete = adressePropriete;
        this.contribuableId = contribuableId;
        this.nomContribuable = nomContribuable;
        this.natureImpotId = natureImpotId;
        this.codeNatureImpot = codeNatureImpot;
        this.nomNatureImpot = nomNatureImpot;
        this.agentTaxateurId = agentTaxateurId;
        this.nomAgentTaxateur = nomAgentTaxateur;
        this.agentValidateurId = agentValidateurId;
        this.nomAgentValidateur = nomAgentValidateur;
        this.paiementId = paiementId;
        this.apurementId = apurementId;
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDateTaxation() {
        return dateTaxation;
    }

    public void setDateTaxation(Date dateTaxation) {
        this.dateTaxation = dateTaxation;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getExercice() {
        return exercice;
    }

    public void setExercice(String exercice) {
        this.exercice = exercice;
    }

    public StatutTaxation getStatut() {
        return statut;
    }

    public void setStatut(StatutTaxation statut) {
        this.statut = statut;
    }

    public TypeImpot getTypeImpot() {
        return typeImpot;
    }

    public void setTypeImpot(TypeImpot typeImpot) {
        this.typeImpot = typeImpot;
    }

    public boolean isExoneration() {
        return exoneration;
    }

    public void setExoneration(boolean exoneration) {
        this.exoneration = exoneration;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public UUID getDeclarationId() {
        return declarationId;
    }

    public void setDeclarationId(UUID declarationId) {
        this.declarationId = declarationId;
    }

    public Date getDateDeclaration() {
        return dateDeclaration;
    }

    public void setDateDeclaration(Date dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public UUID getProprieteId() {
        return proprieteId;
    }

    public void setProprieteId(UUID proprieteId) {
        this.proprieteId = proprieteId;
    }

    public String getAdressePropriete() {
        return adressePropriete;
    }

    public void setAdressePropriete(String adressePropriete) {
        this.adressePropriete = adressePropriete;
    }

    public UUID getContribuableId() {
        return contribuableId;
    }

    public void setContribuableId(UUID contribuableId) {
        this.contribuableId = contribuableId;
    }

    public String getNomContribuable() {
        return nomContribuable;
    }

    public void setNomContribuable(String nomContribuable) {
        this.nomContribuable = nomContribuable;
    }

    public UUID getNatureImpotId() {
        return natureImpotId;
    }

    public void setNatureImpotId(UUID natureImpotId) {
        this.natureImpotId = natureImpotId;
    }

    public String getCodeNatureImpot() {
        return codeNatureImpot;
    }

    public void setCodeNatureImpot(String codeNatureImpot) {
        this.codeNatureImpot = codeNatureImpot;
    }

    public String getNomNatureImpot() {
        return nomNatureImpot;
    }

    public void setNomNatureImpot(String nomNatureImpot) {
        this.nomNatureImpot = nomNatureImpot;
    }

    public UUID getAgentTaxateurId() {
        return agentTaxateurId;
    }

    public void setAgentTaxateurId(UUID agentTaxateurId) {
        this.agentTaxateurId = agentTaxateurId;
    }

    public String getNomAgentTaxateur() {
        return nomAgentTaxateur;
    }

    public void setNomAgentTaxateur(String nomAgentTaxateur) {
        this.nomAgentTaxateur = nomAgentTaxateur;
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

    public UUID getPaiementId() {
        return paiementId;
    }

    public void setPaiementId(UUID paiementId) {
        this.paiementId = paiementId;
    }

    public UUID getApurementId() {
        return apurementId;
    }

    public void setApurementId(UUID apurementId) {
        this.apurementId = apurementId;
    }
}
