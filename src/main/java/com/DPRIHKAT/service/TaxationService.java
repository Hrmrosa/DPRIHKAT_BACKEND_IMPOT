package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.DPRIHKAT.repository.DeclarationRepository;
import com.DPRIHKAT.repository.NatureImpotRepository;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.repository.TaxationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Service pour gérer les taxations des biens déclarés
 * @author amateur
 */
@Service
public class TaxationService {

    private static final Logger logger = LoggerFactory.getLogger(TaxationService.class);

    @Autowired
    private TaxationRepository taxationRepository;
    
    @Autowired
    private ProprieteRepository proprieteRepository;
    
    @Autowired
    private DeclarationRepository declarationRepository;
    
    @Autowired
    private NatureImpotRepository natureImpotRepository;

    /**
     * Récupère toutes les taxations
     * @return Liste de toutes les taxations
     */
    public List<Taxation> getAllTaxations() {
        return taxationRepository.findAll();
    }
    
    /**
     * Récupère toutes les taxations avec pagination
     * @param pageable Informations de pagination
     * @return Page de taxations
     */
    public Page<Taxation> getAllTaxationsPaginated(Pageable pageable) {
        return taxationRepository.findAll(pageable);
    }

    /**
     * Récupère toutes les taxations actives
     * @return Liste des taxations actives
     */
    public List<Taxation> getAllActiveTaxations() {
        return taxationRepository.findByActifTrue();
    }
    
    /**
     * Récupère toutes les taxations actives avec pagination
     * @param pageable Informations de pagination
     * @return Page de taxations actives
     */
    public Page<Taxation> getAllActiveTaxationsPaginated(Pageable pageable) {
        return taxationRepository.findByActifTrue(pageable);
    }

    /**
     * Récupère une taxation par son ID
     * @param id L'ID de la taxation
     * @return La taxation correspondante, si elle existe
     */
    public Optional<Taxation> getTaxationById(UUID id) {
        return taxationRepository.findById(id);
    }

    /**
     * Récupère toutes les taxations pour une propriété donnée
     * @param proprieteId L'ID de la propriété
     * @return Liste des taxations pour cette propriété
     */
    public List<Taxation> getTaxationsByProprieteId(UUID proprieteId) {
        return proprieteRepository.findById(proprieteId)
                .map(propriete -> {
                    List<Declaration> declarations = propriete.getDeclarations();
                    List<Taxation> taxations = new ArrayList<>();
                    for (Declaration declaration : declarations) {
                        taxations.addAll(taxationRepository.findByDeclarationAndActifTrue(declaration));
                    }
                    return taxations;
                })
                .orElse(Collections.emptyList());
    }
    
    /**
     * Récupère toutes les taxations pour une propriété donnée avec pagination
     * @param proprieteId L'ID de la propriété
     * @param pageable Informations de pagination
     * @return Page de taxations pour cette propriété
     */
    public Page<Taxation> getTaxationsByProprieteIdPaginated(UUID proprieteId, Pageable pageable) {
        return proprieteRepository.findById(proprieteId)
                .map(propriete -> {
                    List<Declaration> declarations = propriete.getDeclarations();
                    List<Taxation> taxations = new ArrayList<>();
                    for (Declaration declaration : declarations) {
                        taxations.addAll(taxationRepository.findByDeclarationAndActifTrue(declaration));
                    }
                    // Convertir la liste en page
                    int start = (int) pageable.getOffset();
                    int end = Math.min((start + pageable.getPageSize()), taxations.size());
                    return new PageImpl<>(taxations.subList(start, end), pageable, taxations.size());
                })
                .orElse(new PageImpl<>(Collections.emptyList(), pageable, 0));
    }

    /**
     * Récupère toutes les taxations pour une déclaration donnée
     * @param declarationId L'ID de la déclaration
     * @return Liste des taxations pour cette déclaration
     */
    public List<Taxation> getTaxationsByDeclarationId(UUID declarationId) {
        return declarationRepository.findById(declarationId)
                .map(declaration -> taxationRepository.findByDeclarationAndActifTrue(declaration))
                .orElse(Collections.emptyList());
    }
    
    /**
     * Récupère toutes les taxations pour une déclaration donnée avec pagination
     * @param declarationId L'ID de la déclaration
     * @param pageable Informations de pagination
     * @return Page de taxations pour cette déclaration
     */
    public Page<Taxation> getTaxationsByDeclarationIdPaginated(UUID declarationId, Pageable pageable) {
        return declarationRepository.findById(declarationId)
                .map(declaration -> taxationRepository.findByDeclarationAndActifTrue(declaration, pageable))
                .orElse(new PageImpl<>(Collections.emptyList(), pageable, 0));
    }

