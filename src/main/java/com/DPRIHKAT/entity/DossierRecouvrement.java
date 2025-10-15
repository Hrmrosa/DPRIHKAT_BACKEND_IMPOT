package com.DPRIHKAT.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DossierRecouvrement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Date dateOuverture;
    private Date dateCloture;
    private String statut;
    private Double totalDette;
    private Double totalRecouvre;
    private String codeQR;

    @ManyToOne
    private Contribuable contribuable;

    @OneToMany(mappedBy = "dossier", cascade = CascadeType.ALL)
    private List<Declaration> declarations = new ArrayList<>();

    @OneToMany(mappedBy = "dossier", cascade = CascadeType.ALL)
    private List<Paiement> paiements = new ArrayList<>();

    @OneToMany(mappedBy = "dossierRecouvrement", cascade = CascadeType.ALL)
    private List<DocumentRecouvrement> documentsRecouvrement = new ArrayList<>();

    @OneToMany(mappedBy = "dossier", cascade = CascadeType.ALL)
    private List<HistoriqueDossier> historique = new ArrayList<>();

    public DossierRecouvrement() {
    }

    public DossierRecouvrement(Date dateOuverture, Date dateCloture, String statut, Double totalDette, Double totalRecouvre, String codeQR) {
        this.dateOuverture = dateOuverture;
        this.dateCloture = dateCloture;
        this.statut = statut;
        this.totalDette = totalDette;
        this.totalRecouvre = totalRecouvre;
        this.codeQR = codeQR;
    }

    // Getters et Setters
    public UUID getId() { 
        return id; 
    }
    public void setId(UUID id) { 
        this.id = id; 
    }
    
    public Date getDateOuverture() { 
        return dateOuverture; 
    }
    public void setDateOuverture(Date dateOuverture) { 
        this.dateOuverture = dateOuverture; 
    }
    
    public Date getDateCloture() { 
        return dateCloture; 
    }
    public void setDateCloture(Date dateCloture) { 
        this.dateCloture = dateCloture; 
    }
    
    public String getStatut() { 
        return statut; 
    }
    public void setStatut(String statut) { 
        this.statut = statut; 
    }
    
    public Double getTotalDette() { 
        return totalDette; 
    }
    public void setTotalDette(Double totalDette) { 
        this.totalDette = totalDette; 
    }
    
    public Double getTotalRecouvre() { 
        return totalRecouvre; 
    }
    public void setTotalRecouvre(Double totalRecouvre) { 
        this.totalRecouvre = totalRecouvre; 
    }
    
    public String getCodeQR() { 
        return codeQR; 
    }
    public void setCodeQR(String codeQR) { 
        this.codeQR = codeQR; 
    }
    
    public Contribuable getContribuable() { 
        return contribuable; 
    }
    public void setContribuable(Contribuable contribuable) { 
        this.contribuable = contribuable; 
    }
    
    public List<Declaration> getDeclarations() { 
        return declarations; 
    }
    public void setDeclarations(List<Declaration> declarations) { 
        this.declarations = declarations; 
    }
    
    public List<Paiement> getPaiements() { 
        return paiements; 
    }
    public void setPaiements(List<Paiement> paiements) { 
        this.paiements = paiements; 
    }
    
    public List<HistoriqueDossier> getHistorique() { 
        return historique; 
    }
    public void setHistorique(List<HistoriqueDossier> historique) { 
        this.historique = historique; 
    }
    
    public List<DocumentRecouvrement> getDocumentsRecouvrement() {
        return documentsRecouvrement;
    }
    
    public void setDocumentsRecouvrement(List<DocumentRecouvrement> documentsRecouvrement) {
        this.documentsRecouvrement = documentsRecouvrement;
    }
    
    /**
     * Ajoute un document de recouvrement au dossier
     * @param document Document à ajouter
     */
    public void ajouterDocumentRecouvrement(DocumentRecouvrement document) {
        document.setDossierRecouvrement(this);
        this.documentsRecouvrement.add(document);
    }
}
