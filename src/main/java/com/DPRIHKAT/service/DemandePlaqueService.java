package com.DPRIHKAT.service;

import com.DPRIHKAT.dto.DemandePlaqueResponseDTO;
import com.DPRIHKAT.dto.NoteTaxationDTO;
import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.DemandePlaque;
import com.DPRIHKAT.entity.Plaque;
import com.DPRIHKAT.entity.Taxation;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.entity.enums.Devise;
import com.DPRIHKAT.entity.enums.StatutDemande;
import com.DPRIHKAT.entity.enums.StatutPlaque;
import com.DPRIHKAT.entity.enums.StatutTaxation;
import com.DPRIHKAT.entity.enums.StatutVehicule;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.DPRIHKAT.repository.ContribuableRepository;
import com.DPRIHKAT.repository.DemandePlaqueRepository;
import com.DPRIHKAT.repository.PlaqueRepository;
import com.DPRIHKAT.repository.TaxationRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Service pour gérer les demandes de plaque d'immatriculation
 * 
 * @author amateur
 */
@Service
public class DemandePlaqueService {
    
    private static final Logger logger = LoggerFactory.getLogger(DemandePlaqueService.class);
    
    @Autowired
    private DemandePlaqueRepository demandePlaqueRepository;
    
    @Autowired
    private VehiculeRepository vehiculeRepository;
    
    @Autowired
    private ContribuableRepository contribuableRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private PlaqueService plaqueService;
    
    @Autowired
    private TaxationService taxationService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private VignetteService vignetteService;
    
    @Autowired
    private PlaqueRepository plaqueRepository;
    
    @Autowired
    private TaxationRepository taxationRepository;
    
    /**
     * Soumettre une nouvelle demande de plaque
     * Crée automatiquement 2 notes de taxation : plaque (37 USD) + vignette (10 USD)
     * 
     * @param contribuableId ID du contribuable
     * @param vehiculeId ID du véhicule
     * @param facture Facture du véhicule
     * @return La demande créée avec les notes de taxation
     */
    @Transactional
    public DemandePlaqueResponseDTO soumettreDemande(UUID contribuableId, UUID vehiculeId, MultipartFile facture) {
        logger.info("Soumission d'une demande de plaque pour le contribuable {} et le véhicule {}", contribuableId, vehiculeId);
        
        // Vérifier que le contribuable existe
        Contribuable contribuable = contribuableRepository.findById(contribuableId)
                .orElseThrow(() -> new RuntimeException("Contribuable non trouvé avec ID: " + contribuableId));
        
        // Vérifier que le véhicule existe et appartient au contribuable
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé avec ID: " + vehiculeId));
        
        if (!vehicule.getProprietaireId().equals(contribuableId)) {
            throw new RuntimeException("Ce véhicule n'appartient pas à ce contribuable");
        }
        
        // Stocker la facture
        String facturePath = fileStorageService.storeFile(facture);
        logger.info("Facture stockée à {}", facturePath);
        
        // Créer la demande
        DemandePlaque demande = new DemandePlaque();
        demande.setContribuable(contribuable);
        demande.setVehicule(vehicule);
        demande.setFacturePath(facturePath);
        demande.setStatut(StatutDemande.TAXEE); // Directement TAXEE car les notes sont créées
        demande.setDateDemande(new Date());
        
        // Sauvegarder la demande d'abord
        demande = demandePlaqueRepository.save(demande);
        logger.info("Demande de plaque créée avec succès: {}", demande.getId());
        
        // Créer la note de taxation pour la plaque (40 USD)
        Taxation taxationPlaque = creerNoteTaxationPlaque(demande, contribuable, vehicule);
        logger.info("Note de taxation plaque créée: {} - Montant: {} USD", taxationPlaque.getNumeroTaxation(), taxationPlaque.getMontant());
        
        // Créer la note de taxation pour la vignette (10 USD pour moto/tricycle)
        Taxation taxationVignette = creerNoteTaxationVignette(demande, contribuable, vehicule);
        logger.info("Note de taxation vignette créée: {} - Montant: {} USD", taxationVignette.getNumeroTaxation(), taxationVignette.getMontant());
        
        // Mettre à jour le statut du véhicule
        vehicule.setStatut(StatutVehicule.TAXE);
        vehiculeRepository.save(vehicule);
        
        // Préparer la réponse avec les notes de taxation
        NoteTaxationDTO notePlaqueDTO = convertirTaxationEnDTO(taxationPlaque, vehicule);
        NoteTaxationDTO noteVignetteDTO = convertirTaxationEnDTO(taxationVignette, vehicule);
        
        DemandePlaqueResponseDTO response = new DemandePlaqueResponseDTO(demande, notePlaqueDTO, noteVignetteDTO);
        response.setMessage("Demande de plaque soumise avec succès. Deux notes de taxation ont été générées : Plaque (37 USD) et Vignette (10 USD). Total à payer: 47 USD");
        
        logger.info("Demande de plaque traitée avec succès avec les notes de taxation");
        return response;
    }
    
