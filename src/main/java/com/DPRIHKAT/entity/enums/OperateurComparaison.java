package com.DPRIHKAT.entity.enums;

/**
 * Opérateurs de comparaison pour les alertes
 */
public enum OperateurComparaison {
    SUPERIEUR(">"),
    INFERIEUR("<"),
    EGAL("="),
    SUPERIEUR_EGAL(">="),
    INFERIEUR_EGAL("<=");
    
    private final String symbole;
    
    OperateurComparaison(String symbole) {
        this.symbole = symbole;
    }
    
    public String getSymbole() {
        return symbole;
    }
}
