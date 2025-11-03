package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.PeriodeRapport;
import com.DPRIHKAT.entity.enums.TypeRapport;
import java.util.Date;
import java.util.UUID;

/**
 * DTO pour la requête de génération de rapport
 * 
 * @author amateur
 */
public class RapportRequestDTO {
    
    private TypeRapport typeRapport;
    private PeriodeRapport periode;
    private Date dateDebut;
    private Date dateFin;
    private UUID agentId; // Optionnel : pour filtrer par agent
    private UUID bureauId; // Optionnel : pour filtrer par bureau
    private UUID divisionId; // Optionnel : pour filtrer par division
    
    public RapportRequestDTO() {
    }
    
    public RapportRequestDTO(TypeRapport typeRapport, PeriodeRapport periode, Date dateDebut, Date dateFin) {
        this.typeRapport = typeRapport;
        this.periode = periode;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }
    
    // Getters et Setters
    public TypeRapport getTypeRapport() {
        return typeRapport;
    }
    
    public void setTypeRapport(TypeRapport typeRapport) {
        this.typeRapport = typeRapport;
    }
    
    public PeriodeRapport getPeriode() {
        return periode;
    }
    
    public void setPeriode(PeriodeRapport periode) {
        this.periode = periode;
    }
    
    public Date getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public Date getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
    
    public UUID getAgentId() {
        return agentId;
    }
    
    public void setAgentId(UUID agentId) {
        this.agentId = agentId;
    }
    
    public UUID getBureauId() {
        return bureauId;
    }
    
    public void setBureauId(UUID bureauId) {
        this.bureauId = bureauId;
    }
    
    public UUID getDivisionId() {
        return divisionId;
    }
    
    public void setDivisionId(UUID divisionId) {
        this.divisionId = divisionId;
    }
}
