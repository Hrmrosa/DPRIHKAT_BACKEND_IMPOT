package com.DPRIHKAT.entity.enums;

/**
 * Enumération des statuts possibles pour une demande de plaque
 * 
 * @author amateur
 */
public enum StatutDemande {
    /**
     * La demande a été soumise par le contribuable mais n'a pas encore été traitée
     */
    SOUMISE,
    
    /**
     * La demande a été validée par un taxateur ou un administrateur
     */
    VALIDEE,
    
    /**
     * Une note de taxation a été générée pour la demande
     */
    TAXEE,
    
    /**
     * Le paiement a été effectué pour la demande
     */
    PAYEE,
    
    /**
     * La demande a été apurée (vérifiée et approuvée pour livraison)
     */
    APUREE,
    
    /**
     * La plaque a été livrée au contribuable
     */
    LIVREE,
    
    /**
     * La demande a été rejetée
     */
    REJETEE
}
