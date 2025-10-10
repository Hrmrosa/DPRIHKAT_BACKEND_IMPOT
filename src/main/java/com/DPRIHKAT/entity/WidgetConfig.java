package com.DPRIHKAT.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.UUID;

/**
 * Configuration d'un widget spécifique dans le tableau de bord
 */
@Entity
public class WidgetConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "dashboard_config_id")
    @JsonIgnore
    private DashboardConfig dashboardConfig;
    
    // Identifiant du widget (statistiques, taxations_mois, etc.)
    private String widgetId;
    
    // Position du widget dans la grille
    private int gridRow;
    private int gridColumn;
    private int gridRowSpan = 1;
    private int gridColumnSpan = 1;
    
    // Taille du widget
    private String size = "medium"; // small, medium, large
    
    // Visibilité du widget
    private boolean visible = true;
    
    // Configuration spécifique au widget (stockée au format JSON)
    @Column(columnDefinition = "TEXT")
    private String configSpecifique;
    
    // Titre personnalisé du widget
    private String titrePersonnalise;
    
    // Couleur d'arrière-plan
    private String backgroundColor;
    
    // Type de graphique (pour les widgets de type graphique)
    private String typeGraphique; // bar, line, pie, etc.
    
    public WidgetConfig() {
    }
    
    public WidgetConfig(DashboardConfig dashboardConfig, String widgetId) {
        this.dashboardConfig = dashboardConfig;
        this.widgetId = widgetId;
    }

    // Getters and Setters
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DashboardConfig getDashboardConfig() {
        return dashboardConfig;
    }

    public void setDashboardConfig(DashboardConfig dashboardConfig) {
        this.dashboardConfig = dashboardConfig;
    }

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

    public int getGridRow() {
        return gridRow;
    }

    public void setGridRow(int gridRow) {
        this.gridRow = gridRow;
    }

    public int getGridColumn() {
        return gridColumn;
    }

    public void setGridColumn(int gridColumn) {
        this.gridColumn = gridColumn;
    }

    public int getGridRowSpan() {
        return gridRowSpan;
    }

    public void setGridRowSpan(int gridRowSpan) {
        this.gridRowSpan = gridRowSpan;
    }

    public int getGridColumnSpan() {
        return gridColumnSpan;
    }

    public void setGridColumnSpan(int gridColumnSpan) {
        this.gridColumnSpan = gridColumnSpan;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getConfigSpecifique() {
        return configSpecifique;
    }

    public void setConfigSpecifique(String configSpecifique) {
        this.configSpecifique = configSpecifique;
    }

    public String getTitrePersonnalise() {
        return titrePersonnalise;
    }

    public void setTitrePersonnalise(String titrePersonnalise) {
        this.titrePersonnalise = titrePersonnalise;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTypeGraphique() {
        return typeGraphique;
    }

    public void setTypeGraphique(String typeGraphique) {
        this.typeGraphique = typeGraphique;
    }
}
