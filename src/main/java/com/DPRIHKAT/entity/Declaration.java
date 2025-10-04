package com.DPRIHKAT.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.DPRIHKAT.entity.enums.StatutDeclaration;
import com.DPRIHKAT.entity.enums.SourceDeclaration;
import com.DPRIHKAT.entity.enums.TypeImpot;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Entité représentant la déclaration d'un bien par un contribuable
 * La déclaration est le fait d'enregistrer un bien au nom d'un contribuable
 * 
 * @author amateur
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Declaration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Date dateDeclaration;

    @Enumerated(EnumType.STRING)
    private StatutDeclaration statut;
    
    @Enumerated(EnumType.STRING)
    private SourceDeclaration source; // EN_LIGNE ou ADMINISTRATION
    
    private boolean actif = true; // Champ pour la suppression logique

    // Bien déclaré
    @ManyToOne
    @JoinColumn(name = "propriete_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Propriete propriete;

    // Concession minière déclarée (si applicable)
    @ManyToOne
    @JoinColumn(name = "concession_id")
    @JsonIdentityReference(alwaysAsId = true)
    private ConcessionMinier concession;

    // Agent qui a validé la déclaration (si déclaration à l'administration)
    @ManyToOne
    @JoinColumn(name = "agent_validateur_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Agent agentValidateur;
    
    // Contribuable qui a déclaré le bien
    @ManyToOne
    @JoinColumn(name = "contribuable_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Contribuable contribuable;
    
    // Taxations associées à cette déclaration
    @OneToMany(mappedBy = "declaration")
    private List<Taxation> taxations = new ArrayList<>();

    // Paiement associé à cette déclaration
    @ManyToOne
    @JoinColumn(name = "paiement_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Paiement paiement;

    private Double montant;

    @Enumerated(EnumType.STRING)
    private TypeImpot typeImpot;

    @ManyToOne
    @JoinColumn(name = "impot_id")
    private Impot impot;

    @OneToMany(mappedBy = "declaration", cascade = CascadeType.ALL)
    private List<Penalite> penalites = new ArrayList<>();

    // Relation avec le dossier de recouvrement
    @ManyToOne
    @JoinColumn(name = "dossier_recouvrement_id")
    private DossierRecouvrement dossier;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    public Declaration() {
    }

    public Declaration(Date dateDeclaration, StatutDeclaration statut, SourceDeclaration source, 
                      Propriete propriete, Contribuable contribuable) {
        this.dateDeclaration = dateDeclaration;
        this.statut = statut;
        this.source = source;
        this.propriete = propriete;
        this.contribuable = contribuable;
        this.actif = true;
    }

    // Méthode utilitaire pour gérer la relation bidirectionnelle avec Propriete
    public void setPropriete(Propriete propriete) {
        if (this.propriete != null && this.propriete.getDeclarations().contains(this)) {
            this.propriete.getDeclarations().remove(this); // Supprimer l'ancienne relation
        }
        this.propriete = propriete;
        if (propriete != null && !propriete.getDeclarations().contains(this)) {
            propriete.getDeclarations().add(this); // Ajouter la nouvelle relation
        }
    }

    // Méthode utilitaire pour gérer la relation bidirectionnelle avec ConcessionMinier
    public void setConcession(ConcessionMinier concession) {
        if (this.concession != null && this.concession.getDeclarations().contains(this)) {
            this.concession.getDeclarations().remove(this); // Supprimer l'ancienne relation
        }
        this.concession = concession;
        if (concession != null && !concession.getDeclarations().contains(this)) {
            concession.getDeclarations().add(this); // Ajouter la nouvelle relation
        }
    }
    
    /**
     * Valide une déclaration soumise
     * @param agent l'agent qui valide la déclaration
     */
    public void valider(Agent agent) {
        if (this.statut != StatutDeclaration.SOUMISE) {
            throw new IllegalStateException("Seule une déclaration soumise peut être validée.");
        }
        this.agentValidateur = agent;
        this.statut = StatutDeclaration.VALIDEE;
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDateDeclaration() {
        return dateDeclaration;
    }

    public void setDateDeclaration(Date dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public StatutDeclaration getStatut() {
        return statut;
    }

    public void setStatut(StatutDeclaration statut) {
        this.statut = statut;
    }

    public SourceDeclaration getSource() {
        return source;
    }

    public void setSource(SourceDeclaration source) {
        this.source = source;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Propriete getPropriete() {
        return propriete;
    }

    public ConcessionMinier getConcession() {
        return concession;
    }

    public Agent getAgentValidateur() {
        return agentValidateur;
    }

    public void setAgentValidateur(Agent agentValidateur) {
        this.agentValidateur = agentValidateur;
    }

    public Contribuable getContribuable() {
        return contribuable;
    }

    public void setContribuable(Contribuable contribuable) {
        this.contribuable = contribuable;
    }

    public List<Taxation> getTaxations() {
        return taxations;
    }

    public void setTaxations(List<Taxation> taxations) {
        this.taxations = taxations;
    }

    /**
     * Détermine le type d'impôt associé à cette déclaration
     * en fonction du bien déclaré (propriété ou concession)
     * @return Le type d'impôt applicable
     */
    public TypeImpot getTypeImpot() {
        if (concession != null) {
            // Si c'est une concession minière, le type d'impôt est ICM
            return TypeImpot.ICM;
        } else if (propriete != null) {
            // Pour les propriétés, le type d'impôt dépend des natures d'impôt associées
            // Par défaut, on considère que c'est un impôt foncier
            return TypeImpot.IF;
        }
        // Type par défaut si aucun bien n'est associé
        return TypeImpot.IF;
    }

    /**
     * Définit le type d'impôt de la déclaration
     * @param typeImpot Le type d'impôt à définir
     */
    public void setTypeImpot(TypeImpot typeImpot) {
        this.typeImpot = typeImpot;
    }

    /**
     * Récupère le type d'impôt de la déclaration
     * @return Le type d'impôt de la déclaration
     */
    public TypeImpot getTypeImpotDeclaration() {
        return typeImpot;
    }

    /**
     * Récupère la date de la déclaration
     * @return La date de déclaration
     */
    public Date getDate() {
        return dateDeclaration;
    }

    /**
     * Définit la date de la déclaration
     * @param date La date à définir
     */
    public void setDate(Date date) {
        this.dateDeclaration = date;
    }

    /**
     * Calcule le montant associé à cette déclaration
     * @return Le montant de la déclaration
     */
    public Double getMontant() {
        if (propriete != null) {
            // Pour les propriétés, on pourrait calculer un montant basé sur la superficie
            // Par exemple : 100 FC par m² (valeur arbitraire pour l'exemple)
            return propriete.getSuperficie() * 100.0;
        } else if (concession != null) {
            // Pour les concessions, on pourrait calculer un montant basé sur le nombre de carrés miniers
            // Par exemple : 1000 FC par carré minier (valeur arbitraire pour l'exemple)
            return (double) (concession.getNombreCarresMinier() * 1000);
        }
        // Montant par défaut si aucun bien n'est associé
        return 0.0;
    }

    /**
     * Définit le paiement associé à cette déclaration
     * @param paiement Le paiement à associer
     */
    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    /**
     * Récupère le paiement associé à cette déclaration
     * @return Le paiement associé
     */
    public Paiement getPaiement() {
        return paiement;
    }

    /**
     * Définit le montant de la déclaration
     * @param montant Le montant à définir
     */
    public void setMontant(Double montant) {
        this.montant = montant;
    }

    /**
     * Récupère le montant de la déclaration
     * @return Le montant de la déclaration
     */
    public Double getMontantDeclaration() {
        return montant;
    }

    public Impot getImpot() {
        return impot;
    }

    public void setImpot(Impot impot) {
        this.impot = impot;
    }

    public List<Penalite> getPenalites() {
        return penalites;
    }

    public void setPenalites(List<Penalite> penalites) {
        this.penalites = penalites;
    }

    public DossierRecouvrement getDossier() {
        return dossier;
    }

    public void setDossier(DossierRecouvrement dossier) {
        this.dossier = dossier;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Bureau getBureau() {
        return this.agentValidateur != null ? this.agentValidateur.getBureau() : null;
    }
}
