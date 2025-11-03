package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.StatutDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeDocumentRecouvrement;
import java.util.Date;
import java.util.UUID;

/**
 * DTO pour les données de recouvrement dans un rapport
 * 
 * @author amateur
 */
public class RapportRecouvrementDTO {
    
    private UUID id;
    private TypeDocumentRecouvrement type;
    private StatutDocumentRecouvrement statut;
    private Date dateGeneration;
    private Date dateNotification;
    private String reference;
    private Double montantPrincipal;
    private Double montantPenalites;
    private Double montantTotal;
    private String nomContribuable;
    private String numeroContribuable;
    private String nomAgentGenerateur;
    private String matriculeAgentGenerateur;
    private String nomAgentNotificateur;
    private String matriculeAgentNotificateur;
    
    public RapportRecouvrementDTO() {
    }
    
    // Getters et Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public TypeDocumentRecouvrement getType() {
        return type;
    }
    
    public void setType(TypeDocumentRecouvrement type) {
        this.type = type;
    }
    
    public StatutDocumentRecouvrement getStatut() {
        return statut;
    }
    
    public void setStatut(StatutDocumentRecouvrement statut) {
        this.statut = statut;
    }
    
    public Date getDateGeneration() {
        return dateGeneration;
    }
    
    public void setDateGeneration(Date dateGeneration) {
        this.dateGeneration = dateGeneration;
    }
    
    public Date getDateNotification() {
        return dateNotification;
    }
    
    public void setDateNotification(Date dateNotification) {
        this.dateNotification = dateNotification;
    }
    
    public String getReference() {
        return reference;
    }
    
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    public Double getMontantPrincipal() {
        return montantPrincipal;
    }
    
    public void setMontantPrincipal(Double montantPrincipal) {
        this.montantPrincipal = montantPrincipal;
    }
    
    public Double getMontantPenalites() {
        return montantPenalites;
    }
    
    public void setMontantPenalites(Double montantPenalites) {
        this.montantPenalites = montantPenalites;
    }
    
    public Double getMontantTotal() {
        return montantTotal;
    }
    
    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
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
    
    public String getNomAgentGenerateur() {
        return nomAgentGenerateur;
    }
    
    public void setNomAgentGenerateur(String nomAgentGenerateur) {
        this.nomAgentGenerateur = nomAgentGenerateur;
    }
    
    public String getMatriculeAgentGenerateur() {
        return matriculeAgentGenerateur;
    }
    
    public void setMatriculeAgentGenerateur(String matriculeAgentGenerateur) {
        this.matriculeAgentGenerateur = matriculeAgentGenerateur;
    }
    
    public String getNomAgentNotificateur() {
        return nomAgentNotificateur;
    }
    
    public void setNomAgentNotificateur(String nomAgentNotificateur) {
        this.nomAgentNotificateur = nomAgentNotificateur;
    }
    
    public String getMatriculeAgentNotificateur() {
        return matriculeAgentNotificateur;
    }
    
    public void setMatriculeAgentNotificateur(String matriculeAgentNotificateur) {
        this.matriculeAgentNotificateur = matriculeAgentNotificateur;
    }
}
