package com.DPRIHKAT.controller;

import com.DPRIHKAT.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Dashboard pour les administrateurs
     * Fournit une vue complète du système avec toutes les statistiques
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminDashboard() {
        try {
            logger.info("Récupération du dashboard administrateur");
            Map<String, Object> dashboardData = dashboardService.getAdminDashboardData();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", dashboardData
                    ));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du dashboard administrateur", e);
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", Map.of(
                                    "code", "DASHBOARD_FETCH_ERROR",
                                    "message", "Erreur lors de la récupération des données du tableau de bord",
                                    "details", e.getMessage()
                            )
                    ));
        }
    }

    /**
     * Dashboard pour les directeurs
     * Fournit une vue d'ensemble avec les statistiques principales
     */
    @GetMapping("/directeur")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<?> getDirecteurDashboard() {
        try {
            logger.info("Récupération du dashboard directeur");
            Map<String, Object> dashboardData = dashboardService.getDirecteurDashboardData();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", dashboardData
                    ));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du dashboard directeur", e);
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", Map.of(
                                    "code", "DASHBOARD_FETCH_ERROR",
                                    "message", "Erreur lors de la récupération des données du tableau de bord",
                                    "details", e.getMessage()
                            )
                    ));
        }
    }

    /**
     * Dashboard pour les chefs de bureau
     */
    @GetMapping("/chef-bureau")
    @PreAuthorize("hasRole('CHEF_DE_BUREAU')")
    public ResponseEntity<?> getChefBureauDashboard() {
        try {
            logger.info("Récupération du dashboard chef de bureau");
            Map<String, Object> dashboardData = dashboardService.getChefBureauDashboardData();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", dashboardData
                    ));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du dashboard chef de bureau", e);
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", Map.of(
                                    "code", "DASHBOARD_FETCH_ERROR",
                                    "message", "Erreur lors de la récupération des données du tableau de bord",
                                    "details", e.getMessage()
                            )
                    ));
        }
    }

    /**
     * Dashboard pour les taxateurs
     */
    @GetMapping("/taxateur")
    @PreAuthorize("hasRole('TAXATEUR')")
    public ResponseEntity<?> getTaxateurDashboard() {
        try {
            logger.info("Récupération du dashboard taxateur");
            Map<String, Object> dashboardData = dashboardService.getTaxateurDashboardData();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", dashboardData
                    ));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du dashboard taxateur", e);
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", Map.of(
                                    "code", "DASHBOARD_FETCH_ERROR",
                                    "message", "Erreur lors de la récupération des données du tableau de bord",
                                    "details", e.getMessage()
                            )
                    ));
        }
    }

    /**
     * Dashboard pour les contribuables
     */
    @GetMapping("/contribuable")
    @PreAuthorize("hasRole('CONTRIBUABLE')")
    public ResponseEntity<?> getContribuableDashboard() {
        try {
            logger.info("Récupération du dashboard contribuable");
            Map<String, Object> dashboardData = dashboardService.getContribuableDashboardData();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", dashboardData
                    ));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du dashboard contribuable", e);
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", Map.of(
                                    "code", "DASHBOARD_FETCH_ERROR",
                                    "message", "Erreur lors de la récupération des données du tableau de bord",
                                    "details", e.getMessage()
                            )
                    ));
        }
    }
    
    /**
     * Endpoint pour récupérer les données en temps réel du dashboard
     * Accessible par tous les utilisateurs authentifiés
     * Renvoie les données adaptées au rôle de l'utilisateur
     */
    @GetMapping("/realtime-data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getRealtimeDashboardData() {
        try {
            logger.info("Récupération des données en temps réel du dashboard");
            Map<String, Object> dashboardData = dashboardService.getDashboardData();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", dashboardData,
                            "timestamp", System.currentTimeMillis()
                    ));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des données en temps réel du dashboard", e);
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", Map.of(
                                    "code", "DASHBOARD_REALTIME_ERROR",
                                    "message", "Erreur lors de la récupération des données en temps réel",
                                    "details", e.getMessage()
                            )
                    ));
        }
    }
}
