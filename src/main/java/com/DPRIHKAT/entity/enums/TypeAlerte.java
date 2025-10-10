package com.DPRIHKAT.entity.enums;

/**
 * Types d'alertes disponibles dans le système
 */
public enum TypeAlerte {
    TAXATION_MONTANT,           // Alerte sur le montant des taxations
    PAIEMENT_MONTANT,           // Alerte sur le montant des paiements
    TAUX_RECOUVREMENT,          // Alerte sur le taux de recouvrement
    NOMBRE_CONTRIBUABLES,       // Alerte sur le nombre de contribuables
    NOMBRE_PROPRIETES,          // Alerte sur le nombre de propriétés
    NOMBRE_VEHICULES,           // Alerte sur le nombre de véhicules
    CONNEXIONS_UTILISATEURS,    // Alerte sur le nombre de connexions utilisateurs
    DECLARATIONS_EN_LIGNE,      // Alerte sur le nombre de déclarations en ligne
    ECHEANCE_PAIEMENT,          // Alerte sur les échéances de paiement
    PERFORMANCE_SYSTEME         // Alerte sur les performances du système
}
