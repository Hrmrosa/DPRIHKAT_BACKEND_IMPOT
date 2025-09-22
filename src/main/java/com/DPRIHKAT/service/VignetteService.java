package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.StatutVignette;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import com.DPRIHKAT.repository.VignetteRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class VignetteService {

    private static final Logger logger = LoggerFactory.getLogger(VignetteService.class);

    @Autowired
    private VignetteRepository vignetteRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TaxationService taxationService;

    /**
     * Generate a vignette for a vehicle
     */
    public Vignette generateVignette(UUID vehiculeId, UUID agentId, Date dateExpiration, double montant, Double puissanceOverride) {
        // Get vehicle and agent
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));

        Utilisateur agent = utilisateurRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé"));

        // Compute tariff breakdown
        TariffBreakdown tb;
        try {
            tb = computeBreakdown(vehicule, puissanceOverride);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du calcul du tarif: " + e.getMessage(), e);
        }

        // Create vignette
        Vignette vignette = new Vignette();
        vignette.setNumero("VIG-" + System.currentTimeMillis());
        vignette.setDateEmission(new Date());
        vignette.setDateExpiration(dateExpiration);
        vignette.setMontant(montant > 0 ? montant : tb.totalCdf);
        vignette.setStatut(StatutVignette.ACTIVE);
        vignette.setVehicule(vehicule);
        vignette.setAgent(agent.getAgent());

        // Set tariff breakdown fields
        vignette.setTscrUsd(tb.tscrUsd);
        vignette.setImpotReelCdf(tb.impotReelCdf);
        vignette.setTotalCdf(tb.totalCdf);
        vignette.setPuissanceFiscale(tb.puissanceUsed);
        vignette.setCategorieTarifaire(tb.categorie);
        vignette.setPlageTarifaire(tb.plage);

        return vignetteRepository.save(vignette);
    }

    /**
     * Save vignette
     */
    public Vignette save(Vignette vignette) {
        return vignetteRepository.save(vignette);
    }

    /**
     * Get vignette by ID
     */
    public Vignette findById(UUID id) {
        return vignetteRepository.findById(id).orElse(null);
    }

    /**
     * Get vignettes by vehicle
     */
    public List<Vignette> getVignettesByVehicle(UUID vehiculeId) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
        return vignetteRepository.findByVehicule(vehicule);
    }

    /**
     * Get all active vignettes
     */
    public List<Vignette> getActiveVignettes() {
        return vignetteRepository.findByStatut(StatutVignette.ACTIVE);
    }

    /**
     * Get all expired vignettes
     */
    public List<Vignette> getExpiredVignettes() {
        Date now = new Date();
        return vignetteRepository.findByStatutAndDateExpirationBefore(StatutVignette.ACTIVE, now);
    }

    /**
     * Génère une vignette pour un véhicule
     * 
     * @param vehiculeId ID du véhicule
     * @return La vignette générée
     */
    @Transactional
    public Vignette genererVignettePourVehicule(UUID vehiculeId) {
        logger.info("Génération d'une vignette pour le véhicule {}", vehiculeId);

        // Vérifier que le véhicule existe
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé avec ID: " + vehiculeId));

        // Calculer la date d'expiration (1 an à partir d'aujourd'hui)
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date dateExpiration = calendar.getTime();

        // Calculer le montant (pourrait être configurable)
        double montant = 25000.0; // Montant fixe pour les vignettes

        // Générer un numéro unique
        String numero = "VIG-" + UUID.randomUUID().toString().substring(0, 8);

        // Créer la vignette
        Vignette vignette = new Vignette();
        vignette.setNumero(numero);
        vignette.setDateEmission(new Date());
        vignette.setDateExpiration(dateExpiration);
        vignette.setMontant(montant);
        vignette.setStatut(StatutVignette.ACTIVE);
        vignette.setActif(true);
        vignette.setVehicule(vehicule);

        // Générer une taxation associée
        Taxation taxation = taxationService.genererTaxationPourVignette(vignette);
        vignette.setTaxation(taxation);

        // Sauvegarder la vignette
        vignette = vignetteRepository.save(vignette);

        logger.info("Vignette générée avec succès pour le véhicule {}", vehiculeId);
        return vignette;
    }

    /**
     * Vérifie si un véhicule a une vignette valide
     * 
     * @param vehiculeId ID du véhicule
     * @return true si le véhicule a une vignette valide, false sinon
     */
    public boolean verifierVignetteValide(UUID vehiculeId) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé avec ID: " + vehiculeId));

        return vehicule.getVignettes().stream()
                .anyMatch(v -> v.isActif() && 
                              v.getStatut() == StatutVignette.ACTIVE && 
                              v.getDateExpiration().after(new Date()));
    }

    // === Private helper methods for tariff computation ===

    private static class VehicleInfo {
        String immatriculation;
        String marque;
        String modele;
        String categorie;
        boolean isUtilitaire;
        Double puissanceCV;
        Double poidsKg;
    }

    private static class TariffBreakdown {
        Double tscrUsd;
        Double impotReelCdf;
        Double totalCdf;
        Double puissanceUsed;
        String categorie;
        String plage;
    }

    private VehicleInfo resolveVehicleInfo(Vehicule vehicule, Double puissanceOverride) throws IOException {
        VehicleInfo info = new VehicleInfo();
        info.immatriculation = vehicule.getImmatriculation();
        info.marque = vehicule.getMarque();
        info.modele = vehicule.getModele();
        if (puissanceOverride != null && puissanceOverride > 0) {
            info.puissanceCV = puissanceOverride;
        }
        // Load voiture.json (array of vehicle entries)
        JsonNode node;
        try {
            node = loadJson("voiture.json");
        } catch (IOException e) {
            // If override provided, we can proceed without voiture.json
            if (puissanceOverride != null && puissanceOverride > 0) {
                info.puissanceCV = puissanceOverride;
                return info;
            }
            throw e;
        }
        // Find matching entry in voiture.json
        JsonNode found = null;
        for (JsonNode entry : node) {
            if (entry.hasNonNull("immatriculation") && entry.get("immatriculation").asText().equals(info.immatriculation)) {
                found = entry;
                break;
            }
            if (entry.hasNonNull("marque") && entry.get("marque").asText().equals(info.marque) &&
                entry.hasNonNull("modele") && entry.get("modele").asText().equals(info.modele)) {
                found = entry;
                break;
            }
        }
        if (found == null) {
            // If override provided, we can proceed without voiture.json
            if (puissanceOverride != null && puissanceOverride > 0) {
                info.puissanceCV = puissanceOverride;
                return info;
            }
            throw new IOException("Véhicule non trouvé dans voiture.json: " + info.immatriculation + " " + info.marque + " " + info.modele);
        }
        // Extract fields from found entry
        if (found.hasNonNull("puissance_viscal")) {
            info.puissanceCV = parseNumber(found.get("puissance_viscal").asText());
        } else if (found.hasNonNull("puissance_cv")) {
            info.puissanceCV = parseNumber(found.get("puissance_cv").asText());
        }
        if (found.hasNonNull("poids_kg")) {
            info.poidsKg = parseNumber(found.get("poids_kg").asText());
        }
        if (found.hasNonNull("categorie")) {
            info.categorie = found.get("categorie").asText();
            info.isUtilitaire = info.categorie.toLowerCase().contains("utilitaire");
        }
        return info;
    }

    private String resolveCategoryName(Vehicule vehicule, VehicleInfo info) {
        // If explicit category in vehicle info, use it
        if (info.categorie != null) {
            return info.categorie;
        }
        // Default category based on vehicle type
        if (vehicule.getModele() != null && vehicule.getModele().toLowerCase().contains("bateau")) {
            return "Bateaux";
        }
        if (info.isUtilitaire) {
            return "Véhicules utilitaires";
        }
        return "Véhicules de tourisme";
    }

    private String resolvePlage(VehicleInfo info) {
        if (info.puissanceCV == null) {
            throw new RuntimeException("Puissance non définie pour le véhicule");
        }
        double cv = info.puissanceCV;
        if (info.isUtilitaire) {
            if (cv <= 2.0) return "jusqu'à 2 CV";
            if (cv <= 5.0) return "de 2 à 5 CV";
            if (cv <= 10.0) return "de 5 à 10 CV";
            if (cv <= 20.0) return "de 10 à 20 CV";
            return "plus de 20 CV";
        } else {
            if (cv <= 3.0) return "jusqu'à 3 CV";
            if (cv <= 6.0) return "de 3 à 6 CV";
            if (cv <= 10.0) return "de 6 à 10 CV";
            if (cv <= 20.0) return "de 10 à 20 CV";
            return "plus de 20 CV";
        }
    }

    private JsonNode loadJson(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        try (InputStream is = resource.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(is);
        }
    }

    private Double parseNumber(String text) {
        if (text == null) return null;
        try {
            return Double.parseDouble(text.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private TariffBreakdown computeBreakdown(Vehicule vehicule, Double puissanceOverride) throws IOException {
        // Load vehicle info from voiture.json (to get puissance/peso/categorie), or use override puissance
        VehicleInfo info = resolveVehicleInfo(vehicule, puissanceOverride);

        // Utiliser le genre et la catégorie fournis par le client si disponibles
        String genre = vehicule.getGenre();
        String categorie = vehicule.getCategorie();

        // Si genre ou catégorie ne sont pas fournis, utiliser les valeurs par défaut
        if (genre == null || genre.isEmpty()) {
            genre = "Véhicules de tourisme"; // Valeur par défaut
        }
        
        if (categorie == null || categorie.isEmpty()) {
            categorie = "Personnes Physiques"; // Valeur par défaut
            
            // Déterminer la catégorie en fonction du type de contribuable
            if (vehicule.getProprietaire() != null && 
                vehicule.getProprietaire().getType() != null && 
                vehicule.getProprietaire().getType().toString().contains("MORALE")) {
                categorie = "Personnes Morales";
            }
        }

        // Déterminer la plage de puissance
        String plage = determinePlagePuissance(info.puissanceCV);

        // Lire le fichier taux_IRV.json
        JsonNode root = loadJson("taux_IRV.json");
        JsonNode taxesCirculation = root.path("taxes_circulation");
        JsonNode categories = taxesCirculation.path("categories");

        // Trouver la catégorie correspondante
        JsonNode categorieNode = null;
        for (JsonNode cat : categories) {
            if (categorie.equalsIgnoreCase(cat.path("categorie").asText())) {
                categorieNode = cat;
                break;
            }
        }
        
        if (categorieNode == null) {
            throw new IOException("Catégorie non trouvée dans taux_IRV.json: " + categorie);
        }

        // Trouver la plage de puissance correspondante
        JsonNode vehicules = categorieNode.path("vehicules");
        JsonNode vehiculeNode = null;
        
        for (JsonNode veh : vehicules) {
            if (veh.has("puissance") && veh.get("puissance").asText().equalsIgnoreCase(plage)) {
                vehiculeNode = veh;
                break;
            }
        }
        
        if (vehiculeNode == null) {
            throw new IOException("Plage de puissance non trouvée dans taux_IRV.json: " + plage + " pour catégorie " + categorie);
        }

        // Extraire les taux
        TariffBreakdown tb = new TariffBreakdown();
        tb.categorie = categorie;
        tb.plage = plage;
        tb.puissanceUsed = info.puissanceCV;
        
        JsonNode tauxUsd = vehiculeNode.path("taux_usd");
        
        // Extraire les valeurs de taux
        tb.tscrUsd = tauxUsd.has("tscr") ? tauxUsd.get("tscr").asDouble() : 0.0;
        double vignette = tauxUsd.has("vignette") ? tauxUsd.get("vignette").asDouble() : 0.0;
        tb.impotReelCdf = vignette * 2000; // Conversion approximative USD -> CDF
        tb.totalCdf = tauxUsd.has("total") ? tauxUsd.get("total").asDouble() * 2000 : 0.0;
        
        return tb;
    }
    
    /**
     * Détermine la plage de puissance selon les critères du fichier taux_IRV.json
     */
    private String determinePlagePuissance(Double puissanceCV) {
        if (puissanceCV == null) {
            return "1-10 CV"; // Valeur par défaut
        }
        
        if (puissanceCV <= 10) {
            return "1-10 CV";
        } else if (puissanceCV <= 15) {
            return "11-15 CV";
        } else {
            return "+15 CV";
        }
    }
}
