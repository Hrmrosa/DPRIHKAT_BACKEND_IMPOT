package com.DPRIHKAT.entity.enums;

/**
 * Enumération des statuts possibles pour un véhicule dans le processus d'immatriculation
 * 
 * @author amateur
 */
public enum StatutVehicule {
    /**
     * Le véhicule est enregistré mais pas encore taxé
     */
    ENREGISTRE,
    
    /**
     * Le véhicule a été taxé mais le paiement n'est pas encore effectué
     */
    TAXE,
    
    /**
     * Le paiement a été effectué, en attente d'attribution de plaque
     */
    PAYE,
    
    /**
     * Une plaque a été attribuée au véhicule
     */
    PLAQUE_ATTRIBUEE,
    
    /**
     * La plaque a été livrée au propriétaire
     */
    ACTIF,
    
    /**
     * Le véhicule est suspendu (non conforme, problème administratif, etc.)
     */
    SUSPENDU,
    
    /**
     * Le véhicule est radié (vendu, détruit, exporté, etc.)
     */
    RADIE
}
