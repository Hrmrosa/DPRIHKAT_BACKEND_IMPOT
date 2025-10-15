package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.StatutDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeRedressement;
import com.DPRIHKAT.repository.DocumentRecouvrementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DocumentRecouvrementService {

    @Autowired
    private DocumentRecouvrementRepository documentRecouvrementRepository;
    
    @Autowired
    private DossierRecouvrementService dossierRecouvrementService;
    
    @Autowired
    private AgentService agentService;
    
    @Autowired
    private ContribuableService contribuableService;

    /**
     * Récupère tous les documents de recouvrement
     * @return Liste des documents de recouvrement
     */
    public List<DocumentRecouvrement> findAll() {
        return documentRecouvrementRepository.findAll();
    }

    /**
     * Récupère un document de recouvrement par son ID
     * @param id ID du document
     * @return Document de recouvrement ou null si non trouvé
     */
    public DocumentRecouvrement findById(UUID id) {
        return documentRecouvrementRepository.findById(id).orElse(null);
    }

    /**
     * Enregistre un document de recouvrement
     * @param document Document à enregistrer
     * @return Document enregistré
     */
    @Transactional
    public DocumentRecouvrement save(DocumentRecouvrement document) {
        return documentRecouvrementRepository.save(document);
    }

    /**
     * Met à jour un document de recouvrement
     * @param id ID du document à mettre à jour
     * @param document Document avec les nouvelles valeurs
     * @return Document mis à jour ou null si non trouvé
     */
    @Transactional
    public DocumentRecouvrement update(UUID id, DocumentRecouvrement document) {
        if (documentRecouvrementRepository.existsById(id)) {
            document.setId(id);
            return documentRecouvrementRepository.save(document);
        }
        return null;
    }

    /**
     * Supprime un document de recouvrement
     * @param id ID du document à supprimer
     */
    @Transactional
    public void deleteById(UUID id) {
        documentRecouvrementRepository.deleteById(id);
    }

    /**
     * Récupère les documents de recouvrement par type
     * @param type Type de document
     * @return Liste des documents du type spécifié
     */
    public List<DocumentRecouvrement> findByType(TypeDocumentRecouvrement type) {
        return documentRecouvrementRepository.findByType(type);
    }

    /**
     * Récupère les documents de recouvrement par statut
     * @param statut Statut des documents
     * @return Liste des documents avec le statut spécifié
     */
    public List<DocumentRecouvrement> findByStatut(StatutDocumentRecouvrement statut) {
        return documentRecouvrementRepository.findByStatut(statut);
    }

    /**
     * Récupère les documents de recouvrement pour un contribuable
     * @param contribuableId ID du contribuable
     * @return Liste des documents du contribuable
     */
    public List<DocumentRecouvrement> findByContribuableId(UUID contribuableId) {
        return documentRecouvrementRepository.findByContribuableId(contribuableId);
    }

    /**
     * Récupère les documents de recouvrement pour un dossier
     * @param dossierRecouvrementId ID du dossier de recouvrement
     * @return Liste des documents du dossier
     */
    public List<DocumentRecouvrement> findByDossierRecouvrementId(UUID dossierRecouvrementId) {
        return documentRecouvrementRepository.findByDossierRecouvrementId(dossierRecouvrementId);
    }

    /**
     * Récupère les documents de recouvrement avec pagination et filtres
     * @param type Type de document (optionnel)
     * @param statut Statut du document (optionnel)
     * @param contribuableId ID du contribuable (optionnel)
     * @param dossierRecouvrementId ID du dossier (optionnel)
     * @param dateDebut Date de début (optionnel)
     * @param dateFin Date de fin (optionnel)
     * @param pageable Informations de pagination
     * @return Page de documents correspondant aux critères
     */
    public Page<DocumentRecouvrement> findWithFilters(
            TypeDocumentRecouvrement type,
            StatutDocumentRecouvrement statut,
            UUID contribuableId,
            UUID dossierRecouvrementId,
            Date dateDebut,
            Date dateFin,
            Pageable pageable) {
        
        return documentRecouvrementRepository.findDocumentsWithFilters(
                type,
                statut,
                contribuableId,
                dossierRecouvrementId,
                dateDebut != null,
                dateDebut,
                dateFin != null,
                dateFin,
                pageable);
    }

    /**
     * Récupère les documents en retard de paiement
     * @param type Type de document
     * @return Liste des documents en retard
     */
    public List<DocumentRecouvrement> findDocumentsEnRetard(TypeDocumentRecouvrement type) {
        return documentRecouvrementRepository.findDocumentsEnRetard(
                type,
                StatutDocumentRecouvrement.NOTIFIE,
                new Date());
    }

    /**
     * Crée un Avis de Mise en Recouvrement (AMR)
     * @param dossierRecouvrementId ID du dossier de recouvrement
     * @param contribuableId ID du contribuable
     * @param agentId ID de l'agent générateur
     * @param montantPrincipal Montant principal
     * @param montantPenalites Montant des pénalités
     * @param typeRedressement Type de redressement
     * @param baseImposable Base imposable
     * @param declarationId ID de la déclaration (optionnel)
     * @return AMR créé
     */
    @Transactional
    public AvisMiseRecouvrement creerAMR(
            UUID dossierRecouvrementId,
            UUID contribuableId,
            UUID agentId,
            Double montantPrincipal,
            Double montantPenalites,
            TypeRedressement typeRedressement,
            String baseImposable,
            UUID declarationId) {
        
        DossierRecouvrement dossier = dossierRecouvrementService.findById(dossierRecouvrementId);
        Contribuable contribuable = contribuableService.findById(contribuableId);
        Agent agent = agentService.findById(agentId);
        Declaration declaration = null;
        
        if (declarationId != null) {
            // Récupérer la déclaration (à implémenter)
            // declaration = declarationService.findById(declarationId);
        }
        
        // Générer une référence unique pour l'AMR
        String reference = "AMR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        AvisMiseRecouvrement amr = new AvisMiseRecouvrement(
                StatutDocumentRecouvrement.GENERE,
                new Date(),
                reference,
                montantPrincipal,
                montantPenalites,
                dossier,
                contribuable,
                agent,
                typeRedressement,
                baseImposable,
                declaration);
        
        return (AvisMiseRecouvrement) documentRecouvrementRepository.save(amr);
    }

    /**
     * Crée une Mise En Demeure de payer (MED)
     * @param dossierRecouvrementId ID du dossier de recouvrement
     * @param contribuableId ID du contribuable
     * @param agentId ID de l'agent générateur
     * @param montantPrincipal Montant principal
     * @param montantPenalites Montant des pénalités
     * @param declarationId ID de la déclaration (optionnel)
     * @param paiementInsuffisant Indique si le paiement est insuffisant
     * @param montantPaye Montant déjà payé
     * @return MED créée
     */
    @Transactional
    public MiseEnDemeure creerMED(
            UUID dossierRecouvrementId,
            UUID contribuableId,
            UUID agentId,
            Double montantPrincipal,
            Double montantPenalites,
            UUID declarationId,
            boolean paiementInsuffisant,
            Double montantPaye) {
        
        DossierRecouvrement dossier = dossierRecouvrementService.findById(dossierRecouvrementId);
        Contribuable contribuable = contribuableService.findById(contribuableId);
        Agent agent = agentService.findById(agentId);
        Declaration declaration = null;
        
        if (declarationId != null) {
            // Récupérer la déclaration (à implémenter)
            // declaration = declarationService.findById(declarationId);
        }
        
        // Générer une référence unique pour la MED
        String reference = "MED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        MiseEnDemeure med = new MiseEnDemeure(
                StatutDocumentRecouvrement.GENERE,
                new Date(),
                reference,
                montantPrincipal,
                montantPenalites,
                dossier,
                contribuable,
                agent,
                declaration,
                paiementInsuffisant,
                montantPaye);
        
        return (MiseEnDemeure) documentRecouvrementRepository.save(med);
    }

    /**
     * Crée une Contrainte Fiscale
     * @param dossierRecouvrementId ID du dossier de recouvrement
     * @param contribuableId ID du contribuable
     * @param agentId ID de l'agent générateur
     * @param receveurId ID du receveur
     * @param montantPrincipal Montant principal
     * @param montantPenalites Montant des pénalités
     * @return Contrainte créée
     */
    @Transactional
    public ContrainteFiscale creerContrainte(
            UUID dossierRecouvrementId,
            UUID contribuableId,
            UUID agentId,
            UUID receveurId,
            Double montantPrincipal,
            Double montantPenalites) {
        
        DossierRecouvrement dossier = dossierRecouvrementService.findById(dossierRecouvrementId);
        Contribuable contribuable = contribuableService.findById(contribuableId);
        Agent agent = agentService.findById(agentId);
        Agent receveur = agentService.findById(receveurId);
        
        // Générer une référence unique pour la contrainte
        String reference = "CONT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        ContrainteFiscale contrainte = new ContrainteFiscale(
                StatutDocumentRecouvrement.GENERE,
                new Date(),
                reference,
                montantPrincipal,
                montantPenalites,
                dossier,
                contribuable,
                agent,
                receveur);
        
        return (ContrainteFiscale) documentRecouvrementRepository.save(contrainte);
    }

    /**
     * Crée un Commandement de Payer
     * @param dossierRecouvrementId ID du dossier de recouvrement
     * @param contribuableId ID du contribuable
     * @param agentId ID de l'agent générateur
     * @param huissierId ID de l'huissier
     * @param montantPrincipal Montant principal
     * @param montantPenalites Montant des pénalités
     * @return Commandement créé
     */
    @Transactional
    public CommandementPayer creerCommandement(
            UUID dossierRecouvrementId,
            UUID contribuableId,
            UUID agentId,
            UUID huissierId,
            Double montantPrincipal,
            Double montantPenalites) {
        
        DossierRecouvrement dossier = dossierRecouvrementService.findById(dossierRecouvrementId);
        Contribuable contribuable = contribuableService.findById(contribuableId);
        Agent agent = agentService.findById(agentId);
        Agent huissier = agentService.findById(huissierId);
        
        // Générer une référence unique pour le commandement
        String reference = "CMD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        CommandementPayer commandement = new CommandementPayer(
                StatutDocumentRecouvrement.GENERE,
                new Date(),
                reference,
                montantPrincipal,
                montantPenalites,
                dossier,
                contribuable,
                agent,
                huissier);
        
        return (CommandementPayer) documentRecouvrementRepository.save(commandement);
    }

    /**
     * Crée un Avis à Tiers Détenteur (ATD)
     * @param dossierRecouvrementId ID du dossier de recouvrement
     * @param contribuableId ID du contribuable
     * @param agentId ID de l'agent générateur
     * @param montantPrincipal Montant principal
     * @param montantPenalites Montant des pénalités
     * @param nomTiersDetenteur Nom du tiers détenteur
     * @param adresseTiersDetenteur Adresse du tiers détenteur
     * @param qualiteTiersDetenteur Qualité du tiers détenteur
     * @return ATD créé
     */
    @Transactional
    public AvisTiersDetenteur creerATD(
            UUID dossierRecouvrementId,
            UUID contribuableId,
            UUID agentId,
            Double montantPrincipal,
            Double montantPenalites,
            String nomTiersDetenteur,
            String adresseTiersDetenteur,
            String qualiteTiersDetenteur) {
        
        DossierRecouvrement dossier = dossierRecouvrementService.findById(dossierRecouvrementId);
        Contribuable contribuable = contribuableService.findById(contribuableId);
        Agent agent = agentService.findById(agentId);
        
        // Générer une référence unique pour l'ATD
        String reference = "ATD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        AvisTiersDetenteur atd = new AvisTiersDetenteur(
                StatutDocumentRecouvrement.GENERE,
                new Date(),
                reference,
                montantPrincipal,
                montantPenalites,
                dossier,
                contribuable,
                agent,
                nomTiersDetenteur,
                adresseTiersDetenteur,
                qualiteTiersDetenteur);
        
        return (AvisTiersDetenteur) documentRecouvrementRepository.save(atd);
    }

    /**
     * Crée une Saisie Mobilière
     * @param dossierRecouvrementId ID du dossier de recouvrement
     * @param contribuableId ID du contribuable
     * @param agentId ID de l'agent générateur
     * @param huissierId ID de l'huissier
     * @param montantPrincipal Montant principal
     * @param montantPenalites Montant des pénalités
     * @param descriptionBiensSaisis Description des biens saisis
     * @param valeurEstimeeBiens Valeur estimée des biens
     * @param typeBiensMobiliers Type de biens mobiliers
     * @param lieuStockage Lieu de stockage
     * @param gardien Gardien des biens
     * @return Saisie mobilière créée
     */
    @Transactional
    public SaisieMobiliere creerSaisieMobiliere(
            UUID dossierRecouvrementId,
            UUID contribuableId,
            UUID agentId,
            UUID huissierId,
            Double montantPrincipal,
            Double montantPenalites,
            String descriptionBiensSaisis,
            Double valeurEstimeeBiens,
            String typeBiensMobiliers,
            String lieuStockage,
            String gardien) {
        
        DossierRecouvrement dossier = dossierRecouvrementService.findById(dossierRecouvrementId);
        Contribuable contribuable = contribuableService.findById(contribuableId);
        Agent agent = agentService.findById(agentId);
        Agent huissier = agentService.findById(huissierId);
        
        // Générer une référence unique pour la saisie
        String reference = "SM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        SaisieMobiliere saisie = new SaisieMobiliere(
                StatutDocumentRecouvrement.GENERE,
                new Date(),
                reference,
                montantPrincipal,
                montantPenalites,
                dossier,
                contribuable,
                agent,
                huissier,
                descriptionBiensSaisis,
                valeurEstimeeBiens,
                typeBiensMobiliers,
                lieuStockage,
                gardien);
        
        return (SaisieMobiliere) documentRecouvrementRepository.save(saisie);
    }

    /**
     * Crée une Saisie Immobilière
     * @param dossierRecouvrementId ID du dossier de recouvrement
     * @param contribuableId ID du contribuable
     * @param agentId ID de l'agent générateur
     * @param huissierId ID de l'huissier
     * @param montantPrincipal Montant principal
     * @param montantPenalites Montant des pénalités
     * @param descriptionBiensSaisis Description des biens saisis
     * @param valeurEstimeeBiens Valeur estimée des biens
     * @param referencesCadastrales Références cadastrales
     * @param adresseBien Adresse du bien
     * @param superficie Superficie du bien
     * @param titresPropriete Titres de propriété
     * @return Saisie immobilière créée
     */
    @Transactional
    public SaisieImmobiliere creerSaisieImmobiliere(
            UUID dossierRecouvrementId,
            UUID contribuableId,
            UUID agentId,
            UUID huissierId,
            Double montantPrincipal,
            Double montantPenalites,
            String descriptionBiensSaisis,
            Double valeurEstimeeBiens,
            String referencesCadastrales,
            String adresseBien,
            Double superficie,
            String titresPropriete) {
        
        DossierRecouvrement dossier = dossierRecouvrementService.findById(dossierRecouvrementId);
        Contribuable contribuable = contribuableService.findById(contribuableId);
        Agent agent = agentService.findById(agentId);
        Agent huissier = agentService.findById(huissierId);
        
        // Générer une référence unique pour la saisie
        String reference = "SI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        SaisieImmobiliere saisie = new SaisieImmobiliere(
                StatutDocumentRecouvrement.GENERE,
                new Date(),
                reference,
                montantPrincipal,
                montantPenalites,
                dossier,
                contribuable,
                agent,
                huissier,
                descriptionBiensSaisis,
                valeurEstimeeBiens,
                referencesCadastrales,
                adresseBien,
                superficie,
                titresPropriete);
        
        return (SaisieImmobiliere) documentRecouvrementRepository.save(saisie);
    }

    /**
     * Crée une Fermeture d'Établissement
     * @param dossierRecouvrementId ID du dossier de recouvrement
     * @param contribuableId ID du contribuable
     * @param agentId ID de l'agent générateur
     * @param agentOPJId ID de l'agent OPJ
     * @param montantPrincipal Montant principal
     * @param montantPenalites Montant des pénalités
     * @param adresseEtablissement Adresse de l'établissement
     * @param motifFermeture Motif de la fermeture
     * @return Fermeture d'établissement créée
     */
    @Transactional
    public FermetureEtablissement creerFermetureEtablissement(
            UUID dossierRecouvrementId,
            UUID contribuableId,
            UUID agentId,
            UUID agentOPJId,
            Double montantPrincipal,
            Double montantPenalites,
            String adresseEtablissement,
            String motifFermeture) {
        
        DossierRecouvrement dossier = dossierRecouvrementService.findById(dossierRecouvrementId);
        Contribuable contribuable = contribuableService.findById(contribuableId);
        Agent agent = agentService.findById(agentId);
        Agent agentOPJ = agentService.findById(agentOPJId);
        
        // Générer une référence unique pour la fermeture
        String reference = "FE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        FermetureEtablissement fermeture = new FermetureEtablissement(
                StatutDocumentRecouvrement.GENERE,
                new Date(),
                reference,
                montantPrincipal,
                montantPenalites,
                dossier,
                contribuable,
                agent,
                agentOPJ,
                adresseEtablissement,
                motifFermeture);
        
        return (FermetureEtablissement) documentRecouvrementRepository.save(fermeture);
    }

    /**
     * Met à jour le statut d'un document de recouvrement
     * @param id ID du document
     * @param statut Nouveau statut
     * @return Document mis à jour
     */
    @Transactional
    public DocumentRecouvrement updateStatut(UUID id, StatutDocumentRecouvrement statut) {
        DocumentRecouvrement document = documentRecouvrementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));
        
        document.setStatut(statut);
        
        if (statut == StatutDocumentRecouvrement.NOTIFIE) {
            document.setDateNotification(new Date());
        }
        
        return documentRecouvrementRepository.save(document);
    }
}
