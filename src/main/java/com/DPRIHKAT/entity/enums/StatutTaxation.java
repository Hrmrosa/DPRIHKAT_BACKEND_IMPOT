package com.DPRIHKAT.entity.enums;

/**
 * Énumération des statuts possibles pour une taxation
 * 
 * @author amateur
 */
public enum StatutTaxation {
    SOUMISE,    // La taxation a été soumise mais pas encore validée
    VALIDEE,    // La taxation a été validée par un agent habilité
    PAYEE,      // La taxation a été payée
    APUREE,     // La taxation a été apurée
    ANNULEE     // La taxation a été annulée
}
