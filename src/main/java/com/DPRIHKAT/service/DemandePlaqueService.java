package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.DemandePlaque;
import com.DPRIHKAT.entity.Plaque;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.entity.enums.StatutDemande;
import com.DPRIHKAT.entity.enums.StatutPlaque;
import com.DPRIHKAT.repository.ContribuableRepository;
import com.DPRIHKAT.repository.DemandePlaqueRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    
    /**
     * Soumettre une nouvelle demande de plaque
     * 
     * @param contribuableId ID du contribuable
     * @param vehiculeId ID du véhicule
     * @param facture Facture du véhicule
     * @return La demande créée
     */
    @Transactional
    public DemandePlaque soumettreDemande(UUID contribuableId, UUID vehiculeId, MultipartFile facture) {
        logger.info("Soumission d'une demande de plaque pour le contribuable {} et le véhicule {}", contribuableId, vehiculeId);
        
        // Vérifier que le contribuable existe
        Contribuable contribuable = contribuableRepository.findById(contribuableId)
                .orElseThrow(() -> new RuntimeException("Contribuable non trouvé avec ID: " + contribuableId));
        
        // Vérifier que le véhicule existe et appartient au contribuable
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé avec ID: " + vehiculeId));
        
        if (!vehicule.getProprietaire().getId().equals(contribuableId)) {
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
        demande.setStatut(StatutDemande.SOUMISE);
        demande.setDateDemande(new Date());
        
        logger.info("Demande de plaque créée avec succès");
        return demandePlaqueRepository.save(demande);
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
        return demandePlaqueRepository.findByContribuableId(contribuableId);
    }
    
    /**
     * Récupérer les demandes par véhicule
     * 
     * @param vehiculeId ID du véhicule
     * @return Liste des demandes
     */
    public List<DemandePlaque> getDemandesByVehicule(UUID vehiculeId) {
        return demandePlaqueRepository.findByVehiculeId(vehiculeId);
    }
    
    /**
     * Récupérer les demandes par contribuable et statut
     * 
     * @param contribuableId ID du contribuable
     * @param statut Statut des demandes
     * @return Liste des demandes
     */
    public List<DemandePlaque> getDemandesByContribuableAndStatut(UUID contribuableId, StatutDemande statut) {
        return demandePlaqueRepository.findByContribuableIdAndStatut(contribuableId, statut);
    }
    
    /**
     * Récupérer les demandes validées par un utilisateur
     * 
     * @param validateurId ID du validateur
     * @return Liste des demandes
     */
    public List<DemandePlaque> getDemandesByValidateur(UUID validateurId) {
        return demandePlaqueRepository.findByValidateurId(validateurId);
    }
}
