package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.ComparaisonDonnees;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.PeriodeComparaison;
import com.DPRIHKAT.entity.enums.TypeComparaison;
import com.DPRIHKAT.repository.ComparaisonDonneesRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

/**
 * Service pour gérer les comparaisons de données
 */
@Service
public class ComparaisonService {

    private final ComparaisonDonneesRepository comparaisonDonneesRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final DashboardService dashboardService;
    private final ObjectMapper objectMapper;

    public ComparaisonService(
            ComparaisonDonneesRepository comparaisonDonneesRepository,
            UtilisateurRepository utilisateurRepository,
            DashboardService dashboardService,
            ObjectMapper objectMapper) {
        this.comparaisonDonneesRepository = comparaisonDonneesRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.dashboardService = dashboardService;
        this.objectMapper = objectMapper;
    }

    /**
     * Récupère toutes les comparaisons d'un utilisateur
     */
    public List<ComparaisonDonnees> getComparaisonsForUser(UUID utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        return comparaisonDonneesRepository.findByUtilisateur(utilisateur);
    }
    
    /**
     * Récupère toutes les comparaisons actives d'un utilisateur
     */
    public List<ComparaisonDonnees> getComparaisonsActivesForUser(UUID utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        return comparaisonDonneesRepository.findByUtilisateurAndActive(utilisateur, true);
    }
    
    /**
     * Crée une nouvelle comparaison
     */
    @Transactional
    public ComparaisonDonnees createComparaison(UUID utilisateurId, ComparaisonDonnees comparaisonDonnees) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        comparaisonDonnees.setUtilisateur(utilisateur);
        comparaisonDonnees.setDateCreation(new Date());
        comparaisonDonnees.setDerniereExecution(null);
        
        // Calculer les dates de début et fin des périodes
        calculerPeriodes(comparaisonDonnees);
        
        if (comparaisonDonnees.isExecutionAutomatique() && comparaisonDonnees.getFrequenceExecution() != null) {
            comparaisonDonnees.setProchaineExecution(calculerProchaineExecution(comparaisonDonnees));
        }
        