    /**
     * Valider une demande de plaque
     * 
     * @param demandeId ID de la demande
     * @param validateurId ID du validateur
     * @return La demande validée
     */
    @Transactional
    public DemandePlaque validerDemande(UUID demandeId, UUID validateurId) {
        try {
            logger.info("Validation de la demande de plaque {} par le validateur {}", demandeId, validateurId);
            
            // Vérifier que la demande existe
            DemandePlaque demande = demandePlaqueRepository.findById(demandeId)
                    .orElseThrow(() -> new RuntimeException("Demande non trouvée avec ID: " + demandeId));
            
            // Vérifier que la demande est en statut SOUMISE
            if (demande.getStatut() != StatutDemande.SOUMISE) {
                throw new RuntimeException("Cette demande ne peut pas être validée car elle n'est pas en statut SOUMISE");
            }
            
            // Vérifier que le validateur existe
            Utilisateur validateur = utilisateurRepository.findById(validateurId)
                    .orElseThrow(() -> new RuntimeException("Validateur non trouvé avec ID: " + validateurId));
            
            // Mettre à jour le statut
            demande.setStatut(StatutDemande.VALIDEE);
            demande.setDateValidation(new Date());
            demande.setValidateur(validateur);
            
            // Sauvegarder la demande
            demande = demandePlaqueRepository.save(demande);
            logger.info("Demande validée avec succès");
            
            // Générer la taxation
            taxationService.genererTaxationPourPlaque(demande);
            logger.info("Taxation générée avec succès");
            
            // Mettre à jour le statut
            demande.setStatut(StatutDemande.TAXEE);
            demande = demandePlaqueRepository.save(demande);
            
            // Envoyer un email au contribuable
            try {
                emailService.envoyerNotificationValidation(demande);
                logger.info("Email de notification envoyé avec succès");
            } catch (Exception e) {
                logger.error("Erreur lors de l'envoi de l'email de notification", e);
            }
            
            return demande;
        } catch (Exception e) {
            logger.error("Erreur lors de la validation de la demande", e);
            throw e;
        }
    }
    
    /**
     * Rejeter une demande de plaque
     * 
     * @param demandeId ID de la demande
     * @param validateurId ID du validateur
     * @param motifRejet Motif du rejet
     * @return La demande rejetée
     */
    @Transactional
    public DemandePlaque rejeterDemande(UUID demandeId, UUID validateurId, String motifRejet) {
        logger.info("Rejet de la demande de plaque {} par le validateur {}", demandeId, validateurId);
        
        // Vérifier que la demande existe
        DemandePlaque demande = demandePlaqueRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée avec ID: " + demandeId));
        
        // Vérifier que la demande est en statut SOUMISE
        if (demande.getStatut() != StatutDemande.SOUMISE) {
            throw new RuntimeException("Cette demande ne peut pas être rejetée car elle n'est pas en statut SOUMISE");
        }
        
        // Vérifier que le validateur existe
        Utilisateur validateur = utilisateurRepository.findById(validateurId)
                .orElseThrow(() -> new RuntimeException("Validateur non trouvé avec ID: " + validateurId));
        
        // Mettre à jour le statut
        demande.setStatut(StatutDemande.REJETEE);
        demande.setValidateur(validateur);
        demande.setMotifRejet(motifRejet);
        
        // Sauvegarder la demande
        demande = demandePlaqueRepository.save(demande);
        logger.info("Demande rejetée avec succès");
        
        // Envoyer un email au contribuable
        try {
            emailService.envoyerNotificationRejet(demande);
            logger.info("Email de notification envoyé avec succès");
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email de notification", e);
        }
        
        return demande;
    }
    
