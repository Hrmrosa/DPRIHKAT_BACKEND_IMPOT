package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.Paiement;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.Taxation;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.repository.ContribuableRepository;
import com.DPRIHKAT.repository.PaiementRepository;
import com.DPRIHKAT.repository.PlaqueRepository;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.repository.TaxationRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final TaxationRepository taxationRepository;
    private final PaiementRepository paiementRepository;
    private final ProprieteRepository proprieteRepository;
    private final ContribuableRepository contribuableRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final PlaqueRepository plaqueRepository;
    private final LogService logService;

    public DashboardService(
            TaxationRepository taxationRepository,
            PaiementRepository paiementRepository,
            ProprieteRepository proprieteRepository,
            ContribuableRepository contribuableRepository,
            UtilisateurRepository utilisateurRepository,
            VehiculeRepository vehiculeRepository,
            PlaqueRepository plaqueRepository,
            LogService logService) {
        this.taxationRepository = taxationRepository;
        this.paiementRepository = paiementRepository;
        this.proprieteRepository = proprieteRepository;
        this.contribuableRepository = contribuableRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.plaqueRepository = plaqueRepository;
        this.logService = logService;
    }

    /**
     * Récupère les données du tableau de bord pour les administrateurs
     * Vue complète du système avec toutes les statistiques
     */
    public Map<String, Object> getAdminDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();
        
        // Statistiques générales
        dashboardData.put("statistiques", getStatistiquesGenerales());
        
        // Données pour les graphiques
        dashboardData.put("graphiques", getGraphiquesData());
        
        // Données pour la carte
        dashboardData.put("carte", getCarteData());
        
        // Derniers utilisateurs
        dashboardData.put("derniers_utilisateurs", getDerniersUtilisateurs(10));
        
        // Derniers logs de connexion
        dashboardData.put("logs_connexion", getLogsConnexion(20));
        
        // Taxations récentes
        dashboardData.put("taxations_recentes", getTaxationsRecentes(10));
        
        // Paiements récents
        dashboardData.put("paiements_recents", getPaiementsRecents(10));
        
        return dashboardData;
    }

    /**
     * Récupère les données du tableau de bord pour les directeurs
     * Vue d'ensemble avec les statistiques principales
     */
    public Map<String, Object> getDirecteurDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();
        
        // Statistiques générales
        dashboardData.put("statistiques", getStatistiquesGenerales());
        
        // Données pour les graphiques
        dashboardData.put("graphiques", getGraphiquesData());
        
        // Données pour la carte
        dashboardData.put("carte", getCarteData());
        
        // Taxations récentes
        dashboardData.put("taxations_recentes", getTaxationsRecentes(5));
        
        // Paiements récents
        dashboardData.put("paiements_recents", getPaiementsRecents(5));
        
        return dashboardData;
    }

    /**
     * Récupère les données du tableau de bord pour les chefs de bureau
     */
    public Map<String, Object> getChefBureauDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();
        
        // TODO: Implémenter les données spécifiques pour les chefs de bureau
        
        return dashboardData;
    }

    /**
     * Récupère les données du tableau de bord pour les taxateurs
     */
    public Map<String, Object> getTaxateurDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();
        
        // TODO: Implémenter les données spécifiques pour les taxateurs
        
        return dashboardData;
    }

    /**
     * Récupère les données du tableau de bord pour les contribuables
     */
    public Map<String, Object> getContribuableDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();

        // TODO: Implémenter les données spécifiques pour les contribuables

        return dashboardData;
    }

    /**
     * Récupère les statistiques publiques pour le site web (sans authentification)
     * Données générales pour affichage public
     */
    public Map<String, Object> getPublicStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Nombre total de contribuables
        long totalContribuables = contribuableRepository.count();
        stats.put("total_contribuables", totalContribuables);
        
        // Répartition des contribuables par type
        stats.put("contribuables_par_type", getContribuablesParType());
        
        // Nombre total de propriétés
        long totalProprietes = proprieteRepository.count();
        stats.put("total_proprietes", totalProprietes);
        
        // Répartition des propriétés par type
        stats.put("proprietes_par_type", getProprieteParType());
        
        // Nombre total de véhicules
        long totalVehicules = vehiculeRepository.count();
        stats.put("total_vehicules", totalVehicules);
        
        // Nombre total de plaques
        long totalPlaques = plaqueRepository.count();
        stats.put("total_plaques", totalPlaques);
        
        // Nombre de plaques disponibles
        long plaquesDisponibles = plaqueRepository.findByDisponibleTrue().size();
        stats.put("plaques_disponibles", plaquesDisponibles);
        
        // Nombre de plaques attribuées
        long plaquesAttribuees = plaqueRepository.findByDisponibleFalse().size();
        stats.put("plaques_attribuees", plaquesAttribuees);
        
        // Montant total des taxations
        BigDecimal montantTotalTaxations = taxationRepository.findAll().stream()
                .map(taxation -> BigDecimal.valueOf(taxation.getMontant() != null ? taxation.getMontant() : 0.0))
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        stats.put("montant_total_taxations", montantTotalTaxations);
        
        // Montant total des paiements
        BigDecimal montantTotalPaiements = paiementRepository.findAll().stream()
                .map(paiement -> BigDecimal.valueOf(paiement.getMontant() != null ? paiement.getMontant() : 0.0))
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        stats.put("montant_total_paiements", montantTotalPaiements);
        
        // Taux de recouvrement (paiements / taxations)
        BigDecimal tauxRecouvrement = montantTotalTaxations.compareTo(BigDecimal.ZERO) > 0
                ? montantTotalPaiements.divide(montantTotalTaxations, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"))
                : BigDecimal.ZERO;
        stats.put("taux_recouvrement", tauxRecouvrement);
        
        return stats;
    }
    /**
     * Méthode générique pour récupérer toutes les données du dashboard
     * Cette méthode est utilisée par le service de notification en temps réel
     */
    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();
        
        // Statistiques générales
        dashboardData.put("statistiques", getStatistiquesGenerales());
        
        // Données pour les graphiques
        dashboardData.put("graphiques", getGraphiquesData());
        
        // Taxations récentes
        dashboardData.put("taxations_recentes", getTaxationsRecentes(10));
        
        // Paiements récents
        dashboardData.put("paiements_recents", getPaiementsRecents(10));
        
        // Derniers utilisateurs
        dashboardData.put("derniers_utilisateurs", getDerniersUtilisateurs(10));
        
        // Derniers logs de connexion
        dashboardData.put("logs_connexion", getLogsConnexion(10));
        
        return dashboardData;
    }

    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);


    /**
     * Récupère les statistiques générales du système
     */
    public Map<String, Object> getStatistiquesGenerales() {
        Map<String, Object> stats = new HashMap<>();
        
        // Nombre total de contribuables
        long totalContribuables = contribuableRepository.count();
        stats.put("total_contribuables", totalContribuables);
        
        // Nombre total de propriétés
        long totalProprietes = proprieteRepository.count();
        stats.put("total_proprietes", totalProprietes);
        
        // Nombre total de véhicules
        long totalVehicules = vehiculeRepository.count();
        stats.put("total_vehicules", totalVehicules);
        
        // Nombre total de plaques
        long totalPlaques = plaqueRepository.count();
        stats.put("total_plaques", totalPlaques);
        
        // Nombre de plaques disponibles
        long plaquesDisponibles = plaqueRepository.findByDisponibleTrue().size();
        stats.put("plaques_disponibles", plaquesDisponibles);
        
        // Nombre de plaques attribuées
        long plaquesAttribuees = plaqueRepository.findByDisponibleFalse().size();
        stats.put("plaques_attribuees", plaquesAttribuees);
        
        // Nombre total d'utilisateurs
        long totalUtilisateurs = utilisateurRepository.count();
        stats.put("total_utilisateurs", totalUtilisateurs);
        
        // Montant total des taxations
        BigDecimal montantTotalTaxations = taxationRepository.findAll().stream()
                .map(taxation -> BigDecimal.valueOf(taxation.getMontant() != null ? taxation.getMontant() : 0.0))
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        stats.put("montant_total_taxations", montantTotalTaxations);
        
        // Montant total des paiements
        BigDecimal montantTotalPaiements = paiementRepository.findAll().stream()
                .map(paiement -> BigDecimal.valueOf(paiement.getMontant() != null ? paiement.getMontant() : 0.0))
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        stats.put("montant_total_paiements", montantTotalPaiements);
        
        // Taux de recouvrement (paiements / taxations)
        BigDecimal tauxRecouvrement = montantTotalTaxations.compareTo(BigDecimal.ZERO) > 0
                ? montantTotalPaiements.divide(montantTotalTaxations, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"))
                : BigDecimal.ZERO;
        stats.put("taux_recouvrement", tauxRecouvrement);
        
        return stats;
    }

    /**
     * Récupère les données pour les graphiques
     */
    private Map<String, Object> getGraphiquesData() {
        Map<String, Object> graphiques = new HashMap<>();
        
        // Taxations par mois (12 derniers mois)
        graphiques.put("taxations_par_mois", getTaxationsParMois());
        
        // Paiements par mois (12 derniers mois)
        graphiques.put("paiements_par_mois", getPaiementsParMois());
        
        // Répartition des propriétés par type
        graphiques.put("proprietes_par_type", getProprieteParType());
        
        // Répartition des contribuables par type
        graphiques.put("contribuables_par_type", getContribuablesParType());
        
        return graphiques;
    }

    /**
     * Convertit un LocalDateTime en Date
     */
    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Récupère les taxations par mois pour les 12 derniers mois
     */
    private List<Map<String, Object>> getTaxationsParMois() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Récupérer les 12 derniers mois
        LocalDate now = LocalDate.now();
        for (int i = 11; i >= 0; i--) {
            YearMonth yearMonth = YearMonth.from(now.minusMonths(i));
            LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);
            
            // Convertir LocalDateTime en Date
            Date startDate = convertToDate(startOfMonth);
            Date endDate = convertToDate(endOfMonth);
            
            // Calculer le montant total des taxations pour ce mois
            BigDecimal montantTotal = taxationRepository.findByDateTaxationBetweenAndActifTrue(startDate, endDate).stream()
                    .map(taxation -> BigDecimal.valueOf(taxation.getMontant() != null ? taxation.getMontant() : 0.0))
                    .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
            
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("mois", yearMonth.toString());
            monthData.put("montant", montantTotal);
            result.add(monthData);
        }
        
        return result;
    }

    /**
     * Récupère les paiements par mois pour les 12 derniers mois
     */
    private List<Map<String, Object>> getPaiementsParMois() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Récupérer les 12 derniers mois
        LocalDate now = LocalDate.now();
        for (int i = 11; i >= 0; i--) {
            YearMonth yearMonth = YearMonth.from(now.minusMonths(i));
            LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);
            
            // Convertir LocalDateTime en Date
            Date startDate = convertToDate(startOfMonth);
            Date endDate = convertToDate(endOfMonth);
            
            // Calculer le montant total des paiements pour ce mois
            BigDecimal montantTotal = paiementRepository.findByDateBetween(startDate, endDate).stream()
                    .map(paiement -> BigDecimal.valueOf(paiement.getMontant() != null ? paiement.getMontant() : 0.0))
                    .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
            
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("mois", yearMonth.toString());
            monthData.put("montant", montantTotal);
            result.add(monthData);
        }
        
        return result;
    }

    /**
     * Récupère la répartition des propriétés par type
     */
    private List<Map<String, Object>> getProprieteParType() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Récupérer toutes les propriétés
        List<Propriete> proprietes = proprieteRepository.findAll();
        
        // Grouper par type et compter
        Map<String, Long> countByType = proprietes.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getType().toString(),
                        Collectors.counting()
                ));
        
        // Convertir en liste de maps
        countByType.forEach((type, count) -> {
            Map<String, Object> typeData = new HashMap<>();
            typeData.put("type", type);
            typeData.put("count", count);
            result.add(typeData);
        });
        
        return result;
    }

    /**
     * Récupère la répartition des contribuables par type
     */
    private List<Map<String, Object>> getContribuablesParType() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Récupérer tous les contribuables
        List<Contribuable> contribuables = contribuableRepository.findAll();
        
        // Grouper par type et compter
        Map<String, Long> countByType = contribuables.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getType().toString(),
                        Collectors.counting()
                ));
        
        // Convertir en liste de maps
        countByType.forEach((type, count) -> {
            Map<String, Object> typeData = new HashMap<>();
            typeData.put("type", type);
            typeData.put("count", count);
            result.add(typeData);
        });
        
        return result;
    }

    /**
     * Récupère les données pour la carte
     */
    private Map<String, Object> getCarteData() {
        Map<String, Object> carteData = new HashMap<>();
        
        // Récupérer toutes les propriétés avec coordonnées géographiques
        List<Propriete> proprietes = proprieteRepository.findAll();
        
        // Convertir en format GeoJSON pour la carte
        List<Map<String, Object>> features = proprietes.stream()
                .filter(p -> p.getLocation() != null)
                .map(this::proprieteToGeoJson)
                .collect(Collectors.toList());
        
        carteData.put("type", "FeatureCollection");
        carteData.put("features", features);
        
        return carteData;
    }

    /**
     * Convertit une propriété en objet GeoJSON
     */
    private Map<String, Object> proprieteToGeoJson(Propriete propriete) {
        Map<String, Object> feature = new HashMap<>();
        feature.put("type", "Feature");
        
        // Géométrie
        Map<String, Object> geometry = new HashMap<>();
        geometry.put("type", "Point");
        
        double[] coordinates = new double[2];
        // Extraire les coordonnées du point (centroïde si ce n'est pas un point)
        Point centroid = propriete.getLocation().getCentroid();
        coordinates[0] = centroid.getX(); // longitude
        coordinates[1] = centroid.getY(); // latitude
        
        geometry.put("coordinates", coordinates);
        feature.put("geometry", geometry);
        
        // Propriétés
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", propriete.getId().toString());
        properties.put("type", propriete.getType().toString());
        properties.put("adresse", propriete.getAdresse());
        properties.put("superficie", propriete.getSuperficie());
        properties.put("montantImpot", propriete.getMontantImpot());
        
        if (propriete.getProprietaire() != null) {
            properties.put("proprietaire", propriete.getProprietaire().getNom());
        }
        
        feature.put("properties", properties);
        
        return feature;
    }

    /**
     * Récupère les derniers utilisateurs créés
     */
    private List<Map<String, Object>> getDerniersUtilisateurs(int limit) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Récupérer tous les utilisateurs et les trier par ID (supposant que les IDs plus récents sont plus grands)
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "id"))).getContent();
        
        // Convertir en liste de maps
        for (Utilisateur utilisateur : utilisateurs) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", utilisateur.getId().toString());
            userData.put("nomComplet", utilisateur.getNomComplet());
            userData.put("login", utilisateur.getLogin());
            userData.put("role", utilisateur.getRole().toString());
            userData.put("actif", utilisateur.isActif());
            result.add(userData);
        }
        
        return result;
    }

    /**
     * Récupère les derniers logs de connexion
     */
    private List<Map<String, Object>> getLogsConnexion(int limit) {
        // Utiliser le service de logs pour récupérer les dernières connexions
        return logService.getLastConnectionLogs(limit);
    }

    /**
     * Récupère les taxations récentes
     */
    private List<Map<String, Object>> getTaxationsRecentes(int limit) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Récupérer les taxations récentes
        List<Taxation> taxations = taxationRepository.findAll().stream()
                .sorted((t1, t2) -> t2.getDateTaxation().compareTo(t1.getDateTaxation()))
                .limit(limit)
                .collect(Collectors.toList());
        
        // Convertir en liste de maps
        for (Taxation taxation : taxations) {
            Map<String, Object> taxationData = new HashMap<>();
            taxationData.put("id", taxation.getId().toString());
            taxationData.put("montant", taxation.getMontant());
            taxationData.put("dateTaxation", taxation.getDateTaxation().toString());
            
            if (taxation.getDeclaration() != null && taxation.getDeclaration().getContribuable() != null) {
                taxationData.put("contribuable", taxation.getDeclaration().getContribuable().getNom());
            }
            
            result.add(taxationData);
        }
        
        return result;
    }

    /**
     * Récupère les paiements récents
     */
    private List<Map<String, Object>> getPaiementsRecents(int limit) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Récupérer les paiements récents
        List<Paiement> paiements = paiementRepository.findAll().stream()
                .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
                .limit(limit)
                .collect(Collectors.toList());
        
        // Convertir en liste de maps
        for (Paiement paiement : paiements) {
            Map<String, Object> paiementData = new HashMap<>();
            paiementData.put("id", paiement.getId().toString());
            paiementData.put("montant", paiement.getMontant());
            paiementData.put("datePaiement", paiement.getDate().toString());
            paiementData.put("reference", paiement.getBordereauBancaire());
            
            if (paiement.getTaxation() != null && 
                paiement.getTaxation().getDeclaration() != null && 
                paiement.getTaxation().getDeclaration().getContribuable() != null) {
                paiementData.put("contribuable", paiement.getTaxation().getDeclaration().getContribuable().getNom());
            }
            
            result.add(paiementData);
        }
        
        return result;
    }
}
