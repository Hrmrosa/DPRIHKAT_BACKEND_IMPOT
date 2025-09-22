package com.DPRIHKAT.entity.enums;

/**
 * Enumération des statuts possibles pour une plaque d'immatriculation
 * 
 * @author amateur
 */
public enum StatutPlaque {
    /**
     * La plaque est en stock et n'a pas encore été attribuée
     */
    STOCK,
    
    /**
     * La plaque a été attribuée à un véhicule mais n'a pas encore été livrée
     */
    ATTRIBUEE,
    
    /**
     * La plaque a été livrée au contribuable
     */
    LIVREE
}