    /**
     * Marquer une demande comme payée
     * 
     * @param demandeId ID de la demande
     * @return La demande payée
     */
    @Transactional
    public DemandePlaque marquerCommePaye(UUID demandeId) {
        logger.info("Marquage de la demande de plaque {} comme payée", demandeId);
        
        // Vérifier que la demande existe
        DemandePlaque demande = demandePlaqueRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée avec ID: " + demandeId));
        
        // Vérifier que la demande est en statut TAXEE
        if (demande.getStatut() != StatutDemande.TAXEE) {
            throw new RuntimeException("Cette demande ne peut pas être marquée comme payée car elle n'est pas en statut TAXEE");
        }
        
        // Mettre à jour le statut
        demande.setStatut(StatutDemande.PAYEE);
        demande.setDatePaiement(new Date());
        
        // Mettre à jour le statut du véhicule
        Vehicule vehicule = demande.getVehicule();
        vehicule.setStatut(StatutVehicule.PAYE);
        vehiculeRepository.save(vehicule);
        
        // Sauvegarder la demande
        demande = demandePlaqueRepository.save(demande);
        logger.info("Demande marquée comme payée avec succès");
        
        // Envoyer un email au contribuable
        try {
            emailService.envoyerNotificationPaiement(demande);
            logger.info("Email de notification envoyé avec succès");
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email de notification", e);
        }
        
        return demande;
    }
    
    /**
     * Apurer et livrer une plaque
     * 
     * @param demandeId ID de la demande
     * @param numeroPlaque Numéro de la plaque
     * @return La demande livrée
     */
    @Transactional
    public DemandePlaque apurerEtLivrer(UUID demandeId, String numeroPlaque) {
        logger.info("Apurement et livraison de la plaque pour la demande {}", demandeId);
        
        // Vérifier que la demande existe
        DemandePlaque demande = demandePlaqueRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée avec ID: " + demandeId));
        
        // Vérifier que la demande est en statut PAYEE
        if (demande.getStatut() != StatutDemande.PAYEE) {
            throw new RuntimeException("Cette demande ne peut pas être apurée car elle n'est pas en statut PAYEE");
        }
        
        // Mettre à jour le statut
        demande.setStatut(StatutDemande.APUREE);
        
        // Attribuer une plaque
        Plaque plaque = plaqueService.assignPlaqueToVehicle(demande.getVehicule().getId());
        plaque.setNumplaque(numeroPlaque);
        plaque.setStatut(StatutPlaque.LIVREE);
        plaque.setDemande(demande);
        plaqueService.save(plaque);
        logger.info("Plaque assignée avec succès");
        
        // Générer une vignette automatiquement
        vignetteService.genererVignettePourVehicule(demande.getVehicule().getId());
        logger.info("Vignette générée avec succès");
        
        // Marquer comme livrée
        demande.setStatut(StatutDemande.LIVREE);
        demande.setDateLivraison(new Date());
        demande.setPlaque(plaque);
        
        // Sauvegarder la demande
        demande = demandePlaqueRepository.save(demande);
        logger.info("Demande marquée comme livrée avec succès");
        
        // Envoyer un email au contribuable
        try {
            emailService.envoyerNotificationLivraison(demande);
            logger.info("Email de notification envoyé avec succès");
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email de notification", e);
        }
        
        return demande;
    }
    
    /**
     * Récupérer une demande par son ID
     * 
     * @param id ID de la demande
     * @return La demande
     */
    public DemandePlaque getDemandeById(UUID id) {
        return demandePlaqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée avec ID: " + id));
    }
    
    /**
     * Récupérer toutes les demandes
     * 
     * @return Liste des demandes
     */
    public List<DemandePlaque> getAllDemandes() {
        return demandePlaqueRepository.findAll();
    }
    
    /**
     * Récupérer les demandes par statut
     * 
     * @param statut Statut des demandes
     * @return Liste des demandes
     */
    public List<DemandePlaque> getDemandesByStatut(StatutDemande statut) {
        return demandePlaqueRepository.findByStatut(statut);
    }
    
