package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.NiveauAlerte;
import com.DPRIHKAT.entity.enums.TypeAlerte;
import com.DPRIHKAT.entity.enums.OperateurComparaison;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Configuration d'une alerte basée sur un seuil
 */
@Entity
public class AlerteConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
    
    // Nom de l'alerte
    private String nom;
    
    // Description de l'alerte
    private String description;
    
    // Type d'alerte (taxations, paiements, taux de recouvrement, etc.)
    @Enumerated(EnumType.STRING)
    private TypeAlerte typeAlerte;
    
    // Métrique à surveiller
    private String metrique;
    
    // Opérateur de comparaison (>, <, =, >=, <=)
    @Enumerated(EnumType.STRING)
    private OperateurComparaison operateur;
    
    // Valeur seuil
    private Double valeurSeuil;
    
    // Niveau d'alerte (INFO, WARNING, CRITICAL)
    @Enumerated(EnumType.STRING)
    private NiveauAlerte niveauAlerte;
    
    // Notification par email
    private boolean notificationEmail;
    
    // Notification dans l'application
    private boolean notificationApplication;
    
    // Notification par SMS
    private boolean notificationSMS;
    
    // Fréquence de vérification (en minutes)
    private int frequenceVerification = 60;
    
    // Dernière vérification
    @Temporal(TemporalType.TIMESTAMP)
    private Date derniereVerification;
    
    // Dernière alerte déclenchée
    @Temporal(TemporalType.TIMESTAMP)
    private Date derniereAlerte;
    
    // Alerte active
    private boolean active = true;
    
    // Période de silence après déclenchement (en minutes)
    private int periodeSilence = 1440; // 24 heures par défaut
    
    // Condition supplémentaire (stockée au format JSON)
    @Column(columnDefinition = "TEXT")
    private String conditionSupplementaire;
    
    public AlerteConfig() {
    }
    
    public AlerteConfig(Utilisateur utilisateur, String nom, TypeAlerte typeAlerte, 
                        String metrique, OperateurComparaison operateur, Double valeurSeuil, 
                        NiveauAlerte niveauAlerte) {
        this.utilisateur = utilisateur;
        this.nom = nom;
        this.typeAlerte = typeAlerte;
        this.metrique = metrique;
        this.operateur = operateur;
        this.valeurSeuil = valeurSeuil;
        this.niveauAlerte = niveauAlerte;
        this.notificationApplication = true;
        this.notificationEmail = false;
        this.notificationSMS = false;
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

    public TypeAlerte getTypeAlerte() {
        return typeAlerte;
    }

    public void setTypeAlerte(TypeAlerte typeAlerte) {
        this.typeAlerte = typeAlerte;
    }

    public String getMetrique() {
        return metrique;
    }

    public void setMetrique(String metrique) {
        this.metrique = metrique;
    }

    public OperateurComparaison getOperateur() {
        return operateur;
    }

    public void setOperateur(OperateurComparaison operateur) {
        this.operateur = operateur;
    }

    public Double getValeurSeuil() {
        return valeurSeuil;
    }

    public void setValeurSeuil(Double valeurSeuil) {
        this.valeurSeuil = valeurSeuil;
    }

    public NiveauAlerte getNiveauAlerte() {
        return niveauAlerte;
    }

    public void setNiveauAlerte(NiveauAlerte niveauAlerte) {
        this.niveauAlerte = niveauAlerte;
    }

    public boolean isNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(boolean notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public boolean isNotificationApplication() {
        return notificationApplication;
    }

    public void setNotificationApplication(boolean notificationApplication) {
        this.notificationApplication = notificationApplication;
    }

    public boolean isNotificationSMS() {
        return notificationSMS;
    }

    public void setNotificationSMS(boolean notificationSMS) {
        this.notificationSMS = notificationSMS;
    }

    public int getFrequenceVerification() {
        return frequenceVerification;
    }

    public void setFrequenceVerification(int frequenceVerification) {
        this.frequenceVerification = frequenceVerification;
    }

    public Date getDerniereVerification() {
        return derniereVerification;
    }

    public void setDerniereVerification(Date derniereVerification) {
        this.derniereVerification = derniereVerification;
    }

    public Date getDerniereAlerte() {
        return derniereAlerte;
    }

    public void setDerniereAlerte(Date derniereAlerte) {
        this.derniereAlerte = derniereAlerte;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getPeriodeSilence() {
        return periodeSilence;
    }

    public void setPeriodeSilence(int periodeSilence) {
        this.periodeSilence = periodeSilence;
    }

    public String getConditionSupplementaire() {
        return conditionSupplementaire;
    }

    public void setConditionSupplementaire(String conditionSupplementaire) {
        this.conditionSupplementaire = conditionSupplementaire;
    }
}
