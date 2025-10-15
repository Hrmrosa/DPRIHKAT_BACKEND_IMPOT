package com.DPRIHKAT.entity.enums;

/**
 * Types de redressement fiscal
 */
public enum TypeRedressement {
    REDRESSEMENT_SIMPLE("Redressement simple"),
    TAXATION_OFFICE("Taxation d'office"),
    AVIS_NON_LIEU("Avis de non-lieu");
    
    private final String libelle;
    
    TypeRedressement(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}
