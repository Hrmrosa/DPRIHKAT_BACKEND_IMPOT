package com.DPRIHKAT.entity.enums;

/**
 * Énumération des périodes de rapport disponibles
 * 
 * @author amateur
 */
public enum PeriodeRapport {
    JOUR("Journalier"),
    SEMAINE("Hebdomadaire"),
    MOIS("Mensuel"),
    TRIMESTRE("Trimestriel"),
    SEMESTRE("Semestriel"),
    ANNEE("Annuel"),
    PERSONNALISEE("Période personnalisée");
    
    private final String description;
    
    PeriodeRapport(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
