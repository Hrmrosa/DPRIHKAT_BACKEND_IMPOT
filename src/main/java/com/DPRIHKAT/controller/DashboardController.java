package com.DPRIHKAT.controller;

import com.DPRIHKAT.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

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
            Map<String, Object> dashboardData = dashboardService.getAdminDashboardData();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", dashboardData
                    ));
        } catch (Exception e) {
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
            Map<String, Object> dashboardData = dashboardService.getDirecteurDashboardData();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", dashboardData
                    ));
        } catch (Exception e) {
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
            Map<String, Object> dashboardData = dashboardService.getChefBureauDashboardData();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", dashboardData
                    ));
        } catch (Exception e) {
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
            Map<String, Object> dashboardData = dashboardService.getTaxateurDashboardData();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", dashboardData
                    ));
        } catch (Exception e) {
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
            Map<String, Object> dashboardData = dashboardService.getContribuableDashboardData();
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "data", dashboardData
                    ));
        } catch (Exception e) {
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
}