    /**
     * Récupérer les demandes par contribuable
     * 
     * @param contribuableId ID du contribuable
     * @return Liste des demandes
     */
    public List<DemandePlaque> getDemandesByContribuable(UUID contribuableId) {
        return demandePlaqueRepository.findByContribuable_Id(contribuableId);
    }
    
    /**
     * Récupérer les demandes par véhicule
     * 
     * @param vehiculeId ID du véhicule
     * @return Liste des demandes
     */
    public List<DemandePlaque> getDemandesByVehicule(UUID vehiculeId) {
        return demandePlaqueRepository.findByVehicule_Id(vehiculeId);
    }
    
    /**
     * Récupérer les demandes par contribuable et statut
     * 
     * @param contribuableId ID du contribuable
     * @param statut Statut des demandes
     * @return Liste des demandes
     */
    public List<DemandePlaque> getDemandesByContribuableAndStatut(UUID contribuableId, StatutDemande statut) {
        return demandePlaqueRepository.findByContribuable_IdAndStatut(contribuableId, statut);
    }
    
    /**
     * Récupérer les demandes validées par un utilisateur
     * 
     * @param validateurId ID du validateur
     * @return Liste des demandes
     */
    public List<DemandePlaque> getDemandesByValidateur(UUID validateurId) {
        return demandePlaqueRepository.findByValidateur_Id(validateurId);
    }
    
    /**
     * Attribuer un numéro de plaque à une demande
     * 
     * @param demandeId ID de la demande
     * @param numeroPlaque Numéro de plaque à attribuer
     * @param agentId ID de l'agent qui attribue
     * @return La demande avec la plaque attribuée
     */
    @Transactional
    public DemandePlaque attribuerPlaque(UUID demandeId, String numeroPlaque, UUID agentId) {
        logger.info("Attribution du numéro de plaque {} à la demande {}", numeroPlaque, demandeId);
        
        // Vérifier que la demande existe
        DemandePlaque demande = demandePlaqueRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée avec ID: " + demandeId));
        
        // Vérifier que la demande est en statut PAYEE
        if (demande.getStatut() != StatutDemande.PAYEE) {
            throw new RuntimeException("Cette demande ne peut pas recevoir de plaque car elle n'est pas en statut PAYEE");
        }
        
        // Vérifier que le numéro de plaque n'existe pas déjà
        if (plaqueRepository.findByNumplaque(numeroPlaque).isPresent()) {
            throw new RuntimeException("Ce numéro de plaque existe déjà: " + numeroPlaque);
        }
        
        // Créer une nouvelle plaque
        Plaque plaque = new Plaque();
        plaque.setNumplaque(numeroPlaque);
        plaque.setNumeroSerie(numeroPlaque);
        plaque.setVehicule(demande.getVehicule());
        plaque.setStatut(StatutPlaque.ATTRIBUEE);
        plaque.setDisponible(false);
        plaque.setDemande(demande);
        plaque = plaqueRepository.save(plaque);
        
        // Mettre à jour le véhicule
        Vehicule vehicule = demande.getVehicule();
        vehicule.setNumeroPlaque(numeroPlaque);
        vehicule.setImmatriculation(numeroPlaque); // Remplacer l'immatriculation temporaire
        vehicule.setStatut(StatutVehicule.PLAQUE_ATTRIBUEE);
        vehiculeRepository.save(vehicule);
        
        // Mettre à jour la demande
        demande.setStatut(StatutDemande.VALIDEE);
        demande.setPlaque(plaque);
        demande = demandePlaqueRepository.save(demande);
        
        logger.info("Plaque {} attribuée avec succès à la demande {}", numeroPlaque, demandeId);
        
        // Envoyer un email au contribuable
        try {
            emailService.envoyerNotificationAttribution(demande);
            logger.info("Email de notification envoyé avec succès");
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email de notification", e);
        }
        
        return demande;
    }
    
