package com.DPRIHKAT.service;

import com.DPRIHKAT.dto.ProprieteCreationDTO;
import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.NatureImpot;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.repository.ContribuableRepository;
import com.DPRIHKAT.repository.NatureImpotRepository;
import com.DPRIHKAT.repository.ProprieteRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service pour gérer les propriétés
 * @author amateur
 */
@Service
public class ProprieteService {

    private static final Logger logger = LoggerFactory.getLogger(ProprieteService.class);

    @Autowired
    private ProprieteRepository proprieteRepository;

    @Autowired
    private ContribuableRepository contribuableRepository;

    @Autowired
    private NatureImpotRepository natureImpotRepository;

    /**
     * Crée une nouvelle propriété avec les natures d'impôt associées
     * @param proprieteDTO DTO contenant les informations de la propriété et les IDs des natures d'impôt
     * @return La propriété créée
     * @throws IllegalArgumentException si le contribuable ou une nature d'impôt n'existe pas
     */
    @Transactional
    public Propriete createPropriete(ProprieteCreationDTO proprieteDTO) {
        logger.info("Création d'une nouvelle propriété pour le contribuable avec ID: {}", proprieteDTO.getProprietaireId());

        // Récupérer le contribuable
        Contribuable proprietaire = contribuableRepository.findById(proprieteDTO.getProprietaireId())
                .orElseThrow(() -> new IllegalArgumentException("Contribuable non trouvé avec ID: " + proprieteDTO.getProprietaireId()));

        // Récupérer les natures d'impôt
        List<NatureImpot> naturesImpot = new ArrayList<>();
        if (proprieteDTO.getNaturesImpotIds() != null && !proprieteDTO.getNaturesImpotIds().isEmpty()) {
            naturesImpot = proprieteDTO.getNaturesImpotIds().stream()
                    .map(id -> natureImpotRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Nature d'impôt non trouvée avec ID: " + id)))
                    .collect(Collectors.toList());
        }

        // Créer le point géographique si les coordonnées sont fournies
        Point location = null;
        if (proprieteDTO.getLatitude() != null && proprieteDTO.getLongitude() != null) {
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
            location = geometryFactory.createPoint(new Coordinate(proprieteDTO.getLongitude(), proprieteDTO.getLatitude()));
        }

        // Créer la propriété
        Propriete propriete = new Propriete();
        propriete.setType(proprieteDTO.getType());
        propriete.setLocalite(proprieteDTO.getLocalite());
        propriete.setRangLocalite(proprieteDTO.getRangLocalite());
        propriete.setSuperficie(proprieteDTO.getSuperficie());
        propriete.setAdresse(proprieteDTO.getAdresse());
        propriete.setLocation(location);
        propriete.setProprietaire(proprietaire);
        propriete.setActif(true);
        propriete.setDeclare(false);
        propriete.setDeclarationEnLigne(false);

        // Associer les natures d'impôt
        propriete.setNaturesImpot(naturesImpot);

        // Sauvegarder la propriété
        Propriete savedPropriete = proprieteRepository.save(propriete);
        logger.info("Propriété créée avec succès avec ID: {}", savedPropriete.getId());

        return savedPropriete;
    }

    /**
     * Récupère toutes les propriétés
     * @return Liste de toutes les propriétés
     */
    public List<Propriete> findAll() {
        return proprieteRepository.findAll();
    }

    /**
     * Récupère une propriété par son ID
     * @param id L'ID de la propriété
     * @return La propriété correspondante, s'il existe
     */
    public Propriete findById(UUID id) {
        return proprieteRepository.findById(id).orElse(null);
    }

    /**
     * Récupère les propriétés d'un contribuable
     * @param contribuableId L'ID du contribuable
     * @return Liste des propriétés du contribuable
     */
    public List<Propriete> findByContribuableId(UUID contribuableId) {
        return proprieteRepository.findByProprietaire_Id(contribuableId);
    }
}
