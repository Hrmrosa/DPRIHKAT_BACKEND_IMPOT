package com.DPRIHKAT.entity.enums;

public enum ResultatControle {
    AVIS_NON_LIEU,              // Absence d'anomalies ou renseignements satisfaisants
    AVIS_REDRESSEMENT,          // Présence d'anomalies avec redressement
    TAXATION_OFFICE,            // Taxation d'office (absence déclaration, défaut pièces, opposition)
    REGULARISATION_SPONTANEE    // Régularisation avant mise en demeure
}
