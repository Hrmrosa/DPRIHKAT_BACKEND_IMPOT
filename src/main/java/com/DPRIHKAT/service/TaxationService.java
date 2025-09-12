package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.DPRIHKAT.repository.AgentRepository;
import com.DPRIHKAT.repository.ProprieteImpotRepository;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.repository.TaxationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

/**
 * Service pour gérer les taxations des biens et concessions
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
    private ProprieteImpotRepository proprieteImpotRepository;
    
    @Autowired
    private AgentRepository agentRepository;

    /**
     * Récupère toutes les taxations
     * @return Liste de toutes les taxations
     */
    public List<Taxation> getAllTaxations() {
        return taxationRepository.findAll();
    }

    /**
     * Récupère toutes les taxations actives
     * @return Liste des taxations actives
     */
    public List<Taxation> getAllActiveTaxations() {
        return taxationRepository.findByActifTrue();
    }

    /**
     * Récupère une taxation par son ID
     * @param id L'ID de la taxation
     * @return La taxation correspondante, si elle existe
     */
    public Optional<Taxation> getTaxationById(UUID id) {
        try {
            Taxation taxation = taxationRepository.findByIdWithoutJoins(id);
            return Optional.ofNullable(taxation);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la taxation avec l'ID: {}", id, e);
            return Optional.empty();
        }
    }

    /**
     * Récupère toutes les taxations pour une propriété donnée
     * @param proprieteId L'ID de la propriété
     * @return Liste des taxations pour cette propriété
     */
    public List<Taxation> getTaxationsByProprieteId(UUID proprieteId) {
        Optional<Propriete> proprieteOpt = proprieteRepository.findById(proprieteId);
        if (proprieteOpt.isPresent()) {
            return taxationRepository.findByProprieteAndActifTrue(proprieteOpt.get());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Récupère toutes les taxations pour un exercice donné
     * @param exercice L'exercice (année fiscale)
     * @return Liste des taxations pour cet exercice
     */
    public List<Taxation> getTaxationsByExercice(Integer exercice) {
        return taxationRepository.findByExerciceAndActifTrue(String.valueOf(exercice));
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
     * Récupère toutes les taxations pour un statut donné
     * @param statut Le statut de la taxation
     * @return Liste des taxations pour ce statut
     */
    public List<Taxation> getTaxationsByStatut(StatutTaxation statut) {
        return taxationRepository.findByStatutAndActifTrue(statut);
    }

    /**
     * Génère une taxation pour une propriété
     * @param proprieteId L'ID de la propriété
     * @param natureImpotId L'ID de la nature d'impôt
     * @param exercice L'exercice (année fiscale)
     * @param agentTaxateurId L'ID de l'agent taxateur
     * @return La taxation générée
     */
    public Taxation generateTaxationForProperty(UUID proprieteId, UUID natureImpotId, String exercice, UUID agentTaxateurId) throws Exception {
        // Vérifier si la propriété existe
        Propriete propriete = proprieteRepository.findById(proprieteId)
                .orElseThrow(() -> new Exception("Propriété non trouvée avec l'ID: " + proprieteId));
        
        // Vérifier si le lien propriété-impôt existe
        ProprieteImpot proprieteImpot = proprieteImpotRepository.findById(natureImpotId)
                .orElseThrow(() -> new Exception("Nature d'impôt non trouvée avec l'ID: " + natureImpotId));
        
        // Vérifier si une taxation existe déjà pour cette propriété, cette nature d'impôt et cet exercice
        List<Taxation> existingTaxations = taxationRepository.findByProprieteAndTypeImpotAndExerciceAndActifTrue(
                propriete, determineTypeImpotFromNature(proprieteImpot.getNatureImpot().getCode()), exercice);
        
        if (!existingTaxations.isEmpty()) {
            throw new Exception("Une taxation existe déjà pour cette propriété, cette nature d'impôt et cet exercice");
        }
        
        // Récupérer l'agent taxateur
        Agent agentTaxateur = agentRepository.findById(agentTaxateurId)
                .orElseThrow(() -> new Exception("Agent taxateur non trouvé avec l'ID: " + agentTaxateurId));
        
        // Créer la taxation
        Taxation taxation = new Taxation();
        taxation.setDateTaxation(new Date());
        taxation.setExercice(exercice);
        taxation.setStatut(StatutTaxation.EN_ATTENTE);
        // Convertir le code de la nature d'impôt en TypeImpot
        TypeImpot typeImpot = determineTypeImpotFromNature(proprieteImpot.getNatureImpot().getCode());
        taxation.setTypeImpot(typeImpot);
        taxation.setExoneration(false);
        taxation.setPropriete(propriete);
        taxation.setProprieteImpot(proprieteImpot);
        taxation.setAgent(agentTaxateur);
        
        // Calculer le montant de la taxation
        double montant = calculateTaxAmount(propriete, proprieteImpot);
        taxation.setMontant(montant);
        
        // Définir la date d'échéance (par défaut, fin de l'année fiscale)
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(exercice), Calendar.DECEMBER, 31);
        taxation.setDateEcheance(calendar.getTime());
        
        // Sauvegarder la taxation pour obtenir son ID
        taxation = taxationRepository.save(taxation);
        
        // Générer le numéro de taxation
        String numeroTaxation = generateNumeroTaxation(typeImpot, agentTaxateur, exercice);
        taxation.setNumeroTaxation(numeroTaxation);
        
        // Générer le code QR
        String codeQR = generateQRCode(taxation);
        taxation.setCodeQR(codeQR);
        
        // Sauvegarder à nouveau la taxation avec le numéro et le code QR
        return taxationRepository.save(taxation);
    }

    /**
     * Calcule le montant de la taxation pour une propriété et une nature d'impôt
     * @param propriete La propriété
     * @param proprieteImpot Le lien propriété-impôt
     * @return Le montant de la taxation
     */
    private double calculateTaxAmount(Propriete propriete, ProprieteImpot proprieteImpot) throws IOException {
        // Si un taux d'imposition est défini dans le lien propriété-impôt, l'utiliser
        if (proprieteImpot.getTauxImposition() != null) {
            // Selon le type de propriété, appliquer le taux différemment
            switch (propriete.getType()) {
                case VI:
                case AT:
                    // Pour les villas et ateliers, le taux est appliqué à la superficie
                    return proprieteImpot.getTauxImposition() * propriete.getSuperficie();
                case AP:
                case CITERNE:
                case DEPOT:
                case CH:
                case TE:
                    // Pour les autres types, le taux est un montant fixe
                    return proprieteImpot.getTauxImposition();
                default:
                    return 0.0;
            }
        } else {
            // Sinon, utiliser les taux définis dans le fichier JSON
            return calculateTaxForProperty(propriete);
        }
    }

    /**
     * Calcule le montant de la taxation pour une propriété selon les taux définis dans taux_if.json
     * @param propriete La propriété
     * @return Le montant de la taxation
     */
    public double calculateTaxForProperty(Propriete propriete) throws IOException {
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
     * Détermine le TypeImpot à partir du code de la nature d'impôt
     * @param code le code de la nature d'impôt
     * @return le TypeImpot correspondant
     */
    private TypeImpot determineTypeImpotFromNature(String code) {
        switch (code) {
            case "IF": return TypeImpot.IF;
            case "IRL": return TypeImpot.IRL;
            case "ICM": return TypeImpot.ICM;
            case "IRV": return TypeImpot.IRV;
            case "RL": return TypeImpot.RL;
            default: return TypeImpot.IF; // Valeur par défaut
        }
    }

    /**
     * Génère un numéro de taxation au format 't_0001_typeimpot_codeBureauTaxateur_annee'
     * @param typeImpot Le type d'impôt
     * @param agent L'agent qui a effectué la taxation
     * @param exercice L'exercice (année fiscale)
     * @return Le numéro de taxation généré
     */
    private String generateNumeroTaxation(TypeImpot typeImpot, Agent agent, String exercice) {
        // Récupérer le dernier numéro de taxation pour ce type d'impôt et cette année
        String prefix = "t_";
        String typeImpotStr = typeImpot.toString();
        String codeBureau = agent.getBureau() != null ? agent.getBureau().getCode() : "UNKNOWN";
        
        // Compter le nombre de taxations existantes pour ce type d'impôt et cette année
        long count = taxationRepository.countByTypeImpotAndExercice(typeImpot, exercice) + 1;
        
        // Formater le numéro séquentiel sur 4 chiffres
        String sequentialNumber = String.format("%04d", count);
        
        // Construire le numéro de taxation
        return prefix + sequentialNumber + "_" + typeImpotStr + "_" + codeBureau + "_" + exercice;
    }

    /**
     * Génère un code QR pour une taxation
     * @param taxation La taxation
     * @return Le code QR généré
     */
    private String generateQRCode(Taxation taxation) {
        // TODO : implémenter la génération du code QR
        return "";
    }

    /**
     * Met à jour le statut d'une taxation
     * @param id L'ID de la taxation
     * @param statut Le nouveau statut
     * @return La taxation mise à jour, si elle existe
     */
    public Optional<Taxation> updateTaxationStatus(UUID id, StatutTaxation statut) {
        return taxationRepository.findById(id)
                .map(taxation -> {
                    taxation.setStatut(statut);
                    return taxationRepository.save(taxation);
                });
    }

    /**
     * Marque une taxation comme payée
     * @param id L'ID de la taxation
     * @param paiement Le paiement associé
     * @return La taxation mise à jour, si elle existe
     */
    public Optional<Taxation> markTaxationAsPaid(UUID id, Paiement paiement) {
        return taxationRepository.findById(id)
                .map(taxation -> {
                    taxation.setPaiement(paiement);
                    taxation.setStatut(StatutTaxation.PAYEE);
                    return taxationRepository.save(taxation);
                });
    }

    /**
     * Marque une taxation comme partiellement payée
     * @param id L'ID de la taxation
     * @param paiement Le paiement associé
     * @return La taxation mise à jour, si elle existe
     */
    public Optional<Taxation> markTaxationAsPartiallyPaid(UUID id, Paiement paiement) {
        return taxationRepository.findById(id)
                .map(taxation -> {
                    taxation.setPaiement(paiement);
                    taxation.setStatut(StatutTaxation.PAYEE_PARTIELLEMENT);
                    return taxationRepository.save(taxation);
                });
    }

    /**
     * Marque une taxation comme apurée
     * @param id L'ID de la taxation
     * @param apurement L'apurement associé
     * @return La taxation mise à jour, si elle existe
     */
    public Optional<Taxation> markTaxationAsSettled(UUID id, Apurement apurement) {
        return taxationRepository.findById(id)
                .map(taxation -> {
                    taxation.setApurement(apurement);
                    taxation.setStatut(StatutTaxation.APUREE);
                    return taxationRepository.save(taxation);
                });
    }

    /**
     * Accorde une exonération pour une taxation
     * @param id L'ID de la taxation
     * @param motif Le motif de l'exonération
     * @return La taxation mise à jour, si elle existe
     */
    public Optional<Taxation> grantExemption(UUID id, String motif) {
        return taxationRepository.findById(id)
                .map(taxation -> {
                    taxation.setExoneration(true);
                    taxation.setMotifExoneration(motif);
                    taxation.setStatut(StatutTaxation.EXONEREE);
                    return taxationRepository.save(taxation);
                });
    }

    /**
     * Désactive une taxation (suppression logique)
     * @param id L'ID de la taxation à désactiver
     * @return true si la taxation a été désactivée, false sinon
     */
    public boolean deactivateTaxation(UUID id) {
        return taxationRepository.findById(id)
                .map(taxation -> {
                    taxation.setActif(false);
                    taxationRepository.save(taxation);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Active une taxation
     * @param id L'ID de la taxation à activer
     * @return true si la taxation a été activée, false sinon
     */
    public boolean activateTaxation(UUID id) {
        return taxationRepository.findById(id)
                .map(taxation -> {
                    taxation.setActif(true);
                    taxationRepository.save(taxation);
                    return true;
                })
                .orElse(false);
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
     * Récupère toutes les taxations actives avec pagination
     * @param pageable Informations de pagination
     * @return Page de taxations actives
     */
    public Page<Taxation> getAllActiveTaxationsPaginated(Pageable pageable) {
        return taxationRepository.findByActifTrue(pageable);
    }

    /**
     * Récupère toutes les taxations pour une propriété donnée avec pagination
     * @param proprieteId L'ID de la propriété
     * @param pageable Informations de pagination
     * @return Page de taxations pour cette propriété
     */
    public Page<Taxation> getTaxationsByProprieteId(UUID proprieteId, Pageable pageable) {
        Optional<Propriete> proprieteOpt = proprieteRepository.findById(proprieteId);
        if (proprieteOpt.isPresent()) {
            return taxationRepository.findByProprieteAndActifTrue(proprieteOpt.get(), pageable);
        } else {
            return Page.empty();
        }
    }

    /**
     * Récupère toutes les taxations pour un exercice donné avec pagination
     * @param exercice L'exercice (année fiscale)
     * @param pageable Informations de pagination
     * @return Page de taxations pour cet exercice
     */
    public Page<Taxation> getTaxationsByExercice(Integer exercice, Pageable pageable) {
        return taxationRepository.findByExerciceAndActifTrue(String.valueOf(exercice), pageable);
    }

    /**
     * Récupère toutes les taxations pour un type d'impôt donné avec pagination
     * @param typeImpot Le type d'impôt
     * @param pageable Informations de pagination
     * @return Page de taxations pour ce type d'impôt
     */
    public Page<Taxation> getTaxationsByTypeImpot(TypeImpot typeImpot, Pageable pageable) {
        return taxationRepository.findByTypeImpotAndActifTrue(typeImpot, pageable);
    }

    /**
     * Récupère toutes les taxations pour un statut donné avec pagination
     * @param statut Le statut de la taxation
     * @param pageable Informations de pagination
     * @return Page de taxations pour ce statut
     */
    public Page<Taxation> getTaxationsByStatut(StatutTaxation statut, Pageable pageable) {
        return taxationRepository.findByStatutAndActifTrue(statut, pageable);
    }
    
    /**
     * Annule une taxation en spécifiant un motif d'annulation
     * Cette opération ne peut être effectuée que par un chef de division ou un administrateur
     * 
     * @param taxationId L'ID de la taxation à annuler
     * @param motifAnnulation Le motif d'annulation
     * @return La taxation annulée
     * @throws RuntimeException Si la taxation n'existe pas ou si elle est déjà annulée
     */
    public Taxation annulerTaxation(UUID taxationId, String motifAnnulation) {
        if (motifAnnulation == null || motifAnnulation.trim().isEmpty()) {
            throw new RuntimeException("Le motif d'annulation est obligatoire");
        }
        
        // Récupérer la taxation par son ID
        Taxation taxation = taxationRepository.findById(taxationId)
                .orElseThrow(() -> new RuntimeException("Taxation non trouvée avec l'ID: " + taxationId));
        
        // Vérifier si la taxation est déjà annulée
        if (!taxation.isActif()) {
            throw new RuntimeException("Cette taxation est déjà annulée");
        }
        
        // Annuler la taxation
        taxation.setActif(false);
        taxation.setMotifAnnulation(motifAnnulation);
        taxation.setStatut(StatutTaxation.ANNULEE);
        
        // Sauvegarder les modifications
        return taxationRepository.save(taxation);
    }
}
