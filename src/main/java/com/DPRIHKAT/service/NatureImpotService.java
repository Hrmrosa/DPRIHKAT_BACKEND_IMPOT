package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.NatureImpot;
import com.DPRIHKAT.repository.NatureImpotRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour gérer les natures d'impôt
 * @author amateur
 */
@Service
public class NatureImpotService {

    private static final Logger logger = LoggerFactory.getLogger(NatureImpotService.class);

    @Autowired
    private NatureImpotRepository natureImpotRepository;

    /**
     * Initialise les natures d'impôt à partir du fichier impots.json
     */
    @PostConstruct
    public void init() {
        try {
            loadNaturesImpotFromJson();
        } catch (Exception e) {
            logger.error("Erreur lors de l'initialisation des natures d'impôt", e);
        }
    }

    /**
     * Charge les natures d'impôt à partir du fichier impots.json
     */
    public void loadNaturesImpotFromJson() {
        try {
            ClassPathResource resource = new ClassPathResource("impots.json");
            InputStream inputStream = resource.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(inputStream);
            JsonNode impotsNode = rootNode.path("impots");

            if (impotsNode.isArray()) {
                for (JsonNode impotNode : impotsNode) {
                    String code = impotNode.path("code").asText();
                    String nom = impotNode.path("nom").asText();
                    String description = impotNode.path("description").asText();

                    // Vérifier si la nature d'impôt existe déjà
                    if (!natureImpotRepository.existsByCode(code)) {
                        NatureImpot natureImpot = new NatureImpot(code, nom, description);
                        natureImpotRepository.save(natureImpot);
                        logger.info("Nature d'impôt créée: {}", code);
                    } else {
                        logger.info("Nature d'impôt déjà existante: {}", code);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Erreur lors du chargement des natures d'impôt depuis impots.json", e);
        }
    }

    /**
     * Récupère toutes les natures d'impôt
     * @return Liste de toutes les natures d'impôt
     */
    public List<NatureImpot> getAllNaturesImpot() {
        return natureImpotRepository.findAll();
    }

    /**
     * Récupère toutes les natures d'impôt actives
     * @return Liste des natures d'impôt actives
     */
    public List<NatureImpot> getAllActiveNaturesImpot() {
        return natureImpotRepository.findByActifTrue();
    }

    /**
     * Récupère une nature d'impôt par son ID
     * @param id L'ID de la nature d'impôt
     * @return La nature d'impôt correspondante, si elle existe
     */
    public Optional<NatureImpot> getNatureImpotById(UUID id) {
        return natureImpotRepository.findById(id);
    }

    /**
     * Récupère une nature d'impôt par son code
     * @param code Le code de la nature d'impôt
     * @return La nature d'impôt correspondante, si elle existe
     */
    public Optional<NatureImpot> getNatureImpotByCode(String code) {
        return natureImpotRepository.findByCode(code);
    }

    /**
     * Crée une nouvelle nature d'impôt
     * @param natureImpot La nature d'impôt à créer
     * @return La nature d'impôt créée
     */
    public NatureImpot createNatureImpot(NatureImpot natureImpot) {
        return natureImpotRepository.save(natureImpot);
    }

    /**
     * Met à jour une nature d'impôt existante
     * @param id L'ID de la nature d'impôt à mettre à jour
     * @param natureImpotDetails Les nouvelles informations de la nature d'impôt
     * @return La nature d'impôt mise à jour, si elle existe
     */
    public Optional<NatureImpot> updateNatureImpot(UUID id, NatureImpot natureImpotDetails) {
        return natureImpotRepository.findById(id)
                .map(natureImpot -> {
                    natureImpot.setNom(natureImpotDetails.getNom());
                    natureImpot.setDescription(natureImpotDetails.getDescription());
                    return natureImpotRepository.save(natureImpot);
                });
    }

    /**
     * Désactive une nature d'impôt (suppression logique)
     * @param id L'ID de la nature d'impôt à désactiver
     * @return true si la nature d'impôt a été désactivée, false sinon
     */
    public boolean deactivateNatureImpot(UUID id) {
        return natureImpotRepository.findById(id)
                .map(natureImpot -> {
                    natureImpot.setActif(false);
                    natureImpotRepository.save(natureImpot);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Active une nature d'impôt
     * @param id L'ID de la nature d'impôt à activer
     * @return true si la nature d'impôt a été activée, false sinon
     */
    public boolean activateNatureImpot(UUID id) {
        return natureImpotRepository.findById(id)
                .map(natureImpot -> {
                    natureImpot.setActif(true);
                    natureImpotRepository.save(natureImpot);
                    return true;
                })
                .orElse(false);
    }
}
