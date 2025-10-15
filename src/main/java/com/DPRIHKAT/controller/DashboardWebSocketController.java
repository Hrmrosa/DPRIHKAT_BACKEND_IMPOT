package com.DPRIHKAT.controller;

import com.DPRIHKAT.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur WebSocket pour les mises à jour en temps réel du dashboard
 */
@Controller
public class DashboardWebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardWebSocketController.class);
    
    private final DashboardService dashboardService;
    private final SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    public DashboardWebSocketController(DashboardService dashboardService, SimpMessagingTemplate messagingTemplate) {
        this.dashboardService = dashboardService;
        this.messagingTemplate = messagingTemplate;
    }
    
    /**
     * Point d'entrée pour les requêtes de mise à jour du dashboard
     * Les clients peuvent envoyer un message à /app/dashboard/refresh pour demander une mise à jour
     * La réponse sera envoyée à tous les clients abonnés à /topic/dashboard
     */
    @MessageMapping("/dashboard/refresh")
    @SendTo("/topic/dashboard")
    public Map<String, Object> refreshDashboard() {
        logger.info("Requête de rafraîchissement du dashboard reçue via WebSocket");
        Map<String, Object> response = new HashMap<>();
        response.put("data", dashboardService.getDashboardData());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    /**
     * Méthode pour envoyer des mises à jour aux clients connectés
     * Cette méthode est appelée par le service de notification
     */
    public void sendDashboardUpdate(Map<String, Object> dashboardData) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", dashboardData);
        response.put("timestamp", System.currentTimeMillis());
        
        logger.info("Envoi d'une mise à jour du dashboard via WebSocket");
        messagingTemplate.convertAndSend("/topic/dashboard", response);
    }
    
    /**
     * Méthode pour envoyer des mises à jour spécifiques aux clients connectés
     * Cette méthode est appelée par le service de notification
     */
    public void sendSpecificUpdate(String updateType, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("type", updateType);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        
        logger.info("Envoi d'une mise à jour spécifique ({}) via WebSocket", updateType);
        messagingTemplate.convertAndSend("/topic/updates/" + updateType, response);
    }
}