        return comparaisonDonneesRepository.save(comparaisonDonnees);
    }
    
    /**
     * Met à jour une comparaison existante
     */
    @Transactional
    public ComparaisonDonnees updateComparaison(UUID comparaisonId, ComparaisonDonnees updatedComparaison) {
        ComparaisonDonnees existingComparaison = comparaisonDonneesRepository.findById(comparaisonId)
                .orElseThrow(() -> new RuntimeException("Comparaison non trouvée"));
        
        // Mettre à jour les propriétés
        existingComparaison.setNom(updatedComparaison.getNom());
        existingComparaison.setDescription(updatedComparaison.getDescription());
        existingComparaison.setTypeComparaison(updatedComparaison.getTypeComparaison());
        existingComparaison.setPeriodeReference(updatedComparaison.getPeriodeReference());
        existingComparaison.setPeriodeComparaison(updatedComparaison.getPeriodeComparaison());
        existingComparaison.setParametres(updatedComparaison.getParametres());
        existingComparaison.setActive(updatedComparaison.isActive());
        existingComparaison.setExecutionAutomatique(updatedComparaison.isExecutionAutomatique());
        existingComparaison.setFrequenceExecution(updatedComparaison.getFrequenceExecution());
        existingComparaison.setFiltres(updatedComparaison.getFiltres());
        
        // Recalculer les périodes
        calculerPeriodes(existingComparaison);
        
        // Recalculer la prochaine exécution si nécessaire
        if (existingComparaison.isExecutionAutomatique() && existingComparaison.getFrequenceExecution() != null) {
            existingComparaison.setProchaineExecution(calculerProchaineExecution(existingComparaison));
        }
        
        return comparaisonDonneesRepository.save(existingComparaison);
    }
    
    /**
     * Supprime une comparaison
     */
    @Transactional
    public void deleteComparaison(UUID comparaisonId) {
        comparaisonDonneesRepository.deleteById(comparaisonId);
    }
    
    /**
     * Active ou désactive une comparaison
     */
    @Transactional
    public ComparaisonDonnees toggleComparaison(UUID comparaisonId, boolean active) {
        ComparaisonDonnees comparaison = comparaisonDonneesRepository.findById(comparaisonId)
                .orElseThrow(() -> new RuntimeException("Comparaison non trouvée"));
        
        comparaison.setActive(active);
        
        return comparaisonDonneesRepository.save(comparaison);
    }
    
    /**
     * Exécute une comparaison immédiatement
     */
    @Transactional
    public ComparaisonDonnees executerComparaisonMaintenant(UUID comparaisonId) {
        ComparaisonDonnees comparaison = comparaisonDonneesRepository.findById(comparaisonId)
                .orElseThrow(() -> new RuntimeException("Comparaison non trouvée"));
        
        // Recalculer les périodes
        calculerPeriodes(comparaison);
        
        // Exécuter la comparaison
        Map<String, Object> resultats = executerComparaison(comparaison);
        
        // Mettre à jour les résultats
        try {
            comparaison.setResultats(objectMapper.writeValueAsString(resultats));
        } catch (Exception e) {
            comparaison.setResultats("{}");
        }
        
        // Mettre à jour la date de dernière exécution
        comparaison.setDerniereExecution(new Date());
        
        // Recalculer la prochaine exécution si nécessaire
        if (comparaison.isExecutionAutomatique() && comparaison.getFrequenceExecution() != null) {
            comparaison.setProchaineExecution(calculerProchaineExecution(comparaison));
        }
        
        return comparaisonDonneesRepository.save(comparaison);
    }
    
    /**
     * Vérifie et exécute toutes les comparaisons programmées qui doivent être exécutées
     * Cette méthode est appelée périodiquement par un scheduler
     */
    @Transactional
    public void verifierEtExecuterComparaisonsProgrammees() {
        Date maintenant = new Date();
        List<ComparaisonDonnees> comparaisonsAExecuter = comparaisonDonneesRepository.findComparaisonsAExecuter(maintenant);
        
        for (ComparaisonDonnees comparaison : comparaisonsAExecuter) {
            // Recalculer les périodes
            calculerPeriodes(comparaison);
            
            // Exécuter la comparaison
            Map<String, Object> resultats = executerComparaison(comparaison);
            
            // Mettre à jour les résultats
            try {
                comparaison.setResultats(objectMapper.writeValueAsString(resultats));
            } catch (Exception e) {
                comparaison.setResultats("{}");
            }
            
            // Mettre à jour la date de dernière exécution
            comparaison.setDerniereExecution(maintenant);
            
            // Recalculer la prochaine exécution
            comparaison.setProchaineExecution(calculerProchaineExecution(comparaison));
            
            comparaisonDonneesRepository.save(comparaison);
        }
    }
    
    /**
     * Calcule les dates de début et fin des périodes de référence et de comparaison
     */
    private void calculerPeriodes(ComparaisonDonnees comparaison) {
        LocalDate maintenant = LocalDate.now();
        
        // Calculer la période de référence
        switch (comparaison.getPeriodeReference()) {
            case ANNEE_PRECEDENTE:
                LocalDate debutAnneePrecedente = LocalDate.of(maintenant.getYear() - 1, 1, 1);
                LocalDate finAnneePrecedente = LocalDate.of(maintenant.getYear() - 1, 12, 31);
                comparaison.setDateDebutReference(Date.from(debutAnneePrecedente.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinReference(Date.from(finAnneePrecedente.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case ANNEE_EN_COURS:
                LocalDate debutAnneeEnCours = LocalDate.of(maintenant.getYear(), 1, 1);
                comparaison.setDateDebutReference(Date.from(debutAnneeEnCours.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinReference(Date.from(maintenant.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case TRIMESTRE_PRECEDENT:
                LocalDate finTrimestrePrecedent = YearMonth.from(maintenant.withDayOfMonth(1).minusMonths(1)).atEndOfMonth();
                LocalDate debutTrimestrePrecedent = finTrimestrePrecedent.minusMonths(2).withDayOfMonth(1);
                comparaison.setDateDebutReference(Date.from(debutTrimestrePrecedent.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinReference(Date.from(finTrimestrePrecedent.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case TRIMESTRE_EN_COURS:
                int moisActuel = maintenant.getMonthValue();
                int debutTrimestreActuel = ((moisActuel - 1) / 3) * 3 + 1;
                LocalDate debutTrimestre = LocalDate.of(maintenant.getYear(), debutTrimestreActuel, 1);
                comparaison.setDateDebutReference(Date.from(debutTrimestre.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinReference(Date.from(maintenant.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case MOIS_PRECEDENT:
                LocalDate debutMoisPrecedent = YearMonth.from(maintenant).minusMonths(1).atDay(1);
                LocalDate finMoisPrecedent = YearMonth.from(maintenant).minusMonths(1).atEndOfMonth();
                comparaison.setDateDebutReference(Date.from(debutMoisPrecedent.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinReference(Date.from(finMoisPrecedent.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case MOIS_EN_COURS:
                LocalDate debutMoisEnCours = YearMonth.from(maintenant).atDay(1);
                comparaison.setDateDebutReference(Date.from(debutMoisEnCours.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinReference(Date.from(maintenant.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case SEMAINE_PRECEDENTE:
                LocalDate finSemainePrecedente = maintenant.minusDays(maintenant.getDayOfWeek().getValue());
                LocalDate debutSemainePrecedente = finSemainePrecedente.minusDays(6);
                comparaison.setDateDebutReference(Date.from(debutSemainePrecedente.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinReference(Date.from(finSemainePrecedente.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case SEMAINE_EN_COURS:
                LocalDate debutSemaineEnCours = maintenant.minusDays(maintenant.getDayOfWeek().getValue() - 1);
                comparaison.setDateDebutReference(Date.from(debutSemaineEnCours.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinReference(Date.from(maintenant.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case PERSONNALISEE:
                // Les dates sont déjà définies manuellement
                break;
        }
        
        // Calculer la période de comparaison
        switch (comparaison.getPeriodeComparaison()) {
            case ANNEE_PRECEDENTE:
                LocalDate debutAnneePrecedente = LocalDate.of(maintenant.getYear() - 1, 1, 1);
                LocalDate finAnneePrecedente = LocalDate.of(maintenant.getYear() - 1, 12, 31);
                comparaison.setDateDebutComparaison(Date.from(debutAnneePrecedente.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinComparaison(Date.from(finAnneePrecedente.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case ANNEE_EN_COURS:
                LocalDate debutAnneeEnCours = LocalDate.of(maintenant.getYear(), 1, 1);
                comparaison.setDateDebutComparaison(Date.from(debutAnneeEnCours.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinComparaison(Date.from(maintenant.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case TRIMESTRE_PRECEDENT:
                LocalDate finTrimestrePrecedent = YearMonth.from(maintenant.withDayOfMonth(1).minusMonths(1)).atEndOfMonth();
                LocalDate debutTrimestrePrecedent = finTrimestrePrecedent.minusMonths(2).withDayOfMonth(1);
                comparaison.setDateDebutComparaison(Date.from(debutTrimestrePrecedent.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinComparaison(Date.from(finTrimestrePrecedent.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case TRIMESTRE_EN_COURS:
                int moisActuel = maintenant.getMonthValue();
                int debutTrimestreActuel = ((moisActuel - 1) / 3) * 3 + 1;
                LocalDate debutTrimestre = LocalDate.of(maintenant.getYear(), debutTrimestreActuel, 1);
                comparaison.setDateDebutComparaison(Date.from(debutTrimestre.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinComparaison(Date.from(maintenant.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case MOIS_PRECEDENT:
                LocalDate debutMoisPrecedent = YearMonth.from(maintenant).minusMonths(1).atDay(1);
                LocalDate finMoisPrecedent = YearMonth.from(maintenant).minusMonths(1).atEndOfMonth();
                comparaison.setDateDebutComparaison(Date.from(debutMoisPrecedent.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinComparaison(Date.from(finMoisPrecedent.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case MOIS_EN_COURS:
                LocalDate debutMoisEnCours = YearMonth.from(maintenant).atDay(1);
                comparaison.setDateDebutComparaison(Date.from(debutMoisEnCours.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinComparaison(Date.from(maintenant.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case SEMAINE_PRECEDENTE:
                LocalDate finSemainePrecedente = maintenant.minusDays(maintenant.getDayOfWeek().getValue());
                LocalDate debutSemainePrecedente = finSemainePrecedente.minusDays(6);
                comparaison.setDateDebutComparaison(Date.from(debutSemainePrecedente.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinComparaison(Date.from(finSemainePrecedente.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case SEMAINE_EN_COURS:
                LocalDate debutSemaineEnCours = maintenant.minusDays(maintenant.getDayOfWeek().getValue() - 1);
                comparaison.setDateDebutComparaison(Date.from(debutSemaineEnCours.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                comparaison.setDateFinComparaison(Date.from(maintenant.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
                break;
                
            case PERSONNALISEE:
                // Les dates sont déjà définies manuellement
                break;
        }
    }
    
    /**
     * Calcule la date de la prochaine exécution d'une comparaison
     */
    private Date calculerProchaineExecution(ComparaisonDonnees comparaison) {
        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime prochaine = maintenant.plusDays(comparaison.getFrequenceExecution());
        
        return Date.from(prochaine.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * Exécute une comparaison et retourne les résultats
     */
    private Map<String, Object> executerComparaison(ComparaisonDonnees comparaison) {
        Map<String, Object> resultats = new HashMap<>();
        
        // TODO: Implémenter les différents types de comparaisons
        switch (comparaison.getTypeComparaison()) {
            case TAXATIONS:
                resultats = executerComparaisonTaxations(comparaison);
                break;
            case PAIEMENTS:
                resultats = executerComparaisonPaiements(comparaison);
                break;
            case TAUX_RECOUVREMENT:
                resultats = executerComparaisonTauxRecouvrement(comparaison);
                break;
            case CONTRIBUABLES:
                resultats = executerComparaisonContribuables(comparaison);
                break;
            case PROPRIETES:
                resultats = executerComparaisonProprietes(comparaison);
                break;
            case VEHICULES:
                resultats = executerComparaisonVehicules(comparaison);
                break;
            case DECLARATIONS:
                resultats = executerComparaisonDeclarations(comparaison);
                break;
            case PERFORMANCE_SYSTEME:
                resultats = executerComparaisonPerformanceSysteme(comparaison);
                break;
            default:
                resultats.put("erreur", "Type de comparaison non supporté");
        }
        
        // Ajouter les métadonnées
        resultats.put("dateExecution", new Date());
        resultats.put("periodeReference", Map.of(
            "debut", comparaison.getDateDebutReference(),
            "fin", comparaison.getDateFinReference()
        ));
        resultats.put("periodeComparaison", Map.of(
            "debut", comparaison.getDateDebutComparaison(),
            "fin", comparaison.getDateFinComparaison()
        ));
        
        return resultats;
    }
    
    /**
     * Exécute une comparaison des taxations
     */
    private Map<String, Object> executerComparaisonTaxations(ComparaisonDonnees comparaison) {
        Map<String, Object> resultats = new HashMap<>();
        
        // TODO: Implémenter la comparaison des taxations
        // Exemple simple de résultat
        double valeurReference = 1000000.0;
        double valeurComparaison = 1200000.0;
        double variation = (valeurComparaison - valeurReference) / valeurReference * 100;
        
        resultats.put("valeurReference", valeurReference);
        resultats.put("valeurComparaison", valeurComparaison);
        resultats.put("variation", variation);
        resultats.put("variationPourcentage", String.format("%.2f%%", variation));
        
        return resultats;
    }
    
    /**
     * Exécute une comparaison des paiements
     */
    private Map<String, Object> executerComparaisonPaiements(ComparaisonDonnees comparaison) {
        // TODO: Implémenter la comparaison des paiements
        return new HashMap<>();
    }
    
    /**
     * Exécute une comparaison des taux de recouvrement
     */
    private Map<String, Object> executerComparaisonTauxRecouvrement(ComparaisonDonnees comparaison) {
        // TODO: Implémenter la comparaison des taux de recouvrement
        return new HashMap<>();
    }
    
    /**
     * Exécute une comparaison du nombre de contribuables
     */
    private Map<String, Object> executerComparaisonContribuables(ComparaisonDonnees comparaison) {
        // TODO: Implémenter la comparaison du nombre de contribuables
        return new HashMap<>();
    }
    
    /**
     * Exécute une comparaison du nombre de propriétés
     */
    private Map<String, Object> executerComparaisonProprietes(ComparaisonDonnees comparaison) {
        // TODO: Implémenter la comparaison du nombre de propriétés
        return new HashMap<>();
    }
    
    /**
     * Exécute une comparaison du nombre de véhicules
     */
    private Map<String, Object> executerComparaisonVehicules(ComparaisonDonnees comparaison) {
        // TODO: Implémenter la comparaison du nombre de véhicules
        return new HashMap<>();
    }
    
    /**
     * Exécute une comparaison du nombre de déclarations
     */
    private Map<String, Object> executerComparaisonDeclarations(ComparaisonDonnees comparaison) {
        // TODO: Implémenter la comparaison du nombre de déclarations
        return new HashMap<>();
    }
    
    /**
     * Exécute une comparaison des performances du système
     */
    private Map<String, Object> executerComparaisonPerformanceSysteme(ComparaisonDonnees comparaison) {
        // TODO: Implémenter la comparaison des performances du système
        return new HashMap<>();
    }
}
