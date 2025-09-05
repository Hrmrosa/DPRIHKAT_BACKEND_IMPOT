package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeImpot;

import java.util.Date;
import java.util.UUID;

/**
 * DTO pour représenter une taxation
 * @author amateur
 */
public class TaxationDTO {
    
    private UUID id;
    private Date dateTaxation;
    private Double montant;
    private Integer exercice;
    private StatutTaxation statut;
    private TypeImpot typeImpot;
    private boolean exoneration;
    private String motifExoneration;
    private Date dateEcheance;
    private boolean actif;
    
    private UUID proprieteId;
    private String proprieteAdresse;
    private UUID proprieteImpotId;
    private String natureImpotCode;
    private String natureImpotNom;
    private UUID agentTaxateurId;
    private String agentTaxateurNom;
    private UUID paiementId;
    private UUID apurementId;
    
    public TaxationDTO() {
    }
    
    public TaxationDTO(UUID id, Date dateTaxation, Double montant, Integer exercice, StatutTaxation statut, 
                      TypeImpot typeImpot, boolean exoneration, String motifExoneration, Date dateEcheance, 
                      boolean actif, UUID proprieteId, String proprieteAdresse, UUID proprieteImpotId, 
                      String natureImpotCode, String natureImpotNom, UUID agentTaxateurId, 
                      String agentTaxateurNom, UUID paiementId, UUID apurementId) {
        this.id = id;
        this.dateTaxation = dateTaxation;
        this.montant = montant;
        this.exercice = exercice;
        this.statut = statut;
        this.typeImpot = typeImpot;
        this.exoneration = exoneration;
        this.motifExoneration = motifExoneration;
        this.dateEcheance = dateEcheance;
        this.actif = actif;
        this.proprieteId = proprieteId;
        this.proprieteAdresse = proprieteAdresse;
        this.proprieteImpotId = proprieteImpotId;
        this.natureImpotCode = natureImpotCode;
        this.natureImpotNom = natureImpotNom;
        this.agentTaxateurId = agentTaxateurId;
        this.agentTaxateurNom = agentTaxateurNom;
        this.paiementId = paiementId;
        this.apurementId = apurementId;
    }

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

    public Integer getExercice() {
        return exercice;
    }

    public void setExercice(Integer exercice) {
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

    public String getMotifExoneration() {
        return motifExoneration;
    }

    public void setMotifExoneration(String motifExoneration) {
        this.motifExoneration = motifExoneration;
    }

    public Date getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public UUID getProprieteId() {
        return proprieteId;
    }

    public void setProprieteId(UUID proprieteId) {
        this.proprieteId = proprieteId;
    }

    public String getProprieteAdresse() {
        return proprieteAdresse;
    }

    public void setProprieteAdresse(String proprieteAdresse) {
        this.proprieteAdresse = proprieteAdresse;
    }

    public UUID getProprieteImpotId() {
        return proprieteImpotId;
    }

    public void setProprieteImpotId(UUID proprieteImpotId) {
        this.proprieteImpotId = proprieteImpotId;
    }

    public String getNatureImpotCode() {
        return natureImpotCode;
    }

    public void setNatureImpotCode(String natureImpotCode) {
        this.natureImpotCode = natureImpotCode;
    }

    public String getNatureImpotNom() {
        return natureImpotNom;
    }

    public void setNatureImpotNom(String natureImpotNom) {
        this.natureImpotNom = natureImpotNom;
    }

    public UUID getAgentTaxateurId() {
        return agentTaxateurId;
    }

    public void setAgentTaxateurId(UUID agentTaxateurId) {
        this.agentTaxateurId = agentTaxateurId;
    }

    public String getAgentTaxateurNom() {
        return agentTaxateurNom;
    }

    public void setAgentTaxateurNom(String agentTaxateurNom) {
        this.agentTaxateurNom = agentTaxateurNom;
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
