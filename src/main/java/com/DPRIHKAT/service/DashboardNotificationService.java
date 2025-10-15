package com.DPRIHKAT.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service pour envoyer des notifications en temps réel au dashboard
 */
@Service
public class DashboardNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardNotificationService.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final DashboardService dashboardService;

    @Autowired
    public DashboardNotificationService(SimpMessagingTemplate messagingTemplate, DashboardService dashboardService) {
        this.messagingTemplate = messagingTemplate;
        this.dashboardService = dashboardService;
    }

    /**
     * Notifie les clients connectés d'une mise à jour des statistiques du dashboard
     */
    public void notifyDashboardUpdate() {
        try {
            logger.info("Envoi d'une notification de mise à jour du dashboard");
            
            // Récupérer les données mises à jour du dashboard
            Map<String, Object> dashboardData = dashboardService.getDashboardData();
            
            // Créer la réponse avec un timestamp
            Map<String, Object> response = new HashMap<>();
            response.put("data", dashboardData);
            response.put("timestamp", System.currentTimeMillis());
            
            // Envoyer les données mises à jour à tous les clients abonnés au topic
            messagingTemplate.convertAndSend("/topic/dashboard", response);
            
            logger.info("Notification de mise à jour du dashboard envoyée avec succès");
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de la notification de mise à jour du dashboard", e);
        }
    }
    
    /**
     * Notifie les clients connectés d'une mise à jour spécifique (contribuable, déclaration, paiement, etc.)
     * 
     * @param updateType Type de mise à jour (contribuable, declaration, paiement, etc.)
     * @param data Données mises à jour
     */
    public void notifySpecificUpdate(String updateType, Object data) {
        try {
            logger.info("Envoi d'une notification de mise à jour spécifique: {}", updateType);
            
            Map<String, Object> response = new HashMap<>();
            response.put("type", updateType);
            response.put("data", data);
            response.put("timestamp", System.currentTimeMillis());
            
            // Envoyer les données mises à jour à tous les clients abonnés au topic spécifique
            messagingTemplate.convertAndSend("/topic/updates/" + updateType, response);
            
            // Déclencher également une mise à jour complète du dashboard
            notifyDashboardUpdate();
            
            logger.info("Notification de mise à jour spécifique envoyée avec succès");
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de la notification de mise à jour spécifique", e);
        }
    }
}
