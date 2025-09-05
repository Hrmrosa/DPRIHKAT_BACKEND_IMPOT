package com.DPRIHKAT.entity;

/**
 *
 * @author amateur
 */
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.DPRIHKAT.entity.enums.StatutDeclaration;
import com.DPRIHKAT.entity.enums.TypeImpot;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Declaration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Date date;

    private Double montant;

    @Enumerated(EnumType.STRING)
    private StatutDeclaration statut;

    @Enumerated(EnumType.STRING)
    private TypeImpot typeImpot; // IF, IRL, ICM

    private boolean exoneration; // Gestion des exonérations
    
    private boolean actif = true; // Champ pour la suppression logique

    @ManyToOne
    @JoinColumn(name = "propriete_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Propriete propriete;

    @ManyToOne
    @JoinColumn(name = "concession_id")
    @JsonIdentityReference(alwaysAsId = true)
    private ConcessionMinier concession;

    @ManyToOne
    @JoinColumn(name = "agent_validateur_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Agent agentValidateur;

    @OneToOne
    @JoinColumn(name = "paiement_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Paiement paiement;

    @OneToMany(mappedBy = "declaration")
    private List<Penalite> penalites = new ArrayList<>();

    public Declaration() {
    }

    public Declaration(Date date, Double montant, StatutDeclaration statut, TypeImpot typeImpot, boolean exoneration, Propriete propriete, ConcessionMinier concession, Agent agentValidateur) {
        this.date = date;
        this.montant = montant;
        this.statut = statut;
        this.typeImpot = typeImpot;
        this.exoneration = exoneration;
        setPropriete(propriete); // Utiliser la méthode set pour synchronisation
        setConcession(concession); // Utiliser la méthode set pour synchronisation
        this.agentValidateur = agentValidateur;
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

    // Méthodes
    public void soumettreDeclaration() {
        if (typeImpot == TypeImpot.IRL && !exoneration) {
            double acompte = montant * 0.20;
            // TODO: Générer paiement acompte
            // Solde de 2% dû au 1er février
        }
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public StatutDeclaration getStatut() {
        return statut;
    }

    public void setStatut(StatutDeclaration statut) {
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

    public Paiement getPaiement() {
        return paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public List<Penalite> getPenalites() {
        return penalites;
    }

    public void setPenalites(List<Penalite> penalites) {
        this.penalites = penalites;
    }
}
