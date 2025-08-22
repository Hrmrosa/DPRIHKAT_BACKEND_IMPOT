package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.entity.Vignette;
import com.DPRIHKAT.entity.enums.StatutVignette;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import com.DPRIHKAT.repository.VignetteRepository;
import com.DPRIHKAT.entity.enums.TypeContribuable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.io.IOException;
import java.io.InputStream;

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

        // Create vignette
        Vignette vignette = new Vignette();
        vignette.setNumero(generateVignetteNumber());
        vignette.setDateEmission(new Date());
        vignette.setDateExpiration(dateExpiration);
        vignette.setStatut(StatutVignette.ACTIVE);
        vignette.setActif(true);
        // Compute amount based on JSON rules (voiture.json + puissance_viscal.json). If user provided puissance, prefer it.
        try {
            TariffBreakdown tb = computeBreakdown(vehicule, puissanceOverride);
            // Set breakdown fields
            vignette.setTscrUsd(tb.tscrUsd);
            vignette.setImpotReelCdf(tb.impotReelCdf);
            vignette.setTotalCdf(tb.totalCdf);
            vignette.setPuissanceFiscale(tb.puissanceUsed);
            vignette.setCategorieTarifaire(tb.categorie);
            vignette.setPlageTarifaire(tb.plage);
            double total = tb.totalCdf != null ? tb.totalCdf : 0d;
            vignette.setMontant(total > 0 ? total : montant);
        } catch (IOException e) {
            // Fallback to provided montant if JSON data is insufficient
            vignette.setMontant(montant);
        }
        vignette.setVehicule(vehicule);
        vignette.setAgent(agent.getAgent());

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
     * Get all vignettes
     */
    public List<Vignette> findAll() {
        return vignetteRepository.findAll();
    }

    /**
     * Get active vignettes
     */
    public List<Vignette> getActiveVignettes() {
        return vignetteRepository.findByActifTrue();
    }

    /**
     * Get expired vignettes
     */
    public List<Vignette> getExpiredVignettes() {
        return vignetteRepository.findByDateExpirationBefore(new Date());
    }

    /**
     * Save vignette
     */
    public Vignette save(Vignette vignette) {
        return vignetteRepository.save(vignette);
    }

    /**
     * Update vignette
     */
    public Vignette update(UUID id, Vignette vignette) {
        if (vignetteRepository.existsById(id)) {
            vignette.setId(id);
            return vignetteRepository.save(vignette);
        }
        return null;
    }

    /**
     * Delete vignette
     */
    public void deleteById(UUID id) {
        vignetteRepository.deleteById(id);
    }

    /**
     * Generate a unique vignette number
     */
    private String generateVignetteNumber() {
        return "HK-" + System.currentTimeMillis();
    }

    // ===== JSON-based calculation helpers =====

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

        for (JsonNode type : categoryNode.path("types")) {
            String keyPlage = type.has("plage") ? type.path("plage").asText() : null;
            String keyType = type.has("type") ? type.path("type").asText() : null;
            boolean matches = (keyPlage != null && plage.equalsIgnoreCase(keyPlage)) || (keyPlage == null && keyType != null);
            if (!matches) continue;

            // tscr_usd
            Double tscr = null;
            if (type.path("tscr_usd").isNumber()) {
                tscr = type.path("tscr_usd").asDouble();
            } else if (type.path("tscr_usd").isTextual()) {
                String expr = type.path("tscr_usd").asText(null);
                if (expr != null && expr.contains("/CV") && info.puissanceCV != null) {
                    String num = expr.replace("/CV", "").trim().replace(",", ".");
                    tscr = Double.parseDouble(num) * info.puissanceCV;
                }
            }

            // impot_reel_cdf
            Double ir = null;
            if (type.path("impot_reel_cdf").isNumber()) {
                ir = type.path("impot_reel_cdf").asDouble();
            } else if (type.path("impot_reel_cdf").isTextual()) {
                String expr = type.path("impot_reel_cdf").asText(null);
                if (expr != null && expr.contains("/CV") && info.puissanceCV != null) {
                    String num = expr.replace("/CV", "").trim().replace(",", ".");
                    ir = Double.parseDouble(num) * info.puissanceCV;
                }
            }

            // total_cdf
            Double total = null;
            if (type.path("total_cdf").isNumber()) {
                total = type.path("total_cdf").asDouble();
            } else if (type.path("total_cdf").isTextual()) {
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

        throw new IOException("Plage/type non trouvé dans puissance_viscal.json: " + plage + " pour catégorie " + categoryName);
    }

    private String resolveCategoryName(Vehicule vehicule, VehicleInfo info) {
        // If voiture.json supplies an explicit category, prefer it
        if (info.categorie != null) {
            String c = info.categorie.toLowerCase();
            if (c.contains("utilitaire")) return "Véhicules Utilitaires";
            if (c.contains("moto")) return "Moto";
            if (c.contains("bateau") || c.contains("navire")) return "Bateaux";
        }
        // Otherwise, decide by taxpayer type (PP/PM)
        TypeContribuable type = vehicule.getProprietaire() != null ? vehicule.getProprietaire().getType() : null;
        if (type == TypeContribuable.PERSONNE_MORALE) return "Personnes Morales";
        return "Personnes Physiques";
    }

    private String resolvePlage(VehicleInfo info) {
        // For Utilitaires, use weight bands if available
        if (info.isUtilitaire && info.poidsKg != null) {
            double t = info.poidsKg / 1000.0;
            if (t < 2.5) return "-2,5T";
            if (t <= 10) return "2,5 à 10 T";
            return "+10 T";
        }
        // Otherwise use puissance fiscale
        double cv = info.puissanceCV != null ? info.puissanceCV : 0.0;
        if (cv <= 10) return "1-10CV";
        if (cv <= 15) return "11-15CV";
        return "+15CV";
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
            if (info.puissanceCV != null) return info;
            throw new IOException("Fichier voiture.json introuvable dans resources. Ajoutez-le avec les champs: immatriculation|marque|modele, puissance_cv, poids_kg (optionnel), categorie (optionnel)");
        }
        JsonNode found = null;
        if (node.isArray()) {
            for (JsonNode v : node) {
                String imm = v.path("immatriculation").asText("");
                if (!imm.isEmpty() && imm.equalsIgnoreCase(info.immatriculation)) { found = v; break; }
                String marque = v.path("marque").asText("");
                String modele = v.path("modele").asText("");
                if (!marque.isEmpty() && !modele.isEmpty() && marque.equalsIgnoreCase(info.marque) && modele.equalsIgnoreCase(info.modele)) {
                    found = v; break;
                }
            }
        }
        if (found == null) {
            if (info.puissanceCV != null) return info; // proceed with override
            throw new IOException("Véhicule non trouvé dans voiture.json (immatriculation/marque+modèle)");
        }
        // Extract puissance fiscale and other hints
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

    private double parseNumber(String s) {
        if (s == null) return 0.0;
        try {
            return Double.parseDouble(s.replace(",", ".").replaceAll("[^0-9.]+", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private JsonNode loadJson(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        try (InputStream is = resource.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(is);
        }
    }

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
}
