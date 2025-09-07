package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.NatureImpot;
import com.DPRIHKAT.repository.NatureImpotRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service pour gérer les natures d'impôt
 */
@Service
public class NatureImpotService {

    private static final Logger logger = LoggerFactory.getLogger(NatureImpotService.class);

    @Autowired
    private NatureImpotRepository natureImpotRepository;

    /**
     * Récupère toutes les natures d'impôt actives
     * @return la liste des natures d'impôt actives
     */
    public List<NatureImpot> getAllNaturesImpot() {
        return natureImpotRepository.findByActifTrue();
    }

    /**
     * Récupère une nature d'impôt par son ID
     * @param id l'ID de la nature d'impôt
     * @return la nature d'impôt correspondante, si elle existe
     */
    public Optional<NatureImpot> getNatureImpotById(String id) {
        try {
            return natureImpotRepository.findById(java.util.UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            logger.error("ID de nature d'impôt invalide: {}", id, e);
            return Optional.empty();
        }
    }

    /**
     * Récupère une nature d'impôt par son code
     * @param code le code de la nature d'impôt
     * @return la nature d'impôt correspondante, si elle existe
     */
    public Optional<NatureImpot> getNatureImpotByCode(String code) {
        return natureImpotRepository.findByCode(code);
    }

    /**
     * Crée ou met à jour une nature d'impôt
     * @param natureImpot la nature d'impôt à créer ou mettre à jour
     * @return la nature d'impôt créée ou mise à jour
     */
    @Transactional
    public NatureImpot saveNatureImpot(NatureImpot natureImpot) {
        return natureImpotRepository.save(natureImpot);
    }

    /**
     * Désactive une nature d'impôt (suppression logique)
     * @param id l'ID de la nature d'impôt à désactiver
     * @return true si la nature d'impôt a été désactivée, false sinon
     */
    @Transactional
    public boolean desactiverNatureImpot(String id) {
        try {
            Optional<NatureImpot> natureImpotOpt = natureImpotRepository.findById(java.util.UUID.fromString(id));
            if (natureImpotOpt.isPresent()) {
                NatureImpot natureImpot = natureImpotOpt.get();
                natureImpot.setActif(false);
                natureImpotRepository.save(natureImpot);
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            logger.error("ID de nature d'impôt invalide: {}", id, e);
            return false;
        }
    }

    /**
     * Charge les natures d'impôt depuis le fichier impots.json
     * @return la liste des natures d'impôt chargées
     */
    @Transactional
    public List<NatureImpot> chargerNaturesImpotDepuisFichier() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("impots.json").getInputStream();
            Map<String, List<Map<String, String>>> data = mapper.readValue(inputStream, new TypeReference<>() {});
            
            List<NatureImpot> naturesImpot = new ArrayList<>();
            List<Map<String, String>> impots = data.get("impots");
            
            for (Map<String, String> impot : impots) {
                String code = impot.get("code");
                String nom = impot.get("nom");
                String description = impot.get("description");
                
                Optional<NatureImpot> natureImpotExistante = natureImpotRepository.findByCode(code);
                if (natureImpotExistante.isPresent()) {
                    NatureImpot natureImpot = natureImpotExistante.get();
                    natureImpot.setNom(nom);
                    natureImpot.setDescription(description);
                    natureImpot.setActif(true);
                    naturesImpot.add(natureImpotRepository.save(natureImpot));
                } else {
                    NatureImpot nouvelleNatureImpot = new NatureImpot(code, nom, description);
                    naturesImpot.add(natureImpotRepository.save(nouvelleNatureImpot));
                }
            }
            
            logger.info("Chargement de {} natures d'impôt depuis le fichier impots.json", naturesImpot.size());
            return naturesImpot;
        } catch (IOException e) {
            logger.error("Erreur lors du chargement des natures d'impôt depuis le fichier impots.json", e);
            throw new RuntimeException("Erreur lors du chargement des natures d'impôt", e);
        }
    }
}
