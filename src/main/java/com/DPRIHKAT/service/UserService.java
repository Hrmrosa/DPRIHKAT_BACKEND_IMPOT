package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.Role;
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

    public Utilisateur saveUser(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
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
