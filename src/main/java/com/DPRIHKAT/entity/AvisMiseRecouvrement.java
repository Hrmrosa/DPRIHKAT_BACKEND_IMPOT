package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeRedressement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Date;

/**
 * Entité représentant un Avis de Mise en Recouvrement (AMR)
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AvisMiseRecouvrement extends DocumentRecouvrement {

    @Enumerated(EnumType.STRING)
    private TypeRedressement typeRedressement;
    
    private String baseImposable;
    
    @ManyToOne
    @JoinColumn(name = "declaration_id")
    private Declaration declaration;

    public AvisMiseRecouvrement() {
        super();
        this.setType(TypeDocumentRecouvrement.AMR);
    }

    public AvisMiseRecouvrement(StatutDocumentRecouvrement statut, Date dateGeneration, 
                               String reference, Double montantPrincipal, Double montantPenalites,
                               DossierRecouvrement dossierRecouvrement, Contribuable contribuable, 
                               Agent agentGenerateur, TypeRedressement typeRedressement, 
                               String baseImposable, Declaration declaration) {
        super(TypeDocumentRecouvrement.AMR, statut, dateGeneration, reference, 
              montantPrincipal, montantPenalites, dossierRecouvrement, contribuable, agentGenerateur);
        
        this.typeRedressement = typeRedressement;
        this.baseImposable = baseImposable;
        this.declaration = declaration;
        
        // Définir la date d'échéance à 15 jours après la génération
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateGeneration);
        calendar.add(Calendar.DAY_OF_MONTH, 15);
        this.setDateEcheance(calendar.getTime());
    }

    // Getters et Setters
    public TypeRedressement getTypeRedressement() {
        return typeRedressement;
    }

    public void setTypeRedressement(TypeRedressement typeRedressement) {
        this.typeRedressement = typeRedressement;
    }

    public String getBaseImposable() {
        return baseImposable;
    }

    public void setBaseImposable(String baseImposable) {
        this.baseImposable = baseImposable;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }
}
