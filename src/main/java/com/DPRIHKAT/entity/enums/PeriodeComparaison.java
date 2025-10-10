package com.DPRIHKAT.entity.enums;

/**
 * Périodes de comparaison disponibles
 */
public enum PeriodeComparaison {
    ANNEE_PRECEDENTE("Année précédente"),
    ANNEE_EN_COURS("Année en cours"),
    TRIMESTRE_PRECEDENT("Trimestre précédent"),
    TRIMESTRE_EN_COURS("Trimestre en cours"),
    MOIS_PRECEDENT("Mois précédent"),
    MOIS_EN_COURS("Mois en cours"),
    SEMAINE_PRECEDENTE("Semaine précédente"),
    SEMAINE_EN_COURS("Semaine en cours"),
    PERSONNALISEE("Période personnalisée");
    
    private final String libelle;
    
    PeriodeComparaison(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}
