package com.DPRIHKAT.entity.enums;

/**
 * Énumération des types de rapports disponibles dans le système
 * 
 * @author amateur
 */
public enum TypeRapport {
    TAXATION("Rapport sur les taxations"),
    PAIEMENT("Rapport sur les paiements"),
    RELANCE("Rapport sur les relances"),
    COLLECTE("Rapport sur la collecte de données"),
    RECOUVREMENT("Rapport sur les actes de recouvrement"),
    GLOBAL("Rapport global (tous les types)");
    
    private final String description;
    
    TypeRapport(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
