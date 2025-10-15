package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeDocumentRecouvrement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Date;

/**
 * Entité représentant une Mise En Demeure de payer (MED)
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MiseEnDemeure extends DocumentRecouvrement {

    @ManyToOne
    @JoinColumn(name = "declaration_id")
    private Declaration declaration;
    
    private boolean paiementInsuffisant;
    
    private Double montantPaye;

    public MiseEnDemeure() {
        super();
        this.setType(TypeDocumentRecouvrement.MED);
    }

    public MiseEnDemeure(StatutDocumentRecouvrement statut, Date dateGeneration, 
                        String reference, Double montantPrincipal, Double montantPenalites,
                        DossierRecouvrement dossierRecouvrement, Contribuable contribuable, 
                        Agent agentGenerateur, Declaration declaration, 
                        boolean paiementInsuffisant, Double montantPaye) {
        super(TypeDocumentRecouvrement.MED, statut, dateGeneration, reference, 
              montantPrincipal, montantPenalites, dossierRecouvrement, contribuable, agentGenerateur);
        
        this.declaration = declaration;
        this.paiementInsuffisant = paiementInsuffisant;
        this.montantPaye = montantPaye;
        
        // Définir la date d'échéance à 8 jours après la génération
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateGeneration);
        calendar.add(Calendar.DAY_OF_MONTH, 8);
        this.setDateEcheance(calendar.getTime());
    }

    // Getters et Setters
    public Declaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }

    public boolean isPaiementInsuffisant() {
        return paiementInsuffisant;
    }

    public void setPaiementInsuffisant(boolean paiementInsuffisant) {
        this.paiementInsuffisant = paiementInsuffisant;
    }

    public Double getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(Double montantPaye) {
        this.montantPaye = montantPaye;
    }
}
