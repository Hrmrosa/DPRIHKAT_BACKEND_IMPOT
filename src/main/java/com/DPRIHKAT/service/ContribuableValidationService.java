package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.repository.ContribuableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ContribuableValidationService {

    private final ContribuableRepository contribuableRepository;

    public ContribuableValidationService(ContribuableRepository contribuableRepository) {
        this.contribuableRepository = contribuableRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Contribuable> findExistingContribuable(Contribuable contribuable) {
        // Vérifier par téléphone principal
        if (contribuable.getTelephonePrincipal() != null && !contribuable.getTelephonePrincipal().isEmpty()) {
            Optional<Contribuable> byPhone = contribuableRepository.findByTelephonePrincipal(contribuable.getTelephonePrincipal());
            if (byPhone.isPresent()) {
                return byPhone;
            }
        }

        // Vérifier par email
        if (contribuable.getEmail() != null && !contribuable.getEmail().isEmpty()) {
            Optional<Contribuable> byEmail = contribuableRepository.findByEmail(contribuable.getEmail());
            if (byEmail.isPresent()) {
                return byEmail;
            }
        }

        // Vérifier par numéro d'identification
        if (contribuable.getNumeroIdentificationContribuable() != null && !contribuable.getNumeroIdentificationContribuable().isEmpty()) {
            Optional<Contribuable> byNumero = contribuableRepository.findByNumeroIdentificationContribuable(
                contribuable.getNumeroIdentificationContribuable());
            if (byNumero.isPresent()) {
                return byNumero;
            }
        }

        // Vérification stricte par nom (si demandé)
        if (contribuable.getNom() != null && !contribuable.getNom().isEmpty()) {
            Optional<Contribuable> byNom = contribuableRepository.findByNomIgnoreCase(contribuable.getNom());
            if (byNom.isPresent()) {
                return byNom;
            }
        }

        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public boolean isVehiculeExistant(String immatriculation) {
        return contribuableRepository.existsByVehiculesImmatriculation(immatriculation);
    }
}
