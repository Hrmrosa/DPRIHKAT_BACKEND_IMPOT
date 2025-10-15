package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeDocumentRecouvrement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Date;

/**
 * Entité représentant un Avis à Tiers Détenteur (ATD)
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AvisTiersDetenteur extends DocumentRecouvrement {

    private String nomTiersDetenteur;
    
    private String adresseTiersDetenteur;
    
    private String qualiteTiersDetenteur;
    
    private Date dateReponse;
    
    private String contenuReponse;
    
    private Double montantDetenu;
    
    private Double montantVerse;

    public AvisTiersDetenteur() {
        super();
        this.setType(TypeDocumentRecouvrement.ATD);
    }

    public AvisTiersDetenteur(StatutDocumentRecouvrement statut, Date dateGeneration, 
                             String reference, Double montantPrincipal, Double montantPenalites,
                             DossierRecouvrement dossierRecouvrement, Contribuable contribuable, 
                             Agent agentGenerateur, String nomTiersDetenteur, 
                             String adresseTiersDetenteur, String qualiteTiersDetenteur) {
        super(TypeDocumentRecouvrement.ATD, statut, dateGeneration, reference, 
              montantPrincipal, montantPenalites, dossierRecouvrement, contribuable, agentGenerateur);
        
        this.nomTiersDetenteur = nomTiersDetenteur;
        this.adresseTiersDetenteur = adresseTiersDetenteur;
        this.qualiteTiersDetenteur = qualiteTiersDetenteur;
        
        // Définir la date d'échéance à 8 jours après la génération
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateGeneration);
        calendar.add(Calendar.DAY_OF_MONTH, 8);
        this.setDateEcheance(calendar.getTime());
    }

    // Getters et Setters
    public String getNomTiersDetenteur() {
        return nomTiersDetenteur;
    }

    public void setNomTiersDetenteur(String nomTiersDetenteur) {
        this.nomTiersDetenteur = nomTiersDetenteur;
    }

    public String getAdresseTiersDetenteur() {
        return adresseTiersDetenteur;
    }

    public void setAdresseTiersDetenteur(String adresseTiersDetenteur) {
        this.adresseTiersDetenteur = adresseTiersDetenteur;
    }

    public String getQualiteTiersDetenteur() {
        return qualiteTiersDetenteur;
    }

    public void setQualiteTiersDetenteur(String qualiteTiersDetenteur) {
        this.qualiteTiersDetenteur = qualiteTiersDetenteur;
    }

    public Date getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(Date dateReponse) {
        this.dateReponse = dateReponse;
    }

    public String getContenuReponse() {
        return contenuReponse;
    }

    public void setContenuReponse(String contenuReponse) {
        this.contenuReponse = contenuReponse;
    }

    public Double getMontantDetenu() {
        return montantDetenu;
    }

    public void setMontantDetenu(Double montantDetenu) {
        this.montantDetenu = montantDetenu;
    }

    public Double getMontantVerse() {
        return montantVerse;
    }

    public void setMontantVerse(Double montantVerse) {
        this.montantVerse = montantVerse;
    }
}
