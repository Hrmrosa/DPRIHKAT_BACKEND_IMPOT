package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.StatutRelance;
import com.DPRIHKAT.entity.enums.TypeRelance;
import java.util.Date;
import java.util.UUID;

/**
 * DTO pour les données de relance dans un rapport
 * 
 * @author amateur
 */
public class RapportRelanceDTO {
    
    private UUID id;
    private Date dateEnvoi;
    private TypeRelance type;
    private StatutRelance statut;
    private String contenu;
    private String nomContribuable;
    private String numeroContribuable;
    private UUID dossierId;
    private String numeroDossier;
    
    public RapportRelanceDTO() {
    }
    
    // Getters et Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Date getDateEnvoi() {
        return dateEnvoi;
    }
    
    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
    
    public TypeRelance getType() {
        return type;
    }
    
    public void setType(TypeRelance type) {
        this.type = type;
    }
    
    public StatutRelance getStatut() {
        return statut;
    }
    
    public void setStatut(StatutRelance statut) {
        this.statut = statut;
    }
    
    public String getContenu() {
        return contenu;
    }
    
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    
    public String getNomContribuable() {
        return nomContribuable;
    }
    
    public void setNomContribuable(String nomContribuable) {
        this.nomContribuable = nomContribuable;
    }
    
    public String getNumeroContribuable() {
        return numeroContribuable;
    }
    
    public void setNumeroContribuable(String numeroContribuable) {
        this.numeroContribuable = numeroContribuable;
    }
    
    public UUID getDossierId() {
        return dossierId;
    }
    
    public void setDossierId(UUID dossierId) {
        this.dossierId = dossierId;
    }
    
    public String getNumeroDossier() {
        return numeroDossier;
    }
    
    public void setNumeroDossier(String numeroDossier) {
        this.numeroDossier = numeroDossier;
    }
}
