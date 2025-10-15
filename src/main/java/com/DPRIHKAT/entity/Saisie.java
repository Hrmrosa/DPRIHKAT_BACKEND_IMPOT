package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeDocumentRecouvrement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Date;

/**
 * Entité représentant une Saisie (mobilière ou immobilière)
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Saisie extends DocumentRecouvrement {

    @ManyToOne
    @JoinColumn(name = "huissier_id")
    private Agent huissier;
    
    private Date dateSaisie;
    
    private String lieuSaisie;
    
    private String descriptionBiensSaisis;
    
    private Double valeurEstimeeBiens;
    
    private Date dateVente;
    
    private Double montantVente;
    
    private Double fraisSaisie;
    
    private Double fraisVente;

    public Saisie() {
        super();
    }

    public Saisie(TypeDocumentRecouvrement type, StatutDocumentRecouvrement statut, 
                 Date dateGeneration, String reference, Double montantPrincipal, 
                 Double montantPenalites, DossierRecouvrement dossierRecouvrement, 
                 Contribuable contribuable, Agent agentGenerateur, Agent huissier, 
                 String descriptionBiensSaisis, Double valeurEstimeeBiens) {
        super(type, statut, dateGeneration, reference, montantPrincipal, 
              montantPenalites, dossierRecouvrement, contribuable, agentGenerateur);
        
        this.huissier = huissier;
        this.descriptionBiensSaisis = descriptionBiensSaisis;
        this.valeurEstimeeBiens = valeurEstimeeBiens;
        
        // Calculer les frais de saisie (5% du montant total)
        this.fraisSaisie = (montantPrincipal + (montantPenalites != null ? montantPenalites : 0)) * 0.05;
    }

    // Getters et Setters
    public Agent getHuissier() {
        return huissier;
    }

    public void setHuissier(Agent huissier) {
        this.huissier = huissier;
    }

    public Date getDateSaisie() {
        return dateSaisie;
    }

    public void setDateSaisie(Date dateSaisie) {
        this.dateSaisie = dateSaisie;
    }

    public String getLieuSaisie() {
        return lieuSaisie;
    }

    public void setLieuSaisie(String lieuSaisie) {
        this.lieuSaisie = lieuSaisie;
    }

    public String getDescriptionBiensSaisis() {
        return descriptionBiensSaisis;
    }

    public void setDescriptionBiensSaisis(String descriptionBiensSaisis) {
        this.descriptionBiensSaisis = descriptionBiensSaisis;
    }

    public Double getValeurEstimeeBiens() {
        return valeurEstimeeBiens;
    }

    public void setValeurEstimeeBiens(Double valeurEstimeeBiens) {
        this.valeurEstimeeBiens = valeurEstimeeBiens;
    }

    public Date getDateVente() {
        return dateVente;
    }

    public void setDateVente(Date dateVente) {
        this.dateVente = dateVente;
    }

    public Double getMontantVente() {
        return montantVente;
    }

    public void setMontantVente(Double montantVente) {
        this.montantVente = montantVente;
        
        // Calculer les frais de vente (3% du montant de la vente)
        if (montantVente != null) {
            this.fraisVente = montantVente * 0.03;
        }
    }

    public Double getFraisSaisie() {
        return fraisSaisie;
    }

    public void setFraisSaisie(Double fraisSaisie) {
        this.fraisSaisie = fraisSaisie;
    }

    public Double getFraisVente() {
        return fraisVente;
    }

    public void setFraisVente(Double fraisVente) {
        this.fraisVente = fraisVente;
    }
    
    /**
     * Calcule le montant total incluant les frais de saisie et de vente
     * @return Montant total avec frais
     */
    public Double getMontantTotalAvecFrais() {
        double total = this.getMontantTotal();
        if (this.fraisSaisie != null) {
            total += this.fraisSaisie;
        }
        if (this.fraisVente != null) {
            total += this.fraisVente;
        }
        return total;
    }
}
