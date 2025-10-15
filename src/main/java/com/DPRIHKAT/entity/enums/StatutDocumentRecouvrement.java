package com.DPRIHKAT.entity.enums;

/**
 * Statuts possibles pour les documents de recouvrement
 */
public enum StatutDocumentRecouvrement {
    GENERE("Généré"),
    NOTIFIE("Notifié"),
    EN_COURS_EXECUTION("En cours d'exécution"),
    EXECUTE("Exécuté"),
    NON_EXECUTE("Non exécuté"),
    ANNULE("Annulé");
    
    private final String libelle;
    
    StatutDocumentRecouvrement(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}