    /**
     * Attribuer une plaque depuis le stock à une demande
     * 
     * @param demandeId ID de la demande
     * @param plaqueId ID de la plaque en stock
     * @param agentId ID de l'agent qui attribue
     * @return La demande avec la plaque attribuée
     */
    @Transactional
    public DemandePlaque attribuerPlaqueDepuisStock(UUID demandeId, UUID plaqueId, UUID agentId) {
        logger.info("Attribution de la plaque {} depuis le stock à la demande {}", plaqueId, demandeId);
        
        // Vérifier que la demande existe
        DemandePlaque demande = demandePlaqueRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée avec ID: " + demandeId));
        
        // Vérifier que la demande est en statut PAYEE
        if (demande.getStatut() != StatutDemande.PAYEE) {
            throw new RuntimeException("Cette demande ne peut pas recevoir de plaque car elle n'est pas en statut PAYEE");
        }
        
        // Vérifier que la plaque existe et est disponible
        Plaque plaque = plaqueRepository.findById(plaqueId)
                .orElseThrow(() -> new RuntimeException("Plaque non trouvée avec ID: " + plaqueId));
        
        if (plaque.getStatut() != StatutPlaque.STOCK) {
            throw new RuntimeException("Cette plaque n'est pas disponible en stock");
        }
        
        // Attribuer la plaque au véhicule
        plaque.setVehicule(demande.getVehicule());
        plaque.setStatut(StatutPlaque.ATTRIBUEE);
        plaque.setDisponible(false);
        plaque.setDemande(demande);
        plaque = plaqueRepository.save(plaque);
        
        // Mettre à jour le véhicule
        Vehicule vehicule = demande.getVehicule();
        vehicule.setNumeroPlaque(plaque.getNumplaque());
        vehicule.setImmatriculation(plaque.getNumplaque()); // Remplacer l'immatriculation temporaire
        vehicule.setStatut(StatutVehicule.PLAQUE_ATTRIBUEE);
        vehiculeRepository.save(vehicule);
        
        // Mettre à jour la demande
        demande.setStatut(StatutDemande.VALIDEE);
        demande = demandePlaqueRepository.save(demande);
        
        logger.info("Plaque {} attribuée avec succès depuis le stock à la demande {}", plaqueId, demandeId);
        
        // Envoyer un email au contribuable
        try {
            emailService.envoyerNotificationAttribution(demande);
            logger.info("Email de notification envoyé avec succès");
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email de notification", e);
        }
        
        return demande;
    }
    
    /**
     * Crée une note de taxation pour la plaque d'immatriculation
     * Montant fixe: 37 USD
     */
    private Taxation creerNoteTaxationPlaque(DemandePlaque demande, Contribuable contribuable, Vehicule vehicule) {
        Taxation taxation = new Taxation();
        taxation.setDateTaxation(new Date());
        taxation.setMontant(37.0); // Montant fixe pour la plaque
        taxation.setDevise(Devise.USD);
        taxation.setExercice(String.valueOf(LocalDate.now().getYear()));
        taxation.setStatut(StatutTaxation.EN_ATTENTE);
        taxation.setTypeImpot(TypeImpot.PLAQUE);
        taxation.setContribuableDirect(contribuable);
        taxation.setDemande(demande);
        
        // Générer le numéro de taxation
        String numeroTaxation = String.format("PLAQ_%s_%s_%d",
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                vehicule.getNumeroChassis().substring(Math.max(0, vehicule.getNumeroChassis().length() - 6)),
                LocalDate.now().getYear());
        taxation.setNumeroTaxation(numeroTaxation);
        
        // Date d'échéance: 30 jours
        taxation.setDateEcheance(Date.from(LocalDate.now().plusDays(30)
                .atStartOfDay().toInstant(java.time.ZoneOffset.UTC)));
        
        // Informations bancaires (à configurer selon votre banque)
        taxation.setNomBanque("RAWBANK");
        taxation.setNumeroCompte("CD59 0000 0000 0000 0000 0001");
        taxation.setIntituleCompte("DPRIHKAT - PLAQUES D'IMMATRICULATION");
        
        return taxationRepository.save(taxation);
    }
    
