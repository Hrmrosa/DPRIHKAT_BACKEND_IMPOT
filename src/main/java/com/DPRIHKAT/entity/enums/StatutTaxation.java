package com.DPRIHKAT.entity.enums;

/**
 * Énumération des statuts possibles pour une taxation
 * @author amateur
 */
public enum StatutTaxation {
    EN_ATTENTE,      // Taxation créée mais pas encore validée
    VALIDEE,         // Taxation validée par un agent
    PAYEE,           // Taxation payée par le contribuable
    PAYEE_PARTIELLEMENT, // Taxation partiellement payée
    ANNULEE,         // Taxation annulée
    EXONEREE,        // Taxation avec exonération accordée
    APUREE           // Taxation apurée (régularisée)
}
