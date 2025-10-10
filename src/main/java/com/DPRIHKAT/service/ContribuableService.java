package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.repository.ContribuableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ContribuableService {

    private final ContribuableRepository contribuableRepository;

    public ContribuableService(ContribuableRepository contribuableRepository) {
        this.contribuableRepository = contribuableRepository;
    }

    public List<Contribuable> getAllContribuables() {
        return contribuableRepository.findAll();
    }

    public Contribuable getContribuableById(UUID id) {
        return contribuableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contribuable non trouvé"));
    }

    public Contribuable createContribuable(Contribuable contribuable) {
        return contribuableRepository.save(contribuable);
    }

    public Contribuable updateContribuable(UUID id, Contribuable contribuable) {
        Contribuable existing = getContribuableById(id);
        // Mettre à jour les champs ici
        existing.setNom(contribuable.getNom());
        existing.setSexe(contribuable.getSexe());
        existing.setMatricule(contribuable.getMatricule());
        existing.setAdressePrincipale(contribuable.getAdressePrincipale());
        existing.setAdresseSecondaire(contribuable.getAdresseSecondaire());
        existing.setTelephonePrincipal(contribuable.getTelephonePrincipal());
        existing.setTelephoneSecondaire(contribuable.getTelephoneSecondaire());
        existing.setEmail(contribuable.getEmail());
        existing.setNationalite(contribuable.getNationalite());
        existing.setType(contribuable.getType());
        existing.setIdNat(contribuable.getIdNat());
        existing.setNRC(contribuable.getNRC());
        existing.setSigle(contribuable.getSigle());
        existing.setNumeroIdentificationContribuable(contribuable.getNumeroIdentificationContribuable());
        
        return contribuableRepository.save(existing);
    }

    public Map<String, Object> getContribuableDetails(UUID id) {
        Contribuable contribuable = getContribuableById(id);
        // Implémenter la récupération des biens et véhicules ici
        return Map.of(
            "contribuable", contribuable,
            "proprietes", List.of(), // À remplacer par la vraie implémentation
            "vehicules", List.of()   // À remplacer par la vraie implémentation
        );
    }

    public Contribuable deactivateContribuable(UUID id) {
        Contribuable contribuable = getContribuableById(id);
        contribuable.setActif(false);
        return contribuableRepository.save(contribuable);
    }
}