    /**
     * Crée une note de taxation pour la vignette
     * Montant fixe: 10 USD (pour moto/tricycle)
     */
    private Taxation creerNoteTaxationVignette(DemandePlaque demande, Contribuable contribuable, Vehicule vehicule) {
        Taxation taxation = new Taxation();
        taxation.setDateTaxation(new Date());
        taxation.setMontant(10.0); // Montant fixe pour la vignette moto/tricycle
        taxation.setDevise(Devise.USD);
        taxation.setExercice(String.valueOf(LocalDate.now().getYear()));
        taxation.setStatut(StatutTaxation.EN_ATTENTE);
        taxation.setTypeImpot(TypeImpot.IRV); // Impôt sur Revenu Véhicule (vignette)
        taxation.setContribuableDirect(contribuable);
        taxation.setDemande(demande);
        
        // Générer le numéro de taxation
        String numeroTaxation = String.format("VIG_%s_%s_%d",
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                vehicule.getNumeroChassis().substring(Math.max(0, vehicule.getNumeroChassis().length() - 6)),
                LocalDate.now().getYear());
        taxation.setNumeroTaxation(numeroTaxation);
        
        // Date d'échéance: 30 jours
        taxation.setDateEcheance(Date.from(LocalDate.now().plusDays(30)
                .atStartOfDay().toInstant(java.time.ZoneOffset.UTC)));
        
        // Informations bancaires
        taxation.setNomBanque("RAWBANK");
        taxation.setNumeroCompte("CD59 0000 0000 0000 0000 0002");
        taxation.setIntituleCompte("DPRIHKAT - VIGNETTES VEHICULES");
        
        return taxationRepository.save(taxation);
    }
    
    /**
     * Convertit une entité Taxation en DTO pour l'impression
     */
    private NoteTaxationDTO convertirTaxationEnDTO(Taxation taxation, Vehicule vehicule) {
        NoteTaxationDTO dto = new NoteTaxationDTO();
        
        // Informations de la taxation
        dto.setId(taxation.getId());
        dto.setNumeroTaxation(taxation.getNumeroTaxation());
        dto.setDateTaxation(taxation.getDateTaxation());
        dto.setDateEcheance(taxation.getDateEcheance());
        dto.setMontant(taxation.getMontant());
        dto.setDevise(taxation.getDevise());
        dto.setExercice(taxation.getExercice());
        dto.setStatut(taxation.getStatut());
        dto.setTypeImpot(taxation.getTypeImpot());
        dto.setCodeQR(taxation.getCodeQR());
        
        // Informations bancaires
        dto.setNomBanque(taxation.getNomBanque());
        dto.setNumeroCompte(taxation.getNumeroCompte());
        dto.setIntituleCompte(taxation.getIntituleCompte());
        
        // Informations du contribuable
        if (taxation.getContribuableDirect() != null) {
            Contribuable contrib = taxation.getContribuableDirect();
            dto.setContribuableNom(contrib.getNom());
            dto.setContribuableNRC(contrib.getNRC());
            dto.setContribuableIdNat(contrib.getIdNat());
            dto.setContribuableAdresse(contrib.getAdressePrincipale());
            dto.setContribuableTelephone(contrib.getTelephonePrincipal());
            dto.setContribuableEmail(contrib.getEmail());
        }
        
        // Informations du véhicule
        if (vehicule != null) {
            dto.setVehiculeMarque(vehicule.getMarque());
            dto.setVehiculeModele(vehicule.getModele());
            dto.setVehiculeAnnee(vehicule.getAnnee());
            dto.setVehiculeNumeroChassis(vehicule.getNumeroChassis());
            dto.setVehiculeGenre(vehicule.getGenre());
            dto.setVehiculeCategorie(vehicule.getCategorie());
            dto.setVehiculePuissanceFiscale(vehicule.getPuissanceFiscale());
        }
        
        // Informations de l'agent (si disponible)
        if (taxation.getAgent() != null) {
            dto.setAgentNom(taxation.getAgent().getNom());
            dto.setAgentMatricule(taxation.getAgent().getMatricule());
            if (taxation.getAgent().getBureau() != null) {
                dto.setBureauNom(taxation.getAgent().getBureau().getNom());
            }
            if (taxation.getAgent().getDivision() != null) {
                dto.setDivisionNom(taxation.getAgent().getDivision().getNom());
            }
        }
        
        return dto;
    }
    
