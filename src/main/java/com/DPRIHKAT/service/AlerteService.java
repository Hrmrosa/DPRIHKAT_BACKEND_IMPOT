package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.AlerteConfig;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.NiveauAlerte;
import com.DPRIHKAT.entity.enums.OperateurComparaison;
import com.DPRIHKAT.entity.enums.TypeAlerte;
import com.DPRIHKAT.repository.AlerteConfigRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Service pour gérer les alertes basées sur des seuils
 */
@Service
public class AlerteService {

    private final AlerteConfigRepository alerteConfigRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final DashboardService dashboardService;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public AlerteService(
            AlerteConfigRepository alerteConfigRepository,
            UtilisateurRepository utilisateurRepository,
            DashboardService dashboardService,
            EmailService emailService,
            ObjectMapper objectMapper) {
        this.alerteConfigRepository = alerteConfigRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.dashboardService = dashboardService;
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    /**
     * Récupère toutes les alertes d'un utilisateur
     */
    public List<AlerteConfig> getAlertesForUser(UUID utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        return alerteConfigRepository.findByUtilisateur(utilisateur);
    }
    
    /**
     * Récupère toutes les alertes actives d'un utilisateur
     */
    public List<AlerteConfig> getAlertesActivesForUser(UUID utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        return alerteConfigRepository.findByUtilisateurAndActive(utilisateur, true);
    }
    
    /**
     * Crée une nouvelle alerte
     */
    @Transactional
    public AlerteConfig createAlerte(UUID utilisateurId, AlerteConfig alerteConfig) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        alerteConfig.setUtilisateur(utilisateur);
        alerteConfig.setDerniereVerification(null);
        alerteConfig.setDerniereAlerte(null);
        
        return alerteConfigRepository.save(alerteConfig);
    }
    
    /**
     * Met à jour une alerte existante
     */
    @Transactional
    public AlerteConfig updateAlerte(UUID alerteId, AlerteConfig updatedAlerte) {
        AlerteConfig existingAlerte = alerteConfigRepository.findById(alerteId)
                .orElseThrow(() -> new RuntimeException("Alerte non trouvée"));
        
        // Mettre à jour les propriétés
        existingAlerte.setNom(updatedAlerte.getNom());
        existingAlerte.setDescription(updatedAlerte.getDescription());
        existingAlerte.setTypeAlerte(updatedAlerte.getTypeAlerte());
        existingAlerte.setMetrique(updatedAlerte.getMetrique());
        existingAlerte.setOperateur(updatedAlerte.getOperateur());
        existingAlerte.setValeurSeuil(updatedAlerte.getValeurSeuil());
        existingAlerte.setNiveauAlerte(updatedAlerte.getNiveauAlerte());
        existingAlerte.setNotificationEmail(updatedAlerte.isNotificationEmail());
        existingAlerte.setNotificationApplication(updatedAlerte.isNotificationApplication());
        existingAlerte.setNotificationSMS(updatedAlerte.isNotificationSMS());
        existingAlerte.setFrequenceVerification(updatedAlerte.getFrequenceVerification());
        existingAlerte.setActive(updatedAlerte.isActive());
        existingAlerte.setPeriodeSilence(updatedAlerte.getPeriodeSilence());
        existingAlerte.setConditionSupplementaire(updatedAlerte.getConditionSupplementaire());
        
        return alerteConfigRepository.save(existingAlerte);
    }
    
    /**
     * Supprime une alerte
     */
    @Transactional
    public void deleteAlerte(UUID alerteId) {
        alerteConfigRepository.deleteById(alerteId);
    }
    
    /**
     * Active ou désactive une alerte
     */
    @Transactional
    public AlerteConfig toggleAlerte(UUID alerteId, boolean active) {
        AlerteConfig alerte = alerteConfigRepository.findById(alerteId)
                .orElseThrow(() -> new RuntimeException("Alerte non trouvée"));
        
        alerte.setActive(active);
        
        return alerteConfigRepository.save(alerte);
    }
    
    /**
     * Vérifie toutes les alertes actives
     * Cette méthode est appelée périodiquement par un scheduler
     */
    @Transactional
    public void verifierToutesAlertes() {
        List<AlerteConfig> alertesActives = alerteConfigRepository.findAll().stream()
                .filter(AlerteConfig::isActive)
                .filter(this::doitEtreVerifiee)
                .toList();
        
        for (AlerteConfig alerte : alertesActives) {
            verifierAlerte(alerte);
        }
    }
    
    /**
     * Détermine si une alerte doit être vérifiée en fonction de sa dernière vérification
     */
    private boolean doitEtreVerifiee(AlerteConfig alerte) {
        if (alerte.getDerniereVerification() == null) {
            return true;
        }
        
        long dernierVerifMs = alerte.getDerniereVerification().getTime();
        long maintenant = System.currentTimeMillis();
        long frequenceMs = TimeUnit.MINUTES.toMillis(alerte.getFrequenceVerification());
        
        return (maintenant - dernierVerifMs) >= frequenceMs;
    }
    
