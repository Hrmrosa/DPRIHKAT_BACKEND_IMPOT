package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.RapportProgramme;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.FormatRapport;
import com.DPRIHKAT.entity.enums.FrequenceRapport;
import com.DPRIHKAT.repository.RapportProgrammeRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.*;
import java.util.*;

/**
 * Service pour gérer les rapports programmés
 */
@Service
public class RapportService {

    private final RapportProgrammeRepository rapportProgrammeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final DashboardService dashboardService;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public RapportService(
            RapportProgrammeRepository rapportProgrammeRepository,
            UtilisateurRepository utilisateurRepository,
            DashboardService dashboardService,
            EmailService emailService,
            ObjectMapper objectMapper) {
        this.rapportProgrammeRepository = rapportProgrammeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.dashboardService = dashboardService;
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    /**
     * Récupère tous les rapports d'un utilisateur
     */
    public List<RapportProgramme> getRapportsForUser(UUID utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        return rapportProgrammeRepository.findByUtilisateur(utilisateur);
    }
    
    /**
     * Récupère tous les rapports actifs d'un utilisateur
     */
    public List<RapportProgramme> getRapportsActifsForUser(UUID utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        return rapportProgrammeRepository.findByUtilisateurAndActif(utilisateur, true);
    }
    
    /**
     * Crée un nouveau rapport programmé
     */
    @Transactional
    public RapportProgramme createRapport(UUID utilisateurId, RapportProgramme rapportProgramme) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        rapportProgramme.setUtilisateur(utilisateur);
        rapportProgramme.setDerniereExecution(null);
        rapportProgramme.setProchaineExecution(calculerProchaineExecution(rapportProgramme));
        
        return rapportProgrammeRepository.save(rapportProgramme);
    }
    
    /**
     * Met à jour un rapport existant
     */
    @Transactional
    public RapportProgramme updateRapport(UUID rapportId, RapportProgramme updatedRapport) {
        RapportProgramme existingRapport = rapportProgrammeRepository.findById(rapportId)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé"));
        
        // Mettre à jour les propriétés
        existingRapport.setNom(updatedRapport.getNom());
        existingRapport.setDescription(updatedRapport.getDescription());
        existingRapport.setTypeRapport(updatedRapport.getTypeRapport());
        existingRapport.setFrequence(updatedRapport.getFrequence());
        existingRapport.setFormat(updatedRapport.getFormat());
        existingRapport.setDestinataires(updatedRapport.getDestinataires());
        existingRapport.setObjetEmail(updatedRapport.getObjetEmail());
        existingRapport.setCorpsEmail(updatedRapport.getCorpsEmail());
        existingRapport.setJourSemaine(updatedRapport.getJourSemaine());
        existingRapport.setJourMois(updatedRapport.getJourMois());
        existingRapport.setHeure(updatedRapport.getHeure());
        existingRapport.setMinute(updatedRapport.getMinute());
        existingRapport.setActif(updatedRapport.isActif());
        existingRapport.setParametres(updatedRapport.getParametres());
        existingRapport.setPeriodeDonnees(updatedRapport.getPeriodeDonnees());
        
        // Recalculer la prochaine exécution
        existingRapport.setProchaineExecution(calculerProchaineExecution(existingRapport));
        
        return rapportProgrammeRepository.save(existingRapport);
    }
    
    /**
     * Supprime un rapport
     */
    @Transactional
    public void deleteRapport(UUID rapportId) {
        rapportProgrammeRepository.deleteById(rapportId);
    }
    
    /**
     * Active ou désactive un rapport
     */
    @Transactional
    public RapportProgramme toggleRapport(UUID rapportId, boolean actif) {
        RapportProgramme rapport = rapportProgrammeRepository.findById(rapportId)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé"));
        
        rapport.setActif(actif);
        
        // Si on active le rapport, recalculer la prochaine exécution
        if (actif) {
            rapport.setProchaineExecution(calculerProchaineExecution(rapport));
        }
        
        return rapportProgrammeRepository.save(rapport);
    }
    
    /**
     * Exécute un rapport immédiatement
     */
    @Transactional
    public void executerRapportMaintenant(UUID rapportId) {
        RapportProgramme rapport = rapportProgrammeRepository.findById(rapportId)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé"));
        
        genererEtEnvoyerRapport(rapport);
        
        // Mettre à jour la dernière exécution et recalculer la prochaine
        rapport.setDerniereExecution(new Date());
        rapport.setProchaineExecution(calculerProchaineExecution(rapport));
        
        rapportProgrammeRepository.save(rapport);
    }
    
    /**
     * Vérifie et exécute tous les rapports programmés qui doivent être exécutés
     * Cette méthode est appelée périodiquement par un scheduler
     */
    @Transactional
    public void verifierEtExecuterRapportsProgrammes() {
        Date maintenant = new Date();
        List<RapportProgramme> rapportsAExecuter = rapportProgrammeRepository.findRapportsAExecuter(maintenant);
        
        for (RapportProgramme rapport : rapportsAExecuter) {
            genererEtEnvoyerRapport(rapport);
            
            // Mettre à jour la dernière exécution et recalculer la prochaine
            rapport.setDerniereExecution(maintenant);
            rapport.setProchaineExecution(calculerProchaineExecution(rapport));
            
            rapportProgrammeRepository.save(rapport);
        }
    }
    
