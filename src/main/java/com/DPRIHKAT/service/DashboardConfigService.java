package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.DashboardConfig;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.WidgetConfig;
import com.DPRIHKAT.repository.DashboardConfigRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service pour gérer la configuration personnalisée des tableaux de bord
 */
@Service
public class DashboardConfigService {

    private final DashboardConfigRepository dashboardConfigRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ObjectMapper objectMapper;

    public DashboardConfigService(
            DashboardConfigRepository dashboardConfigRepository,
            UtilisateurRepository utilisateurRepository,
            ObjectMapper objectMapper) {
        this.dashboardConfigRepository = dashboardConfigRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Récupère la configuration du tableau de bord d'un utilisateur
     * Si aucune configuration n'existe, en crée une par défaut
     */
    @Transactional
    public DashboardConfig getConfigForUser(UUID utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        return dashboardConfigRepository.findByUtilisateur(utilisateur)
                .orElseGet(() -> createDefaultConfig(utilisateur));
    }
    
    /**
     * Crée une configuration par défaut pour un utilisateur
     */
    private DashboardConfig createDefaultConfig(Utilisateur utilisateur) {
        DashboardConfig config = new DashboardConfig(utilisateur);
        
        // Créer les configurations de widgets par défaut
        List<String> defaultWidgets = Arrays.asList(
            "statistiques", "taxations_mois", "paiements_mois", 
            "proprietes_type", "contribuables_type", "carte", 
            "utilisateurs", "logs", "taxations", "paiements"
        );
        
        int row = 0;
        int col = 0;
        
        for (String widgetId : defaultWidgets) {
            WidgetConfig widgetConfig = new WidgetConfig(config, widgetId);
            widgetConfig.setGridRow(row);
            widgetConfig.setGridColumn(col);
            
            // Définir la taille par défaut selon le type de widget
            if (widgetId.equals("statistiques") || widgetId.equals("carte")) {
                widgetConfig.setGridColumnSpan(2);
            }
            
            config.getWidgetConfigs().add(widgetConfig);
            
            // Passer à la colonne suivante ou à la ligne suivante
            col++;
            if (col > 1) {
                col = 0;
                row++;
            }
        }
        
        return dashboardConfigRepository.save(config);
    }
    
    /**
     * Met à jour la configuration du tableau de bord d'un utilisateur
     */
    @Transactional
    public DashboardConfig updateConfig(UUID utilisateurId, DashboardConfig updatedConfig) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        DashboardConfig existingConfig = dashboardConfigRepository.findByUtilisateur(utilisateur)
                .orElseGet(() -> new DashboardConfig(utilisateur));
        
        // Mettre à jour les propriétés de base
        existingConfig.setShowStatistiquesGenerales(updatedConfig.isShowStatistiquesGenerales());
        existingConfig.setShowTaxationsParMois(updatedConfig.isShowTaxationsParMois());
        existingConfig.setShowPaiementsParMois(updatedConfig.isShowPaiementsParMois());
        existingConfig.setShowProprieteParType(updatedConfig.isShowProprieteParType());
        existingConfig.setShowContribuablesParType(updatedConfig.isShowContribuablesParType());
        existingConfig.setShowCarte(updatedConfig.isShowCarte());
        existingConfig.setShowDerniersUtilisateurs(updatedConfig.isShowDerniersUtilisateurs());
        existingConfig.setShowLogsConnexion(updatedConfig.isShowLogsConnexion());
        existingConfig.setShowTaxationsRecentes(updatedConfig.isShowTaxationsRecentes());
        existingConfig.setShowPaiementsRecents(updatedConfig.isShowPaiementsRecents());
        
        existingConfig.setWidgetOrder(updatedConfig.getWidgetOrder());
        existingConfig.setLimitDerniersUtilisateurs(updatedConfig.getLimitDerniersUtilisateurs());
        existingConfig.setLimitLogsConnexion(updatedConfig.getLimitLogsConnexion());
        existingConfig.setLimitTaxationsRecentes(updatedConfig.getLimitTaxationsRecentes());
        existingConfig.setLimitPaiementsRecents(updatedConfig.getLimitPaiementsRecents());
        
        existingConfig.setFiltresPersonnalises(updatedConfig.getFiltresPersonnalises());
        existingConfig.setTheme(updatedConfig.getTheme());
        existingConfig.setColorScheme(updatedConfig.getColorScheme());
        existingConfig.setPeriodeGraphiqueMois(updatedConfig.getPeriodeGraphiqueMois());
        existingConfig.setAutoRefresh(updatedConfig.isAutoRefresh());
        existingConfig.setAutoRefreshInterval(updatedConfig.getAutoRefreshInterval());
        
        // Mettre à jour les configurations de widgets
        if (updatedConfig.getWidgetConfigs() != null && !updatedConfig.getWidgetConfigs().isEmpty()) {
            // Supprimer les anciennes configurations
            existingConfig.getWidgetConfigs().clear();
            
            // Ajouter les nouvelles configurations
            for (WidgetConfig widgetConfig : updatedConfig.getWidgetConfigs()) {
                WidgetConfig newWidgetConfig = new WidgetConfig(existingConfig, widgetConfig.getWidgetId());
                newWidgetConfig.setGridRow(widgetConfig.getGridRow());
                newWidgetConfig.setGridColumn(widgetConfig.getGridColumn());
                newWidgetConfig.setGridRowSpan(widgetConfig.getGridRowSpan());
                newWidgetConfig.setGridColumnSpan(widgetConfig.getGridColumnSpan());
                newWidgetConfig.setSize(widgetConfig.getSize());
                newWidgetConfig.setVisible(widgetConfig.isVisible());
                newWidgetConfig.setConfigSpecifique(widgetConfig.getConfigSpecifique());
                newWidgetConfig.setTitrePersonnalise(widgetConfig.getTitrePersonnalise());
                newWidgetConfig.setBackgroundColor(widgetConfig.getBackgroundColor());
                newWidgetConfig.setTypeGraphique(widgetConfig.getTypeGraphique());
                
                existingConfig.getWidgetConfigs().add(newWidgetConfig);
            }
        }
        
        return dashboardConfigRepository.save(existingConfig);
    }
    
    /**
     * Réinitialise la configuration du tableau de bord d'un utilisateur
     */
    @Transactional
    public DashboardConfig resetConfig(UUID utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Supprimer la configuration existante
        dashboardConfigRepository.findByUtilisateur(utilisateur)
                .ifPresent(dashboardConfigRepository::delete);
        
        // Créer une nouvelle configuration par défaut
        return createDefaultConfig(utilisateur);
    }
    
    /**
     * Applique un thème prédéfini à la configuration du tableau de bord
     */
    @Transactional
    public DashboardConfig applyTheme(UUID utilisateurId, String theme) {
        DashboardConfig config = getConfigForUser(utilisateurId);
        
        config.setTheme(theme);
        
        // Appliquer des paramètres spécifiques selon le thème
        switch (theme) {
            case "dark":
                config.setColorScheme("blue-dark");
                break;
            case "light":
                config.setColorScheme("blue-light");
                break;
            case "high-contrast":
                config.setColorScheme("high-contrast");
                break;
            default:
                config.setColorScheme("blue");
                break;
        }
        
        return dashboardConfigRepository.save(config);
    }
}
