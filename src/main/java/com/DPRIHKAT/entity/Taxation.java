package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.DPRIHKAT.entity.enums.Devise;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Entité représentant la taxation d'un bien déclaré
 * La taxation est le fait de taxer un bien déjà déclaré ou enregistré au nom d'un contribuable
 * et assujetti ou lié à un type d'impôt
 * 
 * @author amateur
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Taxation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Date dateTaxation;

    private Double montant;

    private String exercice;
    
    @Enumerated(EnumType.STRING)
    private Devise devise = Devise.USD; // Par défaut en USD

    @Enumerated(EnumType.STRING)
    private StatutTaxation statut;

    @Enumerated(EnumType.STRING)
    private TypeImpot typeImpot;

    private boolean exoneration; // Gestion des exonérations
    
    private String motifExoneration; // Motif de l'exonération
    
    private Date dateEcheance; // Date d'échéance de la taxation
    
    private String numeroTaxation; // Numéro de taxation au format t_0001_typeimpot_codeBureauTaxateur_annee
    
    private String codeQR; // Code QR pour l'impression
    
    private String nomBanque; // Nom de la banque
    
    private String numeroCompte; // Numéro de compte bancaire
    
    private String intituleCompte; // Intitulé du compte bancaire
    
    private String motifAnnulation; // Motif d'annulation de la taxation
    
    private boolean actif = true; // Gestion de la suppression logique
    
    // Déclaration associée à cette taxation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "declaration_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Declaration declaration;

    // Nature d'impôt associée à cette taxation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nature_impot_id")
    @JsonIdentityReference(alwaysAsId = true)
    private NatureImpot natureImpot;

    // Agent qui a effectué la taxation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Agent agent;

    // Paiement associé à cette taxation
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paiement_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Paiement paiement;

    // Apurements associés à cette taxation
    @OneToMany(mappedBy = "taxation", fetch = FetchType.LAZY)
    private List<Apurement> apurements = new ArrayList<>();

    // Relation avec ProprieteImpot
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propriete_impot_id")
    @JsonIdentityReference(alwaysAsId = true)
    private ProprieteImpot proprieteImpot;

    // Contribuable associé à cette taxation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribuable_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Contribuable contribuable;

    // Propriete associée à cette taxation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propriete_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Propriete propriete;

    // Demande de plaque associée à cette taxation
    @OneToOne(mappedBy = "taxation")
    @JsonIdentityReference(alwaysAsId = true)
    private DemandePlaque demande;

    // Vignette associée à cette taxation
    @OneToOne(mappedBy = "taxation")
    @JsonIdentityReference(alwaysAsId = true)
    private Vignette vignette;
    
    // Concession minière associée à cette taxation
    @OneToOne(mappedBy = "taxation")
    @JsonIdentityReference(alwaysAsId = true)
    private ConcessionMinier concessionMinier;

    public Taxation() {
    }

    public Taxation(Date dateTaxation, Double montant, String exercice, StatutTaxation statut, 
                   TypeImpot typeImpot, boolean exoneration, Declaration declaration, 
                   NatureImpot natureImpot, Agent agent) {
        this.dateTaxation = dateTaxation;
        this.montant = montant;
        this.exercice = exercice;
        this.statut = statut;
        this.typeImpot = typeImpot;
        this.exoneration = exoneration;
        this.declaration = declaration;
        this.natureImpot = natureImpot;
        this.agent = agent;
        this.actif = true;
    }

    // Méthodes
    /**
     * Calcule le montant de la taxation en fonction du bien déclaré et de la nature d'impôt
     */
    public void calculerMontant() {
        if (declaration == null || declaration.getPropriete() == null) {
            throw new IllegalStateException("La taxation doit être associée à une déclaration avec un bien valide.");
        }
        
        Propriete bien = declaration.getPropriete();
        
        // Logique de calcul du montant en fonction du type d'impôt et des caractéristiques du bien
        // Cette méthode pourrait être enrichie avec des règles de calcul plus complexes
        switch (typeImpot) {
            case IF:
                // Impôt foncier basé sur la superficie
                this.montant = bien.getSuperficie() * 0.5;
                break;
            case IRL:
                // Impôt sur revenu locatif (estimation)
                this.montant = bien.getSuperficie() * 0.3;
                break;
            case ICM:
                // Impôt sur concession minière
                if (declaration.getConcession() != null) {
                    this.montant = declaration.getConcession().getSuperficie() * 1.2;
                }
                break;
            default:
                // Montant par défaut
                this.montant = 100.0;
        }
        
        // Appliquer l'exonération si nécessaire
        if (exoneration) {
            this.montant = 0.0;
        }
    }

    /**
     * Définit l'apurement pour cette taxation
     */
    public void setApurement(Apurement apurement) {
        if (this.apurements == null) {
            this.apurements = new ArrayList<>();
        }
        this.apurements.add(apurement);
    }

    /**
     * Définit le motif d'exonération
     */
    public void setMotifExoneration(String motifExoneration) {
        this.motifExoneration = motifExoneration;
    }

    /**
     * Récupère le motif d'exonération
     */
    public String getMotifExoneration() {
        return motifExoneration;
    }

    /**
     * Définit le ProprieteImpot associé à cette taxation
     */
    public void setProprieteImpot(ProprieteImpot proprieteImpot) {
        this.proprieteImpot = proprieteImpot;
    }

    /**
     * Récupère le ProprieteImpot associé à cette taxation
     */
    public ProprieteImpot getProprieteImpot() {
        return proprieteImpot;
    }

    /**
     * Récupère la Propriete associée à cette taxation via la déclaration
     */
    public Propriete getPropriete() {
        if (declaration != null) {
            return declaration.getPropriete();
        }
        return null;
    }

    /**
     * Définit la Propriete associée à cette taxation via la déclaration
     */
    public void setPropriete(Propriete propriete) {
        if (this.declaration == null) {
            this.declaration = new Declaration();
        }
        this.declaration.setPropriete(propriete);
    }

    /**
     * Récupère le Contribuable associé à cette taxation via la déclaration
     */
    public Contribuable getContribuable() {
        if (declaration != null) {
            return declaration.getContribuable();
        }
        return null;
    }

    /**
     * Définit le Contribuable associé à cette taxation via la déclaration
     */
    public void setContribuable(Contribuable contribuable) {
        if (this.declaration == null) {
            this.declaration = new Declaration();
        }
        this.declaration.setContribuable(contribuable);
    }

    /**
     * Récupère le nom de l'agent qui a effectué la taxation
     */
    public String getNomAgent() {
        if (agent != null) {
            return agent.getNom();
        }
        return null;
    }

    /**
     * Récupère le code QR
     */
    public String getCodeQR() {
        return codeQR;
    }

    /**
     * Définit le code QR
     */
    public void setCodeQR(String codeQR) {
        this.codeQR = codeQR;
    }

    /**
     * Récupère le numéro de taxation
     */
    public String getNumeroTaxation() {
        return numeroTaxation;
    }

    /**
     * Définit le numéro de taxation
     */
    public void setNumeroTaxation(String numeroTaxation) {
        this.numeroTaxation = numeroTaxation;
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDateTaxation() {
        return dateTaxation;
    }

    public void setDateTaxation(Date dateTaxation) {
        this.dateTaxation = dateTaxation;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getExercice() {
        return exercice;
    }

    public void setExercice(String exercice) {
        this.exercice = exercice;
    }

    public Devise getDevise() {
        return devise;
    }

    public void setDevise(Devise devise) {
        this.devise = devise;
    }

    public StatutTaxation getStatut() {
        return statut;
    }

    public void setStatut(StatutTaxation statut) {
        this.statut = statut;
    }

    public TypeImpot getTypeImpot() {
        return typeImpot;
    }

    public void setTypeImpot(TypeImpot typeImpot) {
        this.typeImpot = typeImpot;
    }

    public boolean isExoneration() {
        return exoneration;
    }

    public void setExoneration(boolean exoneration) {
        this.exoneration = exoneration;
    }

    public Date getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }

    public NatureImpot getNatureImpot() {
        return natureImpot;
    }

    public void setNatureImpot(NatureImpot natureImpot) {
        this.natureImpot = natureImpot;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Paiement getPaiement() {
        return paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public List<Apurement> getApurements() {
        return apurements;
    }

    public void setApurements(List<Apurement> apurements) {
        this.apurements = apurements;
    }

    public Contribuable getContribuableDirect() {
        return contribuable;
    }

    public void setContribuableDirect(Contribuable contribuable) {
        this.contribuable = contribuable;
    }

    public Propriete getProprieteDirect() {
        return propriete;
    }

    public void setProprieteDirect(Propriete propriete) {
        this.propriete = propriete;
    }

    public String getNomBanque() {
        return nomBanque;
    }

    public void setNomBanque(String nomBanque) {
        this.nomBanque = nomBanque;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public String getIntituleCompte() {
        return intituleCompte;
    }

    public void setIntituleCompte(String intituleCompte) {
        this.intituleCompte = intituleCompte;
    }

    public String getMotifAnnulation() {
        return motifAnnulation;
    }

    public void setMotifAnnulation(String motifAnnulation) {
        this.motifAnnulation = motifAnnulation;
    }

    public DemandePlaque getDemande() {
        return demande;
    }

    public void setDemande(DemandePlaque demande) {
        this.demande = demande;
    }

    public Vignette getVignette() {
        return vignette;
    }

    public void setVignette(Vignette vignette) {
        this.vignette = vignette;
    }
    
    public ConcessionMinier getConcessionMinier() {
        return concessionMinier;
    }

    public void setConcessionMinier(ConcessionMinier concessionMinier) {
        this.concessionMinier = concessionMinier;
    }
    /**
     * Alias pour getDateTaxation pour compatibilité avec le service CollecteService
     * @return La date de calcul de la taxation
     */
    public Date getDateCalcul() {
        return this.dateTaxation;
    }
}
