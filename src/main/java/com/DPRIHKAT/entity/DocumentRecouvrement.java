package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeDocumentRecouvrement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

/**
 * Entité représentant un document de recouvrement (AMR, MED, Commandement, etc.)
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DocumentRecouvrement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TypeDocumentRecouvrement type;

    @Enumerated(EnumType.STRING)
    private StatutDocumentRecouvrement statut;

    private Date dateGeneration;
    
    private Date dateNotification;
    
    private Date dateEcheance;
    
    private String reference;
    
    private String contenu;
    
    private Double montantPrincipal;
    
    private Double montantPenalites;
    
    private Double montantTotal;
    
    private String observations;
    
    private boolean actif = true;
    
    private String codeQR;
    
    @ManyToOne
    @JoinColumn(name = "dossier_recouvrement_id")
    @JsonIdentityReference(alwaysAsId = true)
    private DossierRecouvrement dossierRecouvrement;
    
    @ManyToOne
    @JoinColumn(name = "contribuable_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Contribuable contribuable;
    
    @ManyToOne
    @JoinColumn(name = "agent_generateur_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Agent agentGenerateur;
    
    @ManyToOne
    @JoinColumn(name = "agent_notificateur_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Agent agentNotificateur;
    
    @ManyToOne
    @JoinColumn(name = "document_parent_id")
    @JsonIdentityReference(alwaysAsId = true)
    private DocumentRecouvrement documentParent;

    public DocumentRecouvrement() {
    }

    public DocumentRecouvrement(TypeDocumentRecouvrement type, StatutDocumentRecouvrement statut, 
                               Date dateGeneration, String reference, Double montantPrincipal, 
                               Double montantPenalites, DossierRecouvrement dossierRecouvrement, 
                               Contribuable contribuable, Agent agentGenerateur) {
        this.type = type;
        this.statut = statut;
        this.dateGeneration = dateGeneration;
        this.reference = reference;
        this.montantPrincipal = montantPrincipal;
        this.montantPenalites = montantPenalites;
        this.montantTotal = montantPrincipal + (montantPenalites != null ? montantPenalites : 0);
        this.dossierRecouvrement = dossierRecouvrement;
        this.contribuable = contribuable;
        this.agentGenerateur = agentGenerateur;
        this.actif = true;
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TypeDocumentRecouvrement getType() {
        return type;
    }

    public void setType(TypeDocumentRecouvrement type) {
        this.type = type;
    }

    public StatutDocumentRecouvrement getStatut() {
        return statut;
    }

    public void setStatut(StatutDocumentRecouvrement statut) {
        this.statut = statut;
    }

    public Date getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(Date dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public Date getDateNotification() {
        return dateNotification;
    }

    public void setDateNotification(Date dateNotification) {
        this.dateNotification = dateNotification;
    }

    public Date getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Double getMontantPrincipal() {
        return montantPrincipal;
    }

    public void setMontantPrincipal(Double montantPrincipal) {
        this.montantPrincipal = montantPrincipal;
        this.calculerMontantTotal();
    }

    public Double getMontantPenalites() {
        return montantPenalites;
    }

    public void setMontantPenalites(Double montantPenalites) {
        this.montantPenalites = montantPenalites;
        this.calculerMontantTotal();
    }

    public Double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }
    
    private void calculerMontantTotal() {
        this.montantTotal = this.montantPrincipal + (this.montantPenalites != null ? this.montantPenalites : 0);
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getCodeQR() {
        return codeQR;
    }

    public void setCodeQR(String codeQR) {
        this.codeQR = codeQR;
    }

    public DossierRecouvrement getDossierRecouvrement() {
        return dossierRecouvrement;
    }

    public void setDossierRecouvrement(DossierRecouvrement dossierRecouvrement) {
        this.dossierRecouvrement = dossierRecouvrement;
    }

    public Contribuable getContribuable() {
        return contribuable;
    }

    public void setContribuable(Contribuable contribuable) {
        this.contribuable = contribuable;
    }

    public Agent getAgentGenerateur() {
        return agentGenerateur;
    }

    public void setAgentGenerateur(Agent agentGenerateur) {
        this.agentGenerateur = agentGenerateur;
    }

    public Agent getAgentNotificateur() {
        return agentNotificateur;
    }

    public void setAgentNotificateur(Agent agentNotificateur) {
        this.agentNotificateur = agentNotificateur;
    }

    public DocumentRecouvrement getDocumentParent() {
        return documentParent;
    }

    public void setDocumentParent(DocumentRecouvrement documentParent) {
        this.documentParent = documentParent;
    }
}
