package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeDocumentRecouvrement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Entité représentant une Contrainte Fiscale
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ContrainteFiscale extends DocumentRecouvrement {

    @ManyToOne
    @JoinColumn(name = "receveur_id")
    private Agent receveur;
    
    @OneToMany(mappedBy = "documentParent", cascade = CascadeType.ALL)
    private List<DocumentRecouvrement> documentsAssocies = new ArrayList<>();

    public ContrainteFiscale() {
        super();
        this.setType(TypeDocumentRecouvrement.CONTRAINTE);
    }

    public ContrainteFiscale(StatutDocumentRecouvrement statut, Date dateGeneration, 
                            String reference, Double montantPrincipal, Double montantPenalites,
                            DossierRecouvrement dossierRecouvrement, Contribuable contribuable, 
                            Agent agentGenerateur, Agent receveur) {
        super(TypeDocumentRecouvrement.CONTRAINTE, statut, dateGeneration, reference, 
              montantPrincipal, montantPenalites, dossierRecouvrement, contribuable, agentGenerateur);
        
        this.receveur = receveur;
    }

    // Getters et Setters
    public Agent getReceveur() {
        return receveur;
    }

    public void setReceveur(Agent receveur) {
        this.receveur = receveur;
    }

    public List<DocumentRecouvrement> getDocumentsAssocies() {
        return documentsAssocies;
    }

    public void setDocumentsAssocies(List<DocumentRecouvrement> documentsAssocies) {
        this.documentsAssocies = documentsAssocies;
    }
    
    public void ajouterDocumentAssocie(DocumentRecouvrement document) {
        document.setDocumentParent(this);
        this.documentsAssocies.add(document);
    }
}
