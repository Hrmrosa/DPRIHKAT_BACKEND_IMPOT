package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeDocumentRecouvrement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Date;

/**
 * Entité représentant une Saisie Mobilière
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SaisieMobiliere extends Saisie {

    private String typeBiensMobiliers;
    
    private String lieuStockage;
    
    private String gardien;

    public SaisieMobiliere() {
        super();
        this.setType(TypeDocumentRecouvrement.SAISIE_MOBILIERE);
    }

    public SaisieMobiliere(StatutDocumentRecouvrement statut, Date dateGeneration, 
                          String reference, Double montantPrincipal, Double montantPenalites,
                          DossierRecouvrement dossierRecouvrement, Contribuable contribuable, 
                          Agent agentGenerateur, Agent huissier, String descriptionBiensSaisis, 
                          Double valeurEstimeeBiens, String typeBiensMobiliers, 
                          String lieuStockage, String gardien) {
        super(TypeDocumentRecouvrement.SAISIE_MOBILIERE, statut, dateGeneration, reference, 
              montantPrincipal, montantPenalites, dossierRecouvrement, contribuable, 
              agentGenerateur, huissier, descriptionBiensSaisis, valeurEstimeeBiens);
        
        this.typeBiensMobiliers = typeBiensMobiliers;
        this.lieuStockage = lieuStockage;
        this.gardien = gardien;
    }

    // Getters et Setters
    public String getTypeBiensMobiliers() {
        return typeBiensMobiliers;
    }

    public void setTypeBiensMobiliers(String typeBiensMobiliers) {
        this.typeBiensMobiliers = typeBiensMobiliers;
    }

    public String getLieuStockage() {
        return lieuStockage;
    }

    public void setLieuStockage(String lieuStockage) {
        this.lieuStockage = lieuStockage;
    }

    public String getGardien() {
        return gardien;
    }

    public void setGardien(String gardien) {
        this.gardien = gardien;
    }
}
