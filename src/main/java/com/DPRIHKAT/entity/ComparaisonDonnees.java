package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.TypeComparaison;
import com.DPRIHKAT.entity.enums.PeriodeComparaison;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Configuration et résultats d'une comparaison de données
 */
@Entity
public class ComparaisonDonnees {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
    
    // Nom de la comparaison
    private String nom;
    
    // Description de la comparaison
    private String description;
    
    // Type de comparaison
    @Enumerated(EnumType.STRING)
    private TypeComparaison typeComparaison;
    
    // Période de référence
    @Enumerated(EnumType.STRING)
    private PeriodeComparaison periodeReference;
    
    // Période de comparaison
    @Enumerated(EnumType.STRING)
    private PeriodeComparaison periodeComparaison;
    
    // Date de début de la période de référence
    @Temporal(TemporalType.DATE)
    private Date dateDebutReference;
    
    // Date de fin de la période de référence
    @Temporal(TemporalType.DATE)
    private Date dateFinReference;
    
    // Date de début de la période de comparaison
    @Temporal(TemporalType.DATE)
    private Date dateDebutComparaison;
    
    // Date de fin de la période de comparaison
    @Temporal(TemporalType.DATE)
    private Date dateFinComparaison;
    
    // Date de création
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    // Date de dernière exécution
    @Temporal(TemporalType.TIMESTAMP)
    private Date derniereExecution;
    
    // Paramètres de la comparaison (stockés au format JSON)
    @Column(columnDefinition = "TEXT")
    private String parametres;
    
    // Résultats de la comparaison (stockés au format JSON)
    @Column(columnDefinition = "TEXT")
    private String resultats;
    
    // Comparaison active
    private boolean active = true;
    
    // Exécution automatique
    private boolean executionAutomatique = false;
    
    // Fréquence d'exécution automatique (en jours)
    private Integer frequenceExecution;
    
    // Date de prochaine exécution
    @Temporal(TemporalType.TIMESTAMP)
    private Date prochaineExecution;
    
    // Filtres supplémentaires (stockés au format JSON)
    @Column(columnDefinition = "TEXT")
    private String filtres;
    
    public ComparaisonDonnees() {
        this.dateCreation = new Date();
    }
    
    public ComparaisonDonnees(Utilisateur utilisateur, String nom, TypeComparaison typeComparaison, 
                             PeriodeComparaison periodeReference, PeriodeComparaison periodeComparaison) {
        this.utilisateur = utilisateur;
        this.nom = nom;
        this.typeComparaison = typeComparaison;
        this.periodeReference = periodeReference;
        this.periodeComparaison = periodeComparaison;
        this.dateCreation = new Date();
        this.active = true;
        this.executionAutomatique = false;
    }

    // Getters and Setters
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeComparaison getTypeComparaison() {
        return typeComparaison;
    }

    public void setTypeComparaison(TypeComparaison typeComparaison) {
        this.typeComparaison = typeComparaison;
    }

    public PeriodeComparaison getPeriodeReference() {
        return periodeReference;
    }

    public void setPeriodeReference(PeriodeComparaison periodeReference) {
        this.periodeReference = periodeReference;
    }

    public PeriodeComparaison getPeriodeComparaison() {
        return periodeComparaison;
    }

    public void setPeriodeComparaison(PeriodeComparaison periodeComparaison) {
        this.periodeComparaison = periodeComparaison;
    }

    public Date getDateDebutReference() {
        return dateDebutReference;
    }

    public void setDateDebutReference(Date dateDebutReference) {
        this.dateDebutReference = dateDebutReference;
    }

    public Date getDateFinReference() {
        return dateFinReference;
    }

    public void setDateFinReference(Date dateFinReference) {
        this.dateFinReference = dateFinReference;
    }

    public Date getDateDebutComparaison() {
        return dateDebutComparaison;
    }

    public void setDateDebutComparaison(Date dateDebutComparaison) {
        this.dateDebutComparaison = dateDebutComparaison;
    }

    public Date getDateFinComparaison() {
        return dateFinComparaison;
    }

    public void setDateFinComparaison(Date dateFinComparaison) {
        this.dateFinComparaison = dateFinComparaison;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDerniereExecution() {
        return derniereExecution;
    }

    public void setDerniereExecution(Date derniereExecution) {
        this.derniereExecution = derniereExecution;
    }

    public String getParametres() {
        return parametres;
    }

    public void setParametres(String parametres) {
        this.parametres = parametres;
    }

    public String getResultats() {
        return resultats;
    }

    public void setResultats(String resultats) {
        this.resultats = resultats;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isExecutionAutomatique() {
        return executionAutomatique;
    }

    public void setExecutionAutomatique(boolean executionAutomatique) {
        this.executionAutomatique = executionAutomatique;
    }

    public Integer getFrequenceExecution() {
        return frequenceExecution;
    }

    public void setFrequenceExecution(Integer frequenceExecution) {
        this.frequenceExecution = frequenceExecution;
    }

    public Date getProchaineExecution() {
        return prochaineExecution;
    }

    public void setProchaineExecution(Date prochaineExecution) {
        this.prochaineExecution = prochaineExecution;
    }

    public String getFiltres() {
        return filtres;
    }

    public void setFiltres(String filtres) {
        this.filtres = filtres;
    }
}