    /**
     * Récupère toutes les taxations pour un exercice donné
     * @param exercice L'exercice (année fiscale)
     * @return Liste des taxations pour cet exercice
     */
    public List<Taxation> getTaxationsByExercice(String exercice) {
        return taxationRepository.findByExerciceAndActifTrue(exercice);
    }
    
    /**
     * Récupère toutes les taxations pour un exercice donné avec pagination
     * @param exercice L'exercice (année fiscale)
     * @param pageable Informations de pagination
     * @return Page de taxations pour cet exercice
     */
    public Page<Taxation> getTaxationsByExercicePaginated(String exercice, Pageable pageable) {
        return taxationRepository.findByExerciceAndActifTrue(exercice, pageable);
    }

    /**
     * Récupère toutes les taxations pour un type d'impôt donné
     * @param typeImpot Le type d'impôt
     * @return Liste des taxations pour ce type d'impôt
     */
    public List<Taxation> getTaxationsByTypeImpot(TypeImpot typeImpot) {
        return taxationRepository.findByTypeImpotAndActifTrue(typeImpot);
    }
    
    /**
     * Récupère toutes les taxations pour un type d'impôt donné avec pagination
     * @param typeImpot Le type d'impôt
     * @param pageable Informations de pagination
     * @return Page de taxations pour ce type d'impôt
     */
    public Page<Taxation> getTaxationsByTypeImpotPaginated(TypeImpot typeImpot, Pageable pageable) {
        return taxationRepository.findByTypeImpotAndActifTrue(typeImpot, pageable);
    }

    /**
     * Récupère toutes les taxations pour un statut donné
     * @param statut Le statut de la taxation
     * @return Liste des taxations pour ce statut
     */
    public List<Taxation> getTaxationsByStatut(StatutTaxation statut) {
        return taxationRepository.findByStatutAndActifTrue(statut);
    }
    
    /**
     * Récupère toutes les taxations pour un statut donné avec pagination
     * @param statut Le statut de la taxation
     * @param pageable Informations de pagination
     * @return Page de taxations pour ce statut
     */
    public Page<Taxation> getTaxationsByStatutPaginated(StatutTaxation statut, Pageable pageable) {
        return taxationRepository.findByStatutAndActifTrue(statut, pageable);
    }

    /**
     * Génère une taxation pour une déclaration et une nature d'impôt
     * @param declarationId L'ID de la déclaration
     * @param natureImpotId L'ID de la nature d'impôt
     * @param exercice L'exercice fiscal
     * @param agentTaxateurId L'ID de l'agent taxateur
     * @return La taxation générée
     */
    @Transactional
    public Taxation generateTaxationForDeclaration(UUID declarationId, UUID natureImpotId, String exercice, UUID agentTaxateurId) throws Exception {
        // Vérifier si la déclaration existe
        Declaration declaration = declarationRepository.findById(declarationId)
                .orElseThrow(() -> new Exception("Déclaration non trouvée avec l'ID: " + declarationId));
        
        // Vérifier si la nature d'impôt existe
        NatureImpot natureImpot = natureImpotRepository.findById(natureImpotId)
                .orElseThrow(() -> new Exception("Nature d'impôt non trouvée avec l'ID: " + natureImpotId));
        
        // Vérifier si le bien déclaré est associé à cette nature d'impôt
        Propriete propriete = declaration.getPropriete();
        if (propriete == null) {
            throw new Exception("La déclaration n'est pas associée à un bien");
        }
        
        if (!propriete.getNaturesImpot().contains(natureImpot)) {
            throw new Exception("Le bien déclaré n'est pas associé à cette nature d'impôt");
        }
        
        // Vérifier si une taxation existe déjà pour cette déclaration, cette nature d'impôt et cet exercice
        List<Taxation> existingTaxations = taxationRepository.findByDeclarationAndTypeImpotAndExerciceAndActifTrue(
                declaration, TypeImpot.valueOf(natureImpot.getCode()), exercice);
        
        if (!existingTaxations.isEmpty()) {
            throw new Exception("Une taxation existe déjà pour cette déclaration, cette nature d'impôt et cet exercice");
        }
        
        // Créer la taxation
        Taxation taxation = new Taxation();
        taxation.setDateTaxation(new Date());
        taxation.setExercice(exercice);
        taxation.setStatut(StatutTaxation.SOUMISE);
        taxation.setTypeImpot(TypeImpot.valueOf(natureImpot.getCode()));
        taxation.setExoneration(false);
        taxation.setDeclaration(declaration);
        
        // Définir l'agent taxateur
        Agent agentTaxateur = new Agent();
        agentTaxateur.setId(agentTaxateurId);
        
        taxation.setAgentTaxateur(agentTaxateur);
        
        // Calculer le montant de la taxation
        double montant = calculateTaxAmount(propriete, natureImpot);
        taxation.setMontant(montant);
        
        // Sauvegarder la taxation
        return taxationRepository.save(taxation);
    }

