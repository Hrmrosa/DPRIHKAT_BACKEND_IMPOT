package com.DPRIHKAT.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Configuration personnalisée du tableau de bord pour un utilisateur
 */
@Entity
public class DashboardConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    // Widgets activés/désactivés
    private boolean showStatistiquesGenerales = true;
    private boolean showTaxationsParMois = true;
    private boolean showPaiementsParMois = true;
    private boolean showProprieteParType = true;
    private boolean showContribuablesParType = true;
    private boolean showCarte = true;
    private boolean showDerniersUtilisateurs = true;
    private boolean showLogsConnexion = true;
    private boolean showTaxationsRecentes = true;
    private boolean showPaiementsRecents = true;

    // Ordre d'affichage des widgets (liste d'identifiants de widgets)
    @Column(length = 1000)
    private String widgetOrder;

    // Nombre d'éléments à afficher dans les listes
    private int limitDerniersUtilisateurs = 10;
    private int limitLogsConnexion = 20;
    private int limitTaxationsRecentes = 10;
    private int limitPaiementsRecents = 10;

    // Filtres personnalisés (stockés au format JSON)
    @Column(columnDefinition = "TEXT")
    private String filtresPersonnalises;

    // Thème et préférences visuelles
    private String theme = "default";
    private String colorScheme = "blue";
    
    // Période par défaut pour les graphiques
    private int periodeGraphiqueMois = 12;
    
    // Rafraîchissement automatique
    private boolean autoRefresh = false;
    private int autoRefreshInterval = 5; // minutes
    
    @OneToMany(mappedBy = "dashboardConfig", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WidgetConfig> widgetConfigs = new ArrayList<>();

    public DashboardConfig() {
    }

    public DashboardConfig(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.widgetOrder = "statistiques,taxations_mois,paiements_mois,proprietes_type,contribuables_type,carte,utilisateurs,logs,taxations,paiements";
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

    public boolean isShowStatistiquesGenerales() {
        return showStatistiquesGenerales;
    }

    public void setShowStatistiquesGenerales(boolean showStatistiquesGenerales) {
        this.showStatistiquesGenerales = showStatistiquesGenerales;
    }

    public boolean isShowTaxationsParMois() {
        return showTaxationsParMois;
    }

    public void setShowTaxationsParMois(boolean showTaxationsParMois) {
        this.showTaxationsParMois = showTaxationsParMois;
    }

    public boolean isShowPaiementsParMois() {
        return showPaiementsParMois;
    }

    public void setShowPaiementsParMois(boolean showPaiementsParMois) {
        this.showPaiementsParMois = showPaiementsParMois;
    }

    public boolean isShowProprieteParType() {
        return showProprieteParType;
    }

    public void setShowProprieteParType(boolean showProprieteParType) {
        this.showProprieteParType = showProprieteParType;
    }

    public boolean isShowContribuablesParType() {
        return showContribuablesParType;
    }

    public void setShowContribuablesParType(boolean showContribuablesParType) {
        this.showContribuablesParType = showContribuablesParType;
    }

    public boolean isShowCarte() {
        return showCarte;
    }

    public void setShowCarte(boolean showCarte) {
        this.showCarte = showCarte;
    }

    public boolean isShowDerniersUtilisateurs() {
        return showDerniersUtilisateurs;
    }

    public void setShowDerniersUtilisateurs(boolean showDerniersUtilisateurs) {
        this.showDerniersUtilisateurs = showDerniersUtilisateurs;
    }

    public boolean isShowLogsConnexion() {
        return showLogsConnexion;
    }

    public void setShowLogsConnexion(boolean showLogsConnexion) {
        this.showLogsConnexion = showLogsConnexion;
    }

    public boolean isShowTaxationsRecentes() {
        return showTaxationsRecentes;
    }

    public void setShowTaxationsRecentes(boolean showTaxationsRecentes) {
        this.showTaxationsRecentes = showTaxationsRecentes;
    }

    public boolean isShowPaiementsRecents() {
        return showPaiementsRecents;
    }

    public void setShowPaiementsRecents(boolean showPaiementsRecents) {
        this.showPaiementsRecents = showPaiementsRecents;
    }

    public String getWidgetOrder() {
        return widgetOrder;
    }

    public void setWidgetOrder(String widgetOrder) {
        this.widgetOrder = widgetOrder;
    }

    public int getLimitDerniersUtilisateurs() {
        return limitDerniersUtilisateurs;
    }

    public void setLimitDerniersUtilisateurs(int limitDerniersUtilisateurs) {
        this.limitDerniersUtilisateurs = limitDerniersUtilisateurs;
    }

    public int getLimitLogsConnexion() {
        return limitLogsConnexion;
    }

    public void setLimitLogsConnexion(int limitLogsConnexion) {
        this.limitLogsConnexion = limitLogsConnexion;
    }

    public int getLimitTaxationsRecentes() {
        return limitTaxationsRecentes;
    }

    public void setLimitTaxationsRecentes(int limitTaxationsRecentes) {
        this.limitTaxationsRecentes = limitTaxationsRecentes;
    }

    public int getLimitPaiementsRecents() {
        return limitPaiementsRecents;
    }

    public void setLimitPaiementsRecents(int limitPaiementsRecents) {
        this.limitPaiementsRecents = limitPaiementsRecents;
    }

    public String getFiltresPersonnalises() {
        return filtresPersonnalises;
    }

    public void setFiltresPersonnalises(String filtresPersonnalises) {
        this.filtresPersonnalises = filtresPersonnalises;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(String colorScheme) {
        this.colorScheme = colorScheme;
    }

    public int getPeriodeGraphiqueMois() {
        return periodeGraphiqueMois;
    }

    public void setPeriodeGraphiqueMois(int periodeGraphiqueMois) {
        this.periodeGraphiqueMois = periodeGraphiqueMois;
    }

    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public int getAutoRefreshInterval() {
        return autoRefreshInterval;
    }

    public void setAutoRefreshInterval(int autoRefreshInterval) {
        this.autoRefreshInterval = autoRefreshInterval;
    }

    public List<WidgetConfig> getWidgetConfigs() {
        return widgetConfigs;
    }

    public void setWidgetConfigs(List<WidgetConfig> widgetConfigs) {
        this.widgetConfigs = widgetConfigs;
    }
}
