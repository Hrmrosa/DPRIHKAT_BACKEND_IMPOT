package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.entity.Vignette;
import com.DPRIHKAT.entity.enums.StatutVignette;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import com.DPRIHKAT.repository.VignetteRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class VignetteService {

    @Autowired
    private VignetteRepository vignetteRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

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

        // Decide category name in puissance_viscal.json
        String categoryName = resolveCategoryName(vehicule, info);

        // Decide band/plage in puissance_viscal.json
        String plage = resolvePlage(info);

        // Read puissance_viscal.json
        JsonNode root = loadJson("puissance_viscal.json");
        JsonNode categories = root.path("categories");
        JsonNode categoryNode = null;
        for (JsonNode cat : categories) {
            if (categoryName.equalsIgnoreCase(cat.path("nom").asText())) {
                categoryNode = cat;
                break;
            }
        }
        if (categoryNode == null) {
            throw new IOException("Catégorie non trouvée dans puissance_viscal.json: " + categoryName);
        }

        // Find matching type by plage (or type for Bateaux) and compute breakdown
        TariffBreakdown tb = new TariffBreakdown();
        tb.categorie = categoryName;
        tb.plage = plage;
        tb.puissanceUsed = info.puissanceCV;

        JsonNode types = categoryNode.path("types");
        for (JsonNode type : types) {
            String typePlage = type.path("plage").asText(null);
            String typeType = type.path("type").asText(null);
            // For Bateaux, match by type; for others, match by plage
            boolean match = categoryName.equalsIgnoreCase("Bateaux") ? 
                (typeType != null && typeType.equalsIgnoreCase(plage)) :
                (typePlage != null && typePlage.equalsIgnoreCase(plage));
            if (match) {
                // Compute TSCR (USD)
                Double tscr = null;
                if (type.hasNonNull("tscr_usd")) {
                    String expr = type.path("tscr_usd").asText(null);
                    if (expr != null && expr.contains("/CV") && info.puissanceCV != null) {
                        String num = expr.replace("/CV", "").trim().replace(",", ".");
                        tscr = Double.parseDouble(num) * info.puissanceCV;
                    }
                }

                // Compute IR (CDF)
                Double ir = null;
                if (type.hasNonNull("impot_reel_cdf")) {
                    String expr = type.path("impot_reel_cdf").asText(null);
                    if (expr != null && expr.contains("/CV") && info.puissanceCV != null) {
                        String num = expr.replace("/CV", "").trim().replace(",", ".");
                        ir = Double.parseDouble(num) * info.puissanceCV;
                    }
                }

                // Compute total (CDF)
                Double total = null;
                if (type.hasNonNull("total_cdf")) {
                    String expr = type.path("total_cdf").asText(null);
                    if (expr != null && expr.contains("/CV") && info.puissanceCV != null) {
                        String num = expr.replace("/CV", "").trim().replace(",", ".");
                        total = Double.parseDouble(num) * info.puissanceCV;
                    }
                }

                tb.tscrUsd = tscr;
                tb.impotReelCdf = ir;
                tb.totalCdf = total != null ? total : (ir != null ? ir : 0d);
                return tb;
            }
        }

        throw new IOException("Plage/type non trouvé dans puissance_viscal.json: " + plage + " pour catégorie " + categoryName);
    }
}