    /**
     * Calcule la date de la prochaine exécution d'un rapport
     */
    private Date calculerProchaineExecution(RapportProgramme rapport) {
        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime prochaine = null;
        
        // Créer une date avec l'heure et la minute spécifiées
        LocalDateTime baseDateTime = LocalDateTime.of(
            maintenant.getYear(),
            maintenant.getMonth(),
            maintenant.getDayOfMonth(),
            rapport.getHeure() != null ? rapport.getHeure() : 0,
            rapport.getMinute() != null ? rapport.getMinute() : 0
        );
        
        switch (rapport.getFrequence()) {
            case QUOTIDIEN:
                prochaine = baseDateTime;
                if (prochaine.isBefore(maintenant)) {
                    prochaine = prochaine.plusDays(1);
                }
                break;
                
            case HEBDOMADAIRE:
                int jourSemaine = rapport.getJourSemaine() != null ? rapport.getJourSemaine() : 1; // 1 = Lundi
                prochaine = baseDateTime.with(DayOfWeek.of(jourSemaine));
                if (prochaine.isBefore(maintenant)) {
                    prochaine = prochaine.plusWeeks(1);
                }
                break;
                
            case MENSUEL:
                int jourMois = rapport.getJourMois() != null ? rapport.getJourMois() : 1;
                prochaine = baseDateTime.withDayOfMonth(Math.min(jourMois, baseDateTime.toLocalDate().lengthOfMonth()));
                if (prochaine.isBefore(maintenant)) {
                    prochaine = prochaine.plusMonths(1);
                    // Ajuster pour le nombre de jours dans le mois
                    prochaine = prochaine.withDayOfMonth(Math.min(jourMois, prochaine.toLocalDate().lengthOfMonth()));
                }
                break;
                
            case TRIMESTRIEL:
                int moisActuel = maintenant.getMonthValue();
                int moisProchain = ((moisActuel - 1) / 3 * 3 + 3) % 12 + 1; // Prochain trimestre
                prochaine = baseDateTime.withMonth(moisProchain).withDayOfMonth(1);
                if (prochaine.isBefore(maintenant)) {
                    prochaine = prochaine.plusMonths(3);
                }
                break;
                
            case SEMESTRIEL:
                moisActuel = maintenant.getMonthValue();
                moisProchain = moisActuel <= 6 ? 7 : 1; // Juillet ou Janvier
                prochaine = baseDateTime.withMonth(moisProchain).withDayOfMonth(1);
                if (prochaine.isBefore(maintenant)) {
                    prochaine = prochaine.plusMonths(6);
                }
                break;
                
            case ANNUEL:
                prochaine = baseDateTime.withMonth(1).withDayOfMonth(1); // 1er janvier
                if (prochaine.isBefore(maintenant)) {
                    prochaine = prochaine.plusYears(1);
                }
                break;
                
            default:
                prochaine = maintenant.plusDays(1); // Par défaut, demain
        }
        
        return Date.from(prochaine.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * Génère et envoie un rapport
     */
    private void genererEtEnvoyerRapport(RapportProgramme rapport) {
        try {
            // Générer le contenu du rapport selon son type
            Map<String, Object> donnees = genererDonneesRapport(rapport);
            
            // Générer le fichier du rapport selon le format demandé
            File fichierRapport = genererFichierRapport(rapport, donnees);
            
            // Envoyer le rapport par email
            envoyerRapportParEmail(rapport, fichierRapport);
            
        } catch (Exception e) {
            // Logger l'erreur
            e.printStackTrace();
        }
    }
    
    /**
     * Génère les données pour un rapport
     */
    private Map<String, Object> genererDonneesRapport(RapportProgramme rapport) {
        Map<String, Object> donnees = new HashMap<>();
        
        // Selon le type de rapport, récupérer les données appropriées
        switch (rapport.getTypeRapport()) {
            case "dashboard":
                donnees = dashboardService.getAdminDashboardData();
                break;
            case "taxations":
                // TODO: Implémenter la récupération des données de taxation
                break;
            case "paiements":
                // TODO: Implémenter la récupération des données de paiement
                break;
            default:
                donnees = new HashMap<>();
        }
        
        return donnees;
    }
    
    /**
     * Génère un fichier de rapport selon le format demandé
     */
    private File genererFichierRapport(RapportProgramme rapport, Map<String, Object> donnees) {
        // TODO: Implémenter la génération de fichiers selon le format
        return null;
    }
    
    /**
     * Envoie un rapport par email
     */
    private void envoyerRapportParEmail(RapportProgramme rapport, File fichierRapport) {
        // TODO: Implémenter l'envoi d'email avec pièce jointe
    }
}
