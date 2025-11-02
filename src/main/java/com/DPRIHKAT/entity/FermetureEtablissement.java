package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeDocumentRecouvrement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Date;

/**
 * Entité représentant une Fermeture d'Établissement
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FermetureEtablissement extends DocumentRecouvrement {

    @ManyToOne
    @JoinColumn(name = "agent_opj_id")
    private Agent agentOPJ;
    
    private Date dateFermeture;
    
    private String adresseEtablissement;
    
    private String motifFermeture;
    
    private Date dateReouverture;
    
    private Double montantAmende;

    public FermetureEtablissement() {
        super();
        this.setType(TypeDocumentRecouvrement.FERMETURE_ETABLISSEMENT);
    }

    public FermetureEtablissement(StatutDocumentRecouvrement statut, Date dateGeneration, 
                                 String reference, Double montantPrincipal, Double montantPenalites,
                                 DossierRecouvrement dossierRecouvrement, Contribuable contribuable, 
                                 Agent agentGenerateur, Agent agentOPJ, String adresseEtablissement, 
                                 String motifFermeture) {
        super(TypeDocumentRecouvrement.FERMETURE_ETABLISSEMENT, statut, dateGeneration, reference, 
              montantPrincipal, montantPenalites, dossierRecouvrement, contribuable, agentGenerateur);
        
        this.agentOPJ = agentOPJ;
        this.adresseEtablissement = adresseEtablissement;
        this.motifFermeture = motifFermeture;
        
        // Définir le montant de l'amende en fonction du type de contribuable
        if (contribuable != null) {
            switch (contribuable.getType()) {
                case PERSONNE_MORALE:
                    this.montantAmende = 1000000.0; // 1.000.000 FC
                    break;
                case PERSONNE_PHYSIQUE:
                    if (contribuable.isCommercant()) {
                        this.montantAmende = 100000.0; // 100.000 FC
                    } else {
                        this.montantAmende = 50000.0; // 50.000 FC
                    }
                    break;
                default:
                    this.montantAmende = 100000.0; // Valeur par défaut
            }
        }
    }

    // Getters et Setters
    public Agent getAgentOPJ() {
        return agentOPJ;
    }

    public void setAgentOPJ(Agent agentOPJ) {
        this.agentOPJ = agentOPJ;
    }

    public Date getDateFermeture() {
        return dateFermeture;
    }

    public void setDateFermeture(Date dateFermeture) {
        this.dateFermeture = dateFermeture;
    }

    public String getAdresseEtablissement() {
        return adresseEtablissement;
    }

    public void setAdresseEtablissement(String adresseEtablissement) {
        this.adresseEtablissement = adresseEtablissement;
    }

    public String getMotifFermeture() {
        return motifFermeture;
    }

    public void setMotifFermeture(String motifFermeture) {
        this.motifFermeture = motifFermeture;
    }

    public Date getDateReouverture() {
        return dateReouverture;
    }

    public void setDateReouverture(Date dateReouverture) {
        this.dateReouverture = dateReouverture;
    }

    public Double getMontantAmende() {
        return montantAmende;
    }

    public void setMontantAmende(Double montantAmende) {
        this.montantAmende = montantAmende;
    }
}
