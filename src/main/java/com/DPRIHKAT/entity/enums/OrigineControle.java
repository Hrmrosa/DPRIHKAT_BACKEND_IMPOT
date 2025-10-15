package com.DPRIHKAT.entity.enums;

/**
 * Origines possibles d'un contrôle fiscal
 */
public enum OrigineControle {
    CONTROLE_SUR_PIECES("Contrôle sur pièces"),
    RECHERCHE_RECOUPEMENT("Recherche et recoupement"),
    CONTROLE_SUR_PLACE("Contrôle sur place"),
    DECLARATION_SPONTANEE("Déclaration spontanée");
    
    private final String libelle;
    
    OrigineControle(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}
