package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.NatureImpot;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.ProprieteImpot;
import com.DPRIHKAT.repository.NatureImpotRepository;
import com.DPRIHKAT.repository.ProprieteImpotRepository;
import com.DPRIHKAT.repository.ProprieteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour gérer les liens entre propriétés et impôts
 * @author amateur
 */
@Service
public class ProprieteImpotService {

    private static final Logger logger = LoggerFactory.getLogger(ProprieteImpotService.class);

    @Autowired
    private ProprieteImpotRepository proprieteImpotRepository;

    @Autowired
    private ProprieteRepository proprieteRepository;

    @Autowired
    private NatureImpotRepository natureImpotRepository;

    /**
     * Récupère tous les liens entre propriétés et impôts
     * @return Liste de tous les liens entre propriétés et impôts
     */
    public List<ProprieteImpot> getAllProprietesImpots() {
        return proprieteImpotRepository.findAll();
    }

    /**
     * Récupère un lien entre propriété et impôt par son ID
     * @param id L'ID du lien
     * @return Le lien correspondant, s'il existe
     */
    public Optional<ProprieteImpot> getProprieteImpotById(UUID id) {
        return proprieteImpotRepository.findById(id);
    }

    /**
     * Récupère tous les liens pour une propriété donnée
     * @param proprieteId L'ID de la propriété
     * @return Liste des liens pour cette propriété
     */
    public List<ProprieteImpot> getProprieteImpotsByProprieteId(UUID proprieteId) {
        return proprieteRepository.findById(proprieteId)
                .map(propriete -> proprieteImpotRepository.findByProprieteAndActifTrue(propriete))
                .orElse(List.of());
    }

    /**
     * Récupère tous les liens pour une nature d'impôt donnée
     * @param natureImpotId L'ID de la nature d'impôt
     * @return Liste des liens pour cette nature d'impôt
     */
    public List<ProprieteImpot> getProprieteImpotsByNatureImpotId(UUID natureImpotId) {
        return natureImpotRepository.findById(natureImpotId)
                .map(natureImpot -> proprieteImpotRepository.findByNatureImpotAndActifTrue(natureImpot))
                .orElse(List.of());
    }

    /**
     * Crée un nouveau lien entre une propriété et une nature d'impôt
     * @param proprieteId L'ID de la propriété
     * @param natureImpotId L'ID de la nature d'impôt
     * @param tauxImposition Le taux d'imposition (optionnel)
     * @param commentaire Un commentaire (optionnel)
     * @return Le lien créé, s'il a pu être créé
     */
    public Optional<ProprieteImpot> createProprieteImpot(UUID proprieteId, UUID natureImpotId, Double tauxImposition, String commentaire) {
        Optional<Propriete> proprieteOpt = proprieteRepository.findById(proprieteId);
        Optional<NatureImpot> natureImpotOpt = natureImpotRepository.findById(natureImpotId);

        if (proprieteOpt.isPresent() && natureImpotOpt.isPresent()) {
            Propriete propriete = proprieteOpt.get();
            NatureImpot natureImpot = natureImpotOpt.get();

            // Vérifier si un lien actif existe déjà
            if (proprieteImpotRepository.existsByProprieteAndNatureImpotAndActifTrue(propriete, natureImpot)) {
                logger.warn("Un lien actif existe déjà entre la propriété {} et la nature d'impôt {}", proprieteId, natureImpotId);
                return Optional.empty();
            }

            ProprieteImpot proprieteImpot = new ProprieteImpot(propriete, natureImpot);
            proprieteImpot.setTauxImposition(tauxImposition);
            proprieteImpot.setCommentaire(commentaire);
            proprieteImpot.setDateCreation(new Date());
            proprieteImpot.setDateModification(new Date());
            proprieteImpot.setActif(true);

            return Optional.of(proprieteImpotRepository.save(proprieteImpot));
        }

        return Optional.empty();
    }

    /**
     * Met à jour un lien existant entre une propriété et une nature d'impôt
     * @param id L'ID du lien à mettre à jour
     * @param tauxImposition Le nouveau taux d'imposition (optionnel)
     * @param commentaire Le nouveau commentaire (optionnel)
     * @return Le lien mis à jour, s'il existe
     */
    public Optional<ProprieteImpot> updateProprieteImpot(UUID id, Double tauxImposition, String commentaire) {
        return proprieteImpotRepository.findById(id)
                .map(proprieteImpot -> {
                    if (tauxImposition != null) {
                        proprieteImpot.setTauxImposition(tauxImposition);
                    }
                    if (commentaire != null) {
                        proprieteImpot.setCommentaire(commentaire);
                    }
                    proprieteImpot.setDateModification(new Date());
                    return proprieteImpotRepository.save(proprieteImpot);
                });
    }

    /**
     * Désactive un lien entre une propriété et une nature d'impôt (suppression logique)
     * @param id L'ID du lien à désactiver
     * @return true si le lien a été désactivé, false sinon
     */
    public boolean deactivateProprieteImpot(UUID id) {
        return proprieteImpotRepository.findById(id)
                .map(proprieteImpot -> {
                    proprieteImpot.setActif(false);
                    proprieteImpot.setDateModification(new Date());
                    proprieteImpotRepository.save(proprieteImpot);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Active un lien entre une propriété et une nature d'impôt
     * @param id L'ID du lien à activer
     * @return true si le lien a été activé, false sinon
     */
    public boolean activateProprieteImpot(UUID id) {
        return proprieteImpotRepository.findById(id)
                .map(proprieteImpot -> {
                    proprieteImpot.setActif(true);
                    proprieteImpot.setDateModification(new Date());
                    proprieteImpotRepository.save(proprieteImpot);
                    return true;
                })
                .orElse(false);
    }
}
