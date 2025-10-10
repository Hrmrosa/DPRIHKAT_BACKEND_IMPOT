package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.TypeAnalyse;
import com.DPRIHKAT.entity.enums.PeriodePrediction;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Configuration et résultats d'une analyse prédictive
 */
@Entity
public class AnalysePredictive {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
    
    // Nom de l'analyse
    private String nom;
    
    // Description de l'analyse
    private String description;
    
    // Type d'analyse
    @Enumerated(EnumType.STRING)
    private TypeAnalyse typeAnalyse;
    
    // Période de prédiction
    @Enumerated(EnumType.STRING)
    private PeriodePrediction periodePrediction;
    
    // Date de création
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    // Date de dernière exécution
    @Temporal(TemporalType.TIMESTAMP)
    private Date derniereExecution;
    
    // Paramètres de l'analyse (stockés au format JSON)
    @Column(columnDefinition = "TEXT")
    private String parametres;
    
    // Résultats de l'analyse (stockés au format JSON)
    @Column(columnDefinition = "TEXT")
    private String resultats;
    
    // Précision du modèle (pourcentage)
    private Double precision;
    
    // Analyse active
    private boolean active = true;
    
    // Exécution automatique
    private boolean executionAutomatique = false;
    
    // Fréquence d'exécution automatique (en jours)
    private Integer frequenceExecution;
    
    // Date de prochaine exécution
    @Temporal(TemporalType.TIMESTAMP)
    private Date prochaineExecution;
    
    // Métadonnées supplémentaires (stockées au format JSON)
    @Column(columnDefinition = "TEXT")
    private String metadonnees;
    
    public AnalysePredictive() {
        this.dateCreation = new Date();
    }
    
    public AnalysePredictive(Utilisateur utilisateur, String nom, TypeAnalyse typeAnalyse, 
                            PeriodePrediction periodePrediction) {
        this.utilisateur = utilisateur;
        this.nom = nom;
        this.typeAnalyse = typeAnalyse;
        this.periodePrediction = periodePrediction;
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

    public TypeAnalyse getTypeAnalyse() {
        return typeAnalyse;
    }

    public void setTypeAnalyse(TypeAnalyse typeAnalyse) {
        this.typeAnalyse = typeAnalyse;
    }

    public PeriodePrediction getPeriodePrediction() {
        return periodePrediction;
    }

    public void setPeriodePrediction(PeriodePrediction periodePrediction) {
        this.periodePrediction = periodePrediction;
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

    public Double getPrecision() {
        return precision;
    }

    public void setPrecision(Double precision) {
        this.precision = precision;
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

    public String getMetadonnees() {
        return metadonnees;
    }

    public void setMetadonnees(String metadonnees) {
        this.metadonnees = metadonnees;
    }
}