    /**
     * Vérifie une alerte spécifique
     */
    @Transactional
    public void verifierAlerte(AlerteConfig alerte) {
        // Mettre à jour la date de dernière vérification
        alerte.setDerniereVerification(new Date());
        
        // Récupérer la valeur actuelle de la métrique
        Double valeurActuelle = getValeurMetrique(alerte.getTypeAlerte(), alerte.getMetrique());
        
        // Si la valeur est null, on ne peut pas vérifier l'alerte
        if (valeurActuelle == null) {
            return;
        }
        
        // Vérifier si le seuil est dépassé
        boolean seuilDepasse = evaluerCondition(valeurActuelle, alerte.getOperateur(), alerte.getValeurSeuil());
        
        if (seuilDepasse) {
            // Vérifier si on est dans la période de silence
            if (peutEnvoyerAlerte(alerte)) {
                declencherAlerte(alerte, valeurActuelle);
            }
        }
        
        alerteConfigRepository.save(alerte);
    }
    
    /**
     * Récupère la valeur actuelle d'une métrique
     */
    private Double getValeurMetrique(TypeAlerte typeAlerte, String metrique) {
        Map<String, Object> statistiques = dashboardService.getStatistiquesGenerales();
        
        switch (typeAlerte) {
            case TAXATION_MONTANT:
                return Double.valueOf(statistiques.get("montant_total_taxations").toString());
            case PAIEMENT_MONTANT:
                return Double.valueOf(statistiques.get("montant_total_paiements").toString());
            case TAUX_RECOUVREMENT:
                return Double.valueOf(statistiques.get("taux_recouvrement").toString());
            case NOMBRE_CONTRIBUABLES:
                return Double.valueOf(statistiques.get("total_contribuables").toString());
            case NOMBRE_PROPRIETES:
                return Double.valueOf(statistiques.get("total_proprietes").toString());
            case NOMBRE_VEHICULES:
                return Double.valueOf(statistiques.get("total_vehicules").toString());
            default:
                return null;
        }
    }
    
    /**
     * Évalue une condition avec un opérateur de comparaison
     */
    private boolean evaluerCondition(Double valeurActuelle, OperateurComparaison operateur, Double valeurSeuil) {
        switch (operateur) {
            case SUPERIEUR:
                return valeurActuelle > valeurSeuil;
            case INFERIEUR:
                return valeurActuelle < valeurSeuil;
            case EGAL:
                return Objects.equals(valeurActuelle, valeurSeuil);
            case SUPERIEUR_EGAL:
                return valeurActuelle >= valeurSeuil;
            case INFERIEUR_EGAL:
                return valeurActuelle <= valeurSeuil;
            default:
                return false;
        }
    }
    
    /**
     * Vérifie si on peut envoyer une alerte en fonction de la période de silence
     */
    private boolean peutEnvoyerAlerte(AlerteConfig alerte) {
        if (alerte.getDerniereAlerte() == null) {
            return true;
        }
        
        long derniereAlerteMs = alerte.getDerniereAlerte().getTime();
        long maintenant = System.currentTimeMillis();
        long periodeSilenceMs = TimeUnit.MINUTES.toMillis(alerte.getPeriodeSilence());
        
        return (maintenant - derniereAlerteMs) >= periodeSilenceMs;
    }
    
    /**
     * Déclenche une alerte (notifications, etc.)
     */
    private void declencherAlerte(AlerteConfig alerte, Double valeurActuelle) {
        // Mettre à jour la date de dernière alerte
        alerte.setDerniereAlerte(new Date());
        
        // Créer le message d'alerte
        String message = String.format(
            "Alerte %s: %s - La valeur actuelle de %s est %s %s %s",
            alerte.getNiveauAlerte(),
            alerte.getNom(),
            alerte.getMetrique(),
            valeurActuelle,
            alerte.getOperateur().getSymbole(),
            alerte.getValeurSeuil()
        );
        
        // Envoyer les notifications selon les préférences
        if (alerte.isNotificationEmail()) {
            envoyerEmailAlerte(alerte, message);
        }
        
        if (alerte.isNotificationSMS()) {
            envoyerSMSAlerte(alerte, message);
        }
        
        if (alerte.isNotificationApplication()) {
            creerNotificationApplication(alerte, message);
        }
    }
    
    /**
     * Envoie un email d'alerte
     */
    private void envoyerEmailAlerte(AlerteConfig alerte, String message) {
        String sujet = "Alerte DPRIHKAT: " + alerte.getNom();
        String destinataire = alerte.getUtilisateur().getEmail();
        
        // TODO: Implémenter l'envoi d'email
        // emailService.sendEmail(destinataire, sujet, message);
    }
    
    /**
     * Envoie un SMS d'alerte
     */
    private void envoyerSMSAlerte(AlerteConfig alerte, String message) {
        // TODO: Implémenter l'envoi de SMS
    }
    
    /**
     * Crée une notification dans l'application
     */
    private void creerNotificationApplication(AlerteConfig alerte, String message) {
        // TODO: Implémenter la création de notification
    }
}
