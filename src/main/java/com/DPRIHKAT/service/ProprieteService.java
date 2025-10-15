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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProprieteService {

    private final ProprieteRepository proprieteRepository;
    private final ContribuableRepository contribuableRepository;
    private final NatureImpotRepository natureImpotRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    public ProprieteService(
            ProprieteRepository proprieteRepository,
            ContribuableRepository contribuableRepository,
            NatureImpotRepository natureImpotRepository) {
        this.proprieteRepository = proprieteRepository;
        this.contribuableRepository = contribuableRepository;
        this.natureImpotRepository = natureImpotRepository;
    }

    public List<Propriete> getProprietesByContribuable(UUID contribuableId) {
        return proprieteRepository.findByProprietaire_Id(contribuableId);
    }

    /**
     * Crée une nouvelle propriété à partir des données du DTO
     * @param dto Les données de la propriété à créer
     * @return La propriété créée
     * @throws IllegalArgumentException Si les données sont invalides ou si le propriétaire n'existe pas
     */
    @Transactional
    public Propriete createPropriete(ProprieteCreationDTO dto) {
        // Vérifier et récupérer le propriétaire
        Contribuable proprietaire = contribuableRepository.findById(dto.getProprietaireId())
                .orElseThrow(() -> new IllegalArgumentException("Propriétaire non trouvé avec l'ID: " + dto.getProprietaireId()));

        // Créer un point géométrique si les coordonnées sont fournies
        Point location = null;
        if (dto.getLatitude() != null && dto.getLongitude() != null) {
            location = geometryFactory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));
        }

        // Créer la propriété
        Propriete propriete = new Propriete();
        propriete.setType(dto.getType());
        propriete.setLocalite(dto.getLocalite());
        propriete.setRangLocalite(dto.getRangLocalite());
        propriete.setSuperficie(dto.getSuperficie());
        propriete.setAdresse(dto.getAdresse());
        propriete.setLocation(location);
        propriete.setProprietaire(proprietaire);
        propriete.setActif(true);
        propriete.setDeclare(false);
        propriete.setDeclarationEnLigne(false);

        // Ajouter les natures d'impôt si spécifiées
        if (dto.getNaturesImpotIds() != null && !dto.getNaturesImpotIds().isEmpty()) {
            List<NatureImpot> naturesImpot = new ArrayList<>();
            for (UUID natureImpotId : dto.getNaturesImpotIds()) {
                Optional<NatureImpot> natureImpot = natureImpotRepository.findById(natureImpotId);
                natureImpot.ifPresent(naturesImpot::add);
            }
            propriete.setNaturesImpot(naturesImpot);
        }

        // Calculer le montant de l'impôt
        propriete.calculerImpôt();

        // Sauvegarder et retourner la propriété
        return proprieteRepository.save(propriete);
    }

    // Autres méthodes existantes...
}
