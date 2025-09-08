package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.entity.enums.StatutApurement;
import com.DPRIHKAT.entity.enums.TypeApurement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Entité représentant un apurement
 * Un apurement est associé à une taxation
 * 
 * @author amateur
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Apurement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Date dateDemande;

    // Date de validation de l'apurement
    private Date dateValidation;

    @Enumerated(EnumType.STRING)
    private TypeApurement type;

    private Double montantApure;

    private String motif;

    // Motif de rejet (si applicable)
    private String motifRejet;

    @Enumerated(EnumType.STRING)
    private StatutApurement statut;

    private boolean provisoire; // Provisoire ou définitif
    
    private boolean actif = true; // Champ pour la suppression logique
    
    private boolean declarationPayee = false; // Indique si la déclaration a été payée

    // Déclaration concernée par l'apurement
    @ManyToOne
    @JoinColumn(name = "declaration_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Declaration declaration;

    // Agent qui a initié la demande d'apurement
    @ManyToOne
    @JoinColumn(name = "agent_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Agent agent;

    @ManyToOne
    @JoinColumn(name = "agent_validateur_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Agent agentValidateur;

    // Taxation concernée par l'apurement
    @ManyToOne
    @JoinColumn(name = "taxation_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Taxation taxation;
    
    // Paiement associé à l'apurement
    @ManyToOne
    @JoinColumn(name = "paiement_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Paiement paiement;

    @OneToOne
    @JoinColumn(name = "dossier_recouvrement_id")
    @JsonIdentityReference(alwaysAsId = true)
    private DossierRecouvrement dossierRecouvrement;

    public Apurement() {
    }

    public Apurement(Date dateDemande, TypeApurement type, Double montantApure, String motif, StatutApurement statut, boolean provisoire) {
        this.dateDemande = dateDemande;
        this.type = type;
        this.montantApure = montantApure;
        this.motif = motif;
        this.statut = statut;
        this.provisoire = provisoire;
        this.actif = true;
        this.declarationPayee = false;
    }

    // Méthodes
    public void validerApurement() {
        if (agentValidateur == null || !List.of(Role.APUREUR, Role.CHEF_DE_BUREAU, Role.CHEF_DE_DIVISION).contains(agentValidateur.getUtilisateur().getRole())) {
            throw new IllegalStateException("Seuls les rôles APUREUR, CHEF_DE_BUREAU ou CHEF_DE_DIVISION peuvent valider un apurement.");
        }
        if (dossierRecouvrement == null) {
            throw new IllegalStateException("L'apurement doit être associé à un dossier de recouvrement.");
        }
        this.statut = StatutApurement.ACCEPTEE;
        // Persister (à implémenter avec EntityManager)
    }
    
    /**
     * Marque la taxation comme payée et associe un paiement à l'apurement
     * @param paiement le paiement associé à l'apurement
     */
    public void marquerTaxationPayee(Paiement paiement) {
        if (taxation == null) {
            throw new IllegalStateException("L'apurement doit être associé à une taxation.");
        }
        this.declarationPayee = true;
        this.paiement = paiement;
    }
    
    /**
     * Récupère la déclaration associée à cet apurement
     * @return La déclaration associée
     */

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }

    public Date getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }

    public TypeApurement getType() {
        return type;
    }

    public void setType(TypeApurement type) {
        this.type = type;
    }

    public Double getMontantApure() {
        return montantApure;
    }

    public void setMontantApure(Double montantApure) {
        this.montantApure = montantApure;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getMotifRejet() {
        return motifRejet;
    }

    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }

    public StatutApurement getStatut() {
        return statut;
    }

    public void setStatut(StatutApurement statut) {
        this.statut = statut;
    }

    public boolean isProvisoire() {
        return provisoire;
    }

    public void setProvisoire(boolean provisoire) {
        this.provisoire = provisoire;
    }
    
    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
    
    public boolean isDeclarationPayee() {
        return declarationPayee;
    }

    public void setDeclarationPayee(boolean declarationPayee) {
        this.declarationPayee = declarationPayee;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Agent getAgentValidateur() {
        return agentValidateur;
    }

    public void setAgentValidateur(Agent agentValidateur) {
        this.agentValidateur = agentValidateur;
    }

    public Taxation getTaxation() {
        return taxation;
    }

    public void setTaxation(Taxation taxation) {
        this.taxation = taxation;
    }
    
    public Paiement getPaiement() {
        return paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public DossierRecouvrement getDossierRecouvrement() {
        return dossierRecouvrement;
    }

    public void setDossierRecouvrement(DossierRecouvrement dossierRecouvrement) {
        this.dossierRecouvrement = dossierRecouvrement;
    }
}
