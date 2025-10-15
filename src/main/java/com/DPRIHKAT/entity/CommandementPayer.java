package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeDocumentRecouvrement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Date;

/**
 * Entité représentant un Commandement de Payer
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CommandementPayer extends DocumentRecouvrement {

    @ManyToOne
    @JoinColumn(name = "huissier_id")
    private Agent huissier;
    
    private Date dateSignification;
    
    private String lieuSignification;
    
    private Double fraisCommandement;

    public CommandementPayer() {
        super();
        this.setType(TypeDocumentRecouvrement.COMMANDEMENT);
    }

    public CommandementPayer(StatutDocumentRecouvrement statut, Date dateGeneration, 
                           String reference, Double montantPrincipal, Double montantPenalites,
                           DossierRecouvrement dossierRecouvrement, Contribuable contribuable, 
                           Agent agentGenerateur, Agent huissier) {
        super(TypeDocumentRecouvrement.COMMANDEMENT, statut, dateGeneration, reference, 
              montantPrincipal, montantPenalites, dossierRecouvrement, contribuable, agentGenerateur);
        
        this.huissier = huissier;
        
        // Calculer les frais de commandement (3% du montant total)
        this.fraisCommandement = (montantPrincipal + (montantPenalites != null ? montantPenalites : 0)) * 0.03;
        
        // Définir la date d'échéance à 8 jours après la génération
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateGeneration);
        calendar.add(Calendar.DAY_OF_MONTH, 8);
        this.setDateEcheance(calendar.getTime());
    }

    // Getters et Setters
    public Agent getHuissier() {
        return huissier;
    }

    public void setHuissier(Agent huissier) {
        this.huissier = huissier;
    }

    public Date getDateSignification() {
        return dateSignification;
    }

    public void setDateSignification(Date dateSignification) {
        this.dateSignification = dateSignification;
    }

    public String getLieuSignification() {
        return lieuSignification;
    }

    public void setLieuSignification(String lieuSignification) {
        this.lieuSignification = lieuSignification;
    }

    public Double getFraisCommandement() {
        return fraisCommandement;
    }

    public void setFraisCommandement(Double fraisCommandement) {
        this.fraisCommandement = fraisCommandement;
    }
    
    /**
     * Calcule le montant total incluant les frais de commandement
     * @return Montant total avec frais
     */
    public Double getMontantTotalAvecFrais() {
        return this.getMontantTotal() + (this.fraisCommandement != null ? this.fraisCommandement : 0);
    }
}
