package com.DPRIHKAT.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class ReferenceDataService {
    private final Map<String, Map<String, List<String>>> communesData = new HashMap<>();
    private final Map<String, List<String>> brandModels = new HashMap<>();

    public ReferenceDataService() {
        loadCommunes();
        loadVoitures();
    }

    private void loadCommunes() {
        try {
            ClassPathResource resource = new ClassPathResource("communes.json");
            try (InputStream is = resource.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Map<String, List<String>>> raw = mapper.readValue(
                        is, new TypeReference<Map<String, Map<String, List<String>>>>() {});
                communesData.clear();
                // Normalize keys to keep original key but allow case-insensitive lookup
                communesData.putAll(raw);
            }
        } catch (Exception e) {
            // Keep empty; controller will surface errors if queried
        }
    }

    @SuppressWarnings("unchecked")
    private void loadVoitures() {
        try {
            ClassPathResource resource = new ClassPathResource("voiture.json");
            try (InputStream is = resource.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> root = mapper.readValue(is, new TypeReference<Map<String, Object>>() {});
                Object brandsObj = root.get("car_brands");
                if (brandsObj instanceof List<?> list) {
                    brandModels.clear();
                    for (Object o : list) {
                        if (o instanceof Map<?, ?> m) {
                            Object nameObj = m.get("name");
                            Object modelsObj = m.get("models");
                            if (nameObj instanceof String name && modelsObj instanceof List<?> mods) {
                                List<String> models = new ArrayList<>();
                                for (Object mo : mods) {
                                    if (mo instanceof String s) models.add(s);
                                }
                                brandModels.put(name, models);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Keep empty; controller will surface errors if queried
        }
    }

    // Helpers
    private String findKeyIgnoreCase(Set<String> keys, String wanted) {
        if (wanted == null) return null;
        for (String k : keys) {
            if (k.equalsIgnoreCase(wanted)) return k;
        }
        return null;
    }

    // Communes
    public List<String> listCommunes() {
        List<String> communes = new ArrayList<>(communesData.keySet());
        communes.sort(String::compareToIgnoreCase);
        return communes;
    }

    public List<String> listQuartiers(String commune) {
        String realCommune = findKeyIgnoreCase(communesData.keySet(), commune);
        if (realCommune == null) return null;
        Map<String, List<String>> quartiersMap = communesData.get(realCommune);
        List<String> quartiers = new ArrayList<>(quartiersMap.keySet());
        quartiers.sort(String::compareToIgnoreCase);
        return quartiers;
    }

    public List<String> listAvenues(String commune, String quartier) {
        String realCommune = findKeyIgnoreCase(communesData.keySet(), commune);
        if (realCommune == null) return null;
        Map<String, List<String>> quartiersMap = communesData.get(realCommune);
        String realQuartier = findKeyIgnoreCase(quartiersMap.keySet(), quartier);
        if (realQuartier == null) return null;
        List<String> avenues = quartiersMap.get(realQuartier);
        return avenues == null ? Collections.emptyList() : avenues;
    }

    // Voitures
    public List<String> listMarques() {
        List<String> brands = new ArrayList<>(brandModels.keySet());
        brands.sort(String::compareToIgnoreCase);
        return brands;
    }

    public List<String> listModelsByMarque(String marque) {
        String real = findKeyIgnoreCase(brandModels.keySet(), marque);
        if (real == null) return null;
        return brandModels.getOrDefault(real, Collections.emptyList());
    }
}
