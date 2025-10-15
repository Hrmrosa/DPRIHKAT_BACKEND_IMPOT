package com.DPRIHKAT.service;

import com.DPRIHKAT.dto.ContribuableDetailsDTO;
import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.repository.ContribuableRepository;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContribuableService {

    private static final Logger logger = LoggerFactory.getLogger(ContribuableService.class);
    private final ContribuableRepository contribuableRepository;
    private final ProprieteRepository proprieteRepository;
    private final VehiculeRepository vehiculeRepository;

    public ContribuableService(ContribuableRepository contribuableRepository,
                             ProprieteRepository proprieteRepository,
                             VehiculeRepository vehiculeRepository) {
        this.contribuableRepository = contribuableRepository;
        this.proprieteRepository = proprieteRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    /**
     * Récupère un contribuable par son ID
     * @param id ID du contribuable
     * @return Contribuable trouvé ou null si non trouvé
     */
    public Contribuable findById(UUID id) {
        return contribuableRepository.findById(id).orElse(null);
    }

    /**
     * Récupère tous les contribuables
     * @return Liste de tous les contribuables
     */
    public Map<String, Object> getAllContribuables() {
        logger.info("Récupération de tous les contribuables");
        Map<String, Object> response = new HashMap<>();
        
        List<Contribuable> contribuables = contribuableRepository.findAll();
        logger.debug("Nombre de contribuables trouvés: {}", contribuables.size());
        
        response.put("success", true);
        response.put("data", Map.of("contribuables", contribuables));
        
        return response;
    }

    /**
     * Récupère un contribuable avec tous ses biens (propriétés et véhicules)
     * @param contribuableId ID du contribuable
     * @return Détails complets du contribuable
     */
    public Map<String, Object> getContribuableComplet(UUID contribuableId) {
        logger.info("Récupération des détails complets du contribuable avec ID: {}", contribuableId);
        Map<String, Object> response = new HashMap<>();

        // Récupérer le contribuable de base
        Optional<Contribuable> contribuableOpt = contribuableRepository.findById(contribuableId);
        if (contribuableOpt.isEmpty()) {
            logger.error("Contribuable non trouvé avec ID: {}", contribuableId);
            throw new RuntimeException("Contribuable non trouvé");
        }

        Contribuable contribuable = contribuableOpt.get();

        // Créer le DTO pour la réponse
        ContribuableDetailsDTO detailsDTO = new ContribuableDetailsDTO();
        detailsDTO.setId(contribuable.getId());
        detailsDTO.setNom(contribuable.getNom());

        // Récupérer les propriétés et les transformer en DTO
        List<Propriete> proprietes = proprieteRepository.findByProprietaire_Id(contribuableId);
        List<ContribuableDetailsDTO.ProprieteDTO> proprieteDTOs = proprietes.stream()
                .map(p -> new ContribuableDetailsDTO.ProprieteDTO(
                        p.getId(),
                        p.getAdresse(),
                        p.getMontantImpot() != null ? p.getMontantImpot() : 0.0
                ))
                .collect(Collectors.toList());
        detailsDTO.setProprietes(proprieteDTOs);

        // Récupérer les véhicules (à la fois comme propriétaire et comme contribuable)
        List<Vehicule> vehiculesProprietaire = vehiculeRepository.findByProprietaireId(contribuableId);
        List<Vehicule> vehiculesContribuable = vehiculeRepository.findByContribuableId(contribuableId);

        // Fusionner les deux listes sans doublons
        Set<Vehicule> allVehicules = new HashSet<>();
        allVehicules.addAll(vehiculesProprietaire);
        allVehicules.addAll(vehiculesContribuable);

        // Transformer les véhicules en DTO
        List<ContribuableDetailsDTO.VehiculeDTO> vehiculeDTOs = allVehicules.stream()
                .map(v -> new ContribuableDetailsDTO.VehiculeDTO(
                        v.getId(),
                        v.getImmatriculation(),
                        v.getMarque(),
                        v.getModele()
                ))
                .collect(Collectors.toList());
        detailsDTO.setVehicules(vehiculeDTOs);

        // Construire la réponse finale
        response.put("success", true);
        response.put("data", detailsDTO);

        return response;
    }

    /**
     * Récupère les détails d'un contribuable (version simplifiée)
     * @param contribuableId ID du contribuable
     * @return Détails du contribuable
     */
    public Map<String, Object> getContribuableDetails(UUID contribuableId) {
        logger.info("Récupération des détails du contribuable avec ID: {}", contribuableId);
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        
        // Récupérer le contribuable de base
        Optional<Contribuable> contribuableOpt = contribuableRepository.findById(contribuableId);
        if (contribuableOpt.isEmpty()) {
            logger.error("Contribuable non trouvé avec ID: {}", contribuableId);
            throw new RuntimeException("Contribuable non trouvé");
        }
        
        Contribuable contribuable = contribuableOpt.get();
        
        // Ajouter les informations de base du contribuable
        data.put("contribuable", contribuable);
        
        // Construire la réponse finale
        response.put("success", true);
        response.put("data", data);
        
        return response;
    }

    /**
     * Crée un nouveau contribuable
     * @param contribuable Données du contribuable à créer
     * @return Contribuable créé
     */
    @Transactional
    public Map<String, Object> createContribuable(Contribuable contribuable) {
        logger.info("Création d'un nouveau contribuable: {}", contribuable.getNom());
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        
        // Validation des données
        if (contribuable.getNom() == null || contribuable.getNom().isEmpty()) {
            logger.error("Nom du contribuable manquant");
            throw new IllegalArgumentException("Le nom du contribuable est obligatoire");
        }
        
        // Enregistrement du contribuable
        contribuable.setActif(true);
        Contribuable nouveauContribuable = contribuableRepository.save(contribuable);
        logger.info("Contribuable créé avec ID: {}", nouveauContribuable.getId());
        
        // Construire la réponse
        data.put("contribuable", nouveauContribuable);
        response.put("success", true);
        response.put("data", data);
        
        return response;
    }

    /**
     * Met à jour un contribuable existant
     * @param id ID du contribuable à mettre à jour
     * @param contribuableDetails Nouvelles données du contribuable
     * @return Contribuable mis à jour
     */
    @Transactional
    public Map<String, Object> updateContribuable(UUID id, Contribuable contribuableDetails) {
        logger.info("Mise à jour du contribuable avec ID: {}", id);
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        
        // Vérifier si le contribuable existe
        Optional<Contribuable> contribuableOpt = contribuableRepository.findById(id);
        if (contribuableOpt.isEmpty()) {
            logger.error("Contribuable non trouvé avec ID: {}", id);
            throw new RuntimeException("Contribuable non trouvé");
        }
        
        Contribuable contribuable = contribuableOpt.get();
        
        // Mettre à jour les champs modifiables
        if (contribuableDetails.getNom() != null) {
            contribuable.setNom(contribuableDetails.getNom());
        }
        if (contribuableDetails.getSexe() != null) {
            contribuable.setSexe(contribuableDetails.getSexe());
        }
        if (contribuableDetails.getMatricule() != null) {
            contribuable.setMatricule(contribuableDetails.getMatricule());
        }
        if (contribuableDetails.getAdressePrincipale() != null) {
            contribuable.setAdressePrincipale(contribuableDetails.getAdressePrincipale());
        }
        if (contribuableDetails.getAdresseSecondaire() != null) {
            contribuable.setAdresseSecondaire(contribuableDetails.getAdresseSecondaire());
        }
        if (contribuableDetails.getTelephonePrincipal() != null) {
            contribuable.setTelephonePrincipal(contribuableDetails.getTelephonePrincipal());
        }
        if (contribuableDetails.getTelephoneSecondaire() != null) {
            contribuable.setTelephoneSecondaire(contribuableDetails.getTelephoneSecondaire());
        }
        if (contribuableDetails.getEmail() != null) {
            contribuable.setEmail(contribuableDetails.getEmail());
        }
        if (contribuableDetails.getNationalite() != null) {
            contribuable.setNationalite(contribuableDetails.getNationalite());
        }
        if (contribuableDetails.getType() != null) {
            contribuable.setType(contribuableDetails.getType());
        }
        if (contribuableDetails.getIdNat() != null) {
            contribuable.setIdNat(contribuableDetails.getIdNat());
        }
        if (contribuableDetails.getNRC() != null) {
            contribuable.setNRC(contribuableDetails.getNRC());
        }
        if (contribuableDetails.getSigle() != null) {
            contribuable.setSigle(contribuableDetails.getSigle());
        }
        if (contribuableDetails.getNumeroIdentificationContribuable() != null) {
            contribuable.setNumeroIdentificationContribuable(contribuableDetails.getNumeroIdentificationContribuable());
        }
        
        // Enregistrer les modifications
        Contribuable contribuableMisAJour = contribuableRepository.save(contribuable);
        logger.info("Contribuable mis à jour avec succès: {}", contribuableMisAJour.getId());
        
        // Construire la réponse
        data.put("contribuable", contribuableMisAJour);
        response.put("success", true);
        response.put("data", data);
        
        return response;
    }

    /**
     * Désactive un contribuable (suppression logique)
     * @param id ID du contribuable à désactiver
     * @return Message de confirmation
     */
    @Transactional
    public Map<String, Object> deactivateContribuable(UUID id) {
        logger.info("Désactivation du contribuable avec ID: {}", id);
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        
        // Vérifier si le contribuable existe
        Optional<Contribuable> contribuableOpt = contribuableRepository.findById(id);
        if (contribuableOpt.isEmpty()) {
            logger.error("Contribuable non trouvé avec ID: {}", id);
            throw new RuntimeException("Contribuable non trouvé");
        }
        
        Contribuable contribuable = contribuableOpt.get();
        
        // Désactiver le contribuable
        contribuable.setActif(false);
        Contribuable contribuableDesactive = contribuableRepository.save(contribuable);
        logger.info("Contribuable désactivé avec succès: {}", contribuableDesactive.getId());
        
        // Construire la réponse
        data.put("message", "Contribuable désactivé avec succès");
        data.put("contribuable", contribuableDesactive);
        response.put("success", true);
        response.put("data", data);
        
        return response;
    }
}
