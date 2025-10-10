package com.DPRIHKAT.entity.enums;

/**
 * Périodes de prédiction disponibles pour les analyses prédictives
 */
public enum PeriodePrediction {
    MOIS_1("1 mois"),
    MOIS_3("3 mois"),
    MOIS_6("6 mois"),
    ANNEE_1("1 an"),
    ANNEE_2("2 ans"),
    ANNEE_5("5 ans");
    
    private final String libelle;
    
    PeriodePrediction(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}
