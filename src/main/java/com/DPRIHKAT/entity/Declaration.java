package com.DPRIHKAT.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.DPRIHKAT.entity.enums.StatutDeclaration;
import com.DPRIHKAT.entity.enums.SourceDeclaration;
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
}
