package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.entity.enums.Sexe;
import com.DPRIHKAT.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public Page<Utilisateur> getAllUsers(Pageable pageable) {
        return utilisateurRepository.findAll(pageable);
    }

    public Optional<Utilisateur> getUserById(UUID id) {
        return utilisateurRepository.findById(id);
    }

    public Utilisateur createUser(Utilisateur utilisateur) {
        // Validation des champs obligatoires pour les utilisateurs internes
        if (!utilisateur.getRole().equals(Role.CONTRIBUABLE)) {
            if (utilisateur.getNomComplet() == null || utilisateur.getSexe() == null || 
                utilisateur.getGrade() == null || utilisateur.getMatricule() == null ) {
                throw new IllegalArgumentException("Tous les champs (nomComplet, sexe, grade, matricule, adresse, telephone) sont obligatoires pour les utilisateurs internes");
            }
        }
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur updateUser(UUID id, Utilisateur utilisateurDetails) {
        Utilisateur existingUser = utilisateurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        
        // Mise à jour des champs
        existingUser.setNomComplet(utilisateurDetails.getNomComplet());
        existingUser.setSexe(utilisateurDetails.getSexe());
        existingUser.setGrade(utilisateurDetails.getGrade());
        existingUser.setMatricule(utilisateurDetails.getMatricule());
        existingUser.setEmail(utilisateurDetails.getEmail());
        existingUser.setLogin(utilisateurDetails.getLogin());
        existingUser.setRole(utilisateurDetails.getRole());
        
        return utilisateurRepository.save(existingUser);
    }

    public void deleteUser(UUID id) {
        utilisateurRepository.deleteById(id);
    }

    public boolean existsByLogin(String login) {
        return utilisateurRepository.existsByLogin(login);
    }

    public Optional<Utilisateur> findByLogin(String login) {
        return utilisateurRepository.findByLogin(login);
    }

    public Page<Utilisateur> findByRole(Role role, Pageable pageable) {
        return utilisateurRepository.findByRole(role, pageable);
    }
}
