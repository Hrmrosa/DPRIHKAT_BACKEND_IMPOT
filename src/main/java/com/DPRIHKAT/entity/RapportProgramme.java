package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.FrequenceRapport;
import com.DPRIHKAT.entity.enums.FormatRapport;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Configuration d'un rapport programmé pour envoi automatique
 */
@Entity
public class RapportProgramme {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
    
    // Nom du rapport
    private String nom;
    
    // Description du rapport
    private String description;
    
    // Type de rapport (dashboard, taxations, paiements, etc.)
    private String typeRapport;
    
    // Fréquence d'envoi
    @Enumerated(EnumType.STRING)
    private FrequenceRapport frequence;
    
    // Format du rapport
    @Enumerated(EnumType.STRING)
    private FormatRapport format;
    
    // Destinataires email (séparés par des virgules)
    private String destinataires;
    
    // Objet de l'email
    private String objetEmail;
    
    // Corps de l'email
    @Column(columnDefinition = "TEXT")
    private String corpsEmail;
    
    // Jour de la semaine (1-7, pour les rapports hebdomadaires)
    private Integer jourSemaine;
    
    // Jour du mois (1-31, pour les rapports mensuels)
    private Integer jourMois;
    
    // Heure d'envoi (0-23)
    private Integer heure;
    
    // Minute d'envoi (0-59)
    private Integer minute;
    
    // Date de dernière exécution
    @Temporal(TemporalType.TIMESTAMP)
    private Date derniereExecution;
    
    // Date de prochaine exécution
    @Temporal(TemporalType.TIMESTAMP)
    private Date prochaineExecution;
    
    // Rapport actif
    private boolean actif = true;
    
    // Paramètres du rapport (stockés au format JSON)
    @Column(columnDefinition = "TEXT")
    private String parametres;
    
    // Période de données à inclure (en jours)
    private Integer periodeDonnees;
    
    public RapportProgramme() {
    }
    
    public RapportProgramme(Utilisateur utilisateur, String nom, String typeRapport, 
                           FrequenceRapport frequence, FormatRapport format, String destinataires) {
        this.utilisateur = utilisateur;
        this.nom = nom;
        this.typeRapport = typeRapport;
        this.frequence = frequence;
        this.format = format;
        this.destinataires = destinataires;
        this.objetEmail = "Rapport automatique: " + nom;
        this.heure = 8; // 8h du matin par défaut
        this.minute = 0;
        this.actif = true;
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

    public String getTypeRapport() {
        return typeRapport;
    }

    public void setTypeRapport(String typeRapport) {
        this.typeRapport = typeRapport;
    }

    public FrequenceRapport getFrequence() {
        return frequence;
    }

    public void setFrequence(FrequenceRapport frequence) {
        this.frequence = frequence;
    }

    public FormatRapport getFormat() {
        return format;
    }

    public void setFormat(FormatRapport format) {
        this.format = format;
    }

    public String getDestinataires() {
        return destinataires;
    }

    public void setDestinataires(String destinataires) {
        this.destinataires = destinataires;
    }

    public String getObjetEmail() {
        return objetEmail;
    }

    public void setObjetEmail(String objetEmail) {
        this.objetEmail = objetEmail;
    }

    public String getCorpsEmail() {
        return corpsEmail;
    }

    public void setCorpsEmail(String corpsEmail) {
        this.corpsEmail = corpsEmail;
    }

    public Integer getJourSemaine() {
        return jourSemaine;
    }

    public void setJourSemaine(Integer jourSemaine) {
        this.jourSemaine = jourSemaine;
    }

    public Integer getJourMois() {
        return jourMois;
    }

    public void setJourMois(Integer jourMois) {
        this.jourMois = jourMois;
    }

    public Integer getHeure() {
        return heure;
    }

    public void setHeure(Integer heure) {
        this.heure = heure;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Date getDerniereExecution() {
        return derniereExecution;
    }

    public void setDerniereExecution(Date derniereExecution) {
        this.derniereExecution = derniereExecution;
    }

    public Date getProchaineExecution() {
        return prochaineExecution;
    }

    public void setProchaineExecution(Date prochaineExecution) {
        this.prochaineExecution = prochaineExecution;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getParametres() {
        return parametres;
    }

    public void setParametres(String parametres) {
        this.parametres = parametres;
    }

    public Integer getPeriodeDonnees() {
        return periodeDonnees;
    }

    public void setPeriodeDonnees(Integer periodeDonnees) {
        this.periodeDonnees = periodeDonnees;
    }
}
