package com.DPRIHKAT.entity.enums;

/**
 * Types de documents utilisés dans le processus de recouvrement
 */
public enum TypeDocumentRecouvrement {
    AMR("Avis de Mise en Recouvrement"),
    MED("Mise En Demeure de payer"),
    COMMANDEMENT("Commandement de payer"),
    CONTRAINTE("Contrainte fiscale"),
    ATD("Avis à Tiers Détenteur"),
    SAISIE_MOBILIERE("Saisie mobilière"),
    SAISIE_IMMOBILIERE("Saisie immobilière"),
    FERMETURE_ETABLISSEMENT("Fermeture d'établissement"),
    AVIS_NON_LIEU("Avis de non-lieu"),
    AVIS_REDRESSEMENT("Avis de redressement");
    
    private final String libelle;
    
    TypeDocumentRecouvrement(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}