    /**
     * Calcule le montant de la taxation pour un bien et une nature d'impôt
     * @param propriete Le bien
     * @param natureImpot La nature d'impôt
     * @return Le montant de la taxation
     */
    private double calculateTaxAmount(Propriete propriete, NatureImpot natureImpot) throws IOException {
        // Charger les taux depuis le fichier JSON
        ClassPathResource resource = new ClassPathResource("taux_if.json");
        InputStream inputStream = resource.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(inputStream);

        String propertyType = propriete.getType().name().toLowerCase();
        String rang = "rang" + propriete.getRangLocalite();
        String contribuableType = propriete.getProprietaire().getType().name().toLowerCase();

        JsonNode rateNode = rootNode.path(propertyType).path(rang).path(contribuableType);
        if (rateNode.isMissingNode()) {
            throw new RuntimeException("Taux IF non trouvé pour le type de propriété: " + propertyType + ", rang: " + rang + ", contribuable: " + contribuableType);
        }

        double rate = rateNode.asDouble();
        
        // Calculer selon le type de propriété
        if ("villas".equals(propertyType) || "commercial".equals(propertyType)) {
            // Taux par mètre carré
            return rate * propriete.getSuperficie();
        } else if ("appartements".equals(propertyType) || "domestic".equals(propertyType)) {
            // Taux fixe
            return rate;
        }
        
        return 0.0;
    }

    /**
     * Met à jour le statut d'une taxation
     * @param id L'ID de la taxation
     * @param statut Le nouveau statut
     * @return La taxation mise à jour, si elle existe
     */
    @Transactional
    public Optional<Taxation> updateTaxationStatus(UUID id, StatutTaxation statut) {
        return taxationRepository.findById(id)
                .map(taxation -> {
                    taxation.setStatut(statut);
                    return taxationRepository.save(taxation);
                });
    }

    /**
     * Valide une taxation
     * @param id L'ID de la taxation
     * @param agentValidateurId L'ID de l'agent validateur
     * @return La taxation mise à jour, si elle existe
     */
    @Transactional
    public Optional<Taxation> validerTaxation(UUID id, UUID agentValidateurId) {
        return taxationRepository.findById(id)
                .map(taxation -> {
                    if (taxation.getStatut() != StatutTaxation.SOUMISE) {
                        throw new IllegalStateException("Seule une taxation soumise peut être validée");
                    }
                    
                    Agent agentValidateur = new Agent();
                    agentValidateur.setId(agentValidateurId);
                    
                    taxation.setAgentValidateur(agentValidateur);
                    taxation.setStatut(StatutTaxation.VALIDEE);
                    return taxationRepository.save(taxation);
                });
    }

    /**
     * Associe un paiement à une taxation
     * @param id L'ID de la taxation
     * @param paiement Le paiement à associer
     * @return La taxation mise à jour, si elle existe
     */
    @Transactional
    public Optional<Taxation> associerPaiement(UUID id, Paiement paiement) {
        return taxationRepository.findById(id)
                .map(taxation -> {
                    if (taxation.getStatut() != StatutTaxation.VALIDEE) {
                        throw new IllegalStateException("Seule une taxation validée peut être associée à un paiement");
                    }
                    
                    taxation.setPaiement(paiement);
                    taxation.setStatut(StatutTaxation.PAYEE);
                    return taxationRepository.save(taxation);
                });
    }

    /**
     * Accorde une exonération pour une taxation
     * @param id L'ID de la taxation
     * @param motif Le motif de l'exonération
     * @return La taxation mise à jour, si elle existe
     */
    @Transactional
    public Optional<Taxation> accorderExoneration(UUID id, String motif) {
        return taxationRepository.findById(id)
                .map(taxation -> {
                    taxation.setExoneration(true);
                    taxation.setStatut(StatutTaxation.ANNULEE);
                    return taxationRepository.save(taxation);
                });
    }

    /**
     * Désactive une taxation (suppression logique)
     * @param id L'ID de la taxation à désactiver
     * @return true si la taxation a été désactivée, false sinon
     */
    @Transactional
    public boolean desactiverTaxation(UUID id) {
        return taxationRepository.findById(id)
                .map(taxation -> {
                    taxation.setActif(false);
                    taxationRepository.save(taxation);
                    return true;
                })
                .orElse(false);
    }
}