    /**
     * Récupère toutes les demandes avec leurs notes de taxation et plaques
     * 
     * @return Liste complète des demandes avec tous les détails
     */
    @Transactional(readOnly = true)
    public List<DemandePlaqueResponseDTO> getToutesLesDemandesAvecDetails() {
        logger.info("Récupération de toutes les demandes avec détails complets");
        
        List<DemandePlaque> demandes = demandePlaqueRepository.findAll();
        List<DemandePlaqueResponseDTO> result = new java.util.ArrayList<>();
        
        for (DemandePlaque demande : demandes) {
            // Récupérer les notes de taxation pour cette demande
            List<Taxation> taxations = taxationRepository.findByDemande(demande);
            
            NoteTaxationDTO notePlaque = null;
            NoteTaxationDTO noteVignette = null;
            
            for (Taxation taxation : taxations) {
                if (taxation.getTypeImpot() == TypeImpot.PLAQUE) {
                    notePlaque = convertirTaxationEnDTO(taxation, demande.getVehicule());
                } else if (taxation.getTypeImpot() == TypeImpot.IRV) {
                    noteVignette = convertirTaxationEnDTO(taxation, demande.getVehicule());
                }
            }
            
            DemandePlaqueResponseDTO dto = new DemandePlaqueResponseDTO(demande, notePlaque, noteVignette);
            result.add(dto);
        }
        
        logger.info("{} demandes récupérées avec détails", result.size());
        return result;
    }
    
    /**
     * Récupère les demandes par statut avec leurs notes de taxation et plaques
     * 
     * @param statut Statut des demandes
     * @return Liste des demandes avec tous les détails
     */
    @Transactional(readOnly = true)
    public List<DemandePlaqueResponseDTO> getDemandesAvecDetailsByStatut(StatutDemande statut) {
        logger.info("Récupération des demandes avec statut {} et détails complets", statut);
        
        List<DemandePlaque> demandes = demandePlaqueRepository.findByStatut(statut);
        List<DemandePlaqueResponseDTO> result = new java.util.ArrayList<>();
        
        for (DemandePlaque demande : demandes) {
            // Récupérer les notes de taxation pour cette demande
            List<Taxation> taxations = taxationRepository.findByDemande(demande);
            
            NoteTaxationDTO notePlaque = null;
            NoteTaxationDTO noteVignette = null;
            
            for (Taxation taxation : taxations) {
                if (taxation.getTypeImpot() == TypeImpot.PLAQUE) {
                    notePlaque = convertirTaxationEnDTO(taxation, demande.getVehicule());
                } else if (taxation.getTypeImpot() == TypeImpot.IRV) {
                    noteVignette = convertirTaxationEnDTO(taxation, demande.getVehicule());
                }
            }
            
            DemandePlaqueResponseDTO dto = new DemandePlaqueResponseDTO(demande, notePlaque, noteVignette);
            result.add(dto);
        }
        
        logger.info("{} demandes récupérées avec détails", result.size());
        return result;
    }
    
    /**
     * Récupère les demandes d'un contribuable avec leurs notes de taxation et plaques
     * 
     * @param contribuableId ID du contribuable
     * @return Liste des demandes avec tous les détails
     */
    @Transactional(readOnly = true)
    public List<DemandePlaqueResponseDTO> getDemandesAvecDetailsByContribuable(UUID contribuableId) {
        logger.info("Récupération des demandes du contribuable {} avec détails complets", contribuableId);
        
        List<DemandePlaque> demandes = demandePlaqueRepository.findByContribuable_Id(contribuableId);
        List<DemandePlaqueResponseDTO> result = new java.util.ArrayList<>();
        
        for (DemandePlaque demande : demandes) {
            // Récupérer les notes de taxation pour cette demande
            List<Taxation> taxations = taxationRepository.findByDemande(demande);
            
            NoteTaxationDTO notePlaque = null;
            NoteTaxationDTO noteVignette = null;
            
            for (Taxation taxation : taxations) {
                if (taxation.getTypeImpot() == TypeImpot.PLAQUE) {
                    notePlaque = convertirTaxationEnDTO(taxation, demande.getVehicule());
                } else if (taxation.getTypeImpot() == TypeImpot.IRV) {
                    noteVignette = convertirTaxationEnDTO(taxation, demande.getVehicule());
                }
            }
            
            DemandePlaqueResponseDTO dto = new DemandePlaqueResponseDTO(demande, notePlaque, noteVignette);
            result.add(dto);
        }
        
        logger.info("{} demandes récupérées avec détails", result.size());
        return result;
    }
}
