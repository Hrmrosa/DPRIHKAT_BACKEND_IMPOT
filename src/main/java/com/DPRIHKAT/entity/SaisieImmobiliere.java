package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeDocumentRecouvrement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Date;

/**
 * Entité représentant une Saisie Immobilière
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SaisieImmobiliere extends Saisie {

    private String referencesCadastrales;
    
    private String adresseBien;
    
    private Double superficie;
    
    private String titresPropriete;
    
    private Date dateTranscriptionConservatoire;
    
    private String numeroTranscription;

    public SaisieImmobiliere() {
        super();
        this.setType(TypeDocumentRecouvrement.SAISIE_IMMOBILIERE);
    }

    public SaisieImmobiliere(StatutDocumentRecouvrement statut, Date dateGeneration, 
                            String reference, Double montantPrincipal, Double montantPenalites,
                            DossierRecouvrement dossierRecouvrement, Contribuable contribuable, 
                            Agent agentGenerateur, Agent huissier, String descriptionBiensSaisis, 
                            Double valeurEstimeeBiens, String referencesCadastrales, 
                            String adresseBien, Double superficie, String titresPropriete) {
        super(TypeDocumentRecouvrement.SAISIE_IMMOBILIERE, statut, dateGeneration, reference, 
              montantPrincipal, montantPenalites, dossierRecouvrement, contribuable, 
              agentGenerateur, huissier, descriptionBiensSaisis, valeurEstimeeBiens);
        
        this.referencesCadastrales = referencesCadastrales;
        this.adresseBien = adresseBien;
        this.superficie = superficie;
        this.titresPropriete = titresPropriete;
    }

    // Getters et Setters
    public String getReferencesCadastrales() {
        return referencesCadastrales;
    }

    public void setReferencesCadastrales(String referencesCadastrales) {
        this.referencesCadastrales = referencesCadastrales;
    }

    public String getAdresseBien() {
        return adresseBien;
    }

    public void setAdresseBien(String adresseBien) {
        this.adresseBien = adresseBien;
    }

    public Double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public String getTitresPropriete() {
        return titresPropriete;
    }

    public void setTitresPropriete(String titresPropriete) {
        this.titresPropriete = titresPropriete;
    }

    public Date getDateTranscriptionConservatoire() {
        return dateTranscriptionConservatoire;
    }

    public void setDateTranscriptionConservatoire(Date dateTranscriptionConservatoire) {
        this.dateTranscriptionConservatoire = dateTranscriptionConservatoire;
    }

    public String getNumeroTranscription() {
        return numeroTranscription;
    }

    public void setNumeroTranscription(String numeroTranscription) {
        this.numeroTranscription = numeroTranscription;
    }
}
