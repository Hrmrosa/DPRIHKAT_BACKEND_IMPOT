package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.Paiement;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import com.DPRIHKAT.repository.DeclarationRepository;
import com.DPRIHKAT.repository.PaiementRepository;
import com.DPRIHKAT.repository.ProprieteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CollecteService {

    @Autowired
    private DeclarationRepository declarationRepository;

    @Autowired
    private PaiementRepository paiementRepository;
    
    @Autowired
    private ProprieteRepository proprieteRepository;

    /**
     * Enrichit une liste de propriétés avec les informations de déclaration et de paiement
     * 
     * @param proprietes Liste des propriétés à enrichir
     * @return Liste des propriétés enrichies avec les informations de déclaration et paiement
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> enrichirProprietesAvecDeclarations(List<Propriete> proprietes) {
        List<Map<String, Object>> proprietesEnrichies = new ArrayList<>();
        
        for (Propriete propriete : proprietes) {
            Map<String, Object> proprieteEnrichie = new HashMap<>();
            proprieteEnrichie.put("propriete", propriete);
            
            // Récupérer les déclarations associées à cette propriété
            List<Declaration> declarations = propriete.getDeclarations();
            
            // Initialiser les flags
            boolean aDeclare = !declarations.isEmpty();
            boolean paiementComplet = false;
            boolean paiementPartiel = false;
            
            // Liste pour stocker les informations des déclarations et paiements
            List<Map<String, Object>> declarationsInfo = new ArrayList<>();
            
            for (Declaration declaration : declarations) {
                Map<String, Object> declarationInfo = new HashMap<>();
                declarationInfo.put("id", declaration.getId());
                declarationInfo.put("date", declaration.getDateDeclaration());
                declarationInfo.put("statut", declaration.getStatut());
                
                // Récupérer le paiement associé à cette déclaration
                Paiement paiement = declaration.getPaiement();
                if (paiement != null) {
                    Map<String, Object> paiementInfo = new HashMap<>();
                    paiementInfo.put("id", paiement.getId());
                    paiementInfo.put("statut", paiement.getStatut());
                    paiementInfo.put("montant", paiement.getMontant());
                    
                    // Vérifier le statut du paiement
                    if (paiement.getStatut() == StatutPaiement.COMPLETE) {
                        paiementComplet = true;
                    } else if (paiement.getStatut() == StatutPaiement.PARTIEL) {
                        paiementPartiel = true;
                    }
                    
                    declarationInfo.put("paiement", paiementInfo);
                }
                
                declarationsInfo.add(declarationInfo);
            }
            
            // Ajouter les informations de déclaration et paiement à la propriété enrichie
            proprieteEnrichie.put("aDeclare", aDeclare);
            proprieteEnrichie.put("paiementComplet", paiementComplet);
            proprieteEnrichie.put("paiementPartiel", paiementPartiel);
            proprieteEnrichie.put("declarations", declarationsInfo);
            
            proprietesEnrichies.add(proprieteEnrichie);
        }
        
        return proprietesEnrichies;
    }
    
    /**
     * Récupère et enrichit les propriétés d'un contribuable spécifique
     * 
     * @param contribuableId ID du contribuable
     * @return Liste des propriétés enrichies avec les informations de déclaration et paiement
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getProprietesEnrichiesParContribuable(UUID contribuableId) {
        List<Propriete> proprietes = proprieteRepository.findByProprietaire_Id(contribuableId);
        return enrichirProprietesAvecDeclarations(proprietes);
    }
}
